package gt.edu.umg.proyectoprogra2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import gt.edu.umg.proyectoprogra2.Adaptador.ImageAdapter;
import gt.edu.umg.proyectoprogra2.BaseDatos.DatabaseHelper;
import gt.edu.umg.proyectoprogra2.Modelo.ImageItem;

public class GaleriaActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private List<ImageItem> imageList;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galeria_activity);


        recyclerView = findViewById(R.id.galeriaView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);
        imageList = new ArrayList<>();

        // Cargar imágenes desde la base de datos
        cargarImagenes();

        //Obtener las coordenada del Intent
        double latitude = getIntent().getDoubleExtra("latitude", 0.0);
        double longitude = getIntent().getDoubleExtra("longitude", 0.0);
        adapter = new ImageAdapter(imageList, latitude, longitude);
        recyclerView.setAdapter(adapter);

    }


    private void cargarImagenes() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT IMAGEN FROM " + DatabaseHelper.TABLE_NAME, null);

            if (cursor.moveToFirst()) {
                do {
                    byte[] imageBytes = cursor.getBlob(0); // Obtener el BLOB
                    imageList.add(new ImageItem(imageBytes));
                } while (cursor.moveToNext());
            }
            cursor.close();

    }
}
