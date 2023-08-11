package cn.qbs.wa.teach.exam.admin.controller;

import cn.qbs.wa.teach.common.core.domain.IdListAndUserIdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.admin.pojo.dto.orgdesk.OrgDeskTaskExamInfoDTO;
import cn.qbs.wa.teach.exam.admin.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;


/**
 * 考试表(Exam)表机构前台h5控制层
 *
 * @author WX
 * @since 2022-03-16 15:52:56
 */
@RestController
@RequestMapping("orgDeskTask")
@RequiredArgsConstructor
public class OrgDeskTaskExamController {

    @Resource
    private OrgDeskTaskExamService orgDeskTaskExamService;

    /**
     * 查询考试信息
     *
     * @param params
     * @return
     */
    @PostMapping("getExamList")
    public R<List<OrgDeskTaskExamInfoDTO>> getExamList(@RequestBody @Validated IdListAndUserIdRequest params) {
        return R.ok(orgDeskTaskExamService.getExamList(params));
    }

}

