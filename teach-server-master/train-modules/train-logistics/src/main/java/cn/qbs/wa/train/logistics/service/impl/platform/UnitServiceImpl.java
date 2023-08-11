package cn.qbs.wa.train.logistics.service.impl.platform;

import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.train.logistics.mapper.UnitMapper;
import cn.qbs.wa.train.logistics.entity.Unit;
import cn.qbs.wa.train.logistics.pojo.unitstaff.UnitStaffPageRequest;
import cn.qbs.wa.train.logistics.pojo.unitstaff.UnitStaffPageResponse;
import cn.qbs.wa.train.logistics.service.platform.UnitService;
import cn.qbs.wa.train.logistics.pojo.unit.*;
import cn.qbs.wa.train.logistics.service.platform.UnitStaffService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

    @Autowired
    private UnitStaffService unitStaffService;

    @Override
    public boolean add(UnitAddRequest params) {
        Unit unit = new Unit();
        BeanUtils.copyProperties(params, unit);
        return this.save(unit);
    }

    @Override
    public IPage<UnitPageResponse> page(UnitPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public List<Unit> listUnit() {
        List<Unit> unitList=list(new LambdaQueryWrapper<>());
        return unitList;
    }

    @Override
    public UnitDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(UnitUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        Unit unit = new Unit();
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
        UnitStaffPageRequest unitStaffPageRequest=new UnitStaffPageRequest();
        for (Long id: idList) {
            unitStaffPageRequest.setUnitId(id);
            List<UnitStaffPageResponse> unitStaffPageResponse=unitStaffService.page(unitStaffPageRequest).getRecords();
            if (!Constants.DB_FAIL.equals(unitStaffPageResponse.size())){
                throw new ServiceException("有单位职员的单位无法删除");
            }
        }
        return this.removeByIds(idList);
    }

}

