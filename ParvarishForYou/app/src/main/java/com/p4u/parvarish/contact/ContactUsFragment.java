package com.p4u.parvarish.contact;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import com.p4u.parvarish.R;

public class ContactUsFragment extends Fragment {

    private static final String TAG = "ContactUsFragment";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View contactPage = inflater.inflate(R.layout.contact_page, container, false);
       // simulateDayNight(/* DAY */);
        Element adsElement = new Element();
        adsElement.setTitle("Join with us");

        contactPage = new ContactPage(getActivity())
                .isRTL()
                .setImage(R.drawable.logo)
                .addItem(new Element().setTitle("Version 1.1"))
                .addItem(adsElement)
                .addGroup()
                .addEmail()
                .addWebsite()
                .addFacebook()
                // .addTwitter("medyo80")
                .addYoutube()
                // .addPlayStore("com.ideashower.readitlater.pro")
                // .addInstagram("medyo80")
                // .addGitHub("medyo")
                .addItem(getCopyRightsElement())
                .create();


        //initViews();

        return contactPage;
    }
    private void initViews(){

    }
    private Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.about_icon_copy_right);
        copyRightsElement.setIconTint(R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint();
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ContactUsFragment.this.getActivity(), copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }
/*
    private void simulateDayNight() {
        final int DAY = 0;
        final int NIGHT = 1;
        final int FOLLOW_SYSTEM = 3;

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode != Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }
    }*/

}
