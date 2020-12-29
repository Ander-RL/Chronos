package arl.chronos.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import arl.chronos.classes.Alarma;
// Este ViewModel es para las alarmas. Otras operaciones tendrían su propio ViewModel
public class MyViewModel extends AndroidViewModel {

    private Repositorio repositorio;
    private LiveData<List<Alarma>> todasAlarmas;

    public MyViewModel(@NonNull Application application) {
        super(application);
        repositorio = new Repositorio(application);
        todasAlarmas = repositorio.getTodasAlarmas();
    }

    // La Activity solo tiene acceso al ViewModel y no al repositorio. Por eso se añaden los metodos de acceso al mismo.
    public void insert(Alarma alarma){
        repositorio.insert(alarma);
    }

    public void update(Alarma alarma){
        repositorio.update(alarma);
    }

    public void delete(Alarma alarma){
        repositorio.delete(alarma);
    }

    public void deleteAll(){
        repositorio.deleteTodasAlarmas();
    }

    public LiveData<List<Alarma>> getTodasAlarmas() {
        return todasAlarmas;
    }
}
