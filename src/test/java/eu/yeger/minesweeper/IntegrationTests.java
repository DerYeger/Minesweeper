package eu.yeger.minesweeper;

import eu.yeger.minesweeper.model.Cell;
import eu.yeger.minesweeper.model.Game;
import eu.yeger.minesweeper.model.ModelBuilder;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class IntegrationTests extends ApplicationTest {

    private Stage stage;

    private boolean won;
    private boolean lost;

    @Override
    public void start(final Stage stage)  {
        this.stage = stage;
        won = false;
        lost = false;
    }

    private void click(final int x, final int y, final MouseButton button) {
        clickOn(stage.getX() + x, stage.getY() + y, button);
    }

    private void setScene(final Scene scene) {
        Platform.runLater(() -> {
            stage.hide();
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
        });
       WaitForAsyncUtils.waitForFxEvents();
    }

    private Game buildTestGameAlpha() {
        final Game game = new Game(2, 2);
        final Cell tl = new Cell(0, 0);
        final Cell tr = new Cell(1, 0);
        final Cell bl = new Cell(0, 1);
        final Cell br = new Cell(1, 1);
        tl.withNeighbors(Arrays.asList(tr, bl, br));
        game.withCells(Arrays.asList(tl, tr, bl, br));
        br.bomb.set(true);

        return game;
    }

    private Game buildTestGameBeta() {
        final Game game = new Game(2, 2);
        final Cell tl = new Cell(0, 0);
        final Cell tr = new Cell(1, 0);
        final Cell bl = new Cell(0, 1);
        final Cell br = new Cell(1, 1);
        tl.number.set(1);
        tr.number.set(1);
        bl.number.set(1);
        br.number.set(1);
        tl.withNeighbors(Arrays.asList(tr, bl, br));
        game.withCells(Arrays.asList(tl, tr, bl, br));
        br.bomb.set(true);

        return game;
    }

    @Test
    public void testWin() {
        final Minesweeper minesweeper = Minesweeper
                .builder()
                .width(2)
                .height(2)
                .bombCount(0)
                .cellSize(30)
                .onGameWon(() -> won = true)
                .onGameLost(() -> lost = true)
                .build();
        setScene(new Scene((Parent) minesweeper.instance()));
        click(15, 15, MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(won);
        assertFalse(lost);
    }

    @Test
    public void testFlag() {
        final Minesweeper minesweeper = Minesweeper
                .builder()
                .width(1)
                .height(1)
                .bombCount(1)
                .cellSize(20)
                .onGameWon(() -> won = true)
                .onGameLost(() -> lost = true)
                .build();
        setScene(new Scene((Parent) minesweeper.instance()));
        clickOn(stage, MouseButton.SECONDARY);
        clickOn(stage, MouseButton.PRIMARY);
        assertFalse(won);
        assertFalse(lost);
        clickOn(stage, MouseButton.SECONDARY);
        clickOn(stage, MouseButton.PRIMARY);
        assertFalse(won);
        assertTrue(lost);
    }

    @Test
    public void testUnveiling() {
        final ModelBuilder modelBuilder = mock(ModelBuilder.class);
        when(modelBuilder.buildGame()).thenReturn(buildTestGameAlpha());
        final Minesweeper minesweeper = Minesweeper
                .builder()
                .cellSize(30)
                .onGameWon(() -> won = true)
                .onGameLost(() -> lost = true)
                .build();
        minesweeper.setModelBuilder(modelBuilder);
        setScene(new Scene((Parent) minesweeper.instance()));
        click(15, 15, MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(won);
        assertFalse(lost);
    }

    @Test
    public void testUnveilingBlocked() {
        final ModelBuilder modelBuilder = mock(ModelBuilder.class);
        when(modelBuilder.buildGame()).thenReturn(buildTestGameBeta());
        final Minesweeper minesweeper = Minesweeper
                .builder()
                .cellSize(30)
                .onGameWon(() -> won = true)
                .onGameLost(() -> lost = true)
                .build();
        minesweeper.setModelBuilder(modelBuilder);
        setScene(new Scene((Parent) minesweeper.instance()));
        click(15, 15, MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();
        assertFalse(won);
        assertFalse(lost);
    }
}