package com.finitemonkey.dougb.nflcrimewatch.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.finitemonkey.dougb.nflcrimewatch.R;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Arrests;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SourcedOffensesAdapter extends RecyclerView.Adapter<SourcedOffensesAdapter.SourcedOffensesViewHolder> {

    private List<Arrests> mArrests;
    private int mArrestsCount = 0;
    private Context mContext;
    private SourceOffensesHolderClickListener mListener;
    private Boolean mClickable;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MMM dd, `yy");

    public SourcedOffensesAdapter(Context context, Boolean clickable, SourceOffensesHolderClickListener listener) {
        mContext = context;
        mClickable = clickable;
        mListener = listener;
    }

    public void setArrests(List<Arrests> arrests) {
        mArrests = arrests;
        mArrestsCount = mArrests.size();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SourcedOffensesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the template
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.rv_template_sourced_offenses, parent, false);

        return new SourcedOffensesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SourcedOffensesViewHolder holder, int position) {
        // Get the TeamRecents item to work from
        Arrests arrest = mArrests.get(position);

        // Set the logo
        holder.mTeamLogo.setImageResource(arrest.getLogo());
        holder.mTeamLogo.setContentDescription(
                arrest.getTeamPreferredName() + " " + mContext.getResources().getString(
                        R.string.logo_label));

        // Set the offense color
        int colorCode = Color.parseColor("#" + arrest.getCrimeCategoryColor());
        holder.mOffenseColor.setBackgroundColor(colorCode);

        // Set the text displays
        holder.mPlayerName.setText(arrest.getPlayerName());
        holder.mTeamName.setText(arrest.getTeamPreferredName());
        //holder.mDivision.setText("(" + arrest.getTeamConference() + " " + arrest.getTeamDivision() + ")");
        holder.mOffense.setText(arrest.getCategory());
        holder.mDescription.setText(arrest.getDescription() + "  (" + arrest.getResolution() + ")");

        // Set the date display
        String date = mDateFormat.format(arrest.getDate());
        holder.mDate.setText(date);
    }

    @Override
    public int getItemCount() {
        return mArrestsCount;
    }

    class SourcedOffensesViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
        @BindView(R.id.iv_team_logo)
        ImageView mTeamLogo;
        @BindView(R.id.tv_player_name)
        TextView mPlayerName;
        @BindView(R.id.tv_team_name)
        TextView mTeamName;
        @BindView(R.id.tv_offense_type)
        TextView mOffense;
        @BindView(R.id.tv_offense_description)
        TextView mDescription;
        @BindView(R.id.iv_offense_color)
        ImageView mOffenseColor;
        @BindView(R.id.tv_offense_date)
        TextView mDate;
        //@BindView(R.id.tv_div_conf)
        //TextView mDivision;

        public SourcedOffensesViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            // If we've drilled in far enough, the items won't be clickable any more
            if (!mClickable) return;

            int clickPosition = getAdapterPosition();
            String playerName = mArrests.get(clickPosition).getPlayerName();
            mListener.onSourceOffensesHolderClick(playerName);
        }
    }

    public interface SourceOffensesHolderClickListener {
        void onSourceOffensesHolderClick(String playerName);
    }
}
