package com.example.a8_3_db_comms_room.db.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a8_3_db_comms_room.*;
import com.example.a8_3_db_comms_room.db.entity.Contact;

import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>{


    class ContactViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvFirstName;
        private final TextView tvLastName;
        private final TextView tvPhoneNumber;
        private ContactViewHolder(View itemView){
            super(itemView);
            tvFirstName = itemView.findViewById(R.id.tvFirstName);
            tvLastName = itemView.findViewById(R.id.tvLastName);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
        }

    }

    private final LayoutInflater mInflater;
    private List<Contact> mContacts;

    public ContactListAdapter(Context context){mInflater = LayoutInflater.from(context);}

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.recyclerview_contact_item, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position){
        if(mContacts!=null){
            Contact current = mContacts.get(position);
            holder.tvFirstName.setText(current.getFirstname()+"");
            holder.tvLastName.setText(current.getLastname());
            holder.tvPhoneNumber.setText(current.getPhone());
        } else {
            holder.tvFirstName.setText("No Firstname");
            holder.tvLastName.setText("No Lastname");
            holder.tvPhoneNumber.setText("No Phonenumber");
        }
    }

    public void setContacts(List<Contact> contacts){
        mContacts = contacts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        if(mContacts!=null){
            return mContacts.size();
        } else {
            return 0;
        }
    }

    public Contact getItem(int position){
        return mContacts.get(position);
    }
}