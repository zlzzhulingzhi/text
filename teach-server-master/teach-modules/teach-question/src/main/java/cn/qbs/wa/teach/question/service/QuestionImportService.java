package cn.qbs.wa.teach.question.service;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.question.pojo.question.ExcelQuestion;
import cn.qbs.wa.teach.question.pojo.question.ExcelQuestionParseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 试题导入Service
 *
 * @Author zcm
 * @Date 2022-06-20 11:20
 * @Version 1.0
 */
public interface QuestionImportService {


    /**
     * Excel解析
     * @param multipartFile
     * @return
     */
    List<ExcelQuestionParseResult> excelParse(MultipartFile multipartFile);

    /**
     * Excel试题批量保存
     * @param excelQuestionList
     * @return
     */
    R excelQuestionBatchSave(List<ExcelQuestion> excelQuestionList);

    /**
     * 输出Excel
     * @param importRecordId
     * @param response
     */
    void outExcel(Long importRecordId, HttpServletResponse response) throws Exception;

}
