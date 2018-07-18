package com.finitemonkey.dougb.nflcrimewatch.ui.fragments;

import android.app.Application;
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
import com.finitemonkey.dougb.nflcrimewatch.data.placeholders.Positions;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Recents;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.PositionCountsViewModel;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.PositionCountsViewModelFactory;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.PositionRecentsViewModel;
import com.finitemonkey.dougb.nflcrimewatch.network.RecentsAPI;
import com.finitemonkey.dougb.nflcrimewatch.ui.adapters.PositionRecentsAdapter;
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


public class PositionRecentsFragment extends Fragment implements PositionRecentsAdapter.PositionRecentsHolderClickListener {
    private static final String TAG = PositionRecentsFragment.class.getSimpleName();
    @BindView(R.id.rv_team_recents)
    RecyclerView mRecyclerView;
    private PositionRecentsAdapter mAdapter;
    private Context mContext;
    private Boolean mHasCheckedUpdate = false;
    private OnFragmentInteractionListener mListener;


    public PositionRecentsFragment() {
        // Required empty public constructor
    }

    public static PositionRecentsFragment newInstance() {
        PositionRecentsFragment fragment = new PositionRecentsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setupPositionCountsViewModel() {
        Application application = getActivity().getApplication();
        int sourceId = getResources().getInteger(R.integer.source_type_position);

        PositionCountsViewModel viewModel = ViewModelProviders.of(
                this, new PositionCountsViewModelFactory(application, sourceId)).get(
                PositionCountsViewModel.class);
        viewModel.getPositionCounts().observe(this, new Observer<List<Positions>>() {
            @Override
            public void onChanged(@Nullable List<Positions> positions) {
                // Set the adapter and see if we need a data refresh
                mAdapter.setPositionRecents(positions);
            }
        });
    }

    private void setupPositionRecentsViewModel() {
        PositionRecentsViewModel viewModel = ViewModelProviders.of(this).get(
                PositionRecentsViewModel.class);
        viewModel.getPositionRecents().observe(this, new Observer<List<Recents>>() {
            @Override
            public void onChanged(@Nullable List<Recents> recents) {
                if (!mHasCheckedUpdate) {
                    checkIfUpdatedToday(recents);
                }
            }
        });
    }

    private void checkIfUpdatedToday(List<Recents> recents) {
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
                    mContext, getResources().getInteger(R.integer.source_type_position),
                    positionIds,
                    strBegin, strToday, 100
            );
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
        int spanCount = getResources().getInteger(R.integer.num_team_recents_grid);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, spanCount));
        // Initialize the adapter and attach to the view
        mAdapter = new PositionRecentsAdapter(mContext, this);
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
        setupPositionCountsViewModel();
        setupPositionRecentsViewModel();

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
    public void onPositionRecentsHolderClick(String teamId) {
        mListener.onFragmentInteraction(R.string.source_team, teamId);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int sourceType, String teamId);
    }
}
