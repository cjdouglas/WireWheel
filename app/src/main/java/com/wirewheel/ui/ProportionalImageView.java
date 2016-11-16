package com.wirewheel.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Chris on 10/1/2016.
 *
 * Thanks to ryadel.com for this code and explanation
 * http://www.ryadel.com/en/android-proportionally-stretch-imageview-fit-whole-screen-width-maintaining-aspect-ratio/
 *
 * This class provides an ImageView which fills the image horizontally and
 * provides the appropriate vertical height to match the original aspect ratio.
 */
public class ProportionalImageView extends ImageView {

    /**
     * Constructor for the ProportionalImageView
     * @param context The Context this View will be displayed in
     */
    public ProportionalImageView(Context context) {
        super(context);
    }

    /**
     * Constructor for the ProportionalImageView with a provided AttributeSet
     * @param context The Context this View will be displayed in
     * @param attrs The attributes of the XML tag inflating this View
     */
    public ProportionalImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructor for the ProportionalImageView with provided AttributeSet and Style
     * @param context The Context this View will be displayed in
     * @param attrs The attributes of the XML tag inflating this View
     * @param defStyle The default style of this View
     */
    public ProportionalImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Re-sizes the image to the given dimensions
     * @param widthMeasureSpec The width
     * @param heightMeasureSpec The height
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();
        if (d != null) {
            int w = MeasureSpec.getSize(widthMeasureSpec);
            int h = w * d.getIntrinsicHeight() / d.getIntrinsicWidth();
            setMeasuredDimension(w, h);
        }
        else super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
