package cn.qbs.wa.teach.question.api;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.question.api.factory.RemotePaperFallbackFactory;
import cn.qbs.wa.teach.question.api.pojo.DTO.paper.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author zcm
 * @Date 2021/11/26 16:21
 * @Version 1.0
 */
@FeignClient(contextId = "remotePaperService", name = "teach-question", path = "question/paper", fallbackFactory = RemotePaperFallbackFactory.class)
public interface RemotePaperService {

    /**
     * 获取试卷分类
     *
     * @return
     */
    @PostMapping("/category")
    R<List<CategoryTreeNodeDTO>> category();

    /**
     * 搜索试卷
     * @param request
     * @return 试卷分页列表
     */
    @PostMapping("/search")
    R<SearchPageDTO<PaperDTO>> search(@RequestBody PaperSearchDTO request);

    @PostMapping("/detail")
    R<PaperDetailDTO> detail(@RequestBody IdOrgRequest request);

    /**
     * 修改试卷可编辑状态
     * @param request
     * @return
     */
    @PostMapping("/editable")
    R<Boolean> editable(@RequestBody UpdatePaperEditableRequest request);

    /**
     * 查询基本试卷列表
     */
    @ApiOperation("查询基本试卷列表")
    @PostMapping("basicList")
    R<List<BasicPaper>> basicList(@RequestBody IdListRequest param);

}
