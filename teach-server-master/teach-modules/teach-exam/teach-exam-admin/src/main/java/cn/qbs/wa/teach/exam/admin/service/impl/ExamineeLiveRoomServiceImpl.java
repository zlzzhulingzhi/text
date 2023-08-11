package cn.qbs.wa.teach.exam.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.qbs.wa.teach.basic.api.RemoteUserService;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserListDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserListResultDTO;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.teach.exam.admin.mapper.ExamineeLiveRoomMapper;
import cn.qbs.wa.teach.exam.admin.mapper.ExamineeRecordMapper;
import cn.qbs.wa.teach.exam.admin.mapper.RuleMapper;
import cn.qbs.wa.teach.exam.admin.mapper.ViolationRecordMapper;
import cn.qbs.wa.teach.exam.admin.pojo.examineeliveroom.*;
import cn.qbs.wa.teach.exam.admin.service.ExamineeLiveRoomService;
import cn.qbs.wa.teach.exam.common.entity.ExamineeLiveRoom;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecord;
import cn.qbs.wa.teach.exam.common.entity.Rule;
import cn.qbs.wa.teach.exam.common.entity.ViolationRecord;
import cn.qbs.wa.teach.exam.common.enumerate.ExamineeRecordStatusEnum;
import cn.qbs.wa.teach.exam.common.enumerate.RuleEnum;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentSearchDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 考生直播房间(ExamineeLiveRoom)表服务实现类
 *
 * @author makejava
 * @since 2022-01-04 11:51:44
 */
@Slf4j
@Service("examineeLiveRoomService")
public class ExamineeLiveRoomServiceImpl extends ServiceImpl<ExamineeLiveRoomMapper, ExamineeLiveRoom> implements ExamineeLiveRoomService {

    /**
     * 调用直播接口
     */
    //@Resource
    //private RemoteLiveShowService remoteLiveShowService;

    @Resource
    private RemoteUserService remoteUserService;

    //@Resource
    //private RemotePlaybackRecordService remotePlaybackRecordService;

    @Resource
    private ExamineeRecordMapper examineeRecordMapper;

    @Resource
    private RuleMapper ruleMapper;

    @Resource
    private ViolationRecordMapper violationRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PushOrPullShowResult live(ExamineeLiveRoomAddRequest params) {
        PushOrPullShowResult pushOrPullShowResult = new PushOrPullShowResult();
        ExamineeLiveRoom examineeLiveRoom = new ExamineeLiveRoom();
        Long userId = SecurityUtils.getLoginUser().getUserid();
        ArrayList<Long> arrayList = new ArrayList<>();
        arrayList.add(userId);
        List<UserListResultDTO> userListResultDTOS = remoteUserList(arrayList);
        return pushOrPullShowResult;
        //判断是否有存在监控房间
        //ExamineeLiveRoom examineeLiveRoom1 = baseMapper.selectOne(new LambdaQueryWrapper<ExamineeLiveRoom>().eq(ExamineeLiveRoom::getExamId, params.getExamId()).eq(ExamineeLiveRoom::getExamineeId, params.getExamineeId()).eq(ExamineeLiveRoom::getExamineeRecordId, params.getExamineeRecordId()));
        //if (StringUtils.isNotNull(examineeLiveRoom1)) {
        //    BasicLiveShowInfoDTO basicLiveShowInfoDTO = new BasicLiveShowInfoDTO();
        //    basicLiveShowInfoDTO.setStreamName(examineeLiveRoom1.getStreamName());
        //    BasicLiveShowInfoResultDTO data = remoteLiveShowService.getInfo(basicLiveShowInfoDTO).getData();
        //    BeanUtils.copyProperties(data, pushOrPullShowResult);
        //    pushOrPullShowResult.setUserName(userListResultDTOS.get(0).getRealName());
        //    pushOrPullShowResult.setUserId(userId);
        //    return pushOrPullShowResult;
        //} else {
        //    //不存在房间,新建房间
        //    LiveShowMonitorAddDTO liveShowMonitorAddDTO = new LiveShowMonitorAddDTO();
        //    liveShowMonitorAddDTO.setActualStartTime(params.getStartTime());
        //    liveShowMonitorAddDTO.setEndTime(params.getEndTime());
        //    liveShowMonitorAddDTO.setLiveRoomType(params.getLiveRoomType());
        //    LiveShowAddResultDTO data = remoteLiveShowService.addMonitor(liveShowMonitorAddDTO).getData();
        //    if (StringUtils.isNotNull(data)) {
        //        BeanUtils.copyProperties(data, examineeLiveRoom);
        //        BeanUtils.copyProperties(data, pushOrPullShowResult);
        //    }
        //    examineeLiveRoom.setExamId(params.getExamId());
        //    examineeLiveRoom.setExamineeId(params.getExamineeId());
        //    examineeLiveRoom.setExamineeRecordId(params.getExamineeRecordId());
        //    pushOrPullShowResult.setUserName(userListResultDTOS.get(0).getRealName());
        //    pushOrPullShowResult.setUserId(userId);
        //    this.save(examineeLiveRoom);
        //    return pushOrPullShowResult;
        //}
    }

    @Override
    public List<ExamineeLiveRoomPageResponse> page(ExamineeLiveRoomPageRequest params) {
        List<ExamineeLiveRoomPageResponse> page = baseMapper.page(params);
        return getFilePageResponseIPage(page, false);
    }

    @Resource
    private RemoteStudentService remoteStudentService;

    private List<ExamineeLiveRoomPageResponse> getFilePageResponseIPage(List<ExamineeLiveRoomPageResponse> page, Boolean flag) {
        List<Long> ids = page.stream().map(ExamineeLiveRoomPageResponse::getUserId).distinct().collect(Collectors.toList());
        StudentSearchDTO searchDTO = new StudentSearchDTO();
        searchDTO.setUserIds(ids);
        R<List<StudentDTO>> userList = remoteStudentService.list(searchDTO);
        List<StudentDTO> studentDTOList = userList.getData();
        if (StringUtils.isNotEmpty(studentDTOList)) {
            studentDTOList.forEach(u -> {
                List<ExamineeLiveRoomPageResponse> collect = page.stream().filter(r -> r.getUserId().equals(u.getUserId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect)) {
                    collect.forEach(i -> i.setRealName(u.getRealName()));
                }
            });

            // 远程调用获取用户信息
           /* List<UserListResultDTO> userList = remoteUserList(ids);
            if (CollUtil.isNotEmpty(userList)) {
                Map<Long, String> userMap = userList.stream().collect(Collectors.toMap(UserListResultDTO::getId, UserListResultDTO::getRealName));
                Map<Long, String> phoneMap = userList.stream().collect(Collectors.toMap(UserListResultDTO::getId, UserListResultDTO::getPhone));*/

                    /*info.setRealName(userMap.get(info.getUserId()));
                    info.setPhone(phoneMap.get(info.getUserId()));*/
            //设置回放地址
            //for (ExamineeLiveRoomPageResponse info : page) {
            //    if (flag) {
            //        BasicPlaybackRecordDetailDTO basicPlaybackRecordDetailDTO = new BasicPlaybackRecordDetailDTO();
            //        basicPlaybackRecordDetailDTO.setStreamName(info.getStreamName());
            //        BasicPlaybackRecordDetailResultDTO data = remotePlaybackRecordService.detail(basicPlaybackRecordDetailDTO).getData();
            //        if (StringUtils.isNotNull(data)) {
            //            List<String> playbackUrls = data.getPlaybackUrls();
            //            info.setPlaybackUrl(playbackUrls.get(0));
            //            info.setFlag(OthersEnum.CALCULATED.getIndex());
            //        } else {
            //            info.setFlag(OthersEnum.NOT_CALCULATED.getIndex());
            //        }
            //    }
            //}

        }
        return page;
    }

    /**
     * 远程调用获取用户信息
     */
    private List<UserListResultDTO> remoteUserList(List<Long> ids) {
        UserListDTO userListDTO = new UserListDTO();
        userListDTO.setIdList(ids);
        return unboxingRemoteCallData(remoteUserService.listUser(userListDTO));
    }

    private <T> T unboxingRemoteCallData(R<T> data) {
        T obj = data.getData();
        if (R.SUCCESS == data.getCode() && ObjectUtil.isNotEmpty(obj)) {
            return obj;
        }
        return null;
    }

    @Override
    public List<ExamineeLiveRoomPageResponse> detail(ExamineeLiveRoomPageRequest params) {
        List<ExamineeLiveRoomPageResponse> examineeLiveRoomPageResponseIPage = baseMapper.selectDetailList(params);
        return getFilePageResponseIPage(examineeLiveRoomPageResponseIPage, true);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(ExamineeLiveRoomUpdateRequest params) {
        ExamineeRecord examineeRecordByCheat = getExamineeRecordByCheat(params);
        examineeRecordMapper.updateById(examineeRecordByCheat);
        //插入信息到违规考试表,监考的违规信息写死为摄像头录像
        ViolationRecord violationRecord = new ViolationRecord();
        violationRecord.setRuleId(ruleMapper.selectOne(new LambdaQueryWrapper<Rule>().eq(Rule::getCode, RuleEnum.CAMERA_RECORDING.getCode())).getId());
        violationRecord.setExamineeId(params.getExamineeId());
        violationRecord.setExamineeRecordId(params.getExamineeRecordId());
        violationRecord.setText(params.getRemark());
        violationRecordMapper.insert(violationRecord);
        return true;
    }

    private ExamineeRecord getExamineeRecordByCheat(ExamineeLiveRoomUpdateRequest params) {
        List<ExamineeRecord> examineeRecords = examineeRecordMapper.selectList(new LambdaQueryWrapper<ExamineeRecord>().eq(ExamineeRecord::getExamineeId, params.getExamineeId()).eq(ExamineeRecord::getCalculated, Constants.DB_TRUE));
        ExamineeRecord examineeRecord = new ExamineeRecord();
        examineeRecord.setId(params.getId());
        examineeRecord.setRemark(params.getRemark());
        examineeRecord.setStatus(ExamineeRecordStatusEnum.INTERRUPT.getStatus());
        examineeRecord.setEnabled(Constants.DB_FAIL);
        examineeRecord.setScore(BigDecimal.ZERO);
        //有最高分的就统计无效,没有统计过最高分的就马上计算为有效统计
        if (CollUtil.isNotEmpty(examineeRecords)) {
            examineeRecord.setCalculated(false);
        } else {
            examineeRecord.setCalculated(true);
        }
        return examineeRecord;
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

