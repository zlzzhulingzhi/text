package cn.qbs.wa.train.allowance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author qcj
 */
@Getter
@AllArgsConstructor
public enum ApplyEndedEnum {

  /** 未结束 */
  NOT_OVER(0),

  /** 结束 */
  OVER(1),
  ;

  private final Integer code;

  public static FlowCodeEnum getEnumByCode(Integer code) {
    if (code == null) {
      return null;
    }
    for (FlowCodeEnum flowCodeEnum : FlowCodeEnum.values()) {
      if (flowCodeEnum.getCode().equals(code)) {
        return flowCodeEnum;
      }
    }
    return null;
  }
}
