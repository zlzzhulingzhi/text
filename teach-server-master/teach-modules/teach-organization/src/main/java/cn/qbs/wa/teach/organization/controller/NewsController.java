package cn.qbs.wa.teach.organization.controller;


import cn.qbs.wa.teach.common.core.domain.BatchFlagRequest;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.organization.pojo.news.*;
import cn.qbs.wa.teach.organization.service.NewsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;



/**
 * 新闻(News)表控制层
 *
 * @author makejava
 * @since 2022-01-18 09:30:01
 */
@RestController
@RequestMapping("news")
@Api(tags = "新闻管理")
public class NewsController {

    /**
     * 服务对象
     */
    @Resource
    private NewsService newsService;


    /**
     * 新增新闻
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("news:add")
    //@Log(title = "新增新闻", businessType = BusinessType.INSERT)
    @ApiOperation("新增")
    public R<Boolean> add(@RequestBody @Validated NewsAddRequest params) {
        return R.ok(this.newsService.add(params));
    }

    /**
     * 分页查询新闻
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("news:page")
    //@Log(title = "分页查询新闻", businessType = BusinessType.OTHER)
    @ApiOperation("分页查询")
    public R<IPage<NewsPageResponse>> page(@RequestBody NewsPageRequest params) {
        return R.ok(this.newsService.page(params));
    }

    /**
     * 查询新闻详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("news:details")
    //@Log(title = "新闻详情", businessType = BusinessType.OTHER)
    @ApiOperation("详情")
    public R<NewsDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.newsService.detail(request.getId()));
    }

    /**
     * 修改新闻
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("news:update")
    //@Log(title = "更新新闻", businessType = BusinessType.UPDATE)
    @ApiOperation("更新")
    public R<Boolean> update(@RequestBody @Validated NewsUpdateRequest params) {
        return R.ok(this.newsService.update(params));
    }

    /**
     * 删除新闻
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("news:delete")
    //@Log(title = "删除新闻", businessType = BusinessType.DELETE)
    @ApiOperation("删除")
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.newsService.deleteByIds(request.getIdList()));
    }



    /**
     * 批量启用/禁用
     */
    @PostMapping("batch-status")
    //@RequiresPermissions("role:delete")
    @Log(title = "批量上架/下架】", businessType = BusinessType.OTHER)
    @ApiOperation("批量上架/下架")
    public R batchStatus(@RequestBody BatchFlagRequest request) {
        this.newsService.batchStatus(request.getFlag(), request.getIdList());
        return R.ok();
    }

    @PostMapping("updateViews")
    //@RequiresPermissions("news:update")
    //@Log(title = "更新新闻", businessType = BusinessType.UPDATE)
    @ApiOperation("更新浏览量")
    public R updateViews(@RequestBody IdRequest params) {
        this.newsService.updateViews(params);
        return R.ok();
    }

}

