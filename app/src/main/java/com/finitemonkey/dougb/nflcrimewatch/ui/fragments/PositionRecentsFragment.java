package com.finitemonkey.dougb.nflcrimewatch.ui.fragments;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
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
import com.finitemonkey.dougb.nflcrimewatch.data.proxies.Positions;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.PositionCountsViewModel;
import com.finitemonkey.dougb.nflcrimewatch.ui.adapters.PositionRecentsAdapter;

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

    private Parcelable mListState;


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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mListState = savedInstanceState.getParcelable("ListState");
        } catch (Exception e) {

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ListState", mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    private void setupPositionCountsViewModel() {
        Application application = getActivity().getApplication();
        int sourceId = getResources().getInteger(R.integer.source_type_position);

        PositionCountsViewModel viewModel = ViewModelProviders.of(
                this).get(PositionCountsViewModel.class);
        viewModel.getPositionCounts().observe(this, new Observer<List<Positions>>() {
            @Override
            public void onChanged(@Nullable List<Positions> positions) {
                // Set the adapter and see if we need a data refresh
                mAdapter.setPositionRecents(positions);
                if (mListState != null) {
                    mRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
                }
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
    public void onPositionRecentsHolderClick(String position) {
        mListener.onFragmentInteraction(R.string.source_position, position);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int sourceType, String position);
    }
}
