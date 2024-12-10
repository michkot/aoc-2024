import kotlin.time.measureTime

//@JvmInline
/*value*/ class DiskBlock private constructor(
	public val id: Int,
	public val size: Byte,
) {
	companion object {
		fun file(fileId: Int, size: Byte) = DiskBlock(fileId, size)
		val space = DiskBlock(-1, 1)
	}

	fun fileIdOrZero() = if (id == -1) 0 else id
}

//val inputFile = "Day09_sample";
val inputFile = "Day09_input";

// state:
var hdd: Array<DiskBlock> = Array<DiskBlock>(0) { _ -> DiskBlock.space };

// create hard drive from input
fun loadHdd() {
	var inputIdx = -1
	val inputStr = readInput(inputFile)
		.first();

	var blockIdx = 0;
	hdd = Array<DiskBlock>(/*maxsize*/inputStr.length * 9) { _ ->
		DiskBlock.space
	};

	val input = inputStr.asSequence()
		.map { it.code - '0'.code }
	for (digit in input) {
		inputIdx++
		val isFileElseSpace = inputIdx and 0x1 == 0x0
		if (isFileElseSpace) {
			val fileIdx = inputIdx shr 1
			val fileBlock = DiskBlock.file(fileIdx, digit.toByte());
			repeat(digit) {
				hdd[blockIdx++] = (fileBlock)
			}
		} else {
			repeat(digit) { hdd[blockIdx++] = (DiskBlock.space) }
		}
	}
	hdd = hdd.copyOf(blockIdx) as Array<DiskBlock>;
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

		moveBlocks()

		val checksum: Long =
			hdd.foldIndexed(0L) { index, acc, diskBlock ->
				acc + (index *
					diskBlock.fileIdOrZero())
			}

		checksum.println();
		"end".println()

	}.println()

}

// wrong: too small: 23996653
// correct... use long :facepaln:, 6386640365805
