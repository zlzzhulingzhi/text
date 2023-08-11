package cn.qbs.wa.teach.exam.consumer.service.impl;

import cn.qbs.wa.teach.exam.consumer.mapper.ExamUserVisibleMapper;
import cn.qbs.wa.teach.exam.consumer.service.ExamUserVisibleService;
import cn.qbs.wa.teach.exam.common.entity.ExamUserVisible;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 考试可见用户(ExamUserVisible)表服务实现类
 *
 * @author zcm
 * @since 2021-12-14 11:43:32
 */
@Slf4j
@Service("examUserVisibleService")
public class ExamUserVisibleServiceImpl extends ServiceImpl<ExamUserVisibleMapper, ExamUserVisible> implements ExamUserVisibleService {

}

