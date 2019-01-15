package BottomNavigation;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ok.pinjamlaptop.R;

/**
 * Created by ryuze on 6/25/2018.
 */

public class MasterFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    public MasterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_master, container, false);

        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.tool_bar_fragment);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        getActivity().setTitle(null);

        tabLayout = (TabLayout)view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Merek"));
        tabLayout.addTab(tabLayout.newTab().setText("Processor"));
        tabLayout.addTab(tabLayout.newTab().setText("VGA"));
        tabLayout.addTab(tabLayout.newTab().setText("Anggota"));
        tabLayout.addTab(tabLayout.newTab().setText("Laptop"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        viewPager = (ViewPager)view.findViewById(R.id.proses_pager);
        final PagerAdapterMaster prosesPagerAdapter = new PagerAdapterMaster(
                getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(prosesPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

}
