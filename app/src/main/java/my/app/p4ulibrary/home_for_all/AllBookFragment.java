package my.app.p4ulibrary.home_for_all;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import my.app.p4ulibrary.main_menu.HomeFragment;
import my.app.p4ulibrary.R;
import my.app.p4ulibrary.classes.Book;

public class AllBookFragment extends HomeFragment {


    private ListView listViewBooks;
    private List<Book> books;

    private DatabaseReference databaseBooks;
    private View v;
    private TextView dBookid,dAuthor,dTitle,dCost,dDonor,dDonorMobile,dLocation,dYear,dSubject,dDonorTime,dIssueTo,dIssueTime;
    private Button dBack;
    private View dialogView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_all_book,container,false);
        databaseBooks = FirebaseDatabase.getInstance().getReference("books");
        //getting views
        initViews();

      books = new ArrayList<>();
        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book book = books.get(i);
                showDialog(
                        book.getBookId(),
                        book.getBookTitle(),
                        book.getBookCost(),
                        book.getBookAuthor(),
                        book.getBookYear(),
                        book.getBookSubject(),
                        book.getBookAvaibility(),
                        book.getBookLocation(),
                        book.getBookDonor(),
                        book.getBookDonorMobile(),
                        book.getBookDonorTime(),
                        book.getBookHandoverTo(),
                        book.getBookHandoverTime());
            }
        });







        return v;
    }
    private void initViews(){

        listViewBooks = (ListView) v.findViewById(R.id.view_list);

    }
    public void onStart() {

        super.onStart();
        databaseBooks.addValueEventListener(new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //clearing the previous artist list
                books.clear();
                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Book book = postSnapshot.getValue(Book.class);
                    //adding artist to the list
                    books.add(book);

                }

                //creating adapter
                BookList bookAdapter = new BookList (getActivity(), books);
                //attaching adapter to the listview
                listViewBooks.setAdapter(bookAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @SuppressLint("InflateParams")
    private void showDialog(final String dbookId,
                            final String dbookTitle,
                            final String dbookAuthor,
                            final String dbookSubject,
                            final String dbookYear,
                            final String dbookCost,
                            final String dbookAvaibility,
                            final String dbookLocation,
                            final String dbookDonor,
                            final String dbookDonorMobile,
                            final String dbookDonorTime,
                            final String dbookHandoverTo,
                            final String dbookHandoverTime) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.show_book_dialog, null);
        dialogBuilder.setView(dialogView);

        init_dialog_views();

        dialogBuilder.setTitle("Book Record");
        final AlertDialog b = dialogBuilder.create();
        b.show();
        dBookid.setText (dbookId);
        dAuthor.setText(dbookSubject);///field missmath
        dCost.setText(dbookAuthor);//mismatch
        dTitle.setText(dbookTitle);
        dLocation.setText(dbookLocation);
        dDonor.setText(dbookDonor);
        dDonorMobile.setText(dbookDonorMobile);
        dYear.setText(dbookYear);
        dSubject.setText(dbookCost);//mismatch
        dIssueTo.setText(dbookHandoverTo);
        dIssueTime.setText(dbookHandoverTime);
        dDonorTime.setText(dbookDonorTime);
        dBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.cancel();
            }
        });


    }
    private void init_dialog_views(){
        dBookid=(TextView)dialogView.findViewById (R.id.tvBookid);
        dTitle = (TextView) dialogView.findViewById(R.id.tvTitle);
        dCost = (TextView) dialogView.findViewById(R.id.tvCost);
        dAuthor = (TextView) dialogView.findViewById(R.id.tvAuthor);
        dYear = (TextView) dialogView.findViewById(R.id.tvYear);
        dSubject = (TextView) dialogView.findViewById(R.id.tvSubject);
        dLocation = (TextView) dialogView.findViewById(R.id.tvLocation);
        dDonor = (TextView) dialogView.findViewById(R.id.tvDonor);
        dDonorMobile = (TextView) dialogView.findViewById(R.id.tvDonorMobile);
        dDonorTime = (TextView) dialogView.findViewById(R.id.tvDonateTime);
        dIssueTo = (TextView) dialogView.findViewById(R.id.tvBookIssueto);
        dIssueTime = (TextView) dialogView.findViewById(R.id.tvIssueTime);
        dBack=(Button)dialogView.findViewById(R.id.dbuttonBack);
    }


}
