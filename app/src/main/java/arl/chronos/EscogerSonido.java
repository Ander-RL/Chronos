package arl.chronos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class EscogerSonido extends AppCompatActivity {
    private String nombreSonido;
    private Button aceptar;
    private Button cancelar;
    private TextView sonidoElegido;

    public static final String EXTRA_NOMBRE_SONIDO = "arl.chronos.EXTRA_NOMBRE_SONIDO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.escoger_sonido);

        aceptar  = findViewById(R.id.btn_aceptar_sonido);
        cancelar = findViewById(R.id.btn_cancelar_alarma);

        sonidoElegido = findViewById(R.id.tv_sonido_elegido);

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
}