package com.upup8.rfilepicker.data;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.upup8.rfilepicker.model.FileEntity;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.MediaColumns.DATA;

/**
 * FileScannerTask
 * Created by renwoxing on 2017/11/28.
 */
@Deprecated
public class FileScannerTask extends AsyncTask<Void, Void, List<FileEntity>> {

    public interface FileScannerCallback {
        void onScannerResult(List<FileEntity> entities);
    }

    private final String[] DOC_PROJECTION = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Files.FileColumns.TITLE
    };
    private final FileScannerCallback fileScannerListener;

    private final Context context;

    /**
     * 通过 type 条件查询
     * @param context
     * @param type
     * @param fileResultCallback
     */
    public FileScannerTask(Context context, int type, FileScannerCallback fileResultCallback) {
        this.context = context;
        this.fileScannerListener = fileResultCallback;
    }

    /**
     * 异步查询任务
     *
     * @param voids
     * @return
     */
    @Override
    protected List<FileEntity> doInBackground(Void... voids) {

        List<FileEntity> fileEntities = new ArrayList<>();
        String selection = MediaStore.Files.FileColumns.MIME_TYPE + "= ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "  // mp3
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "  //video
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "  //rar
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? ";  //zip
        String[] selectionArgs = new String[]{
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("text"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("dotx"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("dotx"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("ppt"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("pptx"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("potx"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("ppsx"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("xls"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("xlsx"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("xltx"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpg"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpg"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("png"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("svg"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("gif"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("video"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("rar"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("zip")
        };
        for (String str : selectionArgs) {
            Log.i("selectionArgs", str);
        }
        final Cursor cursor = context.getContentResolver()
                .query(
                        MediaStore.Files.getContentUri("external"),//数据源
                        DOC_PROJECTION,//查询类型
                        selection,//查询条件
                        selectionArgs,
                        MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
                );
        if (cursor != null) {
            fileEntities = getFiles(cursor);
            cursor.close();
        }
        return fileEntities;
    }

    /**
     * 查询完成后
     *
     * @param fileEntities
     */
    @Override
    protected void onPostExecute(List<FileEntity> fileEntities) {
        super.onPostExecute(fileEntities);
    }

    private List<FileEntity> getFiles(Cursor cursor) {
        List<FileEntity> fileEntities = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(DATA));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE));
            if (path != null) {
//                FileType fileType = getFileType(PickerManager.getInstance().getFileTypes(), path);
//                if (fileType != null && !(new File(path).isDirectory())) {
//
//                    FileEntity entity = new FileEntity(id, title, path);
//                    entity.setFileType(fileType);
//
//                    String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE));
//                    if (mimeType != null && !TextUtils.isEmpty(mimeType))
//                        entity.setMimeType(mimeType);
//                    else {
//                        entity.setMimeType("");
//                    }
//
//                    entity.setSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)));
//                    if(PickerManager.getInstance().files.contains(entity)){
//                        entity.setSelected(true);
//                    }
//                    if (!fileEntities.contains(entity))
//                        fileEntities.add(entity);
//                }
            }
        }
        return fileEntities;
    }

}
