package com.finitemonkey.dougb.nflcrimewatch.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finitemonkey.dougb.nflcrimewatch.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AdFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();


    public AdFragment() {
        // Required empty public constructor
    }

    public static AdFragment newInstance(String param1, String param2) {
        AdFragment fragment = new AdFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // This is the paid version, so no Ad for you!
        View view = inflater.inflate(R.layout.fragment_ad, container, false);

        return view;
    }

}
