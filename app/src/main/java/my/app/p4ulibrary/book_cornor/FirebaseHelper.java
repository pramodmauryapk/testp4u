package my.app.p4ulibrary.book_cornor;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Objects;

import my.app.p4ulibrary.classes.Book;

public class FirebaseHelper {

    private DatabaseReference db;
    Boolean saved=null;

    FirebaseHelper(DatabaseReference db) {
        this.db = db;
    }

    //READ
    public ArrayList<String> retrieve()
    {
        final ArrayList<String> spacecrafts=new ArrayList<>();

        db.addChildEventListener(new ChildEventListener () {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot,spacecrafts);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot,spacecrafts);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return spacecrafts;
    }

    private void fetchData(DataSnapshot snapshot, ArrayList<String> spacecrafts)
    {
        spacecrafts.clear();
        for (DataSnapshot ds:snapshot.getChildren())
        {
            String name= Objects.requireNonNull(ds.getValue(Book.class)).getBookAuthor();
            spacecrafts.add(name);
        }

    }
}






















