package com.p4u.parvarish.splash_activity.component.finish;

import android.annotation.SuppressLint;
import android.content.Context;

import com.p4u.parvarish.R;


@SuppressLint("ViewConstructor")
public class FinishedFailureView extends FinishedView {

  public FinishedFailureView(Context context, int parentWidth, int mainColor, int secondaryColor,
                             int tintColor) {
    super(context, parentWidth, mainColor, secondaryColor, tintColor);
  }

  @Override
  protected int getDrawable() {
    return R.drawable.ic_failure_mark;
  }

  @Override
  protected int getDrawableTintColor() {
    return tintColor;
  }

  @Override
  protected int getCircleColor() {
    return secondaryColor;
  }
}