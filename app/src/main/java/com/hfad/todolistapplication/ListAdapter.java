package com.hfad.todolistapplication;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {


    private String[] itemName;
    private String[] itemStatus;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView v){
            super(v);
            cardView  = v;
        }
    }
    public ListAdapter(String[] itemName, String[] itemStatus){
        this.itemName = itemName;
        this.itemStatus = itemStatus;
    }
    @Override
    public int getItemCount(){
        return itemName.length;
    }
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView itemNameView = (TextView)cardView.findViewById(R.id.item_name);
        TextView itemStatusView = (TextView)cardView.findViewById(R.id.item_name);

    }



}
