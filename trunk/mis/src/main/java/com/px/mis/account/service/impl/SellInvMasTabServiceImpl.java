package com.px.mis.account.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.SellInvMasTabDao;
import com.px.mis.account.dao.SellInvSubTabDao;
import com.px.mis.account.entity.SellComnInv;
import com.px.mis.account.entity.SellInvMasTab;
import com.px.mis.account.entity.SellInvSubTab;
import com.px.mis.account.service.SellInvMasTabService;
import com.px.mis.account.service.SellInvSubTabService;
import com.px.mis.purc.dao.CustDocDao;
import com.px.mis.purc.dao.EntrsAgnStlDao;
import com.px.mis.purc.dao.EntrsAgnStlSubDao;
import com.px.mis.purc.dao.RtnGoodsDao;
import com.px.mis.purc.dao.RtnGoodsSubDao;
import com.px.mis.purc.dao.SellSnglDao;
import com.px.mis.purc.dao.SellSnglSubDao;
import com.px.mis.purc.entity.EntrsAgnStlSub;
import com.px.mis.purc.entity.RtnGoodsSub;
import com.px.mis.purc.entity.SellSnglSub;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;
//���۷�Ʊ����
@Service
@Transactional
public class SellInvMasTabServiceImpl extends poiTool implements SellInvMasTabService {
	@Autowired
	private SellInvMasTabDao sellInvMasTabDao;
//	@Autowired
//	private SellTypeDao sellTypeDao;
//	@Autowired
//	private BizTypeDao bizTypeDao;
//	@Autowired
//	private BizMemDocDao bizMemDocDao;
	@Autowired
	private CustDocDao custDocDao;
	@Autowired
	private SellInvSubTabDao SellInvSubTabDao;
	@Autowired
	private SellInvSubTabService sellInvSubTabService;
	@Autowired
	private SellSnglSubDao sellSnglSubDao;
	@Autowired
	private SellSnglDao sellSnglDao;
	@Autowired
	private RtnGoodsDao rtnGoodsDao;//�˻�������
	@Autowired
	private RtnGoodsSubDao rtnGoodsSubDao;//�˻����ӱ�
	@Autowired
	private EntrsAgnStlDao entrsAgnStlDao;//ί�д������㵥����
	@Autowired
	private EntrsAgnStlSubDao entrsAgnStlSubDao;//ί�д������㵥�ӱ�
	
	@Override
	public ObjectNode insertSellInvMasTab(SellInvMasTab sellInvMasTab,String userId) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sellInvNum = sellInvMasTab.getSellInvNum();
		System.out.println("dfghjklsdfghjkl"+sellInvNum);
		if(sellInvNum==null || sellInvNum.equals("")) {
			on.put("isSuccess", false);
			on.put("message", "����ʧ��,���۷�Ʊ�Ų���Ϊ��");
		}else if(sellInvMasTabDao.selectSellInvMasTabBySellInvNum(sellInvNum)!=null){
			on.put("isSuccess", false);
			on.put("message", "���������۷�Ʊ��"+sellInvNum+"�Ѵ��ڣ�����ʧ�ܣ�");
		}else if(custDocDao.selectCustDocByCustId(sellInvMasTab.getCustId())==null) {
			on.put("isSuccess", false);
			on.put("message", "�����пͻ����"+sellInvMasTab.getCustId()+"�����ڣ�����ʧ�ܣ�");
		}else {
			int insertResult = sellInvMasTabDao.insertSellInvMasTab(sellInvMasTab);
			if(insertResult==1) {
				List<SellInvSubTab> sellSubList = sellInvMasTab.getSellSubList();
				for(SellInvSubTab sellInvSubTab:sellSubList) {
					sellInvSubTab.setSellInvNum(sellInvNum);
					on = sellInvSubTabService.insertSellInvSubTab(sellInvSubTab);
				}
			}else {
				on.put("isSuccess", false);
				on.put("message", "���۷�Ʊ��������ʧ��");
			}
		}
		return on;
	}

	@Override
	public ObjectNode updateSellInvMasTab(SellInvMasTab sellInvMasTab,List<SellInvSubTab> sellInvSubTabList) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		String sellInvNum = sellInvMasTab.getSellInvNum();
		if(sellInvNum==null || sellInvNum.equals("")) {
			on.put("isSuccess", false);
			on.put("message", "����ʧ��,���۷�Ʊ�Ų���Ϊ��");
		}else if(sellInvMasTabDao.selectSellInvMasTabBySellInvNum(sellInvNum)==null){
			on.put("isSuccess", false);
			on.put("message", "���������۷�Ʊ��"+sellInvNum+"�����ڣ�����ʧ�ܣ�");
		}else if(custDocDao.selectCustDocByCustId(sellInvMasTab.getCustId())==null) {
			on.put("isSuccess", false);
			on.put("message", "�����пͻ����"+sellInvMasTab.getCustId()+"�����ڣ�����ʧ�ܣ�");
		}else {
			int updateResult = sellInvMasTabDao.updateSellInvMasTabBySellInvNum(sellInvMasTab);
			int deleteResult = sellInvSubTabService.deleteSellInvSubTabBySellInvNumv(sellInvMasTab.getSellInvNum());
			if(updateResult==1 && deleteResult >=1) {
				for(SellInvSubTab sellInvSubTab:sellInvSubTabList) {
					sellInvSubTab.setSellInvNum(sellInvNum);
					SellInvSubTabDao.insertSellInvSubTab(sellInvSubTab);
				}
				on.put("isSuccess", true);
				on.put("message", "���۷�Ʊ���´���ɹ�");
			}else {
				on.put("isSuccess", false);
				on.put("message", "���۷�Ʊ���´���ʧ��");
			}
		}
		return on;
	}

	@Override
	public ObjectNode deleteSellInvMasTabBySellInvNum(String sellInvNum) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
			
		Integer deleteResult = sellInvSubTabService.deleteSellInvSubTabBySellInvNumv(sellInvNum);
		deleteResult = sellInvMasTabDao.deleteSellInvMasTabBySellInvNum(sellInvNum);
		if(deleteResult==1) {
			on.put("isSuccess", true);
			on.put("message", "����ɹ�");
		}else if(deleteResult==0){
			on.put("isSuccess", true);
			on.put("message", "û��Ҫɾ��������");		
		}else {
			on.put("isSuccess", false);
			on.put("message", "����ʧ��");
		}
		return on;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public SellInvMasTab selectSellInvMasTabBySellInvNum(String sellInvNum) {
		SellInvMasTab sellInvMasTab = sellInvMasTabDao.selectSellInvMasTabBySellInvNum(sellInvNum);
		return sellInvMasTab;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectSellInvMasTabList(Map map) {
		String resp="";
		List<SellInvMasTab> list = sellInvMasTabDao.selectSellInvMasTabList(map);
		int count = sellInvMasTabDao.selectSellInvMasTabCount(map);
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/sellInvMasTab/selectSellInvMasTab", true, "��ѯ�ɹ���", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	//����ɾ��
	@Override
	public String deleteSellInvMasTabList(String sellInvNum) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		List<String> list = getList(sellInvNum);
		if(sellInvMasTabDao.selectSellInvMasTabIsNtChk(sellInvNum)==0) {
			int a = sellInvMasTabDao.deleteSellInvMasTabList(list);
			if(a>=1) {
			    isSuccess=true;
			    message="ɾ���ɹ���";
			}else {
				isSuccess=false;
				message="ɾ��ʧ�ܣ�";
			}
		}else {
			isSuccess=false;
			message="���ݺ�Ϊ��"+sellInvNum+"�ķ�Ʊ�ѱ���ˣ��޷�ɾ����";
		}
		try {
			resp=BaseJson.returnRespObj("account/sellInvMasTab/deleteSellInvMasTabList", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	/**
	 * id����list
	 * 
	 * @param id
	 *            id(����Ѷ��ŷָ�)
	 * @return List����
	*/
	public List<String> getList(String id) {
		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
		    list.add(str[i]);
	    }
		return list;
   }
	
	//��Ʊ���
	@Override
	public String updateSellInvMasTabIsNtChkList(List<SellInvMasTab> sellInvMasTabList) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		for(SellInvMasTab sellInvMasTab:sellInvMasTabList) {
			String sellSnglNum=sellInvMasTab.getSellSnglNum();//��ȡ��Ӧ���ݺ�
			String toFormTypEncd=sellInvMasTab.getToFormTypEncd();//��ȡ��Ӧ��������
			if(sellInvMasTab.getIsNtChk()==1) {
				if(sellInvMasTabDao.selectSellInvMasTabIsNtChk(sellInvMasTab.getSellInvNum())==0) {
					if(sellSnglNum!=null && sellSnglNum!="") {
						if(toFormTypEncd!=null && toFormTypEncd!="") {
							if(toFormTypEncd.equals("007")) {
								List<String> sellSnglIdList = getSellSnglList(sellSnglNum);//��ȡ���۵����
								for(String sellSnglId : sellSnglIdList) {	
									sellSnglDao.updateSellSnglIsNtBllgOK(sellSnglId);//�޸Ŀ�Ʊ״̬
								}
							}else if(toFormTypEncd.equals("008")) {
								List<String> sellSnglIdList = getSellSnglList(sellSnglNum);//��ȡ�˻��������
								for(String rtnGoodsId : sellSnglIdList) {	
									rtnGoodsDao.updateRtnGoodsIsNtBllgOK(rtnGoodsId);//�޸Ŀ�Ʊ״̬
								}
							}else if(toFormTypEncd.equals("025")) {
								List<String> sellSnglIdList = getSellSnglList(sellSnglNum);//��ȡί�д������㵥���
								for(String stlSnglId : sellSnglIdList) {	
									entrsAgnStlDao.updateEntrsAgnStlIsNtBllgOK(stlSnglId);//�޸Ŀ�Ʊ״̬
								}
							}
						}else {
							isSuccess=false;
							message +="���ݺ�Ϊ"+sellInvMasTab.getSellInvNum()+ "�ķ�Ʊ��Ӧҵ�񵥾ݵĵ�������Ϊ�գ��޷���ˣ�\n";
						}
						
					}
					//�޸����״̬
					int a =sellInvMasTabDao.updateSellInvMasTabIsNtChk(sellInvMasTab);
					if(a>=1) {
					    isSuccess=true;
					    message+="���ݺ�Ϊ"+sellInvMasTab.getSellInvNum()+ "������ר�÷�Ʊ��˳ɹ���\n";
					}else {
						isSuccess=false;
						message +="���ݺ�Ϊ"+sellInvMasTab.getSellInvNum()+ "������ר�÷�Ʊ���ʧ�ܣ�\n";
					}
				}else if(sellInvMasTabDao.selectSellInvMasTabIsNtChk(sellInvMasTab.getSellInvNum())==1){
					isSuccess=false;
					message += "���ݺ�Ϊ"+sellInvMasTab.getSellInvNum()+ "������ר�÷�Ʊ����ˣ�����Ҫ�ظ����\n";
				}
			}else if(sellInvMasTab.getIsNtChk()==0) {
				if(sellInvMasTabDao.selectSellInvMasTabIsNtBookEntry(sellInvMasTab.getSellInvNum())==0) {
					if(sellInvMasTabDao.selectSellInvMasTabIsNtChk(sellInvMasTab.getSellInvNum())==1) {
						if(sellSnglNum!=null && sellSnglNum!="" ) {
							if(toFormTypEncd!=null && toFormTypEncd!="") {
								if(toFormTypEncd.equals("007")) {
									List<String> sellSnglIdList = getSellSnglList(sellSnglNum);//��ȡ���۵����
									for(String sellSnglId : sellSnglIdList) {	
										sellSnglDao.updateSellSnglIsNtBllgNO(sellSnglId);//�޸Ŀ�Ʊ״̬
									}
								}else if(toFormTypEncd.equals("008")) {
									List<String> sellSnglIdList = getSellSnglList(sellSnglNum);//��ȡ�˻��������
									for(String rtnGoodsId : sellSnglIdList) {	
										rtnGoodsDao.updateRtnGoodsIsNtBllgNO(rtnGoodsId);//�޸Ŀ�Ʊ״̬
									}
								}else if(toFormTypEncd.equals("025")) {
									List<String> sellSnglIdList = getSellSnglList(sellSnglNum);//��ȡί�д������㵥���
									for(String stlSnglId : sellSnglIdList) {	
										entrsAgnStlDao.updateEntrsAgnStlIsNtBllgNO(stlSnglId);//�޸Ŀ�Ʊ״̬
									}
								}
							}else {
								isSuccess=false;
								message +="���ݺ�Ϊ"+sellInvMasTab.getSellInvNum()+ "�ķ�Ʊ��Ӧҵ�񵥾ݵĵ�������Ϊ�գ��޷���ˣ�\n";
							}
						}
						int a = sellInvMasTabDao.updateSellInvMasTabIsNtChk(sellInvMasTab);
						if(a>=1){
						    isSuccess=true;
						    message+="���ݺ�Ϊ"+sellInvMasTab.getSellInvNum()+ "������ר�÷�Ʊ����ɹ���\n";
						}else {
							isSuccess=false;
							message +="���ݺ�Ϊ"+sellInvMasTab.getSellInvNum()+ "������ר�÷�Ʊ����ʧ�ܣ�\n";
						}
					}else{
						isSuccess=false;
						message += "���ݺ�Ϊ"+sellInvMasTab.getSellInvNum()+ "������ר�÷�Ʊδ��ˣ�������˸õ���\n";
					}
				}else {
					isSuccess=false;
					message += "���ݺ�Ϊ"+sellInvMasTab.getSellInvNum()+ "������ר�÷�Ʊ�Ѽ��ˣ��޷�����\n";
				}
				
			}
		}
	    try {
			resp=BaseJson.returnRespObj("/account/sellInvMasTab/updateSellInvMasTabIsNtChk", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	/**
	 * id����list
	 * 
	 * @param id id(����Ѷ��ŷָ�)
	 * @return List����
	 */
	public List<String> getSellSnglList(String id) {
		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
			list.add(str[i]);
		}
		return list;
	}
	
	//�������۳��ⵥ����һ�����۷�Ʊ
	@RequestMapping(value="selectSellInvMasTabBingList",method = RequestMethod.POST)
	@ResponseBody
	public String selectSellInvMasTabBingList(List<SellComnInv> sellSnglList) {
		String resp="";
		String message="";
		Boolean isSuccess=true;
		try {
			//ʵ����һ���µĶ���    ������ͨ��Ʊ����
			SellInvMasTab sellInvMasTab = new SellInvMasTab();
			List<SellInvSubTab> sellInvSubTabList = new ArrayList<>();
			Map<String,Object> map  = new HashMap<>();
			//��ȡList�����е�һ����Ӧ��������map��
			SellComnInv sellSngl = sellSnglList.get(0);
			//���ͻ����룬���ű��룬ҵ��Ա����ƴ������
			String str1 = sellSngl.getCustId()+sellSngl.getDeptId()+sellSngl.getAccNum();
			map.put("uniqInd", str1);
			String sellSnglId="";
			for(SellComnInv sellSng: sellSnglList) {
				sellSnglId+=sellSng.getSellInvNum()+",";
				String str2 = sellSng.getCustId()+sellSng.getDeptId()+sellSng.getAccNum();
				if(str2.equals(map.get("uniqInd").toString())==false) {
					isSuccess=false;
					message="����ѡ����ͬ�ͻ������š�ҵ��Ա�ĵ��ݽ���������������ѡ��";
				}else if(str2.equals(map.get("uniqInd").toString())==true){
					BeanUtils.copyProperties(sellInvMasTab,sellSng);//�����۳��ⵥ�������������۷�Ʊ����
					sellInvMasTab.setSellSnglNum(sellSnglId.substring(0,sellSnglId.length()-1));
					if(sellInvMasTab.getSellSnglNum()!=null) {
						if(sellSng.getFormTypEncd()==null || sellSng.getFormTypEncd().equals("")) {
							isSuccess=false;
							throw new RuntimeException("����ѡ�����,�������������յĵ����޵������ͣ�");
						}else if(sellSng.getFormTypEncd().equals("007")) {//���۵�
							//��������������ѯ��ص��ӱ���Ϣ
							List<SellSnglSub> sellSnglSnglList = sellSnglSubDao.selectSellSnglSubBySellSnglId(sellSng.getSellInvNum());
							for(SellSnglSub sellSnglSub:sellSnglSnglList) {
								SellInvSubTab sellInvSubTab = new SellInvSubTab();
								BeanUtils.copyProperties(sellInvSubTab,sellSnglSub);//�����۵��ӱ��������۷�Ʊ�ӱ�
								sellInvSubTabList.add(sellInvSubTab);
							}
						}else if(sellSng.getFormTypEncd().equals("008")) {//�˻���
							//��������������ѯ��ص��ӱ���Ϣ
							List<RtnGoodsSub> rtnGoodsSubList = rtnGoodsSubDao.selectRtnGoodsSubByRtnGoodsId(sellSng.getSellInvNum());
							for(RtnGoodsSub rtnGoodsSub:rtnGoodsSubList) {
								SellInvSubTab sellInvSubTab = new SellInvSubTab();
								BeanUtils.copyProperties(sellInvSubTab,rtnGoodsSub);//���˻����ӱ��������۷�Ʊ�ӱ�
								sellInvSubTabList.add(sellInvSubTab);
							}
						}else if(sellSng.getFormTypEncd().equals("025")) {//ί�д������㵥
							//��������������ѯ��ص��ӱ���Ϣ
							List<EntrsAgnStlSub> entrsAgnStlSubList = entrsAgnStlSubDao.selectEntrsAgnStlSubByStlSnglId(sellSng.getSellInvNum());
							for(EntrsAgnStlSub entrsAgnStlSub:entrsAgnStlSubList) {
								SellInvSubTab sellInvSubTab = new SellInvSubTab();
								BeanUtils.copyProperties(sellInvSubTab,entrsAgnStlSub);//��ί�д������㵥���������۷�Ʊ�ӱ�
								sellInvSubTabList.add(sellInvSubTab);
							}
						}
					}else {
						isSuccess=false;
						throw new RuntimeException("����ѡ�����,�������������յĵ����޶�����ţ�");
					}
					isSuccess=true;
					message="�����ɹ���";
				}
			}
			sellInvMasTab.setSellSubList(sellInvSubTabList);
			resp=BaseJson.returnRespObj("/account/sellInvMasTab/selectSellInvMasTabBingList", isSuccess, message,sellInvMasTab);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	//����
	@Override
	public String uploadFileAddDb(MultipartFile file) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, SellInvMasTab> sellInvMasTabMap = uploadScoreInfo(file);

		for (Map.Entry<String, SellInvMasTab> entry : sellInvMasTabMap.entrySet()) {
			SellInvMasTab sellInvMasTab =sellInvMasTabDao.selectSellInvMasTabBySellInvNum(entry.getValue().getSellInvNum());
			if (sellInvMasTab != null) {
				isSuccess = false;
				message = "���۷�Ʊ�в��ֵ��ݱ���Ѵ��ڣ��޷����룬������ٵ��룡";
				throw new RuntimeException(message);
			}
			sellInvMasTabDao.insertSellInvMasTabUpload(entry.getValue());
			for (SellInvSubTab sellInvSubTab : entry.getValue().getSellSubList()) {
				sellInvSubTab.setSellInvNum(entry.getValue().getSellInvNum());
				
			}
			SellInvSubTabDao.insertSellInvSubTabList(entry.getValue().getSellSubList());
			isSuccess = true;
			message = "���۷�Ʊ����ɹ���";
		}

		try {
			resp = BaseJson.returnRespObj("purc/sellInvMasTab/uploadSellInvMasTabFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����excle
	private Map<String, SellInvMasTab> uploadScoreInfo(MultipartFile file) {
		Map<String, SellInvMasTab> temp = new HashMap<>();
		int j = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			InputStream fileIn = file.getInputStream();
			// ����ָ�����ļ�����������Excel�Ӷ�����Workbook����
			Workbook wb0 = new HSSFWorkbook(fileIn);
			// ��ȡExcel�ĵ��еĵ�һ����
			Sheet sht0 = wb0.getSheetAt(0);
			// ��õ�ǰsheet�Ŀ�ʼ��
			int firstRowNum = sht0.getFirstRowNum();
			// ��ȡ�ļ������һ��
			int lastRowNum = sht0.getLastRowNum();
			// ���������ֶκ��±�ӳ��
			SetColIndex(sht0.getRow(firstRowNum));
			// ��Sheet�е�ÿһ�н��е���
			for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
				j++;
				Row r = sht0.getRow(i);
				// �����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
				if (r.getRowNum() < 1) {
					continue;
				}
				String orderNo = GetCellData(r, "���۷�Ʊ����");
				// ����ʵ����
				SellInvMasTab sellInvMasTab = new SellInvMasTab();
				if (temp.containsKey(orderNo)) {
					sellInvMasTab = temp.get(orderNo);
				}
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				sellInvMasTab.setSellInvNum(orderNo);//���۷�Ʊ����
				if (GetCellData(r, "��Ʊ����") == null || GetCellData(r, "��Ʊ����").equals("")) {
					sellInvMasTab.setBllgDt(null);
				} else {
					sellInvMasTab.setBllgDt(GetCellData(r, "��Ʊ����").replaceAll("[^0-9:-]", " "));//��Ʊ����
				}
				sellInvMasTab.setAccNum(GetCellData(r, "ҵ��Ա����")); // ҵ��Ա����', varchar(200
				sellInvMasTab.setUserName(GetCellData(r, "��Ӧ�̱���")); // '��Ӧ�̱���', varchar(200
				sellInvMasTab.setBizTypId(GetCellData(r, "ҵ�����ͱ���"));
				sellInvMasTab.setSellTypId(GetCellData(r, "�������ͱ���")); // '�������ͱ���', varchar(200
				sellInvMasTab.setDeptId(GetCellData(r, "���ű���")); // '���ű���', varchar(200
				sellInvMasTab.setFormTypEncd(GetCellData(r, "�������ͱ���"));// �������ͱ���
				sellInvMasTab.setToFormTypEncd(GetCellData(r, "��Ӧ�������ͱ���"));//��Ӧ�������ͱ���
				sellInvMasTab.setIsNtChk(new Double(GetCellData(r, "�Ƿ����")).intValue()); // '�Ƿ����', int(11
				sellInvMasTab.setChkr(GetCellData(r, "�����")); // '�����', varchar(200
				if (GetCellData(r, "���ʱ��") == null || GetCellData(r, "���ʱ��").equals("")) {
					sellInvMasTab.setChkTm(null);
				} else {
					sellInvMasTab.setChkTm(GetCellData(r, "���ʱ��").replaceAll("[^0-9:-]", " "));// ���ʱ��
				}
				
				sellInvMasTab.setIsNtBookEntry(new Double(GetCellData(r,"�Ƿ����")).intValue()); //�Ƿ����
				sellInvMasTab.setBookEntryPers(GetCellData(r,"������")); // ������',
				if(GetCellData(r,"����ʱ��")==null || GetCellData(r,"����ʱ��").equals("")) {
					sellInvMasTab.setBookEntryTm(null); 
				}else {
					sellInvMasTab.setBookEntryTm(GetCellData(r,"����ʱ��").replaceAll("[^0-9:-]"," "));//����ʱ�� 
				}
				 
				sellInvMasTab.setSetupPers(GetCellData(r, "������")); // '������', varchar(200
				if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
					sellInvMasTab.setSetupTm(null);
				} else {
					sellInvMasTab.setSetupTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				sellInvMasTab.setMdfr(GetCellData(r, "�޸���")); // '�޸���', varchar(200
				if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
					sellInvMasTab.setModiTm(null);
				} else {
					sellInvMasTab.setModiTm(GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				sellInvMasTab.setSellSnglNum(GetCellData(r, "��Դ���ݺ�"));
				sellInvMasTab.setMemo(GetCellData(r, "����ע")); // '��ע', varchar(2000
				List<SellInvSubTab> sellInvSubTabList = sellInvMasTab.getSellSubList();//���۷�Ʊ�ӱ�
				if (sellInvSubTabList == null) {
					sellInvSubTabList = new ArrayList<>();
				}
				SellInvSubTab sellInvSubTab = new SellInvSubTab();

				sellInvSubTab.setWhsEncd(GetCellData(r, "�ֿ����")); // '�ֿ����',
				sellInvSubTab.setInvtyEncd(GetCellData(r, "�������")); // '�������',
				sellInvSubTab.setQty(GetBigDecimal(GetCellData(r, "����"), 8)); // '����',
				sellInvSubTab.setBxQty(GetBigDecimal(GetCellData(r, "����"), 8)); // '����',
				sellInvSubTab.setBxRule(GetBigDecimal(GetCellData(r, "���"), 8));//���
				sellInvSubTab.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8)); // '��˰����',
				sellInvSubTab.setPrcTaxSum(GetBigDecimal(GetCellData(r, "��˰�ϼ�"), 8)); // '��˰�ϼ�',
				sellInvSubTab.setNoTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8)); // '��˰����',
				sellInvSubTab.setNoTaxAmt(GetBigDecimal(GetCellData(r, "��˰���"), 8)); // '��˰���',
				sellInvSubTab.setTaxAmt(GetBigDecimal(GetCellData(r, "˰��"), 8)); // '˰��',
				sellInvSubTab.setTaxRate(GetBigDecimal(GetCellData(r, "˰��"), 8)); // '˰��',
				sellInvSubTab.setIntlBat(GetCellData(r, "��������")); // ��������
				sellInvSubTab.setBatNum(GetCellData(r, "����")); // '����',
				sellInvSubTab.setMemo(GetCellData(r, "�ӱ�ע")); // '��ע',
				sellInvSubTabList.add(sellInvSubTab);//�ӱ�
				sellInvMasTab.setSellSubList(sellInvSubTabList);;
				temp.put(orderNo, sellInvMasTab);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
		}
		return temp;
	}

	@Override
	public String upLoadSellInvMasTabList(Map map) {
		String resp = "";
		List<SellInvMasTab> poList = sellInvMasTabDao.printingSellInvMasTabList(map);
		try {
			resp = BaseJson.returnRespObjList("purc/sellInvMasTab/printingSellInvMasTabList", true, "��ѯ�ɹ���", null, poList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

}
