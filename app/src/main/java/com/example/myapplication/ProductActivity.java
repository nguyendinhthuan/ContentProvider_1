package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    private EditText edt_ID, edt_Name, edt_Unit, edt_Madein;
    private Button btn_Exit, btn_Save, btn_Select, btn_Delete, btn_Update;
    private GridView gridView;
    static final String uri = "content://com.example.myapplication";

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);


        AnhXa();
        Save();
        Select();
    }

    public void AnhXa() {
        edt_ID = (EditText) findViewById(R.id.edt_ID);
        edt_Name = (EditText) findViewById(R.id.edt_Name);
        edt_Unit = (EditText) findViewById(R.id.edt_Unit);
        edt_Madein = (EditText) findViewById(R.id.edt_Madein);

        btn_Exit = (Button) findViewById(R.id.btn_Exit);
        btn_Save = (Button) findViewById(R.id.btn_Save);
        btn_Select = (Button) findViewById(R.id.btn_Select);
        btn_Delete = (Button) findViewById(R.id.btn_Delete);
        btn_Update = (Button) findViewById(R.id.bt_Update);

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int id = (Math.round(i/4) * 4);
                edt_ID.setText(adapterView.getItemAtPosition(id).toString());
                edt_Name.setText(adapterView.getItemAtPosition(id+1).toString());
                edt_Unit.setText(adapterView.getItemAtPosition(id+2).toString());
                edt_Madein.setText(adapterView.getItemAtPosition(id+3).toString());
            }
        });

        dbHelper = new DBHelper(this);
    }

    public void Save() {
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("id", edt_ID.getText().toString());
                contentValues.put("name", edt_Name.getText().toString());
                contentValues.put("unit", edt_Unit.getText().toString());
                contentValues.put("madein", edt_Madein.getText().toString());

                Uri product = Uri.parse(uri);
                Uri insert_uri = getContentResolver().insert(product, contentValues);
                Toast.makeText(ProductActivity.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Select() {
        btn_Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> list_string = new ArrayList<>();
                Uri product = Uri.parse(uri);
                Cursor cursor = getContentResolver().query(product, null, null, null, "name");
                if (cursor != null) {
                    cursor.moveToFirst();
                    do {
                        list_string.add(cursor.getInt(0) + "");
                        list_string.add(cursor.getString(1));
                        list_string.add(cursor.getString(2));
                        list_string.add(cursor.getString(3));
                    } while (cursor.moveToNext());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProductActivity.this, android.R.layout.simple_list_item_1, list_string);
                    gridView.setAdapter(adapter);
                }
            }
        });
    }
}
