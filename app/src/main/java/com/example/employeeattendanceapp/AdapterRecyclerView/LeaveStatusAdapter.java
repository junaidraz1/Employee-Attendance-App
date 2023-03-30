package com.example.employeeattendanceapp.AdapterRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeeattendanceapp.Listeners.RecyclerClickListener;
import com.example.employeeattendanceapp.Model.LeaveStatusData;
import com.example.employeeattendanceapp.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

public class LeaveStatusAdapter extends RecyclerView.Adapter<LeaveStatusAdapter.ViewHolder> {

    Context context;
    ArrayList<LeaveStatusData> leaveStatusData;
    RecyclerClickListener recyclerClickListener;

    public LeaveStatusAdapter(Context context, ArrayList<LeaveStatusData> leaveStatusData, RecyclerClickListener recyclerClickListener) {
        this.context = context;
        this.leaveStatusData = leaveStatusData;
        this.recyclerClickListener = recyclerClickListener;
    }

    @NonNull
    @Override
    public LeaveStatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_rv_leavestatus, parent, false);
        return new LeaveStatusAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveStatusAdapter.ViewHolder holder, int position) {

        String[] formattedSdate = leaveStatusData.get(holder.getAdapterPosition()).leaveStartDate.split("T");
        String[] formattedEdate = leaveStatusData.get(holder.getAdapterPosition()).leaveEndDate.split("T");

        if (leaveStatusData.get(holder.getAdapterPosition()).leaveIsShortLeave.equals("false")) {
            holder.tvFromDate.setText(formattedSdate[0]);
            holder.tvToDate.setText(formattedEdate[0]);
            holder.tvShortLeave.setVisibility(View.GONE);

        } else {
            holder.tvFromDate.setText(formattedSdate[0] + " " + formattedSdate[1]);
            holder.tvToDate.setText(formattedEdate[0] + " " + formattedEdate[1]);
            holder.tvShortLeave.setVisibility(View.VISIBLE);
            holder.tvShortLeave.setText("Short Leave");
        }

        holder.tvLeaveTitle.setText(leaveStatusData.get(holder.getAdapterPosition()).leaveTitle);
        holder.tvLeaveType.setText(leaveStatusData.get(holder.getAdapterPosition()).leaveType);
        holder.tvDescription.setText(leaveStatusData.get(holder.getAdapterPosition()).leaveDescription);
        holder.tvLineManager.setText(leaveStatusData.get(holder.getAdapterPosition()).leaveLineManagerName);

        if (leaveStatusData.get(holder.getAdapterPosition()).leaveStatus.equalsIgnoreCase("approved")) {
            holder.tvStatus.setText(leaveStatusData.get(holder.getAdapterPosition()).leaveStatus);
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.green_bg));

        } else if (leaveStatusData.get(holder.getAdapterPosition()).leaveStatus.equalsIgnoreCase("forward")) {
            holder.tvStatus.setText(leaveStatusData.get(holder.getAdapterPosition()).leaveStatus);
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.gold));

        } else if (leaveStatusData.get(holder.getAdapterPosition()).leaveStatus.equalsIgnoreCase("rejected")) {
            holder.tvStatus.setText(leaveStatusData.get(holder.getAdapterPosition()).leaveStatus);
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
        }

        if (leaveStatusData.get(holder.getAdapterPosition()).leaveAttachment != null && !leaveStatusData.get(holder.getAdapterPosition()).leaveAttachment.equals("")) {
            holder.attachmentLayout.setVisibility(View.VISIBLE);
            holder.attachmentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerClickListener.itemClickListener(holder.getAdapterPosition(), leaveStatusData.get(holder.getAdapterPosition()).leaveAttachment);
                }
            });
        } else {
            holder.attachmentLayout.setVisibility(View.GONE);
        }

        holder.toggleSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                if (holder.toggleLayout.isExpanded()) {
                    rotate = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                }
                rotate.setDuration(1000);
                rotate.setFillAfter(true);
                rotate.setInterpolator(new LinearInterpolator());
                holder.ivViewMore.startAnimation(rotate);
                holder.toggleLayout.toggle();
            }
        });
    }

    @Override
    public int getItemCount() {
        return leaveStatusData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvFromDate, tvToDate, tvShortLeave, tvLeaveType, tvLeaveTitle, tvDescription, tvLineManager, tvStatus;
        ImageView ivViewMore;

        RelativeLayout toggleSlider, attachmentLayout;
        ExpandableLayout toggleLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFromDate = itemView.findViewById(R.id.tv_fromDate);
            tvToDate = itemView.findViewById(R.id.tv_toDate);
            tvLeaveType = itemView.findViewById(R.id.tv_leaveType);
            tvShortLeave = itemView.findViewById(R.id.tv_shortLeave);
            tvLeaveTitle = itemView.findViewById(R.id.tv_leaveBody);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvLineManager = itemView.findViewById(R.id.tv_lineaManager);
            tvStatus = itemView.findViewById(R.id.tv_status);
            ivViewMore = itemView.findViewById(R.id.iv_dropExpandable);
            toggleSlider = itemView.findViewById(R.id.rl_toggleLayout);
            toggleLayout = itemView.findViewById(R.id.el_descriptionlayout);
            attachmentLayout = itemView.findViewById(R.id.rl_viewAttach);

        }
    }
}
