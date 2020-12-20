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
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;

import java.util.Calendar;

public class CrearAlarma extends AppCompatActivity {

    private Button cancelar;                 private Button guardar;                 private ImageButton reloj;
    private String hora;                     private String min;
    private Boolean l;                       private Boolean m;                      private Boolean x;                private Boolean j;
    private Boolean v;                       private Boolean s;                      private Boolean d;
    private TimePickerDialog timePicker;     private Calendar calendar;              private EditText hhmm;
    private final String CERO = "0";         private final String DOS_PUNTOS = ":";  boolean wasChecked;

    public static final String EXTRA_HORA = "arl.chronos.EXTRA_HORA";                     public static final String EXTRA_MIN  = "arl.chronos.EXTRA_MIN";
    public static final String EXTRA_LUN  = "arl.chronos.EXTRA_LUN";                      public static final String EXTRA_MAR  = "arl.chronos.EXTRA_MAR";
    public static final String EXTRA_MIE  = "arl.chronos.EXTRA_MIE";                      public static final String EXTRA_JUE  = "arl.chronos.EXTRA_JUE";
    public static final String EXTRA_VIE  = "arl.chronos.EXTRA_VIE";                      public static final String EXTRA_SAB  = "arl.chronos.EXTRA_SAB";
    public static final String EXTRA_DOM  = "arl.chronos.EXTRA_DOM";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_alarma);

        cancelar = findViewById(R.id.btn_cancelar_alarma);
        guardar  = findViewById(R.id.btn_guardar_alarma);
        reloj    = findViewById(R.id.btn_reloj);
        hhmm     = findViewById(R.id.edit_hhmm);

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

                        hhmm.setText(horaFormateada + DOS_PUNTOS + minutoFormateado);

                        hora = horaFormateada;
                        min  = minutoFormateado;
                    }
                }, horas, mins, true);
                timePicker.show();
            }
        });
    }

    public void onRadioButtonClick(View view) {

            boolean checked = ((RadioButton) view).isChecked();
            int id = view.getId();

            if(wasChecked && checked){
                ((RadioButton) view).setChecked(false);
                wasChecked = false;

                if(id == R.id.radio_l){l = false;}
                if(id == R.id.radio_m){m = false;}
                if(id == R.id.radio_x){x = false;}
                if(id == R.id.radio_j){j = false;}
                if(id == R.id.radio_v){v = false;}
                if(id == R.id.radio_s){s = false;}
                if(id == R.id.radio_d){d = false;}

            }else if(checked){
                wasChecked = true;

                if(id == R.id.radio_l){l = true;}
                if(id == R.id.radio_m){m = true;}
                if(id == R.id.radio_x){x = true;}
                if(id == R.id.radio_j){j = true;}
                if(id == R.id.radio_v){v = true;}
                if(id == R.id.radio_s){s = true;}
                if(id == R.id.radio_d){d = true;}
            }
        }
}
