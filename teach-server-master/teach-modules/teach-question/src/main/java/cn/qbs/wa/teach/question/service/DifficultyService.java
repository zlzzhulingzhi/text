package cn.qbs.wa.teach.question.service;

import cn.qbs.wa.teach.question.entity.Difficulty;
import cn.qbs.wa.teach.common.core.domain.EnableRequest;
import cn.qbs.wa.teach.question.pojo.SelectOption;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 难度(Difficulty)表服务接口
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-09 10:42:49
 */
public interface DifficultyService extends IService<Difficulty> {

    boolean enable(EnableRequest request);

    List<SelectOption> getSelectOptionList();

    Difficulty getByCache(Long difficultyId);

}

