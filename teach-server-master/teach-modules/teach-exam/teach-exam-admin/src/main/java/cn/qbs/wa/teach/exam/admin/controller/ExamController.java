package cn.qbs.wa.teach.exam.admin.controller;


import cn.hutool.core.util.DesensitizedUtil;
import cn.qbs.wa.teach.common.core.domain.*;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.teach.exam.admin.pojo.exam.*;
import cn.qbs.wa.teach.exam.admin.service.ComExamService;
import cn.qbs.wa.teach.exam.admin.service.ExamService;
import cn.qbs.wa.teach.exam.admin.service.ExamineeRecordService;
import cn.qbs.wa.teach.exam.common.entity.Exam;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;


/**
 * 考试表(Exam)表控制层
 *
 * @author zcm
 * @since 2021-12-14 13:51:56
 */
@Slf4j
@RestController
@RequestMapping("exam")
public class ExamController {

    @Resource
    private ExamService examService;

    @Resource
    private ExamineeRecordService examineeRecordService;

    @Resource(name = "ComExamServiceImpl")
    private ComExamService comExamService;

    /**
     * 新增考试表
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("exam:add")
    //@Log(title = "新增考试表", businessType = BusinessType.INSERT)
    public R<Long> add(@RequestBody @Validated ExamAddRequest params) {
        return R.ok(this.examService.add(params));
    }


    /**
     * 新增考试表
     *
     * @param params
     * @return
     */
    @PostMapping("add/v2")
    //@RequiresPermissions("exam:add")
    @Log(title = "新增考试", businessType = BusinessType.INSERT)
    public R addV2(@RequestBody @Validated ExamAddRequest params) {
        this.comExamService.add(params);
        return R.ok();
    }


    /**
     * 分页查询考试表
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("exam:page")
    //@Log(title = "分页查询考试表", businessType = BusinessType.OTHER)
    public R<IPage<ExamPageResponse>> page(@RequestBody ExamPageRequest params) {
        return R.ok(this.examService.page(params));
    }

    /**
     * 分页查询考试表
     *
     * @param params
     * @return
     */
    @PostMapping("page/v2")
    //@RequiresPermissions("exam:page")
    @Log(title = "分页查询考试表", businessType = BusinessType.QUERY)
    public R<IPage<ExamPageResponseV2>> pageV2(@RequestBody ExamPageRequestV2 params) {
        return R.ok(this.examService.pageV2(params));
    }

    /**
     * 分页查询考试表
     *
     * @param params
     * @return
     */
    @PostMapping("page/v3")
    //@RequiresPermissions("exam:page")
    @Log(title = "分页查询考试表", businessType = BusinessType.QUERY)
    public R<IPage<ExamPageResponseV3>> pageV3(@RequestBody ExamPageRequestV3 params) {
        return R.ok(this.examService.pageV3(params));
    }

    /**
     * 分页查询指派用户的考试记录
     *
     * @param params
     * @return
     */
    @PostMapping("assignUserExamRecord/page")
    @ApiOperation("分页查询指派用户的考试记录")
    @Log(title = "分页查询指派用户的考试记录", businessType = BusinessType.QUERY)
    public R<IPage<ExamUserRecordPageResponse>> assignUserExamRecordPage(@RequestBody ExamUserRecordPageRequest params) {
        return R.ok(this.examService.assignUserExamRecordPage(params));
    }

    /**
     * 考试记录导出
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping("record/export")
    @ApiOperation("考试记录导出")
    @Log(title = "考试记录导出", businessType = BusinessType.EXPORT)
    public void examRecordExport(@RequestBody @Validated ExamUserRecordPageRequest request, HttpServletResponse response) throws IOException {
        Exam exam = this.examService.getById(request.getExamId());
        if (exam == null) {
            throw new ServiceException("考试不存在或已删除！");
        }

        String fileName = String.format("%s-%s.xlsx", exam.getExamName(), "考试记录");
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream");
        response.addHeader("fileName", encodedFileName);
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName);

        // 全部导出
        request.setSize(999);
        IPage<ExamUserRecordPageResponse> examRecordPage = this.examService.assignUserExamRecordPage(request);
        List<ExamUserRecordPageResponse> examRecordList = examRecordPage.getRecords();
        EasyExcel.write(response.getOutputStream()).sheet("考试记录").head(ExamUserRecordPageResponse.class).doWrite(examRecordList);
    }

    /**
     * 分页查询指派用户的考试记录
     *
     * @param params
     * @return
     */
    @PostMapping("examAssignUser/page")
    @ApiOperation("考试直接指派学员分页")
    @Log(title = "考试直接指派学员分页", businessType = BusinessType.QUERY)
    public R<PageResultComDTO<StudentDTO> > examAssignUserPage(@RequestBody ExamAssignUserPageRequest params) {
        return R.ok(this.examService.examAssignUserPage(params));
    }

    /**
     * 考试直接指派学员
     *
     * @param params
     * @return
     */
    @PostMapping("assignUser")
    @ApiOperation("考试直接指派学员")
    @Log(title = "考试直接指派学员", businessType = BusinessType.QUERY)
    public R<Boolean> assignUser(@RequestBody ExamAssignUserRequest params) {
        return R.ok(this.examService.assignUser(params));
    }

    /**
     * 查询考试表详情
     *
     * @param params
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("exam:details")
    @Log(title = "考试表详情", businessType = BusinessType.QUERY)
    public R<ExamDetailResponse> detail(@RequestBody @Validated IdRequest params) {
        return R.ok(this.examService.detail(params.getId()));
    }

    /**
     * 修改考试表
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("exam:update")
    //@Log(title = "更新考试表", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated ExamUpdateRequest params) {
        return R.ok(this.examService.update(params));
    }

    /**
     * 修改考试表
     *
     * @param params
     * @return
     */
    @PostMapping("update/v2")
    //@RequiresPermissions("exam:update")
    @Log(title = "更新考试", businessType = BusinessType.UPDATE)
    public R updateV2(@RequestBody ExamUpdateRequest params) {
        this.comExamService.update(params);
        return R.ok();
    }

    /**
     * 考试上架
     *
     * @param params
     * @return
     */
    @PostMapping("onShelf")
    //@RequiresPermissions("exam:onShelf")
    @Log(title = "考试上架", businessType = BusinessType.UPDATE)
    public R<Boolean> onShelf(@RequestBody @Validated IdListRequest params) {
        return R.ok(this.comExamService.release(params.getIdList(), true));
    }

    /**
     * 考试下架
     *
     * @param params
     * @return
     */
    @PostMapping("offShelf")
    //@RequiresPermissions("exam:offShelf")
    @Log(title = "考试下架", businessType = BusinessType.UPDATE)
    public R<Boolean> offShelf(@RequestBody @Validated IdListRequest params) {
        return R.ok(this.comExamService.revokeRelease(params.getIdList(), true));
    }

    /**
     * 启动考试
     *
     * @param request
     * @return
     */
    @PostMapping("start")
    //@RequiresPermissions("exam:start")
    //@Log(title = "启动考试", businessType = BusinessType.UPDATE)
    public R<Boolean> start(@RequestBody @Validated IdRequest request) {
        log.info("手动启动考试 {examId: {}}", request.getId());
        this.comExamService.start(request.getId(), true);
        return R.ok(true);
    }

    /**
     * 结束考试
     *
     * @param request
     * @return
     */
    @PostMapping("end")
    //@RequiresPermissions("exam:end")
    @Log(title = "结束考试", businessType = BusinessType.UPDATE)
    public R<Boolean> end(@RequestBody @Validated IdRequest request) {
        log.info("手动结束考试 {examId: {}}", request.getId());
        this.comExamService.end(request.getId(), true);
        return R.ok(true);
    }

    /**
     * 删除考试
     *
     * @param request
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("exam:delete")
    @Log(title = "删除考试", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody @Validated IdRequest request) {
        return R.ok(this.comExamService.delete(request.getId()));
    }

    /**
     * 启用停用考试
     *
     * @param request
     * @return
     */
    @PostMapping("enable")
    //@RequiresPermissions("exam:enable")
    @Log(title = "启用停用考试", businessType = BusinessType.UPDATE)
    public R<Boolean> enable(@RequestBody @Validated EnableRequest request) {
        return R.ok(this.examService.enable(request));
    }

    /**
     * 考试统计
     *
     * @param request
     * @return
     */
    @PostMapping("statistics")
    //@RequiresPermissions("exam:statistics")
    @Log(title = "考试统计", businessType = BusinessType.QUERY)
    public R<ExamStatistics> statistics(@RequestBody @Validated IdRequest request) {
        return R.ok(this.examService.statistics(request.getId()));
    }

    /**
     * 考试记录
     *
     * @param request
     * @return
     */
    @PostMapping("record")
    //@RequiresPermissions("exam:record")
    @Log(title = "考试记录", businessType = BusinessType.OTHER)
    public R<IPage<ExamRecord>> record(@RequestBody @Validated ExamRecordPageRequest request) {
        return R.ok(this.examineeRecordService.selectListByExamId(request));
    }

    /**
     * 考试排名
     *
     * @param request
     * @return
     */
    @PostMapping("ranking")
    //@RequiresPermissions("exam:ranking")
    @Log(title = "考试排名", businessType = BusinessType.QUERY)
    public R<IPage<ExamRanking>> ranking(@RequestBody @Validated ExamRecordPageRequest request) {
        return R.ok(this.examineeRecordService.rankingByExamId(request));
    }

    /**
     * 考试排名导出
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping("ranking/export")
//    @ApiIgnore
    @Log(title = "考试排名导出", businessType = BusinessType.EXPORT)
    public void rankingExport(@RequestBody @Validated ExamRecordPageRequest request, HttpServletResponse response) throws IOException {
        Exam exam = this.examService.getById(request.getExamId());
        if (exam == null) {
            throw new ServiceException("考试不存在或已删除！");
        }

        String fileName = String.format("%s-%s.xlsx", exam.getExamName(), "考试成绩排名");
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream");
        response.addHeader("fileName", encodedFileName);
        response.setHeader("Access-Control-Expose-Headers", "fileName");
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName);

        // 全部导出
        request.setSize(999);
        IPage<ExamRanking> examRankingPage = this.examineeRecordService.rankingByExamId(request);
        List<ExamRanking> examRankingList = examRankingPage.getRecords();
        examRankingList.forEach(i -> i.setMobile(DesensitizedUtil.mobilePhone(i.getMobile())));
        EasyExcel.write(response.getOutputStream()).sheet("考试排名").head(ExamRanking.class).doWrite(examRankingList);
    }

    /**
     * 试题正确率
     *
     * @param request
     * @return
     */
    @PostMapping("questionCorrectRate")
    //@RequiresPermissions("exam:questionCorrectRate")
    @Log(title = "试题正确率", businessType = BusinessType.QUERY)
    public R<QuestionCorrectRateWrap> questionCorrectRate(@RequestBody @Validated IdRequest request) {
        return R.ok(this.examineeRecordService.questionCorrectRateByExamId(request.getId()));
    }

    /**
     * 试题分析
     *
     * @param request
     * @return
     */
    @PostMapping("questionAnalysis")
    // 目前先和 后台管理/考试列表 绑定
    @RequiresPermissions("System:Education:Exam:List")
    @Log(title = "试题分析", businessType = BusinessType.QUERY)
    public R<QuestionAnalysisWrap> questionAnalysis(@RequestBody @Validated IdRequest request) {
        return R.ok(this.examineeRecordService.questionAnalysisByExamId(request.getId()));
    }

    /**
     * 考试记录详情
     *
     * @param request
     * @return
     */
    @PostMapping("record/details")
    // 目前先和 后台管理/阅卷管理 绑定
    @RequiresPermissions("System:Education:Exam:Marking")
    @Log(title = "考试记录详情", businessType = BusinessType.OTHER)
    public R<ExamRecordDetails> recordDetails(@RequestBody @Validated IdRequest request) {
        return R.ok(this.examineeRecordService.recordDetails(request.getId()));
    }

    /**
     * 删除考试记录
     *
     * @param request
     * @return
     */
    @PostMapping("record/delete")
    //@RequiresPermissions("exam:deleteRecord")
    @Log(title = "删除考试记录", businessType = BusinessType.DELETE)
    public R<Boolean> deleteRecord(@RequestBody @Validated IdRequest request) {
        return R.ok(this.examineeRecordService.deleteRecord(request.getId()));
    }

    /**
     * 自动批改
     *
     * @param request
     * @return
     */
    @PostMapping("autoCorrect")
    //@RequiresPermissions("exam:autoCorrect")
    @Log(title = "自动批改", businessType = BusinessType.UPDATE)
    public R<Boolean> autoCorrect(@RequestBody @Validated IdRequest request) {
        log.info("手动触发考试自动批改 {examId: {}}", request.getId());
        this.comExamService.autoCorrect(request.getId());
        return R.ok(true, "正在批改中，请稍候再查看结果");
    }

}

