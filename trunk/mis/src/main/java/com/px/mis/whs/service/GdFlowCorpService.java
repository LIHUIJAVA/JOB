package com.px.mis.whs.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.whs.entity.GdFlowCorp;

public interface GdFlowCorpService {

    //����������˾
    public ObjectNode insertGdFlowCorp(GdFlowCorp record);

    //�޸�������˾
    public ObjectNode updateGdFlowCorp(GdFlowCorp record);

    //ɾ��������˾
    public ObjectNode deleteGdFlowCorp(String gdFlowEncd);

    public String deleteGFlowCorpList(String gdFlowEncd);

    //�򵥲� ������˾
    public GdFlowCorp selectGdFlowCorp(String gdFlowEncd);

    public List<GdFlowCorp> selectGdFlowCorpList(String gdFlowEncd);

    //��ҳ��
    public String queryList(Map map);

    //��ӡ
    public String queryListDaYin(Map map);

    public String uploadFileAddDb(MultipartFile file);

}
