package com.aguila.wavm.capability.werewolf;

public enum WerewolfType {

	IMMUNE,
	NONE,
	INFECTED,
	FULL;
	
	public static WerewolfType byOrdinal(int ordinal) {
		return values()[ordinal];
	}
}
