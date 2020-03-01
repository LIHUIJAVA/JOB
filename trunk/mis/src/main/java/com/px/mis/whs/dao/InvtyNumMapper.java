package com.px.mis.whs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.whs.entity.GdsBit;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.MovBitTab;
import com.px.mis.whs.entity.OthOutIntoWhs;

//�����������
public interface InvtyNumMapper {

    //�ش�����  ָ����λ ��������Ҫ�������ӿ�����  �޸ĳ���ⵥ�����״̬[������--�������]��
    public int updateInvtyTab(List<InvtyTab> invtyTab);//�ִ�������(����)

    public int updateInvtyTabJia(List<InvtyTab> invtyTab);//�ִ�������(�ӷ�)

    public int updateAInvtyTab(List<InvtyTab> invtyTab);//����������(����)

    public int updateAInvtyTabJia(List<InvtyTab> invtyTab);//����������(�ӷ�)

    //�޸Ŀ��������  ������Ϣ
    public int updateInvtyTabAmtJia(List<InvtyTab> invtyTab);//������������ʱ���¿��� �ִ�������

    public int updateInvtyTabAmt(List<InvtyTab> invtyTab);//������������ʱ���¿��� �ִ�������

    public List<MovBitTab> selectAllMbit(List<MovBitTab> movBitTab);//�����ⵥ����Ļ�λ�Ƿ�ԭ�л�λ����������������δ���������»�λ��

    public int insertMovBitTab(List<MovBitTab> movBitTab);//������λ��

    //public InvtyTab selectInvtyTab(InvtyTab invtyTab);//��ѯ�ִ���--
    public List<GdsBit> selectRegn(@Param("gdsBitEncd") String gdsBitEncd);//ͨ����λ��ѯ����--

    public int updateMovbitTab(List<MovBitTab> movBitTab);//�޸���λ������(����)

    public int updateMovbitTabJia(List<MovBitTab> movBitTab);//�޸���λ������(�ӷ�)

    //��ѯ����
    InvtyTab selectInvtyTabByTerm(InvtyTab invtyTab);

    /**
     * ��������
     * <p>
     * ��� �½���� ����Ľ��ֵ
     */
    int insertInvtyTabList(List<InvtyTab> invtyTabList);

    public List<InvtyTab> selectInvtyTabByBatNum(InvtyTab invtyTabList);


    public MovBitTab selectMbit(MovBitTab movBitTab);//�����ⵥ����Ļ�λ�Ƿ�ԭ�л�λ����������������δ���������»�λ��

    public int updateJiaMbit(MovBitTab movBitTab);//������������

    public int updateJianMbit(MovBitTab movBitTab);//δ��������

    public int insertMovBitTabJia(MovBitTab movBitTab);//������λ��
    //ɾ��
//	public int insertMovBitTabJia(MovBitTab movBitTab);//������λ��


}
