package com.p4u.parvarish.menu_data;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.p4u.parvarish.R;

import static java.util.Objects.requireNonNull;

public class ManageArticleFragment extends Fragment {

    private static final String TAG = "OurWorkFragment";
    private EditText editText;
    private TextView textView;
    private Button btnadd,btnupdate,btndelete;
    private Context context;
    private View v;
    private String s;
    private Spinner sp;
    private Bundle bundle;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_manage_articles, container, false);
        context = container.getContext();
        initViews();
        //s="TELENT_SUPPORT";
        bundle=new Bundle();
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(sp.getSelectedItem().equals("TELENT SUPPORT")){
                    s="TELENT_SUPPORT";
                }else if(sp.getSelectedItem().equals("TECHNICAL PARTNERSHIP")){
                    s="TECHNICAL_PARTNERSHIP";
                }else if(sp.getSelectedItem().equals("CAPACITY BUILDING")){
                    s="CAPACITY_BUILDING";
                }else if(sp.getSelectedItem().equals("INTERNSHIP PROGRAM")){
                    s="INTERNSHIP_PROGRAM";
                }else if(sp.getSelectedItem().equals("ACADEMIC PARTNER")){
                    s="ACADEMIC_PARTNER";
                }
                bundle.putString("Page",s);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                s="TELENT_SUPPORT";
                bundle.putString("Page",s);
            }
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, upload_article.class));
            }
        });
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(new ManageContentFragment());
            }
        });

        return v;
    }
    private void initViews(){
        btnadd=v.findViewById(R.id.BtnAdd);
        btnupdate=v.findViewById(R.id.BtnUpdate);
        sp=v.findViewById(R.id.sppagelist);
    }
    // switching fragment
    private void switchFragment(Fragment fragment) {

        fragment.setArguments(bundle);
        if (getActivity() != null) {
            requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack(null).commit();
        }
    }
}
