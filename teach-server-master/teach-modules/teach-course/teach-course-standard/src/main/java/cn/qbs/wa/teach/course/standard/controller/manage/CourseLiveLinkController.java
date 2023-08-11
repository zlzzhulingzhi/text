//package cn.qbs.wa.teach.course.standard.controller.manage;
//
//import cn.qbs.wa.teach.common.core.domain.IdListRequest;
//import cn.qbs.wa.teach.common.core.domain.R;
//import cn.qbs.wa.teach.common.log.annotation.Log;
//import cn.qbs.wa.teach.common.log.enums.BusinessType;
//import cn.qbs.wa.teach.course.common.entity.CourseLiveLink;
//import cn.qbs.wa.teach.course.standard.pojo.courselivelink.CourseLiveLinkAddRequest;
//import cn.qbs.wa.teach.course.standard.pojo.courselivelink.CourseLiveLinkDetailRequest;
//import cn.qbs.wa.teach.course.standard.pojo.courselivelink.CourseLiveLinkDetailResponse;
//import cn.qbs.wa.teach.course.standard.pojo.courselivelink.CourseLiveLinkUpdateRequest;
//import cn.qbs.wa.teach.course.standard.service.CourseLiveLinkService;
//import cn.qbs.wa.teach.course.standard.service.RemoteService;
//import cn.qbs.wa.teach.course.live.api.pojo.DTO.live.LiveResultDTO;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.BeanUtils;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * 【课程直播外链】(CourseLiveLink)表控制层
// *
// * @author makejava
// * @version 1.0
// * @date 2021-12-29 17:01:10
// */
//@Api(tags = "课程关联直播")
//@RestController
//@RequestMapping("/live/link")
//public class CourseLiveLinkController {
//
//    /**
//     * 服务对象
//     */
//    @Resource
//    private CourseLiveLinkService courseLiveLinkService;
//
//    @Resource
//    private RemoteService remoteService;
//
//    /**
//     * 新增【课程直播外链】
//     *
//     * @param params
//     * @return
//     */
//    @ApiOperation(value = "新增关联直播")
//    @PostMapping("/add")
//    //@RequiresPermissions("courseLiveLink:add")
//    @Log(title = "新增关联直播", businessType = BusinessType.INSERT)
//    public R<Boolean> add(@RequestBody @Validated CourseLiveLinkAddRequest params) {
//        return R.ok(this.courseLiveLinkService.add(params));
//    }
//
//    /**
//     * 查询【课程直播外链】详情
//     */
//    @ApiOperation(value = "课程关联直播详情")
//    @PostMapping("/detail")
//    //@RequiresPermissions("courseLiveLink:details")
//    //@Log(title = "【课程直播外链】详情", businessType = BusinessType.OTHER)
//    public R<CourseLiveLinkDetailResponse> detail(@RequestBody @Validated CourseLiveLinkDetailRequest request) {
//        CourseLiveLinkDetailResponse linkDetailResponse = this.courseLiveLinkService.detailByCourseId(request.getCourseId());
//        if (linkDetailResponse != null) {
//            LiveResultDTO liveResultDTO = remoteService.remoteLive(linkDetailResponse.getLiveId());
//            if (liveResultDTO != null) {
//                BeanUtils.copyProperties(liveResultDTO, linkDetailResponse, "id", "sort");
//            }
//        }
//        return R.ok(linkDetailResponse);
//    }
//
//    /**
//     * 修改【课程直播外链】
//     *
//     * @param params
//     * @return
//     */
//    @ApiOperation(value = "更新关联直播")
//    @PostMapping("/update")
//    //@RequiresPermissions("courseLiveLink:update")
//    @Log(title = "更新关联直播", businessType = BusinessType.UPDATE)
//    public R<Boolean> update(@RequestBody @Validated CourseLiveLinkUpdateRequest params) {
//        return R.ok(this.courseLiveLinkService.update(params));
//    }
//
//    /**
//     * 删除【课程直播外链】
//     *
//     * @param idList 主键集合
//     * @return
//     */
//    @ApiOperation(value = "删除关联直播")
//    @PostMapping("/delete")
//    //@RequiresPermissions("courseLiveLink:delete")
//    @Log(title = "删除关联直播", businessType = BusinessType.DELETE)
//    public R<Boolean> delete(@RequestBody @Validated IdListRequest idList) {
//        List<CourseLiveLink> courseLiveLinks = this.courseLiveLinkService.listByIds(idList.getIdList());
//        if (!courseLiveLinks.isEmpty()) {
//            return R.ok(Boolean.TRUE);
//        }
//        boolean delete = this.courseLiveLinkService.deleteByIds(idList.getIdList());
//        if (delete) {
//            for (CourseLiveLink liveLink : courseLiveLinks) {
//                remoteService.remoteAssociateLive(liveLink.getCourseId(), 1);
//            }
//        }
//        return R.ok(delete);
//    }
//
//}
//
