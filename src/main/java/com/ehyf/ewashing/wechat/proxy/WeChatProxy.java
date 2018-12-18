package com.ehyf.ewashing.wechat.proxy;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.ehyf.ewashing.util.HttpAccessUtil;
import com.ehyf.ewashing.wechat.model.AccessToken;
import com.ehyf.ewashing.wechat.model.ErrorMsgManager;
import com.ehyf.ewashing.wechat.model.JsapiTicket;
import com.ehyf.ewashing.wechat.model.MsgResult;
import com.ehyf.ewashing.wechat.model.NewsResult;
import com.ehyf.ewashing.wechat.model.Result;
import com.ehyf.ewashing.wechat.model.SubscribeUsers;
import com.ehyf.ewashing.wechat.model.TemplateMsg;
import com.ehyf.ewashing.wechat.model.TemplateMsgResult;
import com.ehyf.ewashing.wechat.model.Ticket;
import com.ehyf.ewashing.wechat.model.UserInfo;
import com.ehyf.ewashing.wechat.model.UserInfoByCode;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 微信公众号代理
 * @author shenxiaozhong
 *
 */
public class WeChatProxy {

	private static Logger log = Logger.getLogger(WeChatProxy.class);

	/**
	 * 
	 * @description 获取微信公众号token信息
	 * @author shenxiaozhong
	 * @param grant_type
	 *            获取access_token填写client_credential
	 * @param appid
	 *            微信公众号
	 * @param secret
	 *            微信公众号密码
	 * @version 创建时间：2014年8月14日 上午10:03:36
	 */
	public static AccessToken getAccessToken(final String grant_type, final String appid, final String secret) {
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=" + grant_type + "&appid=" + appid + "&secret="
				+ secret;
		String result = HttpAccessUtil.httpGetSSL(url);
		AccessToken accessToken = JSON.parseObject(result, AccessToken.class);
		if (accessToken == null || accessToken.getAccess_token() == null) {
			recordErrorMsg(result, "getAccessToken");
			return null;
		}
		return accessToken;
	}

	
	public static SubscribeUsers getSubscribeUser(final String access_token, final String nextOpenid) {
		String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + access_token + "&next_openid="
				+ nextOpenid;
		String result = HttpAccessUtil.httpGetSSL(url);
		SubscribeUsers subscribeUsers = JSON.parseObject(result, SubscribeUsers.class);

		if (subscribeUsers == null || subscribeUsers.getData() == null) {
			recordErrorMsg(result, "getSubscribeUser");
			return null;
		}

		return subscribeUsers;
	}

	
	public static boolean setUserGroup(final String access_token, final String openId, final String groupId) {
		String url = "https://api.weixin.qq.com/cgi-bin/groups/members/update";
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("access_token", access_token);
		paramMap.put("openid", openId);
		paramMap.put("to_groupid", groupId);
		net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(paramMap);
		String result = HttpAccessUtil.httpPostSSL(url, jsonObject.toString());
		Result resultBean = JSON.parseObject(result, Result.class);
		if (resultBean == null) {
			return false;
		} else {
			if ("0".equals(resultBean.getErrcode())) {
				return true;
			} else {
				log.error("setUserGroup 设置用户分组失败：错误代码:" + resultBean.getErrcode() + " 错误信息：" + resultBean.getErrmsg());
				return false;
			}
		}

	}

	public static Result sendCSMessage(final String access_token, final String json) {
		String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + access_token;
		String result = HttpAccessUtil.httpPostSSL(url, json);
		Result resultBean = JSON.parseObject(result, Result.class);
		if (resultBean == null) {
			resultBean = new Result();
			resultBean.setErrcode(-1);
			resultBean.setErrmsg(resultBean.getErrmsg());
			return resultBean;
		} else {
			if (resultBean.getErrcode() == 0) {
				return resultBean;
			} else {
				log.error("sendCSMessage 发送信息失败：错误代码:" + resultBean.getErrcode() + " 错误信息："
						+ ErrorMsgManager.getErrorMsg(resultBean.getErrcode()));
				return resultBean;
			}
		}
	}

	public static Ticket createQrcode(final String access_token, final String json) {
		String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + access_token;
		String result = HttpAccessUtil.httpPostSSL(url, json);
		Ticket ticker = JSON.parseObject(result, Ticket.class);
		if (ticker == null || ticker.getTicket() == null) {
			recordErrorMsg(result, "getTicket");
			return null;
		}
		return ticker;
	}

	public static JsapiTicket getTicket(final String access_token) {
		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + access_token + "&type=jsapi";
		String result = HttpAccessUtil.httpGetSSL(url);
		JsapiTicket ticker = JSON.parseObject(result, JsapiTicket.class);
		if (ticker == null || ticker.getTicket() == null) {
			recordErrorMsg(result, "getTicket");
			return null;
		}
		return ticker;
	}

	/**
	 * 获取用户信息
	 * @param access_token 微信token
	 * @param openid 唯一openid
	 * @return
	 */
	public static UserInfo getUserInfo(final String access_token, final String openid) {
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + access_token + "&openid=" + openid
				+ "&lang=zh_CN";
		String result = HttpAccessUtil.httpGetSSL(url);
		UserInfo userInfo = JSON.parseObject(result, UserInfo.class);
		if (userInfo == null || userInfo.getOpenid() == null) {
			recordErrorMsg(result, "getUserInfo");
			return null;
		}
		return userInfo;
	}

	public static MsgResult sendBoradcastMsg(final String access_token, final String json) {
		String url = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=" + access_token;
		String result = HttpAccessUtil.httpPostSSL(url, json);
		MsgResult msgResultBean = JSON.parseObject(result, MsgResult.class);
		if (msgResultBean == null || msgResultBean.getMsg_id() == null) {
			recordErrorMsg(result, "sendBoradcastMsg");
			return null;
		}
		return msgResultBean;
	}

	public static NewsResult uploadNews(final String access_token, final String json) {
		String url = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=" + access_token;
		String result = HttpAccessUtil.httpPostSSL(url, json);
		NewsResult newsResultBean = JSON.parseObject(result, NewsResult.class);
		if (newsResultBean == null || newsResultBean.getMedia_id() == null) {
			recordErrorMsg(result, "uploadNews");
			return null;
		}
		return newsResultBean;

	}

	public static byte[] getQRCode(final String ticker) {
		String url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticker;
		return HttpAccessUtil.httpGetByte(url);
	}

	/**
	 * 发送模板消息接口 2016/09/08 shenxiaozhong
	 * 
	 * @param access_token
	 *            微信授权token
	 * @param templateMsg
	 *            模版消息
	 * @return
	 */
	public static TemplateMsgResult sendTemplateMsg(final String access_token, TemplateMsg templateMsg) {
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + access_token;
		String result = HttpAccessUtil.httpPostSSL(url, JSON.toJSONString(templateMsg));
		TemplateMsgResult resultModel = JSON.parseObject(result, TemplateMsgResult.class);
		return resultModel;
	}

	/**
	 * 创建自定义菜单
	 * @param access_token
	 * @param menuJson
	 * @return
	 */
	public static TemplateMsgResult creatMenu(final String access_token, String menuJson) {
		String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+ access_token;
		String result = HttpAccessUtil.httpPostSSL(url, menuJson);
		TemplateMsgResult resultModel = JSON.parseObject(result, TemplateMsgResult.class);
		return resultModel;
	}
	
	/**
	 * 删除自定义菜单
	 * @param access_token
	 * @param menuJson
	 * @return
	 */
	public static TemplateMsgResult deleteMenu(final String access_token) {
		String url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=" + access_token;
		String result = HttpAccessUtil.httpPostSSL(url, "");
		TemplateMsgResult resultModel = JSON.parseObject(result, TemplateMsgResult.class);
		return resultModel;
	}
	
	
	@SuppressWarnings("unused")
	private static void recordErrorMsg(String result, String methodName) {
		Result resultBean = JSON.parseObject(result, Result.class);
		if (resultBean == null) {
			log.info(methodName + " 返回结果为空,请确认当前网络环境能否访问微信API");
		} else {
			log.info(methodName + " 返回代码：" + resultBean.getErrcode() + "  返回信息:"
					+ ErrorMsgManager.getErrorMsg(resultBean.getErrcode()));
		}

	}

	public static UserInfoByCode getUserInfoByCode(final String appid, final String secret, final String code) {
		UserInfoByCode resultModel = null;
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
		url = String.format(url, appid, secret, code);
		log.info("getUserInfoByCode的url为" + url);
		String result = HttpAccessUtil.httpGetSSL(url);
		log.info("getUserInfoByCode的result为" + result);
		try {
			resultModel = JSON.parseObject(result, UserInfoByCode.class);
		} catch (Exception e) {
			log.error(result);
		}
		return resultModel;
	}

	public static UserInfo getUserInfoByCode(final String access_token, final String openid) {
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid
				+ "&lang=zh_CN";
		String result = HttpAccessUtil.httpGetSSL(url);
		UserInfo userInfo = JSON.parseObject(result, UserInfo.class);
		if (userInfo == null || userInfo.getOpenid() == null) {
			recordErrorMsg(result, "getUserInfo");
			return null;
		}
		return userInfo;
	}

	/**
     * 根据appid 判断类型，1：萨维亚 2：浣衣坊
     * @param appId
     * @return
     */
    public static int judgeAppIdType(String appId){
        if("wxc6097b5bf9ba801d".equals(appId)){
            return 2;
        }
        if("wx614b63f73b55d7ef".equals(appId)){
            return 1;
        }
        return 0;
    }
	
	public static void main(String[] args) {
		
		
		createSwy();
		createhyf();
		/*AccessToken token =getAccessToken("client_credential", "wxc6097b5bf9ba801d", "be4d1a0381ac606745a4cbe119dc8f7a");

		//AccessToken token =getAccessToken("client_credential", "wx614b63f73b55d7ef", "caa0df7f27bfbcf17e102123cddb849a");
		System.out.println(token.getAccess_token());
		
		//http://www.sawyxd.com/hyfnode/wx/wx_login
		//xiyinode
		JSONArray menuArray =new JSONArray();
		
		JSONObject button1 =new JSONObject();
		button1.put("type", "view");
		button1.put("name", "我要洗衣");
		button1.put("key", "ewashing_order");
		button1.put("url", "http://www.sawyxd.com/hyfnode/wx/wx_login");
		
		JSONObject button2 =new JSONObject();
		button2.put("type", "click");
		button2.put("name", "会员中心");
		button2.put("key", "member_center");
		
		JSONArray subMenuList =new JSONArray();
		JSONObject button2_1 =new JSONObject();
		button2_1.put("type", "view");
		button2_1.put("name", "APP下载");
		button2_1.put("key", "app_download");
		button2_1.put("url", "https://www.pgyer.com/5wQB");
		
		JSONObject button2_2 =new JSONObject();
		button2_2.put("type", "view");
		button2_2.put("name", "小浣招募");
		button2_2.put("key", "e_manager");
		button2_2.put("url", "http://www.sawyxd.com/hyfnode/zhaomu");
		
		JSONObject button2_3 =new JSONObject();
		button2_3.put("type", "view");
		button2_3.put("name", "个人中心");
		button2_3.put("key", "person_center");
		button2_3.put("url", "http://www.sawyxd.com/hyfnode/wx/wx_user_login");
		
		JSONObject button2_4 =new JSONObject();
		button2_4.put("type", "view");
		button2_4.put("name", "我的订单");
		button2_4.put("key", "my_order");
		button2_4.put("url", "http://www.sawyxd.com/hyfnode/wx/wx_order_login");
		
		subMenuList.add(button2_1);
		subMenuList.add(button2_2);
		subMenuList.add(button2_3);
		subMenuList.add(button2_4);
		
		button2.put("sub_button", subMenuList);
		
		
		JSONObject button3 =new JSONObject();
		button3.put("type", "click");
		button3.put("name", "在线客服");
		button3.put("key", "online_service");
		
		JSONArray subMenuList3 =new JSONArray();
		JSONObject button3_1 =new JSONObject();
		button3_1.put("type", "view");
		button3_1.put("name", "我要吐槽");
		button3_1.put("key", "we_tucao");
		button3_1.put("url", "https://www.pgyer.com/5wQB");
		
		JSONObject button3_2 =new JSONObject();
		button3_2.put("type", "view");
		button3_2.put("name", "联系客服");
		button3_2.put("key", "connect_kefu");
		button3_2.put("url", "https://www.pgyer.com/5wQB");
		
		
		subMenuList3.add(button3_1);
		subMenuList3.add(button3_2);
		
		button3.put("sub_button", subMenuList3);
		
		
		menuArray.add(button1);
		menuArray.add(button2);
		menuArray.add(button3);
		
		JSONObject o =new JSONObject();
		o.put("button", menuArray);
		
		
		
		TemplateMsgResult result =creatMenu(token.getAccess_token(), o.toString());
		System.out.println(result.getErrcode()+","+result.getErrmsg());*/
		
		//SubscribeUsers result =getSubscribeUser(token.getAccess_token(), "");
		//System.out.println(result.getData().getOpenid().size());
		
		/*TemplateMsgResult result =deleteMenu(token.getAccess_token());
		System.out.println(result.getErrcode()+","+result.getErrmsg());*/
		
		/*SubscribeUsers users =getSubscribeUser(token.getAccess_token(), "");
		System.out.println(users.getCount());*/
		
		/*UserInfo user =getUserInfo(token.getAccess_token(), "obS7YvyD27MQj-ZfwPzSaEqsV0hc");
		System.out.println(user.getNickname());*/
		/*TemplateMsg message =new TemplateMsg();
		message.setTemplate_id("KB2gW3XZcdepu7pmgGruDWgcuMqnBOGEXjnEk6hysPY");
		message.setUrl("www.baidu.com");
		message.setTouser("obS7YvyD27MQj-ZfwPzSaEqsV0hc");
		
		Map<String, TemplateMsgData> data =new HashMap<String, TemplateMsgData>();
		TemplateMsgData data1 =new TemplateMsgData();
		data1.setValue("121212");
		data.put("keyword1", data1);
		
		TemplateMsgData data2 =new TemplateMsgData();
		data2.setValue("121212");
		data.put("keyword2", data2);
		
		TemplateMsgData data3 =new TemplateMsgData();
		data3.setValue("121212");
		data.put("keyword3", data3);
		
		
		TemplateMsgData remark =new TemplateMsgData();
		remark.setValue("121212");
		data.put("remark", remark);
		
		message.setData(data);
		sendTemplateMsg(token.getAccess_token(), message);*/

	}

	private static void createhyf() {
		// TODO Auto-generated method stub
		AccessToken token =getAccessToken("client_credential", "wxc6097b5bf9ba801d", "be4d1a0381ac606745a4cbe119dc8f7a");
		System.out.println(token.getAccess_token());
		
		//http://www.sawyxd.com/hyfnode/wx/wx_login
		//xiyinode
		JSONArray menuArray =new JSONArray();
		
		JSONObject button1 =new JSONObject();
		button1.put("type", "view");
		button1.put("name", "我要洗衣");
		button1.put("key", "ewashing_order");
		button1.put("url", "http://www.sawyxd.com/hyfnode/wx/wx_login");
		
		JSONObject button2 =new JSONObject();
		button2.put("type", "click");
		button2.put("name", "会员中心");
		button2.put("key", "member_center");
		
		JSONArray subMenuList =new JSONArray();
		JSONObject button2_1 =new JSONObject();
		button2_1.put("type", "view");
		button2_1.put("name", "APP下载");
		button2_1.put("key", "app_download");
		button2_1.put("url", "https://www.pgyer.com/RC2Y");
		
		JSONObject button2_2 =new JSONObject();
		button2_2.put("type", "view");
		button2_2.put("name", "小浣招募");
		button2_2.put("key", "e_manager");
		button2_2.put("url", "http://www.sawyxd.com/hyfnode/zhaomu");
		
		JSONObject button2_3 =new JSONObject();
		button2_3.put("type", "view");
		button2_3.put("name", "个人中心");
		button2_3.put("key", "person_center");
		button2_3.put("url", "http://www.sawyxd.com/hyfnode/wx/wx_user_login");
		
		JSONObject button2_4 =new JSONObject();
		button2_4.put("type", "view");
		button2_4.put("name", "我的订单");
		button2_4.put("key", "my_order");
		button2_4.put("url", "http://www.sawyxd.com/hyfnode/wx/wx_order_login");
		
		subMenuList.add(button2_1);
		subMenuList.add(button2_2);
		subMenuList.add(button2_3);
		subMenuList.add(button2_4);
		
		button2.put("sub_button", subMenuList);
		
		
		JSONObject button3 =new JSONObject();
		button3.put("type", "click");
		button3.put("name", "在线客服");
		button3.put("key", "online_service");
		
		JSONArray subMenuList3 =new JSONArray();
		JSONObject button3_1 =new JSONObject();
		button3_1.put("type", "view");
		button3_1.put("name", "我要吐槽");
		button3_1.put("key", "we_tucao");
		button3_1.put("url", "https://www.pgyer.com/RC2Y");
		
		JSONObject button3_2 =new JSONObject();
		button3_2.put("type", "view");
		button3_2.put("name", "联系客服");
		button3_2.put("key", "connect_kefu");
		button3_2.put("url", "https://www.pgyer.com/RC2Y");
		
		
		subMenuList3.add(button3_1);
		subMenuList3.add(button3_2);
		
		button3.put("sub_button", subMenuList3);
		
		
		menuArray.add(button1);
		menuArray.add(button2);
		menuArray.add(button3);
		
		JSONObject o =new JSONObject();
		o.put("button", menuArray);
		
		
		
		TemplateMsgResult result =creatMenu(token.getAccess_token(), o.toString());
		System.out.println(result.getErrcode()+","+result.getErrmsg());
	}

	private static void createSwy() {

		AccessToken token =getAccessToken("client_credential", "wx614b63f73b55d7ef", "caa0df7f27bfbcf17e102123cddb849a");
		System.out.println(token.getAccess_token());
		
		//http://www.sawyxd.com/hyfnode/wx/wx_login
		//xiyinode
		JSONArray menuArray =new JSONArray();
		
		JSONObject button1 =new JSONObject();
		button1.put("type", "view");
		button1.put("name", "我要洗衣");
		button1.put("key", "ewashing_order");
		button1.put("url", "http://www.sawyxd.com/xiyinode/wx/wx_login");
		
		JSONObject button2 =new JSONObject();
		button2.put("type", "click");
		button2.put("name", "会员中心");
		button2.put("key", "member_center");
		
		JSONArray subMenuList =new JSONArray();
		JSONObject button2_1 =new JSONObject();
		button2_1.put("type", "view");
		button2_1.put("name", "APP下载");
		button2_1.put("key", "app_download");
		button2_1.put("url", "https://www.pgyer.com/XoYL");
		
		JSONObject button2_2 =new JSONObject();
		button2_2.put("type", "view");
		button2_2.put("name", "小萨招募");
		button2_2.put("key", "e_manager");
		button2_2.put("url", "http://www.sawyxd.com/xiyinode/zhaomu");
		
		JSONObject button2_3 =new JSONObject();
		button2_3.put("type", "view");
		button2_3.put("name", "个人中心");
		button2_3.put("key", "person_center");
		button2_3.put("url", "http://www.sawyxd.com/xiyinode/wx/wx_user_login");
		
		JSONObject button2_4 =new JSONObject();
		button2_4.put("type", "view");
		button2_4.put("name", "我的订单");
		button2_4.put("key", "my_order");
		button2_4.put("url", "http://www.sawyxd.com/xiyinode/wx/wx_order_login");
		
		subMenuList.add(button2_1);
		subMenuList.add(button2_2);
		subMenuList.add(button2_3);
		subMenuList.add(button2_4);
		
		button2.put("sub_button", subMenuList);
		
		
		JSONObject button3 =new JSONObject();
		button3.put("type", "click");
		button3.put("name", "在线客服");
		button3.put("key", "online_service");
		
		JSONArray subMenuList3 =new JSONArray();
		JSONObject button3_1 =new JSONObject();
		button3_1.put("type", "view");
		button3_1.put("name", "我要吐槽");
		button3_1.put("key", "we_tucao");
		button3_1.put("url", "https://www.pgyer.com/XoYL");
		
		JSONObject button3_2 =new JSONObject();
		button3_2.put("type", "view");
		button3_2.put("name", "联系客服");
		button3_2.put("key", "connect_kefu");
		button3_2.put("url", "https://www.pgyer.com/XoYL");
		
		
		subMenuList3.add(button3_1);
		subMenuList3.add(button3_2);
		
		button3.put("sub_button", subMenuList3);
		
		
		menuArray.add(button1);
		menuArray.add(button2);
		menuArray.add(button3);
		
		JSONObject o =new JSONObject();
		o.put("button", menuArray);
		
		TemplateMsgResult result =creatMenu(token.getAccess_token(), o.toString());
		System.out.println(result.getErrcode()+","+result.getErrmsg());
	}
}
