package my.app.p4ulibrary.Progress.component.finish;

import android.annotation.SuppressLint;
import android.content.Context;

import my.app.p4ulibrary.R;


/**
 * @author jlmd
 */
@SuppressLint("ViewConstructor")
public class FinishedOkView extends FinishedView {

  public FinishedOkView(Context context, int parentWidth, int mainColor, int secondaryColor,
                        int tintColor) {
    super(context, parentWidth, mainColor, secondaryColor, tintColor);
  }

  @Override
  protected int getDrawable() {
    return R.drawable.ic_checked_mark;
  }

  @Override
  protected int getDrawableTintColor() {
    return tintColor;
  }

  @Override
  protected int getCircleColor() {
    return mainColor;
  }
}
