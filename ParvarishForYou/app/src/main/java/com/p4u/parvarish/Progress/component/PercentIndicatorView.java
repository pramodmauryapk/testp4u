package com.p4u.parvarish.Progress.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

@SuppressLint({"ViewConstructor", "AppCompatCustomView"})
public class PercentIndicatorView extends TextView {

  private final int parentWidth;
  private int textColor;

  public PercentIndicatorView(Context context, int parentWidth, int textColor) {
    super(context);
    this.parentWidth = parentWidth;
    this.textColor = textColor;
    init();
  }

  private void init() {
    int textSize = (35 * parentWidth) / 700;
    setTextSize(textSize);
    setTextColor(this.textColor);
    setGravity(Gravity.CENTER);
    setAlpha(0.8f);
  }

  @SuppressLint("SetTextI18n")
  public void setPercent(int percent) {
    setText(percent + "%");
  }

  public void startAlphaAnimation() {
    AlphaAnimation alphaAnimation = new AlphaAnimation (1, 0);
    alphaAnimation.setDuration(700);
    alphaAnimation.setFillAfter(true);
    startAnimation(alphaAnimation);
  }
}
