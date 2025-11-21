package JDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.StringJoiner;

public class JDBC_GUI extends JFrame {
    private Connection con;
    private Statement st;
    private PreparedStatement ps;

    public JDBC_GUI() {
        setTitle("JDBC Database Manager");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1, 10, 10));

        JButton createBtn = new JButton("Create Table");
        JButton insertBtn = new JButton("Insert Data");
        JButton updateBtn = new JButton("Update Data");
        JButton deleteBtn = new JButton("Delete Data");
        JButton selectBtn = new JButton("View Table Data");
        JButton exitBtn = new JButton("Exit");

        add(createBtn);
        add(insertBtn);
        add(updateBtn);
        add(deleteBtn);
        add(selectBtn);
        add(exitBtn);

        connectToDatabase();

        createBtn.addActionListener(e -> createTable());
        insertBtn.addActionListener(e -> insertData());
        updateBtn.addActionListener(e -> updateData());
        deleteBtn.addActionListener(e -> deleteData());
        selectBtn.addActionListener(e -> viewData());
        exitBtn.addActionListener(e -> {
            closeResources();
            System.exit(0);
        });

        setVisible(true);
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:sqlserver://karthik\\sqlexpress; databaseName=JDBC; encrypt=true; trustServerCertificate=true";
            String user = "sa";
            String password = "Karthik@123";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
        } catch (Exception e) {
            showError("Connection Failed: " + e.getMessage());
        }
    }

    private void createTable() {
        try {
            String tableName = JOptionPane.showInputDialog("Enter table name:");
            int columnCount = Integer.parseInt(JOptionPane.showInputDialog("Number of columns:"));

            StringBuilder columnDefs = new StringBuilder();
            for (int i = 0; i < columnCount; i++) {
                String colName = JOptionPane.showInputDialog("Column " + (i + 1) + " name:");
                String colType = JOptionPane.showInputDialog("Data type for " + colName + " (e.g., INT, VARCHAR(100)):");
                String isPrimary = JOptionPane.showInputDialog("Is " + colName + " PRIMARY KEY? (yes/no):");

                columnDefs.append(colName).append(" ").append(colType);
                if ("yes".equalsIgnoreCase(isPrimary)) {
                    columnDefs.append(" PRIMARY KEY");
                }
                if (i < columnCount - 1) columnDefs.append(", ");
            }

            String query = "IF OBJECT_ID('" + tableName + "', 'U') IS NULL BEGIN CREATE TABLE " + tableName + " (" + columnDefs + ") END";
            st.execute(query);
            showMessage("Table '" + tableName + "' created successfully.");
        } catch (Exception e) {
            showError("Error creating table: " + e.getMessage());
        }
    }

    private void insertData() {
        try {
            String tableName = JOptionPane.showInputDialog("Enter table name:");
            int columnCount = Integer.parseInt(JOptionPane.showInputDialog("Number of columns:"));

            String[] columns = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columns[i] = JOptionPane.showInputDialog("Column " + (i + 1) + " name:");
            }

            StringJoiner colJoiner = new StringJoiner(", ");
            StringJoiner placeholders = new StringJoiner(", ");
            for (String col : columns) {
                colJoiner.add(col);
                placeholders.add("?");
            }

            String query = "INSERT INTO " + tableName + " (" + colJoiner + ") VALUES (" + placeholders + ")";
            ps = con.prepareStatement(query);

            int rows = Integer.parseInt(JOptionPane.showInputDialog("Number of rows to insert:"));
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columnCount; j++) {
                    String value = JOptionPane.showInputDialog("Row " + (i + 1) + ", " + columns[j] + ":");
                    ps.setString(j + 1, value);
                }
                ps.executeUpdate();
            }
            showMessage("Inserted " + rows + " row(s) into " + tableName);
        } catch (Exception e) {
            showError("Error inserting data: " + e.getMessage());
        }
    }

    private void updateData() {
        try {
            String tableName = JOptionPane.showInputDialog("Enter table name:");
            String updateCol = JOptionPane.showInputDialog("Column to update:");
            String newValue = JOptionPane.showInputDialog("New value for " + updateCol + ":");
            String whereCol = JOptionPane.showInputDialog("Condition column:");
            String whereVal = JOptionPane.showInputDialog("Value for " + whereCol + ":");

            String query = "UPDATE " + tableName + " SET " + updateCol + " = ? WHERE " + whereCol + " = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, newValue);
            ps.setString(2, whereVal);
            int rows = ps.executeUpdate();
            showMessage("Updated " + rows + " row(s).");
        } catch (Exception e) {
            showError("Error updating data: " + e.getMessage());
        }
    }

    private void deleteData() {
        try {
            String tableName = JOptionPane.showInputDialog("Enter table name:");
            String whereCol = JOptionPane.showInputDialog("Condition column:");
            String whereVal = JOptionPane.showInputDialog("Value for " + whereCol + ":");

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete matching rows?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            String query = "DELETE FROM " + tableName + " WHERE " + whereCol + " = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, whereVal);
            int rows = ps.executeUpdate();
            showMessage("Deleted " + rows + " row(s).");
        } catch (Exception e) {
            showError("Error deleting data: " + e.getMessage());
        }
    }

    private void viewData() {
        try {
            String tableName = JOptionPane.showInputDialog("Enter table name:");
            String query = "SELECT * FROM " + tableName;
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();

            StringBuilder result = new StringBuilder();
            while (rs.next()) {
                for (int i = 1; i <= colCount; i++) {
                    result.append(rsmd.getColumnName(i)).append(": ").append(rs.getString(i)).append("  ");
                }
                result.append("\n");
            }

            JTextArea textArea = new JTextArea(result.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(450, 300));
            JOptionPane.showMessageDialog(this, scrollPane, "Table Data", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            showError("Error viewing data: " + e.getMessage());
        }
    }

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void closeResources() {
        try {
            if (ps != null) ps.close();
            if (st != null) st.close();
            if (con != null) con.close();
        } catch (Exception e) {
            showError("Error closing resources: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JDBC_GUI::new);
    }
}
