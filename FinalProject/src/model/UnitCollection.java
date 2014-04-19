package model;

import java.awt.List;
import java.util.ArrayList;

public class UnitCollection {
	
	ArrayList<Unit> unitList;
	
	public UnitCollection(){
		unitList = new ArrayList<>();
		Unit goku = new Goku('g');
		Unit link = new Link('l');
		Unit mario = new Mario('m');
		Unit megaMan = new MegaMan('M');
		Unit sonic = new Sonic('s');
		//can add more
		unitList.add(goku);
		unitList.add(link);
		unitList.add(mario);
		unitList.add(megaMan);
		unitList.add(sonic);
	}
	
	public ArrayList<Unit> getList(){
		return unitList;
	}
	
	public Unit getUnit(char c){
		
		for(Unit unit : unitList){
			if(unit.getCharRepresentation()==c){
				return unit;
			}
		}
		return null;
		
	}
	
	public String toString(){
		String str = "";
		
		for(int i = 0; i < unitList.size(); i++){
			str += unitList.get(i).getName() + "\t";
			str += unitList.get(i).getAttackPower() + "\t";
			str += unitList.get(i).getAttackRange() + "\t";
			str += unitList.get(i).getHealth();
			str += "\n";
		}
		return str;
	}
}
