package BottomNavigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by OK on 5/11/2018.
 */

public class PagerAdapterMaster extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapterMaster(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MerekFragment merekFragment = new MerekFragment();
                return merekFragment;
            case 1:
                ProcessorFragment processorFragment = new ProcessorFragment();
                return processorFragment;
            case 2:
                VGAFragment vgaFragment = new VGAFragment();
                return vgaFragment;
            case 3:
                AnggotaFragment anggotaFragment = new AnggotaFragment();
                return anggotaFragment;
            case 4:
                LaptopFragment laptopFragment = new LaptopFragment();
                return laptopFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
