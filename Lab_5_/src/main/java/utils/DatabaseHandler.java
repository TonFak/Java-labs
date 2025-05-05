package utils;

import models.Task;
import models.Category;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private static final String DB_URL = "jdbc:sqlite:tasks.db";
    private Connection connection;

    public DatabaseHandler() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            createTables();
            insertDefaultCategories();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables() throws SQLException {
        String createCategoriesTable = "CREATE TABLE IF NOT EXISTS categories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL UNIQUE)";

        String createTasksTable = "CREATE TABLE IF NOT EXISTS tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "description TEXT," +
                "due_date TEXT," +
                "priority INTEGER," +
                "category_id INTEGER," +
                "FOREIGN KEY(category_id) REFERENCES categories(id))";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createCategoriesTable);
            stmt.execute(createTasksTable);
        }
    }

    private void insertDefaultCategories() throws SQLException {
        String[] defaultCategories = {"Работа", "Учеба", "Личное", "Семья"};

        for (String category : defaultCategories) {
            if (!categoryExists(category)) {
                String query = "INSERT INTO categories (name) VALUES (?)";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setString(1, category);
                    stmt.executeUpdate();
                }
            }
        }
    }

    private boolean categoryExists(String name) throws SQLException {
        String query = "SELECT 1 FROM categories WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    // Методы для работы с задачами
    public List<Task> getAllTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT t.id, t.title, t.description, t.due_date, t.priority, c.name as category " +
                "FROM tasks t LEFT JOIN categories c ON t.category_id = c.id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                LocalDate dueDate = rs.getString("due_date") != null ?
                        LocalDate.parse(rs.getString("due_date")) : null;

                tasks.add(new Task(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        dueDate,
                        rs.getInt("priority"),
                        rs.getString("category")
                ));
            }
        }
        return tasks;
    }

    public void addTask(Task task) throws SQLException {
        String query = "INSERT INTO tasks (title, description, due_date, priority, category_id) " +
                "VALUES (?, ?, ?, ?, (SELECT id FROM categories WHERE name = ?))";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setString(3, task.getDueDate() != null ? task.getDueDate().toString() : null);
            stmt.setInt(4, task.getPriority());
            stmt.setString(5, task.getCategory());
            stmt.executeUpdate();
        }
    }

    public void updateTask(Task task) throws SQLException {
        String query = "UPDATE tasks SET title = ?, description = ?, due_date = ?, " +
                "priority = ?, category_id = (SELECT id FROM categories WHERE name = ?) " +
                "WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setString(3, task.getDueDate() != null ? task.getDueDate().toString() : null);
            stmt.setInt(4, task.getPriority());
            stmt.setString(5, task.getCategory());
            stmt.setInt(6, task.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteTask(int id) throws SQLException {
        String query = "DELETE FROM tasks WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Методы для работы с категориями
    public List<String> getAllCategories() throws SQLException {
        List<String> categories = new ArrayList<>();
        String query = "SELECT name FROM categories";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                categories.add(rs.getString("name"));
            }
        }
        return categories;
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}