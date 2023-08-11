package cn.qbs.wa.train.logistics.service.impl.platform;

import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.train.basic.api.RemoteTrainDictService;
import cn.qbs.wa.train.basic.api.pojo.DTO.dict.DictPageRequestDTO;
import cn.qbs.wa.train.basic.api.pojo.DTO.dict.TreeDictDTO;
import cn.qbs.wa.train.basic.api.pojo.DTO.dict.TreeDictResultDTO;
import cn.qbs.wa.train.logistics.entity.Dormitory;
import cn.qbs.wa.train.logistics.entity.DormitorySchedule;
import cn.qbs.wa.train.logistics.entity.Organization;
import cn.qbs.wa.train.logistics.enums.BuldingFloorRoomTypeEnum;
import cn.qbs.wa.train.logistics.excel.*;
import cn.qbs.wa.train.logistics.excel.DormitoryScheduleExcelData;
import cn.qbs.wa.train.logistics.mapper.DormitoryScheduleMapper;
import cn.qbs.wa.train.logistics.mapper.OrganizationMapper;
import cn.qbs.wa.train.logistics.pojo.dormitory.DormitoryPageRequest;
import cn.qbs.wa.train.logistics.pojo.dormitory.DormitoryPageResponse;
import cn.qbs.wa.train.logistics.pojo.organization.OrganizationPageRequest;
import cn.qbs.wa.train.logistics.service.platform.DormitoryScheduleService;
import cn.qbs.wa.train.logistics.pojo.dormitoryschedule.*;
import cn.qbs.wa.train.logistics.service.platform.DormitoryService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 宿舍排期表(DormitorySchedule)表服务实现类
 *
 * @author makejava
 * @since 2022-10-18 10:03:57
 */
@Slf4j
@Service("dormitoryScheduleService")
public class DormitoryScheduleServiceImpl extends ServiceImpl<DormitoryScheduleMapper, DormitorySchedule> implements DormitoryScheduleService {

    @Resource
    DormitoryScheduleDataValidateFactory dormitoryScheduleDataValidateFactory;

    @Resource
    RemoteTrainDictService remoteTrainDictService;

    @Resource
    OrganizationMapper organizationMapper;

    @Resource
    DormitoryService dormitoryService;

    @Override
    public List<DormitorySchedule> batchAdd(List<DormitoryScheduleDataParseResult> params){
        List<DormitorySchedule> dormitoryScheduleList=new ArrayList<>();
        for (DormitoryScheduleDataParseResult param:params) {
            DictPageRequestDTO dictPageRequestDTO=new DictPageRequestDTO();
            if(StringUtils.isNotBlank(param.getBuilding())){
                dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.BULDING.getName());
                dictPageRequestDTO.setDictValue(param.getBuilding());
                //根据单元字典名称和字典值获取字典码
                String building=remoteTrainDictService.getDictKey(dictPageRequestDTO).getRemoteData();
                param.setBuilding(building);
            }
            if(StringUtils.isNotBlank(param.getFloor())){
                dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.FLOOR.getName());
                dictPageRequestDTO.setDictValue(param.getFloor());
                //根据楼层字典名称和字典值获取字典码
                String floor=remoteTrainDictService.getDictKey(dictPageRequestDTO).getRemoteData();
                param.setFloor(floor);
            }
            if(StringUtils.isNotBlank(param.getRoomType())){
                dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.DORMITORY.getName());
                dictPageRequestDTO.setDictValue(param.getRoomType());
                //根据类别字典名称和字典值获取字典码
                String roomType=remoteTrainDictService.getDictKey(dictPageRequestDTO).getRemoteData();
                param.setRoomType(roomType);
            }
            String useDate=param.getUseDate();
            if(useDate.contains("-")){
                String[] useDates=useDate.split("-");
                LocalDate startDate= null;
                try {
                    startDate =  LocalDate.parse(useDates[Constants.DB_FAIL], DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                } catch (Exception e) {
                    throw new ServiceException("日期格式错误,应类似2022/01/01格式");
                }
                LocalDate endDate= null;
                try {
                    endDate = LocalDate.parse(useDates[Constants.DB_TRUE], DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                } catch (Exception e) {
                    throw new ServiceException("日期格式错误,应类似2022/01/01格式");
                }
                List<LocalDate> dateList=getTwoDaysDayDes(startDate,endDate);
                    for (LocalDate date:dateList) {
                        DormitorySchedule dormitorySchedule = new DormitorySchedule();
                        BeanUtils.copyProperties(param, dormitorySchedule);
                        OrganizationPageRequest organizationPageRequest=new OrganizationPageRequest();
                        organizationPageRequest.setName(param.getOrgName());
                        //通过机构名称获取机构id
                        Long orgId=organizationMapper.getIdByOrgName(organizationPageRequest);
                        dormitorySchedule.setOrgId(orgId);
                        DormitoryPageRequest dormitoryPageRequest=new DormitoryPageRequest();
                        dormitoryPageRequest.setBuilding(param.getBuilding());
                        dormitoryPageRequest.setFloor(param.getFloor());
                        dormitoryPageRequest.setRoomType(param.getRoomType());
                        List<Dormitory> list = dormitoryService.lambdaQuery().
                                eq(Dormitory::getBuilding, param.getBuilding())
                                .eq(Dormitory::getFloor, param.getFloor())
                                .eq(Dormitory::getRoomNo, param.getRoomNo()).list();
                        if(list!=null && !list.isEmpty()){
                            Long dormitoryId=list.get(Constants.DB_FAIL).getId();
                            dormitorySchedule.setDormitoryId(dormitoryId);
                        }
                        dormitorySchedule.setUseDate(date);
                        DormitorySchedulePageRequest dormitorySchedulePageRequest=new DormitorySchedulePageRequest();
                        dormitorySchedulePageRequest.setBuilding(param.getBuilding());
                        dormitorySchedulePageRequest.setFloor(param.getFloor());
                        dormitorySchedulePageRequest.setRoomNo(param.getRoomNo());
                        dormitorySchedulePageRequest.setUseDate(date);
                        IPage<DormitorySchedulePageResponse> dormitorySchedulePageResponseIPage=page(dormitorySchedulePageRequest);
                        if(!dormitorySchedulePageResponseIPage.getRecords().isEmpty()){
                            throw new ServiceException("宿舍已被使用，请选其他使用日期");
                        }
                        dormitoryScheduleList.add(dormitorySchedule);
                    }

            }else {
                DormitorySchedule dormitorySchedule = new DormitorySchedule();
                BeanUtils.copyProperties(param, dormitorySchedule);
                OrganizationPageRequest organizationPageRequest=new OrganizationPageRequest();
                organizationPageRequest.setName(param.getOrgName());
                //通过机构名称获取机构id
                Long orgId=organizationMapper.getIdByOrgName(organizationPageRequest);
                dormitorySchedule.setOrgId(orgId);
                DormitoryPageRequest dormitoryPageRequest=new DormitoryPageRequest();
                dormitoryPageRequest.setBuilding(param.getBuilding());
                dormitoryPageRequest.setFloor(param.getFloor());
                dormitoryPageRequest.setRoomType(param.getRoomType());
                List<Dormitory> list = dormitoryService.lambdaQuery().
                        eq(Dormitory::getBuilding, param.getBuilding())
                        .eq(Dormitory::getFloor, param.getFloor())
                        .eq(Dormitory::getRoomType, param.getRoomType()).list();
                if(list!=null && !list.isEmpty()){
                    Long dormitoryId=list.get(Constants.DB_FAIL).getId();
                    dormitorySchedule.setDormitoryId(dormitoryId);
                }
                LocalDate date= null;
                try {
                    date = LocalDate.parse(useDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                } catch (Exception e) {
                    throw new ServiceException("日期格式错误,应类似2022/01/01格式");
                }
                dormitorySchedule.setUseDate(date);
                DormitorySchedulePageRequest dormitorySchedulePageRequest=new DormitorySchedulePageRequest();
                dormitorySchedulePageRequest.setBuilding(param.getBuilding());
                dormitorySchedulePageRequest.setFloor(param.getFloor());
                dormitorySchedulePageRequest.setRoomNo(param.getRoomNo());
                dormitorySchedulePageRequest.setUseDate(date);
                IPage<DormitorySchedulePageResponse> dormitorySchedulePageResponseIPage=page(dormitorySchedulePageRequest);
                if(!dormitorySchedulePageResponseIPage.getRecords().isEmpty()){
                    throw new ServiceException("宿舍已被使用，请选其他使用日期");
                }
                dormitoryScheduleList.add(dormitorySchedule);
            }
        }
        this.saveBatch(dormitoryScheduleList);
        return dormitoryScheduleList;
    }
  /*  private List<Date> getDaysBetweenDates(Date startDate, Date endDate) {
        List<Date> dates = new ArrayList<Date>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);

        while (calendar.getTime().before(endDate)) {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        while(calendar.getTime().equals(endDate)){
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }*/

    @Override
    public IPage<DormitorySchedulePageResponse> page(DormitorySchedulePageRequest params) {
        if(params.getStartDate()!=null && params.getEndDate()!=null){
            List<LocalDate> dateList=getTwoDaysDayDes(params.getStartDate(),params.getEndDate());
            List<DormitorySchedulePageResponse> dormitorySchedulePageResponseList=new ArrayList<>();
            for (int i=0;i<dateList.size();i++) {
                params.setUseDate(dateList.get(i));
                List<DormitorySchedulePageResponse> dormitorySchedulePageResponses=baseMapper.page(params.createMpPage(), params).getRecords();
                dormitorySchedulePageResponseList.addAll(dormitorySchedulePageResponses);
            }
            for (DormitorySchedulePageResponse dormitorySchedulePageResponse:dormitorySchedulePageResponseList) {
                DictPageRequestDTO dictPageRequestDTO=new DictPageRequestDTO();
                if(dormitorySchedulePageResponse.getBuilding()!=null){
                    dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.BULDING.getName());
                    dictPageRequestDTO.setDictKey(dormitorySchedulePageResponse.getBuilding());
                    //根据单元字典码和字典值获取字典名称
                    String building=remoteTrainDictService.getDictValue(dictPageRequestDTO).getRemoteData();
                    dormitorySchedulePageResponse.setBuilding(building);
                }
                if(dormitorySchedulePageResponse.getFloor()!=null){
                    dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.FLOOR.getName());
                    dictPageRequestDTO.setDictKey(dormitorySchedulePageResponse.getFloor());
                    //根据楼层字典码和字典值获取字典名称
                    String floors=remoteTrainDictService.getDictValue(dictPageRequestDTO).getRemoteData();
                    dormitorySchedulePageResponse.setFloor(floors);
                }
                if(dormitorySchedulePageResponse.getRoomType()!=null){
                    dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.DORMITORY.getName());
                    dictPageRequestDTO.setDictKey(dormitorySchedulePageResponse.getRoomType());
                    //根据类别字典码和字典值获取字典名称
                    String roomType=remoteTrainDictService.getDictValue(dictPageRequestDTO).getRemoteData();
                    dormitorySchedulePageResponse.setRoomType(roomType);
                }
            }
            //获取空page对象
            params.setFloor(Constants.DB_FAIL.toString());
            IPage<DormitorySchedulePageResponse> dormitorySchedulePageResponseIPage=baseMapper.page(params.createMpPage(), params);;
            dormitorySchedulePageResponseIPage.setRecords(dormitorySchedulePageResponseList);
            return dormitorySchedulePageResponseIPage;
        }
        IPage<DormitorySchedulePageResponse> dormitorySchedulePageResponseIPage= baseMapper.page(params.createMpPage(), params);
        List<DormitorySchedulePageResponse> dormitorySchedulePageResponseList= dormitorySchedulePageResponseIPage.getRecords();
        for (DormitorySchedulePageResponse dormitorySchedulePageResponse:dormitorySchedulePageResponseList) {
            DictPageRequestDTO dictPageRequestDTO=new DictPageRequestDTO();
            if(dormitorySchedulePageResponse.getBuilding()!=null){
                dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.BULDING.getName());
                dictPageRequestDTO.setDictKey(dormitorySchedulePageResponse.getBuilding());
                //根据单元字典码和字典值获取字典名称
                String building=remoteTrainDictService.getDictValue(dictPageRequestDTO).getRemoteData();
                dormitorySchedulePageResponse.setBuilding(building);
            }
            if(dormitorySchedulePageResponse.getFloor()!=null){
                dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.FLOOR.getName());
                dictPageRequestDTO.setDictKey(dormitorySchedulePageResponse.getFloor());
                //根据楼层字典码和字典值获取字典名称
                String floor=remoteTrainDictService.getDictValue(dictPageRequestDTO).getRemoteData();
                dormitorySchedulePageResponse.setFloor(floor);
            }
            if(dormitorySchedulePageResponse.getRoomType()!=null){
                dictPageRequestDTO.setCode(BuldingFloorRoomTypeEnum.DORMITORY.getName());
                dictPageRequestDTO.setDictKey(dormitorySchedulePageResponse.getRoomType());
                //根据类别字典码和字典值获取字典名称
                String roomType=remoteTrainDictService.getDictValue(dictPageRequestDTO).getRemoteData();
                dormitorySchedulePageResponse.setRoomType(roomType);
            }
        }
        return dormitorySchedulePageResponseIPage;
    }

    @Override
    public DormitoryScheduleDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(DormitoryScheduleUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        DormitorySchedule dormitorySchedule = new DormitorySchedule();
        BeanUtils.copyProperties(params, dormitorySchedule);
        return this.updateById(dormitorySchedule);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public List<DormitoryScheduleDataParseResult> importPre(MultipartFile file) {
       ExcelAnalysisListener dataListener = new ExcelAnalysisListener();
        try {
            EasyExcel.read(file.getInputStream(), DormitoryScheduleExcelData.class, dataListener).ignoreEmptyRow(true).sheet(1).doRead();
        } catch (Exception e) {
            throw new ServiceException("上传的导入模板格式不正确！");
        }
        List<DormitoryScheduleExcelData> dataList = dataListener.getDataList();
        if (CollectionUtils.isEmpty(dataList)){
            throw new ServiceException("解析不到数据！");
        }
        List<DormitoryScheduleDataParseResult> resultList = new ArrayList<>();
        List<DormitoryScheduleDataParseResult> errorList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            DormitoryScheduleExcelData dormitoryScheduleData = dataList.get(i);
            DormitoryScheduleDataParseResult result = new DormitoryScheduleDataParseResult();
            BeanUtils.copyProperties(dormitoryScheduleData, result);
            DormitoryScheduleDataValidate dormitoryScheduleDataValidate = dormitoryScheduleDataValidateFactory.getExcelDormitoryScheduleValidate();
            dormitoryScheduleDataValidate.validate(dormitoryScheduleData);
            boolean passed = dormitoryScheduleDataValidate.passed();
            result.setPassed(passed);
            if (!passed) {
                result.setErrorReasons(dormitoryScheduleDataValidate.getErrorReasons());
                errorList.add(result);
            }
            resultList.add(result);
        }
        return resultList;
    }

    class ExcelAnalysisListener extends AnalysisEventListener<DormitoryScheduleExcelData> {

        private List<DormitoryScheduleExcelData> dataList = new ArrayList<>();

        /**
         * 每解析一行会回调此方法
         * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
         *
         * @param
         * @param
         */
        @Override
        public void invoke(DormitoryScheduleExcelData dormitoryScheduleExcelData, AnalysisContext analysisContext) {
            Integer rowIndex = analysisContext.readRowHolder().getRowIndex();
            dormitoryScheduleExcelData.setRowNum(rowIndex + 1);
            //数据存储到list，供批量处理，或后续自己业务逻辑处理。
            if (StringUtils.hasText(dormitoryScheduleExcelData.getOrgName())) {
                dataList.add(dormitoryScheduleExcelData);
                return;
            }
            //dataList.add(employeeExcelData);
            // 判断是否所有属性都为空
            Map<Integer, Cell> cellMap = analysisContext.readRowHolder().getCellMap();
            cellMap.forEach((k,v) -> {
                ReadCellData cell = (ReadCellData) v;
                if (!CellDataTypeEnum.EMPTY.equals(cell.getType())) {
                    dataList.add(dormitoryScheduleExcelData);
                    return;
                }
            });
        }


        /**
         * 整个excel解析结束会执行此方法
         *
         * @param analysisContext
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        }

        public List<DormitoryScheduleExcelData> getDataList() {
            return dataList;
        }

    }

    /*@Override
    public List<DormitoryStateResponse> getDormitoryState(DormitoryStateRequest params) {
        List<DormitoryStateResponse> dormitoryStateResponseList=new ArrayList<>();
        List<LocalDate> dateList= getTwoDaysDayDes(params.getStartDate(),params.getEndDate());
        DormitoryPageRequest dormitoryPageRequest=new DormitoryPageRequest();
        dormitoryPageRequest.setBuilding(params.getBuilding());
        dormitoryPageRequest.setFloor(params.getFloor());
        dormitoryPageRequest.setRoomNo(params.getRoomNo());
        dormitoryPageRequest.setRoomType(params.getRoomType());
        //获取宿舍列表
        List<DormitoryPageResponse> dormitoryPageResponseList=dormitoryService.pages(dormitoryPageRequest);
        DormitoryStateResponse dormitoryStateResponse=new DormitoryStateResponse();
        for (DormitoryPageResponse dormitoryPageResponse:dormitoryPageResponseList) {
            List<UseDateState> useDateStateList=new ArrayList<>();
            for ( LocalDate date:dateList) {
                UseDateState useDateState=new UseDateState();
                useDateState.setUseDate(date);
                //获取宿舍排期列表
                List<DormitorySchedule> dormitoryScheduleList=this.lambdaQuery().list();
                useDateState.setUseState(Constants.DB_FAIL.toString());
                for (DormitorySchedule dormitorySchedule:dormitoryScheduleList) {
                    if (dormitorySchedule.getBuilding().equals(dormitoryPageResponse.getBuilding()) &&
                            dormitorySchedule.getFloor().equals(dormitoryPageResponse.getFloor()) &&
                            dormitorySchedule.getRoomNo().equals(dormitoryPageResponse.getRoomNo()) &&
                                    dormitorySchedule.getUseDate().equals(date)){
                        useDateState.setUseState(Constants.DB_TRUE.toString());
                    }
                    if(Constants.DB_TRUE.toString().equals(useDateState.getUseState())){
                        useDateState.setOrgName(dormitorySchedule.getOrgName());
                    }
                }
                useDateStateList.add(useDateState);
            }
            dormitoryPageResponse.setUseDateStateList(useDateStateList);
        }
        //分页
        int total=dormitoryPageResponseList.size();
        List<DormitoryPageResponse> pageInfo = page(dormitoryPageResponseList,params.getCurrent(), params.getSize());
        dormitoryStateResponse.setDormitoryPageResponseList(pageInfo);
        dormitoryStateResponse.setTotal(total);
        dormitoryStateResponse.setDormitoryPageResponseList(pageInfo);
        List<UseDateStateCount> useDateStateCountList=new ArrayList<>();
        //统计使用情况
        if(Constants.DB_TRUE.equals(dateList.size())){
            List<String> lists=new ArrayList<>();
            TreeDictDTO treeDictDTO=new TreeDictDTO();
            treeDictDTO.setCode("dormitory");
            //获取房型
            List<TreeDictResultDTO> treeDictResultDTOList=remoteTrainDictService.treeList(treeDictDTO).getRemoteData();
            for (TreeDictResultDTO treeDictResultDTO:treeDictResultDTOList) {
                for (int i=0;i<treeDictResultDTO.getChildren().size();i++){
                    lists.add(treeDictResultDTO.getChildren().get(i).getDictKey());
                }
            }
            for (String list:lists) {
                Integer useNum=Constants.DB_FAIL;
                UseDateStateCount useDateStateCount=new UseDateStateCount();
                dormitoryPageRequest.setRoomType(list);
                //获取宿舍列表
                List<DormitoryPageResponse> dormitoryPageResponseLists=dormitoryService.pages(dormitoryPageRequest);
                Integer totalNum=dormitoryPageResponseLists.size();
                //获取宿舍排期列表
                List<DormitorySchedule> dormitoryScheduleList=this.lambdaQuery().eq(DormitorySchedule::getUseDate,params.getStartDate())
                        .eq(DormitorySchedule::getRoomType,list).list();
                for (DormitoryPageResponse dormitoryPageResponse:dormitoryPageResponseLists) {
                    for (DormitorySchedule dormitorySchedule:dormitoryScheduleList) {
                        if (dormitorySchedule.getBuilding().equals(dormitoryPageResponse.getBuilding()) &&
                                dormitorySchedule.getFloor().equals(dormitoryPageResponse.getFloor()) &&
                                dormitorySchedule.getRoomNo().equals(dormitoryPageResponse.getRoomNo())){
                            useNum++;
                        }
                    }
                }
                Integer freeNum=totalNum-useNum;
                useDateStateCount.setRoomType(list);
                useDateStateCount.setUseNum(useNum);
                useDateStateCount.setFreeNum(freeNum);
                useDateStateCountList.add(useDateStateCount);
            }
        }
        dormitoryStateResponse.setUseDateStateCountList(useDateStateCountList);
        dormitoryStateResponseList.add(dormitoryStateResponse);
        return dormitoryStateResponseList;
    }*/

    private List page(List list, int pageNo, int pageSize){

        List result = new ArrayList();
        if (list != null && list.size() > 0) {
            int allCount = list.size();
            int pageCount = (allCount + pageSize - 1) / pageSize;
            if (pageNo >= pageCount) {
                pageNo = pageCount;
            }
            int start = (pageNo - 1) * pageSize;
            int end = pageNo * pageSize;
            if (end >= allCount) {
                end = allCount;
            }
            for (int i = start; i < end; i++) {
                result.add(list.get(i));
            }
        }
        return (result.size() > 0) ? result : null;
    }

    private List<LocalDate> getTwoDaysDayDes(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dateList = new ArrayList<>();
        try {
            Date dateOne = localDateConvertDate(startDate);
            Date dateTwo = localDateConvertDate(endDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateOne);
            dateList.add(startDate);
            while (dateTwo.after(calendar.getTime())) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                dateList.add(calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateList;
    }

    private Date localDateConvertDate(LocalDate localDate) {
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
    }

}

