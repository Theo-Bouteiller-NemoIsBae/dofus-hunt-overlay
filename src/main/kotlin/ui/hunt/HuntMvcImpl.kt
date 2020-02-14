package ui.hunt

import extension.isNumeric
import javafx.collections.ObservableList
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import shared.api.httprequest.Direction
import shared.api.httprequest.result.Hint

class HuntMvcImpl(
    private val context: Hunt
) : HuntMvc {

    init {
        context.fxid<ComboBox<String>>("hintComboBox").getValue(context, ComboBox<String>::javaClass).let { comboBox ->
            comboBox.isDisable = true
            comboBox.valueProperty().addListener { _, _, new ->
                if (null != new) {
                    println("New : $new")
                    context.onUserSelectAnHint(new)
                }
            }
        }

        context.fxid<Button>("leftButton").getValue(context, Button::javaClass).setOnMouseClicked {
            makeRequest(Direction.LEFT)
        }

        context.fxid<Button>("rightButton").getValue(context, Button::javaClass).setOnMouseClicked {
            makeRequest(Direction.RIGHT)
        }

        context.fxid<Button>("downButton").getValue(context, Button::javaClass).setOnMouseClicked {
            makeRequest(Direction.BOTTOM)
        }

        context.fxid<Button>("upButton").getValue(context, Button::javaClass).setOnMouseClicked {
            makeRequest(Direction.TOP)
        }

        context.fxid<Button>("missingHintButton").getValue(context, Button::javaClass).let { button ->

            button.isDisable = true

            button.setOnMouseClicked {

            }
        }

        context.fxid<TextField>("xTextField").getValue(context, TextField::javaClass).let { xTextField ->

            xTextField.setOnMouseClicked {
                xTextField.text = ""
            }

            context.fxid<Button>("plusXButton").getValue(context, Button::javaClass).setOnMouseClicked {
                incrementTextField(xTextField, 1)
            }

            context.fxid<Button>("minusXButton").getValue(context, Button::javaClass).setOnMouseClicked {
                decrementTextField(xTextField, 1)
            }
        }

        context.fxid<TextField>("yTextField").getValue(context, TextField::javaClass).let { yTextField ->

            yTextField.setOnMouseClicked {
                yTextField.text = ""
            }

            context.fxid<Button>("plusYButton").getValue(context, Button::javaClass).setOnMouseClicked {
                incrementTextField(yTextField, 1)
            }

            context.fxid<Button>("minusYButton").getValue(context, Button::javaClass).setOnMouseClicked {
                decrementTextField(yTextField, 1)
            }
        }
    }

    private fun incrementTextField(textFlied: TextField, incrementNumber: Int) {
        if (textFlied.text.isNumeric()) {
            textFlied.text = textFlied.text.toInt().plus(incrementNumber).toString()
        }
    }

    private fun decrementTextField(textFlied: TextField, decrementNumber: Int) {
        if (textFlied.text.isNumeric()) {
            textFlied.text = textFlied.text.toInt().minus(decrementNumber).toString()
        }
    }


    private fun makeRequest(direction: Direction) {
        context.fxid<ComboBox<String>>("hintComboBox").getValue(context, ComboBox<String>::javaClass).isDisable = true

        if (
            context.fxid<TextField>("xTextField").getValue(context, TextField::javaClass).text.isNumeric() &&
            context.fxid<TextField>("yTextField").getValue(context, TextField::javaClass).text.isNumeric()
        ) {
            context.onUserWantToKnowHintResult(
                x = context.fxid<TextField>("xTextField").getValue(context, TextField::javaClass).text.toInt(),
                y = context.fxid<TextField>("yTextField").getValue(context, TextField::javaClass).text.toInt(),
                direction = direction
            )
        }
    }

    override fun onHintsAreLoaded(items: ObservableList<String>) {
        context.fxid<ComboBox<String>>("hintComboBox").getValue(context, ComboBox<String>::javaClass).let { comboBox ->
            comboBox.items = items
            comboBox.isDisable = false
        }
    }

    override fun onSelectHintAreLoaded(hint: Hint) {
        println("hint : n: ${hint.nameId} d: ${hint.d} x: ${hint.x} y: ${hint.y} name: ${hint.name} direction: ${hint.direction?.value}")
        context.fxid<ComboBox<String>>("hintComboBox").getValue(context, ComboBox<String>::javaClass).isDisable = true
    }

    override fun onUserDoneHintPopUp(hint: Hint) {
        context.fxid<ComboBox<String>>("hintComboBox").getValue(context, ComboBox<String>::javaClass).items = null
        context.fxid<TextField>("xTextField").getValue(context, TextField::javaClass).text = hint.x.toString()
        context.fxid<TextField>("yTextField").getValue(context, TextField::javaClass).text = hint.y.toString()
    }
}