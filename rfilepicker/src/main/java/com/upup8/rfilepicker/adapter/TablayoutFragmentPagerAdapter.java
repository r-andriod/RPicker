package com.upup8.rfilepicker.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.upup8.rfilepicker.fragment.RFilePickerFragment;
import com.upup8.rfilepicker.fragment.PhotoPickerFragment;

/**
 * TablayoutFragmentPagerAdapter
 * Created by renwoxing on 2017/11/27.
 */
public class TablayoutFragmentPagerAdapter extends FragmentPagerAdapter {

    //final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[]{"文档", "图片", "影音", "其它"};
    private Context context;

    public TablayoutFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return RFilePickerFragment.newInstance(position + 1);
        }
        return new PhotoPickerFragment();
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}