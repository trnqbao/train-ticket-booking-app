package com.java.trainticketbookingapp.TicketManagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.java.trainticketbookingapp.Fragment.BookingFragment;
import com.java.trainticketbookingapp.R;

import java.util.HashMap;
import java.util.Map;

public class ViewTicketActivity extends AppCompatActivity {

    Button btn_shareTicket, btn_ticketCode;
    TextView destination, start, departuretime, price;
    ImageButton imgBtnBack, imgBtnOption;
    FirebaseUser user;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ticket);

//        btn_shareTicket = findViewById(R.id.btn_share_ticket);
//        btn_ticketCode = findViewById(R.id.btn_ticketCode);
        destination = findViewById(R.id.destination);
        start = findViewById(R.id.start);
        price = findViewById(R.id.price);
        departuretime = findViewById(R.id.departureTime);

        imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnOption = findViewById(R.id.imgBtnOption);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        String ticketStart = getIntent().getStringExtra("ticket_start");
        String ticketDestination = getIntent().getStringExtra("ticket_destination");
        String ticketPrice = getIntent().getStringExtra("ticket_price");
        String ticketDepartureTime = getIntent().getStringExtra("ticket_departure_time");


//        btn_ticketCode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String ticketInfo = "... your ticket information string..."; // replace with actual information
//                Bitmap qrCode = generateQRCode(ticketInfo);
//                if (qrCode != null) {
//                    ImageView imageView = new ImageView(ViewTicketActivity.this);
//                    imageView.setImageBitmap(qrCode);
//                    new AlertDialog.Builder(ViewTicketActivity.this)
//                            .setTitle("Ticket QR Code")
//                            .setView(imageView)
//                            .setPositiveButton("Close", null)
//                            .show();
//                } else {
//                    Toast.makeText(ViewTicketActivity.this, "Error generating QR code", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        btn_shareTicket.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String ticketInfo = "Start: " + ticketStart + "\n" +
//                        "Destination: " + ticketDestination + "\n" +
//                        "Departure Time: " + ticketDepartureTime + "\n" +
//                        "Price: " + ticketPrice;
//
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("text/plain");
//                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Your Ticket Information");
//                shareIntent.putExtra(Intent.EXTRA_TEXT, ticketInfo);
//
//                startActivity(Intent.createChooser(shareIntent, "Share Ticket"));
//            }
//        });

        destination.setText(ticketDestination);
        start.setText(ticketStart);
        departuretime.setText(ticketDepartureTime);
        price.setText(ticketPrice);

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new BookingFragment());
            }
        });

        imgBtnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog(ticketStart, ticketDestination, ticketDepartureTime, ticketPrice);
            }
        });
    }

    private Bitmap generateQRCode(String ticketInfo) {
        try {
            MultiFormatWriter writer = new MultiFormatWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix matrix = writer.encode(ticketInfo, BarcodeFormat.QR_CODE, 500, 500, hints);

            int width = matrix.getWidth();
            int height = matrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showOptionsDialog(String ticketStart, String ticketDestination, String ticketDepartureTime, String ticketPrice) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options")
                .setItems(new CharSequence[]{"Share", "QR Code"}, (dialog, which) -> {
                    // Handle option selection
                    switch (which) {
                        case 0:
                            shareTicket(ticketStart, ticketDestination, ticketDepartureTime, ticketPrice);
                            break;
                        case 1:
                            showQRCode("example ticket...");
                            break;
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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

                startActivity(Intent.createChooser(shareIntent, "Share Ticket"));
    }

    private void showQRCode(String ticketInfo) {
        Bitmap qrCode = generateQRCode(ticketInfo);
        if (qrCode != null) {
            ImageView imageView = new ImageView(ViewTicketActivity.this);
            imageView.setImageBitmap(qrCode);
            new AlertDialog.Builder(ViewTicketActivity.this)
                    .setTitle("Ticket QR Code")
                    .setView(imageView)
                    .setPositiveButton("Close", null)
                    .show();
        } else {
            Toast.makeText(ViewTicketActivity.this, "Error generating QR code", Toast.LENGTH_SHORT).show();
        }
    }

    public void replaceFragment(Fragment fragment) {
        // Replace with fragment transaction
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.view_ticket, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        finish();
    }

}