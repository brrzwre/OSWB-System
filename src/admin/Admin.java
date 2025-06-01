/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package admin;

import model.Employee;

/**
 *
 * @author LENOVO
 */
public class Admin extends Employee {

    public Admin(String id, String name, String username, String password) {
        super(id, name, username, password, "Admin");
    }

    @Override
public void openDashboard() {
        new AdminManageUsersGUI(this).setVisible(true);
    }
}
