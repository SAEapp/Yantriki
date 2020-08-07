package com.example.quizapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class cardAdapter extends PagerAdapter {

    private List<CardModel> cardModels;
    private LayoutInflater layoutInflater;
    private Context context;

    public cardAdapter(List<CardModel> cardModels, Context context){

        this.cardModels= cardModels;
        this.context= context;

    }

    @Override
    public int getCount() {
        return cardModels.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater= LayoutInflater.from(context);
        View view= layoutInflater.inflate(R.layout.card ,container, false);
        ImageView imageView;
        TextView title, desc;
        imageView = view.findViewById(R.id.Image);
        title= view.findViewById(R.id.Topic);
        desc= view.findViewById(R.id.desc);

        imageView.setImageResource(cardModels.get(position).getImage());
        title.setText(cardModels.get(position).getTitle());
        desc.setText(cardModels.get(position).getDesc());

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
        //super.destroyItem(container, position, object);
    }
}
