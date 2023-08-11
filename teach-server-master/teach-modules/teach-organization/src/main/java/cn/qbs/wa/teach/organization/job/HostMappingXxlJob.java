//package cn.qbs.wa.teach.organization.job;
//
//import cn.hutool.core.util.StrUtil;
//import cn.qbs.wa.teach.common.core.constant.CacheConstants;
//import cn.qbs.wa.teach.common.core.constant.Constants;
//import cn.qbs.wa.teach.common.redis.service.RedisService;
//import cn.qbs.wa.teach.organization.entity.Organization;
//import cn.qbs.wa.teach.organization.service.OrganizationService;
//import com.xxl.job.core.context.XxlJobHelper;
//import com.xxl.job.core.handler.annotation.XxlJob;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Component
//public class HostMappingXxlJob {
//
//    @Resource
//    private OrganizationService organizationService;
//
//    @Resource
//    private RedisService redisService;
//
//    @XxlJob("rebuildHostMapping")
//    public void rebuildHostMapping() {
//        List<Organization> organizationList = organizationService.lambdaQuery().select(Organization::getId, Organization::getDomain).eq(Organization::getEnabled, Constants.DB_TRUE).list();
//        if (!organizationList.isEmpty()) {
//            Map<String, String> domainMap = organizationList.stream().filter(e -> StrUtil.isNotBlank(e.getDomain())).collect(Collectors.toMap(Organization::getDomain, o -> o.getId().toString(), (a, b) -> a));
//            XxlJobHelper.log("域名映射: {}", domainMap);
//            redisService.deleteObject(CacheConstants.TENANT_HOST_MAPPING);
//            redisService.setCacheMap(CacheConstants.TENANT_HOST_MAPPING, domainMap);
//        }
//    }
//
//}
