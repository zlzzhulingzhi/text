package cn.qbs.wa.teach.question.service.impl;

import cn.qbs.wa.teach.question.entity.PaperCategory;
import cn.qbs.wa.teach.question.mapper.PaperCategoryMapper;
import cn.qbs.wa.teach.question.service.PaperCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 试卷分类关联(PaperCategory)表服务实现类
 *
 * @author zcm
 * @version 1.0
 * @date 2021-12-02 15:37:02
 */
@Slf4j
@Service("paperCategoryService")
public class PaperCategoryServiceImpl extends ServiceImpl<PaperCategoryMapper, PaperCategory> implements PaperCategoryService {

    
}

