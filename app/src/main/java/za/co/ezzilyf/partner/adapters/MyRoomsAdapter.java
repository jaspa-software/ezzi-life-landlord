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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_room, viewGroup,
                false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {

        int space = roomList.get(position).getTotalTenants() - roomList.get(position).getCurrentTenants();

        myViewHolder.status.setText(roomList.get(position).getCurrentTenants() + " tenants, "  + space  +" space(s) remaining");

        myViewHolder.roomId.setText(roomList.get(position).getRoomNumber());
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView roomId, status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            roomId = itemView.findViewById(R.id.list_room_roomId);

            status = itemView.findViewById(R.id.list_room_tenants);

        }
    }
}
