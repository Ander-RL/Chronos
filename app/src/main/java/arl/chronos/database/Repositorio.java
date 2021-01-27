package arl.chronos.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import arl.chronos.classes.Alarma;

// Gestiona la recogida y envio de los datos entre la BBDD y la Interfaz de Usuario (UI). La Activity accede al ViewModel y este ultimo al repositorio.
public class Repositorio {

    private AlarmaDAO alarmaDAO;
    private LiveData<List<Alarma>> todasAlarmas;

    // Contexto para crear la instancia de la base de datos
    public Repositorio(Application application) {
        BaseDatos baseDatos = BaseDatos.getInstance(application);
        alarmaDAO = baseDatos.alarmaDAO();
        todasAlarmas = alarmaDAO.getTodasAlarmas();
    }

    public void insert(Alarma alarma){
        new InsertAlarmaAsyncTask(alarmaDAO).execute(alarma);
    }

    public void update(Alarma alarma){
        new UpdateAlarmaAsyncTask(alarmaDAO).execute(alarma);
    }

    public void delete(Alarma alarma){
        new DeleteAlarmaAsyncTask(alarmaDAO).execute(alarma);
    }

    public void deleteTodasAlarmas(){
        new DeleteTodasAlarmaAsyncTask(alarmaDAO).execute();
    }

    public LiveData<List<Alarma>> getTodasAlarmas(){
        return todasAlarmas;
    }

    // Ejecucion asincrona para que no crasee la aplicacion
    public static class InsertAlarmaAsyncTask extends AsyncTask<Alarma, Void, Void>{

        private AlarmaDAO alarmaDAO;
        private InsertAlarmaAsyncTask(AlarmaDAO alarmaDAO){
            this.alarmaDAO = alarmaDAO;
        }

        @Override
        protected Void doInBackground(Alarma... alarmas) {
            alarmaDAO.insert(alarmas[0]);
            return null;
        }
    }

    // Ejecucion asincrona para que no crasee la aplicacion
    public static class UpdateAlarmaAsyncTask extends AsyncTask<Alarma, Void, Void>{

        private AlarmaDAO alarmaDAO;
        private UpdateAlarmaAsyncTask(AlarmaDAO alarmaDAO){
            this.alarmaDAO = alarmaDAO;
        }

        @Override
        protected Void doInBackground(Alarma... alarmas) {
            alarmaDAO.update(alarmas[0]);
            return null;
        }
    }

    // Ejecucion asincrona para que no crasee la aplicacion
    public static class DeleteAlarmaAsyncTask extends AsyncTask<Alarma, Void, Void>{

        private AlarmaDAO alarmaDAO;
        private DeleteAlarmaAsyncTask(AlarmaDAO alarmaDAO){
            this.alarmaDAO = alarmaDAO;
        }

        @Override
        protected Void doInBackground(Alarma... alarmas) {
            alarmaDAO.delete(alarmas[0]);
            return null;
        }
    }

    // Ejecucion asincrona para que no crasee la aplicacion
    public static class DeleteTodasAlarmaAsyncTask extends AsyncTask<Alarma, Void, Void>{

        private AlarmaDAO alarmaDAO;
        private DeleteTodasAlarmaAsyncTask(AlarmaDAO alarmaDAO){
            this.alarmaDAO = alarmaDAO;
        }

        @Override
        protected Void doInBackground(Alarma... alarmas) {
            alarmaDAO.deleteTodasAlarmas();
            return null;
        }
    }
}
