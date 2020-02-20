package shared.dragresizemod

import javafx.scene.Node

interface OnDragResizeEventListener {
    fun onDrag (node: Node?, x: Double, y: Double, h: Double, w: Double)

    fun onResize (node: Node?, x: Double, y: Double, h: Double, w: Double)
}