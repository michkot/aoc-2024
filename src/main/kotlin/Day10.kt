import kotlin.time.measureTime

//private val inputFile = "Day10_sample";
private val inputFile = "Day10_input";

@Suppress("DataClassPrivateConstructor")
private data class Coordinate private constructor(
	val vertical: Int,
	val horizontal: Int,
) {
	companion object {
		val verticalMin = 0;
		val horizontalMin = 0;
		var verticalMax = -1;
		var horizontalMax = -1

		fun createCoordinateOrNull(
			vertical: Int,
			horizontal: Int,
		): Coordinate? {
			if (vertical in verticalMin..verticalMax
				&& horizontal in horizontalMin..horizontalMax
			) {
				return Coordinate(vertical, horizontal)
			} else {
				return null;
			}
		}
	}

	fun tryMove(direction: Direction) = createCoordinateOrNull(
		this.vertical + direction.vertical,
		this.horizontal + direction.horizontal,
	)
}

private sealed class Direction(val vertical: Int, val horizontal: Int)

private data object Up : Direction(-1, 0)
private data object Down : Direction(1, 0)
private data object Left : Direction(0, 1)
private data object Right : Direction(0, -1)

private val directions = arrayOf(Up, Down, Left, Right);

private fun main() {
	measureTime {
		val lines = loadData(readInput(inputFile))

		fun getLevelAt(newLocation: Coordinate) =
			lines[newLocation.vertical][newLocation.horizontal]

		// TODO: optimize - remember already visited locations
		fun searchForPeaks(
			from: Coordinate,
			currentLevel: Int,
			reachablePeaks: HashSet<Coordinate>,
		) {
			for (dir in directions) {
				val newLocation = from.tryMove(dir);
				if (newLocation == null) continue;
				val newLevel = getLevelAt(newLocation);
				if (newLevel != currentLevel + 1) continue;
				if (newLevel == 9) {
					reachablePeaks.add(newLocation);
					continue;
				}
				searchForPeaks(newLocation, newLevel, reachablePeaks);
			}
		}

		var sumOfScores: Long = 0L;
		lines.forEachIndexed { ver, line ->
			line.forEachIndexed { hor, number ->
				if (isTrailHead(number)) {
					val location =
						Coordinate.createCoordinateOrNull(ver, hor)!!;
					val reachablePeaks = HashSet<Coordinate>();

					searchForPeaks(location, number, reachablePeaks)

					val score = reachablePeaks.size;
					sumOfScores += score;
				}
			}
		}

		sumOfScores.println();
		"end".println()
	}.println()
}

private fun loadData(
	inputLines: List<String>,
): Array<IntArray> {
	val inputMatrixLines = inputLines.map { line ->
		//			val lineAsNumbers = IntArray(line.length);
		//			line.forEachIndexed { idx, digit -> lineAsNumbers[idx] = digit.digitToInt() };
		line.map { digit -> digit.digitToInt() }.toIntArray();
	}.toTypedArray();

	Coordinate.verticalMax = inputMatrixLines.lastIndex;
	Coordinate.horizontalMax = inputMatrixLines[0].lastIndex;
	return inputMatrixLines
}

private fun isTrailHead(number: Int) = number == 0
