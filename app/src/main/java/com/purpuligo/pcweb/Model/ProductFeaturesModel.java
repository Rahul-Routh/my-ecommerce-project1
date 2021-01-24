package com.purpuligo.pcweb.Model;

public class ProductFeaturesModel {
    private String featuresName;
    private String featuresValue;


    public ProductFeaturesModel(String featuresName, String featuresValue) {
        this.featuresName = featuresName;
        this.featuresValue = featuresValue;
    }

    public String getFeaturesName() {
        return featuresName;
    }

    public void setFeaturesName(String featuresName) {
        this.featuresName = featuresName;
    }

    public String getFeaturesValue() {
        return featuresValue;
    }

    public void setFeaturesValue(String featuresValue) {
        this.featuresValue = featuresValue;
    }
}
