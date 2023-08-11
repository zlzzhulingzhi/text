package cn.qbs.wa.teach.organization.service.inner;

import cn.qbs.wa.teach.organization.entity.Lecturer;
import cn.qbs.wa.teach.organization.pojo.lecturer.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 讲师表(Lecturer)表服务接口
 *
 * @author makejava
 * @since 2021-11-17 11:25:32
 */
public interface LecturerInnerService extends IService<Lecturer> {

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    LecturerDetailResponse detail(Serializable id);
}

