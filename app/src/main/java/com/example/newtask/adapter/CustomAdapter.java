package com.example.newtask.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newtask.R;
import com.example.newtask.model.Taxi;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {


    private LayoutInflater inflater;
    List<Taxi> list;
    private OnItemClickListener mListener;

    public CustomAdapter(Context ctx, List<Taxi> objects) {
        inflater = LayoutInflater.from(ctx);
        list = objects;
    }
    public interface  OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.custom, parent, false);
        MyViewHolder holder = new MyViewHolder(view,mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txId.setText(Integer.toString(list.get(position).getId()));
        holder.txType.setText(list.get(position).getFleetType());
        if (list.get(position).getFleetType().equals("TAXI")) {
            holder.imageView.setImageResource(R.drawable.car_taxi);
        } else {
            holder.imageView.setImageResource(R.drawable.pooliing);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txId;
        private TextView txType;
        private ImageView imageView;

        private MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            txId = itemView.findViewById(R.id.custom_id);
            txType = itemView.findViewById(R.id.txt_id);
            imageView = itemView.findViewById(R.id.custom_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
