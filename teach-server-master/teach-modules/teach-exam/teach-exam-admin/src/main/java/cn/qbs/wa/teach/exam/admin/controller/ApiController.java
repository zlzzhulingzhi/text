package cn.qbs.wa.teach.exam.admin.controller;


import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.admin.pojo.api.ApiExamPageRequest;
import cn.qbs.wa.teach.exam.admin.pojo.api.ApiExamPageResponse;
import cn.qbs.wa.teach.exam.admin.pojo.api.ApiExamineeeListRequest;
import cn.qbs.wa.teach.exam.admin.pojo.api.ApiExamineeeListResponse;
import cn.qbs.wa.teach.exam.admin.service.ApiService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 考试表(Exam)表控制层
 *
 * @author zcm
 * @since 2021-12-14 13:51:56
 */
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ApiController {

    private final ApiService apiService;


    /**
     * 分页查询考试
     *
     * @param params
     * @return
     */
    @PostMapping("exam/page")
    //@RequiresPermissions("api:exam:page")
    //@Log(title = "分页查询考试", businessType = BusinessType.OTHER)
    public R<IPage<ApiExamPageResponse>> page(@RequestBody ApiExamPageRequest params) {
        IPage<ApiExamPageResponse> examPage = apiService.examPage(params);
        return R.ok(examPage);
    }

    /**
     * 查询考试考生列表
     *
     * @param params
     * @return
     */
    @PostMapping("exam/examineeList")
    //@RequiresPermissions("api:exam:examineeList")
    //@Log(title = "查询考试考生列表", businessType = BusinessType.OTHER)
    public R<List<ApiExamineeeListResponse>> examineeList(@RequestBody ApiExamineeeListRequest params) {
        List<ApiExamineeeListResponse> examineeList = apiService.examineeList(params);
        return R.ok(examineeList);
    }

}

