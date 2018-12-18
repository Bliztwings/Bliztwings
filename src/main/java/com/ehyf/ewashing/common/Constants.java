package com.ehyf.ewashing.common;

import java.math.BigDecimal;

/**
 * 常量
 * @author jelly
 *
 */
public class Constants {
    public static final String CURRENT_USER = "user";
    public static final String SESSION_FORCE_LOGOUT_KEY = "session.force.logout";
    public static final String IS_DELETED_NO="0";
    public static final String FLAG_ADD="0";
    public static final String FLAG_UPD="1";
    public static final String CARD_SUPPLIER = "萨维亚科技有限公司";
    public static final String INIT_PASSWORD="123456";
    public static final String MEMBER_CARD_PREFIX_MB="MB";
    public static final String CREATE_USER_SYSTEM="system";
    public static final BigDecimal ZERO = BigDecimal.valueOf(0);
	public static final BigDecimal RECHARGE_MAX = BigDecimal.valueOf(10000);
	public static final long CHECKCODE_INVALID_SECOND = 60*3;
	public static final BigDecimal O2O_NEW_AMOUNT = BigDecimal.valueOf(20);
    
	public static final String PICTURE_URL_PREFIX="http://ouanv8ds6.bkt.clouddn.com/ewashing/";
	
    public static final String MEMBER_TYPE_CARD="card";
    public static final String MEMBER_TYPE_NORMAL="normal";
    public static final String CARD_STATUS_NORMAL = "normal";
    public static final String CARD_STATUS_NEW = "new";
    public static final String CARD_STATUS_BACKCARD="back_card";
    public static final String CARD_STATUS_REPORTLOSS="report_loss";
    public static final String CARD_STATUS_FILLCARD="fill_card";
    public static final String CARD_STATUS_CANCEL = "cancel";
    public static final String CARD_OPERATOR_TYPE_GRANT="grant";
    public static final String CARD_OPERATOR_TYPE_REPORTLOSS="report_loss";
    public static final String CARD_OPERATOR_TYPE_UNREPORTLOSS="unreport_loss";
    public static final String CARD_OPERATOR_TYPE_REBACK="reback";
    public static final String CARD_OPERATOR_TYPE_CANCEL = "cancel";
    public static final String CARD_OPERATOR_TYPE_FILL = "fill";
    
    public static final String CARD_OPERATOR_STATUS_APPROVED="approved";
    
    public static final String CARD_FLOWING_OPE_TYPE_RECHARGE="recharge";
    public static final String CARD_FLOWING_OPE_TYPE_REFUND="refund";
    public static final String CARD_FLOWING_OPE_TYPE_CONSUMPTION="consumption";
    
    public static final String CARD_APPLY_STATUS_UNAPPROVED="un_approved";
    public static final String CARD_APPLY_STATUS_APPROVED="approved";
    public static final String CARD_APPLY_STATUS_UNPASS="un_passed";
    public static final String CARD_APPLY_STATUS_WAREHOUSED="warehoused";
    public static final String CARD_APPLY_STATUS_RECEIVED="received";
    
    public static final String MEMBER_CLIENT_TYPE_PC="pc";
    public static final String MEMBER_CLIENT_TYPE_MOBILE="mobile";
    public static final String MEMBER_CLIENT_TYPE_SWY="swy";
    public static final String MEMBER_CLIENT_TYPE_OTHER="other";
    
    public static final String PAYED_METHOD_CASH="cash";
    
    public static final String APPID_SWY = "wx614b63f73b55d7ef";
    public static final String APPID_HYF = "wxc6097b5bf9ba801d";
    
    
    public static final String MSG_TYPE_O2O_REGISTER = "O2O_REGISTER";
    public static final String MSG_TYPE_XE_REGISTER = "XE_REGISTER";
    public static final String MSG_TYPE_RESET_PWD_O2O = "RESET_PWD_O2O";
}
