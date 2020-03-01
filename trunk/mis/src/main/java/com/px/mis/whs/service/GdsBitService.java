package com.px.mis.whs.service;


import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.whs.entity.GdsBit;
import com.px.mis.whs.entity.GdsBitTyp;

//��λ����
public interface GdsBitService {

    //������λ����
    public ObjectNode insertGdsBit(GdsBit record);

    //�޸Ļ�λ����
    public ObjectNode updateGdsBit(GdsBit record);

    //ɾ����λ����
    public ObjectNode deleteGdsBit(String gdsBitEncd);

    public String deleteGdsBitList(String gdsBitEncd);

    //�򵥲�  ��λ����
    public GdsBit selectGdsBit(String gdsBitEncd);

    public List<GdsBit> selectGdsBitList(String gdsBitEncd);

    //��ҳ��
    public String queryList(Map map);

    //��ѯ���л�λ����
    public String selectgTypList();

    //��ӡ
    public String queryListDaYin(Map map);

    public String uploadFileAddDb(MultipartFile file);
}
