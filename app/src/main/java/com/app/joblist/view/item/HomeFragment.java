package com.app.joblist.view.item;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.joblist.R;
import com.app.joblist.databinding.FragmentHomeBinding;
import com.app.joblist.model.PositionModel;
import com.app.joblist.service.BaseRetrofit;
import com.app.joblist.service.RetrofitInterface;
import com.app.joblist.utilities.GlobalFunction;
import com.app.joblist.view.adapter.PositionRecyclerAdapter;

import java.util.List;

public class HomeFragment extends Fragment {

   FragmentHomeBinding binding;
   Boolean isFirst;
   PositionRecyclerAdapter adapter;
   Integer page = 1;
   String description = "";
   String location = "";
   Boolean fulltime = false;
   Boolean isShow = false;


   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      // Inflate the layout for this fragment
      binding = FragmentHomeBinding.inflate(inflater, container, false);
      binding.rvJob.setLayoutManager(new LinearLayoutManager(requireContext()));
      adapter = new PositionRecyclerAdapter(requireContext());
      binding.rvJob.setAdapter(adapter);
      isFirst = true;
      getPositionList(page, description, location, fulltime);
      binding.rvJob.addOnScrollListener(new RecyclerView.OnScrollListener() {
         @Override
         public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (!recyclerView.canScrollVertically(1)) {
//               GlobalFunction.showToast(requireContext(), "last");
               page += 1;
               getPositionList(page, description, location, fulltime);
            }
         }
      });
      binding.switchFulltime.setOnCheckedChangeListener((buttonView, isChecked) -> {
         fulltime = isChecked;
      });
      binding.btnFilter.setOnClickListener(v -> {
         isFirst = true;
         adapter = new PositionRecyclerAdapter(requireContext());
         binding.rvJob.setAdapter(adapter);
         page = 1;
         description = binding.etDescription.getText().toString().toLowerCase();
         location = binding.etLocation.getText().toString().toLowerCase();
         getPositionList(page, description, location, fulltime);
      });
      binding.btnExpandMore.setOnClickListener(v -> {
         if (!isShow) {
            isShow = true;
            binding.llSearchDetail.setVisibility(View.VISIBLE);
            binding.btnExpandMore.setImageResource(R.drawable.outline_expand_less_black_36);
         } else {
            isShow = false;
            binding.llSearchDetail.setVisibility(View.GONE);
            binding.btnExpandMore.setImageResource(R.drawable.outline_expand_more_black_36);
         }
      });

      return binding.getRoot();
   }

   private void getPositionList(Integer page, String description, String location, Boolean fullTime) {
      if (isFirst) {
         binding.progressBar.setVisibility(View.VISIBLE);
      } else {
         binding.progressBarBottom.setVisibility(View.VISIBLE);
      }
      BaseRetrofit.getRetrofitServer().create(RetrofitInterface.class).getJobList(page, description, location, fullTime).enqueue(new Callback<List<PositionModel>>() {
         @Override
         public void onResponse(Call<List<PositionModel>> call, Response<List<PositionModel>> response) {
            if (response.body() != null) {
               if (response.isSuccessful() && response.code() == 200) {
                  List<PositionModel> listResponse = response.body();
                  for (PositionModel position : listResponse) {
                     adapter.addItem(position);
                  }
               }
            }
            if (isFirst) {
               binding.progressBar.setVisibility(View.GONE);
               isFirst = false;
            } else {
               binding.progressBarBottom.setVisibility(View.GONE);
            }
         }

         @Override
         public void onFailure(Call<List<PositionModel>> call, Throwable t) {
            GlobalFunction.showToast(requireContext(), t.getLocalizedMessage());
            if (isFirst) {
               binding.progressBar.setVisibility(View.GONE);
            } else {
               binding.progressBarBottom.setVisibility(View.GONE);
            }
         }
      });
   }
}