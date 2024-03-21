package org.jim.core.packets;

/**
 * <pre>
 **
 * 加入群组申请的结果
 * </pre>
 * enum {@code JoinGroupResult}
 */
public enum OutGroupResult{
  /**
   * <pre>
   * 能够退出
   * </pre>
   *
   * <code>JOIN_GROUP_RESULT_UNKNOWN = 0;</code>
   */
  OUT_GROUP_RESULT_UNKNOWN(0),
  /**
   * <pre>
   * 不能退出
   * </pre>
   *
   * <code>JOIN_GROUP_RESULT_OK = 1;</code>
   */
  OUT_GROUP_RESULT_OK(1);

  public final int getNumber() {
    return value;
  }

  public static OutGroupResult valueOf(int value) {
    return forNumber(value);
  }

  public static OutGroupResult forNumber(int value) {
    switch (value) {
      case 0: return OUT_GROUP_RESULT_UNKNOWN;
      case 1: return OUT_GROUP_RESULT_OK;
      default: return null;
    }
  }

  private final int value;

  private OutGroupResult(int value) {
    this.value = value;
  }
}

