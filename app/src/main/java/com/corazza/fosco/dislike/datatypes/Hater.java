package com.corazza.fosco.dislike.datatypes;

import java.util.ArrayList;

/**
 * Created by Simone on 17/09/2014.
 */
public class Hater {

    public String name;
    public ArrayList<GraphElement> elements;
    public String id;

    public Hater(String id, String name) {
        this.id = id;
        this.name = name;
        elements = new  ArrayList<GraphElement>();
    }

    public Hater(String id) {
        this.id = id;
        elements = new  ArrayList<GraphElement>();
    }

    public boolean hates(BaseElement e){
        for(BaseElement i : elements)
            if(e.equals(i)) return true;
        return false;
    }

    public void addElement(HoSElement element){
        elements.add(new GraphElement(element));
    }

    public void deleteElement(HoSElement element){
        deleteElement(element.getId());
    }

    public void deleteElement(String i){
        ArrayList<GraphElement> removes = new ArrayList<GraphElement>();
        for(GraphElement element : elements)
            if(element.equals(i)) removes.add(element);
        elements.removeAll(removes);
    }
}
