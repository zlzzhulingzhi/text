package cn.qbs.wa.train.logistics.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.train.logistics.mapper.ClassroomMapper;
import cn.qbs.wa.train.logistics.mapper.ClazzLessonArrangeMapper;
import cn.qbs.wa.train.logistics.entity.ClazzLessonArrange;
import cn.qbs.wa.train.logistics.pojo.clazz.IntegrateClazzResponse;
import cn.qbs.wa.train.logistics.pojo.clazzlesson.LessonResponse;
import cn.qbs.wa.train.logistics.pojo.clazzsign.SignInStudentResponse;
import cn.qbs.wa.train.logistics.pojo.lecturer.LecturerClazzMap;
import cn.qbs.wa.train.logistics.service.ClazzLessonArrangeService;
import cn.qbs.wa.train.logistics.pojo.clazzlessonarrange.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.text.Collator;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 班级课程安排(ClazzLessonArrange)表服务实现类
 *
 * @author makejava
 * @since 2023-03-14 09:21:37
 */
@Slf4j
@Service("clazzLessonArrangeService")
public class ClazzLessonArrangeServiceImpl extends ServiceImpl<ClazzLessonArrangeMapper, ClazzLessonArrange> implements ClazzLessonArrangeService {


    @Override
    public boolean addBatch(List<ClazzLessonArrangeAddRequest> params) {
        List<ClazzLessonArrange> clazzLessonArranges=new ArrayList<>();
        for (ClazzLessonArrangeAddRequest param:params) {
            param.setOrgId(SecurityContextHolder.getOrgId());
            ClazzLessonArrange clazzLessonArrange = new ClazzLessonArrange();
            BeanUtils.copyProperties(param, clazzLessonArrange);
            clazzLessonArranges.add(clazzLessonArrange);
        }
        return this.saveBatch(clazzLessonArranges);
    }

    @Override
    public boolean add(ClazzLessonArrangeAddRequest params) {
        params.setOrgId(SecurityContextHolder.getOrgId());
        ClazzLessonArrange clazzLessonArrange = new ClazzLessonArrange();
        BeanUtils.copyProperties(params, clazzLessonArrange);
        return this.save(clazzLessonArrange);
    }

    @Override
    public IPage<ClazzLessonArrangePageResponse> page(ClazzLessonArrangePageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public ClazzLessonArrangeDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(ClazzLessonArrangeUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        ClazzLessonArrange clazzLessonArrange = new ClazzLessonArrange();
        BeanUtils.copyProperties(params, clazzLessonArrange);
        return this.updateById(clazzLessonArrange);
    }

    @Override
    public boolean sort(ClazzLessonArrangeUpdateRequest params) {
        if(CollectionUtils.isNotEmpty(params.getSortedIds())){
            for (int i=0;i<params.getSortedIds().size();i++){
                ClazzLessonArrange clazzLessonArrange=new ClazzLessonArrange();
                clazzLessonArrange.setSort(i+1);
                clazzLessonArrange.setId(params.getSortedIds().get(i));
                this.updateById(clazzLessonArrange);
            }
        }
        return true;
    }

    @Override
    public List<ClazzLessonArrangeListResponse> listV2(ClazzLessonArrangePageRequest request) {
        List<ClazzLessonArrangeListResponse> clazzLessonArrangeListResponses=baseMapper.listV2(request);
        return clazzLessonArrangeListResponses;
    }

    @Override
    public List<ClazzLessonArrangeListResponse> listV3(ClazzLessonArrangePageRequest request) {
        List<ClazzLessonArrangeListResponse> clazzLessonArrangeListResponseList=new ArrayList<>();
        List<LocalDate> lessonDate=getTwoDaysDayDes(request.getStartDate().toLocalDate(), request.getEndDate().toLocalDate());
        for (LocalDate l:lessonDate){
            ClazzLessonArrangeListResponse clazzLessonArrangeListResponse=new ClazzLessonArrangeListResponse();
            clazzLessonArrangeListResponse.setLessonDate(l.toString());
            clazzLessonArrangeListResponse.setLessonList(new ArrayList<>());
            clazzLessonArrangeListResponseList.add(clazzLessonArrangeListResponse);
        }
        List<ClazzLessonArrangeListResponse> clazzLessonArrangeListResponses=baseMapper.listV3(request);
        if(CollectionUtils.isNotEmpty(clazzLessonArrangeListResponses)){
            for (ClazzLessonArrangeListResponse clazzLessonArrangeListResponse:clazzLessonArrangeListResponses) {
                clazzLessonArrangeListResponse.setLessonDate(clazzLessonArrangeListResponse.getStartDate().toString().substring(0,10));
                String lessonTime=clazzLessonArrangeListResponse.getStartDate().toLocalTime().toString()+"-"+
                        clazzLessonArrangeListResponse.getEndDate().toLocalTime().toString();
                clazzLessonArrangeListResponse.setLessonTime(lessonTime);
            }
            Map<String, List<ClazzLessonArrangeListResponse>> map =  clazzLessonArrangeListResponses.stream().
                    collect(Collectors.groupingBy(ClazzLessonArrangeListResponse::getLessonDate));
            for (ClazzLessonArrangeListResponse clazzLessonArrangeListResponse:clazzLessonArrangeListResponseList){
                List<ClazzLessonArrangeListResponse> clazzLessonArrangeListResponseList1=map.get(clazzLessonArrangeListResponse.getLessonDate());
                List<LessonResponse> list = BeanUtil.copyToList(clazzLessonArrangeListResponseList1, LessonResponse.class);
                if(CollectionUtils.isNotEmpty(list)){
                    clazzLessonArrangeListResponse.setLessonList(list);
                }
            }
        }
        //处理一节课多天
        request.setStartDate(null);
        request.setEndDate(null);
        List<ClazzLessonArrangeListResponse> clazzLessonArrangeListResponses2=baseMapper.listV3(request);
        List<ClazzLessonArrangeListResponse> clazzLessonArrangeListResponseList2=new ArrayList<>();
        if(CollectionUtils.isNotEmpty(clazzLessonArrangeListResponses2)){
            for (ClazzLessonArrangeListResponse clazzLessonArrangeListResponse2:clazzLessonArrangeListResponses2) {
                if(!clazzLessonArrangeListResponse2.getStartDate().equals(clazzLessonArrangeListResponse2.getEndDate())){
                    List<LocalDate> lessonDate2=getTwoDaysDayDes(clazzLessonArrangeListResponse2.getStartDate().toLocalDate(),
                            clazzLessonArrangeListResponse2.getEndDate().toLocalDate());
                    clazzLessonArrangeListResponse2.setLessonDate(clazzLessonArrangeListResponse2.getStartDate().toString().substring(0,10));
                    String lessonTime=clazzLessonArrangeListResponse2.getStartDate().toLocalTime().toString()+"-"+
                            clazzLessonArrangeListResponse2.getEndDate().toLocalTime().toString();
                    clazzLessonArrangeListResponse2.setLessonTime(lessonTime);
                    for (LocalDate localDate:lessonDate2){
                        ClazzLessonArrangeListResponse clazzLessonArrangeListResponse=new ClazzLessonArrangeListResponse();
                        BeanUtils.copyProperties(clazzLessonArrangeListResponse2,clazzLessonArrangeListResponse);
                        clazzLessonArrangeListResponse.setLessonDate(localDate.toString());
                        clazzLessonArrangeListResponseList2.add(clazzLessonArrangeListResponse);
                    }
                }

            }
            if(CollectionUtils.isNotEmpty(clazzLessonArrangeListResponseList2)){
                Map<String, List<ClazzLessonArrangeListResponse>> map2 =  clazzLessonArrangeListResponseList2.stream().
                        collect(Collectors.groupingBy(ClazzLessonArrangeListResponse::getLessonDate));
                for (ClazzLessonArrangeListResponse clazzLessonArrangeListResponse:clazzLessonArrangeListResponseList){
                    List<LessonResponse> list = BeanUtil.copyToList(map2.get(clazzLessonArrangeListResponse.getLessonDate()), LessonResponse.class);
                    if(CollectionUtils.isNotEmpty(list)){
                        list.addAll(clazzLessonArrangeListResponse.getLessonList());
                        List<LessonResponse> newList = list.stream().distinct().collect(Collectors.toList());
                        newList.sort(Comparator.comparing(LessonResponse::getLessonTime));
                        clazzLessonArrangeListResponse.setLessonList(newList);
                    }

                }
            }
        }
        return clazzLessonArrangeListResponseList;
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

    @Override
    public ClazzLessonArrangeWeekResponse getWeek(ClazzLessonArrangePageRequest request) {
        ClazzLessonArrangeWeekResponse clazzLessonArrangeWeekResponse=baseMapper.getWeek(request);
        if(clazzLessonArrangeWeekResponse==null){
           return new ClazzLessonArrangeWeekResponse();
        }
        clazzLessonArrangeWeekResponse.setCurrentDate(LocalDate.now());
        return clazzLessonArrangeWeekResponse;
    }

    @Override
    public List<ClazzLessonArrangeListResponse> listV4(ClazzLessonArrangePageRequest request) {
        List<ClazzLessonArrangeListResponse> clazzLessonArrangeListResponseList=new ArrayList<>();
        List<ClazzLessonArrangeListResponse> clazzLessonArrangeListResponses=baseMapper.listV3(request);
        if(CollectionUtils.isNotEmpty(clazzLessonArrangeListResponses)){
            for (ClazzLessonArrangeListResponse clazzLessonArrangeListResponse:clazzLessonArrangeListResponses) {
                clazzLessonArrangeListResponse.setLessonDate(clazzLessonArrangeListResponse.getStartDate().toLocalDate().toString());
            }
            Map<String, List<ClazzLessonArrangeListResponse>> map =  clazzLessonArrangeListResponses.stream().
                    collect(Collectors.groupingBy(ClazzLessonArrangeListResponse::getLessonDate));
            Iterator<Map.Entry<String, List<ClazzLessonArrangeListResponse>>> entryIterator= map.entrySet().iterator();
            while(entryIterator.hasNext()){
                Map.Entry<String, List<ClazzLessonArrangeListResponse>> entry=entryIterator.next();
                ClazzLessonArrangeListResponse clazzLessonArrangeListResponse=new ClazzLessonArrangeListResponse();
                clazzLessonArrangeListResponse.setLessonDate(entry.getKey());
                clazzLessonArrangeListResponse.setLessonNameList(entry.getValue().stream().map(ClazzLessonArrangeListResponse::getLessonName).collect(Collectors.toList()));
                clazzLessonArrangeListResponse.setLessonIdList(entry.getValue().stream().map(ClazzLessonArrangeListResponse::getLessonId).collect(Collectors.toList()));
                clazzLessonArrangeListResponseList.add(clazzLessonArrangeListResponse);
            }
            clazzLessonArrangeListResponseList=clazzLessonArrangeListResponseList.stream().
                    sorted(Comparator.comparing(ClazzLessonArrangeListResponse::getLessonDate)).collect(Collectors.toList());
            for (ClazzLessonArrangeListResponse clazzLessonArrangeListResponse:clazzLessonArrangeListResponseList) {
                clazzLessonArrangeListResponse.setLessonIdList(new ArrayList<>(new HashSet<>(clazzLessonArrangeListResponse.getLessonIdList())));
                clazzLessonArrangeListResponse.setLessonNameList(new ArrayList<>(new HashSet<>(clazzLessonArrangeListResponse.getLessonNameList())));
            }
        }
        return clazzLessonArrangeListResponseList;
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public List<LecturerClazzMap> queryClazzLastByLecturerIds(List<Long> lecturerIds) {
        return baseMapper.queryClazzLastByLecturerIds(lecturerIds);
    }

    @Override
    public List<IntegrateClazzResponse> listClazzByLecturerId(Long lecturerId) {
        return baseMapper.listClazzByLecturerId(lecturerId);
    }
}

