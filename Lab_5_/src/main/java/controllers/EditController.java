package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Task;
import utils.DatabaseHandler;

import java.sql.SQLException;
import java.time.LocalDate;

public class EditController {
    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private DatePicker dueDatePicker;
    @FXML private ComboBox<Integer> priorityCombo;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private Task task;
    private MainController mainController;
    private DatabaseHandler dbHandler;

    @FXML
    public void initialize() {
        dbHandler = new DatabaseHandler();

        // Заполнение комбобоксов
        priorityCombo.getItems().addAll(1, 2, 3, 4, 5);
        priorityCombo.setValue(3);

        try {
            categoryCombo.getItems().addAll(dbHandler.getAllCategories());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setTask(Task task) {
        this.task = task;
        if (task != null) {
            // Заполнение формы данными задачи
            titleField.setText(task.getTitle());
            descriptionArea.setText(task.getDescription());
            dueDatePicker.setValue(task.getDueDate());
            priorityCombo.setValue(task.getPriority());
            categoryCombo.setValue(task.getCategory());
        } else {
            // Значения по умолчанию для новой задачи
            dueDatePicker.setValue(LocalDate.now());
            categoryCombo.getSelectionModel().selectFirst();
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleSave() {
        if (validateInput()) {
            try {
                Task updatedTask = new Task(
                        task != null ? task.getId() : 0,
                        titleField.getText(),
                        descriptionArea.getText(),
                        dueDatePicker.getValue(),
                        priorityCombo.getValue(),
                        categoryCombo.getValue()
                );

                if (task == null) {
                    dbHandler.addTask(updatedTask);
                } else {
                    dbHandler.updateTask(updatedTask);
                }

                mainController.refreshData();
                closeWindow();
            } catch (SQLException e) {
                showAlert("Ошибка базы данных", "Не удалось сохранить задачу");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private boolean validateInput() {
        if (titleField.getText().trim().isEmpty()) {
            showAlert("Ошибка", "Заголовок не может быть пустым");
            return false;
        }

        if (categoryCombo.getValue() == null) {
            showAlert("Ошибка", "Выберите категорию");
            return false;
        }

        return true;
    }

    private void closeWindow() {
        titleField.getScene().getWindow().hide();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}