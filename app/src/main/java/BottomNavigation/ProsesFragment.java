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
 * A simple {@link Fragment} subclass.
 */
public class ProsesFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    public ProsesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_proses, container, false);

        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.tool_bar_fragment);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        getActivity().setTitle(null);

        tabLayout = (TabLayout)view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Peminjaman"));
        tabLayout.addTab(tabLayout.newTab().setText("Pengembalian"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager)view.findViewById(R.id.proses_pager);
        final PagerAdapterProses pagerAdapterProses = new PagerAdapterProses(
                getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapterProses);
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
