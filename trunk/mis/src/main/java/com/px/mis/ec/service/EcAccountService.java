package com.px.mis.ec.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.ec.entity.EcAccount;

public interface EcAccountService {

	public String insert(List<EcAccount> ecAccounts);
	public String delete(String ids);
	public String update(EcAccount ecAccount);
	public String selectList(Map map);
	//����
	public String check(String userId,String ids,String startDate,String endDate);
	//����
	public String importAccount(MultipartFile file,String userId,String storeId);
	//����
	public String download(String userId,String storeId,String startDate,String endDate);
	//�Զ�����
	public String autoCheck(String checker,String checkTime);
	//���Ҳ�ѯ
	public String goToCheck(Map map);
}
