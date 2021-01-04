package arl.chronos.classes;

import android.content.Context;
import android.os.Vibrator;

public class Vibrador implements Runnable {
    private boolean vibrar;
    private Context context;
    private Vibrator vibrator;

    public Vibrador(Context context) {
        this.context = context;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public boolean isVibrar() {
        return vibrar;
    }

    public void setVibrar(boolean vibrar) {
        this.vibrar = vibrar;
    }

    public void vibrar(){
        vibrator.vibrate(new long[]{500, 1000, 500, 1000}, 0);
    }

    @Override
    public void run() {
        if (vibrar) {
            while (vibrar) {
                vibrator.vibrate(new long[]{500, 1000, 500, 1000}, 0);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                vibrar = isVibrar();
            }
        }
        vibrator.cancel();
    }
}
