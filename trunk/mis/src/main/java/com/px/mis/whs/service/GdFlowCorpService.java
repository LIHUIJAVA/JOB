package com.px.mis.whs.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.whs.entity.GdFlowCorp;

public interface GdFlowCorpService {

    //新增物流公司
    public ObjectNode insertGdFlowCorp(GdFlowCorp record);

    //修改物流公司
    public ObjectNode updateGdFlowCorp(GdFlowCorp record);

    //删除物流公司
    public ObjectNode deleteGdFlowCorp(String gdFlowEncd);

    public String deleteGFlowCorpList(String gdFlowEncd);

    //简单查 物流公司
    public GdFlowCorp selectGdFlowCorp(String gdFlowEncd);

    public List<GdFlowCorp> selectGdFlowCorpList(String gdFlowEncd);

    //分页查
    public String queryList(Map map);

    //打印
    public String queryListDaYin(Map map);

    public String uploadFileAddDb(MultipartFile file);

}
