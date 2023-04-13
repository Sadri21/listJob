package com.app.joblist.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.joblist.R;
import com.app.joblist.databinding.ItemJobBinding;
import com.app.joblist.model.PositionModel;
import com.app.joblist.view.OfficeDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PositionRecyclerAdapter extends RecyclerView.Adapter<PositionRecyclerAdapter.ViewHolder> {

   ArrayList<PositionModel> positionListAll = new ArrayList<>();
   Context context;

   public PositionRecyclerAdapter(Context context) {
      this.context = context;
   }

   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
      return new ViewHolder(itemView);
   }

   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      PositionModel positionJob = positionListAll.get(position);
      ItemJobBinding binding = holder.binding;
      if (positionJob != null) {
         binding.rootLayout.setVisibility(View.VISIBLE);
         binding.jobName.setText(positionJob.getTitle());
         binding.jobCompany.setText(positionJob.getCompany());
         binding.jobLocation.setText(positionJob.getLocation());
         Picasso.get().load(positionJob.getCompanyLogo()).placeholder(R.drawable.office_holder).into(binding.imgOffice);
         binding.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(context, OfficeDetail.class);
               intent.putExtra("id", positionJob.getId());
               context.startActivity(intent);
            }
         });
      } else {
         binding.rootLayout.setVisibility(View.GONE);
      }
   }

   @Override
   public int getItemCount() {
      return positionListAll.size();
   }

   public void addItem(PositionModel jobPosition) {
      positionListAll.add(jobPosition);
      notifyItemInserted(positionListAll.size() - 1);
   }


   public static class ViewHolder extends RecyclerView.ViewHolder {

      ItemJobBinding binding;
      public ViewHolder(View itemView) {
         super(itemView);
         binding = ItemJobBinding.bind(itemView);
      }
   }
}
