package cn.qbs.wa.train.logistics.service;

import cn.qbs.wa.train.logistics.entity.StudentDept;
import cn.qbs.wa.train.logistics.pojo.studentdept.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 学员部门表(StudentDept)表服务接口
 *
 * @author makejava
 * @since 2022-05-09 15:15:30
 */
public interface StudentDeptService extends IService<StudentDept> {

    /**
     * 新增学员部门表
     * @param params
     * @return
     */
    boolean batchAdd(StudentDeptBatchAddRequest params);

    boolean examAddStudent(Long deptId);

    boolean add(StudentDeptSingleAddRequest params);

    /**
     * 分页查询学员部门表
     * @param params
     * @return
     */
    IPage<StudentDeptPageResponse> page(StudentDeptPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    StudentDeptDetailResponse detail(Serializable id);

    /**
     * 更新学员部门表
     * @param params
     * @return
     */
    boolean update(StudentDeptUpdateRequest params);

    /**
     * 删除学员部门表
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    boolean courseAddStudent(Long deptId);

}

