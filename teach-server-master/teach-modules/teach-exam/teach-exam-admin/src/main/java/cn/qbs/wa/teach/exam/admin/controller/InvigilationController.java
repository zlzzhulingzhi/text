package cn.qbs.wa.teach.exam.admin.controller;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.admin.pojo.invigilation.TerminateExamRequest;
import cn.qbs.wa.teach.exam.admin.service.InvigilationService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 监考 Controller
 * @Author zcm
 * @Date 2022/1/25 10:08
 * @Version 1.0
 */
@RestController
@RequestMapping("/invigilation")
@RequiredArgsConstructor
public class InvigilationController {

    private final InvigilationService invigilationService;


    /**
     * 终止考试
     *
     * @param params
     * @return
     */
    @PostMapping("terminateExam")
    //@RequiresPermissions("invigilation:terminateExam")
    //@Log(title = "终止考试", businessType = BusinessType.UPDATE)
    public R<Boolean> terminateExam(@RequestBody @Validated TerminateExamRequest params) {
        return R.ok(this.invigilationService.terminateExam(params));
    }

}
