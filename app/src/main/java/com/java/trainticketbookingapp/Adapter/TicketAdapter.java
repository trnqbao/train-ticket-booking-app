package com.java.trainticketbookingapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.java.trainticketbookingapp.Model.Ticket;
import com.java.trainticketbookingapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {

    private List<Ticket> ticketList;

    public TicketAdapter(List<Ticket> ticketList)
    {
        this.ticketList = ticketList;
    }
    private OnTicketClickListener listener;

    public void setOnTicketClickListener(OnTicketClickListener listener) {
        this.listener = listener;
    }

    public interface OnTicketClickListener
    {
        void onTicketClicked(Ticket ticket);
    }

    @NonNull
    @Override
    public TicketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ticket_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketAdapter.ViewHolder holder, int position) {
        Ticket ticket = ticketList.get(position);
        holder.bind(ticket);
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }
    public void saveToDatabase(Ticket ticket) {
        // Get Realtime Database reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Ticket booked");

        // Generate unique key
        String key = databaseReference.push().getKey();

        // Create ticket data map
        Map<String, Object> ticketData = new HashMap<>();
        ticketData.put("id", ticket.getId());
        ticketData.put("start", ticket.getStart());
        ticketData.put("destination", ticket.getDestination());
        ticketData.put("price", ticket.getPrice());
        ticketData.put("totalTime", ticket.getTotalTime());
        ticketData.put("departureTime", ticket.getDepartureTime());
        ticketData.put("arrivalTime", ticket.getArrivalTime());
        ticketData.put("trainID", ticket.getTrainID());
        ticketData.put("date", ticket.getDate());

        // Save ticket data
        databaseReference.child(key).setValue(ticketData);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDepartureTime;
        private TextView tvArrivalTime;
        private TextView tvTrainID;
        private TextView tvTotalTripTime;
        private TextView tvTripPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDepartureTime = itemView.findViewById(R.id.tv_departureTime);
            tvArrivalTime = itemView.findViewById(R.id.tv_arrivalTime);
            tvTrainID = itemView.findViewById(R.id.tv_trainID);
            tvTotalTripTime = itemView.findViewById(R.id.tv_totalTripTime);
            tvTripPrice = itemView.findViewById(R.id.tv_priceID);
        }

        public void bind(Ticket ticket) {
            tvDepartureTime.setText(ticket.getDepartureTime());
            tvArrivalTime.setText(ticket.getArrivalTime());
            tvTrainID.setText(ticket.getTrainID());
            tvTotalTripTime.setText(ticket.getTotalTime());
            tvTripPrice.setText(String.valueOf(ticket.getPrice()));
        }
    }
}
