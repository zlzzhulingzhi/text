package cn.qbs.wa.teach.question.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.EnableRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.organization.api.RemoteEmployeeService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.employee.EmployeePageResultDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.employee.EmployeePageSearchDTO;
import cn.qbs.wa.teach.question.constant.QuestionConst;
import cn.qbs.wa.teach.question.elasticsearch.SearchPageResult;
import cn.qbs.wa.teach.question.entity.*;
import cn.qbs.wa.teach.question.enumerate.CategoryGroupEnum;
import cn.qbs.wa.teach.question.enumerate.QuestionTypeEnum;
import cn.qbs.wa.teach.question.mapper.PaperMapper;
import cn.qbs.wa.teach.question.mapper.QuestionMapper;
import cn.qbs.wa.teach.question.pojo.category.SimpleCategoryDTO;
import cn.qbs.wa.teach.question.pojo.paper.*;
import cn.qbs.wa.teach.question.pojo.question.QuestionDetailResponse;
import cn.qbs.wa.teach.question.pojo.question.QuestionGroupResponse;
import cn.qbs.wa.teach.question.pojo.question.search.QuestionSearchResult;
import cn.qbs.wa.teach.question.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 试卷(Paper)表服务实现类
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-18 20:48:48
 */
@Slf4j
@Service("paperService")
@RequiredArgsConstructor
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements PaperService {

    /**
     * 试卷最大题数
     */
    @Value("${paperMaxQuestionCount:200}")
    private Integer paperMaxQuestionCount;

    private final QuestionService questionService;

    private final QuestionTypeService questionTypeService;

    private final PaperQuestionTypeService paperQuestionTypeService;

    private final PaperQuestionService paperQuestionService;

    private final CategoryService categoryService;

    private final PaperCategoryService paperCategoryService;

    private final ElasticSearchPaperService paperSearchService;

    private final MqProducerService mqProducerService;

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private RemoteEmployeeService remoteEmployeeService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(PaperAddRequest params) {
        List<Long> categoryIdList = params.getCategoryIdList();
        if (CollectionUtils.isEmpty(categoryIdList)) {
            throw new IllegalParamsException("分类不能为空！");
        }

        Paper paper = new Paper();
        BeanUtils.copyProperties(params, paper);
        this.save(paper);
        Long paperId = paper.getId();

        // 保存试卷和分类关联关系
        List<PaperCategory> paperCategoryList = new ArrayList<>();
        categoryIdList = categoryIdList.stream().distinct().collect(Collectors.toList());
        for (Long categoryId : categoryIdList) {
            Category category = categoryService.getById(categoryId);
            if (category == null || category.getGroupId() != CategoryGroupEnum.PAPER.getId()) {
                throw new IllegalParamsException(String.format("分类[id: %s]不存在", categoryId));
            }
            PaperCategory paperCategory = new PaperCategory();
            paperCategory.setCategoryId(categoryId);
            paperCategory.setPaperId(paperId);
            paperCategoryList.add(paperCategory);
        }
        this.paperCategoryService.saveBatch(paperCategoryList);

        saveOther(params, paperId);

        mqProducerService.sendPaperUpdateMsg(paperId, SecurityContextHolder.getOrgId());
//        questionBasketService.empty();
        return paperId;
    }

    private void saveOther(PaperAddRequest params, Long paperId) {
        List<PaperAddRequest.QuestionType> questionTypeList = params.getQuestionTypeList();
        List<Long> questinIdList = new ArrayList<>();
        for (PaperAddRequest.QuestionType questionType : questionTypeList) {
            for (PaperAddRequest.QuestionType.Question question : questionType.getQuestionList()) {
                if (question != null && question.getId() != null) {
                    questinIdList.add(question.getId());
                }
            }
        }
        if ((questinIdList.size()) > paperMaxQuestionCount) {
            throw new ServiceException("试题不能超过" + paperMaxQuestionCount + "道！");
        }

        List<PaperQuestionType> paperQuestionTypeList = new ArrayList<>();
        List<PaperQuestion> paperQuestionList = new ArrayList<>();

        AtomicInteger questionTypeIndex = new AtomicInteger(0);
        AtomicInteger questionGlobalIndex = new AtomicInteger(0);

        for (PaperAddRequest.QuestionType qt : questionTypeList) {
            Long questionTypeId = qt.getId();
            QuestionType questionType = questionTypeService.getByCache(questionTypeId);
            if (questionType == null) {
                throw new IllegalParamsException(String.format("题型[id: %s]不存在", questionTypeId));
            }

            paperQuestionTypeList.add(new PaperQuestionType(paperId, questionTypeId, questionTypeIndex.incrementAndGet()));

            AtomicInteger questionLocalIndex = new AtomicInteger(0);
            for (PaperAddRequest.QuestionType.Question q : qt.getQuestionList()) {
                Long questionId = q.getId();
                Question question = this.questionService.getById(questionId);
                if (question == null) {
                    throw new IllegalParamsException(String.format("试题[id: %s]不存在", questionId));
                }

                PaperQuestion paperQuestion = new PaperQuestion();
                paperQuestion.setPaperId(paperId);
                paperQuestion.setQuestionId(questionId);
                paperQuestion.setScore(q.getScore() != null ? q.getScore() : question.getScore());
                // 目前还没有父子题
                paperQuestion.setParentId(questionId);
                paperQuestion.setLocalOrder(questionLocalIndex.incrementAndGet());
                paperQuestion.setGlobalOrder(questionGlobalIndex.incrementAndGet());
                paperQuestionList.add(paperQuestion);
            }
        }

        this.paperQuestionTypeService.saveBatch(paperQuestionTypeList);
        this.paperQuestionService.saveBatch(paperQuestionList);

        BigDecimal totalScore = paperQuestionList.stream().map(PaperQuestion::getScore).reduce(BigDecimal.ZERO, BigDecimal::add);
        this.lambdaUpdate().eq(Paper::getId, paperId).set(Paper::getQuestionCount, paperQuestionList.size()).set(Paper::getTotalScore, totalScore).update();

        // 将试题置为不可编辑
        this.questionService.lambdaUpdate().set(Question::getEditable, false).in(Question::getId, questinIdList).update();
        for (Long questionId : questinIdList) {
            mqProducerService.sendQuestionUpdateMsg(questionId, SecurityContextHolder.getOrgId());
        }
    }

    @Override
    public IPage<PaperPageResponse> page(PaperPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public PaperDetailResponse detail(Long id) {
        PaperDetailResponse detail = baseMapper.selectDetailById(id);
        if (detail != null) {
            List<PaperQuestion> paperQuestionList = this.paperQuestionService.lambdaQuery()
                    .select(PaperQuestion::getQuestionId, PaperQuestion::getScore, PaperQuestion::getLocalOrder)
                    .eq(PaperQuestion::getPaperId, id)
                    .list();

            List<Long> questionIdList = paperQuestionList.stream().map(i -> i.getQuestionId()).collect(Collectors.toList());
            List<QuestionDetailResponse> questionDetailResponseList = this.questionMapper.selectDetailsByIdList(questionIdList);
            List<QuestionSearchResult> questionList = BeanUtil.copyToList(questionDetailResponseList, QuestionSearchResult.class);
            this.questionService.fillOtherField(questionList);

            Map<Long, PaperQuestion> questionMap = paperQuestionList.stream().collect(Collectors.toMap(PaperQuestion::getQuestionId, Function.identity()));
            for (QuestionSearchResult questionSearchResult : questionList) {
                Long questionId = questionSearchResult.getId();
                PaperQuestion paperQuestion = questionMap.get(questionId);
                if (paperQuestion != null) {
                    questionSearchResult.setScore(paperQuestion.getScore());
                    questionSearchResult.setLocalOrder(paperQuestion.getLocalOrder());
                }
            }

            for (QuestionGroupResponse group : detail.getQuestionTypeList()) {
                List<QuestionSearchResult> list = questionList.stream()
                        .filter(q -> q.getQuestionTypeId().equals(group.getQuestionTypeId()))
                        .collect(Collectors.toList());

                if (!CollectionUtils.isEmpty(list)) {
                    list = list.stream().sorted(Comparator.comparing(QuestionSearchResult::getLocalOrder, Comparator.nullsLast(Comparator.naturalOrder()))).collect(Collectors.toList());
                    group.setQuestionList(list);
                }

                if (group.getQuestionTypeId().equals(QuestionTypeEnum.TIAN_KONG_TI.getId())) {
                    group.getQuestionList().parallelStream().forEach(q -> {
                        Integer answerCount = 1;
                        if (StringUtils.isNotBlank(q.getAnswer())) {
                            answerCount = q.getAnswer().split("\\" + QuestionConst.TIAN_KONG_TI_ANSWER_SEPARATOR).length;
                        }
                        q.setAnswerCount(answerCount);
                    });
                }
            }
        }
        return detail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(PaperUpdateRequest params) {
        Long paperId = params.getId();
        if (paperId == null) {
            throw new IllegalParamsException("ID不能为空！");
        }

        Paper paper = this.getById(paperId);
        if (paper == null) {
            throw new IllegalParamsException(String.format("试卷[id: %s]不存在", paperId));
        }

        if (paper.getEditable() != null && !paper.getEditable()) {
            throw new ServiceException("当前试卷不可编辑！");
        }

        List<Long> categoryIdList = params.getCategoryIdList();
        if (CollectionUtils.isEmpty(categoryIdList)) {
            throw new IllegalParamsException("分类不能为空！");
        }
        categoryIdList = categoryIdList.stream().distinct().collect(Collectors.toList());
        this.updatePaperCategory(paperId, categoryIdList);

        // 先删除，再新增
        this.paperQuestionTypeService.lambdaUpdate().eq(PaperQuestionType::getPaperId, paperId).remove();
        this.paperQuestionService.lambdaUpdate().eq(PaperQuestion::getPaperId, paperId).remove();

        this.lambdaUpdate().eq(Paper::getId, paperId)
                .set(Paper::getName, params.getName())
                .set(Paper::getRemark, params.getRemark())
                .update();
        saveOther(params, paperId);

        mqProducerService.sendPaperUpdateMsg(paperId, SecurityContextHolder.getOrgId());
        return true;
    }


    private void updatePaperCategory(Long paperId, List<Long> categoryIds) {
        // 保存试卷和分类关联关系
        List<PaperCategory> dbPaperCategoryList = this.paperCategoryService.lambdaQuery()
                .eq(PaperCategory::getPaperId, paperId)
                .list();
        List<PaperCategory> paperCategoryList = new ArrayList<>();
        for (Long categoryId : categoryIds) {
            PaperCategory dbPaperCategory = dbPaperCategoryList.stream()
                    .filter(i -> i.getCategoryId().equals(categoryId))
                    .findFirst().orElse(null);

            // 数据库已经存在，则不用再添加了，将其从集合中移除
            // 不存在则放入新增集合中
            if (dbPaperCategory != null) {
                dbPaperCategoryList.remove(dbPaperCategory);

            } else {
                Category category = categoryService.getById(categoryId);
                if (category == null || category.getGroupId() != CategoryGroupEnum.PAPER.getId()) {
                    throw new IllegalParamsException(String.format("分类[id: %s]不存在", categoryId));
                }
                PaperCategory paperCategory = new PaperCategory();
                paperCategory.setCategoryId(categoryId);
                paperCategory.setPaperId(paperId);
                paperCategoryList.add(paperCategory);
            }
        }
        if (!CollectionUtils.isEmpty(paperCategoryList)) {
            paperCategoryService.saveBatch(paperCategoryList);
        }

        // 剩下的就是本次移除的
        if (!CollectionUtils.isEmpty(dbPaperCategoryList)) {
            List<Long> categoryIdList = dbPaperCategoryList.stream().map(i -> i.getId()).collect(Collectors.toList());
            paperCategoryService.lambdaUpdate().in(PaperCategory::getId, categoryIdList).remove();
        }
    }

    @Override
    public boolean enable(EnableRequest request) {
        List<Long> paperIdList = request.getIdList();
        List<Paper> paperList = this.lambdaQuery().in(Paper::getId, paperIdList).eq(Paper::getEditable, false).list();
        if (CollectionUtils.isNotEmpty(paperList)) {
            throw new ServiceException("试卷不允许删除！");
        }
        mqProducerService.sendPaperDeleteMsg(paperIdList);
        return this.lambdaUpdate().in(Paper::getId, paperIdList).set(Paper::getEnabled, request.getEnabled()).update();
    }

    @Override
    public void saveEsIndex(Long paperId, Long orgId) throws Exception {
        if (paperId == null) {
            throw new IllegalParamsException("paperId 不能为空！");
        }
        if (orgId == null) {
            throw new IllegalParamsException("orgId 不能为空！");
        }

        SecurityContextHolder.setOrgId(String.valueOf(orgId));
        EsPaper esPaper = new EsPaper();
        Paper paper = this.getById(paperId);
        BeanUtils.copyProperties(paper, esPaper);

        List<PaperCategory> paperCategoryList = paperCategoryService.lambdaQuery().eq(PaperCategory::getPaperId, paperId).list();
        if (CollectionUtils.isNotEmpty(paperCategoryList)) {
            List<SimpleCategoryDTO> categoryList = new ArrayList<>();
            for (PaperCategory paperCategory : paperCategoryList) {
                Category category = categoryService.getById(paperCategory.getCategoryId());
                if (category != null) {
                    categoryList.add(new SimpleCategoryDTO(category.getId(), category.getName()));
                }
            }
            esPaper.setCategoryList(categoryList);
        }

        try {
            paperSearchService.save(esPaper);
        } catch (IOException e) {
            log.error("索引试卷异常--> esPaper: {}", esPaper);
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean upddateEditable(UpdatePaperEditableRequest request) {
        Long paperId = request.getId();
        Paper paper = this.getById(paperId);
        if (paper == null) {
            throw new ServiceException("查不到试卷！");
        }

        return this.lambdaUpdate().eq(Paper::getId, paperId).set(Paper::getEditable, request.getEditable()).update();
    }

    @Override
    public SearchPageResult<PaperSearchResult> search(PaperSearchRequest param) {
        SearchPageResult<PaperSearchResult> searchPageResult = new SearchPageResult<>();
        searchPageResult.setPageNum(param.getPageNum());
        searchPageResult.setPageSize(param.getPageSize());

        // 是否需要从ES查询
        if (needESSearch(param)) {
            // 先从ES中获取基本信息
            SearchPageResult<EsPaper> esSearchPageResult = this.paperSearchService.search(param);
            // 再从数据库中查询详细信息
            List<EsPaper> list = esSearchPageResult.getList();
            if (CollectionUtils.isNotEmpty(list)) {
                List<Long> paperIdList = list.stream().map(i -> i.getId()).collect(Collectors.toList());
                List<PaperSearchResult> newPaperList = getPaperSearchResultList(paperIdList);
                Map<Long, EsPaper> resultMap = list.stream().collect(Collectors.toMap(EsPaper::getId, Function.identity()));
                newPaperList.forEach(i -> {
                    EsPaper result = resultMap.get(i.getId());
                    if (result != null) {
                        i.setName(result.getName());
                    }
                });

                searchPageResult.setList(newPaperList);
                searchPageResult.setTotal(esSearchPageResult.getTotal());
            }

        } else {
            Page page = new Page(param.getPageNum(), param.getPageSize());
            IPage<Long> idPage = this.getBaseMapper().pageId(page, param);
            List<Long> paperIdList = idPage.getRecords();
            List<PaperSearchResult> list = getPaperSearchResultList(paperIdList);
            searchPageResult.setList(list);
            searchPageResult.setTotal(idPage.getTotal());
        }

        return searchPageResult;
    }

    private boolean needESSearch(PaperSearchRequest params) {
        // 产品要求修改为不经过分词再搜索，就没必要从ES查了
        /*if (params != null) {
            if (StringUtils.isNotBlank(params.getKeyword())) {
                return true;
            }
        }*/

        return false;
    }

    private List<PaperSearchResult> getPaperSearchResultList(List<Long> paperIdList) {
        List<PaperSearchResult> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(paperIdList)) {
            List<PaperSearchResult> paperList = this.getBaseMapper().selectByIdListAndOrder(paperIdList);
            if (CollectionUtils.isNotEmpty(paperList)) {
                List<Long> userIdList = paperList.stream().map(PaperSearchResult::getCreateBy).collect(Collectors.toList());
                EmployeePageSearchDTO employeePageSearchDTO = new EmployeePageSearchDTO();
                employeePageSearchDTO.setUserIdList(userIdList);
                employeePageSearchDTO.setSize(userIdList.size());
                R<PageResultComDTO<EmployeePageResultDTO>> remoteResult = remoteEmployeeService.page(employeePageSearchDTO);
                PageResultComDTO<EmployeePageResultDTO> remoteData = remoteResult.getRemoteData();

                if (remoteData != null && CollectionUtils.isNotEmpty(remoteData.getRecords())) {
                    Map<Long, EmployeePageResultDTO> userMap = remoteData.getRecords().stream().collect(Collectors.toMap(EmployeePageResultDTO::getUserId, o -> o, (o1, o2) -> o1));
                    paperList.forEach(i -> {
                        EmployeePageResultDTO u = userMap.get(i.getCreateBy());
                        if (u != null) {
                            i.setCreateByName(u.getRealName());
                        }
                    });
                }
            }
            return paperList;
        }
        return list;
    }

}

