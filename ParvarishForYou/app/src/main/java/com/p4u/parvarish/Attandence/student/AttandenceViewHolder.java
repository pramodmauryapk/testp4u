package com.p4u.parvarish.Attandence.student;

import android.widget.TextView;

public class AttandenceViewHolder
{
    private TextView textView3;



    private TextView textView1;
    private TextView textView2;
    public AttandenceViewHolder()
    {
    }

    public AttandenceViewHolder(TextView textView1, TextView textView2, TextView textView3)
    {
        this.textView3 = textView3;
        this.textView1 = textView1;
        this.textView2 = textView2;
    }

    public TextView getTextView3() {
        return textView3;
    }

    public void setTextView3(TextView textView3) {
        this.textView3 = textView3;
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

