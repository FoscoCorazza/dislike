package com.corazza.fosco.dislike.utilities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.corazza.fosco.dislike.R;
import com.corazza.fosco.dislike.datatypes.BaseElement;
import com.corazza.fosco.dislike.datatypes.GraphElement;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Simone on 19/09/2014.
 */
public class Utilities {
    public static Context APPLICATION_CONTEXT;

    public static Point deviceSize(Activity a){
        Display display = a.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static class StatisticsTasks{

        public static double standardDeviation(ArrayList<GraphElement> elements) {
            double v = 0;
            double mean = mean(elements);

            for (GraphElement element : elements) {
                v += Math.pow(element.getMeNeither() - mean, 2);
            }

            v = Math.sqrt(v / elements.size());
            return v;
        }

        public static double mean(ArrayList<GraphElement> elements) {
            double m = 0;
            for (GraphElement element : elements) {
                m += element.getMeNeither();
            }
            return m / elements.size();
        }

        public static double scaleUnindipendentDeviation(ArrayList<GraphElement> elements) {
            return standardDeviation(elements) / mean(elements);
        }

    }

    public static class ImageTasks{

        public static void downloadImageViewContent(BaseElement element, ImageView iv, boolean circleCrop){
            if(circleCrop)
                iv.setImageResource(R.drawable.circletest);
            else
                iv.setImageResource(R.drawable.black);
            new DownloadImageTask(iv, element, circleCrop).execute(element.getImageResource());
        }


        private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;
            BaseElement cachedElement;
            boolean circleCrop, haveToSave = true;


            public DownloadImageTask(ImageView bmImage, BaseElement element, boolean circleCrop) {
                this.bmImage = bmImage;
                this.cachedElement = element;
                this.circleCrop = circleCrop;
            }

            protected Bitmap doInBackground(String... urls) {
                //Prima di fare davvero il dowloand controllo che la abbia cachata
                String urldisplay = urls[0];
                haveToSave = true;
                //setCachedImage(false, i) NON sovrascrive nella piccola cache un eventuale immagine
                cachedElement.setCachedImage(false, loadImageFromStorage(CachePath(), escapeThis(prefix(circleCrop) + urldisplay)));
                if(cachedElement.isCached()) {
                    haveToSave = false;
                    return cachedElement.getCachedImage();
                }

                //Alla fine ne faccio il Download
                Bitmap mIcon11 = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                if(circleCrop) mIcon11 = getCircleCroppedBitmap(mIcon11);
                cachedElement.setCachedImage(true, mIcon11);
                return mIcon11;
            }

            protected void onPostExecute(Bitmap result) {
                if(circleCrop) bmImage.setBackgroundResource(R.drawable.circletest);
                bmImage.setImageBitmap(result);
                bmImage.startAnimation(AnimationUtils.loadAnimation(bmImage.getContext(), R.anim.fade_in));
                if(cachedElement instanceof GraphElement) ((GraphElement) cachedElement).lineAlpha = 255;
                if(haveToSave) saveToInternalStorage(result, escapeThis(prefix(circleCrop) + cachedElement.getImageResource()));
            }
        }

        public static String escapeThis(String s){
            return s.replace("€", "&euro;")
                    .replace("\"", "&quot;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("/", "&#47;")
                    .replace("\\", "&#92;")
                    .replace(":", "&#58;")
                    .replace(".", "&#46;") + ".png";
        }

        private static String prefix(boolean circleCropped){
            String prefix = "&hos;";
            if(circleCropped) prefix = "&circleCropped;";
            return  prefix;
        }


        public static Bitmap getCircleCroppedBitmap(Bitmap bitmap) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            int radius = Math.min(bitmap.getHeight() / 2, bitmap.getWidth() / 2);
            canvas.drawCircle((bitmap.getWidth() / 2) - 1, bitmap.getHeight() / 2, radius-2, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            return output;
        }


        public static void saveToInternalStorage(Bitmap b, String name){
            File mypath =new File(CachePath(),name);

            FileOutputStream out;
            try {
                out = new FileOutputStream(mypath);
                b.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static Bitmap loadImageFromStorage(File path, String name)
        {
            try {
                File f = new File(path, name);
                if (!f.exists()) return null;
                return BitmapFactory.decodeStream(new FileInputStream(f));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        private static File _cachePath;
        @TargetApi(Build.VERSION_CODES.KITKAT)
        public static File CachePath(){
            if(_cachePath != null) return _cachePath;
            ContextWrapper cw = new ContextWrapper(APPLICATION_CONTEXT);

            String A = Environment.DIRECTORY_DOWNLOADS;
            _cachePath=new File(cw.getExternalFilesDirs(A)[1],"dislike");
            while(!_cachePath.getName().equals("com.corazza.fosco.dislike"))
                _cachePath = _cachePath.getParentFile();
            _cachePath=new File(_cachePath,"iHopeSenpaiWillNoticeMe");
            _cachePath.mkdirs();


            return _cachePath;
        }

    }

    public static String encode(String s){
        try {
            return URLEncoder.encode(s
                    .replace("'", "´"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }

    public static String decode(String s){
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }

}
