package org.jevis.jeconfig.batchmode;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import org.controlsfx.dialog.Wizard;
import org.controlsfx.dialog.WizardPane;
import org.jevis.jeconfig.JEConfig;

/**
 *
 * @author CalisZ
 */
public class WizardStartPane extends WizardPane {

    private String control = "Manual";
    private RadioButton manual;
    private RadioButton automated;
    private RadioButton templateBased;

    public WizardStartPane() {
        setMinSize(500, 500);

        //INFO
        //Stage stage = (Stage) this.getScene().getWindow();
        //stage.setTitle("JEVIS Wizard");
        setContent(getInit());
        setGraphic(JEConfig.getImage("create_wizard.png", 100, 100));
    }

    private VBox getInit() {
        VBox vbox = new VBox();

        Label index = new Label();
        index.setText("JEVIS setup ");

        ToggleGroup group = new ToggleGroup();
        manual = new RadioButton("Manual");
        manual.setToggleGroup(group);
        manual.setSelected(true);

        automated = new RadioButton("Automated");
        automated.setToggleGroup(group);

        templateBased = new RadioButton("Template Based");
        templateBased.setToggleGroup(group);

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov,
                    Toggle old_toggle, Toggle new_toggle) {
                if (manual.isSelected()) {
                    setControl(manual.getText());
//                    System.out.println(manual.getText());
                } else if (automated.isSelected()) {
                    setControl(automated.getText());
//                    System.out.println(automated.getText());
                } else if (templateBased.isSelected()) {
                    setControl(templateBased.getText());
//                    System.out.println(templateBased.getText());
                }
            }
        });

        vbox.setSpacing(30);

        vbox.getChildren().addAll(index, manual, automated, templateBased);
        vbox.setPadding(new Insets(200, 10, 10, 20));

        return vbox;
    }

    @Override
    public void onEnteringPage(Wizard wizard) {

        ObservableList<ButtonType> list = getButtonTypes();

        for (ButtonType type : list) {
            if (type.getButtonData().equals(ButtonBar.ButtonData.BACK_PREVIOUS)) {
                Node prev = lookupButton(type);
                prev.visibleProperty().setValue(Boolean.FALSE);
            }
        }
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getControl() {
        return control;
    }
}
