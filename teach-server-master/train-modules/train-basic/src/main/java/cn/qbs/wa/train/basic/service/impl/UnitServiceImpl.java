package cn.qbs.wa.train.basic.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniMemberPageRequestDTO;
import cn.qbs.wa.teach.out.union.admin.api.RemoteUnionMemberService;
import cn.qbs.wa.train.basic.entity.Unit;
/*import cn.qbs.wa.train.basic.entity.UnitStaff;*/
import cn.qbs.wa.train.basic.mapper.UnitMapper;
/*import cn.qbs.wa.train.basic.mapper.UnitStaffMapper;*/
import cn.qbs.wa.train.basic.pojo.unit.*;
import cn.qbs.wa.train.basic.service.UnitService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 单位表(Unit)表服务实现类
 *
 * @author makejava
 * @since 2022-09-29 08:31:21
 */
@Slf4j
@Service("unitService")
public class UnitServiceImpl extends ServiceImpl<UnitMapper, Unit> implements UnitService {

    /*@Autowired
    private UnitStaffMapper unitStaffMapper;*/
    @Autowired
    RemoteUnionMemberService remoteUnionMemberService;

    @Override
    public boolean add(UnitAddRequest params) {
        Long count = baseMapper.selectCount(Wrappers.<Unit>lambdaQuery().eq(Unit::getName, params.getName()));
        if(count>Constants.DB_FAIL){
            throw new ServiceException("已有同名单位");
        }
        if (StrUtil.isNotBlank(params.getContactNumber())) {
            params.setContactNumber(AesUtil.encode(params.getContactNumber()));
        }
        Unit unit = new Unit();
        BeanUtils.copyProperties(params, unit);
        return this.save(unit);
    }

    @Override
    public IPage<UnitPageResponse> page(UnitPageRequest params) {
        IPage<UnitPageResponse> unitPageResponseIPage=baseMapper.page(params.createMpPage(), params);
        for (UnitPageResponse unitPageResponse:unitPageResponseIPage.getRecords()) {
            if (StrUtil.isNotBlank(unitPageResponse.getContactNumber())) {
                String phone1=AesUtil.decode(unitPageResponse.getContactNumber());
                String phone2=phone1.substring(0,3)+"****"+phone1.substring(phone1.length()-4,phone1.length());
                unitPageResponse.setContactNumber(phone2);
            }
        }
        return unitPageResponseIPage;
    }

    @Override
    public UnitDetailResponse detail(Serializable id) {
        UnitDetailResponse unitDetailResponse=baseMapper.selectDetailById(id);
        if (StrUtil.isNotBlank(unitDetailResponse.getContactNumber())) {
            unitDetailResponse.setContactNumber(AesUtil.decode(unitDetailResponse.getContactNumber()));
        }
        return unitDetailResponse;
    }

    @Override
    public boolean update(UnitUpdateRequest params) {
        Unit unit=baseMapper.selectById(params.getId());
        Long count = baseMapper.selectCount(Wrappers.<Unit>lambdaQuery().eq(Unit::getName, params.getName()));
        if(!unit.getName().equals(params.getName()) && count>Constants.DB_FAIL){
            throw new ServiceException("已有同名单位");
        }
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        if (StrUtil.isNotBlank(params.getContactNumber())) {
            params.setContactNumber(AesUtil.encode(params.getContactNumber()));
        }
        BeanUtils.copyProperties(params, unit);
        return this.updateById(unit);
    }

    @Override
    public boolean updateBatch(List<UnitUpdateRequest> params) {
        List<Unit> unitList=new ArrayList<>();
        for (UnitUpdateRequest param: params) {
            if (param.getId() == null) {
                throw new IllegalParamsException("ID不能为空！");
            }
            Unit unit = new Unit();
            BeanUtils.copyProperties(param, unit);
            unitList.add(unit);
        }

        return this.updateBatchById(unitList);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        for (Long id: idList) {
            UniMemberPageRequestDTO uniMemberPageRequestDTO=new UniMemberPageRequestDTO();
            uniMemberPageRequestDTO.setUnitId(id);
            Long count=remoteUnionMemberService.getCount(uniMemberPageRequestDTO).getRemoteData();
            if (count>Constants.DB_FAIL){
                throw new ServiceException("有单位职员的单位无法删除");
            }
            /*Long count = unitStaffMapper.selectCount(Wrappers.<UnitStaff>lambdaQuery().eq(UnitStaff::getUnitId,id));
            if (count>Constants.DB_FAIL){
                throw new ServiceException("有单位职员的单位无法删除");
            }*/
        }
        return this.removeByIds(idList);
    }

}

