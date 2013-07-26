package com.example.android101.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.example.android101.R;
import com.example.android101.WalletActivity;
import com.example.android101.data.WalletService;
import com.example.android101.data.model.User;
import com.example.android101.data.response.DirectoryMerchantResponse;
import com.google.gson.Gson;
import java.util.List;
import javax.inject.Inject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Activities are high-level screens in an app. Similar to pages on a website. Content
 * inside an activity can change based on some state or user interaction. Large changes
 * are easier by just moving to a new activity.
 *
 * This activity is responsible for display the contents of the directory. We'll present it
 * as a large grid of images in the hopes of dazzling and delighting users--er--customers into
 * forking over their cash.
 */
public class DirectoryActivity extends WalletActivity {

  // Dagger automatically sets these values. Injection is performed automatically in WalletActivity.
  @Inject Gson gson;
  @Inject WalletService service;

  /**
   * Android has a managed lifecycle. The operating system creates and destroys things as the user
   * moves throughout the app. We get callbacks at various states. "onCreate" is the first.
   */
  @Override protected void onCreate(Bundle savedInstanceState) {
    // Call up the base class. This is where we centralize things like dependency injection and
    // checking for a user. Once this is done (and I've done it all for you) it's rarely touched.
    super.onCreate(savedInstanceState);
    // The base class, WalletActivity, might have called finish() if the user was logged out. We
    // need to check that here and bail because they are being shuffled to the login screen.
    if (isFinishing()) return;

    // Inflate the layout for the directory.
    setContentView(R.layout.directory_activity);

    // Find the GridView we created which is going to hold the merchants images.
    GridView merchants = (GridView) findViewById(R.id.merchants);
    // Give it an adapter which binds the mock data to the individual merchant view. This is stored
    // in a local variable that's final so that we can access it in the click listener and Retrofit
    // callback down below.
    final MerchantAdapter adapter = new MerchantAdapter(this);
    merchants.setAdapter(adapter);


    // Call the server for a list of up to 30 merchants. Retrofit will automatically perform this
    // on a background thread and
    service.listMerchants(30, new Callback<DirectoryMerchantResponse>() {
      @Override
      public void success(DirectoryMerchantResponse directoryMerchantResponse, Response response) {
        List<User> users = directoryMerchantResponse.entities;
        // Update the adapter using the user list from the response object.
        adapter.setUsers(users);
        // Tell the adapter to notify any views that they need to refresh. This will cause our
        // GridView to re-query the adapter and display the new data.
        adapter.notifyDataSetChanged();
      }

      @Override public void failure(RetrofitError retrofitError) {
        // This will never happen! Right? Right...
      }
    });

    // The click listener allows us to respond to the user clicking on a merchant in the grid.
    merchants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        // Get the user at the position that was clicked.
        User user = adapter.getItem(position);
        // Using Gson, turn that user object into a JSON string.
        String userString = gson.toJson(user);

        // Create an intent from the current activity to MerchantActivity. Intents are how actions
        // are conveyed in Android both between applications and inside of an application. Here
        // Android will create a MerchantActivity in response to this intent.
        Intent intent = new Intent(DirectoryActivity.this, MerchantActivity.class);
        // Pass along our selected user in JSON string form as an extra. We'll read this on the
        // other side.
        intent.putExtra("user", userString);

        // And go!
        startActivity(intent);
      }
    });
  }
}
