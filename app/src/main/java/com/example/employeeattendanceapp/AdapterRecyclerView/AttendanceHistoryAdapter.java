package com.example.employeeattendanceapp.AdapterRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeeattendanceapp.Listeners.RecyclerClickListener;
import com.example.employeeattendanceapp.Model.AttendanceHistoryData;
import com.example.employeeattendanceapp.R;

import java.util.ArrayList;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class AttendanceHistoryAdapter extends RecyclerView.Adapter<AttendanceHistoryAdapter.ViewHolder> {

    Context context;
    ArrayList<AttendanceHistoryData> attendanceHistoryData;
    RecyclerClickListener recyclerClickListener;

    public AttendanceHistoryAdapter(Context context, ArrayList<AttendanceHistoryData> attendanceHistoryData, RecyclerClickListener recyclerClickListener) {
        this.context = context;
        this.attendanceHistoryData = attendanceHistoryData;
        this.recyclerClickListener = recyclerClickListener;
    }

    @NonNull
    @Override
    public AttendanceHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layouts_rv_history, parent, false);
        return new AttendanceHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceHistoryAdapter.ViewHolder holder, int position) {

        String[] trimmedDate = attendanceHistoryData.get(holder.getAdapterPosition()).atdDate.split("T");
        holder.tvDate.setText(trimmedDate[0]);

        holder.tvCheckInTime.setText(attendanceHistoryData.get(holder.getAdapterPosition()).atdTimeIn);
        holder.tvCheckOutTime.setText(attendanceHistoryData.get(holder.getAdapterPosition()).atdTimeOut);
        holder.tvAtdStatus.setText(attendanceHistoryData.get(holder.getAdapterPosition()).atdStatus);

        String status = attendanceHistoryData.get(holder.getAdapterPosition()).atdStatus;
        if (status != null) {
            if (status.equalsIgnoreCase("day off")) {
                holder.layoutHistoryRow.setBackgroundColor(context.getResources().getColor(R.color.offDay));

            } else if (status.equalsIgnoreCase("absent")) {
                holder.layoutHistoryRow.setBackgroundColor(context.getResources().getColor(R.color.absent_day));

            } else if (status.equalsIgnoreCase("present")) {
                holder.layoutHistoryRow.setBackgroundColor(context.getResources().getColor(R.color.normalDay));

            } else if (status.equalsIgnoreCase("late")) {
                holder.layoutHistoryRow.setBackgroundColor(context.getResources().getColor(R.color.late_day));
            }
        }

//        holder.tvMonthName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                recyclerClickListener.itemClickListener(holder.getAdapterPosition(),
//                        holder.tvMonthName.getText().toString());
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return attendanceHistoryData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate, tvCheckInTime, tvCheckOutTime, tvAtdStatus;
        LinearLayout layoutHistoryRow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tv_date);
            tvCheckInTime = itemView.findViewById(R.id.tv_checkInTime);
            tvCheckOutTime = itemView.findViewById(R.id.tv_checkOutTime);
            tvAtdStatus = itemView.findViewById(R.id.tv_status);
            layoutHistoryRow = itemView.findViewById(R.id.ll_textView);

        }
    }
}
