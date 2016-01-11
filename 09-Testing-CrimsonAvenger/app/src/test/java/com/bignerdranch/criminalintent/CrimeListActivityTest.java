package com.bignerdranch.criminalintent;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.bignerdranch.android.criminalintent.BuildConfig;
import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.criminalintent.model.Crime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.util.ActivityController;

import java.lang.reflect.Field;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Pieter-Jan on 6/01/2016.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class CrimeListActivityTest
{
    private ActivityController<CrimeListActivity> controller;
    private CrimeListActivity activity;

    @Before
    public void setup()
    {
        controller = Robolectric.buildActivity(CrimeListActivity.class);
        activity = controller.attach().create().visible().start().resume().get();
    }

    @After
    public void finishComponentTesting() {
        // sCrimeLab is the static variable name which holds the singleton instance
        resetSingleton(CrimeLab.class, "sCrimeLab");
    }

    private void resetSingleton(Class clazz, String fieldName) {
        Field instance;
        try {
            instance = clazz.getDeclaredField(fieldName);
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Test /**MENU ITEM*/
    public void validateSubtitleMenuItem()
    {
        // No subtitle default
        assertNull(activity.getSupportActionBar().getSubtitle());

        // Subtitle after click on SHOW SUBTITLE
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        shadowActivity.clickMenuItem(R.id.menu_item_show_subtitle);
        assertTrue(activity.getSupportActionBar().getSubtitle().toString().matches(".*crimes"));

        // No subtitle after click on HIDE SUBTITLE
        shadowActivity.clickMenuItem(R.id.menu_item_show_subtitle);
        assertNull(activity.getSupportActionBar().getSubtitle());
    }

    @Test /**MENU ITEM*/
    public void validateAddEntryMenuItem()
    {
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        shadowActivity.clickMenuItem(R.id.menu_item_new_crime);

        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);

        assertEquals(shadowIntent.getComponent().getClassName(), CrimePagerActivity.class.getName());
    }

    @Test /**INTENT*/
    public void validateSelectCrimeFromList()
    {
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);

        Crime crime = new Crime("TestCrime");
        crime.setId(123L);

        CrimeListFragment listFragment = (CrimeListFragment) activity.getSupportFragmentManager().findFragmentByTag(SingleFragmentActivity.FRAGMENT_TAG);
        listFragment.mCallbacks.onCrimeSelected(crime);

        Intent startedIntent = shadowActivity.getNextStartedActivity();
        assertEquals((long) crime.getId(), startedIntent.getLongExtra("com.bignerdranch.android.criminalintent.crime_id", -1));
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);

        assertEquals(shadowIntent.getComponent().getClassName(), CrimePagerActivity.class.getName());
    }

    @Test /**ROTATION*/
    public void recreatesActivity() {
        Bundle bundle = new Bundle();

        // No subtitle default
        assertNull(activity.getSupportActionBar().getSubtitle());

        // Subtitle after click on SHOW SUBTITLE
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        shadowActivity.clickMenuItem(R.id.menu_item_show_subtitle);

        // Destroy the original activity
        controller
                .saveInstanceState(bundle)
                .pause()
                .stop()
                .destroy();

        // Bring up a new activity
        controller = Robolectric.buildActivity(CrimeListActivity.class)
                .create(bundle)
                .start()
                .restoreInstanceState(bundle)
                .resume()
                .visible();
        activity = controller.get();

        // Check is subtitle is still visible after rotation
        assertTrue(activity.getSupportActionBar().getSubtitle().toString().matches(".*crimes"));
    }

}