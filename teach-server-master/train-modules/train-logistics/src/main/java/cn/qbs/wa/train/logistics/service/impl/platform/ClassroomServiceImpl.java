package cn.qbs.wa.train.logistics.service.impl.platform;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import cn.qbs.wa.train.basic.api.RemoteTrainDictService;
import cn.qbs.wa.train.basic.api.pojo.DTO.dict.DictPageRequestDTO;
import cn.qbs.wa.train.logistics.entity.Classroom;
import cn.qbs.wa.train.logistics.entity.ClassroomSchedule;
import cn.qbs.wa.train.logistics.entity.Organization;
import cn.qbs.wa.train.logistics.enums.BuldingFloorRoomTypeEnum;
import cn.qbs.wa.train.logistics.enums.FileTypeEnum;
import cn.qbs.wa.train.logistics.enums.SummaryTypeEnum;
import cn.qbs.wa.train.logistics.excel.*;
import cn.qbs.wa.train.logistics.mapper.ClassroomMapper;
import cn.qbs.wa.train.logistics.mapper.ClassroomValuationMapper;
import cn.qbs.wa.train.logistics.mapper.OrganizationMapper;
import cn.qbs.wa.train.logistics.pojo.classroom.*;
import cn.qbs.wa.train.logistics.pojo.classroomattach.ClassroomAttachAddRequest;
import cn.qbs.wa.train.logistics.pojo.classroomattach.ClassroomAttachPageRequest;
import cn.qbs.wa.train.logistics.pojo.classroomattach.ClassroomAttachPageResponse;
import cn.qbs.wa.train.logistics.pojo.classroomattach.ClassroomAttachUpdateRequest;
import cn.qbs.wa.train.logistics.pojo.classroomdevice.ClassroomDeviceAddRequest;
import cn.qbs.wa.train.logistics.pojo.classroomdevice.ClassroomDevicePageRequest;
import cn.qbs.wa.train.logistics.pojo.classroomdevice.ClassroomDevicePageResponse;
import cn.qbs.wa.train.logistics.pojo.classroomdevice.ClassroomDeviceUpdateRequest;
import cn.qbs.wa.train.logistics.pojo.classroomschedule.ClassroomScheduleDetailResponse;
import cn.qbs.wa.train.logistics.pojo.scenedevice.SceneDeviceDetailResponse;
import cn.qbs.wa.train.logistics.service.ClassroomScheduleService;
import cn.qbs.wa.train.logistics.service.platform.ClassroomAttachService;
import cn.qbs.wa.train.logistics.service.platform.ClassroomDeviceService;
import cn.qbs.wa.train.logistics.service.platform.ClassroomService;
import cn.qbs.wa.train.logistics.service.platform.SceneDeviceService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 教室表(Classroom)表服务实现类
 *
 * @author makejava
 * @since 2022-10-11 17:30:11
 */
@Slf4j
@Service("classroomService")
public class ClassroomServiceImpl extends ServiceImpl<ClassroomMapper, Classroom> implements ClassroomService {

    @Resource
    ClassroomDataValidateFactory classroomDataValidateFactory;

    @Resource
    RemoteTrainDictService remoteTrainDictService;

    @Resource
    ClassroomAttachService classroomAttachService;

    @Resource
    SceneDeviceService sceneDeviceService;

    @Resource
    ClassroomDeviceService classroomDeviceService;

    @Resource
    ClassroomScheduleService classroomScheduleService;

    @Resource
    OrganizationMapper organizationMapper;

    @Resource
    ClassroomValuationMapper classroomValuationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(ClassroomAddRequest params) {
        Classroom classroom = new Classroom();
        BeanUtils.copyProperties(params, classroom);
        ClassroomPageRequest classroomPageRequest = new ClassroomPageRequest();
        classroomPageRequest.setBuilding(params.getBuilding());
        classroomPageRequest.setFloor(params.getFloor());
        classroomPageRequest.setRoomNo(params.getRoomNo());
        // 根据单元楼层编号查询教室
        IPage<ClassroomPageResponse> classroomPageResponseIPage = page(classroomPageRequest);
        if (!classroomPageResponseIPage.getRecords().isEmpty()) {
            throw new ServiceException("已存在该教室");
        }
        this.save(classroom);
        ClassroomAttachAddRequest classroomAttachAddRequest = new ClassroomAttachAddRequest();
        classroomAttachAddRequest.setClassroomId(classroom.getId());
        if (StringUtils.isNotBlank(params.getFileType())) {
            classroomAttachAddRequest.setFileType(params.getFileType());
        } else {
            classroomAttachAddRequest.setFileType(FileTypeEnum.pic.getCode());
        }
        classroomAttachAddRequest.setFileUrl(params.getFileUrl());
        // 添加教室附件
        classroomAttachService.add(classroomAttachAddRequest);
        ClassroomDeviceAddRequest classroomDeviceAddRequest = new ClassroomDeviceAddRequest();
        classroomDeviceAddRequest.setClassroomId(classroom.getId());
        classroomDeviceAddRequest.setSceneDeviceId(params.getSceneDeviceId());
        // 添加教室设施
        classroomDeviceService.add(classroomDeviceAddRequest);
        return true;
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
        List<String> fileUrlList = new ArrayList<>();
        if (!classroomAttachPageResponseIPage.getRecords().isEmpty()) {
            List<ClassroomAttachPageResponse> classroomDevicePageResponseList =
                    classroomAttachPageResponseIPage.getRecords();
            for (ClassroomAttachPageResponse classroomAttachPageResponse :
                    classroomDevicePageResponseList) {
                classroomDetailResponse.setFileType(classroomAttachPageResponse.getFileType());
                fileUrlList.add(classroomAttachPageResponse.getFileUrl());
            }
        }
        String[] fileUrl = {};
        classroomDetailResponse.setFileUrl(fileUrlList.toArray(fileUrl));
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
            List<Long> sceneDeviceId = new ArrayList<>();
            for (ClassroomDevicePageResponse classroomDevicePageResponse :
                    classroomDevicePageResponseList) {
                // 查询场景设备
                SceneDeviceDetailResponse sceneDeviceDetailResponse =
                        sceneDeviceService.detail(classroomDevicePageResponse.getSceneDeviceId());
                if (sceneDeviceDetailResponse != null) {
                    sceneDeviceId.add(sceneDeviceDetailResponse.getId());
                }
            }
            classroomDetailResponse.setSceneDeviceId(sceneDeviceId);
        }
        return classroomDetailResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(ClassroomUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        Classroom classroom1 = this.lambdaQuery().eq(Classroom::getBuilding, params.getBuilding()).eq(Classroom::getFloor, params.getFloor()).
                eq(Classroom::getRoomNo, params.getRoomNo()).one();
        if (classroom1 != null && !classroom1.getId().equals(params.getId())) {
            throw new ServiceException("已存在该教室");
        }
        Classroom classroom = new Classroom();
        BeanUtils.copyProperties(params, classroom);
        ClassroomAttachUpdateRequest classroomAttachUpdateRequest = new ClassroomAttachUpdateRequest();
        if (StringUtils.isNotBlank(params.getFileType())) {
            classroomAttachUpdateRequest.setFileType(params.getFileType());
        } else {
            classroomAttachUpdateRequest.setFileType(FileTypeEnum.pic.getCode());
        }
        classroomAttachUpdateRequest.setClassroomId(params.getId());
        classroomAttachUpdateRequest.setFileUrl(params.getFileUrl());
        // 更新教室附件
        classroomAttachService.update(classroomAttachUpdateRequest);
        ClassroomDeviceUpdateRequest classroomDeviceUpdateRequest =
                new ClassroomDeviceUpdateRequest();
        classroomDeviceUpdateRequest.setSceneDeviceId(params.getSceneDeviceId());
        classroomDeviceUpdateRequest.setClassroomId(params.getId());
        // 更新教室设施
        classroomDeviceService.update(classroomDeviceUpdateRequest);
        return this.updateById(classroom);
    }

    @Override
    public boolean updateBatch(ClassroomUpdateBatchRequest params) {
        List<Classroom> classroomList = new ArrayList<>();
        for (Long id : params.getIds()) {
            if (id == null) {
                throw new IllegalParamsException("ID不能为空！");
            }
            Classroom classroom = new Classroom();
            classroom.setId(id);
            classroom.setEnabled(params.getEnabled());
            classroomList.add(classroom);
        }
        return this.updateBatchById(classroomList);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        for (Long id : idList) {
            Long count = classroomScheduleService.lambdaQuery().eq(ClassroomSchedule::getClassroomId, id).ge(ClassroomSchedule::getUseDate, LocalDate.now()).count();
            if (count > Constants.DB_FAIL) {
                throw new ServiceException("教室已被预订，无法删除");
            }
        }
        return this.removeByIds(idList);
    }

    @Override
    public List<Classroom> listClassroom(ClassroomPageRequest request) {
        List<Classroom> classroomList =
                this.lambdaQuery()
                        .eq(Classroom::getEnabled, Constants.DB_TRUE)
                        .eq(Classroom::getDeleted, Constants.DB_FAIL)
                        .list();
        for (Classroom classRoom : classroomList) {
            if (classRoom.getRoomType() != null) {
                DictPageRequestDTO dictPageRequestDTO = new DictPageRequestDTO();
                dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.CLASSROOM.getName());
                dictPageRequestDTO.setDictKey(classRoom.getRoomType());
                // 根据类别字典码和字典值获取字典名称
                String roomType = remoteTrainDictService.getDictValue(dictPageRequestDTO).getRemoteData();
                classRoom.setRoomType(roomType);
            }
        }
        return classroomList;
    }

    @Override
    public IPage<ClassroomPageResponse> classroomSchedulePage(ClassroomPageRequest params) {

        IPage<ClassroomPageResponse> page = page(params);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return page;
        }
        List<Long> classroomIds =
                page.getRecords().stream().map(ClassroomPageResponse::getId).collect(Collectors.toList());
        List<ClassroomSchedule> classroomSchedules =
                this.classroomScheduleService
                        .lambdaQuery()
                        .in(ClassroomSchedule::getClassroomId, classroomIds)
                        .list();
        if (CollectionUtils.isEmpty(classroomSchedules)) {
            return page;
        }
        Map<Long, List<ClassroomSchedule>> classroomScheduleMap =
                classroomSchedules.stream()
                        .collect(Collectors.groupingBy(ClassroomSchedule::getClassroomId));
        page.getRecords()
                .forEach(
                        l -> {
                            List<ClassroomSchedule> classroomScheduleList = classroomScheduleMap.get(l.getId());
                            l.setClassroomScheduleDetailResponses(
                                    TreeUtil.copyBeanList(
                                            classroomScheduleList, ClassroomScheduleDetailResponse.class));
                        });
        return page;
    }

    @Override
    public IPage<ClassroomPageResponse> getClassroomState(ClassroomPageRequest params) {
        IPage<ClassroomPageResponse> classroomPageResponseIPage = baseMapper.page(params.createMpPage(), params);
        for (ClassroomPageResponse classroomPageResponse : classroomPageResponseIPage.getRecords()) {
            List<ClassroomSchedule> classroomScheduleList = classroomScheduleService.lambdaQuery().eq(ClassroomSchedule::getClassroomId, classroomPageResponse.getId())
                    .ge(ClassroomSchedule::getUseDate, params.getStartDate()).le(ClassroomSchedule::getUseDate, params.getEndDate()).list();
            classroomPageResponse.setClassroomScheduleDetailResponses(
                    TreeUtil.copyBeanList(
                            classroomScheduleList, ClassroomScheduleDetailResponse.class));
            for (ClassroomScheduleDetailResponse classroomScheduleDetailResponse : classroomPageResponse.getClassroomScheduleDetailResponses()) {
                Organization organization = organizationMapper.selectById(classroomScheduleDetailResponse.getOrgId());
                classroomScheduleDetailResponse.setOrgName(organization.getName());
            }
        }
        return classroomPageResponseIPage;
    }

    @Override
    public ClassroomPriceResponse detailFee(Long id) {
        ClassroomPriceResponse classroomPriceResponse = new ClassroomPriceResponse();
        ClassroomDetailResponse classroomDetailResponse = baseMapper.selectDetailById(id);
        if (classroomDetailResponse.getArea() == null) {
            return classroomPriceResponse;
        }
        classroomPriceResponse = classroomValuationMapper.getClassroomPrice();
        classroomPriceResponse.setSiteUnitPrice(classroomPriceResponse.getSiteFee());
        classroomPriceResponse.setAirConditionerUnitPrice(classroomPriceResponse.getAirConditionerFee());
        classroomPriceResponse.setAirExhaustUnitPrice(classroomPriceResponse.getAirExhaustFee());
        classroomPriceResponse.setWaterPowerUnitPrice(classroomPriceResponse.getWaterPowerFee());
        classroomPriceResponse.setSiteFee(classroomPriceResponse.getSiteFee().multiply(classroomDetailResponse.getArea()));
        classroomPriceResponse.setAirConditionerFee(classroomPriceResponse.getAirConditionerFee().multiply(classroomDetailResponse.getArea()));
        classroomPriceResponse.setAirExhaustFee(classroomPriceResponse.getAirExhaustFee().multiply(classroomDetailResponse.getArea()));
        classroomPriceResponse.setWaterPowerFee(classroomPriceResponse.getWaterPowerFee().multiply(classroomDetailResponse.getArea()));
        classroomPriceResponse.setTotalFee(classroomPriceResponse.getWaterPowerFee().
                add(classroomPriceResponse.getSiteFee().add(classroomPriceResponse.getAirConditionerFee().add(classroomPriceResponse.getAirExhaustFee()))));
        return classroomPriceResponse;
    }

    @Override
    public List<ClassroomDataParseResult> importPre(MultipartFile file) {
        ExcelAnalysisListener dataListener = new ExcelAnalysisListener();
        try {
            EasyExcel.read(file.getInputStream(), ClassroomExcelData.class, dataListener).ignoreEmptyRow(true).sheet(1).doRead();
        } catch (Exception e) {
            throw new ServiceException("上传的导入模板格式不正确！");
        }
        List<ClassroomExcelData> dataList = dataListener.getDataList();
        if (CollectionUtils.isEmpty(dataList)) {
            throw new ServiceException("解析不到数据！");
        }
        List<ClassroomDataParseResult> resultList = new ArrayList<>();
        List<ClassroomDataParseResult> errorList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            ClassroomExcelData classroomData = dataList.get(i);
            ClassroomDataParseResult result = new ClassroomDataParseResult();
            BeanUtils.copyProperties(classroomData, result);
            ClassroomDataValidate classroomDataValidate = classroomDataValidateFactory.getExcelClassroomValidate();
            classroomDataValidate.validate(classroomData);
            boolean passed = classroomDataValidate.passed();
            result.setPassed(passed);
            if (!passed) {
                result.setErrorReasons(classroomDataValidate.getErrorReasons());
                errorList.add(result);
            }
            resultList.add(result);
        }
        return resultList;
    }

    class ExcelAnalysisListener extends AnalysisEventListener<ClassroomExcelData> {

        private List<ClassroomExcelData> dataList = new ArrayList<>();


        @Override
        public void invoke(ClassroomExcelData classroomExcelData, AnalysisContext analysisContext) {
            Integer rowIndex = analysisContext.readRowHolder().getRowIndex();
            classroomExcelData.setRowNum(rowIndex + 1);
            //数据存储到list，供批量处理，或后续自己业务逻辑处理。
            if (StringUtils.hasText(classroomExcelData.getBuilding())) {
                DictPageRequestDTO dictPageRequestDTO = new DictPageRequestDTO();
                dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.BULDING.getName());
                dictPageRequestDTO.setDictValue(classroomExcelData.getBuilding());
                //根据单元字典名称和字典值获取字典码
                String building = remoteTrainDictService.getDictKey(dictPageRequestDTO).getRemoteData();
                classroomExcelData.setBuilding(building);
                dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.FLOOR.getName());
                dictPageRequestDTO.setDictValue(classroomExcelData.getFloor());
                //根据楼层字典名称和字典值获取字典码
                String floor = remoteTrainDictService.getDictKey(dictPageRequestDTO).getRemoteData();
                classroomExcelData.setFloor(floor);
                dataList.add(classroomExcelData);
                if (StringUtils.isNotBlank(classroomExcelData.getRoomType())) {
                    dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.CLASSROOM.getName());
                    dictPageRequestDTO.setDictValue(classroomExcelData.getRoomType());
                    //根据类别字典名称和字典值获取字典码
                    String roomType = remoteTrainDictService.getDictKey(dictPageRequestDTO).getRemoteData();
                    classroomExcelData.setRoomType(roomType);
                }
                return;
            }
            //dataList.add(employeeExcelData);
            // 判断是否所有属性都为空
            Map<Integer, Cell> cellMap = analysisContext.readRowHolder().getCellMap();
            cellMap.forEach((k, v) -> {
                ReadCellData cell = (ReadCellData) v;
                if (!CellDataTypeEnum.EMPTY.equals(cell.getType())) {
                    dataList.add(classroomExcelData);
                    return;
                }
            });
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        }

        public List<ClassroomExcelData> getDataList() {
            return dataList;
        }

    }

    @Override
    public List<Classroom> batchAdd(List<ClassroomDataParseResult> params) {
        List<Classroom> classroomList = new ArrayList<>();
        for (ClassroomDataParseResult param : params) {
            Classroom classroom = new Classroom();
            BeanUtils.copyProperties(param, classroom);
            Long count = this.lambdaQuery().eq(Classroom::getBuilding, classroom.getBuilding()).
                    eq(Classroom::getFloor, classroom.getFloor()).eq(Classroom::getRoomNo, classroom.getRoomNo()).count();
            if (count > Constants.DB_FAIL) {
                throw new ServiceException("已存在该教室");
            }
            classroomList.add(classroom);
        }
        this.saveBatch(classroomList);
        return classroomList;
    }

    @Override
    public IPage<ClassroomSummaryResponse> getClassroomSummary(ClassroomSummaryRequest params) {
        if (params.getUseDate() == null) {
            return params.createMpPage();
        }
        IPage<ClassroomSummaryResponse> classroomSummaryResponse = baseMapper.getClassroomSummary(params.createMpPage(), params);
        if (CollectionUtils.isNotEmpty(classroomSummaryResponse.getRecords())) {
            for (ClassroomSummaryResponse classroomSummary : classroomSummaryResponse.getRecords()) {
                classroomSummary.setFreeNum(classroomSummary.getTotalNum() - classroomSummary.getUseNum());
            }
        }
        return classroomSummaryResponse;
    }

    @Override
    public IPage<ClassroomPageResponse> getClassroomSummaryDetail(ClassroomSummaryRequest params) {
        if (SummaryTypeEnum.TOTAL.getCode().equals(params.getSummaryType())) {
            ClassroomPageRequest classroomPageRequest = BeanUtil.copyProperties(params, ClassroomPageRequest.class);
            IPage<ClassroomPageResponse> classroomPageResponseIPage = baseMapper.page(params.createMpPage(), classroomPageRequest);
            return classroomPageResponseIPage;
        } else if (SummaryTypeEnum.USE.getCode().equals(params.getSummaryType())) {
            IPage<ClassroomPageResponse> classroomPageResponseIPage = baseMapper.pageV2(params.createMpPage(), params);
            for (ClassroomPageResponse classroomPageResponse : classroomPageResponseIPage.getRecords()) {
                List<ClassroomSchedule> classroomScheduleList = classroomScheduleService.lambdaQuery().eq(ClassroomSchedule::getClassroomId, classroomPageResponse.getId())
                        .eq(ClassroomSchedule::getUseDate, params.getUseDate()).list();
                classroomPageResponse.setClassroomScheduleDetailResponses(
                        TreeUtil.copyBeanList(
                                classroomScheduleList, ClassroomScheduleDetailResponse.class));
                for (ClassroomScheduleDetailResponse classroomScheduleDetailResponse : classroomPageResponse.getClassroomScheduleDetailResponses()) {
                    Organization organization = organizationMapper.selectById(classroomScheduleDetailResponse.getOrgId());
                    classroomScheduleDetailResponse.setOrgName(organization.getName());
                }
            }
            return classroomPageResponseIPage;
        } else if (SummaryTypeEnum.FREE.getCode().equals(params.getSummaryType())) {
            List<Classroom> classroomList = baseMapper.selectList(Wrappers.<Classroom>lambdaQuery()
                    .eq(Classroom::getRoomType, params.getRoomType()));
            List<Long> ids = classroomList.stream().map(Classroom::getId).collect(Collectors.toList());
            List<ClassroomSchedule> classroomScheduleList = classroomScheduleService.lambdaQuery().eq(ClassroomSchedule::getUseDate, params.getUseDate()).list();
            List<Long> useIds = classroomScheduleList.stream().map(ClassroomSchedule::getClassroomId).collect(Collectors.toList());
            ids.removeAll(useIds);
            if (CollectionUtils.isNotEmpty(ids)) {
                IPage<Classroom> classroomIPage = baseMapper.selectPage(params.createMpPage(), Wrappers.<Classroom>lambdaQuery().in(Classroom::getId, ids));
                return classroomIPage.convert(r -> BeanUtil.copyProperties(r, ClassroomPageResponse.class));
            }
        }
        return params.createMpPage();
    }

    @Override
    public IPage<ClassroomPageResponse> getClassroomSummaryDetailV2(ClassroomSummaryRequest params) {
        if (SummaryTypeEnum.TOTAL.getCode().equals(params.getSummaryType())) {
            ClassroomPageRequest classroomPageRequest = BeanUtil.copyProperties(params, ClassroomPageRequest.class);
            IPage<ClassroomPageResponse> classroomPageResponseIPage = baseMapper.page(params.createMpPage(), classroomPageRequest);
            return classroomPageResponseIPage;
        } else if (SummaryTypeEnum.USE.getCode().equals(params.getSummaryType())) {
            IPage<ClassroomPageResponse> classroomPageResponseIPage = baseMapper.pageV2(params.createMpPage(), params);
            for (ClassroomPageResponse classroomPageResponse : classroomPageResponseIPage.getRecords()) {
                Organization organization = organizationMapper.selectById(classroomPageResponse.getOrgId());
                classroomPageResponse.setOrgName(organization.getName());
            }
            return classroomPageResponseIPage;
        } else if (SummaryTypeEnum.FREE.getCode().equals(params.getSummaryType())) {
            List<Classroom> classroomList = baseMapper.selectList(Wrappers.<Classroom>lambdaQuery()
                    .eq(Classroom::getRoomType, params.getRoomType()));
            List<Long> ids = classroomList.stream().map(Classroom::getId).collect(Collectors.toList());
            List<ClassroomSchedule> classroomScheduleList = classroomScheduleService.lambdaQuery().eq(ClassroomSchedule::getUseDate, params.getUseDate()).list();
            List<Long> useIds = classroomScheduleList.stream().map(ClassroomSchedule::getClassroomId).collect(Collectors.toList());
            ids.removeAll(useIds);
            if (CollectionUtils.isNotEmpty(ids)) {
                IPage<Classroom> classroomIPage = baseMapper.selectPage(params.createMpPage(), Wrappers.<Classroom>lambdaQuery().in(Classroom::getId, ids));
                return classroomIPage.convert(r -> BeanUtil.copyProperties(r, ClassroomPageResponse.class));
            }
        }
        return params.createMpPage();
    }

    @Override
    public IPage<ClassroomPageResponse> pageInuse(ClassroomPageRequest params) {
        return baseMapper.pageInuse(params.createMpPage(), params);
    }

    @Override
    public IPage<ClassroomPageResponse> pageUnused(ClassroomPageRequest params) {
        return baseMapper.pageUnused(params.createMpPage(), params);
    }
}
