package my.app.p4ulibrary.book_cornor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import my.app.p4ulibrary.main_menu.HomeFragment;
import my.app.p4ulibrary.R;


public class ListBookFragment extends HomeFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         final View v = inflater.inflate
                (R.layout.fragment_list_book, container, false);


        return v;
    }
}
