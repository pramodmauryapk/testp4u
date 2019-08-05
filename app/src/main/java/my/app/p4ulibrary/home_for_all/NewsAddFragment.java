package my.app.p4ulibrary.home_for_all;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import my.app.p4ulibrary.R;
import my.app.p4ulibrary.main_menu.HomeFragment;

public class NewsAddFragment extends HomeFragment {


    private DatabaseReference myref;

    private View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_add_news,container,false);

        myref = FirebaseDatabase.getInstance().getReference("News");

        initViews();



        return v;
    }
    private void initViews(){

    }


}
