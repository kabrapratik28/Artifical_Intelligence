package competitionJava;
import java.util.ArrayList;

public class raidValueOccuipedData {
	ArrayList<ArrayList<String>> boardXYValues;
	boolean isNeighbourChanged;
	
	public raidValueOccuipedData(ArrayList<ArrayList<String>> boardXYValues,boolean isNeighbourChanged){
		this.boardXYValues = boardXYValues;
		this.isNeighbourChanged = isNeighbourChanged;
	}
}
