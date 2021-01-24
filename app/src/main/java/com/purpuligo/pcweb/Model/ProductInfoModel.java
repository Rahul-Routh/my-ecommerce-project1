package com.purpuligo.pcweb.Model;

public class ProductInfoModel {
    private String infoName;
    private String infoValue;


    public ProductInfoModel(String infoName, String infoValue) {
        this.infoName = infoName;
        this.infoValue = infoValue;
    }

    public String getInfoName() {
        return infoName;
    }

    public void setInfoName(String infoName) {
        this.infoName = infoName;
    }

    public String getInfoValue() {
        return infoValue;
    }

    public void setInfoValue(String infoValue) {
        this.infoValue = infoValue;
    }
}
