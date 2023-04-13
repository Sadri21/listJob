package com.app.joblist.view.item;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.joblist.R;
import com.app.joblist.databinding.FragmentAccountBinding;
import com.app.joblist.utilities.GlobalFunction;
import com.app.joblist.view.LoginActivity;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountFragment extends Fragment {

   FragmentAccountBinding binding;
   String loginType;
   GoogleSignInClient gsc;

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      // Inflate the layout for this fragment
      binding = FragmentAccountBinding.inflate(inflater, container, false);
      String type = GlobalFunction.readFromSharedPreferences(requireContext(), "login-type");
      if (type.equals("google")) {
         gsc = GlobalFunction.getGoogleClient(requireContext());
         GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(requireContext());
         if (acct != null) {
            binding.lblName.setText(acct.getDisplayName());
            binding.lblEmail.setText(acct.getEmail());
            Log.e("TAG", "onCreateView: " );
            Picasso.get().load(acct.getPhotoUrl()).placeholder(R.drawable.profile_holder).into(binding.imgHolder);
         }
         binding.btnSignOut.setOnClickListener(v -> googleSignOut());
      } else {
         AccessToken accessToken = AccessToken.getCurrentAccessToken();
         GraphRequest request = GraphRequest.newMeRequest(
                 accessToken,
                 (object, response) -> {
                    try {
                       String name = object.getString("name");

                       String urlImage = "http://graph.facebook.com/" + object.getString("id") +"/picture";
                       Log.e("TAG", "onCreateView: " + urlImage );
                       binding.lblName.setText(name);
                       binding.lblEmail.setText("");
                       Picasso.get().load(urlImage).placeholder(R.drawable.profile_holder).into(binding.imgHolder);
                    } catch (JSONException e) {
                       throw new RuntimeException(e);
                    }
                    // Application code
                 });
         Bundle parameters = new Bundle();
         parameters.putString("fields", "id,name,link");
         request.setParameters(parameters);
         request.executeAsync();

         binding.btnSignOut.setOnClickListener(v -> {
            LoginManager.getInstance().logOut();
            gotoLogin();
         });
      }
      return binding.getRoot();
   }

   private void googleSignOut() {
      gsc.signOut().addOnCompleteListener(task -> {
         gotoLogin();
      });
   }

   private void gotoLogin() {
      GlobalFunction.writeToSharedPreferences(requireContext(), "login-type", "");
      Intent intent = new Intent(requireContext(), LoginActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      requireContext().startActivity(intent);
   }
}