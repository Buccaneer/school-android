package com.pieter_jan.kappagrrm;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements MainAdapter.CharacterSelectedListener
{
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        startFragment();
    }

    private void startFragment()
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container, new CharacterListFragment(), "characterListFragment");
        transaction.commit();
    }

    @Override
    public void characterSelected(Character character)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Bundle args = new Bundle();
        args.putParcelable(CharacterDetailsFragment.PARCEL, character);
        CharacterDetailsFragment fragment = new CharacterDetailsFragment();
        fragment.setArguments(args);
        transaction.replace(R.id.container, fragment, "characterDetailsFragment");
        transaction.addToBackStack(null);
        transaction.commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        fragmentManager.popBackStack();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(false);
        return true;
    }


}
