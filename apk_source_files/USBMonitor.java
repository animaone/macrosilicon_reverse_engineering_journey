package com.p004ms.ms2160.myapplication;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/* renamed from: com.ms.ms2160.myapplication.USBMonitor */
public final class USBMonitor {
    public static final String ACTION_USB_DEVICE_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    private static final String ACTION_USB_PERMISSION_BASE = "com.serenegiant.USB_PERMISSION.";
    private static final boolean DEBUG = false;
    private static final String TAG = "USBMonitor";
    private static final int USB_DIR_IN = 128;
    private static final int USB_DIR_OUT = 0;
    private static final int USB_DT_BOS = 15;
    private static final int USB_DT_CONFIG = 2;
    private static final int USB_DT_CS_CONFIG = 34;
    private static final int USB_DT_CS_DEVICE = 33;
    private static final int USB_DT_CS_ENDPOINT = 37;
    private static final int USB_DT_CS_INTERFACE = 36;
    private static final int USB_DT_CS_RADIO_CONTROL = 35;
    private static final int USB_DT_CS_STRING = 35;
    private static final int USB_DT_DEBUG = 10;
    private static final int USB_DT_DEVICE = 1;
    private static final int USB_DT_DEVICE_CAPABILITY = 16;
    private static final int USB_DT_DEVICE_QUALIFIER = 6;
    private static final int USB_DT_DEVICE_SIZE = 18;
    private static final int USB_DT_ENCRYPTION_TYPE = 14;
    private static final int USB_DT_ENDPOINT = 5;
    private static final int USB_DT_INTERFACE = 4;
    private static final int USB_DT_INTERFACE_ASSOCIATION = 11;
    private static final int USB_DT_INTERFACE_POWER = 8;
    private static final int USB_DT_KEY = 13;
    private static final int USB_DT_OTG = 9;
    private static final int USB_DT_OTHER_SPEED_CONFIG = 7;
    private static final int USB_DT_PIPE_USAGE = 36;
    private static final int USB_DT_RPIPE = 34;
    private static final int USB_DT_SECURITY = 12;
    private static final int USB_DT_SS_ENDPOINT_COMP = 48;
    private static final int USB_DT_STRING = 3;
    private static final int USB_DT_WIRELESS_ENDPOINT_COMP = 17;
    private static final int USB_DT_WIRE_ADAPTER = 33;
    private static final int USB_RECIP_DEVICE = 0;
    private static final int USB_RECIP_ENDPOINT = 2;
    private static final int USB_RECIP_INTERFACE = 1;
    private static final int USB_RECIP_MASK = 31;
    private static final int USB_RECIP_OTHER = 3;
    private static final int USB_RECIP_PORT = 4;
    private static final int USB_RECIP_RPIPE = 5;
    private static final int USB_REQ_CLEAR_FEATURE = 1;
    private static final int USB_REQ_CS_DEVICE_GET = 160;
    private static final int USB_REQ_CS_DEVICE_SET = 32;
    private static final int USB_REQ_CS_ENDPOINT_GET = 162;
    private static final int USB_REQ_CS_ENDPOINT_SET = 34;
    private static final int USB_REQ_CS_INTERFACE_GET = 161;
    private static final int USB_REQ_CS_INTERFACE_SET = 33;
    private static final int USB_REQ_GET_CONFIGURATION = 8;
    private static final int USB_REQ_GET_DESCRIPTOR = 6;
    private static final int USB_REQ_GET_ENCRYPTION = 14;
    private static final int USB_REQ_GET_HANDSHAKE = 16;
    private static final int USB_REQ_GET_INTERFACE = 10;
    private static final int USB_REQ_GET_SECURITY_DATA = 19;
    private static final int USB_REQ_GET_STATUS = 0;
    private static final int USB_REQ_LOOPBACK_DATA_READ = 22;
    private static final int USB_REQ_LOOPBACK_DATA_WRITE = 21;
    private static final int USB_REQ_RPIPE_ABORT = 14;
    private static final int USB_REQ_RPIPE_RESET = 15;
    private static final int USB_REQ_SET_ADDRESS = 5;
    private static final int USB_REQ_SET_CONFIGURATION = 9;
    private static final int USB_REQ_SET_CONNECTION = 17;
    private static final int USB_REQ_SET_DESCRIPTOR = 7;
    private static final int USB_REQ_SET_ENCRYPTION = 13;
    private static final int USB_REQ_SET_FEATURE = 3;
    private static final int USB_REQ_SET_HANDSHAKE = 15;
    private static final int USB_REQ_SET_INTERFACE = 11;
    private static final int USB_REQ_SET_INTERFACE_DS = 23;
    private static final int USB_REQ_SET_ISOCH_DELAY = 49;
    private static final int USB_REQ_SET_SECURITY_DATA = 18;
    private static final int USB_REQ_SET_SEL = 48;
    private static final int USB_REQ_SET_WUSB_DATA = 20;
    private static final int USB_REQ_STANDARD_DEVICE_GET = 128;
    private static final int USB_REQ_STANDARD_DEVICE_SET = 0;
    private static final int USB_REQ_STANDARD_ENDPOINT_GET = 130;
    private static final int USB_REQ_STANDARD_ENDPOINT_SET = 2;
    private static final int USB_REQ_STANDARD_INTERFACE_GET = 129;
    private static final int USB_REQ_STANDARD_INTERFACE_SET = 1;
    private static final int USB_REQ_SYNCH_FRAME = 12;
    private static final int USB_REQ_VENDER_DEVICE_GET = 160;
    private static final int USB_REQ_VENDER_DEVICE_SET = 32;
    private static final int USB_REQ_VENDER_ENDPOINT_GET = 162;
    private static final int USB_REQ_VENDER_ENDPOINT_SET = 34;
    private static final int USB_REQ_VENDER_INTERFACE_GET = 161;
    private static final int USB_REQ_VENDER_INTERFACE_SET = 33;
    private static final int USB_TYPE_CLASS = 32;
    private static final int USB_TYPE_MASK = 96;
    private static final int USB_TYPE_RESERVED = 96;
    private static final int USB_TYPE_STANDARD = 0;
    private static final int USB_TYPE_VENDOR = 64;
    private final String ACTION_USB_PERMISSION = "com.ms.ms2160.USB_PERMISSION";
    /* access modifiers changed from: private */
    public volatile boolean destroyed;
    /* access modifiers changed from: private */
    public final Handler mAsyncHandler;
    /* access modifiers changed from: private */
    public final ConcurrentHashMap<UsbDevice, UsbControlBlock> mCtrlBlocks = new ConcurrentHashMap<>();
    private final Runnable mDeviceCheckRunnable = new Runnable() {
        public void run() {
            int hasPermissionCounts;
            int m;
            if (!USBMonitor.this.destroyed) {
                List<UsbDevice> devices = USBMonitor.this.getDeviceList();
                int n = devices.size();
                synchronized (USBMonitor.this.mHasPermissions) {
                    hasPermissionCounts = USBMonitor.this.mHasPermissions.size();
                    USBMonitor.this.mHasPermissions.clear();
                    for (UsbDevice device : devices) {
                        USBMonitor.this.hasPermission(device);
                    }
                    m = USBMonitor.this.mHasPermissions.size();
                }
                if (n > USBMonitor.this.mDeviceCounts || m > hasPermissionCounts) {
                    int unused = USBMonitor.this.mDeviceCounts = n;
                    if (USBMonitor.this.mOnDeviceConnectListener != null) {
                        for (int i = 0; i < n; i++) {
                            final UsbDevice device2 = devices.get(i);
                            USBMonitor.this.mAsyncHandler.post(new Runnable() {
                                public void run() {
                                    USBMonitor.this.mOnDeviceConnectListener.onAttach(device2);
                                }
                            });
                        }
                    }
                }
                USBMonitor.this.mAsyncHandler.postDelayed(this, 2000);
            }
        }
    };
    private final Runnable mDeviceCheckRunnableOne = new Runnable() {
        public void run() {
            int hasPermissionCounts;
            int m;
            if (!USBMonitor.this.destroyed) {
                List<UsbDevice> devices = USBMonitor.this.getDeviceList();
                int n = devices.size();
                Log.i(USBMonitor.TAG, "2");
                synchronized (USBMonitor.this.mHasPermissions) {
                    hasPermissionCounts = USBMonitor.this.mHasPermissions.size();
                    USBMonitor.this.mHasPermissions.clear();
                    for (UsbDevice device : devices) {
                        USBMonitor.this.hasPermission(device);
                    }
                    m = USBMonitor.this.mHasPermissions.size();
                    Log.i(USBMonitor.TAG, "m = " + m);
                    Log.i(USBMonitor.TAG, "n = " + n);
                    Log.i(USBMonitor.TAG, "mDeviceCounts = " + USBMonitor.this.mDeviceCounts);
                    Log.i(USBMonitor.TAG, "hasPermissionCounts = " + hasPermissionCounts);
                }
                if (n > USBMonitor.this.mDeviceCounts || m > hasPermissionCounts) {
                    int unused = USBMonitor.this.mDeviceCounts = n;
                    if (USBMonitor.this.mOnDeviceConnectListener != null) {
                        for (int i = 0; i < n; i++) {
                            final UsbDevice device2 = devices.get(i);
                            USBMonitor.this.mAsyncHandler.post(new Runnable() {
                                public void run() {
                                    Log.i(USBMonitor.TAG, "3 ");
                                    USBMonitor.this.mOnDeviceConnectListener.onAttach(device2);
                                }
                            });
                        }
                    }
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public volatile int mDeviceCounts = 0;
    private List<DeviceFilter> mDeviceFilters = new ArrayList();
    /* access modifiers changed from: private */
    public final SparseArray<WeakReference<UsbDevice>> mHasPermissions = new SparseArray<>();
    /* access modifiers changed from: private */
    public final OnDeviceConnectListener mOnDeviceConnectListener;
    private PendingIntent mPermissionIntent = null;
    /* access modifiers changed from: private */
    public final UsbManager mUsbManager;
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            UsbDevice device;
            if (!USBMonitor.this.destroyed) {
                String action = intent.getAction();
                if ("com.ms.ms2160.USB_PERMISSION".equals(action)) {
                    synchronized (USBMonitor.this) {
                        UsbDevice device2 = (UsbDevice) intent.getParcelableExtra("device");
                        if (!intent.getBooleanExtra("permission", false)) {
                            USBMonitor.this.processCancel(device2);
                        } else if (device2 != null) {
                            Log.i(USBMonitor.TAG, "process Connect");
                            USBMonitor.this.processConnect(device2);
                        }
                    }
                } else if (USBMonitor.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                    UsbDevice device3 = (UsbDevice) intent.getParcelableExtra("device");
                    boolean unused = USBMonitor.this.updatePermission(device3, USBMonitor.this.hasPermission(device3));
                    USBMonitor.this.processAttach(device3);
                } else if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action) && (device = (UsbDevice) intent.getParcelableExtra("device")) != null) {
                    UsbControlBlock ctrlBlock = (UsbControlBlock) USBMonitor.this.mCtrlBlocks.remove(device);
                    if (ctrlBlock != null) {
                        ctrlBlock.close();
                    }
                    int unused2 = USBMonitor.this.mDeviceCounts = 0;
                    USBMonitor.this.processDettach(device);
                }
            }
        }
    };
    private final WeakReference<Context> mWeakContext;

    /* renamed from: com.ms.ms2160.myapplication.USBMonitor$OnDeviceConnectListener */
    public interface OnDeviceConnectListener {
        void onAttach(UsbDevice usbDevice);

        void onCancel(UsbDevice usbDevice);

        void onConnect(UsbDevice usbDevice, UsbControlBlock usbControlBlock, boolean z);

        void onDettach(UsbDevice usbDevice);

        void onDisconnect(UsbDevice usbDevice, UsbControlBlock usbControlBlock);
    }

    public USBMonitor(Context context, OnDeviceConnectListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("OnDeviceConnectListener should not null.");
        }
        this.mWeakContext = new WeakReference<>(context);
        this.mUsbManager = (UsbManager) context.getSystemService("usb");
        this.mOnDeviceConnectListener = listener;
        this.mAsyncHandler = HandlerThreadHandler.createHandler(TAG);
        this.destroyed = false;
    }

    public void destroy() {
        unregister();
        if (!this.destroyed) {
            this.destroyed = true;
            Set<UsbDevice> keys = this.mCtrlBlocks.keySet();
            if (keys != null) {
                try {
                    for (UsbDevice key : keys) {
                        UsbControlBlock ctrlBlock = this.mCtrlBlocks.remove(key);
                        if (ctrlBlock != null) {
                            ctrlBlock.close();
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "destroy:", e);
                }
            }
            this.mCtrlBlocks.clear();
            try {
                this.mAsyncHandler.getLooper().quit();
            } catch (Exception e2) {
                Log.e(TAG, "destroy:", e2);
            }
        }
    }

    public synchronized void register() throws IllegalStateException {
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        } else if (this.mPermissionIntent == null) {
            Context context = (Context) this.mWeakContext.get();
            if (context != null) {
                this.mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent("com.ms.ms2160.USB_PERMISSION"), 0);
                IntentFilter filter = new IntentFilter("com.ms.ms2160.USB_PERMISSION");
                filter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
                context.registerReceiver(this.mUsbReceiver, filter);
            }
            this.mDeviceCounts = 0;
        }
    }

    public void registerCheckDevice() {
        this.mAsyncHandler.removeCallbacks(this.mDeviceCheckRunnableOne);
        this.mDeviceCounts = 0;
        Log.i(TAG, "1");
        this.mAsyncHandler.postDelayed(this.mDeviceCheckRunnableOne, 0);
    }

    public synchronized void unregister() throws IllegalStateException {
        this.mDeviceCounts = 0;
        if (!this.destroyed) {
        }
        if (this.mPermissionIntent != null) {
            Context context = (Context) this.mWeakContext.get();
            if (context != null) {
                try {
                    context.unregisterReceiver(this.mUsbReceiver);
                } catch (Exception e) {
                    Log.w(TAG, e);
                }
            }
            this.mPermissionIntent = null;
        }
        return;
    }

    public synchronized boolean isRegistered() {
        return !this.destroyed && this.mPermissionIntent != null;
    }

    public void setDeviceFilter(DeviceFilter filter) throws IllegalStateException {
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        }
        this.mDeviceFilters.clear();
        this.mDeviceFilters.add(filter);
    }

    public void addDeviceFilter(DeviceFilter filter) throws IllegalStateException {
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        }
        this.mDeviceFilters.add(filter);
    }

    public void removeDeviceFilter(DeviceFilter filter) throws IllegalStateException {
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        }
        this.mDeviceFilters.remove(filter);
    }

    public void setDeviceFilter(List<DeviceFilter> filters) throws IllegalStateException {
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        }
        this.mDeviceFilters.clear();
        this.mDeviceFilters.addAll(filters);
    }

    public void addDeviceFilter(List<DeviceFilter> filters) throws IllegalStateException {
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        }
        this.mDeviceFilters.addAll(filters);
    }

    public void removeDeviceFilter(List<DeviceFilter> filters) throws IllegalStateException {
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        }
        this.mDeviceFilters.removeAll(filters);
    }

    public int getDeviceCount() throws IllegalStateException {
        if (!this.destroyed) {
            return getDeviceList().size();
        }
        throw new IllegalStateException("already destroyed");
    }

    public List<UsbDevice> getDeviceList() throws IllegalStateException {
        if (!this.destroyed) {
            return getDeviceList(this.mDeviceFilters);
        }
        throw new IllegalStateException("already destroyed");
    }

    public List<UsbDevice> getDeviceList(List<DeviceFilter> filters) throws IllegalStateException {
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        }
        HashMap<String, UsbDevice> deviceList = this.mUsbManager.getDeviceList();
        List<UsbDevice> result = new ArrayList<>();
        if (deviceList != null) {
            if (filters == null || filters.isEmpty()) {
                result.addAll(deviceList.values());
            } else {
                for (UsbDevice device : deviceList.values()) {
                    Iterator<DeviceFilter> it = filters.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        DeviceFilter filter = it.next();
                        if (filter != null && filter.matches(device)) {
                            if (!filter.isExclude) {
                                result.add(device);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public List<UsbDevice> getDeviceList(DeviceFilter filter) throws IllegalStateException {
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        }
        HashMap<String, UsbDevice> deviceList = this.mUsbManager.getDeviceList();
        List<UsbDevice> result = new ArrayList<>();
        if (deviceList != null) {
            for (UsbDevice device : deviceList.values()) {
                if (filter == null || (filter.matches(device) && !filter.isExclude)) {
                    result.add(device);
                }
            }
        }
        return result;
    }

    public Iterator<UsbDevice> getDevices() throws IllegalStateException {
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        }
        HashMap<String, UsbDevice> list = this.mUsbManager.getDeviceList();
        if (list != null) {
            return list.values().iterator();
        }
        return null;
    }

    public final void dumpDevices() {
        int num_interface;
        HashMap<String, UsbDevice> list = this.mUsbManager.getDeviceList();
        if (list != null) {
            Set<String> keys = list.keySet();
            if (keys == null || keys.size() <= 0) {
                Log.i(TAG, "no device");
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (String key : keys) {
                UsbDevice device = list.get(key);
                if (device != null) {
                    num_interface = device.getInterfaceCount();
                } else {
                    num_interface = 0;
                }
                sb.setLength(0);
                for (int i = 0; i < num_interface; i++) {
                    sb.append(String.format(Locale.US, "interface%d:%s", new Object[]{Integer.valueOf(i), device.getInterface(i).toString()}));
                }
                Log.i(TAG, "key=" + key + ":" + device + ":" + sb.toString());
            }
            return;
        }
        Log.i(TAG, "no device");
    }

    public final boolean hasPermission(UsbDevice device) throws IllegalStateException {
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        }
        return updatePermission(device, device != null && this.mUsbManager.hasPermission(device));
    }

    /* access modifiers changed from: private */
    public boolean updatePermission(UsbDevice device, boolean hasPermission) {
        int deviceKey = getDeviceKey(device, true);
        synchronized (this.mHasPermissions) {
            if (!hasPermission) {
                this.mHasPermissions.remove(deviceKey);
            } else if (this.mHasPermissions.get(deviceKey) == null) {
                this.mHasPermissions.put(deviceKey, new WeakReference(device));
            }
        }
        return hasPermission;
    }

    public synchronized boolean requestPermission(UsbDevice device) {
        boolean result = false;
        synchronized (this) {
            if (device == null) {
                Log.v(TAG, "device == null");
            } else {
                Log.v(TAG, String.format("pid = %d vid = %d", new Object[]{Integer.valueOf(device.getProductId()), Integer.valueOf(device.getVendorId())}));
                result = false;
                if (device.getProductId() == 24609 && device.getVendorId() == 21325) {
                    if (!isRegistered()) {
                        processCancel(device);
                        result = true;
                    } else if (device == null) {
                        processCancel(device);
                        result = true;
                    } else if (this.mUsbManager.hasPermission(device)) {
                        processConnect(device);
                    } else {
                        try {
                            this.mUsbManager.requestPermission(device, this.mPermissionIntent);
                        } catch (Exception e) {
                            Log.w(TAG, e);
                            processCancel(device);
                            result = true;
                        }
                    }
                }
            }
        }
        return result;
    }

    public UsbControlBlock openDevice(UsbDevice device) throws SecurityException {
        if (hasPermission(device)) {
            UsbControlBlock result = this.mCtrlBlocks.get(device);
            if (result != null) {
                return result;
            }
            UsbControlBlock result2 = new UsbControlBlock(device);
            this.mCtrlBlocks.put(device, result2);
            return result2;
        }
        throw new SecurityException("has no permission");
    }

    /* access modifiers changed from: private */
    public final void processConnect(final UsbDevice device) {
        if (!this.destroyed) {
            updatePermission(device, true);
            this.mAsyncHandler.post(new Runnable() {
                public void run() {
                    boolean createNew;
                    UsbControlBlock ctrlBlock = (UsbControlBlock) USBMonitor.this.mCtrlBlocks.get(device);
                    if (ctrlBlock == null) {
                        ctrlBlock = new UsbControlBlock(device);
                        USBMonitor.this.mCtrlBlocks.put(device, ctrlBlock);
                        createNew = true;
                    } else {
                        createNew = false;
                    }
                    if (USBMonitor.this.mOnDeviceConnectListener != null) {
                        USBMonitor.this.mOnDeviceConnectListener.onConnect(device, ctrlBlock, createNew);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public final void processCancel(final UsbDevice device) {
        if (!this.destroyed) {
            updatePermission(device, false);
            if (this.mOnDeviceConnectListener != null) {
                this.mAsyncHandler.post(new Runnable() {
                    public void run() {
                        USBMonitor.this.mOnDeviceConnectListener.onCancel(device);
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public final void processAttach(final UsbDevice device) {
        if (!this.destroyed && this.mOnDeviceConnectListener != null) {
            this.mAsyncHandler.post(new Runnable() {
                public void run() {
                    USBMonitor.this.mOnDeviceConnectListener.onAttach(device);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public final void processDettach(final UsbDevice device) {
        if (!this.destroyed && this.mOnDeviceConnectListener != null) {
            this.mAsyncHandler.post(new Runnable() {
                public void run() {
                    USBMonitor.this.mOnDeviceConnectListener.onDettach(device);
                }
            });
        }
    }

    public static final String getDeviceKeyName(UsbDevice device) {
        return getDeviceKeyName(device, (String) null, false);
    }

    public static final String getDeviceKeyName(UsbDevice device, boolean useNewAPI) {
        return getDeviceKeyName(device, (String) null, useNewAPI);
    }

    @SuppressLint({"NewApi"})
    public static final String getDeviceKeyName(UsbDevice device, String serial, boolean useNewAPI) {
        if (device == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(device.getVendorId());
        sb.append("#");
        sb.append(device.getProductId());
        sb.append("#");
        sb.append(device.getDeviceClass());
        sb.append("#");
        sb.append(device.getDeviceSubclass());
        sb.append("#");
        sb.append(device.getDeviceProtocol());
        if (!TextUtils.isEmpty(serial)) {
            sb.append("#");
            sb.append(serial);
        }
        if (useNewAPI && BuildCheck.isAndroid5()) {
            sb.append("#");
            if (TextUtils.isEmpty(serial)) {
                sb.append(device.getSerialNumber());
                sb.append("#");
            }
            sb.append(device.getManufacturerName());
            sb.append("#");
            sb.append(device.getConfigurationCount());
            sb.append("#");
            if (BuildCheck.isMarshmallow()) {
                sb.append(device.getVersion());
                sb.append("#");
            }
        }
        return sb.toString();
    }

    public static final int getDeviceKey(UsbDevice device) {
        if (device != null) {
            return getDeviceKeyName(device, (String) null, false).hashCode();
        }
        return 0;
    }

    public static final int getDeviceKey(UsbDevice device, boolean useNewAPI) {
        if (device != null) {
            return getDeviceKeyName(device, (String) null, useNewAPI).hashCode();
        }
        return 0;
    }

    public static final int getDeviceKey(UsbDevice device, String serial, boolean useNewAPI) {
        if (device != null) {
            return getDeviceKeyName(device, serial, useNewAPI).hashCode();
        }
        return 0;
    }

    /* renamed from: com.ms.ms2160.myapplication.USBMonitor$UsbDeviceInfo */
    public static class UsbDeviceInfo {
        public String manufacturer;
        public String product;
        public String serial;
        public String usb_version;
        public String version;

        /* access modifiers changed from: private */
        public void clear() {
            this.serial = null;
            this.version = null;
            this.product = null;
            this.manufacturer = null;
            this.usb_version = null;
        }

        public String toString() {
            Object[] objArr = new Object[5];
            objArr[0] = this.usb_version != null ? this.usb_version : "";
            objArr[1] = this.manufacturer != null ? this.manufacturer : "";
            objArr[2] = this.product != null ? this.product : "";
            objArr[3] = this.version != null ? this.version : "";
            objArr[4] = this.serial != null ? this.serial : "";
            return String.format("UsbDevice:usb_version=%s,manufacturer=%s,product=%s,version=%s,serial=%s", objArr);
        }
    }

    private static String getString(UsbDeviceConnection connection, int id, int languageCount, byte[] languages) {
        String result;
        byte[] work = new byte[256];
        int i = 1;
        String result2 = null;
        while (i <= languageCount) {
            int ret = connection.controlTransfer(128, 6, id | 768, languages[i], work, 256, 0);
            if (ret > 2 && work[0] == ret && work[1] == 3) {
                try {
                    result = new String(work, 2, ret - 2, "UTF-16LE");
                    try {
                        if (!"Љ".equals(result)) {
                            return result;
                        }
                        result = null;
                    } catch (UnsupportedEncodingException e) {
                    }
                } catch (UnsupportedEncodingException e2) {
                    result = result2;
                }
            } else {
                result = result2;
            }
            i++;
            result2 = result;
        }
        return result2;
    }

    public UsbDeviceInfo getDeviceInfo(UsbDevice device) {
        return updateDeviceInfo(this.mUsbManager, device, (UsbDeviceInfo) null);
    }

    public static UsbDeviceInfo getDeviceInfo(Context context, UsbDevice device) {
        return updateDeviceInfo((UsbManager) context.getSystemService("usb"), device, new UsbDeviceInfo());
    }

    public static UsbDeviceInfo updateDeviceInfo(UsbManager manager, UsbDevice device, UsbDeviceInfo _info) {
        UsbDeviceInfo info;
        if (_info != null) {
            info = _info;
        } else {
            info = new UsbDeviceInfo();
        }
        info.clear();
        if (device != null) {
            if (BuildCheck.isLollipop()) {
                info.manufacturer = device.getManufacturerName();
                info.product = device.getProductName();
                info.serial = device.getSerialNumber();
            }
            if (BuildCheck.isMarshmallow()) {
                info.usb_version = device.getVersion();
            }
            if (manager != null && manager.hasPermission(device)) {
                UsbDeviceConnection connection = manager.openDevice(device);
                byte[] desc = connection.getRawDescriptors();
                if (TextUtils.isEmpty(info.usb_version)) {
                    info.usb_version = String.format("%x.%02x", new Object[]{Integer.valueOf(desc[3] & 255), Integer.valueOf(desc[2] & 255)});
                }
                if (TextUtils.isEmpty(info.version)) {
                    info.version = String.format("%x.%02x", new Object[]{Integer.valueOf(desc[13] & 255), Integer.valueOf(desc[12] & 255)});
                }
                if (TextUtils.isEmpty(info.serial)) {
                    info.serial = connection.getSerial();
                }
                byte[] languages = new byte[256];
                int languageCount = 0;
                try {
                    int result = connection.controlTransfer(128, 6, 768, 0, languages, 256, 0);
                    if (result > 0) {
                        languageCount = (result - 2) / 2;
                    }
                    if (languageCount > 0) {
                        if (TextUtils.isEmpty(info.manufacturer)) {
                            info.manufacturer = getString(connection, desc[14], languageCount, languages);
                        }
                        if (TextUtils.isEmpty(info.product)) {
                            info.product = getString(connection, desc[15], languageCount, languages);
                        }
                        if (TextUtils.isEmpty(info.serial)) {
                            info.serial = getString(connection, desc[16], languageCount, languages);
                        }
                    }
                } finally {
                    connection.close();
                }
            }
            if (TextUtils.isEmpty(info.manufacturer)) {
                info.manufacturer = USBVendorId.vendorName(device.getVendorId());
            }
            if (TextUtils.isEmpty(info.manufacturer)) {
                info.manufacturer = String.format("%04x", new Object[]{Integer.valueOf(device.getVendorId())});
            }
            if (TextUtils.isEmpty(info.product)) {
                info.product = String.format("%04x", new Object[]{Integer.valueOf(device.getProductId())});
            }
        }
        return info;
    }

    /* renamed from: com.ms.ms2160.myapplication.USBMonitor$UsbControlBlock */
    public static final class UsbControlBlock implements Cloneable {
        private final int mBusNum;
        protected UsbDeviceConnection mConnection;
        private final int mDevNum;
        protected final UsbDeviceInfo mInfo;
        private final SparseArray<SparseArray<UsbInterface>> mInterfaces;
        private final WeakReference<UsbDevice> mWeakDevice;
        private final WeakReference<USBMonitor> mWeakMonitor;

        private UsbControlBlock(USBMonitor monitor, UsbDevice device) {
            String[] v = null;
            this.mInterfaces = new SparseArray<>();
            this.mWeakMonitor = new WeakReference<>(monitor);
            this.mWeakDevice = new WeakReference<>(device);
            this.mConnection = monitor.mUsbManager.openDevice(device);
            this.mInfo = USBMonitor.updateDeviceInfo(monitor.mUsbManager, device, (UsbDeviceInfo) null);
            String name = device.getDeviceName();
            v = !TextUtils.isEmpty(name) ? name.split("/") : v;
            int busnum = 0;
            int devnum = 0;
            if (v != null) {
                busnum = Integer.parseInt(v[v.length - 2]);
                devnum = Integer.parseInt(v[v.length - 1]);
            }
            this.mBusNum = busnum;
            this.mDevNum = devnum;
            if (this.mConnection != null) {
                Log.i(USBMonitor.TAG, String.format(Locale.US, "name=%s,desc=%d,busnum=%d,devnum=%d,rawDesc=", new Object[]{name, Integer.valueOf(this.mConnection.getFileDescriptor()), Integer.valueOf(busnum), Integer.valueOf(devnum)}) + this.mConnection.getRawDescriptors());
                return;
            }
            Log.e(USBMonitor.TAG, "could not connect to device " + name);
        }

        private UsbControlBlock(UsbControlBlock src) throws IllegalStateException {
            this.mInterfaces = new SparseArray<>();
            USBMonitor monitor = src.getUSBMonitor();
            UsbDevice device = src.getDevice();
            if (device == null) {
                throw new IllegalStateException("device may already be removed");
            }
            this.mConnection = monitor.mUsbManager.openDevice(device);
            if (this.mConnection == null) {
                throw new IllegalStateException("device may already be removed or have no permission");
            }
            this.mInfo = USBMonitor.updateDeviceInfo(monitor.mUsbManager, device, (UsbDeviceInfo) null);
            this.mWeakMonitor = new WeakReference<>(monitor);
            this.mWeakDevice = new WeakReference<>(device);
            this.mBusNum = src.mBusNum;
            this.mDevNum = src.mDevNum;
        }

        public UsbControlBlock clone() throws CloneNotSupportedException {
            try {
                return new UsbControlBlock(this);
            } catch (IllegalStateException e) {
                throw new CloneNotSupportedException(e.getMessage());
            }
        }

        public USBMonitor getUSBMonitor() {
            return (USBMonitor) this.mWeakMonitor.get();
        }

        public final UsbDevice getDevice() {
            return (UsbDevice) this.mWeakDevice.get();
        }

        public String getDeviceName() {
            UsbDevice device = (UsbDevice) this.mWeakDevice.get();
            return device != null ? device.getDeviceName() : "";
        }

        public int getDeviceId() {
            UsbDevice device = (UsbDevice) this.mWeakDevice.get();
            if (device != null) {
                return device.getDeviceId();
            }
            return 0;
        }

        public String getDeviceKeyName() {
            return USBMonitor.getDeviceKeyName((UsbDevice) this.mWeakDevice.get());
        }

        public String getDeviceKeyName(boolean useNewAPI) throws IllegalStateException {
            if (useNewAPI) {
                checkConnection();
            }
            return USBMonitor.getDeviceKeyName((UsbDevice) this.mWeakDevice.get(), this.mInfo.serial, useNewAPI);
        }

        public int getDeviceKey() throws IllegalStateException {
            checkConnection();
            return USBMonitor.getDeviceKey((UsbDevice) this.mWeakDevice.get());
        }

        public int getDeviceKey(boolean useNewAPI) throws IllegalStateException {
            if (useNewAPI) {
                checkConnection();
            }
            return USBMonitor.getDeviceKey((UsbDevice) this.mWeakDevice.get(), this.mInfo.serial, useNewAPI);
        }

        public String getDeviceKeyNameWithSerial() {
            return USBMonitor.getDeviceKeyName((UsbDevice) this.mWeakDevice.get(), this.mInfo.serial, false);
        }

        public int getDeviceKeyWithSerial() {
            return getDeviceKeyNameWithSerial().hashCode();
        }

        public synchronized UsbDeviceConnection getConnection() {
            return this.mConnection;
        }

        public synchronized int getFileDescriptor() throws IllegalStateException {
            checkConnection();
            return this.mConnection.getFileDescriptor();
        }

        public synchronized byte[] getRawDescriptors() throws IllegalStateException {
            checkConnection();
            return this.mConnection.getRawDescriptors();
        }

        public int getVenderId() {
            UsbDevice device = (UsbDevice) this.mWeakDevice.get();
            if (device != null) {
                return device.getVendorId();
            }
            return 0;
        }

        public int getProductId() {
            UsbDevice device = (UsbDevice) this.mWeakDevice.get();
            if (device != null) {
                return device.getProductId();
            }
            return 0;
        }

        public String getUsbVersion() {
            return this.mInfo.usb_version;
        }

        public String getManufacture() {
            return this.mInfo.manufacturer;
        }

        public String getProductName() {
            return this.mInfo.product;
        }

        public String getVersion() {
            return this.mInfo.version;
        }

        public String getSerial() {
            return this.mInfo.serial;
        }

        public int getBusNum() {
            return this.mBusNum;
        }

        public int getDevNum() {
            return this.mDevNum;
        }

        public synchronized UsbInterface getInterface(int interface_id) throws IllegalStateException {
            return getInterface(interface_id, 0);
        }

        public synchronized UsbInterface getInterface(int interface_id, int altsetting) throws IllegalStateException {
            UsbInterface intf;
            checkConnection();
            SparseArray<UsbInterface> intfs = this.mInterfaces.get(interface_id);
            if (intfs == null) {
                intfs = new SparseArray<>();
                this.mInterfaces.put(interface_id, intfs);
            }
            intf = intfs.get(altsetting);
            if (intf == null) {
                UsbDevice device = (UsbDevice) this.mWeakDevice.get();
                int n = device.getInterfaceCount();
                int i = 0;
                while (true) {
                    if (i >= n) {
                        break;
                    }
                    UsbInterface temp = device.getInterface(i);
                    if (temp.getId() == interface_id && temp.getAlternateSetting() == altsetting) {
                        intf = temp;
                        break;
                    }
                    i++;
                }
                if (intf != null) {
                    intfs.append(altsetting, intf);
                }
            }
            return intf;
        }

        public synchronized void claimInterface(UsbInterface intf) {
            claimInterface(intf, true);
        }

        public synchronized void claimInterface(UsbInterface intf, boolean force) {
            checkConnection();
            this.mConnection.claimInterface(intf, force);
        }

        public synchronized void releaseInterface(UsbInterface intf) throws IllegalStateException {
            checkConnection();
            SparseArray<UsbInterface> intfs = this.mInterfaces.get(intf.getId());
            if (intfs != null) {
                intfs.removeAt(intfs.indexOfValue(intf));
                if (intfs.size() == 0) {
                    this.mInterfaces.remove(intf.getId());
                }
            }
            this.mConnection.releaseInterface(intf);
        }

        public synchronized void close() {
            if (this.mConnection != null) {
                this.mConnection.close();
                this.mConnection = null;
                USBMonitor monitor = (USBMonitor) this.mWeakMonitor.get();
                if (monitor != null) {
                    if (monitor.mOnDeviceConnectListener != null) {
                        monitor.mOnDeviceConnectListener.onDisconnect((UsbDevice) this.mWeakDevice.get(), this);
                    }
                    monitor.mCtrlBlocks.remove(getDevice());
                }
            }
        }

        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o instanceof UsbControlBlock) {
                UsbDevice device = ((UsbControlBlock) o).getDevice();
                if (device != null) {
                    return device.equals(this.mWeakDevice.get());
                }
                if (this.mWeakDevice.get() == null) {
                    return true;
                }
                return false;
            } else if (o instanceof UsbDevice) {
                return o.equals(this.mWeakDevice.get());
            } else {
                return super.equals(o);
            }
        }

        private synchronized void checkConnection() throws IllegalStateException {
            if (this.mConnection == null) {
                throw new IllegalStateException("already closed");
            }
        }
    }
}
