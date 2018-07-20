package com.finitemonkey.dougb.nflcrimewatch.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Arrests;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Recents;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.TeamRecentsViewModel;
import com.finitemonkey.dougb.nflcrimewatch.network.RecentsAPI;
import com.finitemonkey.dougb.nflcrimewatch.ui.adapters.TeamRecentsAdapter;
import com.finitemonkey.dougb.nflcrimewatch.utils.RecentsUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.DividerItemDecoration.HORIZONTAL;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;


public class TeamRecentsFragment extends Fragment implements TeamRecentsAdapter.TeamRecentsHolderClickListener {
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
        viewModel.getTeamRecents().observe(this, new Observer<List<Arrests>>() {
            @Override
            public void onChanged(@Nullable List<Arrests> recents) {
                // Set the adapter and see if we need a data refresh
                mAdapter.setTeamRecents(recents);
            }
        });
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
        int spanCount = getResources().getInteger(R.integer.num_team_recents_grid);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, spanCount));
        // Initialize the adapter and attach to the view
        mAdapter = new TeamRecentsAdapter(mContext, this);
        mRecyclerView.setAdapter(mAdapter);

        // Put in a separator between lines
        Boolean showVerticalDividers = getResources().getBoolean(R.bool.show_vertical_dividers);
        DividerItemDecoration decoration = new DividerItemDecoration(
                mContext.getApplicationContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);
        if (showVerticalDividers) {
            DividerItemDecoration landDecoration = new DividerItemDecoration(
                    mContext.getApplicationContext(), HORIZONTAL);
            mRecyclerView.addItemDecoration(landDecoration);
        }

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

    @Override
    public void onTeamRecentsHolderClick(String teamId) {
        mListener.onFragmentInteraction(R.string.source_team, teamId);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int sourceType, String teamId);
    }
}
