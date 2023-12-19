package com.java.trainticketbookingapp.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.java.trainticketbookingapp.AccountManagement.ViewTicketActivity;
import com.java.trainticketbookingapp.Model.Ticket;
import com.java.trainticketbookingapp.R;

import java.util.List;


public class BookedTicketAdapter extends RecyclerView.Adapter<BookedTicketAdapter.ViewHolder> {

    private List<Ticket> ticketList;
    Button btn_viewTicket;

    public BookedTicketAdapter(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    @NonNull
    @Override
    public BookedTicketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booked_ticket_item_layout, parent, false);
        btn_viewTicket = view.findViewById(R.id.btn_viewTicket);

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
        private TextView tvTripTime;
        private TextView tvStart;
        private TextView tvDestination;
        private TextView tvTotalTripTime;
        private TextView tvTripPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTripTime = itemView.findViewById(R.id.tv_trip_time);
            tvStart = itemView.findViewById(R.id.tv_trip_pick_up);
            tvDestination = itemView.findViewById(R.id.tv_trip_drop_off);
            tvTotalTripTime = itemView.findViewById(R.id.tv_total_trip_time);
            tvTripPrice = itemView.findViewById(R.id.tv_trip_price);

            btn_viewTicket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the position of the clicked item
                    int position = getAdapterPosition();

                    // Get the relevant ticket information
                    Ticket ticket = ticketList.get(position);
                    int ticketID = ticket.getId();
                    String ticketStart = ticket.getStart();
                    String ticketDestination = ticket.getDestination();
                    String ticketPrice = ticket.getPrice();
                    String ticketDepartureTime = ticket.getDepartureTime();

                    // Start the intent to ViewTicket.class
                    Intent intent = new Intent(v.getContext(), ViewTicketActivity.class);
                    intent.putExtra("ticket_start", ticketStart);
                    intent.putExtra("ticket_destination", ticketDestination);
                    intent.putExtra("ticket_price", ticketPrice);
                    intent.putExtra("ticket_departure_time", ticketDepartureTime);
                    intent.putExtra("ticket_id", ticketID);

                    v.getContext().startActivity(intent);
                }
            });
        }

        public void bind(Ticket ticket) {
            tvTripTime.setText(ticket.getDepartureTime());
            tvStart.setText(ticket.getStart());
            tvDestination.setText(ticket.getDestination());
            tvTotalTripTime.setText(ticket.getTotalTime());
            tvTripPrice.setText(ticket.getPrice());
        }
    }
}
