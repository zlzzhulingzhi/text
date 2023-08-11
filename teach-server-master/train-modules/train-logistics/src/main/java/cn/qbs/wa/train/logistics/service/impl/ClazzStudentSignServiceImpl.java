package cn.qbs.wa.train.logistics.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniMemberDTO;
import cn.qbs.wa.train.logistics.entity.ClazzStudent;
import cn.qbs.wa.train.logistics.entity.ClazzStudentSign;
import cn.qbs.wa.train.logistics.enums.SignInStatusEnum;
import cn.qbs.wa.train.logistics.mapper.ClazzStudentMapper;
import cn.qbs.wa.train.logistics.mapper.ClazzStudentSignMapper;
import cn.qbs.wa.train.logistics.pojo.clazzsign.*;
import cn.qbs.wa.train.logistics.service.ClazzStudentSignService;
import cn.qbs.wa.train.logistics.service.RemoteService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.Collator;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 班级学员签到表(ClazzStudentSign)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-26 10:38:28
 */
@Slf4j
@Service("clazzStudentSignService")
public class ClazzStudentSignServiceImpl extends ServiceImpl<ClazzStudentSignMapper, ClazzStudentSign> implements ClazzStudentSignService {

    @Resource
    private ClazzStudentMapper clazzStudentMapper;

    @Resource
    private RemoteService remoteService;

    @Override
    public IPage<SignInStudentResponse> pageSignIn(SignInPageRequest request) {
        // 1.分页查询班级下的学员信息
        Page<ClazzStudent> page = request.createMpPage();
        if (request.getStudentStatus() == 1) {
            // 班级在读学员
            clazzStudentMapper.selectPage(
                    page,
                    Wrappers.<ClazzStudent>lambdaQuery().select(ClazzStudent::getMemberId).eq(ClazzStudent::getClazzId, request.getClazzId())
            );
        } else {
            // 存在签到记录，但是已经被移出班级的班级的学员
            this.baseMapper.pageStudentOut(page, request.getClazzId());
        }
        if (page.getRecords().isEmpty()) {
            return new Page<>();
        }

        // 2.根据学员ID查询签到记录
        SignInSearchRequest search = BeanUtil.copyProperties(request, SignInSearchRequest.class);
        List<ClazzStudentSign> signInList = this.baseMapper.list(search);

        // 3.根据`开始节数 ~ 结束节数`时间跨度与签到记录构造签到情况
        List<Long> memberIds = page.getRecords().stream().map(ClazzStudent::getMemberId).collect(Collectors.toList());
        List<SignInStudentResponse> signInStudentResponses = genStuSignInList(memberIds, signInList, search.getLessonStart(), search.getLessonEnd(), false);

        IPage<SignInStudentResponse> resultPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        resultPage.setRecords(signInStudentResponses);
        return resultPage;
    }

    @Override
    public IPage<SignInRecordResponse> pageSignInRecord(SignInRecordPageRequest request) {
        IPage<SignInRecordResponse> page = this.baseMapper.pageSignInRecord(request.createMpPage(), request);
        List<SignInRecordResponse> records = page.getRecords();
        if (!records.isEmpty()) {
            // 查询学员姓名
            List<Long> memberIds = records.stream().map(SignInRecordResponse::getMemberId).collect(Collectors.toList());
            Map<Long, UniMemberDTO> memberMap = remoteService.remoteMemberMap(memberIds);
            for (SignInRecordResponse record : records) {
                Optional.ofNullable(memberMap.get(record.getMemberId())).ifPresent(m -> {
                    record.setStudentName(m.getRealName());
                    record.setPhone(m.getPhone());
                });
            }
        }
        return page;
    }

    @Override
    public List<SignInStudentResponse> classSignTable(SignInClazzRequest request) {
        // 1.查询班级学员
        List<ClazzStudent> clazzStudents = clazzStudentMapper.selectList(Wrappers.<ClazzStudent>lambdaQuery().select(ClazzStudent::getMemberId).eq(ClazzStudent::getClazzId, request.getClazzId()));
        // 存在签到记录但已经被移出班级的班级的学员
        List<ClazzStudent> outStudents = this.baseMapper.listStudentOut(request.getClazzId());
        // 汇总学员ID列表
        List<Long> memberIds = new ArrayList<>(clazzStudents.size() + outStudents.size());
        if (CollUtil.isNotEmpty(clazzStudents)) {
            memberIds.addAll(clazzStudents.stream().map(ClazzStudent::getMemberId).collect(Collectors.toList()));
        }
        if (CollUtil.isNotEmpty(outStudents)) {
            memberIds.addAll(outStudents.stream().map(ClazzStudent::getMemberId).collect(Collectors.toList()));
        }
        if (CollUtil.isEmpty(memberIds)) {
            return Collections.emptyList();
        }

        // 2.根据学员ID查询签到记录
        SignInSearchRequest search = BeanUtil.copyProperties(request, SignInSearchRequest.class);
        search.setSignInStatusList(Arrays.asList(SignInStatusEnum.NORMAL.getCode(), SignInStatusEnum.SICK.getCode(), SignInStatusEnum.ABSENT.getCode()));
        List<ClazzStudentSign> signInList = this.baseMapper.list(search);

        // 3.根据`开始节数 ~ 结束节数`时间跨度与签到记录构造签到情况
        return genStuSignInList(memberIds, signInList, search.getLessonStart(), search.getLessonEnd(), true);
    }

    /**
     * @param memberIds    班级学员ID列表
     * @param signInList   已签到的记录
     * @param lessonStart  签到开始节数
     * @param lessonEnd    签到结束节数
     * @param sortByCNName 根据中文名称排序
     * @return 学员签到数组
     */
    private List<SignInStudentResponse> genStuSignInList(List<Long> memberIds, List<ClazzStudentSign> signInList, Integer lessonStart, Integer lessonEnd, boolean sortByCNName) {
        // 查询学员姓名
        Map<Long, UniMemberDTO> memberMap = remoteService.remoteMemberMap(memberIds);

        // 构造学员列表
        List<SignInStudentResponse> signInStudentList = memberIds.stream().map(id -> {
            SignInStudentResponse sign = new SignInStudentResponse();
            sign.setMemberId(id);
            Optional.ofNullable(memberMap.get(id)).ifPresent(m -> {
                sign.setStudentName(m.getRealName());
                sign.setPhone(m.getPhone());
            });
            return sign;
        }).collect(Collectors.toList());

        // 根据签到记录构造 学员 -> 签到课次 -> 签到内容 映射关系
        Map<Long, Map<Integer, SignInItemDTO>> memberSignMap = CollUtil.isEmpty(signInList) ? Collections.emptyMap() :
                signInList.stream().collect(Collectors.groupingBy(ClazzStudentSign::getMemberId,
                        Collectors.toMap(ClazzStudentSign::getLessonNum, e -> {
                            SignInItemDTO sign = BeanUtil.copyProperties(e, SignInItemDTO.class);
                            sign.setEditable(Constants.DB_TRUE);
                            sign.setSignInId(e.getId());
                            return sign;
                        }, (a, b) -> a)
                ));

        // 分别注入学员签到记录
        for (SignInStudentResponse response : signInStudentList) {
            // 没有姓名的学员，用空字符串替代，避免中文排序空指针异常
            if (StrUtil.isBlank(response.getStudentName())) {
                response.setStudentName("");
            }
            Long memberId = response.getMemberId();
            // 签到数量
            response.setNormalCount(memberSignMap.containsKey(memberId) ? memberSignMap.get(memberId).size() : 0);
            // 设置签到记录
            response.setSignInList(genSignInItemList(memberSignMap.get(memberId), lessonStart, lessonEnd));
        }
        if (sortByCNName) {
            // 按照中文姓名排序 a~z
            signInStudentList.sort(Comparator.comparing(SignInStudentResponse::getStudentName, Collator.getInstance(java.util.Locale.CHINA)));
        }
        return signInStudentList;
    }

    /**
     * 根据ID数组，查询学员姓名
     *
     * @param memberIds 学员用户ID数组
     * @return 学员用户ID:学员用户名 映射Map
     */
    private Map<Long, String> getMemberNameMap(List<Long> memberIds) {
        List<UniMemberDTO> memberList = remoteService.remoteMemberList(memberIds);
        return CollUtil.isEmpty(memberList) ? Collections.emptyMap() : memberList.stream().collect(Collectors.toMap(UniMemberDTO::getId, UniMemberDTO::getRealName, (a, b) -> a));
    }

    /**
     * 根据 开始节数-结束节数 构造签到记录
     *
     * @param signMap     学员签到节数
     * @param lessonStart 开始节数
     * @param lessonEnd   结束节数
     */
    private List<SignInItemDTO> genSignInItemList(Map<Integer, SignInItemDTO> signMap, Integer lessonStart, Integer lessonEnd) {
        List<SignInItemDTO> signInList = new ArrayList<>(lessonEnd - lessonStart + 1);
        for (int i = lessonStart; i <= lessonEnd; i++) {
            SignInItemDTO signInItem;
            if (signMap != null && signMap.containsKey(i)) {
                // 存在签到记录
                signInItem = signMap.get(i);
                // 取消签到的不显示相应的数据
                if (SignInStatusEnum.NOT_SUBMIT.getCode().equals(signInItem.getSignInStatus())) {
                    signInItem.setRemark(null);
                    signInItem.setSignInDate(null);
                    signInItem.setLessonHour(null);
                }
            } else {
                // 不存在则构造空记录
                signInItem = new SignInItemDTO();
                signInItem.setLessonNum(i);
            }
            signInItem.setEditable(Constants.DB_TRUE);
            signInList.add(signInItem);
        }
        return signInList;
    }

    @Override
    public Boolean singleSignIn(SignInSingleRequest request) {
        if (request.getSignInId() != null) {
            // 取消签到 or 再次签到
            Long signInId = request.getSignInId();
            ClazzStudentSign sign = this.getById(signInId);
            if (sign == null) {
                log.error("签到ID不存在, ID：{}", signInId);
                throw new ServiceException("取消签到失败");
            }
            // 更新签到记录，若原记录签到状态与当前请求签到状态不一致情况下更新
            //if (!request.getSignInStatus().equals(sign.getSignInStatus())) {
                ClazzStudentSign signUpdater = new ClazzStudentSign();
                signUpdater.setId(signInId);
                signUpdater.setSignInStatus(request.getSignInStatus());
                signUpdater.setLessonHour(request.getLessonHour());
                signUpdater.setRemark(Optional.ofNullable(request.getRemark()).orElse(StrUtil.EMPTY));
                signUpdater.setSignInDate(request.getSignInDate());
                this.updateById(signUpdater);
            //}
        } else {
            // 新增签到
            // 判断该课次是否已签到
            ClazzStudentSign sign = this.lambdaQuery().select(ClazzStudentSign::getId, ClazzStudentSign::getSignInStatus)
                    .eq(ClazzStudentSign::getClazzId, request.getClazzId())
                    .eq(ClazzStudentSign::getMemberId, request.getMemberId())
                    .eq(ClazzStudentSign::getLessonNum, request.getLessonNum())
                    .one();
            // 已经签到则直接返回
            if (sign != null && SignInStatusEnum.NORMAL.getCode().equals(sign.getSignInStatus())) {
                return Boolean.FALSE;
            } else {
                ClazzStudentSign clazzStudentSign = BeanUtil.copyProperties(request, ClazzStudentSign.class);
                clazzStudentSign.setOrgId(SecurityContextHolder.getOrgId());
                if (sign != null) {
                    clazzStudentSign.setId(sign.getId());
                }
                // 保存签到记录
                this.saveOrUpdate(clazzStudentSign);
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean batchSignIn(SignInBatchRequest request) {
        Long clazzId = request.getClazzId();

        // 根据模式过滤获取最终签到学员
        List<Long> memberIds = filterModeMembers(request.getSelectMode(), clazzId, request.getMemberIds());

        // 构造学员批量签到记录数组
        List<ClazzStudentSign> batchSignList = genStudentSignInObjs(memberIds, request.getLessonNums(), request);

        // 匹配已经存在的记录
        matchExistRecord(clazzId, memberIds, request.getLessonNums(), batchSignList);

        // 批量新增或更新记录
        return this.saveOrUpdateBatch(batchSignList);
    }

    /**
     * 根据签到模式筛选最终学员用户
     *
     * @param mode      签到模式 1:选择部分学生 2:选择班级
     * @param clazzId   班级ID
     * @param memberIds 签到学员ID数组
     */
    private List<Long> filterModeMembers(Integer mode, Long clazzId, List<Long> memberIds) {
        // 签到模式 1:选择部分学生 2:选择班级
        if (1 == mode) {
            if (CollUtil.isEmpty(memberIds)) {
                throw new ServiceException("请选择签到学员");
            }
            List<ClazzStudent> studentList = clazzStudentMapper.selectList(Wrappers.<ClazzStudent>lambdaQuery().select(ClazzStudent::getMemberId).eq(ClazzStudent::getClazzId, clazzId).in(ClazzStudent::getMemberId, memberIds));
            if (CollUtil.isEmpty(studentList)) {
                throw new ServiceException("签到学员，非当前班级学员");
            }
            if (studentList.size() != memberIds.size()) {
                memberIds = studentList.stream().map(ClazzStudent::getMemberId).collect(Collectors.toList());
            }
        } else {
            List<ClazzStudent> studentList = clazzStudentMapper.selectList(Wrappers.<ClazzStudent>lambdaQuery().select(ClazzStudent::getMemberId).eq(ClazzStudent::getClazzId, clazzId));
            if (CollUtil.isEmpty(studentList)) {
                throw new ServiceException("班级暂无可签到学员");
            }
            memberIds = studentList.stream().map(ClazzStudent::getMemberId).collect(Collectors.toList());
        }
        return memberIds;
    }

    /**
     * 构造学员签到记录
     *
     * @param memberIds  学员用户ID数组
     * @param lessonNums 课时数
     * @param signData   签到数据
     * @return 签到数据记录
     */
    private List<ClazzStudentSign> genStudentSignInObjs(List<Long> memberIds, List<Integer> lessonNums, SignInAddRequest signData) {
        List<ClazzStudentSign> list = new ArrayList<>(memberIds.size() * lessonNums.size());
        Long orgId = SecurityContextHolder.getOrgId();
        for (Long memberId : memberIds) {
            for (Integer lessonNum : lessonNums) {
                ClazzStudentSign clazzStudentSign = BeanUtil.copyProperties(signData, ClazzStudentSign.class);
                clazzStudentSign.setMemberId(memberId);
                clazzStudentSign.setOrgId(orgId);
                clazzStudentSign.setLessonNum(lessonNum);
                clazzStudentSign.setLessonHour(clazzStudentSign.getLessonHour());
                list.add(clazzStudentSign);
            }
        }
        return list;
    }

    /**
     * 匹配已经签到历史记录
     *
     * @param clazzId       班级ID
     * @param memberIds     学员用户ID数组
     * @param lessonNums    签到课次数
     * @param batchSignList 当前签到记录
     */
    private void matchExistRecord(Long clazzId, List<Long> memberIds, List<Integer> lessonNums, List<ClazzStudentSign> batchSignList) {
        // 查询 学员 在 lessonNum 下的签到记录
        LambdaQueryChainWrapper<ClazzStudentSign> lambdaQueryWrapper = this.lambdaQuery()
                .select(ClazzStudentSign::getId, ClazzStudentSign::getMemberId, ClazzStudentSign::getLessonNum)
                .eq(ClazzStudentSign::getClazzId, clazzId)
                .in(ClazzStudentSign::getMemberId, memberIds);
        if (lessonNums.size() == 1) {
            lambdaQueryWrapper.eq(ClazzStudentSign::getLessonNum, lessonNums.get(0));
        } else {
            lambdaQueryWrapper.in(ClazzStudentSign::getLessonNum, lessonNums);
        }
        List<ClazzStudentSign> stuSignInRecordList = lambdaQueryWrapper.list();
        if (!stuSignInRecordList.isEmpty()) {
            // 根据签到记录构造 学员 -> 课次 -> 签到ID 映射关系
            Map<Long, Map<Integer, Long>> memberSignMap = stuSignInRecordList.stream().collect(
                    Collectors.groupingBy(ClazzStudentSign::getMemberId, Collectors.toMap(ClazzStudentSign::getLessonNum, ClazzStudentSign::getId, (a, b) -> a.compareTo(b) > 0 ? a : b))
            );
            // 将已存在的签到记录设置ID，进行更新操作
            for (ClazzStudentSign s : batchSignList) {
                if (memberSignMap.containsKey(s.getMemberId()) && memberSignMap.get(s.getMemberId()).containsKey(s.getLessonNum())) {
                    s.setId(memberSignMap.get(s.getMemberId()).get(s.getLessonNum()));
                    // 清空无关数据，减少 update 字段
                    s.setOrgId(null);
                    s.setClazzId(null);
                    s.setLessonNum(null);
                }
            }
        }
    }

    @Override
    public List<ClazzSignExcelDTO> generateExcelData(SignInRecordPageRequest request) {
        List<SignInRecordResponse> signInRecordResponses = this.baseMapper.signInRecordList(request);
        if (!signInRecordResponses.isEmpty()) {
            // 查询学员姓名
            List<Long> memberIds = signInRecordResponses.stream().map(SignInRecordResponse::getMemberId).collect(Collectors.toList());
            Map<Long, UniMemberDTO> memberMap = remoteService.remoteMemberMap(memberIds);
            for (SignInRecordResponse record : signInRecordResponses) {
                Optional.ofNullable(memberMap.get(record.getMemberId())).ifPresent(m -> {
                    record.setStudentName(m.getRealName());
                    record.setPhone(m.getPhone());
                });
            }
        }
        return ClazzStudentSignServiceImpl.convertSignInRecordResponseToClazzSignExcelDTO(signInRecordResponses);
    }

    private static List<ClazzSignExcelDTO> convertSignInRecordResponseToClazzSignExcelDTO(List<SignInRecordResponse> sourceData) {
        if (sourceData.isEmpty()) {
            return Collections.emptyList();
        }

        ArrayList<ClazzSignExcelDTO> targetData = new ArrayList<>(sourceData.size());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        for (SignInRecordResponse response : sourceData) {
            ClazzSignExcelDTO clazzSignExcelDTO = new ClazzSignExcelDTO();
            clazzSignExcelDTO.setClazzName(StringUtils.trimToEmpty(response.getClazzName()));
            clazzSignExcelDTO.setStudentName(StringUtils.trimToEmpty(response.getStudentName()));
            clazzSignExcelDTO.setLessonNum(response.getLessonNum());
            if (response.getSignInDate() != null) {
                clazzSignExcelDTO.setSignInDate(response.getSignInDate().format(formatter));
            }
            if (response.getSignInStatus() != null) {
                if(SignInStatusEnum.NORMAL.getCode().equals(response.getSignInStatus())){
                    clazzSignExcelDTO.setSignInStatus(SignInStatusEnum.NORMAL.getName());
                }
                if(SignInStatusEnum.SICK.getCode().equals(response.getSignInStatus())){
                    clazzSignExcelDTO.setSignInStatus(SignInStatusEnum.SICK.getName());
                }
                if(SignInStatusEnum.ABSENT.getCode().equals(response.getSignInStatus())){
                    clazzSignExcelDTO.setSignInStatus(SignInStatusEnum.ABSENT.getName());
                }

            }
            targetData.add(clazzSignExcelDTO);
        }

        return targetData;
    }
}