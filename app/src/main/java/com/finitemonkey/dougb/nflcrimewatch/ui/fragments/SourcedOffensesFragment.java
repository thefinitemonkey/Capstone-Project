package com.finitemonkey.dougb.nflcrimewatch.ui.fragments;

import android.app.Application;
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
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Arrests;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.CrimesArrestsViewModel;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.CrimesArrestsViewModelFactory;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.PlayerArrestsViewModel;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.PlayerArrestsViewModelFactory;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.PositionArrestsViewModel;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.PositionArrestsViewModelFactory;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.TeamArrestsViewModel;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.TeamArrestsViewModelFactory;
import com.finitemonkey.dougb.nflcrimewatch.network.ArrestsAPI;
import com.finitemonkey.dougb.nflcrimewatch.ui.adapters.SourcedOffensesAdapter;
import com.finitemonkey.dougb.nflcrimewatch.utils.ArrestsUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.DividerItemDecoration.HORIZONTAL;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;


public class SourcedOffensesFragment extends Fragment implements SourcedOffensesAdapter.SourceOffensesHolderClickListener {
    private static final String TAG = SourcedOffensesFragment.class.getSimpleName();
    @BindView(R.id.rv_sourced_offenses)
    RecyclerView mRecyclerView;
    private SourcedOffensesAdapter mAdapter;
    private Context mContext;
    private Boolean mHasCheckedUpdate = false;
    private String mParamId = "";

    private OnFragmentInteractionListener mListener;

    public SourcedOffensesFragment() {
        // Required empty public constructor
    }

    public static SourcedOffensesFragment newInstance() {
        SourcedOffensesFragment fragment = new SourcedOffensesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setupTeamArrestsViewModel() {
        Application application = this.getActivity().getApplication();
        TeamArrestsViewModel viewModel =
                ViewModelProviders.of(this, new TeamArrestsViewModelFactory(
                        application, mParamId)
                ).get(TeamArrestsViewModel.class);
        viewModel.getTeamArrests().observe(this, new Observer<List<Arrests>>() {
            @Override
            public void onChanged(@Nullable List<Arrests> arrests) {
                // Set the adapter and see if we need a data refresh
                mAdapter.setArrests(arrests);
            }
        });
    }

    private void setupPositionArrestsViewModel() {
        Application application = this.getActivity().getApplication();
        PositionArrestsViewModel viewModel = ViewModelProviders.of(
                this, new PositionArrestsViewModelFactory(
                        application, mParamId)).get(PositionArrestsViewModel.class);
        viewModel.getPositionArrests().observe(this, new Observer<List<Arrests>>() {
            @Override
            public void onChanged(@Nullable List<Arrests> arrests) {
                mAdapter.setArrests(arrests);
            }
        });
    }

    private void setupCrimeArrestsViewModel() {
        Application application = this.getActivity().getApplication();
        CrimesArrestsViewModel viewModel = ViewModelProviders.of(
                this, new CrimesArrestsViewModelFactory(application, mParamId)
        ).get(CrimesArrestsViewModel.class);
        viewModel.getCrimeArrests().observe(this, new Observer<List<Arrests>>() {
            @Override
            public void onChanged(@Nullable List<Arrests> arrests) {
                mAdapter.setArrests(arrests);
            }
        });
    }

    private void setupPlayerArrestsViewModel() {
        Application application = this.getActivity().getApplication();
        PlayerArrestsViewModel viewModel = ViewModelProviders.of(
                this, new PlayerArrestsViewModelFactory(application, mParamId)
        ).get(PlayerArrestsViewModel.class);
        viewModel.getPlayerArrests().observe(this, new Observer<List<Arrests>>() {
            @Override
            public void onChanged(@Nullable List<Arrests> arrests) {
                mAdapter.setArrests(arrests);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Creating fragment view for SourcedOffenses");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sourced_offenses, container, false);
        mContext = view.getContext();
        ButterKnife.bind(this, view);

        // Set the recycler layout to a grid with a width of 1 by default
        int spanCount = getResources().getInteger(R.integer.num_team_recents_grid);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, spanCount));
        // Initialize the adapter and attach to the view
        Boolean isClickable = getArguments().getBoolean(
                getResources().getString(R.string.offenses_clickable));
        mAdapter = new SourcedOffensesAdapter(mContext, isClickable, this);
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

        // Get the source ID for future use
        mParamId = getArguments().getString(getResources().getString(R.string.source_id));

        // Set up the appropriate viewModel
        int sourceType = getArguments().getInt(
                getResources().getString(R.string.sourced_instance_type));
        switch (sourceType) {
            case (R.string.source_team): {
                setupTeamArrestsViewModel();
                break;
            }
            case (R.string.source_position): {
                setupPositionArrestsViewModel();
                break;
            }
            case (R.string.source_crime): {
                setupCrimeArrestsViewModel();
                break;
            }
            case (R.string.source_player): {
                setupPlayerArrestsViewModel();
                break;
            }
        }

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
    public void onSourceOffensesHolderClick(String playerName) {
        mListener.onFragmentInteraction(playerName);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String sourceId);
    }
}
