package com.java.trainticketbookingapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.trainticketbookingapp.Model.Ticket;
import com.java.trainticketbookingapp.R;
import com.java.trainticketbookingapp.TicketManagement.BookedTicketDetailsActivity;

import java.util.List;


public class BookedTicketAdapter extends RecyclerView.Adapter<BookedTicketAdapter.ViewHolder> {

    private List<Ticket> ticketList;

    public BookedTicketAdapter(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    @NonNull
    @Override
    public BookedTicketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booked_ticket_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookedTicketAdapter.ViewHolder holder, int position) {
        Ticket ticket = ticketList.get(position);
        holder.bind(ticket);
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTripTime, tvTrainId;
        private TextView tvStart;
        private TextView tvDestination;
        private TextView tvTotalTripTime;
        private TextView tvTripPrice;
        private TextView tvDepartureStation;

        private ImageView imgOption;
        String ticketDepartureTime;
        FirebaseUser user;
        FirebaseAuth auth, ticketAuth;
        DatabaseReference reference;
        int ticketID;

        String title, message, cancel_yes, cancel_no;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTripTime = itemView.findViewById(R.id.tv_trip_time);
            tvStart = itemView.findViewById(R.id.tv_trip_pick_up);
            tvDestination = itemView.findViewById(R.id.tv_trip_drop_off);
            tvTotalTripTime = itemView.findViewById(R.id.tv_total_trip_time);
            tvDepartureStation = itemView.findViewById(R.id.tv_departure_station);
            tvTrainId = itemView.findViewById(R.id.tv_train_id);
            imgOption = itemView.findViewById(R.id.img_options);

            auth = FirebaseAuth.getInstance();
            ticketAuth = FirebaseAuth.getInstance();

            user = auth.getCurrentUser();
            String userID = user.getUid();

            reference = FirebaseDatabase.getInstance().getReference("Registered user").child(userID).child("tickets");
            imgOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTicketOptions(view);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getTicketData(view);
                }
            });
        }


        public void bind(Ticket ticket) {
            tvTripTime.setText(ticket.getDepartureTime());
            tvStart.setText(ticket.getStart());
            tvDestination.setText(ticket.getDestination());
            tvTrainId.setText(ticket.getTrainID());
        }

        private void getTicketData(View view) {

            int i = getAdapterPosition();

            Ticket ticket = ticketList.get(i);
            ticketID = ticket.getId();
            String ticketStart = ticket.getStart();
            String ticketDestination = ticket.getDestination();
            String ticketStartTime = ticket.getDepartureTime();
            String ticketArrivalTime = ticket.getArrivalTime();
            String ticketPrice = ticket.getPrice();
            ticketDepartureTime = ticket.getDepartureTime();
            String ticketTotalTripTime = ticket.getTotalTime();
            String ticketTrainID = ticket.getTrainID();
            String ticketDate = ticket.getDate();

            // Start the intent to ViewTicket.class
            Intent intent = new Intent(view.getContext(), BookedTicketDetailsActivity.class);
            intent.putExtra("ticket_ID", String.valueOf(ticketID));
            intent.putExtra("ticket_start", ticketStart);
            intent.putExtra("ticket_destination", ticketDestination);
            intent.putExtra("ticket_start_time", ticketStartTime);
            intent.putExtra("ticket_arrival_time", ticketArrivalTime);
            intent.putExtra("ticket_price", ticketPrice);
            intent.putExtra("ticket_departure_time", ticketDepartureTime);
            intent.putExtra("ticket_id", ticketID);
            intent.putExtra("ticket_total_trip_time", ticketTotalTripTime);
            intent.putExtra("ticket_train_id", ticketTrainID);

            view.getContext().startActivity(intent);
        }


        private void showTicketOptions(View view) {

            Context context = view.getContext();

            PopupMenu popupMenu = new PopupMenu(itemView.getContext(), view);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.ticket_options_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId(); // Use getItemId() as an integer
                if (itemId == R.id.item_delete_ticket) {

                    title = context.getString(R.string.cancel_ticket);
                    message = context.getString(R.string.cancel_ticket_confirm);
                    cancel_yes = context.getString(R.string.cancel_yes);
                    cancel_no = context.getString(R.string.cancel_no);

                    deleteTicket();

                    return true;
                }
                return false;
            });
            popupMenu.show();
        }


        private void deleteTicket() {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton(cancel_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {

                        Ticket ticketToDelete = ticketList.get(position);
                        String ticketIdToRemove = String.valueOf(ticketToDelete.getId());

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                                    Ticket ticket = snapshot.getValue(Ticket.class);
                                    if (ticket != null && String.valueOf(ticket.getId()).equals(ticketIdToRemove)) {
                                        snapshot.getRef().removeValue();
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle the error, if any
                                Log.e("BookedTicketAdapter", "Error deleting ticket: " + error.getMessage());
                            }
                        });

                        ticketList.remove(position);
                        notifyItemRemoved(position);
                    }
                    dialogInterface.dismiss();
                }
            });

            builder.setNegativeButton(cancel_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}

