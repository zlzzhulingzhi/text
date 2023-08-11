package cn.qbs.wa.teach.course.api.factory;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.RemoteWCourseService;
import cn.qbs.wa.teach.course.api.pojo.DTO.wcourse.*;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */

@Component
public class RemoteWCourseFallbackFactory implements FallbackFactory<RemoteWCourseService> {


    @Override
    public RemoteWCourseService create(Throwable cause) {
        return new RemoteWCourseService() {
            @Override
            public R<PageResultComDTO<WCoursePageResultDTO>> page(WCoursePageSearchDTO params) {
                return null;
            }

            @Override
            public R<PageResultComDTO<WCoursePageResultDTO>> search(WCoursePageSearchDTO params) {
                return null;
            }

            @Override
            public R<PageResultComDTO<WCoursePageByChildResultDTO>> pageByChild(WCoursePageSearchDTO params) {
                return null;
            }

            @Override
            public R add(WCourseAddDTO params) {
                return null;
            }

            @Override
            public R delete(IdListRequest request) {
                return null;
            }

            @Override
            public R<Boolean> update(WCourseUpdateDTO params) {
                return null;
            }
        };
    }
}
