package com.example.labfirebase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DetailActivity extends AppCompatActivity implements OnCompleteListener<QuerySnapshot> {



    String productName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        this.productName = intent.getStringExtra("name");
        this.ReadDataFireStore();
    }

    public void ReadDataFireStore(){


        FirebaseFirestore fireStoreDataBase = FirebaseFirestore.getInstance();
        fireStoreDataBase.collection("Products").get().addOnCompleteListener(this);


    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {


        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {

                Map<String,Object> product = document.getData();
                String productName = (String)product.get("name");

                if(productName.equals(this.productName)){

                    String price = (String)product.get("price");
                    String description = (String) product.get("description");
                    String image = (String) product.get("image");

                    TextView textViewName = findViewById(R.id.textViewNameValue);
                    textViewName.setText(this.productName);

                    TextView textViewPrice = findViewById(R.id.textViewPriceValue);
                    textViewPrice.setText(price);

                    TextView textViewDescription = findViewById(R.id.textViewDescriptionValue);
                    textViewDescription.setText(description);

                    ImageView imageViewImage = findViewById(R.id.imageViewDetailPhoto);
                    ImageDownloader imageDownloader = new ImageDownloader();
                    try {
                        Bitmap bitmap =  imageDownloader.execute(image).get();
                        imageViewImage.setImageBitmap(bitmap);
                        Toast.makeText(getApplicationContext(),"link: " + image,Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),"Se descargo la imagen",Toast.LENGTH_LONG).show();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    break;
                }

            }
        } else {
            //Log.w(TAG, "Error getting documents.", task.getException());
        }

    }

    public class ImageDownloader extends AsyncTask<String,Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {

            try {

                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }


    }

}
