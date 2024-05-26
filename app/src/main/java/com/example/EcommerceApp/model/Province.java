package com.example.EcommerceApp.model;

import java.util.List;

public class Province {
    private int ProvinceID;
    private String ProvinceName;

    public int getProvinceID() {
        return ProvinceID;
    }

    public void setProvinceID(int provinceID) {
        ProvinceID = provinceID;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String provinceName) {
        ProvinceName = provinceName;
    }

    public int getCountryID() {
        return CountryID;
    }

    public void setCountryID(int countryID) {
        CountryID = countryID;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public List<String> getNameExtension() {
        return NameExtension;
    }

    public void setNameExtension(List<String> nameExtension) {
        NameExtension = nameExtension;
    }

    public int getEnable() {
        return IsEnable;
    }

    public void setEnable(int enable) {
        IsEnable = enable;
    }

    public int getRegionID() {
        return RegionID;
    }

    public void setRegionID(int regionID) {
        RegionID = regionID;
    }

    public int getRegionCPN() {
        return RegionCPN;
    }

    public void setRegionCPN(int regionCPN) {
        RegionCPN = regionCPN;
    }

    public int getUpdatedBy() {
        return UpdatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        UpdatedBy = updatedBy;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getUpdateAt() {
        return UpdateAt;
    }

    public void setUpdateAt(String updateAt) {
        UpdateAt = updateAt;
    }

    public Boolean getCanUpdateCOD() {
        return CanUpdateCOD;
    }

    public void setCanUpdateCOD(Boolean canUpdateCOD) {
        CanUpdateCOD = canUpdateCOD;
    }

    private int CountryID;
    private String Code;
    private List<String> NameExtension;
    private int IsEnable;
    private int RegionID;
    private int RegionCPN;
    private int UpdatedBy;
    private int Status;
    private String CreatedAt;
    private String UpdateAt;
    private Boolean CanUpdateCOD;
}
