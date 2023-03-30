package com.example.employeeattendanceapp.AdapterRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeeattendanceapp.Model.EducationData;
import com.example.employeeattendanceapp.R;

import java.util.ArrayList;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.ViewHolder> {

    Context context;
    ArrayList<EducationData> educationData;

    public EducationAdapter(Context context, ArrayList<EducationData> educationData) {
        this.context = context;
        this.educationData = educationData;
    }

    @NonNull
    @Override
    public EducationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_rv_education, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EducationAdapter.ViewHolder holder, int position) {

        if (educationData.get(holder.getAdapterPosition()).empStartDate != null) {
            String[] separated = educationData.get(holder.getAdapterPosition()).empStartDate.split("T");
            holder.tvStartDate.setText(separated[0]);
        } else {
            holder.tvStartDate.setText("-");
        }

        if (educationData.get(holder.getAdapterPosition()).empEndDate != null) {
            String[] separated = educationData.get(holder.getAdapterPosition()).empEndDate.split("T");
            holder.tvEndDate.setText(separated[0]);
        } else {
            holder.tvEndDate.setText("-");
        }

        if (educationData.get(holder.getAdapterPosition()).empDegreeName != null) {
            holder.tvDegreeName.setText(educationData.get(holder.getAdapterPosition()).empDegreeName);
        } else {
            holder.tvDegreeName.setText("-");
        }

        if (educationData.get(holder.getAdapterPosition()).empFieldName != null) {
            holder.tvFieldOfStudy.setText(educationData.get(holder.getAdapterPosition()).empFieldName);
        } else {
            holder.tvFieldOfStudy.setText("-");
        }

        if (educationData.get(holder.getAdapterPosition()).empInstituteName != null) {
            holder.tvInstitute.setText(educationData.get(holder.getAdapterPosition()).empInstituteName);
        } else {
            holder.tvInstitute.setText("-");
        }

        if (educationData.get(holder.getAdapterPosition()).empGrade != null) {
            holder.tvGrade.setText(educationData.get(holder.getAdapterPosition()).empGrade);
        } else {
            holder.tvGrade.setText("-");
        }

    }

    @Override
    public int getItemCount() {
        return educationData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvStartDate, tvEndDate, tvDegreeName, tvFieldOfStudy, tvInstitute, tvGrade;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvStartDate = itemView.findViewById(R.id.tv_startDate);
            tvEndDate = itemView.findViewById(R.id.tv_endDate);
            tvDegreeName = itemView.findViewById(R.id.tv_degreeName);
            tvFieldOfStudy = itemView.findViewById(R.id.tv_fOS);
            tvInstitute = itemView.findViewById(R.id.tv_institute);
            tvGrade = itemView.findViewById(R.id.tv_grade);

        }
    }
}
