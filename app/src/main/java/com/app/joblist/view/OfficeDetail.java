package com.app.joblist.view;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.app.joblist.R;
import com.app.joblist.databinding.ActivityOfficeDetailBinding;
import com.app.joblist.model.PositionModel;
import com.app.joblist.service.BaseRetrofit;
import com.app.joblist.service.RetrofitInterface;
import com.app.joblist.utilities.GlobalFunction;
import com.squareup.picasso.Picasso;

public class OfficeDetail extends AppCompatActivity {

   ActivityOfficeDetailBinding binding;
   String id;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      binding = ActivityOfficeDetailBinding.inflate(getLayoutInflater());
      getSupportActionBar().hide();
      id = getIntent().getStringExtra("id");
      setContentView(binding.getRoot());
      getJobDetail();
      binding.backButton.setOnClickListener(v -> finish());
   }

   private void getJobDetail() {
      BaseRetrofit.getRetrofitServer().create(RetrofitInterface.class).getJobDetail(id).enqueue(new Callback<PositionModel>() {
         @Override
         public void onResponse(Call<PositionModel> call, Response<PositionModel> response) {
            if (response.body() != null) {
               if (response.isSuccessful() && response.code() == 200) {
                  PositionModel jobData = response.body();
                  binding.jobCompany.setText(jobData.getCompany());
                  binding.jobLocation.setText(jobData.getLocation());
                  binding.jobCompanyLink.setText(jobData.getCompanyUrl());
                  binding.jobTitle.setText(jobData.getTitle());
                  String fulltime;
                  if (jobData.getType().equals("Full Time")) {
                     fulltime = "Yes";
                  } else {
                     fulltime = "No";
                  }
                  binding.jobFulltime.setText(fulltime);
                  binding.jobDescription.setText(Html.fromHtml(Html.fromHtml(jobData.getDescription()).toString()));
                  Picasso.get().load(jobData.getCompanyLogo()).placeholder(R.drawable.office_holder).into(binding.imgOffice);
               }
            }
         }

         @Override
         public void onFailure(Call<PositionModel> call, Throwable t) {
            GlobalFunction.showToast(OfficeDetail.this, t.getLocalizedMessage());

         }
      });
   }
}