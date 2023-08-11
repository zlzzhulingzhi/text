package cn.qbs.wa.teach.course.standard.service.impl;

import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.course.standard.entity.TCourse;
import cn.qbs.wa.teach.course.standard.mapper.TCourseMapper;
import cn.qbs.wa.teach.course.standard.pojo.course.CourseDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.course.CourseListRequest;
import cn.qbs.wa.teach.course.standard.pojo.tcourse.TCoursePageRequest;
import cn.qbs.wa.teach.course.standard.pojo.tcourse.TCoursePageResponse;
import cn.qbs.wa.teach.course.standard.pojo.tcoursestudent.TCourseStudentPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.tcoursestudent.TCourseStudentPageResponse;
import cn.qbs.wa.teach.course.standard.service.CourseService;
import cn.qbs.wa.teach.course.standard.service.TCourseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程-报名分享(TCourse)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-24 19:09:01
 */
@Slf4j
@Service("tCourseService")
public class TCourseServiceImpl extends ServiceImpl<TCourseMapper, TCourse> implements TCourseService {

    @Resource
    CourseService courseService;

    @Override
    public List<TCoursePageResponse> listCourse(TCoursePageRequest params) {
        List<Long> courseIdList=new ArrayList<>();
        if(StringUtils.isNotBlank(params.getCourseName())){
            CourseListRequest courseListRequest=new CourseListRequest();
            courseListRequest.setCourseName(params.getCourseName());
            List<CourseDetailResponse> courseDetailResponseList=courseService.getCourse(courseListRequest);
            for (CourseDetailResponse courseDetailResponse:courseDetailResponseList) {
                courseIdList.add(courseDetailResponse.getId());
            }
            params.setCourseIdList(courseIdList);
            if(courseIdList==null || courseIdList.isEmpty() ){
                courseIdList.add(Constants.DB_FAIL.longValue());
                params.setCourseIdList(courseIdList);
                List<TCoursePageResponse> tCoursePageResponseList=baseMapper.listCourse(params);
                return tCoursePageResponseList;
            }
        }
        List<TCoursePageResponse> tCoursePageResponseList=baseMapper.listCourse(params);
        /*for (TCoursePageResponse tCoursePageResponse:tCoursePageResponseList) {
            //查询课程信息
            CourseDetailResponse courseDetailResponse=courseService.getCourseName(tCoursePageResponse.getCourseId());
            if(courseDetailResponse!=null){
                tCoursePageResponse.setCourseName(courseDetailResponse.getCourseName());
            }
        }*/
        return tCoursePageResponseList;
    }
}

