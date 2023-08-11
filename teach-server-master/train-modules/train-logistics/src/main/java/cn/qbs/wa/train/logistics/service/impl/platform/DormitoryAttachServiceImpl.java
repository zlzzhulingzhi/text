package cn.qbs.wa.train.logistics.service.impl.platform;

import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.train.logistics.mapper.DormitoryAttachMapper;
import cn.qbs.wa.train.logistics.entity.DormitoryAttach;
import cn.qbs.wa.train.logistics.pojo.classroomattach.ClassroomAttachPageRequest;
import cn.qbs.wa.train.logistics.pojo.classroomattach.ClassroomAttachPageResponse;
import cn.qbs.wa.train.logistics.service.platform.DormitoryAttachService;
import cn.qbs.wa.train.logistics.pojo.dormitoryattach.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 宿舍附件(DormitoryAttach)表服务实现类
 *
 * @author makejava
 * @since 2022-10-13 09:36:14
 */
@Slf4j
@Service("dormitoryAttachService")
public class DormitoryAttachServiceImpl extends ServiceImpl<DormitoryAttachMapper, DormitoryAttach> implements DormitoryAttachService {

    @Override
    public boolean add(DormitoryAttachAddRequest params) {
        DormitoryAttach dormitoryAttach = new DormitoryAttach();
        BeanUtils.copyProperties(params, dormitoryAttach);
        if(params.getFileUrl()!=null){
            String fileUrl="";
            for (int i=0;i<params.getFileUrl().length;i++){
                fileUrl=fileUrl+","+params.getFileUrl()[i];
            }
            dormitoryAttach.setFileUrl(fileUrl.substring(Constants.DB_TRUE));
        }
        return this.save(dormitoryAttach);
    }

    @Override
    public IPage<DormitoryAttachPageResponse> page(DormitoryAttachPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public DormitoryAttachDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(DormitoryAttachUpdateRequest params) {
        DormitoryAttachPageRequest dormitoryAttachPageRequest=new DormitoryAttachPageRequest();
        dormitoryAttachPageRequest.setDormitoryId(params.getDormitoryId());
        //查询宿舍附件
        List<DormitoryAttachPageResponse> dormitoryAttachPageResponseList=page(dormitoryAttachPageRequest).getRecords();
        if (dormitoryAttachPageResponseList == null ) {
            throw new IllegalParamsException("ID不能为空！");
        }
        DormitoryAttach dormitoryAttach = new DormitoryAttach();
        BeanUtils.copyProperties(params, dormitoryAttach);
        dormitoryAttach.setId(dormitoryAttachPageResponseList.get(Constants.DB_FAIL).getId());
        if(params.getFileUrl()!=null){
            String fileUrl="";
            for (int i=0;i<params.getFileUrl().length;i++){
                fileUrl=fileUrl+","+params.getFileUrl()[i];
            }
            dormitoryAttach.setFileUrl(fileUrl.substring(Constants.DB_TRUE));
        }
        return this.updateById(dormitoryAttach);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

