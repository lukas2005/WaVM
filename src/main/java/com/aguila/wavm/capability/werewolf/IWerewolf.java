package com.aguila.wavm.capability.werewolf;

public interface IWerewolf {
	
	public WerewolfType getWerewolfType();
	
	public void setWerewolfType(WerewolfType wwt);
	
	public boolean getIsTransformed();
	
	public void setIsTransformed(boolean is);
	
	public int getBloodLust();
	
	public void setBloodLust(int time);
	
	public int getTransformCount();
	
	public void incrementTransformCount();
	
	public void setTransformCount(int count);
	
	public void setEntity(int id);
	
	public int getEntity();
}
