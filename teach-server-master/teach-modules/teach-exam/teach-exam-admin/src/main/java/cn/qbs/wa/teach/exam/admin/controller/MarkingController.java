package cn.qbs.wa.teach.exam.admin.controller;


import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.exam.admin.pojo.exam.*;
import cn.qbs.wa.teach.exam.admin.service.MarkingService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 规则控制层
 *
 * @author zcm
 * @version 1.0
 * @date 2021-12-15 14:42:49
 */
@RestController
@RequestMapping("marking")
@RequiredArgsConstructor
public class MarkingController {

    private final MarkingService markingService;

    /**
     * 阅卷分页查询
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("marking:page")
    @Log(title = "阅卷分页查询", businessType = BusinessType.QUERY)
    public R<IPage<MarkingPageResponse>> markingPage(@RequestBody MarkingPageRequest params) {
        return R.ok(this.markingService.markingPage(params));
    }

    /**
     * 阅卷-考试记录分页查询
     *
     * @param params
     * @return
     */
    @PostMapping("examRecordPage")
    //@RequiresPermissions("marking:examRecordPage")
    @Log(title = "阅卷-考试记录分页查询", businessType = BusinessType.QUERY)
    public R<IPage<MarkingExamRecordPageResponse>> examRecordPage(@RequestBody MarkingExamRecordPageRequest params) {
        return R.ok(this.markingService.examRecordPage(params));
    }

    /**
     * 批改
     * @param request
     * @return
     */
    @PostMapping("correct")
    //@RequiresPermissions("marking:correct")
    @Log(title = "批改", businessType = BusinessType.UPDATE)
    public R<Boolean> correct(@RequestBody @Validated CorrectRequest request) {
        return R.ok(this.markingService.correct(request));
    }

}

