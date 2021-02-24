package arl.chronos.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import arl.chronos.classes.Crono;

@Dao
public interface CronoDAO {

    @Insert
    void insert(Crono crono);

    @Delete
    void delete(Crono crono);

    @Query("SELECT * FROM crono_tabla")
    LiveData<List<Crono>> getTodoCronos();

    @Query("DELETE FROM crono_tabla")
    void deleteTodoCronos();
}
