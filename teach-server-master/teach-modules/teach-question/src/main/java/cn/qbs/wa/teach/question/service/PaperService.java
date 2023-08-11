package cn.qbs.wa.teach.question.service;

import cn.qbs.wa.teach.common.core.domain.EnableRequest;
import cn.qbs.wa.teach.question.elasticsearch.SearchPageResult;
import cn.qbs.wa.teach.question.entity.Paper;
import cn.qbs.wa.teach.question.pojo.paper.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 试卷(Paper)表服务接口
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-18 20:48:48
 */
public interface PaperService extends IService<Paper> {

    /**
     * 新增试卷
     * @param params
     * @return
     */
    Long add(PaperAddRequest params);

    /**
     * 分页查询试卷
     * @param params
     * @return
     */
    IPage<PaperPageResponse> page(PaperPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    PaperDetailResponse detail(Long id);

    /**
     * 更新试卷
     * @param params
     * @return
     */
    boolean update(PaperUpdateRequest params);

    boolean enable(EnableRequest request);

    void saveEsIndex(Long paperId, Long orgId) throws Exception;

    boolean upddateEditable(UpdatePaperEditableRequest request);

    SearchPageResult<PaperSearchResult> search(PaperSearchRequest param);

}

