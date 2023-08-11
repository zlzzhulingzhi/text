package cn.qbs.wa.train.logistics.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.SocialLoginInfoDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniMemberDTO;
import cn.qbs.wa.teach.out.union.admin.api.RemoteUnionMemberService;
import cn.qbs.wa.teach.utils.CommonSetInfoUtil;
import cn.qbs.wa.train.logistics.entity.MemberClockIn;
import cn.qbs.wa.train.logistics.enums.SiteCodeEnum;
import cn.qbs.wa.train.logistics.mapper.ClazzMapper;
import cn.qbs.wa.train.logistics.mapper.MemberClockInMapper;
import cn.qbs.wa.train.logistics.pojo.clazz.LiteClazzResponse;
import cn.qbs.wa.train.logistics.pojo.clazz.MyClazzListRequest;
import cn.qbs.wa.train.logistics.pojo.memberclockin.*;

import cn.qbs.wa.train.logistics.service.MemberClockInService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 学员打卡记录(MemberClockIn)表服务实现类
 *
 * @author makejava
 * @since 2022-12-26 15:42:22
 */
@Slf4j
@Service("memberClockInService")
public class MemberClockInServiceImpl extends ServiceImpl<MemberClockInMapper, MemberClockIn> implements MemberClockInService {

    @Resource
    ClazzMapper clazzMapper;

    @Resource
    RemoteUnionMemberService remoteUnionMemberService;

    @Resource
    RemoteStudentService remoteStudentService;

    @Override
    public boolean add(MemberClockInAddRequest params) {
        MemberClockIn memberClockIn = BeanUtil.copyProperties(params, MemberClockIn.class);
        memberClockIn.setClockInDate(LocalDate.now());
        memberClockIn.setMemberId(SecurityContextHolder.getUserId());
        return this.save(memberClockIn);
    }

    @Override
    public IPage<MemberClockInPageResponse> page(MemberClockInPageRequest params) {
        IPage<MemberClockInPageResponse> page = baseMapper.page(params.createMpPage(), params);
        List<MemberClockInPageResponse> records = page.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            List<Long> userIdList = records.stream().map(MemberClockInPageResponse::getMemberId).distinct().collect(Collectors.toList());
            IdListRequest idListRequest = new IdListRequest();
            idListRequest.setIdList(userIdList);
            List<UniMemberDTO> userList = remoteUnionMemberService.list(idListRequest).getRemoteData();
            if (CollUtil.isNotEmpty(userList)) {
                CommonSetInfoUtil.setInfo(userList, records,
                        UniMemberDTO::getId, MemberClockInPageResponse::getMemberId,
                        UniMemberDTO::getRealName, MemberClockInPageResponse::getRealName);
            }
        }
        return page;
    }

    @Override
    public IPage<MemberClockInPageResponse> pages(MemberClockInPagesRequest params) {
        if(StringUtils.isNotBlank(params.getStudentName())){
            StudentDTO studentDTO=new StudentDTO();
            studentDTO.setOrgId(SecurityContextHolder.getOrgId());
            studentDTO.setRealName(params.getStudentName());
            List<StudentDTO> studentDTOS=remoteStudentService.getStu(studentDTO).getRemoteData();
            List<Long> memberIdList=new ArrayList<>();
            if(studentDTOS!=null && !studentDTOS.isEmpty()){
                for (StudentDTO stu:studentDTOS) {
                    memberIdList.add(stu.getUserId());
                }
                params.setMemberIdList(memberIdList);
            }else {
                memberIdList.add(Constants.DB_FAIL.longValue());
                params.setMemberIdList(memberIdList);
            }
        }else {
            StudentDTO studentDTO=new StudentDTO();
            studentDTO.setOrgId(SecurityContextHolder.getOrgId());
            List<StudentDTO> studentDTOS=remoteStudentService.getStu(studentDTO).getRemoteData();
            List<Long> memberIdList=new ArrayList<>();
            if(studentDTOS!=null && !studentDTOS.isEmpty()){
                for (StudentDTO stu:studentDTOS) {
                    memberIdList.add(stu.getUserId());
                }
                params.setMemberIdList(memberIdList);
            }else {
                memberIdList.add(Constants.DB_FAIL.longValue());
                params.setMemberIdList(memberIdList);
            }
        }
        IPage<MemberClockInPageResponse> page = baseMapper.pages(params.createMpPage(), params);
        if (CollUtil.isNotEmpty(page.getRecords())) {
            for (MemberClockInPageResponse memberClockInPageResponse:page.getRecords()) {
                SocialLoginInfoDTO socialLoginInfoDTO=new SocialLoginInfoDTO();
                socialLoginInfoDTO.setOrgId(SecurityContextHolder.getOrgId());
                socialLoginInfoDTO.setUnionId(memberClockInPageResponse.getMemberId().toString());
                StudentDTO studentDTO=remoteStudentService.getSocialLoginInfos(socialLoginInfoDTO).getRemoteData();
                if(studentDTO!=null){
                    memberClockInPageResponse.setRealName(studentDTO.getRealName());
                    memberClockInPageResponse.setPhone(studentDTO.getPhone());
                }
            }
        }
        return page;
    }

    @Override
    public MemberClockInDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(MemberClockInUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        MemberClockIn memberClockIn = new MemberClockIn();
        BeanUtils.copyProperties(params, memberClockIn);
        return this.updateById(memberClockIn);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public IPage<MemberClockInPageResponse> pageMember(MemberClockInSelectPageRequest request) {
        MemberClockInPageRequest params = BeanUtil.copyProperties(request, MemberClockInPageRequest.class);
        params.setMemberId(SecurityContextHolder.getUserId());
        return baseMapper.page(params.createMpPage(), params);

    }

    @Override
    public List<MemberClockInCalendarResponse> calendarList(MemberClockInCalendarRequest params) {
        LocalDate start = LocalDate.of(params.getSearchYear(), params.getSearchMonth(), 1);
        LocalDate end = start.with(TemporalAdjusters.lastDayOfMonth());
        params.setMemberId(SecurityContextHolder.getUserId());
        params.setSearchStartDate(start);
        params.setSearchEndDate(end);
        List<MemberClockInCalendarDetailResponse> list = baseMapper.calendarList(params);


        Map<LocalDate, List<MemberClockInCalendarDetailResponse>> dateGroup = new HashMap<>();
        if (CollUtil.isNotEmpty(list)) {
            dateGroup = list.stream().collect(Collectors.groupingBy(MemberClockInCalendarDetailResponse::getClockInDate));
        }
        List<MemberClockInCalendarResponse> totalList = new ArrayList<>();
        long days = start.until(end, ChronoUnit.DAYS);
        for (int i = 0; i <= days; i++) {
            MemberClockInCalendarResponse memberClockInCalendarResponse = new MemberClockInCalendarResponse();
            LocalDate date = start.plusDays(i);
            memberClockInCalendarResponse.setClockInDate(date);
            if (dateGroup.containsKey(date)) {
                memberClockInCalendarResponse.setClockIned(true);
            }
            totalList.add(memberClockInCalendarResponse);
        }
        return totalList;
    }

    @Override
    public MemberClockInCalendarDetailOverViewResponse calendarDetail(MemberClockInCalendarDetailRequest params) {
        MemberClockInCalendarDetailOverViewResponse memberClockInCalendarDetailOverViewResponse = new MemberClockInCalendarDetailOverViewResponse();
        MemberClockInCalendarRequest request = new MemberClockInCalendarRequest();
        request.setMemberId(SecurityContextHolder.getUserId());
        request.setClockInDate(params.getClockInDate());
        List<MemberClockInCalendarDetailResponse> list = baseMapper.calendarList(request);
        if (CollUtil.isNotEmpty(list)) {
            memberClockInCalendarDetailOverViewResponse.setStudyRecordList(
                    list.stream().filter(i -> SiteCodeEnum.JIAOXUELOU.getCode().equals(i.getSiteCode())).collect(Collectors.toList()));
            memberClockInCalendarDetailOverViewResponse.setRestRecordList(
                    list.stream().filter(i -> SiteCodeEnum.SUSHE.getCode().equals(i.getSiteCode())).collect(Collectors.toList())
            );
        }
        return memberClockInCalendarDetailOverViewResponse;
    }

    @Override
    public List<LiteClazzResponse> selectClazzList() {
        MyClazzListRequest myClazzListRequest = new MyClazzListRequest();
        myClazzListRequest.setMemberId(SecurityContextHolder.getUserId());
        return clazzMapper.listMyClazz(myClazzListRequest);
    }


}

