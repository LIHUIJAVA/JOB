package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.ec.dao.PresentRangeListDao;
import com.px.mis.ec.dao.ProActivityDao;
import com.px.mis.ec.dao.ProPlansDao;
import com.px.mis.ec.entity.PresentRange;
import com.px.mis.ec.entity.PresentRangeList;
import com.px.mis.ec.service.PresentRangeListService;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

@Service
@Transactional
public class PresentRangeListServiceImpl implements PresentRangeListService {
    @Autowired
    private PresentRangeListDao presentRangeListDao;
    @Autowired
    private ProPlansDao proPlansDao;
    @Autowired
    private InvtyDocDao invtyDocDao;
    @Autowired
    private ProActivityDao proActDao;

    @Override
    public ObjectNode insertPresentRange(PresentRange presentRange, List<PresentRangeList> cList) {
        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (presentRangeListDao.selectPresentRangeById(presentRange.getPresentRangeEncd()) != null) {
            on.put("isSuccess", false);
            on.put("message", "��Ʒ��Χ����" + presentRange.getPresentRangeEncd() + "�Ѿ����ڣ�����ʧ�ܣ�");
        } else {
            int insertResult = presentRangeListDao.insertPresentRange(presentRange);
            int insertResults = presentRangeListDao.insertPresentRangeList(cList);
            if (insertResult == 1) {
                on.put("isSuccess", true);
                on.put("message", "�����ɹ�");
            } else {
                on.put("isSuccess", false);
                on.put("message", "����ʧ��");
            }
        }
        return on;
    }

    @Override
    public ObjectNode updatePresentRangeListById(PresentRange presentRange, List<PresentRangeList> cList) {
        ObjectNode on = null;
        try {
            on = JacksonUtil.getObjectNode("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> list = proActDao.selectpro(null,null,presentRange.getPresentRangeEncd());
        for (String str : list) {
            if (str == null || str.equals("1")) {
                on.put("isSuccess", false);
                on.put("message", "����ʧ�ܣ����������ˣ�");
                return on;
            }
        }
        if (presentRangeListDao.selectPresentRangeById(presentRange.getPresentRangeEncd()) == null) {
            on.put("isSuccess", false);
            on.put("message", "����ʧ�ܣ���Ʒ��Χ����" + presentRange.getPresentRangeEncd() + "�����ڣ�");
        } /*
           * else if(proPlansDao.selectByNo(presentRangeList.getProPlansNo())==null){
           * on.put("isSuccess", false); on.put("message",
           * "���������ӱ����"+presentRangeList.getProPlansNo()+"�����ڣ�����ʧ�ܣ�"); }
           */
        else {
            int updateResult = presentRangeListDao.updatePresentRangeById(presentRange);
            int delet = presentRangeListDao.deleteByPresentRangeList(presentRange.getPresentRangeEncd());
            int insert = presentRangeListDao.insertPresentRangeList(cList);
            if (updateResult == 1) {
                on.put("isSuccess", true);
                on.put("message", "���³ɹ�");
            } else {
                on.put("isSuccess", false);
                on.put("message", "����ʧ��");
            }
        }
        return on;
    }

    @Override
	public ObjectNode deletePresentRangeListById(String presentRangeEncd) {
		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");

			List<String> list = proActDao.selectpro(null, null, presentRangeEncd);
			for (String str : list) {
				if (str == null || str.equals("1")) {
					on.put("isSuccess", false);
					on.put("message", presentRangeEncd+"ɾ��ʧ�ܣ����������ˣ�");
					return on;
				}
			}

			if (presentRangeListDao.selectPresentRangeById(presentRangeEncd) == null) {
				on.put("isSuccess", false);
				on.put("message", "ɾ��ʧ�ܣ�" + presentRangeEncd + "�����ڣ�");
			} else {
				int deleteResult = presentRangeListDao.deletePresentRangeById(presentRangeEncd);
				int deleteResults = presentRangeListDao.deleteByPresentRangeList(presentRangeEncd);
				if (deleteResult == 1) {
					on.put("isSuccess", true);
					on.put("message", presentRangeEncd+"ɾ���ɹ�");
				}else {
					on.put("isSuccess", false);
					on.put("message", "ɾ��ʧ��");
				}
			}
		} catch (Exception e) {
			on.put("isSuccess", false);
			on.put("message", "ɾ��ʧ��");
		}
		return on;
	}

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public String selectPresentRangeListById(String presentRangeEncd) {
        String resp = "";
        PresentRange presentRange = presentRangeListDao.selectPresentRangeById(presentRangeEncd);
        List<PresentRangeList> presentRangeList = presentRangeListDao.selectByPresentRangeList(presentRangeEncd);

        try {

            resp = BaseJson.returnRespObjList("ec/presentMode/selectPresentModeById", true, "��ѯ�ɹ���", presentRange, presentRangeList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public String selectPresentRangeListList(Map map) {
        String resp = "";
        List<PresentRange> list = presentRangeListDao.selectPresentRangesd(map);
        int count = presentRangeListDao.selectPresentRangeCount(map);
        int listNum = 0;
        if (list != null) {
            listNum = list.size();
        }
        try {
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            int pages = (count + pageSize - 1) / pageSize;
            resp = BaseJson.returnRespList("/ec/presentRangeList/selectPresentRangeList", true, "��ѯ�ɹ���", count, pageNo, pageSize, listNum, pages, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp;
    }

}
