package competitionJava;

import java.util.ArrayList;
import java.util.HashMap;

public class Cache implements java.io.Serializable {
	ArrayList<Tuple<Integer,Integer>> positions;
	HashMap<String,Cache> childStates;
	
	public Cache(){
		this.positions = new ArrayList<>();
		this.childStates = new HashMap<>();
	}
	
	public Cache(ArrayList<Tuple<Integer,Integer>> positions,HashMap<String,Cache> childStates){
		this.positions = positions;
		this.childStates = childStates;
	}
}
