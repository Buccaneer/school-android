package com.bignerdranch.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.criminalintent.model.Crime;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnPageChange;

public class CrimePagerActivity extends AppCompatActivity
        implements CrimeFragment.Callbacks {
    private static final String EXTRA_CRIME_ID =
            "com.bignerdranch.android.criminalintent.crime_id";

    @Bind(R.id.activity_crime_pager_view_pager) ViewPager mViewPager;
    private List<Crime> mCrimes;

    public static Intent newIntent(Context packageContext, Long crimeId) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        Long crimeId = (Long) getIntent()
                .getSerializableExtra(EXTRA_CRIME_ID);

        ButterKnife.bind(this);

        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                if (i == 0)
                {
                    setTitleFromPos(i);
                }
                break;
            }
        }
    }

    @OnPageChange(R.id.activity_crime_pager_view_pager)
    void onPageSelected(int position) {
        setTitleFromPos(position);
    }

    private void setTitleFromPos(int position)
    {
        Crime crime = mCrimes.get(position);
        if (crime.getTitle() != null) {
            setTitle(crime.getTitle());
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {

    }
}
