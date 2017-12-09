package com.upup8.rfilepicker.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * TabLayoutFragmentPagerAdapter
 * Created by renwoxing on 2017/11/27.
 */
public class TabLayoutFragmentPagerAdapter extends FragmentPagerAdapter {

    //final int PAGE_COUNT = 4;
    //private String tabTitles[] = new String[]{"图片", "视频", "语音", "文档"};
    private Context mContext;
    List<String> tabTitles;
    List<Fragment> fragmentList;

    public Fragment currentFragment;


    public TabLayoutFragmentPagerAdapter(Context context, FragmentManager fm, List<Fragment> fragments, List<String> titleList) {
        super(fm);
        this.mContext = context;
        this.tabTitles = titleList;
        this.fragmentList = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
        //return RFilePickerFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }


    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        currentFragment = (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }
}