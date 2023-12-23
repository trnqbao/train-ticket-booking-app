package com.java.trainticketbookingapp.Adapter;

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
import androidx.recyclerview.widget.RecyclerView;

import com.java.trainticketbookingapp.Model.Ticket;
import com.java.trainticketbookingapp.R;
import com.java.trainticketbookingapp.TicketManagement.BookedTicketDetailsActivity;

import java.util.List;


public class BookedTicketAdapter extends RecyclerView.Adapter<BookedTicketAdapter.ViewHolder> {

    private List<Ticket> ticketList;
//    Button btn_viewTicket;

    public BookedTicketAdapter(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    @NonNull
    @Override
    public BookedTicketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booked_ticket_item_layout, parent, false);
//        btn_viewTicket = view.findViewById(R.id.btn_viewTicket);

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
        String specificDepartureTime = "12:00 PM"; // Change this to your specific time


        public ViewHolder(View itemView) {
            super(itemView);
            tvTripTime = itemView.findViewById(R.id.tv_trip_time);
            tvStart = itemView.findViewById(R.id.tv_trip_pick_up);
            tvDestination = itemView.findViewById(R.id.tv_trip_drop_off);
            tvTotalTripTime = itemView.findViewById(R.id.tv_total_trip_time);
            tvDepartureStation = itemView.findViewById(R.id.tv_departure_station);
            tvTrainId = itemView.findViewById(R.id.tv_train_id);
//            tvTripPrice = itemView.findViewById(R.id.tv_trip_price);
//            imgOption = itemView.findViewById(R.id.img_options);

//            imgOption.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    showTicketOptions(view);
//                }
//            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getTicketData(view);
                }
            });

//            btn_viewTicket.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Get the position of the clicked item
//                    int position = getAdapterPosition();
//
//                    // Get the relevant ticket information
//                    Ticket ticket = ticketList.get(position);
//                    int ticketID = ticket.getId();
//                    String ticketStart = ticket.getStart();
//                    String ticketDestination = ticket.getDestination();
//                    String ticketPrice = ticket.getPrice();
//                    String ticketDepartureTime = ticket.getDepartureTime();
//
//                    // Start the intent to ViewTicket.class
//                    Intent intent = new Intent(v.getContext(), ViewTicketActivity.class);
//                    intent.putExtra("ticket_start", ticketStart);
//                    intent.putExtra("ticket_destination", ticketDestination);
//                    intent.putExtra("ticket_price", ticketPrice);
//                    intent.putExtra("ticket_departure_time", ticketDepartureTime);
//                    intent.putExtra("ticket_id", ticketID);
//
//                    v.getContext().startActivity(intent);
//                }
//            });

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
            int ticketID = ticket.getId();
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

        // Khong can refund ve - bo di
//        private void showTicketOptions(View view) {
//            PopupMenu popupMenu = new PopupMenu(itemView.getContext(), view);
//            MenuInflater inflater = popupMenu.getMenuInflater();
//            inflater.inflate(R.menu.ticket_options_menu, popupMenu.getMenu());
//
//            popupMenu.setOnMenuItemClickListener(item -> {
//                String itemId = (String) item.getTitle();
//
//                if (itemId.equals("item_ticket_details")) {
//                    Log.e("TAG", "showTicketOptions: " + itemId);
//                    viewTicketDetails(view);
//                    return true;
//                } else if (itemId.equals("item_refund_ticket")) {
//                    refundTicket();
//                    return true;
//                } else if (itemId.equals("item_delete_ticket")) {
//                    deleteTicket();
//                    return true;
//                }
//                return false;
//            });
//            popupMenu.show();
//        }
//
//
//        // viet may cai nay di
//        private void viewTicketDetails(View view) {
//            getTicketData(view);
//        }
//
//        private void refundTicket() {
//
//        }
//
//        private void deleteTicket() {
//
//        }
//
//    }
    }
}

