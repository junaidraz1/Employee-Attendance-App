package com.example.employeeattendanceapp.AdapterRecyclerView;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeeattendanceapp.Model.ExperienceData;
import com.example.employeeattendanceapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceAdapter.ViewHolder> {

    Context context;
    ArrayList<ExperienceData> experienceDataList;

    public ExperienceAdapter(Context context, ArrayList<ExperienceData> experienceDataList) {
        this.context = context;
        this.experienceDataList = experienceDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_rv_experience, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String status = experienceDataList.get(holder.getAdapterPosition()).expIsCurrent;

        if (experienceDataList.get(holder.getAdapterPosition()).expFromDate != null) {
            String[] separated = experienceDataList.get(holder.getAdapterPosition()).expFromDate.split("T");
            holder.tvStartDate.setText(separated[0]);
        } else {
            holder.tvStartDate.setText("-");
        }

        if (experienceDataList.get(holder.getAdapterPosition()).expToDate != null) {
            String[] separated = experienceDataList.get(holder.getAdapterPosition()).expToDate.split("T");
            holder.tvEndDate.setText(separated[0]);

        } else if (status.equalsIgnoreCase("true")) {
            holder.tvEndDate.setText("Currently Working");
            holder.tvEndDate.setTextColor(context.getResources().getColor(R.color.green_bg));

        } else {
            holder.tvEndDate.setText("-");
        }

        if (experienceDataList.get(holder.getAdapterPosition()).expTitle != null) {
            holder.tvJobTitle.setText(experienceDataList.get(holder.getAdapterPosition()).expTitle);

        } else {
            holder.tvJobTitle.setText("-");
        }

        if (experienceDataList.get(holder.getAdapterPosition()).expIsCurrent != null) {
            if (status.equalsIgnoreCase("true")) {

                holder.tvJobStatus.setText("Currently Working");
            } else {
                holder.tvJobStatus.setText("Currently Not Working");
            }
        } else {
            holder.tvJobStatus.setText("-");
        }

        if (experienceDataList.get(holder.getAdapterPosition()).expCompanyName != null) {
            holder.tvOrgName.setText(experienceDataList.get(holder.getAdapterPosition()).expCompanyName);
        } else {
            holder.tvOrgName.setText("-");
        }

        if (experienceDataList.get(holder.getAdapterPosition()).expCompanyLocatioName != null) {
            holder.tvOrgLocation.setText(experienceDataList.get(holder.getAdapterPosition()).expCompanyLocatioName);
        } else {
            holder.tvOrgLocation.setText("-");
        }

    }

    @Override
    public int getItemCount() {
        return experienceDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvStartDate, tvEndDate, tvJobTitle, tvJobStatus, tvOrgName, tvOrgLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvStartDate = itemView.findViewById(R.id.tv_startDate);
            tvEndDate = itemView.findViewById(R.id.tv_endDate);
            tvJobTitle = itemView.findViewById(R.id.tv_jobTitle);
            tvJobStatus = itemView.findViewById(R.id.tv_jobStatus);
            tvOrgName = itemView.findViewById(R.id.tv_companyName);
            tvOrgLocation = itemView.findViewById(R.id.tv_companyAddress);
        }
    }
}
