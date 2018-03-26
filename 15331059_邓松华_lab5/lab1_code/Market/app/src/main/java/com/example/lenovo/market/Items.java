package com.example.lenovo.market;

/**
 * Created by Lenovo on 2017/10/19.
 */

public class Items {
    private String name;
    private String price;
    private String style;
    private String message;
    private int imageId;
    Items(String g,String p,String s,String m,int i) {
        name=g;
        price=p;
        style=s;
        message=m;
        imageId=i;
    }
    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }


    public String getStyle() {
        return style;
    }

    public String getMessage() {
        return message;
    }
    public int getImageId() {
        return imageId;
    }

}

