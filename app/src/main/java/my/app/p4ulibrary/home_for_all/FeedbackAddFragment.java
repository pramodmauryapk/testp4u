package my.app.p4ulibrary.home_for_all;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import my.app.p4ulibrary.R;
import my.app.p4ulibrary.main_menu.HomeFragment;

public class FeedbackAddFragment extends HomeFragment {


    private DatabaseReference myref;
    private EditText editText;
    private TextView textView;
    private Button button;
    private View v;
    private RatingBar ratingBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_add_news,container,false);

        myref = FirebaseDatabase.getInstance().getReference("News");

        initViews();
        textView.setText ("Enter Feedback ");
        button.setText ("Submit");
        ratingBar.setVisibility (View.VISIBLE);
        return v;
    }
    private void initViews(){
        editText=(EditText)v.findViewById (R.id.textView1);
        textView=(TextView)v.findViewById (R.id.txt1);
        button=(Button)v.findViewById (R.id.btn_add);
        ratingBar=(RatingBar)v.findViewById (R.id.ratingBar);
    }


}
