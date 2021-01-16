package arl.chronos.classes;


import android.net.Uri;

public class Sonido {
    private final Uri uri;
    private final String nombre;

    public Sonido (Uri uri, String nombre) {
        this.uri = uri;
        this.nombre = nombre;
    }

    public Uri getUri() {
        return uri;
    }

    public String getNombre() {
        return nombre;
    }
}
