package cn.qbs.wa.teach.exam.api.factory;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.api.RemoteExamRuleService;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @Author zcm
 * @Date 2021/11/26 16:23
 * @Version 1.0
 */
public class RemoteExamRuleFallbackFactory implements FallbackFactory<RemoteExamRuleService> {

    @Override
    public RemoteExamRuleService create(Throwable cause) {
        return new RemoteExamRuleService() {
            @Override
            public R addRulesToOrg(IdRequest request) {
                return R.fail("服务暂无法访问");
            }
        };
    }

}
