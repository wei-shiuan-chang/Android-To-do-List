package com.hfad.todolistapplication;

public class List {

    private String item;
    private String status;



    private List(String item, String status){
        this.item = item;
        this.status = status;
    }

    public String getItem(){
        return this.item;
    }

    public String getStatus(){
        return this.status;
    }


}
