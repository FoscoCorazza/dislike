package com.corazza.fosco.dislike;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.corazza.fosco.dislike.datatypes.BaseElement;
import com.corazza.fosco.dislike.utilities.DatabaseUtilities;
import com.corazza.fosco.dislike.utilities.Global;

import java.util.ArrayList;


public class SplashScreen extends Activity {

    public boolean loaded(){return Global.ELEMENTS != null;}
    public boolean animationEnded = false;
    public boolean connectionActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        load();
        logoAnimationStart();
    }

    public void onPostExecute_ofGetElement(ArrayList<BaseElement> result, Object jolly){
        if(jolly instanceof String && jolly.equals("NOCONNECTION")) connectionActive = false;
        else Global.ELEMENTS.addAll(result);
        moveOn();
    }

    //Passa alla prossima Activity, se si può fare.
    private void moveOn(){
        if ((!connectionActive || loaded()) && animationEnded) {
            startActivity(new Intent(this, HallOfShame.class));
            finish();
        }
    }

    private void load(){
        DatabaseUtilities.getElements(this, Global.LANGUAGE);
    }

    private void logoAnimationStart(){
        findViewById(R.id.letter0).setAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_in));
        findViewById(R.id.letter0).getAnimation().setStartOffset(1000);
        findViewById(R.id.letter0).getAnimation().start();

        findViewById(R.id.letter1).setAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_in));
        findViewById(R.id.letter1).getAnimation().setStartOffset(1000 + Global.SPLASH_SPEED * 1);
        findViewById(R.id.letter1).getAnimation().start();

        findViewById(R.id.letter2).setAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_in));
        findViewById(R.id.letter2).getAnimation().setStartOffset(1000 + Global.SPLASH_SPEED * 2);
        findViewById(R.id.letter2).getAnimation().start();

        findViewById(R.id.letter3).setAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_in));
        findViewById(R.id.letter3).getAnimation().setStartOffset(1000 + Global.SPLASH_SPEED * 3);
        findViewById(R.id.letter3).getAnimation().start();

        findViewById(R.id.letter4).setAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_in));
        findViewById(R.id.letter4).getAnimation().setStartOffset(1000 + Global.SPLASH_SPEED * 4);
        findViewById(R.id.letter4).getAnimation().start();

        findViewById(R.id.letter6).setAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_in));
        findViewById(R.id.letter6).getAnimation().setStartOffset(1000 + Global.SPLASH_SPEED * 5);
        findViewById(R.id.letter6).getAnimation().start();

        findViewById(R.id.letter5).setAnimation(AnimationUtils.loadAnimation(this, R.anim.throw_in));
        findViewById(R.id.letter5).getAnimation().setStartOffset(1000 + Global.SPLASH_SPEED * 7);
        findViewById(R.id.letter5).getAnimation().start();

        findViewById(R.id.letter5).getAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animationEnded = true;
                moveOn();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
