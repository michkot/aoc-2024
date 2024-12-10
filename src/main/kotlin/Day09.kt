import kotlin.time.measureTime

@JvmInline
value class DiskBlock private constructor(private val id: Int) {
	companion object {
		fun file(fileId: Int) = DiskBlock(fileId)
		val space = DiskBlock(-1)
	}

	fun fileIdOrZero() = if (id == -1) 0 else id
}

//val inputFile = "Day09_sample";
val inputFile = "Day09_input";

// state:
val hdd = ArrayList<DiskBlock>()

// create hard drive from input
fun loadHdd() {
	var currIdx = -1
	val input = readInput(inputFile)
		.first()
		.asSequence()
		.map { it.code - '0'.code }
	for (digit in input) {
		currIdx++
		val isFileElseSpace = currIdx and 0x1 == 0x0
		if (isFileElseSpace) {
			val currFileIdx = currIdx shr 1
			repeat(digit) { hdd.add(DiskBlock.file(currFileIdx)) }
		} else {
			repeat(digit) { hdd.add(DiskBlock.space) }
		}
	}
}

fun moveBlocks() {
	val maxBlockId = hdd.lastIndex
	var leftmostFreeBlockId = hdd.indexOf(DiskBlock.space)
	fun findNewLeftmostSpace() {
		leftmostFreeBlockId++
		leftmostFreeBlockId += hdd
			.asSequence()
			.drop(leftmostFreeBlockId)
			.indexOf(DiskBlock.space)
	}

	var currIdx = maxBlockId + 1
	while (leftmostFreeBlockId < --currIdx) {
		if (hdd[currIdx] == DiskBlock.space) continue

		hdd[leftmostFreeBlockId] = hdd[currIdx]
		hdd[currIdx] = DiskBlock.space
		findNewLeftmostSpace()
	}
}

fun main() {

	measureTime {
		loadHdd()
		hdd.trimToSize()

		moveBlocks()

		val checksum =
			hdd.foldIndexed(0) { index, acc, diskBlock -> acc + (index * diskBlock.fileIdOrZero()) }

		checksum.println();
		"end".println()

	}.println()

}
