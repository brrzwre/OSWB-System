/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package purchase;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
/**
 *
 * @author User
 */
public class GeneratePOPanel extends javax.swing.JPanel {

    /**
     * Creates new form GeneratePOPanel
     */
    private PurchaseManager purchaseManager;
    public GeneratePOPanel(PurchaseManager purchaseManager) {
        this.purchaseManager = purchaseManager;
        initComponents();
        loadPendingPRs();
        loadAllSupplierIDs();
    }
    
    private void loadPendingPRs() {
        cmbPRID.removeAllItems();
        try (BufferedReader br = new BufferedReader(new FileReader("purchaserequisition.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7 && parts[6].equalsIgnoreCase("Pending")) {
                    cmbPRID.addItem(parts[0]); 
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading PRs.");
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

        cmbPRID = new javax.swing.JComboBox<>();
        txtItemCode = new javax.swing.JTextField();
        txtItemName = new javax.swing.JTextField();
        txtQuantity = new javax.swing.JTextField();
        txtRequestedBy = new javax.swing.JTextField();
        btnGeneratePO = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtDate = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cmbSupplierID = new javax.swing.JComboBox<>();
        btnDeclinePR = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(204, 204, 204));

        cmbPRID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-" }));
        cmbPRID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPRIDActionPerformed(evt);
            }
        });

        txtItemCode.setEditable(false);
        txtItemCode.setBackground(new java.awt.Color(255, 255, 255));
        txtItemCode.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        txtItemCode.setText("Item Code");

        txtItemName.setEditable(false);
        txtItemName.setBackground(new java.awt.Color(255, 255, 255));
        txtItemName.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        txtItemName.setText("Item Name");

        txtQuantity.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        txtQuantity.setText("Quantity");

        txtRequestedBy.setEditable(false);
        txtRequestedBy.setBackground(new java.awt.Color(255, 255, 255));
        txtRequestedBy.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        txtRequestedBy.setText("Requested By");

        btnGeneratePO.setBackground(new java.awt.Color(0, 204, 51));
        btnGeneratePO.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGeneratePO.setText("Generate Purchase Order");
        btnGeneratePO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGeneratePOActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Purchase Requisition ID");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Item Code");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Item Name");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Item Quantity");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Requested By");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Date Requested");

        txtDate.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        txtDate.setText("Date Requested");
        txtDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDateActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Supplier ID");

        btnDeclinePR.setBackground(new java.awt.Color(255, 51, 51));
        btnDeclinePR.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDeclinePR.setText("Decline Purchase Requisition");
        btnDeclinePR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeclinePRActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel8.setText("Purchase Order Creation");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(234, 234, 234)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtItemName)
                            .addComponent(txtQuantity)
                            .addComponent(txtRequestedBy)
                            .addComponent(txtDate)
                            .addComponent(cmbSupplierID, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)
                        .addGap(72, 72, 72))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(btnDeclinePR)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGeneratePO))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(cmbPRID, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(255, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbPRID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtRequestedBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cmbSupplierID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGeneratePO)
                    .addComponent(btnDeclinePR))
                .addContainerGap(53, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private boolean isDuplicateItemInPending(String itemCode, String currentPRID) {
    try (BufferedReader br = new BufferedReader(new FileReader("purchaserequisition.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 7 && !parts[0].equals(currentPRID) && parts[1].equals(itemCode) && parts[6].equalsIgnoreCase("Pending")) {
                return true;
            }
        }
    } catch (IOException e) {
    }

    try (BufferedReader br = new BufferedReader(new FileReader("purchaseorder.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 9 && parts[2].equals(itemCode) && parts[8].equalsIgnoreCase("Pending")) {
                return true;
            }
        }
    } catch (IOException e) {
    }

    return false;
}

    
    private void loadAllSupplierIDs() {
        cmbSupplierID.removeAllItems();
        try (BufferedReader br = new BufferedReader(new FileReader("supplier.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1) {
                    cmbSupplierID.addItem(parts[0]);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading suppliers.");
        }
    }
    
    private String getSupplierIDForItem(String itemCode) {
        try (BufferedReader br = new BufferedReader(new FileReader("items.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[0].equals(itemCode)) {
                    return parts[2];
                }
            }
        } catch (IOException e) {
        }
        return null;
    }
    
    private void cmbPRIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPRIDActionPerformed
        String selectedPR = (String) cmbPRID.getSelectedItem();
        if (selectedPR == null) return;

        try (BufferedReader br = new BufferedReader(new FileReader("purchaserequisition.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(selectedPR)) {
                    txtItemCode.setText(parts[1]);
                        if (isDuplicateItemInPending(parts[1], parts[0])) {
                            JOptionPane.showMessageDialog(this, 
                            "⚠ Warning: This item is already requested or ordered in another pending PR/PO.", 
                            "Duplicate Item Detected", 
                            JOptionPane.WARNING_MESSAGE);
                        }
                    txtItemName.setText(parts[2]);
                    txtQuantity.setText(parts[3]);
                    txtDate.setText(parts[4]);
                    txtRequestedBy.setText(parts[5]);

                    String matchedSupplier = getSupplierIDForItem(parts[1]);
                    if (matchedSupplier != null) {
                        cmbSupplierID.setSelectedItem(matchedSupplier);
                        cmbSupplierID.setEnabled(false);
                    } else {
                        cmbSupplierID.setSelectedIndex(-1);
                        cmbSupplierID.setEnabled(true);
                    }
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading PR details.");
        }
    }//GEN-LAST:event_cmbPRIDActionPerformed

    private void btnGeneratePOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGeneratePOActionPerformed
        String poID = generateNextPOID();
        String prID = (String) cmbPRID.getSelectedItem();
        String itemCode = txtItemCode.getText();
        String itemName = txtItemName.getText();
        String quantity = txtQuantity.getText();
        String supplierID = (String) cmbSupplierID.getSelectedItem();
        String dateRequested = txtDate.getText();
        String requestedBy = txtRequestedBy.getText();
        String status = "Pending";
        String approvedBy = "";
        String line = String.join(",", poID, prID, itemCode, itemName, quantity, supplierID, dateRequested, requestedBy, status, approvedBy);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("purchaseorder.txt", true))) {
            bw.write(line);
            bw.newLine();
            updatePRStatus(prID, "Accepted"); 
            JOptionPane.showMessageDialog(this, "Purchase Order " + poID + " generated and PR marked as Accepted.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing PO to file.");
        }
    }//GEN-LAST:event_btnGeneratePOActionPerformed

    private void btnDeclinePRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeclinePRActionPerformed
        String prID = (String) cmbPRID.getSelectedItem();
        if (prID == null) {
            JOptionPane.showMessageDialog(this, "No PR selected.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to decline PR: " + prID + "?", 
            "Confirm Decline", 
            JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        boolean success = updatePRStatus(prID, "Declined");
        if (success) {
        JOptionPane.showMessageDialog(this, "PR " + prID + " has been declined.");
            loadPendingPRs(); // Refresh list
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update PR.");
        }
    }//GEN-LAST:event_btnDeclinePRActionPerformed

    private void txtDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDateActionPerformed

    private boolean updatePRStatus(String prID, String newStatus) {
        StringBuilder updatedContent = new StringBuilder();
        boolean updated = false;
        try (BufferedReader br = new BufferedReader(new FileReader("purchaserequisition.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7 && parts[0].equals(prID)) {
                    parts[6] = newStatus;
                    updated = true;
                }
                updatedContent.append(String.join(",", parts)).append("\n");
            }
        } catch (IOException e) {
            return false;
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("purchaserequisition.txt"))) {
            bw.write(updatedContent.toString());
        } catch (IOException e) {
            return false;
        }
        return updated;
    }

    
private String generateNextPOID() {
    int count = 1;
    try (BufferedReader br = new BufferedReader(new FileReader("purchaseorder.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            count++;
        }
    } catch (IOException e) {
    }
    return String.format("PO%03d", count);
}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDeclinePR;
    private javax.swing.JButton btnGeneratePO;
    private javax.swing.JComboBox<String> cmbPRID;
    private javax.swing.JComboBox<String> cmbSupplierID;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextField txtItemCode;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtRequestedBy;
    // End of variables declaration//GEN-END:variables
}
