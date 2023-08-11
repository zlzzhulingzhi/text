package cn.qbs.wa.teach.question.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.EnableRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.teach.question.constant.QuestionConst;
import cn.qbs.wa.teach.question.elasticsearch.SearchPageResult;
import cn.qbs.wa.teach.question.entity.*;
import cn.qbs.wa.teach.question.enumerate.CategoryGroupEnum;
import cn.qbs.wa.teach.question.enumerate.QuestionTypeEnum;
import cn.qbs.wa.teach.question.mapper.CategoryMapper;
import cn.qbs.wa.teach.question.mapper.PaperMapper;
import cn.qbs.wa.teach.question.mapper.QuestionMapper;
import cn.qbs.wa.teach.question.pojo.SelectOption;
import cn.qbs.wa.teach.question.pojo.category.SimpleCategoryDTO;
import cn.qbs.wa.teach.question.pojo.question.*;
import cn.qbs.wa.teach.question.pojo.question.option.QuestionOptionTo;
import cn.qbs.wa.teach.question.pojo.question.search.*;
import cn.qbs.wa.teach.question.service.*;
import cn.qbs.wa.teach.question.util.CacheUtil;
import cn.qbs.wa.teach.question.util.FileTypeCheckUtil;
import cn.qbs.wa.teach.question.util.RedisKeyUtil;
import cn.qbs.wa.teach.question.validate.QuestionValidate;
import cn.qbs.wa.teach.question.validate.QuestionValidateFactory;
import cn.qbs.wa.teach.question.validate.excel.ExcelQuestionValidate;
import cn.qbs.wa.teach.question.validate.excel.ExcelQuestionValidateFactory;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 试题(Question)表服务实现类
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 18:17:22
 */
@Slf4j
@Service("questionService")
@RequiredArgsConstructor
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    private final QuestionValidateFactory questionValidateFactory;

    private final QuestionBodyService questionBodyService;

    private final QuestionOptionService questionOptionService;

    private final QuestionCategoryService questionCategoryService;

    private final QuestionSearchService questionSearchService;

    private final CategoryService categoryService;

    private final ExcelQuestionValidateFactory excelQuestionValidateFactory;

    private final DifficultyService difficultyService;

    private final QuestionTypeService questionTypeService;

    private final CacheUtil cacheUtil;

    private final MqProducerService mqProducerService;

    private final PaperMapper paperMapper;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private QuestionBasketService questionBasketService;


    private static final Map<Integer, Integer> categoryLevelWeightMap = new HashMap<>();
    static {
        categoryLevelWeightMap.put(1, 10000);
        categoryLevelWeightMap.put(2, 1000);
        categoryLevelWeightMap.put(3, 100);
        categoryLevelWeightMap.put(4, 10);
        categoryLevelWeightMap.put(5, 1);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(QuestionAddRequest params) {
        Long questionTypeId = params.getQuestionTypeId();
        QuestionValidate questionValidate = questionValidateFactory.getQuestionValidate(questionTypeId);
        BasicQuestion basicQuestion = new BasicQuestion();
        BeanUtils.copyProperties(params, basicQuestion);
        questionValidate.validate(basicQuestion);

        validateCategory(params.getCategoryIds());

        Question question = new Question();
        BeanUtils.copyProperties(params, question);
        if (question.getScore() == null) {
            question.setScore(QuestionConst.DEFAULT_SCORE);
        }
        boolean success = this.save(question);
        if (!success) {
            throw new ServiceException("保存试题失败！");
        }

        QuestionBody questionBody = new QuestionBody();
        BeanUtils.copyProperties(params, questionBody);
        Long questionId = question.getId();
        questionBody.setId(questionId);
        success = questionBodyService.save(questionBody);
        if (!success) {
            throw new ServiceException("保存试题主体失败！");
        }

        // 保存试题和分类关联关系
        List<QuestionCategory> questionCategoryList = new ArrayList<>();
        for (Long categoryId : params.getCategoryIds()) {
            QuestionCategory questionCategory = new QuestionCategory();
            questionCategory.setCategoryId(categoryId);
            questionCategory.setQuestionId(questionId);
            questionCategoryList.add(questionCategory);
        }
        success = questionCategoryService.saveBatch(questionCategoryList);
        if (!success) {
            throw new ServiceException("保存试题分类失败！");
        }

        // 选择题保存选项
        if (QuestionTypeEnum.DAN_XUAN_TI.getId() == questionTypeId || QuestionTypeEnum.DUO_XUAN_TI.getId() == questionTypeId) {
            if (!CollectionUtils.isEmpty(params.getOptions())) {
                List<QuestionOption> questionOptionList = new ArrayList<>();
                for (QuestionOptionTo optionTo : params.getOptions()) {
                    QuestionOption option = new QuestionOption();
                    BeanUtils.copyProperties(optionTo, option);
                    option.setQuestionId(questionId);
                    questionOptionList.add(option);
                }
                success = questionOptionService.saveBatch(questionOptionList);
                if (!success) {
                    throw new ServiceException("保存试题选项失败！");
                }
            }
        }

        mqProducerService.sendQuestionUpdateMsg(questionId, SecurityContextHolder.getOrgId());
        return questionId;
    }

    private void validateCategory(List<Long> categoryIds) {
        List<Category> categoryList = categoryService.lambdaQuery()
                .eq(Category::getGroupId, CategoryGroupEnum.QUESTION.getId())
                .in(Category::getId, categoryIds)
                .eq(Category::getEnabled, false)
                .list();
        if (CollectionUtils.isNotEmpty(categoryList)) {
            String categoryNames = categoryList.stream().map(i -> i.getName()).collect(Collectors.joining("、"));
            throw new ServiceException(String.format("分类【%s】已被禁用！", categoryNames));
        }
    }

    @Override
    public QuestionDetailResponse detail(Serializable id) {
        if (id == null) {
            return null;
        }
        QuestionDetailResponse detail = baseMapper.selectDetailById(id);
        if (detail != null) {
            if (CollectionUtils.isNotEmpty(detail.getCategoryList())) {
                List<Long> categoryIdList = detail.getCategoryList().stream().map(c -> c.getId()).distinct().collect(Collectors.toList());
                detail.setCategoryIds(categoryIdList);
            }
        }
        return detail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(QuestionUpdateRequest params) {
        Long id = params.getId();
        if (id == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        Question question = this.getById(id);
        if (question == null) {
            throw new IllegalParamsException("试题不存在！");
        }

        List<Paper> paperList = paperMapper.selectListByQuestionId(id);
        if (CollectionUtils.isNotEmpty(paperList)) {
            throw new ServiceException("试题已被使用，不能修改，请复制为1道新试题后再修改！");
        }

        Long questionTypeId = params.getQuestionTypeId();
        QuestionValidate questionValidate = questionValidateFactory.getQuestionValidate(questionTypeId);
        BasicQuestion basicQuestion = new BasicQuestion();
        BeanUtils.copyProperties(params, basicQuestion);
        questionValidate.validate(basicQuestion);

//        validateCategory(params.getCategoryIds());

        BeanUtils.copyProperties(params, question);
        if (question.getScore() == null) {
            question.setScore(QuestionConst.DEFAULT_SCORE);
        }
        boolean success = this.updateById(question);
        if (!success) {
            throw new ServiceException("保存试题失败！");
        }

        QuestionBody questionBody = new QuestionBody();
        BeanUtils.copyProperties(params, questionBody);
        Long questionId = question.getId();
        questionBody.setId(questionId);
        success = questionBodyService.updateById(questionBody);
        if (!success) {
            throw new ServiceException("保存试题主体失败！");
        }

        // 保存试题和分类关联关系
        List<QuestionCategory> dbQuestionCategoryList = this.questionCategoryService.lambdaQuery()
                .eq(QuestionCategory::getQuestionId, questionId)
                .list();
        List<QuestionCategory> questionCategoryList = new ArrayList<>();
        for (Long categoryId : params.getCategoryIds()) {
            QuestionCategory dbQuestionCategory = dbQuestionCategoryList.stream()
                    .filter(i -> i.getCategoryId().equals(categoryId))
                    .findFirst().orElse(null);

            // 数据库已经存在，则不用再添加了，将其从集合中移除
            // 不存在则放入新增集合中
            if (dbQuestionCategory != null) {
                dbQuestionCategoryList.remove(dbQuestionCategory);

            } else {
                QuestionCategory questionCategory = new QuestionCategory();
                questionCategory.setCategoryId(categoryId);
                questionCategory.setQuestionId(questionId);
                questionCategoryList.add(questionCategory);
            }
        }
        if (!CollectionUtils.isEmpty(questionCategoryList)) {
            success = questionCategoryService.saveBatch(questionCategoryList);
        }

        // 剩下的就是本次移除的
        if (!CollectionUtils.isEmpty(dbQuestionCategoryList)) {
            List<Integer> categoryIdList = dbQuestionCategoryList.stream().map(i -> i.getId()).collect(Collectors.toList());
            success &= questionCategoryService.lambdaUpdate().in(QuestionCategory::getId, categoryIdList).remove();
        }
        if (!success) {
            throw new ServiceException("保存试题分类失败！");
        }

        // 选择题保存选项
        if (QuestionTypeEnum.DAN_XUAN_TI.getId() == questionTypeId || QuestionTypeEnum.DUO_XUAN_TI.getId() == questionTypeId) {
            List<QuestionOption> dbOptionList = this.questionOptionService.lambdaQuery()
                    .eq(QuestionOption::getQuestionId, questionId)
                    .list();

            for (QuestionOptionTo optionTo : params.getOptions()) {
                // 如果数据库中已存在此选项，则更新
                // 否则新增
                QuestionOption dbOption = dbOptionList.stream().filter(i -> i.getOption().equalsIgnoreCase(optionTo.getOption()))
                        .findFirst()
                        .orElse(null);
                if (dbOption != null) {
                    dbOption.setContent(optionTo.getContent());
                    dbOption.setRemark(optionTo.getRemark());
                    this.questionOptionService.updateById(dbOption);
                    dbOptionList.remove(dbOption);
                } else {
                    dbOption = new QuestionOption();
                    BeanUtils.copyProperties(optionTo, dbOption);
                    dbOption.setQuestionId(questionId);
                    this.questionOptionService.save(dbOption);
                }
            }
            // 剩下的就是本次移除的
            if (!CollectionUtils.isEmpty(dbOptionList)) {
                List<Long> optionIdList = dbOptionList.stream().map(i -> i.getId()).collect(Collectors.toList());
                success &= this.questionOptionService.lambdaUpdate().in(QuestionOption::getId, optionIdList).remove();
            }
            if (!success) {
                throw new ServiceException("保存试题选项失败！");
            }
        } else {
            // 其他题型则清空选项，避免选择题转成其他题型时，选项依然存在的问题
            this.questionOptionService.lambdaUpdate().eq(QuestionOption::getQuestionId, questionId).remove();
        }

        mqProducerService.sendQuestionUpdateMsg(questionId, SecurityContextHolder.getOrgId());
        log.info("更新完毕");
        return true;
    }

    @Override
    public void deleteByIds(List<Long> idList) {
        for (Long questionId : idList) {
            this.deleteById(questionId);
        }

        new Thread(() -> this.questionBasketService.removeQuestionFormAllQuestionBasket(idList)).start();
    }

    @Override
    @Transactional
    public boolean deleteById(Long questionId) {
        List<Paper> paperList = paperMapper.selectListByQuestionId(questionId);
        if (CollectionUtils.isNotEmpty(paperList)) {
            throw new ServiceException("试题已被试卷使用，不能删除！");
        }

        this.questionCategoryService.remove(Wrappers.lambdaQuery(QuestionCategory.class).eq(QuestionCategory::getQuestionId, questionId));
        this.questionOptionService.remove(Wrappers.lambdaQuery(QuestionOption.class).eq(QuestionOption::getQuestionId, questionId));

        this.questionBodyService.removeById(questionId);
        this.removeById(questionId);
        // 使用消息队列删除ES索引存在延迟等问题，删除操作并发不大，改为直接删除
//        mqProducerService.sendQuestionDeleteMsg(Collections.singletonList(questionId));
        questionSearchService.delete(questionId);
        return true;
    }

    @Override
    public boolean enable(EnableRequest request) {
        List<Long> questionIdList = request.getIdList();
        boolean success = this.lambdaUpdate().in(Question::getId, questionIdList)
                .set(Question::getEnabled, request.getEnabled())
                .set(Question::getUpdateBy, SecurityContextHolder.getUserId())
                .set(Question::getUpdateTime, LocalDateTime.now())
                .update();
//        mqProducerService.sendQuestionDeleteMsg(questionIdList);
        Long orgId = SecurityContextHolder.getOrgId();
        for (Long questionId : questionIdList) {
            mqProducerService.sendQuestionUpdateMsg(questionId, orgId);
        }
        return success;
    }

    @Override
    public SearchPageResult<QuestionSearchResult> search(QuestionSearchRequest params) {
        long beginTime = System.currentTimeMillis();
        long startTime = System.currentTimeMillis();
        SearchPageResult<QuestionSearchResult> searchPageResult = new SearchPageResult<>();

        // 如果传入单个分类ID，则附带查询出下面子节点下挂的试题
        Long categoryId = params.getCategoryId();
        if (categoryId != null) {
            List<Long> categoryIds = new ArrayList<>();
            categoryIds.add(categoryId);
            List<Long> childCategoryIdList = this.categoryMapper.getChildIdList(categoryId);
            if (CollectionUtils.isNotEmpty(childCategoryIdList)) {
                categoryIds.addAll(childCategoryIdList);
            }
            params.setCategoryIds(categoryIds);
        }

        if (needESSearch(params)) {
            // 先从ES中获取基本信息
            searchPageResult = questionSearchService.search(params);
            // 再从数据库中查询详细信息
            List<QuestionSearchResult> list = searchPageResult.getList();
            if (CollectionUtils.isNotEmpty(list)) {
                List<Long> questionIdList = list.stream().map(i -> i.getId()).collect(Collectors.toList());
                List<QuestionSearchResult> newQuestionList = getQuestionSearchResultList(questionIdList);
                Map<Long, QuestionSearchResult> resultMap = list.stream().collect(Collectors.toMap(QuestionSearchResult::getId, Function.identity()));
                newQuestionList.forEach(i -> {
                    QuestionSearchResult result = resultMap.get(i.getId());
                    if (result != null) {
                        i.setContent(result.getContent());
                    }
                });

                searchPageResult.setList(newQuestionList);
            }

        } else {
            Page page = new Page(params.getPageNum(), params.getPageSize());
            IPage<Long> idPage = questionMapper.pageId(page, params);
            List<Long> questionIdList = idPage.getRecords();
            List<QuestionSearchResult> list = getQuestionSearchResultList(questionIdList);
            searchPageResult.setList(list);
            searchPageResult.setPageNum(Long.valueOf(idPage.getCurrent()).intValue());
            searchPageResult.setPageSize(Long.valueOf(idPage.getSize()).intValue());
            searchPageResult.setTotal(idPage.getTotal());
        }

        log.info("搜索试题耗时{}ms", (System.currentTimeMillis() - startTime));
//        startTime = System.currentTimeMillis();
//        fillOtherField(searchPageResult.getList());
//        log.info("填充试题属性耗时{}ms", (System.currentTimeMillis() - startTime));
        log.info("搜索试题总耗时{}ms", (System.currentTimeMillis() - beginTime));
        return searchPageResult;
    }

    private List<QuestionSearchResult> getQuestionSearchResultList(List<Long> questionIdList) {
        List<QuestionSearchResult> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(questionIdList)) {
            List<QuestionDetailResponse> newQuestionList = questionMapper.selectDetailsByIdListByOrder(questionIdList);
            list = newQuestionList.stream().map(i -> {
                QuestionSearchResult q = new QuestionSearchResult();
                BeanUtil.copyProperties(i, q);
                return q;
            }).collect(Collectors.toList());
        }
        return list;
    }

    private boolean needESSearch(QuestionSearchRequest params) {
        if (params != null) {
            if (StringUtils.isNotBlank(params.getKeyword())
                 /* || params.getCategoryId() != null
                    || CollectionUtils.isNotEmpty(params.getCategoryIds())
                    || CollectionUtils.isNotEmpty(params.getDifficultyIds())
                    || CollectionUtils.isNotEmpty(params.getQuestionTypeIds())*/) {
                return true;
            }
        }

        return false;
    }

    /**
     * 填充题型名称、难度名称、分类列表
     * @param searchResultList
     */
    @Override
    public void fillOtherField(List<QuestionSearchResult> searchResultList) {
        if (!CollectionUtils.isEmpty(searchResultList)) {
            for (QuestionSearchResult question : searchResultList) {
                /*QuestionType questionType = this.questionTypeService.getByCache(question.getQuestionTypeId());
                if (questionType != null) {
                    question.setQuestionTypeName(questionType.getName());
                }*/

                /*Difficulty difficulty = this.difficultyService.getByCache(question.getDifficultyId());
                if (difficulty != null) {
                    question.setDifficultyName(difficulty.getName());
                }*/

                for (Long categoryId : question.getCategoryIds()) {
                    Category category = this.categoryService.getById(categoryId);
                    if (category != null) {
                        question.getCategoryList().add(new SimpleCategoryDTO(category.getId(), category.getName()));
                    }
                }
            }
        }
    }

    @Override
    public List<ExcelQuestionParseResult> excelParse(MultipartFile multipartFile) {
        Instant beginTime = Instant.now();
        if (!FileTypeCheckUtil.isExcel(multipartFile)) {
            throw new ServiceException("文件格式有误，请检查！");
        }
        Instant startTime = Instant.now();
        ExcelQuestionAnalysisListener readListener = new ExcelQuestionAnalysisListener();
        try {
            EasyExcel.read(multipartFile.getInputStream(), ExcelQuestion.class, readListener).ignoreEmptyRow(true).sheet(1).doRead();
        } catch (Exception e) {
            throw new ServiceException("无法正常读取Excel表格，请检查！");
        }

        List<ExcelQuestion> dataList = readListener.getDataList();
        log.info("解析Excel耗时{}s", Duration.between(startTime, Instant.now()).getSeconds());
//        log.info("Excel导入试题解析结果: {}", dataList);

        startTime = Instant.now();
        List<ExcelQuestionParseResult> resultList = new ArrayList<>();
        List<ExcelQuestionParseResult> errorList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            ExcelQuestion excelQuestion = dataList.get(i);
            ExcelQuestionParseResult result = new ExcelQuestionParseResult();
            BeanUtil.copyProperties(excelQuestion, result);
            ExcelQuestionValidate excelQuestionValidate = excelQuestionValidateFactory.getExcelQuestionValidate(excelQuestion.getQuestionTypeName());
//            String mark = String.format("第%d行", excelQuestion.getRowIndex());
//            excelQuestionValidate.setMark(mark);
            excelQuestionValidate.validate(excelQuestion);

            boolean passed = excelQuestionValidate.passed();
            result.setPassed(passed);
            if (!passed) {
                result.setErrorReasons(excelQuestionValidate.getErrorReasons());
                errorList.add(result);
            } else {
                resultList.add(result);
            }
        }

        if (CollectionUtils.isNotEmpty(errorList)) {
            StringBuilder errorMsg = new StringBuilder();
            for (int j = 0; j < errorList.size(); j++) {
                ExcelQuestionParseResult parseResult =  errorList.get(j);
                if (j > 0) {
                    errorMsg.append("<br/>");
                }
                errorMsg.append("【第").append(parseResult.getRowNum()).append("行】");
                for (String errorReason : parseResult.getErrorReasons()) {
                    errorMsg.append(errorReason);
                }
            }

            throw new IllegalParamsException(errorMsg.toString());
        }

        log.info("校验Excel耗时{}s，条数: {}", Duration.between(startTime, Instant.now()).getSeconds(), resultList.size());
        log.info("解析验证Excel总耗时{}s", Duration.between(beginTime, Instant.now()).getSeconds());
        return resultList;
    }

    @Override
    public R<List<ExcelQuestionSaveResult>> excelQuestionBatchSave(List<ExcelQuestionSaveTo> excelQuestionList) {
        if (CollectionUtils.isEmpty(excelQuestionList)) {
            throw new IllegalParamsException("参数不能为空！");
        }

        /*
            原本设计是：后端把所有试题都校验、保存1遍，给出成功或失败的标记与前端传过来的每道题的标识绑定，将结果一次性返回给前端。
                       然后前端区分显示保存成功和失败的，用户在页面修改后再提交只提交之前保存失败的。已保存成功的不再提交，避免重复导入。

            先修改设计为：后端把错误信息一次性返回给前端，如果有错误，则用户根据错误提示修改Excel后再提交，这样会出现重复，必须把之前保存成功的删除。
                         通过在此方法上添加事务也是无法解决问题的，因为一旦保存出现异常回滚，就无法再保存后面的，不能一次性给出所有错误。
                         而且在每次添加试题成功后会将试题加入到ES索引中，对MySQL数据的修改可以通过事务回滚，但是，对ES的修改却无法回滚，依然能查到试题。
                         所以，将添加成功的试题ID保存到集合中，全部保存完毕后，如果出现异常，则删除已加入的试题。
         */
        boolean hasError = false;
        List<Long> savedQuestionIdList = new ArrayList<>();
        List<ExcelQuestionSaveResult> responseList = new ArrayList<>();
        // 存在重复创建分类的并发问题，后续有时间再做优化处理
//        CountDownLatch countDownLatch = new CountDownLatch(excelQuestionList.size());
        for (ExcelQuestionSaveTo excelQuestion : excelQuestionList) {
//            poolTaskExecutor.execute(() -> {
                try {
                    String questionTypeName = excelQuestion.getQuestionTypeName();
                    ExcelQuestionValidate excelQuestionValidate = excelQuestionValidateFactory.getExcelQuestionValidate(questionTypeName);
                    excelQuestionValidate.validate(excelQuestion);

                    ExcelQuestionSaveResult result = new ExcelQuestionSaveResult();
                    result.setKey(excelQuestion.getKey());
                    if (!excelQuestionValidate.passed()) {
                        hasError = true;
                        result.setErrorReasons(excelQuestionValidate.getErrorReasons());
                        responseList.add(result);
                        continue;
                    }

                    String difficultyName = excelQuestion.getDifficultyName();
                    Difficulty difficulty = cacheUtil.getFromBucket(RedisKeyUtil.getDifficultyNameKey(difficultyName),
                            () -> difficultyService.lambdaQuery().eq(Difficulty::getName, difficultyName).last("limit 1").one()
                    );
                    if (difficulty == null) {
//                        hasError = true;
//                        result.addErrorReason("不存在的难度！");

                        // 填写错误，默认简单
                        difficulty = cacheUtil.getFromBucket(RedisKeyUtil.getDifficultyNameKey(QuestionConst.DEFAULT_DIFFICULTY_NAME),
                                () -> difficultyService.lambdaQuery().eq(Difficulty::getName, difficultyName).last("limit 1").one()
                        );

                    } else if (!difficulty.getEnabled()) {
                        hasError = true;
                        result.addErrorReason("难度已禁用！");
                    }

                    QuestionType questionType = cacheUtil.getFromBucket(RedisKeyUtil.getQuestionTypeNameKey(questionTypeName),
                            () -> questionTypeService.lambdaQuery().eq(QuestionType::getName, questionTypeName).last("limit 1").one()
                    );
                    if (questionType == null) {
                        hasError = true;
                        result.addErrorReason("不支持的题型！");
                    } else if (!questionType.getEnabled()) {
                        hasError = true;
                        result.addErrorReason("题型已禁用！");
                    }

                    String categoryName = excelQuestion.getCategoryName().trim();
                    String[] categoryNameArray = categoryName.split("/");
                    Category category = null;
                    try {
                        category = categoryService.findOrCreate(categoryNameArray, CategoryGroupEnum.QUESTION);
                    } catch (Exception e) {
                        hasError = true;
                        result.addErrorReason(e.getMessage());
                    }

                    if (CollectionUtils.isEmpty(result.getErrorReasons())) {
                        // 保存, 构建参数
                        QuestionAddRequest questionAddRequest = new QuestionAddRequest();
                        BeanUtils.copyProperties(excelQuestion, questionAddRequest);
                        questionAddRequest.setDifficultyId(difficulty.getId());
                        questionAddRequest.setQuestionTypeId(questionType.getId());
                        questionAddRequest.setCategoryIds(Arrays.asList(Long.valueOf(category.getId())));
                        questionAddRequest.setOptions(builderOptionList(excelQuestion));

                        try {
                            Long questionId = this.add(questionAddRequest);
                            savedQuestionIdList.add(questionId);
                            result.setId(questionId);
                        } catch (Exception e) {
                            hasError = true;
                            result.addErrorReason(e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    responseList.add(result);
                } catch (Exception e) {
                    hasError = true;
                    log.error("保存Excel试题异常: {}", excelQuestion);
                    e.printStackTrace();
                } finally {
//                    countDownLatch.countDown();
                }
//            });
        }

        /*try {
            countDownLatch.await(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            log.error("保存Excel试题异步任务被打断：{}", e);
        }*/

        if (hasError) {
            // 出现异常，将保存成功的试题删除，避免重复保存
            asyncDeleteQuestion(savedQuestionIdList);
            return R.fail(responseList, "保存失败！");
        } else {
            return R.ok(responseList, "保存成功！");
        }
    }

    private void asyncDeleteQuestion(List<Long> questionIdList) {
        if (CollectionUtils.isNotEmpty(questionIdList)) {
            CountDownLatch countDownLatch = new CountDownLatch(questionIdList.size());
            for (Long questionId : questionIdList) {
                threadPoolTaskExecutor.execute(() -> {
                    try {
                        deleteById(questionId);
                    } finally {
                        countDownLatch.countDown();
                    }
                });
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<QuestionSearchResult> smartChooseQuestions(SmartChooseQuestionRequest params) throws IOException {
        // 如果传入单个分类ID，则附带查询出下面子节点下挂的试题
        Long categoryId = params.getCategoryId();
        if (categoryId != null) {
            List<Long> categoryIds = new ArrayList<>();
            categoryIds.add(categoryId);
            List<Long> childCategoryIdList = this.categoryMapper.getChildIdList(categoryId);
            if (CollectionUtils.isNotEmpty(childCategoryIdList)) {
                categoryIds.addAll(childCategoryIdList);
            }
            params.setCategoryIds(categoryIds);
        }

        List<QuestionSearchResult> questionList = new ArrayList<>();
        for (QuestionTypeQuantity questionTypeQuantity : params.getQuestionTypeQuantityList()) {
            // 构建新的查询参数
            QuestionRandomSearchParam questionRandomSearchParam = new QuestionRandomSearchParam();
            questionRandomSearchParam.setQuestionTypeQuantity(questionTypeQuantity);
            questionRandomSearchParam.setCategoryIds(params.getCategoryIds());
            questionRandomSearchParam.setDifficultyIds(params.getDifficultyIds());
            questionRandomSearchParam.setOrgId(params.getOrgId());
            List<QuestionSearchResult> resultList = this.questionSearchService.randomSearch(questionRandomSearchParam);
            if (CollectionUtils.isNotEmpty(resultList)) {
                // 再从数据库中查询详细信息
                List<Long> questionIdList = resultList.stream().map(i -> i.getId()).collect(Collectors.toList());
                resultList = getQuestionSearchResultList(questionIdList);
            }
            questionList.addAll(resultList);
        }

//        fillOtherField(questionList);
        return questionList;
    }

    @Override
    @Deprecated
    public List<?> countCategory(Long orgId) throws IOException {
        List<CategoryQuestionCount> resultList = questionSearchService.countCategory(orgId);
        List<Category> categoryList = this.categoryService.getByGroupId(CategoryGroupEnum.QUESTION.getId(), null);
        for (Category item : categoryList) {
            CategoryQuestionCount countResult = resultList.stream().filter(i -> i.getId().equals(item.getId())).findFirst().orElse(null);
            if (countResult == null) {
                CategoryQuestionCount categoryQuestionCountResult = new CategoryQuestionCount(item.getId(), item.getName(), item.getSortNum(), 0L);
                categoryQuestionCountResult.setParentId(item.getParentId());
                resultList.add(categoryQuestionCountResult);
            } else {
                countResult.setParentId(item.getParentId());
                countResult.setName(item.getName());
                countResult.setSortNum(item.getSortNum());
            }
        }

        resultList = resultList.stream()
                .filter(i -> i.getSortNum() != null)
                .sorted(
                        Comparator.comparing(CategoryQuestionCount::getSortNum)
                                .thenComparing(CategoryQuestionCount::getId)
                ).collect(Collectors.toList());
        return TreeUtil.tree(resultList);
    }

    /**
     * 已废弃
     * @param orgId
     * @return
     * @throws IOException
     */
    @Override
    public CategoryQuestionCount categoryWithTotalQuestionCount(Long orgId) throws IOException {
        Long totalQuestion = questionSearchService.totalQuestion(orgId);
        CategoryQuestionCount root = new CategoryQuestionCount(0L, "全部分类", 0, totalQuestion);
        List<CategoryQuestionCount> resultList;
        List<Category> categoryList = this.categoryService.getByGroupId(CategoryGroupEnum.QUESTION.getId(), null);
        if (CollectionUtils.isEmpty(categoryList)) {
            resultList = new ArrayList<>();
        } else {
            resultList = categoryList.stream().map(c -> {
                        CategoryQuestionCount categoryQuestionCount = new CategoryQuestionCount(c.getId(), c.getName(), c.getSortNum(), null);
                        categoryQuestionCount.setParentId(c.getParentId());
                        return categoryQuestionCount;
                    }
            ).collect(Collectors.toList());
        }

        List<CategoryQuestionCount> treeList = TreeUtil.tree(resultList);
        root.setChildren(treeList);
        return root;
    }

    @Override
    public QuestionCountResponse count(QuestionCountRequest param) throws IOException {
        // 如果传入单个分类ID，则附带查询出下面子节点下挂的试题
        Long categoryId = param.getCategoryId();
        if (categoryId != null) {
            List<Long> categoryIds = new ArrayList<>();
            categoryIds.add(categoryId);
            List<Long> childCategoryIdList = this.categoryMapper.getChildIdList(categoryId);
            if (CollectionUtils.isNotEmpty(childCategoryIdList)) {
                categoryIds.addAll(childCategoryIdList);
            }
            param.setCategoryIds(categoryIds);
        }

        QuestionCountResponse result = this.questionSearchService.count(param);

        List<SelectOption> difficultySelectOptionList = this.difficultyService.getSelectOptionList();
        for (SelectOption item : difficultySelectOptionList) {
            CountResult countResult = result.getDifficultyList().stream().filter(i -> i.getId().equals(item.getId())).findFirst().orElse(null);
            if (countResult == null) {
                result.getDifficultyList().add(new CountResult(item.getId(), item.getName(), item.getSortNum(), 0L));
            } else {
                countResult.setName(item.getName());
                countResult.setSortNum(item.getSortNum());
            }
        }

        List<SelectOption> questionTypeSelectOptionList = this.questionTypeService.getSelectOptionList();
        for (SelectOption item : questionTypeSelectOptionList) {
            CountResult countResult = result.getQuestionTypeList().stream().filter(i -> i.getId().equals(item.getId())).findFirst().orElse(null);
            if (countResult == null) {
                result.getQuestionTypeList().add(new CountResult(item.getId(), item.getName(), item.getSortNum(), 0L));
            } else {
                countResult.setName(item.getName());
                countResult.setSortNum(item.getSortNum());
            }
        }

        // 排序
        List<CountResult> difficultyList = result.getDifficultyList().stream().sorted(Comparator.comparing(CountResult::getSortNum)).collect(Collectors.toList());
        result.setDifficultyList(difficultyList);
        List<CountResult> questionTypeList = result.getQuestionTypeList().stream().sorted(
                Comparator.comparing(CountResult::getSortNum, Comparator.nullsLast(Comparator.naturalOrder()))
            ).collect(Collectors.toList());
        result.setQuestionTypeList(questionTypeList);
        return result;
    }

    @Override
    public List<QuestionGroupResponse> groupList(List<Long> questionIdList) throws IOException {
        List<QuestionDetailResponse> questionDetailResponseList = this.questionMapper.selectDetailsByIdList(questionIdList);
        List<QuestionSearchResult> questionList = BeanUtil.copyToList(questionDetailResponseList, QuestionSearchResult.class);
        fillOtherField(questionList);

        if (!CollectionUtils.isEmpty(questionList)) {
            Map<Long, List<QuestionSearchResult>> groupMap = questionList.stream().collect(Collectors.groupingBy(QuestionSearchResult::getQuestionTypeId, Collectors.toList()));
            List<QuestionGroupResponse> list = groupMap.entrySet().stream().map(m -> {
                QuestionGroupResponse group = new QuestionGroupResponse();
                Long questionTypeId = m.getKey();
                group.setQuestionTypeId(questionTypeId);
                QuestionType questionType = this.questionTypeService.getByCache(questionTypeId);
                if (questionType != null) {
                    String questionTypeName = questionType.getName();
                    group.setQuestionTypeName(questionTypeName);
                    group.setSortNum(questionType.getSortNum());
                }
                group.setQuestionList(m.getValue());
                return group;
            }).collect(Collectors.toList());

            list = list.stream().sorted(Comparator.comparingInt(QuestionGroupResponse::getSortNum)).collect(Collectors.toList());
            AtomicInteger index = new AtomicInteger(0);
            list.forEach(i -> {
                i.setSortNum(index.incrementAndGet());
                // 填空题设置答案数量
                if (i.getQuestionTypeId().equals(QuestionTypeEnum.TIAN_KONG_TI.getId())) {
                    questionList.parallelStream().forEach(q -> {
                        Integer answerCount = 1;
                        if (StringUtils.isNotBlank(q.getAnswer())) {
                            answerCount = q.getAnswer().split("\\" + QuestionConst.TIAN_KONG_TI_ANSWER_SEPARATOR).length;
                        }
                        q.setAnswerCount(answerCount);
                    });
                }
            });
            return list;
        }

        return null;
    }

    @Override
    public List<Question> selectByQuestionIdList(List<Long> questionIdList) {
        if (CollectionUtils.isEmpty(questionIdList)) {
            return null;
        }
        return this.lambdaQuery().eq(Question::getEnabled, true).in(Question::getId, questionIdList).list();
    }

    @Override
    public List<QuestionDetailResponse> details(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return null;
        }
        return baseMapper.selectDetailsByIdList(idList);
    }

    @Override
    public void saveEsIndex(Long questionId, Long orgId) throws Exception {
        if (questionId == null) {
            throw new IllegalParamsException("questionId 不能为空！");
        }
        if (orgId == null) {
            throw new IllegalParamsException("orgId 不能为空！");
        }

        SecurityContextHolder.setOrgId(String.valueOf(orgId));
        QuestionDetailResponse detail = this.detail(questionId);
        if (detail == null) {
            return;
        }

        ESQuestion esQuestion = new ESQuestion();
        BeanUtils.copyProperties(detail, esQuestion);

        Integer weight = 0;
        Category category = baseMapper.selectMinLevelCategory(questionId);
        if (category != null) {
            weight = category.getLevel() * categoryLevelWeightMap.get(category.getLevel());
        }
        esQuestion.setWeight(weight);

        try {
            questionSearchService.save(esQuestion);
        } catch (IOException e) {
            log.error("索引试题异常--> esQuestion: " + JSON.toJSONString(esQuestion), e);
            throw e;
        }
    }

    @Override
    public void addQuestionToESByCategoryIdList(List<Long> categoryIdList, Long orgId) throws Exception {
        if (CollectionUtils.isNotEmpty(categoryIdList) && orgId != null) {
            SecurityContextHolder.setSelectOrgId(String.valueOf(orgId));
            List<Long> questionIdList = baseMapper.selectByCategoryIdList(categoryIdList);
            if (CollectionUtils.isNotEmpty(questionIdList)) {
                for (Long questionId : questionIdList) {
                    this.saveEsIndex(questionId, orgId);
                }
            }
        }
    }

    private List<QuestionOptionTo> builderOptionList(ExcelQuestionSaveTo excelQuestion) {
        List<QuestionOptionTo> optionList = new ArrayList<>();
        if (excelQuestion != null) {
            builderOptionList(optionList, new QuestionOptionTo("A", excelQuestion.getOptionA()));
            builderOptionList(optionList, new QuestionOptionTo("B", excelQuestion.getOptionB()));
            builderOptionList(optionList, new QuestionOptionTo("C", excelQuestion.getOptionC()));
            builderOptionList(optionList, new QuestionOptionTo("D", excelQuestion.getOptionD()));
            builderOptionList(optionList, new QuestionOptionTo("E", excelQuestion.getOptionE()));
            builderOptionList(optionList, new QuestionOptionTo("F", excelQuestion.getOptionF()));
            builderOptionList(optionList, new QuestionOptionTo("G", excelQuestion.getOptionG()));
            builderOptionList(optionList, new QuestionOptionTo("H", excelQuestion.getOptionH()));
            builderOptionList(optionList, new QuestionOptionTo("I", excelQuestion.getOptionI()));
            builderOptionList(optionList, new QuestionOptionTo("J", excelQuestion.getOptionJ()));
        }
        return optionList;
    }

    private void builderOptionList(List<QuestionOptionTo> optionList, QuestionOptionTo option) {
        if (option != null && StringUtils.isNotBlank(option.getOption()) && StringUtils.isNotBlank(option.getContent())) {
            optionList.add(option);
        }
    }


    class ExcelQuestionAnalysisListener extends AnalysisEventListener<ExcelQuestion> {

        private List<ExcelQuestion> dataList = new ArrayList<>();

        /**
         * 每解析一行会回调此方法
         * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
         *
         * @param questionExcelVo
         * @param analysisContext
         */
        @Override
        public void invoke(ExcelQuestion questionExcelVo, AnalysisContext analysisContext) {
            Integer rowIndex = analysisContext.readRowHolder().getRowIndex();
            questionExcelVo.setRowNum(rowIndex + 1);
            //数据存储到list，供批量处理，或后续自己业务逻辑处理。
            if (StringUtils.isNotBlank(questionExcelVo.getContent())) {
                dataList.add(questionExcelVo);
                return;
            }

            // 判断是否所有属性都为空
            Map<Integer, Cell> cellMap = analysisContext.readRowHolder().getCellMap();
            cellMap.forEach((k,v) -> {
                ReadCellData cell = (ReadCellData) v;
                if (!CellDataTypeEnum.EMPTY.equals(cell.getType())) {
                    dataList.add(questionExcelVo);
                    return;
                }
            });
            /*if (!ObjUtil.checkNullAndEmpty(questionExcelVo)) {
                dataList.add(questionExcelVo);
            }*/
        }

        /**
         * 整个excel解析结束会执行此方法
         *
         * @param analysisContext
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
            log.info("所有数据解析完成！");
            // 这里也要保存数据，确保最后遗留的数据也存储到数据库
//            saveData();
        }

        public List<ExcelQuestion> getDataList() {
            return dataList;
        }

    }

}

