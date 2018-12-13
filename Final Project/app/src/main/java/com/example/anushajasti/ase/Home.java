package com.example.anushajasti.ase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Home extends AppCompatActivity {
    Button cam;
    float max;
    int pos;
    Bitmap image;
    String imagepath;
    EditText searchterm;
    JSONArray items, ebayitem;
    ListView list, ebaylist;
    Single<ClassifiedImages> observable;
    public String productname = "";
    ImageView imageView, ebayimageView;
    Button profile, reviewlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        cam = (Button) findViewById(R.id.cam);

        searchterm = (EditText) findViewById(R.id.searchterm);
        /*profile = (Button) findViewById(R.id.profile);*/
        ebaylist = (ListView) findViewById(R.id.ebaylist);
        ebaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject ebayitem2=null;
                try {
                    ebayitem2 = (JSONObject) ebayitem.get(position);
                    JSONArray galleryurl = ebayitem2.getJSONArray("galleryURL");
                    String img = galleryurl.get(0).toString();
                    JSONArray viewItemURL = ebayitem2.getJSONArray("viewItemURL");
                    String url = viewItemURL.get(0).toString();
                    Log.d("output", img);
                    JSONArray title = ebayitem2.getJSONArray("title");
                    String name1 = title.get(0).toString();
                    Log.d("output", name1);
                    JSONArray sellingStatus = ebayitem2.getJSONArray("sellingStatus");
                    JSONObject sellingStatusbody = new JSONObject(sellingStatus.get(0).toString());
                    JSONArray currentprice = sellingStatusbody.getJSONArray("currentPrice");
                    JSONObject currentpricebody = new JSONObject(currentprice.get(0).toString());
                    String price = currentpricebody.getString("__value__");
                    Log.d("output", price);
                    Intent redirect = new Intent(Home.this, SeleteItem.class);
                    redirect.putExtra("name", name1);
                    redirect.putExtra("desc", name1);
                    redirect.putExtra("price", price);
                    redirect.putExtra("img", img);
                    redirect.putExtra("url", url);
                    startActivity(redirect);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               // Toast.makeText(getBaseContext(),"Ebay",Toast.LENGTH_SHORT).show();
            }
        });
        /*reviewlist = (Button) findViewById(R.id.review);*/
        BottomNavigationView bottomNav = findViewById(R.id.btmNav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

    /*    reviewlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productname.isEmpty()) {
                    productname = searchterm.getText().toString();
                }
                Intent reviewslist = new Intent(Home.this, ProductReviews.class);
                reviewslist.putExtra("productname", productname);
                Log.d("name", searchterm.getText().toString());
                startActivity(reviewslist);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userprofile = new Intent(Home.this, UserProfile.class);
                startActivity(userprofile);
            }
        });*/
        observable = Single.create((SingleOnSubscribe<ClassifiedImages>) emitter -> {
            InputStream imageStream = null;
            try {
                imageStream = new FileInputStream(imagepath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream finalImageStream = imageStream;
            IamOptions options = new IamOptions.Builder()
                    .apiKey("WhZHP3W1OLDr-IlXmyb1Hg9nv3vbKrpALqUW3jowJUUX")
                    .build();

            VisualRecognition visualRecognition = new VisualRecognition("2018-03-19", options);
            ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                    .imagesFile(finalImageStream)
                    .imagesFilename("fruitbowl.jpg")
                    .classifierIds(Collections.singletonList("default"))
                    .threshold((float) 0.6)
                    .owners(Collections.singletonList("me"))
                    .build();
            ClassifiedImages classifiedImages = visualRecognition.classify(classifyOptions).execute();
            Log.d("tag", "images classified");
            emitter.onSuccess(classifiedImages);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Intent redirect = null;
            switch (menuItem.getItemId()){
                case R.id.navhome:
                    break;
                case R.id.navreview:
                    if (productname.isEmpty()) {
                        productname = searchterm.getText().toString();
                        if(!productname.isEmpty()){
                            Intent reviewslist = new Intent(Home.this, ProductReviews.class);
                            reviewslist.putExtra("productname", productname);
                            Log.d("name", searchterm.getText().toString());
                            startActivity(reviewslist);
                        }else{
                            Toast.makeText(getBaseContext(),"Please search product.",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Intent reviewslist = new Intent(Home.this, ProductReviews.class);
                        reviewslist.putExtra("productname", productname);
                        Log.d("name", searchterm.getText().toString());
                        startActivity(reviewslist);
                    }

                    break;
                case R.id.navuserprofile:
                    redirect = new Intent(Home.this, UserProfile.class);
                    startActivity(redirect);
                    break;
                case R.id.navlogout:
                    FirebaseAuth.getInstance().signOut();
                    redirect = new Intent(Home.this, MainActivity.class);
                    startActivity(redirect);
                    break;
            }
            return true;
        }
    };
    public void getResult() {
        observable.subscribe(new SingleObserver<ClassifiedImages>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(ClassifiedImages classifiedImages) {
                // System.out.println(classifiedImages.toString());
                //Log.d("result",classifiedImages.toString());
                String res = classifiedImages.toString();
                //parse json objects
                Log.d("output", res);

                try {
                    JSONObject reader = new JSONObject(res);

                    JSONArray images = reader.getJSONArray("images");

                    JSONObject classifier = new JSONObject(images.get(0).toString());
                    JSONArray classifiers1 = classifier.getJSONArray("classifiers");
                    JSONObject classes = new JSONObject(classifiers1.get(0).toString());

                    JSONArray x = classes.getJSONArray("classes");

                /*JSONObject y=new JSONObject(x.get(0).toString());

                    float classname1=Float.parseFloat(y.getString("score"));
                max=classname1;

                for(int i=0;i<x.length();i++){
                    JSONObject val=new JSONObject(x.get(i).toString());
                    float score=Float.parseFloat(val.getString("score"));
                    if(max<=score){
                        max=score;
                        pos=i;
                    }
                }*/
                    JSONObject finalcal = new JSONObject(x.get(0).toString());

                    String classname = finalcal.getString("class");
                    Log.d("x", classname.toString());
                    //Log.d("y",classname1.toString());

                    //                  for (int i=0 ; i<x.length;i++){
                    //                      if(y<x[i]){
                    //                          y=x[i];
                    //                  }
                    //                  }
                    productname = classname;
                    WalmartmainSearch(classname);
                    EbaymainSearch(classname);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public void opencam(View v) {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, 100);

    }

    public void WalmartmainSearch(String searchterm) {
        String url = "http://api.walmartlabs.com/v1/search?apiKey=bwwkxupm3s275sdcnb42bekg&query=" + searchterm;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final JSONObject jsonResult;
                final String result = response.body().string();
                try {
                    Log.d("out", result);
                    JSONObject res = new JSONObject(result);
                    items = res.getJSONArray("items");
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            list = (ListView) findViewById(R.id.list);
                            CustomAdapter customadapter = new CustomAdapter();
                            list.setAdapter(customadapter); // Stuff that updates the UI
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    JSONObject item = null;
                                    try {
                                        item = (JSONObject) items.get(position);
                                        String img = item.getString("mediumImage");
                                        Log.d("output", img);
                                        String name1 = item.getString("name");
                                        Log.d("output", name1);
                                        String desc = item.getString("shortDescription");
                                        Log.d("output", desc);
                                        String price = item.getString("salePrice");
                                        Log.d("output", price);
                                        String url = item.getString("productUrl");
                                        Log.d("output", url);
                                        Intent redirect = new Intent(Home.this, SeleteItem.class);
                                        redirect.putExtra("name", name1);
                                        redirect.putExtra("desc", desc);
                                        redirect.putExtra("price", price);
                                        redirect.putExtra("img", img);
                                        redirect.putExtra("url", url);
                                        startActivity(redirect);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                   // Toast.makeText(getBaseContext(),"Walmart",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("error", e.toString());
                }
            }
        });
    }

    public void EbaymainSearch(String searchterm) {
        String url = "https://svcs.ebay.com/services/search/FindingService/v1?SECURITY-APPNAME=AnushaJa-Shopping-PRD-3f8f66498-05ef6018&OPERATION-NAME=findItemsByKeywords&SERVICE-VERSION=1.0.0&RESPONSE-DATA-FORMAT=JSON&callback=_cb_findItemsByKeywords&REST-PAYLOAD&keywords=" + searchterm + "&paginationInput.entriesPerPage=6&GLOBAL-ID=EBAY-US&siteid=0";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final JSONObject jsonResult;
                final String result = response.body().string();
                final String result1 = result.substring(28);
                System.out.println(result1);
                try {
                    JSONObject res = new JSONObject(result1);
                    JSONArray finditemsbyid = res.getJSONArray("findItemsByKeywordsResponse");
                    JSONObject responsebody = new JSONObject(finditemsbyid.get(0).toString());
                    JSONArray searchresult = responsebody.getJSONArray("searchResult");
                    JSONObject searchresultbody = new JSONObject(searchresult.get(0).toString());
                    ebayitem = searchresultbody.getJSONArray("item");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CustomAdapter2 ca2 = new CustomAdapter2();
                            ebaylist.setAdapter(ca2);
                        }
                    });
                    //JSONObject res1 = res.getJSONObject("_cb_findItemsByKeywords");
                    // System.out.println(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*try {
                    Log.d("out",result);
                    JSONObject res=new JSONObject(result);
                    items=res.getJSONArray("items");
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            list = (ListView)findViewById(R.id.list);
                            CustomAdapter customadapter= new CustomAdapter();
                            list.setAdapter(customadapter); // Stuff that updates the UI

                        }
                    });

                }
                catch (Exception e) {
                    e.printStackTrace();
                    Log.e("error",e.toString());
                }*/
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100) {
            image = (Bitmap) data.getExtras().get("data");
            imagepath = saveimage();
            getResult();
        }
    }

    public String saveimage() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "sample" + timeStamp + ".jpg";
        File file = new File(getFilesDir(), filename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    class CustomAdapter2 extends BaseAdapter {

        @Override
        public int getCount() {
            return ebayitem.length();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.customlayout, null);
            ebayimageView = (ImageView) convertView.findViewById(R.id.imageView);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView desc = (TextView) convertView.findViewById(R.id.Description);
            try {
                JSONObject ebayitem2 = (JSONObject) ebayitem.get(position);
                JSONArray galleryurl = ebayitem2.getJSONArray("galleryURL");
                String img = galleryurl.get(0).toString();
                Log.d("output", img);
                JSONArray title = ebayitem2.getJSONArray("title");
                String name1 = title.get(0).toString();
                Log.d("output", name1);
                JSONArray sellingStatus = ebayitem2.getJSONArray("sellingStatus");
                JSONObject sellingStatusbody = new JSONObject(sellingStatus.get(0).toString());
                JSONArray currentprice = sellingStatusbody.getJSONArray("currentPrice");
                JSONObject currentpricebody = new JSONObject(currentprice.get(0).toString());
                String price = currentpricebody.getString("__value__");
                Log.d("output", price);
                loadImageFromUrl(img, ebayimageView);
                name.setText(name1);
                desc.setText("$"+price);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }

    class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return items.length();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.customlayout, null);
            imageView = (ImageView) convertView.findViewById(R.id.imageView);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView desc = (TextView) convertView.findViewById(R.id.Description);
            try {
                JSONObject item = (JSONObject) items.get(position);
                String img = item.getString("thumbnailImage");
                Log.d("output", img);
                String name1 = item.getString("name");
                Log.d("output", name1);
                String price = item.getString("salePrice");
                Log.d("output", price);
                loadImageFromUrl(img, imageView);
                name.setText(name1);
                desc.setText("$"+price);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return convertView;
        }


    }

    private void loadImageFromUrl(String img, ImageView image) {
        Picasso.with(Home.this).load(img).placeholder(R.mipmap.ic_launcher)
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

    public void search(View v) {
        productname = searchterm.getText().toString();
        WalmartmainSearch(searchterm.getText().toString());
        EbaymainSearch(searchterm.getText().toString());
    }
}
