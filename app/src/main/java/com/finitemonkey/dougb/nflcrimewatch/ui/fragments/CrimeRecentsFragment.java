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
import com.finitemonkey.dougb.nflcrimewatch.data.proxies.Crimes;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.CrimesCountsViewModel;
import com.finitemonkey.dougb.nflcrimewatch.ui.adapters.CrimeRecentsAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.DividerItemDecoration.HORIZONTAL;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;


public class CrimeRecentsFragment extends Fragment implements CrimeRecentsAdapter.CrimeRecentsHolderClickListener {
    private static final String TAG = CrimeRecentsFragment.class.getSimpleName();
    @BindView(R.id.rv_team_recents)
    RecyclerView mRecyclerView;
    private CrimeRecentsAdapter mAdapter;
    private Context mContext;
    private Boolean mHasCheckedUpdate = false;
    private OnFragmentInteractionListener mListener;


    public CrimeRecentsFragment() {
        // Required empty public constructor
    }

    public static CrimeRecentsFragment newInstance() {
        CrimeRecentsFragment fragment = new CrimeRecentsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setupCrimeCountsViewModel() {
        Application application = getActivity().getApplication();
        int sourceId = getResources().getInteger(R.integer.source_type_crime);

        CrimesCountsViewModel viewModel = ViewModelProviders.of(
                this).get(CrimesCountsViewModel.class);
        viewModel.getCrimesCounts().observe(this, new Observer<List<Crimes>>() {
            @Override
            public void onChanged(@Nullable List<Crimes> crimes) {
                // Set the adapter and see if we need a data refresh
                mAdapter.setCrimeRecents(crimes);
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
        mAdapter = new CrimeRecentsAdapter(mContext, this);
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
        setupCrimeCountsViewModel();

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
    public void onCrimeRecentsHolderClick(String crime) {
        mListener.onFragmentInteraction(R.string.source_crime, crime);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int sourceType, String crime);
    }
}
