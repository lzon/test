package coms.example.administrator.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "UUIUI____";
    private RecyclerView recyclerView;


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
    Set set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
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
        deviceName = findViewById(R.id.device_name);
        textView1 = findViewById(R.id.recieve_text);
        recyclerView = findViewById(R.id.recycler_view);
        set = new HashSet();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new MyAdapter(this, set));
    }


    //点击确定发给蓝牙发送消息
    public void onClickQueDing(View view) {
        sendDataBluetooth();
    }

    private void sendDataBluetooth() {
        if (mBluetoothGatt != null && isServiceConnected) {
            BluetoothGattService gattService = mBluetoothGatt.getService(UUID_SERVICE);
            BluetoothGattCharacteristic characteristic = gattService.getCharacteristic(UUID_WRITE);
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


        }
    }

    //点击关闭
    public void onClickGuanBi(View view) {
        view.setBackgroundResource(R.drawable.img_button_blue);
        findViewById(R.id.queding).setBackgroundResource(R.drawable.img_button_grade);
    }

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
    ScanCallback callback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice device = result.getDevice();
            String name = device.getName();
            String address = device.getAddress();
            Log.e("tag:name", "" + name);
            Log.e("tag:address", "" + address);
            set.add(address);
            recyclerView.getAdapter().notifyDataSetChanged();
            if (name != null) {
                deviceName.setText(name);
                if (name.contains("SMOS-P1")) {
                    mDevice = device;
                    mBluetoothAdapter.getBluetoothLeScanner().stopScan(callback);
                }
            }
            byte[] bytes = result.getScanRecord().getBytes();
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };

    //打开蓝牙
    public void openBle(View view) {

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        //得到蓝牙适配器
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter != null) {
            Toast.makeText(this, "已打开", Toast.LENGTH_SHORT).show();
        }
        //蓝牙设备不可以用， 就是没有开， 跳到开启界面去打开蓝牙。
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "前手机不支持蓝牙功能", Toast.LENGTH_SHORT).show();
            return;
        }
        //没有打开蓝牙
        if (mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
        // 打开蓝牙,不跳用户界面，自形打开
//        if (!mBluetoothAdapter.isEnabled()) {
//            mBluetoothAdapter.enable();
//        }
//        mBluetoothAdapter.startDiscovery();//自动配对
    }

    //扫描
    public void startScan(View view) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //30秒扫描不到蓝牙，就停止扫描
                mScanning = false;
//                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                BluetoothLeScanner bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
                if (bluetoothLeScanner != null) {
                    bluetoothLeScanner.stopScan(callback);
                    Toast.makeText(MainActivity.this, "停止扫描", Toast.LENGTH_SHORT).show();
                }

            }
        }, 30000);
        mScanning = true;
        // 定义一个回调接口供扫描结束处理
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "没有打开蓝牙", Toast.LENGTH_SHORT).show();
            return;
        }
        BluetoothLeScanner bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        bluetoothLeScanner.startScan(callback);
    }

    private String TAG = "haha";
    private boolean isServiceConnected;

    //连接蓝牙
    public void startConnect(View view) {
        if (mDevice != null) {
            if (mBluetoothGatt != null) {
                mBluetoothGatt.disconnect();
                mBluetoothGatt.close();
                mBluetoothGatt = null;
            }
            mBluetoothGatt = mDevice.connectGatt(MainActivity.this, false, mGattCallback);
//            BluetoothGatt last = mBluetoothGatts.remove(address);
//            if(last != null){
//                last.disconnect();
//                last.close();
//            }
//            if (gatt == null) {
//                mBluetoothGatts.remove(address);
//                return false;
//            } else {
//                // TODO: if state is 141, it can be connected again after about 15
//                // seconds
//                mBluetoothGatts.put(address, gatt);
//                return true;
//            }
        }
        Toast.makeText(this, "没有蓝牙设备", Toast.LENGTH_SHORT).show();
    }

    //发送数据
    public void startSend() {
        if (mBluetoothGatt != null && isServiceConnected) {
            BluetoothGattService gattService = mBluetoothGatt.getService(UUID_SERVICE);
            BluetoothGattCharacteristic characteristic = gattService.getCharacteristic(UUID_WRITE);

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
            characteristic.setValue(rAndO);
            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
            mBluetoothGatt.writeCharacteristic(characteristic);
        }
        Toast.makeText(this, "没有连接设备", Toast.LENGTH_SHORT).show();
    }

    public static byte[] intToByte4(int i) {
        byte[] targets = new byte[2];
        targets[0] = (byte) (i & 0xFF);
        targets[1] = (byte) (i >> 8 & 0xFF);
        return targets;
    }

    //读取数据
    //一般是在BluetoothGattCharacteristic这个里面被动读取
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

    //停掉连接
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

        //发送成功与否会调用这个方法
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
//            boolean b = mBluetoothGatt.setCharacteristicNotification(characteristic, true);
//            mBluetoothGatt.readCharacteristic(characteristic);
            if (status != BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "onCharacteristicWrite()检测到不可以写数据    status：" + status);
                return;
            }
            Log.e(TAG, "onCharacteristicWrite()检测到可以去写数据了");
            Log.d("写出的数据：", "" + characteristic.getValue());
        }

        //接收到数据会调用这个方法
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

            /**得到数据*****/
            String address = gatt.getDevice().getAddress();
            long starttime = System.currentTimeMillis();
            byte[] bytes = characteristic.getValue();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02x", bytes[i]));
                Log.d(TAG, " time :" + starttime + "     bytes：" + i + "     =" + Integer.toHexString(bytes[i]));
            }
            Log.d(TAG, "出来的数据：" + sb.toString());
        }
    };


    @Override
    protected void onDestroy() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
        }
        super.onDestroy();
    }


}

