package com.android.androidassignment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductRecyclerviewAdapter extends
        RecyclerView.Adapter<ProductRecyclerviewAdapter.viewholder>
        implements Filterable {

    ArrayList<Integer> id;
    ArrayList<String> productname, productprice,searchlist;

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_child,parent,false);
        return new viewholder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        if(id.size()>0)
        {
            holder.text1.setText(searchlist.get(position));
            holder.text2.setText("Price : $"+productprice.get(position));
        }
    }


    @Override
    public int getItemCount() {
        return searchlist.size();
    }

    public ProductRecyclerviewAdapter(ArrayList<Integer> id,
                                      ArrayList<String> productname,
                                      ArrayList<String> productprice) {
        this.id = id;
        this.productname = productname;
        this.productprice = productprice;
        this.searchlist = productname;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charseq = constraint.toString();
                if(charseq.isEmpty())
                {
                    searchlist = productname;
                }
                else {
                    ArrayList<String> filteredlist = new ArrayList<String>();
                    for (String product : productname) {
                        if (product.toLowerCase().contains(charseq.toLowerCase())) {
                            filteredlist.add(product);
                        }
                        searchlist =  filteredlist;
                    }
                }
                FilterResults results = new FilterResults();
                results.values = searchlist;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                searchlist = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }
        };

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
                    Intent intent = new Intent(itemView.getContext(),ViewingActivity.class);
                    intent.putExtra("id",id.get(getAdapterPosition()));
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }
}
