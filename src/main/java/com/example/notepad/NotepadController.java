package com.example.notepad;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioMenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

import java.io.*;
import java.util.Optional;

public class NotepadController {

    @FXML
    private TextArea text_Area;
    @FXML
    private RadioMenuItem wrapButton;


    private boolean is_text_changed = false;
    private File file = null;

    public void initialize() {
        text_Area.textProperty().addListener((observable, oldValue, newValue) -> {
            is_text_changed = true;
        });
    }


    public void new_notepad() {
        if (is_text_changed) {
            promptSaveDialog();
        }
        text_Area.clear();
        is_text_changed = false;
        this.file = null;
    }

    public void save() {
        if (this.file == null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save File");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(new Stage());
            if (file != null) {
                saveTextToFile(file);
                this.file = file;
            }
        }
        else
            saveTextToFile(this.file);

    }

    public void open() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            readTextFromFile(file);
            this.file = file;
        }
    }

    public void exit() {
        if (is_text_changed) {
            promptSaveDialog();
        }
        System.exit(0);
    }

    public void undo() {
        text_Area.undo();
    }

    public void wrap() {
        text_Area.setWrapText(wrapButton.isSelected());
    }

    public void font() {
        // Font
    }

    public void about() {
        // About
    }

    private void saveTextToFile(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(text_Area.getText());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void readTextFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            text_Area.setText(content.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void promptSaveDialog() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Save Changes");
        alert.setHeaderText("You have unsaved changes. Do you want to save before proceeding?");

        ButtonType saveButton = new ButtonType("Save");
        ButtonType dontSaveButton = new ButtonType("Don't Save");

        alert.getButtonTypes().setAll(saveButton, dontSaveButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == saveButton) {
                save();
            } else if (result.get() == dontSaveButton) {
                // nothing
            }
        }
    }
}
