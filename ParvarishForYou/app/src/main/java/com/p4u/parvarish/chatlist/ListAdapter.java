package com.p4u.parvarish.chatlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.p4u.parvarish.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class ListAdapter extends BaseAdapter {

    // Declare Variables
    private Context context;
    private String[] name,message;
    private int[] image;

    ListAdapter(Context context, String[] name, String[] message, int[] image) {
        this.context = context;
        this.name = name;
        this.message = message;
        this.image = image;
    }

    @Override
    public int getCount() {
        return name.length;
    }


    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables
        TextView txtname,txtmessage;
        CircleImageView imagename;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        @SuppressLint("ViewHolder") View itemView = inflater.inflate(R.layout.item_list, parent, false);

        // Locate the TextViews in listview_item.xml
        txtname = itemView.findViewById(R.id.txt_name);

        txtmessage = itemView.findViewById(R.id.txt_msg);

        // Locate the ImageView in listview_item.xml
        imagename =  itemView.findViewById(R.id.image_data);

        // Capture position and set to the TextViews
        txtname.setText(name[position]);

        txtmessage.setText(message[position]);


        // Capture position and set to the ImageView
        imagename.setImageResource(image[position]);

        return itemView;
    }
}
