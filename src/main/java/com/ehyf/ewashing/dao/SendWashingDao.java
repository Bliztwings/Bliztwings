package com.ehyf.ewashing.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.ehyf.ewashing.entity.SendWashing;
import com.ehyf.ewashing.vo.ReceptionStatisticsVo;

@Repository
public interface SendWashingDao extends BaseDao<SendWashing> {

	List<ReceptionStatisticsVo> receptionStatistics(ReceptionStatisticsVo receptionStatisticsVo);

	List<ReceptionStatisticsVo> statisticalAnalysis(ReceptionStatisticsVo vo);

}
