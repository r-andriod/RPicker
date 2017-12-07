package com.upup8.rfilepicker.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.upup8.rfilepicker.fragment.RFilePickerFragment;

/**
 * TablayoutFragmentPagerAdapter
 * Created by renwoxing on 2017/11/27.
 */
public class TablayoutFragmentPagerAdapter extends FragmentPagerAdapter {

    //final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[]{"图片", "视频", "语音", "文档"};
    private Context mContext;

    public TablayoutFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
//        if (position == 0) {
//            return RFilePickerFragment.newInstance(RFilePickerConst.MEDIA_TYPE_IMAGE);
//        }
        return RFilePickerFragment.newInstance(position);
        //return new PhotoPickerFragment();
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