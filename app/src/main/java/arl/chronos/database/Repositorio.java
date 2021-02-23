package arl.chronos.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import arl.chronos.classes.Alarma;
import arl.chronos.classes.AlarmaUnica;

// Gestiona la recogida y envio de los datos entre la BBDD y la Interfaz de Usuario (UI). La Activity accede al ViewModel y este ultimo al repositorio.
public class Repositorio {

    private AlarmaDAO alarmaDAO;
    private LiveData<List<Alarma>> todasAlarmas;
    private LiveData<List<AlarmaUnica>> todasAlarmasUnicas;

    // Contexto para crear la instancia de la base de datos
    public Repositorio(Application application) {
        BaseDatos baseDatos = BaseDatos.getInstance(application);
        alarmaDAO = baseDatos.alarmaDAO();
        todasAlarmas = alarmaDAO.getTodasAlarmas();
        todasAlarmasUnicas = alarmaDAO.getTodasAlarmasUnicas();
    }

    public void insert(Alarma alarma){
        new InsertAlarmaAsyncTask(alarmaDAO).execute(alarma);
    }

    public void insertUnica(AlarmaUnica alarmaUnica){
        new InsertAlarmaUnicaAsyncTask(alarmaDAO).execute(alarmaUnica);
    }

    public void update(Alarma alarma){
        new UpdateAlarmaAsyncTask(alarmaDAO).execute(alarma);
    }

    public void updateUnica(AlarmaUnica alarmaUnica){
        new UpdateAlarmaUnicaAsyncTask(alarmaDAO).execute(alarmaUnica);
    }

    public void delete(Alarma alarma){
        new DeleteAlarmaAsyncTask(alarmaDAO).execute(alarma);
    }

    public void deleteUnica(AlarmaUnica alarmaUnica){
        new DeleteAlarmaUnicaAsyncTask(alarmaDAO).execute(alarmaUnica);
    }

    public void deleteTodasAlarmas(){
        new DeleteTodasAlarmaAsyncTask(alarmaDAO).execute();
    }

    public void deleteTodasAlarmasUnicas(){
        new DeleteTodasAlarmasUnicasAsyncTask(alarmaDAO).execute();
    }

    public LiveData<List<Alarma>> getTodasAlarmas(){
        return todasAlarmas;
    }
    public LiveData<List<AlarmaUnica>> getTodasAlarmasUnicas(){
        return todasAlarmasUnicas;
    }

    // Ejecucion asincrona para que no tumbe la aplicacion
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

    public static class InsertAlarmaUnicaAsyncTask extends AsyncTask<AlarmaUnica, Void, Void>{

        private AlarmaDAO alarmaDAO;
        private InsertAlarmaUnicaAsyncTask(AlarmaDAO alarmaDAO){
            this.alarmaDAO = alarmaDAO;
        }

        @Override
        protected Void doInBackground(AlarmaUnica... alarmasUnicas) {
            alarmaDAO.insert(alarmasUnicas[0]);
            return null;
        }
    }

    // Ejecucion asincrona para que no tumbe la aplicacion
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

    public static class UpdateAlarmaUnicaAsyncTask extends AsyncTask<AlarmaUnica, Void, Void>{

        private AlarmaDAO alarmaDAO;
        private UpdateAlarmaUnicaAsyncTask(AlarmaDAO alarmaDAO){
            this.alarmaDAO = alarmaDAO;
        }

        @Override
        protected Void doInBackground(AlarmaUnica... alarmasUnicas) {
            alarmaDAO.update(alarmasUnicas[0]);
            return null;
        }
    }

    // Ejecucion asincrona para que no tumbe la aplicacion
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

    public static class DeleteAlarmaUnicaAsyncTask extends AsyncTask<AlarmaUnica, Void, Void>{

        private AlarmaDAO alarmaDAO;
        private DeleteAlarmaUnicaAsyncTask(AlarmaDAO alarmaDAO){
            this.alarmaDAO = alarmaDAO;
        }

        @Override
        protected Void doInBackground(AlarmaUnica... alarmasUnicas) {
            alarmaDAO.delete(alarmasUnicas[0]);
            return null;
        }
    }

    // Ejecucion asincrona para que no tumbe la aplicacion
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

    public static class DeleteTodasAlarmasUnicasAsyncTask extends AsyncTask<AlarmaUnica, Void, Void>{

        private AlarmaDAO alarmaDAO;
        private DeleteTodasAlarmasUnicasAsyncTask(AlarmaDAO alarmaDAO){
            this.alarmaDAO = alarmaDAO;
        }

        @Override
        protected Void doInBackground(AlarmaUnica... alarmasUnicas) {
            alarmaDAO.deleteTodasAlarmasUnicas();
            return null;
        }
    }
}
