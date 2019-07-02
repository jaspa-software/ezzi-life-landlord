package za.co.ezzilyf.partner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.models.Tenant;

public class MyTenantsAdapter extends RecyclerView.Adapter<MyTenantsAdapter.MyViewHolder> {
    private List<Tenant> tenantList;
    private Context context;

    public MyTenantsAdapter(List<Tenant> tenantList, Context context) {
        this.context = context;
        this.tenantList = tenantList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_tenant, viewGroup,
                false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {

        myViewHolder.name.setText(tenantList.get(position).getFullNames());

        myViewHolder.institution.setText(tenantList.get(position).getInstitution() + " - " +
                tenantList.get(position).getStudentNumber());

    }

    @Override
    public int getItemCount() {
        return tenantList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, institution;
        ImageView photo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.list_tenant_ivName);

            institution = itemView.findViewById(R.id.list_tenant_ivInstitution);

            photo = itemView.findViewById(R.id.list_tenant_ivPhoto);

        }
    }
}
