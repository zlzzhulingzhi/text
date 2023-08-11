package cn.qbs.wa.teach.question.service.impl;

import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserListResultDTO;
import cn.qbs.wa.teach.domain.BasePageRequest;
import cn.qbs.wa.teach.question.entity.QuestionImportRecord;
import cn.qbs.wa.teach.question.mapper.QuestionImportRecordMapper;
import cn.qbs.wa.teach.question.pojo.question.QuestionImportRecordPageResponse;
import cn.qbs.wa.teach.question.service.QuestionImportRecordService;
import cn.qbs.wa.teach.question.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 试题导入记录表(QuestionImportRecord)表服务实现类
 *
 * @author zcm
 * @version 1.0
 * @date 2022-06-17 17:41:44
 */
@Slf4j
@Service("questionImportRecordService")
public class QuestionImportRecordServiceImpl extends ServiceImpl<QuestionImportRecordMapper, QuestionImportRecord> implements QuestionImportRecordService {

    @Resource
    private UserService userService;


    @Override
    public IPage<QuestionImportRecordPageResponse> page(BasePageRequest params) {
        IPage<QuestionImportRecordPageResponse> page = baseMapper.page(params.createMpPage());
        List<QuestionImportRecordPageResponse> recordResponseList = page.getRecords();
        if (CollectionUtils.isNotEmpty(recordResponseList)) {
            List<Long> userIdList = recordResponseList.stream().map(QuestionImportRecordPageResponse::getCreateBy).collect(Collectors.toList());
            List<UserListResultDTO> userList = userService.getUserList(userIdList);
            if (CollectionUtils.isNotEmpty(userList)) {
                Map<Long, UserListResultDTO> userMap = userList.stream().collect(Collectors.toMap(UserListResultDTO::getId, Function.identity()));
                recordResponseList.forEach(i -> {
                    UserListResultDTO user = userMap.get(i.getCreateBy());
                    if (user != null) {
                        i.setImporter(user.getRealName());
                    }
                });
            }
        }
        return page;
    }

}

