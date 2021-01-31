/*
proghub.ru

Равнины и впадины

Дан двумерный массив (матрица) содержащий только 0 и 1.
Матрица представляет собой землю, где 0 - впадина, 1 - равнина.
Впадины могут быть разные по размерам, организовываются они путем соединения соседних
ячеек сверху или снизу, не по диагонали.

Необходимо написать функцию которая вернет размеры всех впадин в порядке возрастания.

Сложность: Middle
*/


fun countHoles(cells: Array<Array<Int>>): MutableList<Int> {

    var holes = mutableListOf<Int>()
    val sizeX = cells[0].size
    val sizeY = cells.size


    /*
     * Recursive function to locate & to measure the hole
     *
     * @params [y][x] - starting coordinates
     * @returns size of the hole for current recursion level
     */
    fun getHole(x: Int, y: Int): Int {
        var sizeOfTheHole = 1

        /*
         * high order function to check (via lambda), whether coordinates in range or not
         * then, call getHole() again if the cell is in a hollow
         */
        fun checkNCall(
            newX: Int,
            newY: Int,
            checkLimits: (x: Int, y: Int) -> Boolean
        ) {
            if (checkLimits(newX, newY)) {
                if (cells[newY][newX] == 0) {
                    sizeOfTheHole += getHole(newX, newY)
                }
            }
        }

        println("sells[$y][$x]: 0=>1")
        cells[y][x] = 1 // mark cell as 'located' already

        checkNCall(x - 1, y) { x, _ ->
            x >= 0
        }
//        if ((x - 1) >= 0) {
//            if (cells[y][x - 1] == 0) {
//                sizeOfTheHole += getHole(x - 1, y)
//            }
//        }

        checkNCall(x + 1, y) { x, _ ->
            x < sizeX
        }

        checkNCall(x, y + 1) { _, y ->
            y < sizeY
        }

        return sizeOfTheHole
    }

    /* loop through the array */
    for (i in 0 until sizeY) {
        for (j in 0 until sizeX) {
            if (cells[i][j] != 0) // the cell is in a plain => skip it
                continue

            println("gonna find new hole from sells[$i][$j]...")
            var newHole = getHole(j, i)
            println("got new hole from sells[$i][$j] of size $newHole")
            println("-------")
            holes.add(newHole)
        }
    }

    return holes
}

fun main(args: Array<String>) {
    val cells = Array(5, { Array(6, { 0 }) })
    cells[0] = arrayOf(1, 0, 0, 1, 0, 1)
    cells[1] = arrayOf(1, 0, 1, 0, 1, 1)
    cells[2] = arrayOf(1, 1, 0, 0, 0, 1)
    cells[3] = arrayOf(0, 1, 1, 1, 0, 1)
    cells[4] = arrayOf(0, 1, 0, 0, 0, 1)

//     val cells = Array(3, { Array(6, { 0 }) })
//     cells[0] = arrayOf(1, 1, 0, 1, 1, 0)
//     cells[1] = arrayOf(1, 0, 0, 0, 1, 0)
//     cells[2] = arrayOf(1, 0, 0, 1, 1, 0)


    var holes = countHoles(cells)
    holes.sort()
    println("========================")
    println("========================")
    println("========================")
    holes.forEachIndexed { idx, elem ->
        println("hole[$idx]: $elem")
    }
}