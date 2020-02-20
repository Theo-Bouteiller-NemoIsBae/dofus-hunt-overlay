package shared.dragresizemod

import javafx.event.EventHandler
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent
import javafx.scene.shape.Rectangle


/**
 *  ************* How to use ************************
 *
 * Rectangle rectangle = new Rectangle(50, 50);
 * rectangle.setFill(Color.BLACK);
 * DragResizeMod.makeResizable(rectangle, null);
 *
 * Pane root = new Pane();
 * root.getChildren().add(rectangle);
 *
 * primaryStage.setScene(new Scene(root, 300, 275));
 * primaryStage.show();
 *
 * ************* OnDragResizeEventListener **********
 *
 * You need to override OnDragResizeEventListener and
 * 1) preform out of main field bounds check
 * 2) make changes to the node
 * (this class will not change anything in node coordinates)
 *
 * There is defaultListener and it works only with Canvas nad Rectangle
 *
 * Code made by https://github.com/varren/JavaFX-Resizable-Draggable-Node/
 */

class DragResizeMod() {

    private val defaultListener: OnDragResizeEventListener = object : OnDragResizeEventListener {
        override fun onDrag(node: Node?, x: Double, y: Double, h: Double, w: Double) {
            setNodeSize(node, x, y, h, w)
        }

        override fun onResize(node: Node?, x: Double, y: Double, h: Double, w: Double) {
            setNodeSize(node, x, y, h, w)
        }
    }

     private fun setNodeSize(node: Node?, x: Double, y: Double, h: Double, w: Double) {
         if (null != node) {
             node.layoutX = x
             node.layoutY = y

             if (node is Canvas) {
                 (node as Canvas).width = w
                 (node as Canvas).height = h
             } else if (node is Rectangle) {
                 (node as Rectangle).width = w
                 (node as Rectangle).height = h
             }
         }
     }


    private var clickX = 0.0
    private  var clickY: Double = 0.0
    private  var nodeX: Double = 0.0
    private  var nodeY: Double = 0.0
    private  var nodeH: Double = 0.0
    private  var nodeW: Double = 0.0

    private var state = DragResizeState.DEFAULT

    private var node: Node? = null
    private var listener: OnDragResizeEventListener = defaultListener

    private val MARGIN = 8
    private val MIN_W = 30.0
    private val MIN_H = 20.0

    constructor (node: Node, listener: OnDragResizeEventListener?) : this() {
        this.node = node
        if (listener != null) this.listener = listener
    }

    fun makeResizable(node: Node) {
        makeResizable(node, null)
    }

    fun makeResizable(node: Node, listener: OnDragResizeEventListener?) {
        val resizer = DragResizeMod(node, listener)
        node.setOnMousePressed { event -> resizer.mousePressed(event) }
        node.onMouseDragged = EventHandler<MouseEvent?> { event -> resizer.mouseDragged(event) }
        node.setOnMouseMoved { event -> resizer.mouseOver(event) }
        node.onMouseReleased = EventHandler<MouseEvent?> { event -> resizer.mouseReleased(event) }
    }

    fun mouseReleased(event: MouseEvent?) {
        if (null != node) {
            node!!.cursor = Cursor.DEFAULT
            state = DragResizeState.DEFAULT
        }
    }

    fun mouseOver(event: MouseEvent) {
        if (null != node) {
            node!!.cursor = getCursorForState(currentMouseState(event))
        }
    }

    private fun currentMouseState(event: MouseEvent): DragResizeState {
        var state = DragResizeState.DEFAULT
        val left = isLeftResizeZone(event)
        val right = isRightResizeZone(event)
        val top = isTopResizeZone(event)
        val bottom = isBottomResizeZone(event)
        if (left && top) state = DragResizeState.NW_RESIZE else if (left && bottom) state = DragResizeState.SW_RESIZE else if (right && top) state =
            DragResizeState.NE_RESIZE else if (right && bottom) state = DragResizeState.SE_RESIZE else if (right) state =
            DragResizeState.E_RESIZE else if (left) state = DragResizeState.W_RESIZE else if (top) state = DragResizeState.N_RESIZE else if (bottom) state =
            DragResizeState.S_RESIZE else if (isInDragZone(event)) state = DragResizeState.DRAG
        return state
    }

    private fun getCursorForState(state: DragResizeState): Cursor {
        return when (state) {
            DragResizeState.NW_RESIZE -> Cursor.NW_RESIZE
            DragResizeState.SW_RESIZE -> Cursor.SW_RESIZE
            DragResizeState.NE_RESIZE -> Cursor.NE_RESIZE
            DragResizeState.SE_RESIZE -> Cursor.SE_RESIZE
            DragResizeState.E_RESIZE -> Cursor.E_RESIZE
            DragResizeState.W_RESIZE -> Cursor.W_RESIZE
            DragResizeState.N_RESIZE -> Cursor.N_RESIZE
            DragResizeState.S_RESIZE -> Cursor.S_RESIZE
            else -> Cursor.DEFAULT
        }
    }


    fun mouseDragged(event: MouseEvent?) {
        if (null != event) {
            if (listener != null) {
                val mouseX = parentX(event.x)
                val mouseY = parentY(event.y)
                if (state == DragResizeState.DRAG) {
                    listener.onDrag(node, mouseX - clickX, mouseY - clickY, nodeH, nodeW)
                } else if (state != DragResizeState.DEFAULT) { //resizing
                    var newX = nodeX
                    var newY = nodeY
                    var newH = nodeH
                    var newW = nodeW
                    // Right Resize
                    if (state == DragResizeState.E_RESIZE || state == DragResizeState.NE_RESIZE || state == DragResizeState.SE_RESIZE) {
                        newW = mouseX - nodeX
                    }
                    // Left Resize
                    if (state == DragResizeState.W_RESIZE || state == DragResizeState.NW_RESIZE || state == DragResizeState.SW_RESIZE) {
                        newX = mouseX
                        newW = nodeW + nodeX - newX
                    }
                    // Bottom Resize
                    if (state == DragResizeState.S_RESIZE || state == DragResizeState.SE_RESIZE || state == DragResizeState.SW_RESIZE) {
                        newH = mouseY - nodeY
                    }
                    // Top Resize
                    if (state == DragResizeState.N_RESIZE || state == DragResizeState.NW_RESIZE || state == DragResizeState.NE_RESIZE) {
                        newY = mouseY
                        newH = nodeH + nodeY - newY
                    }
                    //min valid rect Size Check
                    if (newW < MIN_W) {
                        if (state == DragResizeState.W_RESIZE || state == DragResizeState.NW_RESIZE || state == DragResizeState.SW_RESIZE) newX =
                            newX - MIN_W + newW
                        newW = MIN_W
                    }
                    if (newH < MIN_H) {
                        if (state == DragResizeState.N_RESIZE || state == DragResizeState.NW_RESIZE || state == DragResizeState.NE_RESIZE) newY =
                            newY + newH - MIN_H
                        newH = MIN_H
                    }
                    listener.onResize(node, newX, newY, newH, newW)
                }
            }
        }
    }

    fun mousePressed(event: MouseEvent) {
        state = if (isInResizeZone(event)) {
            setNewInitialEventCoordinates(event)
            currentMouseState(event)
        } else if (isInDragZone(event)) {
            setNewInitialEventCoordinates(event)
            DragResizeState.DRAG
        } else {
            DragResizeState.DEFAULT
        }
    }

    private fun setNewInitialEventCoordinates(event: MouseEvent) {
        nodeX = nodeX()
        nodeY = nodeY()
        nodeH = nodeH()
        nodeW = nodeW()
        clickX = event.x
        clickY = event.y
    }

    private fun isInResizeZone(event: MouseEvent): Boolean {
        return (isLeftResizeZone(event) || isRightResizeZone(event)
                || isBottomResizeZone(event) || isTopResizeZone(event))
    }

    private fun isInDragZone(event: MouseEvent): Boolean {
        val xPos = parentX(event.x)
        val yPos = parentY(event.y)
        val nodeX = nodeX() + MARGIN
        val nodeY = nodeY() + MARGIN
        val nodeX0 = nodeX() + nodeW() - MARGIN
        val nodeY0 = nodeY() + nodeH() - MARGIN
        return xPos > nodeX && xPos < nodeX0 && yPos > nodeY && yPos < nodeY0
    }

    private fun isLeftResizeZone(event: MouseEvent): Boolean {
        return intersect(0.0, event.x)
    }

    private fun isRightResizeZone(event: MouseEvent): Boolean {
        return intersect(nodeW(), event.x)
    }

    private fun isTopResizeZone(event: MouseEvent): Boolean {
        return intersect(0.0, event.y)
    }

    private fun isBottomResizeZone(event: MouseEvent): Boolean {
        return intersect(nodeH(), event.y)
    }

    private fun intersect(side: Double, point: Double): Boolean {
        return side + MARGIN > point && side - MARGIN < point
    }

    private fun parentX(localX: Double): Double {
        return nodeX() + localX
    }

    private fun parentY(localY: Double): Double {
        return nodeY() + localY
    }

    private fun nodeX(): Double {
        if (null != node) {
            return node!!.boundsInParent.minX
        }

        return 0.0
    }

    private fun nodeY(): Double {
        if (null != node) {
            return node!!.boundsInParent.minY
        }

        return 0.0
    }

    private fun nodeW(): Double {
        if (null != node) {
            return node!!.boundsInParent.width
        }

        return 0.0
    }

    private fun nodeH(): Double {
        if (null != node) {
            return node!!.boundsInParent.height
        }

        return 0.0
    }
}