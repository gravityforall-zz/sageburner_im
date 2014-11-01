

package com.sageburner.im.android.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.ViewPager;
import android.view.View;
import com.sageburner.im.android.R;

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

    //-----------------------------------------------------------------------------
    // Used by ViewPager.  "Object" represents the page; tell the ViewPager where the
    // page should be displayed, from left-to-right.  If the page no longer exists,
    // return POSITION_NONE.
//    @Override
//    public int getItemPosition(Object object) {
//        int index = fragments.indexOf(object);
//        if (index == -1)
//            return POSITION_NONE;
//        else
//            return index;
//    }

    //-----------------------------------------------------------------------------
    // Used by ViewPager.  Called when ViewPager needs a page to display; it is our job
    // to add the page to the container, which is normally the ViewPager itself.  Since
    // all our pages are persistent, we simply retrieve it from our "views" ArrayList.
//    @Override
//    public Object instantiateItem (ViewGroup container, int position)
//    {
//        View v = views.get (position);
//        container.addView (v);
//        return v;
//    }

    //-----------------------------------------------------------------------------
    // Used by ViewPager.  Called when ViewPager no longer needs a page to display; it
    // is our job to remove the page from the container, which is normally the
    // ViewPager itself.  Since all our pages are persistent, we do nothing to the
    // contents of our "views" ArrayList.
//    @Override
//    public void destroyItem (ViewGroup container, int position, Object object)
//    {
//        container.removeView (views.get (position));
//    }

    //-----------------------------------------------------------------------------
    // Used by ViewPager; can be used by app as well.
    // Returns the total number of pages that the ViewPage can display.  This must
    // never be 0.
    @Override
    public int getCount() {
        return fragments.size();
    }

    //-----------------------------------------------------------------------------
    // Used by ViewPager.
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view == object;
//    }

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
    // Removes "view" from "views".
    // Retuns position of removed view.
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

    // Other relevant methods:

    // finishUpdate - called by the ViewPager - we don't care about what pages the
    // pager is displaying so we don't use this method.

//    @Override
//    public int getCount() {
//        return 2;
//    }

    @Override
    public Fragment getItem(final int position) {
        return fragments.get(position);
//        final Fragment result;
//        switch (position) {
//            case 0:
//                result = new FriendsListFragment();
//                break;
//            case 1:
//                result = new ConversationFragment();
//                break;
//            default:
//                result = null;
//                break;
//        }
//        if (result != null) {
//            result.setArguments(new Bundle()); //TODO do we need this?
//        }
//        return result;
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        return fragmentTitles.get(position);
    }
}
