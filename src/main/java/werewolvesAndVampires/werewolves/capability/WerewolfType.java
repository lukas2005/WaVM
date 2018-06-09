package werewolvesAndVampires.werewolves.capability;

public enum WerewolfType {
NONE(0), INFECTED(1), FULL(2);
	
	public final int id;
	WerewolfType(int in) {
		id = in;
	}
	
	public static WerewolfType getEnumFromId(int in) {
		switch (in) {
		case 0:
			return WerewolfType.NONE;
		case 1:
			return WerewolfType.INFECTED;
		case 2:
			return WerewolfType.FULL;
		default:
			return WerewolfType.NONE;
		}
	}
}
