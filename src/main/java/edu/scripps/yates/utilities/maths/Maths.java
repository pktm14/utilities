package edu.scripps.yates.utilities.maths;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.rank.Median;

import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.list.array.TFloatArrayList;
import gnu.trove.list.array.TIntArrayList;

public class Maths {
	private Maths() {
	}

	/**
	 * Returns the maximum value in the array a[], -infinity if no such value.
	 */
	public static double max(double[] a) {
		double max = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < a.length; i++) {
			if (Double.isNaN(a[i]))
				return Double.NaN;
			if (a[i] > max)
				max = a[i];
		}
		return max;
	}

	/**
	 * Returns the maximum value in the subarray a[lo..hi], -infinity if no such
	 * value.
	 */
	public static double max(double[] a, int lo, int hi) {
		if (lo < 0 || hi >= a.length || lo > hi)
			throw new RuntimeException("Subarray indices out of bounds");
		double max = Double.NEGATIVE_INFINITY;
		for (int i = lo; i <= hi; i++) {
			if (Double.isNaN(a[i]))
				return Double.NaN;
			if (a[i] > max)
				max = a[i];
		}
		return max;
	}

	public static double max(Double... doubles) {
		double max = -Double.MAX_VALUE;
		for (final Double double1 : doubles) {
			if (double1 != null) {
				if (max < double1) {
					max = double1;
				}
			}
		}
		return max;
	}

	public static float max(Float... numbers) {
		float max = -Float.MAX_VALUE;
		for (final Float number : numbers) {
			if (number != null) {
				if (max < number) {
					max = number;
				}
			}
		}
		return max;
	}

	public static double min(Double... doubles) {
		double min = Double.MAX_VALUE;
		for (final Double double1 : doubles) {
			if (double1 != null) {
				if (min > double1) {
					min = double1;
				}
			}
		}
		return min;
	}

	public static float min(float... numbers) {
		float min = Float.MAX_VALUE;
		for (final Float number : numbers) {
			if (min > number) {
				min = number;
			}
		}
		return min;
	}

	/**
	 * Returns the maximum value in the array a[], Integer.MIN_VALUE if no such
	 * value.
	 */
	public static int max(int[] a) {
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < a.length; i++) {
			if (a[i] > max)
				max = a[i];
		}
		return max;
	}

	/**
	 * Returns the minimum value in the array a[], +infinity if no such value.
	 */
	public static double min(double[] a) {
		double min = Double.POSITIVE_INFINITY;
		for (int i = 0; i < a.length; i++) {
			if (Double.isNaN(a[i]))
				return Double.NaN;
			if (a[i] < min)
				min = a[i];
		}
		return min;
	}

	/**
	 * Returns the minimum value in the subarray a[lo..hi], +infinity if no such
	 * value.
	 */
	public static double min(double[] a, int lo, int hi) {
		if (lo < 0 || hi >= a.length || lo > hi)
			throw new RuntimeException("Subarray indices out of bounds");
		double min = Double.POSITIVE_INFINITY;
		for (int i = lo; i <= hi; i++) {
			if (Double.isNaN(a[i]))
				return Double.NaN;
			if (a[i] < min)
				min = a[i];
		}
		return min;
	}

	/**
	 * Returns the minimum value in the array a[], Integer.MAX_VALUE if no such
	 * value.
	 */
	public static int min(int[] a) {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < a.length; i++) {
			if (a[i] < min)
				min = a[i];
		}
		return min;
	}

	/**
	 * Returns the average value in the array a[], NaN if no such value.
	 */
	public static double mean(double[] a) {
		if (a.length == 0)
			return Double.NaN;
		final double sum = sum(a);
		return sum / a.length;
	}

	/**
	 * Returns the average value in the array a[], NaN if no such value.
	 */
	public static double mean(Double[] a) {
		if (a.length == 0)
			return Double.NaN;
		final double sum = sum(a);
		return sum / a.length;
	}

	/**
	 * Returns the average value in the array a[], NaN if no such value.
	 */
	public static double mean(TDoubleArrayList a) {
		if (a.isEmpty())
			return Double.NaN;

		return a.sum() / a.size();
	}

	/**
	 * Returns the average value in the array a[], NaN if no such value.
	 */
	public static double mean(TIntArrayList a) {
		if (a.isEmpty())
			return Double.NaN;

		return 1.0 * a.sum() / a.size();
	}

	/**
	 * Returns the average value in the array a[], NaN if no such value.
	 */
	public static float mean(TFloatArrayList a) {
		if (a.isEmpty())
			return Float.NaN;

		return a.sum() / a.size();
	}

	/**
	 * Returns the average value in the subarray a[lo..hi], NaN if no such
	 * value.
	 */
	public static double mean(double[] a, int lo, int hi) {
		final int length = hi - lo + 1;
		if (lo < 0 || hi >= a.length || lo > hi)
			throw new RuntimeException("Subarray indices out of bounds");
		if (length == 0)
			return Double.NaN;
		final double sum = sum(a, lo, hi);
		return sum / length;
	}

	/**
	 * Returns the average value in the array a[], NaN if no such value.
	 */
	public static double mean(int[] a) {
		if (a.length == 0)
			return Double.NaN;
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			sum = sum + a[i];
		}
		return sum / a.length;
	}

	/**
	 * Returns the sample variance in the array a[], NaN if no such value.
	 */
	public static double var(double[] a) {
		if (a.length == 0)
			return Double.NaN;
		final double avg = mean(a);
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			sum += (a[i] - avg) * (a[i] - avg);
		}
		return sum / (a.length - 1);
	}

	/**
	 * Returns the sample variance in the subarray a[lo..hi], NaN if no such
	 * value.
	 */
	public static double var(double[] a, int lo, int hi) {
		final int length = hi - lo + 1;
		if (lo < 0 || hi >= a.length || lo > hi)
			throw new RuntimeException("Subarray indices out of bounds");
		if (length == 0)
			return Double.NaN;
		final double avg = mean(a, lo, hi);
		double sum = 0.0;
		for (int i = lo; i <= hi; i++) {
			sum += (a[i] - avg) * (a[i] - avg);
		}
		return sum / (length - 1);
	}

	/**
	 * Returns the sample variance in the array a[], NaN if no such value.
	 */
	public static double var(int[] a) {
		if (a.length == 0)
			return Double.NaN;
		final double avg = mean(a);
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			sum += (a[i] - avg) * (a[i] - avg);
		}
		return sum / (a.length - 1);
	}

	/**
	 * Returns the population variance in the array a[], NaN if no such value.
	 */
	public static double varp(double[] a) {
		if (a.length == 0)
			return Double.NaN;
		final double avg = mean(a);
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			sum += (a[i] - avg) * (a[i] - avg);
		}
		return sum / a.length;
	}

	/**
	 * Returns the population variance in the subarray a[lo..hi], NaN if no such
	 * value.
	 */
	public static double varp(double[] a, int lo, int hi) {
		final int length = hi - lo + 1;
		if (lo < 0 || hi >= a.length || lo > hi)
			throw new RuntimeException("Subarray indices out of bounds");
		if (length == 0)
			return Double.NaN;
		final double avg = mean(a, lo, hi);
		double sum = 0.0;
		for (int i = lo; i <= hi; i++) {
			sum += (a[i] - avg) * (a[i] - avg);
		}
		return sum / length;
	}

	/**
	 * Returns the sample standard deviation in the array a[], NaN if no such
	 * value.
	 */
	public static double stddev(double[] a) {
		return Math.sqrt(var(a));
	}

	/**
	 * Returns the sample standard deviation in the array a[], NaN if no such
	 * value.
	 */
	public static double stddev(TDoubleArrayList a) {
		return stddev(a.toArray());
	}

	/**
	 * Returns the sample standard deviation in the subarray a[lo..hi], NaN if
	 * no such value.
	 */
	public static double stddev(double[] a, int lo, int hi) {
		return Math.sqrt(var(a, lo, hi));
	}

	/**
	 * Returns the sample standard deviation in the array a[], NaN if no such
	 * value.
	 */
	public static double stddev(int[] a) {
		return Math.sqrt(var(a));
	}

	/**
	 * Returns the sample standard deviation in the array a[], NaN if no such
	 * value.
	 */
	public static double stddev(TIntArrayList a) {
		return stddev(a.toArray());
	}

	/**
	 * Returns the population standard deviation in the array a[], NaN if no
	 * such value.
	 */
	public static double stddevp(double[] a) {
		return Math.sqrt(varp(a));
	}

	/**
	 * Returns the population standard deviation in the subarray a[lo..hi], NaN
	 * if no such value.
	 */
	public static double stddevp(double[] a, int lo, int hi) {
		return Math.sqrt(varp(a, lo, hi));
	}

	/**
	 * Returns the sum of all values in the array a[].
	 */
	public static double sum(double[] a) {
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			sum += a[i];
		}
		return sum;
	}

	public static double sum(Double[] a) {
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			if (a[i] != null) {
				sum += a[i];
			}
		}
		return sum;
	}

	/**
	 * Returns the sum of all values in the subarray a[lo..hi].
	 */
	public static double sum(double[] a, int lo, int hi) {
		if (lo < 0 || hi >= a.length || lo > hi)
			throw new RuntimeException("Subarray indices out of bounds");
		double sum = 0.0;
		for (int i = lo; i <= hi; i++) {
			sum += a[i];
		}
		return sum;
	}

	/**
	 * Returns the sum of all values in the array a[].
	 */
	public static int sum(int[] a) {
		int sum = 0;
		for (int i = 0; i < a.length; i++) {
			sum += a[i];
		}
		return sum;
	}

	/**
	 * Checks if the value is equals to Double.MAX_VALUE or Double.MIN_VALUE or
	 * the negative of these numbers
	 *
	 * @param value
	 * @return
	 */
	public static boolean isMaxOrMinValue(double value) {
		if (Double.compare(Double.MAX_VALUE, value) == 0 || Double.compare(Double.MIN_VALUE, value) == 0)
			return true;
		if (Double.compare(Double.MAX_VALUE, -value) == 0 || Double.compare(Double.MIN_VALUE, -value) == 0)
			return true;
		return false;
	}

	/**
	 * Calculates de median absolute deviation (MAD)
	 *
	 * @param values
	 * @return
	 */
	public static double mad(double[] values) {
		final Median medianCalculator = new Median();
		final double median = medianCalculator.evaluate(values);

		final double[] tmp = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			final double value = values[i];
			tmp[i] = Math.abs(value - median);
		}
		final double ret = medianCalculator.evaluate(tmp);
		return ret;
	}

	/**
	 * This is an outliers test: Mi=0.6745(xi−x~)/MAD where MAD is the median
	 * absolute deviation x~ is the median of the population and xi is the value
	 * to test wether is an outlier or not.<br>
	 *
	 *
	 * @param valueToTest
	 * @param populationValues
	 * @return if the return value is greater than 3.5, then it should be
	 *         considered as a potential outlier.
	 */
	public static double iglewiczHoaglinTest(double valueToTest, double[] populationValues) {
		final double factor = 0.6745;
		final Median medianCalculator = new Median();
		final double populationMedian = medianCalculator.evaluate(populationValues);
		final double mad = mad(populationValues);

		final double ret = Math.abs(factor * (valueToTest - populationMedian) / mad);
		return ret;

	}

	/**
	 * Z-score calculation: (value-mean)/stdev a z-score greater than 3 could be
	 * considered as an outlier
	 *
	 * @param valueToTest
	 * @param populationValues
	 * @return
	 */
	public static double zScore(double valueToTest, double[] populationValues) {

		final double populationMean = new Mean().evaluate(populationValues);
		final double populationSTD = new StandardDeviation().evaluate(populationValues);

		final double ret = (valueToTest - populationMean) / populationSTD;
		return ret;

	}

	public static void main(String[] args) {
		final double[] data = { 1.58, 0, 1.73 };

		System.out.println(iglewiczHoaglinTest(0, data));

	}

	public static int factorial(int n) {
		if (n >= 13) {
			throw new IllegalArgumentException("n cannot be greater than 13, otherwise the integer overflows");
		}
		if (n == 0) {
			return 1;
		} else {
			return n * factorial(n - 1);
		}
	}

	public static double log(double x, int base) {
		return Math.log(x) / Math.log(base);
	}
}
