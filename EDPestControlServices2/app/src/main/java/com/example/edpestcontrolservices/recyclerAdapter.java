package com.example.edpestcontrolservices;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {

    private ArrayList<AccountSettingsMenu> accountSettings;
    private RecyclerViewClickListener listener;

    public recyclerAdapter(ArrayList<AccountSettingsMenu> accountSettings, RecyclerViewClickListener listener) {
        this.accountSettings = accountSettings;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView myProfile;

        public MyViewHolder(final View view) {
            super(view);
            myProfile = view.findViewById(R.id.settingTxt);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAbsoluteAdapterPosition());
        }
    }

    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        String setting = accountSettings.get(position).getAccountmenu();
        holder.myProfile.setText(setting);
    }

    @Override
    public int getItemCount() {
        return accountSettings.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }
}
