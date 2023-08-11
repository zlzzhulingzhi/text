package cn.qbs.wa.train.logistics.service.manage;

import cn.qbs.wa.train.logistics.entity.Dormitory;
import cn.qbs.wa.train.logistics.pojo.dormitory.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 宿舍表(Dormitory)表服务接口
 *
 * @author makejava
 * @since 2022-10-08 17:39:59
 */
public interface DormitoryManageService extends IService<Dormitory> {

    /**
     * 分页查询宿舍表
     *
     * @param params
     * @return
     */
    IPage<DormitoryPageResponse> page(DormitoryPageRequest params);

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    DormitoryDetailResponse detail(Long id);

}

