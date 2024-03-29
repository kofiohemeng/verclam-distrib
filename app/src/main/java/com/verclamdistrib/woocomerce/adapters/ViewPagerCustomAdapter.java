package com.verclamdistrib.woocomerce.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.verclamdistrib.woocomerce.customs.CustomViewPager;
import com.verclamdistrib.woocomerce.models.category_model.CategoryDetails;


/**
 * ViewPagerAdapter is the adapter class of ViewPager holding Number of Pages in HomePage
 **/

public class ViewPagerCustomAdapter extends FragmentStatePagerAdapter {

    // Keep track of current Position
    private int mCurrentPosition = -1;
    
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();


    public ViewPagerCustomAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }


    //********** Returns the Fragment associated with a specified Position *********//

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }



    //********** Returns the number of Views available *********//

    @Override
    public int getCount() {
        return fragmentList.size();
    }



    //********** Called by the ViewPager to obtain the Title of specified Page *********//

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }



    //********** Called to Inform the Adapter of which Item is currently considered to be the Primary *********//

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        if (position != mCurrentPosition) {
            Fragment fragment = (Fragment) object;
            
            // Cast the given container to CustomViewPager
            CustomViewPager pager = (CustomViewPager) container;

            if (fragment != null && fragment.getView() != null) {
                mCurrentPosition = position;

                // Set the measured View to CustomViewPager
                pager.setCurrentView(fragment.getView());
            }
        }
    }



    //********** Adds the new Fragment to the ViewPager *********//

    public void addFragment(Fragment fragment, String title) {
        // Add the given Fragment to FragmentList
        fragmentList.add(fragment);
        
        // Add the Title of a given Fragment to FragmentTitleList
        fragmentTitleList.add(title);
    }

}

