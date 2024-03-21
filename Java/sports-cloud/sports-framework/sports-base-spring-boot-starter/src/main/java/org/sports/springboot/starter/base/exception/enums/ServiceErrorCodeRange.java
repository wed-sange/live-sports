package org.sports.springboot.starter.base.exception.enums;

import org.sports.springboot.starter.base.exception.ErrorCode;

/**
 * 业务异常的错误码区间，解决：解决各模块错误码定义，避免重复，在此只声明不做实际使用
 *
 * 一共 10 位，分成四段
 *
 * 第一段，1 位，类型
 *      1 - 业务级别异常
 *      x - 预留
 * 第二段，3 位，系统类型
 *      001 - 用户系统
 *      002 - 商品系统
 *      003 - 订单系统
 *      004 - 支付系统
 *      005 - 优惠劵系统
 *      ... - ...
 * 第三段，3 位，模块
 *      不限制规则。
 *      一般建议，每个系统里面，可能有多个模块，可以再去做分段。以用户系统为例子：
 *          001 - OAuth2 模块
 *          002 - User 模块
 *          003 - MobileCode 模块
 * 第四段，3 位，错误码
 *       不限制规则。
 *       一般建议，每个模块自增。
 */
public interface ServiceErrorCodeRange {
    ErrorCode ACCOUNT_DISABLED = new ErrorCode(1001001001, "账号被禁用,请联系管理员");
    ErrorCode ACCOUNT_CANCELD = new ErrorCode(1001001002, "账号已注销");
    ErrorCode ACCOUNT_PASSWD_FAIL = new ErrorCode(1001001003, "账号密码有误");
    ErrorCode PASSWD_FAIL = new ErrorCode(1001001004, "密码有误");
    ErrorCode VERIFICATION_INCORRECT = new ErrorCode(1001001010, "验证码有误");
    ErrorCode VERIFICATION_EXPIRE = new ErrorCode(1001001011, "验证码已过期");
    ErrorCode VERIFICATION_TEL_ERROE = new ErrorCode(1001001012, "手机号格式有误");
    ErrorCode VERIFICATION_EMAIL_ERROR = new ErrorCode(1001001013, "邮箱格式错误");
    ErrorCode MONTH_LIMIT_ONE = new ErrorCode(1001002001, "每月只能修改一次");

    ErrorCode HAS_SAME_NICKNAME = new ErrorCode(1001002002, "昵称重复");

    // 模块 system 错误码区间 [1-000-001-000 ~ 1-000-002-000]

}
