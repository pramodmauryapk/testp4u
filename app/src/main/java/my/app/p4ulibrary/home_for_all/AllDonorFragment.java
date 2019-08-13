package my.app.p4ulibrary.home_for_all;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import my.app.p4ulibrary.main_menu.HomeFragment;
import my.app.p4ulibrary.R;
import my.app.p4ulibrary.classes.Book;


public class AllDonorFragment extends HomeFragment {

    private ListView listViewdonors;
    private List<Book> donors,udonors;

    private DatabaseReference databaseBooks;
    private View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate
                (R.layout.fragment_all_donors, container, false);
        databaseBooks = FirebaseDatabase.getInstance().getReference("books");
        //getting views
        initViews();

        donors = new ArrayList<>();
        udonors=new ArrayList<> ();
        return v;
    }
    private void initViews(){

        listViewdonors = (ListView) v.findViewById(R.id.view_list);

    }
    public void onStart() {

        super.onStart();
        databaseBooks.addValueEventListener(new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                donors.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Book donor = postSnapshot.getValue(Book.class);
                    donors.add(donor);

                }
                udonors.clear();

/*

                for (int i = 0; i < donors.size(); i++) {
                    for (int o = i; o < donors.size(); o++) {
                        if ((donors.get (i).getBookDonorMobile ().equals (donors.get(o).getBookDonorMobile ()))) {

                  //          udonors.add(donors.get (o));
                            donors.remove (i);
                        }
                    }
                }
*/
                //creating adapter

                DonorList donorAdapter = new DonorList(getActivity(), donors);
                listViewdonors.setAdapter(donorAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }






}
