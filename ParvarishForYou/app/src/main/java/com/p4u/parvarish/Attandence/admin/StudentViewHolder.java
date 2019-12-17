package com.p4u.parvarish.Attandence.admin;

import android.widget.CheckBox;
import android.widget.TextView;

class StudentViewHolder
{
    private CheckBox checkBox;


    private TextView textView1;
    private TextView textView2;
    public StudentViewHolder()
    {
    }

    public StudentViewHolder(TextView textView1, TextView textView2, CheckBox checkBox)
    {
        this.checkBox = checkBox;
        this.textView1 = textView1;
        this.textView2=textView2;
    }

    public CheckBox getCheckBox()
    {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox)
    {
        this.checkBox = checkBox;
    }
    public TextView getTextView1() {
        return textView1;
    }

    public void setTextView1(TextView textView1) {
        this.textView1 = textView1;
    }

    public TextView getTextView2() {
        return textView2;
    }

    public void setTextView2(TextView textView2) {
        this.textView2 = textView2;
    }


}

