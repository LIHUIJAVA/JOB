package com.px.mis.whs.service;


import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.whs.entity.AmbDisambSngl;
import com.px.mis.whs.entity.AmbDisambSnglubTab;


public interface AmbDisambSnglService {

    //������װ��ж��
    public String insertAmbDisambSngl(String userId, AmbDisambSngl aSngl, List<AmbDisambSnglubTab> aList,String loginTime);

    //�޸���װ��ж��
    public String updateAmbDisambSngl(AmbDisambSngl aSngl, List<AmbDisambSnglubTab> aList);

    //ɾ����װ��ж��   ��ɾ
    public String deleteAllAmbDisambSngl(String formNum);

    //�򵥲� ��װ��ж��
    public String query(String formNum);

    public String queryList(Map map);

    //���
    public String updateASnglChk(String userId, String jsonBody, String name, String formDate);

    //����
    public String updateASnglNoChk(String userId, String jsonBody, String name);

    //��ӡ
    public String queryListDaYin(Map map);

    public String uploadFileAddDb(MultipartFile file);

    public String uploadFileAddDbU8(MultipartFile file);

}
