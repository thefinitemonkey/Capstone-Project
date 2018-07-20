package com.finitemonkey.dougb.nflcrimewatch.ui.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.finitemonkey.dougb.nflcrimewatch.R;
import com.finitemonkey.dougb.nflcrimewatch.ui.fragments.SourcedOffensesFragment;
import com.finitemonkey.dougb.nflcrimewatch.ui.fragments.TeamRecentsFragment;

import java.util.List;

public class SourcedOffensesActivity extends AppCompatActivity implements SourcedOffensesFragment.OnFragmentInteractionListener {
    private String mSourceId;
    private int mSourceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the source passed in on the savedInstanceState
        mSourceId = getIntent().getStringExtra(getResources().getString(R.string.source_id));
        mSourceType = getIntent().getIntExtra(
                getResources().getString(R.string.sourced_instance_type), 0);

        setContentView(R.layout.activity_sourced_offenses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSourcedOffenseDisplay();
    }

    private void setSourcedOffenseDisplay() {
        clearFragments();

        // Create the offense display fragment
        FragmentManager fm = getSupportFragmentManager();
        SourcedOffensesFragment sof = new SourcedOffensesFragment();
        // Set parameters for the source ID (value of team / position / crime) and
        // source type (team / position / crime) on the fragment
        Bundle bundle = new Bundle();
        bundle.putString(getResources().getString(R.string.source_id), mSourceId);
        bundle.putInt(getResources().getString(R.string.sourced_instance_type), mSourceType);
        sof.setArguments(bundle);
        fm.beginTransaction().add(R.id.cl_sourced_offenses, sof).commit();
    }

    private void clearFragments() {
        // Remove all fragments from the display
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> frags = fm.getFragments();
        if (frags == null) return;

        View cl = findViewById(R.id.cl_sourced_offenses);
        for (Fragment frag : frags) {
            if (frag.getView() == null) return;

            View view = (View) frag.getView().getParent();
            if (view.equals(cl)) {
                fm.beginTransaction().remove(frag).commit();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
