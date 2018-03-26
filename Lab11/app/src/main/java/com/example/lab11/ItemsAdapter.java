/**
 * Created by Lenovo on 2017/12/10.
 */
package com.example.lab11;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Lenovo on 2017/10/19.
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    private List<User>users;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView id;
        TextView blog;
        View itemsView;
        public ViewHolder(View view) {
            super(view);
            itemsView=view;
            name=(TextView) view.findViewById(R.id.text1_r);
            id=(TextView) view.findViewById(R.id.text2_r);
            blog=(TextView)view.findViewById(R.id.text3_r);
        }
    }
    public ItemsAdapter(List<User> r) {
        users=r;
    }

    private OnItemClickListener clickListener;

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public static interface OnItemClickListener {
        void onClick(View view, User flag, int position);
    }
    private OnItemLongClickListener longClickListener;

    public void setLongClickListener(OnItemLongClickListener clickLongListener) {
        this.longClickListener = clickLongListener;
    }

    public static interface OnItemLongClickListener {
        void onLongClick(View view, User flag, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card,parent,false);
        final ViewHolder  holder=new ViewHolder(view);
        holder.itemsView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int position =holder.getAdapterPosition();
                User flag=users.get(position);
                clickListener.onClick(view,flag,position);
            }
        });
        holder.itemsView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                int position =holder.getAdapterPosition();
                User flag=users.get(position);
                notifyItemRemoved(position);
                longClickListener.onLongClick(view,flag,position);
                return true;
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position) {
        User items=users.get(position);
        holder.name.setText(items.getLogin());

        holder.id.setText("id: "+items.getId());
        holder.blog.setText("blog: "+items.getBlog());
    }
    @Override
    public int getItemCount() {
        return users.size();
    }
}