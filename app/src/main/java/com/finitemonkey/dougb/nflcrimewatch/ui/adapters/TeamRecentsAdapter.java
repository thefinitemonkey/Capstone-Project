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
import com.finitemonkey.dougb.nflcrimewatch.data.tables.TeamRecents;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamRecentsAdapter extends RecyclerView.Adapter<TeamRecentsAdapter.TeamRecentsViewHolder> {

    private List<TeamRecents> mTeamRecents;
    private int mTeamRecentsCount = 0;
    private Context mContext;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MMM dd, `yy");
    private TeamRecentsHolderClickListener mListener;

    public TeamRecentsAdapter(Context context, TeamRecentsHolderClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setTeamRecents(List<TeamRecents> teamRecents) {
        mTeamRecents = teamRecents;
        mTeamRecentsCount = mTeamRecents.size();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TeamRecentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the template
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.rv_template_team_recents, parent, false);

        return new TeamRecentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamRecentsViewHolder holder, int position) {

        // Get the TeamRecents item to work from
        TeamRecents tr = mTeamRecents.get(position);

        // Set the logo
        holder.mTeamLogo.setImageResource(tr.getLogo());

        // Set the offense color
        //int colorCode = Color.parseColor("#" + tr.getCrimeCategoryColor());
        //holder.mOffenseColor.setBackgroundColor(colorCode);

        // Set the text displays
        holder.mPlayerName.setText(tr.getPlayerName());
        holder.mTeamName.setText(tr.getTeamPreferredName());
        holder.mDivision.setText("(" + tr.getTeamConference() + " " + tr.getTeamDivision() + ")");
        //holder.mOffense.setText(tr.getCategory());
        //holder.mDescription.setText(tr.getDescription() + "  (" + tr.getResolution() + ")");

        // Set the date display
        String date = mDateFormat.format(tr.getDate());
        holder.mDate.setText(date);
    }

    @Override
    public int getItemCount() {
        return mTeamRecentsCount;
    }

    class TeamRecentsViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
        @BindView(R.id.iv_team_logo)
        ImageView mTeamLogo;
        @BindView(R.id.tv_player_name)
        TextView mPlayerName;
        @BindView(R.id.tv_team_name)
        TextView mTeamName;
        @BindView(R.id.tv_offense_date)
        TextView mDate;
        @BindView(R.id.tv_div_conf)
        TextView mDivision;

        public TeamRecentsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            String teamId = mTeamRecents.get(clickPosition).getTeam();
            mListener.onTeamRecentsHolderClick(teamId);
        }
    }

    public interface TeamRecentsHolderClickListener {
        void onTeamRecentsHolderClick(String teamId);
    }
}
