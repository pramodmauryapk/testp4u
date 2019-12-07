package com.p4u.parvarish.navistrip;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.p4u.parvarish.R;

import java.util.Objects;

public class HelpingFragment extends Fragment {
    private ViewPager mViewPager;
    private FragmentPagerAdapter adapterViewPager;
    private View v;
    private Context context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.helping_main, container, false);
        context = container.getContext();









        return v;
    }
    public void onStart() {

        super.onStart();
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(context, Objects.requireNonNull(getActivity()).getSupportFragmentManager());
        ViewPager viewPager = v. findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = v.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }


}