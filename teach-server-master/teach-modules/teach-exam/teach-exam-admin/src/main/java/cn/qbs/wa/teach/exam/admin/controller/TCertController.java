package cn.qbs.wa.teach.exam.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.cert.api.RemoteCertService;
import cn.qbs.wa.teach.cert.api.pojo.DTO.cert.CertPageRequestDTO;
import cn.qbs.wa.teach.cert.api.pojo.DTO.cert.CertPageResponseDTO;
import cn.qbs.wa.teach.cert.api.pojo.DTO.cert.awardRecord.AwardRecordPageResponseDTO;
import cn.qbs.wa.teach.cert.api.pojo.DTO.cert.awardRecord.PersonCertDetailRequestDTO;
import cn.qbs.wa.teach.cert.api.pojo.DTO.cert.awardRecord.PersonCertDetailResponseDTO;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.BatchFlagRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.teach.exam.admin.pojo.exam.ExamRecord;
import cn.qbs.wa.teach.exam.admin.pojo.tcert.*;
import cn.qbs.wa.teach.exam.admin.service.TCertService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;


/**
 * 考试证书(TCert)表控制层
 *
 * @author makejava
 * @since 2022-05-16 13:47:59
 */
@RestController
@RequestMapping("tCert")
@Api(tags = "考试模块的证书")
public class TCertController {

    /**
     * 服务对象
     */
    @Resource
    private TCertService tCertService;

    @Resource
    private RemoteCertService remoteCertService;


    /**
     * 新增考试的证书
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @ApiOperation("新增考试的证书")
    //@RequiresPermissions("tCert:add")
    //@Log(title = "新增考试的证书", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated TCertAddRequest params) {
        return R.ok(this.tCertService.add(params));
    }

    /**
     * 选择需要关联的证书列表
     *
     * @param params
     * @return
     */
    @PostMapping("certPage")
    //@RequiresPermissions("tCert:page")
    //@Log(title = "选择需要关联的证书列表", businessType = BusinessType.OTHER)
    @ApiOperation("选择需要关联的证书列表")
    public R<PageResultComDTO<CertPageResponseDTO>> certPage(@RequestBody TCertPageRequest params) {
        return this.remoteCertService.page(BeanUtil.copyProperties(params, CertPageRequestDTO.class));

    }

    /**
     * 已创建的考试证书列表
     *
     * @param
     * @return
     */
    @PostMapping("certAndExamList")
    //@RequiresPermissions("tCert:delete")
    //@Log(title = "已创建的考试证书列表", businessType = BusinessType.DELETE)
    @ApiOperation("已创建的考试证书列表")
    public R<List<CertDetailResponse>> certList(@RequestBody TCertDetailRequest request) {
        return R.ok(this.tCertService.certList(request));
    }

    /**
     * 分页查询任务证书表
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("tCert:page")
    //@Log(title = "分页查询任务证书表", businessType = BusinessType.OTHER)
    @ApiIgnore
    public R<IPage<TCertPageResponse>> page(@RequestBody TCertPageRequest params) {
        return R.ok(this.tCertService.page(params));
    }

    /**
     * 查询任务证书表详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("tCert:details")
    //@Log(title = "任务证书表详情", businessType = BusinessType.OTHER)
    @ApiIgnore
    public R<TCertDetailResponse> detail(Long id) {
        return R.ok(this.tCertService.detail(id));
    }

    /**
     * 修改任务证书表
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("tCert:update")
    //@Log(title = "更新任务证书表", businessType = BusinessType.UPDATE)
    @ApiIgnore
    public R<Boolean> update(@RequestBody @Validated TCertUpdateRequest params) {
        return R.ok(this.tCertService.update(params));
    }

    /**
     * 删除已创建的证书
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("tCert:delete")
    //@Log(title = "删除已创建的证书", businessType = BusinessType.DELETE)
    @ApiOperation("删除已创建的证书")
    public R<Boolean> delete(@RequestBody @Validated TCertUpdateRequest params) {
        return R.ok(this.tCertService.deleteByIds(params));
    }

    /**
     * 发放证书用户分页
     * @param recordPageRequest
     * @return
     */
    @PostMapping("award/page")
    //@RequiresPermissions("cert:details")
    //@Log(title = "证书详情", businessType = BusinessType.OTHER)
    @ApiOperation("分页查询颁发证书用户列表")
    public R<PageResultComDTO<AwardRecordPageResponseDTO>> awardRecordPage(@RequestBody AwardRecordPageRequest idRequest){
        return R.ok(this.tCertService.awardRecordPage(idRequest));
    }

    /**
     * 证书记录 禁用/启用
     * @param batchFlagRequest
     * @return
     */
    @PostMapping("award/batch-enabled")
    @ApiOperation("证书作废,支持批量和单个")
    public R batchEnable(@RequestBody @Validated BatchFlagRequest batchFlagRequest){
        batchFlagRequest.setOrgId(SecurityContextHolder.getOrgId());
        return R.ok(this.remoteCertService.batchEnable(batchFlagRequest));
    }

    @PostMapping("acquire")
    //@RequiresPermissions("cert:details")
    //@Log(title = "查询个人已获取的证书", businessType = BusinessType.OTHER)
    @ApiOperation("查询个人已获取的证书")
    public R<List<PersonCertDetailResponseDTO>> acquire(@RequestBody PersonCertDetailRequestDTO personCertDetailRequest){
        return this.remoteCertService.acquire(personCertDetailRequest);
    }

    @PostMapping("getCertInfo")
    //@RequiresPermissions("cert:details")
    //@Log(title = "个人查询考试证书信息", businessType = BusinessType.OTHER)
    @ApiOperation("个人查询考试证书信息")
    @ApiIgnore
    public R<List<PersonCertDetailResponseDTO>> getCertInfo(@RequestBody ExamRecord examRecord) {
        //查询证书id
        return R.ok(tCertService.getCertInfo(examRecord));

    }

}

