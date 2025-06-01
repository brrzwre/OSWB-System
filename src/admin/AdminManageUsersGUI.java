/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AdminManageUsersGUI extends JFrame {
    // Components
    private Admin admin;
    private JPanel panelSidebar, panelMain;
    private JLabel lblAdmin, lblTitle;
    private JTextField txtUserID, txtName, txtUsername, txtPassword;
    private JComboBox<String> cmbRole;
    private JTable tblUsers;
    private JButton btnAdd, btnEdit, btnDelete, btnClear, btnLogout;
    
    public AdminManageUsersGUI(Admin admin) {
    this(); // Calls the default constructor to build the layout
    this.admin = admin;
    loadUsersIntoTable();
    System.out.println("Welcome, " + admin.getName());
}

private void addUser() {
    String id = txtUserID.getText().trim();
    String name = txtName.getText().trim();
    String username = txtUsername.getText().trim();
    String password = txtPassword.getText().trim();
    String role = (String) cmbRole.getSelectedItem();


    // Validation
    if (id.isEmpty() || name.isEmpty() || username.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields.");
        return;
    }

    try (BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt", true))) {
        // Format: ID,Name,Username,Password,Role
        String line = id + "," + name + "," + username + "," + password + "," + role;
        bw.write(line);
        bw.newLine();
        bw.flush();
        JOptionPane.showMessageDialog(this, "User added successfully.");

        // Refresh table
        loadUsersIntoTable();
        clearForm();

    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error writing to users file.");
        e.printStackTrace();
    }
}
    
private void editUser() {
    int selectedRow = tblUsers.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a user to edit.");
        return;
    }

    String updatedId = txtUserID.getText().trim();
    String updatedName = txtName.getText().trim();
    String updatedUsername = txtUsername.getText().trim();
    String updatedPassword = txtPassword.getText().trim();
    String updatedRole = (String) cmbRole.getSelectedItem();

    if (updatedId.isEmpty() || updatedName.isEmpty() || updatedUsername.isEmpty() || updatedRole.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields.");
        return;
    }

    try {
        StringBuilder updatedContent;
        try ( // Read all lines
                BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            updatedContent = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5 && parts[0].equals(updatedId)) {
                    // If this is the selected user, replace it with new data
                    String newPassword = updatedPassword.isEmpty() ? parts[3] : updatedPassword; // Keep old password if none entered
                    line = updatedId + "," + updatedName + "," + updatedUsername + "," + newPassword + "," + updatedRole;
                }
                updatedContent.append(line).append("\n");
            }
        }

        try ( // Write the updated content back
                BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt"))) {
            bw.write(updatedContent.toString());
        }

        JOptionPane.showMessageDialog(this, "User updated successfully.");
        loadUsersIntoTable();
        clearForm();

    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error updating user.");
        e.printStackTrace();
    }
}

private void deleteUser() {
    int selectedRow = tblUsers.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a user to delete.");
        return;
    }

    String userIdToDelete = tblUsers.getValueAt(selectedRow, 0).toString();

    int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete user ID: " + userIdToDelete + "?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);

    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    try {

        StringBuilder updatedContent;
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            updatedContent = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5 && !parts[0].equals(userIdToDelete)) {
                    updatedContent.append(line).append("\n");
                }
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt"))) {
            bw.write(updatedContent.toString());
        }

        JOptionPane.showMessageDialog(this, "User deleted successfully.");
        loadUsersIntoTable();
        clearForm();

    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error deleting user.");
        e.printStackTrace();
    }
}


private void clearForm() {
    txtUserID.setText("");
    txtName.setText("");
    txtUsername.setText("");
    txtPassword.setText("");
    cmbRole.setSelectedIndex(0);
}
    

    public AdminManageUsersGUI() {
        setTitle("Admin - Manage Users");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        initSidebar();
        initMainPanel();
    }

private void loadUsersIntoTable() {
    DefaultTableModel model = (DefaultTableModel) tblUsers.getModel();
    model.setRowCount(0); // Clear existing rows

    try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 5) {
                String userId = parts[0];
                String name = parts[1];
                String username = parts[2];
                // Skip password for security
                String role = parts[4];

                model.addRow(new Object[]{userId, name, username, role});
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Failed to load users from file.");
        e.printStackTrace();
    }
}

    
    private void initSidebar() {
        panelSidebar = new JPanel();
        panelSidebar.setLayout(null);
        panelSidebar.setBounds(0, 0, 200, 500);
        panelSidebar.setBackground(new Color(204, 204, 255));

//        lblIcon = new JLabel();
//        lblIcon.setBounds(50, 20, 100, 100);
//
//        // Load and scale the image
//        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/5a54cfdb6320b05029b8fafb6fdb5f4e-removebg-preview.png"));
//        Image scaledImage = originalIcon.getImage().getScaledInstance(lblIcon.getWidth(), lblIcon.getHeight(), Image.SCALE_SMOOTH);
//        ImageIcon scaledIcon = new ImageIcon(scaledImage);
//
//        lblIcon.setIcon(scaledIcon);
//        panelSidebar.add(lblIcon);



        lblAdmin = new JLabel("Welcome, " + (admin != null ? admin.getName() : "Administrator"), SwingConstants.CENTER);
        lblAdmin.setBounds(25, 130, 150, 30);
        panelSidebar.add(lblAdmin);

        btnLogout = new JButton("Logout");
        btnLogout.setBounds(25, 400, 150, 30);
        panelSidebar.add(btnLogout);
        btnLogout.addActionListener(e -> {
    dispose(); // Close admin window
    new LoginFormGUI().setVisible(true); // Return to login screen
});

        add(panelSidebar);
    }


    private void initMainPanel() {
        panelMain = new JPanel();
        panelMain.setLayout(null);
        panelMain.setBounds(200, 0, 600, 500);

        lblTitle = new JLabel("User Management", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBounds(180, 10, 250, 30);
        panelMain.add(lblTitle);

        JLabel lblID = new JLabel("User ID:");
        lblID.setBounds(30, 60, 100, 25);
        txtUserID = new JTextField();
        txtUserID.setBounds(130, 60, 150, 25);
        panelMain.add(lblID);
        panelMain.add(txtUserID);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(30, 100, 100, 25);
        txtName = new JTextField();
        txtName.setBounds(130, 100, 150, 25);
        panelMain.add(lblName);
        panelMain.add(txtName);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(30, 140, 100, 25);
        txtUsername = new JTextField();
        txtUsername.setBounds(130, 140, 150, 25);
        panelMain.add(lblUsername);
        panelMain.add(txtUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(30, 180, 100, 25);
        txtPassword = new JTextField();
        txtPassword.setBounds(130, 180, 150, 25);
        panelMain.add(lblPassword);
        panelMain.add(txtPassword);

        JLabel lblRole = new JLabel("Role:");
        lblRole.setBounds(30, 220, 100, 25);
        cmbRole = new JComboBox<>(new String[] {"Admin", "SM", "PM", "IM", "FM"});
        cmbRole.setBounds(130, 220, 150, 25);
        panelMain.add(lblRole);
        panelMain.add(cmbRole);

        btnAdd = new JButton("Add");
        btnAdd.setBounds(30, 270, 70, 30);
        btnAdd.addActionListener(e -> addUser()); // ← Paste this here ✅
        panelMain.add(btnAdd);
        
        btnEdit = new JButton("Edit");
        btnEdit.setBounds(110, 270, 70, 30);
        btnEdit.addActionListener(e -> editUser());
        panelMain.add(btnEdit);
        
        btnDelete = new JButton("Delete");
        btnDelete.setBounds(190, 270, 90, 30);
        btnDelete.addActionListener(e -> deleteUser());
        panelMain.add(btnDelete);
        
        btnClear = new JButton("Clear");
        btnClear.setBounds(290, 270, 90, 30);
        btnClear.addActionListener(e -> clearForm()); // ← For the clear button too ✅
        panelMain.add(btnClear);

        tblUsers = new JTable(new DefaultTableModel(
            new Object[][] {},
            new String[] {"User ID", "Name", "Username", "Role"}
        ));
        JScrollPane scrollPane = new JScrollPane(tblUsers);
        scrollPane.setBounds(30, 310, 520, 130);
        panelMain.add(scrollPane);
        
tblUsers.getSelectionModel().addListSelectionListener(e -> {
    if (!e.getValueIsAdjusting() && tblUsers.getSelectedRow() != -1) {
        int row = tblUsers.getSelectedRow();
        txtUserID.setText(tblUsers.getValueAt(row, 0).toString());
        txtName.setText(tblUsers.getValueAt(row, 1).toString());
        txtUsername.setText(tblUsers.getValueAt(row, 2).toString());
        cmbRole.setSelectedItem(tblUsers.getValueAt(row, 3).toString());

        // Password is not shown for security reasons, you can leave it empty
        txtPassword.setText(""); // Admin can re-enter password if needed
    }
});

        add(panelMain);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminManageUsersGUI().setVisible(true));
    }
} 

