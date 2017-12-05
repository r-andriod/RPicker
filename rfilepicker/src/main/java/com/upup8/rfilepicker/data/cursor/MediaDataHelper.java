package com.upup8.rfilepicker.data.cursor;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.upup8.rfilepicker.data.RFilePickerConst;
import com.upup8.rfilepicker.data.cursor.callback.IFileResultCallback;
import com.upup8.rfilepicker.data.cursor.callback.RFileLoaderCallback;
import com.upup8.rfilepicker.model.FileEntity;

/**
 * 文件数据源处理
 * MediaDataHelper
 * Created by renwoxing on 2017/11/29.
 */
public class MediaDataHelper {


    public static void getImages(FragmentActivity activity, Bundle args, IFileResultCallback<FileEntity> resultCallback) {
        if (activity.getSupportLoaderManager().getLoader(RFilePickerConst.MEDIA_TYPE_IMAGE) != null)
            activity.getSupportLoaderManager().restartLoader(RFilePickerConst.MEDIA_TYPE_IMAGE, args, new RFileLoaderCallback(activity, resultCallback));
        else
            activity.getSupportLoaderManager().initLoader(RFilePickerConst.MEDIA_TYPE_IMAGE, args, new RFileLoaderCallback(activity, resultCallback));
    }

    public static void getVideos(FragmentActivity activity, Bundle args, IFileResultCallback<FileEntity> resultCallback) {
        if (activity.getSupportLoaderManager().getLoader(RFilePickerConst.MEDIA_TYPE_VIDEO) != null)
            activity.getSupportLoaderManager().restartLoader(RFilePickerConst.MEDIA_TYPE_VIDEO, args, new RFileLoaderCallback(activity, resultCallback));
        else
            activity.getSupportLoaderManager().initLoader(RFilePickerConst.MEDIA_TYPE_VIDEO, args, new RFileLoaderCallback(activity, resultCallback));
    }

    public static void getAudios(FragmentActivity activity, Bundle args, IFileResultCallback<FileEntity> callback) {
        if (activity.getSupportLoaderManager().getLoader(RFilePickerConst.MEDIA_TYPE_AUDIO) != null)
            activity.getSupportLoaderManager().restartLoader(RFilePickerConst.MEDIA_TYPE_AUDIO, args, new RFileLoaderCallback(activity, callback));
        else
            activity.getSupportLoaderManager().initLoader(RFilePickerConst.MEDIA_TYPE_AUDIO, args, new RFileLoaderCallback(activity, callback));
    }

    public static void getDocument(FragmentActivity activity, Bundle args, IFileResultCallback<FileEntity> callback) {
        if (activity.getSupportLoaderManager().getLoader(RFilePickerConst.MEDIA_TYPE_DOCUMENT) != null)
            activity.getSupportLoaderManager().restartLoader(RFilePickerConst.MEDIA_TYPE_DOCUMENT, args, new RFileLoaderCallback(activity, callback));
        else
            activity.getSupportLoaderManager().initLoader(RFilePickerConst.MEDIA_TYPE_DOCUMENT, args, new RFileLoaderCallback(activity, callback));

    }

    /**
     * 根据条件查询不同类型的方法
     * @param activity
     * @param args
     * @param callback
     */
    public static void getFiles(FragmentActivity activity, Bundle args, IFileResultCallback<FileEntity> callback) {
        int mType = args.getInt(RFilePickerConst.EXTRA_FILE_TYPE, RFilePickerConst.MEDIA_TYPE_IMAGE);
        if (activity.getSupportLoaderManager().getLoader(mType) != null)
            activity.getSupportLoaderManager().restartLoader(mType, args, new RFileLoaderCallback(activity, callback));
        else
            activity.getSupportLoaderManager().initLoader(mType, args, new RFileLoaderCallback(activity, callback));

    }
}
