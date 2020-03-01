package com.px.mis.system.entity;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.px.mis.util.BaseJson;

/**
 * mis_log
 *
 * @author
 */
public class MisLog implements Serializable {
    /**
     * id
     */
    @JsonIgnore
    private Long id;

    /**
     * ������id
     */
    @JsonProperty("������ID")
    private String accNum;

    /**
     * ������IP
     */
    @JsonProperty("������IP")
    private String accNumIp;

    /**
     * ��������
     */
    @JsonProperty("��������")
    private String operationName;

    /**
     * ����ģ��
     */
    @JsonProperty("����ģ��")
    private String operationModule;

    /**
     * ����ʱ��
     */
    @JsonProperty("����ʱ��")
    private String operatingTime;

    /**
     * ����״̬
     */
    @JsonProperty("����״̬")
    private String operatingState;
    /**
     * �������
     */
    @JsonProperty("�������")
    private String operatingResults;
    /**
     * ���׵�½ʱ��
     */
    @JsonProperty("���׵�½ʱ��")
    private String accountLandingTime;

    /**
     * �������ץ�
     */
    @JsonProperty("��������")
    private String operatingAccount;

    /**
     * ����URL
     */
    @JsonProperty("����URL")
    private String operatingUrl;

    /**
     * ��ע
     */
    @JsonProperty("��ע")
    private String remark;

    public MisLog() {

    }

    /**
     * @param operationName    ������������
     * @param operationModule  ����ģ������
     * @param remark           ��ע
     * @param json             ��������
     * @param request          http����
     * @param operatingState   ����״̬
     * @param operatingResults �������
     */
    public MisLog(String operationName, String operationModule, String remark, String json, HttpServletRequest request,
                  String operatingState, String operatingResults) {

        try {
            this.remark = remark;
            this.operationName = operationName;
            this.operationModule = operationModule;
            String accNum = BaseJson.getReqHead(json).get("accNum").asText();
            String loginTime = BaseJson.getReqHead(json).get("loginTime").asText();
            Optional.ofNullable(accNum).orElseThrow(() -> new RuntimeException("��¼ʧЧ�������µ�¼"));
            Optional.ofNullable(loginTime).orElseThrow(() -> new RuntimeException("��¼ʧЧ�������µ�¼"));
            this.accNum = accNum;
            this.accountLandingTime = loginTime;
            int serverport = request.getLocalPort();
            this.accNumIp = request.getRemoteAddr();
            this.operatingTime = LocalDateTime.now().toString();
            this.operatingAccount = serverport + "";
            this.operatingUrl = request.getRequestURI();
            this.operatingState = operatingState;
            this.operatingResults = operatingResults;
        } catch (IOException e) {
        }
    }

    public MisLog(String operationName, String operationModule, String remark, String json, HttpServletRequest request,
                  Exception e) {

        try {
            this.remark = remark;
            this.operationName = operationName;
            this.operationModule = operationModule;
            String accNum = BaseJson.getReqHead(json).get("accNum").asText();
            String loginTime = BaseJson.getReqHead(json).get("loginTime").asText();
            Optional.ofNullable(accNum).orElseThrow(() -> new RuntimeException("��¼ʧЧ�������µ�¼"));
            Optional.ofNullable(loginTime).orElseThrow(() -> new RuntimeException("��¼ʧЧ�������µ�¼"));
            this.accNum = accNum;
            this.accountLandingTime = loginTime;
            int serverport = request.getLocalPort();
            this.accNumIp = request.getRemoteAddr();
            this.operatingTime = LocalDateTime.now().toString();
            this.operatingAccount = serverport + "";
            this.operatingUrl = request.getRequestURI();
            this.operatingState = "ʧ��";
            this.operatingResults = (e.getMessage() != null && e.getMessage().length() > 200)
                    ? e.getMessage().substring(0, 200)
                    : e.getMessage();
        } catch (IOException es) {
        }

    }

    public MisLog(String operationName, String operationModule, String remark, String json,
                  HttpServletRequest request) {
        try {
            this.remark = remark;
            this.operationName = operationName;
            this.operationModule = operationModule;
            String accNum = BaseJson.getReqHead(json).get("accNum").asText();
            String loginTime = BaseJson.getReqHead(json).get("loginTime").asText();
            Optional.ofNullable(accNum).orElseThrow(() -> new RuntimeException("��¼ʧЧ�������µ�¼"));
            Optional.ofNullable(loginTime).orElseThrow(() -> new RuntimeException("��¼ʧЧ�������µ�¼"));
            this.accNum = accNum;
            this.accountLandingTime = loginTime;
            int serverport = request.getLocalPort();
            this.accNumIp = request.getRemoteAddr();
            this.operatingTime = LocalDateTime.now().toString();
            this.operatingAccount = serverport + "";
            this.operatingUrl = request.getRequestURI();
            this.operatingState = "�ɹ�";
            this.operatingResults = "�ɹ�";
        } catch (IOException e) {
        }
    }

//	public MisLog(String operationName, String operationModule, String remark, String json,
//			HttpServletRequest request) {
//
//		this.remark = remark;
//		this.operationName = operationName;
//		this.operationModule = operationModule;
////		String accNum = JSON.parseObject(json).getJSONObject("reqHead").getString("accNum");
////		String loginTime = JSON.parseObject(json).getJSONObject("reqHead").getString("loginTime");
////		Optional.ofNullable(accNum).orElseThrow(() -> new RuntimeException("��¼ʧЧ�������µ�¼"));
////		Optional.ofNullable(loginTime).orElseThrow(() -> new RuntimeException("��¼ʧЧ�������µ�¼"));
////		this.accNum = accNum;
////		this.accountLandingTime = loginTime;
//		int serverport = request.getLocalPort();
//		this.accNumIp = request.getRemoteAddr();
//		this.operatingTime = LocalDateTime.now().toString();
//		this.operatingAccount = serverport + "";
//		this.operatingUrl = request.getRequestURI();
//
//	}

    private static final long serialVersionUID = 1L;

    public MisLog(String accNum, String accNumIp, String operationName, String operationModule, String operatingTime,
                  String operatingState, String operatingResults, String accountLandingTime, String operatingAccount,
                  String operatingUrl, String remark) {
        this.accNum = accNum;
        this.accNumIp = accNumIp;
        this.operationName = operationName;
        this.operationModule = operationModule;
        this.operatingTime = operatingTime;
        this.operatingState = operatingState;
        this.operatingResults = operatingResults;
        this.accountLandingTime = accountLandingTime;
        this.operatingAccount = operatingAccount;
        this.operatingUrl = operatingUrl;
        this.remark = remark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccNum() {
        return accNum;
    }

    public void setAccNum(String accNum) {
        this.accNum = accNum;
    }

    public String getAccNumIp() {
        return accNumIp;
    }

    public void setAccNumIp(String accNumIp) {
        this.accNumIp = accNumIp;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getOperationModule() {
        return operationModule;
    }

    public void setOperationModule(String operationModule) {
        this.operationModule = operationModule;
    }

    public String getOperatingTime() {
        return operatingTime;
    }

    public void setOperatingTime(String operatingTime) {
        this.operatingTime = operatingTime;
    }

    public final String getOperatingState() {
        return operatingState;
    }

    public final void setOperatingState(String operatingState) {
        this.operatingState = operatingState;
    }

    public final String getOperatingResults() {
        return operatingResults;
    }

    public final void setOperatingResults(String operatingResults) {
        this.operatingResults = operatingResults;
    }

    public String getAccountLandingTime() {
        return accountLandingTime;
    }

    public void setAccountLandingTime(String accountLandingTime) {
        this.accountLandingTime = accountLandingTime;
    }

    public String getOperatingAccount() {
        return operatingAccount;
    }

    public void setOperatingAccount(String operatingAccount) {
        this.operatingAccount = operatingAccount;
    }

    public String getOperatingUrl() {
        return operatingUrl;
    }

    public void setOperatingUrl(String operatingUrl) {
        this.operatingUrl = operatingUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}