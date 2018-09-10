package com.example.luis9.xperto_plus;

import android.app.Application;

public class GlobalVars extends Application {
    private String name;
    private String email;
    private String pass;
    private String birth;
    private String gender;
    private String country;
    private String image;
    private String id;
    private String tel;

    public String getId() {
        return id;
    }

    private int weight, height,ejercicio;


    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name=name;
    }
    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public String getPass(){
        return this.pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }
    public String getBirth(){
        return this.birth;
    }
    public void setBirth(String birth) {
        this.birth = birth;
    }
    public String getGender(){
        return this.gender;
    }
    public void setGender(String gender){
        this.gender=gender;
        if(gender.equals("1")){
            this.gender="Male";
        }
        if(gender.equals("2")){
            this.gender="Female";
        }
    }
    public String getCountry(){
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image=image;
    }

    public String getID() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public  int getEjercicio(){
        return  this.ejercicio;
    }

    public void setEjercicio(int ejercicio){
        this.ejercicio = ejercicio;
    }

    public  String getTel(){
        return  this.tel;
    }

    public void setTel(String tel){
        this.tel = tel;
    }
}
