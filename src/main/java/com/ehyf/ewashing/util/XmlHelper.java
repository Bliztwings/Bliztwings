package com.ehyf.ewashing.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.io.Resources;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ehyf.ewashing.entity.Menu;


/**
 * 根据节点获取菜单信息
 * @author shenxiaozhong
 *
 */
public class XmlHelper {

	@SuppressWarnings("unchecked")
	public static List<Menu> getElemetByNode(String nodePath,String fileName){
		try {
			List<Menu> mlist =new ArrayList<Menu>();
			SAXReader reader = new SAXReader();
			BufferedReader re = new BufferedReader(
					new InputStreamReader(Resources.getResourceAsStream(fileName), "UTF-8"));
			Document document = reader.read(re);
			if (document == null) {

			}
			Element root = (Element) document.getRootElement();
			List<Element> list = root.selectNodes(nodePath);

			if (!CollectionUtils.isEmpty(list)) {
				for (Element e : list) {
					List<Element> listAttr = e.elements();
					Menu m =new Menu();
					m.setMenuName(listAttr.get(0).getText());
					m.setMenuUrl(listAttr.get(1).getText());
					mlist.add(m);
				}
			}
			return mlist;
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}
}
