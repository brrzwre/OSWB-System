/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package purchase;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class ViewRequisitionsPanel extends javax.swing.JPanel {

    /**
     * Creates new form ViewRequisitionsPanel
     */
    
    private PurchaseManager purchaseManager;
    public ViewRequisitionsPanel(PurchaseManager purchaseManager) {
        this.purchaseManager = purchaseManager;
        initComponents();
        loadRequisitionData("","All");
    }
    
    private void loadRequisitionData(String keyword, String statusFilter) {
        DefaultTableModel model = (DefaultTableModel) tblRequisitions.getModel();
    model.setRowCount(0); 

    try (BufferedReader br = new BufferedReader(new FileReader("purchaserequisition.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");

            String status = data[data.length - 1].trim(); // assuming status is the last field
            if (!statusFilter.equals("All") && !status.equalsIgnoreCase(statusFilter)) {
                continue;
            }

            boolean match = false;
            for (String field : data) {
                if (field.toLowerCase().contains(keyword.toLowerCase())) {
                    match = true;
                    break;
                }
            }

            if (match || keyword.isEmpty()) {
                model.addRow(data);
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error loading requisition data: " + e.getMessage());
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

        tblRequisitions1 = new javax.swing.JScrollPane();
        tblRequisitions = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        cmbStatusFilter = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();

        setBackground(new java.awt.Color(204, 204, 204));

        tblRequisitions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "PR ID", "Item Code", "Item Name", "Quantity", "Requested Date", "Requested By (SM ID)", "Status"
            }
        ));
        tblRequisitions1.setViewportView(tblRequisitions);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setText("VIEW PURCHASE REQUISITIONS");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        cmbStatusFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Pending", "Accepted", "Declined", " " }));
        cmbStatusFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbStatusFilterActionPerformed(evt);
            }
        });

        jLabel2.setText("Filter Status");

        btnRefresh.setText("Refresh Search");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(286, 286, 286)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnRefresh)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(btnSearch)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cmbStatusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(tblRequisitions1, javax.swing.GroupLayout.PREFERRED_SIZE, 828, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(cmbStatusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(tblRequisitions1, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRefresh)
                .addContainerGap(31, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
    String keyword = txtSearch.getText().trim();
    String statusFilter = cmbStatusFilter.getSelectedItem().toString();
    loadRequisitionData(keyword, statusFilter);
    }//GEN-LAST:event_btnSearchActionPerformed

    private void cmbStatusFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStatusFilterActionPerformed
        String keyword = txtSearch.getText().trim();
        String statusFilter = cmbStatusFilter.getSelectedItem().toString();
        loadRequisitionData(keyword, statusFilter);
    }//GEN-LAST:event_cmbStatusFilterActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
         txtSearch.setText(""); 
        cmbStatusFilter.setSelectedItem("All"); 
        loadRequisitionData("","All");  
    }//GEN-LAST:event_btnRefreshActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox<String> cmbStatusFilter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTable tblRequisitions;
    private javax.swing.JScrollPane tblRequisitions1;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
