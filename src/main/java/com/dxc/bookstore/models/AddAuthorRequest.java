package com.dxc.bookstore.models;

public class AddAuthorRequest {
    private String name;
    private String birthday;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
