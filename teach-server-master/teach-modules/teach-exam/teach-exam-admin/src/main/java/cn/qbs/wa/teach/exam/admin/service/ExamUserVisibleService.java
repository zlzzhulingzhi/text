package cn.qbs.wa.teach.exam.admin.service;

import cn.qbs.wa.teach.exam.common.entity.ExamUserVisible;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 考试可见用户(ExamUserVisible)表服务接口
 *
 * @author zcm
 * @since 2021-12-14 11:43:32
 */
public interface ExamUserVisibleService extends IService<ExamUserVisible> {


    /**
     * 删除【课程可见用户】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    boolean addUser(List<Long> examIdList, List<StudentDTO> studentDTOList);
}

