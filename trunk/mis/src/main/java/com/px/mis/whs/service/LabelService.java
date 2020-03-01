package com.px.mis.whs.service;

import java.util.Map;

import com.px.mis.whs.entity.LabelSteup;
import com.px.mis.whs.entity.LabelTemplateRelation;
import com.px.mis.whs.entity.LabelTemplates;

public interface LabelService {
    // LabelTemplateRelation ģ���Ӧ��
    // LabelSteup ��ǩ����
    // LabelTemplates ��ӡģ��

    /**
     * ��ӡģ������
     */
    public String insertLabelTemplates(LabelTemplates labelTemplates);

    /**
     * ��ӡģ���޸�
     */
    public String updateLabelTemplates(LabelTemplates labelTemplates);

    /**
     * ��ӡģ��ɾ��
     */
    public String deleteLabelTemplates(String string);

    /**
     * ģ���Ӧ�� ����
     */
    public String insertLabelTemplateRelation(LabelTemplateRelation labelTemplateRelation);

    /**
     * ģ���Ӧ�� �޸�
     */
    public String updateLabelTemplateRelation(LabelTemplateRelation labelTemplateRelation);

    /**
     * ģ���Ӧ�� ɾ��
     */
    public String deleteLabelTemplateRelation(String string);

    /**
     * ��ǩ���� ����
     */
    public String insertLabelSteup(LabelSteup labelSteup);

    /**
     * �޸ı�ǩ����
     */
    public String updateLabelSteup(LabelSteup labelSteup);

    /**
     * ɾ�� ��ǩ����
     */
    public String deleteLabelSteup(String string);

    /**
     * ��ӡģ���ѯ
     */
    public String queryListLabelTemplates(LabelTemplates labelTemplates, Integer pageNo, Integer pageSize);

    /**
     * ģ���Ӧ���ѯ
     */
    public String queryListLabelTemplateRelation(LabelTemplateRelation labelTemplateRelation, int pageNo, int pageSize);

    /**
     * ��ǩ���ò�ѯ
     */
    public String queryListLabelSteup(Map<String, Object> map);

}
