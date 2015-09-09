/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jevis.jeconfig.batchmode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.Pair;
import org.controlsfx.dialog.Wizard;
import org.controlsfx.dialog.WizardPane;
import org.jevis.api.JEVisAttribute;
import org.jevis.api.JEVisClass;
import org.jevis.api.JEVisException;
import org.jevis.api.JEVisObject;
import org.jevis.jeconfig.tool.ImageConverter;
import org.joda.time.DateTime;

/**
 *
 * @author CalisZ
 */
public class ManualWizardStep2 extends WizardPane {

    private JEVisClass createClass;
    private TextField serverNameTextField;

    private ObservableList<String> typeNames = FXCollections.observableArrayList();
    private ObservableList<String> listBuildSample = FXCollections.observableArrayList();
    private WizardSelectedObject wizardSelectedObject;
    private Map<String, String> map = new TreeMap<String, String>();

    public ManualWizardStep2(WizardSelectedObject wizardSelectedObject) {
        this.wizardSelectedObject = wizardSelectedObject;
        setMinSize(500, 500);
        setGraphic(null);

    }

    @Override
    public void onEnteringPage(Wizard wizard) {
        setContent(getInit());
        ObservableList<ButtonType> list = getButtonTypes();

        for (ButtonType type : list) {
            if (type.getButtonData().equals(ButtonBar.ButtonData.BACK_PREVIOUS)) {
                Node prev = lookupButton(type);
                prev.visibleProperty().setValue(Boolean.FALSE);
            }
        }
    }

    @Override
    public void onExitingPage(Wizard wizard) {
        try {
            JEVisObject newObject = wizardSelectedObject.getSelectedObject().buildObject(serverNameTextField.getText(), createClass);
            newObject.commit();
            commitAttributes(newObject);

//            for (Map.Entry<String, String> entrySet : map.entrySet()) {
//                String key = entrySet.getKey();
//                String value = entrySet.getValue();
//                System.out.println("map : " + key + "  " + value);
//            }
        } catch (JEVisException ex) {
            Logger.getLogger(ManualWizardStep2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void commitAttributes(JEVisObject newObject) {
        try {
            List<JEVisAttribute> attribut = newObject.getAttributes();
            ObservableList<JEVisAttribute> mylist = FXCollections.observableArrayList(attribut);
            sortTheChildren(mylist);

            for (Map.Entry<String, String> entrySet : map.entrySet()) {
                listBuildSample.add(entrySet.getValue());
            }

            for (int i = 0; i < mylist.size(); i++) {
                mylist.get(i).buildSample(new DateTime(), listBuildSample.get(i)).commit();
            }

        } catch (JEVisException ex) {
            Logger.getLogger(ManualWizardStep2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void sortTheChildren(ObservableList<JEVisAttribute> list) {
        Comparator<JEVisAttribute> sort = new Comparator<JEVisAttribute>() {
            @Override
            public int compare(JEVisAttribute o1, JEVisAttribute o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };
        FXCollections.sort(list, sort);
    }

    private BorderPane getInit() {
        BorderPane root = new BorderPane();

        ObservableList<JEVisClass> options = FXCollections.observableArrayList();

        try {
            options = FXCollections.observableArrayList(wizardSelectedObject.getSelectedObject().getAllowedChildrenClasses());
        } catch (JEVisException ex) {
            Logger.getLogger(ManualWizardStep2.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Set the cell properties for ComboBox
        Callback<ListView<JEVisClass>, ListCell<JEVisClass>> cellFactory = new Callback<ListView<JEVisClass>, ListCell<JEVisClass>>() {
            @Override
            public ListCell<JEVisClass> call(ListView<JEVisClass> param) {
                final ListCell<JEVisClass> cell = new ListCell<JEVisClass>() {
                    {
                        super.setPrefWidth(260);
                    }

                    @Override
                    public void updateItem(JEVisClass item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !empty) {
                            HBox box = new HBox(5);
                            box.setAlignment(Pos.CENTER_LEFT);
                            try {
                                ImageView icon = ImageConverter.convertToImageView(item.getIcon(), 15, 15);
                                Label cName = new Label(item.getName());
                                cName.setTextFill(Color.BLACK);
                                box.getChildren().setAll(icon, cName);

                            } catch (JEVisException ex) {
                                Logger.getLogger(CreateTable.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            setGraphic(box);

                        }
                    }
                };
                return cell;
            }
        };

        Label serverName = new Label();
        serverNameTextField = new TextField();
        serverNameTextField.setPrefWidth(200);
        serverNameTextField.setPromptText("Server Name");

        ComboBox<JEVisClass> classComboBox = new ComboBox<JEVisClass>(options);
        classComboBox.setCellFactory(cellFactory);
        classComboBox.setButtonCell(cellFactory.call(null));
        classComboBox.setMinWidth(250);
        classComboBox.getSelectionModel().selectFirst();
        createClass = classComboBox.getSelectionModel().getSelectedItem();

        classComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                typeNames.clear();
                map.clear();
                listBuildSample.clear();
                createClass = classComboBox.getSelectionModel().getSelectedItem();
                try {
                    for (int i = 0; i < createClass.getTypes().size(); i++) {
                        typeNames.add(createClass.getTypes().get(i).getName());

//                        System.out.println(createClass.getTypes().get(i).getName() + " : " + createClass.getTypes().get(i).getGUIDisplayType());
                    }
                } catch (JEVisException ex) {
                    Logger.getLogger(CreateTable.class.getName()).log(Level.SEVERE, null, ex);
                }

                root.setCenter(getTypes());
            }
        });

        //Get and set the typenames from a class. Typenames are for the columnnames.
        try {
            for (int i = 0; i < createClass.getTypes().size(); i++) {
                typeNames.add(createClass.getTypes().get(i).getName());
            }
        } catch (JEVisException ex) {
            Logger.getLogger(CreateTable.class.getName()).log(Level.SEVERE, null, ex);
        }

        serverName.setText("Name : ");

        //Servername and ComboBox
        HBox hBoxTop = new HBox();
        hBoxTop.setSpacing(10);
        hBoxTop.getChildren().addAll(serverName, serverNameTextField, classComboBox);
        hBoxTop.setPadding(new Insets(10, 10, 10, 10));

        root.setTop(hBoxTop);
        root.setCenter(getTypes());

        return root;
    }

    //Erzeuge Label,TextField und CheckBox fuer Gridpane
    public GridPane getTypes() {
        GridPane gridpane = new GridPane();

        for (int i = 0; i < typeNames.size(); i++) {
            Label label = new Label(typeNames.get(i) + " : ");
            try {
//                System.out.println("->" + createClass.getTypes().get(i).getName() + " : " + createClass.getTypes().get(i).getGUIDisplayType());
                if (createClass.getTypes().get(i).getGUIDisplayType() == null || createClass.getTypes().get(i).getGUIDisplayType().equals("Text")) {

                    TextField textField = new TextField();
                    textField.setId(createClass.getTypes().get(i).getName());
                    textField.setPrefWidth(400);
                    textField.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                            System.out.println("Control id :  " + textField.getId() + " : " + textField.getText());
                            map.put(textField.getId(), textField.getText());
                        }
                    });
                    gridpane.addRow(i, label, textField);
                } else {
                    CheckBox checkBox = new CheckBox();

                    checkBox.setId(createClass.getTypes().get(i).getName());
                    //TODO 
                    checkBox.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            if (checkBox.isSelected() == true) {
                                map.put(checkBox.getId(), "1");
                            } else {
                                map.put(checkBox.getId(), "0");
                            }
                        }
                    });

                    gridpane.addRow(i, label, checkBox);
                }
            } catch (JEVisException ex) {
                Logger.getLogger(ManualWizardStep2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        gridpane.setHgap(10);//horizontal gap in pixels 
        gridpane.setVgap(10);//vertical gap in pixels
        gridpane.setPadding(new Insets(10, 10, 10, 10));////margins around the whole grid

        return gridpane;
    }
}