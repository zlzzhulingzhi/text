package cn.qbs.wa.train.logistics.service.impl.platform;

import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.train.logistics.entity.ClassroomAttach;
import cn.qbs.wa.train.logistics.enums.FileTypeEnum;
import cn.qbs.wa.train.logistics.mapper.ClassroomAttachMapper;
import cn.qbs.wa.train.logistics.pojo.classroomattach.*;
import cn.qbs.wa.train.logistics.pojo.classroomdevice.ClassroomDevicePageResponse;
import cn.qbs.wa.train.logistics.service.platform.ClassroomAttachService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 教室附件(ClassroomAttach)表服务实现类
 *
 * @author makejava
 * @since 2022-10-12 15:36:11
 */
@Slf4j
@Service("classroomAttachService")
public class ClassroomAttachServiceImpl extends ServiceImpl<ClassroomAttachMapper, ClassroomAttach> implements ClassroomAttachService {

    @Override
    public boolean add(ClassroomAttachAddRequest params) {
        List<ClassroomAttach> classroomAttachList = new ArrayList<>();
        if(params.getFileUrl() != null){
            String[] fileUrls=params.getFileUrl();
            for (int i=0;i<params.getFileUrl().length;i++){
                ClassroomAttach classroomAttach=new ClassroomAttach();
                classroomAttach.setClassroomId(params.getClassroomId());
                classroomAttach.setFileType(params.getFileType());
                classroomAttach.setFileUrl(fileUrls[i]);
                classroomAttachList.add(classroomAttach);
            }
        }
        return this.saveBatch(classroomAttachList);
    }

    @Override
    public IPage<ClassroomAttachPageResponse> page(ClassroomAttachPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public ClassroomAttachDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(ClassroomAttachUpdateRequest params) {
        ClassroomAttachPageRequest classroomAttachPageRequest=new ClassroomAttachPageRequest();
        classroomAttachPageRequest.setClassroomId(params.getClassroomId());
        //查询教室附件
        List<ClassroomAttachPageResponse> classroomAttachPageResponseList=page(classroomAttachPageRequest).getRecords();
        if(params.getFileUrl() != null){
            if (classroomAttachPageResponseList == null ) {
                List<ClassroomAttach> classroomAttachList = new ArrayList<>();
                if(params.getFileUrl() != null){
                    String[] fileUrls=params.getFileUrl();
                    for (int i=0;i<params.getFileUrl().length;i++){
                        ClassroomAttach classroomAttach=new ClassroomAttach();
                        classroomAttach.setClassroomId(params.getClassroomId());
                        classroomAttach.setFileType(params.getFileType());
                        classroomAttach.setFileUrl(fileUrls[i]);
                        classroomAttachList.add(classroomAttach);
                    }
                }
                return this.saveBatch(classroomAttachList);
            }else {
                List<Long> idList =new ArrayList<>() ;
                for (ClassroomAttachPageResponse classroomAttachPageResponse: classroomAttachPageResponseList) {
                    idList.add(classroomAttachPageResponse.getId());
                }
                //删除原来教室附件
                deleteByIds(idList);
                List<ClassroomAttach> classroomAttachList = new ArrayList<>();
                if(params.getFileUrl() != null){
                    String[] fileUrls=params.getFileUrl();
                    for (int i=0;i<params.getFileUrl().length;i++){
                        ClassroomAttach classroomAttach=new ClassroomAttach();
                        classroomAttach.setClassroomId(params.getClassroomId());
                        classroomAttach.setFileType(params.getFileType());
                        classroomAttach.setFileUrl(fileUrls[i]);
                        classroomAttachList.add(classroomAttach);
                    }
                }
                return this.saveBatch(classroomAttachList);
            }
        }
        return true;

    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

