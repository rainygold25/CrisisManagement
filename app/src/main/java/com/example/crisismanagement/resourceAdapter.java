package com.example.crisismanagement;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class resourceAdapter extends RecyclerView.Adapter<resourceAdapter.ViewHolder> {

    resourceItem[] resourceItems;
    Context context;

    public resourceAdapter(resourceItem[] resourceItems, SearchResources activity) {
        this.resourceItems = resourceItems;
        this.context = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_view_resource, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final resourceItem curr_resourceItem = resourceItems[position];
        holder.resource_image.setImageBitmap(convertStringToBitmap(curr_resourceItem.getImage()));
        holder.textView_distance.setText(curr_resourceItem.getDistance());
        holder.textView_address.setText(curr_resourceItem.getAddress());
        holder.textView_date.setText(curr_resourceItem.getDate());
        holder.textView_name.setText(curr_resourceItem.getName());
        holder.textView_contact_info.setText(curr_resourceItem.getContact_information());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, curr_resourceItem.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static Bitmap convertStringToBitmap(String string) {
        byte[] byteArray1;
        byteArray1 = Base64.decode(string, Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray1, 0,
                byteArray1.length);/* w  w  w.ja va 2 s  .  c om*/
        return bmp;
    }

    @Override
    public int getItemCount() {
        return resourceItems.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView resource_image;
        TextView textView_name;
        TextView textView_contact_info;
        TextView textView_date;
        TextView textView_address;
        TextView textView_distance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            resource_image = (ImageView) itemView.findViewById(R.id.cardView_resource_item_image);
            textView_name = (TextView) itemView.findViewById(R.id.textView_MainName);
            textView_contact_info = (TextView) itemView.findViewById(R.id.textView_contact);
            textView_date = (TextView) itemView.findViewById(R.id.textView_Date);
            textView_address = (TextView) itemView.findViewById(R.id.textView_address);
            textView_distance = (TextView) itemView.findViewById(R.id.textView_distance);
        }
    }
}
