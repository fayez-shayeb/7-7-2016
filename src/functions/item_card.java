/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import Table_render.*;
import database_connection.db_Connection;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Fayez
 */
public class item_card extends javax.swing.JFrame {

    public db_Connection conn_obj = new db_Connection();
    public ResultSet r;
    Jtable_render renderer_jTable_obj = new Jtable_render();
    /**
     * Creates new form item_card
     */
    public item_card() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        searchItemName = new javax.swing.JFrame();
        jLabel6 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable5 = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // This is how we disable editing:
                return false;
            }
        };
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        searchItemName.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel6.setText("بحث عن صنف");

        jTextField2.setBackground(new java.awt.Color(0, 204, 102));
        jTextField2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField2.setToolTipText("");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField2KeyPressed(evt);
            }
        });

        jTable5.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable5.setRowHeight(30);
        jTable5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable5MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable5MousePressed(evt);
            }
        });
        jScrollPane8.setViewportView(jTable5);

        javax.swing.GroupLayout searchItemNameLayout = new javax.swing.GroupLayout(searchItemName.getContentPane());
        searchItemName.getContentPane().setLayout(searchItemNameLayout);
        searchItemNameLayout.setHorizontalGroup(
            searchItemNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchItemNameLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(searchItemNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, searchItemNameLayout.createSequentialGroup()
                        .addComponent(jTextField2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6))
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        searchItemNameLayout.setVerticalGroup(
            searchItemNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchItemNameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(searchItemNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("كرت الصنف");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jDateChooser1.setDate(get_date_jdate());
        jDateChooser1.setBackground(new java.awt.Color(0, 255, 255));
        jDateChooser1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jDateChooser1.setDateFormatString("yyyy/MM/dd");
        jDateChooser1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("من تاريخ :");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("الى تاريخ:");

        jDateChooser2.setDate(get_date_jdate());
        jDateChooser2.setBackground(new java.awt.Color(0, 255, 255));
        jDateChooser2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jDateChooser2.setDateFormatString("yyyy/MM/dd");
        jDateChooser2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jButton1.setText("اسم الصنف");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jButton2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jButton2.setText("بحث");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(204, 0, 0));
        jLabel5.setText("المستودع:");

        jComboBox2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        try{
            r = conn_obj.conn_exec("select location_name from location ");//لانها ثوابت في الداتا بيس وهي المواقع ونوع الزبائن
            while (r.next()) {
                jComboBox2.addItem(r.getString(1));
            }
        }catch(SQLException e){

        }

        jButton3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jButton3.setText("احتساب الكمية");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(396, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel4)))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton2, jButton3});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel4))
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton3)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jDateChooser1, jDateChooser2});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton2, jButton3, jComboBox2, jLabel5});

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);
        renderer_jTable_obj.Renderer(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyPressed

        int key = evt.getKeyCode();
        if (evt.getSource() == jTextField2) {
            if (key == KeyEvent.VK_ENTER) {

                r = conn_obj.conn_exec("select item_name as اسم_الصنف from ("
                        + "select items.main_items.item_name,items.main_items.item_id,"
                        + "items.items_ranking.rank_item_id,items.items_ranking.rank"
                        + " from items.main_items,items.items_ranking"
                        + " where "
                        + " items.main_items.item_id = items.items_ranking.rank_item_id \n"
                        + " and \n"
                        + " item_name LIKE '%" + jTextField2.getText().trim() + "%' order by rank desc) as ddddddddddd");

                jTable5.setModel(DbUtils.resultSetToTableModel(r));
                
            }
            renderer_jTable_obj.Renderer(jTable5);
        }

    }//GEN-LAST:event_jTextField2KeyPressed

    private void jTable5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable5MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable5MouseClicked

    private void jTable5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable5MousePressed
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            evt.consume();
                jLabel3.setText(jTable5.getValueAt(jTable5.getSelectedRow(), jTable5.getSelectedColumn()).toString());
                jTextField2.requestFocus();
                jTextField2.selectAll();
            searchItemName.dispose();
        }        
    }//GEN-LAST:event_jTable5MousePressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
         searchItemName.pack();
        searchItemName.setLocationRelativeTo(this);
        searchItemName.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        java.util.Date d;
        d = jDateChooser1.getDate();
        java.sql.Date sqlDate1 = new java.sql.Date(d.getTime());
        d = jDateChooser2.getDate();
        java.sql.Date sqlDate2 = new java.sql.Date(d.getTime());
        String location = (String) jComboBox2.getSelectedItem();
        String stm="";
        stm="select item_name الصنف,unit_name الوحدة,movement الحركة,bill_date التاريخ,bill_id رقم_الحركة,item_quantity كمية,item_bonus بونص, item_price سعر, (select location_name from location where location_id=location_idd)as  مستودع,'' كمية_الحركة,'' الكمية_الكلية  \n" +
                "from\n" +
                "(\n" +
                "select (select item_name from items.main_items where item_id = customer_bills_items.item_id),(select unit_name from items.item_units where unit_id = customer_bills_items.item_unit),\n" +
                "customer_bills_items.item_quantity,customer_bills_items.item_bonus,customer_bills_items.item_price,\n" +
                "       'فاتورة_مبيعات' as movement,customer_bills.bill_date,customer_bills.bill_id,customer_bills.bill_location_id as location_idd\n" +
                " from public.customer_bills,public.customer_bills_items\n" +
                " where customer_bills.bill_id=customer_bills_items.bill_id\n" +
                "       and customer_bills_items.item_id=(select item_id from items.main_items where item_name = '"+jLabel3.getText()+"')  "
                + " and   bill_date >= '"+sqlDate1+"' and bill_date <= '"+sqlDate2+"'  \n" +
                "\n" +
                "       union \n" +
                "\n" +
                "select (select item_name from items.main_items where item_id = vendor_bills_items.item_id),(select unit_name from items.item_units where unit_id = vendor_bills_items.item_unit),\n" +
                "vendor_bills_items.item_quantity,vendor_bills_items.item_bonus,vendor_bills_items.item_price,\n" +
                "       'فاتورة_مشتريات' as movement,vendor_bills.bill_date,vendor_bills.bill_id,vendor_bills.bill_location_id as location_idd\n" +
                " from public.vendor_bills,public.vendor_bills_items\n" +
                " where vendor_bills.bill_id=vendor_bills_items.bill_id\n" +
                "       and vendor_bills_items.item_id=(select item_id from items.main_items where item_name = '"+jLabel3.getText()+"') "
                + " and  bill_date >= '"+sqlDate1+"' and bill_date <= '"+sqlDate2+"'  \n" +
                "\n" +
                "       union\n" +
                "       \n" +
                "select (select item_name from items.main_items where item_id = return_customer_bills_items.return_item_id),(select unit_name from items.item_units where unit_id = return_customer_bills_items.return_item_unit),\n" +
                "return_customer_bills_items.return_item_quantity,0,return_customer_bills_items.return_item_price,\n" +
                "       'مردود_مبيعات' as movement,return_customer_bills.return_bill_date as bill_date,return_customer_bills.return_bill_id,return_customer_bills.return_bill_location_id as location_idd\n" +
                " from public.return_customer_bills,public.return_customer_bills_items\n" +
                " where return_customer_bills.return_bill_id=return_customer_bills_items.return_bill_id\n" +
                "       and return_customer_bills_items.return_item_id=(select item_id from items.main_items where item_name = '"+jLabel3.getText()+"')  "
                + " and  return_bill_date >= '"+sqlDate1+"' and return_bill_date <= '"+sqlDate2+"'   \n" +
                "\n" +
                "       union\n" +
                "       \n" +
                "select (select item_name from items.main_items where item_id = return_vendor_bills_items.return_item_id),(select unit_name from items.item_units where unit_id = return_vendor_bills_items.return_item_unit),\n" +
                "return_vendor_bills_items.return_item_quantity,0,return_vendor_bills_items.return_item_price,\n" +
                "       'مردود_مشتريات' as movement,return_vendor_bills.return_bill_date as bill_date,return_vendor_bills.return_bill_id,return_vendor_bills.return_bill_location_id as location_idd\n" +
                " from public.return_vendor_bills,public.return_vendor_bills_items\n" +
                " where return_vendor_bills.return_bill_id=return_vendor_bills_items.return_bill_id\n" +
                "       and return_vendor_bills_items.return_item_id=(select item_id from items.main_items where item_name = '"+jLabel3.getText()+"') "
                + "  and return_bill_date >= '"+sqlDate1+"' and return_bill_date <= '"+sqlDate2+"'  \n" +
                "\n" +
                "       \n" +
                "             \n" +
                "\n" +
                ")  as dv  where location_idd=(select location_id from location where location_name ='"+location+"')  order by bill_date";
        System.out.println(stm);
        r = conn_obj.conn_exec(stm);
        jTable1.setModel(DbUtils.resultSetToTableModel(r));
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
prepare_stm_that_return_quantity_to_store();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(item_card.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(item_card.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(item_card.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(item_card.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new item_card().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox jComboBox2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTable jTable1;
    public javax.swing.JTable jTable5;
    private javax.swing.JTextField jTextField2;
    public javax.swing.JFrame searchItemName;
    // End of variables declaration//GEN-END:variables

public Date get_date_jdate() {
        Date d = new Date();
        return d;
    }

public void prepare_stm_that_return_quantity_to_store()//عند تعديل قيد فاتورة الارجاع ننقص كميات الفاتورة من المحزون
{
    
        double all_movement_quantity=0;
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            try {
                float quantity = Float.parseFloat(jTable1.getValueAt(i, 5).toString());
                float bounus = Float.parseFloat(jTable1.getValueAt(i, 6).toString());
                float movement_quantity=quantity+bounus;
                //System.out.println(net_Q);
                r=conn_obj.conn_exec(""
                        + "select quantity,main_item_id from  (\n" +
                        "select \n" +
                        "\n" +
                        "items.main_items.item_name,\n" +
                        "items.main_items.item_id as main_item_id,\n" +
                        "\n" +
                        "items.item_units.unit_id,\n" +
                        "items.item_units.unit_name,\n" +
                        "\n" +
                        "items.item_relations.item_id,\n" +
                        "items.item_relations.item_unit,\n" +
                        "items.item_relations.item_relation*" +movement_quantity + " as quantity\n" +
                        "\n" +
                        "from items.main_items,items.item_units,items.item_relations\n" +
                        "\n" +
                        "where \n" +
                        "items.main_items.item_name='" + jTable1.getValueAt(i, 0) + "'  AND\n" +
                        "items.item_units.unit_name='" + jTable1.getValueAt(i, 1) + "'  AND\n" +
                        "items.main_items.item_id=items.item_relations.item_id AND\n" +
                        "items.item_units.unit_id=items.item_relations.item_unit )as anyThing");
                r.next();
                int Dec_or_Inc=0;
                if(jTable1.getValueAt(i, 2).toString().trim().equals("فاتورة_مبيعات")||jTable1.getValueAt(i, 2).toString().trim().equals("مردود_مشتريات"))
                    Dec_or_Inc=-1;
                else
                    Dec_or_Inc=1;
                
                all_movement_quantity+=r.getDouble("quantity")*Dec_or_Inc;
                System.out.println(all_movement_quantity);
                jTable1.setValueAt(movement_quantity*Dec_or_Inc, i, 9);
                jTable1.setValueAt(all_movement_quantity, i, 10);
            } catch (SQLException ex) {
            }    
        }
    } 
    



}
