package com.upup8.rfilepicker.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.upup8.rfilepicker.R;
import com.upup8.rfilepicker.adapter.RFilePickerRvAdapter;
import com.upup8.rfilepicker.data.RFilePickerConst;
import com.upup8.rfilepicker.data.cursor.MediaDataHelper;
import com.upup8.rfilepicker.data.cursor.callback.IFileResultCallback;
import com.upup8.rfilepicker.databinding.RpickerFragmentDocumentBinding;
import com.upup8.rfilepicker.model.FileEntity;
import com.upup8.rfilepicker.view.RFilePickerItemDecorator;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RFilePickerFragment extends Fragment {


    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private RpickerFragmentDocumentBinding binding;
    private List<FileEntity> fileEntityList;
    private SparseArray<FileEntity> groupArr = new SparseArray<>();
    private SparseArray<FileEntity> fileArr = new SparseArray<>();

    public static RFilePickerFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        RFilePickerFragment pageFragment = new RFilePickerFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }


    public RFilePickerFragment() {
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
                    public void onResultCallback(SparseArray<FileEntity> dirArr, SparseArray<FileEntity> fileSparseArr) {
                        groupArr = dirArr;
                        fileArr = fileSparseArr;
                        convertSparseArrayToList(-1l);
                        initList();
                    }
                });
    }


    private void initList() {
        binding.rpRcPhoto.setLayoutManager(new LinearLayoutManager(getContext()));
        //binding.rpRcPhoto.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
        binding.rpRcPhoto.addItemDecoration(new RFilePickerItemDecorator(getContext(),
                RFilePickerItemDecorator.VERTICAL_LIST,
                new RFilePickerItemDecorator.RFIlePickerDecorationCallback() {
                    @Override
                    public boolean getIsGroupHeadByPos(int position) {
                        //Log.d("getIsGroupHeadByPos", "getIsGroupHeadByPos: " + files.get(position).toString());
                        if (fileEntityList != null && fileEntityList.size() > 0) {
                            if ((fileEntityList.get(position).getFileId() == 0)) {
                                return true;
                            }
                        }
                        return false;
                    }
                }));
//        RFilePickerRvAdapter adapter = new RFilePickerRvAdapter(getContext(), fileEntityList, new RFilePickerItemClickListener() {
//            @Override
//            public void onItemClick(Long groupId, int pos) {
//                convertSparseArrayToList(groupId);
//            }
//        });
        RFilePickerRvAdapter adapter = new RFilePickerRvAdapter(getContext(), fileEntityList);
        binding.rpRcPhoto.setAdapter(adapter);
        /**
         * 展开与收起
         * 滚动事件
         */
        adapter.setOnScrollListener(new RFilePickerRvAdapter.OnScrollListener() {
            @Override
            public void scrollTo(int pos) {
                binding.rpRcPhoto.scrollToPosition(pos);
            }
        });
        //adapter.notifyDataSetChanged();
    }

    /**
     * 处理 SparseArray to list
     *
     * @param groupId 根据 group id 来转数据 默认为第一个 group
     */
    private void convertSparseArrayToList(Long groupId) {
//        if (fileEntityList != null && fileEntityList.size() > 0) {
//            fileEntityList.clear();
//        }
        fileEntityList = new ArrayList<>();
        for (int i = 0; i < groupArr.size(); i++) {
            fileEntityList.add(groupArr.get(groupArr.keyAt(i)));
            for (int j = 0; j < fileArr.size(); j++) {
                if (groupId == -1) {
                    if (groupArr.keyAt(i) == fileArr.get(fileArr.keyAt(j)).getDirId()) {
                        fileEntityList.add(fileArr.get(fileArr.keyAt(j)));
                    }
                } else {
                    if (groupArr.keyAt(i) == fileArr.get(fileArr.keyAt(j)).getDirId() && groupArr.keyAt(i) == groupId) {
                        fileEntityList.add(fileArr.get(fileArr.keyAt(j)));
                    }
                }
            }
        }
        Log.d("arr to list", "convertSparseArrayToList:  size: " + fileEntityList.size() + "|" + groupId + " group size:" + groupArr.size());
    }
}
