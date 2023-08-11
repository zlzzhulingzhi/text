package cn.qbs.wa.teach.question.service.impl;

import cn.qbs.wa.teach.question.entity.PaperQuestion;
import cn.qbs.wa.teach.question.mapper.PaperQuestionMapper;
import cn.qbs.wa.teach.question.service.PaperQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 试卷试题关联(PaperQuestion)表服务实现类
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-19 15:18:24
 */
@Slf4j
@Service("paperQuestionService")
public class PaperQuestionServiceImpl extends ServiceImpl<PaperQuestionMapper, PaperQuestion> implements PaperQuestionService {

}

