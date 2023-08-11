package cn.qbs.wa.union.admin.mapper;

import cn.qbs.wa.union.admin.entity.UniMember;
import cn.qbs.wa.union.admin.pojo.unimember.UniMemberPageRequest;
import cn.qbs.wa.union.admin.pojo.unimember.UniMemberResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

/**
 * 统一会员用户表(UniMember)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-07-21 09:11:24
 */
public interface UniMemberMapper extends BaseMapper<UniMember> {

    IPage<UniMemberResponse> page(@Param("page") IPage<?> page, @Param("params") UniMemberPageRequest params);
}

