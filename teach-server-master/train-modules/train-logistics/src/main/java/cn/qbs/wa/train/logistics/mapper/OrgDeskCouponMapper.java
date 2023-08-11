package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.Dept;
import cn.qbs.wa.train.logistics.pojo.orgbackcoupon.OrgDeptOrRoleResponseDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.io.Serializable;
import java.util.List;

/**
 * 机构前台优惠券所用的远程接口数据库访问层
 *
 * @author WX
 * @since 2022-03-23
 */
public interface OrgDeskCouponMapper extends BaseMapper<Dept> {

    List<OrgDeptOrRoleResponseDTO> getOrgDept(Serializable id);

}

