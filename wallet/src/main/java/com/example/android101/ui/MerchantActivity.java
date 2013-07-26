package com.example.android101.ui;

import android.os.Bundle;
import android.widget.TextView;
import com.example.android101.R;
import com.example.android101.WalletActivity;
import com.example.android101.data.model.User;
import com.example.android101.util.SquaredImageView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;

public class MerchantActivity extends WalletActivity {

  // Dagger automatically sets these values. Injection is performed automatically in WalletActivity.
  @Inject Gson gson;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (isFinishing()) return;

    // Pull out the user from the intent. The calling activity is responsible for adding it.
    String userString = getIntent().getStringExtra("user");
    // Use Gson to convert it from a JSON string back into an object we can work with.
    User user = gson.fromJson(userString, User.class);

    // Inflate the layout XML
    setContentView(R.layout.merchant_activity);

    // Find the relevant views for binding the user data.
    SquaredImageView image =
        (SquaredImageView) findViewById(R.id.merchant_image);
    TextView name = (TextView) findViewById(R.id.merchant_name);
    TextView bio = (TextView) findViewById(R.id.merchant_bio);

    // Make sure our action bar title is the merchant name.
    setTitle(user.name);

    name.setText(user.name);
    bio.setText("Location: " + user.location.latitude + ", " + user.location.longitude);

    Picasso.with(this)
        .load(user.getCuratedUri())
        .placeholder(R.drawable.app_icon)
        .resize(400, 400)
        .centerCrop()
        .into(image);
  }
}
