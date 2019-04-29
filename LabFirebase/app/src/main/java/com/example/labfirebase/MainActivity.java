package com.example.labfirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, OnCompleteListener<QuerySnapshot> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.ReadDataFireStore();
        ListView listViewProductos = findViewById(R.id.listiViewProducts);
        listViewProductos.setOnItemClickListener(this);


    }




    public void ReadDataFireStore(){

        FirebaseFirestore fireStoreDataBase = FirebaseFirestore.getInstance();
        fireStoreDataBase.collection("Products").get().addOnCompleteListener(this);

    }


    public void FireStoreTest(){

        FirebaseFirestore fireStoreDataBase = FirebaseFirestore.getInstance();

        Map<String,String> user = new HashMap<>();
        user.put("name", "Ches");
        user.put("price", "1 million");
        user.put("description", "She is perfect");
        user.put("image:", "hola.com");

        fireStoreDataBase.collection("Products")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void TestList(){

        ListView listView = findViewById(R.id.listiViewProducts);
        final ArrayList<String> elements = new ArrayList<String>();
        elements.add("McDonalds");
        elements.add("Il Volpino");
        elements.add("Dominos Pizza");
        elements.add("Monster Pizza");
        elements.add("The Draft");
        elements.add("Burger King");
        elements.add("Chante Bike Club");
        elements.add("Ches Toronjas");

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1
                        ,elements);
        listView.setAdapter(itemsAdapter);
    }

    public void OnButtonAddProductClick(View view){

        Intent intent = new Intent(this, AddProductActivity.class);
        startActivity(intent);
    }

    public void OnButtonDeleteProductClick(View view){
        Intent intent = new Intent(this, DeleteProductAcivity.class);
        startActivity(intent);
    }

    public void OnButtonReloadProductsClick(View view){

        this.ReadDataFireStore();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        String productName = (String)parent.getItemAtPosition(position);

        Toast toast = Toast.makeText(this,productName,Toast.LENGTH_LONG);
        toast.show();

        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra("name",productName);
        startActivity(intent);

    }


    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {

        ListView listView = findViewById(R.id.listiViewProducts);
        final ArrayList<String> products = new ArrayList<String>();


        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {

                Map<String,Object> product = document.getData();
                String productName = (String)product.get("name");
                products.add(productName);

                //Log.d("CHES", productName);
                //Log.d("CHES", document.getId() + " => " + document.getData());


            }
        } else {
            //Log.w(TAG, "Error getting documents.", task.getException());
        }


        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1
                        ,products);
        listView.setAdapter(itemsAdapter);



    }
}
