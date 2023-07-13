package com.example.edpestcontrolservices;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class editProfileAdapter extends RecyclerView.Adapter<editProfileAdapter.MyViewHolder> {
    private ArrayList<GetSetProfile> profileList;
    private RecyclerViewClickListener listener;

    public editProfileAdapter(ArrayList<GetSetProfile> profileList, RecyclerViewClickListener listener) {
        this.profileList = profileList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView editProfileText;

        public MyViewHolder(final View view) {
            super(view);
            editProfileText = view.findViewById(R.id.editProfileTxt);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAbsoluteAdapterPosition());
        }
    }

    @NonNull
    @Override
    public editProfileAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.editprofile_list_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull editProfileAdapter.MyViewHolder holder, int position) {
        String profile = profileList.get(position).getProfile();
        holder.editProfileText.setText(profile);
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    public interface RecyclerViewClickListener {
        void onClick (View v, int position);
    }
}
