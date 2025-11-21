# JDBC GUI-Database-Manager
A Java Swing desktop application that connects to Microsoft SQL Server using JDBC and provides a simple graphical interface for managing database operations. This tool is designed for learning, prototyping, and small-scale database management tasks.

📖 Project Overview
This project demonstrates how to integrate Java Swing with JDBC to perform common database operations. The GUI allows users to:
- Create tables dynamically with custom column definitions
- Insert multiple rows of data securely using prepared statements
- Update existing records
- Delete records with confirmation
- View table contents in a scrollable text area

✨ Features
- Dynamic Table Creation: Define table name, columns, data types, and primary keys interactively.
- Secure Data Manipulation: Uses PreparedStatement to prevent SQL injection.
- CRUD Operations: Insert, update, delete, and view data directly from the GUI.
- Scrollable Results: Displays query results in a JTextArea with scroll support.
- Resource Management: Closes connections and statements gracefully on exit.

🛠️ Prerequisites
Before running the application, ensure you have:
- Java JDK 8+
- Microsoft SQL Server (with a running instance)
- SQL Server JDBC Driver (mssql-jdbc.jar)
- A database named JDBC (or update the connection string accordingly)

🚀 How to Run
- Clone the repository:
git clone https://github.com/your-username/jdbc-gui-database-manager.git
cd jdbc-gui-database-manager
- Compile the project:
javac -cp .;mssql-jdbc.jar JDBC/JDBC_GUI.java
- Run the application:
java -cp .;mssql-jdbc.jar JDBC.JDBC_GUI

📌 Usage Instructions
1. Create Table
- Click Create Table
- Enter table name, number of columns, column names, data types, and primary key(s)
- Example: Create a table Students with columns ID INT PRIMARY KEY, Name VARCHAR(100)
2. Insert Data
- Click Insert Data
- Enter table name and column names
- Provide values for each row
- Example: Insert ID=1, Name='Alice'
3. Update Data
- Click Update Data
- Choose column to update, new value, and condition column/value
- Example: Update Name='Bob' where ID=1
4. Delete Data
- Click Delete Data
- Provide condition column and value
- Confirm deletion
- Example: Delete rows where ID=1
5. View Data
- Click View Table Data
- Displays all rows in a scrollable text area

⚙️ Technologies Used
- Java Swing – GUI framework
- JDBC – Database connectivity
- Microsoft SQL Server – Relational database
- PreparedStatement – Secure SQL execution

📄 License
This project is licensed under the MIT License.
You are free to use, modify, and distribute this project with attribution.
