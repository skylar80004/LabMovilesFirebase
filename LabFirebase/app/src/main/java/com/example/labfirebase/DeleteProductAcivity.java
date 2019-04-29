package com.example.labfirebase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DeleteProductAcivity extends AppCompatActivity implements OnCompleteListener<QuerySnapshot>, AdapterView.OnItemClickListener {



    private Map<String,String> documentsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product_acivity);

        this.documentsMap = new HashMap<>();
        ListView listViewProductos = findViewById(R.id.listViewProductsDelete);
        listViewProductos.setOnItemClickListener(this);
        this.ReadDataFireStore();
    }

    public void ReadDataFireStore(){

        FirebaseFirestore fireStoreDataBase = FirebaseFirestore.getInstance();
        fireStoreDataBase.collection("Products").get().addOnCompleteListener(this);

    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {

        ListView listView = findViewById(R.id.listViewProductsDelete);
        final ArrayList<String> products = new ArrayList<String>();


        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {

                Map<String,Object> product = document.getData();
                String productName = (String)product.get("name");
                products.add(productName);
                this.documentsMap.put(productName, document.getId());

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String productName = (String)parent.getItemAtPosition(position);
        String docID = this.documentsMap.get(productName);

        FirebaseFirestore fireStoreDataBase = FirebaseFirestore.getInstance();

        fireStoreDataBase.collection("Products").document(docID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        ReadDataFireStore();
                        Toast toast = Toast.makeText(getApplicationContext(), "Se elimino el producto", Toast.LENGTH_LONG);
                        toast.show();
                        //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       // Log.w(TAG, "Error deleting document", e);
                    }
                });


    }
}
