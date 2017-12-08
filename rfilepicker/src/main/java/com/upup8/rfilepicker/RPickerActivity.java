package com.upup8.rfilepicker;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.upup8.rfilepicker.adapter.RFilePickerItemClickListener;
import com.upup8.rfilepicker.adapter.TabLayoutFragmentPagerAdapter;
import com.upup8.rfilepicker.databinding.ActivityRpickerBinding;
import com.upup8.rfilepicker.fragment.RFilePickerFragment;
import com.upup8.rfilepicker.model.FileEntity;
import com.upup8.rfilepicker.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

import static com.upup8.rfilepicker.data.RFilePickerConst.MEDIA_TYPE_AUDIO;
import static com.upup8.rfilepicker.data.RFilePickerConst.MEDIA_TYPE_DOCUMENT;
import static com.upup8.rfilepicker.data.RFilePickerConst.MEDIA_TYPE_IMAGE;
import static com.upup8.rfilepicker.data.RFilePickerConst.MEDIA_TYPE_VIDEO;

public class RPickerActivity extends AppCompatActivity implements RFilePickerItemClickListener {

    ActivityRpickerBinding binding;

    private TabLayoutFragmentPagerAdapter pagerAdapter;
    private List<FileEntity> mSelectFiles;
    private List<Fragment> mFragmentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rpicker);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rpicker);

        initFragment();
    }

    private void initFragment() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(RFilePickerFragment.newInstance(MEDIA_TYPE_IMAGE));
        mFragmentList.add(RFilePickerFragment.newInstance(MEDIA_TYPE_VIDEO));
        mFragmentList.add(RFilePickerFragment.newInstance(MEDIA_TYPE_AUDIO));
        mFragmentList.add(RFilePickerFragment.newInstance(MEDIA_TYPE_DOCUMENT));
        List<String> titleList = new ArrayList<>();
        titleList.add("图片");
        titleList.add("视频");
        titleList.add("语音");
        titleList.add("文件");
        pagerAdapter = new TabLayoutFragmentPagerAdapter(this, getSupportFragmentManager(), mFragmentList, titleList);

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
        refreshView();
    }


    private void refreshView() {
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
