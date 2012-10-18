package com.shopify.android.beermon.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;
import com.shopify.android.beermon.R;

/**
 * Created with IntelliJ IDEA.
 * User: carson
 * Date: 2012-10-18
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class SlantedTextView extends TextView {
    public int angle;

    private void setupAngle(Context context, AttributeSet attrs) {
        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.SlantedText);
        this.angle = styledAttrs.getInt(R.styleable.SlantedText_slantedTextAngle, 0);
    }

    public SlantedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupAngle(context, attrs);
    }

    public SlantedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupAngle(context, attrs);
    }

    public SlantedTextView(Context context) {
        super(context);
        angle = 0;
    }

    protected void onDraw(Canvas canvas) {
        //This saves the matrix that the canvas applies to draws, so it can be restored later.
        canvas.save();

        //now we change the matrix
        //We need to rotate around the center of our text
        //Otherwise it rotates around the origin, and that's bad.
        float py = this.getHeight()/2.0f;
        float px = this.getWidth()/2.0f;
        canvas.rotate(angle, px, py);

        //draw the text with the matrix applied.
        super.onDraw(canvas);

        //restore the old matrix.
        canvas.restore();
    }
}
