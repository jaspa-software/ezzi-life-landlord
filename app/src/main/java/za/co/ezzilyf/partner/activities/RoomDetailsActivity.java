package za.co.ezzilyf.partner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nullable;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.adapters.MyRoomsAdapter;
import za.co.ezzilyf.partner.adapters.SlidingImageAdapter;
import za.co.ezzilyf.partner.models.Room;

public class RoomDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private TextView textView;

    private ProgressBar progressBar;

    private String propertyId;

    private MyRoomsAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_room_details);

        propertyId = getIntent().getStringExtra("PROPERTY_ID");

        recyclerView = findViewById(R.id.room_details_recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setHasFixedSize(true);

        textView = findViewById(R.id.room_details_message);

        progressBar = findViewById(R.id.room_details_progressBar);

        if (propertyId !=null) {

            getRooms();

        }

        ImageView btnClose = findViewById(R.id.room_details_btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        Button btnAddRoom = findViewById(R.id.room_details_btnAddRoom);

        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAddRoomDialog();

            }
        });
    }

    private void showAddRoomDialog() {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_room, viewGroup, false);

        final EditText etRoomName = dialogView.findViewById(R.id.dialog_add_room_etRoomNumber);

        final EditText etRental = dialogView.findViewById(R.id.dialog_add_room_etMonthlyRental);

        final EditText etMaxOccupants = dialogView.findViewById(R.id.dialog_add_room_etTotalNumberOfOccupants);

        final RadioButton rbMale = dialogView.findViewById(R.id.dialog_add_room_rbMale);

        final RadioButton rbFemale = dialogView.findViewById(R.id.dialog_add_room_rbFemale);

        final RadioButton rbSingle = dialogView.findViewById(R.id.dialog_add_room_rbSingle);

        final RadioButton rbSharing = dialogView.findViewById(R.id.dialog_add_room_rbSharing);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                if (TextUtils.isEmpty(etRoomName.getText())) {

                    etRoomName.setError("Room Name/Number required!");
                    etRoomName.requestFocus();

                    return;
                }

                if (TextUtils.isEmpty(etRental.getText())) {

                    etRental.setError("Rental Field is required!");
                    etRental.requestFocus();

                    return;
                }

                if (TextUtils.isEmpty(etMaxOccupants.getText())) {

                    etMaxOccupants.setError("Allowed Occupants field is required!");
                    etMaxOccupants.requestFocus();

                    return;
                }

                if (!rbFemale.isChecked() && !rbMale.isChecked()) {

                    Toast.makeText(RoomDetailsActivity.this, "Occupants Type is required", Toast.LENGTH_SHORT).show();

                    return;
                }

                if (!rbSingle.isChecked() && !rbSharing.isChecked()) {

                    Toast.makeText(RoomDetailsActivity.this, "Room Type is required", Toast.LENGTH_SHORT).show();

                    return;
                }


                HashMap<String, Object> roomDetails = new HashMap<>();

                roomDetails.put("propertyId",propertyId);

                roomDetails.put("rental",Double.parseDouble(etRental.getText().toString()));

                roomDetails.put("roomNumber", etRoomName.getText().toString());

                if (rbSharing.isChecked()) {

                    roomDetails.put("roomType", "Sharing");

                }else{

                    roomDetails.put("roomType", "Single");

                }

                if (rbFemale.isChecked()) {

                    roomDetails.put("tenantType", "Female");

                }else{

                    roomDetails.put("roomType", "Male");

                }


                roomDetails.put("totalTenants", Integer.parseInt(etMaxOccupants.getText().toString()) );

                FirebaseFirestore nearByRef = FirebaseFirestore.getInstance();

                nearByRef.collection("rooms")
                        .document(propertyId)
                        .collection("propertyRooms")
                        .add(roomDetails)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                dialog.dismiss();

                                Toast.makeText(RoomDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();

        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.show();

    }

    private void getRooms() {

        FirebaseFirestore roomsRef = FirebaseFirestore.getInstance();

        roomsRef.collection("rooms")
                .document(propertyId)
                .collection("propertyRooms")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (e !=null) {

                            textView.setText(e.getMessage());

                            recyclerView.setVisibility(View.GONE);

                            progressBar.setVisibility(View.GONE);

                            textView.setVisibility(View.VISIBLE);

                            return;
                        }

                        if (queryDocumentSnapshots !=null && queryDocumentSnapshots.size() > 0) {

                            List<Room> roomList = new ArrayList<>();

                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                                Room room = doc.toObject(Room.class);

                                roomList.add(room);
                            }

                            adapter = new MyRoomsAdapter(roomList, RoomDetailsActivity.this);

                            recyclerView.setAdapter(adapter);

                            recyclerView.setVisibility(View.VISIBLE);

                            progressBar.setVisibility(View.GONE);

                            textView.setVisibility(View.GONE);

                            adapter.notifyDataSetChanged();

                        } else {

                            textView.setText("Not available rooms");

                            recyclerView.setVisibility(View.GONE);

                            progressBar.setVisibility(View.GONE);

                            textView.setVisibility(View.VISIBLE);
                        }

                    }
                });

    }

}
