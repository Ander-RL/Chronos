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
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import arl.chronos.adapters.RcvAdapterSonidos;
import arl.chronos.classes.ServicioSonido;
import arl.chronos.classes.Sonido;

public class EscogerSonido extends AppCompatActivity {
    private String nombreSonido;
    private Button aceptar;
    private Button cancelar;
    private TextView sonidoElegido;
    private RecyclerView recyclerView;
    private RcvAdapterSonidos rcvAdapterSonidos;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Sonido> sonidoList;

    public static final String EXTRA_NOMBRE_SONIDO = "arl.chronos.EXTRA_NOMBRE_SONIDO";
    public static final String EXTRA_URI_SONIDO = "arl.chronos.EXTRA_URI_SONIDO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.escoger_sonido);

        aceptar = findViewById(R.id.btn_aceptar_sonido);
        cancelar = findViewById(R.id.btn_cancelar_sonido);

        sonidoElegido = findViewById(R.id.tv_sonido_elegido);

        sonidoList = getSonidoList();

        recyclerView = findViewById(R.id.rcv_sonidos);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rcvAdapterSonidos = new RcvAdapterSonidos();
        recyclerView.setAdapter(rcvAdapterSonidos);
        rcvAdapterSonidos.setSonidos(sonidoList);

        rcvAdapterSonidos.setOnSonidoClickListener(new RcvAdapterSonidos.OnSonidoClickListener() {
            @Override
            public void onSonidoClick(String nombreSonido, String sonidoUri) {
                sonidoElegido.setText(nombreSonido);
                aceptar.setEnabled(true);

                Intent intentServicio = new Intent(getApplicationContext(), ServicioSonido.class);
                intentServicio.putExtra(EXTRA_NOMBRE_SONIDO, nombreSonido);
                intentServicio.putExtra(EXTRA_URI_SONIDO, sonidoUri);
                intentServicio.setAction(".classes.ServicioSonido");
                startService(intentServicio);
            }
        });

        cancelar.setOnClickListener(view -> {

            Intent intentServicio = new Intent(getApplicationContext(), ServicioSonido.class);
            intentServicio.setAction(".classes.ServicioSonido");
            stopService(intentServicio);

            Intent intent = new Intent(this, CrearEditarAlarma.class);
            startActivity(intent);
        });

        if (sonidoElegido.getText().equals(getResources().getString(R.string.tv_ningun_sonido))) {
            aceptar.setEnabled(false);
        }

        aceptar.setOnClickListener(view -> {

            Intent intentServicio = new Intent(getApplicationContext(), ServicioSonido.class);
            intentServicio.setAction(".classes.ServicioSonido");
            stopService(intentServicio);

            nombreSonido = sonidoElegido.getText().toString();
            Intent intent = new Intent(this, CrearEditarAlarma.class);
            intent.putExtra(EXTRA_NOMBRE_SONIDO, nombreSonido);
            intent.putExtra(EXTRA_URI_SONIDO, localizarSonido(nombreSonido).toString()); // Se transforma la URI en String
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    public ArrayList<Sonido> getSonidoList() {
        ArrayList<Sonido> sonidoList = new ArrayList<>();

        Uri sonidos;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //sonidos = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
            sonidos = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_INTERNAL);
        } else {
            //sonidos = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            sonidos = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        }

        String[] columnas = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE
        };

        String ordenarSonido = MediaStore.Audio.Media.TITLE + " ASC";

        try (Cursor cursor = getApplicationContext().getContentResolver().query(
                sonidos, columnas, null, null, ordenarSonido
        )) {
            //int idColumna = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            //int nombreColumna = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int idColumna = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int nombreColumna = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);

            while (cursor.moveToNext()) {
                // Obtiene los valores de las columnas del audio de turno
                long id = cursor.getLong(idColumna);
                String nombre = cursor.getString(nombreColumna);

                //Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
                Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, id);

                // Guarda los valores de las columnas de contenUri  en la lista.
                sonidoList.add(new Sonido(contentUri, nombre));
            }
        }

        return sonidoList;
    }

    // Busca y devuelve la Uri con el nombre de la canción/sonido elegido
    private Uri localizarSonido(String nombreSonido) {
        int s = 0;
        for (int i = 0; i < sonidoList.size(); i++) {
            if (sonidoList.get(i).getNombre().equals(nombreSonido)) {
                s = i;
            }
        }
        return sonidoList.get(s).getUri();
    }
}