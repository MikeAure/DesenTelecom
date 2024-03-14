package com.lu.gademo.trace.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class LatLng {
    public final double latitude;
    public final double longitude;
    private static DecimalFormat a;

    public LatLng(double var1, double var3) {
        if (-180.0 <= var3 && var3 < 180.0) {
            this.longitude = a(var3);
        } else {
            this.longitude = a(((var3 - 180.0) % 360.0 + 360.0) % 360.0 - 180.0);
        }

        this.latitude = a(Math.max(-90.0, Math.min(90.0, var1)));
    }

    private static double a(double var0) {
        double var2 = var0;

        try {
            var2 = Double.parseDouble(a.format(var0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return var2;
    }

    public int hashCode() {
        byte var1 = 31;
        int var2 = 1;
        long var3 = Double.doubleToLongBits(this.latitude);
        var2 = var1 * var2 + (int)(var3 ^ var3 >>> 32);
        var3 = Double.doubleToLongBits(this.longitude);
        var2 = var1 * var2 + (int)(var3 ^ var3 >>> 32);
        return var2;
    }

    public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        } else if (!(var1 instanceof LatLng)) {
            return false;
        } else {
            LatLng var2 = (LatLng)var1;
            return this.latitude == var2.latitude && this.longitude == var2.longitude;
        }
    }

    public String toString() {
        return "lat/lng: (" + this.latitude + "," + this.longitude + ")";
    }

    public int describeContents() {
        return 0;
    }


    static {
        a = new DecimalFormat("0.000000", new DecimalFormatSymbols(Locale.US));
    }
}
