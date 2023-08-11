package cn.qbs.wa.teach.organization.controller;

import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.CacheConstants;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.teach.common.security.auth.AuthUtil;
import cn.qbs.wa.teach.organization.entity.Employee;
import cn.qbs.wa.teach.organization.entity.Organization;
import cn.qbs.wa.teach.organization.pojo.employee.EmployeeLoginPermissionResponse;
import cn.qbs.wa.teach.organization.pojo.index.EmployeeLoginRequest;
import cn.qbs.wa.teach.organization.pojo.index.OrgInfoResponse;
import cn.qbs.wa.teach.organization.service.EmployeeService;
import cn.qbs.wa.teach.organization.service.IndexService;
import cn.qbs.wa.teach.organization.service.OrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yjx
 */
@Api(tags = "机构后台-登录首页")
@RefreshScope
@RestController
public class IndexController {

    @Resource
    private IndexService indexService;

    @Resource
    private EmployeeService employeeService;

    @Resource
    private OrganizationService organizationService;

    @Resource
    private RedisService redisService;

    @Value("${system.protocol:https://}")
    private String httpProtocol;

    @Value("${system.primaryHost:armpc.cn}")
    private String primaryHost;

    @ApiOperation("机构列表")
    @PostMapping("/org-list")
    public R<List<OrgInfoResponse>> listOrg() {
        Long userId = SecurityContextHolder.getUserId();
        List<OrgInfoResponse> orgList = indexService.listOrg(userId);
        return R.ok(orgList);
    }

    @ApiOperation("职工菜单权限")
    @PostMapping("/login-permission")
    public R<EmployeeLoginPermissionResponse> getUserPermission(@RequestBody EmployeeLoginRequest request) {
        Long userId = SecurityContextHolder.getUserId();
        Employee employee = employeeService.lambdaQuery().eq(Employee::getOrgId, request.getOrgId()).eq(Employee::getUserId, userId).one();
        if (employee == null) {
            return R.ok(new EmployeeLoginPermissionResponse());
        }
        if (Constants.DB_FAIL.equals(employee.getEnabled())) {
            return R.fail("该员工账号已被禁用，请联系机构管理员");
        }
        SecurityContextHolder.setSelectOrgId(request.getOrgId().toString());
        return R.ok(employeeService.getEmployeePermission(request.getApplicationTypeId(), employee));
    }

    @ApiOperation("获取登录用户机构域名地址")
    @PostMapping("/domain")
    public R<OrgInfoResponse> domain() {
        Long orgId = SecurityContextHolder.getOrgId();
        Organization org = organizationService.getById(orgId);
        OrgInfoResponse response = new OrgInfoResponse();
        if (org != null && StrUtil.isNotBlank(org.getDomain())) {
            //String domain = org.getDomain();
            String domain = "wa-screen";
            response.setDomain(httpProtocol + domain + StrUtil.DOT + primaryHost);
            Boolean hasKey = redisService.hasCacheMapKey(CacheConstants.TENANT_HOST_MAPPING, domain);
            if (Boolean.FALSE.equals(hasKey)) {
                redisService.setCacheMapValue(CacheConstants.TENANT_HOST_MAPPING, domain, org.getId().toString());
            }
        }
        return R.ok(response);
    }

    @ApiOperation("重定向域名地址")
    @PostMapping("/redirect")
    public R<String> redirect(@RequestBody IdRequest idRequest) {
        Long orgId = SecurityContextHolder.getOrgId();
        if (Constants.INIT_ORG.equals(orgId)) {
            orgId = idRequest.getId();
        }
        Organization org = organizationService.getById(orgId);
        if (org != null && StrUtil.isNotBlank(org.getDomain())) {
            String domain = org.getDomain();
            String host = httpProtocol + domain + StrUtil.DOT + primaryHost;
            Boolean hasKey = redisService.hasCacheMapKey(CacheConstants.TENANT_HOST_MAPPING, domain);
            if (Boolean.FALSE.equals(hasKey)) {
                redisService.setCacheMapValue(CacheConstants.TENANT_HOST_MAPPING, domain, org.getId().toString());
            }
            return R.ok(host);
        }
        return R.ok();
    }
}
