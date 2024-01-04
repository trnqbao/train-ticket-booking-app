package com.java.trainticketbookingapp.TicketManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.graphics.Bitmap;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.zxing.qrcode.QRCodeWriter;
import com.java.trainticketbookingapp.Fragment.BookingFragment;
import com.java.trainticketbookingapp.R;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class BookedTicketDetailsActivity extends AppCompatActivity {
    final String TAG = "TicketDetails";
    TextView tvticketID, destination, start, departuretime, price, tvTotalTime, tvStartStation, tvDesStation, arrivaltime, username, trainId, departtureDate, arrivalDate;
    TextView menuStart, menuDes;
    ImageButton imgBtnBack, imgBtnOption;
    FirebaseUser user;
    FirebaseAuth auth;
    private String data;
//    private String ticketCode = getIntent().getStringExtra("ticket_code");

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ticket);

        tvticketID = findViewById(R.id.tv_training_booking_code);
        destination = findViewById(R.id.destination);
        start = findViewById(R.id.start);
        price = findViewById(R.id.price);
        departuretime = findViewById(R.id.departureTime);
        tvTotalTime = findViewById(R.id.tv_total_trip_time);
        tvStartStation = findViewById(R.id.tv_departure_station);
        tvDesStation = findViewById(R.id.tv_arrival_station);
        arrivaltime = findViewById(R.id.tv_arrival_time);
        username = findViewById(R.id.tv_user_name);
        trainId = findViewById(R.id.tv_train_id);
        departtureDate = findViewById(R.id.tv_departure_date);
        arrivalDate = findViewById(R.id.tv_arrival_date);

        menuStart = findViewById(R.id.tv_start);
        menuDes = findViewById(R.id.tv_des);

        imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnOption = findViewById(R.id.imgBtnOption);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Registered user").child(user.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userUsername = dataSnapshot.child("userName").getValue(String.class);
                        username.setText(userUsername);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        String ticketID = getIntent().getStringExtra(String.valueOf("ticket_ID"));
        String ticketStart = getIntent().getStringExtra("ticket_start");
        String ticketDestination = getIntent().getStringExtra("ticket_destination");
        String ticketPrice = getIntent().getStringExtra("ticket_price");
        String ticketDepartureTime = getIntent().getStringExtra("ticket_departure_time");
        String ticketTotalTripTime = getIntent().getStringExtra("ticket_total_trip_time");
        String ticketArrivalTime = getIntent().getStringExtra("ticket_arrival_time");
        String ticketTrainID = getIntent().getStringExtra("ticket_train_id");
        String ticketDepartureDate = getIntent().getStringExtra("ticket_date");
        String ticketArrivalDate = calculateArrivalDate(ticketDepartureDate, ticketDepartureTime, ticketTotalTripTime);
//        ticketCode = getIntent().getStringExtra("ticket_code");

        menuStart.setText(ticketStart);
        menuDes.setText(ticketDestination);

//        tvticketID.setText(String.valueOf(ticketID));
        destination.setText(ticketDestination);
        start.setText(ticketStart);
        departuretime.setText(ticketDepartureTime);
        arrivaltime.setText(ticketArrivalTime);
        price.setText(ticketPrice + " VND");
        tvTotalTime.setText(ticketTotalTripTime);
        tvStartStation.setText(ticketStart + " Station");
        tvDesStation.setText(ticketDestination + " Station");
        trainId.setText(ticketTrainID);
        departtureDate.setText(ticketDepartureDate);
//        Log.e(TAG, "onCreate: " + ticketDepartureDate );
        arrivalDate.setText(ticketArrivalDate);

        data = user.getDisplayName() + "-" +
                ticketID + "-" +
                ticketStart + "-" +
                ticketDestination + "-" +
                ticketDepartureTime + "-" +
                ticketArrivalTime + "-" +
                ticketPrice + " VND-" +
                ticketTotalTripTime + "-" +
                ticketTrainID;



        tvticketID.setText(ticketID);

        imgBtnBack.setOnClickListener(v -> replaceFragment(new BookingFragment()));

        imgBtnOption.setOnClickListener(view -> showOptionsDialog(Integer.parseInt(ticketID), ticketStart, ticketDestination, ticketDepartureTime, ticketPrice, ticketTotalTripTime));

    }

    private Bitmap generateQRCode(String ticketInfo) {
        try {
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(ticketInfo, BarcodeFormat.QR_CODE, 500, 500, hints);

            int width = matrix.getWidth();
            int height = matrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            int[] pixels = new int[width * height];

            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = matrix.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }

            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showOptionsDialog(int ticketID, String ticketStart, String ticketDestination, String ticketDepartureTime, String ticketPrice, String totalTripTime) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.options))
                .setItems(new CharSequence[]{getString(R.string.share_ticket), getString(R.string.qr_code)}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            shareTicket(ticketStart, ticketDestination, ticketDepartureTime, ticketPrice);
                            break;
                        case 1:
                            showQRCode(data);
                            Log.e(TAG, "showOptionsDialog: " + data);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void shareTicket(String ticketStart, String ticketDestination, String ticketDepartureTime, String ticketPrice) {
        String ticketInfo = "Start: " + ticketStart + "\n" +
                        "Destination: " + ticketDestination + "\n" +
                        "Departure Time: " + ticketDepartureTime + "\n" +
                        "Price: " + ticketPrice;

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Your Ticket Information");
                shareIntent.putExtra(Intent.EXTRA_TEXT, ticketInfo);

                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_ticket)));
    }

    private void showQRCode(String ticketInfo) {
        Bitmap qrCode = generateQRCode(ticketInfo);
        if (qrCode != null) {
            ImageView imageView = new ImageView(BookedTicketDetailsActivity.this);
            imageView.setImageBitmap(qrCode);
            new AlertDialog.Builder(BookedTicketDetailsActivity.this)
                    .setTitle(getString(R.string.qr_code))
                    .setView(imageView)
                    .setPositiveButton(getString(R.string.close), null)
                    .show();
        } else {
            Toast.makeText(BookedTicketDetailsActivity.this, "Error generating QR code", Toast.LENGTH_SHORT).show();
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.view_ticket, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        finish();
    }

    private String calculateArrivalDate(String departureDate, String departureTime, String duration) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH'h'mm");

        try {
            Date departureDateTime = dateFormat.parse(departureDate);
            Date departureTimeOnly = timeFormat.parse(departureTime);

            int totalMinutes = parseDuration(duration);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(departureDateTime);
            calendar.set(Calendar.HOUR_OF_DAY, departureTimeOnly.getHours());
            calendar.set(Calendar.MINUTE, departureTimeOnly.getMinutes());

            calendar.add(Calendar.MINUTE, totalMinutes);

            Date arrivalDate = calendar.getTime();
            return dateFormat.format(arrivalDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int parseDuration(String duration) {
        int hours = 0;
        int minutes = 0;

        String[] parts = duration.split("h|H|m|M");
        if (parts.length >= 2) {
            hours = Integer.parseInt(parts[0]);
            minutes = Integer.parseInt(parts[1]);
        }

        return hours * 60 + minutes;
    }

}