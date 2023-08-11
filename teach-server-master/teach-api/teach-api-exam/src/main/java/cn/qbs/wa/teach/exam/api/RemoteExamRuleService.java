package cn.qbs.wa.teach.exam.api;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.api.factory.RemoteExamRuleFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author zcm
 * @Date 2021/11/26 16:21
 * @Version 1.0
 */
@FeignClient(contextId = "remoteExamRuleService", name = "teach-exam-admin", path = "/exam/admin/rule", fallbackFactory = RemoteExamRuleFallbackFactory.class)
public interface RemoteExamRuleService {

    /**
     * 为机构添加考试规则
     * @param request
     * @return
     */
    @PostMapping("/addRulesToOrg")
    R addRulesToOrg(@RequestBody IdRequest request);

}
