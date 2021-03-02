package com.rxmediaapp.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomMediumTextView extends TextView {

    public CustomMediumTextView(Context context) {
        super(context);
        init();
    }

    public CustomMediumTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    public CustomMediumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    private void init() {

        if (!isInEditMode()) {

            Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"Raleway-SemiBold.ttf");
            setTypeface(tf);

        }

    }

}
