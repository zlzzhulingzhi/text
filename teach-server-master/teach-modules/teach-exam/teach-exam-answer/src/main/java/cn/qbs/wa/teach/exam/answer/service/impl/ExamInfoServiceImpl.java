package cn.qbs.wa.teach.exam.answer.service.impl;

import cn.qbs.wa.teach.exam.answer.mapper.ExamInfoMapper;
import cn.qbs.wa.teach.exam.answer.service.ExamInfoService;
import cn.qbs.wa.teach.exam.common.entity.ExamInfo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 考试信息表(ExamInfo)表服务实现类
 *
 * @author zcm
 * @since 2021-12-14 11:43:32
 */
@Slf4j
@Service("examInfoService")
public class ExamInfoServiceImpl extends ServiceImpl<ExamInfoMapper, ExamInfo> implements ExamInfoService {

}

