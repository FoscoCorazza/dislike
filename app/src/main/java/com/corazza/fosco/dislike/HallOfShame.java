package com.corazza.fosco.dislike;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.corazza.fosco.dislike.adapters.ArrayAdapterForHoS;
import com.corazza.fosco.dislike.adapters.ViewHolder_HoSE;
import com.corazza.fosco.dislike.datatypes.Hater;
import com.corazza.fosco.dislike.datatypes.HoSElement;
import com.corazza.fosco.dislike.utilities.DatabaseUtilities;
import com.corazza.fosco.dislike.utilities.Global;
import com.corazza.fosco.dislike.utilities.Utilities;

import java.util.ArrayList;


public class HallOfShame extends Activity {

    ListView listOfContents;

    //PassingVariables
    public static HoSElement errantElement;
    public static Hater errantHater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_of_shame);
        Utilities.APPLICATION_CONTEXT = getApplicationContext();

        DatabaseUtilities.getDislikesOf(this, Global.THIS_USER);
        DatabaseUtilities.getHoSElements(this);

        overridePendingTransition(R.anim.flip_in,R.anim.flip_out);
    }

    public void initList(ArrayList<HoSElement> list){
        ArrayAdapterForHoS adapter = new ArrayAdapterForHoS(this, R.layout.hos_element, list);
        listOfContents = (ListView) findViewById(R.id.hos_listOfContent);
        listOfContents.setAdapter(adapter);
    }


    //LISTENERS PER ATTIVITA' DEL DB
    public void onPostExecute_ofGetHoSElementTask(ArrayList<String> result, Object jolly){initList(DatabaseUtilities.resultToHoSEArray(result));}
    public void onPostExecute_ofGetDislikesTask(ArrayList<String> result, Object jolly){Global.THIS_USER.elements.addAll(DatabaseUtilities.resultToGraphEArray(result));}

    public void onPostExecute_ofSetNewDislike(ArrayList<String> result, HoSElement elementAffected, Object jolly){
        Toast.makeText(this,"'Neanche a me' aggiunto", Toast.LENGTH_LONG).show();
        elementAffected.viewHolder.iDislikeThisAlready = true;
        Global.THIS_USER.addElement(new HoSElement(
                elementAffected.getId(),
                Global.THIS_USER,
                elementAffected.getName(),
                (String) jolly,
                elementAffected.getImageResource(),
                elementAffected.getMeNeither()
        ));
        elementAffected.increaseMeNeither(1);
        ViewHolder_HoSE.applyConditionalChangesInCaseOf_MeNeither(elementAffected);

    }
    public void onPostExecute_ofDelOldDislike(ArrayList<String> result, HoSElement elementAffected, Object jolly){
        Toast.makeText(this,"Idea cambiata", Toast.LENGTH_LONG).show();
        elementAffected.viewHolder.iDislikeThisAlready = false;
        Global.THIS_USER.deleteElement(elementAffected);
        elementAffected.decreaseMeNeither(1);
        ViewHolder_HoSE.applyConditionalChangesInCaseOf_MeNeither(elementAffected);
    }

    public void changeAlertColor(AlertDialog dialog){
        final Resources res = getResources();
        final int titleDividerId = res.getIdentifier("titleDivider", "id", "android");
        final View titleDivider = dialog.findViewById(titleDividerId);
        titleDivider.setBackgroundColor(res.getColor(R.color.greenS50B100));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void toTheTop(View v){
        if(listOfContents == null) return;
        listOfContents.smoothScrollToPosition(0);
    }

    public void toGraphate(View v){
        startActivity(new Intent(this, Graphate.class));
        overridePendingTransition(R.anim.flip_in,R.anim.flip_out);
    }

    public void brandNewDislike(View v){
        Intent i = new Intent(this, HallOfShameNew.class);
        i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
        overridePendingTransition(R.anim.flip_in,R.anim.flip_out);
    }
}
