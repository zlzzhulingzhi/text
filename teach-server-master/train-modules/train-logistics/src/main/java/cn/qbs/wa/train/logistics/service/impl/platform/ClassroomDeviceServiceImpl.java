package cn.qbs.wa.train.logistics.service.impl.platform;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.train.logistics.mapper.ClassroomDeviceMapper;
import cn.qbs.wa.train.logistics.entity.ClassroomDevice;
import cn.qbs.wa.train.logistics.service.platform.ClassroomDeviceService;
import cn.qbs.wa.train.logistics.pojo.classroomdevice.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 教室设施(ClassroomDevice)表服务实现类
 *
 * @author makejava
 * @since 2022-10-12 18:57:19
 */
@Slf4j
@Service("classroomDeviceService")
public class ClassroomDeviceServiceImpl extends ServiceImpl<ClassroomDeviceMapper, ClassroomDevice> implements ClassroomDeviceService {

    @Override
    public boolean add(ClassroomDeviceAddRequest params) {
        List<ClassroomDevice> classroomDeviceList=new ArrayList<>();
        if(params.getSceneDeviceId()!=null){
            for (Long sceneDeviceId:params.getSceneDeviceId()) {
                ClassroomDevice classroomDevice = new ClassroomDevice();
                classroomDevice.setClassroomId(params.getClassroomId());
                classroomDevice.setSceneDeviceId(sceneDeviceId);
                classroomDeviceList.add(classroomDevice);
            }
        }
        return this.saveBatch(classroomDeviceList);
    }

    @Override
    public IPage<ClassroomDevicePageResponse> page(ClassroomDevicePageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public ClassroomDeviceDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(ClassroomDeviceUpdateRequest params) {
        ClassroomDevicePageRequest classroomDevicePageRequest=new ClassroomDevicePageRequest();
        classroomDevicePageRequest.setClassroomId(params.getClassroomId());
        //查询教室设施
        List<ClassroomDevicePageResponse> classroomDevicePageResponseList=page(classroomDevicePageRequest).getRecords();
        if(params.getSceneDeviceId()!=null){
            if(classroomDevicePageResponseList==null){
                List<ClassroomDevice> classroomDeviceList=new ArrayList<>();
                for (Long sceneDeviceId:params.getSceneDeviceId()) {
                    ClassroomDevice classroomDevice = new ClassroomDevice();
                    classroomDevice.setClassroomId(params.getClassroomId());
                    classroomDevice.setSceneDeviceId(sceneDeviceId);
                    classroomDeviceList.add(classroomDevice);
                }
                return this.saveBatch(classroomDeviceList);
            }else {
                List<Long> idList =new ArrayList<>() ;
                for (ClassroomDevicePageResponse classroomDevicePageResponse: classroomDevicePageResponseList) {
                    idList.add(classroomDevicePageResponse.getId());
                }
                //删除原来教室设施
                deleteByIds(idList);
                List<ClassroomDevice> classroomDeviceList=new ArrayList<>();
                for (Long sceneDeviceId:params.getSceneDeviceId()) {
                    ClassroomDevice classroomDevice = new ClassroomDevice();
                    classroomDevice.setClassroomId(params.getClassroomId());
                    classroomDevice.setSceneDeviceId(sceneDeviceId);
                    classroomDeviceList.add(classroomDevice);
                }
                return this.saveBatch(classroomDeviceList);
            }
        }
        return true;
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

