package cn.qbs.wa.teach.question.service;

import cn.qbs.wa.teach.common.core.domain.EnableRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.question.elasticsearch.SearchPageResult;
import cn.qbs.wa.teach.question.entity.Question;
import cn.qbs.wa.teach.question.pojo.question.*;
import cn.qbs.wa.teach.question.pojo.question.search.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * 试题(Question)表服务接口
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 18:17:09
 */
public interface QuestionService extends IService<Question> {

    /**
     * 新增试题
     * @param params
     * @return 保存后的试题ID
     */
    Long add(QuestionAddRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    QuestionDetailResponse detail(Serializable id);

    /**
     * 更新试题
     * @param params
     * @return
     */
    boolean update(QuestionUpdateRequest params);

    /**
     * 删除试题
     * @param idList
     * @return
     */
    void deleteByIds(List<Long> idList);

    boolean deleteById(Long questionId);

    boolean enable(EnableRequest request);

    /**
     * 题库搜索
     * @param searchRequest
     * @return
     */
    SearchPageResult<QuestionSearchResult> search(QuestionSearchRequest searchRequest);

    /**
     * 填充题型名称、难度名称、分类列表
     * @param searchResultList
     */
    void fillOtherField(List<QuestionSearchResult> searchResultList);

    /**
     * Excel解析
     * @param multipartFile
     * @return
     */
    @Deprecated
    List<ExcelQuestionParseResult> excelParse(MultipartFile multipartFile);

    /**
     * Excel试题批量保存
     * @param excelQuestionList
     * @return
     */
    @Deprecated
    R<List<ExcelQuestionSaveResult>> excelQuestionBatchSave(List<ExcelQuestionSaveTo> excelQuestionList);

    /**
     * 智能挑题
     * @param params
     * @return
     */
    List<QuestionSearchResult> smartChooseQuestions(SmartChooseQuestionRequest params) throws IOException;

    /**
     * 试题分类计数
     * @return
     */
    @Deprecated
    List<?> countCategory(Long orgId) throws IOException;

    /**
     * 试题分类计数
     * @return
     */
    @Deprecated
    CategoryQuestionCount categoryWithTotalQuestionCount(Long orgId) throws IOException;

    QuestionCountResponse count(QuestionCountRequest countRequest) throws IOException;

    /**
     * 试题分组(按题型)
     * @param questionIdList
     * @return
     */
    List<QuestionGroupResponse> groupList(List<Long> questionIdList) throws IOException;

    List<Question> selectByQuestionIdList(List<Long> questionIdList);

    List<QuestionDetailResponse> details(List<Long> idList);

    void saveEsIndex(Long questionId, Long orgId) throws Exception ;

    void addQuestionToESByCategoryIdList(List<Long> categoryIdList, Long orgId) throws Exception;

}

