package com.jcmels.liba.piliplaydemo;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.jcmels.liba.piliplaydemo.fragment.CategoryFragment;
import com.jcmels.liba.piliplaydemo.fragment.HotFragment;
import com.jcmels.liba.piliplaydemo.fragment.MyFragment;


public class MainActivity extends AppCompatActivity implements
        CategoryFragment.OnFragmentInteractionListener,
        HotFragment.OnFragmentInteractionListener,
        MyFragment.OnFragmentInteractionListener {

    private AHBottomNavigation bottomnavigation;
    public Context context;
    private android.widget.FrameLayout flmainfragment;
    private HotFragment hotFragment;
    private CategoryFragment categoryFragment;
    private MyFragment myFragment;
    private FragmentTransaction transition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initView();
        initData();
        hotFragment = new HotFragment();
        transition = getSupportFragmentManager().beginTransaction();
        transition.add(R.id.fl_mainfragment, hotFragment).commit();
        transition.show(hotFragment);
    }

    private void initData() {
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.ic_hot, R.color.color_tab_1);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_2, R.drawable.ic_category, R.color.color_tab_2);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_3, R.drawable.ic_my, R.color.color_tab_3);
        bottomnavigation.addItem(item1);
        bottomnavigation.addItem(item2);
        bottomnavigation.addItem(item3);
        bottomnavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));
        bottomnavigation.setAccentColor(Color.parseColor("#F63D2B"));
        bottomnavigation.setInactiveColor(Color.parseColor("#747474"));

        bottomnavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                transition = getSupportFragmentManager().beginTransaction();
                hideAddFragment(transition);
                switch (position) {
                    case 0: {
                        if (hotFragment == null) {
                            hotFragment = new HotFragment();
                            transition.add(R.id.fl_mainfragment, hotFragment);
                        }
                        transition.show(hotFragment);
                        break;
                    }
                    case 1: {
                        if (categoryFragment == null) {
                            categoryFragment = new CategoryFragment();
                            transition.add(R.id.fl_mainfragment, categoryFragment);
                        }
                        transition.show(categoryFragment);
                        break;
                    }
                    case 2: {
                        if (myFragment == null) {
                            myFragment = new MyFragment();
                            transition.add(R.id.fl_mainfragment, myFragment);
                        }
                        transition.show(myFragment);
                        break;
                    }
                }
                transition.commit();
                return true;
            }
        });
    }

    private void hideAddFragment(FragmentTransaction transition) {
        if (hotFragment != null) transition.hide(hotFragment);
        if (categoryFragment != null) transition.hide(categoryFragment);
        if (myFragment != null) transition.hide(myFragment);
    }

    private void initView() {
        bottomnavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        flmainfragment = (FrameLayout) findViewById(R.id.fl_mainfragment);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
