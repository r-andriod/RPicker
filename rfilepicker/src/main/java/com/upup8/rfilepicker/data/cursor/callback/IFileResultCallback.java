package com.upup8.rfilepicker.data.cursor.callback;

import java.util.List;

/**
 * 文件列表回调
 * IFileResultCallback
 * Created by renwoxing on 2017/11/29.
 */
public interface IFileResultCallback<T> {

    void onResultCallback(List<T> files);
}
