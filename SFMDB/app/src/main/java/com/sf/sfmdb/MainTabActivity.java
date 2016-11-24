package com.sf.sfmdb;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.design.widget.TabLayout;
import android.widget.Toast;

import com.sf.sfmdb.data.MovieData;
import com.sf.sfmdb.data.MovieDataManager;

import org.json.JSONObject;

import java.util.List;

import static android.R.id.list;

/**
 * Created by aeppili on 11/23/16.
 * Main Activity that gets launched
 *  - Uses TabLayout for Search + Favorites section
 *  - Uses SearchView to provide UI for input
 *  UI -
 *    TabLayout with RecyclerViews for Movies list
 *    Referefnce:http://stackoverflow.com/questions/34579614/how-to-implement-recyclerview-in-a-fragment-with-tablayout
 */

public class MainTabActivity extends AppCompatActivity {
    public static final String TAG = "MainTab";
    private MoviesFragment searchResultsFrag;
    private MoviesFragment favoritesFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_maintab);

        searchResultsFrag = MoviesFragment.newInstanceWithParams(false);
        favoritesFrag = MoviesFragment.newInstanceWithParams(true);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter =
                new PagerAdapter(getSupportFragmentManager(), MainTabActivity.this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == 1) {
                    if(favoritesFrag != null) {
                        favoritesFrag.updateData(MoviesFragment.getFavMovieList());
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
//                if(position == 1) {
//                    if(favoritesFrag != null) {
//                        favoritesFrag.updateData(MoviesFragment.getFavMovieList());
//                    }
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);


        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if(i==0) {
                tab.setText("Search Results");
            } else if(i==1) {
                tab.setText("Favorite Movies");
            }
           // tab.setCustomView(pagerAdapter.getTabView(i));
        }

    }


    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchView.setInputType(InputType.TYPE_NULL);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit | Search Query "+query);
                searchView.clearFocus();
                searchMovies(query);
                return true;

            }

            @Override
            public boolean onQueryTextChange(String query) {
                return true;
            }

        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    class PagerAdapter extends FragmentPagerAdapter {

        String tabTitles[] = new String[] { "Search Results", "Favorites" };
        Context context;

        public PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return searchResultsFrag;
                case 1:
                    return favoritesFrag;
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }

    /**
     * Helper method to search OMDB
     * @param searchCriteria  search movies based on searchCriteria
     */
    private void searchMovies(String searchCriteria) {
        Log.d(TAG, "Query DB for . "+searchCriteria);
        MovieDataManager.getMovieByTitle(searchCriteria, new MovieDataManager.MovieSearchListener() {
            @Override
            public void onResponseReceived(JSONObject jsonDataList) {
                if(jsonDataList == null) {
                    Snackbar.make(findViewById(android.R.id.content), "No movies found for the search criteria", Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }
                List<MovieData> movieData = MovieData.parseMovieListJson(jsonDataList);
                if(movieData == null || movieData.size() ==0) {
                    Snackbar.make(findViewById(android.R.id.content), "No movies found for the search criteria", Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }
                searchResultsFrag.updateData(movieData);
            }
        });
    }
}