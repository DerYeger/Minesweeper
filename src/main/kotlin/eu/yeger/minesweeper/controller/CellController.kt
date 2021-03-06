package eu.yeger.minesweeper.controller

import eu.yeger.kofx.extension.bindVisible
import eu.yeger.kofx.extension.styleClasses
import eu.yeger.kofx.fragment.*
import eu.yeger.kofx.property.flip
import eu.yeger.minesweeper.ViewConfiguration
import eu.yeger.minesweeper.model.Cell
import eu.yeger.minesweeper.model.Game
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent

object CellController {

    fun initialize(cell: Cell, viewConfig: ViewConfiguration) = with(viewConfig) {
        stackPane {
            maxWidth = cellSize
            maxHeight = cellSize
            styleClasses("cell")
            child {
                if (cell.hasMine) {
                    imageView(mineIcon, fit = true) {
                        styleClasses("mine")
                    }
                } else {
                    label(cell.numberProperty.asString()) {
                        styleClasses("number", asWord(cell.number))
                    }
                }
            }.bindVisible(cell.unveiledProperty)
            child {
                rectangle(cellSize) {
                    setOnMouseClicked { handleClick(cell, it) }
                    styleClasses("blocker")
                    bindVisible(cell.unveiledProperty.not())
                }
            }
            child {
                imageView(flagIcon, fit = true) {
                    bindVisible(cell.flagProperty)
                    setOnMouseClicked { event -> handleClick(cell, event) }
                    styleClasses("flag")
                }
            }
        }
    }

    private fun handleClick(cell: Cell, event: MouseEvent) {
        if (cell.unveiled || cell.game.state != Game.State.IN_PROGRESS) return
        if (event.button == MouseButton.PRIMARY && !cell.hasFlag) {
            cell.unveiled = true
        } else if (event.button == MouseButton.SECONDARY) {
            cell.flagProperty.flip()
        }
    }

    private fun asWord(number: Int) = when (number) {
        0 -> "zero"
        1 -> "one"
        2 -> "two"
        3 -> "three"
        4 -> "four"
        5 -> "five"
        6 -> "six"
        7 -> "seven"
        8 -> "eight"
        else -> "null"
    }
}
