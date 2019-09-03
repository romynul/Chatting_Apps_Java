/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connect_AIUBIAN;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author alger
 */
public class Inbox extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement ps = null;
    Statement stmt = null;
    ResultSet rs = null;
    String userName;
    
    public Inbox() {
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        inbox_tbl.setRowHeight(inbox_tbl.getRowHeight() + 4);
        inbox_tbl.setShowGrid(false);
        inbox_tbl.setIntercellSpacing(new Dimension(0, 0));
    }
    
    public void inbox_tblListener(){
        
        inbox_tbl.addMouseListener(new MouseAdapter() {
        public void mousePresed(MouseEvent me){
            JTable tbl =(JTable) me.getSource();
            Point p = me.getPoint();
            int row = tbl.rowAtPoint(p);
            if(me.getClickCount() == 2 ){
                String recepient = inbox_tbl.getValueAt(row, 0).toString();
                Private pm = new Private();
                pm.yourUsername = userName;
                pm.username = recepient;
                pm.show();
                
            }
        
        }
        
        });
    
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        inbox_tbl = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 102, 204));

        inbox_tbl.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        inbox_tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sender", "Date/Time"
            }
        ));
        jScrollPane1.setViewportView(inbox_tbl);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("INBOX");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(158, 158, 158)
                        .addComponent(jLabel1)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        conn = DBConnect.DBConnect();
        inbox_tblListener();
        inbox_tbl.setDefaultEditor(Object.class,null);
        DefaultTableModel model = (DefaultTableModel) inbox_tbl.getModel();
        TableColumn col = inbox_tbl.getColumnModel().getColumn(0);
        TableColumn col1 = inbox_tbl.getColumnModel().getColumn(1);
        col.setPreferredWidth(120);
        col1.setPreferredWidth(280);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        int cols = inbox_tbl.getColumnCount();
        for(int i = 0; i < cols; i++){
            inbox_tbl.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        String sql = "SELECT * FROM private_chat WHERE recepient = '"+userName+"' GROUP BY sender ORDER BY 'id'";
        try{
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                String sender = rs.getString("sender");
                String date = rs.getString("date/time");
                Object[] row = {sender,date};
                model.addRow(row);
            }
        
        }catch(Exception ex){
            System.out.println(ex);
            
        }
        inbox_tbl.addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent me) {
            JTable tbl =(JTable) me.getSource();
            Point p = me.getPoint();
            int row = tbl.rowAtPoint(p);
            if (me.getClickCount() == 2) {
                String recepient = inbox_tbl.getValueAt(row, 0).toString();
                Private pm = new Private();
                pm.yourUsername = userName;
                pm.username = recepient;
                pm.show();

            }
        }

        private void setVisible(boolean b) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
    }//GEN-LAST:event_formWindowOpened

   
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Inbox.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Inbox.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Inbox.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Inbox.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Inbox().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable inbox_tbl;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
