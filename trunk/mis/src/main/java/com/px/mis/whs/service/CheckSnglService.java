package com.px.mis.whs.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.whs.entity.CheckSngl;
import com.px.mis.whs.entity.CheckSnglSubTab;

public interface CheckSnglService {


    //�����̵㵥
    public String insertCheckSngl(String userId, CheckSngl cSngl, List<CheckSnglSubTab> cSubTabList,String loginTime);

    //�޸��̵㵥
    public String updateCheckSngl(CheckSngl cSngl, List<CheckSnglSubTab> cSubTabList);

    //ɾ���̵㵥
    //����ɾ
    public String deleteAllCheckSngl(String checkFormNum);

    //�򵥲� �̵㵥
    public String query(String checkFormNum);

    public String queryList(Map map);

    //��ӡ
    public String queryListDaYin(Map map);

    //�̵㵥�б�
    //ͨ���ֿ⡢�̵����š����������롢ʧЧ�����ա�����Ϊ��ʱ�Ƿ��̵�
    public String selectCheckSnglList(Map map) throws IOException;

    //���
    public String updateCSnglChk(String userId, String jsonBody, String userName, String formDate);

    //����
    public String updateCSnglNoChk(String userId, String jsonBody);

    //PDA �����̵����
    public String checkSnglList(String whsEncd) throws IOException;

    //pda �����ӱ����
    public String updateCheckTab(CheckSngl cSngl, List<CheckSnglSubTab> cSubTabList) throws IOException;

    //����֮������״̬  ����PC�ν��и���  ��֤����ͬ��
    public String updateStatus(List<CheckSngl> cList);

    public String uploadFileAddDb(MultipartFile file);

    public String uploadFileAddDbU8(MultipartFile file);

    //ͨ���ֿ⡢�̵����š����������롢ʧЧ�����ա�����Ϊ��ʱ�Ƿ��̵�
    public String selectCheckSnglGdsBitList(Map map);
}
