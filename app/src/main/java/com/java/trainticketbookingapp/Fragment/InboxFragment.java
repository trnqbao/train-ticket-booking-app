package com.java.trainticketbookingapp.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.trainticketbookingapp.Model.UserAccount;
import com.java.trainticketbookingapp.R;
import com.java.trainticketbookingapp.Task.SendMailTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class InboxFragment extends Fragment {

    private EditText edt_message, edit_title;
    private Button bt_send;
    FirebaseUser user;
    FirebaseAuth auth;
    String email_sender;
    String email_admin = "hiepgale0817@gmail.com";
    private List<String> chatMessages = new ArrayList<>();
    private Map<String, String> preExistingAnswers = new HashMap<>();
    private Spinner spinnerPreExistingMessages;
    private TextView tvAutoReply;
    private SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        edt_message = view.findViewById(R.id.edt_message);
        edit_title = view.findViewById(R.id.edt_title);
        spinnerPreExistingMessages = view.findViewById(R.id.spinner_pre_existing_messages);
        bt_send = view.findViewById(R.id.bt_send);
        tvAutoReply = view.findViewById(R.id.tv_auto_reply);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        get_userEmail(user);
        loadChatMessages();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, chatMessages);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPreExistingMessages.setAdapter(spinnerAdapter);

        // Set up OnItemSelectedListener for the spinner
        spinnerPreExistingMessages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Set edt_message when an item is selected in the spinner
                String selectedMessage = spinnerPreExistingMessages.getSelectedItem().toString();
                edt_message.setText(selectedMessage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subject = edit_title.getText().toString();
                String selectedMessage = spinnerPreExistingMessages.getSelectedItem().toString();
                String userMessage;

                if (!TextUtils.isEmpty(selectedMessage) && !selectedMessage.equals("Select a message")) {
                    userMessage = selectedMessage;
                    edt_message.setText(userMessage);
                } else {
                    // If no pre-existing message is selected, use the entered message
                    userMessage = edt_message.getText().toString();
                }

                // Check if the message is one of the pre-existing messages
                if (preExistingAnswers.containsKey(userMessage)) {
                    // Get the pre-existing answer
                    String autoReply = preExistingAnswers.get(userMessage);
                    // Display user message and auto-reply in the chat
                    tvAutoReply.setText("Auto-reply: " + autoReply);
                    edt_message.getText().clear();

                } else {
                    // If the message is not a pre-existing one, proceed to send the email
                    String emailBody = "Message from: " + email_sender + "\n\n" + userMessage;
                    MimeMessage message = new MimeMessage(new SendMailTask().session);
                    try {
                        message.setFrom(new InternetAddress(email_sender));
                        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email_admin));
                        message.setSubject(subject);
                        message.setText(emailBody);
                        new SendMailTask().execute(message);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                    tvAutoReply.setText("");
                }
            }
        });

        return view;
    }

    private void get_userEmail(FirebaseUser user) {
        String userID = user.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered user");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount userAccount = snapshot.getValue(UserAccount.class);
                if (userAccount != null) {
                    email_sender = user.getEmail();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void loadChatMessages() {

            // Add pre-existing chat messages to the list
            chatMessages.add(getString(R.string.how_to_use_ticket));
            preExistingAnswers.put(getString(R.string.how_to_use_ticket), getString(R.string.answer_use_ticket));

            chatMessages.add(getString(R.string.how_to_book_ticket));
            preExistingAnswers.put(getString(R.string.how_to_book_ticket), getString(R.string.answer_book_ticket));

            chatMessages.add(getString(R.string.how_to_change_language));
            preExistingAnswers.put(getString(R.string.how_to_change_language), getString(R.string.answer_change_language));

            chatMessages.add(getString(R.string.how_to_update_profile));
            preExistingAnswers.put(getString(R.string.how_to_update_profile), getString(R.string.answer_update_profile));

    }

}
