package com.px.mis.whs.entity;

//仓库用户
public class UserWhs {
    private Integer id;
    /**
     * 用户编码
     */
    private String accNum;
    /**
     * 用户编码
     */
    private String userName;

    /**
     * 总仓库编码
     */

    private String realWhs;

    /**
     * 总仓库名称
     */

    private String realNm;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccNum() {
        return accNum;
    }

    public void setAccNum(String accNum) {
        this.accNum = accNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealWhs() {
        return realWhs;
    }

    public void setRealWhs(String realWhs) {
        this.realWhs = realWhs;
    }

    public String getRealNm() {
        return realNm;
    }

    public void setRealNm(String realNm) {
        this.realNm = realNm;
    }

}