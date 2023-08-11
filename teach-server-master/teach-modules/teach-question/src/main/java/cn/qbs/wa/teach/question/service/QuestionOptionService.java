package cn.qbs.wa.teach.question.service;

import cn.qbs.wa.teach.question.entity.QuestionOption;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 试题选项(QuestionOption)表服务接口
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-08 18:48:25
 */
public interface QuestionOptionService extends IService<QuestionOption> {

    /**
     * 删除试题选项
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);
    
}

