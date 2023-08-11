package cn.qbs.wa.teach.question.service.impl;

import cn.qbs.wa.teach.question.entity.QuestionBody;
import cn.qbs.wa.teach.question.mapper.QuestionBodyMapper;
import cn.qbs.wa.teach.question.service.QuestionBodyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 试题主体(QuestionBody)表服务实现类
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-04 10:20:43
 */
@Slf4j
@Service("questionBodyService")
public class QuestionBodyServiceImpl extends ServiceImpl<QuestionBodyMapper, QuestionBody> implements QuestionBodyService {

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }
    
}

