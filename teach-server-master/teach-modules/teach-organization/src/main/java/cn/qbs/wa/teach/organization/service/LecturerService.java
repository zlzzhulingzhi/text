package cn.qbs.wa.teach.organization.service;

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
public interface LecturerService extends IService<Lecturer> {

    /**
     * 新增讲师表
     *
     * @param params
     * @return
     */
    boolean add(LecturerAddRequest params);

    /**
     * 分页查询讲师表
     *
     * @param params
     * @return
     */
    IPage<LecturerPageResponse> page(LecturerPageRequest params);

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    LecturerDetailResponse detail(Serializable id);

    /**
     * 更新讲师表
     *
     * @param params
     * @return
     */
    boolean update(LecturerUpdateRequest params);

    /**
     * 删除讲师表
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<LecturerListResponse> listLecture(LecturerListRequest request);

    IPage<LecturerPageResponse> pageAdmin(LecturerPageRequest params);

    IPage<LecturerResponse> pageLecturerByPlat(LecturerPageQuery params);

    List<LecturerClazzResponse> pageLecturerClazz(Long lecturerId);
}

