package com.app.joblist.view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.joblist.databinding.ActivityLoginBinding;
import com.app.joblist.utilities.GlobalFunction;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

   private ActivityLoginBinding binding;
   GoogleSignInClient gsc;
   ActivityResultLauncher<Intent> googleResult;
   CallbackManager callbackManager;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
      binding = ActivityLoginBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());
      getSupportActionBar().hide();
      registerLaunch();

      String type = "";
      type = GlobalFunction.readFromSharedPreferences(this, "login-type");
      if (!type.equals("")) {
         gotoDashboard();
      }
      gsc = GlobalFunction.getGoogleClient(this);
      callbackManager = CallbackManager.Factory.create();

      LoginManager.getInstance().registerCallback(callbackManager,
              new FacebookCallback<LoginResult>() {
                 @Override
                 public void onSuccess(LoginResult loginResult) {
                    // App code
                    GlobalFunction.writeToSharedPreferences(LoginActivity.this, "login-type", "facebook");
                    gotoDashboard();
                    Log.e("daw", "onSuccess: sucess");
                 }

                 @Override
                 public void onCancel() {
                    // App code
                 }

                 @Override
                 public void onError(FacebookException exception) {
                    // App code
                    Log.e("daw", "onSuccess: " + exception.getLocalizedMessage());
                 }
              });

      binding.googleSignIn.setOnClickListener(v -> {
         googleSignIn();
      });
      binding.btnFacebookSign.setOnClickListener(v -> {
         LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
      });
   }

   private void registerLaunch() {
     googleResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
         if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GlobalFunction.writeToSharedPreferences(this, "login-type", "google");
            try {
               task.getResult(ApiException.class);
            } catch (ApiException e) {
               throw new RuntimeException(e);
            }
            gotoDashboard();
         }
      });
   }

   private void googleSignIn() {
      Intent signInIntent = gsc.getSignInIntent();
      googleResult.launch(signInIntent);
   }

   private void gotoDashboard() {
      Intent intent = new Intent(this, DashboardActivity.class);
      startActivity(intent);
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      callbackManager.onActivityResult(requestCode, resultCode, data);
      super.onActivityResult(requestCode, resultCode, data);
   }
}