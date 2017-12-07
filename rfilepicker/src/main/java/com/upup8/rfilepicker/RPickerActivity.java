package com.upup8.rfilepicker;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.upup8.rfilepicker.adapter.RFilePickerItemClickListener;
import com.upup8.rfilepicker.adapter.TablayoutFragmentPagerAdapter;
import com.upup8.rfilepicker.databinding.ActivityRpickerBinding;
import com.upup8.rfilepicker.model.FileEntity;
import com.upup8.rfilepicker.utils.FileUtils;

import java.util.List;

public class RPickerActivity extends AppCompatActivity implements RFilePickerItemClickListener {

    ActivityRpickerBinding binding;

    private TablayoutFragmentPagerAdapter pagerAdapter;
    private List<FileEntity> mSelectFiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rpicker);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rpicker);

        initFragment();
    }

    private void initFragment() {
        pagerAdapter = new TablayoutFragmentPagerAdapter(this, getSupportFragmentManager());
        binding.rpVpChooseFile.setAdapter(pagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.rpVpChooseFile);
        binding.tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }


    /**
     * 选中文件回调
     *
     * @param fileEntities
     */
    @Override
    public void onFileItemSelectClick(List<FileEntity> fileEntities) {
        Log.d("main activity", "------------------ = >onFileItemSelectClick:  size:" + fileEntities.size());
        mSelectFiles = fileEntities;
        refershView();
    }


    private void refershView() {
        int count = 0;
        long size = 0;
        if (mSelectFiles != null && mSelectFiles.size() > 0) {
            for (int i = 0; i < mSelectFiles.size(); i++) {
                size = size + mSelectFiles.get(i).getFileSize();
                count++;
            }
        }
        binding.rpChooseSize.setText(getString(R.string.rp_select_txt, FileUtils.getReadableFileSize(size)));
        if (count > 0) {
            binding.rpTvSend.setSelected(true);
            binding.rpTvSend.setTextColor(getResources().getColor(R.color.colorFFFFFF));
        } else {
            binding.rpTvSend.setSelected(false);
            binding.rpTvSend.setTextColor(getResources().getColor(R.color.colorC9C9C9));
        }
        if (true) {//多选
            binding.rpTvSend.setText("发送(" + count + ")");
        } else {
            //mBinding.tvSend.setText("发送");
        }


    }
}
