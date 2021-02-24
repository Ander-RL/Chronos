package arl.chronos.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import arl.chronos.classes.Alarma;
import arl.chronos.classes.AlarmaUnica;

@Dao
public interface AlarmaDAO {

    @Insert
    void insert(Alarma alarma);

    @Insert
    void insert(AlarmaUnica alarmaUnica);

    @Update
    void update(Alarma alarma);

    @Update
    void update(AlarmaUnica alarmaUnica);

    @Delete
    void delete(Alarma alarma);

    @Delete
    void delete(AlarmaUnica alarmaUnica);

    @Query("DELETE FROM alarma_tabla")
    void deleteTodasAlarmas();

    @Query("DELETE FROM alarma_unica_tabla")
    void deleteTodasAlarmasUnicas();

    @Query("SELECT * FROM alarma_tabla ORDER BY hora DESC")
    LiveData<List<Alarma>> getTodasAlarmas(); // LiveData permite observar los cambios automaticamente

    @Query("SELECT * FROM alarma_unica_tabla ORDER BY hora DESC")
    LiveData<List<AlarmaUnica>> getTodasAlarmasUnicas();

}
