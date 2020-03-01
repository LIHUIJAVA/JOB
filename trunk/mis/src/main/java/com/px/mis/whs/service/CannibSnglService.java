package com.px.mis.whs.service;


import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.whs.entity.CannibSngl;
import com.px.mis.whs.entity.CannibSnglSubTab;


/**
 * @author zhangxiaoyu
 * @version ����ʱ�䣺2018��11��15�� ����2:18:39
 * @ClassName ������
 * @Description ������
 */
public interface CannibSnglService {

    //��ӵ�����
    public String insertCSngl(String userId, CannibSngl cSngl,
                              List<CannibSnglSubTab> cList,String loginTime);

    //�޸ĵ�����
    public String updateCSngl(CannibSngl cSngl, List<CannibSnglSubTab> cList);

    //ɾ�������� ��ɾ
    public String deleteAllCSngl(String formNum);

    //�򵥲�  ������
    public String query(String formNum);

    public String queryList(Map map);

    //���
    public String updateCSnglChk(String userId, String jsonBody, String userName, String formDate);

    //����
    public String updateCSnglNoChk(String userId, String jsonBody, String userName);

    //��ӡ
    public String queryListDaYin(Map map);

    //PDA ��ѯ
    public String selectCSnglChkr();

    public String updateCSnglSubTab(CannibSnglSubTab cSubTab);//�޸��ӱ�

    public String updateCSngl(CannibSngl cSngl);//�޸�����

    //��ѯ������ִ���  ������
    public String selectInvty(Map map);


    public String uploadFileAddDb(MultipartFile file);

    public String uploadFileAddDbU8(MultipartFile file);
}
