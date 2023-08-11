package cn.qbs.wa.teach.exam.answer.service.impl;

import cn.qbs.wa.teach.exam.answer.mapper.CategoryMapper;
import cn.qbs.wa.teach.exam.answer.service.CategoryService;
import cn.qbs.wa.teach.exam.common.entity.Category;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 分类(Category)表服务实现类
 *
 * @author zcm
 * @since 2021-12-14 11:34:12
 */
@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

}

