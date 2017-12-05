package com.upup8.rfilepicker.data.cursor.loader;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

/**
 * RVideoLoader
 * Created by renwoxing on 2017/11/29.
 */
public class RVideoLoader extends CursorLoader {

    // MediaStore.Video.Media.DATA：视频文件路径；
    // MediaStore.Video.Media.DISPLAY_NAME : 视频文件名，如 testVideo.mp4
    // MediaStore.Video.Media.TITLE: 视频标题 : testVideo
    // MediaStore.Video.Media.SIZE: 视频文件大小  bytes
    // MediaStore.Video.Media.DURATION :视频时长 ms
    private static final String[] VIDEO_PROJECTION = {
            //Base File
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DATE_MODIFIED,
            //Video File
            MediaStore.Video.Media.DURATION
    };

    private RVideoLoader(Context context, Uri uri, String[] projection, String selection,
                         String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }

    public RVideoLoader(Context context) {
        super(context);

        setProjection(VIDEO_PROJECTION);
        setUri(MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        setSortOrder(MediaStore.Video.Media.DATE_ADDED + " DESC");

        //setSelection(MIME_TYPE + "=? or " +
        //        MIME_TYPE + "=?");
        //String[] selectionArgs;
        //selectionArgs = new String[]{"video/mpeg", "video/mp4"};
        //setSelectionArgs(selectionArgs);
    }
}
