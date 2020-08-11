package com.example.quizapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QARecyclerAdapter extends RecyclerView.Adapter<QARecyclerAdapter.QAViewHolder> {
    public List<String> optionsList;
    private OnOptionListner onOptionListner;

    public QARecyclerAdapter(List<String> optionsList,OnOptionListner onOptionListner) {
        this.optionsList = optionsList;
        this.onOptionListner = onOptionListner;
    }

    @NonNull
    @Override
    public QAViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_item_layout, parent, false);
        return new QAViewHolder(view,onOptionListner);
    }

    @Override
    public void onBindViewHolder(@NonNull QAViewHolder holder, int position) {
        String value = optionsList.get(position);
        holder.optn.setText(value);
    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }

    public class QAViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button optn;
        OnOptionListner onOptionListner;
        public QAViewHolder(@NonNull View itemView,OnOptionListner onOptionListner) {
            super(itemView);
            optn = itemView.findViewById(R.id.opt);
            this.onOptionListner = onOptionListner;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onOptionListner.OnOptionClick(v,getAdapterPosition());
        }
    }

    public  interface OnOptionListner{
        void OnOptionClick(View v ,int position);
    }
}
