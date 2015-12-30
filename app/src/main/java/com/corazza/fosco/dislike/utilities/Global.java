package com.corazza.fosco.dislike.utilities;

import android.content.Context;

import com.corazza.fosco.dislike.datatypes.BaseElement;
import com.corazza.fosco.dislike.datatypes.Hater;

import java.util.ArrayList;

/**
 * Created by Simone on 01/10/2014.
 */
public class Global {
    public static final String IT  = "it";
    public static final String EN  = "en";

    public static String LANGUAGE = IT;
    public static Hater THIS_USER = new Hater("1","Fosco");
    public static int SPLASH_SPEED = 50;
    public static ArrayList<BaseElement> ELEMENTS = new ArrayList<BaseElement>();
    public static Context CONTEXT;
}
