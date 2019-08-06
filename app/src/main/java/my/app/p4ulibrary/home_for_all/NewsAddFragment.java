package my.app.p4ulibrary.home_for_all;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import my.app.p4ulibrary.R;
import my.app.p4ulibrary.classes.News;
import my.app.p4ulibrary.main_menu.HomeFragment;

public class NewsAddFragment extends HomeFragment {

    private String newsid,newstext;
    private DatabaseReference myRef;
    private EditText editText;
    private Button save;
    private View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_add_news,container,false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("books");

        initViews();
        save.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                //updatenews();
            }
        });



        return v;
    }
    private void initViews(){
    editText=(EditText)v.findViewById (R.id.marqueetext);
    save=(Button)v.findViewById (R.id.btn_add);
    }
    private void updatenews(){
        String newsid = myRef.push().getKey();
        String newstext = editText.getText ().toString ();
        News news= new News(newsid, newstext);
        myRef.setValue (news);
        myRef.child(Objects.requireNonNull (newsid)).setValue(news);
        Toast.makeText(getContext(), "News Updated", Toast.LENGTH_LONG).show();

    }


}
