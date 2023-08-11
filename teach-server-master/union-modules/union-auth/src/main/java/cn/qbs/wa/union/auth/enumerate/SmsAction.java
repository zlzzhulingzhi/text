package cn.qbs.wa.union.auth.enumerate;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SmsAction {

    BIND_PHONE(0, "bindPhone"),

    FORGET_PWD(5, "forgetPwd"),

    Login(1, "login"),

    REGISTER(2, "register"),

    CHANGE_PHONE(3, "changePhone");




    @JsonValue
    private final Integer id;

    private final String text;


    public static SmsAction getLoginType(Integer id) {
        SmsAction[] values = SmsAction.values();
        for (SmsAction value : values) {
            if (value.getId().equals(id)) {
                return value;
            }
        }
        return null;
    }
}
