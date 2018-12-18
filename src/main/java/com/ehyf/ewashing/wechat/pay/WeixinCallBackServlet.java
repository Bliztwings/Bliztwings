package com.ehyf.ewashing.wechat.pay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ehyf.ewashing.entity.MemberCard;
import com.ehyf.ewashing.entity.PrepayOrder;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.entity.StoreOrder;
import com.ehyf.ewashing.restful.client.ResultData;
import com.ehyf.ewashing.restful.service.MemberRestfulService;
import com.ehyf.ewashing.service.EwashingStoreBusinessService;
import com.ehyf.ewashing.service.EwashingStoreOrderService;
import com.ehyf.ewashing.service.PrepayOrderService;
import com.ehyf.ewashing.util.LocalThreadUtils;



/**
 * Servlet implementation class WeixinCallBackServlet
 */
@Component("weixinCallBackServlet")
public class WeixinCallBackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	Logger logger =Logger.getLogger(WeixinCallBackServlet.class);

	@Autowired
	private EwashingStoreOrderService  orderService;
	
	@Autowired
	private PrepayOrderService  prepayOrderService;
	
	@Autowired
	private MemberRestfulService memberRestfulService;
	
	@Autowired
	private EwashingStoreBusinessService storeBusiness;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WeixinCallBackServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    SAXReader reader = new SAXReader();
	    JSONObject result = new JSONObject();
        try {
            BufferedReader re = new BufferedReader(new InputStreamReader(request.getInputStream(), "GBK")); 
            Document document = reader.read(re);
            if (document == null) {
                logger.info("xml 为空");
            }
            Element root = document.getRootElement();
            for (Iterator<?> it = root.elementIterator(); it.hasNext();) {
                Element element = (Element) it.next();
                result.put(element.getName(), element.getText());
                System.out.println(element.getName()+":"+element.getText());
            }
            if("SUCCESS".equals(result.getString("return_code")) && "SUCCESS".equals(result.getString("result_code"))){
                
            	JSONObject json =orderService.orderQuery(result.getString("appid"),result.getString("out_trade_no"));
    			String return_code  =json.getString("return_code");
    			
    			if("SUCCESS".equals(return_code)){
    				String result_code   =json.getString("result_code ");
        			if("SUCCESS".equals(result_code)){
        				String trade_state   =json.getString("trade_state ");
        				if("SUCCESS".equals(trade_state)){
							PrepayOrder prepayOrder =prepayOrderService.queryOrderByTradeNo(result.getString("out_trade_no"));
							if(prepayOrder==null){
								return;
							}
							doBusiness(prepayOrder,response,result);
        				}
        			}
    			}
            }
            else{
            	responseXml("FAIL", "支付失败，验证订单失败", response);
            }
        }catch(Exception e){
            logger.info(e.getMessage());
        }
	}

	private synchronized void doBusiness(PrepayOrder prepayOrder,HttpServletResponse response,JSONObject result) throws Exception {
		if("1".equals(prepayOrder.getType())){
			StoreOrder orderInfo =orderService.getById(prepayOrder.getOrderId());
			if("1".equals(orderInfo.getPayStatus())){
				responseXml("SUCCESS", null, response);
				return;
			}
			if("2".equals(prepayOrder.getStatus())){
				responseXml("FAIL", "预付订单，已经支付", response);
				return;
			}
			int total_fee =result.getInt("total_fee");
			BigDecimal receiveableAmount =orderInfo.getReceivableAmount();
			receiveableAmount=(receiveableAmount.setScale(2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
			if(total_fee!=receiveableAmount.intValue()){
				responseXml("FAIL", "支付失败,支付金额与本地金额不一致", response);
                return;
			}
			SecurityUser loginUser =new SecurityUser();
			loginUser.setId(prepayOrder.getMemberId());
			loginUser.setUsername(orderInfo.getMemberName());
			boolean flag =storeBusiness.o2oPayment(prepayOrder.getOrderId(), orderInfo.getClothesAmount(), orderInfo.getFreight(), prepayOrder.getMemberId(), "3", orderInfo.getMobilePhone(), loginUser);
			if (!flag) {
				responseXml("FAIL", "支付失败", response);
			} else {
				PrepayOrder po = new PrepayOrder();
				po.setId(prepayOrder.getId());
				po.setStatus("2");
				prepayOrderService.update(po);
				responseXml("SUCCESS", "支付成功", response);
				logger.info("用户:"+prepayOrder.getMemberId()+"，订单号："+prepayOrder.getOrderCode()+"，支付金额："+prepayOrder.getOrderAmount()+",付款成功");
			}
			
		}
		
		if("2".equals(prepayOrder.getType())){
			
			logger.info("正在给用户:"+prepayOrder.getMemberId()+"卡ID:"+prepayOrder.getMemberCardId()+"，充值，充值金额："+prepayOrder.getOrderAmount());
			int total_fee =result.getInt("total_fee");
			BigDecimal receiveableAmount =prepayOrder.getOrderAmount();
			receiveableAmount=(receiveableAmount.setScale(2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
			if(total_fee!=receiveableAmount.intValue()){
				responseXml("FAIL", "充值失败,充值金额与本地金额不一致", response);
                return;
			}
						
			MemberCard mc =new MemberCard();
			mc.setId(prepayOrder.getMemberCardId());
			mc.setCashAmount(prepayOrder.getOrderAmount());
			mc.setGivedAmount(RechargeRule.getRuleAmount(mc.getCashAmount(), result.getString("appid")));
			ResultData<Object>  res =memberRestfulService.onlineRecharge(mc);
			
			if ("9999".equals(res.getResCode())) {
				responseXml("FAIL", "充值失败", response);
			} else {

				PrepayOrder po = new PrepayOrder();
				po.setId(prepayOrder.getId());
				po.setStatus("2");
				prepayOrderService.update(po);
				
				responseXml("SUCCESS", "支付成功", response);
			}
			
		}
	}

	private void responseXml(String flag,String message,HttpServletResponse response){
	    Document xml = DocumentHelper.createDocument();
        Element catalogElement = xml.addElement("xml");
        
        Element retcode = catalogElement.addElement("return_code");
        retcode.addCDATA(flag);
        
        Element reason = catalogElement.addElement("return_msg");
        reason.addCDATA(message);
        
        try {
            response.getOutputStream().write(xml.asXML().getBytes());
        }
        catch (IOException e) {
        }
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    SAXReader reader = new SAXReader();
	    JSONObject result = new JSONObject();
        try {
            BufferedReader re = new BufferedReader(new InputStreamReader(request.getInputStream(), "GBK")); 
            Document document = reader.read(re);
            if (document == null) {
                logger.info("xml 为空");
            }
            Element root = document.getRootElement();
            for (Iterator<?> it = root.elementIterator(); it.hasNext();) {
                Element element = (Element) it.next();
                result.put(element.getName(), element.getText());
                System.out.println(element.getName()+":"+element.getText());
            }
			if ("SUCCESS".equals(result.getString("return_code")) && "SUCCESS".equals(result.getString("result_code"))) {
				JSONObject json = orderService.orderQuery(result.getString("appid"),result.getString("out_trade_no"));
				String return_code = json.getString("return_code");

				if ("SUCCESS".equals(return_code)) {
					String result_code = json.getString("result_code");
					if ("SUCCESS".equals(result_code)) {
						String trade_state = json.getString("trade_state");
						if ("SUCCESS".equals(trade_state)) {
							PrepayOrder prepayOrder =prepayOrderService.queryOrderByTradeNo(result.getString("out_trade_no"));
							if(prepayOrder==null){
								return;
							}
							doBusiness(prepayOrder,response,result);
						}
					}
				}
				else{
					responseXml("FAIL", "支付失败，验证订单失败", response);
				}
			}
            
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

}
