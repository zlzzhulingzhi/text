package cn.qbs.wa.train.logistics.service.impl.platform;

import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.train.basic.api.RemoteTrainDictService;
import cn.qbs.wa.train.basic.api.pojo.DTO.dict.DictPageRequestDTO;
import cn.qbs.wa.train.logistics.entity.*;
import cn.qbs.wa.train.logistics.enums.BuldingFloorRoomTypeEnum;
import cn.qbs.wa.train.logistics.enums.FileTypeEnum;
import cn.qbs.wa.train.logistics.enums.SceneDeviceCategoryEnum;
import cn.qbs.wa.train.logistics.mapper.DormitoryMapper;
import cn.qbs.wa.train.logistics.entity.Dormitory;
import cn.qbs.wa.train.logistics.mapper.DormitoryScheduleMapper;
import cn.qbs.wa.train.logistics.pojo.classroom.ClassroomPageRequest;
import cn.qbs.wa.train.logistics.pojo.classroom.ClassroomPageResponse;
import cn.qbs.wa.train.logistics.pojo.classroomschedule.ClassroomScheduleDetailResponse;
import cn.qbs.wa.train.logistics.pojo.dormitory.DormitoryPageRequest;

import cn.qbs.wa.train.logistics.pojo.dormitoryschedule.DormitoryScheduleDetailResponse;
import cn.qbs.wa.train.logistics.pojo.dormitoryschedule.UseDateStateCount;
import cn.qbs.wa.train.logistics.service.platform.DormitoryService;
import cn.qbs.wa.train.logistics.pojo.dormitory.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 宿舍表(Dormitory)表服务实现类
 *
 * @author makejava
 * @since 2022-10-08 17:39:59
 */
@Slf4j
@Service("dormitoryService")
public class DormitoryServiceImpl extends ServiceImpl<DormitoryMapper, Dormitory> implements DormitoryService {

    @Resource
    RemoteTrainDictService remoteTrainDictService;
    @Resource
    DormitoryScheduleMapper dormitoryScheduleMapper;

    @Override
    public boolean add(DormitoryAddRequest params) {
        Dormitory dormitory = new Dormitory();
        BeanUtils.copyProperties(params, dormitory);
        DormitoryPageRequest dormitoryPageRequest=new DormitoryPageRequest();
        dormitoryPageRequest.setBuilding(params.getBuilding());
        dormitoryPageRequest.setFloor(params.getFloor());
        dormitoryPageRequest.setRoomNo(params.getRoomNo());
        //根据单元楼层编号查询教室
        IPage<DormitoryPageResponse> dormitoryPageResponseIPage=page(dormitoryPageRequest);
        if(!dormitoryPageResponseIPage.getRecords().isEmpty()){
            throw new ServiceException("已存在该宿舍");
        }
        /*DormitoryAttachAddRequest dormitoryAttachAddRequest=new DormitoryAttachAddRequest();
        dormitoryAttachAddRequest.setDormitoryId(dormitory.getId());
        if(StringUtils.isNotBlank(params.getFileType())){
            dormitoryAttachAddRequest.setFileType(params.getFileType());
        }else {
            dormitoryAttachAddRequest.setFileType(FileTypeEnum.pic.getCode());
        }
        dormitoryAttachAddRequest.setFileUrl(params.getFileUrl());
        //添加宿舍附件
        dormitoryAttachService.add(dormitoryAttachAddRequest);
        DormitoryDeviceAddRequest dormitoryDeviceAddRequest=new DormitoryDeviceAddRequest();
        dormitoryDeviceAddRequest.setDormitoryId(dormitory.getId());
        dormitoryDeviceAddRequest.setSceneDeviceId(params.getSceneDeviceId());
        //添加宿舍设施
        dormitoryDeviceService.add(dormitoryDeviceAddRequest);*/
        return this.save(dormitory);
    }

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
    public List<DormitoryPageResponse> pages(DormitoryPageRequest params) {
        List<DormitoryPageResponse> dormitoryPageResponseIPage= baseMapper.pages(params);
        return dormitoryPageResponseIPage;
    }
    @Override
    public DormitoryDetailResponse detail(Long id) {
        DormitoryDetailResponse dormitoryDetailResponse=baseMapper.selectDetailById(id);
        return dormitoryDetailResponse;
    }

    @Override
    public boolean update(DormitoryUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        Dormitory dormitory1=this.lambdaQuery().eq(Dormitory::getBuilding,params.getBuilding()).eq(Dormitory::getFloor,params.getFloor()).
                eq(Dormitory::getRoomNo,params.getRoomNo()).one();
        if(dormitory1!=null && !dormitory1.getId().equals(params.getId())){
            throw new ServiceException("已存在该宿舍");
        }
        Dormitory dormitory = new Dormitory();
        BeanUtils.copyProperties(params, dormitory);
        return this.updateById(dormitory);
        /*DormitoryAttachUpdateRequest dormitoryAttachUpdateRequest=new DormitoryAttachUpdateRequest();
        dormitoryAttachUpdateRequest.setDormitoryId(params.getId());
        dormitoryAttachUpdateRequest.setFileUrl(params.getFileUrl());
        //更新宿舍附件
        dormitoryAttachService.update(dormitoryAttachUpdateRequest);
        DormitoryDeviceUpdateRequest dormitoryDeviceUpdateRequest=new DormitoryDeviceUpdateRequest();
        dormitoryDeviceUpdateRequest.setSceneDeviceId(params.getSceneDeviceId());
        dormitoryDeviceUpdateRequest.setDormitoryId(params.getId());
        //更新宿舍设施
        dormitoryDeviceService.update(dormitoryDeviceUpdateRequest);*/

    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        for (Long id:idList) {
            Long count=dormitoryScheduleMapper.selectCount(new LambdaQueryWrapper<DormitorySchedule>().eq(DormitorySchedule::getDormitoryId,id).ge(DormitorySchedule::getUseDate, LocalDate.now()));
            if(count>Constants.DB_FAIL){
                throw new ServiceException("宿舍已被预订，无法删除");
            }
        }
        return this.removeByIds(idList);
    }

    @Override
    public boolean updateBatch(DormitoryUpdateBatchRequest params) {
        List<Dormitory> dormitoryList=new ArrayList<>();
        for (Long id:params.getIds()) {
            if (id == null) {
                throw new IllegalParamsException("ID不能为空！");
            }
            Dormitory dormitory = new Dormitory();
            dormitory.setId(id);
            dormitory.setEnabled(params.getEnabled());
            dormitoryList.add(dormitory);
        }
        return this.updateBatchById(dormitoryList);
    }

    @Override
    public List<Dormitory> listDormitory(DormitoryPageRequest request) {
        List<Dormitory> dormitoryList=this.lambdaQuery().eq(Dormitory::getEnabled, Constants.DB_TRUE)
                .eq(Dormitory::getDeleted,Constants.DB_FAIL).list();
        for (Dormitory dormitory:dormitoryList) {
            DictPageRequestDTO dictPageRequestDTO=new DictPageRequestDTO();
            if(dormitory.getBuilding()!=null){
                dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.BULDING.getName());
                dictPageRequestDTO.setDictKey(dormitory.getBuilding());
                //根据单元字典码和字典值获取字典名称
                String building=remoteTrainDictService.getDictValue(dictPageRequestDTO).getRemoteData();
                dormitory.setBuilding(building);
            }
            if(dormitory.getFloor()!=null){
                dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.FLOOR.getName());
                dictPageRequestDTO.setDictKey(dormitory.getFloor());
                //根据楼层字典码和字典值获取字典名称
                String floor=remoteTrainDictService.getDictValue(dictPageRequestDTO).getRemoteData();
                dormitory.setFloor(floor);
            }
            if(dormitory.getRoomType()!=null){
                dictPageRequestDTO.setCode( BuldingFloorRoomTypeEnum.DORMITORY.getName());
                dictPageRequestDTO.setDictKey(dormitory.getRoomType());
                //根据类别字典码和字典值获取字典名称
                String roomType=remoteTrainDictService.getDictValue(dictPageRequestDTO).getRemoteData();
                dormitory.setRoomType(roomType);
            }
        }
        return dormitoryList;
    }

    @Override
    public IPage<DormitoryPageResponse> getDormitoryState(DormitoryPageRequest params) {
        IPage<DormitoryPageResponse> dormitoryPageResponseIPage = baseMapper.page(params.createMpPage(), params);
        for (DormitoryPageResponse dormitoryPageResponse:dormitoryPageResponseIPage.getRecords()) {
            List<DormitorySchedule> dormitoryScheduleList=dormitoryScheduleMapper.selectList(new LambdaQueryWrapper<DormitorySchedule>().eq(DormitorySchedule::getDormitoryId,dormitoryPageResponse.getId())
                    .ge(DormitorySchedule::getUseDate,params.getStartDate()).lt(DormitorySchedule::getUseDate,params.getEndDate()));
            dormitoryPageResponse.setDormitoryScheduleDetailResponses(
                    TreeUtil.copyBeanList(
                            dormitoryScheduleList, DormitoryScheduleDetailResponse.class));
            for (DormitoryScheduleDetailResponse classroomScheduleDetailResponse: dormitoryPageResponse.getDormitoryScheduleDetailResponses()) {
                classroomScheduleDetailResponse.setOrgName(classroomScheduleDetailResponse.getOrgName());
            }
        }
        return dormitoryPageResponseIPage;
    }

    @Override
    public List<UseDateStateCount> getDormitoryCount(DormitoryPageRequest params) {
        List<UseDateStateCount> useDateStateCountList=baseMapper.pageCount(params);
        List<UseDateStateCount> useDateStateCountList2=dormitoryScheduleMapper.pageCount(params);
        for (UseDateStateCount useDateStateCount:useDateStateCountList) {
            for (UseDateStateCount useDateStateCount2:useDateStateCountList2) {
                if(useDateStateCount.getRoomType().equals(useDateStateCount2.getRoomType())){
                    useDateStateCount.setUseNum(useDateStateCount2.getUseNum());
                    useDateStateCount.setFreeNum(useDateStateCount.getFreeNum()-useDateStateCount2.getUseNum());
                }

            }
        }
        return useDateStateCountList;
    }

}

