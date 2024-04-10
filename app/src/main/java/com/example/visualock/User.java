package com.example.visualock;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String parameter;
    private List<String> images_pass;
    private String uID;

    public User(String uID, String name, String parameter, List<String> images_pass) {
        this.uID = uID;
        this.name = name;
        this.images_pass = images_pass;
        this.parameter = parameter;
    }


    public User(String uID, String name) {
        this.uID = uID;
        this.name = name;
        images_pass = new ArrayList<>();
        parameter = "8:8:8:8:8";
    }
    public User() {
        this.uID = "";
        this.name = "";
        images_pass = new ArrayList<>();
        parameter = "8:8:8:8:8";
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public List<String> getImages_pass() {
        return images_pass;
    }
    public String getuID() {
        return uID;
    }
    public void setuID(String uID) {
        this.uID = uID;
    }

    public void setImages_pass(List<String> images_pass) {
        this.images_pass = images_pass;
    }
    public void insertImages_pass(String image) {
        this.images_pass.add(image);
    }
    public void removeImages_pass(String image) {
        this.images_pass.remove(image);
    }




}
