package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.GdsBit;
import com.px.mis.whs.entity.MovBitTab;
import com.px.mis.whs.entity.Regn;
import com.px.mis.whs.entity.WhsDoc;
import com.px.mis.whs.entity.MovBitList;

//��λ����
public interface GdsBitDistionMapper {

    //��ѯ�ֿ⡢���򡢻�λ�������Ϣ
    public List<WhsDoc> selectWDoc();

    public List<Regn> selectRegn(@Param("whsEncd") String whsEncd);

    public List<GdsBit> selectBit(@Param("regnEncd") String regnEncd);

    public List<MovBitTab> selectInvty(@Param("gdsBitEncd") String gdsBitEncd);

    public List<Map> selectInvtyWhs(Map map);

    public int selectInvtyWhsCount(Map map);

    //������λ�嵥
    public int insertMovBitList(List<MovBitList> movBitList);

    //��Ҫ��
    public List<MovBitTab> selectMTab(@Param("whsEncd") String whsEncd);

    //PAD չʾ�б�
    public List<MovBitList> selectMTabList(List<String> list);

    public int updateMBitList(MovBitList movBitList);//�ش���λ�嵥

    //PC����λ
    public int updateMovbit(MovBitTab movBitTab);

    public int updateMovbitTab(List<MovBitTab> movBitTab);//�޸���λ������(����)

    public int updateMovbitTabJia(List<MovBitTab> movBitTab);//�޸���λ������(�ӷ�)

    //��ѯ
    public List<MovBitList> selectMTabListId(List<String> list);

    //ɾ��
    public void deleteMTabList(List<String> list);

    //����
    public List<MovBitList> selectMTabLists(Map<?, ?> map);

    public int selectMTabListsCount(Map<?, ?> map);

    //������ѯ�ֿ�����л�λ
    public List<Map<String, Object>> selectWhsgds(Map map);


}
