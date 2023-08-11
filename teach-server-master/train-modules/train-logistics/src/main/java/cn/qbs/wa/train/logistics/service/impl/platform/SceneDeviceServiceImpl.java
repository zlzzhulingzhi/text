package cn.qbs.wa.train.logistics.service.impl.platform;

import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.train.logistics.entity.Classroom;
import cn.qbs.wa.train.logistics.mapper.SceneDeviceMapper;
import cn.qbs.wa.train.logistics.entity.SceneDevice;
import cn.qbs.wa.train.logistics.pojo.classroomattach.ClassroomAttachPageRequest;
import cn.qbs.wa.train.logistics.pojo.classroomattach.ClassroomAttachPageResponse;
import cn.qbs.wa.train.logistics.pojo.classroomdevice.ClassroomDevicePageRequest;
import cn.qbs.wa.train.logistics.pojo.classroomdevice.ClassroomDevicePageResponse;
import cn.qbs.wa.train.logistics.service.platform.ClassroomDeviceService;
import cn.qbs.wa.train.logistics.service.platform.SceneDeviceService;
import cn.qbs.wa.train.logistics.pojo.scenedevice.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 场景设备(SceneDevice)表服务实现类
 *
 * @author makejava
 * @since 2022-10-12 19:03:49
 */
@Slf4j
@Service("sceneDeviceService")
public class SceneDeviceServiceImpl extends ServiceImpl<SceneDeviceMapper, SceneDevice> implements SceneDeviceService {

    @Resource
    ClassroomDeviceService classroomDeviceService;

    @Override
    public Boolean add(SceneDeviceAddRequest params) {
        List<SceneDevice> sceneDeviceList=this.lambdaQuery().eq(SceneDevice::getDeviceName,params.getDeviceName()).list();
        if(sceneDeviceList!=null && !sceneDeviceList.isEmpty()){
            throw new ServiceException("已有同名设备");
        }
        SceneDevice sceneDevice = new SceneDevice();
        BeanUtils.copyProperties(params, sceneDevice);
        return this.save(sceneDevice);
    }

    @Override
    public IPage<SceneDevicePageResponse> page(SceneDevicePageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public SceneDeviceDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(SceneDeviceUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        List<SceneDevice> sceneDeviceList=this.lambdaQuery().eq(SceneDevice::getDeviceName,params.getDeviceName()).list();
        if(sceneDeviceList!=null && !sceneDeviceList.isEmpty() && !params.getId().equals(sceneDeviceList.get(Constants.DB_FAIL).getId())){
            throw new ServiceException("已有同名设备");
        }
        SceneDevice sceneDevice = new SceneDevice();
        BeanUtils.copyProperties(params, sceneDevice);
        return this.updateById(sceneDevice);
    }

    @Override
    public boolean updateBatch(SceneDeviceUpdateBatchRequest params) {
        List<SceneDevice> sceneDevices=new ArrayList<>();
        for (Long id : params.getIds()) {
            if (id == null) {
                throw new IllegalParamsException("ID不能为空！");
            }
            SceneDevice sceneDevice = new SceneDevice();
            sceneDevice.setId(id);
            sceneDevice.setEnabled(params.getEnabled());
            sceneDevices.add(sceneDevice);
        }
        return this.updateBatchById(sceneDevices);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        for (Long id:idList) {
            ClassroomDevicePageRequest classroomDevicePageRequest=new ClassroomDevicePageRequest();
            classroomDevicePageRequest.setSceneDeviceId(id);
            IPage<ClassroomDevicePageResponse> classroomDevicePageResponseIPage=classroomDeviceService.page(classroomDevicePageRequest);
            if (!classroomDevicePageResponseIPage.getRecords().isEmpty()){
                throw new ServiceException("请先在教室删除设备再到列表来删除设备");
            }
        }
        return this.removeByIds(idList);
    }

    @Override
    public  List<SceneDevice> listSceneDevice(SceneDevicePageRequest request) {
        List<SceneDevice> sceneDeviceList=list(new LambdaQueryWrapper<SceneDevice>().eq(SceneDevice::getCategory, request.getCategory()).
                eq(SceneDevice::getEnabled, Constants.DB_TRUE).orderByAsc(SceneDevice::getSort));
        return sceneDeviceList;
    }

}

