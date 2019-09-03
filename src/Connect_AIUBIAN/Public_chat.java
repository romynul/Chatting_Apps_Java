/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connect_AIUBIAN;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListModel;

/**
 *
 * @author alger
 */
public class Public_chat extends javax.swing.JFrame {

    String ID;
    String USER;
    String NICK;
    Connection conn = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    /**
     * Creates new form Public
     */
    public Public_chat() {
        initComponents();
        setLocationRelativeTo(null);
       
    }
    
    public void NewmsgDisplay(){
        msg_area.setText("");
        String message = "";
        String nickname = "";
        String messageDisplay = "";
        String id = "";
        String sql1 = "SELECT * FROM public_chat ";
            try{
                ps = conn.prepareStatement(sql1);
                rs = ps.executeQuery();
                while(rs.next()){
                    message = rs.getString("message");
                    nickname = rs.getString("username");
                    id = rs.getString("id");
                    messageDisplay = nickname + " Says: " + message +"\n";
                    msg_area.append(messageDisplay);
                    msg_area.setCaretPosition(msg_area.getDocument().getLength());
                }   
            }catch(Exception ex){
                System.out.println(ex);
            }
    }
    public void UsersList(){
        DefaultListModel List = new DefaultListModel();
        String sql = "SELECT * FROM account WHERE NOT (username = '"+USER+"') ";
        try{
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                String user = rs.getString("username");
                List.addElement(user);
                userList.setModel(List);
            }
            
        }catch(Exception ex){
            System.out.println(ex);
        }
        
    }
    
    public void ListMenu(){
      
        userList.addMouseListener(new MouseAdapter(){
        @Override
        public void mouseClicked(MouseEvent e) {
            int index  = userList.getSelectedIndex();
            String s = (String) userList.getSelectedValue();
            final String selectedUser = userList.getSelectedValue();
            if(e.getButton() ==  MouseEvent.BUTTON1 && e.getClickCount() == 1 && index != -1){
                JPopupMenu pop = new JPopupMenu();
                JMenuItem menu0 = new JMenuItem("View Information");
                JMenuItem menu1 = new JMenuItem("Add Friend");
                JMenuItem menu2 = new JMenuItem("Private Message");
                ActionListener menuListener = new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    if("Private Message".equals(event.getActionCommand())){
                        Private pm = new Private();
                        pm.username = selectedUser;
                        pm.yourUsername = USER;
                        pm.show();
                        
                    }else if("View Information".equals(event.getActionCommand())){
                        String sql = "SELECT * FROM account WHERE username ='"+selectedUser+"'";
                        String name = "";
                        byte[] img = null;
                        String age = "";
                        String sem = "";
                        String gender = "";
                        try{
                            ps = conn.prepareStatement(sql);
                            rs = ps.executeQuery();
                            if(rs.next()){
                                name = rs.getString("name");
                                img = rs.getBytes("image");
                                age = rs.getString("age");
                                sem = rs.getString("semester");
                                gender = rs.getString("gender");
                                Info info = new Info();
                                info.NAME = name;
                                info.IMG = img;
                                info.AGE = age;
                                info.GENDER=gender;
                                info.SEMESTER=sem;
                                info.USERNAME = selectedUser;
                                info.u = USER;
                                info.setVisible(true);
                            }
                        
                        }catch(Exception ex){
                        
                        }
                        
                    }else if("Add Friend".equals(event.getActionCommand())){
                        int opt = JOptionPane.showConfirmDialog(null, "Confirm Friend Request?","Confirmation",JOptionPane.YES_NO_OPTION);
                        if(opt != JOptionPane.NO_OPTION){
                            String sql = "INSERT INTO request(request,requestSender,requestReceiver,status)VALUES('Add Friend','"+USER+"','"+selectedUser+"','pending')";
                            try{    
                                stmt = conn.createStatement();
                                if(stmt.executeUpdate(sql) == 1){
                                    JOptionPane.showMessageDialog(null, "Friend Request Sent");
                                }
                            
                            }catch(Exception ex){
                                System.out.println(ex);
                            }
                        }
                    }
                }
                };
                pop.add(menu0);
                pop.add(menu1);
                pop.add(menu2);
                menu0.addActionListener(menuListener);
                menu1.addActionListener(menuListener);
                menu2.addActionListener(menuListener);
                userList.setComponentPopupMenu(pop);    
            }   
          }
    });
    }
    
    /*public void Notification(){
        
        String[] info = new String [5];
        String sql = "SELECT * FROM private_chat WHERE status = 'pending' AND recepient = '"+USER+"'";
        try{
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                info[0] = rs.getString("msg_id");
                info[1] = rs.getString("message");
                info[2] = rs.getString("sender");
                info[3] = rs.getString("recepient");
                info[4] = rs.getString("status"); 
                String sql1 = "SELECT * FROM private_chat WHERE status = 'pending' AND recepient = '"+USER+"' ORDER BY sender";
                try{
                    ps = conn.prepareStatement(sql1);
                    rs = ps.executeQuery();
                    while(rs.next()){
                        String user = rs.getString("recepient");
                        String sender = rs.getString("sender");
                        String status = rs.getString("status");
                        String id = rs.getString("msg_id");
                        if("pending".equals(status)){
                            JOptionPane.showMessageDialog(null, sender + " sent you a private message");
                            Private pm = new Private();
                            pm.username = sender;
                            pm.yourUsername = USER;
                            String sqll = "UPDATE private_chat SET status = 'seen' WHERE msg_id = '"+id+"'";
                            try{
                                stmt = conn.createStatement();
                                stmt.executeUpdate(sqll);
                            }catch(Exception ex){
                                System.out.println(ex);
                            }
                            pm.show();
                            
                        }

                    }
                    
                }catch(Exception ex){
                    System.out.println(ex);
                }
            }
        
        }catch(Exception ex){
            System.out.println(ex);
        };
        
    }*/
  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        msg_area = new javax.swing.JTextArea();
        msg_tf = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        userList = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(250, 250, 250));

        jPanel3.setBackground(new java.awt.Color(0, 102, 204));

        jPanel2.setBackground(new java.awt.Color(0, 102, 204));

        jPanel1.setBackground(new java.awt.Color(0, 102, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2), "Connected Friends Public Chat", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(758, 550));

        msg_area.setEditable(false);
        msg_area.setColumns(20);
        msg_area.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        msg_area.setRows(5);
        msg_area.setAutoscrolls(false);
        msg_area.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        msg_area.setDropMode(javax.swing.DropMode.INSERT);
        msg_area.setFocusable(false);
        msg_area.setRequestFocusEnabled(false);
        msg_area.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                msg_areaInputMethodTextChanged(evt);
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                msg_areaCaretPositionChanged(evt);
            }
        });
        jScrollPane1.setViewportView(msg_area);

        msg_tf.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N

        jButton1.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jButton1.setText("SEND");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        userList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Member", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(0, 102, 204))); // NOI18N
        userList.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        userList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                userListMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(userList);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(msg_tf)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(msg_tf, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE))
                .addGap(35, 35, 35))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(138, 138, 138))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 564, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 571, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        conn =  DBConnect.DBConnect();
        ListMenu();
        msg_area.getCaret().setVisible(false);
        UsersList();
        NewmsgDisplay();
        Runnable helloRunnable = new Runnable() {
        public void run() {
//            Notification();
            NewmsgDisplay();
        }
        };
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 1000, TimeUnit.MILLISECONDS);
    
    }//GEN-LAST:event_formWindowOpened

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String msg = msg_tf.getText();
        String sql = "INSERT INTO public_chat(message,username)VALUES('"+msg+"','"+NICK+"')";
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
//                msg_area.append(NICK + " says: "+msg +"\n");
            
        }catch(Exception ex){
            System.out.println(ex);
        }
        msg_tf.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void userListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userListMouseClicked

    }//GEN-LAST:event_userListMouseClicked

    private void msg_areaCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_msg_areaCaretPositionChanged

    }//GEN-LAST:event_msg_areaCaretPositionChanged

    private void msg_areaInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_msg_areaInputMethodTextChanged

    }//GEN-LAST:event_msg_areaInputMethodTextChanged

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(Public_chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Public_chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Public_chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Public_chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Public_chat().setVisible(true);
                
            }
            
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea msg_area;
    private javax.swing.JTextField msg_tf;
    private javax.swing.JList<String> userList;
    // End of variables declaration//GEN-END:variables
}
