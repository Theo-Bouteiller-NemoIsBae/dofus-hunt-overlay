package shared.draw.square

import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import javafx.scene.shape.Line

class Square(
    private var x: Double,
    private var y: Double,
    private var width: Double,
    private var height: Double,
    private val id: String
) {

    private val topLine: Line = Line()
    private val leftLine: Line = Line()
    private val rightLine: Line = Line()
    private val bottomLine: Line = Line()

    private val square: ArrayList<Line> = arrayListOf()

    init {
        sizeLine()
        addLineToSquare()
    }

    fun move(x: Double = this.x, y: Double = this.y) {
        this.x = x
        this.y = y

        sizeLine()
    }

    private fun sizeLine() {
        topLine.startX = x
        topLine.endX = x + width
        topLine.startY = y
        topLine.endY = y
        topLine.id = "${id}_topLine"

        leftLine.startX = x
        leftLine.endX = x
        leftLine.startY = y
        leftLine.endY = y + height
        leftLine.id = "${id}_leftLine"

        bottomLine.startX = x
        bottomLine.endX = x + width
        bottomLine.startY = y + height
        bottomLine.endY = y + height
        bottomLine.id = "${id}_bottomLine"

        rightLine.startX = x + width
        rightLine.endX = x + width
        rightLine.startY = y
        rightLine.endY = y + height
        rightLine.id = "${id}_rightLine"

    }

    private fun addLineToSquare() {
        square.clear()
        square.add(leftLine)
        square.add(topLine)
        square.add(rightLine)
        square.add(bottomLine)
    }

    fun getSquare(): ArrayList<Line> {
        return square
    }
}