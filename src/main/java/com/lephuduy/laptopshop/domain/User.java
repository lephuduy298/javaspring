package com.lephuduy.laptopshop.domain;

public class User {
    // 1 Attributes
    private String id;
    private String email;
    private String password;
    private String fullName;
    private String address;
    private String phone;

    // getter, setter methods
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Constructors methods
    // public User() {

    // }

    // public User(String id, String fullName, String password, String address,
    // String phone, String email) {
    // this.id = id;
    // this.address = address;
    // this.email = email;
    // this.fullName = fullName;
    // this.phone = phone;
    // this.password = password;
    // }
    @Override
    public String toString() {
        return "User [id=" + id + ", email=" + email + ", password=" + password + ", fullName=" + fullName
                + ", address=" + address + ", phone=" + phone + "]";
    }
}