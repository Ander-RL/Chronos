package arl.chronos.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import arl.chronos.classes.Alarma;

// @Database(entities = {Alarma.class, Nota.class}. version = 1) ExportSchema sirve para exportar la BBDD a un fichero para hacer version control.
@Database(entities = {Alarma.class}, version = 2, exportSchema = false)
public abstract class BaseDatos extends RoomDatabase{

    // Singleton. Solo se puede instanciar una vez.
    private static BaseDatos instance;
    // Metodo abstracto;
    public abstract AlarmaDAO alarmaDAO(); // El codigo de este metodo es autogenerado por Room en el metodo que construye el singleton de la base de datos (getInstance).

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
            alarmaDAO.insert(new Alarma("05", "34", true, false, false, true, false, false, false, true, null, null, false));
            alarmaDAO.insert(new Alarma("15", "09", false, true, true, true, false, false, true, false, null, null, false));
            return null;
        }
    }
}
