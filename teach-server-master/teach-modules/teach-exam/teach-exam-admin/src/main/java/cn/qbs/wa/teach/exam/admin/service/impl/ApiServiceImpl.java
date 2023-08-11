package cn.qbs.wa.teach.exam.admin.service.impl;

import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserListResultDTO;
import cn.qbs.wa.teach.exam.admin.mapper.ExamMapper;
import cn.qbs.wa.teach.exam.admin.pojo.api.ApiExamPageRequest;
import cn.qbs.wa.teach.exam.admin.pojo.api.ApiExamPageResponse;
import cn.qbs.wa.teach.exam.admin.pojo.api.ApiExamineeeListRequest;
import cn.qbs.wa.teach.exam.admin.pojo.api.ApiExamineeeListResponse;
import cn.qbs.wa.teach.exam.admin.service.ApiService;
import cn.qbs.wa.teach.exam.admin.service.ExamService;
import cn.qbs.wa.teach.exam.admin.service.UserService;
import cn.qbs.wa.teach.exam.common.entity.Exam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author zcm
 * @Date 2022/1/19 14:16
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiServiceImpl implements ApiService {

    private final ExamMapper examMapper;

    private final ExamService examService;

    private final UserService userService;


    @Override
    public IPage<ApiExamPageResponse> examPage(ApiExamPageRequest params) {
        return this.examService.lambdaQuery()
                .like(StringUtils.isNotBlank(params.getExamName()), Exam::getExamName, params.getExamName())
                .page(params.createMpPage())
                .convert(i -> {
                    ApiExamPageResponse response = new ApiExamPageResponse();
                    response.setExamId(i.getId());
                    response.setExamName(i.getExamName());
                    response.setPassScore(i.getPassScore());
                    response.setCreateTime(i.getCreateTime());
                    return response;
                });
    }

    @Override
    public List<ApiExamineeeListResponse> examineeList(ApiExamineeeListRequest params) {
        List<ApiExamineeeListResponse> examineeeListResponseList = examMapper.selectExamineeList(params);
        if (CollectionUtils.isNotEmpty(examineeeListResponseList)) {
            List<Long> userIdList = examineeeListResponseList.stream().map(i -> i.getUserId()).collect(Collectors.toList());
            List<UserListResultDTO> userList = userService.getUserList(userIdList);
            if (CollectionUtils.isNotEmpty(userList)) {
                for (ApiExamineeeListResponse response : examineeeListResponseList) {
                    UserListResultDTO userListResultDTO = userList.stream().filter(u -> u.getId().equals(response.getUserId())).findFirst().orElse(null);
                    if (userListResultDTO != null) {
                        response.setExamineeName(userListResultDTO.getRealName());
                    }
                }
            }
        }
        return examineeeListResponseList;
    }

}
