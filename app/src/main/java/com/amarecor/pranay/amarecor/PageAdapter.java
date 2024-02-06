package com.amarecor.pranay.amarecor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PageAdapter extends FragmentStatePagerAdapter {
    int numOfTab;
    public PageAdapter(FragmentManager fm, int numOfTab) {
        super(fm);
        this.numOfTab=numOfTab;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                Basic tb1=new Basic();
                return tb1;
            case 1:
                Doctors tb2=new Doctors();
                return tb2;
            case 2:
                RCPA tb3=new RCPA();
                return tb3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTab;
    }
}
