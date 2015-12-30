package com.corazza.fosco.dislike.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.corazza.fosco.dislike.HallOfShame;
import com.corazza.fosco.dislike.HallOfShameNew;
import com.corazza.fosco.dislike.SplashScreen;
import com.corazza.fosco.dislike.datatypes.BaseElement;
import com.corazza.fosco.dislike.datatypes.GraphElement;
import com.corazza.fosco.dislike.datatypes.Hater;
import com.corazza.fosco.dislike.datatypes.HoSElement;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseUtilities {

    public static final String DELIM = "<&>";

    private static class dbQueryTask extends AsyncTask<String, String, Void>
    {

        InputStream is = null ;
        ArrayList<String> result = new ArrayList<String>();
        String q = "";
        Activity activity;
        int caller = -1;
        HoSElement element;
        Object jolly;

        protected void onPreExecute() {}

        public dbQueryTask(String q, Activity activity, int caller, HoSElement element, Object jolly){
            this.activity = activity;
            this.element = element;
            this.caller = caller;
            this.q = q;
            this.jolly = jolly;
        }

        @Override
        protected Void doInBackground(String... params) {
            /*if(!hasActiveInternetConnection(activity)){
                jolly = "NOCONNECTION";
                return null;
            }*/

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(q);

            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(param));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                //read content
                is =  httpEntity.getContent();

            } catch (Exception e) {
                jolly = "NOCONNECTION";
                return null;
            }
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line = "", input = "";

                while((line=br.readLine())!=null)
                {
                   input = input + line;
                }

                for (String allLine : input.split("<br>")) {
                    result.add(allLine);
                }

                is.close();

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error converting result "+e.toString());
            }

            return null;

        }

        protected void onPostExecute(Void v) {
            callCorrespondentMethod(activity, caller, result, element, jolly);
        }

        }


    public static  ArrayList<HoSElement> resultToHoSEArray(ArrayList<String> r){
        ArrayList<HoSElement> result = new ArrayList<HoSElement>();
        for(String line : r){
            if(!line.trim().equals(""))
                result.add(new HoSElement(
                        line.split(DELIM)[0].trim(),            // What
                        new Hater(line.split(DELIM)[1].trim(), line.split(DELIM)[3].trim()), // Who
                        line.split(DELIM)[2].trim(),            // Why
                        line.split(DELIM)[4].trim(),            // Name
                        line.split(DELIM)[5].trim(),            // Resource
                        Integer.parseInt(line.split(DELIM)[6].trim()))); //MeNeither
        }
        return result;
    }

    public static  ArrayList<GraphElement> resultToGraphEArray(ArrayList<String> r){
    ArrayList<GraphElement> result = new ArrayList<GraphElement>();
    for(String line : r){
        if(!line.trim().equals(""))
            result.add(new GraphElement(
                    line.split(DELIM)[0].trim(),            // What
                    new Hater(line.split(DELIM)[1].trim()), // Who
                    line.split(DELIM)[2].trim(),            // Why
                    line.split(DELIM)[4].trim(),            // Name
                    line.split(DELIM)[5].trim(),            // Resource
                    Integer.parseInt(line.split(DELIM)[6].trim()))); //MeNeither
    }
    return result;
}

    public static  ArrayList<BaseElement> resultToStringArray(ArrayList<String> r){
        ArrayList<BaseElement> result = new ArrayList<BaseElement>();
        for(String line : r){
            if(!line.trim().equals(""))
                /*result.add(line.split(DELIM)[1].trim() + ", " +
                           line.split(DELIM)[2].trim() + " [" +
                           line.split(DELIM)[0].trim()+ "]");*/
                result.add(new BaseElement(
                            line.split(DELIM)[0].trim(),
                            line.split(DELIM)[1].trim(),
                            "",
                            0));
        }
        return result;
    }

    public static void getHoSElements(HallOfShame activity){
        String s = "http://dislike.netsons.org/getHallOfShame.php?usr=qkobrgfw_fosco&pwd=Subs0n1ca!";
        new dbQueryTask(s,activity, GETHOSELEMENT, null, null).execute();
    }

    public static void getDislikesOf(HallOfShame activity, Hater h){
        String s = "http://dislike.netsons.org/getDislikes.php?usr=qkobrgfw_fosco&pwd=Subs0n1ca!&who=" + h.id;
        new dbQueryTask(s,activity, GETDISLIKES, null, null).execute();
    }

    public static void getElements(Activity activity, String lang){
        String s = "http://dislike.netsons.org/getElements.php?usr=qkobrgfw_fosco&pwd=Subs0n1ca!&lang=" + lang;
        new dbQueryTask(s,activity, GETELEMENT, null, null).execute();
    }

    public static void setNewElement(Activity activity, Hater h, HoSElement element){
        String s = "http://dislike.netsons.org/setNewElement.php?usr=qkobrgfw_fosco&pwd=Subs0n1ca!&";
        s += "who="    + h.id + "&";
        s += "name="   + Utilities.encode(element.getName()) + "&";
        s += "res="    + Utilities.encode(element.getImageResource(false)) + "&";
        s += "why="    + Utilities.encode(element.getWhy().replace("'", "´"));
        new dbQueryTask(s,activity, SETNEWELEMENT, element, null).execute();
    }

    public static void setNewDislike(final HallOfShame activity, final Hater hater, final HoSElement element) {

        AlertDialog.Builder alert = new AlertDialog.Builder(activity);

        alert.setTitle("Neanche a me piace " + element.getName());
        alert.setMessage("perchè a mio parere");


        // Set an EditText view to get user input
        final EditText input = new EditText(activity);
        alert.setView(input);

        input.setSingleLine(false);
        input.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String s = "http://dislike.netsons.org/setNewDislike.php?usr=qkobrgfw_fosco&pwd=Subs0n1ca!&";
                s += "who="    + hater.id + "&";
                s += "what="   + element.getId() + "&";
                s += "why="    + Utilities.encode(input.getText().toString().replace("'", "´")) + "&";
                s += "oldwho=" + element.who.id;

                new dbQueryTask(s, activity, SETNEWDISLIKE, element, input.getText().toString()).execute();

            }
        });

        activity.changeAlertColor(alert.show());

    }

    public static void delOldDislike(HallOfShame activity, Hater hater, HoSElement element) {
        String s = "http://dislike.netsons.org/delOldDislike.php?usr=qkobrgfw_fosco&pwd=Subs0n1ca!&";
        s += "who="    + hater.id + "&";
        s += "what="   + element.getId() + "&";
        s += "oldwho=" + element.who.id;

        new dbQueryTask(s, activity, DELOLDDISLIKE, element, null).execute();

    }

    private static void callCorrespondentMethod(Activity activity, int whatCalled, ArrayList<String> result, HoSElement elementAffected, Object jolly){
        if(activity instanceof HallOfShame){
            switch (whatCalled){
                case GETHOSELEMENT:
                    ((HallOfShame)activity).onPostExecute_ofGetHoSElementTask(result, jolly);
                    break;
                case GETDISLIKES:
                    ((HallOfShame)activity).onPostExecute_ofGetDislikesTask(result, jolly);
                    break;
                case SETNEWDISLIKE:
                    ((HallOfShame)activity).onPostExecute_ofSetNewDislike(result, elementAffected, jolly);
                    break;
                case DELOLDDISLIKE:
                    ((HallOfShame)activity).onPostExecute_ofDelOldDislike(result, elementAffected, jolly);
                    break;
            }
        }
        if(activity instanceof HallOfShameNew){
            switch (whatCalled){
                case SETNEWELEMENT:
                    ((HallOfShameNew)activity).onPostExecute_ofSetNewElement(result, elementAffected, jolly);
                    break;
            }
        }
        if(activity instanceof SplashScreen){
            switch (whatCalled){
                case GETELEMENT:
                    ((SplashScreen)activity).onPostExecute_ofGetElement(resultToStringArray(result), jolly);
                    break;
            }
        }
    }


    public static boolean hasActiveInternetConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e("ERR", "Error checking internet connection", e);
            }
        } else {
            Log.d("ERR", "No network available!");
        }
        return false;
    }


    private static final int GETHOSELEMENT  = 0;
    private static final int GETDISLIKES    = 1;
    private static final int SETNEWDISLIKE  = 2;
    private static final int DELOLDDISLIKE  = 3;
    private static final int SETNEWELEMENT  = 4;
    private static final int GETELEMENT     = 5;



}
