package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.TermBgnBal;
import com.px.mis.purc.entity.EntrsAgnDelvSub;
import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.purc.entity.RtnGoodsSub;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.entity.ToGdsSnglSub;
import com.px.mis.whs.entity.InvtyTab;

public interface InvtyTabDao {
	
	//������������
	int insertInvtyTabList(List<IntoWhsSub> invtySubList);
	//�ɹ�������ʱ����ִ�еĲ���
	int insertInvtyTabToIntoWhs(IntoWhsSub invtySub);//���ɹ���ⵥ���ӱ���Ϣ ���� ��������
	int updateInvtyTabJiaToIntoWhs(IntoWhsSub intoWhsSub); //�ɹ�������ʱ �޸� �������� ��
	int updateInvtyTabJianToIntoWhs(IntoWhsSub intoWhsSub);//�ɹ�������ʱ �޸� �������� ��
	int updateInvtyTabJiaToIntoWhsReturn(IntoWhsSub intoWhsSub); //�ɹ��˻����ʱ �޸� �������� ��
	int updateInvtyTabJianToIntoWhsReturn(IntoWhsSub intoWhsSub);//�ɹ��˻����ʱ �޸� �������� ��
	
	//���������ʱ����ִ�еĲ���
	int insertInvtyTabByToGdsSngl(ToGdsSnglSub toGdsSnglSub);//�����������ӱ���Ϣ������������
	
	//��ͨ�����˻����������������Ϣ
	int insertInvtyTabToRtnGoods(RtnGoodsSub rtnGoodsSub);
	//ί�д����˻����������������Ϣ
    int insertInvtyTabToEnAgDelSub(EntrsAgnDelvSub entrsAgnDelvSub);
	
    int insertInvtyTabBySellOutWhsSub(SellOutWhsSub sellOutWhsSub);//���۳��ⵥ���ʱ�����������
	int updateInvtyTabJiaToSellOutWhs(SellOutWhsSub sellOutWhsSub);//���۳��ⵥ���ʱ �޸� �������� ��
	int updateInvtyTabJianToSellOutWhs(SellOutWhsSub sellOutWhsSub);//���۳��ⵥ���ʱ �޸� �������� ��
	
	//�ִ����Ϳ�������������
	int updateInvtyTabNowStokJia(InvtyTab invtyTab);//�ִ����������
	int updateInvtyTabNowStokJian(InvtyTab invtyTab);//�ִ����������
	int updateInvtyTabAvalQtyJia(InvtyTab invtyTab);//�ɴ����������
	int updateInvtyTabAvalQtyJian(InvtyTab invtyTab);//�ɴ����������
	
	/**��ѯ������ݲɹ�����ӱ� --���������� �������ѯʹ��**/
	@Deprecated
	InvtyTab selectInvtyTabByTerm(IntoWhsSub intoWhsSub);
	@Deprecated
	/** ��ѯ������Ϣ--����for update�� Ч�ʽϵ� ������ƽʱ��ѯʹ��* */
	InvtyTab selectInvtyTabsByTerm(InvtyTab invtyTab);
	
	List<InvtyTab> selectInvtyTabTerm();
	BigDecimal sumNowStockIn(Map<String,Object> parmMap);
	TermBgnBal sumNowStockOut(InvtyTab tab);
	BigDecimal sumAvalStockIn(Map<String,Object> parmMap);
	BigDecimal sumAvalStockOut(Map<String,Object> parmMap);
	void insertInvtyTab(List<InvtyTab> itList);
	
	List<TermBgnBal> selectInvtyTabIn();
	TermBgnBal sumTerm(InvtyTab it);
	
	void dealUpdate(InvtyTab it);
	
//	//�ִ�����������(����)
//	int updateInvtyTabList(List<InvtyTab> invtyTab);
//	//�ִ�����������(����)
//	int updateInvtyTabJiaList(List<InvtyTab> invtyTab); 
	
}