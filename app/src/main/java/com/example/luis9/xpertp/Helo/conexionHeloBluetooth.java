package com.example.luis9.xpertp.Helo;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luis9.xpertp.R;
import com.example.luis9.xpertp.conduccion;
import com.example.luis9.xpertp.typeWriterAnimation;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.worldgn.connector.Connector;
import com.worldgn.connector.DeviceItem;
import com.worldgn.connector.ScanCallBack;

import java.util.ArrayList;

public class conexionHeloBluetooth extends AppCompatActivity implements ScanCallBack {

    //VARIABLES
    TextView battery, conn_status, bond_status,mac,textScanning;
    Button scan, unpair,comenzar;
    MeasurementReceiver heloMeasurementReceiver;
    IntentFilter intentFilter;
    private AlertDialog alertDialog;
    private final String TAG = conexionHeloBluetooth.class.getSimpleName();
    private LinearLayout dynamic_measure_layout;
    DeviceItem deviceItem = null;
    private static final int REQUEST_ENABLED = 0;
    Handler handler,handler2;
    CountDownTimer countDownTimer;

    BluetoothAdapter bluetoothAdapter;
    PermissionListener permissionlistener;
    ProgressBar progressBar;
    SharedPreferences spConnectionHelo;
    //
    public final String BROADCAST_ACTION_APPVERSION_MEASUREMENT = "com.worldgn.connector.APPVERSION_MEASUREMENT";
    public final String BROADCAST_ACTION_HELO_DEVICE_RESET = "com.worldgn.connector.ACTION_HELO_DEVICE_RESET";
    public final String BROADCAST_ACTION_HELO_CONNECTED = "com.worldgn.connector.ACTION_HELO_CONNECTED";
    public final String BROADCAST_ACTION_HELO_DISCONNECTED = "com.worldgn.connector.ACTION_HELO_DISCONNECTED";
    public final String BROADCAST_ACTION_HELO_BONDED = "com.worldgn.connector.ACTION_HELO_BONDED";
    public final String BROADCAST_ACTION_HELO_UNBONDED = "com.worldgn.connector.ACTION_HELO_UNBONDED";
    public final String BROADCAST_ACTION_HELO_FIRMWARE = "com.worldgn.connector.ACTION_HELO_FIRMWARE";
    public final String BROADCAST_ACTION_MEASUREMENT_WRITE_FAILURE = "com.worldgn.connector.MEASURE_WRITE_FAILURE";
    public final String BROADCAST_ACTION_BATTERY = "com.worldgn.connector.BATTERY";
    public  final String INTENT_KEY_BATTERY = "BATTERY";
    public  final String INTENT_KEY_FIRMWARE = "HELO_FIRMWARE";
    public  final String INTENT_KEY_MAC = "HELO_MAC";
    public  final String INTENT_MEASUREMENT_WRITE_FAILURE = "MEASUREMENT_WRITE_FAILURE";
    //

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conexion_helo_bluetooth);
        spConnectionHelo = PreferenceManager.getDefaultSharedPreferences(this);
        //BLUETOOTH
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        handler = new Handler();
        handler2 = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bluetoothAdapter.disable();
            }
        },1000);
        //
        //CAST
        textScanning = findViewById(R.id.textScanning);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        scan = (Button) findViewById(R.id.scan);
        unpair = (Button) findViewById(R.id.unpair);
        comenzar = findViewById(R.id.buttonEmpezar);
        mac = (TextView)findViewById(R.id.mac);
        dynamic_measure_layout = (LinearLayout) findViewById(R.id.buttons);
        battery = (TextView) findViewById(R.id.battery);
        conn_status = (TextView) findViewById(R.id.conn_status);
        bond_status = (TextView) findViewById(R.id.bond_status);
        heloMeasurementReceiver = new MeasurementReceiver();
        //
        intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION_APPVERSION_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_BATTERY);
        intentFilter.addAction(BROADCAST_ACTION_HELO_CONNECTED);
        intentFilter.addAction(BROADCAST_ACTION_HELO_DISCONNECTED);
        intentFilter.addAction(BROADCAST_ACTION_HELO_DEVICE_RESET);
        intentFilter.addAction(BROADCAST_ACTION_HELO_BONDED);
        intentFilter.addAction(BROADCAST_ACTION_HELO_UNBONDED);
        intentFilter.addAction(BROADCAST_ACTION_HELO_FIRMWARE);
        intentFilter.addAction(BROADCAST_ACTION_HELO_DEVICE_RESET);
        intentFilter.addAction(BROADCAST_ACTION_MEASUREMENT_WRITE_FAILURE);
        //
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.tConectarBle)
                .setCancelable(false)
                .setPositiveButton(R.string.Si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        connectBleDevice();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });

        //Creating dialog box
        alertDialog = builder.create();


        permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(conexionHeloBluetooth.this, R.string.PermisoConcedido, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(conexionHeloBluetooth.this, R.string.PermisoDenegado + "\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT)
                        .show();
            }


        };
        checkBTenabled();
        if(isGpsEnabled()) {
            checkLocationPermission();
        } else {
            buildAlertMessageNoGps();
        }

        //AUTOMATIC STATUSES
        /*if(spConnectionHelo.getBoolean("connected",false)){
            conn_status.setText(R.string.Conectado);
            bond_status.setText(R.string.Enlazado);
            mac.setText(spConnectionHelo.getString("mac",getString(R.string.tConexionMac)));
            battery.setText(spConnectionHelo.getString("battery",getString(R.string.tConexionMac)));
            conn_status.setTextColor(Color.GREEN);
            bond_status.setTextColor(Color.GREEN);
            mac.setTextColor(Color.GREEN);
            battery.setTextColor(Color.GREEN);
        }*/
    }
    private void checkLocationPermission() {
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleTitle(R.string.rationale_title)
                .setRationaleMessage(R.string.rationale_message)
                .setDeniedTitle("Permiso denegado")
                .setDeniedMessage(
                        "Si rechaza el permiso, no puede usar este servicio\n\nPor favor active los permisos en [Setting] > [Permission]")
                .setGotoSettingButtonText("bla bla")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();
    }

    private boolean isGpsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        } else {

            return true;
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.Ubicacion)
                .setCancelable(false)
                .setPositiveButton(R.string.Si, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 102);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        Toast.makeText(conexionHeloBluetooth.this, R.string.UbicacionMensaje, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



    private void checkBTenabled() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(this, R.string.bluetoothNo, Toast.LENGTH_LONG).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                // Bluetooth is not enable :)
                Toast.makeText(this, R.string.bluetoothOff, Toast.LENGTH_LONG).show();
                mBluetoothAdapter.enable();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(heloMeasurementReceiver, intentFilter);
    }

    @Override
    public void onScanStarted() {
        progressBar.setVisibility(View.VISIBLE);
        textScanning.setVisibility(View.VISIBLE);
    }

    @Override
    public void onScanFinished() {
        progressBar.setVisibility(View.INVISIBLE);
        textScanning.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLedeviceFound(DeviceItem deviceItem) {
        progressBar.setVisibility(View.INVISIBLE);
        textScanning.setVisibility(View.INVISIBLE);
        this.deviceItem = deviceItem;
        alertDialog.show();
    }
    @Override
    public void onPairedDeviceNotFound() {
        Toast.makeText(this, R.string.DispositivoNoEncontrado, Toast.LENGTH_LONG).show();
    }

    //
    public class MeasurementReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null && intent.getAction() != null) {

                if (intent.getAction().equals(BROADCAST_ACTION_BATTERY)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int batteryVal = intent.getIntExtra(INTENT_KEY_BATTERY, -1);
                            battery.setText(Integer.toString(batteryVal));
                            battery.setTextColor(Color.GREEN);
                            //
                            SharedPreferences.Editor spConnectionHeloEditor = spConnectionHelo.edit();
                            spConnectionHeloEditor.putString("battery",Integer.toString(batteryVal));
                            spConnectionHeloEditor.apply();
                            //
                        }
                    });

                } else if (intent.getAction().equals(BROADCAST_ACTION_HELO_DISCONNECTED)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            conn_status.setText(R.string.tConexionDesconectado);
                            mac.setText("");
                            battery.setText("");
                            conn_status.setTextColor(Color.WHITE);
                            mac.setTextColor(Color.WHITE);
                            battery.setTextColor(Color.WHITE);
                            comenzar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EF9A9A")));
                            SharedPreferences.Editor spConnectionHeloEditor = spConnectionHelo.edit();
                            spConnectionHeloEditor.putBoolean("connected",false);
                            spConnectionHeloEditor.apply();
                        }
                    });
                } else if (intent.getAction().equals(BROADCAST_ACTION_HELO_CONNECTED)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            conn_status.setText(R.string.Conectado);
                            conn_status.setTextColor(Color.GREEN);
                            mac.setText(intent.getStringExtra(INTENT_KEY_MAC));
                            mac.setTextColor(Color.GREEN);
                            comenzar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#43cd80")));
                            SharedPreferences.Editor spConnectionHeloEditor = spConnectionHelo.edit();
                            spConnectionHeloEditor.putString("mac",mac.getText().toString());
                            spConnectionHeloEditor.putBoolean("connected",true);
                            spConnectionHeloEditor.apply();

                        }
                    });
                } else if (intent.getAction().equals(BROADCAST_ACTION_HELO_DEVICE_RESET)) {
                    Toast.makeText(conexionHeloBluetooth.this, "Restart Helo device by pressing 8 seconds", Toast.LENGTH_LONG).show();
                } else if (intent.getAction().equals(BROADCAST_ACTION_HELO_BONDED)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bond_status.setText(R.string.Enlazado);
                            bond_status.setTextColor(Color.GREEN);
                        }
                    });
                }else if (intent.getAction().equals(BROADCAST_ACTION_HELO_UNBONDED)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bond_status.setText(R.string.tEnlaceDesenlazado);
                            bond_status.setTextColor(Color.WHITE);
                        }
                    });
                } else if(intent.getAction().equals(BROADCAST_ACTION_MEASUREMENT_WRITE_FAILURE)) {
                    String message = intent.getStringExtra(INTENT_MEASUREMENT_WRITE_FAILURE);
                    Toast.makeText(conexionHeloBluetooth.this, message, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void scan(View view) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, REQUEST_ENABLED);
    }


    private void connectBleDevice() {
        Connector.getInstance().connect(deviceItem);
    }

    public void comenzar(View view){
        if (!conn_status.getText().toString().equals("Conectado")){
           Snackbar snackbar = Snackbar.make(findViewById(R.id.conexionBlue), R.string.DispositivoNoConectado, Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            startActivity(new Intent(conexionHeloBluetooth.this, conduccion.class));
        }
    }

    public void unpair(View view) {
        scan.setEnabled(true);
        Connector.getInstance().unbindDevice();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                bluetoothAdapter.disable();
            }
        },500);

        progressBar.setVisibility(View.INVISIBLE);
        textScanning.setVisibility(View.INVISIBLE);
        bond_status.setText(R.string.tEnlaceDesenlazado);
        conn_status.setText(R.string.tConexionDesconectado);
        battery.setText("");
        conn_status.setTextColor(Color.WHITE);
        mac.setTextColor(Color.WHITE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 102:
                if(isGpsEnabled()) {
                    checkLocationPermission();
                } else {
                    Toast.makeText(conexionHeloBluetooth.this, R.string.GPSenciende, Toast.LENGTH_LONG).show();
                }
            case 0:
                if (bluetoothAdapter.isEnabled()) {
                    Connector.getInstance().scan(this);
                }
                break;
        }
    }

}
