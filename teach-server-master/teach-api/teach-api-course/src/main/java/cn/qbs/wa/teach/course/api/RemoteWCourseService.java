package cn.qbs.wa.teach.course.api;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.factory.RemoteWCourseFallbackFactory;
import cn.qbs.wa.teach.course.api.pojo.DTO.wcourse.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Administrator
 */
@FeignClient(contextId = "remoteWCourseService", name = "teach-course", path = "course/WCourse", fallbackFactory = RemoteWCourseFallbackFactory.class)
public interface RemoteWCourseService {


    /**
     * 分页查询机构插件表
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    @ApiOperation("分页查询插件表")
    R<PageResultComDTO<WCoursePageResultDTO>> page(@RequestBody WCoursePageSearchDTO params);

    @PostMapping("/search")
    R<PageResultComDTO<WCoursePageResultDTO>> search(@RequestBody WCoursePageSearchDTO params);

    /**
     * 分页查询插件
     *
     * @param params
     * @return
     */
    @PostMapping("pageByChild")
    //@RequiresPermissions("wCourse:page")
    //@Log(title = "分页查询插件-课程表", businessType = BusinessType.OTHER)
    R<PageResultComDTO<WCoursePageByChildResultDTO>> pageByChild(@RequestBody WCoursePageSearchDTO params);


    /**
     * 新增插件-讲师表
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @ApiOperation("新增插件-课程表")
    R add(@RequestBody @Validated WCourseAddDTO params);

    /**
     * 删除插件-讲师表
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    @ApiOperation("删除插件-课程表")
    R delete(@RequestBody IdListRequest request);

    /**
     * 修改插件-课程表
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    @ApiOperation("更新插件-课程表")
    R<Boolean> update(@RequestBody @Validated WCourseUpdateDTO params);


}
