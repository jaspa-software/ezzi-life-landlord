package za.co.ezzilyf.partner.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.activities.PropertyDetailsActivity;
import za.co.ezzilyf.partner.models.Property;

public class MyPropertiesAdapter extends RecyclerView.Adapter<MyPropertiesAdapter.MyViewHolder> {
    private List<Property> propertyList;
    private Context context;

    public MyPropertiesAdapter(List<Property> propertyList, Context context) {
        this.context = context;
        this.propertyList = propertyList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_my_property, viewGroup,
                false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {

        String status = propertyList.get(position).getListingStatus();

        if (TextUtils.equals(status,"Pending for Approval")) {

            myViewHolder.status.setBackgroundResource(R.color.colorPending);

        }else if (TextUtils.equals(status,"Confirm Inspection Date")) {

            myViewHolder.status.setBackgroundResource(R.color.colorPending);

        } else if (TextUtils.equals(status,"Pending Inspection")) {

            myViewHolder.status.setBackgroundResource(R.color.colorPending);

        }else if (TextUtils.equals(status,"Approved")) {

            myViewHolder.status.setBackgroundResource(R.color.colorApproved);

        }else if (TextUtils.equals(status,"Not Approved")) {

            myViewHolder.status.setBackgroundResource(R.color.colorDeclined);

        }else if (TextUtils.equals(status,"Resubmit")) {

            myViewHolder.status.setBackgroundResource(R.color.colorFeedback);
        }

        myViewHolder.name.setText(propertyList.get(position).getPropertyName());

        myViewHolder.location.setText(propertyList.get(position).getPropertyLocation());

        myViewHolder.status.setText(propertyList.get(position).getListingStatus());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent = new Intent(context, PropertyDetailsActivity.class);

               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

               intent.putExtra("PROPERTY", propertyList.get(position));

               context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,location, status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            location = itemView.findViewById(R.id.list_my_property_tvLocation);

            status = itemView.findViewById(R.id.list_my_property_tvStatus);

            name = itemView.findViewById(R.id.list_my_property_tvName);

        }
    }
}
