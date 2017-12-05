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

import static android.provider.MediaStore.Images.ImageColumns.ORIENTATION;

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
    private static final String[] thumbVideoColumnsProjection = new String[]{
            MediaStore.Video.Thumbnails.DATA,
            MediaStore.Video.Thumbnails.VIDEO_ID
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
                getVideoResult(data);
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
        //List<FileEntity> fileEntityList = new ArrayList<>();

        if (data.getPosition() != -1) {
            data.moveToPosition(-1);
        }
        StringBuilder inImageIds = new StringBuilder(Thumbnails.IMAGE_ID);
        inImageIds.append(" in ( ");
        if (data.moveToFirst()) {
            while (data.moveToNext()) {
                Long fileDirId = data.getLong(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_ID));
                String fileDirName = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME));

                Long imageId = data.getLong(data.getColumnIndexOrThrow(BaseColumns._ID));
                String name = data.getString(data.getColumnIndexOrThrow(Media.TITLE)); //ImageColumns
                String imagePath = data.getString(data.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                long size = data.getLong(data.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE));
                long time = data.getLong(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_MODIFIED));
                int orientation = data.getInt(data.getColumnIndexOrThrow(ORIENTATION));

                int mediaType = 0;
                if (!TextUtils.isEmpty(imagePath)) {
                    File file = new File(imagePath);
                    if (!file.exists() || file.length() <= 0) {
                        continue;
                    }
                    if (TextUtils.isEmpty(name)) {
                        name = imagePath.substring(imagePath.lastIndexOf("/") + 1);
                    }

                    /* 分组 */
                    // done: 2017/12/1 判断是否已存在
                    if (tempDirIdArray.get(fileDirId.intValue()) == null) {
                        FileEntity fileDirInfo = new FileEntity();
                        fileDirInfo.setFileId(0l);
                        fileDirInfo.setDirId(fileDirId);
                        fileDirInfo.setFileName(fileDirName);
                        fileDirInfo.setFilePath(imagePath);
                        fileDirInfo.setFileType(FileEntity.FileType.IMAGE);
                        fileDirInfo.setFileSize(0);
                        fileDirInfo.setDirFileCount(1);
                        tempDirIdArray.put(fileDirId.intValue(), fileDirInfo);
                    } else {
                        //更新数量
                        tempDirIdArray.get(fileDirId.intValue()).setDirFileCount(tempDirIdArray.get(fileDirId.intValue()).getDirFileCount() + 1);
                    }

                    // file
                    FileEntity fileInfo = new FileEntity();
                    fileInfo.setFileId(imageId);
                    fileInfo.setDirId(fileDirId);
                    fileInfo.setFileName(name);
                    fileInfo.setFilePath(imagePath);
                    fileInfo.setFileType(String.valueOf(mediaType));
                    fileInfo.setFileSize(size);
                    fileInfo.setDirFileCount(0);
                    fileInfo.setFileModifiedTime(time);
                    fileInfo.setOrientation(orientation);
                    tempArray.put(imageId.intValue(), fileInfo);
                    inImageIds.append(imageId).append(",");


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
                    //if (fileEntityList.contains(fileThumbnailEntity)) {
                    //    fileEntityList.get(fileEntityList.indexOf(fileThumbnailEntity)).setThumbnail(true);
                    //    fileEntityList.get(fileEntityList.indexOf(fileThumbnailEntity)).setFileThumbnail(thumbnailPath);
                    fileThumbnailEntity.setThumbnail(true);
                    fileThumbnailEntity.setFileThumbnail(thumbnailPath);
                    //}
                }
            } while (data.moveToNext());
        }


        /** 处理 SparseArray */
//        for (int i = 0; i < tempDirIdArray.size(); i++) {
//            fileEntityList.add(tempDirIdArray.get(tempDirIdArray.keyAt(i)));
//            for (int j = 0; j < tempArray.size(); j++) {
//                if (tempDirIdArray.keyAt(i) == tempArray.get(tempArray.keyAt(j)).getDirId()) {
//                    fileEntityList.add(tempArray.get(tempArray.keyAt(j)));
//                }
//            }
//        }


        /**
         * 返回 list
         */
        if (resultCallback != null) {
            resultCallback.onResultCallback(tempDirIdArray, tempArray);
        }

        tempArray.clear();
        tempArray = null;
        tempDirIdArray.clear();
        tempDirIdArray = null;
        //fileGroupList = Collections.emptyList();
    }

    private void getVideoResult(Cursor data) {

        SparseArray<FileEntity> tempArray = new SparseArray<FileEntity>();
        SparseArray<FileEntity> tempDirIdArray = new SparseArray<>();

        if (data.getPosition() != -1) {
            data.moveToPosition(-1);
        }
        StringBuilder inVideoIds = new StringBuilder(MediaStore.Video.Thumbnails.VIDEO_ID);
        inVideoIds.append(" in ( ");
        if (data.moveToFirst()) {
            while (data.moveToNext()) {
                Long fileDirId = data.getLong(data.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.BUCKET_ID));
                String fileDirName = data.getString(data.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME));

                Long videoId = data.getLong(data.getColumnIndexOrThrow(BaseColumns._ID));
                String name = data.getString(data.getColumnIndexOrThrow(Media.TITLE)); //ImageColumns
                String videoPath = data.getString(data.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                long size = data.getLong(data.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE));
                long time = data.getLong(data.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATE_MODIFIED));

                //int orientation = data.getInt(data.getColumnIndexOrThrow(ORIENTATION));

                int mediaType = 0;

                if (!TextUtils.isEmpty(videoPath)) {
                    File file = new File(videoPath);
                    if (!file.exists() || file.length() <= 0) {
                        continue;
                    }
                    if (TextUtils.isEmpty(name)) {
                        name = videoPath.substring(videoPath.lastIndexOf("/") + 1);
                    }
                    //FileEntity FileInfo = new FileEntity(fileId, imageId, name, imagePath, FileEntity.FileType.IMAGE, size, time * 1000, true, "image");

                    /* 分组 */
                    // done: 2017/12/1 判断是否已存在
                    if (tempDirIdArray.get(fileDirId.intValue()) == null) {
                        FileEntity fileDirInfo = new FileEntity();
                        fileDirInfo.setFileId(0l);
                        fileDirInfo.setDirId(fileDirId);
                        fileDirInfo.setFileName(fileDirName);
                        fileDirInfo.setFilePath(videoPath);
                        fileDirInfo.setFileType(FileEntity.FileType.VIDEO);
                        fileDirInfo.setFileSize(0);
                        fileDirInfo.setDirFileCount(1);
                        tempDirIdArray.put(fileDirId.intValue(), fileDirInfo);
                    } else {
                        //更新数量
                        tempDirIdArray.get(fileDirId.intValue()).setDirFileCount(tempDirIdArray.get(fileDirId.intValue()).getDirFileCount() + 1);
                    }

                    // file
                    FileEntity fileInfo = new FileEntity();
                    fileInfo.setFileId(videoId);
                    fileInfo.setDirId(fileDirId);
                    fileInfo.setFileName(name);
                    fileInfo.setFilePath(videoPath);
                    fileInfo.setFileType(String.valueOf(mediaType));
                    fileInfo.setFileSize(size);
                    fileInfo.setDirFileCount(0);
                    fileInfo.setFileModifiedTime(time);
                    //fileInfo.setOrientation(orientation);
                    tempArray.put(videoId.intValue(), fileInfo);

                    inVideoIds.append(videoId).append(",");


                }
            }
        }

        inVideoIds.deleteCharAt(inVideoIds.length() - 1);
        inVideoIds.append(" ) ");
        data = context.get().getContentResolver()
                .query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                        thumbVideoColumnsProjection,
                        inVideoIds.toString(),
                        null, null);
        // 缩略图部分
        if (data.moveToFirst()) {
            do {
                int _vidoId = data.getInt(data.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.VIDEO_ID));
                String thumbnailPath = data.getString(data.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
                FileEntity fileThumbnailEntity = tempArray.get(_vidoId);
                if (fileThumbnailEntity != null) {
                    fileThumbnailEntity.setThumbnail(true);
                    fileThumbnailEntity.setFileThumbnail(thumbnailPath);

                }
            } while (data.moveToNext());
        }


        /**
         * 返回 list
         */
        if (resultCallback != null) {
            resultCallback.onResultCallback(tempDirIdArray, tempArray);
        }

        tempArray.clear();
        tempArray = null;
        tempDirIdArray.clear();
        tempDirIdArray = null;
    }


}
