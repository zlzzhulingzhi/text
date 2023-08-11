package cn.qbs.wa.teach.organization.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.course.api.RemoteCourseLecturerService;
import cn.qbs.wa.teach.course.api.RemoteCourseService;
import cn.qbs.wa.teach.course.api.pojo.DTO.courseLecture.CourseLecturerPageRequestDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.courseLecture.CourseLecturerQueryDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.lecturer.CourseLecturerDTO;
import cn.qbs.wa.teach.organization.entity.Employee;
import cn.qbs.wa.teach.organization.entity.Lecturer;

import cn.qbs.wa.teach.organization.mapper.LecturerMapper;
import cn.qbs.wa.teach.organization.pojo.lecturer.*;
import cn.qbs.wa.teach.organization.service.EmployeeService;
import cn.qbs.wa.teach.organization.service.LecturerService;
import cn.qbs.wa.train.logistics.api.RemoteTrainClazzService;
import cn.qbs.wa.train.logistics.api.pojo.DTO.IntegrateClazzResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Resource
    private RemoteCourseService remoteCourseService;

    @Resource
    RemoteCourseLecturerService remoteCourseLecturerService;

    @Resource
    RemoteTrainClazzService remoteTrainClazzService;

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
            List<Lecturer> lecturerList = this.lambdaQuery().eq(Lecturer::getEmployeeId, params.getEmployeeId()).ne(Lecturer::getId,params.getId()).list();
            if (CollUtil.isNotEmpty(lecturerList)) {
                throw new ServiceException("该账号已关联其他讲师！");
            }
        }
        lecturer = new Lecturer();
        BeanUtils.copyProperties(params, lecturer);
        //远程修改课程讲师信息
        CourseLecturerDTO courseLecturerDTO = new CourseLecturerDTO();
        courseLecturerDTO.setLecturerId(lecturer.getId());
        courseLecturerDTO.setLecturerName(lecturer.getLecturerName());
        remoteCourseService.updateCourseLecturer(courseLecturerDTO);
        return this.updateById(lecturer);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        CourseLecturerPageRequestDTO courseLecturerPageRequestDTO=new CourseLecturerPageRequestDTO();
        for (Long lecturerId : idList) {
            courseLecturerPageRequestDTO.setLecturerId(lecturerId);
            Long count=remoteCourseLecturerService.count(courseLecturerPageRequestDTO).getRemoteData();
            if (count> Constants.DB_FAIL) {
                throw new ServiceException("该讲师已关联课程，无法删除！");
            }
        }
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

    @Override
    public List<LecturerClazzResponse> pageLecturerClazz(Long lecturerId) {
        R<List<IntegrateClazzResponse>> r = remoteTrainClazzService.listClazzByLecturerId(new IdRequest(lecturerId));
        if (!r.isOk()) {
            throw new ServiceException(r.getMsg());
        }
        if (CollUtil.isEmpty(r.getData())) {
            return Collections.emptyList();
        }
        return BeanUtil.copyToList(r.getData(), LecturerClazzResponse.class);
    }

    @Override
    public IPage<LecturerResponse> pageLecturerByPlat(LecturerPageQuery params) {
        IPage<LecturerResponse> page = params.createMpPage();
        List<Long> lecturerIds = new ArrayList<>();
        if (StrUtil.isNotBlank(params.getCourseName())) {
            // 根据课程名称查询对应的讲师ID数组
            CourseLecturerQueryDTO lecturerQuery = new CourseLecturerQueryDTO();
            lecturerQuery.setCourseName(params.getCourseName());
            R<List<Long>> r = remoteCourseLecturerService.queryLecturerIds(lecturerQuery);
            if (!r.isOk()) {
                throw new ServiceException(r.getMsg());
            }
            if (CollUtil.isEmpty(r.getData())) {
                return page;
            }
            lecturerIds = r.getData();
        }
        LecturerPageRequest req = new LecturerPageRequest();
        req.setIdList(lecturerIds);
        req.setName(params.getLecturerName());
        if (params.getOrgId() != null) {
            SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
            IPage<LecturerPageResponse> p = baseMapper.page(page, req);
            page = convertLecturerPage(page, p);
        } else {
            IPage<LecturerPageResponse> p = baseMapper.pageAdmin(page, req);
            page = convertLecturerPage(page, p);
        }
        // 查询 班级班级信息
        if (page.getTotal() > 0) {
            lecturerIds = page.getRecords().stream().map(LecturerResponse::getLecturerId).collect(Collectors.toList());
            R<Map<Long, String>> r = remoteTrainClazzService.queryClazzLastByLecturerIds(new IdListRequest(lecturerIds, null));
            if (r.getRemoteData() != null) {
                Map<Long, String> lecturerMapClazz = r.getData();
                for (LecturerResponse record : page.getRecords()) {
                    record.setClazzName(lecturerMapClazz.get(record.getLecturerId()));
                }
            }
        }
        return page;
    }

    private IPage<LecturerResponse> convertLecturerPage(IPage<LecturerResponse> page, IPage<LecturerPageResponse> p) {
        if (!p.getRecords().isEmpty()) {
            page = p.convert(e -> {
                LecturerResponse response = new LecturerResponse();
                response.setLecturerId(e.getId());
                response.setLecturerName(e.getLecturerName());
                response.setOrgName(e.getOrgName());
                response.setSex(e.getSex());
                return response;
            });
        }
        return page;
    }

}

