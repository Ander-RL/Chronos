package arl.chronos;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;

import arl.chronos.classes.Alarma;

public class CrearAlarma extends AppCompatActivity {

    private Button cancelar;                 private Button guardar;                 private ImageButton reloj;
    private boolean activar;                 private int hora;                       private int min;
    private Boolean l = false;               private Boolean m = false;              private Boolean x = false;        private Boolean j = false;
    private Boolean v = false;               private Boolean s = false;              private Boolean d = false;
    private TimePickerDialog timePicker;     private Calendar calendar;              private EditText hhmm;
    private final String CERO = "0";         private final String DOS_PUNTOS = ":";  private SwitchMaterial sonidoSwitch;
    private String horaFormateada;           private String minutoFormateado;
    private TextView selectorSonido;         private TextView tvNombreSonido;
    private String nombreSonido;             private Boolean sonar;

    private final int ESCOGER_SONIDO_CODE = 1;


    public static final String EXTRA_HORA = "arl.chronos.EXTRA_HORA";                     public static final String EXTRA_MIN  = "arl.chronos.EXTRA_MIN";
    public static final String EXTRA_LUN  = "arl.chronos.EXTRA_LUN";                      public static final String EXTRA_MAR  = "arl.chronos.EXTRA_MAR";
    public static final String EXTRA_MIE  = "arl.chronos.EXTRA_MIE";                      public static final String EXTRA_JUE  = "arl.chronos.EXTRA_JUE";
    public static final String EXTRA_VIE  = "arl.chronos.EXTRA_VIE";                      public static final String EXTRA_SAB  = "arl.chronos.EXTRA_SAB";
    public static final String EXTRA_DOM  = "arl.chronos.EXTRA_DOM";                      public static final String EXTRA_ACT  = "arl.chronos.EXTRA_ACT";
    public static final String EXTRA_SONIDO = "arl.chronos.EXTRA_SONIDO";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_alarma);

        cancelar = findViewById(R.id.btn_cancelar_alarma);
        guardar  = findViewById(R.id.btn_guardar_alarma);
        reloj    = findViewById(R.id.btn_reloj);
        hhmm     = findViewById(R.id.edit_hhmm);

        selectorSonido = findViewById(R.id.tv_selector);
        sonidoSwitch   = findViewById(R.id.sonido);
        tvNombreSonido = findViewById(R.id.tv_nombre_sonido);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra(EXTRA_HORA, hora);  intent.putExtra(EXTRA_MIN, min);   intent.putExtra(EXTRA_LUN, l);
                intent.putExtra(EXTRA_MAR,  m);     intent.putExtra(EXTRA_MIE, x);     intent.putExtra(EXTRA_JUE, j);
                intent.putExtra(EXTRA_VIE,  v);     intent.putExtra(EXTRA_SAB, s);     intent.putExtra(EXTRA_DOM, d);
                intent.putExtra(EXTRA_ACT, activar);intent.putExtra(EXTRA_SONIDO, nombreSonido);

                setResult(RESULT_OK, intent);
                finish();
            }
        });

        reloj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar = Calendar.getInstance();
                int horas = calendar.get(Calendar.HOUR_OF_DAY);
                int mins = calendar.get(Calendar.MINUTE);

                timePicker = new TimePickerDialog(CrearAlarma.this, R.style.DialogTheme,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hh, int mm) {
                        //Formateo el hora obtenido: antepone el 0 si son menores de 10
                        String horaFormateada =  (hh < 10)? (CERO + hh) : String.valueOf(hh);
                        //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                        String minutoFormateado = (mm < 10)? (CERO + mm):String.valueOf(mm);

                        //Toast.makeText(getApplicationContext(), horaFormateada + DOS_PUNTOS + minutoFormateado, Toast.LENGTH_LONG).show();
                        hhmm.setText(horaFormateada + DOS_PUNTOS + minutoFormateado);

                        hora = hh;
                        min  = mm;
                        //Toast.makeText(getApplicationContext(), hh + ":" + mm, Toast.LENGTH_LONG).show();
                    }
                }, horas, mins, true);
                timePicker.show();
            }
        });

        // Se lanza la actividad para escoger el sonido de la alarma
        selectorSonido.setOnClickListener(view -> {
            Intent intent = new Intent();
            startActivityForResult(intent, ESCOGER_SONIDO_CODE); // TODO Solicitar Permiso de accesso a memoria antes de lanzar activity.
        });
    }

    public void onRadioButtonClick(View view) {

            boolean checked = ((RadioButton) view).isChecked();
            boolean active = view.isActivated();
            int id = view.getId();

            if(active && checked){
                ((RadioButton) view).setChecked(false);

                if(id == R.id.radio_l){l = false; view.setActivated(false);}
                if(id == R.id.radio_m){m = false; view.setActivated(false);}
                if(id == R.id.radio_x){x = false; view.setActivated(false);}
                if(id == R.id.radio_j){j = false; view.setActivated(false);}
                if(id == R.id.radio_v){v = false; view.setActivated(false);}
                if(id == R.id.radio_s){s = false; view.setActivated(false);}
                if(id == R.id.radio_d){d = false; view.setActivated(false);}

            }else if(!active && checked){
                ((RadioButton) view).setChecked(true);

                if(id == R.id.radio_l){l = true; view.setActivated(true);}
                if(id == R.id.radio_m){m = true; view.setActivated(true);}
                if(id == R.id.radio_x){x = true; view.setActivated(true);}
                if(id == R.id.radio_j){j = true; view.setActivated(true);}
                if(id == R.id.radio_v){v = true; view.setActivated(true);}
                if(id == R.id.radio_s){s = true; view.setActivated(true);}
                if(id == R.id.radio_d){d = true; view.setActivated(true);}
            }
        }

        public void onActivarClick(View view){

            boolean checked = ((SwitchMaterial) view).isActivated();

            if(checked){ activar = false; }
            if(!checked) { activar = true; }

        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ESCOGER_SONIDO_CODE && resultCode == RESULT_OK){
            nombreSonido = data.getStringExtra(EscogerSonido.EXTRA_NOMBRE_SONIDO);
            tvNombreSonido.setText(nombreSonido);
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();;
        }
    }
}
