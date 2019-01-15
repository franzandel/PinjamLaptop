package BottomNavigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import LoginRegister.Pengembalian;

/**
 * Created by OK on 5/11/2018.
 */

public class PagerAdapterProses extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapterProses(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PeminjamanFragment peminjamanFragment = new PeminjamanFragment();
                return peminjamanFragment;
            case 1:
                PengembalianFragment pengembalianFragment = new PengembalianFragment();
                return pengembalianFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
