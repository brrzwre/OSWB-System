/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package finance;

import java.io.*;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import java.util.Set;
import java.util.HashSet;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;





public class FinanceFrame2 extends javax.swing.JFrame {

    private FinanceManager financeManager;
    
    public FinanceFrame2(FinanceManager financeManager) {
        initComponents();
        loadPOFromFile();
        loadPRFromFile();
        populateDateComboBox();
        setupTableSelectionListener();
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);
        loadUnpaidPOFromFile();
        loadPaidPOFromFile();
        setupUnpaidPOSelectionListener();
        txtPriceUnit.addActionListener(e -> updateCostField());
    }
    
    
    
    public void loadPOFromFile() {
        DefaultTableModel model = (DefaultTableModel) tablePO.getModel();
        model.setRowCount(0); 

        try (BufferedReader br = new BufferedReader(new FileReader("purchaseorder.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 9) {
                    model.addRow(new Object[]{
                        data[0].trim(),    // PO ID
                        data[1].trim(),    // PR ID
                        data[2].trim(),    // Item ID
                        data[3].trim(),    // Item Name
                        Integer.parseInt(data[4].trim()), // Quantity
                        data[6].trim(),    // Date
                        data[5].trim(),    // Supplier ID
                        data[8].trim()     // Status
                    });
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading purchase orders: " + e.getMessage());
        }
    }

    public void updatePOStatusInFile(String poIdToUpdate, String newStatus) {
        File inputFile = new File("purchaseorder.txt");
    File updatedMainFile = new File("purchaseorder_updated.txt");
    File tempFile = new File("purchaseorder_temp.txt");

    try (
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        PrintWriter writerMain = new PrintWriter(new FileWriter(updatedMainFile));
        PrintWriter writerTemp = new PrintWriter(new FileWriter(tempFile))
    ) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 9) {
                if (parts[0].equals(poIdToUpdate)) {
                    parts[8] = newStatus; // update status
                }

                // Write updated line to the main file
                writerMain.println(String.join(",", parts));

                // If status is Approved, calculate price & cost and write to temp
                if (parts[8].equalsIgnoreCase("Approved")) {
                    String itemId = parts[2].trim();
                    int quantity = Integer.parseInt(parts[4].trim());
                    // Use values from GUI tab 1 if matching PO ID
                        double price = 0.0;
                        double cost = 0.0;

                        if (poIdToUpdate.equals(parts[0].trim())) {
                            try {
                                price = Double.parseDouble(txtPriceUnit.getText().trim());
                                cost = Double.parseDouble(textFieldCost.getText().trim());
                            } catch (NumberFormatException e) {
                                price = 0.0;
                                cost = 0.0;
                            }
                        }

                    writerTemp.println(String.join(",", 
                        parts[0].trim(), // PO ID
                        parts[1].trim(), // PR ID
                        parts[2].trim(), // Item ID
                        parts[3].trim(), // Item Name
                        parts[4].trim(), // Quantity
                        parts[5].trim(), // Supplier ID
                        parts[6].trim(), // Date
                        parts[7].trim(), // SM ID
                        parts[8].trim(), // Status
                        String.format("%.2f", price), // Price Unit
                        String.format("%.2f", cost)   // Cost
                    ));
                }
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error updating PO status: " + e.getMessage());
        return;
    }

    // Replace the original file
    if (!inputFile.delete()) {
        JOptionPane.showMessageDialog(this, "Could not delete original purchaseorder.txt");
        return;
    }
    if (!updatedMainFile.renameTo(inputFile)) {
        JOptionPane.showMessageDialog(this, "Could not rename updated file to purchaseorder.txt");
    }
    }

 
    public void saveTableToFile() {
        DefaultTableModel model = (DefaultTableModel) tablePO.getModel();
        try (PrintWriter pw = new PrintWriter(new FileWriter("purchaseorder.txt"))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                String line = String.join(",",
                    model.getValueAt(i, 0).toString(),
                    model.getValueAt(i, 1).toString(),
                    model.getValueAt(i, 2).toString(),
                    model.getValueAt(i, 3).toString(),
                    model.getValueAt(i, 4).toString(),
                    model.getValueAt(i, 6).toString(),
                    model.getValueAt(i, 5).toString(),
                    "SM001",
                    model.getValueAt(i, 7).toString()
                );
                pw.println(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving purchase orders: " + e.getMessage());
        }
    }


    private void setupTableSelectionListener() {
        tablePO.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tablePO.getSelectionModel().addListSelectionListener(event -> {
        if (!event.getValueIsAdjusting()) {
            int selectedRow = tablePO.getSelectedRow();
            if (selectedRow >= 0) {
                String poID = tablePO.getValueAt(selectedRow, 0).toString();
                String prID = tablePO.getValueAt(selectedRow, 1).toString();
                String itemID = tablePO.getValueAt(selectedRow, 2).toString(); // use for salesData lookup
                String itemName = tablePO.getValueAt(selectedRow, 3).toString();
                String supplier = tablePO.getValueAt(selectedRow, 6).toString();
                int quantity = Integer.parseInt(tablePO.getValueAt(selectedRow, 4).toString());

                textFieldPOID.setText(poID);
                textFieldPRID.setText(prID);
                textFieldItem.setText(itemName);
                textFieldSupplier.setText(supplier);
                textFieldQuantity.setText(String.valueOf(quantity));

                Double priceUnit = findPriceUnitFromSalesData(itemID);
                if (priceUnit != null) {
                    txtPriceUnit.setText(String.format("%.2f", priceUnit));
                    double cost = priceUnit * quantity;
                    textFieldCost.setText(String.format("%.2f", cost));
                } else {
                    txtPriceUnit.setText(""); // allow manual input
                    textFieldCost.setText(""); // wait for manual price to be filled
                }
            }
        }
    });
    }
    
    private Double findPriceUnitFromSalesData(String itemID) {
    try (BufferedReader br = new BufferedReader(new FileReader("salesData.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 4 && parts[1].trim().equals(itemID)) {
                return Double.parseDouble(parts[3].trim()); // price per unit
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error reading salesData.txt: " + e.getMessage());
    }
    return null; // not 
    }   

    public void searchPO(String keyword) {
        DefaultTableModel model = (DefaultTableModel) tablePO.getModel();
        model.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader("purchaseorder.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.toLowerCase().contains(keyword.toLowerCase())) {
                    String[] data = line.split(",");
                    if (data.length >= 9) {
                        model.addRow(new Object[]{
                            data[0].trim(),
                            data[1].trim(),
                            data[2].trim(),
                            data[3].trim(),
                            Integer.parseInt(data[4].trim()),
                            data[6].trim(),
                            data[5].trim(),
                            data[8].trim()
                        });
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error searching purchase orders: " + e.getMessage());
        }
    }
    
    private void updateCostField() {
    try {
        double price = Double.parseDouble(txtPriceUnit.getText().trim());
        int quantity = Integer.parseInt(textFieldQuantity.getText().trim());
        double total = price * quantity;
        textFieldCost.setText(String.format("%.2f", total));
    } catch (NumberFormatException e) {
        textFieldCost.setText("Invalid");
    }
}


    public void filterByStatus(String status) {
    DefaultTableModel model = (DefaultTableModel) tablePO.getModel();
    model.setRowCount(0);

    try (BufferedReader br = new BufferedReader(new FileReader("purchaseorder.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length >= 9 && data[8].trim().equalsIgnoreCase(status)) {
                model.addRow(new Object[]{
                    data[0].trim(),
                    data[1].trim(),
                    data[2].trim(),
                    data[3].trim(),
                    Integer.parseInt(data[4].trim()),
                    data[6].trim(),
                    data[5].trim(),
                    data[8].trim()
                });
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error filtering purchase orders: " + e.getMessage());
    }
}
    
//PURCHASE REQUISITION TAB 2
    
public void loadPRFromFile() {
    DefaultTableModel model = (DefaultTableModel) tblPR.getModel();
    model.setRowCount(0);  // Clear existing rows

    try (BufferedReader br = new BufferedReader(new FileReader("purchaserequisition.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                String[] data = line.split(",");
                if (data.length >= 7) {
                    model.addRow(new Object[]{
                        data[0].trim(),
                        data[1].trim(),
                        data[2].trim(),
                        data[3].trim(),
                        data[4].trim(),
                        data[5].trim(),
                        data[6].trim()
                    });
                }
                
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error loading purchase requisitions: " + e.getMessage());
    }
}


    public void searchPR(String keyword) {
        DefaultTableModel model = (DefaultTableModel) tblPR.getModel();
        model.setRowCount(0);

        try (BufferedReader br = new BufferedReader(new FileReader("purchaserequisition.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.toLowerCase().contains(keyword.toLowerCase())) {
                    String[] data = line.split(",");
                    if (data.length >= 7) {
                        model.addRow(new Object[]{
                            data[0].trim(),
                            data[1].trim(),
                            data[2].trim(),
                            data[3].trim(),
                            data[4].trim(),
                            data[5].trim(),
                            data[6].trim()
                        });
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error searching purchase requisitions: " + e.getMessage());
        }
    }
    
//    UNPAID PO PAYMENT PAGE TAB 3
    public void loadUnpaidPOFromFile() {
    DefaultTableModel model = (DefaultTableModel) tableUnpaidPO.getModel();
    model.setRowCount(0);

    try (BufferedReader br = new BufferedReader(new FileReader("purchaseorder_temp.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length >= 11) {
                model.addRow(new Object[] {
                    data[0].trim(), // PO ID
                    data[1].trim(), // PR ID
                    data[2].trim(), // Item ID
                    data[3].trim(), // Item Name
                    Integer.parseInt(data[4].trim()), // Quantity
                    data[5].trim(), // Supplier ID
                    data[6].trim(), // Date
                    data[7].trim(), // SM ID
                    data[8].trim(), // Status
                    data[9].trim(), // Price Unit
                    data[10].trim() // Cost
                });
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error loading unpaid purchase orders: " + e.getMessage());
    }
}
    
    
    public void loadPaidPOFromFile() {
    DefaultTableModel model = (DefaultTableModel) tablePaidPO.getModel();
    model.setRowCount(0);

    try (BufferedReader br = new BufferedReader(new FileReader("purchaseorderpaid.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length >= 11) {
                model.addRow(new Object[]{
                    data[0].trim(), // PO ID
                    data[1].trim(), // PR ID
                    data[2].trim(), // Item ID
                    data[3].trim(), // Item Name
                    Integer.parseInt(data[4].trim()), // Quantity
                    data[5].trim(), // Supplier ID
                    data[6].trim(), // Date
                    data[7].trim(), // SM ID
                    data[8].trim(), // Status
                    data[9].trim(), // Price Unit
                    data[10].trim() // Cost
                });
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error loading paid purchase orders: " + e.getMessage());
    }
    }
    

private void setupUnpaidPOSelectionListener() {
    tableUnpaidPO.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tableUnpaidPO.getSelectionModel().addListSelectionListener(event -> {
        if (!event.getValueIsAdjusting()) {
            int selectedRow = tableUnpaidPO.getSelectedRow();
            if (selectedRow >= 0) {
                textFieldupaidPOID1.setText(tableUnpaidPO.getValueAt(selectedRow, 0).toString());
                textFieldPRID1.setText(tableUnpaidPO.getValueAt(selectedRow, 1).toString());
                textFieldItem1.setText(tableUnpaidPO.getValueAt(selectedRow, 3).toString());
                textFieldSupplier1.setText(tableUnpaidPO.getValueAt(selectedRow, 5).toString());
                textFieldQuantity1.setText(tableUnpaidPO.getValueAt(selectedRow, 4).toString());

                txtPriceUnit1.setText(tableUnpaidPO.getValueAt(selectedRow, 9).toString());
                textFieldCost1.setText(tableUnpaidPO.getValueAt(selectedRow, 10).toString());
        
            }
        }
    });
}

    private void populateDateComboBox() {
    Set<String> uniqueDates = new HashSet<>();
    try (BufferedReader br = new BufferedReader(new FileReader("purchaseorderpaid.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length >= 9) {
                uniqueDates.add(data[6].trim());  // Date is at index 6
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error reading dates: " + e.getMessage());
    }

    for (String date : uniqueDates) {
        jComboBoxDate.addItem(date); // Replace jComboBoxDate with your actual combo box variable
    }
}

    
    
   

    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabPane = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablePO = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        textFieldPOID = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        textFieldPRID = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        textFieldItem = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        textFieldSupplier = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        textFieldQuantity = new javax.swing.JTextField();
        textFieldCost = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        buttonReject = new javax.swing.JButton();
        buttonApprove = new javax.swing.JButton();
        txtPriceUnit = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton11 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        txtPR = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPR = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableUnpaidPO = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        textFieldupaidPOID1 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        textFieldPRID1 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        textFieldItem1 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        textFieldSupplier1 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        textFieldQuantity1 = new javax.swing.JTextField();
        textFieldCost1 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        buttonApprove1 = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        txtPriceUnit1 = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        tablePaidPO = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jButton10 = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jComboBoxDate = new javax.swing.JComboBox<>();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel7 = new javax.swing.JPanel();
        btnItemEntry = new javax.swing.JButton();
        btnSupplierEntry = new javax.swing.JButton();
        btnSalesEntry = new javax.swing.JButton();
        btnPurchaseReq = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tabPane.setBackground(new java.awt.Color(51, 153, 255));
        tabPane.setTabPlacement(javax.swing.JTabbedPane.RIGHT);
        tabPane.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablePO.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "PO ID", "PR ID", "Item ID", "Item Name", "Quantity", "Date ", "ID", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablePO.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tablePO.setCellSelectionEnabled(true);
        tablePO.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tablePO);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 530, 430));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("Purchase Order");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, -1, -1));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("PO Details");

        jLabel4.setText("PO ID:");

        textFieldPOID.setEditable(false);

        jLabel5.setText("PR ID: ");

        textFieldPRID.setEditable(false);

        jLabel6.setText("Item:");

        textFieldItem.setEditable(false);

        jLabel7.setText("Supplier: ");

        jLabel8.setText("Quantity:");

        textFieldQuantity.setEditable(false);

        textFieldCost.setEditable(false);

        jLabel9.setText("Cost: ");

        buttonReject.setText("Reject");
        buttonReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRejectActionPerformed(evt);
            }
        });

        buttonApprove.setText("Approve");
        buttonApprove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonApproveActionPerformed(evt);
            }
        });

        txtPriceUnit.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPriceUnitFocusLost(evt);
            }
        });

        jLabel13.setText("Price Unit:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textFieldSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldItem, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textFieldPRID, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldPOID, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(buttonApprove)
                        .addGap(18, 18, 18)
                        .addComponent(buttonReject))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addComponent(jLabel9)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                            .addComponent(textFieldCost, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel8)
                                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtPriceUnit)
                                .addComponent(textFieldQuantity, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)))))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel3)
                .addGap(23, 23, 23)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(textFieldPOID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(textFieldPRID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(textFieldItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(textFieldSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(textFieldQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPriceUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(33, 33, 33)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonApprove)
                    .addComponent(buttonReject))
                .addGap(47, 47, 47))
        );

        jPanel2.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 70, 230, 450));

        jLabel24.setText("Filter");
        jPanel2.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 70, -1, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "Pending", "Approved", "Rejected" }));
        jComboBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel2.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 70, 110, -1));

        jButton11.setText("Search");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 70, 70, -1));
        jPanel2.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, 110, -1));

        jLabel26.setText("Search:");
        jPanel2.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, -1));

        tabPane.addTab("tab 1", jPanel2);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel10.setText("Purchase Requisition");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

        jLabel27.setText("Search: ");
        jPanel1.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 69, -1, -1));

        txtPR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPRActionPerformed(evt);
            }
        });
        jPanel1.add(txtPR, new org.netbeans.lib.awtextra.AbsoluteConstraints(77, 66, 122, -1));

        jButton12.setText("Search");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(211, 66, -1, -1));

        tblPR.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7"
            }
        ));
        jScrollPane1.setViewportView(tblPR);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 800, 400));

        tabPane.addTab("tab 2", jPanel1);

        jPanel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tableUnpaidPO.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "PO ID", "PR ID", "ITEM ID", "ITEM NAME ", "QTY", "SUPPLIER", "DATE", "SM ID", "STATUS", "PRICE UNIT", "COST"
            }
        ));
        jScrollPane4.setViewportView(tableUnpaidPO);

        jPanel4.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 540, 190));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel11.setText("Payment");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, -1, -1));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel16.setText("PO Details");

        jLabel17.setText("PO ID:");

        textFieldupaidPOID1.setEditable(false);

        jLabel18.setText("PR ID: ");

        textFieldPRID1.setEditable(false);

        jLabel19.setText("Item:");

        textFieldItem1.setEditable(false);

        jLabel20.setText("Supplier: ");

        jLabel21.setText("Quantity:");

        textFieldQuantity1.setEditable(false);

        textFieldCost1.setEditable(false);

        jLabel22.setText("Cost: ");

        buttonApprove1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        buttonApprove1.setText("Pay");
        buttonApprove1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        buttonApprove1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonApprove1ActionPerformed(evt);
            }
        });

        jLabel28.setText("Price Unit:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textFieldQuantity1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textFieldSupplier1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldItem1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textFieldPRID1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldupaidPOID1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                            .addComponent(jLabel28)
                            .addGap(18, 18, 18)
                            .addComponent(txtPriceUnit1))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                            .addComponent(jLabel22)
                            .addGap(30, 30, 30)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(textFieldCost1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(buttonApprove1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel16)
                .addGap(23, 23, 23)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(textFieldupaidPOID1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(textFieldPRID1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(textFieldItem1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(textFieldSupplier1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(textFieldQuantity1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(txtPriceUnit1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldCost1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addGap(18, 18, 18)
                .addComponent(buttonApprove1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(71, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 70, 230, 470));

        tablePaidPO.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "PO ID", "PR ID", "ITEM ID", "QTY", "SUPPLIER", "DATE", "SM ID", "STATUS", "PRICE UNIT", "COST"
            }
        ));
        jScrollPane6.setViewportView(tablePaidPO);

        jPanel4.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 320, 540, 220));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setText("Paid PO");
        jPanel4.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, -1, -1));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel23.setText("Unpaid PO");
        jPanel4.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, -1, -1));

        tabPane.addTab("tab 3", jPanel4);

        jButton10.setText("Generate Report");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel25.setText("Date: ");

        jButton13.setText("Export Report");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel14.setText("Generate Report");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane5.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(586, 586, 586)
                        .addComponent(jButton13)
                        .addGap(18, 18, 18)
                        .addComponent(jButton10))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBoxDate, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel14))))
                .addContainerGap(38, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 790, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jComboBoxDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton10)
                    .addComponent(jButton13))
                .addGap(24, 24, 24))
        );

        tabPane.addTab("tab 4", jPanel5);

        jPanel7.setBackground(new java.awt.Color(214, 225, 248));

        btnItemEntry.setText("Purchase Order");
        btnItemEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemEntryActionPerformed(evt);
            }
        });

        btnSupplierEntry.setText("Purchase Requisition");
        btnSupplierEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupplierEntryActionPerformed(evt);
            }
        });

        btnSalesEntry.setText("Payment");
        btnSalesEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalesEntryActionPerformed(evt);
            }
        });

        btnPurchaseReq.setText("Generate Report");
        btnPurchaseReq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseReqActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel1.setText("Finance Manager!");

        btnLogout.setBackground(new java.awt.Color(51, 51, 255));
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setText("Logout");
        btnLogout.setToolTipText("");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel29.setText("Welcome,");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnSupplierEntry, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
            .addComponent(btnItemEntry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnPurchaseReq, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLogout)
                .addGap(49, 49, 49))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(btnSalesEntry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(118, 118, 118)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(btnItemEntry)
                .addGap(18, 18, 18)
                .addComponent(btnSupplierEntry)
                .addGap(18, 18, 18)
                .addComponent(btnSalesEntry)
                .addGap(18, 18, 18)
                .addComponent(btnPurchaseReq)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 222, Short.MAX_VALUE)
                .addComponent(btnLogout)
                .addGap(22, 22, 22))
        );

        jButton6.setText("Refresh");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel12.setText("Finance Management");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(173, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 909, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(160, 160, 160)
                        .addComponent(jButton6)))
                .addGap(35, 35, 35))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 944, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton6)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(tabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 569, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        loadPOFromFile();
        loadPRFromFile();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void btnItemEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemEntryActionPerformed
        tabPane.setSelectedIndex(0);
    }//GEN-LAST:event_btnItemEntryActionPerformed

    private void btnSupplierEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupplierEntryActionPerformed
        tabPane.setSelectedIndex(1);
        loadPRFromFile();
    }//GEN-LAST:event_btnSupplierEntryActionPerformed

    private void btnSalesEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalesEntryActionPerformed
        tabPane.setSelectedIndex(2);
        loadUnpaidPOFromFile();
        loadPaidPOFromFile();
    }//GEN-LAST:event_btnSalesEntryActionPerformed

    private void btnPurchaseReqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseReqActionPerformed
        tabPane.setSelectedIndex(3);
    }//GEN-LAST:event_btnPurchaseReqActionPerformed

    private void buttonApprove1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonApprove1ActionPerformed
        int selectedRow = tableUnpaidPO.getSelectedRow();
    if (selectedRow >= 0) {
        DefaultTableModel unpaidModel = (DefaultTableModel) tableUnpaidPO.getModel();
        DefaultTableModel paidModel = (DefaultTableModel) tablePaidPO.getModel();

        String[] rowData = new String[11];
        for (int i = 0; i < 9; i++) {
            rowData[i] = unpaidModel.getValueAt(selectedRow, i).toString();
        }
        rowData[8] = "Paid"; // status
        rowData[9] = txtPriceUnit1.getText().trim();  // PRICE UNIT
        rowData[10] = textFieldCost1.getText().trim(); // COST

        // ✅ Append to paid model
        paidModel.addRow(rowData);

        // ✅ Append to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("purchaseorderpaid.txt", true))) {
            bw.write(String.join(",", rowData));
            bw.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving paid PO: " + e.getMessage());
        }

        unpaidModel.removeRow(selectedRow);
        textFieldupaidPOID1.setText("");
        textFieldPRID1.setText("");
        textFieldItem1.setText("");
        textFieldSupplier1.setText("");
        textFieldQuantity1.setText("");
        txtPriceUnit1.setText("");
        textFieldCost1.setText("");

        JOptionPane.showMessageDialog(this, "PO has been marked as Paid.");
    } else {
        JOptionPane.showMessageDialog(this, "Please select a PO to pay.");
    }
    }//GEN-LAST:event_buttonApprove1ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        String text = txtPR.getText();
        searchPR(text);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void txtPRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPRActionPerformed

    }//GEN-LAST:event_txtPRActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        String keyword = jTextField1.getText().trim();
        if (keyword.isEmpty()) {
            loadPOFromFile();
        } else {
            searchPO(keyword);
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        String selectedStatus = jComboBox1.getSelectedItem().toString();

        if (selectedStatus.equals("-")) {
            // Show all data
            loadPOFromFile();
        } else {
            filterByStatus(selectedStatus);
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void buttonApproveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonApproveActionPerformed
        int selectedRow = tablePO.getSelectedRow();
        if (selectedRow >= 0) {
            String poId = tablePO.getValueAt(selectedRow, 0).toString();
            tablePO.setValueAt("Approved", selectedRow, 7);
            updatePOStatusInFile(poId, "Approved");
            JOptionPane.showMessageDialog(this, "Purchase order approved.");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a purchase order to approve.");
        }
    }//GEN-LAST:event_buttonApproveActionPerformed

    private void buttonRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRejectActionPerformed
        int selectedRow = tablePO.getSelectedRow();
        if (selectedRow >= 0) {
            String poId = tablePO.getValueAt(selectedRow, 0).toString();
            tablePO.setValueAt("Rejected", selectedRow, 7);
            updatePOStatusInFile(poId, "Rejected");
            JOptionPane.showMessageDialog(this, "Purchase order rejected.");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a purchase order to reject.");
        }
    }//GEN-LAST:event_buttonRejectActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        String selectedDate = jComboBoxDate.getSelectedItem().toString();
    StringBuilder report = new StringBuilder();
    double total = 0.0;

    try (BufferedReader br = new BufferedReader(new FileReader("purchaseorderpaid.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length >= 11 && data[6].trim().equals(selectedDate)) {
                String poId = data[0].trim();
                String itemName = data[3].trim();
                String quantity = data[4].trim();
                String supplier = data[5].trim();
                String cost = data[10].trim(); // Column 11 = cost

                report.append("PO ID: ").append(poId)
                      .append(", Item: ").append(itemName)
                      .append(", Quantity: ").append(quantity)
                      .append(", Supplier: ").append(supplier)
                      .append(", Cost: RM ").append(cost).append("\n");

                try {
                    total += Double.parseDouble(cost);
                } catch (NumberFormatException ex) {
                    // Skip invalid cost
                }
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage());
        return;
    }

    report.append("\nTotal Payment on ").append(selectedDate)
          .append(": RM ").append(String.format("%.2f", total));
    
    jTextArea1.setText(report.toString());
    }//GEN-LAST:event_jButton10ActionPerformed

    private void txtPriceUnitFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPriceUnitFocusLost
        updateCostField();

    }//GEN-LAST:event_txtPriceUnitFocusLost

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
         String reportText = jTextArea1.getText();
    if (reportText.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No report to export.");
        return;
    }

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save Report as PDF");
    fileChooser.setSelectedFile(new File("FinanceReport.pdf"));
    int userSelection = fileChooser.showSaveDialog(this);

    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToSave = fileChooser.getSelectedFile();

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA, 12);

            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float y = yStart;
            float leading = 15;

            contentStream.beginText();
            contentStream.newLineAtOffset(margin, y);

            String[] lines = reportText.split("\n");
            for (String line : lines) {
                if (y <= margin) {
                    contentStream.endText();
                    contentStream.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    y = yStart;
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, y);
                }
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -leading);
                y -= leading;
            }

            contentStream.endText();
            contentStream.close();
            document.save(fileToSave);

            JOptionPane.showMessageDialog(this, "Report exported to PDF successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error exporting PDF: " + e.getMessage());
        }
    }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout Finance Manager dashboard?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            this.dispose(); // Close the current dashboard
            new admin.LoginFormGUI().setVisible(true); // Return to login screen
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FinanceManager fm = new FinanceManager("FM001", "Firas", "firas", "password123");
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FinanceFrame2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FinanceFrame2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FinanceFrame2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FinanceFrame2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FinanceFrame2(fm).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnItemEntry;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPurchaseReq;
    private javax.swing.JButton btnSalesEntry;
    private javax.swing.JButton btnSupplierEntry;
    private javax.swing.JButton buttonApprove;
    private javax.swing.JButton buttonApprove1;
    private javax.swing.JButton buttonReject;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBoxDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JTable tablePO;
    private javax.swing.JTable tablePaidPO;
    private javax.swing.JTable tableUnpaidPO;
    private javax.swing.JTable tblPR;
    private javax.swing.JTextField textFieldCost;
    private javax.swing.JTextField textFieldCost1;
    private javax.swing.JTextField textFieldItem;
    private javax.swing.JTextField textFieldItem1;
    private javax.swing.JTextField textFieldPOID;
    private javax.swing.JTextField textFieldPRID;
    private javax.swing.JTextField textFieldPRID1;
    private javax.swing.JTextField textFieldQuantity;
    private javax.swing.JTextField textFieldQuantity1;
    private javax.swing.JTextField textFieldSupplier;
    private javax.swing.JTextField textFieldSupplier1;
    private javax.swing.JTextField textFieldupaidPOID1;
    private javax.swing.JTextField txtPR;
    private javax.swing.JTextField txtPriceUnit;
    private javax.swing.JTextField txtPriceUnit1;
    // End of variables declaration//GEN-END:variables
}
