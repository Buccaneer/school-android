package com.pieter_jan.kappagrrm;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pieter_jan.kappagrrm.model.Character;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainAdapter.CharacterSelectedListener
{
    public static final String EVERYONEDIED = "EVERYONEDIED";
    private static final String listFragmentTag = "characterListFragment";
    private static final String detailFragmentTag = "characterDetailsFragment";
    private boolean everyoneDied;

    FragmentManager fragmentManager;
    GRRM grrm;
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grrm = GRRM.getInstance(this);
        mPrefs = getSharedPreferences("grrm", Context.MODE_PRIVATE);
        everyoneDied = mPrefs.getBoolean(EVERYONEDIED, false);
        fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(listFragmentTag) == null)
            startFragment();
        if(fragmentManager.findFragmentByTag(detailFragmentTag) != null)
            enableUpNavigation(true);
    }

    private void startFragment()
    {
        if (everyoneDied)
        {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container, new GrrmFragment(), "grrmFragment");
            transaction.commit();
        }
        else
        {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.container, new CharacterListFragment(), listFragmentTag);
            transaction.commit();
        }
    }

    @Override
    public void characterSelected(Character character)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Bundle args = new Bundle();
        args.putParcelable(CharacterDetailsFragment.PARCEL, character);
        CharacterDetailsFragment fragment = new CharacterDetailsFragment();
        fragment.setArguments(args);
        transaction.replace(R.id.container, fragment, detailFragmentTag);
        transaction.addToBackStack(null);
        transaction.commit();
        enableUpNavigation(true);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        fragmentManager.popBackStack();
        enableUpNavigation(false);
        return true;
    }

    private void enableUpNavigation(boolean enable)
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(enable);
    }

    public List<Character> getCharacters()
    {
        List<Character> characters = grrm.getRemaining();
        if (characters.size() > 0)
            return characters;

        characters = new ArrayList<>();
        for (int i = 0; i < Characters.IDS.length; i++)
        {
            characters.add(new Character(getResources().getIdentifier("img" + i, "drawable", getPackageName()), Characters.NAMES[i], Characters.DESCRIPTIONS[i]));
        }
        grrm.init(characters);
        return characters;
    }

    public void killCharacter(Character c)
    {
        grrm.killCharacter(c);
    }

    public void everyoneDied()
    {
        everyoneDied = true;
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putBoolean(EVERYONEDIED, true);
        ed.apply();

        fragmentManager.popBackStack();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, new GrrmFragment(), "grrmFragment");
        transaction.commit();
    }

}
