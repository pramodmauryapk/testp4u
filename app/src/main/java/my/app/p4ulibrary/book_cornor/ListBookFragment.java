package my.app.p4ulibrary.book_cornor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import my.app.p4ulibrary.main_menu.HomeFragment;
import my.app.p4ulibrary.R;


public class ListBookFragment extends HomeFragment {

    private View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v  = inflater.inflate
                (R.layout.fragment_list_book, container, false);


        return v;
    }
}
