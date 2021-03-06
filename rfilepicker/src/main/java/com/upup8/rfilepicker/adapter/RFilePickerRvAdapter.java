package com.upup8.rfilepicker.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.upup8.rfilepicker.R;
import com.upup8.rfilepicker.RPickerActivity;
import com.upup8.rfilepicker.databinding.RpfilepickerFileBodyItemBinding;
import com.upup8.rfilepicker.databinding.RpfilepickerFileHeadItemBinding;
import com.upup8.rfilepicker.model.FileEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.upup8.rfilepicker.data.RFilePickerConst.FILE_VH_BODY;
import static com.upup8.rfilepicker.data.RFilePickerConst.FILE_VH_HEADE;

/**
 * RFilePickerRvAdapter
 * Created by renwoxing on 2017/12/1.
 */
public class RFilePickerRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    // 数据展示 list
    private List<FileEntity> mFileEntityList;
    //全部 list
    private List<FileEntity> mAllfileList;
    //选择 list
    private List<FileEntity> mSelectFileList;

    private Context mContext;

    private RpfilepickerFileHeadItemBinding headerBinding;
    private RpfilepickerFileBodyItemBinding bodyBinding;
    private LayoutInflater inflater;
    private OnScrollListener mOnScrollListener;


    private RFilePickerItemClickListener clickListener;


    @Deprecated
    public RFilePickerRvAdapter(Context mContext, List<FileEntity> fileEntityList, RFilePickerItemClickListener clickListener) {
        this.mFileEntityList = fileEntityList;
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
        this.clickListener = clickListener;
    }

    public RFilePickerRvAdapter(Context mContext, SparseArray<FileEntity> dirArr, SparseArray<FileEntity> fileArr, RFilePickerItemClickListener clickListener) {
        mAllfileList = new ArrayList<>();
        for (int i = 0; i < fileArr.size(); i++) {
            mAllfileList.add(fileArr.valueAt(i));
        }
        this.mFileEntityList = convertSparseArrayToList(dirArr, fileArr, -1l);
        this.mSelectFileList = new ArrayList<>();
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
        this.clickListener = clickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case FILE_VH_HEADE:
                headerBinding = DataBindingUtil.inflate(inflater, R.layout.rpfilepicker_file_head_item, parent, false);
                holder = new FileHeaderViewHolder(headerBinding);
                break;
            case FILE_VH_BODY:
                bodyBinding = DataBindingUtil.inflate(inflater, R.layout.rpfilepicker_file_body_item, parent, false);
                holder = new FileBodyViewHolder(bodyBinding);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FileHeaderViewHolder) {
            //heada
            final FileHeaderViewHolder headerViewHolder = (FileHeaderViewHolder) holder;
            headerViewHolder.getBinding().setRfilePickerHeader(mFileEntityList.get(position));
            headerViewHolder.getBinding().executePendingBindings();
        } else if (holder instanceof FileBodyViewHolder) {
            //body
            final FileBodyViewHolder bodyViewHolder = (FileBodyViewHolder) holder;
            bodyViewHolder.getBinding().setRfilePickerBody(mFileEntityList.get(position));
            //设置选中与否
            bodyViewHolder.binding.rfilePickerSelect.setSelected(mFileEntityList.get(position).isSelect());

            if (mFileEntityList.get(position).isThumbnail()) {
                String imagePath = mFileEntityList.get(position).getFileThumbnail();
                Glide.with(mContext)
                        .load(new File(imagePath))
                        .apply(RequestOptions
                                .centerInsideTransform()
                                .override(40, 42)
                                .placeholder(R.drawable.ic_picker_file))
                        //.thumbnail(0.5f)
                        .into(bodyViewHolder.getBinding().rfilePickerImageType);
            } else {
                //Glide.with(mContext).load(fileEntityList.get(position).getFilePath()).into(bodyViewHolder.getBinding().rfilePickerImageType);
            }
            bodyViewHolder.getBinding().executePendingBindings();
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (mFileEntityList.get(position).getFileId() == 0) {
            return FILE_VH_HEADE;
        } else {
            return FILE_VH_BODY;
        }
    }

    @Override
    public int getItemCount() {
        return mFileEntityList.size();
    }


    public class FileHeaderViewHolder extends RecyclerView.ViewHolder {

        private RpfilepickerFileHeadItemBinding binding;

        public FileHeaderViewHolder(final RpfilepickerFileHeadItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getLayoutPosition();
                    if (mFileEntityList.get(position).isSelect()) {
                        binding.rfilePickerHeaderTvUpDown.setSelected(false);
                        mFileEntityList.get(position).setSelect(false);
                        Long dirId = mFileEntityList.get(position).getDirId();  // DIR id
                        // child pos
                        int pos = mFileEntityList.size() > position + 1 ? position + 1 : mFileEntityList.size();
                        //Log.d("FileHeaderViewHolder", "onClick: " + position + " pos:" + pos + " size:" + fileList.size());
                        //Log.d("--2", "onClick:  file arr size:" + fileList.size());
                        addAllChildList(getChildListByGroupId(mAllfileList, dirId), pos);
                    } else {
                        mFileEntityList.get(position).setSelect(true);
                        binding.rfilePickerHeaderTvUpDown.setSelected(true);
                        //Log.d("---item", "list size:" + fileEntityList.size() + "pos: " + position + " file size:" + fileEntityList.get(position).getDirFileCount() + "\n" + fileEntityList.get(position).toString());
                        int pos = mFileEntityList.size() - 1 > position + 1 ? position + 1 : mFileEntityList.size() - 1;
                        if (mFileEntityList.get(pos).getFileId() != 0) { //防止 误删除下一个 group
                            deleteAllChildList(pos, (int) mFileEntityList.get(position).getDirFileCount());
                        }
                    }
                }
            });
        }

        public RpfilepickerFileHeadItemBinding getBinding() {
            return binding;
        }

    }

    public class FileBodyViewHolder extends RecyclerView.ViewHolder {

        private RpfilepickerFileBodyItemBinding binding;

        public FileBodyViewHolder(RpfilepickerFileBodyItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getLayoutPosition();
                    FileEntity tmp = mAllfileList.get(mAllfileList.indexOf(mFileEntityList.get(position)));
                    boolean exist = mSelectFileList.contains(tmp);
                    //Log.d("body", "---------- >onClick: exist:" + exist + " string:\n" + tmp.toString() + " \n select size:" + mSelectFileList.size());
                    if (exist) {
                        mSelectFileList.remove(tmp);
                        binding.rfilePickerSelect.setSelected(false);
                        tmp.setSelect(false);
                        clickListener.onFileItemUnSelectClick(tmp);
                    } else if (!isFileMax()) {
                        mSelectFileList.add(tmp);
                        //mFileEntityList.get(position).setSelect(true);
                        binding.rfilePickerSelect.setSelected(true);
                        tmp.setSelect(true);
                        // 回调选中 list
                        clickListener.onFileItemSelectClick(tmp);
                    }
                }
            });

        }

        public RpfilepickerFileBodyItemBinding getBinding() {
            return binding;
        }
    }


    /**
     * 滚动监听接口
     */
    public interface OnScrollListener {
        void scrollTo(int pos);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }


    /**
     * 处理 SparseArray to list
     *
     * @param groupArr
     * @param fileSparseArr
     * @param groupId       根据 group id 来转数据 默认为第一个 group
     * @return
     */
    private List<FileEntity> convertSparseArrayToList(SparseArray<FileEntity> groupArr, SparseArray<FileEntity> fileSparseArr, Long groupId) {
        List<FileEntity> fileList = new ArrayList<>();
        for (int i = 0; i < groupArr.size(); i++) {
            fileList.add(groupArr.get(groupArr.keyAt(i)));
            for (int j = 0; j < fileSparseArr.size(); j++) {
                if (groupId.longValue() == -1l) {
                    if (groupArr.keyAt(i) == (int) fileSparseArr.get(fileSparseArr.keyAt(j)).getDirId().longValue()) {
                        fileList.add(fileSparseArr.get(fileSparseArr.keyAt(j)));
                    }
                } else {
                    if (groupArr.keyAt(i) == fileSparseArr.get(fileSparseArr.keyAt(j)).getDirId().intValue() && groupArr.keyAt(i) == groupId.intValue()) {
                        fileList.add(fileSparseArr.get(fileSparseArr.keyAt(j)));
                    }
                }
            }
        }
        //Log.d("arr to list", "convertSparseArrayToList:  size: " + fileList.size() + "|" + groupId + " group size:" + groupArr.size());
        return fileList;
    }

    /**
     * 根据group id 获取 child list
     *
     * @param sourceFileList
     * @param groupId
     * @return
     */
    private List<FileEntity> getChildListByGroupId(List<FileEntity> sourceFileList, Long groupId) {
        List<FileEntity> retFileList = new ArrayList<>();
        for (int j = 0; j < sourceFileList.size(); j++) {
            //Log.d("---- >", j + ": getChildListByGroupId: " + sourceFileList.get(j).toString());
            //if (groupId.equals(sourceFileList.get(j).getDirId()) && sourceFileList.get(j).getDirId().longValue() == groupId.longValue()) {
            if (groupId.equals(sourceFileList.get(j).getDirId())) {
                //Log.d("<---", "getChildListByGroupId: " + sourceFileList.get(j).toString());
                retFileList.add(sourceFileList.get(j));
            }
        }
        //Log.d(" child----", "getChildListByGroupId: size: " + retFileList.size() + " file list size:" + sourceFileList.size() + " | dir id:" + groupId);
        return retFileList;
    }


    /**
     * 添加所有child
     *
     * @param lists
     * @param position
     */
    private void addAllChildList(List<FileEntity> lists, int position) {
        mFileEntityList.addAll(position, lists);
        notifyItemRangeInserted(position, lists.size());
    }

    /**
     * 删除所有child
     *
     * @param position
     * @param itemCount
     */
    private void deleteAllChildList(int position, int itemCount) {
        for (int i = 0; i < itemCount; i++) {
            //Log.d("del---", "deleteAllChildList: pos:" + position + "|" + itemCount);
            mFileEntityList.remove(position);
        }
        notifyItemRangeRemoved(position, itemCount);
    }

    private boolean isFileMax() {
        if (mContext != null) {
            if (mContext instanceof RPickerActivity) {
                RPickerActivity activity = (RPickerActivity) mContext;
                if (null != activity) return activity.isMaxFile();
            }
        }
        return false;
    }
}
