package cn.qbs.wa.train.logistics.service.manage;

import cn.qbs.wa.train.logistics.entity.Clazz;
import cn.qbs.wa.train.logistics.pojo.clazz.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.util.List;

/**
 * 班级表(Clazz)表服务接口
 *
 * @author makejava
 * @since 2022-10-08 16:41:48
 */
public interface ClazzService extends IService<Clazz> {

    /**
     * 新增班级表
     *
     * @param params
     * @return
     */
    Clazz add(ClazzAddRequest params);

    /**
     * 分页查询班级表
     *
     * @param params
     * @return
     */
    IPage<ClazzPageResponse> page(ClazzPageRequest params);

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    ClazzDetailResponse detail(ClazzDetailRequest request);

    /**
     * 更新班级表
     *
     * @param params
     * @return
     */
    boolean update(ClazzUpdateRequest params);

    /**
     * 删除班级表
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<ClazzListResponse> list(ClazzListRequest params);

    ClazzInfoResponse info(Long clazzId);
}

