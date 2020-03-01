package com.px.mis.whs.service;

import java.util.List;
import java.util.Map;

import com.px.mis.whs.entity.MergePickSngl;
import com.px.mis.whs.entity.MovBitTab;
import com.px.mis.whs.entity.PickSnglSubTab;
import com.px.mis.whs.entity.PickSnglTab;

public interface PickSnglService {

    //��ѯ���۵�
    public String selectSellById(Map sellSnglId) throws Exception;

    //��Ӽ����(��Ҫ��  �Ͳ�ѯ���۵��ϲ���)
//	public String insertPickSngl(PickSnglTab pickTab,List<PickSnglSubTab> pickList);
    public String insertPickSngl(String pickTab, String pickList, String userName,String loginTime);

    //��������б���ʾ
    public String queryAllList(Map map);

    //ͨ�����������ݺŲ�ѯ�ӱ���Ϣ
    public String selectPSubTabById(String pickSnglNum);

    //�������-���б���ʾ
    public String queryList(Map map);

    //�ϲ������
    public String selectPSubTab(String userId, String pickSnglNum, String whsEncd);

    //ɾ����� �ϲ���
    public String deleteMerPickSngl(List<MergePickSngl> mSnglList);

    public String deletePickSngl(String mSnglList);

    //PDA  ��ʾ���м�����б�-
    public String selectAllMerge(String whsEncd);

    public String selectAllById(String pickSnglNum);

    //PDA �ش��ӿ�  �����ɱ�ʶ  ������ʱ��
    public String updatePTabPda(PickSnglTab pSnglTab, List<MovBitTab> list);

    //pc �ش��ӿ�  �����ɱ�ʶ  ������ʱ��
    public String updatePTabPC(PickSnglTab pSnglTab);

    //��ӡ
    public String queryListPrint(Map map);
}
