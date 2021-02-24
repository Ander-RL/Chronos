package arl.chronos.classes;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "crono_tabla")
public class Crono {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String horas;
    private String minutos;
    private String segundos;

    public Crono(String horas, String minutos, String segundos){
        this.horas = horas;
        this.minutos = minutos;
        this.segundos = segundos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getHoras() {
        return horas;
    }

    public String getMinutos() {
        return minutos;
    }

    public String getSegundos() {
        return segundos;
    }
}
