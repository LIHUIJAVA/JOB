package com.px.mis.whs.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.util.BaseJson;
import com.px.mis.whs.dao.LabelsteupDAO;
import com.px.mis.whs.dao.LabeltemplaterelationDAO;
import com.px.mis.whs.dao.LabeltemplatesDAO;
import com.px.mis.whs.entity.LabelSteup;
import com.px.mis.whs.entity.LabelTemplateRelation;
import com.px.mis.whs.entity.LabelTemplates;
import com.px.mis.whs.service.LabelService;

@Service
@Transactional
public class LabelServiceImpl implements LabelService {
    // ��ǩ����
    @Autowired
    LabelsteupDAO labelsteupDAO;
    // ģ���Ӧ��
    @Autowired
    LabeltemplaterelationDAO labeltemplaterelationDAO;
    // ��ӡģ��
    @Autowired
    LabeltemplatesDAO labeltemplatesDAO;

    @Override
    public String insertLabelTemplates(LabelTemplates labelTemplates) {
        String resp = "";
        // ��ѯ�Ƿ��ж�Ӧģ���
        if (labeltemplatesDAO.selectByTemplateId(labelTemplates, null, null).size() == 0) {
            // ����
            labeltemplatesDAO.insert(labelTemplates);
            try {
                resp = BaseJson.returnRespObj("whs/label/insertLabelTemplates", true, "�����ɹ�", labelTemplates);
            } catch (IOException e) {


            }
        } else {
            try {
                resp = BaseJson.returnRespObj("whs/label/insertLabelTemplates", false, "�����ظ�", null);
            } catch (IOException e) {


            }
        }
        return resp;
    }

    @Override
    public String updateLabelTemplates(LabelTemplates labelTemplates) {
        String resp = "";
        // ����idΪ��
        labelTemplates.setTemplateId(null);
        // ����ģ��
        if (labeltemplatesDAO.updateByPrimaryKeySelective(labelTemplates) == 1) {
            try {
                resp = BaseJson.returnRespObj("whs/label/updateLabelTemplates", true, "�޸ĳɹ�", labelTemplates);
            } catch (IOException e) {


            }
            return resp;
        }
        try {
            resp = BaseJson.returnRespObj("whs/label/updateLabelTemplates", false, "�޸�ʧ��", null);
        } catch (IOException e) {


        }

        return resp;
    }

    /**
     * id����list
     *
     * @param id id(����Ѷ��ŷָ�)
     * @return List����
     */
    public List<String> getList(String id) {
        List<String> list = new ArrayList<String>();
        String[] str = id.split(",");
        for (int i = 0; i < str.length; i++) {
            // System.out.println(str[i] + "===str[" + i + "]");
            list.add(str[i]);
        }
        return list;
    }

    @Override
    public String deleteLabelTemplates(String string) {
        String resp = "";

        List<String> list = getList(string);
        // ɾ��ģ��
        if (labeltemplatesDAO.deleteByPrimaryKey(list) == 0) {
            try {
                resp = BaseJson.returnRespObj("whs/label/updateLabelTemplates", false, "ɾ��ʧ��", null);
            } catch (IOException e) {


            }
            return resp;
        }
        try {
            resp = BaseJson.returnRespObj("whs/label/updateLabelTemplates", true, "ɾ���ɹ�", null);
        } catch (IOException e) {


        }

        return resp;
    }

    @Override
    public String insertLabelTemplateRelation(LabelTemplateRelation labelTemplateRelation) {
        String resp = "";
        // ģ�����
        Optional.ofNullable(labelTemplateRelation.getTemplateId()).orElseThrow(() -> new RuntimeException("ģ����룬����"));
        // ƽ̨����
        Optional.ofNullable(labelTemplateRelation.getPlatformId()).orElseThrow(() -> new RuntimeException("ƽ̨���룬����"));
        // �ֿ����
        Optional.ofNullable(labelTemplateRelation.getWarehouseId()).orElseThrow(() -> new RuntimeException("�ֿ���룬����"));
        // ��ѯ�Ƿ��ж�Ӧģ���
        if (labeltemplaterelationDAO.selectByPrimaryId(labelTemplateRelation, null, null).size() == 0) {
            // ����
            labeltemplaterelationDAO.insert(labelTemplateRelation);
            try {
                resp = BaseJson.returnRespObj("whs/label/insertlabeLtemplaterelation", true, "�����ɹ�",
                        labelTemplateRelation);
            } catch (IOException e) {


            }
        } else {
            try {
                resp = BaseJson.returnRespObj("whs/label/insertlabeLtemplaterelation", false, "�����ظ�", null);
            } catch (IOException e) {


            }
        }
        return resp;
    }

    @Override
    public String updateLabelTemplateRelation(LabelTemplateRelation labelTemplateRelation) {
        String resp = "";

        // ģ�����
        Optional.ofNullable(labelTemplateRelation.getTemplateId()).orElseThrow(() -> new RuntimeException("ģ����룬����"));
        // ƽ̨����
        Optional.ofNullable(labelTemplateRelation.getPlatformId()).orElseThrow(() -> new RuntimeException("ƽ̨���룬����"));
        // �ֿ����
        Optional.ofNullable(labelTemplateRelation.getWarehouseId()).orElseThrow(() -> new RuntimeException("�ֿ���룬����"));

        // ����ģ���Ӧ��
        if (labeltemplaterelationDAO.updateByPrimaryKeySelective(labelTemplateRelation) == 1) {
            try {
                resp = BaseJson.returnRespObj("whs/label/updateLabelTemplates", true, "�޸ĳɹ�", labelTemplateRelation);
            } catch (IOException e) {


            }
            return resp;

        }
        try {
            resp = BaseJson.returnRespObj("whs/label/updateLabelTemplates", false, "�޸�ʧ��", null);
        } catch (IOException e) {


        }

        return resp;
    }

    @Override
    public String deleteLabelTemplateRelation(String string) {
        String resp = "";

        List<String> list = getList(string);
        // ɾ��ģ��
        if (labeltemplaterelationDAO.deleteByPrimaryKey(list) == 0) {
            try {
                resp = BaseJson.returnRespObj("whs/label/deleteLabelTemplateRelation", false, "ɾ��ʧ��", null);
            } catch (IOException e) {


            }
            return resp;
        }
        try {
            resp = BaseJson.returnRespObj("whs/label/deleteLabelTemplateRelation", true, "ɾ���ɹ�", null);
        } catch (IOException e) {


        }

        return resp;
    }

    // ��ǩ���� ����
    @Override
    public String insertLabelSteup(LabelSteup labelSteup) {
        String resp = "";
        // ģ�����
        String templateId = labelSteup.getTemplateId();
        // ��ǩ����
        String labelId = labelSteup.getLabelId();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("templateId", templateId);
        map.put("labelId", labelId);

        Optional.ofNullable(templateId).orElseThrow(() -> new RuntimeException("ģ����룬����"));
        Optional.ofNullable(labelId).orElseThrow(() -> new RuntimeException("��ǩ���룬����"));
        // ��ѯ�Ƿ��ж�Ӧģ���
        if (labelsteupDAO.selectByPrimaryQuery(map).size() == 0) {
            // ����
            labelsteupDAO.insert(labelSteup);
            try {
                resp = BaseJson.returnRespObj("whs/label/insertLabelSteup", true, "�����ɹ�", labelSteup);
            } catch (IOException e) {


            }
        } else {
            try {
                resp = BaseJson.returnRespObj("whs/label/insertLabelSteup", false, "�����ظ�", null);
            } catch (IOException e) {


            }
        }
        return resp;
    }

    // �޸ı�ǩ����
    @Override
    public String updateLabelSteup(LabelSteup labelSteup) {
        String resp = "";
        // �޸ı�ǩ����
        if (labelsteupDAO.updateByPrimaryKeySelective(labelSteup) == 0) {
            try {
                resp = BaseJson.returnRespObj("whs/label/updateLabelSteup", false, "�޸�ʧ��", null);
            } catch (IOException e) {


            }
            return resp;
        }
        try {
            resp = BaseJson.returnRespObj("whs/label/updateLabelSteup", true, "�޸ĳɹ�", labelSteup);
        } catch (IOException e) {


        }

        return resp;
    }

    @Override
    public String deleteLabelSteup(String string) {
        String resp = "";

        List<String> list = getList(string);
        // ɾ��ģ��
        if (labelsteupDAO.deleteByPrimaryKey(list) == 0) {
            try {
                resp = BaseJson.returnRespObj("whs/label/updateLabelTemplates", false, "ɾ��ʧ��", null);
            } catch (IOException e) {


            }
            return resp;
        }
        try {
            resp = BaseJson.returnRespObj("whs/label/updateLabelTemplates", true, "ɾ���ɹ�", null);
        } catch (IOException e) {


        }

        return resp;
    }

    @Override
    public String queryListLabelTemplates(LabelTemplates labelTemplates, Integer pageNo, Integer pageSize) {
        String resp = "";

        List<LabelTemplates> labelTemplatesList = labeltemplatesDAO.selectByTemplateId(labelTemplates,
                (pageNo - 1) * pageSize, pageSize);
        int count = labeltemplatesDAO.selectByTemplateCount(labelTemplates);
        int listNum = labelTemplatesList.size();
        int pages = count / pageSize + 1;

        try {
            resp = BaseJson.returnRespList("whs/label/queryListLabelTemplates", true, "��ѯ�ɹ���", count, pageNo, pageSize,
                    listNum, pages, labelTemplatesList);
        } catch (IOException e) {


        }

        return resp;
    }

    @Override
    public String queryListLabelTemplateRelation(LabelTemplateRelation labelTemplateRelation, int pageNo,
                                                 int pageSize) {
        String resp = "";

        List<LabelTemplateRelation> labelTemplates = labeltemplaterelationDAO.selectByPrimaryId(labelTemplateRelation,
                (pageNo - 1) * pageSize, pageSize);
        int count = labeltemplaterelationDAO.selectByTemplateCount(labelTemplateRelation);
        int listNum = labelTemplates.size();
        int pages = count / pageSize + 1;

        try {
            resp = BaseJson.returnRespList("whs/label/queryListLabelTemplates", true, "��ѯ�ɹ���", count, pageNo + 1,
                    pageSize, listNum, pages, labelTemplates);
        } catch (IOException e) {


        }

        return resp;
    }

    @Override
    public String queryListLabelSteup(Map<String, Object> map) {
        String resp = "";
        Integer pageNo = (Integer) map.get("pageNo");
        Integer pageSize = (Integer) map.get("pageSize");
        map.put("index", (pageNo - 1) * pageSize);
        map.put("num", pageSize);
        List<LabelSteup> steups = labelsteupDAO.selectByPrimaryQuery(map);
        Integer count = labelsteupDAO.selectByTemplateCount(map);

        Integer listNum = steups.size();
        Integer pages = count / pageSize;
        if (count % pageSize > 0) {
            pages += 1;
        }
        try {
            resp = BaseJson.returnRespList("whs/label/queryListLabelTemplates", true, "��ѯ�ɹ���", count, pageNo, pageSize,
                    listNum, pages, steups);
        } catch (IOException e) {


        }

        return resp;
    }

}
