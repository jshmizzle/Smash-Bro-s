package model;

import java.awt.List;
import java.util.ArrayList;

public class ItemCollection {
	
	ArrayList<Item> itemList;
	
	public ItemCollection(){
		itemList = new ArrayList<>();
		Item rage = new Rage();
		Item potion = new Potion();
		Item shield = new Shield();
		Item sneakers = new Sneakers();
		//can add more
		itemList.add(rage);
		itemList.add(potion);
		itemList.add(shield);
		itemList.add(sneakers);
	}
	
	public ArrayList<Item> getList(){
		return itemList;
	}
	
	public Item getItem(String s){
		
		for(Item item : itemList){
			if(item.getName().equals(s)){
				return item;
			}
		}
		return null;
		
	}
	
	public String toString(){
		String str = "";
		
		for(int i = 0; i < itemList.size(); i++){
			str += itemList.get(i).getName();
			str += "\n";
		}
		return str;
	}
}
