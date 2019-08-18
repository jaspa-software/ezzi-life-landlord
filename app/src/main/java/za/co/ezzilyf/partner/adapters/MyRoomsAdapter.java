package za.co.ezzilyf.partner.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.activities.RoomDetailsActivity;
import za.co.ezzilyf.partner.models.Room;

public class MyRoomsAdapter  extends RecyclerView.Adapter<MyRoomsAdapter.MyViewHolder> {
    private List<Room> roomList;
    private Context context;

    public MyRoomsAdapter(List<Room> roomList, Context context) {
        this.context = context;
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_room, viewGroup,
                false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {

        myViewHolder.roomId.setText(roomList.get(position).getRoomNumber() +
                " - " + roomList.get(position).getTypeOfOccupants());

        myViewHolder.status.setText(roomList.get(position).getOccupants() + " spaces " + roomList.get(position).getRoomStatus());

        myViewHolder.propertyName.setText(roomList.get(position).getPropertyName());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, RoomDetailsActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("ROOM",roomList.get(position));

                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView roomId, propertyName, status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            roomId = itemView.findViewById(R.id.list_room_roomId);

            status = itemView.findViewById(R.id.list_room_roomStatus);

            propertyName = itemView.findViewById(R.id.list_room_propertyName);

        }
    }
}
