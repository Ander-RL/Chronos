package arl.chronos;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;

public class CrearEditarAlarma extends AppCompatActivity {

    private Button cancelar;
    private Button guardar;
    private ImageButton reloj;
    private ImageButton btnCalendario;
    private boolean activar;
    private int hora;
    private int min;
    private int ano;
    private int mes;
    private int dia;
    private String fecha;
    private Boolean l = false;
    private Boolean m = false;
    private Boolean x = false;
    private Boolean j = false;
    private Boolean v = false;
    private Boolean s = false;
    private Boolean d = false;
    private TimePickerDialog timePicker;
    private DatePickerDialog datePicker;
    private Calendar calendar;
    private EditText hhmm;
    private EditText etFecha;
    private final String CERO = "0";
    private final String DOS_PUNTOS = ":";
    private SwitchMaterial sonidoSwitch;
    private String horaFormateada;
    private String minutoFormateado;
    private TextView selectorSonido;
    private TextView selectorMemoria;
    private TextView tvNombreSonido;
    private String nombreSonido;
    private Boolean sonar;
    private Uri sonidoUri;
    private final int ESCOGER_SONIDO_CODE = 1;
    private final int STORAGE_PERMISSION_CODE = 1;

    public static final String EXTRA_ID = "arl.chronos.EXTRA_ID";
    public static final String EXTRA_TIPO_ALARMA = "arl.chronos.EXTRA_TIPO_ALARMA";
    public static final String EXTRA_ANO = "arl.chronos.EXTRA_ANO";
    public static final String EXTRA_MES = "arl.chronos.EXTRA_MES";
    public static final String EXTRA_DIA = "arl.chronos.EXTRA_DIA";
    public static final String EXTRA_HORA = "arl.chronos.EXTRA_HORA";
    public static final String EXTRA_MIN = "arl.chronos.EXTRA_MIN";
    public static final String EXTRA_LUN = "arl.chronos.EXTRA_LUN";
    public static final String EXTRA_MAR = "arl.chronos.EXTRA_MAR";
    public static final String EXTRA_MIE = "arl.chronos.EXTRA_MIE";
    public static final String EXTRA_JUE = "arl.chronos.EXTRA_JUE";
    public static final String EXTRA_VIE = "arl.chronos.EXTRA_VIE";
    public static final String EXTRA_SAB = "arl.chronos.EXTRA_SAB";
    public static final String EXTRA_DOM = "arl.chronos.EXTRA_DOM";
    public static final String EXTRA_ACT = "arl.chronos.EXTRA_ACT";
    public static final String EXTRA_SONIDO = "arl.chronos.EXTRA_SONIDO";
    public static final String EXTRA_SONAR = "arl.chronos.EXTRA_SONAR";
    public static final String EXTRA_URI = "arl.chronos.EXTRA_URI";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_alarma);

        cancelar = findViewById(R.id.btn_cancelar_alarma);
        guardar = findViewById(R.id.btn_guardar_alarma);
        reloj = findViewById(R.id.btn_reloj);
        hhmm = findViewById(R.id.edit_hhmm);
        etFecha = findViewById(R.id.edit_fecha);
        btnCalendario = findViewById(R.id.btn_calendario);

        selectorSonido = findViewById(R.id.tv_selector);
        selectorMemoria = findViewById(R.id.tv_selector_sd);
        sonidoSwitch = findViewById(R.id.sonido);
        tvNombreSonido = findViewById(R.id.tv_nombre_sonido);

        // Si se edita la alarma, se entra en esta condición
        Intent intentEditarAlarma = getIntent();

        if (intentEditarAlarma.hasExtra(EXTRA_ID)) {

            String ho = intentEditarAlarma.getStringExtra(EXTRA_HORA);
            String mi = intentEditarAlarma.getStringExtra(EXTRA_MIN);
            String tiempo = ho + DOS_PUNTOS + mi;

            // Se añaden a estas variables para que luego se pasen de vuelta correctamente y por separado.
            hora = Integer.parseInt(ho);
            min = Integer.parseInt(mi);
            // Se pone la hora en el textview
            hhmm.setText(tiempo);
            tvNombreSonido.setText(intentEditarAlarma.getStringExtra(EXTRA_SONIDO));
            sonar = intentEditarAlarma.getBooleanExtra(EXTRA_SONAR, false);
            sonidoSwitch.setChecked(sonar);
            sonidoUri = Uri.parse(intentEditarAlarma.getStringExtra(EXTRA_URI));
            String tipo_alarma = intentEditarAlarma.getStringExtra(EXTRA_TIPO_ALARMA);

            if (tipo_alarma.equals("unica")) {

                String year = intentEditarAlarma.getStringExtra(EXTRA_ANO);
                String month = intentEditarAlarma.getStringExtra(EXTRA_MES);
                String day = intentEditarAlarma.getStringExtra(EXTRA_DIA);

                ano = Integer.parseInt(year);
                mes = Integer.parseInt(month);
                dia = Integer.parseInt(day);

                fecha = year + "/" + month + "/" + day;
                etFecha.setText(fecha);
                deshabilitarSemana();

            } else {

                RadioButton rL = findViewById(R.id.radio_l);
                RadioButton rM = findViewById(R.id.radio_m);
                RadioButton rX = findViewById(R.id.radio_x);
                RadioButton rJ = findViewById(R.id.radio_j);
                RadioButton rV = findViewById(R.id.radio_v);
                RadioButton rS = findViewById(R.id.radio_s);
                RadioButton rD = findViewById(R.id.radio_d);
                l = intentEditarAlarma.getBooleanExtra(EXTRA_LUN, false);
                m = intentEditarAlarma.getBooleanExtra(EXTRA_MAR, false);
                x = intentEditarAlarma.getBooleanExtra(EXTRA_MIE, false);
                j = intentEditarAlarma.getBooleanExtra(EXTRA_JUE, false);
                v = intentEditarAlarma.getBooleanExtra(EXTRA_VIE, false);
                s = intentEditarAlarma.getBooleanExtra(EXTRA_SAB, false);
                d = intentEditarAlarma.getBooleanExtra(EXTRA_DOM, false);
                rL.setChecked(l);
                rM.setChecked(m);
                rX.setChecked(x);
                rJ.setChecked(j);
                rV.setChecked(v);
                rS.setChecked(s);
                rD.setChecked(d);
            }

            SwitchMaterial activarAlarma = findViewById(R.id.activar);
            activar = intentEditarAlarma.getBooleanExtra(EXTRA_ACT, false);
            activarAlarma.setChecked(activar);
        }

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

                if (etFecha.getText().toString().isEmpty()) {
                    ano = -1;
                    mes = -1;
                    dia = -1;
                }

                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra(EXTRA_HORA, hora);
                intent.putExtra(EXTRA_MIN, min);
                intent.putExtra(EXTRA_LUN, l);
                intent.putExtra(EXTRA_MAR, m);
                intent.putExtra(EXTRA_MIE, x);
                intent.putExtra(EXTRA_JUE, j);
                intent.putExtra(EXTRA_VIE, v);
                intent.putExtra(EXTRA_SAB, s);
                intent.putExtra(EXTRA_DOM, d);
                intent.putExtra(EXTRA_ANO, ano);
                intent.putExtra(EXTRA_MES, mes);
                intent.putExtra(EXTRA_DIA, dia);
                intent.putExtra(EXTRA_ACT, activar);

                nombreSonido = tvNombreSonido.getText().toString();

                if (nombreSonido.equals(getResources().getString(R.string.tv_ningun_sonido)) || nombreSonido.equals("")) {
                    intent.putExtra(EXTRA_SONIDO, "");
                    intent.putExtra(EXTRA_URI, "");
                } else {
                    intent.putExtra(EXTRA_SONIDO, nombreSonido);
                    intent.putExtra(EXTRA_URI, sonidoUri.toString());
                }
                intent.putExtra(EXTRA_SONAR, sonar);

                int id = getIntent().getIntExtra(EXTRA_ID, -1);
                if (id != -1) {
                    intent.putExtra(EXTRA_ID, id);
                }

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

                timePicker = new TimePickerDialog(CrearEditarAlarma.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hh, int mm) {
                        //Formateo el hora obtenido: antepone el 0 si son menores de 10
                        String horaFormateada = (hh < 10) ? (CERO + hh) : String.valueOf(hh);
                        //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                        String minutoFormateado = (mm < 10) ? (CERO + mm) : String.valueOf(mm);

                        hhmm.setText(horaFormateada + DOS_PUNTOS + minutoFormateado);

                        hora = hh;
                        min = mm;
                    }
                }, horas, mins, true);
                timePicker.show();
            }
        });

        btnCalendario.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            datePicker = new DatePickerDialog(this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    ano = datePicker.getYear();
                    mes = datePicker.getMonth() + 1;
                    dia = datePicker.getDayOfMonth();
                    Toast.makeText(CrearEditarAlarma.this, ano + "/" + mes + "/" + dia, Toast.LENGTH_SHORT).show();
                    fecha = ano + "/" + mes + "/" + dia;
                    etFecha.setText(fecha);
                }
            }, year, month, day);
            datePicker.show();

            if (!etFecha.getText().toString().equals(getResources().getString(R.string.yyy_mm_dd))) {
                // Desabilita botones repeticion semanal
                deshabilitarSemana();
            }
        });

        // Se lanza la actividad para escoger el sonido de la alarma
        selectorSonido.setOnClickListener(view -> {
            Intent intent = new Intent(this, EscogerSonido.class);
            intent.putExtra("selector", "system");
            startActivityForResult(intent, ESCOGER_SONIDO_CODE);
        });

        selectorMemoria.setOnClickListener(view -> {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(this, EscogerSonido.class);
                intent.putExtra("selector", "storage");
                startActivityForResult(intent, ESCOGER_SONIDO_CODE);
            } else {
                requestStoragePermission();
            }
        });
    }

    private void requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            ActivityCompat.requestPermissions(CrearEditarAlarma.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, (R.string.permiso_concedido), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, (R.string.permiso_denegado), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onRadioButtonClick(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        boolean active = view.isActivated();
        int id = view.getId();

        if (active && checked) {
            ((RadioButton) view).setChecked(false);

            if (id == R.id.radio_l) {
                l = false;
                view.setActivated(false);
            }
            if (id == R.id.radio_m) {
                m = false;
                view.setActivated(false);
            }
            if (id == R.id.radio_x) {
                x = false;
                view.setActivated(false);
            }
            if (id == R.id.radio_j) {
                j = false;
                view.setActivated(false);
            }
            if (id == R.id.radio_v) {
                v = false;
                view.setActivated(false);
            }
            if (id == R.id.radio_s) {
                s = false;
                view.setActivated(false);
            }
            if (id == R.id.radio_d) {
                d = false;
                view.setActivated(false);
            }

        } else if (!active && checked) {
            ((RadioButton) view).setChecked(true);

            if (id == R.id.radio_l) {
                l = true;
                view.setActivated(true);
            }
            if (id == R.id.radio_m) {
                m = true;
                view.setActivated(true);
            }
            if (id == R.id.radio_x) {
                x = true;
                view.setActivated(true);
            }
            if (id == R.id.radio_j) {
                j = true;
                view.setActivated(true);
            }
            if (id == R.id.radio_v) {
                v = true;
                view.setActivated(true);
            }
            if (id == R.id.radio_s) {
                s = true;
                view.setActivated(true);
            }
            if (id == R.id.radio_d) {
                d = true;
                view.setActivated(true);
            }
        }
    }

    public void onActivarClick(View view) {
        SwitchMaterial toggle = (SwitchMaterial) view;
        boolean checked = toggle.isChecked();

        if (!checked) {
            activar = false;
        }
        if (checked) {
            activar = true;
        }
    }

    public void onActivarSonido(View view) {
        SwitchMaterial toggle = (SwitchMaterial) view;
        boolean checked = toggle.isChecked();

        if (!checked) {
            sonar = false;
        }
        if (checked) {
            sonar = true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ESCOGER_SONIDO_CODE && resultCode == RESULT_OK) {
            nombreSonido = data.getStringExtra(EscogerSonido.EXTRA_NOMBRE_SONIDO);
            sonidoUri = Uri.parse(data.getStringExtra(EscogerSonido.EXTRA_URI_SONIDO)); // Se transforma el String de vuelta a URI
            tvNombreSonido.setText(nombreSonido);
        } else {
            Toast.makeText(this, (R.string.error), Toast.LENGTH_SHORT).show();
        }
    }

    private void deshabilitarSemana() {
        findViewById(R.id.radio_l).setEnabled(false);
        findViewById(R.id.radio_m).setEnabled(false);
        findViewById(R.id.radio_x).setEnabled(false);
        findViewById(R.id.radio_j).setEnabled(false);
        findViewById(R.id.radio_v).setEnabled(false);
        findViewById(R.id.radio_s).setEnabled(false);
        findViewById(R.id.radio_d).setEnabled(false);

        findViewById(R.id.radio_l).setActivated(false);
        findViewById(R.id.radio_m).setActivated(false);
        findViewById(R.id.radio_x).setActivated(false);
        findViewById(R.id.radio_j).setActivated(false);
        findViewById(R.id.radio_v).setActivated(false);
        findViewById(R.id.radio_s).setActivated(false);
        findViewById(R.id.radio_d).setActivated(false);

        findViewById(R.id.radio_l).setVisibility(View.INVISIBLE);
        findViewById(R.id.radio_m).setVisibility(View.INVISIBLE);
        findViewById(R.id.radio_x).setVisibility(View.INVISIBLE);
        findViewById(R.id.radio_j).setVisibility(View.INVISIBLE);
        findViewById(R.id.radio_v).setVisibility(View.INVISIBLE);
        findViewById(R.id.radio_s).setVisibility(View.INVISIBLE);
        findViewById(R.id.radio_d).setVisibility(View.INVISIBLE);
    }
}
