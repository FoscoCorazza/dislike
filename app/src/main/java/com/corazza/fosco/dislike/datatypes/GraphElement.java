package com.corazza.fosco.dislike.datatypes;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.corazza.fosco.dislike.utilities.Global;

import java.util.ArrayList;

/**
 * Created by Simone on 17/09/2014.
 */
public class GraphElement extends HoSElement{

    public ArrayList<GraphElement> directSuperiorsList;
    public int virtualRow = -1;
    public int lineAlpha = 0;


    public double distanceFrom(GraphElement e){
        int myX = getX() + getDiamater()/2;
        int myY = getY() + getDiamater()/2;
        int eX =  e.getX() + e.getDiamater()/2;
        int eY =  e.getY() + e.getDiamater()/2;

        return Math.sqrt(Math.pow(myX-eX,2) + Math.pow(myY-eY,2));

    }

    public int getDiamater(){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getImageView().getLayoutParams();
        return params.width;
    }

    public int getY(){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getImageView().getLayoutParams();
        return params.topMargin;
    }

    public int getX(){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getImageView().getLayoutParams();
        return params.leftMargin;
    }

    public void setY(int y){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getImageView().getLayoutParams();
        params.topMargin = y;
        getImageView().setLayoutParams(params);
        if(textViewWhy != null){
            RelativeLayout.LayoutParams paramsTV = (RelativeLayout.LayoutParams) textViewWhy.getLayoutParams();
            paramsTV.topMargin =  tvPosition().y;
            textViewWhy.setLayoutParams(paramsTV);
        }
    }

    public void setX(int x){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getImageView().getLayoutParams();
        params.leftMargin = x;
        getImageView().setLayoutParams(params);
        if(textViewWhy != null){
            RelativeLayout.LayoutParams paramsTV = (RelativeLayout.LayoutParams) textViewWhy.getLayoutParams();
            paramsTV.leftMargin =  tvPosition().x;
            textViewWhy.setLayoutParams(paramsTV);
        }
    }

    public boolean touches(GraphElement e, int offset){
        return distanceFrom(e) <= (getDiamater()/2 + e.getDiamater()/2 + offset);
    }

    public ImageView getImageView () {
        return imageView;
    }

    public void setImageView (ImageView  textView) {
        this.imageView = textView;
    }

    private ImageView imageView;


    public GraphElement(String what, Hater who,  String why, String elementName, String imageResource, int meNeither)  {
        super(what, who,  why, elementName, imageResource, meNeither);
        directSuperiorsList = new ArrayList<GraphElement>();
    }

    public GraphElement(HoSElement e)  {
        super(e.getId(), e.who, e.getName(), e.getWhy(),  e.getImageResource(), e.getMeNeither());
        directSuperiorsList = new ArrayList<GraphElement>();
    }

    public static GraphElement LowQuaElement(String what, String imageResource, int meNeither){return new GraphElement("1", Global.THIS_USER,what,"",imageResource,meNeither); }

    public void addAncestor(GraphElement a){
        if(!successorOf(a)) directSuperiorsList.add(a);
        eraseTransitivity(a);
    }

    public boolean successorOf(GraphElement e){
        if(directSuperiorsList == null || directSuperiorsList.size() == 0) return false;
        if(directSuperiorsList.contains(e)) return true;
        return myParentsAreSuccessorOf(e);
    }

    public boolean myParentsAreSuccessorOf(GraphElement e){
        for( GraphElement p : directSuperiorsList){
            if(p.successorOf(e)) return true;
        }
        return false;
    }

    public void eraseTransitivity(GraphElement element){
        ArrayList<GraphElement> newDirectSuperiorList = new ArrayList<GraphElement>();

        for( GraphElement p : directSuperiorsList){
           if(!element.successorOf(p)) newDirectSuperiorList.add(p);
        }

        directSuperiorsList.clear();
        directSuperiorsList.addAll(newDirectSuperiorList);

    }

    public static ArrayList<GraphElement> greatAncestors_Excluding(ArrayList<GraphElement> list,ArrayList<GraphElement> excluders, int row) {
        ArrayList<GraphElement> r = new ArrayList<GraphElement>();
        for(GraphElement e : list){
            if( sub(e.directSuperiorsList,excluders) == 0 && !excluders.contains(e)) {
                r.add(e);
                e.virtualRow = row;
            }
        }

        return r;
    }

    public static ArrayList<GraphElement> reorder(ArrayList<GraphElement> toReorder){
        ArrayList<GraphElement> r = new ArrayList<GraphElement>();
        int row = 0;
        while(r.size() != toReorder.size()){
            r.addAll(greatAncestors_Excluding(toReorder, r, row));
            row++;
        }

        return r;
    }

    private static int sub(ArrayList<GraphElement> list,ArrayList<GraphElement> excluders){
        int c = 0;
        for(GraphElement e : list){
            if(!excluders.contains(e)) c++;
        }
        return c;
    }


    public void initTextView(RelativeLayout container, Context ctx){
        setWhy(getWhy(), ctx);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = tvPosition().x;
        params.topMargin  = tvPosition().y;
        textViewWhy.setLayoutParams(params);
        textViewWhy.setMaxWidth(getDiamater()*2);
        textViewWhy.setGravity(Gravity.CENTER);
        if( textViewWhy.getParent() != null)
            ((ViewGroup)textViewWhy.getParent()).removeView(textViewWhy);
        container.addView(textViewWhy, params);
        textViewWhy.setVisibility(View.INVISIBLE);
    }

    private Point tvPosition(){
        int x = getX();
        int y = getY() + getDiamater() + 5;
        if(textViewWhy != null)
            x = getX() + getImageView().getWidth()/2 - textViewWhy.getWidth()/2;

        return new Point(x,y);
    }

}
