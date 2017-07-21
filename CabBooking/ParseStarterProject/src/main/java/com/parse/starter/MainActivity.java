/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class MainActivity extends AppCompatActivity {

  Switch aSwitch;

    public void redirectAs(){

        if(ParseUser.getCurrentUser().get("riderordriver").equals("rider")){
            Intent intent = new Intent(MainActivity.this,MapsActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(MainActivity.this,ViewRequestActivity.class);
            startActivity(intent);
        }
    }

  public void getStarted(View view){
    aSwitch = (Switch)findViewById(R.id.switch1);

    Log.i("Switch Value ", String.valueOf(aSwitch.isChecked()));

      String userType = "rider";
      if(aSwitch.isChecked()){
          userType = "driver";
      }
      ParseUser.getCurrentUser().put("riderordriver",userType);
      ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
          @Override
          public void done(ParseException e) {
              if(e == null){
                  redirectAs();
                  Log.i("Info","Redirecting to maps ");

              }
          }
      });
      Log.i("Info","Redirecting as "+userType);

  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getSupportActionBar().hide();


    if (ParseUser.getCurrentUser() == null){
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null){
                    Log.i("Info","Anonymous Login Successful");
                }else{
                    Log.i("Info","Anonymous Login Failed");
                    e.printStackTrace();
                }
            }
        });
    }
      else{
        if(ParseUser.getCurrentUser().get("riderordriver")!=null){
            Log.i("Info","Redirecting as :"+ParseUser.getCurrentUser().get("riderordriver"));
            redirectAs();
            Log.i("Info","Redirecting to maps ");
            Toast.makeText(getApplicationContext(),"Redirecting",Toast.LENGTH_SHORT).show();
        }

    }

    
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}