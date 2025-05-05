package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Task;
import utils.DatabaseHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MainController {
    @FXML private TableView<Task> tasksTable;
    @FXML private TableColumn<Task, String> titleColumn;
    @FXML private TableColumn<Task, String> descriptionColumn;
    @FXML private TableColumn<Task, String> dueDateColumn;
    @FXML private TableColumn<Task, Integer> priorityColumn;
    @FXML private TableColumn<Task, String> categoryColumn;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryFilter;
    @FXML private DatePicker dateFilter;

    private DatabaseHandler dbHandler;
    private ObservableList<Task> tasks = FXCollections.observableArrayList();
    private FilteredList<Task> filteredTasks;

    @FXML
    public void initialize() {
        dbHandler = new DatabaseHandler();

        // Настройка таблицы
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        // Загрузка данных
        loadTasks();
        loadCategories();

        // Настройка фильтрации
        filteredTasks = new FilteredList<>(tasks, p -> true);
        tasksTable.setItems(filteredTasks);

        // Обработчики изменений фильтров
        searchField.textProperty().addListener((obs, oldVal, newVal) -> updateFilters());
        categoryFilter.valueProperty().addListener((obs, oldVal, newVal) -> updateFilters());
        dateFilter.valueProperty().addListener((obs, oldVal, newVal) -> updateFilters());
    }

    private void loadTasks() {
        try {
            tasks.clear();
            tasks.addAll(dbHandler.getAllTasks());
        } catch (SQLException e) {
            showAlert("Ошибка базы данных", "Не удалось загрузить задачи");
            e.printStackTrace();
        }
    }

    private void loadCategories() {
        try {
            //  Сохраняем текущее выбранное значение
            String previouslySelected = categoryFilter.getValue();

            // Очищаем и заполняем ComboBox
            categoryFilter.getItems().clear();
            categoryFilter.getItems().add("Все категории");

            // Добавляем категории из БД с проверкой на null
            List<String> categories = dbHandler.getAllCategories();
            if (categories != null && !categories.isEmpty()) {
                categoryFilter.getItems().addAll(categories);
            }

            // Восстанавливаем выбор или устанавливаем по умолчанию
            if (previouslySelected != null && categoryFilter.getItems().contains(previouslySelected)) {
                categoryFilter.setValue(previouslySelected);
            } else {
                categoryFilter.setValue("Все категории");
            }

        } catch (SQLException e) {
            showAlert("Ошибка базы данных", "Не удалось загрузить категории");
            e.printStackTrace();

            // Минимальная инициализация при ошибке
            categoryFilter.getItems().clear();
            categoryFilter.getItems().add("Все категории");
            categoryFilter.setValue("Все категории");
        }
    }

    private void updateFilters() {
        filteredTasks.setPredicate(task -> {
            // Фильтр по тексту поиска
            if (!searchField.getText().isEmpty() &&
                    !task.getTitle().toLowerCase().contains(searchField.getText().toLowerCase()) &&
                    !task.getDescription().toLowerCase().contains(searchField.getText().toLowerCase())) {
                return false;
            }

            // Фильтр по категории
            String selectedCategory = categoryFilter.getValue();
            if (selectedCategory != null
                    && !selectedCategory.equals("Все категории")
                    && !selectedCategory.equals(task.getCategory())) {
                return false;
            }

            // Фильтр по дате
            if (dateFilter.getValue() != null &&
                    (task.getDueDate() == null || !task.getDueDate().equals(dateFilter.getValue()))) {
                return false;
            }

            return true;
        });
    }

    @FXML
    private void handleAdd() {
        showTaskDialog(null);
    }

    @FXML
    private void handleEdit() {
        Task selected = tasksTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showTaskDialog(selected);
        } else {
            showAlert("Ошибка", "Выберите задачу для редактирования");
        }
    }

    @FXML
    private void handleDelete() {
        Task selected = tasksTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                dbHandler.deleteTask(selected.getId());
                loadTasks();
            } catch (SQLException e) {
                showAlert("Ошибка базы данных", "Не удалось удалить задачу");
                e.printStackTrace();
            }
        } else {
            showAlert("Ошибка", "Выберите задачу для удаления");
        }
    }

    @FXML
    private void resetFilters() {
        searchField.clear();
        categoryFilter.setValue("Все категории");
        dateFilter.setValue(null);
    }

    private void showTaskDialog(Task task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/edit.fxml"));
            Parent root = loader.load();

            EditController controller = loader.getController();
            controller.setTask(task);
            controller.setMainController(this);

            Stage stage = new Stage();
            stage.setTitle(task == null ? "Добавить задачу" : "Редактировать задачу");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tasksTable.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить форму редактирования");
            e.printStackTrace();
        }
    }

    public void refreshData() {
        loadTasks();
        loadCategories();
    }

    @FXML
    private void handleExit() {
        dbHandler.close();
        System.exit(0);
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("О программе");
        alert.setHeaderText("Электронный ежедневник");
        alert.setContentText("Версия 1.0\nJavaFX приложение для управления задачами");
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}