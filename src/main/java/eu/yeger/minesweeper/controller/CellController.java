package eu.yeger.minesweeper.controller;

import eu.yeger.minesweeper.model.Cell;
import eu.yeger.minesweeper.model.Game;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class CellController {

    private final Cell cell;
    private final int cellSize;

    private Image flagImage;

    public CellController(final Cell cell,
                          final int cellSize,
                          final Image flagImage) {
        this.cell = cell;
        this.cellSize = cellSize;
        this.flagImage = flagImage;
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
        } else {
            stateLabel.setText(Integer.toString(cell.number.get()));
            stateLabel.getStyleClass().addAll("number", asWord(cell.number.get()));
        }
        cellContainer.getChildren().add(stateLabel);

        final Rectangle rectangle = new Rectangle();
        rectangle.visibleProperty().bind(cell.unveiled.not());
        rectangle.getStyleClass().add("blocker");
        rectangle.setWidth(cellSize);
        rectangle.setHeight(cellSize);
        cellContainer.getChildren().add(rectangle);
        rectangle.setOnMouseClicked(event -> handleClick(cell, event));

        final ImageView flag = new ImageView();
        flag.setImage(flagImage);
        flag.visibleProperty().bindBidirectional(cell.flag);
        flag.getStyleClass().add("flag");
        flag.setOnMouseClicked(event -> handleClick(cell, event));
        flag.setFitWidth(cellSize);
        flag.setFitHeight(cellSize);
        cellContainer.getChildren().add(flag);

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

    private String asWord(final int number) {
        switch (number) {
            case 0:
                return "zero";
            case 1:
                return "one";
            case 2:
                return "two";
            case 3:
                return "three";
            case 4:
                return "four";
            case 5:
                return "five";
            case 6:
                return "six";
            case 7:
                return "seven";
            case 8:
                return "eight";
            default:
                return null;
        }
    }
}
