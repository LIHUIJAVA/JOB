package com.px.mis.account.service.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.BankDocDao;
import com.px.mis.account.entity.BankDoc;
import com.px.mis.account.service.BankDocService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
@Service
@Transactional
public class BankDocServiceImpl  extends poiTool  implements BankDocService {
	@Autowired
	private BankDocDao bankDocDao;
	@Override
	public ObjectNode insertBankDoc(BankDoc bankDoc) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		if(bankDocDao.selectBankDocByOrdrNum(bankDoc.getBankEncd())==null) {
			int insertResult = bankDocDao.insertBankDoc(bankDoc);
			if(insertResult==1) {
				on.put("isSuccess", true);
				on.put("message", "�����ɹ�");
			}else {
				on.put("isSuccess", false);
				on.put("message", "����ʧ��");
			}
		}else {
			on.put("isSuccess", false);
			on.put("message", "����ʧ�ܣ�����"+bankDoc.getBankEncd()+"�Ѵ��ڣ�");
		}
		
		return on;	
	}

	@Override
	public ObjectNode updateBankDocByordrNum(List<BankDoc> bankDocList) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		for(BankDoc bankDoc: bankDocList) {
			String bankEncd = bankDoc.getBankEncd();
			if(bankEncd == null) {
				on.put("isSuccess", false);
				on.put("message", "�޸�ʧ�ܣ���������Ϊnull��");
			}else if (bankDocDao.selectBankDocByOrdrNum(bankEncd)==null) {
				on.put("isSuccess", false);
				on.put("message", "�޸�ʧ�ܣ����"+bankEncd+"�����ڣ�");
			}else {
				int updateResult = bankDocDao.updateBankDocByOrdrNum(bankDoc);
				if(updateResult==1) {
					on.put("isSuccess", true);
					on.put("message", "�޸ĳɹ�");
				}else {
					on.put("isSuccess", false);
					on.put("message", "�޸�ʧ��");
				}
			}
		}
		return on;
	}

	@Override
	public ObjectNode deleteBankDocByOrdrNum(String bankEncd) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		List<String> lists = getList(bankEncd);
		Integer deleteResult = bankDocDao.deleteBankDocByOrdrNum(lists);
		if(deleteResult>=1) {
			on.put("isSuccess", true);
			on.put("message", "ɾ���ɹ�");
		}else if(deleteResult==0){
			on.put("isSuccess", true);
			on.put("message", "û��Ҫɾ��������");		
		}else {
			on.put("isSuccess", false);
			on.put("message", "ɾ��ʧ��");
		}
		
		return on;
	}
	/**
	 * id����list
	 * 
	 * @param id id(����Ѷ��ŷָ�)
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

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public BankDoc selectBankDocByordrNum(String id) {
		BankDoc bankDoc = bankDocDao.selectBankDocByOrdrNum(id);
		return bankDoc;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectBankDocList(Map map) {
		String resp="";
		List<BankDoc> list = bankDocDao.selectBankDocList(map);
		int count = bankDocDao.selectBankDocCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/bankDoc/selectBankDoc", true, "��ѯ�ɹ���", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String selectBankDocPrint(Map map) {

		String resp = "";
		List<BankDoc> list = bankDocDao.selectBankDocList(map);

		try {
			resp = BaseJson.returnRespList("/account/bankDoc/selectBankDocPrint", true, "��ѯ�ɹ���", list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;

	}
	@Override
	public String uploadFileAddDb(MultipartFile file) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, BankDoc> pusOrderMap = uploadScoreInfo(file);
		System.out.println(pusOrderMap.size());
		for (Map.Entry<String, BankDoc> entry : pusOrderMap.entrySet()) {

			BankDoc acctItmDoc = bankDocDao.selectBankDocByOrdrNum(entry.getValue().getBankEncd());

			if (acctItmDoc != null) {
				throw new RuntimeException("�����ظ����� " + entry.getValue().getBankEncd() + " ����");
			}
			try {
				bankDocDao.insertBankDoc(entry.getValue());

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				throw new RuntimeException("����sql����");

			}
		}

		isSuccess = true;
		message = "���������ɹ���";
		try {
			resp = BaseJson.returnRespObj("account/bankDoc/uploadFileAddDb", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resp;

	}
	// ����excle
	private Map<String, BankDoc> uploadScoreInfo(MultipartFile file) {
		Map<String, BankDoc> temp = new HashMap<>();
		int j = 1;
		try {
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

				String orderNo = GetCellData(r, "���б���");
				// ����ʵ����
				BankDoc bankDoc = new BankDoc();
				if (temp.containsKey(orderNo)) {
					bankDoc = temp.get(orderNo);
				}
				// r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//						System.out.println(r.getCell(0));
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������

				bankDoc.setBankEncd(orderNo);//���б���String
				bankDoc.setBankNm(GetCellData(r, "��������"));//��������String
				bankDoc.setIndvAcctIsFixlen(
						(new Double(GetCellData(r, "�����˺��Ƿ񶨳�") == null || GetCellData(r, "�����˺��Ƿ񶨳�").equals("") ? "0"
								: GetCellData(r, "�����˺��Ƿ񶨳�"))).intValue());//����bank_encd�˺��Ƿ񶨳�Integer
				bankDoc.setIndvAcctNumLen(
						(new Double(GetCellData(r, "�����˺ų���") == null || GetCellData(r, "�����˺ų���").equals("") ? "0"
								: GetCellData(r, "�����˺ų���"))).intValue());//�����˺ų���Integer
				bankDoc.setAutoOutIndvNumLen(
						(new Double(GetCellData(r, "�Զ������ĸ����˺ų���") == null || GetCellData(r, "�Զ������ĸ����˺ų���").equals("") ? "0"
								: GetCellData(r, "�Զ������ĸ����˺ų���"))).intValue());//�Զ������ĸ����˺ų���Integer
				bankDoc.setCorpEncd(GetCellData(r, "��λ����"));//��λ����String
				bankDoc.setCompAcctIsFixLen(
						(new Double(GetCellData(r, "��ҵ�˺��Ƿ񶨳�") == null || GetCellData(r, "��ҵ�˺��Ƿ񶨳�").equals("") ? "0"
								: GetCellData(r, "��ҵ�˺��Ƿ񶨳�"))).intValue());//��ҵ�˺��Ƿ񶨳�Integer
				bankDoc.setCompAcctNumLen(
						(new Double(GetCellData(r, "��ҵ�˺ų���") == null || GetCellData(r, "��ҵ�˺ų���").equals("") ? "0"
								: GetCellData(r, "��ҵ�˺ų���"))).intValue());//��ҵ�˺ų���Integer
				bankDoc.setBankId(
						(new Double(GetCellData(r, "���б�ʶ") == null || GetCellData(r, "���б�ʶ").equals("") ? "0"
								: GetCellData(r, "���б�ʶ"))).intValue());//���б�ʶInteger
				bankDoc.setIsSysData(
						(new Double(GetCellData(r, "�Ƿ�ϵͳԤ��") == null || GetCellData(r, "�Ƿ�ϵͳԤ��").equals("") ? "0"
								: GetCellData(r, "�Ƿ�ϵͳԤ��"))).intValue());//�Ƿ�ϵͳԤ��Integer
				bankDoc.setPubufts(new Date());//ʱ���Date
				
		
				temp.put(orderNo, bankDoc);

			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("������ʽ�����" + j + "��"+e.getMessage());
		}
		return temp;
	}
}
