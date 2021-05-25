package arl.chronos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import arl.chronos.adapters.PagerAdapter;
import arl.chronos.classes.Alarma;
import arl.chronos.classes.AlarmaUnica;
import arl.chronos.classes.MyApplication;
import arl.chronos.database.AlarmaDAO;
import arl.chronos.database.BaseDatos;
import arl.chronos.database.MyViewModel;
import arl.chronos.receiver.AlarmReceiver;
import arl.chronos.receiver.AlertReceiver;

public class MainActivity extends AppCompatActivity {

    private MyViewModel myViewModel;
    private Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myContext = this;

        // Borrar el titulo de la Appbar.
        setTitle("");

        // Se coloca la Toolbar.
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Instancia de TabLayout de activity_main.
        TabLayout tabLayout = findViewById(R.id.tablayout);
        // Se añaden las tabs al tablayout y se asigna un titulo a cada una.
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tabfragment_alarmas));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tabfragment_crono));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tabfragment_calendario));
        // Se establece que las tabs ocupen el espacio completo del tablayout.
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Se usa PagerAdapter para gestionar las diferentes paginas/fragmentos
        ViewPager viewPager = findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        // Alojamos los tabfragments en el layout que nos permite movernos de izq a derecha
        viewPager.setAdapter(pagerAdapter); // El adaptador gestionara el cambio de un tab a otro

        // Se establecen los listeners para los clicks
        // Cuando se toca el layout del viewpager, se llama al tablayout
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        // Cuando se llama al tablayout o se toca en uno de los tabs
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
    }

    // Gestionamos el menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.borrar_todas_alarmas_menu:

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        BaseDatos baseDatos = BaseDatos.getInstance(MyApplication.get());
                        AlarmaDAO alarmaDAO = baseDatos.alarmaDAO();

                        ArrayList<Alarma> alarmas = (ArrayList<Alarma>) alarmaDAO.getTodaAlarma();
                        ArrayList<AlarmaUnica> alarmasUnicas = (ArrayList<AlarmaUnica>) alarmaDAO.getTodaAlarmaUnica();

                        for(Alarma alarma : alarmas) {
                            Log.d("Cancel", "bucle for");
                            Log.d("Cancel", "id -> " + alarma.getId());
                            cancelAlarma(alarma.getId(), myContext);
                        }
                        for(AlarmaUnica alarma : alarmasUnicas) {
                            Log.d("Cancel", "bucle for");
                            Log.d("Cancel", "id -> " + alarma.getId());
                            cancelAlarma(alarma.getId(), myContext);
                        }

                        myViewModel.deleteAll();
                        myViewModel.deleteAllUnica();
                    }
                };

                thread.start();
                thread.interrupt();

                Snackbar.make(getWindow().getDecorView().getRootView(), (R.string.eliminadas_todas_alarmas), Snackbar.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cancelAlarma(int code, Context context) {
        Log.d("Cancel", "cancelAlarma");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, code, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        alarmManager.cancel(pendingIntent);

        AlarmManager alarmManager2 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent2 = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, code, intent2, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        alarmManager2.cancel(pendingIntent2);
    }
}