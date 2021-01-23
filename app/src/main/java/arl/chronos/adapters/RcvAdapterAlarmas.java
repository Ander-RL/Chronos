package arl.chronos.adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import arl.chronos.CrearEditarAlarma;
import arl.chronos.R;
import arl.chronos.classes.Alarma;
import arl.chronos.classes.AlertReceiver;

public class RcvAdapterAlarmas extends RecyclerView.Adapter<RcvAdapterAlarmas.MyViewHolder> {

    private List<Alarma> alarmas = new ArrayList<>();
    private OnItemClickListener listener;
    Context context;
    private String ho;
    private String mi;
    private int hor;
    private int min;
    private int seg;
    //private static boolean repetirAlarma = true;
    private Boolean l = false;
    private Boolean m = false;
    private Boolean x = false;
    private Boolean j = false;
    private Boolean v = false;
    private Boolean s = false;
    private Boolean d = false;
    private String nombreSonido;
    private String sonidoUri;
    private Boolean sonar;

    public static final String MENSAJE = "mensaje_alarma";
    public static final String ID_ALARMA = "id_alarma";
    public static final String PARAR = "parar";

    public RcvAdapterAlarmas(Context context) {
        this.context = context;
    }

    public void setAlarmas(List<Alarma> alarmas) {
        this.alarmas = alarmas;
        notifyDataSetChanged();
    }

    // Para poder pasar al metodo de Swip (en TabFragmentAlarmas) y poder borrar esa alarma en concreto. Pasamos su posicion.
    public Alarma getAlarmaAt(int position) {
        return alarmas.get(position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // Parent es RecyclerView. Context es Fragment.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_alarmas_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Alarma currentAlarma = alarmas.get(position);

        holder.hora.setText(currentAlarma.getHora() + ":" + currentAlarma.getMinuto());
        ho = currentAlarma.getHora();
        mi = currentAlarma.getMinuto();
        nombreSonido = currentAlarma.getNombreSonido();
        sonidoUri = currentAlarma.getSonidoUri();
        sonar = currentAlarma.getSonar();

        hor = Integer.parseInt(ho);
        min = Integer.parseInt(mi);
        seg = 0;

        if (currentAlarma.getLunes() == true) {
            holder.lunes.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
            l = true;
        }
        if (currentAlarma.getMartes() == true) {
            holder.martes.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
            m = true;
        }
        if (currentAlarma.getMiercoles() == true) {
            holder.miercoles.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
            x = true;
        }
        if (currentAlarma.getJueves() == true) {
            holder.jueves.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
            j = true;
        }
        if (currentAlarma.getViernes() == true) {
            holder.viernes.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
            v = true;
        }
        if (currentAlarma.getSabado() == true) {
            holder.sabado.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
            s = true;
        }
        if (currentAlarma.getDomingo() == true) {
            holder.domingo.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
            d = true;
        }
        if (currentAlarma.getActivated() == true) {
            holder.activated.setChecked(true);

            // Comprueba el día de la semana con los días elegidos para la alarma
            if (diaAlarma(currentAlarma)) {
                // Para que no funcione en caso de que no haya valores
                if (!ho.isEmpty() && !mi.isEmpty()) {
                    // Para que las alarmas activas con hora anterior a la actual no se ejecuten
                    if (!fechaAlarma(hor, min, seg).before(Calendar.getInstance())) {
                        startAlarma(fechaAlarma(hor, min, seg), currentAlarma.getId());
                    }
                        /*if (!fechaAlarma(Integer.parseInt(ho), Integer.parseInt(mi)).before(Calendar.getInstance())) {
                            startAlarma(fechaAlarma(Integer.parseInt(ho), Integer.parseInt(mi)), currentAlarma.getId());
                        }*/
                }
            }
        } else {
            holder.activated.setChecked(false);
            //cancelAlarma();
            cancelAlarma(currentAlarma.getId());
        }

    }

    @Override
    public int getItemCount() {
        return alarmas.size();
    }

    // CLASE INTERNA

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView hora;
        TextView lunes;
        TextView martes;
        TextView miercoles;
        TextView jueves;
        TextView viernes;
        TextView sabado;
        TextView domingo;
        TextView editarAlarma;
        SwitchMaterial activated;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hora = itemView.findViewById(R.id.tv_hora);
            lunes = itemView.findViewById(R.id.tv_lunes);
            martes = itemView.findViewById(R.id.tv_martes);
            miercoles = itemView.findViewById(R.id.tv_miercoles);
            jueves = itemView.findViewById(R.id.tv_jueves);
            viernes = itemView.findViewById(R.id.tv_viernes);
            sabado = itemView.findViewById(R.id.tv_sabado);
            domingo = itemView.findViewById(R.id.tv_domingo);
            activated = itemView.findViewById(R.id.activated);
            editarAlarma = itemView.findViewById(R.id.tv_editar_alarma);

            editarAlarma.setOnClickListener(new View.OnClickListener() { // FUNCIONA SI TOCAS SOBRE LA ALARMA
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(alarmas.get(position), true);
                        //Toast.makeText(context, getAdapterPosition() + " Position", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            activated.setOnClickListener(new View.OnClickListener() {// FUNCIONA SI TOCAS SOBRE EL SWITCH
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(alarmas.get(position), false);
                        //Toast.makeText(context, getAdapterPosition() + " Position", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Alarma alarma, Boolean editar);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private Calendar fechaAlarma(int hora, int min, int seg) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hora);
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, seg);

        return c;
    }

    private void startAlarma(Calendar c, int code) {

        Log.d("/////ADAPTER ALARM/////", nombreSonido + " ---> Uri: " + sonidoUri + " ---> Sonar: " + sonar);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        intent.putExtra(MENSAJE, ho + ":" + mi);
        intent.putExtra(ID_ALARMA, code);
        intent.putExtra(PARAR, "no");
        intent.putExtra(CrearEditarAlarma.EXTRA_SONIDO, nombreSonido);
        intent.putExtra(CrearEditarAlarma.EXTRA_URI, sonidoUri);
        intent.putExtra(CrearEditarAlarma.EXTRA_SONAR, sonar);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, code, intent, PendingIntent.FLAG_UPDATE_CURRENT); // FLAG envia la info de putExtra

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarma(int code) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, code, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }

    private Boolean diaAlarma(Alarma alarma) {
        Calendar c = Calendar.getInstance();
        int dia = c.get(Calendar.DAY_OF_WEEK);

        switch (dia) {
            case Calendar.MONDAY:
                return alarma.getLunes();
            case Calendar.TUESDAY:
                return alarma.getMartes();
            case Calendar.WEDNESDAY:
                return alarma.getMiercoles();
            case Calendar.THURSDAY:
                return alarma.getJueves();
            case Calendar.FRIDAY:
                return alarma.getViernes();
            case Calendar.SATURDAY:
                return alarma.getSabado();
            case Calendar.SUNDAY:
                return alarma.getDomingo();
            default:
                return false;
        }
    }
}
