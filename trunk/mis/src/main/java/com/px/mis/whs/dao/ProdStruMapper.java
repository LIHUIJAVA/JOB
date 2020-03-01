package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.px.mis.whs.entity.ProdStru;
import com.px.mis.whs.entity.ProdStruMap;
import com.px.mis.whs.entity.ProdStruSubTab;

public interface ProdStruMapper {

    //1.������Ʒ�ṹ
    public int insertProdStru(ProdStru record);

    public int insertProdStruSubTab(List<ProdStruSubTab> pList); //�����ӱ�

    //��Ʒ�ṹ
    public int exIinsertProdStru(ProdStru record);

    //    public int exInsertProdStruSubTab(List<ProdStruSubTab> pList); //�����ӱ�
    //2.�޸Ĳ�Ʒ�ṹ
    public int updateProdStru(ProdStru record);

    //3.ɾ����Ʒ�ṹ   ��ɾ����ɾ��
    public int deleteProdStruSubTab(String ordrNumMom);

    public int deleteAllProdStru(List<String> ordrNum);

    //4.�򵥲�  ��Ʒ�ṹ
    public List<ProdStru> selectProdStruList(List<ProdStru> cList);

    //�򵥲�  ��Ʒ�ṹ
    public List<ProdStru> selectMomEncdAmbDisambSngl(Map map);

    public int countMomEncdAmbDisambSngl(Map map);

    public ProdStru selectMomEncd(@Param("momEncd") String Momencd);

    public ProdStru selectProdStru(@Param("ordrNum") String ordrNum);

    public List<ProdStruSubTab> selectProdStruSubTabList(@Param("ordrNum") String ordrNum);

    //��ҳ��
    public List<ProdStru> selectList(Map map);

    public int selectCount(Map map);

    //��ѯ ��ȡ��ĸ���Ĳ�Ʒ�ṹ��Ϣ
    public List<ProdStru> selectProdStruByMom(@Param("ordrNum") String ordrNum);


    //���
    public int updatePStruChk(List<ProdStru> cList);

    //����
    public int updatePStruNoChk(List<ProdStru> cList);

    //��ӡ
    public List<ProdStruMap> queryListPrint(Map map);
}