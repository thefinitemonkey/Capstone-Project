package com.finitemonkey.dougb.nflcrimewatch.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.finitemonkey.dougb.nflcrimewatch.R;
import com.finitemonkey.dougb.nflcrimewatch.data.placeholders.Crimes;
import com.finitemonkey.dougb.nflcrimewatch.data.placeholders.Positions;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CrimeRecentsAdapter extends RecyclerView.Adapter<CrimeRecentsAdapter.CrimeRecentsViewHolder> {

    private List<Crimes> mCrimes;
    private int mCrimesRecentCount = 0;
    private Context mContext;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MMM dd, `yy");
    private CrimeRecentsHolderClickListener mListener;

    public CrimeRecentsAdapter(Context context, CrimeRecentsHolderClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setCrimeRecents(List<Crimes> crimes) {
        mCrimes = crimes;
        mCrimesRecentCount = mCrimes.size();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CrimeRecentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the template
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.rv_template_crime_recents, parent, false);

        return new CrimeRecentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrimeRecentsViewHolder holder, int position) {

        // Get the TeamRecents item to work from
        Crimes tr = mCrimes.get(position);

        // Set the text displays

        holder.mCrimeName.setText(tr.getCategory());
        holder.mArrestsCount.setText(tr.getArrestCount() + " " + mContext.getResources().getString(R.string.number_arrests_slug));
    }

    @Override
    public int getItemCount() {
        return mCrimesRecentCount;
    }

    class CrimeRecentsViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
        @BindView(R.id.tv_crime_name)
        TextView mCrimeName;
        @BindView(R.id.tv_crime_arrests_count)
        TextView mArrestsCount;

        public CrimeRecentsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            String crimeId = mCrimes.get(clickPosition).getEncounter();
            mListener.onCrimeRecentsHolderClick(crimeId);
        }
    }

    public interface CrimeRecentsHolderClickListener {
        void onCrimeRecentsHolderClick(String positionId);
    }
}
