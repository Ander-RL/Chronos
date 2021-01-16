package arl.chronos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import arl.chronos.adapters.RcvAdapterSonidos;
import arl.chronos.classes.Sonido;

public class EscogerSonido extends AppCompatActivity {
    private String nombreSonido;
    private Button aceptar;
    private Button cancelar;
    private TextView sonidoElegido;
    private RecyclerView recyclerView;
    private RcvAdapterSonidos rcvAdapterSonidos;
    private RecyclerView.LayoutManager layoutManager;

    public static final String EXTRA_NOMBRE_SONIDO = "arl.chronos.EXTRA_NOMBRE_SONIDO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.escoger_sonido);

        aceptar  = findViewById(R.id.btn_aceptar_sonido);
        cancelar = findViewById(R.id.btn_cancelar_sonido);

        sonidoElegido = findViewById(R.id.tv_sonido_elegido);

        ArrayList<Sonido> sonidoList = getSonidoList();

        recyclerView = findViewById(R.id.rcv_sonidos);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rcvAdapterSonidos = new RcvAdapterSonidos(this);
        recyclerView.setAdapter(rcvAdapterSonidos);
        rcvAdapterSonidos.setSonidos(sonidoList);

        cancelar.setOnClickListener(view -> {
            Intent intent = new Intent(this, CrearAlarma.class);
            startActivity(intent);
        });

        aceptar.setOnClickListener(view -> {
            Intent intent = new Intent(this, CrearAlarma.class);
            intent.putExtra(EXTRA_NOMBRE_SONIDO, nombreSonido);

            setResult(RESULT_OK, intent);
            finish();
        });

    }

    public ArrayList<Sonido> getSonidoList() {
        ArrayList<Sonido> sonidoList = new ArrayList<>();

        Uri sonidos;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            sonidos = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            sonidos = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        String[] columnas = new String[] {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE
        };

        String ordenarSonido = MediaStore.Audio.Media.TITLE + " DESC";

        try (Cursor cursor = getApplicationContext().getContentResolver().query(
                sonidos, columnas, null, null, ordenarSonido
        )){
            //int idColumna = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            //int nombreColumna = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int idColumna = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int nombreColumna = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);

            while (cursor.moveToNext()){
                // Obtiene los valores de las columnas del audio de turno
                long id = cursor.getLong(idColumna);
                String nombre = cursor.getString(nombreColumna);

                Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

                // Guarda los valores de las columnas de contenUri  en la lista.
                sonidoList.add(new Sonido(contentUri, nombre));
            }
        }

        return sonidoList;
    }
}