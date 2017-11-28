package com.upup8.rfilepicker.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.upup8.rfilepicker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DocumentFragment extends Fragment {


    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    public static DocumentFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        DocumentFragment pageFragment = new DocumentFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }



    public DocumentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_document, container, false);
    }

}
