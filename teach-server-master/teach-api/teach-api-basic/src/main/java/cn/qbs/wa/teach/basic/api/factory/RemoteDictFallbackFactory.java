package cn.qbs.wa.teach.basic.api.factory;

import cn.qbs.wa.teach.basic.api.RemoteDictService;
import cn.qbs.wa.teach.basic.api.pojo.DTO.dict.DictPageRequestDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.dict.DictDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.dict.DictResultDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:03
 */
public class RemoteDictFallbackFactory implements FallbackFactory<RemoteDictService> {

    @Override
    public RemoteDictService create(Throwable cause) {
        return new RemoteDictService(){

            @Override
            public R<List<DictResultDTO>> dictList(DictDTO params) {
                return null;
            }

            @Override
            public R<String> getDictKey(DictPageRequestDTO params) {
                return null;
            }

            @Override
            public R<String> getDictValue(DictPageRequestDTO params) {
                return null;
            }
        };

    }
}
