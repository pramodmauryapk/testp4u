package com.p4u.parvarish.menu_data;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.p4u.parvarish.R;
import com.squareup.picasso.Picasso;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class LayoutArticleList extends ArrayAdapter<Article_Model>{
    private Activity context;
    private List<Article_Model> aricles;
    public String s;

    public LayoutArticleList(Activity context, List<Article_Model> aricles) {
        super(context, R.layout.layout_article, aricles);
        this.context = context;
        this.aricles = aricles;
    }



    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.layout_article, null, true);
        try {


            TextView Title = listViewItem.findViewById(R.id.headingTv);
            TextView Article = listViewItem.findViewById(R.id.articleTv);
            ImageView img = listViewItem.findViewById(R.id.rImageView);
            Article_Model articles = aricles.get(position);
            Title.setText(articles.getTitle());
            Article.setText(articles.getDescription());
            if(articles.getImageUrl()!=null) {
                Picasso.get()
                        .load(articles.getImageUrl())
                        .placeholder(R.drawable.loading)
                        .fit()
                        .into(img);
            }else{
                img.setVisibility(View.GONE);
            }

        }
        catch (Exception e){
            Log.d(TAG,"Cannot Fatch record");
        }

        return listViewItem;
    }





}
