package com.ehyf.ewashing.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ehyf.ewashing.dao.ClothesAttachDao;
import com.ehyf.ewashing.dao.ClothesPhotoDao;
import com.ehyf.ewashing.dao.EwashingStoreClothesDao;
import com.ehyf.ewashing.emun.ClothesStatus;
import com.ehyf.ewashing.emun.PayStatus;
import com.ehyf.ewashing.emun.ServiceType;
import com.ehyf.ewashing.emun.Urgency;
import com.ehyf.ewashing.entity.ClothesAttach;
import com.ehyf.ewashing.entity.ClothesPhoto;
import com.ehyf.ewashing.entity.EwashingDataDictionary;
import com.ehyf.ewashing.entity.StoreClothes;
import com.ehyf.ewashing.vo.FactoryStatisticsVo;

@Service
public class EwashingStoreClothesService extends BaseService<EwashingStoreClothesDao,StoreClothes> {

	@Autowired
	private EwashingStoreClothesDao  clothesDao;
	
	@Autowired
	private ClothesAttachDao clothesAttachDao;
	
	@Autowired
	private ClothesPhotoDao mClothesPhotoDao;
	
	@Autowired
	private EwashingDataDictionaryService dicService;
	
	public List<StoreClothes> clothesStatistics(FactoryStatisticsVo fsVo) {
		
		List<StoreClothes> list = clothesDao.clothesStatistics(fsVo);
		convertList(list);
		return list;
	}
	
	private void convertList(List<StoreClothes> list) {

		if (!CollectionUtils.isEmpty(list)) {

			for (StoreClothes clothes : list) {

				// 获取衣服照片信息
				ClothesPhoto photo =new ClothesPhoto();
				photo.setClothesId(clothes.getId());
				List<ClothesPhoto> photoList =mClothesPhotoDao.findList(photo);
				if(!CollectionUtils.isEmpty(photoList)){
					clothes.setHasPhoto("1");
					clothes.setPhotoList(photoList);
				}
				clothes.setServiceFee(clothes.getServiceFee()== null ? BigDecimal.ZERO :clothes.getServiceFee());
				clothes.setStatus(ClothesStatus.getName(clothes.getStatus()));
				clothes.setServiceType(ServiceType.getName(clothes.getServiceType()));
				clothes.setUrgency(Urgency.getName(clothes.getUrgency()));
				clothes.setPayStatus(PayStatus.getName(clothes.getPayStatus()));
				if (clothes.getId() != null && !"".equals(clothes.getId())) {
					// List<EwashingDataDictionary> dataList =
					// dicService.queryTextNameByIds(clothes.getAttachment().split(","));

					// 根据clothesId 获取对应的附件信息
					ClothesAttach attach = new ClothesAttach();
					attach.setClothesId(clothes.getId());
					List<ClothesAttach> dataList = clothesAttachDao.findList(attach);

					StringBuffer sb = new StringBuffer();
					if (!CollectionUtils.isEmpty(dataList)) {

						for (int i = 0; i < dataList.size(); i++) {
							if (i > 0) {
								sb.append(",");
								sb.append(dataList.get(i).getAttachName());
							} else {
								sb.append(dataList.get(i).getAttachName());
							}
						}
					}
					clothes.setAttachment(sb.toString());
				}

				if (clothes.getFlaw() != null && !"".equals(clothes.getFlaw())) {
					List<EwashingDataDictionary> dataList = dicService.queryTextNameByIds(clothes.getFlaw().split(","));
					StringBuffer sb = new StringBuffer();
					if (!CollectionUtils.isEmpty(dataList)) {

						for (int i = 0; i < dataList.size(); i++) {
							if (i > 0) {
								sb.append(",");
								sb.append(dataList.get(i).getDataName());
							} else {
								sb.append(dataList.get(i).getDataName());
							}
						}
					}
					clothes.setFlaw(sb.toString());
				}
			}

		}

	}
}
