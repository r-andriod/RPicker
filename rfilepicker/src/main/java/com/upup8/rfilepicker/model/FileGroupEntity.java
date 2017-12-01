package com.upup8.rfilepicker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件分组
 * 如 document 下的 pdf word ppt excel 等
 * FileGroupInfo
 * Created by renwoxing on 2017/11/28.
 */
@Deprecated
public class FileGroupEntity {

    private String groupName;
    private boolean isChoose = false;
    private List<FileEntity> mFileList = new ArrayList<>();

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public List<FileEntity> getFileList() {
        return mFileList;
    }

    public void setFileList(List<FileEntity> mFileList) {
        this.mFileList = mFileList;
    }
}
