package com.p4u.parvarish.fancydialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import com.p4u.parvarish.R;

public class FancyAlertDialog {


    private FancyAlertDialog(Builder builder){
        String title = builder.title;
        String message = builder.message;
        Activity activity = builder.activity;
        int icon = builder.icon;
        Animation animation = builder.animation;
        Icon visibility = builder.visibility;
        FancyAlertDialogListener pListener = builder.pListener;
        FancyAlertDialogListener nListener = builder.nListener;
        String positiveBtnText = builder.positiveBtnText;
        String negativeBtnText = builder.negativeBtnText;
        int pBtnColor = builder.pBtnColor;
        int nBtnColor = builder.nBtnColor;
        int bgColor = builder.bgColor;
        boolean cancel = builder.cancel;
    }


    public static class Builder{
        private static final String TAG = "FancyAlertDialog";
        private String title,message,positiveBtnText,negativeBtnText,input;
        private Activity activity;
        private int icon;
        private Icon visibility;
        private Animation animation;
        private FancyAlertDialogListener pListener,nListener;
        private int pBtnColor,nBtnColor,bgColor;
        private boolean cancel;

        public Builder(Activity activity){
            this.activity=activity;
        }
        public Builder setTitle(String title){
            this.title=title;
            return this;
        }

        public Builder setBackgroundColor(int bgColor){
            this.bgColor=bgColor;
            return this;
        }

        public Builder setMessage(String message){
            this.message=message;
            return this;
        }
        public Builder setPositiveBtnText(String positiveBtnText){
            this.positiveBtnText=positiveBtnText;
            return this;
        }

        public Builder setPositiveBtnBackground(int pBtnColor){
            this.pBtnColor=pBtnColor;
            return this;
        }

        public Builder setNegativeBtnText(String negativeBtnText){
            this.negativeBtnText=negativeBtnText;
            return this;
        }

        public Builder setNegativeBtnBackground(int nBtnColor){
            this.nBtnColor=nBtnColor;
            return this;
        }


        //setIcon
        public Builder setIcon(int icon, Icon visibility){
            this.icon=icon;
            this.visibility=visibility;
            return this;
        }

        public Builder setAnimation(Animation animation){
            this.animation=animation;
            return this;
        }

        //set Positive listener
        public Builder OnPositiveClicked(FancyAlertDialogListener pListener){
            this.pListener=pListener;
            return this;
        }

        //set Negative listener
        public Builder OnNegativeClicked(FancyAlertDialogListener nListener){
            this.nListener=nListener;
            return this;
        }

        public Builder isCancellable(boolean cancel){
            this.cancel=cancel;
            return this;
        }

        public FancyAlertDialog build(){
            TextView message1,title1;
            ImageView iconImg;
            Button nBtn,pBtn;
            View view;
            final Dialog dialog;
            if(animation==Animation.POP)
            dialog=new Dialog(activity,R.style.PopTheme);
            else if(animation==Animation.SIDE)
            dialog=new Dialog(activity, R.style.SideTheme);
            else if(animation==Animation.SLIDE)
            dialog=new Dialog(activity,R.style.SlideTheme);
            else
            dialog=new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(cancel);
            dialog.setContentView(R.layout.fancyalertdialog);

            //getting resources
            view=dialog.findViewById(R.id.background);
            title1= dialog.findViewById(R.id.title);
            message1=dialog.findViewById(R.id.message);
            iconImg=dialog.findViewById(R.id.icon);
            nBtn=dialog.findViewById(R.id.negativeBtn);
            pBtn=dialog.findViewById(R.id.positiveBtn);
            title1.setText(title);
            message1.setText(message);
            if(positiveBtnText!=null)
            pBtn.setText(positiveBtnText);
            if(pBtnColor!=0)
            { GradientDrawable bgShape = (GradientDrawable)pBtn.getBackground();
              bgShape.setColor(pBtnColor);
            }
            if(nBtnColor!=0)
            { GradientDrawable bgShape = (GradientDrawable)nBtn.getBackground();
              bgShape.setColor(nBtnColor);
            }
            if(negativeBtnText!=null)
            nBtn.setText(negativeBtnText);
            iconImg.setImageResource(icon);
            if(visibility==Icon.Visible)
            iconImg.setVisibility(View.VISIBLE);
            else
            iconImg.setVisibility(View.GONE);
            if(bgColor!=0)
            view.setBackgroundColor(bgColor);
            if(pListener!=null) {
                pBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view1) {
                        pListener.OnClick();
                        dialog.dismiss();
                    }
                });
            }
            else{
                pBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view12) {
                        dialog.dismiss();
                    }
                });
            }

            if(nListener!=null){
                nBtn.setVisibility(View.VISIBLE);
                nBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view13) {
                        nListener.OnClick();

                        dialog.dismiss();
                    }
                });
            }


            dialog.show();

            return new FancyAlertDialog(this);

        }
    }

}
