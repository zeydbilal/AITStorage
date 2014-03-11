/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jevis.jeconfig;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import org.jevis.jeconfig.plugin.object.ObjectPlugin;
import org.jevis.jeconfig.Constants.Plugin.*;

/**
 *
 * @author Florian Simon <florian.simon@envidatec.com>
 */
public class GlobalToolBar {

    private static final String STANDARD_BUTTON_STYLE = "-fx-background-color: transparent;-fx-background-insets: 0 0 0;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-background-insets: 1 1 1;";
    private PluginManager pm;

    public GlobalToolBar(PluginManager pm) {
        this.pm = pm;
    }

    public ToolBar ToolBarFactory() {
        ToolBar toolBar = new ToolBar();
        double iconSize = 20;
        ToggleButton newB = new ToggleButton("", JEConfig.getImage("list-add.png", iconSize, iconSize));
        changeBackgroundOnHoverUsingBinding(newB);
        addEventHandler(newB, Constants.Plugin.Command.NEW);

        ToggleButton save = new ToggleButton("", JEConfig.getImage("save.gif", iconSize, iconSize));
        changeBackgroundOnHoverUsingBinding(save);
        addEventHandler(save, Constants.Plugin.Command.SAVE);

        ToggleButton delete = new ToggleButton("", JEConfig.getImage("list-remove.png", iconSize, iconSize));
        changeBackgroundOnHoverUsingBinding(delete);
        addEventHandler(delete, Constants.Plugin.Command.DELTE);


        Separator sep1 = new Separator();
        toolBar.getItems().addAll(newB, save, delete, sep1);
        return toolBar;
    }

    private void addEventHandler(ToggleButton button, final int command) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (pm.getSelectedPlugin() instanceof ObjectPlugin) {
                    ((ObjectPlugin) pm.getSelectedPlugin()).handelRequest(command);
                }

            }
        });

    }

    private void addEventHandler(Button button, final int command) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (pm.getSelectedPlugin() instanceof ObjectPlugin) {
                    ((ObjectPlugin) pm.getSelectedPlugin()).handelRequest(command);
                }

            }
        });

    }

    private static void changeBackgroundOnHoverUsingBinding(Node node) {
        node.styleProperty().bind(
                Bindings
                .when(node.hoverProperty())
                .then(
                new SimpleStringProperty(HOVERED_BUTTON_STYLE))
                .otherwise(
                new SimpleStringProperty(STANDARD_BUTTON_STYLE)));
    }
}
