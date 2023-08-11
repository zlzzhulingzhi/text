package cn.qbs.wa.teach.question.controller;


import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.domain.BasePageRequest;
import cn.qbs.wa.teach.question.pojo.question.ExcelQuestion;
import cn.qbs.wa.teach.question.pojo.question.ExcelQuestionParseResult;
import cn.qbs.wa.teach.question.pojo.question.QuestionImportRecordPageResponse;
import cn.qbs.wa.teach.question.service.QuestionImportRecordService;
import cn.qbs.wa.teach.question.service.QuestionImportService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 试题导入记录控制层
 *
 * @author zcm
 * @version 1.0
 * @date 2022-06-17 17:41:44
 */
@RestController
@RequestMapping("question/import")
public class QuestionImportController {


    @Resource
    private QuestionImportService questionImportService;

    @Resource
    private QuestionImportRecordService questionImportRecordService;


    /**
     * Excel试题解析
     * @return
     */
    @PostMapping("/parse")
    //@RequiresPermissions("question:import:excelParse")
    //@Log(title = "Excel试题解析", businessType = BusinessType.IMPORT)
    public R<List<ExcelQuestionParseResult>> excelParse(@RequestParam("file") MultipartFile multipartFile) {
        return R.ok(questionImportService.excelParse(multipartFile));
    }

    /**
     * Excel试题批量保存
     * @return
     */
    @PostMapping("/batchSave")
//    @RequiresPermissions("question:import:batchSave")
    //@Log(title = "Excel试题保存", businessType = BusinessType.IMPORT)
    public R excelQuestionBatchSave(@RequestBody List<ExcelQuestion> excelQuestionList) {
        return questionImportService.excelQuestionBatchSave(excelQuestionList);
    }

    /**
     * 分页查询试题导入记录表
     *
     * @param params
     * @return 
     */
    @PostMapping("record/page")
    //@RequiresPermissions("question:import:record:page")
    //@Log(title = "分页查询试题导入记录表", businessType = BusinessType.OTHER)
    public R<IPage<QuestionImportRecordPageResponse>> page(@RequestBody BasePageRequest params) {
        return R.ok(this.questionImportRecordService.page(params));
    }


    /**
     * 下载试题导入报告
     * @param response
     * @throws Exception
     */
    @PostMapping("/download/report")
    //@Log(title = "下载试题导入报告", businessType = BusinessType.OTHER)
    //@RequiresPermissions("question:import:download:report")
    public void downloadReport(@RequestBody IdRequest params, HttpServletResponse response) throws Exception {
        this.questionImportService.outExcel(params.getId(), response);
    }

}

