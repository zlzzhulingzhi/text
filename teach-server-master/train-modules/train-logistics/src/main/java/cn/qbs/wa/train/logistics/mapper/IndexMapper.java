package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.pojo.index.OrgInfoResponse;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yjx
 */
public interface IndexMapper {

    @InterceptorIgnore(tenantLine = "true")
    List<OrgInfoResponse> listOrg(@Param("userId") Long userId);
}
