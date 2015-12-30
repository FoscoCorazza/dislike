package com.corazza.fosco.dislike.datatypes;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.corazza.fosco.dislike.utilities.Utilities;

/**
 * Created by Simone on 17/09/2014.
 */
public class BaseElement {
    private String id;
    private String name;


    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getId()   {return id;}

    private int meNeither;
    private String imageResource;

    public void setImageResource(String url){
        imageResource = url;
    }
    public String getImageResource(){
        return getImageResource(true);
    }
    public String getImageResource(boolean addHTTP){
        if(addHTTP) return "http://" + Utilities.decode(getImageResource(false));
        return imageResource
                .replace("http://","")
                .replace("https://","");
    }
    public void setImageResource(String url, ImageView iv){
        setImageResource(url);
        image = null;
        Utilities.ImageTasks.downloadImageViewContent(this,iv,false);
    }

    public Bitmap getCachedImage() {
        return image;
    }

    public void setCachedImage(boolean override, Bitmap image) {
        if(override || !isCached())
            this.image = image;
    }

    public boolean isCached(){
        return this.image != null;
    }

    private Bitmap image;

    public int getMeNeither(){return meNeither;}
    public void setMeNeither(int value){meNeither = value;}
    public void decreaseMeNeither(int value){meNeither = meNeither - value;}
    public void increaseMeNeither(int value){meNeither = meNeither + value;}

    public BaseElement(String id, String name, String imageResource, int meNeither) {
        this.meNeither = meNeither;
        this.id= id;
        this.name = name;
        this.imageResource = imageResource;
    }

    public boolean equals(BaseElement e){
        return equals(e.getId());
    }

    public boolean equals(String e){
        return this.id.equals(e);
    }

    @Override
    public String toString() {return name;}
}
