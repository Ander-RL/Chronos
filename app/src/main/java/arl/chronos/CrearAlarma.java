package arl.chronos;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class CrearAlarma extends AppCompatActivity {

    private Button cancelar;                 private Button guardar;                private ImageButton reloj;
    private int hora;                        private int min;
    private Boolean l;                       private Boolean m;                     private Boolean x;                private Boolean j;
    private Boolean v;                       private Boolean s;                     private Boolean d;
    private final String HORA = "hora";      private final String MIN = "min";
    private final String LUN = "L";          private final String MAR = "M";        private final String MIE = "X";   private final String JUE = "J";
    private final String VIE = "V";          private final String SAB = "S";        private final String DOM = "D";
    private TimePickerDialog timePicker;     private Calendar calendar;             private EditText hhmm;
    private final String CERO = "0";         private final String DOS_PUNTOS = ":";


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
                // TODO Devolver valor de hora y días de alarma. RECOGER LOS VALORES E INTRODUCIRLOS
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra(HORA, hora); intent.putExtra(MIN, min);   intent.putExtra(LUN, l);
                intent.putExtra(MAR, m);     intent.putExtra(MIE, x);     intent.putExtra(JUE, j);
                intent.putExtra(VIE, v);     intent.putExtra(SAB, s);     intent.putExtra(DOM, d);
            }
        });

        reloj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar = Calendar.getInstance();
                hora = calendar.get(Calendar.HOUR_OF_DAY);
                min = calendar.get(Calendar.MINUTE);
                Toast.makeText(view.getContext(), "ON CLICK", Toast.LENGTH_LONG).show();

                timePicker = new TimePickerDialog(CrearAlarma.this, R.style.DialogTheme,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hh, int mm) {
                        //Formateo el hora obtenido: antepone el 0 si son menores de 10
                        String horaFormateada =  (hh < 10)? String.valueOf(CERO + hh) : String.valueOf(hh);
                        //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                        String minutoFormateado = (mm < 10)? String.valueOf(CERO + mm):String.valueOf(mm);

                        hhmm.setText(horaFormateada + DOS_PUNTOS + minutoFormateado);
                    }
                }, hora, min, true);
                timePicker.show();
            }
        });


    }
}
