package com.mmalvandi.serviceone.maindraweritems;
/* Creator: Mahdi on 7/15/2017. */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mmalvandi.serviceone.R;

public class ListAdapter extends RecyclerView.Adapter {

    private Context context;
    private Items[] items;

    public ListAdapter(Context context, Items[] items) {
        this.context = context;
        this.items = items;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
            imageView = (ImageView) itemView.findViewById(R.id.iconView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_drawer_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        alternativeOnBindViewHolder((ViewHolder) holder, position);
    }

    private void alternativeOnBindViewHolder(ViewHolder holder, int position) {
        Items items = this.items[position];
        holder.imageView.setImageResource(items.getPicResId());
        holder.textView.setText(items.getText());
    }

    @Override
    public int getItemCount() {
        return items.length;
    }
}
