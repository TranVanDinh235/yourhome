package com.example.homedy.Account;

import com.example.homedy.R;

public class Account {
    private int image;
    private String name;
    private String userName;
    private String passWord;
    private String email;
    private String phoneNumber;
    private String facebook;
    private String address;

    public Account(){}

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static Account newInstance(int image, String name, String userName, String passWord, String email, String phoneNumber, String facebook, String address){
        Account account = new Account();
        account.setImage(image);
        account.setName(name);
        account.setUserName(userName);
        account.setPassWord(passWord);
        account.setEmail(email);
        account.setPhoneNumber(phoneNumber);
        account.setFacebook(facebook);
        account.setAddress(address);
        return account;
    }

    public static Account newInstanceExample(){
        return newInstance(R.drawable.imageavatar, "Tran Van Dinh", "Tran Van Dinh", "12345", "abc@gmail.com", "1234556", "facebook.com", "Ha Noi");
    }
}
