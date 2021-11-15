package com.p004ms.ms2160.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* renamed from: com.ms.ms2160.myapplication.DeviceFilter */
public final class DeviceFilter {
    private static final String TAG = "DeviceFilter";
    public final boolean isExclude;
    public final int mClass;
    public final String mManufacturerName;
    public final int mProductId;
    public final String mProductName;
    public final int mProtocol;
    public final String mSerialNumber;
    public final int mSubclass;
    public final int mVendorId;

    public DeviceFilter(int vid, int pid, int clasz, int subclass, int protocol, String manufacturer, String product, String serialNum) {
        this(vid, pid, clasz, subclass, protocol, manufacturer, product, serialNum, false);
    }

    public DeviceFilter(int vid, int pid, int clasz, int subclass, int protocol, String manufacturer, String product, String serialNum, boolean isExclude2) {
        this.mVendorId = vid;
        this.mProductId = pid;
        this.mClass = clasz;
        this.mSubclass = subclass;
        this.mProtocol = protocol;
        this.mManufacturerName = manufacturer;
        this.mProductName = product;
        this.mSerialNumber = serialNum;
        this.isExclude = isExclude2;
    }

    public DeviceFilter(UsbDevice device) {
        this(device, false);
    }

    public DeviceFilter(UsbDevice device, boolean isExclude2) {
        this.mVendorId = device.getVendorId();
        this.mProductId = device.getProductId();
        this.mClass = device.getDeviceClass();
        this.mSubclass = device.getDeviceSubclass();
        this.mProtocol = device.getDeviceProtocol();
        this.mManufacturerName = null;
        this.mProductName = null;
        this.mSerialNumber = null;
        this.isExclude = isExclude2;
    }

    public static List<DeviceFilter> getDeviceFilters(Context context, int deviceFilterXmlId) {
        DeviceFilter deviceFilter;
        XmlPullParser parser = context.getResources().getXml(deviceFilterXmlId);
        List<DeviceFilter> deviceFilters = new ArrayList<>();
        try {
            for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
                if (eventType == 2 && (deviceFilter = readEntryOne(context, parser)) != null) {
                    deviceFilters.add(deviceFilter);
                }
            }
        } catch (XmlPullParserException e) {
            Log.d(TAG, "XmlPullParserException", e);
        } catch (IOException e2) {
            Log.d(TAG, "IOException", e2);
        }
        return Collections.unmodifiableList(deviceFilters);
    }

    private static final int getAttributeInteger(Context context, XmlPullParser parser, String namespace, String name, int defaultValue) {
        int result = defaultValue;
        try {
            String v = parser.getAttributeValue(namespace, name);
            if (TextUtils.isEmpty(v) || !v.startsWith("@")) {
                int radix = 10;
                if (v != null && v.length() > 2 && v.charAt(0) == '0' && (v.charAt(1) == 'x' || v.charAt(1) == 'X')) {
                    radix = 16;
                    v = v.substring(2);
                }
                return Integer.parseInt(v, radix);
            }
            int resId = context.getResources().getIdentifier(v.substring(1), (String) null, context.getPackageName());
            return resId > 0 ? context.getResources().getInteger(resId) : result;
        } catch (Resources.NotFoundException e) {
            return defaultValue;
        } catch (NumberFormatException e2) {
            return defaultValue;
        } catch (NullPointerException e3) {
            return defaultValue;
        }
    }

    private static final boolean getAttributeBoolean(Context context, XmlPullParser parser, String namespace, String name, boolean defaultValue) {
        boolean result = defaultValue;
        try {
            String v = parser.getAttributeValue(namespace, name);
            if ("TRUE".equalsIgnoreCase(v)) {
                return true;
            }
            if ("FALSE".equalsIgnoreCase(v)) {
                return false;
            }
            if (TextUtils.isEmpty(v) || !v.startsWith("@")) {
                int radix = 10;
                if (v != null && v.length() > 2 && v.charAt(0) == '0' && (v.charAt(1) == 'x' || v.charAt(1) == 'X')) {
                    radix = 16;
                    v = v.substring(2);
                }
                return Integer.parseInt(v, radix) != 0;
            }
            int resId = context.getResources().getIdentifier(v.substring(1), (String) null, context.getPackageName());
            if (resId > 0) {
                return context.getResources().getBoolean(resId);
            }
            return result;
        } catch (Resources.NotFoundException e) {
            return defaultValue;
        } catch (NumberFormatException e2) {
            return defaultValue;
        } catch (NullPointerException e3) {
            return defaultValue;
        }
    }

    private static final String getAttributeString(Context context, XmlPullParser parser, String namespace, String name, String defaultValue) {
        int resId;
        String str = defaultValue;
        try {
            String result = parser.getAttributeValue(namespace, name);
            if (result == null) {
                result = defaultValue;
            }
            if (TextUtils.isEmpty(result) || !result.startsWith("@") || (resId = context.getResources().getIdentifier(result.substring(1), (String) null, context.getPackageName())) <= 0) {
                return result;
            }
            return context.getResources().getString(resId);
        } catch (Resources.NotFoundException e) {
            return defaultValue;
        } catch (NumberFormatException e2) {
            return defaultValue;
        } catch (NullPointerException e3) {
            return defaultValue;
        }
    }

    public static DeviceFilter readEntryOne(Context context, XmlPullParser parser) throws XmlPullParserException, IOException {
        int vendorId = -1;
        int productId = -1;
        int deviceClass = -1;
        int deviceSubclass = -1;
        int deviceProtocol = -1;
        boolean exclude = false;
        String manufacturerName = null;
        String productName = null;
        String serialNumber = null;
        boolean hasValue = false;
        int eventType = parser.getEventType();
        while (eventType != 1) {
            String tag = parser.getName();
            if (!TextUtils.isEmpty(tag) && tag.equalsIgnoreCase("usb-device")) {
                if (eventType == 2) {
                    hasValue = true;
                    vendorId = getAttributeInteger(context, parser, (String) null, "vendor-id", -1);
                    if (vendorId == -1 && (vendorId = getAttributeInteger(context, parser, (String) null, "vendorId", -1)) == -1) {
                        vendorId = getAttributeInteger(context, parser, (String) null, "venderId", -1);
                    }
                    productId = getAttributeInteger(context, parser, (String) null, "product-id", -1);
                    if (productId == -1) {
                        productId = getAttributeInteger(context, parser, (String) null, "productId", -1);
                    }
                    deviceClass = getAttributeInteger(context, parser, (String) null, "class", -1);
                    deviceSubclass = getAttributeInteger(context, parser, (String) null, "subclass", -1);
                    deviceProtocol = getAttributeInteger(context, parser, (String) null, "protocol", -1);
                    manufacturerName = getAttributeString(context, parser, (String) null, "manufacturer-name", (String) null);
                    if (TextUtils.isEmpty(manufacturerName)) {
                        manufacturerName = getAttributeString(context, parser, (String) null, "manufacture", (String) null);
                    }
                    productName = getAttributeString(context, parser, (String) null, "product-name", (String) null);
                    if (TextUtils.isEmpty(productName)) {
                        productName = getAttributeString(context, parser, (String) null, "product", (String) null);
                    }
                    serialNumber = getAttributeString(context, parser, (String) null, "serial-number", (String) null);
                    if (TextUtils.isEmpty(serialNumber)) {
                        serialNumber = getAttributeString(context, parser, (String) null, "serial", (String) null);
                    }
                    exclude = getAttributeBoolean(context, parser, (String) null, "exclude", false);
                } else if (eventType == 3 && hasValue) {
                    return new DeviceFilter(vendorId, productId, deviceClass, deviceSubclass, deviceProtocol, manufacturerName, productName, serialNumber, exclude);
                }
            }
            eventType = parser.next();
        }
        return null;
    }

    private boolean matches(int clasz, int subclass, int protocol) {
        return (this.mClass == -1 || clasz == this.mClass) && (this.mSubclass == -1 || subclass == this.mSubclass) && (this.mProtocol == -1 || protocol == this.mProtocol);
    }

    public boolean matches(UsbDevice device) {
        if (this.mVendorId != -1 && device.getVendorId() != this.mVendorId) {
            return false;
        }
        if (this.mProductId != -1 && device.getProductId() != this.mProductId) {
            return false;
        }
        if (matches(device.getDeviceClass(), device.getDeviceSubclass(), device.getDeviceProtocol())) {
            return true;
        }
        int count = device.getInterfaceCount();
        for (int i = 0; i < count; i++) {
            UsbInterface intf = device.getInterface(i);
            if (matches(intf.getInterfaceClass(), intf.getInterfaceSubclass(), intf.getInterfaceProtocol())) {
                return true;
            }
        }
        return false;
    }

    public boolean isExclude(UsbDevice device) {
        return this.isExclude && matches(device);
    }

    public boolean matches(DeviceFilter f) {
        if (this.isExclude != f.isExclude) {
            return false;
        }
        if (this.mVendorId != -1 && f.mVendorId != this.mVendorId) {
            return false;
        }
        if (this.mProductId != -1 && f.mProductId != this.mProductId) {
            return false;
        }
        if (f.mManufacturerName != null && this.mManufacturerName == null) {
            return false;
        }
        if (f.mProductName != null && this.mProductName == null) {
            return false;
        }
        if (f.mSerialNumber != null && this.mSerialNumber == null) {
            return false;
        }
        if (this.mManufacturerName != null && f.mManufacturerName != null && !this.mManufacturerName.equals(f.mManufacturerName)) {
            return false;
        }
        if (this.mProductName != null && f.mProductName != null && !this.mProductName.equals(f.mProductName)) {
            return false;
        }
        if (this.mSerialNumber == null || f.mSerialNumber == null || this.mSerialNumber.equals(f.mSerialNumber)) {
            return matches(f.mClass, f.mSubclass, f.mProtocol);
        }
        return false;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this.mVendorId == -1 || this.mProductId == -1 || this.mClass == -1 || this.mSubclass == -1 || this.mProtocol == -1) {
            return false;
        }
        if (obj instanceof DeviceFilter) {
            DeviceFilter filter = (DeviceFilter) obj;
            if (filter.mVendorId != this.mVendorId || filter.mProductId != this.mProductId || filter.mClass != this.mClass || filter.mSubclass != this.mSubclass || filter.mProtocol != this.mProtocol) {
                return false;
            }
            if (filter.mManufacturerName != null && this.mManufacturerName == null) {
                return false;
            }
            if (filter.mManufacturerName == null && this.mManufacturerName != null) {
                return false;
            }
            if (filter.mProductName != null && this.mProductName == null) {
                return false;
            }
            if (filter.mProductName == null && this.mProductName != null) {
                return false;
            }
            if (filter.mSerialNumber != null && this.mSerialNumber == null) {
                return false;
            }
            if (filter.mSerialNumber == null && this.mSerialNumber != null) {
                return false;
            }
            if (filter.mManufacturerName != null && this.mManufacturerName != null && !this.mManufacturerName.equals(filter.mManufacturerName)) {
                return false;
            }
            if (filter.mProductName != null && this.mProductName != null && !this.mProductName.equals(filter.mProductName)) {
                return false;
            }
            if (filter.mSerialNumber != null && this.mSerialNumber != null && !this.mSerialNumber.equals(filter.mSerialNumber)) {
                return false;
            }
            if (filter.isExclude == this.isExclude) {
                z = false;
            }
            return z;
        } else if (!(obj instanceof UsbDevice)) {
            return false;
        } else {
            UsbDevice device = (UsbDevice) obj;
            if (!this.isExclude && device.getVendorId() == this.mVendorId && device.getProductId() == this.mProductId && device.getDeviceClass() == this.mClass && device.getDeviceSubclass() == this.mSubclass && device.getDeviceProtocol() == this.mProtocol) {
                return true;
            }
            return false;
        }
    }

    public int hashCode() {
        return ((this.mVendorId << 16) | this.mProductId) ^ (((this.mClass << 16) | (this.mSubclass << 8)) | this.mProtocol);
    }

    public String toString() {
        return "DeviceFilter[mVendorId=" + this.mVendorId + ",mProductId=" + this.mProductId + ",mClass=" + this.mClass + ",mSubclass=" + this.mSubclass + ",mProtocol=" + this.mProtocol + ",mManufacturerName=" + this.mManufacturerName + ",mProductName=" + this.mProductName + ",mSerialNumber=" + this.mSerialNumber + ",isExclude=" + this.isExclude + "]";
    }
}
