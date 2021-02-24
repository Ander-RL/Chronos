package arl.chronos.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import arl.chronos.classes.Alarma;
import arl.chronos.classes.AlarmaUnica;
import arl.chronos.classes.Crono;

@Database(entities = {Alarma.class, AlarmaUnica.class, Crono.class}, version = 1, exportSchema = false)
public abstract class BaseDatos extends RoomDatabase{

    // Singleton. Solo se puede instanciar una vez.
    private static BaseDatos instance;
    // Metodo Getter abstracto;
    public abstract AlarmaDAO alarmaDAO(); // El codigo de este metodo es autogenerado por Room en el metodo que construye el singleton de la base de datos (getInstance).
    public abstract CronoDAO cronoDAO();

    // Solo un hilo por vez puede acceder a este metodo.
    public static synchronized BaseDatos getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    BaseDatos.class, "base_datos")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack) // Para poblar la BBDD en un principio (para testear la app).
                    .build();
        }
        return instance;
    }

    // Para tener datos de partida y poder debugear la aplicacion.
    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{

        private AlarmaDAO alarmaDAO;
        public PopulateDbAsyncTask(BaseDatos db){
            alarmaDAO = db.alarmaDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            alarmaDAO.insert(new Alarma("05", "34", true, false, false, true, false, false, false, true, "", "", false));
            alarmaDAO.insert(new Alarma("15", "09", false, true, true, true, false, false, true, false, "", "", false));
            alarmaDAO.insert(new AlarmaUnica("15", "09", "2021", "02", "22", false, "", "", false));
            alarmaDAO.insert(new AlarmaUnica("15", "09", "2021", "03", "22", true, "", "", false));
            return null;
        }
    }
}
