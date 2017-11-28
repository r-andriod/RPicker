package com.upup8.rfilepicker;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import com.upup8.rfilepicker.adapter.TablayoutFragmentPagerAdapter;
import com.upup8.rfilepicker.databinding.ActivityRpickerBinding;

public class RPickerActivity extends AppCompatActivity {

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
        pagerAdapter = new TablayoutFragmentPagerAdapter(getSupportFragmentManager(), this);
        binding.vpChooseFile.setAdapter(pagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.vpChooseFile);
        binding.tabLayout.setTabMode(TabLayout.MODE_FIXED);

    }
}
