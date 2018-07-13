package com.finitemonkey.dougb.nflcrimewatch.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finitemonkey.dougb.nflcrimewatch.R;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.TeamRecents;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.TeamRecentsViewModel;
import com.finitemonkey.dougb.nflcrimewatch.network.RecentByTeamsAPI;
import com.finitemonkey.dougb.nflcrimewatch.ui.adapters.TeamRecentsAdapter;
import com.finitemonkey.dougb.nflcrimewatch.utils.TeamRecentsUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;


public class TeamRecentsFragment extends Fragment {
    private static final String TAG = TeamRecentsFragment.class.getSimpleName();
    @BindView(R.id.rv_team_recents)
    RecyclerView mRecyclerView;
    private TeamRecentsAdapter mAdapter;
    private Context mContext;
    private Boolean mHasCheckedUpdate = false;

    private OnFragmentInteractionListener mListener;

    public TeamRecentsFragment() {
        // Required empty public constructor
    }

    public static TeamRecentsFragment newInstance() {
        TeamRecentsFragment fragment = new TeamRecentsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setupTeamRecentsViewModel() {
        TeamRecentsViewModel viewModel = ViewModelProviders.of(this).get(
                TeamRecentsViewModel.class);
        viewModel.getTeamRecents().observe(this, new Observer<List<TeamRecents>>() {
            @Override
            public void onChanged(@Nullable List<TeamRecents> teamRecents) {
                // Set the adapter and see if we need a data refresh
                mAdapter.setTeamRecents(teamRecents);
                if (!mHasCheckedUpdate) {checkIfUpdatedToday(teamRecents);}
            }
        });
    }

    private void checkIfUpdatedToday(List<TeamRecents> teamRecents) {
        // Check if the update has already been done today
        Boolean hasBeenUpdated = TeamRecentsUtils.startCheckUpdatedInPastDay(teamRecents);
        Log.d(TAG, "onTeamRecentsCheckResult: data has been updated today is " + hasBeenUpdated);

        // If not updated yet then kick off the update
        if (!hasBeenUpdated) {
            // Need to make the daily check for updates to TeamRecents (recents offenses by team)
            String[] teamsIds = getResources().getStringArray(R.array.team_ids_array);
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strToday = dateFormat.format(today);
            String strBegin = "2000-01-01";
            RecentByTeamsAPI recentsRetrieval = new RecentByTeamsAPI();
            recentsRetrieval.getRecentByTeams(mContext, teamsIds, strBegin, strToday);
        }

        mHasCheckedUpdate = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Creating fragment view for TeamRecents");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team_recents, container, false);
        mContext = view.getContext();
        ButterKnife.bind(this, view);

        // Set the recycler layout to a grid with a width of 1 by default
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
        // Initialize the adapter and attach to the view
        mAdapter = new TeamRecentsAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);

        // Put in a separator between lines
        DividerItemDecoration decoration = new DividerItemDecoration(
                mContext.getApplicationContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        // Set up the viewModel
        setupTeamRecentsViewModel();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                                               + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}