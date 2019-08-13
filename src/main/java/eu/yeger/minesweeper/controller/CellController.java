package eu.yeger.minesweeper.controller;

import eu.yeger.minesweeper.model.Cell;
import eu.yeger.minesweeper.model.Game;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class CellController {

    private final Cell cell;
    private final int cellSize;

    public CellController(final Cell cell,
                          final int cellSize) {
        this.cell = cell;
        this.cellSize = cellSize;
    }

    public Node initialize() {
        final StackPane cellContainer = new StackPane();
        cellContainer.getStyleClass().add("cell");
        cellContainer.setMaxWidth(cellSize);
        cellContainer.setMaxHeight(cellSize);

        final Label stateLabel = new Label();
        if (cell.bomb.get()) {
            stateLabel.setText("B");
            stateLabel.getStyleClass().add("bomb");
        } else if (cell.number.get() > 0) {
            stateLabel.setText(Integer.toString(cell.number.get()));
            stateLabel.getStyleClass().add("number");
        }
        cellContainer.getChildren().add(stateLabel);

        final Rectangle rectangle = new Rectangle();
        rectangle.visibleProperty().bind(cell.unveiled.not());
        rectangle.getStyleClass().add("blocker");
        rectangle.setWidth(cellSize);
        rectangle.setHeight(cellSize);
        cellContainer.getChildren().add(rectangle);
        rectangle.setOnMouseClicked(event -> handleClick(cell, event));

        final Label flagLabel = new Label();
        flagLabel.visibleProperty().bindBidirectional(cell.flag);
        flagLabel.setText("F");
        flagLabel.getStyleClass().add("flag");
        flagLabel.setOnMouseClicked(event -> handleClick(cell, event));
        cellContainer.getChildren().add(flagLabel);

        return cellContainer;
    }

    private void handleClick(final Cell cell,
                             final MouseEvent event) {
        if (cell.unveiled.get() || !cell.getGame().state.get().equals(Game.State.IN_PROGRESS)) return;
        if (event.getButton() == MouseButton.PRIMARY) {
            if (!cell.flag.get()) {
                cell.unveiled.set(true);
            }
        } else if (event.getButton() == MouseButton.SECONDARY) { //toggle flag
            cell.flag.set(!cell.flag.get());
        }
    }
}
