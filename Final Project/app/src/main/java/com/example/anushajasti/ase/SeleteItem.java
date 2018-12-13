package com.example.anushajasti.ase;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class SeleteItem extends AppCompatActivity {
    String name = "";
    String desc = "";
    String price = "";
    String img = "";
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selete_item);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        desc = intent.getStringExtra("desc");
        price = intent.getStringExtra("price");
        img = intent.getStringExtra("img");
        url = intent.getStringExtra("url");
        ImageView imgView = (ImageView) findViewById(R.id.img);

        TextView lblName = findViewById(R.id.lblTitle);
        lblName.setText(name);
        TextView lblDesc = findViewById(R.id.lblDesc);
        lblDesc.setText(desc);
        TextView lblPrice = findViewById(R.id.lblPrice);
        lblPrice.setText("$"+price);
        loadImageFromUrl(img, imgView);
    }

    public void openProduct(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private void loadImageFromUrl(String img, ImageView image) {
        Picasso.with(SeleteItem.this).load(img).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(image, new com.squareup.picasso.Callback() {

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }
}
