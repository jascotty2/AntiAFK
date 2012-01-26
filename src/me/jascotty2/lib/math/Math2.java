
package me.jascotty2.lib.math;

public class Math2 {
	static public double round(double d, int places) {
		double p = Math.pow(10, (double) places);
		return Math.round(d * p) / p;
	}
}
