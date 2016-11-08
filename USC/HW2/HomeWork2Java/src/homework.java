import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.*;

class answerGiver {
	int maxValueObtained;
	ArrayList<ArrayList<String>> bestMove;
	Tuple<Integer,Integer> xyCoordinatesOfAction;
	
	public answerGiver(int maxValueObtained,ArrayList<ArrayList<String>> bestMove,Tuple<Integer,Integer> xyCoordinatesOfAction){
		this.maxValueObtained = maxValueObtained;
		this.bestMove = bestMove;
		this.xyCoordinatesOfAction = xyCoordinatesOfAction;
	}
}
class InpuData {
	int dimensionOFBoard;
	String modeOFGame;
	String youPlay;
	int depthOfSearch;
	ArrayList<ArrayList<Integer>> boardValues;
	ArrayList<ArrayList<String>> occupiedPositions;
	
	public InpuData(int dimensionOFBoard,String modeOFGame,String youPlay,int depthOfSearch,ArrayList<ArrayList<Integer>> boardValues,ArrayList<ArrayList<String>> occupiedPositions) {
		this.dimensionOFBoard = dimensionOFBoard;
		this.modeOFGame = modeOFGame;
		this.youPlay = youPlay;
		this.depthOfSearch = depthOfSearch;
		this.boardValues = boardValues;
		this.occupiedPositions = occupiedPositions;
	}	
}

class raidValueOccuipedData {
	ArrayList<ArrayList<String>> boardXYValues;
	boolean isNeighbourChanged;
	
	public raidValueOccuipedData(ArrayList<ArrayList<String>> boardXYValues,boolean isNeighbourChanged){
		this.boardXYValues = boardXYValues;
		this.isNeighbourChanged = isNeighbourChanged;
	}
}

class Tuple<X, Y> { 
	  public final X x; 
	  public final Y y; 
	  public Tuple(X x, Y y) { 
	    this.x = x; 
	    this.y = y; 
	  } 
	} 

public class homework {

	static Map<Integer, String> mapOfColumnName;

	static {
		mapOfColumnName = new HashMap<Integer, String>() {
			{
				put(0, "A");
				put(1, "B");
				put(2, "C");
				put(3, "D");
				put(4, "E");
				put(5, "F");
				put(6, "G");
				put(7, "H");
				put(8, "I");
				put(9, "J");
				put(10, "K");
				put(11, "L");
				put(12, "M");
				put(13, "N");
				put(14, "O");
				put(15, "P");
				put(16, "Q");
				put(17, "R");
				put(18, "S");
				put(19, "T");
				put(20, "U");
				put(21, "V");
				put(22, "W");
				put(23, "X");
				put(24, "Y");
				put(25, "Z");
			}

		};
	}

	static ArrayList<ArrayList<String>> deepCopy(ArrayList<ArrayList<String>> old) {
		ArrayList<ArrayList<String>> deepCopied = new ArrayList<>();
		for (ArrayList<String> eachArrayList : old) {
			ArrayList<String> newEachRow = new ArrayList();
			for (String eachValue : eachArrayList) {
				newEachRow.add(eachValue);
			}
			deepCopied.add(newEachRow);
		}
		return deepCopied;
	}

	InpuData readInput(String inputFilename) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(inputFilename));
		int dimensionOFBoard = Integer.parseInt(br.readLine());
		String modeOFGame = br.readLine();
		String youPlay = br.readLine();
		int depthOfSearch = Integer.parseInt(br.readLine());
		ArrayList<ArrayList<Integer>> boardValues = new ArrayList<>();
		for (int row = 0; row < dimensionOFBoard; row++) {
			String boardValuesC = br.readLine();
			String[] boardValuesInString = boardValuesC.split("\\s+");
			int[] boardValuesArray = Arrays.asList(boardValuesInString).stream().mapToInt(Integer::parseInt).toArray();
			ArrayList<Integer> eachRow = new ArrayList<>();
			for (int eachValue : boardValuesArray) {
				eachRow.add(eachValue);
			}
			boardValues.add(eachRow);
		}

		ArrayList<ArrayList<String>> occupiedPositions = new ArrayList<>();
		for (int row = 0; row < dimensionOFBoard; row++) {
			String boardValuesC = br.readLine();
			ArrayList<String> eachRow = new ArrayList<>();
			for (int i = 0; i < boardValuesC.length(); i++) {
				char c = boardValuesC.charAt(i);
				eachRow.add(Character.toString(c));
			}
			occupiedPositions.add(eachRow);
		}
		br.close();
		return new InpuData(dimensionOFBoard, modeOFGame, youPlay, depthOfSearch, boardValues, occupiedPositions);
	}

	void writeOutPutToFile(String answer, String outputFileName, boolean print) throws FileNotFoundException {
		if (!print) {
			try (PrintWriter out = new PrintWriter(outputFileName)) {
				out.println(answer);
			}
		} else {
			System.out.println(answer);
		}
	}

	boolean isRaidMove(ArrayList<ArrayList<String>> boardXYValues, int currentRow, int currentColoumn,
			String playerTurn) {
		int lenghtOfBoard = boardXYValues.size();

		if ((currentRow - 1 >= 0) && boardXYValues.get(currentRow - 1).get(currentColoumn).equals(playerTurn)) {
			return true;
		}

		if ((currentRow + 1 < lenghtOfBoard)
				&& boardXYValues.get(currentRow + 1).get(currentColoumn).equals(playerTurn)) {
			return true;
		}

		if ((currentColoumn - 1 >= 0) && boardXYValues.get(currentRow).get(currentColoumn - 1).equals(playerTurn)) {
			return true;
		}

		if ((currentColoumn + 1 < lenghtOfBoard)
				&& boardXYValues.get(currentRow).get(currentColoumn + 1).equals(playerTurn)) {
			return true;
		}
		return false;
	}

	raidValueOccuipedData raidOccupyingNeighbours(ArrayList<ArrayList<String>> boardXYValues, int currentRow,
			int currentColoumn, String playerTurn) {
		boolean flagIsNeighboursChanged = false;
		int lenghtOfBoard = boardXYValues.size();
		String oppositePlayerValue = oppositePlayer(playerTurn);
		if (isRaidMove(boardXYValues, currentRow, currentColoumn, playerTurn)) {
			if ((currentRow - 1 >= 0)
					&& boardXYValues.get(currentRow - 1).get(currentColoumn).equals(oppositePlayerValue)) {
				boardXYValues.get(currentRow - 1).set(currentColoumn, playerTurn);
				flagIsNeighboursChanged = true;
			}
			if ((currentRow + 1 < lenghtOfBoard)
					&& boardXYValues.get(currentRow + 1).get(currentColoumn).equals(oppositePlayerValue)) {
				boardXYValues.get(currentRow + 1).set(currentColoumn, playerTurn);
				flagIsNeighboursChanged = true;
			}
			if (currentColoumn - 1 >= 0
					&& boardXYValues.get(currentRow).get(currentColoumn - 1).equals(oppositePlayerValue)) {
				boardXYValues.get(currentRow).set(currentColoumn - 1, playerTurn);
				flagIsNeighboursChanged = true;
			}
			if (currentColoumn + 1 < lenghtOfBoard
					&& boardXYValues.get(currentRow).get(currentColoumn + 1).equals(oppositePlayerValue)) {
				boardXYValues.get(currentRow).set(currentColoumn + 1, playerTurn);
				flagIsNeighboursChanged = true;
			}
		}
		return (new raidValueOccuipedData(boardXYValues, flagIsNeighboursChanged));
	}

	ArrayList<Tuple<Integer, Integer>> nextPossibleActions(ArrayList<ArrayList<String>> boardXYValues,
			String playerTurn) {
		int lenghtOfBoard = boardXYValues.size();
		ArrayList<Tuple<Integer, Integer>> nextValidMoves = new ArrayList<>();
		ArrayList<Tuple<Integer, Integer>> nextValidRaidMoves = new ArrayList<>();
		for (int row = 0; row < lenghtOfBoard; row++) {
			for (int clm = 0; clm < lenghtOfBoard; clm++) {
				if (boardXYValues.get(row).get(clm).equals(".")) {
					if (!this.isRaidMove(boardXYValues, row, clm, playerTurn)) {
						nextValidMoves.add(new Tuple<Integer, Integer>(row, clm));
					} else {
						nextValidRaidMoves.add(new Tuple<Integer, Integer>(row, clm));
					}
				}
			}
		}
		nextValidMoves.addAll(nextValidRaidMoves);
		return nextValidMoves;
	}

	ArrayList<ArrayList<String>> takeNextAction(ArrayList<ArrayList<String>> boardXYValues, String currentPlayer,
			Tuple<Integer, Integer> actionLocation) {
		ArrayList<ArrayList<String>> newBoardValue = deepCopy(boardXYValues);
		int xLocation = actionLocation.x;
		int yLocation = actionLocation.y;
		newBoardValue.get(xLocation).set(yLocation, currentPlayer);
		raidValueOccuipedData newBoardValueDetails = this.raidOccupyingNeighbours(newBoardValue, xLocation, yLocation,
				currentPlayer);
		return newBoardValueDetails.boardXYValues;
	}

	boolean isTerminalState(ArrayList<ArrayList<String>> boardXYValues) {
		int lenghtOfXYBoard = boardXYValues.size();
		for (int row = 0; row < lenghtOfXYBoard; row++) {
			for (int clm = 0; clm < lenghtOfXYBoard; clm++) {
				if (boardXYValues.get(row).get(clm).equals(".")) {
					return false;
				}
			}
		}
		return true;
	}

	int heuristicCalculator(ArrayList<ArrayList<Integer>> juiceValues, ArrayList<ArrayList<String>> boardXYValues,
			String maxPlayer) {
		int heuristicValueOfX = 0;
		int heuristicValueOfO = 0;

		int lenghtOfXYBoard = boardXYValues.size();
		for (int row = 0; row < lenghtOfXYBoard; row++) {
			for (int clm = 0; clm < lenghtOfXYBoard; clm++) {
				String value = boardXYValues.get(row).get(clm);
				if (value.equals("X")) {
					heuristicValueOfX = heuristicValueOfX + juiceValues.get(row).get(clm);
				} else if (value.equals("O")) {
					heuristicValueOfO = heuristicValueOfO + juiceValues.get(row).get(clm);
				}
			}
		}

		if (maxPlayer.equals("X")) {
			return heuristicValueOfX - heuristicValueOfO;
		} else {
			return heuristicValueOfO - heuristicValueOfX;
		}
	}

	String oppositePlayer(String currentPlayer) {
		String oppositePlayer = "";
		if (currentPlayer.equals("X")) {
			oppositePlayer = "O";
		} else {
			oppositePlayer = "X";
		}
		return oppositePlayer;
	}

	String answerConverter(ArrayList<ArrayList<String>> oldValues, ArrayList<ArrayList<String>> newValues,
			Tuple<Integer, Integer> xyCoordinatesOfAction) {
		int lenghtOfvalues = oldValues.size();
		int x = xyCoordinatesOfAction.x;
		int y = xyCoordinatesOfAction.y;
		String playerName = newValues.get(x).get(y);
		StringBuffer answer = new StringBuffer();

		int xNew = x + 1;
		String yNew = mapOfColumnName.get(y);
		answer.append(yNew);
		answer.append(Integer.toString(xNew));
		answer.append(" ");

		ArrayList<ArrayList<String>> tempValues = deepCopy(oldValues);
		tempValues.get(x).set(y, playerName);
		if (raidOccupyingNeighbours(tempValues, x, y, playerName).isNeighbourChanged) {
			answer.append("Raid");
		} else {
			answer.append("Stake");
		}
		answer.append("\n");

		for (int row = 0; row < lenghtOfvalues; row++) {
			for (int clm = 0; clm < lenghtOfvalues; clm++) {
				answer.append(newValues.get(row).get(clm));
			}
			answer.append("\n");
		}

		return answer.toString();
	}

	answerGiver minimax(ArrayList<ArrayList<Integer>> juiceValues, ArrayList<ArrayList<String>> boardXYValues,
			int depthLimit, String maxPlayer) {
		int currentDepth = 1;
		String playerTurn = maxPlayer;
		int maxValueObtained = Integer.MIN_VALUE;
		ArrayList<ArrayList<String>> bestMove = deepCopy(boardXYValues);
		Tuple<Integer, Integer> xyCoordinatesOfAction = new Tuple<Integer, Integer>(0, 0);

		ArrayList<Tuple<Integer, Integer>> nextValidMoves = nextPossibleActions(boardXYValues, maxPlayer);
		for (Tuple<Integer, Integer> eachNextValidAction : nextValidMoves) {
			ArrayList<ArrayList<String>> newBoardValue = takeNextAction(boardXYValues, playerTurn, eachNextValidAction);
			answerGiver maxStateInTree = minValue(juiceValues, newBoardValue, maxPlayer, depthLimit, currentDepth + 1,
					oppositePlayer(playerTurn));

			if (maxStateInTree.maxValueObtained > maxValueObtained) {
				maxValueObtained = maxStateInTree.maxValueObtained;
				bestMove = deepCopy(newBoardValue);
				xyCoordinatesOfAction = eachNextValidAction;
			}
		}
		return (new answerGiver(maxValueObtained, bestMove, xyCoordinatesOfAction));
	}

	answerGiver minValue(ArrayList<ArrayList<Integer>> juiceValues, ArrayList<ArrayList<String>> boardXYValues,
			String maxPlayer, int depthLimit, int currentDepth, String playerTurn) {
		if (isTerminalState(boardXYValues) || depthLimit < currentDepth) {
			return (new answerGiver(heuristicCalculator(juiceValues, boardXYValues, maxPlayer), boardXYValues, null));
		}

		int minValueObtained = Integer.MAX_VALUE;
		ArrayList<ArrayList<String>> bestMove = deepCopy(boardXYValues);

		ArrayList<Tuple<Integer, Integer>> nextValidMoves = nextPossibleActions(boardXYValues, playerTurn);
		for (Tuple<Integer, Integer> eachNextValidAction : nextValidMoves) {
			ArrayList<ArrayList<String>> newBoardValue = takeNextAction(boardXYValues, playerTurn, eachNextValidAction);
			answerGiver maxStateInTree = maxValue(juiceValues, newBoardValue, maxPlayer, depthLimit, currentDepth + 1,
					oppositePlayer(playerTurn));
			if (maxStateInTree.maxValueObtained < minValueObtained) {
				minValueObtained = maxStateInTree.maxValueObtained;
				bestMove = deepCopy(newBoardValue);
			}
		}
		return new answerGiver(minValueObtained, bestMove, null);
	}

	answerGiver maxValue(ArrayList<ArrayList<Integer>> juiceValues, ArrayList<ArrayList<String>> boardXYValues,
			String maxPlayer, int depthLimit, int currentDepth, String playerTurn) {
		if (isTerminalState(boardXYValues) || depthLimit < currentDepth) {
			return new answerGiver(heuristicCalculator(juiceValues, boardXYValues, maxPlayer), boardXYValues, null);
		}
		int maxValueObtained = Integer.MIN_VALUE;
		ArrayList<ArrayList<String>> bestMove = deepCopy(boardXYValues);

		ArrayList<Tuple<Integer, Integer>> nextValidMoves = nextPossibleActions(boardXYValues, playerTurn);
		for (Tuple<Integer, Integer> eachNextValidAction : nextValidMoves) {
			ArrayList<ArrayList<String>> newBoardValue = takeNextAction(boardXYValues, playerTurn, eachNextValidAction);
			answerGiver maxStateInTree = minValue(juiceValues, newBoardValue, maxPlayer, depthLimit, currentDepth + 1,
					oppositePlayer(playerTurn));
			if (maxStateInTree.maxValueObtained > maxValueObtained) {
				maxValueObtained = maxStateInTree.maxValueObtained;
				bestMove = deepCopy(newBoardValue);
			}
		}
		return new answerGiver(maxValueObtained, bestMove, null);
	}

	answerGiver alphaBeta(ArrayList<ArrayList<Integer>> juiceValues, ArrayList<ArrayList<String>> boardXYValues,
			int depthLimit, String maxPlayer) {
		int lenghtOfBoard = juiceValues.size();
		int currentDepth = 1;
		String playerTurn = maxPlayer;
		int alphaValue = Integer.MIN_VALUE;
		int betaValue = Integer.MAX_VALUE;
		ArrayList<ArrayList<String>> bestMove = deepCopy(boardXYValues);
		Tuple<Integer, Integer> xyCoordinatesOfAction = new Tuple<Integer, Integer>(0, 0);

		ArrayList<Tuple<Integer, Integer>> nextValidMoves = nextPossibleActions(boardXYValues, maxPlayer);
		for (Tuple<Integer, Integer> eachNextValidAction : nextValidMoves) {
			ArrayList<ArrayList<String>> newBoardValue = takeNextAction(boardXYValues, playerTurn, eachNextValidAction);
			answerGiver maxStateInTree = minAlphaBeta(juiceValues, newBoardValue, maxPlayer, depthLimit,
					currentDepth + 1, oppositePlayer(playerTurn), alphaValue, betaValue);

			int currentAlphaValue = maxStateInTree.maxValueObtained;
			if (currentAlphaValue >= betaValue) {
				return (new answerGiver(currentAlphaValue, deepCopy(newBoardValue), eachNextValidAction));
			}
			if (currentAlphaValue > alphaValue) {
				alphaValue = currentAlphaValue;
				bestMove = deepCopy(newBoardValue);
				xyCoordinatesOfAction = eachNextValidAction;
			}
		}
		return (new answerGiver(alphaValue, bestMove, xyCoordinatesOfAction));
	}

	answerGiver maxAlphaBeta(ArrayList<ArrayList<Integer>> juiceValues, ArrayList<ArrayList<String>> boardXYValues,
			String maxPlayer, int depthLimit, int currentDepth, String playerTurn, int alphaValue, int betaValue) {
		if (isTerminalState(boardXYValues) || depthLimit < currentDepth) {
			return (new answerGiver(heuristicCalculator(juiceValues, boardXYValues, maxPlayer), boardXYValues, null));
		}
		ArrayList<ArrayList<String>> bestMove = deepCopy(boardXYValues);
		ArrayList<Tuple<Integer, Integer>> nextValidMoves = nextPossibleActions(boardXYValues, playerTurn);
		for (Tuple<Integer, Integer> eachNextValidAction : nextValidMoves) {
			ArrayList<ArrayList<String>> newBoardValue = takeNextAction(boardXYValues, playerTurn, eachNextValidAction);
			answerGiver maxStateInTree = minAlphaBeta(juiceValues, newBoardValue, maxPlayer, depthLimit,
					currentDepth + 1, oppositePlayer(playerTurn), alphaValue, betaValue);
			int currentAlphaValue = maxStateInTree.maxValueObtained;
			if (currentAlphaValue >= betaValue) {
				return (new answerGiver(currentAlphaValue, deepCopy(newBoardValue), null));
			}
			if (currentAlphaValue > alphaValue) {
				alphaValue = currentAlphaValue;
				bestMove = deepCopy(newBoardValue);
			}
		}
		return (new answerGiver(alphaValue, bestMove, null));
	}

	answerGiver minAlphaBeta(ArrayList<ArrayList<Integer>> juiceValues, ArrayList<ArrayList<String>> boardXYValues,
			String maxPlayer, int depthLimit, int currentDepth, String playerTurn, int alphaValue, int betaValue) {
		if (isTerminalState(boardXYValues) || depthLimit < currentDepth) {
			return (new answerGiver(heuristicCalculator(juiceValues, boardXYValues, maxPlayer), boardXYValues, null));
		}
		ArrayList<ArrayList<String>> bestMove = deepCopy(boardXYValues);
		ArrayList<Tuple<Integer, Integer>> nextValidMoves = nextPossibleActions(boardXYValues, playerTurn);

		for (Tuple<Integer, Integer> eachNextValidAction : nextValidMoves) {
			ArrayList<ArrayList<String>> newBoardValue = takeNextAction(boardXYValues, playerTurn, eachNextValidAction);
			answerGiver maxStateInTree = maxAlphaBeta(juiceValues, newBoardValue, maxPlayer, depthLimit,
					currentDepth + 1, oppositePlayer(playerTurn), alphaValue, betaValue);
			int currentBetaValue = maxStateInTree.maxValueObtained;
			if (currentBetaValue <= alphaValue) {
				return (new answerGiver(currentBetaValue, deepCopy(newBoardValue), null));
			}
			if (currentBetaValue < betaValue) {
				betaValue = currentBetaValue;
				bestMove = deepCopy(newBoardValue);
			}
		}
		return (new answerGiver(betaValue, bestMove, null));
	}

	public static String runHomeWork(String fileName) throws NumberFormatException, IOException {
		homework hw = new homework();
		InpuData inputData = hw.readInput(fileName);
		String answerInString;
		if (inputData.modeOFGame.equals("MINIMAX")) {
			answerGiver heuristicAndPositions = hw.minimax(inputData.boardValues, inputData.occupiedPositions,
					inputData.depthOfSearch, inputData.youPlay);
			answerInString = hw.answerConverter(inputData.occupiedPositions, heuristicAndPositions.bestMove,
					heuristicAndPositions.xyCoordinatesOfAction);
		} else {
			answerGiver heuristicAndPositions = hw.alphaBeta(inputData.boardValues, inputData.occupiedPositions,
					inputData.depthOfSearch, inputData.youPlay);
			answerInString = hw.answerConverter(inputData.occupiedPositions, heuristicAndPositions.bestMove,
					heuristicAndPositions.xyCoordinatesOfAction);
		}
		return answerInString;
	}

	public static void main(String[] args) throws NumberFormatException, IOException {
		homework hw = new homework();
		InpuData inputData = hw.readInput("input.txt");
		if (inputData.modeOFGame.equals("MINIMAX")) {
			answerGiver heuristicAndPositions = hw.minimax(inputData.boardValues, inputData.occupiedPositions,
					inputData.depthOfSearch, inputData.youPlay);
			String answerInString = hw.answerConverter(inputData.occupiedPositions, heuristicAndPositions.bestMove,
					heuristicAndPositions.xyCoordinatesOfAction);
			hw.writeOutPutToFile(answerInString, "output.txt", false);
		} else {
			answerGiver heuristicAndPositions = hw.alphaBeta(inputData.boardValues, inputData.occupiedPositions,
					inputData.depthOfSearch, inputData.youPlay);
			String answerInString = hw.answerConverter(inputData.occupiedPositions, heuristicAndPositions.bestMove,
					heuristicAndPositions.xyCoordinatesOfAction);
			hw.writeOutPutToFile(answerInString, "output.txt", false);
		}
	}

}
