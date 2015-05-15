package howest.desopdrachtmobileapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class MainActivity extends ActionBarActivity implements DirectionFragment.TextClicked, NavigationDrawerFragment.NavigationDrawerCallbacks {
    public ArrayList<String> types = new ArrayList<String>();
    String oldFragmentTag;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    public void sendText(String text){
        // Get Fragment B
//        MapsFragment frag = (MapsFragment)
//                getSupportFragmentManager().findFragmentById(R.id.);
//        frag.updateText(text);
        if (!types.contains(text)) {
            types.add(text);
        }

    }
    @Override
    public void deleteText(String text){
        // Get Fragment B
//        MapsFragment frag = (MapsFragment)
//                getSupportFragmentManager().findFragmentById(R.id.);
//        frag.updateText(text);
        if (types.contains(text)) {
//            types.add(text);
            types.remove(types.indexOf(text));
            types = types;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Fragment objFragment = null;
        android.app.ListFragment fragments = null;
        //omzetten arraylist in array van strings
        Bundle args = new Bundle();
        String[] mStringArray = new String[types.size()];
        mStringArray = types.toArray(mStringArray);
        // end omzetten

        switch (position) {
            case 1:
                fragments = new MainFragment();
                args.putStringArray("types", mStringArray);
                fragments.setArguments(args);
                updateFragment(fragments, objFragment, "main1");
                oldFragmentTag = "main";
                break;
            case 2:
                objFragment = new MapsFragment();
                args.putStringArray("types", mStringArray);
                objFragment.setArguments(args);
                updateFragment(fragments, objFragment, "maps");
                oldFragmentTag = "maps";
                break;
            case 0:
                objFragment = new DirectionFragment();
                updateFragment(fragments, objFragment, "direction");
                oldFragmentTag = "direction";
                break;
        }
        // update the main content by replacing fragments

    }

    public void updateFragment(android.app.ListFragment fragments, Fragment objFragment, String tag){

        if(fragments != null) {

                getFragmentManager().beginTransaction()
                        .replace(R.id.container, fragments, tag)
                        .addToBackStack(null)
                        .commit();
        }else {
            Fragment f = this.getSupportFragmentManager().findFragmentByTag(tag);
            if(f != null){
                getSupportFragmentManager().beginTransaction()
                        .show(f)
                        .commit();

            }else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, objFragment, tag)
                        .addToBackStack(null)
                        .commit();
            }
        }

                if(oldFragmentTag == "main"){
            android.app.Fragment f = getFragmentManager().findFragmentByTag(oldFragmentTag);
                    getFragmentManager().beginTransaction()
                    .replace(R.id.container, new android.app.Fragment())
                    .addToBackStack(null)
                    .commit();
            oldFragmentTag = null;
        }
        if(oldFragmentTag != "main" && oldFragmentTag != null){
            Fragment f = getSupportFragmentManager().findFragmentByTag(oldFragmentTag);
            getSupportFragmentManager().beginTransaction()
                    .hide(f)
                    .commit();
            oldFragmentTag = null;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
