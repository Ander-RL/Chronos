package arl.chronos.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alarma_tabla")
public class Alarma {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String hora;
    private String minuto;
    private Boolean lunes;
    private Boolean martes;
    private Boolean miercoles;
    private Boolean jueves;
    private Boolean viernes;
    private Boolean sabado;
    private Boolean domingo;
    private Boolean activated;

    public Alarma(String hora, String minuto, Boolean lunes, Boolean martes, Boolean miercoles, Boolean jueves, Boolean viernes, Boolean sabado, Boolean domingo, Boolean activated) {
        // id se genera automaticamente
        this.hora = hora;
        this.minuto = minuto;
        this.lunes = lunes;
        this.martes = martes;
        this.miercoles = miercoles;
        this.jueves = jueves;
        this.viernes = viernes;
        this.sabado = sabado;
        this.domingo = domingo;
        this.activated = activated;
    }
    // Porque id es el unico parametro que no esta en el constructor
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getHora() {
        return hora;
    }

    public String getMinuto() {
        return minuto;
    }

    public Boolean getLunes() {
        return lunes;
    }

    public Boolean getMartes() {
        return martes;
    }

    public Boolean getMiercoles() {
        return miercoles;
    }

    public Boolean getJueves() {
        return jueves;
    }

    public Boolean getViernes() {
        return viernes;
    }

    public Boolean getSabado() {
        return sabado;
    }

    public Boolean getDomingo() {
        return domingo;
    }

    public Boolean getActivated() {
        return activated;
    }
}
