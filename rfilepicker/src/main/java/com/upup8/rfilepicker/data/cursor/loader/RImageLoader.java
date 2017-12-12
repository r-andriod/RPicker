package com.upup8.rfilepicker.data.cursor.loader;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

/**
 * RImageLoader
 * Created by renwoxing on 2017/11/29.
 */
public class RImageLoader extends CursorLoader {

    // MediaStore.Images.Media.DATA：图片文件路径；
    // MediaStore.Images.Media.DISPLAY_NAME : 图片文件名，如 testVideo.jpg
    // MediaStore.Images.Media.TITLE: 图片标题 : testVideo
    // MediaStore.Images.Media.SIZE: 图片文件大小  bytes
    // MediaStore.Images.Media.ORIENTATION :图片旋转方向
    private static final String[] IMAGE_PROJECTION = {
            //Base File
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.DATE_ADDED,
            //Image File
            MediaStore.Images.Media.ORIENTATION
    };

    private RImageLoader(Context context, Uri uri, String[] projection, String selection,
                         String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }

    public RImageLoader(Context context) {
        super(context);
        setProjection(IMAGE_PROJECTION);
        setUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        setSortOrder(MediaStore.Images.Media.DATE_ADDED + " DESC");

        setSelection(MIME_TYPE + "=? or " +
                MIME_TYPE + "=? or " +
                MIME_TYPE + "=? or " +
                MIME_TYPE + "=?");

        String[] selectionArgs;
        selectionArgs = new String[]{"image/jpeg", "image/png", "image/jpg", "image/gif"};
        setSelectionArgs(selectionArgs);
    }
}
