package com.example.Wk2_project;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPicture;
        TextView tvName;
        TextView tvPhone;
        LinearLayout parentLayout;
        Button callButton;
        Button msgButton;

    public MyViewHolder(View view){
        super(view);

        //ivPicture = view.findViewById(R.id.iv_picture);
        tvName = view.findViewById(R.id.tv_name);
        //tvPhone = view.findViewById(R.id.tv_phone);
        parentLayout = view.findViewById(R.id.linearLayout);
        callButton = view.findViewById(R.id.bt_call);
        msgButton = view.findViewById(R.id.bt_msg);

    }
}

    private ArrayList<ContactInfo> contactInfoArrayList;
    MyAdapter(ArrayList<ContactInfo> contactInfoAL){
        this.contactInfoArrayList = contactInfoAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder myViewHolder = (MyViewHolder) holder;

        //myViewHolder.ivPicture.setImageResource(contactInfoArrayList.get(position).drawableId);
        myViewHolder.tvName.setText(contactInfoArrayList.get(position).name);
        //myViewHolder.tvPhone.setText(contactInfoArrayList.get(position).phone);
        final String Name = contactInfoArrayList.get(position).name;
        final String Ph_number = contactInfoArrayList.get(position).phone;
        final String ID = contactInfoArrayList.get(position).id;
        final String Image_ID = contactInfoArrayList.get(position).image_id;
        myViewHolder.msgButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Toast.makeText(view.getContext(), "메시지 보내기", Toast.LENGTH_SHORT).show();
                Intent d = new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:"+Ph_number));
                view.getContext().startActivity(d);
            }
        });
        myViewHolder.callButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Toast.makeText(view.getContext(), "전화 걸기", Toast.LENGTH_SHORT).show();
                Intent c = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+Ph_number));
                view.getContext().startActivity(c);
            }
                                                   });
        myViewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(view.getContext(), "연락처 보기", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(view.getContext(), subactivity_contact.class);
                i.putExtra("name", Name);
                i.putExtra("ph_num", Ph_number);
                i.putExtra("id", ID);
                i.putExtra("image_id", Image_ID);
                view.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactInfoArrayList.size();
    }
}