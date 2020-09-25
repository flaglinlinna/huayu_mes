package com.web.attendance.entity;

public class UserInfo {
    private String name;
    private Boolean Enabled;
    private String Password;
    private Integer Privilege;
    private String EnrollNumber;

    public UserInfo(String name, Boolean enabled, String password, Integer privilege, String enrollNumber) {
        this.name = name;
        Enabled = enabled;
        Password = password;
        Privilege = privilege;
        EnrollNumber = enrollNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnabled() {
        return Enabled;
    }

    public void setEnabled(Boolean enabled) {
        Enabled = enabled;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Integer getPrivilege() {
        return Privilege;
    }

    public void setPrivilege(Integer privilege) {
        Privilege = privilege;
    }

    public String getEnrollNumber() {
        return EnrollNumber;
    }

    public void setEnrollNumber(String enrollNumber) {
        EnrollNumber = enrollNumber;
    }

    public UserInfo(){};
}
