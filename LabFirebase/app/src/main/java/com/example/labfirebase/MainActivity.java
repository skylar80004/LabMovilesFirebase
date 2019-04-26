package com.example.labfirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.TestList();
        ListView listViewProductos = findViewById(R.id.listiViewProducts);
        listViewProductos.setOnItemClickListener(this);
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        String productName = (String)parent.getItemAtPosition(position);

        Toast toast = Toast.makeText(this,productName,Toast.LENGTH_LONG);
        toast.show();


        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra("name",productName);
        startActivity(intent);

    }


}
