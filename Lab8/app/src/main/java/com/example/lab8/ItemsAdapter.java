package com.example.lab8;

/**
 * Created by Lenovo on 2017/12/10.
 */
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Lenovo on 2017/10/19.
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    private List<Items> rItems;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView gift;
        TextView birth;
        TextView name;
        View itemsView;
        public ViewHolder(View view) {
            super(view);
            itemsView=view;
            gift=(TextView) view.findViewById(R.id.items_gift);
            name=(TextView) view.findViewById(R.id.items_name);
            birth=(TextView)view.findViewById(R.id.items_birth);
        }
    }
    public ItemsAdapter(List<Items> r) {
        rItems=r;
    }

    private OnItemClickListener clickListener;

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public static interface OnItemClickListener {
        void onClick(View view,Items flag,int position);
    }
    private OnItemLongClickListener longClickListener;

    public void setLongClickListener(OnItemLongClickListener clickLongListener) {
        this.longClickListener = clickLongListener;
    }

    public static interface OnItemLongClickListener {
        void onLongClick(View view,Items flag,int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false);
        final ViewHolder  holder=new ViewHolder(view);
        holder.itemsView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int position =holder.getAdapterPosition();
                Items flag=rItems.get(position);
                clickListener.onClick(view,flag,position);
            }
        });
        holder.itemsView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                int position =holder.getAdapterPosition();
                Items flag=rItems.get(position);
                longClickListener.onLongClick(view,flag,position);
                return true;
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position) {
        Items items=rItems.get(position);
        holder.birth.setText(items.getBirth());
        holder.gift.setText(items.getGift());
        holder.name.setText(items.getName());
    }
    @Override
    public int getItemCount() {
        return rItems.size();
    }
}