package cn.qbs.wa.teach.question.mapper;

import java.io.Serializable;
import java.util.List;

import cn.qbs.wa.teach.question.pojo.category.CategoryQuestionCountRequest;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.question.entity.Category;
import cn.qbs.wa.teach.question.pojo.category.CategoryDetailResponse;
import cn.qbs.wa.teach.question.pojo.category.CategoryPageRequest;
import cn.qbs.wa.teach.question.pojo.category.CategoryPageResponse;

/**
 * 分类(Category)表数据库访问层
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 17:27:56
 */
public interface CategoryMapper extends BaseMapper<Category> {

    @InterceptorIgnore(tenantLine = "true")
    int insertBatch(@Param("entities") List<Category> entities);

    IPage<CategoryPageResponse> page(@Param("page") IPage<?> page, @Param("params") CategoryPageRequest params);

    CategoryDetailResponse selectDetailById(Serializable id);

    List<CategoryPageResponse> childrenList(@Param("parentId") Long parentId);

    /**
     * 获取当前节点下面的子节点ID列表
     * @param parentId
     * @return
     */
    List<Long> getChildIdList(@Param("parentId") Long parentId);

    Long selectQuestionCount(@Param("param") CategoryQuestionCountRequest param);

    Long selectPaperCount(@Param("categoryIdList") List<Long> categoryIdList);

}

