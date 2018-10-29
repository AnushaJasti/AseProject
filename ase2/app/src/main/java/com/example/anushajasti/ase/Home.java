package com.example.anushajasti.ase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

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
    Bitmap image;
    String imagepath;

    Single<ClassifiedImages> observable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        cam=(Button) findViewById(R.id.cam);
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
            Log.d("tag","images classified");
            emitter.onSuccess(classifiedImages);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public void getResult() {
        observable.subscribe(new SingleObserver<ClassifiedImages>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(ClassifiedImages classifiedImages) {
                // System.out.println(classifiedImages.toString());
                //Log.d("result",classifiedImages.toString());
                String res=classifiedImages.toString();
                //parse json objects
                Log.d("output",res);

                try {
                    JSONObject  reader = new JSONObject(res);
                    
                JSONArray images = reader.getJSONArray("images");

                JSONObject classifier = new JSONObject(images.get(0).toString());
                JSONArray classifiers1 = classifier.getJSONArray("classifiers");
                JSONObject classes= new JSONObject(classifiers1.get(0).toString());

                JSONArray x=classes.getJSONArray("classes");

                JSONObject y=new JSONObject(x.get(0).toString());

                String classname=y.getString("class");
                Log.d("x",classname.toString());

                    String url= "http://api.walmartlabs.com/v1/search?apiKey=bwwkxupm3s275sdcnb42bekg&query="+classname;
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
                                Log.d("out",result);

                                }
                                catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

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
    public void opencam(View v){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, 100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if(requestCode==100){
        image=(Bitmap)data.getExtras().get("data");
        imagepath=saveimage();
        getResult();
    }
    }
    public String saveimage(){
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

}
