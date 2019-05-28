package com.example.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_CONTACT = 2;
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    private CheckBox mPoliceRequiredCheckBox;
    private Button mReportButton;
    private Button mSuspectButton;

    public static Fragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, id);
        Fragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        assert getArguments() != null;
        UUID id = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.getInstance(getActivity()).getCrime(id);
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getInstance(getActivity()).updateCrime(mCrime);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateButton = view.findViewById(R.id.crime_date);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                assert manager != null;
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mTimeButton = view.findViewById(R.id.crime_time);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                assert manager != null;
                dialog.show(manager, DIALOG_TIME);
            }
        });
        updateDate();

        mSolvedCheckBox = view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mCrime.setSolved(isChecked);
                mPoliceRequiredCheckBox.setEnabled(!isChecked);
                if (isChecked) {
                    mCrime.setPoliceRequired(false);
                    mPoliceRequiredCheckBox.setChecked(false);
                }
            }
        });

        mPoliceRequiredCheckBox = view.findViewById(R.id.crime_police_required);
        mPoliceRequiredCheckBox.setEnabled(!mCrime.isSolved());
        mPoliceRequiredCheckBox.setChecked(mCrime.isPoliceRequired());
        mPoliceRequiredCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mCrime.setPoliceRequired(isChecked);
            }
        });

        mReportButton = view.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
//                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
//                intent = Intent.createChooser(intent, getString(R.string.send_report));
//                startActivity(intent);
                ShareCompat.IntentBuilder.from(Objects.requireNonNull(getActivity()))
                        .setSubject(getString(R.string.crime_report_subject))
                        .setText(getCrimeReport())
                        .setChooserTitle(getString(R.string.send_report))
                        .setType("text/plain")
                        .startChooser();
            }
        });

        mSuspectButton = view.findViewById(R.id.crime_suspect);
        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
//        pickContact.addCategory(Intent.CATEGORY_HOME);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });
        if (null != mCrime.getSuspect()) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        PackageManager manager = Objects.requireNonNull(getActivity()).getPackageManager();
        if (null == manager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY)) {
            mSuspectButton.setEnabled(false);
        } else {
            mSuspectButton.setEnabled(true);
        }

        return view;
    }

    private String getCrimeReport() {
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String solvedString = getString(mCrime.isSolved() ?
                R.string.crime_report_solved : R.string.crime_report_unsolved);
        String suspect = mCrime.getSuspect();
        suspect = (null == suspect) ?
                getString(R.string.crime_report_no_suspect) :
                getString(R.string.crime_report_suspect, suspect);
        return getString(R.string.crime_report,
                mCrime.getTitle(), dateString, solvedString, suspect);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_crime) {
            CrimeLab.getInstance(getActivity()).deleteCrime(mCrime);
            Objects.requireNonNull(getActivity()).finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK != resultCode) {
            return;
        }
        if (REQUEST_DATE == requestCode) {
            Date date = DatePickerFragment.getDate(data);
            mCrime.setDate(date);
            updateDate();
        } else if (REQUEST_TIME == requestCode) {
            Date time = TimePickerFragment.getTime(data);
            mCrime.setDate(time);
            updateDate();
        } else if (REQUEST_CONTACT == requestCode) {
            Uri contactUri = data.getData();
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            assert contactUri != null;
            try (Cursor cursor = Objects.requireNonNull(getActivity()).getContentResolver().query(
                    contactUri, queryFields, null, null, null)) {
                assert cursor != null;
                if (0 != cursor.getCount()) {
                    cursor.moveToFirst();
                    String suspect = cursor.getString(0);
                    mCrime.setSuspect(suspect);
                    mSuspectButton.setText(suspect);
                }
            }
        }
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getFormattedDate());
        mTimeButton.setText(mCrime.getFormattedTime());
    }
}
