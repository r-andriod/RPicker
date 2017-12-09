package com.upup8.rfilepicker;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.upup8.rfilepicker.adapter.RFilePickerItemClickListener;
import com.upup8.rfilepicker.adapter.TabLayoutFragmentPagerAdapter;
import com.upup8.rfilepicker.databinding.ActivityRpickerBinding;
import com.upup8.rfilepicker.fragment.RFilePickerFragment;
import com.upup8.rfilepicker.model.FileEntity;
import com.upup8.rfilepicker.utils.FileUtils;
import com.upup8.rfilepicker.utils.SystemUtil;

import java.lang.reflect.Field;
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
        binding.rpVpChooseFile.setOffscreenPageLimit(3);
        binding.rpVpChooseFile.setAdapter(pagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.rpVpChooseFile);
        binding.tabLayout.setTabMode(TabLayout.MODE_FIXED);

        //反射修改宽度
        setUpIndicatorWidth(binding.tabLayout);
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

    private void setUpIndicatorWidth(TabLayout mTabLayout) {
        Class<?> tabLayoutClass = mTabLayout.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayoutClass.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        LinearLayout layout = null;
        try {
            if (tabStrip != null) {
                layout = (LinearLayout) tabStrip.get(mTabLayout);
            }
            for (int i = 0; i < layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);
                child.setPadding(0, 0, 0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(SystemUtil.dp(50f));
                    params.setMarginEnd(SystemUtil.dp(50f));
                }
                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
