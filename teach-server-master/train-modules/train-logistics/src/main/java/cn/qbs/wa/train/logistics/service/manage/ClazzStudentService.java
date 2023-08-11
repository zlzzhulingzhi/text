package cn.qbs.wa.train.logistics.service.manage;

import cn.qbs.wa.train.logistics.entity.ClazzStudent;
import cn.qbs.wa.train.logistics.pojo.clazzstudent.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 班级学员表(ClazzStudent)表服务接口
 *
 * @author makejava
 * @since 2022-10-08 17:28:14
 */
public interface ClazzStudentService extends IService<ClazzStudent> {

    /**
     * 新增班级学员表
     *
     * @param params
     * @return
     */
    boolean add(List<ClazzStudentAddRequest> params);

    /**
     * 分页查询班级学员表
     *
     * @param params
     * @return
     */
    IPage<ClazzStudentPageResponse> page(ClazzStudentPageRequest params);

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    ClazzStudentDetailResponse detail(Serializable id);

    /**
     * 更新班级学员表
     *
     * @param params
     * @return
     */
    boolean update(ClazzStudentUpdateRequest params);

    /**
     * 删除班级学员表
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<ClazzStudentMap> queryClazzLast(List<Long> memberIds);
}

