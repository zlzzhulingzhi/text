package cn.qbs.wa.teach.question.service.impl;

import cn.qbs.wa.teach.question.entity.PaperQuestionType;
import cn.qbs.wa.teach.question.mapper.PaperQuestionTypeMapper;
import cn.qbs.wa.teach.question.service.PaperQuestionTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 试卷题型(PaperQuestionType)表服务实现类
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-19 15:18:23
 */
@Slf4j
@Service("paperQuestionTypeService")
public class PaperQuestionTypeServiceImpl extends ServiceImpl<PaperQuestionTypeMapper, PaperQuestionType> implements PaperQuestionTypeService {

}

