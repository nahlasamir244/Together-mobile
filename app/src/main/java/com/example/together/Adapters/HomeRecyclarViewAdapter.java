package com.example.together.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.together.R;
import com.example.together.data.model.UserGroup;
import com.example.together.data.storage.Storage;
import com.example.together.group_screens.single_group.GroupViewPager;
import com.example.together.utils.HelperClass;

import java.util.ArrayList;

import static com.example.together.utils.HelperClass.TAG;

public class HomeRecyclarViewAdapter extends RecyclerView.Adapter<HomeRecyclarViewAdapter.MyViewHolder> {


    ArrayList<UserGroup> userGroups = new ArrayList<>();
    Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public HomeRecyclarViewAdapter(ArrayList<UserGroup> userGroups, Context context) {
        this.userGroups = userGroups;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HomeRecyclarViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.home_group_cell, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.title.setText(userGroups.get(position).getName());
        holder.description.setText(userGroups.get(position).getDescription());
        if (userGroups.get(position).getPhoto() != null) {
            Log.i(TAG,  "HomeRecyclarViewAdapter -- onBindViewHolder: [img no null]");
            Bitmap photo = HelperClass.decodeBase64(userGroups.get(position)
                    .getPhoto());
            holder.groupImage.setImageBitmap(photo);
        } else {
            Log.i(TAG,  "HomeRecyclarViewAdapter -- onBindViewHolder: [img null]");

            holder.groupImage.setImageResource(R.drawable.default_img);
        }
        holder.groupCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToGroup = new Intent(context, GroupViewPager.class);
                //goToGroup.putExtra("group",userGroups.get(position));
                Storage storage = new Storage();
                storage.saveGroupObject(userGroups.get(position), context);
                context.startActivity(goToGroup);
            }
        });

// =======
//         holder.title.setText(pojos.get(position).title);
//         holder.description.setText(pojos.get(position).description);
//         holder.groupImage.setImageResource(pojos.get(position).image);
//         if (position == 0) {
//             holder.groupCardView.setOnClickListener(v -> {
//                 Log.i(HelperClass.TAG, "onBindViewHolder: ");
//                 Intent goToGroup = new Intent(context,
//                         com.example.together.group_screens.ViewGroup.class);
//                 context.startActivity(goToGroup);
//             });
//         } else if (position == 1) {
//             holder.groupCardView.setOnClickListener(v -> {
//                 Intent goToGroup = new Intent(context, GroupViewPager.class);
//                 context.startActivity(goToGroup);
//             });
//         } else if (position == 2) {
//             holder.groupCardView.setOnClickListener(v -> {
//                 Intent intent = new Intent(context, TestApis.class);
//                 context.startActivity(intent);
//             });
//         } else if (position == 3) {
//             holder.groupCardView.setOnClickListener(v -> {
//                 Intent intent = new Intent(context, EditGroupInfo.class);
//                 context.startActivity(intent);
//             });
//         } else {
//             holder.groupCardView.setOnClickListener(v -> {
//                 Intent intent = new Intent(context, ToDoListMain.class);
//                 context.startActivity(intent);
//             });
//         }
// >>>>>>> master

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return userGroups.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView description;
        public ImageView groupImage;
        public FrameLayout groupCardView;
        public View layout;

        public MyViewHolder(View v) {
            super(v);
            layout = v;
            groupImage = v.findViewById(R.id.group_image);
            title = v.findViewById(R.id.group_title);
            description = v.findViewById(R.id.group_description);
            groupCardView = v.findViewById(R.id.group_card);
        }
    }
}
