package cn.qbs.wa.teach.course.standard.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.course.common.entity.CourseAttachment;
import cn.qbs.wa.teach.course.standard.mapper.CourseAttachmentMapper;
import cn.qbs.wa.teach.course.standard.pojo.courseattachment.*;
import cn.qbs.wa.teach.course.standard.pojo.coursecomponent.ComponentChangeNameRequest;
import cn.qbs.wa.teach.course.standard.service.CourseAttachmentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 【课程讲义】(CourseAttachment)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-29 14:43:51
 */
@Slf4j
@Service("courseAttachmentService")
public class CourseAttachmentServiceImpl extends ServiceImpl<CourseAttachmentMapper, CourseAttachment> implements CourseAttachmentService {
    //@Resource
    //private RemoteFileService remoteFileService;


    @Override
    public boolean add(CourseAttachmentAddRequest params) {
        CourseAttachment courseAttachment = new CourseAttachment();
        BeanUtils.copyProperties(params, courseAttachment);
        return this.save(courseAttachment);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addBatch(CourseAttachmentAddBatchRequest params) {
        List<CourseAttachment> attachList = params.getAttachList().stream()
                .peek(e -> e.setCourseId(params.getCourseId()))
                .map(e -> BeanUtil.copyProperties(e, CourseAttachment.class))
                .collect(Collectors.toList());
        this.saveBatch(attachList);
        return false;
    }

    @Override
    public boolean changeName(ComponentChangeNameRequest params) {
        CourseAttachment courseAttachment = this.getById(params.getId());
        if (courseAttachment == null) {
            throw new IllegalParamsException("查不到课程讲义！");
        }

        return this.lambdaUpdate().eq(CourseAttachment::getId, params.getId())
                .set(CourseAttachment::getResourceFileName, params.getName())
                .update();
    }


    @Override
    public IPage<CourseAttachmentPageResponse> page(CourseAttachmentPageRequest params) {
        IPage<CourseAttachmentPageResponse> courseAttachmentPage = this.baseMapper.page(params.createMpPage(), params);
        fillFileSize(courseAttachmentPage);
        return courseAttachmentPage;
    }

    private void fillFileSize(IPage<CourseAttachmentPageResponse> courseAttachmentPage) {
        //if (courseAttachmentPage != null) {
        //    List<CourseAttachmentPageResponse> courseAttachmentList = courseAttachmentPage.getRecords();
        //    if (CollectionUtils.isNotEmpty(courseAttachmentList)) {
        //        List<Long> fileIdList = courseAttachmentList.stream().map(CourseAttachmentPageResponse::getResourceFileId).collect(Collectors.toList());
        //        FileRequestDTO fileRequestDTO = new FileRequestDTO();
        //        fileRequestDTO.setFileIdList(fileIdList);
        //        fileRequestDTO.setFileType("all");
        //        R<List<FileResponseDTO>> r = remoteFileService.list(fileRequestDTO);
        //        if (r != null && r.isOk()) {
        //            List<FileResponseDTO> list = r.getData();
        //            if (CollectionUtils.isNotEmpty(list)) {
        //                Map<Long, FileResponseDTO> map = list.stream().collect(Collectors.toMap(FileResponseDTO::getId, Function.identity()));
        //                for (CourseAttachmentPageResponse courseAttachment : courseAttachmentList) {
        //                    FileResponseDTO fileResponseDTO = map.get(courseAttachment.getResourceFileId());
        //                    if (fileResponseDTO != null) {
        //                        courseAttachment.setFileSize(fileResponseDTO.getFileSize());
        //                        courseAttachment.setLength(fileResponseDTO.getLength());
        //                    }
        //                }
        //            }
        //        }
        //    }
        //}
    }

    @Override
    public List<CourseAttachmentPageResponse> list(CourseAttachmentListRequest params) {
        return this.baseMapper.list(params);
    }

    @Override
    public List<CourseAttachmentPageResponse> listByCourseId(Long courseId) {
        CourseAttachmentListRequest params = new CourseAttachmentListRequest();
        params.setCourseId(courseId);
        return this.list(params);
    }

    @Override
    public CourseAttachmentDetailResponse detail(Serializable id) {
        return this.baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(CourseAttachmentUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        CourseAttachment courseAttachment = new CourseAttachment();
        BeanUtils.copyProperties(params, courseAttachment);
        return this.updateById(courseAttachment);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

