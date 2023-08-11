package cn.qbs.wa.train.basic.api.factory;


import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.basic.api.RemoteTrainDictService;
import cn.qbs.wa.train.basic.api.pojo.DTO.dict.DictPageRequestDTO;
import cn.qbs.wa.train.basic.api.pojo.DTO.dict.TreeDictDTO;
import cn.qbs.wa.train.basic.api.pojo.DTO.dict.TreeDictResultDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:03
 */
public class RemoteTrainDictFallbackFactory implements FallbackFactory<RemoteTrainDictService> {

    @Override
    public RemoteTrainDictService create(Throwable cause) {
        return new RemoteTrainDictService(){

            @Override
            public R<List<TreeDictResultDTO>> treeList(TreeDictDTO params) {
                return null;
            }

            @Override
            public R<String> getDictValue(DictPageRequestDTO params) {
                return null;
            }

            @Override
            public R<String> getDictKey(DictPageRequestDTO params) {
                return null;
            }
        };

    }
}
