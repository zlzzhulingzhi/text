package cn.qbs.wa.teach.question.service;

import cn.qbs.wa.teach.domain.BasePageRequest;
import cn.qbs.wa.teach.question.entity.QuestionImportRecord;
import cn.qbs.wa.teach.question.pojo.question.QuestionImportRecordPageResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 试题导入记录表(QuestionImportRecord)表服务接口
 *
 * @author zcm
 * @version 1.0
 * @date 2022-06-17 17:41:44
 */
public interface QuestionImportRecordService extends IService<QuestionImportRecord> {

    /**
     * 分页查询
     * @param params
     * @return
     */
    IPage<QuestionImportRecordPageResponse> page(BasePageRequest params);

}

