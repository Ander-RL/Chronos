package arl.chronos.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import arl.chronos.classes.Alarma;
import arl.chronos.classes.AlarmaUnica;

// Este ViewModel es para las alarmas. Otras operaciones tendrían su propio ViewModel
public class MyViewModel extends AndroidViewModel {

    private Repositorio repositorio;
    private LiveData<List<Alarma>> todasAlarmas;
    private LiveData<List<AlarmaUnica>> todasAlarmasUnicas;

    public MyViewModel(@NonNull Application application) {
        super(application);
        repositorio = new Repositorio(application);
        todasAlarmas = repositorio.getTodasAlarmas();
        todasAlarmasUnicas = repositorio.getTodasAlarmasUnicas();
    }

    // La Activity solo tiene acceso al ViewModel y no al repositorio. Por eso se añaden los metodos de acceso al mismo.
    public void insert(Alarma alarma){
        repositorio.insert(alarma);
    }
    public void insertUnica(AlarmaUnica alarmaUnica){
        repositorio.insertUnica(alarmaUnica);
    }

    public void update(Alarma alarma){
        repositorio.update(alarma);
    }
    public void updateUnica(AlarmaUnica alarmaUnica){
        repositorio.updateUnica(alarmaUnica);
    }

    public void delete(Alarma alarma){
        repositorio.delete(alarma);
    }
    public void deleteUnica(AlarmaUnica alarmaUnica){
        repositorio.deleteUnica(alarmaUnica);
    }

    public void deleteAll(){
        repositorio.deleteTodasAlarmas();
    }
    public void deleteAllUnica(AlarmaUnica alarmaUnica){
        repositorio.deleteTodasAlarmasUnicas();
    }

    public LiveData<List<Alarma>> getTodasAlarmas() {
        return todasAlarmas;
    }

    public LiveData<List<AlarmaUnica>> getTodasAlarmasUnicas() {
        return todasAlarmasUnicas;
    }
}
