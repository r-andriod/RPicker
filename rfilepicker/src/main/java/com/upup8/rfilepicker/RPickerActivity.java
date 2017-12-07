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
     * @param fileEntities
     */
    @Override
    public void onFileItemSelectClick(List<FileEntity> fileEntities) {
        Log.d("main activity", "------------------ = >onFileItemSelectClick:  size:" + fileEntities.size());
        long count = 0l;
        if (fileEntities != null && fileEntities.size() > 0) {
            for (FileEntity item : fileEntities) {
                count = count + item.getFileSize();
            }
            binding.rpChooseSize.setText(getString(R.string.rp_select_txt, FileUtils.getReadableFileSize(count)));
            binding.rpTvSend.setText(getString(R.string.rp_ok_txt, fileEntities.size()));
        }
    }
}
