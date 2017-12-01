package com.upup8.rfilepicker.data.cursor.loader;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

/**
 * RFileLoader
 * Created by renwoxing on 2017/11/29.
 */
public class RFileLoader extends CursorLoader {


    private static final String[] FILE_PROJECTION = {
            //Base File
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.TITLE,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.DATE_MODIFIED,

            //Normal File
            MediaStore.Files.FileColumns.MIME_TYPE
    };

    public RFileLoader(Context context){
        super(context);

        setProjection(FILE_PROJECTION);
        setUri(MediaStore.Files.getContentUri("external"));
        setSortOrder(MediaStore.Files.FileColumns.DATE_ADDED + " DESC");

    }




    private RFileLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }
}
