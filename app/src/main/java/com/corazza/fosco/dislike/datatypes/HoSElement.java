package com.corazza.fosco.dislike.datatypes;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.corazza.fosco.dislike.adapters.ViewHolder_HoSE;

/**
 * Created by Simone on 08/09/2014.
 */
public class HoSElement extends BaseElement{
    public Hater who;
    private String why;
    public String getWho() {return who.name;}
    public ViewHolder_HoSE viewHolder;

    public TextView textViewWhy;



    public String getWhy(){
        return why;
    }

    public void setWhy(String value, boolean createIfNull, Context ctx){
        why = value;
        if(textViewWhy == null && !createIfNull) return;
        if(textViewWhy == null) textViewWhy = new TextView(ctx);
        textViewWhy.setText(value);
    }
    public void setWhy(String value, Context ctx){
        setWhy(value, true, ctx);
    }
    public void setWhy(String value){
        setWhy(value, false, null);
    }

    public HoSElement(String what, Hater who, String why, String elementName, String imageResource, int meNeither) {
        super(what, elementName, imageResource, meNeither);
        this.who = who;
        setWhy(why);
    }


}
