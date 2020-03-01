package com.px.mis.whs.service;


import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.whs.entity.Regn;


//���򵵰�
public interface RegnService {

    //�������򵵰�
    public ObjectNode insertRegn(Regn record);

    //�޸����򵵰�
    public ObjectNode updateRegn(Regn record);

    //ɾ�����򵵰�
    public ObjectNode deleteRegn(String regnEncd);

    public String deleteRegnList(String regnEncd);

    //�򵥲�  ���򵵰�
    public Regn selectRegn(String regnEncd);

    public List<Regn> selectRegnList(String regnEncd);

    //��ҳ��
    public String queryList(Map map);

    //��ӡ
    public String queryListDaYin(Map map);

    //����
    public String uploadFileAddDb(MultipartFile file);

}
