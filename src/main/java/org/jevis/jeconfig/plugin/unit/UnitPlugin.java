/**
 * Copyright (C) 2009 - 2014 Envidatec GmbH <info@envidatec.com>
 *
 * This file is part of JEConfig.
 *
 * JEConfig is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation in version 3.
 *
 * JEConfig is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * JEConfig. If not, see <http://www.gnu.org/licenses/>.
 *
 * JEConfig is part of the OpenJEVis project, further project information are
 * published at <http://www.OpenJEVis.org/>.
 */
package org.jevis.jeconfig.plugin.unit;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.SplitPaneBuilder;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.jevis.api.JEVisDataSource;
import org.jevis.application.unit.UnitObject;
import org.jevis.application.unit.UnitTree;
import org.jevis.jeconfig.Constants;
import org.jevis.jeconfig.JEConfig;
import org.jevis.jeconfig.Plugin;

/**
 *
 * @author Florian Simon <florian.simon@envidatec.com>
 */
public class UnitPlugin implements Plugin {

    private StringProperty name = new SimpleStringProperty("*NO_NAME*");
    private StringProperty id = new SimpleStringProperty("*NO_ID*");
    private JEVisDataSource ds;
    private BorderPane border;
//    private ObjectTree tf;
    private UnitTree tree;
    private UnitEditor _editor;

    public UnitPlugin(JEVisDataSource ds, String newname) {
        this.ds = ds;
        name.set(newname);
    }

    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public void setName(String value) {
        name.set(value);
    }

    @Override
    public StringProperty nameProperty() {
        return name;
    }

    @Override
    public String getUUID() {
        return id.get();
    }

    @Override
    public void setUUID(String newid) {
        id.set(newid);
    }

    @Override
    public StringProperty uuidProperty() {
        return id;
    }

    @Override
    public Node getConntentNode() {
        if (border == null) {

            tree = new UnitTree(ds);
            _editor = new UnitEditor();

            tree.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<TreeItem<UnitObject>>() {

                @Override
                public void onChanged(ListChangeListener.Change<? extends TreeItem<UnitObject>> change) {
                    System.out.println("user selected: ");
                    for (TreeItem<UnitObject> object : change.getList()) {
                        System.out.println(" - " + object.getValue().getUnit().toString());
                    }
                    try {
                        _editor.setUnit(change.getList().get(0).getValue());
                    } catch (NullPointerException ne) {
                        System.out.println("waring, nullpoint in unittree selection");
                    }
                }
            });

            VBox left = new VBox();
            left.setStyle("-fx-background-color: #E2E2E2;");
            left.getChildren().addAll(tree);
            VBox.setVgrow(tree, Priority.ALWAYS);

            SplitPane sp = SplitPaneBuilder.create()
                    .items(left, _editor.getView())
                    .dividerPositions(new double[]{.2d, 0.8d}) // why does this not work!?
                    .orientation(Orientation.HORIZONTAL)
                    .build();
            sp.setId("mainsplitpane");
            sp.setStyle("-fx-background-color: " + Constants.Color.LIGHT_GREY2);
            border = new BorderPane();
            border.setCenter(sp);
            border.setStyle("-fx-background-color: " + Constants.Color.LIGHT_GREY2);
        }

        return border;
    }

    @Override
    public Node getMenu() {
        return null;
    }

    @Override
    public Node getToolbar() {
        return null;
    }

    @Override
    public JEVisDataSource getDataSource() {
        return ds;
    }

    @Override
    public void setDataSource(JEVisDataSource ds) {
        this.ds = ds;
    }

    @Override
    public void handelRequest(int cmdType) {
//        try {
//            switch (cmdType) {
//                case Constants.Plugin.Command.SAVE:
//                    System.out.println("save");
//                    tree.fireSaveAttributes(false);
//                    break;
//                case Constants.Plugin.Command.DELTE:
//                    tree.fireDelete(tree.getSelectedObject());
//                    break;
//                case Constants.Plugin.Command.EXPAND:
//                    System.out.println("Expand");
//                    break;
//                case Constants.Plugin.Command.NEW:
//                    tree.fireEventNew(tree.getSelectedObject());
//                    break;
//                case Constants.Plugin.Command.RELOAD:
//                    tree.reload();
//                    break;
//                default:
//                    System.out.println("Unknows command ignore...");
//            }
//        } catch (Exception ex) {
//        }

    }

    @Override
    public void fireCloseEvent() {
//        try {
//            tree.fireSaveAttributes(true);
//        } catch (JEVisException ex) {
//            Logger.getLogger(UnitPlugin.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public void Save() {
//        try {
//            tree.fireSaveAttributes(false);
//        } catch (JEVisException ex) {
//            Logger.getLogger(UnitPlugin.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public ImageView getIcon() {
        return JEConfig.getImage("1405444584_measure.png", 20, 20);
    }
}
