package com.corazza.fosco.dislike.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.corazza.fosco.dislike.R;
import com.corazza.fosco.dislike.datatypes.HoSElement;

import java.util.List;

/**
 * Created by Simone on 08/09/2014.
 */
public class ArrayAdapterForHoS extends ArrayAdapter<HoSElement> {

    Context context;
    int resourceId;

    public ArrayAdapterForHoS(Context context, int resourceId,
                                 List<HoSElement> items) {
        super(context, resourceId, items);
        this.resourceId = resourceId;
        this.context = context;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder_HoSE holder;
        final HoSElement element = getItem(position);

        boolean firstLoad = (convertView == null);

        convertView =   ViewHolder_HoSE.inflateConvertView(convertView, context, resourceId);
        holder =        ViewHolder_HoSE.generateHolder(convertView, firstLoad);
                        ViewHolder_HoSE.populateHolder(holder, element, position, 3, firstLoad);
                        ViewHolder_HoSE.addListeners(convertView, holder, context, element, position);


        return convertView;
    }

}