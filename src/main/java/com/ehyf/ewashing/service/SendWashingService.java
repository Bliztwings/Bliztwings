package com.ehyf.ewashing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehyf.ewashing.dao.SendWashingDao;
import com.ehyf.ewashing.vo.ReceptionStatisticsVo;

@Service
public class SendWashingService {

	@Autowired
	private SendWashingDao sendDao;
	public List<ReceptionStatisticsVo> receptionStatistics(ReceptionStatisticsVo receptionStatisticsVo) {
		return sendDao.receptionStatistics(receptionStatisticsVo);
	}
	public List<ReceptionStatisticsVo> statisticalAnalysis(ReceptionStatisticsVo vo) {
		return sendDao.statisticalAnalysis(vo);
	}

}
