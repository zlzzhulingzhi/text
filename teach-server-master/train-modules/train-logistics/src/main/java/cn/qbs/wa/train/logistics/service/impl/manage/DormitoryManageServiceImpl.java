package cn.qbs.wa.train.logistics.service.impl.manage;

import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.train.basic.api.RemoteTrainDictService;
import cn.qbs.wa.train.basic.api.pojo.DTO.dict.DictPageRequestDTO;
import cn.qbs.wa.train.logistics.entity.Dormitory;
import cn.qbs.wa.train.logistics.enums.BuldingFloorRoomTypeEnum;
import cn.qbs.wa.train.logistics.mapper.DormitoryMapper;
import cn.qbs.wa.train.logistics.pojo.dormitory.*;
import cn.qbs.wa.train.logistics.service.manage.DormitoryManageService;
import cn.qbs.wa.train.logistics.service.platform.DormitoryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 宿舍表(Dormitory)表服务实现类
 *
 * @author makejava
 * @since 2022-10-08 17:39:59
 */
@Slf4j
@Service("dormitoryManageService")
public class DormitoryManageServiceImpl extends ServiceImpl<DormitoryMapper, Dormitory> implements DormitoryManageService {

    @Resource
    RemoteTrainDictService remoteTrainDictService;

    @Override
    public IPage<DormitoryPageResponse> page(DormitoryPageRequest params) {
        IPage<DormitoryPageResponse> dormitoryPageResponseIPage= baseMapper.page(params.createMpPage(), params);
        List<DormitoryPageResponse> dormitoryPageResponseList= dormitoryPageResponseIPage.getRecords();
        for (DormitoryPageResponse dormitoryPageResponse:dormitoryPageResponseList) {
            DictPageRequestDTO dictPageRequestDTO=new DictPageRequestDTO();
           if(dormitoryPageResponse.getBuilding()!=null){
                dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.BULDING.getName());
                dictPageRequestDTO.setDictKey(dormitoryPageResponse.getBuilding());
                //根据单元字典码和字典值获取字典名称
                String building=remoteTrainDictService.getDictValue(dictPageRequestDTO).getRemoteData();
               dormitoryPageResponse.setBuilding(building);
            }
            if(dormitoryPageResponse.getFloor()!=null){
                dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.FLOOR.getName());
                dictPageRequestDTO.setDictKey(dormitoryPageResponse.getFloor());
                //根据楼层字典码和字典值获取字典名称
                String floor=remoteTrainDictService.getDictValue(dictPageRequestDTO).getRemoteData();
                dormitoryPageResponse.setFloor(floor);
            }
            if(dormitoryPageResponse.getRoomType()!=null){
                dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.DORMITORY.getName());
                dictPageRequestDTO.setDictKey(dormitoryPageResponse.getRoomType());
                //根据类别字典码和字典值获取字典名称
                String roomType=remoteTrainDictService.getDictValue(dictPageRequestDTO).getRemoteData();
                dormitoryPageResponse.setRoomType(roomType);
            }
        }
        return dormitoryPageResponseIPage;
    }
    @Override
    public DormitoryDetailResponse detail(Long id) {
        DormitoryDetailResponse dormitoryDetailResponse=baseMapper.selectDetailById(id);
        /*DormitoryAttachPageRequest dormitoryAttachPageRequest=new DormitoryAttachPageRequest();
        dormitoryAttachPageRequest.setDormitoryId(id);
        //查询宿舍附件
        IPage<DormitoryAttachPageResponse> dormitoryAttachPageResponseIPage=dormitoryAttachService.page(dormitoryAttachPageRequest);
        if(!dormitoryAttachPageResponseIPage.getRecords().isEmpty()){
            List<DormitoryAttachPageResponse> dormitoryDevicePageResponseList=dormitoryAttachPageResponseIPage.getRecords();
            for (DormitoryAttachPageResponse dormitoryAttachPageResponse: dormitoryDevicePageResponseList) {
                dormitoryDetailResponse.setFileType(dormitoryAttachPageResponse.getFileType());
                if(dormitoryAttachPageResponse.getFileUrl()!=null){
                    dormitoryDetailResponse.setFileUrl(dormitoryAttachPageResponse.getFileUrl().split(","));
                }
            }
        }
        DormitoryDevicePageRequest dormitoryDevicePageRequest=new DormitoryDevicePageRequest();
        dormitoryDevicePageRequest.setDormitoryId(id);
        //查询教室设施
        IPage<DormitoryDevicePageResponse> dormitoryDevicePageResponseIPage=dormitoryDeviceService.page(dormitoryDevicePageRequest);
        if(!dormitoryDevicePageResponseIPage.getRecords().isEmpty()){
            List<DormitoryDevicePageResponse>  dormitoryDevicePageResponseList=dormitoryDevicePageResponseIPage.getRecords();
            String deviceName= "";
            for (DormitoryDevicePageResponse dormitoryDevicePageResponse:dormitoryDevicePageResponseList) {
                //查询场景设备
                SceneDeviceDetailResponse sceneDeviceDetailResponse=sceneDeviceService.detail(dormitoryDevicePageResponse.getSceneDeviceId());
                deviceName= deviceName + ","+sceneDeviceDetailResponse.getDeviceName();
            }
            dormitoryDetailResponse.setDeviceName(deviceName.substring(Constants.DB_TRUE));
        }*/
        DictPageRequestDTO dictPageRequestDTO=new DictPageRequestDTO();
        if(dormitoryDetailResponse.getBuilding()!=null){
            dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.BULDING.getName());
            dictPageRequestDTO.setDictKey(dormitoryDetailResponse.getBuilding());
            //根据单元字典码和字典值获取字典名称
            String building=remoteTrainDictService.getDictValue(dictPageRequestDTO).getRemoteData();
            dormitoryDetailResponse.setBuilding(building);
        }
        if(dormitoryDetailResponse.getFloor()!=null){
            dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.FLOOR.getName());
            dictPageRequestDTO.setDictKey(dormitoryDetailResponse.getFloor());
            //根据楼层字典码和字典值获取字典名称
            String floor=remoteTrainDictService.getDictValue(dictPageRequestDTO).getRemoteData();
            dormitoryDetailResponse.setFloor(floor);
        }
        if(dormitoryDetailResponse.getRoomType()!=null){
            dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.DORMITORY.getName());
            dictPageRequestDTO.setDictKey(dormitoryDetailResponse.getRoomType());
            //根据类别字典码和字典值获取字典名称
            String roomType=remoteTrainDictService.getDictValue(dictPageRequestDTO).getRemoteData();
            dormitoryDetailResponse.setRoomType(roomType);
        }
        /*if(FileTypeEnum.pic.getCode().equals(dormitoryDetailResponse.getFileType())){
            dormitoryDetailResponse.setFileType(FileTypeEnum.pic.getName());
        }
        if(FileTypeEnum.doc.getCode().equals(dormitoryDetailResponse.getFileType())){
            dormitoryDetailResponse.setFileType(FileTypeEnum.doc.getName());
        }*/
        return dormitoryDetailResponse;
    }

}

