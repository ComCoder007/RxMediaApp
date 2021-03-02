package com.rxmediaapp.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class CustomNormalButton extends Button {

    public CustomNormalButton(Context context) {

        super(context);
        init();
    }

    public CustomNormalButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomNormalButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"GothamRoundedBook_21018.ttf");
            setTypeface(tf);
        }
    }
}

