package com.upup8.rfilepicker.data.cursor.loader;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import static android.media.MediaCodec.MetricsConstants.MIME_TYPE;

/**
 * RAudioLoader
 * Created by renwoxing on 2017/11/29.
 */
public class RAudioLoader extends CursorLoader {


    private static final String[] AUDIO_PROJECTION = {
            //Base File
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATE_MODIFIED,
            //Audio File
            MediaStore.Audio.Media.DURATION
    };


    public RAudioLoader(Context context) {
        super(context);

        setProjection(AUDIO_PROJECTION);
        setUri(MediaStore.Files.getContentUri("external"));
        setSortOrder(MediaStore.Audio.Media.DATE_ADDED + " DESC");

        setSelection(MIME_TYPE + "=? or "
                + MIME_TYPE + "=? or "
                + MIME_TYPE + "=?");
        String[] selectionArgs;
        selectionArgs = new String[]{"audio/mpeg", "audio/mp3", "audio/x-ms-wma"};
        setSelectionArgs(selectionArgs);
    }

    public RAudioLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }
}
