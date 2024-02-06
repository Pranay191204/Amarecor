package com.amarecor.pranay.amarecor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class Pageadapteraddexp extends FragmentStatePagerAdapter {
    int numOfTab;
    public Pageadapteraddexp(FragmentManager fm, int numOfTab) {
        super(fm);
        this.numOfTab=numOfTab;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                AddExpenseDetails tb1=new AddExpenseDetails();
                return tb1;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTab;
    }
}
