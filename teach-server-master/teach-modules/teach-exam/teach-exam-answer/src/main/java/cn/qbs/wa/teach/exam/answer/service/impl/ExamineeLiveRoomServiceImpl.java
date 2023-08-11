package cn.qbs.wa.teach.exam.answer.service.impl;

import cn.qbs.wa.teach.exam.answer.mapper.ExamineeLiveRoomMapper;
import cn.qbs.wa.teach.exam.answer.service.ExamineeLiveRoomService;
import cn.qbs.wa.teach.exam.common.entity.ExamineeLiveRoom;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 考生直播房间(ExamineeLiveRoom)表服务实现类
 *
 * @author zcm
 * @since 2021-12-14 11:43:32
 */
@Slf4j
@Service("examineeLiveRoomService")
public class ExamineeLiveRoomServiceImpl extends ServiceImpl<ExamineeLiveRoomMapper, ExamineeLiveRoom> implements ExamineeLiveRoomService {

}

