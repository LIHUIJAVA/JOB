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
     * 操作人id
     */
    @JsonProperty("操作人ID")
    private String accNum;

    /**
     * 操作人IP
     */
    @JsonProperty("操作人IP")
    private String accNumIp;

    /**
     * 操作名称
     */
    @JsonProperty("操作名称")
    private String operationName;

    /**
     * 操作模块
     */
    @JsonProperty("操作模块")
    private String operationModule;

    /**
     * 操作时间
     */
    @JsonProperty("操作时间")
    private String operatingTime;

    /**
     * 操作状态
     */
    @JsonProperty("操作状态")
    private String operatingState;
    /**
     * 操作结果
     */
    @JsonProperty("操作结果")
    private String operatingResults;
    /**
     * 账套登陆时间
     */
    @JsonProperty("账套登陆时间")
    private String accountLandingTime;

    /**
     * 操作账套
     */
    @JsonProperty("操作账套")
    private String operatingAccount;

    /**
     * 操作URL
     */
    @JsonProperty("操作URL")
    private String operatingUrl;

    /**
     * 备注
     */
    @JsonProperty("备注")
    private String remark;

    public MisLog() {

    }

    /**
     * @param operationName    操作类型名称
     * @param operationModule  操作模块名称
     * @param remark           备注
     * @param json             请求数据
     * @param request          http请求
     * @param operatingState   操作状态
     * @param operatingResults 操作结果
     */
    public MisLog(String operationName, String operationModule, String remark, String json, HttpServletRequest request,
                  String operatingState, String operatingResults) {

        try {
            this.remark = remark;
            this.operationName = operationName;
            this.operationModule = operationModule;
            String accNum = BaseJson.getReqHead(json).get("accNum").asText();
            String loginTime = BaseJson.getReqHead(json).get("loginTime").asText();
            Optional.ofNullable(accNum).orElseThrow(() -> new RuntimeException("登录失效，请重新登录"));
            Optional.ofNullable(loginTime).orElseThrow(() -> new RuntimeException("登录失效，请重新登录"));
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
            Optional.ofNullable(accNum).orElseThrow(() -> new RuntimeException("登录失效，请重新登录"));
            Optional.ofNullable(loginTime).orElseThrow(() -> new RuntimeException("登录失效，请重新登录"));
            this.accNum = accNum;
            this.accountLandingTime = loginTime;
            int serverport = request.getLocalPort();
            this.accNumIp = request.getRemoteAddr();
            this.operatingTime = LocalDateTime.now().toString();
            this.operatingAccount = serverport + "";
            this.operatingUrl = request.getRequestURI();
            this.operatingState = "失败";
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
            Optional.ofNullable(accNum).orElseThrow(() -> new RuntimeException("登录失效，请重新登录"));
            Optional.ofNullable(loginTime).orElseThrow(() -> new RuntimeException("登录失效，请重新登录"));
            this.accNum = accNum;
            this.accountLandingTime = loginTime;
            int serverport = request.getLocalPort();
            this.accNumIp = request.getRemoteAddr();
            this.operatingTime = LocalDateTime.now().toString();
            this.operatingAccount = serverport + "";
            this.operatingUrl = request.getRequestURI();
            this.operatingState = "成功";
            this.operatingResults = "成功";
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
////		Optional.ofNullable(accNum).orElseThrow(() -> new RuntimeException("登录失效，请重新登录"));
////		Optional.ofNullable(loginTime).orElseThrow(() -> new RuntimeException("登录失效，请重新登录"));
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