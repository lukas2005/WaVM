package werewolvesAndVampires.werewolves.capability;

public enum ControlLevel {
	NONE(0), LIMITED(1), ALL(2);

	public final int id;

	ControlLevel(int idd) {
		id = idd;
	}

	public static ControlLevel getEnumFromId(int in) {
		switch (in) {
		case 0:
			return ControlLevel.NONE;
		case 1:
			return ControlLevel.LIMITED;
		case 2:
			return ControlLevel.ALL;
		default:
			return ControlLevel.ALL;
		}
	}
}
