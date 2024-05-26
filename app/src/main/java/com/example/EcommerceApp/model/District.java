package com.example.EcommerceApp.model;


import java.util.List;

public class District {
    private int DistrictID;
    private int ProvinceID;
    private String DistrictName;
    private String Code;
    private int Type;
    private int SupportType;
    private List<String> NameExtension;
    private int IsEnable;
    private int UpdatedBy;
    private String CreatedBy;
    private String UpdateAt;
    private Boolean CanUpdateCOD;
    private int Status;
    private int PickType;
    private int DeliverType;
    private CWhiteListClient WhiteListClient;
    private CWhiteListDistrict WhiteListDistrict;
    private List<String> OnDates;

    public int getDistrictID() {
        return DistrictID;
    }

    public void setDistrictID(int districtID) {
        DistrictID = districtID;
    }

    public int getProvinceID() {
        return ProvinceID;
    }

    public void setProvinceID(int provinceID) {
        ProvinceID = provinceID;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getSupportType() {
        return SupportType;
    }

    public void setSupportType(int supportType) {
        SupportType = supportType;
    }

    public List<String> getNameExtension() {
        return NameExtension;
    }

    public void setNameExtension(List<String> nameExtension) {
        NameExtension = nameExtension;
    }

    public int getIsEnable() {
        return IsEnable;
    }

    public void setIsEnable(int isEnable) {
        IsEnable = isEnable;
    }

    public int getUpdatedBy() {
        return UpdatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        UpdatedBy = updatedBy;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
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

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getPickType() {
        return PickType;
    }

    public void setPickType(int pickType) {
        PickType = pickType;
    }

    public int getDeliverType() {
        return DeliverType;
    }

    public void setDeliverType(int deliverType) {
        DeliverType = deliverType;
    }

    public CWhiteListClient getWhiteListClient() {
        return WhiteListClient;
    }

    public void setWhiteListClient(CWhiteListClient whiteListClient) {
        WhiteListClient = whiteListClient;
    }

    public CWhiteListDistrict getWhiteListDistrict() {
        return WhiteListDistrict;
    }

    public void setWhiteListDistrict(CWhiteListDistrict whiteListDistrict) {
        WhiteListDistrict = whiteListDistrict;
    }

    public List<String> getOnDates() {
        return OnDates;
    }

    public void setOnDates(List<String> onDates) {
        OnDates = onDates;
    }

    public String getReasonCode() {
        return ReasonCode;
    }

    public void setReasonCode(String reasonCode) {
        ReasonCode = reasonCode;
    }

    public String getReasonMessage() {
        return ReasonMessage;
    }

    public void setReasonMessage(String reasonMessage) {
        ReasonMessage = reasonMessage;
    }

    public int getUpdatedEmployee() {
        return UpdatedEmployee;
    }

    public void setUpdatedEmployee(int updatedEmployee) {
        UpdatedEmployee = updatedEmployee;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    private String ReasonCode;
    private String ReasonMessage;
    private int UpdatedEmployee;
    private String UpdatedDate;
    public static class CWhiteListClient{
        private List<String> From;
        private List<String> To;
        private List<String> Return;
    }
    public static class CWhiteListDistrict{
        private List<String> From;
        private List<String> To;
    }


}





