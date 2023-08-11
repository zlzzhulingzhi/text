package cn.qbs.wa.train.logistics.service.impl.manage;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.CacheConstants;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.train.logistics.entity.*;
import cn.qbs.wa.train.logistics.enums.InitRoleEnum;
import cn.qbs.wa.train.logistics.enums.OrgCategoryEnum;
import cn.qbs.wa.train.logistics.mapper.OrganizationMapper;
import cn.qbs.wa.train.logistics.pojo.dept.TreeDeptResponse;
import cn.qbs.wa.train.logistics.pojo.organization.*;
import cn.qbs.wa.train.logistics.pojo.organizationmenu.OrganizationMenuListRequest;
import cn.qbs.wa.train.logistics.service.*;
import cn.qbs.wa.train.logistics.service.manage.OrganizationManageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 组织机构(Organization)表服务实现类
 *
 * @author makejava
 * @since 2021-11-09 20:13:13
 */
@Slf4j
@Service("OrganizationManageService")
public class OrganizationManageServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrganizationManageService {

    @Override
    public OrganizationDetailResponse detail(Serializable id) {
        OrganizationDetailResponse organizationDetailResponse = baseMapper.selectDetailById(id);
        return organizationDetailResponse;
    }



}

