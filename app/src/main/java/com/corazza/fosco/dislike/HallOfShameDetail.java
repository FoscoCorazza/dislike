package com.corazza.fosco.dislike;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.corazza.fosco.dislike.adapters.ViewHolder_HoSE;


public class HallOfShameDetail extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_of_shame_detail);
        LayoutInflater factory = LayoutInflater.from(this);
        RelativeLayout rootElement = ((RelativeLayout) findViewById(R.id.hosdetail_container));
        View mainElement = factory.inflate(R.layout.hos_element, ((RelativeLayout) findViewById(R.id.hosdetail_container)) , false);

        rootElement.addView(mainElement);
        mainElement.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        rootElement.setBackgroundColor(0xff000000);


        ViewHolder_HoSE.populateHolder(
                ViewHolder_HoSE.generateHolder(mainElement, true),
                HallOfShame.errantElement, 0, Integer.MAX_VALUE, true);
    }


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hall_of_shame_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
