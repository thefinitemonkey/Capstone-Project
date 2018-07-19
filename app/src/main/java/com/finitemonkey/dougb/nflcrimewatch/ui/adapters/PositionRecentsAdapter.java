package com.finitemonkey.dougb.nflcrimewatch.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.finitemonkey.dougb.nflcrimewatch.R;
import com.finitemonkey.dougb.nflcrimewatch.data.placeholders.Positions;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Recents;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PositionRecentsAdapter extends RecyclerView.Adapter<PositionRecentsAdapter.PositionRecentsViewHolder> {

    private List<Positions> mPositions;
    private int mPositionRecentsCount = 0;
    private Context mContext;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MMM dd, `yy");
    private PositionRecentsHolderClickListener mListener;

    public PositionRecentsAdapter(Context context, PositionRecentsHolderClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setPositionRecents(List<Positions> positions) {
        mPositions = positions;
        mPositionRecentsCount = mPositions.size();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PositionRecentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the template
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.rv_template_position_recents, parent, false);

        return new PositionRecentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PositionRecentsViewHolder holder, int position) {

        // Get the TeamRecents item to work from
        Positions tr = mPositions.get(position);

        // Set the text displays

        holder.mPositionName.setText(tr.getPlayerPositionName());
        holder.mArrestsCount.setText(tr.getArrestCount() + " " + mContext.getResources().getString(R.string.number_arrests_slug));
    }

    @Override
    public int getItemCount() {
        return mPositionRecentsCount;
    }

    class PositionRecentsViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
        @BindView(R.id.tv_position_name)
        TextView mPositionName;
        @BindView(R.id.tv_arrests_count)
        TextView mArrestsCount;

        public PositionRecentsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            String positionId = mPositions.get(clickPosition).getPlayerPosition();
            mListener.onPositionRecentsHolderClick(positionId);
        }
    }

    public interface PositionRecentsHolderClickListener {
        void onPositionRecentsHolderClick(String positionId);
    }
}
