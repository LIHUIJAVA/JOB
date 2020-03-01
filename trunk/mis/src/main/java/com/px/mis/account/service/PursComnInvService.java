package com.px.mis.account.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.account.entity.PursComnInv;
import com.px.mis.account.entity.PursComnInvSub;
import com.px.mis.account.entity.PursComnInvForU8.U8PursComnInv;
import com.px.mis.purc.entity.IntoWhs;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface PursComnInvService {

	// �����ɹ���ͨ��Ʊ
	String addPursComnInv(PursComnInv pursComnInv, List<PursComnInvSub> pursComnInvSubList, String userId,
			String loginTime);

	// �޸Ĳɹ���ͨ��Ʊ
	String updatePursComnInv(PursComnInv pursComnInv, List<PursComnInvSub> pursComnInvSubList);

	// ɾ���ɹ���ͨ��Ʊ
	String deletePursComnInv(String pursInvNum);

	// ��ѯ�ɹ���ͨ��Ʊ
	String queryPursComnInvByPursInvNum(String pursInvNum);

	// ��ҳ��ѯ�ɹ���ͨ��Ʊ
	String queryPursComnInvList(Map map);

	// ����ɾ���ɹ���ͨ��Ʊ
	String deletePursComnInvList(String pursInvNum);

	// ��˲ɹ���ͨ��Ʊ
	Map<String, Object> updatePursComnInvIsNtChkList(PursComnInv pursComnInv) throws Exception;

	// �ɹ���Ʊ����ʱ�����ղɹ���ⵥ��������Ųɹ���ⵥ
	String selectPursComnInvBingList(List<IntoWhs> intoWhsList);

	// �������۳��ⵥ
	public String uploadFileAddDb(MultipartFile file, int i);

	// ����ʱ��ʹ�õĲ�ѯ�ӿ�
	String printPursComnInvList(Map map);

	// ԭ���ĵ����ӿ�,����ɾ
	String upLoadPursComnInvList(Map map);

	// ���͵�U8
	U8PursComnInv encapsulation(PursComnInv pursComnInv, List<PursComnInvSub> list) throws Exception;

	// ������ѯ->���͵�U8
	String pushToU8(String ids) throws Exception;

	



	

}
