package com.upup8.rfilepicker.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.upup8.rfilepicker.R;
import com.upup8.rfilepicker.databinding.RpfilepickerFileBodyItemBinding;
import com.upup8.rfilepicker.databinding.RpfilepickerFileHeadItemBinding;
import com.upup8.rfilepicker.model.FileEntity;

import java.io.File;
import java.util.List;

import static com.upup8.rfilepicker.data.RFilePickerConst.FILE_VH_BODY;
import static com.upup8.rfilepicker.data.RFilePickerConst.FILE_VH_HEADE;

/**
 * DocumentRvAdapter
 * Created by renwoxing on 2017/12/1.
 */
public class DocumentRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<FileEntity> fileEntityList;

    private Context mContext;

    private RpfilepickerFileHeadItemBinding headerBinding;
    private RpfilepickerFileBodyItemBinding bodyBinding;
    private LayoutInflater inflater;


    public DocumentRvAdapter(Context mContext,List<FileEntity> fileEntityList) {
        this.fileEntityList = fileEntityList;
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case FILE_VH_HEADE:
                headerBinding = DataBindingUtil.inflate(inflater, R.layout.rpfilepicker_file_head_item, parent, false);
                //view = LayoutInflater.from(mContext).inflate(R.layout.rpfilepicker_file_head_item, parent, false);
                holder = new FileHeaderViewHolder(headerBinding);
                break;
            case FILE_VH_BODY:
                bodyBinding = DataBindingUtil.inflate(inflater, R.layout.rpfilepicker_file_body_item, parent, false);
                //view = LayoutInflater.from(mContext).inflate(R.layout.rpfilepicker_file_body_item, parent, false);
                holder = new FileBodyViewHolder(bodyBinding);
                break;
            default:
                headerBinding = DataBindingUtil.inflate(inflater, R.layout.rpfilepicker_file_head_item, parent, false);
                //view = LayoutInflater.from(mContext).inflate(R.layout.rpfilepicker_file_head_item, parent, false);
                holder = new FileHeaderViewHolder(headerBinding);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FileHeaderViewHolder) {
            //head
            final FileHeaderViewHolder headerViewHolder = (FileHeaderViewHolder) holder;
            headerViewHolder.getBinding().setRfilePickerHeader(fileEntityList.get(position));
        } else if (holder instanceof FileBodyViewHolder) {
            //body
            final FileBodyViewHolder bodyViewHolder = (FileBodyViewHolder) holder;
            bodyViewHolder.getBinding().setRfilePickerBody(fileEntityList.get(position));
            if (fileEntityList.get(position).isThumbnail()) {
                String imagePath = fileEntityList.get(position).getFileThumbnail();
                Glide.with(mContext)
                        .load(new File(imagePath))
                        .apply(RequestOptions
                                .centerInsideTransform()
                                .override(40, 42)
                                .placeholder(R.drawable.ic_picker_file))
                        //.thumbnail(0.5f)
                        .into(bodyViewHolder.getBinding().rfilePickerImageType);
                //Glide.with(mContext).load(fileEntityList.get(position).getFileThumbnail()).into(bodyViewHolder.getBinding().rfilePickerImageType);
            } else {
                //Glide.with(mContext).load(fileEntityList.get(position).getFilePath()).into(bodyViewHolder.getBinding().rfilePickerImageType);
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        if (null != fileEntityList.get(position)) {
            if (fileEntityList.get(position).getFileId() == 0) {
                return FILE_VH_HEADE;
            } else {
                return FILE_VH_BODY;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return fileEntityList.size();
    }


    public class FileHeaderViewHolder extends RecyclerView.ViewHolder {

        private RpfilepickerFileHeadItemBinding binding;

        public FileHeaderViewHolder(RpfilepickerFileHeadItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
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
        }

        public RpfilepickerFileBodyItemBinding getBinding() {
            return binding;
        }
    }
}
