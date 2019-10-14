package com.p4u.parvarish.contact_us;

import android.content.Intent;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;


public class Element {

    private String title;
    private Integer iconDrawable;
    private Integer colorDay;
    private Integer colorNight;
    private String value;
    private Intent intent;
    private Integer gravity;
    private Boolean autoIconColor = true;

    private View.OnClickListener onClickListener;

    Element() {

    }

    public Element(String title, Integer iconDrawable) {
        this.title = title;
        this.iconDrawable = iconDrawable;
    }


    View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

       public Element setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

       public Integer getGravity() {
        return gravity;
    }

       public Element setGravity(Integer gravity) {
        this.gravity = gravity;
        return this;
    }


    @Nullable
    public String getTitle() {
        return title;
    }

       public Element setTitle(String title) {
        this.title = title;
        return this;
    }
    @DrawableRes
    @Nullable
    public Integer getIconDrawable() {
        return iconDrawable;
    }
    void setIconDrawable(@DrawableRes Integer iconDrawable) {
        this.iconDrawable = iconDrawable;
    }
    @ColorRes
    @Nullable
    public Integer getIconTint() {
        return colorDay;
    }
    void setIconTint(@ColorRes Integer color) {
        this.colorDay = color;
    }


    @ColorRes
    public Integer getIconNightTint() {
        return colorNight;
    }
    void setIconNightTint() {
        this.colorNight = android.R.color.white;
    }

    public String getValue() {
        return value;
    }

    public Element setValue(String value) {
        this.value = value;
        return this;
    }
    public Intent getIntent() {
        return intent;
    }


    public Element setIntent(Intent intent) {
        this.intent = intent;
        return this;
    }

    public Boolean getAutoApplyIconTint() {
        return autoIconColor;
    }

    public Element setAutoApplyIconTint(Boolean autoIconColor) {
        this.autoIconColor = autoIconColor;
        return this;
    }
}
