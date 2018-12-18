package com.ehyf.ewashing.wechat.pay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ehyf.ewashing.common.Constants;
public abstract class RechargeRule {
	private static Map<String,List<RechargeRuleMapping>> rules = new HashMap<String,List<RechargeRuleMapping>>();
	private static final String rs = "swy=98:25,198:66,298:128;hyf=500:58,3000:998,1000:258";
	
	static {
		List<RechargeRuleMapping> list = null;
		String [] cg = rs.split(";");
		for(String s : cg){
			String[] s1 = s.split("=");
			if("swy".equals(s1[0])){
				if(null == (list = rules.get(Constants.APPID_SWY))){
					list = new ArrayList<RechargeRuleMapping>();
					rules.put(Constants.APPID_SWY, list);
				}
				for(String s2:s1[1].split(",")){
					String[] s3 = s2.split(":");
					RechargeRuleMapping r = new RechargeRuleMapping();
					r.setSysSource(Constants.APPID_SWY);
					r.setRechargeAmount(new BigDecimal(s3[0]));
					r.setGiveAmount(new BigDecimal(s3[1]));
					list.add(r);
				}
			}else if("hyf".equals(s1[0])){
				if(null == (list = rules.get(Constants.APPID_HYF))){
					list = new ArrayList<RechargeRuleMapping>();
					rules.put(Constants.APPID_HYF, list);
				}
				for(String s2:s1[1].split(",")){
					String[] s3 = s2.split(":");
					RechargeRuleMapping r = new RechargeRuleMapping();
					r.setSysSource(Constants.APPID_HYF);
					r.setRechargeAmount(new BigDecimal(s3[0]));
					r.setGiveAmount(new BigDecimal(s3[1]));
					list.add(r);
				}
			}
		}
	}
	
	public static BigDecimal getRuleAmount(BigDecimal amount,String appId){
		if(null==amount || appId == null) return BigDecimal.ZERO;
		List<RechargeRuleMapping> l = null;
		BigDecimal rs = BigDecimal.ZERO;
		if(null!=(l=rules.get(appId))){
			for(RechargeRuleMapping r :l){
				if(amount.compareTo(r.getRechargeAmount())>=0){
					if(rs==null || rs.compareTo(r.getGiveAmount())<0) rs = r.getGiveAmount();
				}
			}
		}
		return rs;
	}
	
}