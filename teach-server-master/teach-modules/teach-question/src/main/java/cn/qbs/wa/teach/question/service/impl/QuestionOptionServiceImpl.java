package cn.qbs.wa.teach.question.service.impl;

import cn.qbs.wa.teach.question.entity.QuestionOption;
import cn.qbs.wa.teach.question.mapper.QuestionOptionMapper;
import cn.qbs.wa.teach.question.service.QuestionOptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 试题选项(QuestionOption)表服务实现类
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-08 18:48:25
 */
@Slf4j
@Service("questionOptionService")
public class QuestionOptionServiceImpl extends ServiceImpl<QuestionOptionMapper, QuestionOption> implements QuestionOptionService {

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }
    
}

