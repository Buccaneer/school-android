package com.example.paradoxexercise;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements ParadoxListFragment.OnParadoxSelectedListener
{

    FragmentManager fragmentManager;
    ParadoxListFragment paradoxListFragment;
    ParadoxDescriptionFragment paradoxDescriptionFragment;

    private boolean large = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.paradoxes);
        large = findViewById(R.id.textLayout) != null;
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState)
    {
        if (savedInstanceState == null)
        {
            paradoxListFragment = new ParadoxListFragment();
            paradoxListFragment.setArguments(getIntent().getExtras());
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, paradoxListFragment, "paradoxListFragment");
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
    public void onParadoxSelected(int paradox)
    {
        paradoxDescriptionFragment = new ParadoxDescriptionFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.PARADOX, paradox);
        paradoxDescriptionFragment.setArguments(args);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (large)
        {
            transaction.replace(R.id.textLayout, paradoxDescriptionFragment, "paradoxDescriptionFragment");
        }
        else
        {
            transaction.replace(R.id.container, paradoxDescriptionFragment, "paradoxDescriptionFragment");
            transaction.addToBackStack(null);
        }
        transaction.commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && !large)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        //This method is called when the up button is pressed. Just the pop back stack.
        fragmentManager.popBackStack();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(false);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
