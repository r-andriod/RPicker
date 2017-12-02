package com.upup8.rfilepicker.data.cursor.callback;

import android.util.SparseArray;

/**
 * 文件列表回调
 * IFileResultCallback
 * Created by renwoxing on 2017/11/29.
 */
public interface IFileResultCallback<T> {

    void onResultCallback(SparseArray<T> groupArr, SparseArray<T> fileArr);
}
