package competitionJava;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class kabara {
	static float ha = (float)0.7;
	static float hb = (float)0.1;
	static float hc = (float)0.2;
	static Map<Integer, String> mapOfColumnName;
	static String cacheFilename = "kabara.cache";
	static int criticaState = 4; // 4 seconds
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

	public ArrayList<ArrayList<String>> deepCopy(ArrayList<ArrayList<String>> old) {
		ArrayList<ArrayList<String>> deepCopied = new ArrayList<>();
		for (ArrayList<String> eachArrayList : old) {
			ArrayList<String> newEachRow = new ArrayList<String>();
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
		float cpuTime = Float.parseFloat(br.readLine());
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
		return new InpuData(dimensionOFBoard, modeOFGame, youPlay, cpuTime, boardValues, occupiedPositions);
	}

	void writeOutPutToFile(String answer, String outputFileName, boolean print) throws FileNotFoundException {
		if (!print) {
			try (PrintWriter out = new PrintWriter(outputFileName)) {
				out.print(answer);
			}
		} else {
			System.out.print(answer);
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
			String playerTurn, ArrayList<Tuple<Integer, Integer>> priorityPositions) {
		int lenghtOfBoard = boardXYValues.size();
		ArrayList<Tuple<Integer, Integer>> nextValidMoves = new ArrayList<>();
		ArrayList<Tuple<Integer, Integer>> nextValidRaidMoves = new ArrayList<>();
		for (int row = 0; row < lenghtOfBoard; row++) {
			for (int clm = 0; clm < lenghtOfBoard; clm++) {
				if (boardXYValues.get(row).get(clm).equals(".")) {
					if (!isRaidMove(boardXYValues, row, clm, playerTurn)) {
						nextValidMoves.add(new Tuple<Integer, Integer>(row, clm));
					} else {
						nextValidRaidMoves.add(new Tuple<Integer, Integer>(row, clm));
					}
				}
			}
		}
		nextValidRaidMoves.addAll(nextValidMoves);

		if (null == priorityPositions || priorityPositions.isEmpty()) {
			return nextValidRaidMoves;
		} else {
			for (Tuple<Integer, Integer> eachNextValidMove : nextValidRaidMoves) {
				if (!priorityPositions.contains(eachNextValidMove)) {
					priorityPositions.add(eachNextValidMove);
				}
			}
			return priorityPositions;
		}

	}

	ArrayList<ArrayList<String>> takeNextAction(ArrayList<ArrayList<String>> boardXYValues, String currentPlayer,
			Tuple<Integer, Integer> actionLocation) {
		ArrayList<ArrayList<String>> newBoardValue = deepCopy(boardXYValues);
		int xLocation = actionLocation.x;
		int yLocation = actionLocation.y;
		newBoardValue.get(xLocation).set(yLocation, currentPlayer);
		raidValueOccuipedData newBoardValueDetails = raidOccupyingNeighbours(newBoardValue, xLocation, yLocation,
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

	float heuristicCalculator(ArrayList<ArrayList<Integer>> juiceValues, ArrayList<ArrayList<String>> boardXYValues,
			String maxPlayer) {
		float heuristicValueOfX = (float) 0;
		float heuristicValueOfO = (float) 0;

		int lenghtOfXYBoard = boardXYValues.size();
		for (int row = 0; row < lenghtOfXYBoard; row++) {
			for (int clm = 0; clm < lenghtOfXYBoard; clm++) {
				String value = boardXYValues.get(row).get(clm);
				if (value.equals("X")) {
					heuristicValueOfX = (heuristicValueOfX + (float) juiceValues.get(row).get(clm));
				} else if (value.equals("O")) {
					heuristicValueOfO = (heuristicValueOfO + (float) juiceValues.get(row).get(clm));
				}
			}
		}

		if (maxPlayer.equals("X")) {
			return (heuristicValueOfX - heuristicValueOfO);
		} else {
			return (heuristicValueOfO - heuristicValueOfX);
		}
	}

	/* Different Heuristics */
	boolean isALLNeighboursMine(ArrayList<ArrayList<String>> boardXYValues, int currentRow, int currentColoumn,
			String playerTurn) {
		int lenghtOfBoard = boardXYValues.size();

		if (!boardXYValues.get(currentRow).get(currentColoumn).equals(playerTurn)) {
			return false;
		}

		if (currentRow - 1 >= 0) {
			if (!boardXYValues.get(currentRow - 1).get(currentColoumn).equals(playerTurn)) {
				return false;
			}
		}
		if (currentRow + 1 < lenghtOfBoard) {
			if (!boardXYValues.get(currentRow + 1).get(currentColoumn).equals(playerTurn)) {
				return false;
			}
		}
		if (currentColoumn - 1 >= 0) {
			if (!boardXYValues.get(currentRow).get(currentColoumn - 1).equals(playerTurn)) {
				return false;
			}
		}
		if (currentColoumn + 1 < lenghtOfBoard) {
			if (!boardXYValues.get(currentRow).get(currentColoumn + 1).equals(playerTurn)) {
				return false;
			}
		}
		return true;
	}

	float terminalHeuristicCalculatorRelative(ArrayList<ArrayList<Integer>> juiceValues,
			ArrayList<ArrayList<String>> boardXYValues, String maxPlayer) {
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
		if((heuristicValueOfX + heuristicValueOfO)==0){
			return (float)0;
		}
		if (maxPlayer.equals("X")) {
			return ((heuristicValueOfX - heuristicValueOfO) / (float) (heuristicValueOfX + heuristicValueOfO));
		} else {
			return ((heuristicValueOfO - heuristicValueOfX) / (float) (heuristicValueOfX + heuristicValueOfO));
		}
	}

	float differenceOfRaidOppositeCanPlay(ArrayList<ArrayList<Integer>> juiceValues,
			ArrayList<ArrayList<String>> boardXYValues, String maxPlayer) {
		int heuristicValueOfX = 0;
		int heuristicValueOfO = 0;
		int lenghtOfBoard = juiceValues.size();
		for (int row = 0; row < lenghtOfBoard; row++) {
			for (int clm = 0; clm < lenghtOfBoard; clm++) {
				if (boardXYValues.get(row).get(clm).equals(".")) {
					if (isRaidMove(boardXYValues, row, clm, "X")) {
						heuristicValueOfX++;
					}
					if (isRaidMove(boardXYValues, row, clm, "O")) {
						heuristicValueOfO++;
					}
				}
			}
		}
		if((heuristicValueOfX + heuristicValueOfO)==0){
			return (float)0;
		}
		if (maxPlayer.equals("X")) {
			return ((heuristicValueOfX) / (float) (heuristicValueOfX + heuristicValueOfO));
		} else {
			return ((heuristicValueOfO) / (float) (heuristicValueOfX + heuristicValueOfO));
		}
	}

	float differenceOfOccupiedScore(ArrayList<ArrayList<Integer>> juiceValues,
			ArrayList<ArrayList<String>> boardXYValues, String maxPlayer) {
		int heuristicValueOfX = 0;
		int heuristicValueOfO = 0;
		int lenghtOfBoard = juiceValues.size();
		for (int row = 0; row < lenghtOfBoard; row++) {
			for (int clm = 0; clm < lenghtOfBoard; clm++) {
				if (isALLNeighboursMine(boardXYValues, row, clm, "X")) {
					heuristicValueOfX = heuristicValueOfX + juiceValues.get(row).get(clm);
				} else if (isALLNeighboursMine(boardXYValues, row, clm, "O")) {
					heuristicValueOfO = heuristicValueOfO + juiceValues.get(row).get(clm);
				}
			}
		}
		if((heuristicValueOfX + heuristicValueOfO)==0){
			return (float)0;
		}
		if (maxPlayer.equals("X")) {
			return ((heuristicValueOfX - heuristicValueOfO) / (float) (heuristicValueOfX + heuristicValueOfO));
		} else {
			return ((heuristicValueOfO - heuristicValueOfX) / (float) (heuristicValueOfX + heuristicValueOfO));
		}
	}

	float intermediateHeuristicCalculator(ArrayList<ArrayList<Integer>> juiceValues,
			ArrayList<ArrayList<String>> boardXYValues, String maxPlayer) {
		float teminalHeuRel = terminalHeuristicCalculatorRelative(juiceValues, boardXYValues, maxPlayer);
		float diffRaidOppPlay = differenceOfRaidOppositeCanPlay(juiceValues, boardXYValues, maxPlayer);
		float diffOccScore = differenceOfOccupiedScore(juiceValues, boardXYValues, maxPlayer);
		float answerToReturn = ha * teminalHeuRel + hb * diffRaidOppPlay + hc * diffOccScore;
		return answerToReturn;
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

	public String stateToString(ArrayList<ArrayList<String>> boardXYValues) {
		StringBuffer toString = new StringBuffer();
		int lenghtOfBoard = boardXYValues.size();

		for (int row = 0; row < lenghtOfBoard; row++) {
			for (int clm = 0; clm < lenghtOfBoard; clm++) {
				toString.append(boardXYValues.get(row).get(clm));
			}
		}
		return toString.toString();
	}

	public ArrayList<Tuple<Integer, Integer>> sortedHashMapValues(
			HashMap<Float, ArrayList<Tuple<Integer, Integer>>> valuesAndPositions, boolean asc) {
		ArrayList<Tuple<Integer, Integer>> listOfOrderedActions = new ArrayList<>();
		ArrayList<Float> keysInOrderOfExploration = new ArrayList<Float>(valuesAndPositions.keySet());
		if (!asc) {
			Collections.sort(keysInOrderOfExploration, Collections.reverseOrder());
		} else {
			Collections.sort(keysInOrderOfExploration);
		}
		for (Float eachKey : keysInOrderOfExploration) {
			ArrayList<Tuple<Integer, Integer>> allPositionsForThisValue = valuesAndPositions.get(eachKey);
			listOfOrderedActions.addAll(allPositionsForThisValue);
		}
		return listOfOrderedActions;
	}

	answerGiver alphaBeta(ArrayList<ArrayList<Integer>> juiceValues, ArrayList<ArrayList<String>> boardXYValues,
			int depthLimit, String maxPlayerTurn, Cache cacheOfOrder) {
		int currentDepth = 1;
		String playerTurn = maxPlayerTurn;
		float alphaValue = -Float.MAX_VALUE;
		float betaValue = Float.MAX_VALUE;
		ArrayList<ArrayList<String>> bestMove = deepCopy(boardXYValues);
		Tuple<Integer, Integer> xyCoordinatesOfAction = new Tuple<Integer, Integer>(0, 0);
		HashMap<Float, ArrayList<Tuple<Integer, Integer>>> valuesAndPositions = new HashMap<>();
		// check my valid next actions prioritized ...
		ArrayList<Tuple<Integer, Integer>> positions = cacheOfOrder.positions;

		ArrayList<Tuple<Integer, Integer>> nextValidMoves = nextPossibleActions(boardXYValues, maxPlayerTurn,
				positions);

		for (Tuple<Integer, Integer> eachNextValidAction : nextValidMoves) {
			ArrayList<ArrayList<String>> newBoardValue = takeNextAction(boardXYValues, playerTurn, eachNextValidAction);
			// cache of new Board value if present in child ...
			String newBoardValueStringFormed = stateToString(newBoardValue);
			Cache cachePassToChild = new Cache();
			if (cacheOfOrder.childStates.containsKey(newBoardValueStringFormed)) {
				cachePassToChild = cacheOfOrder.childStates.get(newBoardValueStringFormed);
			}

			answerGiver maxStateInTree = minAlphaBeta(juiceValues, newBoardValue, maxPlayerTurn, depthLimit,
					currentDepth + 1, oppositePlayer(playerTurn), alphaValue, betaValue, cachePassToChild);

			float currentAlphaValue = maxStateInTree.maxValueObtained;

			// store current child value in hash map and prioritized positions
			// later.
			if (!valuesAndPositions.containsKey(currentAlphaValue)) {
				valuesAndPositions.put(currentAlphaValue, new ArrayList<>());
			}
			valuesAndPositions.get(currentAlphaValue).add(eachNextValidAction);

			// update child cache
			cacheOfOrder.childStates.put(newBoardValueStringFormed, maxStateInTree.cacheNewToReturn);

			if (currentAlphaValue >= betaValue) {
				// sort hashmap values before giving //in descending here
				ArrayList<Tuple<Integer, Integer>> sortedPositions = sortedHashMapValues(valuesAndPositions, false);
				cacheOfOrder.positions = sortedPositions;
				HashMap<String, Cache> newHashMap = new HashMap<>();
				newHashMap.put(stateToString(boardXYValues), cacheOfOrder);
				return (new answerGiver(deepCopy(newBoardValue), eachNextValidAction, newHashMap));
			}
			if (currentAlphaValue > alphaValue) {
				alphaValue = currentAlphaValue;
				bestMove = deepCopy(newBoardValue);
				xyCoordinatesOfAction = eachNextValidAction;
			}
		}
		// sort hashmap values before giving //in descending here
		ArrayList<Tuple<Integer, Integer>> sortedPositions = sortedHashMapValues(valuesAndPositions, false);
		cacheOfOrder.positions = sortedPositions;
		HashMap<String, Cache> newHashMap = new HashMap<>();
		newHashMap.put(stateToString(boardXYValues), cacheOfOrder);
		return (new answerGiver(bestMove, xyCoordinatesOfAction, newHashMap));
	}

	answerGiver maxAlphaBeta(ArrayList<ArrayList<Integer>> juiceValues, ArrayList<ArrayList<String>> boardXYValues,
			String maxPlayer, int depthLimit, int currentDepth, String playerTurn, float alphaValue, float betaValue,
			Cache cacheOfOrder) {

		HashMap<Float, ArrayList<Tuple<Integer, Integer>>> valuesAndPositions = new HashMap<>();

		if (isTerminalState(boardXYValues)) {
			return (new answerGiver(heuristicCalculator(juiceValues, boardXYValues, maxPlayer), boardXYValues, null,
					new Cache()));
		}

		if (depthLimit < currentDepth) {
			return (new answerGiver(intermediateHeuristicCalculator(juiceValues, boardXYValues, maxPlayer),
					boardXYValues, null, new Cache()));
		}

		// ArrayList<ArrayList<String>> bestMove = deepCopy(boardXYValues);
		// check my valid next actions prioritized ...
		ArrayList<Tuple<Integer, Integer>> positions = cacheOfOrder.positions;

		ArrayList<Tuple<Integer, Integer>> nextValidMoves = nextPossibleActions(boardXYValues, playerTurn, positions);

		for (Tuple<Integer, Integer> eachNextValidAction : nextValidMoves) {
			ArrayList<ArrayList<String>> newBoardValue = takeNextAction(boardXYValues, playerTurn, eachNextValidAction);
			// cache of new Board value if present in child ...
			String newBoardValueStringFormed = stateToString(newBoardValue);
			Cache cachePassToChild = new Cache();
			if (cacheOfOrder.childStates.containsKey(newBoardValueStringFormed)) {
				cachePassToChild = cacheOfOrder.childStates.get(newBoardValueStringFormed);
			}

			answerGiver maxStateInTree = minAlphaBeta(juiceValues, newBoardValue, maxPlayer, depthLimit,
					currentDepth + 1, oppositePlayer(playerTurn), alphaValue, betaValue, cachePassToChild);

			float currentAlphaValue = maxStateInTree.maxValueObtained;

			// store current child value in hash map and prioritized positions
			// later.
			if (!valuesAndPositions.containsKey(currentAlphaValue)) {
				valuesAndPositions.put(currentAlphaValue, new ArrayList<>());
			}
			valuesAndPositions.get(currentAlphaValue).add(eachNextValidAction);

			// update child cache
			cacheOfOrder.childStates.put(newBoardValueStringFormed, maxStateInTree.cacheNewToReturn);

			if (currentAlphaValue >= betaValue) {
				// sort hashmap values before giving //in descending here
				ArrayList<Tuple<Integer, Integer>> sortedPositions = sortedHashMapValues(valuesAndPositions, false);
				cacheOfOrder.positions = sortedPositions;
				return (new answerGiver(currentAlphaValue, null, null, cacheOfOrder));
			}
			if (currentAlphaValue > alphaValue) {
				alphaValue = currentAlphaValue;
			}
		}

		// sort hashmap values before giving //in descending here
		ArrayList<Tuple<Integer, Integer>> sortedPositions = sortedHashMapValues(valuesAndPositions, false);
		cacheOfOrder.positions = sortedPositions;
		return (new answerGiver(alphaValue, null, null, cacheOfOrder));
	}

	answerGiver minAlphaBeta(ArrayList<ArrayList<Integer>> juiceValues, ArrayList<ArrayList<String>> boardXYValues,
			String maxPlayer, int depthLimit, int currentDepth, String playerTurn, float alphaValue, float betaValue,
			Cache cacheOfOrder) {

		HashMap<Float, ArrayList<Tuple<Integer, Integer>>> valuesAndPositions = new HashMap<>();

		if (isTerminalState(boardXYValues)) {
			return (new answerGiver(heuristicCalculator(juiceValues, boardXYValues, maxPlayer), boardXYValues, null,
					new Cache()));
		}

		if (depthLimit < currentDepth) {
			return (new answerGiver(intermediateHeuristicCalculator(juiceValues, boardXYValues, maxPlayer),
					boardXYValues, null, new Cache()));
		}

		// ArrayList<ArrayList<String>> bestMove = deepCopy(boardXYValues);
		// check my valid next actions prioritized ...
		ArrayList<Tuple<Integer, Integer>> positions = cacheOfOrder.positions;

		ArrayList<Tuple<Integer, Integer>> nextValidMoves = nextPossibleActions(boardXYValues, playerTurn, positions);

		for (Tuple<Integer, Integer> eachNextValidAction : nextValidMoves) {
			ArrayList<ArrayList<String>> newBoardValue = takeNextAction(boardXYValues, playerTurn, eachNextValidAction);
			// cache of new Board value if present in child ...
			String newBoardValueStringFormed = stateToString(newBoardValue);
			Cache cachePassToChild = new Cache();
			if (cacheOfOrder.childStates.containsKey(newBoardValueStringFormed)) {
				cachePassToChild = cacheOfOrder.childStates.get(newBoardValueStringFormed);
			}

			answerGiver maxStateInTree = maxAlphaBeta(juiceValues, newBoardValue, maxPlayer, depthLimit,
					currentDepth + 1, oppositePlayer(playerTurn), alphaValue, betaValue, cachePassToChild);

			float currentBetaValue = maxStateInTree.maxValueObtained;

			// store current child value in hash map and prioritized positions
			// later.
			if (!valuesAndPositions.containsKey(currentBetaValue)) {
				valuesAndPositions.put(currentBetaValue, new ArrayList<>());
			}
			valuesAndPositions.get(currentBetaValue).add(eachNextValidAction);

			// update child cache
			cacheOfOrder.childStates.put(newBoardValueStringFormed, maxStateInTree.cacheNewToReturn);

			if (currentBetaValue <= alphaValue) {
				// sort hashmap values before giving //in ascending here
				ArrayList<Tuple<Integer, Integer>> sortedPositions = sortedHashMapValues(valuesAndPositions, true);
				cacheOfOrder.positions = sortedPositions;
				return (new answerGiver(currentBetaValue, null, null, cacheOfOrder));
			}
			if (currentBetaValue < betaValue) {
				betaValue = currentBetaValue;
			}
		}
		// sort hashmap values before giving //in descending here
		ArrayList<Tuple<Integer, Integer>> sortedPositions = sortedHashMapValues(valuesAndPositions, true);
		cacheOfOrder.positions = sortedPositions;
		return (new answerGiver(betaValue, null, null, cacheOfOrder));
	}

	float calculateTimeForThisMove(float cpuTime) {
		double d = 0.30;
		float percentageOfTimeGivenForThisMoves = (float) d;
		return percentageOfTimeGivenForThisMoves * cpuTime;
	}

	answerGiver iterativeDeepning(float timeForThisMove, ArrayList<ArrayList<Integer>> juiceValues,
			ArrayList<ArrayList<String>> boardXYValues, int remainingEmptyValues, String youPlay,
			BigDecimal avgTimeForOneNodeToExploreBigDec, Cache cache) {
		int depthToBeExplored = 1;
		int branchingFactor = remainingEmptyValues;
		BigDecimal branchingFactorBigInt = new BigDecimal(String.valueOf(branchingFactor));
		float timeRemaining = timeForThisMove;
		answerGiver currentBestMove = null;
		try {

			while (true) {
				long startTime = System.currentTimeMillis();
				String boardXYInStringFormat = stateToString(boardXYValues);
				if (cache.childStates.containsKey(boardXYInStringFormat)) {
					// check for string present in hash map and board position
					// same
					cache = cache.childStates.get(boardXYInStringFormat);
				}

				BigDecimal totalNumberOfNodesToExplore = branchingFactorBigInt.pow(depthToBeExplored / 2);
				BigDecimal estimatedTimeForNextRound = avgTimeForOneNodeToExploreBigDec
						.multiply(totalNumberOfNodesToExplore);
				BigDecimal timeRemainingBigDecimal = new BigDecimal(timeRemaining);

				if ((estimatedTimeForNextRound.compareTo(timeRemainingBigDecimal) == -1)
						&& (remainingEmptyValues >= depthToBeExplored)) {
					currentBestMove = alphaBeta(juiceValues, boardXYValues, depthToBeExplored, youPlay, cache);
					HashMap<String, Cache> hashMapOfCache = currentBestMove.cacheOfChilds;
					cache = new Cache(new ArrayList<>(), hashMapOfCache);
				} else {
					return currentBestMove;
				}
				depthToBeExplored = depthToBeExplored + 1;
				long stopTime = System.currentTimeMillis();
				float elapsedTime = (float) ((stopTime - startTime) / 1000.0);
				timeRemaining = timeRemaining - elapsedTime;
				BigDecimal elapsedTimeBigDec = new BigDecimal(String.valueOf(elapsedTime));
				avgTimeForOneNodeToExploreBigDec = elapsedTimeBigDec.divide(totalNumberOfNodesToExplore,
						RoundingMode.CEILING);
			}
		} catch (Error e) {
			currentBestMove.cacheOfChilds = new HashMap<>();
			return currentBestMove;
		}
	}

	Cache readCache(String nameOfCache) {
		Cache cache = new Cache();
		try {
			FileInputStream fileIn = new FileInputStream(nameOfCache);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			cache = (Cache) in.readObject();
			in.close();
			fileIn.close();
		} catch (Exception e) {
			// fail silently
			cache = new Cache();
		}
		if (null == cache) {
			cache = new Cache();
		}
		return cache;
	}

	void writeCache(Cache cache, String nameOfCache) {
		try {
			FileOutputStream fileOut = new FileOutputStream(nameOfCache);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(cache);
			out.close();
			fileOut.close();
		} catch (Exception e) {
			// fail silently
		}
	}

	Tuple<Integer, Integer> nextBestPossibleGoodState(ArrayList<ArrayList<String>> boardXYValues,
			ArrayList<ArrayList<Integer>> juiceValues) {
		int maxValue = Integer.MIN_VALUE;
		int lenghtOfBoard = boardXYValues.size();
		Tuple<Integer, Integer> nextValidMove = new Tuple<Integer, Integer>(0, 0);
		for (int row = 0; row < lenghtOfBoard; row++) {
			for (int clm = 0; clm < lenghtOfBoard; clm++) {
				if (boardXYValues.get(row).get(clm).equals('.') && maxValue < juiceValues.get(row).get(clm)) {
					maxValue = juiceValues.get(row).get(clm);
					nextValidMove = new Tuple<Integer, Integer>(row, clm);
				}
			}
		}
		return nextValidMove;
	}

	void fastStateCreatorAndWriter(ArrayList<ArrayList<String>> boardXYValues,
			ArrayList<ArrayList<Integer>> juiceValues, String youPlay) throws FileNotFoundException {
		ArrayList<ArrayList<String>> old = deepCopy(boardXYValues);
		Tuple<Integer, Integer> possibleAction = nextBestPossibleGoodState(boardXYValues, juiceValues);
		ArrayList<ArrayList<String>> newMergedBoard = takeNextAction(boardXYValues, youPlay, possibleAction);
		String answerInString = answerConverter(old, newMergedBoard, possibleAction);
		writeOutPutToFile(answerInString, "output.txt", false);
	}

	public static void main(String[] args) throws NumberFormatException, IOException {
		kabara kab = new kabara();
		InpuData id = kab.readInput("input.txt");
		float cpuTime = id.cpuTime;
		String youPlay = id.youPlay;
		ArrayList<ArrayList<String>> boardXYValues = kab.deepCopy(id.occupiedPositions);

		try {
			if (cpuTime < criticaState) {
				kab.fastStateCreatorAndWriter(boardXYValues, id.boardValues, youPlay);
			} else {
				long startTime = System.currentTimeMillis();
				Cache cacheReadFromFile = kab.readCache(cacheFilename);
				String newNodeBoardString = kab.stateToString(id.occupiedPositions);

				if (!cacheReadFromFile.childStates.isEmpty()) {
					HashMap<String, Cache> allPossibleChilds = cacheReadFromFile.childStates;
					if (allPossibleChilds.containsKey(newNodeBoardString)) {
						cacheReadFromFile = allPossibleChilds.get(newNodeBoardString);
					} else {
						cacheReadFromFile = new Cache();
					}
				}

				answerGiver answerByIterativeDeepning = kab.iterativeDeepning(kab.calculateTimeForThisMove(cpuTime),
						id.boardValues, id.occupiedPositions,
						kab.nextPossibleActions(id.occupiedPositions, youPlay, null).size(), youPlay,
						new BigDecimal("0.0001"), cacheReadFromFile);
				Tuple<Integer, Integer> positionsOfAction = answerByIterativeDeepning.xyCoordinatesOfAction;
				String answerInString = kab.answerConverter(id.occupiedPositions, answerByIterativeDeepning.bestMove,
						positionsOfAction);
				kab.writeOutPutToFile(answerInString, "output.txt", false);
				ArrayList<ArrayList<String>> newNodeBoardOfChild = answerByIterativeDeepning.bestMove;
				String newNodeBoardStringOfChild = kab.stateToString(newNodeBoardOfChild);
				
				if (!answerByIterativeDeepning.cacheOfChilds.isEmpty()) {
					cacheReadFromFile = answerByIterativeDeepning.cacheOfChilds.get(newNodeBoardString).childStates
							.get(newNodeBoardStringOfChild);
				} else {
					cacheReadFromFile = new Cache();
				}
				
				long stopTime = System.currentTimeMillis();
				float elapsedTime = (float) ((stopTime - startTime) / 1000.0);

				if ((cpuTime - elapsedTime) > criticaState) {
					kab.writeCache(cacheReadFromFile, "kabara.cache");
				}
			}
		} catch (Exception e) {
			kab.fastStateCreatorAndWriter(boardXYValues, id.boardValues, youPlay);
		} catch (Error e) {
			kab = new kabara();
			id = kab.readInput("input.txt");
			cpuTime = id.cpuTime;
			youPlay = id.youPlay;
			boardXYValues = kab.deepCopy(id.occupiedPositions);
			kab.fastStateCreatorAndWriter(boardXYValues, id.boardValues, youPlay);
		}
	}
}
