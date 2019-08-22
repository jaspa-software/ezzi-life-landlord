package za.co.ezzilyf.partner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.models.Photo;

public class PropertyPhotosAdapter extends RecyclerView.Adapter<PropertyPhotosAdapter.MyViewHolder> {
    private List<Photo> photos;
    private Context context;

    public PropertyPhotosAdapter(List<Photo> photos, Context context) {
        this.context = context;
        this.photos = photos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_property_images, viewGroup,
                false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {

        Glide
                .with(context)
                .load(photos.get(position).getUrl())
                .centerCrop()
                .placeholder(R.drawable.spinner)
                .into(myViewHolder.photo);


        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Functionality not implemented yet", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView photo, delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.row_property_photo);

            delete = itemView.findViewById(R.id.row_property_delete);

        }
    }
}
