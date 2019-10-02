package com.p4u.parvarish.galary;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class CarouselLayout extends LinearLayout {
    private float scale = CarouselPagerAdapter.BIG_SCALE;

    public CarouselLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CarouselLayout(Context context) {
        super(context);
    }

    public void setScaleBoth(float scale) {
        this.scale = scale;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = this.getWidth();
        int h = this.getHeight();
        canvas.scale(scale, scale, w/2, h/2);
    }
}
