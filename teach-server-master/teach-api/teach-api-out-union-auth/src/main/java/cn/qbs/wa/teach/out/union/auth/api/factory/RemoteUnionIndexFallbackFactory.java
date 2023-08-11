package cn.qbs.wa.teach.out.union.auth.api.factory;


import cn.qbs.wa.teach.out.union.auth.api.RemoteUnionIndexService;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:03
 */
public class RemoteUnionIndexFallbackFactory implements FallbackFactory<RemoteUnionIndexService> {


    @Override
    public RemoteUnionIndexService create(Throwable cause) {
        return null;
    }
}
