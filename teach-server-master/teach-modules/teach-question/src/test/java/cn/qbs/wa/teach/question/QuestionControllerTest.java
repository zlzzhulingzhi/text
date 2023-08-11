package cn.qbs.wa.teach.question;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.question.entity.Question;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @Author zcm
 * @Date 2021/10/28 15:44
 * @Version 1.0
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class QuestionControllerTest {

    private static final String BASE_URL = "/question";

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void addTest() throws Exception {
        Question question = new Question();
        question.setQuestionTypeId(RandomUtils.nextLong(1, 5));
        question.setDifficultyId(RandomUtils.nextLong(1, 5));
        question.setScore(BigDecimal.valueOf(RandomUtils.nextDouble(0.5, 5)));

        SecurityContextHolder.setOrgId(String.valueOf(RandomUtils.nextInt(1, 5)));
        mockMvc.perform(MockMvcRequestBuilders
                .post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JSON.toJSONString(question)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andDo(print())
                .andReturn();
    }

    @Test
    public void batchAdd() throws Exception {
        int size = 30;
        for (int i = 0; i < size; i++) {
            this.addTest();
        }

    }

    @Test
    public void pageTest() throws Exception {
        SecurityContextHolder.setOrgId("4");
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("current", "2")
                        .param("size", "3")
                        .header("Authorization", "Bearer ********-****-****-****-************")
        ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andDo(print());
    }

    @Test
    public void details() throws Exception {
        Integer id = 6;
        SecurityContextHolder.setOrgId("1");
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(BASE_URL + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andDo(print());
    }

    @Test
    public void updateTest() throws Exception {
        Question question = new Question();
        question.setId(6L);
        question.setScore(BigDecimal.valueOf(RandomUtils.nextDouble(0.5, 5)));

        SecurityContextHolder.setOrgId("1");
        mockMvc.perform(MockMvcRequestBuilders
                .put(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JSON.toJSONString(question)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(true))
                .andDo(print());
    }

    @Test
    public void deleteTest() throws Exception {
        String idList = "21, 26";
        SecurityContextHolder.setOrgId("1");
        mockMvc.perform(MockMvcRequestBuilders
                .delete(BASE_URL)
                .param("idList", idList)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(true))
                .andDo(MockMvcResultHandlers.print());
    }



    @Test
    public void add() throws Exception {

        Question question = new Question();
        question.setQuestionTypeId(1000L);
        question.setDifficultyId(3L);
        question.setScore(BigDecimal.valueOf(5));

        SecurityContextHolder.setOrgId("100");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JSON.toJSONString(question)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        //得到返回代码
        int status = mvcResult.getResponse().getStatus();
        log.info("status: {}", status);
        //得到返回结果
        String content = mvcResult.getResponse().getContentAsString();
        log.info("content: {}", content);

    }


}
