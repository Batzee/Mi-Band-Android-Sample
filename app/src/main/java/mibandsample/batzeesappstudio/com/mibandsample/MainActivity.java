package mibandsample.batzeesappstudio.com.mibandsample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zhaoxiaodan.miband.ActionCallback;
import com.zhaoxiaodan.miband.MiBand;
import com.zhaoxiaodan.miband.listeners.NotifyListener;
import com.zhaoxiaodan.miband.model.VibrationMode;

public class MainActivity extends Activity {

    private Button connect;
    private Button vibrate;
    private Button stopVibrate;
    private Button heartRate;

    private static final String TAG = "Mi Band Test";
    private MiBand miband;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        miband = new MiBand(this);

        Object[] devices = BluetoothAdapter.getDefaultAdapter().getBondedDevices().toArray();
        final BluetoothDevice device = (BluetoothDevice) devices[0];

        connect = (Button) findViewById(R.id.buttonConnect);
        vibrate = (Button) findViewById(R.id.buttonVibrate);
        stopVibrate = (Button) findViewById(R.id.buttonStopVibrate);
        heartRate = (Button) findViewById(R.id.buttonHeartRate);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = ProgressDialog.show(MainActivity.this, "", "Connecting, Please Wait......");
                miband.connect(device, new ActionCallback() {

                    @Override
                    public void onSuccess(Object data) {
                        pd.dismiss();
                        Log.d(TAG, "Success !!!");

                        miband.setDisconnectedListener(new NotifyListener() {
                            @Override
                            public void onNotify(byte[] data) {
                                Log.d(TAG, "Disconnected!!!");
                            }
                        });
                    }

                    @Override
                    public void onFail(int errorCode, String msg) {
                        pd.dismiss();
                        Log.d(TAG, "connect fail, code:" + errorCode + ",mgs:" + msg);
                    }
                });
            }
        });

        vibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miband.startVibration(VibrationMode.VIBRATION_WITH_LED);
            }
        });

        stopVibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miband.stopVibration();
            }
        });

        heartRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miband.startHeartRateScan();
            }
        });
    }
}