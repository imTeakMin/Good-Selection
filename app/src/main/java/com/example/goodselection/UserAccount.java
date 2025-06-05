package com.example.goodselection;

import java.util.ArrayList;

public class UserAccount {
    private String idToken; // firebaseUid (고유정보)
    private String emailId; // 로그인 ID
    private String password; // 로그인 PW
    private ArrayList<String> goods; // 소유하고있는 상품
    private int point; // 소유하고있는 포인트


    public UserAccount(){}

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoint() { return point; }

    public void setPoint(int point) { this.point=point; }

    public ArrayList<String> getGoods() {return this.goods;}
    public void setGoods() {if(goods==null) goods=new ArrayList<String>();}
    public void addGoods(String goods) {
        if(this.goods==null)
            this.goods=new ArrayList<>();

        this.goods.add(goods);
    }
    public void deleteGoods(String goods) { this.goods.remove(goods); }

}