package com.corazza.fosco.dislike.utilities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.corazza.fosco.dislike.datatypes.GraphElement;

import java.util.ArrayList;
import java.util.Random;

public class GraphateCanvasView extends RelativeLayout {
    Paint paint = new Paint();
    ArrayList<GraphElement> elements;

    public ArrayList<GraphElement> getElements() {
        return elements;
    }

    public void setElements(ArrayList<GraphElement> elements) {
        this.elements = elements;
    }

    public GraphateCanvasView(Context context) {
        super(context);
        elements = new ArrayList<GraphElement>();
        paint.setColor(0xffaaff7f);
        paint.setStrokeWidth(2f);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);

    }

    @Override
    public void onDraw(Canvas canvas) {
        for(GraphElement son : getElements()){
            for(GraphElement father : son.directSuperiorsList){
                join(father,son,canvas);
            }
        }
    }

    private void join(GraphElement father, GraphElement son, Canvas canvas){
        LayoutParams fatherParameters = (LayoutParams) father.getImageView().getLayoutParams();
        LayoutParams sonParameters = (LayoutParams) son.getImageView().getLayoutParams();
        paint.setAlpha(Math.min(son.lineAlpha, son.lineAlpha));

        int x1 = fatherParameters.leftMargin + fatherParameters.width/2;
        int x2 = sonParameters.leftMargin + sonParameters.width/2;
        int y1 = fatherParameters.topMargin + fatherParameters.height/2;
        int y2 = sonParameters.topMargin + sonParameters.height/2;
        canvas.drawLine(x1,y1,x2,y2,paint);
    }

}
