package com.example.quizapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NsetsAdapter extends RecyclerView.Adapter<NsetsAdapter.MyViewHolder> {

    public List<setslist> sets;
    private OnNoteListener mOnNoteListener;

    public NsetsAdapter(List<setslist> sets, OnNoteListener onNoteListener) {
        this.sets = sets;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_set_item_layout, parent, false);

        return new NsetsAdapter.MyViewHolder(view, mOnNoteListener);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String quizName = sets.get(position).getQuizName();
        holder.setQuizName(quizName);

        String quizDesc = sets.get(position).getQuizDisc();
        holder.setQuizDesc(quizDesc);


    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View mview;
        public TextView QuizName, QuizDesc;
        OnNoteListener onNoteListener;

        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            mview = itemView;
            mview.setOnClickListener(this);
            this.onNoteListener = onNoteListener;

        }

        public void setQuizName(String quizNametxt) {
            QuizName = mview.findViewById(R.id.quizname);
            QuizName.setText(quizNametxt);
        }

        public void setQuizDesc(String quizDesctxt) {
            QuizDesc = mview.findViewById(R.id.quizdescription);
            QuizDesc.setText(quizDesctxt);
        }


        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }

}
