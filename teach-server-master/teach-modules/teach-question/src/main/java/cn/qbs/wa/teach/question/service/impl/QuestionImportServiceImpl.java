package cn.qbs.wa.teach.question.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.question.constant.QuestionConst;
import cn.qbs.wa.teach.question.entity.*;
import cn.qbs.wa.teach.question.enumerate.CategoryGroupEnum;
import cn.qbs.wa.teach.question.pojo.question.ExcelQuestion;
import cn.qbs.wa.teach.question.pojo.question.ExcelQuestionImportResult;
import cn.qbs.wa.teach.question.pojo.question.ExcelQuestionParseResult;
import cn.qbs.wa.teach.question.pojo.question.QuestionAddRequest;
import cn.qbs.wa.teach.question.pojo.question.option.QuestionOptionTo;
import cn.qbs.wa.teach.question.service.*;
import cn.qbs.wa.teach.question.util.CacheUtil;
import cn.qbs.wa.teach.question.util.FileTypeCheckUtil;
import cn.qbs.wa.teach.question.util.RedisKeyUtil;
import cn.qbs.wa.teach.question.validate.excel.ExcelQuestionValidate;
import cn.qbs.wa.teach.question.validate.excel.ExcelQuestionValidateFactory;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 试题导入Service实现
 *
 * @Author zcm
 * @Date 2022-06-20 11:21
 * @Version 1.0
 */
@Slf4j
@Service
public class QuestionImportServiceImpl implements QuestionImportService {

    @Value("${question.importReportTemplate}")
    private String questionImportReportTemplate;

    @Resource
    private CategoryService categoryService;

    @Resource
    private ExcelQuestionValidateFactory excelQuestionValidateFactory;

    @Resource
    private DifficultyService difficultyService;

    @Resource
    private QuestionTypeService questionTypeService;

    @Resource
    private CacheUtil cacheUtil;

    @Resource
    private QuestionImportRecordService questionImportRecordService;

    @Resource
    private QuestionImportRecordDetailService questionImportRecordDetailService;

    @Resource
    private QuestionService questionService;


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
            throw new ServiceException("上传的导入模板格式不正确！");
        }

        List<ExcelQuestion> dataList = readListener.getDataList();
        log.info("解析Excel耗时{}s", Duration.between(startTime, Instant.now()).getSeconds());

        startTime = Instant.now();
        List<ExcelQuestionParseResult> resultList = new ArrayList<>();
        if (CollectionUtils.isEmpty(dataList)){
            if (readListener.isExecuted()) {
                throw new ServiceException("解析不到数据！");
            } else {
                throw new ServiceException("上传的导入模板格式不正确！");
            }
        }
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
                ExcelQuestionParseResult parseResult = errorList.get(j);
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
    public R excelQuestionBatchSave(List<ExcelQuestion> excelQuestionList) {
        if (CollectionUtils.isEmpty(excelQuestionList)) {
            throw new IllegalParamsException("参数不能为空！");
        }

        QuestionImportRecord questionImportRecord = new QuestionImportRecord();
        questionImportRecord.setOrgId(SecurityContextHolder.getOrgId());
        questionImportRecord.setCreateBy(SecurityContextHolder.getUserId());
        questionImportRecord.setImportTime(LocalDateTime.now());
        questionImportRecord.setTotal(excelQuestionList.size());
        this.questionImportRecordService.save(questionImportRecord);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();

        // 存在重复创建分类的并发问题，后续有时间再做优化处理
//        CountDownLatch countDownLatch = new CountDownLatch(excelQuestionList.size());
        Long questionImportRecordId = questionImportRecord.getId();
        for (ExcelQuestion excelQuestion : excelQuestionList) {
//            threadPoolTaskExecutor.execute(() -> {
                List<String> failureReasonList = new ArrayList<>();
                try {
                    String questionTypeName = excelQuestion.getQuestionTypeName();
                    ExcelQuestionValidate excelQuestionValidate = excelQuestionValidateFactory.getExcelQuestionValidate(questionTypeName);
                    excelQuestionValidate.validate(excelQuestion);

                    if (!excelQuestionValidate.passed()) {
                        failureReasonList.addAll(excelQuestionValidate.getErrorReasons());

                    } else {
                        String difficultyName = excelQuestion.getDifficultyName();
                        Difficulty difficulty = cacheUtil.getFromBucket(RedisKeyUtil.getDifficultyNameKey(difficultyName),
                                () -> difficultyService.lambdaQuery().eq(Difficulty::getName, difficultyName).last("limit 1").one()
                        );
                        if (difficulty == null) {
                            // 填写错误，默认简单
                            difficulty = cacheUtil.getFromBucket(RedisKeyUtil.getDifficultyNameKey(QuestionConst.DEFAULT_DIFFICULTY_NAME),
                                    () -> difficultyService.lambdaQuery().eq(Difficulty::getName, difficultyName).last("limit 1").one()
                            );

                        } else if (!difficulty.getEnabled()) {
                            failureReasonList.add("难度已禁用！");
                        }

                        QuestionType questionType = cacheUtil.getFromBucket(RedisKeyUtil.getQuestionTypeNameKey(questionTypeName),
                                () -> questionTypeService.lambdaQuery().eq(QuestionType::getName, questionTypeName).last("limit 1").one()
                        );
                        if (questionType == null) {
                            failureReasonList.add("不支持的题型！");
                        } else if (!questionType.getEnabled()) {
                            failureReasonList.add("题型已禁用！");
                        }

                        String categoryName = excelQuestion.getCategoryName().trim();
                        String[] categoryNameArray = categoryName.split("/");
                        Category category = null;
                        try {
                            category = categoryService.findOrCreate(categoryNameArray, CategoryGroupEnum.QUESTION);
                        } catch (Exception e) {
                            failureReasonList.add(e.getMessage());
                        }

                        if (CollectionUtils.isEmpty(failureReasonList)) {
                            // 保存, 构建参数
                            QuestionAddRequest questionAddRequest = new QuestionAddRequest();
                            BeanUtils.copyProperties(excelQuestion, questionAddRequest);
                            questionAddRequest.setDifficultyId(difficulty.getId());
                            questionAddRequest.setQuestionTypeId(questionType.getId());
                            questionAddRequest.setCategoryIds(Arrays.asList(Long.valueOf(category.getId())));
                            questionAddRequest.setOptions(builderOptionList(excelQuestion));

                            this.questionService.add(questionAddRequest);
                        }
                    }

                } catch (Exception e) {
                    failureReasonList.add(e.getMessage());
                    log.error("保存Excel试题异常: {}", excelQuestion);
                    e.printStackTrace();

                } finally {
                    QuestionImportRecordDetail questionImportRecordDetail = new QuestionImportRecordDetail();
                    questionImportRecordDetail.setOrgId(SecurityContextHolder.getOrgId());
                    questionImportRecordDetail.setQuestionImportRecordId(questionImportRecordId);
                    questionImportRecordDetail.setImportData(JSON.toJSONString(excelQuestion));
                    if (CollectionUtils.isEmpty(failureReasonList)) {
                        successCount.incrementAndGet();
                        questionImportRecordDetail.setSuccess(true);
                    } else {
                        failureCount.incrementAndGet();
                        questionImportRecordDetail.setSuccess(false);
                        String failureReason = failureReasonList.stream().collect(Collectors.joining());
                        questionImportRecordDetail.setFailureReason(failureReason);
                    }
                    this.questionImportRecordDetailService.save(questionImportRecordDetail);
//                    countDownLatch.countDown();
                }
//            });
        }

//        try {
//            countDownLatch.await(5, TimeUnit.MINUTES);
//        } catch (InterruptedException e) {
//            log.error("保存Excel试题异步任务被打断：{}", e);
//        }

        this.questionImportRecordService.lambdaUpdate().eq(QuestionImportRecord::getId, questionImportRecordId)
                .set(QuestionImportRecord::getSuccess, successCount.get())
                .set(QuestionImportRecord::getFailure, failureCount.get())
                .update();

        String msg = String.format("导入成功%d条，导入失败%d条。<br/>如需导入结果明细请直接下载导入报告", successCount.get(), failureCount.get());
        if (failureCount.get() > 0) {
            return R.fail(questionImportRecordId, msg);
        } else {
            return R.ok(questionImportRecordId, msg);
        }
    }

    @Override
    public void outExcel(Long importRecordId, HttpServletResponse response) throws Exception {
        QuestionImportRecord importRecord = this.questionImportRecordService.getById(importRecordId);
        if (importRecord == null) {
            throw new IllegalParamsException("查不到导入记录！");
        }

        List<QuestionImportRecordDetail> questionImportRecordDetailList = this.questionImportRecordDetailService.lambdaQuery()
                .eq(QuestionImportRecordDetail::getQuestionImportRecordId, importRecordId)
                .list();

        List<ExcelQuestionImportResult> dataList = questionImportRecordDetailList.stream().map(i -> {
            ExcelQuestionImportResult excelQuestionImportResult = JSON.parseObject(i.getImportData(), ExcelQuestionImportResult.class);
//            ExcelQuestionImportResult excelQuestionImportResult = new ExcelQuestionImportResult();
//            BeanUtil.copyProperties(i, excelQuestionImportResult);

            excelQuestionImportResult.setImportResult(i.getSuccess() ? "成功" : "失败");
            excelQuestionImportResult.setFailureReason(i.getFailureReason());
            return excelQuestionImportResult;
        }).collect(Collectors.toList());

        //固定的头信息
        response.setContentType("application/vnd.ms-excel");
        String charset = "UTF-8";
        response.setCharacterEncoding(charset);
        String fileName = String.format("试题导入报告（%s）", DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(importRecord.getImportTime()));
        //防止中文乱码
        String encodedFileName = URLEncoder.encode(fileName, charset).replaceAll("\\+", "%20") + ".xlsx";
        response.addHeader("fileName", encodedFileName);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName);

        FileInputStream fileInputStream = new FileInputStream(new File(questionImportReportTemplate));
        EasyExcel.write(response.getOutputStream()/*, ExcelQuestion.class*/).withTemplate(fileInputStream).sheet(1).doFill(dataList);
    }

    private List<QuestionOptionTo> builderOptionList(ExcelQuestion excelQuestion) {
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

        /**
         * 标记是否执行过
         * Excel 文件如果没有读取的sheet，就不会回调方法，就无法判断模板是否正确
         */
        private boolean executed = false;

        public boolean isExecuted() {
            return executed;
        }

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
            cellMap.forEach((k, v) -> {
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
            this.executed = true;
            log.info("所有数据解析完成！");
            // 这里也要保存数据，确保最后遗留的数据也存储到数据库
//            saveData();
        }

        public List<ExcelQuestion> getDataList() {
            return dataList;
        }

    }

}
