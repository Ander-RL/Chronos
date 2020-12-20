package arl.chronos.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import arl.chronos.classes.Alarma;

@Dao
public interface AlarmaDAO {

    @Insert
    void insert(Alarma alarma);

    @Update
    void update(Alarma alarma);

    @Delete
    void delete(Alarma alarma);

    @Query("DELETE FROM alarma_tabla")
    void deleteTodasAlarmas();

    @Query("SELECT * FROM alarma_tabla ORDER BY hora DESC")
    LiveData<List<Alarma>> getTodasAlarmas(); // LiveData permite observar los cambios automaticamente

}
