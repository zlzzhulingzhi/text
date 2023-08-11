package cn.qbs.wa.teach.exam.admin.service;

import cn.qbs.wa.teach.exam.admin.pojo.api.ApiExamPageRequest;
import cn.qbs.wa.teach.exam.admin.pojo.api.ApiExamPageResponse;
import cn.qbs.wa.teach.exam.admin.pojo.api.ApiExamineeeListRequest;
import cn.qbs.wa.teach.exam.admin.pojo.api.ApiExamineeeListResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @Author zcm
 * @Date 2022/1/19 14:16
 * @Version 1.0
 */
public interface ApiService {

    IPage<ApiExamPageResponse> examPage(ApiExamPageRequest params);

    List<ApiExamineeeListResponse> examineeList(ApiExamineeeListRequest params);

}
