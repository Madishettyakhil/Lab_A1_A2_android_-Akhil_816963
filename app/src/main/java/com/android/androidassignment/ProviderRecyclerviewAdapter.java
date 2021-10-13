package com.android.androidassignment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class ProviderRecyclerviewAdapter extends RecyclerView.Adapter<ProviderRecyclerviewAdapter.viewholder> {

    ArrayList<String> providerlist1,providerlist2;
    int frequency;
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_child,parent,false);
        return new viewholder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        if (!providerlist1.isEmpty()) {
            if (providerlist2.size() < providerlist1.size()) {
                frequency = Collections.frequency(providerlist1, providerlist2.get(position));
            } else {
                frequency = 1;
            }

            holder.text1.setText(providerlist2.get(position).toLowerCase());
            holder.text2.setText(String.valueOf(frequency));
        }
    }

    @Override
    public int getItemCount(){
        return providerlist2.size();
    }

    public ProviderRecyclerviewAdapter(ArrayList<String> providerlist1,ArrayList<String> providerlist2)
    {
        this.providerlist1 = providerlist1;
        this.providerlist2 = providerlist2;
    }


    public class viewholder extends RecyclerView.ViewHolder
    {
        TextView text1,text2;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.text1);
            text2 = itemView.findViewById(R.id.text2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(),ViewProductActivity.class);
                    intent.putExtra("provider",providerlist2.get(getAdapterPosition()));
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }

}
