package com.upup8.rfilepicker.data.cursor.callback;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.SparseArray;

import com.upup8.rfilepicker.data.RFilePickerConst;
import com.upup8.rfilepicker.data.cursor.loader.RAudioLoader;
import com.upup8.rfilepicker.data.cursor.loader.RFileLoader;
import com.upup8.rfilepicker.data.cursor.loader.RImageLoader;
import com.upup8.rfilepicker.data.cursor.loader.RVideoLoader;
import com.upup8.rfilepicker.model.FileEntity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * RFileLoaderCallback
 * Created by renwoxing on 2017/11/29.
 */
public class RFileLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {


    private WeakReference<Context> context;

    private IFileResultCallback<FileEntity> resultCallback;

    private int mType = RFilePickerConst.MEDIA_TYPE_FILE;

    private static final String[] thumbnailProjection = {
            Thumbnails.DATA,
            Thumbnails.IMAGE_ID
    };

    /**
     * 构造
     *
     * @param context
     * @param resultCallback
     */
    public RFileLoaderCallback(Context context, IFileResultCallback<FileEntity> resultCallback) {
        this.context = new WeakReference<>(context);
        this.resultCallback = resultCallback;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mType = args.getInt(RFilePickerConst.EXTRA_FILE_TYPE, RFilePickerConst.MEDIA_TYPE_IMAGE);
        switch (mType) {
            case RFilePickerConst.MEDIA_TYPE_AUDIO:
                return new RAudioLoader(context.get());
            case RFilePickerConst.MEDIA_TYPE_FILE:
                return new RFileLoader(context.get());
            case RFilePickerConst.MEDIA_TYPE_IMAGE:
                return new RImageLoader(context.get());
            case RFilePickerConst.MEDIA_TYPE_VIDEO:
                return new RVideoLoader(context.get());
            default:
                return new RFileLoader(context.get());
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) return;
        switch (mType) {
            case RFilePickerConst.MEDIA_TYPE_AUDIO:
                getImageResult(data);
                break;
            case RFilePickerConst.MEDIA_TYPE_FILE:
                getImageResult(data);
                break;
            case RFilePickerConst.MEDIA_TYPE_IMAGE:
                getImageResult(data);
                break;
            case RFilePickerConst.MEDIA_TYPE_VIDEO:
                getImageResult(data);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    @SuppressWarnings("unchecked")
    private void getImageResult(Cursor data) {

        SparseArray<FileEntity> tempArray = new SparseArray<FileEntity>();
        SparseArray<FileEntity> tempDirIdArray = new SparseArray<>();
        //List<FileGroupEntity> fileGroupList = new ArrayList<>();
        List<FileEntity> fileEntityList = new ArrayList<>();

        if (data.getPosition() != -1) {
            data.moveToPosition(-1);
        }
        StringBuilder inImageIds = new StringBuilder(Thumbnails.IMAGE_ID);
        inImageIds.append(" in ( ");
        if (data.moveToFirst()) {
            while (data.moveToNext()) {
                int fileDirId = data.getInt(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_ID));
                String fileDirName = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME));
                long time = data.getLong(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_MODIFIED));
                //String name = data.getString(data.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)); //
                int imageId = data.getInt(data.getColumnIndexOrThrow(BaseColumns._ID));
                String imagePath = data.getString(data.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                String name = data.getString(data.getColumnIndexOrThrow(Media.TITLE)); //ImageColumns
                long size = data.getLong(data.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE));
                //int mediaType = data.getInt(data.getColumnIndexOrThrow(MEDIA_TYPE));
                int mediaType = 0;

                if (!TextUtils.isEmpty(imagePath)) {
                    File file = new File(imagePath);
                    if (!file.exists() || file.length() <= 0) {
                        continue;
                    }
                    if (TextUtils.isEmpty(name)) {
                        name = imagePath.substring(imagePath.lastIndexOf("/") + 1);
                    }
                    //FileEntity FileInfo = new FileEntity(fileId, imageId, name, imagePath, FileEntity.FileType.IMAGE, size, time * 1000, true, "image");

                    /* 分组 */
                    // done: 2017/12/1 判断是否已存在
                    if (tempDirIdArray.get(fileDirId) == null) {
                        FileEntity fileDirInfo = new FileEntity();
                        fileDirInfo.setFileId(0);
                        fileDirInfo.setDirId(fileDirId);
                        fileDirInfo.setFileName(fileDirName);
                        fileDirInfo.setFilePath(imagePath);
                        fileDirInfo.setFileType(FileEntity.FileType.IMAGE);
                        fileDirInfo.setFileSize(0);
                        tempDirIdArray.put(fileDirId, fileDirInfo);
                        // add
                        fileEntityList.add(fileDirInfo);
                    } else {
                        //更新数量
                        tempDirIdArray.get(fileDirId).setFileSize(tempDirIdArray.get(fileDirId).getFileSize() + 1);
                    }


                    // file
                    FileEntity fileInfo = new FileEntity();
                    fileInfo.setFileId(imageId);
                    fileInfo.setDirId(fileDirId);
                    fileInfo.setFileName(name);
                    fileInfo.setFilePath(imagePath);
                    fileInfo.setFileType(String.valueOf(mediaType));
                    fileInfo.setFileSize(size);
                    fileInfo.setFileModifiedTime(time);
                    tempArray.put(imageId, fileInfo);
                    inImageIds.append(imageId).append(",");
                    // add
                    fileEntityList.add(fileInfo);

                }
            }
        }

        inImageIds.deleteCharAt(inImageIds.length() - 1);
        inImageIds.append(" ) ");
        data = context.get().getContentResolver()
                .query(Thumbnails.EXTERNAL_CONTENT_URI,
                        thumbnailProjection,
                        inImageIds.toString(),
                        null, null);
        // 缩略图部分
        if (data.moveToFirst()) {
            do {
                int imageId = data.getInt(data.getColumnIndexOrThrow(Thumbnails.IMAGE_ID));
                String thumbnailPath = data.getString(data.getColumnIndexOrThrow(Media.DATA));
                FileEntity fileThumbnailEntity = tempArray.get(imageId);
                if (fileThumbnailEntity != null) {
                    if (fileEntityList.contains(fileThumbnailEntity)) {
                        fileEntityList.get(fileEntityList.indexOf(fileThumbnailEntity)).setThumbnail(true);
                        fileEntityList.get(fileEntityList.indexOf(fileThumbnailEntity)).setFileThumbnail(thumbnailPath);
                        //fileThumbnailEntity.setThumbnail(true);
                        //fileThumbnailEntity.setFileThumbnail(thumbnailPath);
                        //fileThumbnailEntity
                    }
                }
            } while (data.moveToNext());
        }
        /**
         * 返回 list
         */
        if (resultCallback != null) {
            resultCallback.onResultCallback(fileEntityList);
        }

        tempArray.clear();
        tempArray = null;
        tempDirIdArray.clear();
        tempDirIdArray = null;
        //fileGroupList = Collections.emptyList();
    }


}
