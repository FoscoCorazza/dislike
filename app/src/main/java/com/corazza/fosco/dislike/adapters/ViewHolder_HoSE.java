package com.corazza.fosco.dislike.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.corazza.fosco.dislike.HallOfShame;
import com.corazza.fosco.dislike.HallOfShameDetail;
import com.corazza.fosco.dislike.datatypes.HoSElement;
import com.corazza.fosco.dislike.R;
import com.corazza.fosco.dislike.utilities.DatabaseUtilities;
import com.corazza.fosco.dislike.utilities.Global;
import com.corazza.fosco.dislike.utilities.Utilities;

public class ViewHolder_HoSE {
    public static String htmlBecauseThis = "<font color=\"#ff7f7f\" size = 2>perché a mio parere </font>";
    public static String htmlBecauseYes = "<font color=\"#ff7f7f\" size = 2>perché fanculo, ecco perché.</font>";
    public static String htmlHeader = "";
    public static int vfOpened = -1;

    LinearLayout view;
    ImageView ivWhat;
    TextView tvWho;
    TextView tvWhat;
    AdvancedTextView tvWhy;
    ViewFlipper vfButtons;
    TextView buttonMeNeither;
    TextView buttonSeeDetails;
    public boolean iDislikeThisAlready;



    public static ViewHolder_HoSE generateHolder(View convertView, boolean firstLoad){
        if(firstLoad){
            ViewHolder_HoSE holder = new ViewHolder_HoSE();
            holder.view = (LinearLayout) convertView;
            holder.tvWho = (TextView) convertView.findViewById(R.id.hos_textView_who);
            holder.tvWhat = (TextView) convertView.findViewById(R.id.hos_textView_what);
            holder.tvWhy = (AdvancedTextView) convertView.findViewById(R.id.hos_textView_why);
            holder.ivWhat = (ImageView) convertView.findViewById(R.id.hos_imageView_what);
            holder.buttonMeNeither = (TextView) convertView.findViewById(R.id.hos_textButton_meNeither);
            holder.buttonSeeDetails = (TextView) convertView.findViewById(R.id.hos_textButton_details);
            holder.vfButtons = ((ViewFlipper) convertView.findViewById(R.id.hos_bottomFlipper));
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
               convertView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            convertView.setTag(holder);
        }

        return (ViewHolder_HoSE) convertView.getTag();
    }


    public static View inflateConvertView(View convertView,  Context context, int resourceId){
        if(convertView != null) return convertView;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        return mInflater.inflate(resourceId, null);

    }


    public static void populateHolder(ViewHolder_HoSE holder, HoSElement element, int position, int maxLines, boolean firstLoad){
        Utilities.ImageTasks.downloadImageViewContent(element, holder.ivWhat, false);
        element.viewHolder = holder;

        holder.iDislikeThisAlready = Global.THIS_USER.hates(element);
        holder.tvWhat.setText(element.getName());

        //Gestione WHY
        if (element.getWhy().trim().equals("")) {
            htmlHeader = htmlBecauseYes;
            //convertView.setBackground(context.getResources().getDrawable(R.drawable.red_border_rectangle));
        } else
            htmlHeader = htmlBecauseThis;

        holder.tvWhy.setEllipsize(TextUtils.TruncateAt.END);
        holder.tvWhy.setMaxLines(maxLines);
        holder.tvWhy.setText(htmlHeader + element.getWhy(), TextView.BufferType.SPANNABLE);
        holder.tvWhy.setMovementMethod(new ScrollingMovementMethod());
        if(position == vfOpened) holder.vfButtons.setDisplayedChild(1);
        applyConditionalChangesInCaseOf_MeNeither(element);



    }


    public static void applyConditionalChangesInCaseOf_MeNeither(HoSElement element){
        ViewHolder_HoSE holder = element.viewHolder;

        String others = "";
        if (element.getMeNeither() == 1)
            others = " e ad un altro";
        if (element.getMeNeither() > 1)
            others = " e ad altri " + element.getMeNeither();

        holder.tvWho.setText("A " + element.getWho() + others + " non piace");

        holder.buttonMeNeither.setTypeface(null, Typeface.NORMAL);

        if(holder.iDislikeThisAlready){
            holder.buttonMeNeither.setText(holder.buttonMeNeither.getRootView().getContext().getResources().getString(R.string.changed_my_mind));
            holder.buttonMeNeither.setTypeface(null, Typeface.ITALIC);
            holder.view.setBackgroundResource(R.drawable.green_border_rectangle_meneither);
        }
        else{
            holder.buttonMeNeither.setText(holder.buttonMeNeither.getRootView().getContext().getResources().getString(R.string.me_neither));
            holder.view.setBackgroundResource(R.drawable.green_border_rectangle);}

    }

    public static void addListeners(View convertView, ViewHolder_HoSE holder, final Context context, final HoSElement element, final int position){
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vfOpened != position && viewFlipperAt(view, vfOpened) != null) {viewFlipperAt(view, vfOpened).showPrevious();}
                if(vfOpened != position) {
                    vfOpened = position;
                }
                else {vfOpened = -1;}
                flipVF(view, context, element);
            }
        });

        holder.buttonMeNeither.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vfOpened = -1;
                flipVF((View) view.getParent().getParent(), context, element);
                if(((TextView)view).getText().equals(context.getResources().getString(R.string.me_neither)))
                    DatabaseUtilities.setNewDislike(((HallOfShame)context), Global.THIS_USER, element);
                else
                    DatabaseUtilities.delOldDislike(((HallOfShame)context), Global.THIS_USER, element);
            }
        });

        holder.buttonSeeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vfOpened = -1;
                flipVF((View) view.getParent().getParent(),context, element);
                seeDetailsPressed = true;
            }
        });

    }

    private static boolean seeDetailsPressed = false;

    private static void goToDetailActivity(Context context, HoSElement element){
        HallOfShame.errantElement = element;
        Intent k = new Intent(context, HallOfShameDetail.class);
        context.startActivity(k);
        //((Activity) context).overridePendingTransition(R.anim.flip_in,R.anim.flip_out);
    }

    //UTILITIES PER I LAYOUT

    private static void flipVF(View view, final Context context, final HoSElement element){
        ViewFlipper vf = ((ViewFlipper) view.findViewById(R.id.hos_bottomFlipper));
        vf.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.flip_in));
        vf.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.flip_out));
        vf.showNext();

        vf.getOutAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(seeDetailsPressed)   goToDetailActivity(context, element);
                seeDetailsPressed  =    false;
            }

        });


        }

    public static ViewFlipper viewFlipperAt(View brotherView, int position){
        if(position == -1) return null;
        ListView fatherView = (ListView) brotherView.getParent();
        int p =  position - fatherView.getFirstVisiblePosition() ;
        View elementView = fatherView.getChildAt(p);
        if(p > -1 && elementView != null)
            return ((ViewFlipper) elementView.findViewById(R.id.hos_bottomFlipper));
        else
            return null;
    }

    /*
    public static View getChildAtPosition(final AdapterView view, final int position) {
        final int index = position - view.getFirstVisiblePosition();
        if ((index >= 0) && (index < view.getChildCount())) {
            return view.getChildAt(index);
        } else {
            return null;
        }
    }


    public static void smoothScrollToPosition(View brotherView, final int position){
        if(position == -1) return;
        final AbsListView view = (ListView) brotherView.getParent();

        View child = getChildAtPosition(view, position);
        // There's no need to scroll if child is already at top or view is already scrolled to its end
        if ((child != null) && ((child.getTop() == 0) || ((child.getTop() > 0) && !view.canScrollVertically(1)))) {
            return;
        }

        view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final AbsListView view, final int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    view.setOnScrollListener(null);

                    // Fix for scrolling bug
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            view.setSelection(position);
                        }
                    });
                }
            }

            @Override
            public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount,
                                 final int totalItemCount) { }
        });

        // Perform scrolling to position
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                view.smoothScrollToPositionFromTop(position, 0);
            }
        });

        //fatherView.setSelection(position);
        /*do{
            fatherView.smoothScrollToPosition(position - i);
            i++;
        }while(fatherView.getFirstVisiblePosition() != position);

    }*/
}
