package com.px.mis.ec.entity;

public class EcExpress {
	//电子面单签约公司
	private int id ;
	private String platId;//平台编号
	private String providerCode;//承运商编码
	private int providerId;//承运商id
	private String providerName;//承运商名称
	private String providerType;//承运商类型
	private String branchCode;//网点编码
	private String branchName;//网点名称
	private int amount;//单量余额
	private String provinceId;
	private String provinceName;
	private String cityId;
	private String cityName;
	private String countryId;
	private String countryName;
	private String countrysideId;
	private String countrysideName;
	private String address;
	//private String whsCode;//仓库编码
	//private String whsName;//
	
	private String platName;//平台名称
	
	
	public String getPlatName() {
		return platName;
	}
	public void setPlatName(String platName) {
		this.platName = platName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPlatId() {
		return platId;
	}
	public void setPlatId(String platId) {
		this.platId = platId;
	}
	public String getProviderCode() {
		return providerCode;
	}
	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}
	public int getProviderId() {
		return providerId;
	}
	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public String getProviderType() {
		return providerType;
	}
	public void setProviderType(String providerType) {
		this.providerType = providerType;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getCountrysideId() {
		return countrysideId;
	}
	public void setCountrysideId(String countrysideId) {
		this.countrysideId = countrysideId;
	}
	public String getCountrysideName() {
		return countrysideName;
	}
	public void setCountrysideName(String countrysideName) {
		this.countrysideName = countrysideName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

}
