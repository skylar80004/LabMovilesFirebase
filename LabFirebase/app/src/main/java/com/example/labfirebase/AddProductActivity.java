package com.example.labfirebase;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity implements OnSuccessListener<UploadTask.TaskSnapshot>, OnFailureListener {


    private int GALLERY_REQUEST = 100;
    private StorageReference mStorageRef;
    private String imageURL = "";
    private Uri imageUri = null;
    private String name , price, description;
    private StorageReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        this.name = "";
        this.price = "";
        this.description = "";
        this.mStorageRef = FirebaseStorage.getInstance().getReference();
    }


    public void SendDataFireStore(String name, String price, String description, String image){

        FirebaseFirestore fireStoreDataBase = FirebaseFirestore.getInstance();

        Map<String,String> user = new HashMap<>();
        user.put("name", name);
        user.put("price", price);
        user.put("description", description);
        user.put("image", image);

        fireStoreDataBase.collection("Products")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast toast = Toast.makeText(getApplicationContext(), "El producto fue agregado", Toast.LENGTH_LONG);
                        toast.show();
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


    public void OnClickButtonAddProduct(View view){

        EditText editTextName, editTextPrice, editTextDescription;
        editTextName = findViewById(R.id.editTextNameAdd);
        editTextPrice = findViewById(R.id.editTextPriceAdd);
        editTextDescription = findViewById(R.id.editTextDescriptionAdd);

        String name = editTextName.getText().toString();
        String price = editTextPrice.getText().toString();
        String description = editTextDescription.getText().toString();

        this.name = name;
        this.price = price;
        this.description = description;


        if(this.imageUri != null) {

            this.ref = mStorageRef.child(this.imageUri.toString());
            ref.putFile(this.imageUri)
                    .addOnSuccessListener(this)
                    .addOnFailureListener(this);

        }
        else{

            this.SendDataFireStore(name,price, description,this.imageURL);

        }







    }

    public void OnClickButtonAddPhoto(View view){


        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == this.GALLERY_REQUEST){
                Uri selectedImage = data.getData();
                this.imageUri = selectedImage;

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    ImageView imageView = findViewById(R.id.imageViewProductAdd);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    //Log.i("TAG", "Some exception " + e);
                }

            }


        }

    }

    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



        // Get a URL to the uploaded content
        Toast toast = Toast.makeText(getApplicationContext(), "Se subio a storage", Toast.LENGTH_LONG);
        toast.show();

        this.ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // getting image uri and converting into string
                Uri downloadUrl = uri;
                imageURL = downloadUrl.toString();
                SendDataFireStore(name,price, description,imageURL);
            }
        });

    }

    @Override
    public void onFailure(@NonNull Exception e) {

        this.SendDataFireStore(name,price, description,this.imageURL);

    }
}
