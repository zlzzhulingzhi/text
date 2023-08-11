package cn.qbs.wa.teach.basic.api;

import cn.qbs.wa.teach.basic.api.factory.RemoteDictFallbackFactory;
import cn.qbs.wa.teach.basic.api.pojo.DTO.dict.DictPageRequestDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.dict.DictDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.dict.DictResultDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:02
 */
@FeignClient(contextId = "remoteDictService", name="teach-basic", path = "basic/dict", fallbackFactory = RemoteDictFallbackFactory.class)
public interface RemoteDictService {

    @PostMapping("/list")
    @ApiOperation("查询字典列表")
    R<List<DictResultDTO>> dictList(@RequestBody DictDTO params) ;

    @PostMapping("getDictKey")
    @ApiOperation("根据字典码和字典名称获取字典值")
    R<String> getDictKey(@RequestBody DictPageRequestDTO params);

    @PostMapping("getDictValue")
    @ApiOperation("根据字典码和字典值获取字典名称")
    R<String> getDictValue(@RequestBody DictPageRequestDTO params);
}
