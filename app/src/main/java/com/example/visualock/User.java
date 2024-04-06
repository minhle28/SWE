package com.example.visualock;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String parameter;
    private List<String> images_pass;
    private List<String> images;

    public User(String name, String parameter, List<String> images_pass, List<String> images) {
        this.name = name;
        this.images = images;
        this.images_pass = images_pass;
        this.parameter = parameter;
    }

    public User(String name, List<String> images_pass, List<String> images) {
        this.name = name;
        this.images = images;
        this.images_pass = images_pass;
        parameter = "8:8:8:8:8";
    }

    public User(String name) {
        this.name = name;
        images = new ArrayList<>();
        images_pass = new ArrayList<>();
        parameter = "8:8:8:8:8";
    }
    public User() {
        this.name = "";
        images = new ArrayList<>();
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

    public void setImages_pass(List<String> images_pass) {
        this.images_pass = images_pass;
    }
    public void insertImages_pass(String image) {
        this.images_pass.add(image);
    }
    public void removeImages_pass(String image) {
        this.images_pass.remove(image);
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
    public void insertImages(String image) {
        this.images.add(image);
    }
    public void removeImages(String image) {
        this.images.remove(image);
    }

}
