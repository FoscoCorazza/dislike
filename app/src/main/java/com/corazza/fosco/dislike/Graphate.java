package com.corazza.fosco.dislike;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.corazza.fosco.dislike.datatypes.GraphElement;
import com.corazza.fosco.dislike.utilities.Global;
import com.corazza.fosco.dislike.utilities.GraphateCanvasView;
import com.corazza.fosco.dislike.utilities.Utilities;

import java.util.ArrayList;
import java.util.Random;


public class Graphate extends Activity {

    private GraphateCanvasView  canvas;
    public static final int minD = 100;
    public static final int maxD = 200;

    ArrayList<GraphElement> elements = new ArrayList<GraphElement>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphate);

        canvas = new GraphateCanvasView(this);
        canvas.setBackgroundColor(0x00000000);
        ((ScrollView)findViewById(R.id.graphate_scrollView)).addView(canvas);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
            findViewById(R.id.graphate_scrollView).setLayerType(View.LAYER_TYPE_SOFTWARE, null);

/*
        elements.add(GraphElement.LowQuaElement("A", getResources().getStringArray(R.array.imageTest)[5], 23));

        elements.add(GraphElement.LowQuaElement("B", getResources().getStringArray(R.array.imageTest)[9],26));
        elements.get(elements.size()-1).addAncestor( elements.get(0) ); // A

        elements.add(GraphElement.LowQuaElement("C", getResources().getStringArray(R.array.imageTest)[10],45));
        elements.get(elements.size()-1).addAncestor( elements.get(0) ); // A

        elements.add(GraphElement.LowQuaElement("D", getResources().getStringArray(R.array.imageTest)[3],7));
        elements.get(elements.size()-1).addAncestor( elements.get(1) ); // B
        elements.get(elements.size()-1).addAncestor( elements.get(2) ); // C

        elements.add(GraphElement.LowQuaElement("E", getResources().getStringArray(R.array.imageTest)[4],21));
        elements.get(elements.size()-1).addAncestor( elements.get(1) ); // B

        elements.add(GraphElement.LowQuaElement("F", getResources().getStringArray(R.array.imageTest)[11],240));
        elements.get(elements.size()-1).addAncestor( elements.get(0) ); // A
        elements.get(elements.size()-1).addAncestor( elements.get(3) ); // D
        elements.get(elements.size()-1).addAncestor( elements.get(4) ); // E

        elements.add(GraphElement.LowQuaElement("G", getResources().getStringArray(R.array.imageTest)[12],18));
        elements.get(elements.size()-1).addAncestor( elements.get(2) ); // C
        elements.get(elements.size()-1).addAncestor( elements.get(5) ); // F

        elements.add(GraphElement.LowQuaElement("H", getResources().getStringArray(R.array.imageTest)[13],22));
        elements.get(elements.size()-1).addAncestor( elements.get(2) ); // C

        elements.add(GraphElement.LowQuaElement("I", getResources().getStringArray(R.array.imageTest)[8],222));
        elements.get(elements.size()-1).addAncestor( elements.get(0) ); // A

        elements.add(GraphElement.LowQuaElement("L", getResources().getStringArray(R.array.imageTest)[15],87));
        elements.get(elements.size()-1).addAncestor( elements.get(0) ); // A
        elements.get(elements.size()-1).addAncestor( elements.get(2) ); // C
        elements.get(elements.size()-1).addAncestor( elements.get(0) ); // A

        elements.add(GraphElement.LowQuaElement("M", getResources().getStringArray(R.array.imageTest)[16], 87));
        elements.get(elements.size()-1).addAncestor( elements.get(0) ); // A
        elements.get(elements.size()-1).addAncestor( elements.get(2) ); // C

        elements.add(GraphElement.LowQuaElement("N", getResources().getStringArray(R.array.imageTest)[17], 12));
        elements.get(elements.size()-1).addAncestor( elements.get(0) ); // A
        elements.get(elements.size()-1).addAncestor( elements.get(5) ); // C

        elements = GraphElement.reorder(elements);
*/


    }

    @Override
    protected void onResume() {
        super.onResume();
        createGraph(canvas, GraphElement.reorder(Global.THIS_USER.elements));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.reverse_flip_in, R.anim.reverse_flip_out);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private ImageView addCircleTo(GraphateCanvasView rl, int w, int h, int x, int y, final GraphElement element) {
        ImageView iv = new ImageView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
        params.leftMargin = x;
        params.topMargin = y;
        rl.addView(iv, params);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

        final int canvasWidth = Utilities.deviceSize(this).x;

        iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if( element.textViewWhy.getVisibility() == View.INVISIBLE)
                    element.textViewWhy.setVisibility(View.VISIBLE);
                else
                    element.textViewWhy.setVisibility(View.INVISIBLE);
                return false;
            }
        });


        iv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                view.getParent().getParent().requestDisallowInterceptTouchEvent(true);

                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN :
                    {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        float dx = event.getRawX() - params.leftMargin;
                        float dy = event.getRawY() - params.topMargin;


                        view.setTag(new Point((int)(dx), (int)(dy)));
                    }
                    break;
                    case MotionEvent.ACTION_MOVE :
                    {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        float x = event.getRawX();
                        float y = event.getRawY();

                        x = (x-((Point)view.getTag()).x);
                        y = (y -((Point)view.getTag()).y);

                        if(x + view.getWidth() >= canvasWidth)
                            x = canvasWidth - view.getWidth();

                        x = Math.max(x, 0);
                        y =  Math.max((int)y, 0);

                        element.setX((int)x);
                        element.setY((int)y);



                        if(view.getParent() instanceof GraphateCanvasView) ((GraphateCanvasView)view.getParent()).invalidate();
                    }
                    break;
                    case MotionEvent.ACTION_UP :
                    {

                    }
                }
                return false;
            }
        });


        //iv.setBackground(getResources().getDrawable(R.drawable.circletest));
        return iv;
    }
    private ImageView addCircleTo(GraphateCanvasView rl, int d, int x, int y, GraphElement element) { return addCircleTo(rl, d, d, x, y, element);}

    private int compressRow(int row, ArrayList<GraphElement> elements, int canvasWidth){
        ArrayList<GraphElement> thisRow = new ArrayList<GraphElement>();
        int  totalWidth = 0, maxDiameter = 0;
        //Prelevo tutti gli elementi della Row
        for (GraphElement element : elements) {
            if(element.virtualRow == row) {
                thisRow.add(element);
                totalWidth += element.getDiamater();
                if(maxDiameter<element.getDiamater()) maxDiameter=element.getDiamater();
            }
        }

        if(thisRow.size() == 0) return -1;

        int y = thisRow.get(0).getY();
        int rY=0;
        double area = 0;
        double p = 0;
        GraphElement A,B;
        int span = 10;
        int h;
        double lAB,lBC,lCA;

        //Posiziono e centro nelle X
        int proportionalWidth;
        int precedentWidth = 0;
        for (GraphElement C : thisRow) {
            proportionalWidth = canvasWidth * C.getDiamater() / totalWidth;
            centerElementIn(C, precedentWidth + ( proportionalWidth / 2), y + maxDiameter / 2);
            precedentWidth += proportionalWidth;

            //Ora inizia una roba dettagliata: SE l'elemento C ha un precedente e lo tocca, allora
            //questo precedente va abbassato.
            if(thisRow.indexOf(C) >= 2 && thisRow.indexOf(C) % 2 == 0 && C.touches(thisRow.get(thisRow.indexOf(C) - 1),0)){
                A = thisRow.get(thisRow.indexOf(C) - 2);
                B = thisRow.get(thisRow.indexOf(C) - 1);
                lCA = A.distanceFrom(C);
                lBC = B.getDiamater()/2 + C.getDiamater()/2 + span;
                lAB = B.getDiamater()/2 + A.getDiamater()/2 + span;
                p = (lAB + lBC + lCA)/2;
                area = Math.sqrt(p*(p-lAB)*(p-lCA)*(p-lBC));
                h = (int)(2*area / lCA) ;

                B.setY(B.getY() + h);
                rY = Math.max(rY,B.getY()+B.getDiamater());
                rY = Math.max(rY,A.getY()+A.getDiamater());
            }

            rY = Math.max(rY,C.getY()+C.getDiamater());
        }


        return rY;
    }

    private void centerElementIn(GraphElement element, int x, int y){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) element.getImageView().getLayoutParams();
        params.topMargin = y - params.height/2;
        params.leftMargin = Math.max(0,x - params.width/2);
        if(params.leftMargin + element.getDiamater() >= Utilities.deviceSize(this).x)
            params.leftMargin = Utilities.deviceSize(this).x - element.getDiamater();

    }

    private void createGraph(GraphateCanvasView  rl, ArrayList<GraphElement> elements) {

        rl.removeAllViews();
        int canvasWidth = Utilities.deviceSize(this).x;
        int biggerMeNeither = worstIn(elements).getMeNeither();
        int smallerMeNeither = bestIn(elements).getMeNeither();
        int thisDiameter = 0, prevDiameter = 0;
        int thisY = 0, prevY = 0;
        int spanH = 20;
        int prevRow = 0;
        int prevRowLowerPoint = 0;

        for (GraphElement element : elements) {
            //Calculate Diameter.
            thisDiameter = diameterValue(smallerMeNeither, biggerMeNeither,
                                         minD, maxD, element.getMeNeither());

            //Al cambio di riga la precedente va compressa. Inoltre la centra orizzontalmente
            if(prevRow != element.virtualRow)
                thisY = compressRow(prevRow, elements, canvasWidth) + spanH;

            //AddCircle. Non è necessaria una X, in quanto viene calcolata in compressRow();
            element.setImageView(addCircleTo(rl, thisDiameter, 0, thisY, element));
            element.initTextView(rl,this);
            Utilities.ImageTasks.downloadImageViewContent(element,  element.getImageView(), true);



            //Update "prevVars".
            prevDiameter = thisDiameter;
            prevY = thisY;
            prevRow = element.virtualRow;
        }

        compressRow(prevRow, elements, canvasWidth);

        rl.setElements(elements);
    }


    private GraphElement worstIn(ArrayList<GraphElement> elements) {
        GraphElement worst = GraphElement.LowQuaElement("Dummy","", -1);
        for (GraphElement element : elements) {
            if (worst.getMeNeither() < element.getMeNeither())
                worst = element;
        }
        return worst;
    }

    private GraphElement bestIn(ArrayList<GraphElement> elements) {
        GraphElement best = GraphElement.LowQuaElement("Dummy","", Integer.MAX_VALUE-1);
        for (GraphElement element : elements) {
            if (best.getMeNeither() > element.getMeNeither())
                best = element;
        }
        return best;
    }

    private int diameterValue(int minValue, int maxValue, int minLimit, int maxLimit, int x) {

        double radix = 1 / 4f;
        double weakenedValue = Math.pow((double) x, radix);
        double weakenedMinValue = Math.pow((double) minValue, radix);
        double weakenedMaxValue = Math.pow((double) maxValue, radix);
        double diameterValue = minLimit + (maxLimit - minLimit) * (weakenedValue - weakenedMinValue) / (weakenedMaxValue - weakenedMinValue + 1);

        return (int) diameterValue;

    }



}
