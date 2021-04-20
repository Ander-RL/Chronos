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
import arl.chronos.classes.AlarmaUnica;
import arl.chronos.receiver.AlarmReceiver;
import arl.chronos.receiver.AlertReceiver;
import arl.chronos.receiver.BootCompletedReceiver;
import arl.chronos.service.RestartAlarmsService;

public class RcvAdapterAlarmas extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Alarma> alarmas = new ArrayList<>();
    private List<AlarmaUnica> alarmasUnicas = new ArrayList<>();
    private ArrayList<Object> dataSet = new ArrayList<>();
    private OnItemClickListener listener;
    private OnItemClickListenerUnica listenerUnica;
    Context context;
    private String ho;
    private String mi;
    private String mostrarFecha;
    private int hor;
    private int min;
    private int seg;
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
    private Boolean activar;
    private int tipoAlarma;

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

    public void setAlarmasUnicas(List<AlarmaUnica> alarmasUnicas) {
        this.alarmasUnicas = alarmasUnicas;
        notifyDataSetChanged();
    }

    // Para poder pasar al metodo de Swip (en TabFragmentAlarmas) y poder borrar esa alarma en concreto. Pasamos su posicion.
    public Object getAlarmaAt(int position) {
        return dataSet.get(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // Parent es RecyclerView. Context es Fragment.

        if (viewType == 1) {
            tipoAlarma = 1;
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_alarmas_unicas_layout, parent, false);
            return new MyViewHolderUnica(view);
        }
        tipoAlarma = 0;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_alarmas_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == 0 && (dataSet.get(position) instanceof Alarma)) {

            MyViewHolder myHolder = (MyViewHolder) holder;
            Alarma currentAlarma = (Alarma) dataSet.get(position);

            myHolder.hora.setText(currentAlarma.getHora() + ":" + currentAlarma.getMinuto());
            ho = currentAlarma.getHora();
            mi = currentAlarma.getMinuto();
            nombreSonido = currentAlarma.getNombreSonido();
            sonidoUri = currentAlarma.getSonidoUri();
            sonar = currentAlarma.getSonar();
            activar = currentAlarma.getActivated();

            hor = Integer.parseInt(ho);
            min = Integer.parseInt(mi);
            seg = 0;

            if (currentAlarma.getLunes()) {
                myHolder.lunes.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
                l = true;
            }
            if (currentAlarma.getMartes()) {
                myHolder.martes.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
                m = true;
            }
            if (currentAlarma.getMiercoles()) {
                myHolder.miercoles.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
                x = true;
            }
            if (currentAlarma.getJueves()) {
                myHolder.jueves.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
                j = true;
            }
            if (currentAlarma.getViernes()) {
                myHolder.viernes.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
                v = true;
            }
            if (currentAlarma.getSabado()) {
                myHolder.sabado.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
                s = true;
            }
            if (currentAlarma.getDomingo()) {
                myHolder.domingo.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
                d = true;
            }

            if (currentAlarma.getActivated()) {
                myHolder.activated.setChecked(true);

                String ho = currentAlarma.getHora();
                String mi = currentAlarma.getMinuto();

                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(ho));
                c.set(Calendar.MINUTE, Integer.parseInt(mi));

                startAlarma(c, currentAlarma.getId());

            } else {
                myHolder.activated.setChecked(false);
                cancelAlarma(currentAlarma.getId());
            }
        }

        if (holder.getItemViewType() == 1 && (dataSet.get(position) instanceof AlarmaUnica)) {

            MyViewHolderUnica myHolder = (MyViewHolderUnica) holder;
            AlarmaUnica currentAlarma = (AlarmaUnica) dataSet.get(position);

            myHolder.hora.setText(currentAlarma.getHora() + ":" + currentAlarma.getMinuto());
            ho = currentAlarma.getHora();
            mi = currentAlarma.getMinuto();
            mostrarFecha = currentAlarma.getAno() + "/" + currentAlarma.getMes() + "/" + currentAlarma.getDia();
            nombreSonido = currentAlarma.getNombreSonido();
            sonidoUri = currentAlarma.getSonidoUri();
            sonar = currentAlarma.getSonar();
            activar = currentAlarma.getActivated();

            hor = Integer.parseInt(ho);
            min = Integer.parseInt(mi);
            seg = 0;

            myHolder.fecha.setText(mostrarFecha);

            l = false;
            m = false;
            x = false;
            j = false;
            v = false;
            s = false;
            d = false;

            if (currentAlarma.getActivated()) {
                myHolder.activated.setChecked(true);
                if (!getDiaAlarmaUnica(currentAlarma).before(Calendar.getInstance())) {
                    startAlarmaUnica(getDiaAlarmaUnica(currentAlarma), currentAlarma.getId());
                }
            } else {
                myHolder.activated.setChecked(false);
                cancelAlarma(currentAlarma.getId());
            }
        }

    }

    @Override
    public int getItemCount() {
        return alarmasUnicas.size() + alarmas.size();
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
                        listener.onItemClick(dataSet.get(position), true);
                    }
                }
            });

            activated.setOnClickListener(new View.OnClickListener() {// FUNCIONA SI TOCAS SOBRE EL SWITCH
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(dataSet.get(position), false);
                    }
                }
            });
        }
    }

    public class MyViewHolderUnica extends RecyclerView.ViewHolder {
        TextView hora;
        TextView fecha;
        TextView editarAlarma;
        SwitchMaterial activated;

        public MyViewHolderUnica(@NonNull View itemView) {
            super(itemView);
            hora = itemView.findViewById(R.id.tv_hora_unica);
            fecha = itemView.findViewById(R.id.tv_fecha_unica);
            editarAlarma = itemView.findViewById(R.id.tv_editar_alarma_unica);
            activated = itemView.findViewById(R.id.activated_unica);

            editarAlarma.setOnClickListener(new View.OnClickListener() { // FUNCIONA SI TOCAS SOBRE LA ALARMA
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listenerUnica != null && position != RecyclerView.NO_POSITION) {
                        listenerUnica.onItemClickUnica(dataSet.get(position), true);
                    }
                }
            });

            activated.setOnClickListener(new View.OnClickListener() {// FUNCIONA SI TOCAS SOBRE EL SWITCH
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listenerUnica != null && position != RecyclerView.NO_POSITION) {
                        listenerUnica.onItemClickUnica(dataSet.get(position), false);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Object alarmaO, Boolean editar);
    }

    public interface OnItemClickListenerUnica {
        void onItemClickUnica(Object alarmaO, Boolean editar);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemClickListenerUnica(OnItemClickListenerUnica listenerUnica) {
        this.listenerUnica = listenerUnica;
    }

    @Override
    public int getItemViewType(int position) {

        dataSet = new ArrayList<>();

        for (Alarma alarma : alarmas) {
            dataSet.add(alarma);
        }
        for (AlarmaUnica alarma : alarmasUnicas) {
            dataSet.add(alarma);
        }

        int viewType = 0; // Default --> MyViewHolder

        if (dataSet.get(position) instanceof Alarma) {
            viewType = 0;
        }
        if (dataSet.get(position) instanceof AlarmaUnica) {
            viewType = 1;
        }
        return viewType;
    }

    private void startAlarma(Calendar c, int code) {

        Context ac = context.getApplicationContext();

        AlarmManager alarmManager = (AlarmManager) ac.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ac, AlarmReceiver.class);
        intent.putExtra("alarma", "activar");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ac, code, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT); // FLAG envia la info de putExtra

        alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(c.getTimeInMillis(),pendingIntent), pendingIntent);
    }

    private void startAlarmaUnica(Calendar c, int code) {

        Context ac = context.getApplicationContext();

        AlarmManager alarmManager = (AlarmManager) ac.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ac, AlertReceiver.class);
        intent.putExtra(MENSAJE, ho + ":" + mi);
        intent.putExtra(ID_ALARMA, code);
        intent.putExtra(PARAR, "no");
        intent.putExtra(CrearEditarAlarma.EXTRA_SONIDO, nombreSonido);
        intent.putExtra(CrearEditarAlarma.EXTRA_URI, sonidoUri);
        intent.putExtra(CrearEditarAlarma.EXTRA_SONAR, sonar);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ac, code, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT); // FLAG envia la info de putExtra

        alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(c.getTimeInMillis(), pendingIntent), pendingIntent);
    }

    private void cancelAlarma(int code) {
        Context ac = context.getApplicationContext();
        AlarmManager alarmManager = (AlarmManager) ac.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ac, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ac, code, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    private Calendar getDiaAlarmaUnica(AlarmaUnica alarmaUnica) {

        Calendar c = Calendar.getInstance();

        int ano = Integer.parseInt(alarmaUnica.getAno());
        int mes = Integer.parseInt(alarmaUnica.getMes()) - 1;
        int dia = Integer.parseInt(alarmaUnica.getDia());
        int hora = Integer.parseInt(alarmaUnica.getHora());
        int minuto = Integer.parseInt(alarmaUnica.getMinuto());

        c.set(ano, mes, dia, hora, minuto, 0);

        return c;
    }
}
