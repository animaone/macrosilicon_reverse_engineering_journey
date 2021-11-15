package com.p004ms.ms2160.myapplication;

import android.hardware.usb.UsbDevice;
import android.text.TextUtils;
import android.util.Log;
import com.p004ms.ms2160.CSharpTool.C0442Util;
import com.p004ms.ms2160.myapplication.USBMonitor;

/* renamed from: com.ms.ms2160.myapplication.USBDevice */
public class USBDevice {
    private static final boolean DEBUG = true;
    private static final String DEFAULT_USBFS = "/dev/bus/usb";
    private static final String TAG = USBDevice.class.getSimpleName();
    private static boolean isLoaded;
    private static Object obj;
    private final byte HIDCMD_READ_EEPROM = -27;
    private final byte HIDCMD_READ_FLASH = -11;
    private final byte HIDCMD_READ_SDRAM = -43;
    private final byte HIDCMD_READ_XDATA = -75;
    private final byte HIDCMD_SPECIAL = -90;
    private final byte HIDCMD_SPECIAL_FRAMETRIGGER = 0;
    private final byte HIDCMD_SPECIAL_POWERON = 7;
    private final byte HIDCMD_SPECIAL_STARTTRANS = 4;
    private final byte HIDCMD_SPECIAL_TRANSMODE = 3;
    private final byte HIDCMD_SPECIAL_VIDEOON = 5;
    private final byte HIDCMD_SPECIAL_VIDEO_IN = 1;
    private final byte HIDCMD_SPECIAL_VIDEO_OUT = 2;
    private final byte HIDCMD_WRITE_EEPROM = -26;
    private final byte HIDCMD_WRITE_FLASH = -10;
    private final byte HIDCMD_WRITE_SDRAM = -42;
    private final byte HIDCMD_WRITE_XDATA = -74;
    private final int TimeOut = 1000;
    int libusb_device_handle = 0;
    private USBMonitor.UsbControlBlock mCtrlBlock;
    protected long mNativePtr;
    private final int ram_display_colorspace = 51;
    private final int ram_hpd_flag = 50;
    private final int ram_sdram_type = 48;
    private final int ram_video_port = 49;

    private native int nativeBulkTransferOffset(long j, int i, byte[] bArr, int i2, int i3, int i4);

    private native int nativeClaimInterface(long j, int i);

    private native int nativeClearPool();

    private final native long nativeConnect(int i, int i2, int i3, int i4, int i5, String str);

    private native int nativeControlTransfer(long j, int i, int i2, int i3, int i4, int i5, byte[] bArr, int i6, int i7);

    private native int nativeControlTransferRead(long j, int i, int i2, int i3, int i4, int i5, byte[] bArr, int i6, int i7);

    private native int nativeConvertColorBulkTransfer(long j, int i, byte[] bArr, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9);

    private final native long nativeCreate();

    private native int nativeRealseInterface(long j, int i);

    private final native void nativeRelease(long j);

    public native int nativeLibusbOpen(int i);

    public native void nativeTestColorConvert(byte[] bArr, int i, int i2);

    static {
        System.loadLibrary("usb101");
        System.loadLibrary("msusb");
    }

    public synchronized void open(USBMonitor.UsbControlBlock ctrlBlock) {
        try {
            this.mCtrlBlock = ctrlBlock.clone();
            this.mNativePtr = nativeConnect(this.mCtrlBlock.getVenderId(), this.mCtrlBlock.getProductId(), this.mCtrlBlock.getFileDescriptor(), this.mCtrlBlock.getBusNum(), this.mCtrlBlock.getDevNum(), getUSBFSName(this.mCtrlBlock));
        } catch (Exception e) {
            Log.w(TAG, e);
        }
        if (this.mNativePtr == 0) {
            throw new UnsupportedOperationException("open failed");
        }
        return;
    }

    public synchronized void close() {
        if (this.mNativePtr != 0) {
            nativeRelease(this.mNativePtr);
            this.mNativePtr = 0;
        }
        Log.v(TAG, "close:finished");
    }

    public UsbDevice getDevice() {
        if (this.mCtrlBlock != null) {
            return this.mCtrlBlock.getDevice();
        }
        return null;
    }

    public String getDeviceName() {
        if (this.mCtrlBlock != null) {
            return this.mCtrlBlock.getDeviceName();
        }
        return null;
    }

    public USBMonitor.UsbControlBlock getUsbControlBlock() {
        return this.mCtrlBlock;
    }

    private final String getUSBFSName(USBMonitor.UsbControlBlock ctrlBlock) {
        String result = null;
        String name = ctrlBlock.getDeviceName();
        String[] v = !TextUtils.isEmpty(name) ? name.split("/") : null;
        if (v != null && v.length > 2) {
            StringBuilder sb = new StringBuilder(v[0]);
            for (int i = 1; i < v.length - 2; i++) {
                sb.append("/").append(v[i]);
            }
            result = sb.toString();
        }
        if (!TextUtils.isEmpty(result)) {
            return result;
        }
        Log.w(TAG, "failed to get USBFS path, try to use default path:" + name);
        return DEFAULT_USBFS;
    }

    public void xdata_write(int addr, byte xdata_value) {
        TransferInfo transferInfo = new TransferInfo(0, 33, 9, 768, 0, new byte[]{-74, (byte) ((addr >> 8) & 255), (byte) (addr & 255), xdata_value, 0, 0, 0, 0}, 8, 1000);
        int value = nativeControlTransfer(this.mNativePtr, transferInfo.indexInterface, transferInfo.requestType, transferInfo.request, transferInfo.value, transferInfo.index, transferInfo.buffer, transferInfo.length, transferInfo.timeout);
        if (value != 8) {
            Log.d(TAG, "xdata_write addr: " + Integer.toHexString(addr));
        }
        Log.d(TAG, "xdata_write value: " + value);
    }

    public void frame_trigger(byte fid, byte delay) {
        TransferInfo transferInfo = new TransferInfo(0, 33, 9, 768, 0, new byte[]{-90, 0, fid, delay, 0, 0, 0, 0}, 8, 1000);
        Log.d(TAG, "frame_trigger value: " + nativeControlTransfer(this.mNativePtr, transferInfo.indexInterface, transferInfo.requestType, transferInfo.request, transferInfo.value, transferInfo.index, transferInfo.buffer, transferInfo.length, transferInfo.timeout));
    }

    public byte xdata_read(int addr) {
        byte[] ctrlData = {-75, (byte) ((addr >> 8) & 255), (byte) (addr & 255), 0, 0, 0, 0, 0};
        TransferInfo transferInfo = new TransferInfo(0, 33, 9, 768, 0, ctrlData, 8, 1000);
        Log.d(TAG, "nativeControlTransfer write value: " + nativeControlTransfer(this.mNativePtr, transferInfo.indexInterface, transferInfo.requestType, transferInfo.request, transferInfo.value, transferInfo.index, transferInfo.buffer, transferInfo.length, transferInfo.timeout));
        transferInfo.requestType = 161;
        transferInfo.request = 1;
        Log.d(TAG, "nativeControlTransfer read value: " + nativeControlTransfer(this.mNativePtr, transferInfo.indexInterface, transferInfo.requestType, transferInfo.request, transferInfo.value, transferInfo.index, transferInfo.buffer, transferInfo.length, transferInfo.timeout));
        return ctrlData[3];
    }

    public byte xdata_read_atomic(int addr) {
        byte[] ctrlData = {-75, (byte) ((addr >> 8) & 255), (byte) (addr & 255), 0, 0, 0, 0, 0};
        TransferInfo transferInfo = new TransferInfo(0, 33, 9, 768, 0, ctrlData, 8, 1000);
        Log.d(TAG, "nativeControlTransfer read value: " + nativeControlTransferRead(this.mNativePtr, transferInfo.indexInterface, transferInfo.requestType, transferInfo.request, transferInfo.value, transferInfo.index, transferInfo.buffer, transferInfo.length, transferInfo.timeout));
        return ctrlData[3];
    }

    public void xdata_modBits(int addr, byte value, byte mask) {
        xdata_write(addr, (byte) (((byte) (value & mask)) | ((byte) (((byte) (mask ^ -1)) & xdata_read(addr)))));
    }

    public void xdata_setBits(int addr, byte mask) {
        xdata_write(addr, (byte) (((byte) (mask & 255)) | ((byte) (((byte) (mask ^ -1)) & xdata_read(addr)))));
    }

    public void xdata_clrBits(int addr, byte mask) {
        xdata_write(addr, (byte) (((byte) (mask & 0)) | ((byte) (((byte) (mask ^ -1)) & xdata_read(addr)))));
    }

    public void set_start_trans(byte start_flag) {
        TransferInfo transferInfo = new TransferInfo(0, 33, 9, 768, 0, new byte[]{-90, 4, start_flag, 0, 0, 0, 0, 0}, 8, 1000);
        Log.d(TAG, "set_start_trans value: " + nativeControlTransfer(this.mNativePtr, transferInfo.indexInterface, transferInfo.requestType, transferInfo.request, transferInfo.value, transferInfo.index, transferInfo.buffer, transferInfo.length, transferInfo.timeout));
    }

    public void set_transfer_mode_frame() {
        TransferInfo transferInfo = new TransferInfo(0, 33, 9, 768, 0, new byte[]{-90, 3, (byte) C0442Util.DataType.TRANSFER_MODE.FRAME, 0, 0, 0, 0, 0}, 8, 1000);
        Log.d(TAG, "set_start_trans value: " + nativeControlTransfer(this.mNativePtr, transferInfo.indexInterface, transferInfo.requestType, transferInfo.request, transferInfo.value, transferInfo.index, transferInfo.buffer, transferInfo.length, transferInfo.timeout));
    }

    public void set_video_in(int width, int height, byte color, byte colSel) {
        TransferInfo transferInfo = new TransferInfo(0, 33, 9, 768, 0, new byte[]{-90, 1, (byte) (width >> 8), (byte) width, (byte) (height >> 8), (byte) height, color, colSel}, 8, 1000);
        int nativeControlTransfer = nativeControlTransfer(this.mNativePtr, transferInfo.indexInterface, transferInfo.requestType, transferInfo.request, transferInfo.value, transferInfo.index, transferInfo.buffer, transferInfo.length, transferInfo.timeout);
    }

    public void set_video_on(byte power_on_enable) {
        TransferInfo transferInfo = new TransferInfo(0, 33, 9, 768, 0, new byte[]{-90, 5, power_on_enable, 0, 0, 0, 0, 0}, 8, 1000);
        Log.d(TAG, "set_power_on value: " + nativeControlTransfer(this.mNativePtr, transferInfo.indexInterface, transferInfo.requestType, transferInfo.request, transferInfo.value, transferInfo.index, transferInfo.buffer, transferInfo.length, transferInfo.timeout));
    }

    public void set_power_on(byte power_on_enable) {
        byte[] ctrlData = new byte[8];
        ctrlData[0] = -90;
        ctrlData[1] = 7;
        ctrlData[2] = power_on_enable;
        if (power_on_enable == 1) {
            ctrlData[3] = 2;
        } else {
            ctrlData[3] = 0;
        }
        ctrlData[4] = 0;
        ctrlData[5] = 0;
        ctrlData[6] = 0;
        ctrlData[7] = 0;
        TransferInfo transferInfo = new TransferInfo(0, 33, 9, 768, 0, ctrlData, 8, 1000);
        Log.d(TAG, "set_power_on value: " + nativeControlTransfer(this.mNativePtr, transferInfo.indexInterface, transferInfo.requestType, transferInfo.request, transferInfo.value, transferInfo.index, transferInfo.buffer, transferInfo.length, transferInfo.timeout));
    }

    public void set_video_out(byte timing_index, byte color, int width, int height) {
        TransferInfo transferInfo = new TransferInfo(0, 33, 9, 768, 0, new byte[]{-90, 2, timing_index, color, (byte) (width >> 8), (byte) width, (byte) (height >> 8), (byte) height}, 8, 1000);
        Log.d(TAG, "set_power_on value: " + nativeControlTransfer(this.mNativePtr, transferInfo.indexInterface, transferInfo.requestType, transferInfo.request, transferInfo.value, transferInfo.index, transferInfo.buffer, transferInfo.length, transferInfo.timeout));
    }

    public byte getDisplayHotPlug() {
        return xdata_read_atomic(50);
    }

    public int getVideoPort() {
        return xdata_read(49);
    }

    public int getSDRAMType() {
        return xdata_read(48);
    }

    public int getDisplayColorSpace() {
        return xdata_read(51);
    }

    public int converColortransferBulk(byte[] buff, int length, int width, int height, int stride, int color, int usethread, int clear) {
        if (this.mNativePtr == 0) {
            return 0;
        }
        return nativeConvertColorBulkTransfer(this.mNativePtr, 4, buff, length, 1000, width, height, stride, color, usethread, clear);
    }

    public int ClearPool() {
        return nativeClearPool();
    }

    public int claimInterface(int index) {
        Log.e(TAG, "claimInterface");
        return nativeClaimInterface(this.mNativePtr, 0);
    }

    public int releaseInterface(int index) {
        Log.e(TAG, "releaseInterface");
        return nativeRealseInterface(this.mNativePtr, 0);
    }
}
