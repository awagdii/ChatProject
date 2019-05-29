/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.toggle;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 *
 * @author Shall
 */
public class ToggleSwitch extends Parent {

    private BooleanProperty switchOn = new SimpleBooleanProperty();
    //-------------- Animation responsiple for moving the toggle circle---------------------------------------
    private TranslateTransition translateAnimation = new TranslateTransition(Duration.seconds(.25));

    //--------------Animation responsiple for filling the toggle cirlce---------------------------------------
    private FillTransition fillAnimation = new FillTransition(Duration.seconds(.25));

    //--------------Aimation responsiple for playing the previous two animations at the same time-------------
    private ParallelTransition animation = new ParallelTransition(translateAnimation, fillAnimation);

    public ToggleSwitch() {
        //---------------- Start of toggle button property----------------------
        Rectangle background = new Rectangle(100, 50);
        background.setArcWidth(50);
        background.setArcHeight(50);
        background.setFill(Color.WHITE);
        background.setStroke(Color.LIGHTGRAY);

        Circle trigger = new Circle(25);
        trigger.setCenterX(25);
        trigger.setCenterY(25);
        trigger.setFill(Color.WHITE);
        trigger.setStroke(Color.LIGHTGRAY);
        translateAnimation.setNode(trigger);
        fillAnimation.setShape(background);
        getChildren().addAll(background, trigger);

        switchOn.addListener((obs, oldState, newState) -> {
            boolean isOn = newState.booleanValue();
            translateAnimation.setToX(isOn ? 100 - 50 : 0);
            fillAnimation.setFromValue(isOn ? Color.WHITE : Color.GREEN);
            fillAnimation.setToValue(isOn ? Color.GREEN : Color.WHITE);
            animation.play();
        });
        setOnMouseClicked(event -> {
            switchOn.set(!switchOn.get());
        });

    }

    public void setSwitchOn(boolean state) {
        BooleanProperty switchOnState = new SimpleBooleanProperty(state);
        this.switchOn = switchOnState;
    }

    public BooleanProperty getSwitchOn() {
        return switchOn;
    }

}
