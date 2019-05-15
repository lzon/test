package coms.example.administrator.ble;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import coms.example.administrator.ble.mode.KeyValue;
import coms.example.administrator.ble.mode.MapJianWei;

public class MainActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "UUIUI____";

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }

    RelativeLayout relativeLayoutMain;
    private OrientationEventListener mOrientationListener;
    private MapJianWei mapJianWei;
    private List<KeyValue> listKeyValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapJianWei = new MapJianWei();
        mapJianWei.setCompositeMode("1");
        mapJianWei.setDescription("xx");
        mapJianWei.setTemplateName("0");
        mapJianWei.setFooViewAlpha("alpha");
        listKeyValue = new ArrayList<>();
        init();
        mOrientationListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                Log.v(DEBUG_TAG, "Orientation changed to " + orientation);
            }
        };
        if (mOrientationListener.canDetectOrientation()) {
            Log.v(DEBUG_TAG, "Can detect orientation");
            mOrientationListener.enable();
        } else {
            Log.v(DEBUG_TAG, "Cannot detect orientation");
            mOrientationListener.disable();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 在这里添加屏幕切换后的操作
        System.out.println("newConfig. = " + newConfig.orientation);
        if (mBluetoothGatt != null && isServiceConnected) {
            BluetoothGattService gattService = mBluetoothGatt.getService(UUID_SERVICE);
            BluetoothGattCharacteristic characteristic = gattService.getCharacteristic(UUID_WRITE);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int width = metrics.widthPixels;
            int hight = metrics.widthPixels;

            byte[] rAndO = new byte[19];
            rAndO[0] = 0x10;
            //分辨率，⻓长度的数据
            rAndO[1] = intToByte4(width)[0];
            rAndO[2] = intToByte4(width)[1];
            rAndO[3] = intToByte4(hight)[0];
            rAndO[4] = intToByte4(hight)[1];
            // 0： 表示屏幕没有旋转
            // 1： 表示屏幕有旋转
            if (newConfig.orientation == 1) { //竖屏
                rAndO[5] = 1;
            }
            if (newConfig.orientation == 2) { //
                rAndO[5] = 0;
            }
            characteristic.setValue(rAndO);
            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
            mBluetoothGatt.writeCharacteristic(characteristic);
        }
    }


    private void init() {
        deviceName = (TextView) findViewById(R.id.device_name);
        textView1 = (TextView) findViewById(R.id.recieve_text);
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        //蓝牙设备不可以用， 就是没有开， 跳到开启界面去开启。
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
    }


    //点击确定发给蓝牙发送消息
    public void onClickQueDing(View view) {
        sendDateBluetooth(mapJianWei);
    }

    public int StringCount(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            String s = String.valueOf(str.charAt(i));
            if (s.equals("_")) {
                count++;
            }
        }
        return count;
    }

    private void sendDateBluetooth(MapJianWei mapJianWei) {
        if (mBluetoothGatt != null && isServiceConnected) {
            BluetoothGattService gattService = mBluetoothGatt.getService(UUID_SERVICE);
            BluetoothGattCharacteristic characteristic = gattService.getCharacteristic(UUID_WRITE);
            if (mapJianWei != null) {
                List<KeyValue> listValues = mapJianWei.getKeyTemplate().getList();
                for (int i = 0; i < listValues.size(); i++) {
                    KeyValue keyValue = listValues.get(i);
                    if (keyValue != null) {
                        if (StringCount(keyValue.getKeyName()) >= 2) { //zuhe
                            byte[] keyMap = new byte[19];
                            keyMap[0] = 0x13;
                            //按键值
                            byte x = getKey(keyValue.getKeyName());
                            keyMap[1] = x;
                            //按键坐标x
                            int px = keyValue.getPostion().getX();
                            int py = keyValue.getPostion().getY();
                            keyMap[2] = intToByte4(px)[0];
                            keyMap[3] = intToByte4(px)[1];
                            //按键坐标y
                            keyMap[4] = intToByte4(py)[0];
                            keyMap[5] = intToByte4(py)[1];
                            //属性值：  1：点击
                            keyMap[6] = 1;
                            characteristic.setValue(keyMap);
                            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                            mBluetoothGatt.writeCharacteristic(characteristic);
                        } else {
                            byte[] keyMap = new byte[19];
                            keyMap[0] = 0x11;
                            //按键值
                            byte x = getKey(keyValue.getKeyName());
                            keyMap[1] = x;
                            //按键坐标x
                            int px = keyValue.getPostion().getX();
                            int py = keyValue.getPostion().getY();
                            keyMap[2] = intToByte4(px)[0];
                            keyMap[3] = intToByte4(px)[1];
                            //按键坐标y
                            keyMap[4] = intToByte4(py)[0];
                            keyMap[5] = intToByte4(py)[1];
                            //属性值：  1：点击
                            keyMap[6] = 1;
                            characteristic.setValue(keyMap);
                            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                            mBluetoothGatt.writeCharacteristic(characteristic);
                        }
                    }
                }
            } else return;

            /*
             * 3) 摇杆映射数据(UUID :0x7F12)
             */


            /*
             *4) 组合按键映射(UUID :0x7F12)
                 *            组合按键值：
                            按键                             值
                            L1+A                             1
                            L1+B                             2
                            L1+X                             3
                            L1+Y                             4

             */
            byte[] zuHeMap = new byte[19];
            zuHeMap[0] = 0x13;
            zuHeMap[1] = 0x13;  //按键值
            zuHeMap[2] = intToByte4(100)[0];//按键坐标 X
            zuHeMap[3] = intToByte4(100)[1];//按键坐标 x
            zuHeMap[4] = intToByte4(100)[0];//按键坐标 y
            zuHeMap[5] = intToByte4(100)[1];//按键坐标 y
          /*  属性值：
            1：点击
            2：关联摇杆
            3：关联划屏*/
            zuHeMap[6] = 1;
            zuHeMap[7] = 0;//0表示单次点击;
            zuHeMap[8] = 0;  //为0时表示关联右摇杆，为1时表示左摇杆
            zuHeMap[9] = 0x13;
            zuHeMap[10] = 0x13;

            byte[] rAndO = new byte[20];
            rAndO[0] = 0x10;
            //分辨率，⻓长度的数据
            rAndO[1] = intToByte4(2960)[0];
            rAndO[2] = intToByte4(2960)[1];
//            分辨率，宽度的数据 (2 Bytes
            rAndO[3] = intToByte4(1440)[0];
            rAndO[4] = intToByte4(1440)[1];
//            0： 表示屏幕没有旋转
//            1： 表示屏幕有旋转
            rAndO[5] = 0;
            rAndO[7] = 0x10;
            rAndO[8] = 0x10;
            rAndO[9] = 0x10;
            rAndO[10] = 0x10;
            rAndO[11] = 0x10;

            byte[] rAnd111 = new byte[20];
            rAndO[0] = 0x11;
            //分辨率，⻓长度的数据
            rAndO[1] = 1;
            rAndO[2] = intToByte4(900)[0];
            rAndO[3] = intToByte4(900)[1];
//            分辨率，宽度的数据 (2 Bytes
            rAndO[4] = intToByte4(600)[0];
            rAndO[5] = intToByte4(600)[1];
//            0： 表示屏幕没有旋转
//            1： 表示屏幕有旋转
            rAndO[6] = 1;
            rAndO[7] = 0;
            rAndO[8] = 0;
            rAndO[9] = 0;
            rAndO[10] = 0;
            rAndO[11] = 0;

            characteristic.setValue(rAnd111);
            characteristic.setValue(rAndO);
            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
            mBluetoothGatt.writeCharacteristic(characteristic);


            /**
             *  1)  发送屏幕分辨率和转向
             */
            byte[] rAndO0 = new byte[19];
            rAndO[0] = 0x10;
            //分辨率，⻓长度的数据
            rAndO[1] = intToByte4(1080)[0];
            rAndO[2] = intToByte4(1080)[1];
//            分辨率，宽度的数据 (2 Bytes
            rAndO[3] = intToByte4(1920)[0];
            rAndO[4] = intToByte4(1920)[1];
//            0： 表示屏幕没有旋转
//            1： 表示屏幕有旋转
            rAndO[5] = 01;
            rAndO[7] = 0x10;
            rAndO[8] = 0x10;
            rAndO[9] = 0x10;
            rAndO[10] = 0x10;
            rAndO[11] = 0x10;

            /**
             * 2) 　按键映射数据(UUID :0x7F12)
             */
            byte[] keyMap = new byte[19];
            keyMap[0] = 0x11;
            //按键值
            keyMap[1] = 1;
            //按键坐标x
            keyMap[2] = intToByte4(50)[0];
            keyMap[3] = intToByte4(50)[1];
            //按键坐标y
            keyMap[4] = intToByte4(50)[0];
            keyMap[5] = intToByte4(50)[0];
            //属性值：  1：点击
            keyMap[6] = 0;

            /*
             * 3) 摇杆映射数据(UUID :0x7F12)
             */


            /*
             *4) 组合按键映射(UUID :0x7F12)
                 *            组合按键值：
                            按键                             值
                            L1+A                             1
                            L1+B                             2
                            L1+X                             3
                            L1+Y                             4

             */
            byte[] zuHeMapd = new byte[19];
            zuHeMap[0] = 0x13;
            zuHeMap[1] = 0x13;  //按键值
            zuHeMap[2] = intToByte4(100)[0];//按键坐标 X
            zuHeMap[3] = intToByte4(100)[1];//按键坐标 x
            zuHeMap[4] = intToByte4(100)[0];//按键坐标 y
            zuHeMap[5] = intToByte4(100)[1];//按键坐标 y
          /*  属性值：
            1：点击
            2：关联摇杆
            3：关联划屏*/
            zuHeMap[6] = 1;
            zuHeMap[7] = 0;//0表示单次点击;
            zuHeMap[8] = 0;  //为0时表示关联右摇杆，为1时表示左摇杆
            zuHeMap[9] = 0x13;
            zuHeMap[10] = 0x13;
        }
    }

    private byte getKey(String keyName) {
        byte a = 0;
        if (keyName.equals("KEY_A")) {
            a = 1;
        }
        if (keyName.equals("KEY_B")) {
            a = 2;
        }
        if (keyName.equals("KEY_X")) {
            a = 3;
        }
        if (keyName.equals("KEY_Y")) {
            a = 4;
        }
        if (keyName.equals("KEY_UP")) {
            a = 5;
        }
        if (keyName.equals("KEY_DOWN")) {
            a = 6;
        }
        if (keyName.equals("KEY_LEFT")) {
            a = 7;
        }
        if (keyName.equals("KEY_RIGHT")) {
            a = 8;
        }
        if (keyName.equals("KEY_L1")) {
            a = 9;
        }
        if (keyName.equals("KEY_R1")) {
            a = 0x0A;
        }
        if (keyName.equals("KEY_LT")) {
            a = 0x0B;
        }
        if (keyName.equals("KEY_RT")) {
            a = 0x0C;
        }
        if (keyName.equals("KEY_M1")) {
            a = 0x0D;
        }
        if (keyName.equals("KEY_M2")) {
            a = 0x0E;
        }
        if (keyName.equals("KEY_M3")) {
            a = 0x0F;
        }
        if (keyName.equals("KEY_M4")) {
            a = 0x10;
        }
        if (keyName.equals("KEY_C1")) {
            a = 0x11;
        }
        if (keyName.equals("KEY_C2")) {
            a = 0x12;
        }
        if (keyName.equals("KEY_MOHE")) {
            a = 0x13;
        }
        //组合
        if (keyName.equals("KEY_L1_A")) {
            a = 1;
        }
        if (keyName.equals("KEY_L1_B")) {
            a = 2;
        }
        if (keyName.equals("KEY_L1_X")) {
            a = 3;
        }
        if (keyName.equals("KEY_L1_Y")) {
            a = 4;
        }
        if (keyName.equals("KEY_L2_A")) {
            a = 5;
        }
        if (keyName.equals("KEY_L2_B")) {
            a = 6;
        }
        if (keyName.equals("KEY_L2_X")) {
            a = 7;
        }
        if (keyName.equals("KEY_L2_Y")) {
            a = 8;
        }
        if (keyName.equals("KEY_R1_A")) {
            a = 9;
        }
        if (keyName.equals("KEY_R1_B")) {
            a = 0x0A;
        }
        if (keyName.equals("KEY_R1_X")) {
            a = 0x0B;
        }
        if (keyName.equals("KEY_R1_Y")) {
            a = 0x0C;
        }
        if (keyName.equals("KEY_R2_A")) {
            a = 0x0D;
        }
        if (keyName.equals("KEY_R2_B")) {
            a = 0x0E;
        }
        if (keyName.equals("KEY_R2_X")) {
            a = 0x0F;
        }
        if (keyName.equals("KEY_R2_Y")) {
            a = 0x10;
        }
        if (keyName.equals("KEY_M1_A")) {
            a = 0x11;
        }
        if (keyName.equals("KEY_M1_B")) {
            a = 0x12;
        }
        if (keyName.equals("KEY_M1_X")) {
            a = 0x13;
        }
        if (keyName.equals("KEY_M1_Y")) {
            a = 0x14;
        }
        if (keyName.equals("KEY_M2_A")) {
            a = 0x15;
        }
        if (keyName.equals("KEY_M2_B")) {
            a = 0x16;
        }
        if (keyName.equals("KEY_M2_X")) {
            a = 0x17;
        }
        if (keyName.equals("KEY_M2_Y")) {
            a = 0x18;
        }
        if (keyName.equals("KEY_M3_A")) {
            a = 0x19;
        }
        if (keyName.equals("KEY_M3_B")) {
            a = 0x1A;
        }
        if (keyName.equals("KEY_M3_X")) {
            a = 0x1B;
        }
        if (keyName.equals("KEY_M3_Y")) {
            a = 0x1C;
        }
        if (keyName.equals("KEY_M4_A")) {
            a = 0x1D;
        }
        if (keyName.equals("KEY_M4_B")) {
            a = 0x1F;
        }
        if (keyName.equals("KEY_M4_X")) {
            a = 0x20;
        }
        if (keyName.equals("KEY_M4_Y")) {
            a = 0x21;
        }
        return a;
    }

    @Override
    public void setTurnScreenOn(boolean turnScreenOn) {
        super.setTurnScreenOn(turnScreenOn);
    }


    @Override
    protected void onDestroy() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
        }
        super.onDestroy();
    }

    /**
     * 判断当前界面是否是桌面
     */
    private boolean isHome() {
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return getHomes().contains(rti.get(0).topActivity.getPackageName());
    }

    /**
     * 获得属于桌面的应用的应用包名称
     *
     * @return 返回包含所有包名的字符串列表
     */
    private List<String> getHomes() {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }


    public void onClickGuanBi(View view) {
        view.setBackgroundResource(R.drawable.img_button_blue);
        findViewById(R.id.queding).setBackgroundResource(R.drawable.img_button_grade);
    }




    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler = new Handler();
    private boolean mScanning;

    final UUID UUID_SERVICE = UUID.fromString("00007F10-0000-1000-8000-00805F9B34FB");
    //  设备特征值UUID, 需固件配合同时修改
    final UUID UUID_WRITE = UUID.fromString("00007F12-0000-1000-8000-00805F9B34FB");  // 用于发送数据到设备
    final UUID UUID_NOTIFICATION = UUID.fromString("00007F13-0000-1000-8000-00805F9B34FB"); // 用于接收设备推送的数据
    final UUID UUID_NOTIFICATION_START = UUID.fromString("00007F11-0000-1000-8000-00805F9B34FB"); // 用于接收设备推送的数据

    private BluetoothGatt mBluetoothGatt;
    private TextView deviceName;
    private TextView textView1;
    private BluetoothDevice mDevice;

    final BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.d("haha", "onLeScan:  " + device.getName() + " uuid:" + device.getUuids() + " : " + rssi);
            String name = device.getName();
            if (name != null) {
                deviceName.setText(name);
                if (name.contains("SMOS-P1")) {
                    mDevice = device;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }
        }

    };

    public void startScan(View view) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScanning = false;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }, 30000);
        mScanning = true;
        // 定义一个回调接口供扫描结束处理
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

    private String TAG = "haha";
    private boolean isServiceConnected;


    public void startConnect(View view) {
        if (mDevice != null) {
            if (mBluetoothGatt != null) {
                mBluetoothGatt.disconnect();
                mBluetoothGatt.close();
                mBluetoothGatt = null;
            }
            mBluetoothGatt = mDevice.connectGatt(MainActivity.this, false, mGattCallback);
        }
    }

    public void startSend() {
        if (mBluetoothGatt != null && isServiceConnected) {
            BluetoothGattService gattService = mBluetoothGatt.getService(UUID_SERVICE);
            BluetoothGattCharacteristic characteristic = gattService.getCharacteristic(UUID_WRITE);

//            byte[] bytes = new byte[20];
//            bytes[0] = 0x0A;
//            bytes[1] = 0x04;
//            bytes[2] = 0x00;
//            bytes[3] = 0x65;

            byte[] rAndO = new byte[20];
            rAndO[0] = 0x10;
            //分辨率，⻓长度的数据
            rAndO[1] = intToByte4(2960)[0];
            rAndO[2] = intToByte4(2960)[1];
//            分辨率，宽度的数据 (2 Bytes
            rAndO[3] = intToByte4(1440)[0];
            rAndO[4] = intToByte4(1440)[1];
//            0： 表示屏幕没有旋转
//            1： 表示屏幕有旋转
            rAndO[5] = 0;
            rAndO[7] = 0x10;
            rAndO[8] = 0x10;
            rAndO[9] = 0x10;
            rAndO[10] = 0x10;
            rAndO[11] = 0x10;

            byte[] rAnd111 = new byte[20];
            rAndO[0] = 0x11;
            //分辨率，⻓长度的数据
            rAndO[1] = 1;
            rAndO[2] = intToByte4(900)[0];
            rAndO[3] = intToByte4(900)[1];
//            分辨率，宽度的数据 (2 Bytes
            rAndO[4] = intToByte4(600)[0];
            rAndO[5] = intToByte4(600)[1];
//            0： 表示屏幕没有旋转
//            1： 表示屏幕有旋转
            rAndO[6] = 1;
            rAndO[7] = 0;
            rAndO[8] = 0;
            rAndO[9] = 0;
            rAndO[10] = 0;
            rAndO[11] = 0;

            characteristic.setValue(rAnd111);
            characteristic.setValue(rAndO);
            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
            mBluetoothGatt.writeCharacteristic(characteristic);


            /**
             *  1)  发送屏幕分辨率和转向
             */
            byte[] rAndO0 = new byte[19];
            rAndO[0] = 0x10;
            //分辨率，⻓长度的数据
            rAndO[1] = intToByte4(1080)[0];
            rAndO[2] = intToByte4(1080)[1];
//            分辨率，宽度的数据 (2 Bytes
            rAndO[3] = intToByte4(1920)[0];
            rAndO[4] = intToByte4(1920)[1];
//            0： 表示屏幕没有旋转
//            1： 表示屏幕有旋转
            rAndO[5] = 01;
            rAndO[7] = 0x10;
            rAndO[8] = 0x10;
            rAndO[9] = 0x10;
            rAndO[10] = 0x10;
            rAndO[11] = 0x10;

            /**
             * 2) 　按键映射数据(UUID :0x7F12)
             */
            byte[] keyMap = new byte[19];
            keyMap[0] = 0x11;
            //按键值
            keyMap[1] = 1;
            //按键坐标x
            keyMap[2] = intToByte4(50)[0];
            keyMap[3] = intToByte4(50)[1];
            //按键坐标y
            keyMap[4] = intToByte4(50)[0];
            keyMap[5] = intToByte4(50)[0];
            //属性值：  1：点击
            keyMap[6] = 0;

            /*
             * 3) 摇杆映射数据(UUID :0x7F12)
             */


            /*
             *4) 组合按键映射(UUID :0x7F12)
                 *            组合按键值：
                            按键                             值
                            L1+A                             1
                            L1+B                             2
                            L1+X                             3
                            L1+Y                             4

             */
            byte[] zuHeMap = new byte[19];
            zuHeMap[0] = 0x13;
            zuHeMap[1] = 0x13;  //按键值
            zuHeMap[2] = intToByte4(100)[0];//按键坐标 X
            zuHeMap[3] = intToByte4(100)[1];//按键坐标 x
            zuHeMap[4] = intToByte4(100)[0];//按键坐标 y
            zuHeMap[5] = intToByte4(100)[1];//按键坐标 y
          /*  属性值：
            1：点击
            2：关联摇杆
            3：关联划屏*/
            zuHeMap[6] = 1;
            zuHeMap[7] = 0;//0表示单次点击;
            zuHeMap[8] = 0;  //为0时表示关联右摇杆，为1时表示左摇杆
            zuHeMap[9] = 0x13;
            zuHeMap[10] = 0x13;
        }

    }

    public static byte[] intToByte4(int i) {
        byte[] targets = new byte[2];
        targets[0] = (byte) (i & 0xFF);
        targets[1] = (byte) (i >> 8 & 0xFF);
        return targets;
    }

    public void startRead(View view) {
        if (mDevice != null) {
            if (mBluetoothGatt != null) {
                mBluetoothGatt.disconnect();
                mBluetoothGatt.close();
                mBluetoothGatt = null;
            }
            mBluetoothGatt = mDevice.connectGatt(MainActivity.this, false, mGattCallback);
        }
    }


    public void stopConnect(View view) {
        if (mBluetoothGatt != null) {

            mBluetoothGatt.close();
        }
    }

    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.d("haha", "onConnectionStateChange: " + newState);

            if (status != BluetoothGatt.GATT_SUCCESS) {
                String err = "Cannot connect device with error status: " + status;

                gatt.close();
                if (mBluetoothGatt != null) {
                    mBluetoothGatt.disconnect();
                    mBluetoothGatt.close();
                    mBluetoothGatt = null;
                }
                if (mDevice != null) {
                    mBluetoothGatt = mDevice.connectGatt(MainActivity.this, false, mGattCallback);
                }
                Log.e(TAG, err);
                return;
            }

            if (newState == BluetoothProfile.STATE_CONNECTED) {//当蓝牙设备已经连接

                //获取ble设备上面的服务
                // toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                Log.i("haha", "Attempting to start service discovery:" + mBluetoothGatt.discoverServices());
                Log.d("haha", "onConnectionStateChange: " + "连接成功");
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {//当设备无法连接
                if (mBluetoothGatt != null) {
                    mBluetoothGatt.disconnect();
                    mBluetoothGatt.close();
                    mBluetoothGatt = null;
                }
                gatt.close();
                if (mDevice != null) {
                    mBluetoothGatt = mDevice.connectGatt(MainActivity.this, false, mGattCallback);
                }
            }


        }

        //发现服务回调。
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d("haha", "onServicesDiscovered: " + "发现服务 : " + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                isServiceConnected = true;

                boolean serviceFound;
                Log.d("haha", "onServicesDiscovered: " + "发现服务 : " + status);
                Log.d(TAG, "onServicesDiscovered: " + "读取数据");
                BluetoothGattService gattService = gatt.getService(UUID_SERVICE);
                if (gattService != null && isServiceConnected) {
                    BluetoothGattCharacteristic characteristic = gattService.getCharacteristic(UUID_NOTIFICATION);
                    BluetoothGattCharacteristic characteristic0 = gattService.getCharacteristic(UUID_NOTIFICATION_START);
                    boolean b = gatt.setCharacteristicNotification(characteristic, true);
                    if (b) {
                        List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
                        List<BluetoothGattDescriptor> descriptors1 = characteristic0.getDescriptors();
                        for (BluetoothGattDescriptor descriptor : descriptors1) {
                            boolean b1 = descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            if (b1) {
                                gatt.writeDescriptor(descriptor);
                                Log.d(TAG, "startRead: " + "监听收数据");
                            }
                        }
                        for (BluetoothGattDescriptor descriptor : descriptors) {
                            boolean b1 = descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            if (b1) {
                                gatt.writeDescriptor(descriptor);
                                Log.d(TAG, "startRead: " + "监听收数据");
                            }
                        }
                    }
                }
                serviceFound = true;
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.d(TAG, "read value: " + characteristic.getValue());
            Log.d(TAG, "callback characteristic read status " + status
                    + " in thread " + Thread.currentThread());
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "read value: " + characteristic.getValue());
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.d(TAG, "onDescriptorWrite: " + "设置成功");
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.d(TAG, "onCharacteristicWrite: " + "发送成功");
            boolean b = mBluetoothGatt.setCharacteristicNotification(characteristic, true);
            mBluetoothGatt.readCharacteristic(characteristic);
        }

        @Override
        public final void onCharacteristicChanged(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            byte[] value = characteristic.getValue();
//            Log.d(TAG, "onCharacteristicChanged: " + value.length);
//            String s0 = Integer.toHexString(value[0] & 0xFF);
//            String s = Integer.toHexString(value[1] & 0xFF);
//             Log.d(TAG, "onCharacteristicChanged: " + s0 + "、" + s);
//            textView1.setText("收到: " + s0 + "、" + s);

            //接收到手柄start 启动...
            if (value[7] == 0x08) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("coms.example.administrator.smosmap", "coms.example.administrator.smosmap.MainActivity"));
                startActivity(intent);
            }
            for (int i = 0; i < value.length; i++) {
                Log.d(TAG, "onCharacteristicChanged:--------------------------------------- ");
                Log.d(TAG, "onCharacteristicChanged: " + Integer.toHexString(value[i] & 0xFF));
            }

        }
    };

    List<Button> buttons = new ArrayList<>();

}

