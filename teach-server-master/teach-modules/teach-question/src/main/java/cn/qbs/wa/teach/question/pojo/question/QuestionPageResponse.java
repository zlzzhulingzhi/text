package cn.qbs.wa.teach.question.pojo.question;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.teach.question.entity.Question;

/**
 * 试题(Question)分页查询试题响应
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 18:17:29
 */
@Data
public class QuestionPageResponse extends Question {

    /** 主体部分 */
    @ApiModelProperty(value = "题干")
    private String content;

    @ApiModelProperty(value = "答案")
    private String answer;

    @ApiModelProperty(value = "答案解析")
    private String answerDesc;

    @ApiModelProperty(value = "选项A")
    private String optionA;

    @ApiModelProperty(value = "选项B")
    private String optionB;

    @ApiModelProperty(value = "选项C")
    private String optionC;

    @ApiModelProperty(value = "选项D")
    private String optionD;

    @ApiModelProperty(value = "视频 url")
    private String videoUrl;

    @ApiModelProperty(value = "音频文件url")
    private String audioUrl;

}

