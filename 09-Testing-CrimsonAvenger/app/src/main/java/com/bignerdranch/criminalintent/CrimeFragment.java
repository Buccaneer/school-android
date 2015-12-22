package com.bignerdranch.criminalintent;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.criminalintent.model.Crime;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

import java.io.File;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_IMAGE = "image";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_PHOTO= 2;


    private Crime mCrime;
    @Bind(R.id.crime_title) EditText mTitleField;
    @Bind(R.id.crime_date) Button mDateButton;
    @Bind(R.id.crime_solved) CheckBox mSolvedCheckbox;
    @Bind(R.id.crime_suspect) Button mSuspectButton;
    @Bind(R.id.crime_photo) ImageView mPhotoView;
    private Callbacks mCallbacks;

    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onCrimeUpdated(Crime crime);
    }

    public static CrimeFragment newInstance(Long crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Long crimeId = (Long) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        ButterKnife.bind(this, v);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (getActivity() == null)
                {
                    return;
                }
                mCrime.setTitle(s.toString());
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mSolvedCheckbox.setChecked(mCrime.getSolved());
        mSolvedCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                mCrime.setSolved(isChecked);
                updateCrime();
            }
        });

        updateSuspect();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateCrime();
            updateDate();
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            // Specify which fields you want your query to return
            // values for.
            String[] queryFields = new String[] {
                     ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME
            };
            // Perform your query - the contactUri is like a "where"
            // clause here
            ContentResolver resolver = getActivity().getContentResolver();
            Cursor c = resolver.query(contactUri, queryFields, null, null, null);
            try {
                // Double-check that you actually got results
                if (c.getCount() == 0) {
                    return;
                }
                c.moveToFirst();
                mCrime.setSuspect(c.getString(1));
                String contactId = c.getString(0);
                Cursor mail = resolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + contactId,
                        null, null);
                mail.moveToFirst();
                if (mail.getCount() > 0) {
                    String m = mail.getString(mail.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                    mCrime.setSuspectMail(m);
                }
                mail.close();
                updateCrime();
                mSuspectButton.setText(mCrime.getSuspect());
            } finally {
                c.close();
            }
        } else if (requestCode == REQUEST_PHOTO) {
            //mCrime.setPhoto(data.getStringExtra(MediaStore.EXTRA_OUTPUT));
            updateCrime();
            updatePhotoView();
        }
    }

    @OnClick(R.id.crime_camera)
    public void takePicture()
    {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .getAbsolutePath() + "/" + mCrime.getPhotoFilename();
        File file = new File(path);
        Uri outputFileUri = Uri.fromFile(file);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        mCrime.setPhoto(path);

        startActivityForResult(intent, REQUEST_PHOTO);
    }

    @OnClick(R.id.crime_suspect)
    public void pickSuspect()
    {
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, REQUEST_CONTACT);
    }

    @OnClick(R.id.crime_photo)
    public void showPicture()
    {
        String p = mCrime.getPhoto();
        if (p == null)
            return;

        FragmentManager fm = getActivity()
                .getSupportFragmentManager();

        ImageFragment.createInstance(p)
                .show(fm, DIALOG_IMAGE);
    }

    @OnClick(R.id.crime_report)
    public void reportCrime()
    {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
        if (mCrime.getSuspectMail() != null)
            i.putExtra(Intent.EXTRA_EMAIL, new String[] { mCrime.getSuspectMail() });
        File picture = new File(mCrime.getPhoto());
        if (picture.exists() && picture.canRead())
            i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(picture));
        i = Intent.createChooser(i, getString(R.string.send_report));
        startActivity(i);
    }

    private void updateCrime() {
        CrimeLab.get(getActivity()).updateCrime(mCrime);
        mCallbacks.onCrimeUpdated(mCrime);
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }

    private void updateSuspect()
    {
        if (mCrime.getSuspect() != null)
            mSuspectButton.setText(mCrime.getSuspect());
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.getSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report,
                mCrime.getTitle(), dateString, solvedString, suspect);
        return report;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        updatePhotoView();
    }

    private void updatePhotoView() {
        Glide.with(mPhotoView.getContext())
                .load(mCrime.getPhoto())
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .into(mPhotoView);
    }
}
