package za.co.ezzilyf.partner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.models.Appointment;

public class AppointmentsAdapter  extends RecyclerView.Adapter<AppointmentsAdapter.MyViewHolder> {
    private List<Appointment> appointmentList;
    private Context context;

    public AppointmentsAdapter(List<Appointment> appointmentList, Context context) {
        this.context = context;
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_appointment, viewGroup,
                false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {


    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView residence, date, inspector, comment;

        Button reschedule, confirm;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            residence = itemView.findViewById(R.id.row_appointment_tvResidence);

            date = itemView.findViewById(R.id.row_appointment_tvInspection);

            inspector = itemView.findViewById(R.id.row_appointment_tvInspector);

            comment = itemView.findViewById(R.id.row_appointment_tvComment);

            reschedule = itemView.findViewById(R.id.row_appointment_btnReschedule);

            confirm = itemView.findViewById(R.id.row_appointment_btnConfirm);


        }
    }
}
