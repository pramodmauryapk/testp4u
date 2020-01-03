package com.p4u.parvarish.MenuPages;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.p4u.parvarish.R;
import com.p4u.parvarish.gallary.TouchImageView;
import com.squareup.picasso.Picasso;

public class ArticledetailFragment extends Fragment {

    private static final String TAG = ArticledetailFragment.class.getSimpleName();

    private EditText editText;
    private TextView textView;
    private Button button;
    private Context context;

    private TextView mDescription,mTitle;
    private TouchImageView mImage;
    private View v;
    private String videoId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_article_detail, container, false);

        context = container.getContext();
        initViews();
        mImage.setVisibility(View.VISIBLE);
        assert this.getArguments() != null;
        String imagetitle = this.getArguments().getString("Title");
        String imageurl = this.getArguments().getString("Image");
        String desc=this.getArguments().getString("Description");
        if(imageurl!=null) {
            Picasso.get()
                    .load(imageurl)
                    .placeholder(R.drawable.placeholder)
                    .into(mImage);
        }else{
            mImage.setVisibility(View.GONE);
        }
            mDescription.setText(desc);
            mTitle.setText(imagetitle);


        return v;
    }
    private void initViews(){
        mTitle = v.findViewById(R.id.tvTitle);
        mImage = v.findViewById(R.id.ivImage);
        mDescription =v. findViewById(R.id.tvDescription);
    }


}
