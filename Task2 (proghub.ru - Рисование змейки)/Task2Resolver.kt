/*
proghub.ru

Нарисовать змейку

Напишите функцию, которая принимает число n и выводит таблицу размером n * n,.
заполненную числами от 1 до n^2 по спирали, выходящей из левого верхнего угла и закрученной по часовой стрелке.

Пример входных параметров:
n = 5

Ожидается на выходе:
1  2  3  4  5
16 17 18 19 6
15 24 25 20 7
14 23 22 21 8
13 12 11 10 9

Сложность: Middle
*/

/** Custom exception for exit from matrix filling procedure */
class MatrixIsFullException(message: String) : Exception(message)

/** Current matrix cell coordinates */
object CellCoords {
    var y: Int = 0
    var x: Int = 0
}

/** Current matrix ranges (changes iteratively) */
object MatrixLimits {
    var yStart: Int = 0
    var xStart: Int = 0
    var yEnd: Int = 0
    var xEnd: Int = 0
}

/** Create, fill and return the square matrix of defined size */
fun drawSnake(_size: Int): Array<Array<Int>> {
    var autoIncValue = 1 //incrementing filling value
    val matrix = Array(_size) { Array(_size) { 0 } }
    var isMatrixFull: Boolean // flag of job completion

    /**
     * fill one line / column of the matrix
     * @param coords - position of starting cell for the line / column
     * @param limits - current values of the matrix limits
     * @param updateCoords - lambda fun for coords modification
     * @param checkLimits - lambda fun for check whether current coords in limits or not
     * @param updateLimits - lambda fun for limits modification
     */
    fun fillLineOrColumn(
        coords: CellCoords,
        limits: MatrixLimits,
        updateCoords: (CellCoords) -> Unit,
        checkLimits: (CellCoords, MatrixLimits) -> Boolean,
        updateLimits: (MatrixLimits) -> Unit
    ) {
        isMatrixFull = true

        updateLimits(limits)

        /** set values while coords in correct ranges */
        while (true) {
            if (!checkLimits(coords, limits)) {
                break
            }
            isMatrixFull = false // we still have unfilled cell
            updateCoords(coords)
            matrix[coords.y][coords.x] = autoIncValue++
        } //while

        if (isMatrixFull) // none of cell has been filled in this line / column => matrix is full
            throw MatrixIsFullException("All is done")

//        println("Coords: (${coords.y}, ${coords.x})")
//        println("Limits: (${limits.yStart}, ${limits.xStart} - ${limits.yEnd}, ${limits.xEnd})")

    } //fun fillLineOrColumn()

    CellCoords.x = -1 // prestart values for the 1'st cell
    CellCoords.y = 0

    MatrixLimits.xStart = 0
    MatrixLimits.yStart = 0
    MatrixLimits.xEnd = _size-1
    MatrixLimits.yEnd = _size-1

    try {
        while (true) {

            /** set upper line of the matrix */
            fillLineOrColumn(
                CellCoords,
                MatrixLimits,
                updateCoords = { NewCoords -> NewCoords.x++ }, // [0][0] => [0][1] => [0][2] => [0][3]
                checkLimits = { NewCoords, NewLimits -> (NewCoords.x + 1) <= NewLimits.xEnd },
                updateLimits = { NewLimits -> NewLimits.yStart++ } // upper line: 0 => 1 => 2...
            )

            /** set right column */
            fillLineOrColumn(
                CellCoords,
                MatrixLimits,
                updateCoords = { NewCoords -> NewCoords.y++ }, // [1][3] => [2][3] => [3][3]
                checkLimits = { NewCoords, NewLimits -> (NewCoords.y + 1) <= NewLimits.yEnd },
                updateLimits = { NewLimits -> NewLimits.xEnd-- } // right column: 3 => 2 => 1...
            )

            /** set lower line */
            fillLineOrColumn(
                CellCoords,
                MatrixLimits,
                updateCoords = { NewCoords -> NewCoords.x-- }, // [3][2] => [3][1] => [3][0]
                checkLimits = { NewCoords, NewLimits -> (NewCoords.x - 1) >= NewLimits.xStart },
                updateLimits = { NewLimits -> NewLimits.yEnd-- } // lower line: 3 => 2 => 1...
            )

            /** set left column */
            fillLineOrColumn(
                CellCoords,
                MatrixLimits,
                updateCoords = { NewCoords -> NewCoords.y-- }, // [2][0] => [1][0]
                checkLimits = { NewCoords, NewLimits -> (NewCoords.y - 1) >= NewLimits.yStart },
                updateLimits = { NewLimits -> NewLimits.xStart++ } // left column: 0 => 1 => 2...
            )

        } //repeat until matrix is full

    } catch (e: MatrixIsFullException) {
        println("\nWe've finished!")
    }
    return matrix
}


fun main(args: Array<String>) {
    val matrixSize = 9
    println("==========================")
    println("Gonna draw snake in matrix [$matrixSize x $matrixSize]..")
    var matrix = drawSnake(matrixSize)

    println("==========================")
    println("==========================")
    println("Got snake [$matrixSize x $matrixSize]:")
    var formattedCellValue:String
    for (i in 0 until matrixSize) {
        for (j in 0 until matrixSize) {
            formattedCellValue = String.format("%3d  ", matrix[i][j]) ;
            print("$formattedCellValue")
        }
        println()
    }
}