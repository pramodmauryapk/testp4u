package com.p4u.parvarish.chatlist;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.p4u.parvarish.R;

public class chatlistfragment extends Fragment {
    private String[] name;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_chatlist, container, false);
        Context context = container.getContext();


        name = new String[]{ "Christopea", "Davidbeck", "Lissameri", "DanielJack", "Markburg","Tommy Gilf","Lucifer","Merilin Chris","Emma Watson" };

        String[] message = new String[]{"Hey, How are you ?", "Sound good to me", "I am hearing musics", "join me at evening", "What kind of movie do you like ?", "Hi.Nice to meet you", "Do reach me soon", "Let us go for the jack part ?", "Sure, See you in a bit!"};


        int[] image = new int[]{R.drawable.image1, R.drawable.image2,
                R.drawable.image4, R.drawable.image3,
                R.drawable.image5, R.drawable.image6, R.drawable.image7, R.drawable.image8, R.drawable.image9};

        // Locate the ListView in listview_main.xml
        ListView list = v.findViewById(R.id.mylist);

        // Pass results to ListViewAdapter Class
        ListAdapter adapter = new ListAdapter(context, name, message, image);
        // Binds the Adapter to the ListView
        list.setAdapter(adapter);
        // Capture ListView item click
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Toast.makeText(getActivity(),"You have selected :"+name[position], Toast.LENGTH_SHORT).show();


            }

        });
        return v;
    }
}
