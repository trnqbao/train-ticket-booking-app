package com.java.trainticketbookingapp.AccountManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.trainticketbookingapp.Fragment.BookingFragment;
import com.java.trainticketbookingapp.Fragment.ProfileFragment;
import com.java.trainticketbookingapp.Model.Ticket;
import com.java.trainticketbookingapp.Model.UserAccount;
import com.java.trainticketbookingapp.R;
import com.java.trainticketbookingapp.TicketManagement.TicketList;
import com.squareup.picasso.Picasso;

public class PaymentActivity extends AppCompatActivity {

    TextView tv_ticketID, tv_ticketStart, tv_ticketDes, tv_ticketPrices;
    TextView tv_companyName, tv_bankingName, tv_bankAccount, tv_content, tv_prices, tv_cash1, tv_cash2, tv_cash3, tv_cash4;
    RadioButton rb_internet_banking, rb_cash;
    Button btn_pay, btn_confirm;
    private int userPoint;

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        tv_ticketID = findViewById(R.id.tv_ticketID);
        tv_ticketDes = findViewById(R.id.tv_ticketDestination);
        tv_ticketPrices = findViewById(R.id.tv_ticketPrices);
        tv_ticketStart = findViewById(R.id.tv_ticketStart);

        tv_companyName = findViewById(R.id.tv_companyName);
        tv_bankingName = findViewById(R.id.tv_bankingName);
        tv_bankAccount = findViewById(R.id.tv_bankAccount);
        tv_content = findViewById(R.id.tv_content);
        tv_prices = findViewById(R.id.tv_prices);
        tv_cash1 = findViewById(R.id.cash_1);
        tv_cash2 = findViewById(R.id.cash_2);
        tv_cash3 = findViewById(R.id.cash_3);
        tv_cash4 = findViewById(R.id.cash_4);

        rb_internet_banking = findViewById(R.id.rb_internet_banking);
        rb_cash = findViewById(R.id.rb_cash);
        btn_pay = findViewById(R.id.btn_pay);
        btn_confirm = findViewById(R.id.btn_confirm);

        int ticketId = getIntent().getIntExtra("ticket_id", 0);
        String ticketDestination = getIntent().getStringExtra("ticket_destination");
        String ticketPrice = getIntent().getStringExtra("ticket_price");
        String ticketStart = getIntent().getStringExtra("ticket_start");
        String ticket_departureTime = getIntent().getStringExtra("ticket_departureTime");
        String ticket_totalTime = getIntent().getStringExtra("ticket_totalTime");
        String ticket_arrivalTime = getIntent().getStringExtra("ticket_arrivalTime");
        String ticket_trainID = getIntent().getStringExtra("ticket_trainID");
        String ticket_date = getIntent().getStringExtra("ticket_date");

        // Set the data to TextViews
        tv_ticketID.setText(String.valueOf(ticketId));
        tv_ticketDes.setText(ticketDestination);
        tv_ticketPrices.setText(ticketPrice);
        tv_ticketStart.setText(ticketStart);

        tv_companyName.setText(getString(R.string.internet_companyName));
        tv_bankingName.setText(getString(R.string.internet_bankName));
        tv_bankAccount.setText(getString(R.string.internet_bankAccount));
        tv_content.setText(getString(R.string.internet_transferContent));
        tv_prices.setText(ticketPrice);

        tv_companyName.setVisibility(View.INVISIBLE);
        tv_bankingName.setVisibility(View.INVISIBLE);
        tv_bankAccount.setVisibility(View.INVISIBLE);
        tv_content.setVisibility(View.INVISIBLE);
        tv_prices.setVisibility(View.INVISIBLE);

        tv_cash1.setText(R.string.cash_companyName);
        tv_cash2.setText(R.string.cash_bankName);
        tv_cash3.setText(R.string.cash_bankAccount);
        tv_cash4.setText(R.string.cash_transferContent);

        tv_cash1.setVisibility(View.INVISIBLE);
        tv_cash2.setVisibility(View.INVISIBLE);
        tv_cash3.setVisibility(View.INVISIBLE);
        tv_cash4.setVisibility(View.INVISIBLE);

        if (rb_internet_banking.isChecked()) {

            tv_companyName.setVisibility(View.VISIBLE);
            tv_bankingName.setVisibility(View.VISIBLE);
            tv_bankAccount.setVisibility(View.VISIBLE);
            tv_content.setVisibility(View.VISIBLE);
            tv_prices.setVisibility(View.VISIBLE);

        } else if (rb_cash.isChecked()) {

            tv_cash1.setVisibility(View.VISIBLE);
            tv_cash2.setVisibility(View.VISIBLE);
            tv_cash3.setVisibility(View.VISIBLE);
            tv_cash4.setVisibility(View.VISIBLE);
        }

        btn_pay.setOnClickListener(v -> {

            if (rb_internet_banking.isChecked()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
                builder.setTitle(getString(R.string.internet_bankingDetails));
                builder.setMessage(getString(R.string.internet_details) + ":\n" +

                        getString(R.string.companyName) + " " + tv_companyName.getText() + "\n" +
                        getString(R.string.bankName)+ " " + tv_bankingName.getText() + "\n" +
                        getString(R.string.accountNumber)+ " " + tv_bankAccount.getText() + "\n" +
                        getString(R.string.prices) + " "+ tv_prices.getText() + "\n" +
                        getString(R.string.content) + " "+ tv_content.getText());

                builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                builder.create().show();

            } else if (rb_cash.isChecked()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
                builder.setTitle(getString(R.string.cash_paymentDetails));
                builder.setMessage(getString(R.string.cash_details) + "\n" +

                        tv_cash1.getText() + "\n" +
                        tv_cash2.getText() + "\n" +
                        tv_cash3.getText() + "\n" +
                        tv_cash4.getText() + "\n");

                builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                builder.create().show();

            }
        });

        //Get user Point
        String userID = user.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered user");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount userAccount = snapshot.getValue(UserAccount.class);
                if (userAccount != null) {
                    userPoint = userAccount.getUserPoint();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPoint++;
                reference.child(userID).child("userPoint").setValue(userPoint);
                Ticket ticket = new Ticket(ticketId, ticketStart, ticketDestination, ticketPrice, ticket_totalTime, ticket_departureTime, ticket_arrivalTime, ticket_trainID, ticket_date); // Populate your Ticket object

                reference.child(userID).child("tickets").push().setValue(ticket)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Navigate to BookingFragment
                            Fragment fragment = new BookingFragment();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.replace(R.id.activity_payment, fragment); // Use your actual container ID
                            transaction.addToBackStack(null);
                            transaction.commit();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
            }
        });



    }
}