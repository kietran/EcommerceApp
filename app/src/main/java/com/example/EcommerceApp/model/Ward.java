package com.example.EcommerceApp.model;

import java.util.List;

public class Ward {
    String WardCode;
    String DistrictID;
    String WardName;
    List<String> NameExtension;
    Boolean CanUpdateCOD;
    int SupportType;
    int PickType;
    int DeliverType;
    CWhiteListClient WhiteLostClient;
    CWhiteListWard WhiteListWard;

    public String getWardCode() {
        return WardCode;
    }

    public void setWardCode(String wardCode) {
        WardCode = wardCode;
    }

    public String getDistrictID() {
        return DistrictID;
    }

    public void setDistrictID(String districtID) {
        DistrictID = districtID;
    }

    public String getWardName() {
        return WardName;
    }

    public void setWardName(String wardName) {
        WardName = wardName;
    }

    public List<String> getNameExtension() {
        return NameExtension;
    }

    public void setNameExtension(List<String> nameExtension) {
        NameExtension = nameExtension;
    }

    public Boolean getCanUpdateCOD() {
        return CanUpdateCOD;
    }

    public void setCanUpdateCOD(Boolean canUpdateCOD) {
        CanUpdateCOD = canUpdateCOD;
    }

    public int getSupportType() {
        return SupportType;
    }

    public void setSupportType(int supportType) {
        SupportType = supportType;
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

    public CWhiteListClient getWhiteLostClient() {
        return WhiteLostClient;
    }

    public void setWhiteLostClient(CWhiteListClient whiteLostClient) {
        WhiteLostClient = whiteLostClient;
    }

    public CWhiteListWard getWhiteListWard() {
        return WhiteListWard;
    }

    public void setWhiteListWard(CWhiteListWard whiteListWard) {
        WhiteListWard = whiteListWard;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
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

    public List<String> getOnDates() {
        return OnDates;
    }

    public void setOnDates(List<String> onDates) {
        OnDates = onDates;
    }

    public String getCreatedIP() {
        return CreatedIP;
    }

    public void setCreatedIP(String createdIP) {
        CreatedIP = createdIP;
    }

    public int getCreatedEmployee() {
        return CreatedEmployee;
    }

    public void setCreatedEmployee(int createdEmployee) {
        CreatedEmployee = createdEmployee;
    }

    public String getCreatedSource() {
        return CreatedSource;
    }

    public void setCreatedSource(String createdSource) {
        CreatedSource = createdSource;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public int getUpdatedEmpoyee() {
        return UpdatedEmpoyee;
    }

    public void setUpdatedEmpoyee(int updatedEmpoyee) {
        UpdatedEmpoyee = updatedEmpoyee;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    int Status;
    String ReasonCode;
    String ReasonMessage;
    List<String> OnDates;
    String CreatedIP;
    int CreatedEmployee;
    String CreatedSource;
    String CreatedDate;
    int UpdatedEmpoyee;
    String UpdatedDate;
    public static class CWhiteListClient{
        private List<String> From;
        private List<String> To;
        private List<String> Return;
    }
    public static class CWhiteListWard{
        private List<String> From;
        private List<String> To;
    }

}




