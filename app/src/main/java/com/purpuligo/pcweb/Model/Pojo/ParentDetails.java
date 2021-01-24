package com.purpuligo.pcweb.Model.Pojo;

import java.util.ArrayList;

public class ParentDetails {
    private String parent_name;
    private String parent_id;
    private String parent_image;
    private ArrayList<ProductCategoriesDetails> categories;

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getParent_image() {
        return parent_image;
    }

    public void setParent_image(String parent_image) {
        this.parent_image = parent_image;
    }

    public ArrayList<ProductCategoriesDetails> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<ProductCategoriesDetails> categories) {
        this.categories = categories;
    }
}
