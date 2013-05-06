package co.mczone.util;

public class RandomUtil {
	public static java.util.Random rand = new java.util.Random();

	public static int nextInt(int n) {
		return rand.nextInt(n) + 1;
	}
	
	public static double nextDouble() {
		return rand.nextDouble();
	}
	
	public static int between(int low, int high) {
		return rand.nextInt(high - low + 1) + low;
	}
}
