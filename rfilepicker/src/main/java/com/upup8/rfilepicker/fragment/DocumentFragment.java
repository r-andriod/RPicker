package com.upup8.rfilepicker.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.upup8.rfilepicker.R;
import com.upup8.rfilepicker.adapter.DocumentRvAdapter;
import com.upup8.rfilepicker.data.RFilePickerConst;
import com.upup8.rfilepicker.data.cursor.MediaDataHelper;
import com.upup8.rfilepicker.data.cursor.callback.IFileResultCallback;
import com.upup8.rfilepicker.databinding.RpickerFragmentDocumentBinding;
import com.upup8.rfilepicker.model.FileEntity;
import com.upup8.rfilepicker.view.RFilePickerItemDecorator;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DocumentFragment extends Fragment {


    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private RpickerFragmentDocumentBinding binding;

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
        binding = DataBindingUtil.inflate(inflater, R.layout.rpicker_fragment_document, container, false);
        return binding.getRoot();
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.rpicker_fragment_document, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        Bundle mediaStoreArgs = new Bundle();
        mediaStoreArgs.putInt(RFilePickerConst.EXTRA_FILE_TYPE, RFilePickerConst.MEDIA_TYPE_IMAGE);

        MediaDataHelper.getImages(getActivity(), mediaStoreArgs,
                new IFileResultCallback<FileEntity>() {
                    @Override
                    public void onResultCallback(List<FileEntity> files) {
                        initList(files);
                    }
                });
    }

    private void initList(final List<FileEntity> files) {
        binding.rpRcPhoto.setLayoutManager(new LinearLayoutManager(getContext()));
        //binding.rpRcPhoto.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
        binding.rpRcPhoto.addItemDecoration(new RFilePickerItemDecorator(getContext(),
                RFilePickerItemDecorator.VERTICAL_LIST,
                new RFilePickerItemDecorator.RFIlePickerDecorationCallback() {
                    @Override
                    public boolean getIsGroupHeadeByPos(int position) {
                        if ((files.get(position).getFileId() == 0)) {
                            return true;
                        }
                        return false;
                    }
                }));
        DocumentRvAdapter adapter = new DocumentRvAdapter(getContext(), files);
        binding.rpRcPhoto.setAdapter(adapter);
    }
}
