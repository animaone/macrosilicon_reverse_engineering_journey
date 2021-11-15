package com.p004ms.ms2160.myapplication;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Locale;

/* renamed from: com.ms.ms2160.myapplication.Size */
public class Size implements Parcelable {
    public static final Parcelable.Creator<Size> CREATOR = new Parcelable.Creator<Size>() {
        public Size createFromParcel(Parcel source) {
            return new Size(source);
        }

        public Size[] newArray(int size) {
            return new Size[size];
        }
    };
    public float[] fps;
    public int frameIntervalIndex;
    public int frameIntervalType;
    private String frameRates;
    public int frame_type;
    public int height;
    public int index;
    public int[] intervals;
    public int type;
    public int width;

    public Size(int _type, int _frame_type, int _index, int _width, int _height) {
        this.type = _type;
        this.frame_type = _frame_type;
        this.index = _index;
        this.width = _width;
        this.height = _height;
        this.frameIntervalType = -1;
        this.frameIntervalIndex = 0;
        this.intervals = null;
        updateFrameRate();
    }

    public Size(int _type, int _frame_type, int _index, int _width, int _height, int _min_intervals, int _max_intervals, int _step) {
        this.type = _type;
        this.frame_type = _frame_type;
        this.index = _index;
        this.width = _width;
        this.height = _height;
        this.frameIntervalType = 0;
        this.frameIntervalIndex = 0;
        this.intervals = new int[3];
        this.intervals[0] = _min_intervals;
        this.intervals[1] = _max_intervals;
        this.intervals[2] = _step;
        updateFrameRate();
    }

    public Size(int _type, int _frame_type, int _index, int _width, int _height, int[] _intervals) {
        int n;
        this.type = _type;
        this.frame_type = _frame_type;
        this.index = _index;
        this.width = _width;
        this.height = _height;
        if (_intervals != null) {
            n = _intervals.length;
        } else {
            n = -1;
        }
        if (n > 0) {
            this.frameIntervalType = n;
            this.intervals = new int[n];
            System.arraycopy(_intervals, 0, this.intervals, 0, n);
        } else {
            this.frameIntervalType = -1;
            this.intervals = null;
        }
        this.frameIntervalIndex = 0;
        updateFrameRate();
    }

    public Size(Size other) {
        this.type = other.type;
        this.frame_type = other.frame_type;
        this.index = other.index;
        this.width = other.width;
        this.height = other.height;
        this.frameIntervalType = other.frameIntervalType;
        this.frameIntervalIndex = other.frameIntervalIndex;
        int n = other.intervals != null ? other.intervals.length : -1;
        if (n > 0) {
            this.intervals = new int[n];
            System.arraycopy(other.intervals, 0, this.intervals, 0, n);
        } else {
            this.intervals = null;
        }
        updateFrameRate();
    }

    private Size(Parcel source) {
        this.type = source.readInt();
        this.frame_type = source.readInt();
        this.index = source.readInt();
        this.width = source.readInt();
        this.height = source.readInt();
        this.frameIntervalType = source.readInt();
        this.frameIntervalIndex = source.readInt();
        if (this.frameIntervalType >= 0) {
            if (this.frameIntervalType > 0) {
                this.intervals = new int[this.frameIntervalType];
            } else {
                this.intervals = new int[3];
            }
            source.readIntArray(this.intervals);
        } else {
            this.intervals = null;
        }
        updateFrameRate();
    }

    public Size set(Size other) {
        if (other != null) {
            this.type = other.type;
            this.frame_type = other.frame_type;
            this.index = other.index;
            this.width = other.width;
            this.height = other.height;
            this.frameIntervalType = other.frameIntervalType;
            this.frameIntervalIndex = other.frameIntervalIndex;
            int n = other.intervals != null ? other.intervals.length : -1;
            if (n > 0) {
                this.intervals = new int[n];
                System.arraycopy(other.intervals, 0, this.intervals, 0, n);
            } else {
                this.intervals = null;
            }
            updateFrameRate();
        }
        return this;
    }

    public float getCurrentFrameRate() throws IllegalStateException {
        int n = this.fps != null ? this.fps.length : 0;
        if (this.frameIntervalIndex >= 0 && this.frameIntervalIndex < n) {
            return this.fps[this.frameIntervalIndex];
        }
        throw new IllegalStateException("unknown frame rate or not ready");
    }

    public void setCurrentFrameRate(float frameRate) {
        int index2 = -1;
        int n = this.fps != null ? this.fps.length : 0;
        int i = 0;
        while (true) {
            if (i >= n) {
                break;
            } else if (this.fps[i] <= frameRate) {
                index2 = i;
                break;
            } else {
                i++;
            }
        }
        this.frameIntervalIndex = index2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeInt(this.frame_type);
        dest.writeInt(this.index);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeInt(this.frameIntervalType);
        dest.writeInt(this.frameIntervalIndex);
        if (this.intervals != null) {
            dest.writeIntArray(this.intervals);
        }
    }

    public void updateFrameRate() {
        int n = this.frameIntervalType;
        if (n > 0) {
            this.fps = new float[n];
            for (int i = 0; i < n; i++) {
                this.fps[i] = 1.0E7f / ((float) this.intervals[i]);
            }
        } else if (n == 0) {
            try {
                int min = Math.min(this.intervals[0], this.intervals[1]);
                int max = Math.max(this.intervals[0], this.intervals[1]);
                int step = this.intervals[2];
                if (step <= 0) {
                    float max_fps = 1.0E7f / ((float) min);
                    int m = 0;
                    for (float fps2 = 1.0E7f / ((float) min); fps2 <= max_fps; fps2 += 1.0f) {
                        m++;
                    }
                    this.fps = new float[m];
                    int m2 = 0;
                    float fps3 = 1.0E7f / ((float) min);
                    while (true) {
                        int m3 = m2;
                        if (fps3 > max_fps) {
                            break;
                        }
                        m2 = m3 + 1;
                        this.fps[m3] = fps3;
                        fps3 += 1.0f;
                    }
                } else {
                    int m4 = 0;
                    for (int i2 = min; i2 <= max; i2 += step) {
                        m4++;
                    }
                    this.fps = new float[m4];
                    int i3 = min;
                    int m5 = 0;
                    while (i3 <= max) {
                        this.fps[m5] = 1.0E7f / ((float) i3);
                        i3 += step;
                        m5++;
                    }
                }
            } catch (Exception e) {
                this.fps = null;
            }
        }
        int m6 = this.fps != null ? this.fps.length : 0;
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i4 = 0; i4 < m6; i4++) {
            sb.append(String.format(Locale.US, "%4.1f", new Object[]{Float.valueOf(this.fps[i4])}));
            if (i4 < m6 - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        this.frameRates = sb.toString();
        if (this.frameIntervalIndex > m6) {
            this.frameIntervalIndex = 0;
        }
    }

    public String toString() {
        float frame_rate = 0.0f;
        try {
            frame_rate = getCurrentFrameRate();
        } catch (Exception e) {
        }
        return String.format(Locale.US, "Size(%dx%d@%4.1f,type:%d,frame:%d,index:%d,%s)", new Object[]{Integer.valueOf(this.width), Integer.valueOf(this.height), Float.valueOf(frame_rate), Integer.valueOf(this.type), Integer.valueOf(this.frame_type), Integer.valueOf(this.index), this.frameRates});
    }
}
