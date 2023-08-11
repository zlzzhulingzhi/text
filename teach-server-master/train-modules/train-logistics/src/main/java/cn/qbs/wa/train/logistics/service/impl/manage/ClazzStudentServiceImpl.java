package cn.qbs.wa.train.logistics.service.impl.manage;

import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDetailResponseDTO;
import cn.qbs.wa.train.logistics.mapper.ClazzStudentMapper;
import cn.qbs.wa.train.logistics.entity.ClazzStudent;
import cn.qbs.wa.train.logistics.pojo.clazz.ClazzDetailRequest;
import cn.qbs.wa.train.logistics.pojo.clazz.ClazzDetailResponse;
import cn.qbs.wa.train.logistics.pojo.organization.OrganizationDetailResponse;
import cn.qbs.wa.train.logistics.service.manage.ClazzService;
import cn.qbs.wa.train.logistics.service.manage.ClazzStudentService;
import cn.qbs.wa.train.logistics.pojo.clazzstudent.*;
import cn.qbs.wa.train.logistics.service.manage.OrganizationManageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 班级学员表(ClazzStudent)表服务实现类
 *
 * @author makejava
 * @since 2022-10-08 17:28:14
 */
@Slf4j
@Service("clazzStudentService")
public class ClazzStudentServiceImpl extends ServiceImpl<ClazzStudentMapper, ClazzStudent> implements ClazzStudentService {

    @Autowired
    ClazzService clazzService;

    @Autowired
    OrganizationManageService organizationManageService;

    @Autowired
    RemoteStudentService remoteStudentService;

    @Override
    public boolean add(List<ClazzStudentAddRequest> params) {
        List<ClazzStudent> clazzStudentList=new ArrayList<>();
        for (ClazzStudentAddRequest clazzStudentAddRequest:params) {
            ClazzStudent clazzStudent = new ClazzStudent();
            ClazzStudentPageRequest clazzStudentPageRequest=new ClazzStudentPageRequest();
            clazzStudentPageRequest.setStudentId(clazzStudentAddRequest.getStudentId());
            clazzStudentPageRequest.setClazzId(clazzStudentAddRequest.getClazzId());
            //查询数据库是否有要添加的数据
            IPage<ClazzStudentPageResponse> clazzStudentPageResponseIPage=page(clazzStudentPageRequest);
            if (!Constants.DB_FAIL.equals(clazzStudentPageResponseIPage.getRecords().size())){
                throw new ServiceException("请勿重复添加数据");
            }
            BeanUtils.copyProperties(clazzStudentAddRequest, clazzStudent);
            clazzStudentList.add(clazzStudent);
        }
        return this.saveBatch(clazzStudentList);
    }

    @Override
    public IPage<ClazzStudentPageResponse> page(ClazzStudentPageRequest params) {
        IPage<ClazzStudentPageResponse> clazzStudentPageResponseIPage=baseMapper.page(params.createMpPage(), params);
        List<ClazzStudentPageResponse> clazzStudentPageResponseList=clazzStudentPageResponseIPage.getRecords();
        if (!Constants.DB_FAIL.equals(clazzStudentPageResponseList.size())){
            for (ClazzStudentPageResponse clazzStudentPageResponse:clazzStudentPageResponseList) {
               /* //获取机构名称
                OrganizationDetailResponse organizationDetailResponse=organizationManageService.detail(clazzStudentPageResponse.getOrgId());
                clazzStudentPageResponse.setOrgName(organizationDetailResponse.getName());
                //获取班级名称
                ClazzDetailResponse clazzDetailResponse = clazzService.detail(clazzStudentPageResponse.getClazzId());
                clazzStudentPageResponse.setClazzName(clazzDetailResponse.getName());*/
                StudentDTO studentDTO=new StudentDTO();
                studentDTO.setId(clazzStudentPageResponse.getStudentId());
                //获取学员名字
                StudentDetailResponseDTO studentDetailResponseDTO=remoteStudentService.getStudentName(studentDTO).getRemoteData();
                clazzStudentPageResponse.setStudentName(studentDetailResponseDTO.getRealName());
                clazzStudentPageResponse.setPhone(studentDetailResponseDTO.getPhone());
            }
        }
        return clazzStudentPageResponseIPage;
    }

    @Override
    public ClazzStudentDetailResponse detail(Serializable id) {
        ClazzStudentDetailResponse clazzStudentDetailResponse=baseMapper.selectDetailById(id);;
        //获取机构名称
        OrganizationDetailResponse organizationDetailResponse=organizationManageService.detail(clazzStudentDetailResponse.getOrgId());
        clazzStudentDetailResponse.setOrgName(organizationDetailResponse.getName());
        //获取班级名称
        ClazzDetailRequest clazzDetailRequest=new ClazzDetailRequest();
        clazzDetailRequest.setId(clazzStudentDetailResponse.getClazzId());
        ClazzDetailResponse clazzDetailResponse = clazzService.detail(clazzDetailRequest);
        clazzStudentDetailResponse.setClazzName(clazzDetailResponse.getName());
        StudentDTO studentDTO=new StudentDTO();
        studentDTO.setId(clazzStudentDetailResponse.getStudentId());
        //获取学员名字
        StudentDetailResponseDTO studentDetailResponseDTO=remoteStudentService.getStudentName(studentDTO).getRemoteData();
        clazzStudentDetailResponse.setStudentName(studentDetailResponseDTO.getRealName());
        return clazzStudentDetailResponse;
    }

    @Override
    public boolean update(ClazzStudentUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        ClazzStudentDetailResponse clazzStudentDetailResponse=detail(params.getId());
        if(params.getClazzId()!=null && params.getStudentId()==null){
            ClazzStudentPageRequest clazzStudentPageRequest=new ClazzStudentPageRequest();
            clazzStudentPageRequest.setStudentId(clazzStudentDetailResponse.getStudentId());
            clazzStudentPageRequest.setClazzId(params.getClazzId());
            //查询数据库是否有要修改的数据
            IPage<ClazzStudentPageResponse> clazzStudentPageResponseIPage=page(clazzStudentPageRequest);
            if (!Constants.DB_FAIL.equals(clazzStudentPageResponseIPage.getRecords().size())){
                throw new ServiceException("数据库已有该条数据");
            }
        }
        if(params.getStudentId()!=null && params.getClazzId()==null){
            ClazzStudentPageRequest clazzStudentPageRequest=new ClazzStudentPageRequest();
            clazzStudentPageRequest.setStudentId(params.getStudentId());
            clazzStudentPageRequest.setClazzId(clazzStudentDetailResponse.getClazzId());
            //查询数据库是否有要修改的数据
            IPage<ClazzStudentPageResponse> clazzStudentPageResponseIPage=page(clazzStudentPageRequest);
            if (!Constants.DB_FAIL.equals(clazzStudentPageResponseIPage.getRecords().size())){
                throw new ServiceException("数据库已有该条数据");
            }
        }
        if(params.getStudentId()!=null && params.getClazzId()!=null){
            ClazzStudentPageRequest clazzStudentPageRequest=new ClazzStudentPageRequest();
            clazzStudentPageRequest.setStudentId(params.getStudentId());
            clazzStudentPageRequest.setClazzId(params.getClazzId());
            //查询数据库是否有要修改的数据
            IPage<ClazzStudentPageResponse> clazzStudentPageResponseIPage=page(clazzStudentPageRequest);
            if (!Constants.DB_FAIL.equals(clazzStudentPageResponseIPage.getRecords().size())){
                throw new ServiceException("数据库已有该条数据");
            }
        }
        ClazzStudent clazzStudent = new ClazzStudent();
        BeanUtils.copyProperties(params, clazzStudent);
        return this.updateById(clazzStudent);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public List<ClazzStudentMap> queryClazzLast(List<Long> memberIds) {
        return baseMapper.queryClazzLast(memberIds);
    }
}

