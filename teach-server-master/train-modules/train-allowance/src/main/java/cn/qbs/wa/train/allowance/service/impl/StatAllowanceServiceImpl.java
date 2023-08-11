package cn.qbs.wa.train.allowance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.enums.AllowanceFeeEnum;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniMemberDTO;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniOrgInfoResponseDTO;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniOrgSearchDTO;
import cn.qbs.wa.teach.out.union.admin.api.RemoteUnionMemberService;
import cn.qbs.wa.teach.out.union.admin.api.RemoteUnionOrgService;
import cn.qbs.wa.train.allowance.entity.StatAllowanceYearly;
import cn.qbs.wa.train.allowance.enums.ApplyTypeEnum;
import cn.qbs.wa.train.allowance.enums.FlowStatusEnum;
import cn.qbs.wa.train.allowance.mapper.StatAllowanceMapper;
import cn.qbs.wa.train.allowance.mapper.StatAllowanceYearlyMapper;
import cn.qbs.wa.train.allowance.pojo.stat.*;
import cn.qbs.wa.train.allowance.service.StatAllowanceService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yjx
 */
@Service("statAllowanceService")
public class StatAllowanceServiceImpl implements StatAllowanceService {

    @Resource
    private StatAllowanceMapper statAllowanceMapper;

    @Resource
    private StatAllowanceYearlyMapper statAllowanceYearlyMapper;

    @Resource
    private RemoteUnionMemberService remoteUnionMemberService;

    @Resource
    private RemoteUnionOrgService remoteUnionOrgService;

    @Override
    public SummaryAllowanceResponse orgAllowanceSummary(OrgAllowancePageRequest params) {
        SummaryAllowanceResponse summaryAllowanceResponse=new SummaryAllowanceResponse();
        if(params.getDateStart()==null || params.getDateEnd()==null){
            return summaryAllowanceResponse;
        }
        if(params.getFlowStatus()==null){
            List<Integer> flowStatusList=new ArrayList<>();
            flowStatusList.add(FlowStatusEnum.ING.getCode());
            flowStatusList.add(FlowStatusEnum.PASS.getCode());
            params.setFlowStatusList(flowStatusList);
        }
        if(ApplyTypeEnum.ACTIVITY.getCode().equals(params.getApplyType()) || params.getApplyType()==null || params.getApplyType().isEmpty()){
            summaryAllowanceResponse=statAllowanceMapper.orgAllowanceSummaryActivity(params);
        }
        if(ApplyTypeEnum.ALLOWANCE.getCode().equals(params.getApplyType()) || params.getApplyType()==null || params.getApplyType().isEmpty()){
            SummaryAllowanceResponse summaryAllowanceResponse1=statAllowanceMapper.orgAllowanceSummaryCourse(params);
            if(summaryAllowanceResponse1==null){
                return summaryAllowanceResponse;
            }
            if(params.getApplyType()==null || params.getApplyType().isEmpty()){
                if(summaryAllowanceResponse==null){
                    summaryAllowanceResponse=new SummaryAllowanceResponse();
                    summaryAllowanceResponse.setTotalFee(summaryAllowanceResponse1.getTotalFee());
                }else {
                    summaryAllowanceResponse.setTotalFee(summaryAllowanceResponse.getTotalFee().add(summaryAllowanceResponse1.getTotalFee()));
                }
            }else {
                summaryAllowanceResponse.setTotalFee(summaryAllowanceResponse1.getTotalFee());
            }
            summaryAllowanceResponse.setTotalStudentNum(summaryAllowanceResponse1.getTotalStudentNum());
            SummaryAllowanceResponse summaryAllowanceResponse3=statAllowanceMapper.orgAllowanceSummaryCourse2(params);
            summaryAllowanceResponse.setTotalLessonNum(summaryAllowanceResponse3.getTotalLessonNum());
            List<AllowanceFeeResponse> summaryAllowanceResponse2=statAllowanceMapper.orgAllowanceSummaryCourseFee(params);
            for (AllowanceFeeResponse allowanceFeeResponse:summaryAllowanceResponse2) {
                if(AllowanceFeeEnum.PXCSZZ.getCode().equals(allowanceFeeResponse.getItemCode())){
                    summaryAllowanceResponse.setSiteFundFee(allowanceFeeResponse.getTotalAllowanceFee());
                }
                if(AllowanceFeeEnum.XYJCZZ.getCode().equals(allowanceFeeResponse.getItemCode())){
                    summaryAllowanceResponse.setMealFundFee(allowanceFeeResponse.getTotalAllowanceFee());
                }
                if(AllowanceFeeEnum.XYZSZZ.getCode().equals(allowanceFeeResponse.getItemCode())){
                    summaryAllowanceResponse.setRoomFundFee(allowanceFeeResponse.getTotalAllowanceFee());
                }
                if(AllowanceFeeEnum.XYJTZZ.getCode().equals(allowanceFeeResponse.getItemCode())){
                    summaryAllowanceResponse.setTrafficFundFee(allowanceFeeResponse.getTotalAllowanceFee());
                }
                if(AllowanceFeeEnum.XYXFZZ.getCode().equals(allowanceFeeResponse.getItemCode())){
                    summaryAllowanceResponse.setStudyFundFee(allowanceFeeResponse.getTotalAllowanceFee());
                }
            }
        }
        return summaryAllowanceResponse;
    }

    @Override
    public IPage<OrgAllowanceResponse> orgAllowancePage(OrgAllowancePageRequest params) {
        IPage<OrgAllowanceResponse> orgAllowanceResponseIPage=null;
        if(params.getDateStart()==null || params.getDateEnd()==null){
            return orgAllowanceResponseIPage;
        }
        if(params.getFlowStatus()==null){
            List<Integer> flowStatusList=new ArrayList<>();
            flowStatusList.add(FlowStatusEnum.ING.getCode());
            flowStatusList.add(FlowStatusEnum.PASS.getCode());
            params.setFlowStatusList(flowStatusList);
        }
        if(ApplyTypeEnum.ACTIVITY.getCode().equals(params.getApplyType())){
            orgAllowanceResponseIPage=statAllowanceMapper.pageActivity(params.createMpPage(),params);
        }
        if(ApplyTypeEnum.ALLOWANCE.getCode().equals(params.getApplyType())){
            orgAllowanceResponseIPage=statAllowanceMapper.pageCourse(params.createMpPage(),params);
            for (OrgAllowanceResponse orgAllowanceResponse:orgAllowanceResponseIPage.getRecords()) {
                params.setOrgId(orgAllowanceResponse.getOrgId());
                IPage<OrgAllowanceResponse> orgAllowanceResponseIPage1=statAllowanceMapper.pageCourse2(params.createMpPage(),params);
                orgAllowanceResponse.setTotalStudentNum(orgAllowanceResponseIPage1.getRecords().get(Constants.DB_FAIL).getTotalStudentNum());
                orgAllowanceResponse.setTotalFee(orgAllowanceResponseIPage1.getRecords().get(Constants.DB_FAIL).getTotalFee());
                List<AllowanceFeeResponse> summaryAllowanceResponse2=statAllowanceMapper.pageCourseFee(params);
                for (AllowanceFeeResponse allowanceFeeResponse:summaryAllowanceResponse2) {
                    if(AllowanceFeeEnum.PXCSZZ.getCode().equals(allowanceFeeResponse.getItemCode())){
                        orgAllowanceResponse.setSiteFundFee(allowanceFeeResponse.getTotalAllowanceFee());
                    }
                    if(AllowanceFeeEnum.XYJCZZ.getCode().equals(allowanceFeeResponse.getItemCode())){
                        orgAllowanceResponse.setMealFundFee(allowanceFeeResponse.getTotalAllowanceFee());
                    }
                    if(AllowanceFeeEnum.XYZSZZ.getCode().equals(allowanceFeeResponse.getItemCode())){
                        orgAllowanceResponse.setRoomFundFee(allowanceFeeResponse.getTotalAllowanceFee());
                    }
                    if(AllowanceFeeEnum.XYJTZZ.getCode().equals(allowanceFeeResponse.getItemCode())){
                        orgAllowanceResponse.setTrafficFundFee(allowanceFeeResponse.getTotalAllowanceFee());
                    }
                    if(AllowanceFeeEnum.XYXFZZ.getCode().equals(allowanceFeeResponse.getItemCode())){
                        orgAllowanceResponse.setStudyFundFee(allowanceFeeResponse.getTotalAllowanceFee());
                    }
                }

            }
        }
        if(params.getApplyType()==null || params.getApplyType().isEmpty()){
            orgAllowanceResponseIPage=statAllowanceMapper.pageActivityAndCourse(params.createMpPage(),params);
            for (OrgAllowanceResponse orgAllowanceResponse:orgAllowanceResponseIPage.getRecords()) {
                params.setOrgId(orgAllowanceResponse.getOrgId());
                if(ApplyTypeEnum.ALLOWANCE.getCode().equals(orgAllowanceResponse.getApplyType())){
                    IPage<OrgAllowanceResponse> orgAllowanceResponseIPage1=statAllowanceMapper.pageCourse2(params.createMpPage(),params);
                    orgAllowanceResponse.setTotalStudentNum(orgAllowanceResponseIPage1.getRecords().get(Constants.DB_FAIL).getTotalStudentNum());
                    orgAllowanceResponse.setTotalFee(orgAllowanceResponseIPage1.getRecords().get(Constants.DB_FAIL).getTotalFee());
                    List<AllowanceFeeResponse> summaryAllowanceResponse2=statAllowanceMapper.pageCourseFee(params);
                    for (AllowanceFeeResponse allowanceFeeResponse:summaryAllowanceResponse2) {
                        if(AllowanceFeeEnum.PXCSZZ.getCode().equals(allowanceFeeResponse.getItemCode())){
                            orgAllowanceResponse.setSiteFundFee(allowanceFeeResponse.getTotalAllowanceFee());
                        }
                        if(AllowanceFeeEnum.XYJCZZ.getCode().equals(allowanceFeeResponse.getItemCode())){
                            orgAllowanceResponse.setMealFundFee(allowanceFeeResponse.getTotalAllowanceFee());
                        }
                        if(AllowanceFeeEnum.XYZSZZ.getCode().equals(allowanceFeeResponse.getItemCode())){
                            orgAllowanceResponse.setRoomFundFee(allowanceFeeResponse.getTotalAllowanceFee());
                        }
                        if(AllowanceFeeEnum.XYJTZZ.getCode().equals(allowanceFeeResponse.getItemCode())){
                            orgAllowanceResponse.setTrafficFundFee(allowanceFeeResponse.getTotalAllowanceFee());
                        }
                        if(AllowanceFeeEnum.XYXFZZ.getCode().equals(allowanceFeeResponse.getItemCode())){
                            orgAllowanceResponse.setStudyFundFee(allowanceFeeResponse.getTotalAllowanceFee());
                        }
                    }
                }
            }
        }
        return orgAllowanceResponseIPage;
    }

    @Override
    public IPage<OrgAllowanceSubResponse> orgAllowanceSubPage(OrgAllowancePageRequest params) {
        IPage<OrgAllowanceSubResponse> orgAllowanceSubResponseIPage=null;
        if(params.getFlowStatus()==null){
            List<Integer> flowStatusList=new ArrayList<>();
            flowStatusList.add(FlowStatusEnum.ING.getCode());
            flowStatusList.add(FlowStatusEnum.PASS.getCode());
            params.setFlowStatusList(flowStatusList);
        }
        if(ApplyTypeEnum.ACTIVITY.getCode().equals(params.getApplyType())){
            orgAllowanceSubResponseIPage=statAllowanceMapper.pageSubActivity(params.createMpPage(),params);
        }
        if(ApplyTypeEnum.ALLOWANCE.getCode().equals(params.getApplyType())){
            orgAllowanceSubResponseIPage=statAllowanceMapper.pageSubCourse(params.createMpPage(),params);
            for (OrgAllowanceSubResponse orgAllowanceSubResponse:orgAllowanceSubResponseIPage.getRecords()) {
                params.setOrgId(orgAllowanceSubResponse.getId());
                List<AllowanceFeeResponse> allowanceFeeResponseList=statAllowanceMapper.pageSubCourseFee(params);
                for (AllowanceFeeResponse allowanceFeeResponse:allowanceFeeResponseList) {
                    if(AllowanceFeeEnum.PXCSZZ.getCode().equals(allowanceFeeResponse.getItemCode())){
                        orgAllowanceSubResponse.setSiteFundFee(allowanceFeeResponse.getTotalAllowanceFee());
                    }
                    if(AllowanceFeeEnum.XYJCZZ.getCode().equals(allowanceFeeResponse.getItemCode())){
                        orgAllowanceSubResponse.setMealFundFee(allowanceFeeResponse.getTotalAllowanceFee());
                    }
                    if(AllowanceFeeEnum.XYZSZZ.getCode().equals(allowanceFeeResponse.getItemCode())){
                        orgAllowanceSubResponse.setRoomFundFee(allowanceFeeResponse.getTotalAllowanceFee());
                    }
                    if(AllowanceFeeEnum.XYJTZZ.getCode().equals(allowanceFeeResponse.getItemCode())){
                        orgAllowanceSubResponse.setTrafficFundFee(allowanceFeeResponse.getTotalAllowanceFee());
                    }
                    if(AllowanceFeeEnum.XYXFZZ.getCode().equals(allowanceFeeResponse.getItemCode())){
                        orgAllowanceSubResponse.setStudyFundFee(allowanceFeeResponse.getTotalAllowanceFee());
                    }
                }

            }
        }

        return orgAllowanceSubResponseIPage;
    }

    @Override
    public FeeChartsResponse yearlyFeeChart(StatAllowanceYearlyPageRequest request) {
        FeeChartsResponse feeChartsResponse=new FeeChartsResponse();
        List<StatAllowanceYearly> statAllowanceYearlyList=new ArrayList<>();
        if(request.getStartYear()!=null && request.getEndYear()!=null){
            statAllowanceYearlyList=statAllowanceYearlyMapper.selectList(new LambdaQueryWrapper<StatAllowanceYearly>().
                    ge(StatAllowanceYearly::getYear,request.getStartYear()).le(StatAllowanceYearly::getYear,request.getEndYear()).orderByAsc(StatAllowanceYearly::getYear));
        }else {
           statAllowanceYearlyList=statAllowanceYearlyMapper.selectList(new LambdaQueryWrapper<StatAllowanceYearly>().orderByAsc(StatAllowanceYearly::getYear));
        }
        if(statAllowanceYearlyList!=null && !statAllowanceYearlyList.isEmpty()){
            List<Integer> years=new ArrayList<>();
            FeesResponse feesResponse=new FeesResponse();
            List<BigDecimal> totalFeeList=new ArrayList<>();
            List<BigDecimal> totalActivityFeeList=new ArrayList<>();
            List<BigDecimal> totalCourseFeeList=new ArrayList<>();
            List<BigDecimal> siteFundFeeList=new ArrayList<>();
            List<BigDecimal> mealFundFeeList=new ArrayList<>();
            List<BigDecimal> roomFundFeeList=new ArrayList<>();
            List<BigDecimal> trafficFundFeeList=new ArrayList<>();
            List<BigDecimal> studyFundFeeList=new ArrayList<>();
            for (StatAllowanceYearly statAllowanceYearly:statAllowanceYearlyList) {
                years.add(statAllowanceYearly.getYear());
                totalActivityFeeList.add(statAllowanceYearly.getTotalActivityFee());
                totalCourseFeeList.add(statAllowanceYearly.getTotalCourseFee());
                siteFundFeeList.add(statAllowanceYearly.getSiteFundFee());
                mealFundFeeList.add(statAllowanceYearly.getMealFundFee());
                roomFundFeeList.add(statAllowanceYearly.getRoomFundFee());
                trafficFundFeeList.add(statAllowanceYearly.getTrafficFundFee());
                studyFundFeeList.add(statAllowanceYearly.getStudyFundFee());
                totalFeeList.add(statAllowanceYearly.getTotalActivityFee().add(statAllowanceYearly.getTotalCourseFee()));
            }
            feeChartsResponse.setYears(years);
            feesResponse.setTotalFeeList(totalFeeList);
            feesResponse.setTotalActivityFeeList(totalActivityFeeList);
            feesResponse.setTotalCourseFeeList(totalCourseFeeList);
            feesResponse.setSiteFundFeeList(siteFundFeeList);
            feesResponse.setMealFundFeeList(mealFundFeeList);
            feesResponse.setRoomFundFeeList(roomFundFeeList);
            feesResponse.setTrafficFundFeeList(trafficFundFeeList);
            feesResponse.setStudyFundFeeList(studyFundFeeList);
            feeChartsResponse.setItemList(feesResponse);
        }
        return feeChartsResponse;
    }

    @Override
    public StudyChartsResponse yearlyStudyChart(StatAllowanceYearlyPageRequest request) {
        StudyChartsResponse studyChartsResponse=new StudyChartsResponse();
        List<StatAllowanceYearly> statAllowanceYearlyList=new ArrayList<>();
        if(request.getStartYear()!=null && request.getEndYear()!=null){
            statAllowanceYearlyList=statAllowanceYearlyMapper.selectList(new LambdaQueryWrapper<StatAllowanceYearly>().
                    ge(StatAllowanceYearly::getYear,request.getStartYear()).le(StatAllowanceYearly::getYear,request.getEndYear()).orderByAsc(StatAllowanceYearly::getYear));
        }else {
            statAllowanceYearlyList=statAllowanceYearlyMapper.selectList(new LambdaQueryWrapper<StatAllowanceYearly>().orderByAsc(StatAllowanceYearly::getYear));
        }
        if(statAllowanceYearlyList!=null && !statAllowanceYearlyList.isEmpty()){
            List<Integer> years=new ArrayList<>();
            StudysResponse studysResponse=new StudysResponse();
            List<Integer> totalLessonNumList=new ArrayList<>();
            List<Integer> totalStudentNumList=new ArrayList<>();
            for (StatAllowanceYearly statAllowanceYearly:statAllowanceYearlyList) {
                years.add(statAllowanceYearly.getYear());
                totalLessonNumList.add(statAllowanceYearly.getTotalLessonNum());
                totalStudentNumList.add(statAllowanceYearly.getTotalStudentNum());
            }
            studyChartsResponse.setYears(years);
            studysResponse.setTotalLessonNumList(totalLessonNumList);
            studysResponse.setTotalStudentNumList(totalStudentNumList);
            studyChartsResponse.setItemList(studysResponse);
        }
        return studyChartsResponse;
    }

    @Override
    public SummaryAllowanceResponse studentAllowanceSummary(StudentAllowancePageRequest params) {
        return statAllowanceMapper.studentAllowanceSummary(params);
    }

    @Override
    public IPage<StudentAllowanceResponse> studentAllowancePage(StudentAllowancePageRequest params) {
        Page<StudentAllowanceResponse> page = params.createMpPage();
        long count = statAllowanceMapper.studentAllowancePageCount(params);
        if (count > 0) {
            page.setTotal(count);
            page.setSearchCount(false);
            statAllowanceMapper.studentAllowancePage(page, params);
            List<StudentAllowanceResponse> records = page.getRecords();
            if (!records.isEmpty()) {
                // 查询学员姓名
                List<Long> memberIds = records.stream().map(StudentAllowanceResponse::getMemberId).collect(Collectors.toList());
                List<UniMemberDTO> memberList = remoteUnionMemberService.list(new IdListRequest(memberIds, null)).getRemoteData();
                Map<Long, UniMemberDTO> memberMap = CollUtil.isEmpty(memberList) ? Collections.emptyMap() : memberList.stream().collect(Collectors.toMap(UniMemberDTO::getId, Function.identity()));
                for (StudentAllowanceResponse record : records) {
                    Optional.ofNullable(memberMap.get(record.getMemberId())).ifPresent(m -> record.setStudentPhone(m.getPhone()));
                }
            }
        }
        return page;
    }

    @Override
    public IPage<StudentAllowanceSubResponse> studentAllowanceSubPage(StudentAllowancePageRequest params) {
        return statAllowanceMapper.studentAllowanceSubPage(params.createMpPage(), params);
    }

    @Override
    public FeeResponse pieFeeChart(YearlyPieRequest yearlyPieRequest) {
        StatAllowanceYearly statAllowanceYearly=statAllowanceYearlyMapper.selectOne(new LambdaQueryWrapper<StatAllowanceYearly>().
                eq(StatAllowanceYearly::getYear,yearlyPieRequest.getYear()));
        if(statAllowanceYearly==null){
          return null;
        }
        return BeanUtil.copyProperties(statAllowanceYearly, FeeResponse.class);
    }

    @Override
    public StudyResponse pieStudyChart(YearlyPieRequest yearlyPieRequest) {
        StatAllowanceYearly statAllowanceYearly=statAllowanceYearlyMapper.selectOne(new LambdaQueryWrapper<StatAllowanceYearly>().
                eq(StatAllowanceYearly::getYear,yearlyPieRequest.getYear()));
        if(statAllowanceYearly==null){
            return null;
        }
        return BeanUtil.copyProperties(statAllowanceYearly, StudyResponse.class);
    }

    @Override
    public IPage<StatAllowanceYearlyResponse> yearlyList(StatAllowanceYearlyPageRequest request) {
        return statAllowanceYearlyMapper.selectByCategory(request.createMpPage(),request);
    }

    @Override
    public List<UniOrgInfoResponseDTO> orgList(StatAllowanceTypeRequest params) {
        List<Long> orgIds;
        if (ApplyTypeEnum.ACTIVITY.getCode().equals(params.getApplyType())) {
            orgIds = statAllowanceMapper.listActivityAllowanceOrg();
        } else if (ApplyTypeEnum.ALLOWANCE.getCode().equals(params.getApplyType())) {
            orgIds = statAllowanceMapper.listCourseAllowanceOrg();
        } else {
            orgIds = statAllowanceMapper.listAllowanceOrg();
        }
        if (CollUtil.isNotEmpty(orgIds)) {
            UniOrgSearchDTO searchDTO = new UniOrgSearchDTO();
            searchDTO.setIds(orgIds);
            return remoteUnionOrgService.list(searchDTO).getRemoteData();
        }
        return Collections.emptyList();
    }

    @Override
    public SummaryAllowanceResponse classAllowanceSummary(ClazzAllowancePageRequest params) {
        SummaryAllowanceResponse summary = statAllowanceMapper.classAllowanceSummary(params);
        if (summary != null) {
            SummaryStudentResponse stuSummary = statAllowanceMapper.classStudentSummary(params);
            summary.setTotalLessonNum(stuSummary.getTotalLessonNum());
            summary.setTotalStudentNum(stuSummary.getTotalStudentNum());
        }
        return summary;
    }

    @Override
    public IPage<ClazzAllowanceResponse> classAllowancePage(ClazzAllowancePageRequest params) {
        Page<ClazzAllowanceResponse> page = params.createMpPage();
        // 分页总数
        long count = statAllowanceMapper.classAllowancePageCount(params);
        if (count > 0) {
            page.setTotal(count);
            page.setSearchCount(false);
            statAllowanceMapper.classAllowancePage(page, params);
            if (!page.getRecords().isEmpty()) {
                List<Long> allowanceIds = CollStreamUtil.toList(page.getRecords(), ClazzAllowanceResponse::getId);
                // 分别统计学员
                List<ClazzAllowanceStuResponse> stuSummaryList = statAllowanceMapper.studentLessonSummaryByIds(allowanceIds);
                if (!stuSummaryList.isEmpty()) {
                    Map<Long, SummaryStudentResponse> map = CollStreamUtil.toMap(stuSummaryList, ClazzAllowanceStuResponse::getId, e -> BeanUtil.copyProperties(e, SummaryStudentResponse.class));
                    page.getRecords().forEach(e -> {
                        SummaryStudentResponse stuSummary = map.get(e.getId());
                        if (stuSummary != null) {
                            e.setTotalLessonNum(stuSummary.getTotalLessonNum());
                            e.setTotalStudentNum(stuSummary.getTotalStudentNum());
                        } else {
                            e.setTotalLessonNum(0);
                            e.setTotalStudentNum(0);
                        }
                    });
                }
            }
        }
        return page;
    }

    @Override
    public IPage<ClazzAllowanceSubResponse> classAllowanceSubPage(ClazzAllowanceSubPageRequest params) {
        return statAllowanceMapper.classAllowanceSubPage(params.createMpPage(), params.getId());
    }

    @Override
    public SummaryAllowanceResponse activityAllowanceSummary(ClazzAllowancePageRequest params) {
        return statAllowanceMapper.activityAllowanceSummary(params);
    }

    @Override
    public IPage<ActivityAllowanceResponse> activityAllowancePage(ClazzAllowancePageRequest params) {
        return statAllowanceMapper.activityAllowancePage(params.createMpPage(), params);
    }

}
