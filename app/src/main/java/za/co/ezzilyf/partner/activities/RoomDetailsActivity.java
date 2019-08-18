package za.co.ezzilyf.partner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.Timer;
import java.util.TimerTask;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.adapters.SlidingImageAdapter;
import za.co.ezzilyf.partner.models.Room;

public class RoomDetailsActivity extends AppCompatActivity {

    private static final String TAG = "RoomDetailsActivity";

    private Room room;

    private String[] urls;

    private static ViewPager mPager;

    private static int currentPage = 0;

    private static int NUM_PAGES = 0;

    private Button btnRemoveRoom;

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_room_details);

        Toolbar toolbar = findViewById(R.id.room_details_toolbar);

        setSupportActionBar(toolbar);

        btnRemoveRoom = findViewById(R.id.room_details_btnRemove);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        room = (Room) getIntent().getSerializableExtra("ROOM");

        if (room !=null) {

            getSupportActionBar().setTitle(room.getRoomNumber() + " - " + room.getPropertyName());

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            urls = new String[]{room.getRoomImageOne(), room.getRoomImageTwo(), room.getRoomImageThree()};

            btnRemoveRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    removeRoom(room.getRoomId());

                }
            });

            initSlider();
        }
    }

    private void removeRoom(String roomId) {

        final ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Removing room - " + roomId);

        progressDialog.setCanceledOnTouchOutside(false);

        progressDialog.show();

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("rooms")
                .document(roomId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        progressDialog.dismiss();

                        Toast.makeText(RoomDetailsActivity.this, "Room Removed Successfully", Toast.LENGTH_SHORT).show();

                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();

                        Toast.makeText(RoomDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void initSlider() {

        mPager = findViewById(R.id.room_details_viewPager);
        mPager.setAdapter(new SlidingImageAdapter(RoomDetailsActivity.this,urls));

        CirclePageIndicator indicator = findViewById(R.id.room_overview_indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES = urls.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }
}
