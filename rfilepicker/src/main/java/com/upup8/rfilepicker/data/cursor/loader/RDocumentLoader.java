package com.upup8.rfilepicker.data.cursor.loader;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

/**
 * RDocumentLoader
 * Created by renwoxing on 2017/11/29.
 */
public class RDocumentLoader extends CursorLoader {


    private static final String[] FILE_PROJECTION = {
            //Base File
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.TITLE,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.DATE_MODIFIED,

            //Normal File
            MediaStore.Files.FileColumns.MIME_TYPE
    };

    public RDocumentLoader(Context context) {
        super(context);

        setProjection(FILE_PROJECTION);
        setUri(MediaStore.Files.getContentUri("external"));
        setSortOrder(MediaStore.Files.FileColumns.DATE_ADDED + " DESC");

        setSelection(
                MIME_TYPE + "=? or " +
                        MIME_TYPE + "=? or " +
                        MIME_TYPE + "=? or " +
                        MIME_TYPE + "=? or " +
                        MIME_TYPE + "=? or " +
                        MIME_TYPE + "=?"
        );

        String[] selectionArgs = new String[]{"text/txt", "text/plain", "application/msword", "application/pdf", "application/vnd.ms-powerpoint", "application/vnd.ms-excel"};
        setSelectionArgs(selectionArgs);

    }


    private RDocumentLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }
}
