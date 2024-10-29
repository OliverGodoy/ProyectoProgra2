package gt.edu.umg.proyectoprogra2;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.ByteArrayOutputStream;
import java.util.List;

import gt.edu.umg.proyectoprogra2.Adaptador.ImageAdapter;
import gt.edu.umg.proyectoprogra2.BaseDatos.DatabaseHelper;
import gt.edu.umg.proyectoprogra2.Modelo.ImageItem;


public class CamaraActivity extends AppCompatActivity {
    Button btnCamara;
    ImageView imgView;
    DatabaseHelper databaseHelper;
    FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camara_activity);

        btnCamara = findViewById(R.id.btnCamara);
        imgView = findViewById(R.id.imageView);

        // Inicializar la base de datos
        databaseHelper = new DatabaseHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnCamara.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                abrirCamara();
            }
        });



    }
    public void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, 1);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgView.setImageBitmap(imageBitmap);

            //Guardar imagen en la base de datos
            //Convertir imagen a byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            insertarImagen(byteArray);
        }
    }


    public void insertarImagen(byte[] image) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase(); // Usar la instancia de DatabaseHelper
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_2, image);
        db.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
    }


}
