package cn.qbs.wa.teach.cert.controller;

import cn.qbs.wa.teach.cert.pojo.cert.*;
import cn.qbs.wa.teach.cert.service.CertService;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.R;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/1/22 15:22
 */

@RestController
@RequestMapping("center")
@Api(tags = "证书中心")
public class CertCenterController {

    @Autowired
    CertService certService;


    @PostMapping("select-list")
    @ApiOperation("证书选择列表")
    public R<List<CertListResponse>> mySelectList(@RequestBody CertListRequest params) {
        return R.ok(this.certService.mySelectList(params));
    }

    @PostMapping("myCertSearch")
    @ApiOperation("证书查询")
    public R<MyCertSearchResponse> myCertSearch(@RequestBody @Valid MyCertSearchRequest params) {
        return R.ok(this.certService.myCertSearch(params));
    }

    @PostMapping("myPage")
    @ApiOperation("分页查询我的证书")
    public R<IPage<CertPageResponse>> page(@RequestBody CertPageRequest params) {
        params.setUserId(SecurityContextHolder.getUserId());
        return R.ok(this.certService.myPage(params));
    }

    @PostMapping("myPageV2")
    @ApiOperation("web分页查询我的证书")
    public R<IPage<CertPageResponse>> pageV2(@RequestBody CertPageRequest params) {
        params.setUserId(SecurityContextHolder.getUserId());
        return R.ok(this.certService.myPageV2(params));
    }

    @PostMapping("H5/myPageV2")
    @ApiOperation("H5分页查询我的证书")
    public R<IPage<CertPageResponse>> H5pageV2(@RequestBody CertPageRequest params) {
        params.setUserId(SecurityContextHolder.getUserId());
        return R.ok(this.certService.myPageV2(params));
    }
}
