/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jevis.jeconfig.plugin.object.attribute;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Dialogs;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import org.jevis.jeapi.JEVisAttribute;
import org.jevis.jeapi.JEVisConstants;
import org.jevis.jeapi.JEVisException;
import org.jevis.jeapi.JEVisSample;
import org.jevis.jeconfig.JEConfig;
import org.jevis.jeconfig.sample.SampleTable;
import org.joda.time.DateTime;

/**
 *
 * @author Florian Simon <florian.simon@envidatec.com>
 */
public class StringValueEditor implements AttributeEditor {

    HBox box = new HBox();
    public JEVisAttribute _attribute;
    private TextField _field;
    private Node cell;
    private JEVisSample _newSample;
    private JEVisSample _lastSample;
    private boolean _hasChanged = false;

    public StringValueEditor(JEVisAttribute att) {
        _attribute = att;
    }

    @Override
    public boolean hasChanged() {
//        System.out.println(_attribute.getName() + " changed: " + _hasChanged);
        return _hasChanged;
    }

//    @Override
//    public void setAttribute(JEVisAttribute att) {
//        _attribute = att;
//    }
    @Override
    public void commit() throws JEVisException {
        if (_hasChanged && _newSample != null) {

            //TODO: check if tpye is ok, maybe better at imput time
            _newSample.commit();
        }
    }

    @Override
    public Node getEditor() {

        buildTextFild();

        return box;
//        return _field;
    }

    private void buildTextFild() {
        if (_field == null) {
            _field = new TextField();
            _field.setPrefWidth(500);//TODO: hmm workaround remove

            if (_attribute.hasSample()) {
                _field.setText(_attribute.getLatestSample().getValueAsString());
                _lastSample = _attribute.getLatestSample();
            }

            _field.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                    if (newPropertyValue) {
//                        System.out.println("Textfield on focus");
                    } else {
                        if (_lastSample != null) {
                            if (!_lastSample.getValueAsString().equals(_field.getText())) {
                                _hasChanged = true;
                            } else {
                                _hasChanged = false;
                            }
                        } else {
                            if (!_field.getText().equals("")) {
                                _hasChanged = true;
                            }
                        }

                        if (_hasChanged) {
                            try {
                                _newSample = _attribute.buildSample(new DateTime(), _field.getText());
                            } catch (JEVisException ex) {
                                Logger.getLogger(StringValueEditor.class.getName()).log(Level.SEVERE, null, ex);
                                Dialogs.showErrorDialog(JEConfig.getStage(), ex.getMessage(), "Error", null, ex);
                            }
                        }
                    }
                }
            });

//            _field.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
//                @Override
//                public void handle(KeyEvent event) {
//                    //changed
//                    event.consume();
//
//
//
//                }
//            });

            _field.setPrefWidth(500);
            _field.setId("attributelabel");

            Tooltip tooltip = new Tooltip();
            try {
                tooltip.setText(_attribute.getType().getDescription());
                tooltip.setGraphic(JEConfig.getImage("1393862576_info_blue.png", 30, 30));
                _field.setTooltip(tooltip);
            } catch (JEVisException ex) {
                Logger.getLogger(StringValueEditor.class.getName()).log(Level.SEVERE, null, ex);
            }




            box.getChildren().add(_field);
            HBox.setHgrow(_field, Priority.ALWAYS);

            try {
                if (_attribute.getType().getValidity() == JEVisConstants.Validity.AT_DATE) {
                    System.out.println("is at_DATE");
                    Button chartView = new Button();
                    chartView.setGraphic(JEConfig.getImage("1394566386_Graph.png", 20, 20));
                    chartView.setStyle("-fx-padding: 0 2 0 2;-fx-background-insets: 0;-fx-background-radius: 0;-fx-background-color: transparent;");

                    chartView.setMaxHeight(_field.getHeight());
                    chartView.setMaxWidth(20);

                    box.getChildren().add(chartView);
                    HBox.setHgrow(chartView, Priority.NEVER);

                    chartView.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            Stage dialogStage = new Stage();
                            dialogStage.setTitle("Sample Editor");
                            HBox root = new HBox();

                            root.getChildren().add(new SampleTable(_attribute));

                            Scene scene = new Scene(root);
                            dialogStage.setScene(scene);
                            dialogStage.show();

                        }
                    });




                }
            } catch (Exception ex) {
                Logger.getLogger(StringValueEditor.class.getName()).log(Level.SEVERE, null, ex);
            }



        }
    }
}
