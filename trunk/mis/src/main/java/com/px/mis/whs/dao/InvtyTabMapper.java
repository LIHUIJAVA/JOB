package com.px.mis.whs.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.px.mis.whs.entity.BaoZhiQi;
import com.px.mis.whs.entity.InvldtnDt;
import com.px.mis.whs.entity.InvtyStandReport;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.NowStokReport;
import com.px.mis.whs.entity.NtChkNo;
import com.px.mis.whs.entity.OutIngWaterReport;
import com.px.mis.whs.entity.TransceiverSummaryReport;

@SuppressWarnings("rawtypes")
public interface InvtyTabMapper {


    //������Ԥ��
    public List<BaoZhiQi> selectBaoZhiQiList(Map map);

    public int selectBaoZhiQiCount(Map map);

    public BaoZhiQi selectBaoZhiQiSUM(Map map);

    //�շ������
    public List<TransceiverSummaryReport> selectTSummaryList(Map map);

    public int selectTSummaryCount(Map map);

    public TransceiverSummaryReport selectTSummary(Map map);

    public TransceiverSummaryReport selectTSummaryWhs(TransceiverSummaryReport report);

    public List<InvtyTab> selectothOutIntoWhsInto(Map map);

    public List<InvtyTab> selectothOutIntoWhsOut(Map map);

    //�շ�����ܿ���ڳ�
    public TransceiverSummaryReport selectInvtyTermBgnTab(Map map);


    public List<InvtyTab> selectothIntoWhs(Map map);

    public List<InvtyTab> selectothSellOutWhs(Map map);

    //�ִ�����ѯ
    public List<NowStokReport> selectExtantQtyList(Map map);

    public NowStokReport selectExtantQtySUM(Map map);

    public List<InvtyTab> selectToGdsSngl(Map map);

    public List<InvtyTab> selectSellSngl(Map map);

    public List<InvtyTab> selectSellOutWhs(Map map);

    public List<InvtyTab> selectCannibSngl(Map map);

    public List<InvtyTab> selectIntoWhsSub(Map map);

    public List<InvtyTab> selectNoIntoWhsSub(Map map);

    public List<InvtyTab> selectOutIntoWhsOut(Map map);

    public List<InvtyTab> selectOutIntoWhsIn(Map map);


    public NowStokReport selectWhsSub(NowStokReport nowStokReport);

    public int selectExtantQtyCount(Map map);

    //�������ˮ
    public List<OutIngWaterReport> selectOutIngWaterList(Map map);

    public int selectOutIngWaterCount(Map map);

    public OutIngWaterReport selectOutIngWaterSUM(Map map);

    //    ���������
    public List<Map> outIntoWhsTyp(Map map);

    //���̨��
    public List<Map> selectInvtyStandList(Map map);

    public List<InvtyStandReport> selectOutIngWater(Map map);

    public InvtyStandReport selectOutIngSUM(Map map);

    public int selectInvtyStandCount(Map map);

    public InvtyTab selectInvtyTabSum(Map invtyTab);

    public List<Map> selectInvty(Map map);//��ѯ���д����Ϣ  ���鷵��ǰ��

    //=====zds create=================================
    public void updateByReverse(InvtyTab invtyTab);//��������޸Ŀ��ʱ���

    public InvtyTab selectByReverse(InvtyTab invtyTab);//��������޸Ŀ��ʱ���

    //--------hj---�����ѯ------------------------------------------------------------
    List<InvtyTab> selectInvtyTabList(Map map);

    int selectInvtyTabCount(Map map);

    //  ʧЧ����ά��
    public List<InvldtnDt> selectInvldtnDtList(Map map);

    public InvldtnDt selectInvldtnDtSUM(Map map);

    public int selectInvldtnDtCount(Map map);
//	// ���λ��ܱ�
//	public List<InvldtnDt> selectBatNumSummaryList(Map map);
//	public int selectBatNumSummaryCount(Map map);

    //  δ��˵���
    public List<NtChkNo> selectNtChkNoList(Map map);

    public int selectNtChkNoCount(Map map);

    public int exInvtyTab(List<InvtyTab> values);
}