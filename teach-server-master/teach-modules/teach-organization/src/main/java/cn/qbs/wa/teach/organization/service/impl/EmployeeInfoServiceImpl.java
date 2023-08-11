package cn.qbs.wa.teach.organization.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.organization.mapper.EmployeeInfoMapper;
import cn.qbs.wa.teach.organization.entity.EmployeeInfo;
import cn.qbs.wa.teach.organization.service.EmployeeInfoService;
import cn.qbs.wa.teach.organization.pojo.employeeinfo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 职工信息(EmployeeInfo)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-01-21 11:30:04
 */
@Slf4j
@Service("employeeInfoService")
public class EmployeeInfoServiceImpl extends ServiceImpl<EmployeeInfoMapper, EmployeeInfo> implements EmployeeInfoService {

    @Override
    public boolean add(EmployeeInfoAddRequest params) {
        EmployeeInfo employeeInfo = new EmployeeInfo();
        BeanUtils.copyProperties(params, employeeInfo);
        return this.save(employeeInfo);
    }

    @Override
    public IPage<EmployeeInfoPageResponse> page(EmployeeInfoPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public EmployeeInfoDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(EmployeeInfoUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        EmployeeInfo employeeInfo = new EmployeeInfo();
        BeanUtils.copyProperties(params, employeeInfo);
        return this.updateById(employeeInfo);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }
    
}

