package com.example.abusufian.jobcueandroid;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.PreferenceActivity;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JobSearch extends AppCompatActivity implements
        ActionBar.OnNavigationListener {
    public static String[] subject;
    public static String[] description;
    public static String[] id;
    public static final String KEY_SUB = "subject";
    public static final String KEY_DES = "description";
    public static final String    KEY_ID ="id";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_search);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);





        //TODO END

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        return false;
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Map";
                case 1:
                    return "Job Type";
            }
            return null;
        }
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
            final View rootView = inflater.inflate(R.layout.fragment_job_search, container, false);


            LinearLayout map = (LinearLayout)rootView.findViewById(R.id.Job_map);
            LinearLayout jobType = (LinearLayout)rootView.findViewById(R.id.Job_type);



            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1)
            {
                map.setVisibility(View.VISIBLE);
                jobType.setVisibility(View.GONE);
            }
            else
            {
                map.setVisibility(View.GONE);
                jobType.setVisibility(View.VISIBLE);

                //JOB TYPE data fetch

                //delete this BAAL SAAL

                //fetch data from the server

                AsyncHttpClient client = new AsyncHttpClient();
                client.get("http://jobcue.herokuapp.com/jobs/", new JsonHttpResponseHandler() {


                    @Override
                    public void onSuccess(JSONArray response) {
                        super.onSuccess(response);
                        //Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                        try {
                            //Log.d("JSONArray", response.toString());
                            subject = new String[response.length()];
                            description = new String[response.length()];
                            id=new String[response.length()];
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jo = response.getJSONObject(i);

                                subject[i] = jo.getString(KEY_SUB);
                                description[i] = jo.getString(KEY_DES);
                                id[i]=jo.getString(KEY_ID);
                            }

                            ListView listView = (ListView)rootView.findViewById(R.id.jobsearch_listView);
                            LstViewAdapter lv_adapter = new LstViewAdapter(getActivity(), R.layout.list_item, R.id.txt, subject);
                            listView.setAdapter(lv_adapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable e) {
                        super.onFailure(statusCode, headers, responseBody, e);
                        //Toast.makeText(getContext(), "Error " + statusCode + " " + responseBody, Toast.LENGTH_LONG).show();
                        Log.d("erro22r", "failed");
                    }
                });
            }

            return rootView;
        }
    }

    public void clickMe(View view){
        View parentRow = (View) view.getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);

        String []array = new String[4];

        array[0] = subject[position];
        array[1] = description[position];
        array[2] = id[position];
        array[3] = "False";

        Intent i = new Intent(this, JobDetails.class);
        i.putExtra("job", array);
        startActivity(i);
    }

}
