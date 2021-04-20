package com.example.st_task;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    private ArrayList<Tasks> names;
    private ItemPress mitemPress;

    public Adapter(ArrayList<Tasks> names, ItemPress itemPress) {
        this.names = names;
        this.mitemPress = itemPress;
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list, parent, false);
        return new ViewHolder(view, mitemPress);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText((names.get(position)).getName());
        holder.id.setText(String.valueOf((names.get(position)).getId()));
        holder.status.setText(String.valueOf((names.get(position)).getStatus()));
    }


    @Override
    public int getItemCount() {
        return names.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView name;
        public TextView id;
        public TextView status;
        ItemPress itemPress;

        public ViewHolder(@NonNull View itemView, ItemPress itemPress) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.TaskItem);
            id = (TextView) itemView.findViewById(R.id.TaskNum);
            status = (TextView) itemView.findViewById(R.id.status);
            this.itemPress = itemPress;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemPress.Click(getAdapterPosition());
        }
    }

    public interface ItemPress{
        void Click(int position);
    }
}

