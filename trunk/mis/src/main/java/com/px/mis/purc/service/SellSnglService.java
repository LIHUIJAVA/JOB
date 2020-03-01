package com.px.mis.purc.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.purc.entity.SellSngl;
import com.px.mis.purc.entity.SellSnglSub;

public interface SellSnglService {

	public String addSellSngl(String userId,SellSngl sellSngl,List<SellSnglSub> sellSnglSubList,String loginTime);
	
	public String editSellSngl(SellSngl sellSngl,List<SellSnglSub> sellSnglSubList);
	
	public String deleteSellSngl(String sellSnglId);
	
	public String querySellSngl(String sellSnglId);
	
	public String querySellSnglList(Map map);
	
	String deleteSellSnglList(String sellSnglId);
	
	Map<String,Object> updateSellSnglIsNtChkList (String userId,SellSngl sellSngl,String loginTime) throws Exception;
	
	String printingSellSnglList(Map map);
	
	/**
	 * ������ϸ��ѯ
	 * @param map
	 * @return
	 */
	String querySellSnglByInvtyEncd(Map map);

	//���빦��
	public String uploadFileAddDb(MultipartFile file,int i);
	
	String updateA(Map map);
	/*����ͳ�Ʊ��ѯ*/
	String selectXSTJList(Map map);
	
	//�˻����������۵�ʱ��ѯ���۵��ӱ���Ϣ
	String selectSellSnglSubByRtnblQty(String sellSnglId);
	
	/*����ʱ��ѯ������Ϣ*/
	String querySellSnglLists(Map map);

	String querySellSnglListOrderBy(Map map);
	
}
