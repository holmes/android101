package com.example.android101.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.android101.R;
import com.example.android101.data.MockData;
import com.example.android101.data.model.User;
import com.example.android101.util.BindableAdapter;
import com.example.android101.util.SquaredImageView;
import com.squareup.picasso.Picasso;

public class MerchantAdapter extends BindableAdapter<User> {
  public MerchantAdapter(DirectoryActivity directoryActivity) {
    super(directoryActivity);
  }

  @Override public int getCount() {
    return MockData.MERCHANTS.length;
  }

  @Override public User getItem(int position) {
    return MockData.MERCHANTS[position];
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View newView(LayoutInflater inflater, int position, ViewGroup container) {
    // Inflate the layout for a single merchant for use when a new view is needed.
    return inflater.inflate(R.layout.directory_merchant, container, false);
  }

  @Override public void bindView(View view, int position, User user) {
    // Locate the image view and the text view inside of the layout.
    SquaredImageView image = (SquaredImageView) view.findViewById(R.id.merchant_image);
    TextView name = (TextView) view.findViewById(R.id.merchant_name);

    // Put the merchant name into the TextView for the name.
    name.setText(user.name);

    // Picasso handles downloading in the background, cancelling images that disappear off of the
    // screen, caching on disk and in memory, and even transforming the image.

    Picasso.with(getContext())
        .load(user.getCuratedUri()) // Get the largest curated image url for the merchant
        .resize(300, 300) // Resize it to a reasonable (and square) size
        .centerCrop() // Fills the 300x300 size by cropping the larger dimension of the image
        .placeholder(R.drawable.app_icon) // Default image to display while the url is downloading
        .into(image); // The destination View where the image should be displayed
  }
}
