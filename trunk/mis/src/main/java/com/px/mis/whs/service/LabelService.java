package com.px.mis.whs.service;

import java.util.Map;

import com.px.mis.whs.entity.LabelSteup;
import com.px.mis.whs.entity.LabelTemplateRelation;
import com.px.mis.whs.entity.LabelTemplates;

public interface LabelService {
    // LabelTemplateRelation 模板对应表
    // LabelSteup 标签设置
    // LabelTemplates 打印模板

    /**
     * 打印模板新增
     */
    public String insertLabelTemplates(LabelTemplates labelTemplates);

    /**
     * 打印模板修改
     */
    public String updateLabelTemplates(LabelTemplates labelTemplates);

    /**
     * 打印模板删除
     */
    public String deleteLabelTemplates(String string);

    /**
     * 模板对应表 新增
     */
    public String insertLabelTemplateRelation(LabelTemplateRelation labelTemplateRelation);

    /**
     * 模板对应表 修改
     */
    public String updateLabelTemplateRelation(LabelTemplateRelation labelTemplateRelation);

    /**
     * 模板对应表 删除
     */
    public String deleteLabelTemplateRelation(String string);

    /**
     * 标签设置 新增
     */
    public String insertLabelSteup(LabelSteup labelSteup);

    /**
     * 修改标签设置
     */
    public String updateLabelSteup(LabelSteup labelSteup);

    /**
     * 删除 标签设置
     */
    public String deleteLabelSteup(String string);

    /**
     * 打印模板查询
     */
    public String queryListLabelTemplates(LabelTemplates labelTemplates, Integer pageNo, Integer pageSize);

    /**
     * 模板对应表查询
     */
    public String queryListLabelTemplateRelation(LabelTemplateRelation labelTemplateRelation, int pageNo, int pageSize);

    /**
     * 标签设置查询
     */
    public String queryListLabelSteup(Map<String, Object> map);

}
