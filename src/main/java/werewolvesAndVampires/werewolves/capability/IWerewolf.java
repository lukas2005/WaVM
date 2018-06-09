package werewolvesAndVampires.werewolves.capability;

public interface IWerewolf {
	
	public WerewolfType getWerewolfType();
	
	public void setWerewolfType(WerewolfType wwt);
	
	public boolean getIsTransformed();
	
	public void setIsTransformed(boolean is);
	
	public ControlLevel getControlLevel();
	
	public void setControlLevel(ControlLevel level);
	
	public int getTransformCount();
	
	public void incrementTransformCount();
	
	public void setTransformCount(int count);
}
