package com.pieter_jan.redditzor;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pieter_jan.redditzor.model.DaoMaster;
import com.pieter_jan.redditzor.model.DaoSession;
import com.pieter_jan.redditzor.model.Listing;
import com.pieter_jan.redditzor.model.ListingDao;
import com.pieter_jan.redditzor.model.Post;
import com.pieter_jan.redditzor.model.PostDao;

public class MainActivity extends AppCompatActivity implements SubRedditFragment.Coordinator
{
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private String[] mSubReddits;
    private CharSequence mSubRedditTitle;
    private CharSequence mNavTitle;

    private boolean showMenuItems = true;

    private SharedPreferences mSharedPref;
    private int loadLimit = 25;
    private int subreddit;

    private SQLiteDatabase db;
    private ListingDao listingDao;
    private PostDao postDao;

    private SubRedditFragment subredditFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDb();
        initPreferences();
        initDrawer(savedInstanceState);
        initFragment();
    }

    private void initDb()
    {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "reddit-db", null);
        db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        listingDao = daoSession.getListingDao();
        postDao = daoSession.getPostDao();
    }

    private void initPreferences()
    {
        mSharedPref = getPreferences(Context.MODE_PRIVATE);
        loadLimit = mSharedPref.getInt(SubRedditFragment.LOAD_LIMIT, 25);
        subreddit = mSharedPref.getInt(SubRedditFragment.SUBREDDIT_NUMBER, 0);
    }

    private void initDrawer(Bundle savedInstanceState)
    {
        mSubRedditTitle = mNavTitle = getTitle();
        mSubReddits = getResources().getStringArray(R.array.subreddits_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.nav_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.nav_list_item, mSubReddits));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                null,  /* Toolbar */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        )
        {
            public void onDrawerClosed(View view)
            {
                getSupportActionBar().setTitle(mSubRedditTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView)
            {
                getSupportActionBar().setTitle(mNavTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null)
        {
            selectItem(0);
        }
    }

    private void initFragment()
    {
        subredditFragment = new SubRedditFragment();
        Bundle args = new Bundle();
        args.putInt(SubRedditFragment.SUBREDDIT_NUMBER, subreddit);
        subredditFragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.content, subredditFragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(subreddit, true);
        setTitle(mSubReddits[subreddit]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen && showMenuItems);
        menu.findItem(R.id.action_restart).setVisible(!drawerOpen && showMenuItems);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId())
        {
            case R.id.action_settings:
                postsLoadedPerRequestSeekBar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void reload(MenuItem item)
    {
        Listing listing = subredditFragment.getListing();
        for (Post p : listing.getPosts())
            postDao.delete(p);
        listingDao.delete(listing);
        subredditFragment.reload();
    }

    private void postsLoadedPerRequestSeekBar()
    {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.posts_per_request, (ViewGroup) findViewById(R.id.root));
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(layout);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        final TextView value = (TextView) layout.findViewById(R.id.value);
        final SeekBar sb = (SeekBar) layout.findViewById(R.id.seekbar);
        sb.setMax(75);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                value.setText("Posts per request: " + (progress + 25));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        });
        Button ok = (Button) layout.findViewById(R.id.button_ok);
        ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loadLimit = sb.getProgress() + 25;
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putInt(SubRedditFragment.LOAD_LIMIT, loadLimit);
                editor.commit();
                alertDialog.cancel();
            }
        });
        Button cancel = (Button) layout.findViewById(R.id.button_cancel);
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alertDialog.cancel();
            }
        });
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id)
        {
            selectItem(position);
        }
    }

    @Override
    public int getLoadLimit()
    {
        return loadLimit;
    }

    @Override
    public void goToPost(Post post)
    {
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        showMenuItems = false;
        invalidateOptionsMenu();

        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(PostDetailFragment.POST, post);
        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void selectItem(int position)
    {
        if (subredditFragment != null) {
            subredditFragment.goToSubreddit(position);
            setTitle(mSubReddits[position]);
            mDrawerLayout.closeDrawers();
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        goBack();
        return true;
    }

    @Override
    public void onBackPressed()
    {
        goBack();
    }

    private void goBack()
    {
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        showMenuItems = true;
        invalidateOptionsMenu();
        if (getFragmentManager().getBackStackEntryCount() > 1)
        {
            getFragmentManager().popBackStack();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public void setTitle(CharSequence title)
    {
        mSubRedditTitle = title;
        getSupportActionBar().setTitle(mSubRedditTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void persist(Listing listing)
    {
        for (Post p : listing.getPosts())
        {
            p.setListingId(listing.getId());
            postDao.insertOrReplace(p);
        }
        listingDao.insertOrReplace(listing);
    }

    @Override
    public Listing getListing(String subreddit)
    {
        return listingDao.queryBuilder().where(ListingDao.Properties.Category.eq(subreddit)).unique();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(SubRedditFragment.SUBREDDIT_NUMBER, subreddit);
        editor.commit();
        if (db != null) db.close();
    }

}
