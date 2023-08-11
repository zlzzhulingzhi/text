package cn.qbs.wa.train.logistics.service.platform;

import cn.qbs.wa.train.logistics.entity.Clazz;
import cn.qbs.wa.train.logistics.pojo.clazz.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 班级表(Clazz)表服务接口
 *
 * @author makejava
 * @since 2022-10-08 16:41:48
 */
public interface ClazzPlatService extends IService<Clazz> {


    /**
     * 分页查询班级表
     *
     * @param params
     * @return
     */
    IPage<ClazzPageResponse> page(ClazzPageRequest params);

    IPage<ClazzPageResponse> pageV2(ClazzPageRequest params);

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    ClazzDetailResponse detail(ClazzDetailRequest request);

    IPage<ClazzSummaryResponse> getClazzSummary(ClazzSummaryRequest params);

    ClazzSummaryResponse getClazzTotalSummary(ClazzSummaryRequest params);

    List<IntegrateClazzResponse> listClazzByMemberId(Long memberId);
}

