package cn.qbs.wa.teach.exam.consumer.service.impl;

import cn.qbs.wa.teach.exam.consumer.mapper.ViolationRecordMapper;
import cn.qbs.wa.teach.exam.consumer.service.ViolationRecordService;
import cn.qbs.wa.teach.exam.common.entity.ViolationRecord;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 考生违规记录表(ViolationRecord)表服务实现类
 *
 * @author zcm
 * @since 2021-12-14 11:43:33
 */
@Slf4j
@Service("violationRecordService")
public class ViolationRecordServiceImpl extends ServiceImpl<ViolationRecordMapper, ViolationRecord> implements ViolationRecordService {

}

