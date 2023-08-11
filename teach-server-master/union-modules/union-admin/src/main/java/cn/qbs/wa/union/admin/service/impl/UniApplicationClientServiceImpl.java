package cn.qbs.wa.union.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.union.admin.entity.UniApplicationClient;
import cn.qbs.wa.union.admin.mapper.UniApplicationClientMapper;
import cn.qbs.wa.union.admin.pojo.uniapplicationclient.*;
import cn.qbs.wa.union.admin.service.UniApplicationClientService;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 统一应用客户端(UniApplicationClient)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:10
 */
@Slf4j
@Service("uniApplicationClientService")
public class UniApplicationClientServiceImpl extends ServiceImpl<UniApplicationClientMapper, UniApplicationClient> implements UniApplicationClientService {

    @Override
    public boolean add(UniApplicationClientAddRequest params) {
        UniApplicationClient uniApplicationClient = new UniApplicationClient();
        BeanUtils.copyProperties(params, uniApplicationClient);
        return this.save(uniApplicationClient);
    }

    @Override
    public IPage<UniApplicationClientPageResponse> page(UniApplicationClientPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public UniApplicationClientDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(UniApplicationClientUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        UniApplicationClient uniApplicationClient = new UniApplicationClient();
        BeanUtils.copyProperties(params, uniApplicationClient);
        return this.updateById(uniApplicationClient);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public List<UniApplicationClientDetailResponse> clientList() {
        return BeanUtil.copyToList(list(),UniApplicationClientDetailResponse.class);
    }

}

