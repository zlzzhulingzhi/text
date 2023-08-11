package cn.qbs.wa.teach.organization.controller;


import cn.hutool.core.util.IdUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.excel.EmployeeData;
import cn.qbs.wa.teach.organization.excel.EmployeeDataListener;
import cn.qbs.wa.teach.organization.excel.EmployeeDownloadData;
import cn.qbs.wa.teach.organization.pojo.employee.EmployeeDownloadRequest;
import cn.qbs.wa.teach.organization.pojo.importemployee.*;
import cn.qbs.wa.teach.organization.service.ImportEmployeeService;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;


/**
 * 导入职工表(ImportEmployee)表控制层
 *
 * @author makejava
 * @since 2021-11-16 11:29:43
 */
@RestController
@RequestMapping("importEmployee")
@Api(tags = "职工导入管理")
public class ImportEmployeeController {

    /**
     * 服务对象
     */
    @Resource
    private ImportEmployeeService importEmployeeService;


    /**
     * 新增导入职工表
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("importEmployee:add")
    //@Log(title = "新增导入职工表", businessType = BusinessType.INSERT)
    @ApiOperation("新增")
    public R<Boolean> add(@RequestBody @Validated ImportEmployeeAddRequest params) {
        return R.ok(this.importEmployeeService.add(params));
    }

    /**
     * 分页查询导入职工表
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("importEmployee:page")
    //@Log(title = "分页查询导入职工表", businessType = BusinessType.OTHER)
    @ApiOperation("分页查询")
    public R<IPage<ImportEmployeePageResponse>> page(@RequestBody ImportEmployeePageRequest params) {
        return R.ok(this.importEmployeeService.page(params));
    }

    /**
     * 查询导入职工表详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("importEmployee:details")
    //@Log(title = "导入职工表详情", businessType = BusinessType.OTHER)
    @ApiOperation("详情")
    public R<ImportEmployeeDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.importEmployeeService.detail(request.getId()));
    }

    /**
     * 修改导入职工表
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("importEmployee:update")
    //@Log(title = "更新导入职工表", businessType = BusinessType.UPDATE)
    @ApiOperation("更新")
    public R<Boolean> update(@RequestBody @Validated ImportEmployeeUpdateRequest params) {
        return R.ok(this.importEmployeeService.update(params));
    }

    /**
     * 删除导入职工表
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("importEmployee:delete")
    //@Log(title = "删除导入职工表", businessType = BusinessType.DELETE)
    @ApiOperation("删除")
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.importEmployeeService.deleteByIds(request.getIdList()));
    }


    @ApiOperation(value = "预导入职工信息")
    @PostMapping("/preview")
    public R importPre(MultipartFile file) throws Exception {
        /*String eventId = IdUtil.fastSimpleUUID();
        SecurityContextHolder.setSelectOrgId( SecurityContextHolder.getOrgId().toString());
        EasyExcel.read(file.getInputStream(), EmployeeData.class, new EmployeeDataListener(importEmployeeService, 66L, eventId)).sheet().doRead();
        return R.ok(eventId);*/
        SecurityContextHolder.setSelectOrgId( SecurityContextHolder.getOrgId().toString());
        return R.ok(importEmployeeService.importPre(file,SecurityContextHolder.getOrgId()));
    }

    @ApiOperation(value = "管理员预导入职工信息")
    @PostMapping("admin/preview")
    public R adminImportPre(MultipartFile file, @RequestParam("orgId") Long orgId) throws Exception {
        String eventId = IdUtil.fastSimpleUUID();
        SecurityContextHolder.setSelectOrgId(orgId.toString());
        /*EasyExcel.read(file.getInputStream(), EmployeeData.class, new EmployeeDataListener(importEmployeeService, orgId, eventId,deptId)).sheet().doRead();
        return R.ok(eventId);*/
        return R.ok(importEmployeeService.importPre(file,orgId));
    }



    @ApiOperation(value = "导出")
    @PostMapping("download")
    public void download(HttpServletResponse response, @RequestBody EmployeeDownloadRequest request) throws IOException {

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("用户导出-"+LocalDate.now().toString(), "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        request.setOrgId(SecurityContextHolder.getOrgId());
        EasyExcel.write(response.getOutputStream(), EmployeeDownloadData.class).sheet("sheet1").doWrite(importEmployeeService.listDownloadData(request));
    }

    @ApiOperation(value = "管理员导出")
    @PostMapping("admin/download")
    public void adminDownload(HttpServletResponse response, @RequestBody EmployeeDownloadRequest request) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("用户导出-"+LocalDate.now().toString(), "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        SecurityContextHolder.setSelectOrgId(request.getOrgId().toString());
        EasyExcel.write(response.getOutputStream(), EmployeeDownloadData.class).sheet("sheet1").doWrite(importEmployeeService.listDownloadData(request));
    }

}

