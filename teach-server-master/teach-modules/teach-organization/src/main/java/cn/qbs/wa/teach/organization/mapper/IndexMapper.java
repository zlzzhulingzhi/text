package cn.qbs.wa.teach.organization.mapper;

import cn.qbs.wa.teach.organization.pojo.index.OrgInfoResponse;
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
