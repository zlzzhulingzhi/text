package cn.qbs.wa.train.basic.api;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.basic.api.factory.RemoteTrainDictFallbackFactory;
import cn.qbs.wa.train.basic.api.pojo.DTO.dict.DictPageRequestDTO;
import cn.qbs.wa.train.basic.api.pojo.DTO.dict.TreeDictDTO;
import cn.qbs.wa.train.basic.api.pojo.DTO.dict.TreeDictResultDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:02
 */
@FeignClient(contextId = "remoteTrainDictService", name="train-basic", path = "/train/basic/dict",fallbackFactory = RemoteTrainDictFallbackFactory.class)
public interface RemoteTrainDictService {



    @PostMapping("tree/list")
    @ApiOperation("查询字典表树列表")
    R<List<TreeDictResultDTO>> treeList(@RequestBody TreeDictDTO params) ;

    @PostMapping("getDictValue")
    @ApiOperation("根据字典码和字典值获取字典名称")
    R<String> getDictValue(@RequestBody DictPageRequestDTO params);

    @PostMapping("getDictKey")
    @ApiOperation("根据字典码和字典值获取字典名称")
    R<String> getDictKey(@RequestBody DictPageRequestDTO params);



}
