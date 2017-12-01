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
public class PhotoPickerFragment extends Fragment {


    public PhotoPickerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.rpfilepicker_photo_picker_fragment, container, false);
    }

}
