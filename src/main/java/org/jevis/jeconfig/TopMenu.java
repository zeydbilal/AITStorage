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
package org.jevis.jeconfig;

import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import org.jevis.application.dialog.AboutDialog;
import org.jevis.jeconfig.csv.CSVImportDialog;

/**
 * This class build the top menu bar for JEConfig.
 *
 *
 *
 * @author Florian Simon <florian.simon@envidatec.com>
 */
public class TopMenu extends MenuBar {

    public TopMenu() {
        super();
        Menu menuFile = new Menu("File");
        Menu subMenuImport = new Menu("Import File");
        MenuItem importCSV = new MenuItem("CSV");
        MenuItem importXML = new MenuItem("XML");
        MenuItem importJSON = new MenuItem("JEVis Data Files");

        subMenuImport.getItems().addAll(importCSV, importXML, importJSON);
        menuFile.getItems().add(new MenuItem("New"));
        menuFile.getItems().add(new SeparatorMenuItem());
        menuFile.getItems().add(subMenuImport);
        MenuItem exit = new MenuItem("Exit");
        menuFile.getItems().add(exit);

        importJSON.setDisable(true);
        importXML.setDisable(true);

        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                System.exit(0);
            }
        });

        importCSV.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                CSVImportDialog impDia = new CSVImportDialog();
                impDia.show(JEConfig.getStage(), JEConfig.getDataSource());
            }
        });

        // --- Menu Edit
        Menu menuEdit = new Menu("Edit");
        MenuItem copie = new MenuItem("Copy");
        MenuItem delete = new MenuItem("Delete");
        MenuItem rename = new MenuItem("Rename");
        rename.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            }
        });

        menuEdit.getItems().addAll(copie, delete, rename);

        // --- Menu View
        Menu menuView = new Menu("View");

        Menu options = new Menu("Options");
        final Preferences pref = Preferences.userRoot().node("JEVis.JEConfig.Welcome");
        CheckMenuItem welcome = new CheckMenuItem("Welcome screen");
        welcome.setSelected(pref.getBoolean("show", true));
        welcome.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                pref.putBoolean("show", !pref.getBoolean("show", true));
            }
        });
        options.getItems().add(welcome);

        Menu help = new Menu("Help");

        MenuItem about = new MenuItem("About");
        help.getItems().add(about);
        about.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {

                AboutDialog dia = new AboutDialog();
                dia.show(JEConfig.getStage(), "About", "JEConfig", JEConfig.PROGRAMM_INFO, JEConfig.getImage("JEConfig_mac.png"));

            }
        });

        getMenus().addAll(menuFile, menuEdit, menuView, options, help);
    }
}
