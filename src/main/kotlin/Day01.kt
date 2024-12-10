import kotlin.math.abs
import kotlin.time.measureTime

//private val inputFile = "Day01_sample";
private val inputFile = "Day01_input";

private fun main() = measureTime {
//	part2()
	part2()
	"end".println()
}.println()

private fun part1() {
	val (leftNumbersSorted, rightNumbersSorted) = loadAndSortData()

	var totalDistance: Long = 0;
	for (i in 0..leftNumbersSorted.lastIndex) {
		val distance = abs(leftNumbersSorted[i] - rightNumbersSorted[i])
		totalDistance += distance
	}
	totalDistance.println()
}

private fun part2() {
	val (leftNumbersSorted, rightNumbersSorted) = loadAndSortData()

	val rightCountsCache = HashMap<Int, Int>();
	fun getRightCount(number: Int): Int {
		return rightCountsCache.getOrPut(number) { rightNumbersSorted.count { it == number } }
	}

	var totalSimScore: Long = 0;
	for (i in 0..leftNumbersSorted.lastIndex) {
		val simScore = leftNumbersSorted[i] * getRightCount(leftNumbersSorted[i])
		totalSimScore += simScore
	}
	totalSimScore.println()
}

private fun loadAndSortData(): kotlin.Pair<List<Int>, List<Int>> {
	val preParsed = readInput(inputFile).map { textLine ->
		val (left, right) = textLine.split("   ").map { it.toInt() }
		Pair(left, right)
	};
	val leftNumbers = preParsed.map { (left, _) -> left }
	val rightNumbers = preParsed.map { (_, right) -> right }

	val leftNumbersSorted = leftNumbers.sorted()
	val rightNumbersSorted = rightNumbers.sorted()
	return Pair(leftNumbersSorted, rightNumbersSorted)
}
