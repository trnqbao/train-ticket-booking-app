package com.java.trainticketbookingapp.Task;

import android.os.AsyncTask;
import javax.mail.*;
import java.util.Properties;


public class SendMailTask extends AsyncTask<Message, Void, Boolean> {
    public Session session;

    public SendMailTask() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("hiepgale0817@gmail.com", "psngmckdnalndwtz");
            }
        });
    }

    @Override
    protected Boolean doInBackground(Message... messages) {
        try {
            Transport.send(messages[0]);
            return true; // Email sent successfully
        } catch (MessagingException e) {
            e.printStackTrace();
            return false; // Email sending failed
        }
    }
    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            // Handle success
        } else {
            // Handle failure
        }
    }
}