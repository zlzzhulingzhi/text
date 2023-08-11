package cn.qbs.wa.teach.cert.controller;


import cn.qbs.wa.teach.cert.entity.Cert;
import cn.qbs.wa.teach.cert.pojo.awardrecord.AwardRecordDetailResponse;
import cn.qbs.wa.teach.cert.pojo.awardrecord.AwardRecordPageRequest;
import cn.qbs.wa.teach.cert.pojo.awardrecord.AwardRecordPageResponse;
import cn.qbs.wa.teach.cert.pojo.cert.*;
import cn.qbs.wa.teach.cert.service.CertPostService;
import cn.qbs.wa.teach.cert.service.CertService;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.*;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.common.post.drawable.Poster;
import cn.qbs.wa.teach.common.security.annotation.AutoSelectOrg;
import cn.qbs.wa.teach.exam.api.RemoteExamApiService;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamDTO;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamPageRequestDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;


/**
 * 证书(Cert)表控制层
 *
 * @author makejava
 * @since 2022-01-19 19:19:21
 */
@RestController
@RequestMapping("cert")
@Api(tags = "证书管理")
public class CertController {

    /**
     * 服务对象
     */
    @Resource
    CertService certService;

    @Autowired
    RemoteExamApiService remoteExamApiService;

    @Autowired
    CertPostService certPostService;


    /**
     * 新增证书
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("cert:add")
    //@Log(title = "新增证书", businessType = BusinessType.INSERT)
    @ApiOperation("新增")
    public R<Cert> add(@RequestBody @Validated CertAddRequest params) {
        return R.ok(this.certService.add(params));
    }

    /**
     * 分页查询证书
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("cert:page")
    //@Log(title = "分页查询证书", businessType = BusinessType.OTHER)
    @ApiOperation("分页")
    public R<IPage<CertPageResponse>> page(@RequestBody CertPageRequest params) {
        return R.ok(this.certService.page(params));
    }

    /**
     * 查询证书详情
     *
     * @param  主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("cert:details")
    //@Log(title = "证书详情", businessType = BusinessType.OTHER)
    @ApiOperation("证书详情")
    public R<CertDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.certService.detail(request.getId()));
    }


    /**
     * 修改证书
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("cert:update")
    //@Log(title = "更新证书", businessType = BusinessType.UPDATE)
    @ApiIgnore
    public R<Boolean> update(@RequestBody @Validated CertUpdateRequest params) {
        return R.ok(this.certService.update(params));
    }

    /**
     * 删除证书
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("cert:delete")
    //@Log(title = "删除证书", businessType = BusinessType.DELETE)
    @ApiOperation("删除")
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.certService.deleteByIds(request.getIdList()));
    }

    /**
     * 批量启用/禁用
     */
    @PostMapping("batch-enabled")
    //@RequiresPermissions("role:delete")
    @Log(title = "批量启用/禁用】", businessType = BusinessType.OTHER)
    @ApiOperation("批量启用/禁用")
    public R batchEnabled(@RequestBody BatchFlagRequest request) {
        this.certService.batchEnabled(request.getFlag(), request.getIdList());
        return R.ok();
    }

    @PostMapping("exam/page")
    @ApiOperation("考试分页")
    public R<PageResultComDTO<ExamDTO>> getExamPage(@RequestBody ExamPageRequestDTO params) {
        return R.ok(remoteExamApiService.page(params).getData());
    }

    @PostMapping("user/page")
    @ApiOperation("机构用户分页")
    public R<IPage<CertUserResponse>> getUserPage(@RequestBody CertUserPageRequest params) {
        return R.ok(this.certService.getUserPage(params));
    }

    @PostMapping("student/page")
    @ApiOperation("机构学员分页")
    public R<IPage<CertUserResponse>> getStudentPage(@RequestBody CertStudentPageRequest searchDTO) {
        return R.ok(this.certService.getStudentPage(searchDTO));
    }


    @PostMapping("exam-user/list")
    @ApiOperation("考试用户列表")
    public R<List<CertUserResponse>> getExamUserList(@RequestBody CertExamUserListRequest params) {
        return R.ok(this.certService.getExamUserList(params));
    }

    @PostMapping("postInfo")
    @ApiOperation("海报信息查看")
    public R postInfo(@RequestBody Poster poster) {
        return R.ok(new Poster());
    }


    @PostMapping("award")
    @ApiOperation("证书发布")
    @AutoSelectOrg
    public R award(@RequestBody CertAwardRequest certAwardRequest) {
        certService.award(certAwardRequest);
        certService.certPost(certAwardRequest);
        return R.ok();
    }

    @PostMapping("award-retry")
    @ApiOperation("证书重新发布")
    public R awardRetry(@RequestBody IdRequest request) {
        certService.certPostRetry(request);
        return R.ok();
    }


    @PostMapping("award/batch-enabled")
    @ApiOperation("证书记录 禁用/启用")
    public R batchEnable(@RequestBody @Validated BatchFlagRequest batchFlagRequest) {
        certService.awardBatchEnable(batchFlagRequest);
        certService.certWaterPost(batchFlagRequest, SecurityContextHolder.getOrgId());
        return R.ok();
    }

    @PostMapping("award/page")
    //@RequiresPermissions("cert:details")
    //@Log(title = "证书详情", businessType = BusinessType.OTHER)
    @ApiOperation("发放证书用户分页")
    public R<IPage<AwardRecordPageResponse>> awardRecordPage(@RequestBody AwardRecordPageRequest recordPageRequest) {
        return R.ok(this.certService.awardRecordPage(recordPageRequest));
    }

    @PostMapping("repetition")
    //@RequiresPermissions("cert:details")
    //@Log(title = "判断证书是否添加重复", businessType = BusinessType.OTHER)
    @ApiOperation("判断证书是否添加重复")
    public R<List<CertDetailResponse>> repetition(@RequestBody CertListRequest certDetailRequest) {
        return R.ok(this.certService.repetition(certDetailRequest));
    }

    @PostMapping("acquire")
    //@RequiresPermissions("cert:details")
    //@Log(title = "查询个人已获取的证书", businessType = BusinessType.OTHER)
    @ApiOperation("查询个人已获取的证书")
    public R<List<PersonCertDetailResponse>> acquire(@RequestBody PersonCertDetailRequest personCertDetailRequest) {
        return R.ok(this.certService.acquire(personCertDetailRequest));
    }

    @PostMapping("grant")
    //@RequiresPermissions("cert:details")
    //@Log(title = "查询证书是否已发布", businessType = BusinessType.OTHER)
    @ApiOperation("查询证书是否已发布")
    public R<List<AwardRecordDetailResponse>> grant(@RequestBody IdRequest idRequest) {
        return R.ok(this.certService.grant(idRequest));
    }

    @PostMapping("batchSelect")
    //@RequiresPermissions("cert:details")
    //@Log(title = "批量查询证书", businessType = BusinessType.OTHER)
    @ApiOperation("批量查询证书,主要供外部使用")
    public R<List<PersonCertDetailResponse>> batchSelect(@RequestBody BatchGetCertRequest batchGetCertRequest) {
        return R.ok(this.certService.batchSelect(batchGetCertRequest));
    }
}

