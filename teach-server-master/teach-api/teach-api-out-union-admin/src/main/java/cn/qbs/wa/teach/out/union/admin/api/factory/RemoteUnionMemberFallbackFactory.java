package cn.qbs.wa.teach.out.union.admin.api.factory;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.out.union.admin.api.DTO.*;
import cn.qbs.wa.teach.out.union.admin.api.RemoteUnionMemberService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RemoteUnionMemberFallbackFactory implements FallbackFactory<RemoteUnionMemberService> {

    @Override
    public RemoteUnionMemberService create(Throwable cause) {
        return new RemoteUnionMemberService() {
            @Override
            public R<UniMemberDTO> info(IdRequest idRequest) {
                return null;
            }

            @Override
            public R<UniMemberDTO> register(UniMemberAddDTO params) {
                return null;
            }

            @Override
            public R<List<UniMemberDTO>> list(IdListRequest request) {
                return null;
            }

            @Override
            public R<List<UniMemberDTO>> getByNameOrPhone(UniMemberTCourseStudentRequestDTO params) {
                return null;
            }

            @Override
            public R<Long> getCount(UniMemberPageRequestDTO request) {
                return null;
            }

            @Override
            public R<PageResultComDTO<UniMemberDTO>> query(UniMemberQueryDTO uniMemberDTO) {
                return null;
            }
        };
    }
}
