package cn.qbs.wa.train.allowance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author qcj
 */
@Getter
@AllArgsConstructor
public enum ApplyResultEnum {
  /** 不通过 */
  NO_PASS(0),

  /** 通过 */
  PASS(1),
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
