package cn.qbs.wa.teach.question.service.impl;

import cn.qbs.wa.teach.question.mapper.QuestionImportRecordDetailMapper;
import cn.qbs.wa.teach.question.entity.QuestionImportRecordDetail;
import cn.qbs.wa.teach.question.service.QuestionImportRecordDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 试题导入记录详情(QuestionImportRecordDetail)表服务实现类
 *
 * @author zcm
 * @version 1.0
 * @date 2022-06-20 10:55:16
 */
@Slf4j
@Service("questionImportRecordDetailService")
public class QuestionImportRecordDetailServiceImpl extends ServiceImpl<QuestionImportRecordDetailMapper, QuestionImportRecordDetail> implements QuestionImportRecordDetailService {
    
}

