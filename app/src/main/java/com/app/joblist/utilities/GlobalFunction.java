package com.app.joblist.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class GlobalFunction {
   public static void showToast(Context context, String message) {
      Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
   }

   public static void writeToSharedPreferences(Context context, String key, String value) {
      SharedPreferences.Editor editor = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE).edit();
      editor.putString(key, value);
      editor.apply();
   }

   public static String readFromSharedPreferences(Context context, String key) {
      SharedPreferences prefs = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE);
      return prefs.getString(key, "");
   }

   public static GoogleSignInClient getGoogleClient(Context context) {
      GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
      return GoogleSignIn.getClient(context, gso);

   }

}
