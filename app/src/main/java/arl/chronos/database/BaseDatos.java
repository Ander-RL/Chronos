package arl.chronos.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Alarma.class}, version = 1) // @Database(entities = {Alarma.class, Nota.class}. version = 1)
public abstract class BaseDatos extends RoomDatabase{

    // Singleton. Solo se puede instanciar una vez.
    private static BaseDatos instance;

    public abstract AlarmaDAO alarmaDAO();

    // Solo un hilo por vez puede acceder a este metodo.
    public static synchronized BaseDatos getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    BaseDatos.class, "base_datos")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack) // Para poblar la BBDD en un principio.
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
            alarmaDAO.insert(new Alarma("05", "34", true, false, false, true, false, false, false, true));
            alarmaDAO.insert(new Alarma("15", "09", false, true, true, true, false, false, true, false));
            alarmaDAO.insert(new Alarma("22", "33", false, false, false, true, false, false, false, false));
            alarmaDAO.insert(new Alarma("09", "10", false, false, false, true, true, false, true, true));
            return null;
        }
    }
}
