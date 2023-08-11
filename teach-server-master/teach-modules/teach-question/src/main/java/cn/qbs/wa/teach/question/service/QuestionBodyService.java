package cn.qbs.wa.teach.question.service;

import cn.qbs.wa.teach.question.entity.QuestionBody;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 试题主体(QuestionBody)表服务接口
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-04 10:20:43
 */
public interface QuestionBodyService extends IService<QuestionBody> {

    /**
     * 删除试题主体
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);
    
}

