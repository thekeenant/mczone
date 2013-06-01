package co.mczone.walls;

public class State {
	public static boolean PRE = true;
	public static boolean PREP = false;
	public static boolean PVP = false;

    public static void startPrep() {
        PRE = false;
        PREP = true;
        PVP = false;
    }
    public static void startPVP() {
        PRE = false;
        PREP = false;
        PVP = true;
    }
}
