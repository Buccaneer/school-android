package com.pieter_jan.diary;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements EntryListFragment.OnEntrySelectedListener, SubmitEntryFragment.OnEntrySubmittedListener
{
    //setBackground(getResources().getDrawable(R.drawable.border));

    FragmentManager fragmentManager;
    EntryListFragment entryListFragment;

    Menu menu;
    MenuItem addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_main);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState)
    {
        if (savedInstanceState == null)
        {
            entryListFragment = new EntryListFragment();
            entryListFragment.setArguments(getIntent().getExtras());
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, entryListFragment, "entryListFragment");
            fragmentTransaction.commit();
        }
        if (fragmentManager.getBackStackEntryCount() > 0)
        {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null)
                actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        fragmentManager.popBackStack();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && fragmentManager.getBackStackEntryCount() == 1)
            actionBar.setDisplayHomeAsUpEnabled(false);
        enableButton(true);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEntrySelected(int entry)
    {
        DisplayEntryFragment displayEntryFragment = new DisplayEntryFragment();
        Bundle args = new Bundle();
        args.putInt(DisplayEntryFragment.ENTRY, entry);
        displayEntryFragment.setArguments(args);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, displayEntryFragment, "displayEntryFragment");
        transaction.addToBackStack(null);
        transaction.commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        enableButton(false);
    }

    public void submitEntry(MenuItem item)
    {
        SubmitEntryFragment submitEntryFragment = new SubmitEntryFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container, submitEntryFragment, "submitEntryFragment");
        transaction.addToBackStack(null);
        transaction.commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        enableButton(false);
    }

    @Override
    public void onEntrySubmitted()
    {
        fragmentManager.popBackStack();
    }

    private void enableButton(boolean visible)
    {
        if (addButton == null) addButton = menu.findItem(R.id.action_add);;
        addButton.setVisible(visible);
    }
}
