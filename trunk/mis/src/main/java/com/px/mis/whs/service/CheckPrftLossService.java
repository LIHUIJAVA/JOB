package com.px.mis.whs.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.whs.entity.CheckPrftLoss;
import com.px.mis.whs.entity.CheckPrftLossSubTab;


public interface CheckPrftLossService {

    //�����̵㵥
    public String insertCheckSnglLoss(String userId, CheckPrftLoss cSngl, List<CheckPrftLossSubTab> cSubTabList,String loginTime);

    //�޸��̵㵥
    public String updateCheckSnglLoss(CheckPrftLoss cSngl, List<CheckPrftLossSubTab> cSubTabList);

    //ɾ���̵㵥
    //����ɾ
    public String deleteAllCheckSnglLoss(String checkFormNum);

    //�򵥲� �̵㵥
    public String query(String checkFormNum);

    public String queryList(Map map);

    //���
    public String updateCSnglLossChk(String userId, String jsonBody, String userName, String formDate);

    //����
    public String updateCSnglLossNoChk(String userId, String jsonBody);

    //��ӡ
    public String queryListDaYin(Map map);

    public String uploadFileAddDb(MultipartFile file);

}
