package cn.qbs.wa.union.auth.mapper;

import cn.qbs.wa.union.auth.entity.UniUserMiniapp;
import cn.qbs.wa.union.auth.pojo.uniorg.UniOrg;
import cn.qbs.wa.union.auth.pojo.uniuser.UniUserDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 统一用户绑定小程序(UniUserMiniapp)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-21 08:35:17
 */
public interface UniUserMiniappMapper extends BaseMapper<UniUserMiniapp> {

    UniUserDTO sysUserInfoByOpenId(String openId);

    UniUserDTO orgUserInfoByOpenId(String openId);

    UniUserMiniapp sysUserInfoByPhone(String phone);

    UniUserMiniapp orgUserInfoByPhone(String phone);

    UniOrg getOrgInfo(Long orgId);
}

