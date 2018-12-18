package com.ehyf.ewashing.restful.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehyf.ewashing.entity.SendReceivePerson;
import com.ehyf.ewashing.service.SendReceivePersonService;

@Controller
@RequestMapping("/e")
public class SendReceivePersonRestful {
	@Autowired
	SendReceivePersonService sendReceivePersonService;
	
	@RequestMapping(method = RequestMethod.POST,consumes = "application/json", value = "/login")
	@ResponseBody
	public ResultData<Object> login(@RequestBody SendReceivePerson e) {
		ResultData<Object> ret = sendReceivePersonService.login(e);
		return ret;
	}

}
