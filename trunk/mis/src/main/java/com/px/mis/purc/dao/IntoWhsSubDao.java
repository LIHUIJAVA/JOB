package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.IntoWhsSub;

public interface IntoWhsSubDao {

	int deleteIntoWhsSubByIntoWhsSnglId(String intoWhsSnglId);

	int insertIntoWhsSub(List<IntoWhsSub> intoWhsSubList);

	// ɾ��ʱ���ݵ��U����һ��
	int insertIntoWhsSubDl(List<String> list2);

	List<IntoWhsSub> selectIntoWhsSubByIntoWhsSnglId(String intoWhsSnglId);

	// �ɹ���Ʊ����ʱ��ѯ�ӱ���Ϣ
	List<IntoWhsSub> selectIntoWhsSubByIntoWhsSnglIds(List<String> intoWhsSnglId);

	// �޸Ĳɹ���ⵥ�ӱ���δ��Ʊ����
	int updateIntoWhsSubByInvWhsBat(Map map);

	// ���ݲɹ���ⵥ��Ų�ѯδ��Ʊ����
	BigDecimal selectUnBllgQtyByOrdrNum(Map map);

	// �ɹ��˻�������ʱ��ѯ�ӱ���Ϣ
	List<IntoWhsSub> selectIntoWhsSubByUnReturnQty(List<String> intoWhsSnglId);

	// �޸Ĳɹ���ⵥ�ӱ���δ�˻�����
	int updateIntoWhsSubUnReturnQty(Map map);

	// ���ݲɹ���ⵥ��Ų�ѯδ�˻�����
	BigDecimal selectUnReturnQtyByOrdrNum(Map map);
}