package com.px.mis.whs.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.whs.entity.InvtyTab;

public interface InvtyTabService {

    //������Ԥ��
    public String queryBaoZhiQiList(Map map);

    //�������ˮ
    public String outIngWaterList(Map map);

    //���̨��
    public String InvtyStandList(Map map);

    public String selectInvty(Map map);//��ѯ���д����Ϣ  ���鷵��ǰ��

    //�ִ�����ѯ
    public String selectAvalQty(Map map);//

    //�շ�����ܱ�
    public String selectTSummary(Map map);//

    //--------hj---�����ѯ------------------------------------------------------------
    String queryInvtyTabList(Map map);

    //<!--  ���ݲֿ� ������� ������ ����>0  ������-->
    List<InvtyTab> selectInvtyTabByBatNum(InvtyTab invtyTab);

    //  ���������
    public String outIntoWhsTyp(Map map);

    //ʧЧ����ά��
    String queryInvldtnDtList(Map map);

    String selectNtChkNoList(Map map);

    public String uploadFileAddDb(MultipartFile file);
}
