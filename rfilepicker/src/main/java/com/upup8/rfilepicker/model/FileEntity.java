package com.upup8.rfilepicker.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * FileEntity
 * Created by renwoxing on 2017/11/28.
 */
public class FileEntity implements Parcelable {

    public interface FileType {//文件类型
        String VIDEO = "video";
        String IMAGE = "image";
        String RAR = "rar";
        String EXCEL = "excel";
        String PDF = "pdf";
        String PPT = "ppt";
        String WORD = "word";
        String MP3 = "mp3";
        String TEXT = "txt";
    }

    private Long fileId;
    private Long dirId;
    private String fileName;
    private String filePath;
    private String fileType;
    private long fileSize;
    private int dirFileCount;
    private String fileThumbnail;
    private long fileModifiedTime;
    private boolean isThumbnail;
    private boolean isSelect = false;
    private int orientation = 0;    //旋转
    private int duration = 0;    //时长

    public FileEntity() {
    }

    public FileEntity(Long fileId, Long dirId, String fileName, String filePath, String fileType, long fileSize, String fileThumbnail, long fileModifiedTime, boolean isThumbnail, boolean isSelect) {
        this.fileId = fileId;
        this.dirId = dirId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.fileThumbnail = fileThumbnail;
        this.fileModifiedTime = fileModifiedTime;
        this.isThumbnail = isThumbnail;
        this.isSelect = isSelect;
    }


    public FileEntity(Long fileId, Long dirId, String fileName, String filePath, String fileType, long fileSize, long fileModifiedTime) {
        this.fileId = fileId;
        this.dirId = dirId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.fileModifiedTime = fileModifiedTime;
    }

    public Long getDirId() {
        return dirId;
    }

    public void setDirId(Long dirId) {
        this.dirId = dirId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileThumbnail() {
        return fileThumbnail;
    }

    public void setFileThumbnail(String fileThumbnail) {
        this.fileThumbnail = fileThumbnail;
    }

    public long getFileModifiedTime() {
        return fileModifiedTime;
    }

    public void setFileModifiedTime(long fileModifiedTime) {
        this.fileModifiedTime = fileModifiedTime;
    }

    public boolean isThumbnail() {
        return isThumbnail;
    }

    public void setThumbnail(boolean thumbnail) {
        isThumbnail = thumbnail;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getDirFileCount() {
        return dirFileCount;
    }

    public void setDirFileCount(int dirFileCount) {
        this.dirFileCount = dirFileCount;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.fileId);
        dest.writeLong(this.dirId);
        dest.writeString(this.fileName);
        dest.writeString(this.filePath);
        dest.writeString(this.fileType);
        dest.writeLong(this.fileSize);
        dest.writeString(this.fileThumbnail);
        dest.writeLong(this.fileModifiedTime);
        dest.writeByte(this.isThumbnail ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
        dest.writeInt(this.dirFileCount);
        dest.writeInt(this.orientation);
        dest.writeInt(this.duration);
    }

    protected FileEntity(Parcel in) {
        this.fileId = in.readLong();
        this.dirId = in.readLong();
        this.fileName = in.readString();
        this.filePath = in.readString();
        this.fileType = in.readString();
        this.fileSize = in.readLong();
        this.fileThumbnail = in.readString();
        this.fileModifiedTime = in.readLong();
        this.isThumbnail = in.readByte() != 0;
        this.isSelect = in.readByte() != 0;
        this.dirFileCount = in.readInt();
        this.orientation = in.readInt();
        this.duration = in.readInt();
    }

    public static final Creator<FileEntity> CREATOR = new Creator<FileEntity>() {
        @Override
        public FileEntity createFromParcel(Parcel source) {
            return new FileEntity(source);
        }

        @Override
        public FileEntity[] newArray(int size) {
            return new FileEntity[size];
        }
    };

    @Override
    public String toString() {
        return "FileEntity{" +
                "fileId=" + fileId +
                ", dirId=" + dirId +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize +
                ", dirFileCount=" + dirFileCount +
                ", fileThumbnail='" + fileThumbnail + '\'' +
                ", fileModifiedTime=" + fileModifiedTime +
                ", isThumbnail=" + isThumbnail +
                ", isSelect=" + isSelect +
                ", orientation=" + orientation +
                ", duration=" + duration +
                '}';
    }
}

