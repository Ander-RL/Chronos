package arl.chronos.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import arl.chronos.R;
import arl.chronos.classes.Sonido;

public class RcvAdapterSonidos extends RecyclerView.Adapter<RcvAdapterSonidos.ViewHolderSonidos> {

    private Sonido currentSonido;
    private ArrayList<Sonido> sonidoList;
    private OnSonidoClickListener listener;

    public RcvAdapterSonidos() {
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
        currentSonido = sonidoList.get(position);

        holder.sonido.setText(currentSonido.getNombre());

    }

    @Override
    public int getItemCount() {
        return sonidoList.size();
    }

    public class ViewHolderSonidos extends RecyclerView.ViewHolder {
        TextView sonido;
        TextView sonidoElegido;

        public ViewHolderSonidos(@NonNull View itemView) {
            super(itemView);
            sonido = itemView.findViewById(R.id.tv_sonido);
            sonidoElegido = itemView.findViewById(R.id.tv_sonido_elegido);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    currentSonido = sonidoList.get(position); // Actualiza el sonido actual. De lo contrario siempre pasa el mismo por cada trozo de RecyclerView

                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onSonidoClick(sonido.getText().toString(), currentSonido.getUri().toString());
                    }
                }
            });
        }
    }

    public interface OnSonidoClickListener {
        void onSonidoClick(String nombreSonido, String sonidoUri);
    }

    public void setOnSonidoClickListener(OnSonidoClickListener listener) {
        this.listener = listener;
    }
}
