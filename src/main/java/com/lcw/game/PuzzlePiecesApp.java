/*
 * Copyright (c) 2008, 2014, Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.lcw.game;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * A sample in which an image is broken into pieces to create a jigsaw puzzle.
 *
 * @sampleName Puzzle Pieces
 * @preview preview.png
 * @see javafx.scene.shape.Path
 * @see javafx.scene.image.Image
 * @see javafx.scene.image.ImageView
 * @see javafx.scene.control.Button
 * @see javafx.scene.layout.Pane
 * @see javafx.scene.input.MouseEvent
 * @see javafx.scene.effect.DropShadow
 */
public class PuzzlePiecesApp {

    private static final Image IMAGE = new Image("https://www.vbox.top/app/ad.jpg");
    private Timeline timeline;

    public Parent createContent() {
        // load puzzle image
        int numOfColumns = (int) (IMAGE.getWidth() / Piece.SIZE);
        int numOfRows = (int) (IMAGE.getHeight() / Piece.SIZE);
        // create desk
        final Desk desk = new Desk(numOfColumns, numOfRows);
        // create puzzle pieces
        final List<Piece> pieces = new ArrayList<>();
        for (int col = 0; col < numOfColumns; col++) {
            for (int row = 0; row < numOfRows; row++) {
                int x = col * Piece.SIZE;
                int y = row * Piece.SIZE;
                final Piece piece = new Piece(IMAGE, x, y, row > 0, col > 0,
                        row < numOfRows - 1, col < numOfColumns - 1,
                        desk.getWidth(), desk.getHeight());
                pieces.add(piece);
            }
        }
        desk.getChildren().addAll(pieces);
        // create button box
        Button shuffleButton = new Button("开始");
        shuffleButton.setStyle("-fx-font-size: 2em;");
        shuffleButton.setOnAction((ActionEvent actionEvent) -> {
            if (timeline != null) {
                timeline.stop();
            }
            timeline = new Timeline();
            for (final Piece piece : pieces) {
                piece.setActive();
                double shuffleX = Math.random()
                        * (desk.getWidth() - Piece.SIZE + 48f)
                        - 24f - piece.getCorrectX();
                double shuffleY = Math.random()
                        * (desk.getHeight() - Piece.SIZE + 30f)
                        - 15f - piece.getCorrectY();
                timeline.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(1),
                                new KeyValue(piece.translateXProperty(), shuffleX),
                                new KeyValue(piece.translateYProperty(), shuffleY)));
            }
            timeline.playFromStart();
        });
        Button solveButton = new Button("还原");
        solveButton.setStyle("-fx-font-size: 2em;");
        solveButton.setOnAction((ActionEvent actionEvent) -> {
            if (timeline != null) {
                timeline.stop();
            }
            timeline = new Timeline();
            for (final Piece piece : pieces) {
                piece.setInactive();
                timeline.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(1),
                                new KeyValue(piece.translateXProperty(), 0),
                                new KeyValue(piece.translateYProperty(), 0)));
            }
            timeline.playFromStart();
        });
        HBox buttonBox = new HBox(8);
        buttonBox.getChildren().addAll(shuffleButton, solveButton);
        buttonBox.setAlignment(Pos.CENTER);
        // create vbox for desk and buttons
        VBox vb = new VBox(10);
        vb.setAlignment(Pos.CENTER);
        vb.setPrefWidth(612);
        vb.getChildren().addAll(desk, buttonBox);
        vb.setPadding(new Insets(10, 10, 10, 10));
        vb.setMaxSize(VBox.USE_PREF_SIZE, VBox.USE_PREF_SIZE);
        vb.setMinSize(VBox.USE_PREF_SIZE, VBox.USE_PREF_SIZE);
        return vb;
    }

}
