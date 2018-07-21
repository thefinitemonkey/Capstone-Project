package com.finitemonkey.dougb.nflcrimewatch.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.finitemonkey.dougb.nflcrimewatch.R;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Arrests;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Recents;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Stadiums;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.ClosestTeamViewModel;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.PositionRecentsViewModel;
import com.finitemonkey.dougb.nflcrimewatch.network.RecentsAPI;
import com.finitemonkey.dougb.nflcrimewatch.ui.fragments.CrimeRecentsFragment;
import com.finitemonkey.dougb.nflcrimewatch.ui.fragments.PositionRecentsFragment;
import com.finitemonkey.dougb.nflcrimewatch.ui.fragments.TeamRecentsFragment;
import com.finitemonkey.dougb.nflcrimewatch.utils.RecentsUtils;
import com.finitemonkey.dougb.nflcrimewatch.utils.StadiumUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecentsAPI.RecentByTeamsListener,
        TeamRecentsFragment.OnFragmentInteractionListener,
        PositionRecentsFragment.OnFragmentInteractionListener,
        CrimeRecentsFragment.OnFragmentInteractionListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static Boolean mHasCheckedUpdate = false;
    private static Context mContext;
    @BindView(R.id.drawer_main)
    DrawerLayout mDrawer;
    @BindView(R.id.nav_view_main)
    NavigationView mNavView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private Menu mNavMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        mNavView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // Check the item, close the drawer, and change the view
                        handleNavClick(item);
                        mDrawer.closeDrawers();

                        return true;
                    }
                });
        mNavMenu = mNavView.getMenu();


        setupClosestTeamViewModel();
        setupPositionRecentsViewModel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                mDrawer.openDrawer(GravityCompat.START);
            }
            case R.id.action_settings: {
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleNavClick(MenuItem item) {
        if (item == null) return;

        item.setChecked(true);
        int selectedId = item.getItemId();

        if (selectedId == R.id.nav_menu_item_team) {
            setTeamRecentsDisplay();
        } else if (selectedId == R.id.nav_menu_item_position) {
            setPositionRecentsDisplay();
        } else if (selectedId == R.id.nav_menu_item_crime) {
            setCrimeRecentsDisplay();
        }
    }

    private void setupClosestTeamViewModel() {
        ClosestTeamViewModel viewModel = ViewModelProviders.of(this).get(
                ClosestTeamViewModel.class);
        viewModel.getStadiums().observe(this, new Observer<List<Stadiums>>() {
            @Override
            public void onChanged(@Nullable List<Stadiums> stadiums) {
                // Check if a preferred team has already been set / selected
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                        mContext);
                String prefTeam = sharedPreferences.getString("list_preference_team", null);
                if (prefTeam!= null) {
                    Log.d(TAG, "onChanged: preferred team is " + prefTeam);
                    return;
                }

                // No preferred team, so set one up by getting the phone location

                // Test the calculation to closest stadium
                // lat 29.6503993 lon -95.7350763
                String teamId = StadiumUtils.getClosestTeam(stadiums, 40.1677863, -83.0089769);
                Log.d(TAG, "onChanged: closest team is " + teamId);
            }
        });
    }

    @Override
    public void onRecentByTeamsLoadComplete(List<Arrests> arrests) {
        for (Arrests tr : arrests
                ) {
            Log.d(TAG, "onRecentByTeamsLoadComplete: teamId - " + tr.getTeam());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Add the TeamRecents fragment to the display by default
        Log.d(TAG, "onResume: setting up teamRecents display");
        MenuItem mi = (MenuItem) mNavMenu.findItem(R.id.nav_menu_item_team);
        handleNavClick(mi);
    }

    private void setTeamRecentsDisplay() {
        clearFragments();

        FragmentManager fm = getSupportFragmentManager();
        TeamRecentsFragment trf = new TeamRecentsFragment();
        fm.beginTransaction().add(R.id.cl_main_display, trf).commit();
    }

    private void setPositionRecentsDisplay() {
        clearFragments();

        FragmentManager fm = getSupportFragmentManager();
        PositionRecentsFragment prf = new PositionRecentsFragment();
        fm.beginTransaction().add(R.id.cl_main_display, prf).commit();
    }

    private void setCrimeRecentsDisplay() {
        clearFragments();

        FragmentManager fm = getSupportFragmentManager();
        CrimeRecentsFragment crf = new CrimeRecentsFragment();
        fm.beginTransaction().add(R.id.cl_main_display, crf).commit();
    }

    private void clearFragments() {
        // Remove all fragments from the display
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> frags = fm.getFragments();
        if (frags == null) return;

        View cl = findViewById(R.id.cl_main_display);
        for (Fragment frag : frags) {
            if (frag.getView() == null) return;

            View view = (View) frag.getView().getParent();
            if (view.equals(cl)) {
                fm.beginTransaction().remove(frag).commit();
            }
        }
    }

    private void setupPositionRecentsViewModel() {
        PositionRecentsViewModel viewModel = ViewModelProviders.of(this).get(
                PositionRecentsViewModel.class);
        viewModel.getPositionRecents().observe(this, new Observer<List<Arrests>>() {
            @Override
            public void onChanged(@Nullable List<Arrests> recents) {
                if (!mHasCheckedUpdate) {
                    checkIfUpdatedToday(recents);
                }
            }
        });
    }

    private void checkIfUpdatedToday(List<Arrests> recents) {
        // Check if the update has already been done today
        Boolean hasBeenUpdated = RecentsUtils.startCheckUpdatedInPastDay(recents);
        Log.d(TAG, "onTeamRecentsCheckResult: data has been updated today is " + hasBeenUpdated);

        // If not updated yet then kick off the update
        if (!hasBeenUpdated) {
            // Need to make the daily check for updates to TeamRecents (recents offenses by team)
            String[] positionIds = getResources().getStringArray(R.array.position_id_array);
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strToday = dateFormat.format(today);
            String strBegin = "2000-01-01";
            RecentsAPI recentsRetrieval = new RecentsAPI();
            recentsRetrieval.getRecents(
                    this, getResources().getInteger(R.integer.source_type_position),
                    positionIds,
                    strBegin, strToday, 100
            );
        }

        mHasCheckedUpdate = true;
    }

    @Override
    public void onFragmentInteraction(int sourceType, String sourceId) {
        // Create the intent to navigate to the SourcedOffensesActivity for the selected entity
        Class destinationClass = SourcedOffensesActivity.class;
        Intent showOffenses = new Intent(this, destinationClass);
        showOffenses.putExtra(getResources().getString(R.string.sourced_instance_type), sourceType);
        showOffenses.putExtra(getResources().getString(R.string.source_id), sourceId);
        startActivity(showOffenses);
    }
}
