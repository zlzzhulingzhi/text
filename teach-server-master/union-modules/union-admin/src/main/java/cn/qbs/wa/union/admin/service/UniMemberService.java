package cn.qbs.wa.union.admin.service;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.union.admin.entity.UniMember;
import cn.qbs.wa.union.admin.pojo.unimember.UniMemberPageRequest;
import cn.qbs.wa.union.admin.pojo.unimember.UniMemberRegisterRequest;
import cn.qbs.wa.union.admin.pojo.unimember.UniMemberRegisterResponse;
import cn.qbs.wa.union.admin.pojo.unimember.UniMemberResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 统一会员用户表(UniMember)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-07-21 09:11:24
 */
public interface UniMemberService extends IService<UniMember> {

    /**
     * 注册内部会员用户表
     */
    UniMemberRegisterResponse registerFromInner(UniMemberRegisterRequest params);

    UniMember register(UniMemberRegisterRequest params);

    Boolean changePhone(Long memberId, String phone);

    IPage<UniMemberResponse> page(UniMemberPageRequest params);

    IPage<UniMemberResponse>pageStudent(UniMemberPageRequest params);
}

