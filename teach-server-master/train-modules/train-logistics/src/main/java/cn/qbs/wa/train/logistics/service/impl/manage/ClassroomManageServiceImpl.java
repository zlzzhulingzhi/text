package cn.qbs.wa.train.logistics.service.impl.manage;

import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.train.basic.api.RemoteTrainDictService;
import cn.qbs.wa.train.basic.api.pojo.DTO.dict.DictPageRequestDTO;
import cn.qbs.wa.train.logistics.entity.Classroom;
import cn.qbs.wa.train.logistics.entity.ClassroomSchedule;
import cn.qbs.wa.train.logistics.entity.Organization;
import cn.qbs.wa.train.logistics.enums.BuldingFloorRoomTypeEnum;
import cn.qbs.wa.train.logistics.mapper.ClassroomMapper;
import cn.qbs.wa.train.logistics.mapper.OrganizationMapper;
import cn.qbs.wa.train.logistics.pojo.classroom.*;
import cn.qbs.wa.train.logistics.pojo.classroomattach.ClassroomAttachPageRequest;
import cn.qbs.wa.train.logistics.pojo.classroomattach.ClassroomAttachPageResponse;
import cn.qbs.wa.train.logistics.pojo.classroomdevice.ClassroomDevicePageRequest;
import cn.qbs.wa.train.logistics.pojo.classroomdevice.ClassroomDevicePageResponse;
import cn.qbs.wa.train.logistics.pojo.classroomschedule.ClassroomScheduleDetailResponse;
import cn.qbs.wa.train.logistics.pojo.scenedevice.SceneDeviceDetailResponse;
import cn.qbs.wa.train.logistics.service.ClassroomScheduleService;
import cn.qbs.wa.train.logistics.service.manage.ClassroomManageService;
import cn.qbs.wa.train.logistics.service.platform.ClassroomAttachService;
import cn.qbs.wa.train.logistics.service.platform.ClassroomDeviceService;
import cn.qbs.wa.train.logistics.service.platform.SceneDeviceService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
/**
 * 教室表(Classroom)表服务实现类
 *
 * @author makejava
 * @since 2022-10-11 17:30:11
 */
@Slf4j
@Service("classroomManageService")
public class ClassroomManageServiceImpl extends ServiceImpl<ClassroomMapper, Classroom>
    implements ClassroomManageService {

  @Resource
  ClassroomScheduleService classroomScheduleService;

  @Resource
  ClassroomAttachService classroomAttachService;

  @Resource
  SceneDeviceService sceneDeviceService;

  @Resource
  ClassroomDeviceService classroomDeviceService;

  @Resource
  RemoteTrainDictService remoteTrainDictService;

  @Resource
  OrganizationMapper organizationMapper;

  @Override
  public IPage<ClassroomPageResponse> getClassroomState(ClassroomPageRequest params) {
    IPage<ClassroomPageResponse> classroomPageResponseIPage = baseMapper.page(params.createMpPage(), params);
    for (ClassroomPageResponse classroomPageResponse:classroomPageResponseIPage.getRecords()) {
        List<ClassroomSchedule> classroomScheduleList=classroomScheduleService.lambdaQuery().eq(ClassroomSchedule::getClassroomId,classroomPageResponse.getId()).
                /*eq(ClassroomSchedule::getOrgId,params.getOrgId()).*/ge(ClassroomSchedule::getUseDate,params.getStartDate()).le(ClassroomSchedule::getUseDate,params.getEndDate()).list();
      classroomPageResponse.setClassroomScheduleDetailResponses(
              TreeUtil.copyBeanList(
                      classroomScheduleList, ClassroomScheduleDetailResponse.class));
      for (ClassroomScheduleDetailResponse classroomScheduleDetailResponse: classroomPageResponse.getClassroomScheduleDetailResponses()) {
        Organization organization=organizationMapper.selectById(classroomScheduleDetailResponse.getOrgId());
        classroomScheduleDetailResponse.setOrgName(organization.getName());
      }
    }
    return classroomPageResponseIPage;
  }
  @Override
  public IPage<ClassroomPageResponse> page(ClassroomPageRequest params) {
    IPage<ClassroomPageResponse> classroomPageResponseIPage =
            baseMapper.page(params.createMpPage(), params);
    List<ClassroomPageResponse> classroomPageResponseList = classroomPageResponseIPage.getRecords();
    for (ClassroomPageResponse classroomPageResponse : classroomPageResponseList) {
      DictPageRequestDTO dictPageRequestDTO = new DictPageRequestDTO();
      if (classroomPageResponse.getBuilding() != null) {
        dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.BULDING.getName());
        dictPageRequestDTO.setDictKey(classroomPageResponse.getBuilding());
        // 根据类别字典码和字典值获取字典名称
        String building = remoteTrainDictService.getDictValue(dictPageRequestDTO).getRemoteData();
        classroomPageResponse.setBuilding(building);
      }
      if (classroomPageResponse.getFloor() != null) {
        dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.FLOOR.getName());
        dictPageRequestDTO.setDictKey(classroomPageResponse.getFloor());
        // 根据类别字典码和字典值获取字典名称
        String floor = remoteTrainDictService.getDictValue(dictPageRequestDTO).getRemoteData();
        classroomPageResponse.setFloor(floor);
      }
      if (classroomPageResponse.getRoomType() != null) {
        dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.CLASSROOM.getName());
        dictPageRequestDTO.setDictKey(classroomPageResponse.getRoomType());
        // 根据类别字典码和字典值获取字典名称
        String roomType = remoteTrainDictService.getDictValue(dictPageRequestDTO).getRemoteData();
        classroomPageResponse.setRoomType(roomType);
      }
    }
    return classroomPageResponseIPage;
  }

  @Override
  public ClassroomDetailResponse detail(Long id) {
    ClassroomDetailResponse classroomDetailResponse = baseMapper.selectDetailById(id);
    /*DictPageRequestDTO dictPageRequestDTO = new DictPageRequestDTO();
    if (classroomDetailResponse.getRoomType() != null) {
      dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.CLASSROOM.getName());
      dictPageRequestDTO.setDictKey(classroomDetailResponse.getRoomType());
      // 根据类别字典码和字典值获取字典名称
      String roomType = remoteTrainDictService.getDictValue(dictPageRequestDTO).getRemoteData();
      classroomDetailResponse.setRoomType(roomType);
    }*/
    ClassroomAttachPageRequest classroomAttachPageRequest = new ClassroomAttachPageRequest();
    classroomAttachPageRequest.setClassroomId(id);
    // 查询教室附件
    IPage<ClassroomAttachPageResponse> classroomAttachPageResponseIPage =
            classroomAttachService.page(classroomAttachPageRequest);
    String fileUrl="";
    if (!classroomAttachPageResponseIPage.getRecords().isEmpty()) {
      List<ClassroomAttachPageResponse> classroomDevicePageResponseList =
              classroomAttachPageResponseIPage.getRecords();
      for (ClassroomAttachPageResponse classroomAttachPageResponse :
              classroomDevicePageResponseList) {
        classroomDetailResponse.setFileType(classroomAttachPageResponse.getFileType());
        if (classroomAttachPageResponse.getFileUrl() != null) {
          fileUrl=fileUrl+","+classroomAttachPageResponse.getFileUrl();
          classroomDetailResponse.setFileUrl(fileUrl.substring(Constants.DB_TRUE).split(","));
        }
      }
    }
   /* if (FileTypeEnum.pic.getCode().equals(classroomDetailResponse.getFileType())) {
      classroomDetailResponse.setFileType(FileTypeEnum.pic.getName());
    }
    if (FileTypeEnum.doc.getCode().equals(classroomDetailResponse.getFileType())) {
      classroomDetailResponse.setFileType(FileTypeEnum.doc.getName());
    }*/
    ClassroomDevicePageRequest classroomDevicePageRequest = new ClassroomDevicePageRequest();
    classroomDevicePageRequest.setClassroomId(id);
    // 查询教室设施
    IPage<ClassroomDevicePageResponse> classroomDevicePageResponseIPage =
            classroomDeviceService.page(classroomDevicePageRequest);
    if (!classroomDevicePageResponseIPage.getRecords().isEmpty()) {
      List<ClassroomDevicePageResponse> classroomDevicePageResponseList =
              classroomDevicePageResponseIPage.getRecords();
      List<Long> sceneDeviceId=new ArrayList<>();
      for (ClassroomDevicePageResponse classroomDevicePageResponse :
              classroomDevicePageResponseList) {
        // 查询场景设备
        SceneDeviceDetailResponse sceneDeviceDetailResponse =
                sceneDeviceService.detail(classroomDevicePageResponse.getSceneDeviceId());
        sceneDeviceId.add(sceneDeviceDetailResponse.getId());
      }
      classroomDetailResponse.setSceneDeviceId(sceneDeviceId);
    }
    return classroomDetailResponse;
  }
}
