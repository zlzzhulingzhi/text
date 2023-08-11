package cn.qbs.wa.train.logistics.service;

import cn.qbs.wa.teach.common.core.domain.IdStudentRequest;
import cn.qbs.wa.train.logistics.entity.StudentGroup;
import cn.qbs.wa.train.logistics.pojo.studentgroup.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 学员-分组(StudentGroup)表服务接口
 *
 * @author makejava
 * @since 2022-03-28 16:07:14
 */
public interface StudentGroupService extends IService<StudentGroup> {

    /**
     * 学员贴标签
     * @param params
     * @return
     */
    boolean add(StudentGroupAddRequest params);

    /**
     * 分页查询学员-分组
     * @param params
     * @return
     */
    IPage<StudentGroupPageResponse> page(StudentGroupPageRequest params);

    /**
     * 获取详细信息
     * @param
     * @return
     */
    List<StudentGroupDetailResponse> detail(IdStudentRequest request);

    /**
     * 更新学员-分组
     * @param params
     * @return
     */
    boolean update(StudentGroupUpdateRequest params);

    /**
     * 删除学员-分组
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    boolean addList(StudentGroupAddRequest params);
}

