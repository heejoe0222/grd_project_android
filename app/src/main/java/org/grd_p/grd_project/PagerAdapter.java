package org.grd_p.grd_project;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.grd_p.grd_project.mainFragment.Fragment_main;
import org.grd_p.grd_project.mainFragment.Fragment_report;
import org.grd_p.grd_project.mainFragment.Fragment_setting;
import org.grd_p.grd_project.mainFragment.Fragment_video;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;
    private String[] fragmentTitleList= {"main","report","video","setting"};

    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                Fragment_main tab1 = new Fragment_main();
                return tab1;
            case 1:
                Fragment_report tab2 = new Fragment_report();
                return tab2;
            case 2:
                Fragment_video tab3 = new Fragment_video();
                return tab3;
            case 3:
                Fragment_setting tab4 = new Fragment_setting();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList[position];
    }
}
