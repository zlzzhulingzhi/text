package cn.qbs.wa.teach.exam.common.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 考试规则枚举
 *
 * @Author zcm
 * @Date 2021-12-15 09:33
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum RuleEnum {

    // type 规则类型 1-防作弊规则 2-试题规则 3-考试规则 4-查看试卷作答情况

    // 防作弊规则
    CUT_SCREEN_PROMPT("cut_screen_prompt", "切屏提示", 1, "anti_cheating", "checkbox"),
    CAMERA_RECORDING("camera_recording", "摄像头录像", 1, "anti_cheating", "checkbox"),
    NO_COPY_CUT_PASTE("no_copy_cut_paste", "禁止复制、剪切、粘贴", 1, "anti_cheating", "checkbox"),


    SHUFFLE_QUESTION("shuffle_question", "打乱试题顺序", 2, "shuffle_question", "radio"),


    SHOW_ANSWER_DESC("show_answer_desc", "显示答案解析", 3, "show_answer_desc", "radio"),
    SUPPORT_UPLOAD_ATTACHMENT("support_upload_attachment", "支持上传附件", 3, "support_upload_attachment", "radio"),


    // 查看试卷作答情况
    ALLOW_VIEWING_AFTER_EXAM("allow_viewing_after_exam", "考试结束后允许查看", 4, "view_paper_answers", "radio"),
    ALLOW_VIEWING_AFTER_SUBMIT_PAPER("allow_viewing_after_submit_paper", "交卷后允许查看", 4, "view_paper_answers", "radio"),

    ;


    private final String code;

    private final String ruleName;

    private final int type;

    private final String groupCode;

    /**
     * UI类型 [radio, checkbox]
     */
    private final String uiType;


    /**
     * 根据文本值获取对应的枚举值
     */
    public static RuleEnum fromName(String text) {
        RuleEnum[] values = RuleEnum.values();
        for (RuleEnum value : values) {
            if (value.getRuleName().equals(text)) {
                return value;
            }
        }

        return null;
    }

    public static RuleEnum fromCode(String code) {
        RuleEnum[] values = RuleEnum.values();
        for (RuleEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }

        return null;
    }

    public static List<Map<String, Object>> list(int type) {
        return Arrays.stream(values())
                .filter(item -> item.getType() == type)
                .map(item -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", item.getType());
                    map.put("groupCode", item.getGroupCode());
                    map.put("ruleName", item.getRuleName());
                    map.put("code", item.getCode());
                    map.put("uiType", item.getUiType());
                    return map;
                }).collect(Collectors.toList());
    }

}
