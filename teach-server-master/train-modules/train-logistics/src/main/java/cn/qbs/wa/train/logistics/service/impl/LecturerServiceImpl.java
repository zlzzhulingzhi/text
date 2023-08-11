package cn.qbs.wa.train.logistics.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.train.logistics.entity.Employee;
import cn.qbs.wa.train.logistics.entity.Lecturer;
import cn.qbs.wa.train.logistics.mapper.LecturerMapper;
import cn.qbs.wa.train.logistics.pojo.lecturer.*;
import cn.qbs.wa.train.logistics.service.EmployeeService;
import cn.qbs.wa.train.logistics.service.LecturerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 讲师表(Lecturer)表服务实现类
 *
 * @author makejava
 * @since 2021-11-17 11:25:32
 */
@Slf4j
@Service("lecturerService")
public class LecturerServiceImpl extends ServiceImpl<LecturerMapper, Lecturer> implements LecturerService {

    @Autowired
    EmployeeService employeeService;

    @Override
    public boolean add(LecturerAddRequest params) {
        List<Lecturer> list = list(new LambdaQueryWrapper<Lecturer>().eq(Lecturer::getEmployeeId, params.getEmployeeId()));
        if (CollUtil.isNotEmpty(list)) {
            throw new ServiceException("已有相同的用户");
        }
        Lecturer lecturer = new Lecturer();
        BeanUtils.copyProperties(params, lecturer);
        return this.save(lecturer);
    }

    @Override
    public IPage<LecturerPageResponse> page(LecturerPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public LecturerDetailResponse detail(Serializable id) {
        LecturerDetailResponse lecturerDetailResponse = baseMapper.selectDetailById(id);
        if (lecturerDetailResponse != null) {
            Employee employee = employeeService.getById(lecturerDetailResponse.getEmployeeId());
            if (employee != null) {
                lecturerDetailResponse.setRealName(employee.getRealName());
                lecturerDetailResponse.setPhone(employee.getPhone());
            }
        }
        return lecturerDetailResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(LecturerUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        Lecturer lecturer;
        if (params.getUserId() == null || params.getEmployeeId() == null) {
            this.update(Wrappers.<Lecturer>lambdaUpdate().set(Lecturer::getUserId, null).set(Lecturer::getEmployeeId, null)
                    .eq(Lecturer::getId, params.getId()));
        }else {
            //查询讲师详情
            lecturer = this.lambdaQuery().eq(Lecturer::getEmployeeId, params.getEmployeeId()).one();
            if (lecturer != null){
                if (!lecturer.getEmployeeId().equals(params.getEmployeeId()) && !lecturer.getUserId().equals(params.getUserId())){
                    throw new ServiceException("该账号已关联其他讲师！");
                }
            }
        }
        lecturer = new Lecturer();
        BeanUtils.copyProperties(params, lecturer);
        //远程修改课程讲师信息
      /*  CourseLecturerDTO courseLecturerDTO = new CourseLecturerDTO();
        courseLecturerDTO.setLecturerId(lecturer.getId());
        courseLecturerDTO.setLecturerName(lecturer.getLecturerName());
        remoteCourseService.updateCourseLecturer(courseLecturerDTO);*/
        return this.updateById(lecturer);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        /*LecturerCourseRequestDTO lecturerCourseRequestDTO = new LecturerCourseRequestDTO();
        for (Long lecturerId : idList) {
            lecturerCourseRequestDTO.setLecturerId(lecturerId);
            PageResultComDTO<CoursePageByLecturerResultDTO> pageResultComDTO = remotePlatformCourseService.lectureCoursePage(lecturerCourseRequestDTO).getRemoteData();
            if (CollUtil.isNotEmpty(pageResultComDTO.getRecords())) {
                throw new ServiceException("该讲师已关联课程，无法删除！");
            }
        }*/
        return this.removeByIds(idList);
    }

    @Override
    public List<LecturerListResponse> listLecture(LecturerListRequest request) {
        return baseMapper.listLecture(request);
    }

    @Override
    public IPage<LecturerPageResponse> pageAdmin(LecturerPageRequest params) {
        return baseMapper.pageAdmin(params.createMpPage(), params);
    }

}

