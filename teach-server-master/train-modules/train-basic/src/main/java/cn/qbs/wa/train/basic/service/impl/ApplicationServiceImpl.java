package cn.qbs.wa.train.basic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.train.basic.entity.Application;
import cn.qbs.wa.train.basic.entity.ApplicationApplicationType;
import cn.qbs.wa.train.basic.entity.ApplicationType;
import cn.qbs.wa.train.basic.entity.Menu;
import cn.qbs.wa.train.basic.mapper.ApplicationMapper;
import cn.qbs.wa.train.basic.mapper.ApplicationTypeMapper;
import cn.qbs.wa.train.basic.pojo.app.*;
import cn.qbs.wa.train.basic.service.ApplicationApplicationTypeService;
import cn.qbs.wa.train.basic.service.ApplicationService;
import cn.qbs.wa.train.basic.service.MenuService;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (Application)表服务实现类
 *
 * @author makejava
 * @since 2021-11-02 14:55:27
 */
@Slf4j
@Service("applicationService")
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, Application> implements ApplicationService {

    @Autowired
    ApplicationApplicationTypeService applicationApplicationTypeService;

    @Autowired
    MenuService menuService;

    @Autowired
    ApplicationTypeMapper applicationTypeMapper;

    @Override
    public IPage<ApplicationPageResponse> pageApplication(ApplicationPageRequest request) {
        if (Constants.PLAT_APP_TYPE_ID.equals(request.getAppTypeId())) {
            List<ApplicationType> applicationTypeList = applicationTypeMapper.selectList(Wrappers.<ApplicationType>lambdaQuery().select(ApplicationType::getId).eq(ApplicationType::getParentId, Constants.PLAT_APP_TYPE_ID));
            if (CollUtil.isNotEmpty(applicationTypeList)) {
                List<Long> ids = applicationTypeList.stream().map(ApplicationType::getId).collect(Collectors.toList());
                ids.add(Constants.PLAT_APP_TYPE_ID);
                LambdaQueryWrapper<Application> queryWrapper = Wrappers.<Application>lambdaQuery().in(Application::getAppTypeId, ids)
                        .eq(request.getEnabled() != null, Application::getEnabled, request.getEnabled())
                        .like(StrUtil.isNotBlank(request.getName()), Application::getName, request.getName());
                return this.page(request.createMpPage(), queryWrapper).convert(e -> BeanUtil.copyProperties(e, ApplicationPageResponse.class));
            }
        }
        return this.baseMapper.page(request.createMpPage(), request);

    }


    @Override
    public List<ApplicationFullResponse> getChildrenByAppTypeId(Long applicationTypeId) {
        return baseMapper.getChildrenByAppTypeId(applicationTypeId);
    }

    @Override
    public void batchEnabled(Integer flag, List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请选中");
        }
        List<Application> applications = new ArrayList<>();
        for (Long roleId : idList) {
            Application app = new Application();
            app.setId(roleId);
            app.setEnabled(flag);
            applications.add(app);
        }
        updateBatchById(applications);
    }

    @Override
    @Transactional
    public void add(ApplicationAddRequest request) {
        Application application = new Application();
        BeanUtils.copyProperties(request, application);
        save(application);
        //addApplicationApplicationType(request.getApplicationTypeIdList(), application);
    }

    @Override
    @Transactional
    public void update(ApplicationUpdateRequest request) {
        Application application = new Application();
        BeanUtils.copyProperties(request, application);
        updateById(application);
       // applicationApplicationTypeService.remove(new LambdaQueryWrapper<ApplicationApplicationType>().eq(ApplicationApplicationType::getAppId, application.getId()));
       // addApplicationApplicationType(request.getApplicationTypeIdList(), application);
    }

    @Override
    public ApplicationOneResponse detail(Long id) {
        ApplicationOneResponse applicationOneResponse = new ApplicationOneResponse();
        Application application = getById(id);
        BeanUtils.copyProperties(application, applicationOneResponse);
//        List<ApplicationApplicationType> applicationApplicationTypes = applicationApplicationTypeService.list(new LambdaQueryWrapper<ApplicationApplicationType>().eq(ApplicationApplicationType::getAppId, id));
//        if (CollectionUtils.isNotEmpty(applicationApplicationTypes)) {
//            applicationOneResponse.setApplicationTypeIdList(applicationApplicationTypes.stream().map(ApplicationApplicationType::getAppTypeId).collect(Collectors.toList()));
//        }

        return applicationOneResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<Long> idList) {
        removeByIds(idList);
        menuService.remove(new LambdaQueryWrapper<Menu>().in(Menu::getAppId, idList));
    }

    private void addApplicationApplicationType(List<Long> applicationTypeIdList, Application application) {
        if (CollectionUtils.isNotEmpty(applicationTypeIdList)) {
            List<ApplicationApplicationType> applicationApplicationTypes = new ArrayList<>();
            for (Long applicationTypeId : applicationTypeIdList) {
                ApplicationApplicationType applicationApplicationType = new ApplicationApplicationType();
                applicationApplicationType.setAppTypeId(applicationTypeId);
                applicationApplicationType.setAppId(application.getId());
                applicationApplicationTypes.add(applicationApplicationType);
            }
            applicationApplicationTypeService.saveBatch(applicationApplicationTypes);
        }
    }
}

