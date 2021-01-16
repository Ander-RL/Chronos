package arl.chronos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.radiobutton.MaterialRadioButton;

import java.util.ArrayList;

import arl.chronos.R;
import arl.chronos.classes.Sonido;

public class RcvAdapterSonidos extends RecyclerView.Adapter<RcvAdapterSonidos.ViewHolderSonidos> {
    private Context context;
    private ArrayList<Sonido> sonidoList;

    public RcvAdapterSonidos(Context context) {
        this.context = context;
    }

    public void setSonidos(ArrayList<Sonido> sonidoList) {
        this.sonidoList = sonidoList;
    }

    @NonNull
    @Override
    public RcvAdapterSonidos.ViewHolderSonidos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // Parent es RecyclerView. Context es EscogerSonido.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_sonidos_layout, parent, false);
        ViewHolderSonidos holder = new ViewHolderSonidos(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSonidos holder, int position) {
        Sonido currentSonido = sonidoList.get(position);

        holder.sonido.setText(currentSonido.getNombre());

    }

    @Override
    public int getItemCount() {
        return sonidoList.size();
    }

    public class ViewHolderSonidos extends RecyclerView.ViewHolder {
        TextView sonido;
        MaterialRadioButton radioButton;

        public ViewHolderSonidos(@NonNull View itemView) {
            super(itemView);
            sonido = itemView.findViewById(R.id.tv_sonido);
            radioButton = itemView.findViewById(R.id.rcv_radio_button);

            radioButton.setOnClickListener(view -> view.setActivated(true));
        }
    }
}
