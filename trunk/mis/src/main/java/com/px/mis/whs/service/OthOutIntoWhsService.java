package com.px.mis.whs.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.MovBitTab;
import com.px.mis.whs.entity.OthOutIntoWhs;
import com.px.mis.whs.entity.OthOutIntoWhsSubTab;


public interface OthOutIntoWhsService {

    //�������������
    public String insertOthOutIntoWhs(String userId, OthOutIntoWhs outIntoWhs, List<OthOutIntoWhsSubTab> oList,String loginTime);

    //�޸����������
    public String updateOthOutIntoWhs(OthOutIntoWhs outIntoWhs, List<OthOutIntoWhsSubTab> oList);

    //ɾ�����������
    public String deleteAllOthOutIntoWhs(String formNum);

    //�򵥲� ��װ��ж��
    public String query(String formNum);

    public String queryMovBitTab(String formNum, String eqwe, String qeq);

    public String queryList(Map map);

    //���
    public String updateOutInWhsChk(String userId, String jsonBody,String loginTime);

    //����
    public String updateOutInWhsNoChk(String userId, String jsonBody);

    //��ӡ
    public String queryListDaYin(Map map);

    //PDA ��ⵥ  
    //(��ѯ)  ���
    public String selectOINChkr(String whsEncd);

    //��ѯ  ����
    public String selectOutChkr(String whsEncd);

    //�ش�����  ָ����λ ��ⵥ
    public String uInvMov(List<InvtyTab> invtyTab, List<OthOutIntoWhsSubTab> othOutIntoWhsSubTab,
                          List<MovBitTab> movBitTab, OthOutIntoWhs othOutIntoWhs);

    //PDA ���ⵥ
    public String uOutvMov(List<InvtyTab> invtyTab, List<OthOutIntoWhsSubTab> othOutIntoWhsSubTab,
                           List<MovBitTab> movBitTab, OthOutIntoWhs othOutIntoWhs);

    public String updateMovbitTab(List<MovBitTab> movBitTab);//�޸���λ������

    public String uploadFileAddDb(MultipartFile file);

    public String uploadFileAddDbU8(MultipartFile file);

    //������λ��
    public String insertInvtyGdsBitList(List<MovBitTab> oList, String orderNum,  String  serialNum);

    //ɾ��
    public String deleteInvtyGdsBitList(String num, String denji, String list);

    //��ѯ
    public String queryInvtyGdsBitList(String json);

    //����
    public String uploadInvtyGdsBitList(List<MovBitTab> xinlist, List<MovBitTab> gailist, String orderNum,  String  serialNum);

    // ��������
    public String wholeSingleLianZha(String formNum, String formTypEncd);
    // ���������λ
    String allInvtyGdsBitList( String  formNum );
}
