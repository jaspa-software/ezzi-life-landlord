package za.co.ezzilyf.partner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.models.NearBy;

public class NearByAdapter extends RecyclerView.Adapter<NearByAdapter.MyViewHolder> {
    private List<NearBy> nearByList;
    private Context context;

    public NearByAdapter(List<NearBy> nearByList, Context context) {
        this.context = context;
        this.nearByList = nearByList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_near_by_institution, viewGroup,
                false);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {

        Glide
                .with(context )
                .load(nearByList.get(position).getEmblem())
                .centerCrop()
                .into(myViewHolder.logo);

        myViewHolder.institution.setText(nearByList.get(position).getInstitution());

        myViewHolder.campus.setText(nearByList.get(position).getCampus());

        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Functionality not implemented yet", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return nearByList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView campus, institution;
        ImageView logo, delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            campus = itemView.findViewById(R.id.row_near_by_institution_tvCampus);

            institution = itemView.findViewById(R.id.row_near_by_institution_tvInstitution);

            logo = itemView.findViewById(R.id.row_near_by_institution_logo);

            delete = itemView.findViewById(R.id.row_near_by_institution_ivRemoveInstitution);

        }
    }
}
