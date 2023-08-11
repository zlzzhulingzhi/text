package cn.qbs.wa.teach.out.union.admin.api.factory;


import cn.qbs.wa.teach.out.union.admin.api.RemoteUnionSystemEmployeeUserService;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:03
 */
public class RemoteSystemEmployeeUserFallbackFactory implements FallbackFactory<RemoteUnionSystemEmployeeUserService> {


    @Override
    public RemoteUnionSystemEmployeeUserService create(Throwable cause) {
        return null;
    }
}
