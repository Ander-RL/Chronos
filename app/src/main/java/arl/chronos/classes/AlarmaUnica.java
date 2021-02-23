package arl.chronos.classes;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alarma_unica_tabla")
public class AlarmaUnica {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String hora;
    private String minuto;
    private String ano;
    private String mes;
    private String dia;
    private Boolean activated;
    private String nombreSonido;
    private String sonidoUri;
    private Boolean sonar;

    public AlarmaUnica(String hora, String minuto, String ano, String mes, String dia, Boolean activated, String nombreSonido, String sonidoUri, Boolean sonar) {
        this.hora = hora;
        this.minuto = minuto;
        this.ano = ano;
        this.mes = mes;
        this.dia = dia;
        this.activated = activated;
        this.nombreSonido = nombreSonido;
        this.sonidoUri = sonidoUri;
        this.sonar = sonar;
    }

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

    public String getAno() {
        return ano;
    }

    public String getMes() {
        return mes;
    }

    public String getDia() {
        return dia;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Boolean getActivated() {
        return activated;
    }

    public String getNombreSonido() {
        return nombreSonido;
    }

    public String getSonidoUri() {
        return sonidoUri;
    }

    public Boolean getSonar() {
        return sonar;
    }
}
