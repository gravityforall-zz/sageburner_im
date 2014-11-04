

package com.sageburner.im.android.ui;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Pager adapter
 */
public class BootstrapPagerAdapter extends FragmentPagerAdapter {

    private final Resources resources;

    // This holds all the currently displayable fragments, in order from left to right.
    private List<Fragment> fragments = new ArrayList<Fragment>();
    // This holds the titles of all the currently available fragments
    private List<String> fragmentTitles = new ArrayList<String>();


    /**
     * Create pager adapter
     *
     * @param resources
     * @param fragmentManager
     */
    public BootstrapPagerAdapter(final Resources resources, final FragmentManager fragmentManager) {
        super(fragmentManager);
        this.resources = resources;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    //-----------------------------------------------------------------------------
    // Add "view" to right end of "views".
    // Returns the position of the new view.
    // The app should call this to add pages; not used by ViewPager.
    public int addFragment(Fragment f, String title)
    {
        return addFragment (f, title, fragments.size());
    }

    //-----------------------------------------------------------------------------
    // Add "view" at "position" to "views".
    // Returns position of new view.
    // The app should call this to add pages; not used by ViewPager.
    public int addFragment(Fragment f, String title, int position) {
        fragments.add(position, f);
        fragmentTitles.add(position, title);
        return position;
    }

    //-----------------------------------------------------------------------------
    // Removes "fragment" from "fragments".
    // Returns position of removed view.
    // The app should call this to remove pages; not used by ViewPager.
    public int removeFragment(ViewPager pager, Fragment f) {
        return removeFragment(pager, fragments.indexOf(f));
    }

    //-----------------------------------------------------------------------------
    // Removes the "view" at "position" from "views".
    // Retuns position of removed view.
    // The app should call this to remove pages; not used by ViewPager.
    public int removeFragment(ViewPager pager, int position) {
        // ViewPager doesn't have a delete method; the closest is to set the adapter
        // again.  When doing so, it deletes all its views.  Then we can delete the view
        // from from the adapter and finally set the adapter to the pager again.  Note
        // that we set the adapter to null before removing the view from "views" - that's
        // because while ViewPager deletes all its views, it will call destroyItem which
        // will in turn cause a null pointer ref.
        pager.setAdapter(null);
        fragments.remove(position);
        fragmentTitles.remove(position);
        pager.setAdapter(this);

        return position;
    }

    @Override
    public Fragment getItem(final int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        return fragmentTitles.get(position);
    }
}
