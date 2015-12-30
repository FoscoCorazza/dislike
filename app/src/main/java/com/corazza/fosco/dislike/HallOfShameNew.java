package com.corazza.fosco.dislike;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.corazza.fosco.dislike.adapters.ViewHolder_HoSE;
import com.corazza.fosco.dislike.datatypes.BaseElement;
import com.corazza.fosco.dislike.datatypes.HoSElement;
import com.corazza.fosco.dislike.utilities.DatabaseUtilities;
import com.corazza.fosco.dislike.utilities.Global;
import com.corazza.fosco.dislike.utilities.Utilities;

import java.util.ArrayList;


public class HallOfShameNew extends Activity {

    HoSElement blankElement;
    AutoCompleteTextView what;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_of_shame_new);
        LayoutInflater factory = LayoutInflater.from(this);
        RelativeLayout rootElement = ((RelativeLayout) findViewById(R.id.hosnew_container));
        View mainElement = factory.inflate(R.layout.hos_element, ((RelativeLayout) findViewById(R.id.hosnew_container)) , false);

        rootElement.addView(mainElement);
        mainElement.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        rootElement.setBackgroundColor(0xff000000);

        blankElement = new HoSElement(  "0", //TODO
                                        Global.THIS_USER,
                                        "", "",
                                        "http://i.imgur.com/Dci8MYT.png", 0);

        ViewHolder_HoSE.populateHolder(
                ViewHolder_HoSE.generateHolder(mainElement, true),
                blankElement, 0, Integer.MAX_VALUE, true);

        what = ((AutoCompleteTextView) mainElement.findViewById(R.id.hos_textView_what));

        what.setEnabled(true);
        what.setBackground((new EditText(this)).getBackground());
        what.setCursorVisible(true);
        what.setClickable(true);

        ArrayAdapter<BaseElement> adapter = new ArrayAdapter<BaseElement>(this,
                android.R.layout.simple_dropdown_item_1line, Global.ELEMENTS);
        what.setAdapter(adapter);


        final ImageView imageView = ((ImageView) mainElement.findViewById(R.id.hos_imageView_what));

        TextView plusButton = ((TextView) mainElement.findViewById(R.id.hos_PlusButton));

        plusButton.setVisibility(View.VISIBLE);

        final Activity THIS = this;
        imageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               AlertDialog.Builder alert = new AlertDialog.Builder(THIS);
               alert.setTitle("Risorsa");
               alert.setMessage("URL:");
               // Set an EditText view to get user input
               final EditText input = new EditText(THIS);
               input.setHint("http://");
               alert.setView(input);

               input.setSingleLine(false);
               input.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
               input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);

               alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int whichButton) {
                      blankElement.setImageResource(input.getText().toString(), imageView);
                   }
               });

               alert.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int whichButton) {
                   }
               });

               alert.show();
           }
       });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hall_of_shame_new, menu);
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


    public void addDislike(View view){
        blankElement.setName(what.getText().toString());
        DatabaseUtilities.setNewElement(this,Global.THIS_USER,blankElement);


    }

    public void onPostExecute_ofSetNewElement(ArrayList<String> result, HoSElement elementAffected, Object jolly){
        Intent i = new Intent(this, HallOfShame.class);
        i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        overridePendingTransition(R.anim.reverse_flip_in,R.anim.reverse_flip_out);
        Toast.makeText(this,elementAffected.getName(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.reverse_flip_in, R.anim.reverse_flip_out);
    }
}

