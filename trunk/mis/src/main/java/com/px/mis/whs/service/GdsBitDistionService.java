package com.px.mis.whs.service;

import java.util.List;
import java.util.Map;

import com.px.mis.whs.entity.MovBitList;
import com.px.mis.whs.entity.MovBitTab;

//��λ����
public interface GdsBitDistionService {

    //��ѯ�ֿ⡢���򡢻�λ
    public String selectWDoc();

    public String selectRegn(String whsEncd);

    public String selectBit(String regnEncd);

    public String selectInvty(String gdsBitEncd);

    public String selectInvtyWhs(Map map);

    //PC������λ
    public String updateMovbit(List<MovBitList> movBitList, List<MovBitTab> movBitTab);

    //��Ҫ��
    public String selectMTab(String whsEncd);

    //PAD չʾ�б�
    public String selectMTabList(String whsEncd);

    public String updateMBitList(MovBitList movBitList);//�ش���λ�嵥

    //	��λƥ��1.0��
    public String selectMBitList(Map map);

    //PC������λ����
    public String insertMovbitPC(List<MovBitList> movBitList);

    //��λɾ��
    public String deleteMovbit(String ordrNum);

    //������ѯ
    String selectMTabLists(Map map);

    //������ѯ�ֿ�����л�λ
    public String selectWhsgds(Map map);

}
