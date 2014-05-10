package model;

public enum Scenario {
	Princess(1),Death(2);
	
	private int value;
	
	private Scenario(int value){
		this.value=value;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public boolean compare(Scenario other){
		if(other.getValue() == this.getValue()){
			return true;
		}
		return false;
	}
}
