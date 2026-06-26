package com.example.projecz;


import static android.net.Uri.parse;

import android.net.Uri;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Deal {
    public String Email,Name,Author,Gangree,Script,Price,Address,ID;
    public String ImageFst,ImageScd,ImageThd;
    public Boolean Exchange, Complete;

    public Deal() {
    }

    public Deal(String email,String name, String author, String gangree, String script, String price, String imageFst, String imageScd, String imageThd, Boolean exchange, String address, Boolean complete, String id) {
        Email=email;
        Name = name;
        Author = author;
        Gangree = gangree;
        Script = script;
        Price = price;
        ImageFst = imageFst;
        ImageScd = imageScd;
        ImageThd = imageThd;
        Exchange = exchange;
        Address=address;
        Complete=complete;
        ID=id;
    }
    public String getFirstImage() {
        return ImageFst.toString();
    }
}
