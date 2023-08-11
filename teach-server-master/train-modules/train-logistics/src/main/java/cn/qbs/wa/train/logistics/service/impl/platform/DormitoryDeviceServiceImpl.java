package cn.qbs.wa.train.logistics.service.impl.platform;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.train.logistics.entity.DormitoryDevice;
import cn.qbs.wa.train.logistics.mapper.DormitoryDeviceMapper;
import cn.qbs.wa.train.logistics.entity.DormitoryDevice;
import cn.qbs.wa.train.logistics.pojo.dormitorydevice.DormitoryDevicePageRequest;
import cn.qbs.wa.train.logistics.pojo.dormitorydevice.DormitoryDevicePageResponse;
import cn.qbs.wa.train.logistics.service.platform.DormitoryDeviceService;
import cn.qbs.wa.train.logistics.pojo.dormitorydevice.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 宿舍设施(DormitoryDevice)表服务实现类
 *
 * @author makejava
 * @since 2022-10-13 09:48:04
 */
@Slf4j
@Service("dormitoryDeviceService")
public class DormitoryDeviceServiceImpl extends ServiceImpl<DormitoryDeviceMapper, DormitoryDevice> implements DormitoryDeviceService {

    @Override
    public boolean add(DormitoryDeviceAddRequest params) {
        List<DormitoryDevice> dormitoryDeviceList=new ArrayList<>();
        if(params.getSceneDeviceId()!=null){
            for (Long sceneDeviceId:params.getSceneDeviceId()) {
                DormitoryDevice dormitoryDevice = new DormitoryDevice();
                dormitoryDevice.setDormitoryId(params.getDormitoryId());
                dormitoryDevice.setSceneDeviceId(sceneDeviceId);
                dormitoryDeviceList.add(dormitoryDevice);
            }
        }
        return this.saveBatch(dormitoryDeviceList);
    }

    @Override
    public IPage<DormitoryDevicePageResponse> page(DormitoryDevicePageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public DormitoryDeviceDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(DormitoryDeviceUpdateRequest params) {
        DormitoryDevicePageRequest dormitoryDevicePageRequest=new DormitoryDevicePageRequest();
        dormitoryDevicePageRequest.setDormitoryId(params.getDormitoryId());
        //查询宿舍设施
        List<DormitoryDevicePageResponse> dormitoryDevicePageResponseList=page(dormitoryDevicePageRequest).getRecords();
        if(params.getSceneDeviceId()!=null){
            if(dormitoryDevicePageResponseList==null){
                List<DormitoryDevice> dormitoryDeviceList=new ArrayList<>();
                for (Long sceneDeviceId:params.getSceneDeviceId()) {
                    DormitoryDevice dormitoryDevice = new DormitoryDevice();
                    dormitoryDevice.setDormitoryId(params.getDormitoryId());
                    dormitoryDevice.setSceneDeviceId(sceneDeviceId);
                    dormitoryDeviceList.add(dormitoryDevice);
                }
                return this.saveBatch(dormitoryDeviceList);
            }else {
                List<Long> idList =new ArrayList<>() ;
                for (DormitoryDevicePageResponse dormitoryDevicePageResponse: dormitoryDevicePageResponseList) {
                    idList.add(dormitoryDevicePageResponse.getId());
                }
                //删除原来宿舍设施
                deleteByIds(idList);
                List<DormitoryDevice> dormitoryDeviceList=new ArrayList<>();
                for (Long sceneDeviceId:params.getSceneDeviceId()) {
                    DormitoryDevice dormitoryDevice = new DormitoryDevice();
                    dormitoryDevice.setDormitoryId(params.getDormitoryId());
                    dormitoryDevice.setSceneDeviceId(sceneDeviceId);
                    dormitoryDeviceList.add(dormitoryDevice);
                }
                return this.saveBatch(dormitoryDeviceList);
            }
        }
        return true;
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

