package com.upup8.rfilepicker.adapter;

import com.upup8.rfilepicker.model.FileEntity;

import java.util.List;

/**
 * RFilePickerItemClickListener
 * Created by renwoxing on 2017/12/2.
 */
public interface RFilePickerItemClickListener {

    //void onItemClick(Long groupId, int pos);
    void onFileItemSelectClick(List<FileEntity> fileEntities);

    /**
     * 展开子Item
     *
     * @param item
     */
    //void onExpandList(FileEntity item);

    /**
     * 隐藏子Item
     *
     * @param item
     */
    //void onCollapseList(FileEntity item);
}
