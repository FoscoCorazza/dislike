package com.corazza.fosco.dislike.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Html;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

public class AdvancedTextView extends TextView {
    private String ELLIPSIS = "...";

    private boolean isEllipsized;
    private boolean isStale;
    private boolean programmaticChange;
    private CharSequence fullText;
    private int maxLines = -1;
    private float lineSpacingMultiplier = 1.0f;
    private float lineAdditionalVerticalPadding = 0.0f;

    public AdvancedTextView(Context context) {
        super(context);
    }

    public AdvancedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdvancedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isEllipsized() {
        return isEllipsized;
    }

    @Override
    public void setMaxLines(int maxLines) {
        super.setMaxLines(maxLines);
        this.maxLines = maxLines;
        isStale = true;
    }

    public int getMaxLines() {
        return maxLines;
    }

    @Override
    public void setLineSpacing(float add, float mult) {
        this.lineAdditionalVerticalPadding = add;
        this.lineSpacingMultiplier = mult;
        super.setLineSpacing(add, mult);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);
        if (!programmaticChange) {
            fullText = text;
            isStale = true;
        }
    }




    @Override
    protected void onDraw(Canvas canvas) {
        if (isStale) {
            super.setEllipsize(null);
            resetText();
        }
        super.onDraw(canvas);
    }


    private void resetText() {
        int maxLines = getMaxLines();
        String workingText = Html.fromHtml(fullText.toString()).toString();
        String originalText = workingText;
        String newPut = fullText + "";
        Layout layout = createWorkingLayout(workingText);
        if (layout.getLineCount() > maxLines && maxLines != -1) {
            workingText = originalText.substring(0, layout.getLineEnd(maxLines - 1)).trim();

            //Taglia all'ultimo spazio utile, per non troncare la parola a metà.
            while (createWorkingLayout(workingText + ELLIPSIS).getLineCount() > maxLines) {
                int lastSpace = workingText.lastIndexOf(' ');
                if (lastSpace == -1)
                    break;
                workingText = workingText.substring(0, lastSpace);
            }

        }

        if (!workingText.equals(originalText)) {

            //TODO: questa cosa vuol funziona solo se le funzioni spannable sono PRIMA del taglio.
            int l = fullText.length() - Html.fromHtml(fullText.toString()).toString().length();
            newPut = fullText.subSequence(0, workingText.length() + l) + " " + ELLIPSIS;

        }

        setText(Html.fromHtml(newPut), BufferType.SPANNABLE);

        isStale = false;
    }

    private Layout createWorkingLayout(String workingText) {
        return new StaticLayout(workingText, getPaint(), getWidth() - getPaddingLeft() - getPaddingRight(),
                Alignment.ALIGN_NORMAL, lineSpacingMultiplier, lineAdditionalVerticalPadding, false);
    }

    @Override
    public void setEllipsize(TruncateAt where) {
    }


}