package cn.qbs.wa.teach.question.service;


import cn.qbs.wa.teach.question.entity.QuestionType;
import cn.qbs.wa.teach.common.core.domain.EnableRequest;
import cn.qbs.wa.teach.question.pojo.SelectOption;
import cn.qbs.wa.teach.question.pojo.question.type.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 题型(QuestionType)表服务接口
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 13:35:24
 */
public interface QuestionTypeService extends IService<QuestionType> {

    /**
     * 新增试题
     * @param params
     * @return
     */
    Long add(QuestionTypeAddRequest params);

    /**
     * 分页查询
     * @param params
     * @return
     */
    IPage<QuestionTypePageResponse> page(QuestionTypePageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    QuestionTypeDetailResponse detail(Serializable id);

    /**
     * 更新试题
     * @param params
     * @return
     */
    boolean update(QuestionTypeUpdateRequest params);

    /**
     * 删除
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    boolean enable(EnableRequest request);

    List<SelectOption> getSelectOptionList();

    QuestionType getByCache(Long questionTypeId);

}

