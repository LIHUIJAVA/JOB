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
    // 标签设置
    @Autowired
    LabelsteupDAO labelsteupDAO;
    // 模板对应表
    @Autowired
    LabeltemplaterelationDAO labeltemplaterelationDAO;
    // 打印模板
    @Autowired
    LabeltemplatesDAO labeltemplatesDAO;

    @Override
    public String insertLabelTemplates(LabelTemplates labelTemplates) {
        String resp = "";
        // 查询是否有对应模板号
        if (labeltemplatesDAO.selectByTemplateId(labelTemplates, null, null).size() == 0) {
            // 插入
            labeltemplatesDAO.insert(labelTemplates);
            try {
                resp = BaseJson.returnRespObj("whs/label/insertLabelTemplates", true, "新增成功", labelTemplates);
            } catch (IOException e) {


            }
        } else {
            try {
                resp = BaseJson.returnRespObj("whs/label/insertLabelTemplates", false, "单号重复", null);
            } catch (IOException e) {


            }
        }
        return resp;
    }

    @Override
    public String updateLabelTemplates(LabelTemplates labelTemplates) {
        String resp = "";
        // 设置id为空
        labelTemplates.setTemplateId(null);
        // 更改模板
        if (labeltemplatesDAO.updateByPrimaryKeySelective(labelTemplates) == 1) {
            try {
                resp = BaseJson.returnRespObj("whs/label/updateLabelTemplates", true, "修改成功", labelTemplates);
            } catch (IOException e) {


            }
            return resp;
        }
        try {
            resp = BaseJson.returnRespObj("whs/label/updateLabelTemplates", false, "修改失败", null);
        } catch (IOException e) {


        }

        return resp;
    }

    /**
     * id放入list
     *
     * @param id id(多个已逗号分隔)
     * @return List集合
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
        // 删除模板
        if (labeltemplatesDAO.deleteByPrimaryKey(list) == 0) {
            try {
                resp = BaseJson.returnRespObj("whs/label/updateLabelTemplates", false, "删除失败", null);
            } catch (IOException e) {


            }
            return resp;
        }
        try {
            resp = BaseJson.returnRespObj("whs/label/updateLabelTemplates", true, "删除成功", null);
        } catch (IOException e) {


        }

        return resp;
    }

    @Override
    public String insertLabelTemplateRelation(LabelTemplateRelation labelTemplateRelation) {
        String resp = "";
        // 模板编码
        Optional.ofNullable(labelTemplateRelation.getTemplateId()).orElseThrow(() -> new RuntimeException("模板编码，必填"));
        // 平台编码
        Optional.ofNullable(labelTemplateRelation.getPlatformId()).orElseThrow(() -> new RuntimeException("平台编码，必填"));
        // 仓库编码
        Optional.ofNullable(labelTemplateRelation.getWarehouseId()).orElseThrow(() -> new RuntimeException("仓库编码，必填"));
        // 查询是否有对应模板号
        if (labeltemplaterelationDAO.selectByPrimaryId(labelTemplateRelation, null, null).size() == 0) {
            // 插入
            labeltemplaterelationDAO.insert(labelTemplateRelation);
            try {
                resp = BaseJson.returnRespObj("whs/label/insertlabeLtemplaterelation", true, "新增成功",
                        labelTemplateRelation);
            } catch (IOException e) {


            }
        } else {
            try {
                resp = BaseJson.returnRespObj("whs/label/insertlabeLtemplaterelation", false, "单号重复", null);
            } catch (IOException e) {


            }
        }
        return resp;
    }

    @Override
    public String updateLabelTemplateRelation(LabelTemplateRelation labelTemplateRelation) {
        String resp = "";

        // 模板编码
        Optional.ofNullable(labelTemplateRelation.getTemplateId()).orElseThrow(() -> new RuntimeException("模板编码，必填"));
        // 平台编码
        Optional.ofNullable(labelTemplateRelation.getPlatformId()).orElseThrow(() -> new RuntimeException("平台编码，必填"));
        // 仓库编码
        Optional.ofNullable(labelTemplateRelation.getWarehouseId()).orElseThrow(() -> new RuntimeException("仓库编码，必填"));

        // 更改模板对应表
        if (labeltemplaterelationDAO.updateByPrimaryKeySelective(labelTemplateRelation) == 1) {
            try {
                resp = BaseJson.returnRespObj("whs/label/updateLabelTemplates", true, "修改成功", labelTemplateRelation);
            } catch (IOException e) {


            }
            return resp;

        }
        try {
            resp = BaseJson.returnRespObj("whs/label/updateLabelTemplates", false, "修改失败", null);
        } catch (IOException e) {


        }

        return resp;
    }

    @Override
    public String deleteLabelTemplateRelation(String string) {
        String resp = "";

        List<String> list = getList(string);
        // 删除模板
        if (labeltemplaterelationDAO.deleteByPrimaryKey(list) == 0) {
            try {
                resp = BaseJson.returnRespObj("whs/label/deleteLabelTemplateRelation", false, "删除失败", null);
            } catch (IOException e) {


            }
            return resp;
        }
        try {
            resp = BaseJson.returnRespObj("whs/label/deleteLabelTemplateRelation", true, "删除成功", null);
        } catch (IOException e) {


        }

        return resp;
    }

    // 标签设置 新增
    @Override
    public String insertLabelSteup(LabelSteup labelSteup) {
        String resp = "";
        // 模板编码
        String templateId = labelSteup.getTemplateId();
        // 标签编码
        String labelId = labelSteup.getLabelId();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("templateId", templateId);
        map.put("labelId", labelId);

        Optional.ofNullable(templateId).orElseThrow(() -> new RuntimeException("模板编码，必填"));
        Optional.ofNullable(labelId).orElseThrow(() -> new RuntimeException("标签编码，必填"));
        // 查询是否有对应模板号
        if (labelsteupDAO.selectByPrimaryQuery(map).size() == 0) {
            // 插入
            labelsteupDAO.insert(labelSteup);
            try {
                resp = BaseJson.returnRespObj("whs/label/insertLabelSteup", true, "新增成功", labelSteup);
            } catch (IOException e) {


            }
        } else {
            try {
                resp = BaseJson.returnRespObj("whs/label/insertLabelSteup", false, "单号重复", null);
            } catch (IOException e) {


            }
        }
        return resp;
    }

    // 修改标签设置
    @Override
    public String updateLabelSteup(LabelSteup labelSteup) {
        String resp = "";
        // 修改标签设置
        if (labelsteupDAO.updateByPrimaryKeySelective(labelSteup) == 0) {
            try {
                resp = BaseJson.returnRespObj("whs/label/updateLabelSteup", false, "修改失败", null);
            } catch (IOException e) {


            }
            return resp;
        }
        try {
            resp = BaseJson.returnRespObj("whs/label/updateLabelSteup", true, "修改成功", labelSteup);
        } catch (IOException e) {


        }

        return resp;
    }

    @Override
    public String deleteLabelSteup(String string) {
        String resp = "";

        List<String> list = getList(string);
        // 删除模板
        if (labelsteupDAO.deleteByPrimaryKey(list) == 0) {
            try {
                resp = BaseJson.returnRespObj("whs/label/updateLabelTemplates", false, "删除失败", null);
            } catch (IOException e) {


            }
            return resp;
        }
        try {
            resp = BaseJson.returnRespObj("whs/label/updateLabelTemplates", true, "删除成功", null);
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
            resp = BaseJson.returnRespList("whs/label/queryListLabelTemplates", true, "查询成功！", count, pageNo, pageSize,
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
            resp = BaseJson.returnRespList("whs/label/queryListLabelTemplates", true, "查询成功！", count, pageNo + 1,
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
            resp = BaseJson.returnRespList("whs/label/queryListLabelTemplates", true, "查询成功！", count, pageNo, pageSize,
                    listNum, pages, steups);
        } catch (IOException e) {


        }

        return resp;
    }

}
