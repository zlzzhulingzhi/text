package cn.qbs.wa.teach.basic.service;

import cn.qbs.wa.teach.basic.entity.Application;
import cn.qbs.wa.teach.basic.pojo.app.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * (Application)表服务接口
 *
 * @author makejava
 * @since 2021-11-02 14:55:26
 */
public interface ApplicationService extends IService<Application> {

    IPage<ApplicationPageResponse> pageApplication(ApplicationPageRequest pageRequest);


    List<ApplicationFullResponse> getChildrenByAppTypeId(Long applicationTypeId);

    void batchEnabled(Integer flag, List<Long> idList);

    void add(ApplicationAddRequest request);

    void update(ApplicationUpdateRequest request);

    ApplicationOneResponse detail(Long id);

    void deleteByIds(List<Long> idList);
}

