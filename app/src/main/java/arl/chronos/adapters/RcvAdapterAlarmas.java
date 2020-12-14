package arl.chronos.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;

import arl.chronos.R;
import arl.chronos.classes.Alarmas;
import arl.chronos.database.Alarma;

public class RcvAdapterAlarmas extends RecyclerView.Adapter<RcvAdapterAlarmas.MyViewHolder> {

    private List<Alarma> alarmas = new ArrayList<>();
    Context context;

    public RcvAdapterAlarmas(Context context){
        this.context = context;
    }

    public void setAlarmas(List<Alarma> alarmas){
        this.alarmas = alarmas;
        notifyDataSetChanged();
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

        if (currentAlarma.getLunes() == true) {
            holder.lunes.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
        }
        if (currentAlarma.getMartes() == true) {
            holder.martes.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
        }
        if (currentAlarma.getMiercoles() == true) {
            holder.miercoles.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
        }
        if (currentAlarma.getJueves() == true) {
            holder.jueves.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
        }
        if (currentAlarma.getViernes() == true) {
            holder.viernes.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
        }
        if (currentAlarma.getSabado() == true) {
            holder.sabado.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
        }
        if (currentAlarma.getDomingo() == true) {
            holder.domingo.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
        }
        if (currentAlarma.getActivated() == true) {
            holder.activated.setChecked(true);
        }

    }

    @Override
    public int getItemCount() {
        return alarmas.size();
    }

    // CLASE INTERNA

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView hora;
        TextView lunes;
        TextView martes;
        TextView miercoles;
        TextView jueves;
        TextView viernes;
        TextView sabado;
        TextView domingo;
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
        }
    }

}
