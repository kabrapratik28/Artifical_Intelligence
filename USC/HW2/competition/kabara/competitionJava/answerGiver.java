package competitionJava;

import java.util.ArrayList;
import java.util.HashMap;

public class answerGiver {
	float maxValueObtained;
	
	ArrayList<ArrayList<String>> bestMove;
	Tuple<Integer,Integer> xyCoordinatesOfAction;
	HashMap<String, Cache> cacheOfChilds;
	Cache cacheNewToReturn;
	public answerGiver(float maxValueObtained,ArrayList<ArrayList<String>> bestMove,Tuple<Integer,Integer> xyCoordinatesOfAction,Cache cacheNewToReturn){
		this.maxValueObtained = maxValueObtained;
		this.bestMove = bestMove;
		this.xyCoordinatesOfAction = xyCoordinatesOfAction;
		this.cacheNewToReturn = cacheNewToReturn;
	}
	
	public answerGiver(ArrayList<ArrayList<String>> bestMove,Tuple<Integer,Integer> xyCoordinatesOfAction,HashMap<String, Cache> cacheOfChilds){
		this.bestMove = bestMove;
		this.xyCoordinatesOfAction = xyCoordinatesOfAction;
		this.cacheOfChilds = cacheOfChilds;
	}

}
