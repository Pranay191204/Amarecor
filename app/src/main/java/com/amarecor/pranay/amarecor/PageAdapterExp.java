package com.amarecor.pranay.amarecor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PageAdapterExp extends FragmentStatePagerAdapter {
    int numOfTab;
    public PageAdapterExp(FragmentManager fm, int numOfTab) {
        super(fm);
        this.numOfTab=numOfTab;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                ExpenseDetails tb1=new ExpenseDetails();
                return tb1;
            case 1:
                Location tb2=new Location();
                return tb2;
            case 2:
                Other tb3=new Other();
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
