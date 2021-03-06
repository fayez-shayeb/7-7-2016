package customers;

import Table_render.*;
import database_connection.db_Connection;
import functions.banks;
import functions.get_user_name;
import functions.item_withdrawales;
import functions.script;
import functions.search_check;
import functions.search_vendor_items;
import functions.show_vendor_goods;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import net.proteanit.sql.DbUtils;
import org.jdesktop.swingx.autocomplete.*;
import functions.telephone;
import functions.currency;
import functions.customer_catagories;
import functions.item_card;
import functions.meachanism_to_search_customers_accounts;
import functions.sale_point;
import java.awt.MenuItem;
import java.awt.Robot;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.text.DecimalFormat;
import javax.print.PrintServiceLookup;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Fayez
 */
public class customers extends javax.swing.JFrame {

    public db_Connection conn_obj = new db_Connection();
    public ResultSet r;
    public db_Connection conn_obj2 = new db_Connection();

    public ResultSet r2;
    PreparedStatement pst = null;
    String scanner_path = "C:\\Users\\shayeb\\Documents\\Scanned Documents";
    String file_loc = "backup_2015\\";
    //String file_loc = "d:\\Dropbox\\kufraai\\backup\\";
    String write_to_file_loc = "movements_2015\\";
    //String write_to_file_loc="D:\\Dropbox\\kufraai\\movements\\";
    show_bill_items show_obj = null;
    Font FayezFont = new FontUIResource(new Font("Arial", Font.BOLD, 18));
    JFileChooser fileChooser_restore;
    Jtable_render renderer_jTable_obj = new Jtable_render();
    modify_customer_bill jframe_to_modify_bill;
    modify_customer_payment jframe_to_modify_payment;
    modify_vendor_bill jframe_to_modify_bill_vendor;
    modify_vendor_payment jframe_to_modify_vendor_payment;
    int which_component_request_searchCustomerName = 0;//لمعرفة اي فيلد يريد ام يبحث عن اسم الزبون لنضعه فيه
    int which_component_request_searchItemName = 0;
    int which_component_request_searchCustomerName_vendor = 0;//لمعرفة اي فيلد يريد ام يبحث عن اسم الزبون لنضعه فيه
    int which_component_request_searchItemName_vendor = 0;
    TableModelListener fayez;
    JComboBox Bank_jcomboBox=new JComboBox();
    String stm_to_return_quantity_to_store="";
    String ven_stm_to_return_quantity_to_store="";
    search_check s;
    checks checks_obj=new checks();
    public JComboBox item_Units;//for items
    JComboBox comboBox  = new JComboBox();//jtable6 add new unit to item
    telephone telephone_obj;
    currency currency_obj;
    progressbar_jframe x;
    Robot robot;//لضغط كبسة اوتوماتيكلي
    sale_point sale_point_obj;
    String user_name; 
    add_remove_customers add_remove_customers_object;
    print_customer_move_table print_customer_move_table_object;
    image_icon iconnn;
    String items_images_folder;
    meachanism_to_search_customers_accounts mecha;
    Items items_obj=new Items();
    
    //by default false
    boolean see_profit,see_cost,delete_entry,modify_entry,check_lose,search_in_buy_bills,discount_cus,discount_ven,
            modify_check_status,return_check_to_customer,ranking_items,sale_point,account_zero;//by default false
    //رؤية الارباح==السعر علينا==حذف قيد==تعديل قيد==فحص الخسارة ف الصنف==البحث في فواتير المشتريات==خصم للزبون==خصم للتاجر
    //تغيير حالة الشيكات==ارجاع شك للزبون==تصنيف الاصناف حسب الاستخدام==نقطة بيع==تصفير حساب زبون

    /**
     * Creates new form vendors
     */
    public customers() {

        try {
            add_remove_customers_object=new add_remove_customers();
            user_name = new get_user_name().user_name();//لمعرفة اسم المستخدم
            r = conn_obj.conn_exec("select privileg from users where user_name='"+user_name.trim()+"' ");// 
            r.next();
            String privileg_txt=r.getString("privileg");
            
            r = conn_obj.conn_exec("select var_value from variables where var_name='items_images_folder' ");// 
            r.next();
            items_images_folder=r.getString("var_value");
            
            initComponents();
            jTabbedPane3.addTab("شيكات", new javax.swing.ImageIcon(getClass().getResource("/images/check_48.png")), checks_obj.jTabbedPane_checks);
            jTabbedPane3.addTab("أصناف", new javax.swing.ImageIcon(getClass().getResource("/images/items.png")), items_obj.jTabbedPane3_Items);
            jTabbedPane1.addTab("أسماء الزبائن", add_remove_customers_object.jTabbedPane2);
            this.getInputContext().selectInputMethod(new Locale("ar", "JO"));
            //تغيير حجم الخط في   joptionpane
            UIManager.put("OptionPane.messageFont", FayezFont);
            update_jcomboBox_1_3_4();

            r = conn_obj.conn_exec("select location_name from location ");//لانها ثوابت في الداتا بيس وهي المواقع ونوع الزبائن
            while (r.next()) {
                jComboBox2.addItem(r.getString(1));
                jComboBox9.addItem(r.getString(1));
                jComboBox_bill_store.addItem(r.getString(1));
                jComboBox_return_ven_store.addItem(r.getString(1));
            }
                /////

            ///////////
            jComboBox5.removeAllItems();
            jComboBox6.removeAllItems();
            jComboBox7.removeAllItems();//للحركات والاحصائيات
            r = conn_obj.conn_exec("SELECT catagory_name from customer_catagory order by catagory_name");
            while (r.next()) {
                jComboBox5.addItem(r.getString(1));
                jComboBox6.addItem(r.getString(1));
                jComboBox7.addItem(r.getString(1));//للحركات والاحصائيات
            }
            privileg_panels(privileg_txt);
            privileg_actions(privileg_txt);
            
        } catch (IOException | SQLException ex) {
            Joptionpane_message(ex.getMessage());
        }

    }
   customers(int x)
   {
       
   }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        show_items = new javax.swing.JMenuItem();
        show_image = new javax.swing.JMenuItem();
        modify_record = new javax.swing.JMenuItem();
        show_note = new javax.swing.JMenuItem();
        delete_record = new javax.swing.JMenuItem();
        copy_of_bill = new javax.swing.JMenuItem();
        direct_print = new javax.swing.JMenuItem();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        customer_bill = new javax.swing.JFrame();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jButton20 = new javax.swing.JButton();
        jLabel57 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        searchCustomerName = new javax.swing.JFrame();
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
        searchItemName = new javax.swing.JFrame();
        jLabel34 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable7 = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // This is how we disable editing:
                //return false;
                return  column==2 || column==3 || column==4  ? true : false;
            }

            @Override
            //  Returning the Class of each column will allow different
            //  renderers to be used based on Class
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }

        };
        jCheckBox2 = new javax.swing.JCheckBox();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        search_items_in_customer_bills = new javax.swing.JFrame();
        jPanel8 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel55 = new javax.swing.JLabel();
        jButton16 = new javax.swing.JButton();
        jTextField29 = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        jButton17 = new javax.swing.JButton();
        jPanel30 = new javax.swing.JPanel();
        jScrollPane18 = new javax.swing.JScrollPane();
        jTable11 = new javax.swing.JTable();
        jPanel22 = new javax.swing.JPanel();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        update_or_show_returned_bill_items = new javax.swing.JFrame();
        jPanel36 = new javax.swing.JPanel();
        jScrollPane22 = new javax.swing.JScrollPane();
        jTable15 = new javax.swing.JTable();
        jPanel37 = new javax.swing.JPanel();
        jButton39 = new javax.swing.JButton();
        jButton40 = new javax.swing.JButton();
        jPanel38 = new javax.swing.JPanel();
        jLabel65 = new javax.swing.JLabel();
        jComboBox10 = new javax.swing.JComboBox();
        jTextField36 = new javax.swing.JTextField();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jDateChooser7 = new com.toedter.calendar.JDateChooser();
        jComboBox11 = new javax.swing.JComboBox();
        jScrollPane23 = new javax.swing.JScrollPane();
        jTextArea6 = new javax.swing.JTextArea();
        jLabel74 = new javax.swing.JLabel();
        jButton41 = new javax.swing.JButton();
        jLabel60 = new javax.swing.JLabel();
        jTextField31 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jPanel39 = new javax.swing.JPanel();
        jButton42 = new javax.swing.JButton();
        jLabel75 = new javax.swing.JLabel();
        jTextField37 = new javax.swing.JTextField();
        jButton43 = new javax.swing.JButton();
        searchvendorName = new javax.swing.JFrame();
        jLabel102 = new javax.swing.JLabel();
        jTextField24 = new javax.swing.JTextField();
        jScrollPane29 = new javax.swing.JScrollPane();
        jTable_ven_names = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // This is how we disable editing:
                return false;
            }
        };
        search_ven_ItemName = new javax.swing.JFrame();
        jLabel103 = new javax.swing.JLabel();
        jTextField25 = new javax.swing.JTextField();
        jScrollPane30 = new javax.swing.JScrollPane();
        jTable10 = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // This is how we disable editing:
                //return false;
                return  column==2 || column==3 || column==4 || column==5 ? true : false;
            }};
            jPopupMenu_ven = new javax.swing.JPopupMenu();
            show_items_ven = new javax.swing.JMenuItem();
            show_image_ven = new javax.swing.JMenuItem();
            modify_record_ven = new javax.swing.JMenuItem();
            show_note_ven = new javax.swing.JMenuItem();
            delete_record_ven = new javax.swing.JMenuItem();
            copy_of_bill_ven = new javax.swing.JMenuItem();
            update_or_show_returned_bill_items_ven = new javax.swing.JFrame();
            jPanel46 = new javax.swing.JPanel();
            jScrollPane31 = new javax.swing.JScrollPane();
            jTable16 = new javax.swing.JTable();
            jPanel47 = new javax.swing.JPanel();
            jButton62 = new javax.swing.JButton();
            jButton63 = new javax.swing.JButton();
            jPanel48 = new javax.swing.JPanel();
            jLabel104 = new javax.swing.JLabel();
            jComboBox12 = new javax.swing.JComboBox();
            jTextField38 = new javax.swing.JTextField();
            jLabel105 = new javax.swing.JLabel();
            jLabel106 = new javax.swing.JLabel();
            jLabel107 = new javax.swing.JLabel();
            jDateChooser8 = new com.toedter.calendar.JDateChooser();
            jComboBox13 = new javax.swing.JComboBox();
            jScrollPane32 = new javax.swing.JScrollPane();
            jTextArea7 = new javax.swing.JTextArea();
            jLabel108 = new javax.swing.JLabel();
            jButton64 = new javax.swing.JButton();
            jLabel109 = new javax.swing.JLabel();
            jTextField32 = new javax.swing.JTextField();
            jLabel110 = new javax.swing.JLabel();
            jPanel49 = new javax.swing.JPanel();
            jButton65 = new javax.swing.JButton();
            jLabel111 = new javax.swing.JLabel();
            jTextField39 = new javax.swing.JTextField();
            jButton66 = new javax.swing.JButton();
            search_items_in_vendor_bills = new javax.swing.JFrame();
            jPanel50 = new javax.swing.JPanel();
            jCheckBox8 = new javax.swing.JCheckBox();
            jLabel112 = new javax.swing.JLabel();
            jButton67 = new javax.swing.JButton();
            jTextField33 = new javax.swing.JTextField();
            jLabel113 = new javax.swing.JLabel();
            jButton68 = new javax.swing.JButton();
            jPanel51 = new javax.swing.JPanel();
            jScrollPane33 = new javax.swing.JScrollPane();
            jTable14 = new javax.swing.JTable();
            jFrame1_modify_checks_to_anothor_stat = new javax.swing.JFrame();
            jComboBox1_checks_status = new javax.swing.JComboBox();
            jLabel136 = new javax.swing.JLabel();
            jLabel137 = new javax.swing.JLabel();
            jLabel138 = new javax.swing.JLabel();
            jDateChooser2_checks_change2 = new com.toedter.calendar.JDateChooser();
            jDateChooser1_checks_change_1 = new com.toedter.calendar.JDateChooser();
            jButton1_checks_change_order = new javax.swing.JButton();
            jPanel72 = new javax.swing.JPanel();
            jLabel139 = new javax.swing.JLabel();
            buttonGroup1 = new javax.swing.ButtonGroup();
            popup_menue_payment_reciever = new javax.swing.JPopupMenu();
            jTabbedPane3 = new javax.swing.JTabbedPane();
            jTabbedPane1 = new javax.swing.JTabbedPane();
            jPanel3 = new javax.swing.JPanel();
            jScrollPane5 = new javax.swing.JScrollPane();
            jTable4 = new JTable() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // This is how we disable editing:
                    //return false;
                    return column == 2 || column == 3 || column==4 || column==5 || column==7 ? true : false;
                }
            };
            jPanel11 = new javax.swing.JPanel();
            jButton14 = new javax.swing.JButton();
            jButton18 = new javax.swing.JButton();
            jButton3 = new javax.swing.JButton();
            jButton23 = new javax.swing.JButton();
            jButton73 = new javax.swing.JButton();
            jPanel12 = new javax.swing.JPanel();
            jTextField15 = new javax.swing.JTextField();
            jLabel29 = new javax.swing.JLabel();
            jTextField16 = new javax.swing.JTextField();
            jLabel26 = new javax.swing.JLabel();
            jLabel4 = new javax.swing.JLabel();
            jTextField3 = new javax.swing.JTextField();
            jLabel7 = new javax.swing.JLabel();
            jLabel8 = new javax.swing.JLabel();
            jButton2 = new javax.swing.JButton();
            jLabel30 = new javax.swing.JLabel();
            jTextField17 = new javax.swing.JTextField();
            jButton33 = new javax.swing.JButton();
            jCheckBox3 = new javax.swing.JCheckBox();
            jPanel10 = new javax.swing.JPanel();
            jLabel2 = new javax.swing.JLabel();
            jComboBox1 = new javax.swing.JComboBox();
            jTextField1 = new javax.swing.JTextField();
            jLabel3 = new javax.swing.JLabel();
            jLabel5 = new javax.swing.JLabel();
            jLabel1 = new javax.swing.JLabel();
            jDateChooser1 = new com.toedter.calendar.JDateChooser();
            jComboBox2 = new javax.swing.JComboBox();
            jScrollPane1 = new javax.swing.JScrollPane();
            jTextArea1 = new javax.swing.JTextArea();
            jLabel9 = new javax.swing.JLabel();
            jButton19 = new javax.swing.JButton();
            jPanel15 = new javax.swing.JPanel();
            jButton21 = new javax.swing.JButton();
            jLabel35 = new javax.swing.JLabel();
            jTextField18 = new javax.swing.JTextField();
            jButton22 = new javax.swing.JButton();
            jLabel51 = new javax.swing.JLabel();
            jTextField27 = new javax.swing.JTextField();
            jButton13 = new javax.swing.JButton();
            jLabel53 = new javax.swing.JLabel();
            jLabel54 = new javax.swing.JLabel();
            jButton71 = new javax.swing.JButton();
            jPanel5 = new javax.swing.JPanel();
            jPanel20 = new javax.swing.JPanel();
            jComboBox4 = new javax.swing.JComboBox();
            jLabel22 = new javax.swing.JLabel();
            jButton28 = new javax.swing.JButton();
            jLabel37 = new javax.swing.JLabel();
            jLabel38 = new javax.swing.JLabel();
            jButton30 = new javax.swing.JButton();
            jPanel21 = new javax.swing.JPanel();
            jPanel52 = new javax.swing.JPanel();
            jLabel19 = new javax.swing.JLabel();
            jTextField14 = new javax.swing.JTextField();
            jLabel15 = new javax.swing.JLabel();
            jTextField13 = new javax.swing.JTextField();
            jLabel14 = new javax.swing.JLabel();
            jLabel21 = new javax.swing.JLabel();
            jLabel18 = new javax.swing.JLabel();
            jTextField12 = new javax.swing.JTextField();
            jLabel16 = new javax.swing.JLabel();
            jScrollPane4 = new javax.swing.JScrollPane();
            jTextArea2 = new javax.swing.JTextArea();
            jTextField19 = new javax.swing.JTextField();
            jDateChooser2 = new com.toedter.calendar.JDateChooser();
            jLabel20 = new javax.swing.JLabel();
            jTextField11 = new javax.swing.JTextField();
            jLabel140 = new javax.swing.JLabel();
            jTextField26 = new javax.swing.JTextField();
            jPanel53 = new javax.swing.JPanel();
            jScrollPane13 = new javax.swing.JScrollPane();
            jTable13 = new javax.swing.JTable();
            jButton12 = new javax.swing.JButton();
            jButton8 = new javax.swing.JButton();
            jButton5 = new javax.swing.JButton();
            jPanel1 = new javax.swing.JPanel();
            jScrollPane3 = new javax.swing.JScrollPane();
            jScrollPane6 = new javax.swing.JScrollPane();
            jTable3 = new JTable() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    //return false;
                    return false;
                }
            };
            jPanel9 = new javax.swing.JPanel();
            jLabel17 = new javax.swing.JLabel();
            jComboBox3 = new javax.swing.JComboBox();
            jButton9 = new javax.swing.JButton();
            jButton6 = new javax.swing.JButton();
            jButton10 = new javax.swing.JButton();
            jButton11 = new javax.swing.JButton();
            jButton27 = new javax.swing.JButton();
            jButton34 = new javax.swing.JButton();
            jButton38 = new javax.swing.JButton();
            jPanel2 = new javax.swing.JPanel();
            jScrollPane2 = new javax.swing.JScrollPane();
            jTable1 = new javax.swing.JTable();
            jTabbedPane2 = new javax.swing.JTabbedPane();
            jPanel7 = new javax.swing.JPanel();
            jLabel10 = new javax.swing.JLabel();
            jLabel11 = new javax.swing.JLabel();
            jLabel12 = new javax.swing.JLabel();
            jTextField5 = new javax.swing.JTextField();
            jTextField6 = new javax.swing.JTextField();
            jTextField7 = new javax.swing.JTextField();
            jButton1 = new javax.swing.JButton();
            jLabel27 = new javax.swing.JLabel();
            jComboBox5 = new javax.swing.JComboBox();
            jPanel6 = new javax.swing.JPanel();
            jPanel23 = new javax.swing.JPanel();
            jTextField9 = new javax.swing.JTextField();
            jTextField10 = new javax.swing.JTextField();
            jTextField8 = new javax.swing.JTextField();
            jLabel40 = new javax.swing.JLabel();
            jLabel41 = new javax.swing.JLabel();
            jLabel42 = new javax.swing.JLabel();
            jLabel13 = new javax.swing.JLabel();
            jButton4 = new javax.swing.JButton();
            jLabel144 = new javax.swing.JLabel();
            jComboBox6 = new javax.swing.JComboBox();
            jPanel4 = new javax.swing.JPanel();
            jLabel24 = new javax.swing.JLabel();
            jLabel25 = new javax.swing.JLabel();
            jButton7 = new javax.swing.JButton();
            jButton29 = new javax.swing.JButton();
            jPanel16 = new javax.swing.JPanel();
            jScrollPane11 = new javax.swing.JScrollPane();
            jTable8 = new javax.swing.JTable();
            jButton24 = new javax.swing.JButton();
            jButton25 = new javax.swing.JButton();
            jPanel17 = new javax.swing.JPanel();
            jPanel18 = new javax.swing.JPanel();
            jScrollPane12 = new javax.swing.JScrollPane();
            jTable9 =  new JTable() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // This is how we disable editing:
                    return false;
                }
            };
            jPanel19 = new javax.swing.JPanel();
            jDateChooser3 = new com.toedter.calendar.JDateChooser();
            jLabel36 = new javax.swing.JLabel();
            jButton26 = new javax.swing.JButton();
            jPanel24 = new javax.swing.JPanel();
            jCheckBox4 = new javax.swing.JCheckBox();
            jCheckBox5 = new javax.swing.JCheckBox();
            jCheckBox6 = new javax.swing.JCheckBox();
            jCheckBox7 = new javax.swing.JCheckBox();
            jLabel44 = new javax.swing.JLabel();
            jDateChooser5 = new com.toedter.calendar.JDateChooser();
            jLabel45 = new javax.swing.JLabel();
            jComboBox7 = new javax.swing.JComboBox();
            jLabel145 = new javax.swing.JLabel();
            jRadioButton1 = new javax.swing.JRadioButton();
            jRadioButton2 = new javax.swing.JRadioButton();
            jButton72 = new javax.swing.JButton();
            jPanel31 = new javax.swing.JPanel();
            jScrollPane19 = new javax.swing.JScrollPane();
            jTable12 = new javax.swing.JTable();
            jPanel32 = new javax.swing.JPanel();
            jButton31 = new javax.swing.JButton();
            jButton32 = new javax.swing.JButton();
            jPanel34 = new javax.swing.JPanel();
            jLabel64 = new javax.swing.JLabel();
            jComboBox8 = new javax.swing.JComboBox();
            jTextField34 = new javax.swing.JTextField();
            jLabel66 = new javax.swing.JLabel();
            jLabel67 = new javax.swing.JLabel();
            jLabel68 = new javax.swing.JLabel();
            jDateChooser6 = new com.toedter.calendar.JDateChooser();
            jComboBox9 = new javax.swing.JComboBox();
            jScrollPane20 = new javax.swing.JScrollPane();
            jTextArea5 = new javax.swing.JTextArea();
            jLabel69 = new javax.swing.JLabel();
            jButton35 = new javax.swing.JButton();
            jLabel59 = new javax.swing.JLabel();
            jTextField30 = new javax.swing.JTextField();
            jPanel35 = new javax.swing.JPanel();
            jButton36 = new javax.swing.JButton();
            jLabel70 = new javax.swing.JLabel();
            jTextField35 = new javax.swing.JTextField();
            jButton37 = new javax.swing.JButton();
            jTabbedPane_vendor_action = new javax.swing.JTabbedPane();
            jPanel_add_ven_bill = new javax.swing.JPanel();
            jScrollPane14 = new javax.swing.JScrollPane();
            jTable_bill_items = new javax.swing.JTable();
            jPanel25 = new javax.swing.JPanel();
            jButton_remove_bill_item = new javax.swing.JButton();
            jButton_isert_bill = new javax.swing.JButton();
            jPanel26 = new javax.swing.JPanel();
            jTextField_bill_number = new javax.swing.JTextField();
            jLabel46 = new javax.swing.JLabel();
            jTextField_bill_discount = new javax.swing.JTextField();
            jLabel28 = new javax.swing.JLabel();
            jLabel47 = new javax.swing.JLabel();
            jTextField_bill_dis_ratio = new javax.swing.JTextField();
            jLabel48 = new javax.swing.JLabel();
            jLabel49 = new javax.swing.JLabel();
            jButton15 = new javax.swing.JButton();
            jLabel50 = new javax.swing.JLabel();
            jTextField_bill_before_dis = new javax.swing.JTextField();
            jPanel27 = new javax.swing.JPanel();
            jLabel52 = new javax.swing.JLabel();
            jCombo_vendor_name = new javax.swing.JComboBox();
            jTextField_bill_value = new javax.swing.JTextField();
            jLabel58 = new javax.swing.JLabel();
            jLabel61 = new javax.swing.JLabel();
            jLabel62 = new javax.swing.JLabel();
            jDateChooser_bill_date = new com.toedter.calendar.JDateChooser();
            jComboBox_bill_store = new javax.swing.JComboBox();
            jScrollPane15 = new javax.swing.JScrollPane();
            jTextArea_bill_note = new javax.swing.JTextArea();
            jLabel63 = new javax.swing.JLabel();
            jButton44 = new javax.swing.JButton();
            jPanel28 = new javax.swing.JPanel();
            jButton_search_bill_item = new javax.swing.JButton();
            jLabel76 = new javax.swing.JLabel();
            jTextField_search_barcode = new javax.swing.JTextField();
            jLabel77 = new javax.swing.JLabel();
            jTextField_search_bill_items = new javax.swing.JTextField();
            jPanel_ven_pay = new javax.swing.JPanel();
            jButton45 = new javax.swing.JButton();
            jPanel29 = new javax.swing.JPanel();
            jLabel78 = new javax.swing.JLabel();
            jTextField_ven_pay_total = new javax.swing.JTextField();
            jTextField_ven_pay_cash = new javax.swing.JTextField();
            jLabel79 = new javax.swing.JLabel();
            jTextField_ven_pay_receive = new javax.swing.JTextField();
            jLabel80 = new javax.swing.JLabel();
            jTextField_ven_pay_helder = new javax.swing.JTextField();
            jLabel81 = new javax.swing.JLabel();
            jLabel82 = new javax.swing.JLabel();
            jDateChooser_ven_pay_date = new com.toedter.calendar.JDateChooser();
            jLabel83 = new javax.swing.JLabel();
            jScrollPane16 = new javax.swing.JScrollPane();
            jTextArea_ven_pay_note = new javax.swing.JTextArea();
            jPanel33 = new javax.swing.JPanel();
            jScrollPane17 = new javax.swing.JScrollPane();
            jTable_van_pay_my_checks = new javax.swing.JTable();
            jButton46 = new javax.swing.JButton();
            jButton47 = new javax.swing.JButton();
            jPanel40 = new javax.swing.JPanel();
            jScrollPane21 = new javax.swing.JScrollPane();
            jTable_ven_edorsed_checks = new javax.swing.JTable();
            jButton48 = new javax.swing.JButton();
            jButton49 = new javax.swing.JButton();
            jButton_search_ven_name_pay = new javax.swing.JButton();
            jLabel_ven_acc_sum = new javax.swing.JLabel();
            jLabel_in_Tahseel = new javax.swing.JLabel();
            jLabel84 = new javax.swing.JLabel();
            jComboBox_ven_name_pay = new javax.swing.JComboBox();
            jButton_Tahseel_details = new javax.swing.JButton();
            jButton74 = new javax.swing.JButton();
            jPanel_ven_acount_details = new javax.swing.JPanel();
            jScrollPane24 = new javax.swing.JScrollPane();
            jScrollPane25 = new javax.swing.JScrollPane();
            jTable_show_ven_account_details = new javax.swing.JTable();
            jPanel41 = new javax.swing.JPanel();
            jLabel85 = new javax.swing.JLabel();
            jComboBox_ven_name_for_account_details = new javax.swing.JComboBox();
            jButton50 = new javax.swing.JButton();
            jButton51 = new javax.swing.JButton();
            jButton52 = new javax.swing.JButton();
            jButton53 = new javax.swing.JButton();
            jButton54 = new javax.swing.JButton();
            jButton55 = new javax.swing.JButton();
            jButton56 = new javax.swing.JButton();
            jPanel_ven_names = new javax.swing.JPanel();
            jScrollPane26 = new javax.swing.JScrollPane();
            jTable_shoe_ven_names = new javax.swing.JTable();
            jTabbedPane4 = new javax.swing.JTabbedPane();
            jPanel_add_ven = new javax.swing.JPanel();
            jLabel86 = new javax.swing.JLabel();
            jLabel87 = new javax.swing.JLabel();
            jLabel88 = new javax.swing.JLabel();
            jTextField_ven_name_to_add = new javax.swing.JTextField();
            jTextField_ven_addres_to_add = new javax.swing.JTextField();
            jTextField_ven_phone_to_add = new javax.swing.JTextField();
            jButton_add_new_ven = new javax.swing.JButton();
            jPanel_modify_ven_name = new javax.swing.JPanel();
            jButton57 = new javax.swing.JButton();
            jPanel42 = new javax.swing.JPanel();
            jTextField21 = new javax.swing.JTextField();
            jTextField22 = new javax.swing.JTextField();
            jTextField23 = new javax.swing.JTextField();
            jLabel89 = new javax.swing.JLabel();
            jLabel90 = new javax.swing.JLabel();
            jLabel91 = new javax.swing.JLabel();
            jLabel92 = new javax.swing.JLabel();
            jPanel_del_ven_name = new javax.swing.JPanel();
            jLabel93 = new javax.swing.JLabel();
            jLabel94 = new javax.swing.JLabel();
            jButton58 = new javax.swing.JButton();
            jButton59 = new javax.swing.JButton();
            jPanel_return_ven_bill = new javax.swing.JPanel();
            jScrollPane27 = new javax.swing.JScrollPane();
            jTable_return_ven_bill_items = new javax.swing.JTable();
            jPanel43 = new javax.swing.JPanel();
            jButton_del_return_ven_bill_item = new javax.swing.JButton();
            jButton_return_ven_bill_insert = new javax.swing.JButton();
            jPanel44 = new javax.swing.JPanel();
            jLabel95 = new javax.swing.JLabel();
            jComboBox_van_name_return_bill = new javax.swing.JComboBox();
            jTextField_return_ven_bill_value = new javax.swing.JTextField();
            jLabel96 = new javax.swing.JLabel();
            jLabel97 = new javax.swing.JLabel();
            jLabel98 = new javax.swing.JLabel();
            jDateChooser_return_ven_bill_date = new com.toedter.calendar.JDateChooser();
            jComboBox_return_ven_store = new javax.swing.JComboBox();
            jScrollPane28 = new javax.swing.JScrollPane();
            jTextArea_return_ven_bill_note = new javax.swing.JTextArea();
            jLabel99 = new javax.swing.JLabel();
            jButton60 = new javax.swing.JButton();
            jLabel100 = new javax.swing.JLabel();
            jTextField_return_ven_bil_numberl = new javax.swing.JTextField();
            jPanel45 = new javax.swing.JPanel();
            jButton61 = new javax.swing.JButton();
            jLabel101 = new javax.swing.JLabel();
            jTextField_return_ven_barcode = new javax.swing.JTextField();
            jButton_return_ven_bill_last_price = new javax.swing.JButton();
            jPanel66 = new javax.swing.JPanel();
            jPanel67 = new javax.swing.JPanel();
            jScrollPane42 = new javax.swing.JScrollPane();
            jTable18 = new javax.swing.JTable();
            jPanel68 = new javax.swing.JPanel();
            jDateChooser9 = new com.toedter.calendar.JDateChooser();
            jLabel141 = new javax.swing.JLabel();
            jButton70 = new javax.swing.JButton();
            jPanel69 = new javax.swing.JPanel();
            jCheckBox9 = new javax.swing.JCheckBox();
            jCheckBox10 = new javax.swing.JCheckBox();
            jCheckBox11 = new javax.swing.JCheckBox();
            jCheckBox12 = new javax.swing.JCheckBox();
            jLabel142 = new javax.swing.JLabel();
            jDateChooser10 = new com.toedter.calendar.JDateChooser();
            jLabel143 = new javax.swing.JLabel();
            jMenuBar1 = new javax.swing.JMenuBar();
            jMenu2 = new javax.swing.JMenu();
            jMenu3 = new javax.swing.JMenu();
            jMenuItem2 = new javax.swing.JMenuItem();
            jMenuItem3 = new javax.swing.JMenuItem();
            jMenu4 = new javax.swing.JMenu();
            jMenuItem1 = new javax.swing.JMenuItem();
            jMenuItem4 = new javax.swing.JMenuItem();
            jMenuItem26 = new javax.swing.JMenuItem();
            jMenuItem5 = new javax.swing.JMenuItem();
            jMenuItem6 = new javax.swing.JMenuItem();
            jMenuItem7 = new javax.swing.JMenuItem();
            jMenuItem8 = new javax.swing.JMenuItem();
            jMenu5 = new javax.swing.JMenu();
            jMenuItem9 = new javax.swing.JMenuItem();
            jMenuItem10 = new javax.swing.JMenuItem();
            jMenuItem12 = new javax.swing.JMenuItem();
            jMenuItem13 = new javax.swing.JMenuItem();
            jMenuItem14 = new javax.swing.JMenuItem();
            jMenuItem15 = new javax.swing.JMenuItem();
            jMenuItem16 = new javax.swing.JMenuItem();
            jMenuItem17 = new javax.swing.JMenuItem();
            jMenuItem18 = new javax.swing.JMenuItem();
            jMenuItem27 = new javax.swing.JMenuItem();
            jMenuItem19 = new javax.swing.JMenuItem();
            jMenuItem20 = new javax.swing.JMenuItem();
            jMenuItem11 = new javax.swing.JMenuItem();
            jMenuItem23 = new javax.swing.JMenuItem();
            jMenuItem24 = new javax.swing.JMenuItem();
            jMenuItem25 = new javax.swing.JMenuItem();
            jMenu6 = new javax.swing.JMenu();
            jMenuItem21 = new javax.swing.JMenuItem();
            jMenuItem22 = new javax.swing.JMenuItem();
            jMenu1 = new javax.swing.JMenu();

            show_items.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
            show_items.setForeground(new java.awt.Color(255, 0, 0));
            show_items.setText("تفصيل الحركة");
            show_items.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    show_itemsActionPerformed(evt);
                }
            });
            jPopupMenu1.add(show_items);

            show_image.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
            show_image.setForeground(new java.awt.Color(255, 0, 0));
            show_image.setText("عرض الصورة");
            show_image.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    show_imageActionPerformed(evt);
                }
            });
            jPopupMenu1.add(show_image);

            modify_record.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
            modify_record.setForeground(new java.awt.Color(255, 0, 0));
            modify_record.setText("تعديل القيد");
            modify_record.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    modify_recordActionPerformed(evt);
                }
            });
            jPopupMenu1.add(modify_record);

            show_note.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
            show_note.setForeground(new java.awt.Color(255, 0, 0));
            show_note.setText("عرض الملاحظة");
            show_note.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    show_noteActionPerformed(evt);
                }
            });
            jPopupMenu1.add(show_note);

            delete_record.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
            delete_record.setForeground(new java.awt.Color(255, 0, 0));
            delete_record.setText("حذف القيد");
            delete_record.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    delete_recordActionPerformed(evt);
                }
            });
            jPopupMenu1.add(delete_record);

            copy_of_bill.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
            copy_of_bill.setForeground(new java.awt.Color(255, 0, 0));
            copy_of_bill.setText("نسخ الفاتورة");
            copy_of_bill.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    copy_of_billActionPerformed(evt);
                }
            });
            jPopupMenu1.add(copy_of_bill);

            direct_print.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
            direct_print.setForeground(new java.awt.Color(255, 0, 51));
            direct_print.setText("طباعة مباشرة");
            direct_print.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    direct_printActionPerformed(evt);
                }
            });
            jPopupMenu1.add(direct_print);

            jTable2.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null}
                },
                new String [] {
                    "Title 1", "Title 2", "Title 3", "Title 4"
                }
            ));
            jScrollPane7.setViewportView(jTable2);

            customer_bill.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            customer_bill.setTitle("عرض اصناف الفاتورة");

            jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jScrollPane10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

            jTable6.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 0)));
            jTable6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
            jTable6.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null}
                },
                new String [] {
                    "Title 1", "Title 2", "Title 3", "Title 4"
                }
            ));
            jTable6.setRowHeight(30);
            jTable4.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            jScrollPane10.setViewportView(jTable6);

            jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

            jLabel31.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
            jLabel31.setForeground(new java.awt.Color(255, 0, 51));
            jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabel31.setText("jLabel26");

            jLabel32.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
            jLabel32.setForeground(new java.awt.Color(255, 0, 51));
            jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabel32.setText("jLabel30");

            jLabel33.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
            jLabel33.setForeground(new java.awt.Color(255, 0, 51));
            jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabel33.setText("jLabel31");

            jButton20.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
            jButton20.setText("طباعة الفاتورة");
            jButton20.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton20ActionPerformed(evt);
                }
            });

            jLabel57.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
            jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabel57.setText("jLabel26");

            jLabel39.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
            jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabel39.setText("date");

            jTextField20.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
            jTextField20.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

            jLabel43.setText("ملاحظة اضافية:");

            javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
            jPanel14.setLayout(jPanel14Layout);
            jPanel14Layout.setHorizontalGroup(
                jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel14Layout.createSequentialGroup()
                            .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(36, 36, 36)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jButton20, javax.swing.GroupLayout.DEFAULT_SIZE, 739, Short.MAX_VALUE)
                        .addComponent(jLabel57, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                            .addComponent(jTextField20)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel43)
                            .addGap(48, 48, 48)
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap())
            );
            jPanel14Layout.setVerticalGroup(
                jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel14Layout.createSequentialGroup()
                            .addGap(8, 8, 8)
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel14Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel31)
                        .addComponent(jLabel32)
                        .addComponent(jLabel33))
                    .addGap(11, 11, 11)
                    .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
            jPanel13.setLayout(jPanel13Layout);
            jPanel13Layout.setHorizontalGroup(
                jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
                        .addContainerGap()))
            );
            jPanel13Layout.setVerticalGroup(
                jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                    .addContainerGap(396, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(181, Short.MAX_VALUE)))
            );

            javax.swing.GroupLayout customer_billLayout = new javax.swing.GroupLayout(customer_bill.getContentPane());
            customer_bill.getContentPane().setLayout(customer_billLayout);
            customer_billLayout.setHorizontalGroup(
                customer_billLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(customer_billLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );
            customer_billLayout.setVerticalGroup(
                customer_billLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(customer_billLayout.createSequentialGroup()
                    .addGap(33, 33, 33)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );

            searchCustomerName.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

            jLabel6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
            jLabel6.setText("إسم الزبون");

            jTextField2.setBackground(new java.awt.Color(0, 204, 102));
            jTextField2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
            jTextField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
            jTextField2.setToolTipText("");
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

            javax.swing.GroupLayout searchCustomerNameLayout = new javax.swing.GroupLayout(searchCustomerName.getContentPane());
            searchCustomerName.getContentPane().setLayout(searchCustomerNameLayout);
            searchCustomerNameLayout.setHorizontalGroup(
                searchCustomerNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(searchCustomerNameLayout.createSequentialGroup()
                    .addContainerGap(15, Short.MAX_VALUE)
                    .addGroup(searchCustomerNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, searchCustomerNameLayout.createSequentialGroup()
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel6))
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
            );
            searchCustomerNameLayout.setVerticalGroup(
                searchCustomerNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(searchCustomerNameLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(searchCustomerNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            searchItemName.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            searchItemName.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
                public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                }
                public void windowLostFocus(java.awt.event.WindowEvent evt) {
                    searchItemNameWindowLostFocus(evt);
                }
            });

            jLabel34.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
            jLabel34.setText("اسم الصنف");

            jTextField4.setBackground(new java.awt.Color(0, 204, 102));
            jTextField4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
            jTextField4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
            jTextField4.setToolTipText("");
            jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTextField4KeyPressed(evt);
                }
            });

            /*
            jTable7.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {

                }
            ));
            */
            jTable7.getTableHeader().setFont(new Font("Arial",Font.BOLD,16));
            jTable7.setRowHeight(30);
            jTable7.setFont(new java.awt.Font("Arial", 0, 18));
            ////////////////////////
            jTable7.setGridColor(new java.awt.Color(255, 0, 0));
            jTable7.setShowGrid(true);
            //////////////////////////
            jTable7.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            /////////////////////////
            jTable7.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                public void mouseMoved(java.awt.event.MouseEvent evt) {
                    jTable7MouseMoved(evt);
                }
            });
            jTable7.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    jTable7MousePressed(evt);
                }
            });
            jTable7.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    jTable7KeyReleased(evt);
                }
            });
            ((DefaultCellEditor) jTable7.getDefaultEditor(Object.class)).setClickCountToStart(1);
            jScrollPane9.setViewportView(jTable7);

            jCheckBox2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            jCheckBox2.setSelected(true);
            jCheckBox2.setText("التسعيرة الاخيرة للزبون المختار");
            jCheckBox2.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

            javax.swing.GroupLayout searchItemNameLayout = new javax.swing.GroupLayout(searchItemName.getContentPane());
            searchItemName.getContentPane().setLayout(searchItemNameLayout);
            searchItemNameLayout.setHorizontalGroup(
                searchItemNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, searchItemNameLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(searchItemNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
                        .addGroup(searchItemNameLayout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(jCheckBox2)
                            .addGap(123, 123, 123)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap())
            );
            searchItemNameLayout.setVerticalGroup(
                searchItemNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(searchItemNameLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(searchItemNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel34)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCheckBox2))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            search_items_in_customer_bills.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            search_items_in_customer_bills.setTitle("البحث في فواتير المشتريات");

            jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

            jCheckBox1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
            jCheckBox1.setText("في فواتير جميع الزبائن");

            jLabel55.setDisplayedMnemonic('\u0641');
            jLabel55.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
            jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabel55.setText("فواتير زبون محدد:");

            jButton16.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
            jButton16.setText("بحث");
            jButton16.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton16ActionPerformed(evt);
                }
            });

            jTextField29.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
            jTextField29.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
            jTextField29.setEditable(false);

            jLabel56.setDisplayedMnemonic('\u0641');
            jLabel56.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
            jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabel56.setText("الصنف :");

            jButton17.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
            jButton17.setText("بحث");
            jButton17.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton17ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
            jPanel8.setLayout(jPanel8Layout);
            jPanel8Layout.setHorizontalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addContainerGap(409, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jCheckBox1, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                    .addComponent(jTextField29, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton16))
                                .addComponent(jButton17, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel56)
                                .addComponent(jLabel55))))
                    .addContainerGap())
            );

            jPanel8Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel55, jLabel56});

            jPanel8Layout.setVerticalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jCheckBox1)
                    .addGap(18, 18, 18)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel55)
                        .addComponent(jButton16)
                        .addComponent(jTextField29, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel56)
                        .addComponent(jButton17))
                    .addContainerGap(18, Short.MAX_VALUE))
            );

            jPanel8Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton16, jCheckBox1, jLabel55, jTextField29});

            jPanel30.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            jPanel30.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

            jTable11.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
            jTable11.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {

                }
            ));
            jTable11.setRowHeight(25);
            jScrollPane18.setViewportView(jTable11);

            javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
            jPanel30.setLayout(jPanel30Layout);
            jPanel30Layout.setHorizontalGroup(
                jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane18, javax.swing.GroupLayout.DEFAULT_SIZE, 844, Short.MAX_VALUE)
                    .addContainerGap())
            );
            jPanel30Layout.setVerticalGroup(
                jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel30Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout search_items_in_customer_billsLayout = new javax.swing.GroupLayout(search_items_in_customer_bills.getContentPane());
            search_items_in_customer_bills.getContentPane().setLayout(search_items_in_customer_billsLayout);
            search_items_in_customer_billsLayout.setHorizontalGroup(
                search_items_in_customer_billsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(search_items_in_customer_billsLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(search_items_in_customer_billsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
            );
            search_items_in_customer_billsLayout.setVerticalGroup(
                search_items_in_customer_billsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(search_items_in_customer_billsLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );

            jDateChooser4.setDate(get_date_jdate());
            jDateChooser4.setBackground(new java.awt.Color(0, 255, 255));
            jDateChooser4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            jDateChooser4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

            javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
            jPanel22.setLayout(jPanel22Layout);
            jPanel22Layout.setHorizontalGroup(
                jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 241, Short.MAX_VALUE)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(39, Short.MAX_VALUE)))
            );
            jPanel22Layout.setVerticalGroup(
                jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 47, Short.MAX_VALUE)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            );

            update_or_show_returned_bill_items.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

            jTable15.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            jTable15.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
            jTable15.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "الصنف", "الوحدة", "الكمية", "سعر الوحدة","المبلغ","ملاحظات"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class,java.lang.String.class, java.lang.Float.class, java.lang.Float.class,java.lang.Object.class,java.lang.String.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
            jTable15.setRowHeight(30);
            jTable15.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    jTable15MouseReleased(evt);
                }
            });
            jScrollPane22.setViewportView(jTable15);
            jTableCustomization_fayez_2(jTable15);
            jTable15.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            renderer_jTable_obj.Renderer (jTable15);
            jTable15.getModel().addTableModelListener(new TableModelListener()
                {
                    @Override
                    public void tableChanged(TableModelEvent evt)
                    {
                        try{
                            jTextField36.setText(Float.toString(prepare_bill_jtable15_returns()));
                        }catch(Exception e)
                        {Joptionpane_message(e.getMessage());
                        }
                    }
                });

                jTable15.putClientProperty("terminateEditOnFocusLost", true);

                jTable15.getColumnModel().getColumn(2).setMaxWidth(100);
                jTable15.getColumnModel().getColumn(3).setMaxWidth(100);
                jTable15.getColumnModel().getColumn(4).setMaxWidth(180);

                jPanel37.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

                jButton39.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jButton39.setText("حذف صنف");
                jButton39.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton39ActionPerformed(evt);
                    }
                });

                jButton40.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jButton40.setText("إدخال البيانات");
                jButton40.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton40ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
                jPanel37.setLayout(jPanel37Layout);
                jPanel37Layout.setHorizontalGroup(
                    jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton39)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton40, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );
                jPanel37Layout.setVerticalGroup(
                    jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton39, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                );

                jPanel38.setBorder(javax.swing.BorderFactory.createEtchedBorder());

                jLabel65.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel65.setForeground(new java.awt.Color(204, 0, 0));
                jLabel65.setText("قيمة الفاتورة:");

                jComboBox10.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jComboBox10.setMaximumRowCount(14);
                jComboBox10.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));
                jComboBox10.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                try {
                    r = conn_obj.conn_exec("select customer_name from customers order by customer_name ");
                    while (r.next()) {
                        jComboBox10.addItem(r.getString(1));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
                }

                jTextField36.setBackground(new java.awt.Color(102, 255, 204));
                jTextField36.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField36.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                jTextField36.setText("0");
                jTextField36.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
                jTextField36.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
                jTextField36.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField36ActionPerformed(evt);
                    }
                });
                alinment_component(jTextField1);

                jLabel71.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel71.setForeground(new java.awt.Color(204, 0, 0));
                jLabel71.setText("تاريخ الفاتورة:");

                jLabel72.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel72.setForeground(new java.awt.Color(204, 0, 0));
                jLabel72.setText("المستودع:");

                jLabel73.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel73.setForeground(new java.awt.Color(204, 0, 0));
                jLabel73.setText("إسم الزبون :");

                jDateChooser7.setDate(get_date_jdate());
                jDateChooser7.setBackground(new java.awt.Color(0, 255, 255));
                jDateChooser7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jDateChooser7.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N

                jComboBox11.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                try{
                    r = conn_obj.conn_exec("select location_name from location ");//لانها ثوابت في الداتا بيس وهي المواقع ونوع الزبائن
                    while (r.next()) {
                        jComboBox11.addItem(r.getString(1));

                    }
                }
                catch(SQLException e){
                    Joptionpane_message(e.getMessage());
                }

                jTextArea6.setBackground(new java.awt.Color(204, 255, 204));
                jTextArea6.setColumns(20);
                jTextArea6.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextArea6.setLineWrap(true);
                jTextArea6.setRows(5);
                jTextArea6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                jTextArea6.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
                jScrollPane23.setViewportView(jTextArea6);

                jLabel74.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel74.setForeground(new java.awt.Color(204, 0, 0));
                jLabel74.setText("ملاحظات :");

                jButton41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                jButton41.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton41ActionPerformed(evt);
                    }
                });

                jLabel60.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel60.setForeground(new java.awt.Color(204, 0, 0));
                jLabel60.setText("رقم الفاتورة:");

                jTextField31.setBackground(new java.awt.Color(102, 255, 102));
                jTextField31.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField31.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField31ActionPerformed(evt);
                    }
                });
                alinment_component(jTextField15);

                javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
                jPanel38.setLayout(jPanel38Layout);
                jPanel38Layout.setHorizontalGroup(
                    jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel38Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                        .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel38Layout.createSequentialGroup()
                                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel38Layout.createSequentialGroup()
                                        .addGap(118, 118, 118)
                                        .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel38Layout.createSequentialGroup()
                                        .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel38Layout.createSequentialGroup()
                                        .addComponent(jDateChooser7, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel71, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel38Layout.createSequentialGroup()
                                        .addComponent(jTextField31)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel38Layout.createSequentialGroup()
                                .addComponent(jComboBox11, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel72)
                                .addGap(18, 18, 18)
                                .addComponent(jButton41, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                );
                jPanel38Layout.setVerticalGroup(
                    jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel38Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton41, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel73)
                                .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel72)
                                .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel38Layout.createSequentialGroup()
                                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel65)
                                    .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel38Layout.createSequentialGroup()
                                        .addComponent(jDateChooser7, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(7, 7, 7))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel38Layout.createSequentialGroup()
                                        .addComponent(jLabel71)
                                        .addGap(18, 18, 18)))
                                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel60)
                                    .addComponent(jTextField31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel38Layout.createSequentialGroup()
                                .addComponent(jLabel74)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel23))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                AutoCompleteDecorator.decorate(this.jComboBox10);

                jPanel39.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jPanel39.setPreferredSize(new java.awt.Dimension(626, 50));

                jButton42.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jButton42.setForeground(new java.awt.Color(204, 0, 0));
                jButton42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                jButton42.setText("بحث بالاصناف");
                jButton42.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton42ActionPerformed(evt);
                    }
                });

                jLabel75.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel75.setForeground(new java.awt.Color(204, 0, 0));
                jLabel75.setText("باركود:");

                jTextField37.setBackground(new java.awt.Color(102, 255, 102));
                jTextField37.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jTextField37.setPreferredSize(new java.awt.Dimension(6, 25));
                jTextField37.addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextField37FocusLost(evt);
                    }
                });
                jTextField37.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField37ActionPerformed(evt);
                    }
                });
                alinment_component(jTextField15);

                jButton43.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jButton43.setForeground(new java.awt.Color(204, 0, 0));
                jButton43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                jButton43.setText("التسعيرة الاخيرة");
                jButton43.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton43ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
                jPanel39.setLayout(jPanel39Layout);
                jPanel39Layout.setHorizontalGroup(
                    jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel39Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField37, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton43, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton42, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );
                jPanel39Layout.setVerticalGroup(
                    jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel39Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton42))
                            .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel75, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())
                );

                javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
                jPanel36.setLayout(jPanel36Layout);
                jPanel36Layout.setHorizontalGroup(
                    jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel36Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel39, javax.swing.GroupLayout.DEFAULT_SIZE, 1261, Short.MAX_VALUE)
                            .addComponent(jPanel37, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel36Layout.createSequentialGroup()
                                .addGap(548, 548, 548)
                                .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(29, 29, 29))
                            .addComponent(jScrollPane22))
                        .addContainerGap())
                );
                jPanel36Layout.setVerticalGroup(
                    jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel36Layout.createSequentialGroup()
                        .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane22, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(141, Short.MAX_VALUE))
                );

                javax.swing.GroupLayout update_or_show_returned_bill_itemsLayout = new javax.swing.GroupLayout(update_or_show_returned_bill_items.getContentPane());
                update_or_show_returned_bill_items.getContentPane().setLayout(update_or_show_returned_bill_itemsLayout);
                update_or_show_returned_bill_itemsLayout.setHorizontalGroup(
                    update_or_show_returned_bill_itemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 1281, Short.MAX_VALUE)
                    .addGroup(update_or_show_returned_bill_itemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(update_or_show_returned_bill_itemsLayout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE)))
                );
                update_or_show_returned_bill_itemsLayout.setVerticalGroup(
                    update_or_show_returned_bill_itemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 672, Short.MAX_VALUE)
                    .addGroup(update_or_show_returned_bill_itemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(update_or_show_returned_bill_itemsLayout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE)))
                );

                searchvendorName.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

                jLabel102.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jLabel102.setText("إسم الزبون");

                jTextField24.setBackground(new java.awt.Color(0, 204, 102));
                jTextField24.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jTextField24.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                jTextField24.setToolTipText("");
                jTextField24.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField24ActionPerformed(evt);
                    }
                });
                jTextField24.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyPressed(java.awt.event.KeyEvent evt) {
                        jTextField24KeyPressed(evt);
                    }
                });

                jTable_ven_names.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
                jTable_ven_names.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {
                        {null},
                        {null},
                        {null},
                        {null}
                    },
                    new String [] {
                        "اسم الزبون"
                    }
                ));
                jTable_ven_names.setRowHeight(30);
                jTable_ven_names.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        jTable_ven_namesMouseClicked(evt);
                    }
                    public void mousePressed(java.awt.event.MouseEvent evt) {
                        jTable_ven_namesMousePressed(evt);
                    }
                });
                jScrollPane29.setViewportView(jTable_ven_names);

                javax.swing.GroupLayout searchvendorNameLayout = new javax.swing.GroupLayout(searchvendorName.getContentPane());
                searchvendorName.getContentPane().setLayout(searchvendorNameLayout);
                searchvendorNameLayout.setHorizontalGroup(
                    searchvendorNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(searchvendorNameLayout.createSequentialGroup()
                        .addContainerGap(15, Short.MAX_VALUE)
                        .addGroup(searchvendorNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, searchvendorNameLayout.createSequentialGroup()
                                .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel102))
                            .addComponent(jScrollPane29, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                );
                searchvendorNameLayout.setVerticalGroup(
                    searchvendorNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(searchvendorNameLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(searchvendorNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel102)
                            .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane29, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                search_ven_ItemName.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                search_ven_ItemName.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
                    public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                    }
                    public void windowLostFocus(java.awt.event.WindowEvent evt) {
                        search_ven_ItemNameWindowLostFocus(evt);
                    }
                });

                jLabel103.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jLabel103.setText("اسم الصنف");

                jTextField25.setBackground(new java.awt.Color(0, 204, 102));
                jTextField25.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jTextField25.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                jTextField25.setToolTipText("");
                jTextField25.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyPressed(java.awt.event.KeyEvent evt) {
                        jTextField25KeyPressed(evt);
                    }
                });

                jTable10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                jTable10.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
                jTable10.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {

                    },
                    new String [] {

                    }
                ));
                jTable10.setRowHeight(30);
                jTable10.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mousePressed(java.awt.event.MouseEvent evt) {
                        jTable10MousePressed(evt);
                    }
                });
                jTable10.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyPressed(java.awt.event.KeyEvent evt) {
                        jTable10KeyPressed(evt);
                    }
                    public void keyReleased(java.awt.event.KeyEvent evt) {
                        jTable10KeyReleased(evt);
                    }
                });
                jScrollPane30.setViewportView(jTable10);

                javax.swing.GroupLayout search_ven_ItemNameLayout = new javax.swing.GroupLayout(search_ven_ItemName.getContentPane());
                search_ven_ItemName.getContentPane().setLayout(search_ven_ItemNameLayout);
                search_ven_ItemNameLayout.setHorizontalGroup(
                    search_ven_ItemNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, search_ven_ItemNameLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(search_ven_ItemNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane30, javax.swing.GroupLayout.DEFAULT_SIZE, 913, Short.MAX_VALUE)
                            .addGroup(search_ven_ItemNameLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel103, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                );
                search_ven_ItemNameLayout.setVerticalGroup(
                    search_ven_ItemNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(search_ven_ItemNameLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(search_ven_ItemNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel103)
                            .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane30, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                show_items_ven.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
                show_items_ven.setForeground(new java.awt.Color(255, 0, 0));
                show_items_ven.setText("تفصيل الحركة");
                show_items_ven.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        show_items_venActionPerformed(evt);
                    }
                });
                jPopupMenu_ven.add(show_items_ven);

                show_image_ven.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
                show_image_ven.setForeground(new java.awt.Color(255, 0, 0));
                show_image_ven.setText("عرض الصورة");
                show_image_ven.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        show_image_venActionPerformed(evt);
                    }
                });
                jPopupMenu_ven.add(show_image_ven);

                modify_record_ven.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
                modify_record_ven.setForeground(new java.awt.Color(255, 0, 0));
                modify_record_ven.setText("تعديل القيد");
                modify_record_ven.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        modify_record_venActionPerformed(evt);
                    }
                });
                jPopupMenu_ven.add(modify_record_ven);

                show_note_ven.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
                show_note_ven.setForeground(new java.awt.Color(255, 0, 0));
                show_note_ven.setText("عرض الملاحظة");
                show_note_ven.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        show_note_venActionPerformed(evt);
                    }
                });
                jPopupMenu_ven.add(show_note_ven);

                delete_record_ven.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
                delete_record_ven.setForeground(new java.awt.Color(255, 0, 0));
                delete_record_ven.setText("حذف القيد");
                delete_record_ven.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        delete_record_venActionPerformed(evt);
                    }
                });
                jPopupMenu_ven.add(delete_record_ven);

                copy_of_bill_ven.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
                copy_of_bill_ven.setForeground(new java.awt.Color(255, 0, 0));
                copy_of_bill_ven.setText("نسخ الفاتورة");
                copy_of_bill_ven.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        copy_of_bill_venActionPerformed(evt);
                    }
                });
                jPopupMenu_ven.add(copy_of_bill_ven);

                update_or_show_returned_bill_items_ven.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

                jTable16.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jTable16.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jTable16.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {

                    },
                    new String [] {
                        "الصنف", "الوحدة", "الكمية", "سعر الوحدة","المبلغ","ملاحظات"
                    }
                ) {
                    Class[] types = new Class [] {
                        java.lang.String.class,java.lang.String.class, java.lang.Float.class, java.lang.Float.class,java.lang.Object.class,java.lang.String.class
                    };

                    public Class getColumnClass(int columnIndex) {
                        return types [columnIndex];
                    }
                });
                jTable16.setRowHeight(30);
                jTable16.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseReleased(java.awt.event.MouseEvent evt) {
                        jTable16MouseReleased(evt);
                    }
                });
                jTableCustomization_modify_vendor_return_bill(jTable16);
                jScrollPane31.setViewportView(jTable16);

                jPanel47.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

                jButton62.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jButton62.setText("حذف صنف");
                jButton62.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton62ActionPerformed(evt);
                    }
                });

                jButton63.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jButton63.setText("إدخال البيانات");
                jButton63.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton63ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel47Layout = new javax.swing.GroupLayout(jPanel47);
                jPanel47.setLayout(jPanel47Layout);
                jPanel47Layout.setHorizontalGroup(
                    jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel47Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton62)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton63, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );
                jPanel47Layout.setVerticalGroup(
                    jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel47Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton63, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton62, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                );

                jPanel48.setBorder(javax.swing.BorderFactory.createEtchedBorder());

                jLabel104.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel104.setForeground(new java.awt.Color(204, 0, 0));
                jLabel104.setText("قيمة الفاتورة:");

                jComboBox12.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jComboBox12.setMaximumRowCount(14);
                jComboBox12.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));
                jComboBox12.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                try {
                    r = conn_obj.conn_exec("select vendor_name from vendors order by vendor_name ");
                    while (r.next()) {
                        jComboBox12.addItem(r.getString(1));
                    }
                } catch (SQLException ex) {
                    //Logger.getLogger(vendors.class.getName()).log(Level.SEVERE, null, ex);
                }

                jTextField38.setBackground(new java.awt.Color(102, 255, 204));
                jTextField38.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField38.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                jTextField38.setText("0");
                jTextField38.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
                jTextField38.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
                jTextField38.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField38ActionPerformed(evt);
                    }
                });
                alinment_component(jTextField_bill_value);

                jLabel105.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel105.setForeground(new java.awt.Color(204, 0, 0));
                jLabel105.setText("تاريخ الفاتورة:");

                jLabel106.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel106.setForeground(new java.awt.Color(204, 0, 0));
                jLabel106.setText("المستودع:");

                jLabel107.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel107.setForeground(new java.awt.Color(204, 0, 0));
                jLabel107.setText("إسم الزبون :");

                jDateChooser7.setDate(get_date_jdate());
                jDateChooser8.setBackground(new java.awt.Color(0, 255, 255));
                jDateChooser8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jDateChooser8.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N

                jComboBox13.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                try{
                    r = conn_obj.conn_exec("select location_name from location ");//لانها ثوابت في الداتا بيس وهي المواقع ونوع الزبائن
                    while (r.next()) {
                        jComboBox13.addItem(r.getString(1));

                    }
                }
                catch(SQLException e){
                    Joptionpane_message(e.getMessage());
                }

                jTextArea7.setBackground(new java.awt.Color(204, 255, 204));
                jTextArea7.setColumns(20);
                jTextArea7.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextArea7.setLineWrap(true);
                jTextArea7.setRows(5);
                jTextArea7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                jTextArea7.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
                jScrollPane32.setViewportView(jTextArea7);

                jLabel108.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel108.setForeground(new java.awt.Color(204, 0, 0));
                jLabel108.setText("ملاحظات :");

                jButton64.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                jButton64.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton64ActionPerformed(evt);
                    }
                });

                jLabel109.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel109.setForeground(new java.awt.Color(204, 0, 0));
                jLabel109.setText("رقم الفاتورة:");

                jTextField32.setBackground(new java.awt.Color(102, 255, 102));
                jTextField32.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField32.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField32ActionPerformed(evt);
                    }
                });
                alinment_component(jTextField_bill_number);

                javax.swing.GroupLayout jPanel48Layout = new javax.swing.GroupLayout(jPanel48);
                jPanel48.setLayout(jPanel48Layout);
                jPanel48Layout.setHorizontalGroup(
                    jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel48Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel110, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                        .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel48Layout.createSequentialGroup()
                                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane32, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel48Layout.createSequentialGroup()
                                        .addGap(118, 118, 118)
                                        .addComponent(jLabel108, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel48Layout.createSequentialGroup()
                                        .addComponent(jTextField38, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel104, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel48Layout.createSequentialGroup()
                                        .addComponent(jDateChooser8, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel105, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel48Layout.createSequentialGroup()
                                        .addComponent(jTextField32)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel109, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel48Layout.createSequentialGroup()
                                .addComponent(jComboBox13, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel106)
                                .addGap(18, 18, 18)
                                .addComponent(jButton64, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel107, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                );
                jPanel48Layout.setVerticalGroup(
                    jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel48Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton64, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel107)
                                .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel106)
                                .addComponent(jComboBox13, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel48Layout.createSequentialGroup()
                                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel104)
                                    .addComponent(jTextField38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel48Layout.createSequentialGroup()
                                        .addComponent(jDateChooser8, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(7, 7, 7))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel48Layout.createSequentialGroup()
                                        .addComponent(jLabel105)
                                        .addGap(18, 18, 18)))
                                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel109)
                                    .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel48Layout.createSequentialGroup()
                                .addComponent(jLabel108)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane32, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel110))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                AutoCompleteDecorator.decorate(this.jComboBox12);

                jPanel49.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jPanel49.setPreferredSize(new java.awt.Dimension(626, 50));

                jButton65.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jButton65.setForeground(new java.awt.Color(204, 0, 0));
                jButton65.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                jButton65.setText("بحث بالاصناف");
                jButton65.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton65ActionPerformed(evt);
                    }
                });

                jLabel111.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel111.setForeground(new java.awt.Color(204, 0, 0));
                jLabel111.setText("باركود:");

                jTextField39.setBackground(new java.awt.Color(102, 255, 102));
                jTextField39.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jTextField39.setPreferredSize(new java.awt.Dimension(6, 25));
                jTextField39.addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextField39FocusLost(evt);
                    }
                });
                jTextField39.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField39ActionPerformed(evt);
                    }
                });
                alinment_component(jTextField_bill_number);

                jButton66.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jButton66.setForeground(new java.awt.Color(204, 0, 0));
                jButton66.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                jButton66.setText("التسعيرة الاخيرة");
                jButton66.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton66ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel49Layout = new javax.swing.GroupLayout(jPanel49);
                jPanel49.setLayout(jPanel49Layout);
                jPanel49Layout.setHorizontalGroup(
                    jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel49Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField39, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel111, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton66, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton65, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );
                jPanel49Layout.setVerticalGroup(
                    jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel49Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton66, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton65))
                            .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel111, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())
                );

                javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
                jPanel46.setLayout(jPanel46Layout);
                jPanel46Layout.setHorizontalGroup(
                    jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel46Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel49, javax.swing.GroupLayout.DEFAULT_SIZE, 1261, Short.MAX_VALUE)
                            .addComponent(jPanel47, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel46Layout.createSequentialGroup()
                                .addGap(548, 548, 548)
                                .addComponent(jPanel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(29, 29, 29))
                            .addComponent(jScrollPane31))
                        .addContainerGap())
                );
                jPanel46Layout.setVerticalGroup(
                    jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel46Layout.createSequentialGroup()
                        .addComponent(jPanel48, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane31, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel47, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(141, Short.MAX_VALUE))
                );

                javax.swing.GroupLayout update_or_show_returned_bill_items_venLayout = new javax.swing.GroupLayout(update_or_show_returned_bill_items_ven.getContentPane());
                update_or_show_returned_bill_items_ven.getContentPane().setLayout(update_or_show_returned_bill_items_venLayout);
                update_or_show_returned_bill_items_venLayout.setHorizontalGroup(
                    update_or_show_returned_bill_items_venLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 1281, Short.MAX_VALUE)
                    .addGroup(update_or_show_returned_bill_items_venLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(update_or_show_returned_bill_items_venLayout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(jPanel46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE)))
                );
                update_or_show_returned_bill_items_venLayout.setVerticalGroup(
                    update_or_show_returned_bill_items_venLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 672, Short.MAX_VALUE)
                    .addGroup(update_or_show_returned_bill_items_venLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(update_or_show_returned_bill_items_venLayout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(jPanel46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE)))
                );

                search_items_in_vendor_bills.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                search_items_in_vendor_bills.setTitle("البحث في فواتير المشتريات");

                jPanel50.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

                jCheckBox8.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jCheckBox8.setText("في فواتير جميع الموردين");

                jLabel112.setDisplayedMnemonic('\u0641');
                jLabel112.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jLabel112.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                jLabel112.setText("فواتير مورد محدد:");

                jButton67.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jButton67.setText("بحث");
                jButton67.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton67ActionPerformed(evt);
                    }
                });

                jTextField33.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jTextField33.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                jTextField29.setEditable(false);

                jLabel113.setDisplayedMnemonic('\u0641');
                jLabel113.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jLabel113.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                jLabel113.setText("الصنف :");

                jButton68.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jButton68.setText("بحث");
                jButton68.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton68ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel50Layout = new javax.swing.GroupLayout(jPanel50);
                jPanel50.setLayout(jPanel50Layout);
                jPanel50Layout.setHorizontalGroup(
                    jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel50Layout.createSequentialGroup()
                        .addContainerGap(409, Short.MAX_VALUE)
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel50Layout.createSequentialGroup()
                                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel50Layout.createSequentialGroup()
                                        .addComponent(jTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton67))
                                    .addComponent(jButton68, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel113)
                                    .addComponent(jLabel112))))
                        .addContainerGap())
                );
                jPanel50Layout.setVerticalGroup(
                    jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel50Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jCheckBox8)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel112)
                            .addComponent(jButton67)
                            .addComponent(jTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel113)
                            .addComponent(jButton68))
                        .addContainerGap(18, Short.MAX_VALUE))
                );

                jPanel51.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jPanel51.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

                jTable14.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jTable14.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {

                    },
                    new String [] {

                    }
                ));
                jTable14.setRowHeight(25);
                jScrollPane33.setViewportView(jTable14);

                javax.swing.GroupLayout jPanel51Layout = new javax.swing.GroupLayout(jPanel51);
                jPanel51.setLayout(jPanel51Layout);
                jPanel51Layout.setHorizontalGroup(
                    jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel51Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane33, javax.swing.GroupLayout.DEFAULT_SIZE, 844, Short.MAX_VALUE)
                        .addContainerGap())
                );
                jPanel51Layout.setVerticalGroup(
                    jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel51Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                javax.swing.GroupLayout search_items_in_vendor_billsLayout = new javax.swing.GroupLayout(search_items_in_vendor_bills.getContentPane());
                search_items_in_vendor_bills.getContentPane().setLayout(search_items_in_vendor_billsLayout);
                search_items_in_vendor_billsLayout.setHorizontalGroup(
                    search_items_in_vendor_billsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(search_items_in_vendor_billsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(search_items_in_vendor_billsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                );
                search_items_in_vendor_billsLayout.setVerticalGroup(
                    search_items_in_vendor_billsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(search_items_in_vendor_billsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                );

                jFrame1_modify_checks_to_anothor_stat.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

                jComboBox1_checks_status.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jComboBox1_checks_status.setMaximumRowCount(14);
                jComboBox1_checks_status.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));
                jComboBox1_checks_status.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

                jLabel136.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel136.setForeground(new java.awt.Color(204, 0, 0));
                jLabel136.setText("حالة الشك:");

                jLabel137.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jLabel137.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel137.setText("من تاريخ :");

                jLabel138.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jLabel138.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel138.setText("الى تاريخ:");

                jDateChooser2_checks_change2.setDate(get_date_jdate());
                jDateChooser2_checks_change2.setBackground(new java.awt.Color(0, 255, 255));
                jDateChooser2_checks_change2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jDateChooser2_checks_change2.setDateFormatString("yyyy/MM/dd");
                jDateChooser2_checks_change2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

                jDateChooser1_checks_change_1.setDate(get_date_jdate());
                jDateChooser1_checks_change_1.setBackground(new java.awt.Color(0, 255, 255));
                jDateChooser1_checks_change_1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jDateChooser1_checks_change_1.setDateFormatString("yyyy/MM/dd");
                jDateChooser1_checks_change_1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

                jButton1_checks_change_order.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jButton1_checks_change_order.setText("تنفيذ");
                jButton1_checks_change_order.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton1_checks_change_orderActionPerformed(evt);
                    }
                });

                jPanel72.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

                jLabel139.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jLabel139.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel139.setText("مسحوبات زبون من تاريخ الى تاريخ");
                jLabel139.setToolTipText("");

                javax.swing.GroupLayout jPanel72Layout = new javax.swing.GroupLayout(jPanel72);
                jPanel72.setLayout(jPanel72Layout);
                jPanel72Layout.setHorizontalGroup(
                    jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel72Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel139, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
                        .addContainerGap())
                );
                jPanel72Layout.setVerticalGroup(
                    jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel72Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel139, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                        .addContainerGap())
                );

                javax.swing.GroupLayout jFrame1_modify_checks_to_anothor_statLayout = new javax.swing.GroupLayout(jFrame1_modify_checks_to_anothor_stat.getContentPane());
                jFrame1_modify_checks_to_anothor_stat.getContentPane().setLayout(jFrame1_modify_checks_to_anothor_statLayout);
                jFrame1_modify_checks_to_anothor_statLayout.setHorizontalGroup(
                    jFrame1_modify_checks_to_anothor_statLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jFrame1_modify_checks_to_anothor_statLayout.createSequentialGroup()
                        .addGroup(jFrame1_modify_checks_to_anothor_statLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrame1_modify_checks_to_anothor_statLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel72, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jFrame1_modify_checks_to_anothor_statLayout.createSequentialGroup()
                                .addGroup(jFrame1_modify_checks_to_anothor_statLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jFrame1_modify_checks_to_anothor_statLayout.createSequentialGroup()
                                        .addGap(54, 54, 54)
                                        .addGroup(jFrame1_modify_checks_to_anothor_statLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jFrame1_modify_checks_to_anothor_statLayout.createSequentialGroup()
                                                .addComponent(jDateChooser2_checks_change2, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel138))
                                            .addGroup(jFrame1_modify_checks_to_anothor_statLayout.createSequentialGroup()
                                                .addGroup(jFrame1_modify_checks_to_anothor_statLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jComboBox1_checks_status, 0, 233, Short.MAX_VALUE)
                                                    .addComponent(jDateChooser1_checks_change_1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jFrame1_modify_checks_to_anothor_statLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel137)
                                                    .addComponent(jLabel136)))))
                                    .addGroup(jFrame1_modify_checks_to_anothor_statLayout.createSequentialGroup()
                                        .addGap(171, 171, 171)
                                        .addComponent(jButton1_checks_change_order)))
                                .addGap(0, 28, Short.MAX_VALUE)))
                        .addContainerGap())
                );
                jFrame1_modify_checks_to_anothor_statLayout.setVerticalGroup(
                    jFrame1_modify_checks_to_anothor_statLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jFrame1_modify_checks_to_anothor_statLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel72, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jFrame1_modify_checks_to_anothor_statLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooser1_checks_change_1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel137))
                        .addGroup(jFrame1_modify_checks_to_anothor_statLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooser2_checks_change2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel138))
                        .addGap(17, 17, 17)
                        .addGroup(jFrame1_modify_checks_to_anothor_statLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel136)
                            .addComponent(jComboBox1_checks_status, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jButton1_checks_change_order)
                        .addContainerGap(46, Short.MAX_VALUE))
                );

                AutoCompleteDecorator.decorate(jComboBox1_checks_status);
                jComboBox1_checks_status.removeAllItems();
                try{
                    r = conn_obj.conn_exec("select status from check_status ");//لانها ثوابت في الداتا بيس وهي المواقع ونوع الزبائن
                    while (r.next()) {
                        jComboBox1_checks_status.addItem(r.getString(1));

                    }
                }catch(SQLException s)
                {

                }

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                setTitle("حسابات الزبائن");
                setExtendedState(6);
                addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosed(java.awt.event.WindowEvent evt) {
                        formWindowClosed(evt);
                    }
                    public void windowClosing(java.awt.event.WindowEvent evt) {
                        formWindowClosing(evt);
                    }
                });

                jTabbedPane3.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                jTabbedPane3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

                jTabbedPane1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jTabbedPane1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
                jTabbedPane1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

                jPanel3.setVerifyInputWhenFocusTarget(false);

                jTable4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jTable4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jTable4.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {

                    },
                    new String [] {
                        "الصنف", "الوحدة", "الكمية", "بونص", "سعر الوحدة"," %الخصم بالمئة","المبلغ","ملاحظات"
                    }

                ) {
                    Class[] types = new Class [] {
                        java.lang.String.class,java.lang.String.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class,java.lang.String.class,java.lang.String.class
                    };

                    public Class getColumnClass(int columnIndex) {
                        return types [columnIndex];
                    }
                });
                jTable4.setRowHeight(30);
                jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseReleased(java.awt.event.MouseEvent evt) {
                        jTable4MouseReleased(evt);
                    }
                });
                jScrollPane5.setViewportView(jTable4);
                jTableCustomization_fayez(jTable4);

                jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

                jButton14.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jButton14.setText("طباعة الفاتورة");
                jButton14.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton14ActionPerformed(evt);
                    }
                });

                jButton18.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jButton18.setText("حذف صنف");
                jButton18.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton18ActionPerformed(evt);
                    }
                });

                jButton3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jButton3.setText("إدخال البيانات");
                jButton3.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton3ActionPerformed(evt);
                    }
                });

                jButton23.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jButton23.setText("نواقص");
                jButton23.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton23ActionPerformed(evt);
                    }
                });

                jButton73.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jButton73.setText("إدخال وطباعة");
                jButton73.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton73ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
                jPanel11.setLayout(jPanel11Layout);
                jPanel11Layout.setHorizontalGroup(
                    jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(322, 322, 322)
                        .addComponent(jButton18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton73, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );
                jPanel11Layout.setVerticalGroup(
                    jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton73, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                jPanel12.setBackground(new java.awt.Color(153, 153, 153));
                jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

                jTextField15.setBackground(new java.awt.Color(204, 255, 204));
                jTextField15.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField15.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField15ActionPerformed(evt);
                    }
                });
                alinment_component(jTextField15);

                jLabel29.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel29.setForeground(new java.awt.Color(204, 0, 0));
                jLabel29.setText("خصم مباشر من الفاتورة :");

                jTextField16.setBackground(new java.awt.Color(204, 255, 204));
                jTextField16.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField16.setText("0");
                jTextField16.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyReleased(java.awt.event.KeyEvent evt) {
                        jTextField16KeyReleased(evt);
                    }
                });
                jTextField16.setHorizontalAlignment(JTextField.RIGHT);

                jLabel26.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel26.setForeground(new java.awt.Color(204, 0, 0));
                jLabel26.setText("رقم الفاتورة:");

                jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel4.setForeground(new java.awt.Color(204, 0, 0));
                jLabel4.setText("خصم مئوي من الفاتورة:");

                jTextField3.setBackground(new java.awt.Color(204, 255, 204));
                jTextField3.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField3.setText("0");
                jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyReleased(java.awt.event.KeyEvent evt) {
                        jTextField3KeyReleased(evt);
                    }
                });
                alinment_component(jTextField3);

                jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/percent.png"))); // NOI18N

                jLabel8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel8.setForeground(new java.awt.Color(204, 0, 0));
                jLabel8.setText("صورة الفاتورة:");

                jButton2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
                jButton2.setText("ادخال");
                jButton2.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton2ActionPerformed(evt);
                    }
                });

                jLabel30.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel30.setForeground(new java.awt.Color(204, 0, 0));
                jLabel30.setText("قيمة الفاتورة قبل الخصم:");

                jTextField17.setEditable(false);
                jTextField17.setBackground(new java.awt.Color(204, 255, 204));
                jTextField17.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField17.setText("0");
                jTextField17.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField17ActionPerformed(evt);
                    }
                });
                alinment_component(jTextField17);

                jButton33.setText("نواقص");
                jButton33.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton33ActionPerformed(evt);
                    }
                });

                jCheckBox3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
                jCheckBox3.setSelected(true);
                jCheckBox3.setText("فحص احتمال الخسارة في المبيع");
                jCheckBox3.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

                javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
                jPanel12.setLayout(jPanel12Layout);
                jPanel12Layout.setHorizontalGroup(
                    jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel30)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                .addComponent(jTextField15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                .addComponent(jTextField16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                        .addComponent(jCheckBox3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton33))
                                    .addGroup(jPanel12Layout.createSequentialGroup()
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addContainerGap())
                );
                jPanel12Layout.setVerticalGroup(
                    jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jButton2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel30)
                                .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton33)
                            .addComponent(jCheckBox3))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                jPanel12Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel26, jTextField15});

                jPanel12Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel29, jLabel30, jLabel4, jLabel7, jTextField16, jTextField17, jTextField3});

                jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

                jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel2.setForeground(new java.awt.Color(204, 0, 0));
                jLabel2.setText("قيمة الفاتورة:");

                jComboBox1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jComboBox1.setMaximumRowCount(14);

                jTextField1.setBackground(new java.awt.Color(204, 255, 204));
                jTextField1.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
                jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                jTextField1.setText("0");
                jTextField1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
                jTextField1.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
                jTextField1.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField1ActionPerformed(evt);
                    }
                });
                alinment_component(jTextField1);

                jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel3.setForeground(new java.awt.Color(204, 0, 0));
                jLabel3.setText("تاريخ الفاتورة:");

                jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel5.setForeground(new java.awt.Color(204, 0, 0));
                jLabel5.setText("المستودع:");

                jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel1.setForeground(new java.awt.Color(204, 0, 0));
                jLabel1.setText("إسم الزبون :");

                jDateChooser1.setDate(get_date_jdate());
                jDateChooser1.setBackground(new java.awt.Color(0, 255, 255));
                jDateChooser1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jDateChooser1.setDateFormatString("yyyy/MM/dd");
                jDateChooser1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
                jDateChooser1.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyPressed(java.awt.event.KeyEvent evt) {
                        jDateChooser1KeyPressed(evt);
                    }
                });

                jComboBox2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

                jTextArea1.setBackground(new java.awt.Color(204, 255, 204));
                jTextArea1.setColumns(20);
                jTextArea1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextArea1.setLineWrap(true);
                jTextArea1.setRows(5);
                jTextArea1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                jTextArea1.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
                jScrollPane1.setViewportView(jTextArea1);
                jTextArea1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

                jLabel9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel9.setForeground(new java.awt.Color(204, 0, 0));
                jLabel9.setText("ملاحظات :");

                jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                jButton19.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton19ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
                jPanel10.setLayout(jPanel10Layout);
                jPanel10Layout.setHorizontalGroup(
                    jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel10Layout.createSequentialGroup()
                                    .addGap(118, 118, 118)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel10Layout.createSequentialGroup()
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel2))
                                .addGroup(jPanel10Layout.createSequentialGroup()
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel10Layout.setVerticalGroup(
                    jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                                    .addGap(62, 62, 62)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel10Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel10Layout.createSequentialGroup()
                                            .addGap(12, 12, 12)
                                            .addComponent(jLabel2)))
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel3)))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                jPanel10Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jComboBox1, jComboBox2, jLabel5});

                jPanel10Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3});

                AutoCompleteDecorator.decorate(this.jComboBox1);

                jPanel15.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

                jButton21.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jButton21.setForeground(new java.awt.Color(204, 0, 0));
                jButton21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                jButton21.setText("بحث بالاصناف");
                jButton21.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton21ActionPerformed(evt);
                    }
                });

                jLabel35.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel35.setForeground(new java.awt.Color(204, 0, 0));
                jLabel35.setText("باركود:");

                jTextField18.setBackground(new java.awt.Color(204, 255, 204));
                jTextField18.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jTextField18.setPreferredSize(new java.awt.Dimension(6, 25));
                jTextField18.addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextField18FocusLost(evt);
                    }
                });
                alinment_component(jTextField15);

                jButton22.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jButton22.setForeground(new java.awt.Color(204, 0, 0));
                jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                jButton22.setText("التسعيرة الاخيرة");
                jButton22.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton22ActionPerformed(evt);
                    }
                });

                jLabel51.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel51.setForeground(new java.awt.Color(204, 0, 0));
                jLabel51.setText("بحث اصناف الفاتورة");

                jTextField27.setBackground(new java.awt.Color(204, 255, 204));
                jTextField27.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jTextField27.setPreferredSize(new java.awt.Dimension(6, 25));
                jTextField27.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyReleased(java.awt.event.KeyEvent evt) {
                        jTextField27KeyReleased(evt);
                    }
                });
                alinment_component(jTextField27);

                jButton13.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jButton13.setText("ارباح");
                jButton13.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton13ActionPerformed(evt);
                    }
                });

                jLabel53.setText("0.0");

                jLabel54.setText("%");

                jButton71.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton71ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
                jPanel15.setLayout(jPanel15Layout);
                jPanel15Layout.setHorizontalGroup(
                    jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton71, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField27, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel51)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                );
                jPanel15Layout.setVerticalGroup(
                    jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel54))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel51))
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton71, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jTextField27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                );

                jPanel15Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton21, jButton22, jLabel35, jLabel51, jTextField18, jTextField27});

                jPanel15Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton13, jButton71});

                javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
                jPanel3.setLayout(jPanel3Layout);
                jPanel3Layout.setHorizontalGroup(
                    jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(47, 47, 47))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())))
                );
                jPanel3Layout.setVerticalGroup(
                    jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                jTabbedPane1.addTab("إضافة فاتورة", jPanel3);

                jPanel5.setPreferredSize(new java.awt.Dimension(1000, 601));
                jPanel5.addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusGained(java.awt.event.FocusEvent evt) {
                        jPanel5FocusGained(evt);
                    }
                });

                jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "بيانات", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 24))); // NOI18N
                jPanel20.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

                jComboBox4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jComboBox4.setMaximumRowCount(14);
                jComboBox4.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBox4ItemStateChanged(evt);
                    }
                });

                jLabel22.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jLabel22.setForeground(new java.awt.Color(255, 0, 0));
                jLabel22.setText("إسم الزبون:");

                jButton28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                jButton28.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton28ActionPerformed(evt);
                    }
                });

                jLabel37.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jLabel37.setForeground(new java.awt.Color(255, 0, 0));
                jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                jLabel37.setText("رصيد في التحصيل:");

                jLabel38.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jLabel38.setForeground(new java.awt.Color(255, 0, 0));
                jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                jLabel38.setText("الرصيد :");

                jButton30.setText("تفصيل التحصيل");
                jButton30.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton30ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
                jPanel20.setLayout(jPanel20Layout);
                jPanel20Layout.setHorizontalGroup(
                    jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton30)
                        .addGap(165, 165, 165)
                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22))
                );
                jPanel20Layout.setVerticalGroup(
                    jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel38)
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel37)
                        .addComponent(jButton30))
                );

                jPanel20Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton28, jComboBox4, jLabel22, jLabel37, jLabel38});

                AutoCompleteDecorator.decorate(this.jComboBox4);

                jPanel21.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

                jPanel52.setBorder(javax.swing.BorderFactory.createEtchedBorder());

                jLabel19.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel19.setForeground(new java.awt.Color(204, 0, 0));
                jLabel19.setText("مسلّم المبالغ:");

                jTextField14.setBackground(new java.awt.Color(204, 255, 204));
                jTextField14.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField14.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                jTextField14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
                jTextField14.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
                jTextField14.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mousePressed(java.awt.event.MouseEvent evt) {
                        jTextField14MousePressed(evt);
                    }
                });

                jLabel15.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel15.setForeground(new java.awt.Color(204, 0, 0));
                jLabel15.setText("مبلغ نقدي:");

                jTextField13.setBackground(new java.awt.Color(204, 255, 204));
                jTextField13.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField13.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                jTextField13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
                jTextField13.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

                jLabel14.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel14.setForeground(new java.awt.Color(204, 0, 0));
                jLabel14.setText("قيمة الدفعة:");

                jLabel21.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel21.setForeground(new java.awt.Color(204, 0, 0));
                jLabel21.setText("ملاحظات :");

                jLabel18.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel18.setForeground(new java.awt.Color(204, 0, 0));
                jLabel18.setText("مستلم المبالغ:");

                jTextField12.setBackground(new java.awt.Color(204, 255, 204));
                jTextField12.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField12.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                jTextField12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
                jTextField12.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
                jTextField12.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField12ActionPerformed(evt);
                    }
                });
                jTextField12.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyReleased(java.awt.event.KeyEvent evt) {
                        jTextField12KeyReleased(evt);
                    }
                });

                jLabel16.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel16.setForeground(new java.awt.Color(204, 0, 0));
                jLabel16.setText("رقم السند:");

                jTextArea2.setBackground(new java.awt.Color(204, 255, 204));
                jTextArea2.setColumns(20);
                jTextArea2.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextArea2.setLineWrap(true);
                jTextArea2.setRows(5);
                jTextArea2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                jTextArea2.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
                jScrollPane4.setViewportView(jTextArea2);

                jTextField19.setBackground(new java.awt.Color(204, 255, 204));
                jTextField19.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField19.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                jTextField19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
                jTextField19.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

                jDateChooser2.setDate(get_date_jdate());
                jDateChooser2.setBackground(new java.awt.Color(0, 255, 255));
                jDateChooser2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jDateChooser2.setDateFormatString("yyyy/MM/dd");
                jDateChooser2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N

                jLabel20.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel20.setForeground(new java.awt.Color(204, 0, 0));
                jLabel20.setText("تاريخ الدفعة:");

                jTextField11.setBackground(new java.awt.Color(204, 255, 204));
                jTextField11.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                jTextField11.setText("0");
                jTextField11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
                jTextField11.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
                jTextField11.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField11ActionPerformed(evt);
                    }
                });
                jTextField11.setEditable(false);

                jLabel140.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel140.setForeground(new java.awt.Color(204, 0, 0));
                jLabel140.setText("قيد خصم:");

                jTextField26.setBackground(new java.awt.Color(204, 255, 204));
                jTextField26.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField26.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                jTextField26.setText("0");
                jTextField26.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
                jTextField26.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
                jTextField26.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField26ActionPerformed(evt);
                    }
                });
                jTextField26.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyReleased(java.awt.event.KeyEvent evt) {
                        jTextField26KeyReleased(evt);
                    }
                });

                javax.swing.GroupLayout jPanel52Layout = new javax.swing.GroupLayout(jPanel52);
                jPanel52.setLayout(jPanel52Layout);
                jPanel52Layout.setHorizontalGroup(
                    jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel52Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel140, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel52Layout.createSequentialGroup()
                                .addGap(118, 118, 118)
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel52Layout.createSequentialGroup()
                                .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel52Layout.createSequentialGroup()
                                .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel52Layout.createSequentialGroup()
                                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel52Layout.createSequentialGroup()
                                .addComponent(jTextField11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel52Layout.createSequentialGroup()
                                .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel52Layout.createSequentialGroup()
                                .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                );
                jPanel52Layout.setVerticalGroup(
                    jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel52Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel52Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel140, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel52Layout.createSequentialGroup()
                                .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel52Layout.createSequentialGroup()
                                        .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel52Layout.createSequentialGroup()
                                        .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel52Layout.createSequentialGroup()
                                                .addGap(2, 2, 2)
                                                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                        .addContainerGap())
                );

                jPanel52Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel14, jTextField11});

                jPanel53.setBorder(javax.swing.BorderFactory.createEtchedBorder());

                jTable13.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jTable13.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jTable13.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {

                    },
                    new String [] {
                        "#", "رقم الشيك", "القيمة", "صاحب الشيك", "الاسم المجيّر", "تاريخ الاستحقاق","البنك"," ملاحظة"
                    }
                ) {
                    Class[] types = new Class [] {
                        java.lang.Integer.class,java.lang.String.class,java.lang.Float.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class,java.lang.String.class,java.lang.String.class
                    };

                    public Class getColumnClass(int columnIndex) {
                        return types [columnIndex];
                    }
                });
                /////add check bank jcombobox
                jcombo_table_bank_names();
                TableColumn fayezColumn = jTable13.getColumnModel().getColumn(6);
                fayezColumn.setCellEditor(new DefaultCellEditor(Bank_jcomboBox));
                /////
                jTable13.setRowHeight(30);
                //TableColumn date_start= jTable13.getColumnModel().getColumn(3);
                //date_start.setCellEditor(new DatePickerCellEditor());
                jScrollPane13.setViewportView(jTable13);
                jTable13.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                renderer_jTable_obj.Renderer (jTable13);

                jTable13.getModel().addTableModelListener(fayez=new TableModelListener()
                    {
                        @Override
                        public void tableChanged(TableModelEvent evt)
                        {
                            prepare_payment_sum();
                        }
                    });

                    jTable13.putClientProperty("terminateEditOnFocusLost", true);
                    jTable13.getColumnModel().getColumn(0).setMaxWidth(50);

                    jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Minus-icon.png"))); // NOI18N
                    jButton12.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            jButton12ActionPerformed(evt);
                        }
                    });

                    jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button-Add-icon.png"))); // NOI18N
                    jButton8.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            jButton8ActionPerformed(evt);
                        }
                    });

                    jButton5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                    jButton5.setText("إدخال البيانات");
                    jButton5.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            jButton5ActionPerformed(evt);
                        }
                    });

                    javax.swing.GroupLayout jPanel53Layout = new javax.swing.GroupLayout(jPanel53);
                    jPanel53.setLayout(jPanel53Layout);
                    jPanel53Layout.setHorizontalGroup(
                        jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel53Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel53Layout.createSequentialGroup()
                                    .addComponent(jButton8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton12)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addComponent(jScrollPane13, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addContainerGap())
                        .addGroup(jPanel53Layout.createSequentialGroup()
                            .addGap(449, 449, 449)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );

                    jPanel53Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton12, jButton8});

                    jPanel53Layout.setVerticalGroup(
                        jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel53Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jButton8)
                                .addComponent(jButton12))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap())
                    );

                    jPanel53Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton12, jButton8});

                    javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
                    jPanel21.setLayout(jPanel21Layout);
                    jPanel21Layout.setHorizontalGroup(
                        jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel21Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel53, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addContainerGap())
                    );
                    jPanel21Layout.setVerticalGroup(
                        jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel21Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jPanel52, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );

                    javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
                    jPanel5.setLayout(jPanel5Layout);
                    jPanel5Layout.setHorizontalGroup(
                        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addContainerGap())
                    );
                    jPanel5Layout.setVerticalGroup(
                        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addGap(6, 6, 6)
                            .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );

                    jTabbedPane1.addTab("الدفعات للزبائن", jPanel5);

                    jPanel1.addComponentListener(new java.awt.event.ComponentAdapter() {
                        public void componentShown(java.awt.event.ComponentEvent evt) {
                            jPanel1ComponentShown(evt);
                        }
                    });

                    jTable3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                    jTable3.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null}
                        },
                        new String [] {
                            "برقين", "Title 2", "Title 3", "Title 4"
                        }

                    )

                );
                jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mousePressed(java.awt.event.MouseEvent evt) {
                        jTable3MousePressed(evt);
                    }
                });
                jScrollPane6.setViewportView(jTable3);
                //fayez modification
                jTable3.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
                //jTable3.setBackground(Color.orange);

                jTable3.setForeground(Color.blue);
                jTable3.setRowHeight(24);

                jTable3.setFont(new Font("Arial", Font.BOLD, 20));

                ////// end fayez modification

                jPanel9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

                jLabel17.setBackground(new java.awt.Color(0, 204, 51));
                jLabel17.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel17.setText("أسماء الزبائن");
                jLabel17.setBorder(javax.swing.BorderFactory.createEtchedBorder());

                jComboBox3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jComboBox3.setMaximumRowCount(14);
                jComboBox3.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBox3ItemStateChanged(evt);
                    }
                });

                jButton9.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
                jButton9.setText("عرض جميع الحركات");
                jButton9.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton9ActionPerformed(evt);
                    }
                });

                jButton6.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
                jButton6.setText("تصفير حساب الزبون");
                jButton6.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton6ActionPerformed(evt);
                    }
                });

                jButton10.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
                jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/printer_icon.png"))); // NOI18N
                jButton10.setText("طباعة");
                jButton10.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton10ActionPerformed(evt);
                    }
                });

                jButton11.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
                jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/list_icon.png"))); // NOI18N
                jButton11.setText("عرض جميع حسابات الزبائن");
                jButton11.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton11ActionPerformed(evt);
                    }
                });

                jButton27.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
                jButton27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                jButton27.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton27ActionPerformed(evt);
                    }
                });

                jButton34.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
                jButton34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/printer_icon.png"))); // NOI18N
                jButton34.setText("حفظ في ملف");
                jButton34.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton34ActionPerformed(evt);
                    }
                });

                jButton38.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
                jButton38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/message_email.png"))); // NOI18N
                jButton38.setText("sending Email");
                jButton38.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton38ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
                jPanel9.setLayout(jPanel9Layout);
                jPanel9Layout.setHorizontalGroup(
                    jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 109, Short.MAX_VALUE)
                        .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel17)
                        .addContainerGap())
                );
                jPanel9Layout.setVerticalGroup(
                    jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel17)
                                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton34)
                                        .addComponent(jButton38))))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                jPanel9Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton10, jButton11, jButton34, jButton6, jButton9, jLabel17});

                AutoCompleteDecorator.decorate(this.jComboBox3);

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane6))
                        .addContainerGap())
                );
                jPanel1Layout.setVerticalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                );

                jTabbedPane1.addTab("حسابات الزبائن", jPanel1);

                jPanel2.addComponentListener(new java.awt.event.ComponentAdapter() {
                    public void componentShown(java.awt.event.ComponentEvent evt) {
                        jPanel2ComponentShown(evt);
                    }
                });

                jTable1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jTable1.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                    },
                    new String [] {
                        "Title 1", "Title 2", "Title 3", "Title 4"
                    }
                ){public boolean isCellEditable(int row, int column){return false;}});
                jTable1.setRowHeight(20);
                jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        jTable1MouseClicked(evt);
                    }
                });
                jScrollPane2.setViewportView(jTable1);
                try{

                }catch(Exception e){

                }
                jTable1.setModel(DbUtils.resultSetToTableModel(r));

                jTabbedPane2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jTabbedPane2.setToolTipText("");
                jTabbedPane2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

                jLabel10.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel10.setForeground(new java.awt.Color(204, 0, 0));
                jLabel10.setText("الاسم الجديد:");

                jLabel11.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel11.setForeground(new java.awt.Color(204, 0, 0));
                jLabel11.setText("العنوان:");

                jLabel12.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel12.setForeground(new java.awt.Color(204, 0, 0));
                jLabel12.setText("رقم الهاتف:");

                jTextField5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jTextField5.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

                jTextField6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

                jTextField7.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

                jButton1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jButton1.setText("تنفيذ");
                jButton1.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton1ActionPerformed(evt);
                    }
                });

                jLabel27.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel27.setForeground(new java.awt.Color(204, 0, 0));
                jLabel27.setText("التصنيف:");

                jComboBox5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
                jComboBox5.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jComboBox5ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
                jPanel7.setLayout(jPanel7Layout);
                jPanel7Layout.setHorizontalGroup(
                    jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap(510, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBox5, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                );
                jPanel7Layout.setVerticalGroup(
                    jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel27)
                                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(12, Short.MAX_VALUE))
                );

                jTabbedPane2.addTab("إضافة اسم ", jPanel7);

                jPanel23.setBorder(javax.swing.BorderFactory.createEtchedBorder());

                jTextField9.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jTextField9.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                jTextField9.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField9ActionPerformed(evt);
                    }
                });

                jTextField10.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jTextField10.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                jTextField10.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField10ActionPerformed(evt);
                    }
                });

                jTextField8.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jTextField8.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                jTextField8.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField8ActionPerformed(evt);
                    }
                });

                jLabel40.setText("الاسم");

                jLabel41.setText("الهاتف");

                jLabel42.setText("العنوان");

                jLabel13.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                jLabel13.setText("حدّد الاسم من الجدول الاعلى");

                jButton4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jButton4.setText("حفظ التعديل");
                jButton4.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton4ActionPerformed(evt);
                    }
                });

                jLabel144.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel144.setForeground(new java.awt.Color(204, 0, 0));
                jLabel144.setText("التصنيف:");

                jComboBox6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
                jComboBox6.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jComboBox6ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
                jPanel23.setLayout(jPanel23Layout);
                jPanel23Layout.setHorizontalGroup(
                    jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addContainerGap(164, Short.MAX_VALUE)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(303, 303, 303))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                                    .addComponent(jComboBox6, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel144, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(287, 287, 287)
                                    .addComponent(jLabel13)
                                    .addContainerGap())
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                                    .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel42)
                                        .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTextField9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTextField8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                                            .addComponent(jLabel40)
                                            .addContainerGap()))))))
                );
                jPanel23Layout.setVerticalGroup(
                    jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel144)
                            .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel23Layout.createSequentialGroup()
                                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel40)
                                    .addComponent(jLabel41)
                                    .addComponent(jLabel42))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20))
                            .addGroup(jPanel23Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))
                );

                javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
                jPanel6.setLayout(jPanel6Layout);
                jPanel6Layout.setHorizontalGroup(
                    jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(35, 35, 35))
                );
                jPanel6Layout.setVerticalGroup(
                    jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                );

                jTabbedPane2.addTab("تعديل إسم", jPanel6);

                jLabel24.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel24.setText("إختر الاسم الذي تريد حذفه من الاعلى:");

                jLabel25.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jLabel25.setForeground(new java.awt.Color(255, 0, 0));

                jButton7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
                jButton7.setText("حذف");
                jButton7.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton7ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
                jPanel4.setLayout(jPanel4Layout);
                jPanel4Layout.setHorizontalGroup(
                    jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap(666, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel24)
                                .addGap(76, 76, 76))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(179, 179, 179))))
                );
                jPanel4Layout.setVerticalGroup(
                    jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25)
                            .addComponent(jLabel24))
                        .addGap(48, 48, 48)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(46, Short.MAX_VALUE))
                );

                jTabbedPane2.addTab("حذف اسم", jPanel4);

                jButton29.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jButton29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                jButton29.setText("بحث");
                jButton29.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton29ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(
                    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTabbedPane2)
                            .addComponent(jScrollPane2))
                        .addGap(18, 18, 18)
                        .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(86, 86, 86))
                );
                jPanel2Layout.setVerticalGroup(
                    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(43, Short.MAX_VALUE))
                );

                jTabbedPane2.getAccessibleContext().setAccessibleName("");
                jTabbedPane2.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

                jTabbedPane1.addTab("أسماء الزبائن", jPanel2);

                jPanel16.addComponentListener(new java.awt.event.ComponentAdapter() {
                    public void componentShown(java.awt.event.ComponentEvent evt) {
                        jPanel16ComponentShown(evt);
                    }
                });

                jTable8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jTable8.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jTable8.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                    },
                    new String [] {
                        "Title 1", "Title 2", "Title 3", "Title 4"
                    }
                ) {
                    Class[] types = new Class [] {
                        java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                    };

                    public Class getColumnClass(int columnIndex) {
                        return types [columnIndex];
                    }
                });
                jTable8.setRowHeight(30);
                jScrollPane11.setViewportView(jTable8);

                jButton24.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jButton24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/printer_icon.png"))); // NOI18N
                jButton24.setText("طباعة");
                jButton24.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton24ActionPerformed(evt);
                    }
                });

                jButton25.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jButton25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/DeleteRed.png"))); // NOI18N
                jButton25.setText("حذف سطور");
                jButton25.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton25ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
                jPanel16.setLayout(jPanel16Layout);
                jPanel16Layout.setHorizontalGroup(
                    jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane11)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addContainerGap(490, Short.MAX_VALUE)
                        .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(511, 511, 511))
                );
                jPanel16Layout.setVerticalGroup(
                    jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton24)
                            .addComponent(jButton25))
                        .addGap(55, 55, 55)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(184, Short.MAX_VALUE))
                );

                jTabbedPane1.addTab("أصناف نواقص", jPanel16);

                jPanel18.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

                jTable9.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        jTable9MouseClicked(evt);
                    }
                });
                jScrollPane12.setViewportView(jTable9);

                jPanel19.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

                jDateChooser3.setDate(get_date_jdate());
                jDateChooser3.setBackground(new java.awt.Color(0, 255, 255));
                jDateChooser3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jDateChooser3.setDateFormatString("yyyy/MM/dd");
                jDateChooser3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

                jLabel36.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jLabel36.setForeground(new java.awt.Color(204, 0, 0));
                jLabel36.setText("البحث من تاريخ");

                jButton26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                jButton26.setText("بحث");
                jButton26.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton26ActionPerformed(evt);
                    }
                });

                jPanel24.setBorder(javax.swing.BorderFactory.createEtchedBorder());

                jCheckBox4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                jCheckBox4.setText("دفعات قبض");
                jCheckBox4.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

                jCheckBox5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                jCheckBox5.setText("فواتير مبيعات");
                jCheckBox5.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

                jCheckBox6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                jCheckBox6.setText("خصومات");
                jCheckBox6.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

                jCheckBox7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                jCheckBox7.setText("فواتير مرتجعات");
                jCheckBox7.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

                javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
                jPanel24.setLayout(jPanel24Layout);
                jPanel24Layout.setHorizontalGroup(
                    jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox4))
                );
                jPanel24Layout.setVerticalGroup(
                    jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBox4)
                            .addComponent(jCheckBox5)
                            .addComponent(jCheckBox6)
                            .addComponent(jCheckBox7))
                        .addContainerGap())
                );

                jLabel44.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

                jDateChooser5.setDate(get_date_jdate());
                jDateChooser5.setBackground(new java.awt.Color(0, 255, 255));
                jDateChooser5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jDateChooser5.setDateFormatString("yyyy/MM/dd");
                jDateChooser5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

                jLabel45.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jLabel45.setForeground(new java.awt.Color(204, 0, 0));
                jLabel45.setText("الى تاريخ:");

                jComboBox7.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jComboBox7.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jComboBox7ActionPerformed(evt);
                    }
                });

                jLabel145.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jLabel145.setForeground(new java.awt.Color(204, 0, 0));
                jLabel145.setText("التصنيف:");

                buttonGroup1.add(jRadioButton1);
                jRadioButton1.setSelected(true);
                jRadioButton1.setText("تاريخ الحركة");
                jRadioButton1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

                buttonGroup1.add(jRadioButton2);
                jRadioButton2.setText("تاريخ تسجيل الحركة");
                jRadioButton2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

                jButton72.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
                jButton72.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/printer_icon.png"))); // NOI18N
                jButton72.setText("طباعة");
                jButton72.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton72ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
                jPanel19.setLayout(jPanel19Layout);
                jPanel19Layout.setHorizontalGroup(
                    jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel19Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jButton26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton72, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel19Layout.createSequentialGroup()
                                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel19Layout.createSequentialGroup()
                                        .addComponent(jRadioButton2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jRadioButton1))
                                    .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox7, 0, 265, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel145)
                                .addGap(16, 16, 16)
                                .addComponent(jDateChooser5, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel45)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                );

                jPanel19Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton26, jButton72});

                jPanel19Layout.setVerticalGroup(
                    jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel19Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jDateChooser5, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                                    .addComponent(jDateChooser3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel19Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel145)
                                        .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 5, Short.MAX_VALUE)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRadioButton1)
                            .addComponent(jRadioButton2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton26, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton72, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())
                );

                jPanel19Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton26, jButton72});

                javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
                jPanel18.setLayout(jPanel18Layout);
                jPanel18Layout.setHorizontalGroup(
                    jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(29, 29, 29))
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 1242, Short.MAX_VALUE)
                                .addContainerGap())))
                );
                jPanel18Layout.setVerticalGroup(
                    jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
                jPanel17.setLayout(jPanel17Layout);
                jPanel17Layout.setHorizontalGroup(
                    jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                );
                jPanel17Layout.setVerticalGroup(
                    jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(35, Short.MAX_VALUE))
                );

                jTabbedPane1.addTab("إحصائيات", jPanel17);

                jTable12.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jTable12.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jTable12.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {

                    },
                    new String [] {
                        "الصنف", "الوحدة", "الكمية", "سعر الوحدة","المبلغ","ملاحظات"
                    }
                ) {
                    Class[] types = new Class [] {
                        java.lang.String.class,java.lang.String.class,java.lang.Float.class, java.lang.Float.class,java.lang.Object.class,java.lang.String.class
                    };

                    public Class getColumnClass(int columnIndex) {
                        return types [columnIndex];
                    }
                });
                jTable12.setRowHeight(30);
                jTable12.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseReleased(java.awt.event.MouseEvent evt) {
                        jTable12MouseReleased(evt);
                    }
                });
                jScrollPane19.setViewportView(jTable12);
                return_customer_jTable(jTable12);

                jPanel32.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

                jButton31.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jButton31.setText("حذف صنف");
                jButton31.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton31ActionPerformed(evt);
                    }
                });

                jButton32.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jButton32.setText("إدخال البيانات");
                jButton32.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton32ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
                jPanel32.setLayout(jPanel32Layout);
                jPanel32Layout.setHorizontalGroup(
                    jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton32, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );
                jPanel32Layout.setVerticalGroup(
                    jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                );

                jPanel34.setBorder(javax.swing.BorderFactory.createEtchedBorder());

                jLabel64.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel64.setForeground(new java.awt.Color(204, 0, 0));
                jLabel64.setText("قيمة الفاتورة:");

                jComboBox8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jComboBox8.setMaximumRowCount(14);
                jComboBox8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));
                jComboBox8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

                jTextField34.setBackground(new java.awt.Color(102, 255, 204));
                jTextField34.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField34.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                jTextField34.setText("0");
                jTextField34.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
                jTextField34.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
                jTextField34.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField34ActionPerformed(evt);
                    }
                });
                alinment_component(jTextField1);

                jLabel66.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel66.setForeground(new java.awt.Color(204, 0, 0));
                jLabel66.setText("تاريخ الفاتورة:");

                jLabel67.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel67.setForeground(new java.awt.Color(204, 0, 0));
                jLabel67.setText("المستودع:");

                jLabel68.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel68.setForeground(new java.awt.Color(204, 0, 0));
                jLabel68.setText("إسم الزبون :");

                jDateChooser6.setDate(get_date_jdate());
                jDateChooser6.setBackground(new java.awt.Color(0, 255, 255));
                jDateChooser6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jDateChooser6.setDateFormatString("yyyy/MM/dd");
                jDateChooser6.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N

                jComboBox9.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

                jTextArea5.setBackground(new java.awt.Color(204, 255, 204));
                jTextArea5.setColumns(20);
                jTextArea5.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextArea5.setLineWrap(true);
                jTextArea5.setRows(5);
                jTextArea5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                jTextArea5.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
                jScrollPane20.setViewportView(jTextArea5);

                jLabel69.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel69.setForeground(new java.awt.Color(204, 0, 0));
                jLabel69.setText("ملاحظات :");

                jButton35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                jButton35.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton35ActionPerformed(evt);
                    }
                });

                jLabel59.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel59.setForeground(new java.awt.Color(204, 0, 0));
                jLabel59.setText("رقم الفاتورة:");

                jTextField30.setBackground(new java.awt.Color(102, 255, 102));
                jTextField30.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField30.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField30ActionPerformed(evt);
                    }
                });
                alinment_component(jTextField15);

                javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
                jPanel34.setLayout(jPanel34Layout);
                jPanel34Layout.setHorizontalGroup(
                    jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addContainerGap(109, Short.MAX_VALUE)
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel34Layout.createSequentialGroup()
                                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel34Layout.createSequentialGroup()
                                        .addGap(118, 118, 118)
                                        .addComponent(jLabel69, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                                        .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                                        .addComponent(jDateChooser6, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                                        .addComponent(jTextField30)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel34Layout.createSequentialGroup()
                                .addComponent(jComboBox9, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel67)
                                .addGap(18, 18, 18)
                                .addComponent(jButton35, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                );

                jPanel34Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jComboBox8, jDateChooser6, jTextField34});

                jPanel34Layout.setVerticalGroup(
                    jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton35, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel68)
                                .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel67)
                                .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel34Layout.createSequentialGroup()
                                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel64)
                                    .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel34Layout.createSequentialGroup()
                                        .addComponent(jDateChooser6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(7, 7, 7))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                                        .addComponent(jLabel66)
                                        .addGap(18, 18, 18)))
                                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel59)
                                    .addComponent(jTextField30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel34Layout.createSequentialGroup()
                                .addComponent(jLabel69)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                jPanel34Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel59, jLabel64, jLabel66, jLabel67, jLabel68, jLabel69});

                AutoCompleteDecorator.decorate(this.jComboBox8);

                jPanel35.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jPanel35.setPreferredSize(new java.awt.Dimension(626, 50));

                jButton36.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jButton36.setForeground(new java.awt.Color(204, 0, 0));
                jButton36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                jButton36.setText("بحث بالاصناف");
                jButton36.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton36ActionPerformed(evt);
                    }
                });

                jLabel70.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel70.setForeground(new java.awt.Color(204, 0, 0));
                jLabel70.setText("باركود:");

                jTextField35.setBackground(new java.awt.Color(102, 255, 102));
                jTextField35.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jTextField35.setPreferredSize(new java.awt.Dimension(6, 25));
                jTextField35.addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextField35FocusLost(evt);
                    }
                });
                jTextField35.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField35ActionPerformed(evt);
                    }
                });
                alinment_component(jTextField15);

                jButton37.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jButton37.setForeground(new java.awt.Color(204, 0, 0));
                jButton37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                jButton37.setText("التسعيرة الاخيرة");
                jButton37.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton37ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
                jPanel35.setLayout(jPanel35Layout);
                jPanel35Layout.setHorizontalGroup(
                    jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField35, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton37, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );
                jPanel35Layout.setVerticalGroup(
                    jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton36))
                            .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel70, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())
                );

                javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
                jPanel31.setLayout(jPanel31Layout);
                jPanel31Layout.setHorizontalGroup(
                    jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, 1266, Short.MAX_VALUE)
                            .addComponent(jPanel32, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addGap(548, 548, 548)
                                .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(29, 29, 29))
                            .addComponent(jScrollPane19))
                        .addContainerGap())
                );
                jPanel31Layout.setVerticalGroup(
                    jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(58, Short.MAX_VALUE))
                );

                jTabbedPane1.addTab("فاتورة مرتجعات", jPanel31);

                jTabbedPane3.addTab("الزبائن", new javax.swing.ImageIcon(getClass().getResource("/images/customers.png")), jTabbedPane1); // NOI18N

                jTabbedPane_vendor_action.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jTabbedPane_vendor_action.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
                jTabbedPane_vendor_action.addChangeListener(new javax.swing.event.ChangeListener() {
                    public void stateChanged(javax.swing.event.ChangeEvent evt) {
                        jTabbedPane_vendor_actionStateChanged(evt);
                    }
                });
                jTabbedPane_vendor_action.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

                jPanel_add_ven_bill.setVerifyInputWhenFocusTarget(false);
                jPanel_add_ven_bill.addComponentListener(new java.awt.event.ComponentAdapter() {
                    public void componentShown(java.awt.event.ComponentEvent evt) {
                        jPanel_add_ven_billComponentShown(evt);
                    }
                });

                jTable_bill_items.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jTable_bill_items.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jTable_bill_items.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {

                    },
                    new String [] {
                        "الصنف", "الوحدة", "الكمية", "بونص", "سعر الوحدة"," %الخصم بالمئة","المبلغ","ملاحظات"
                    }
                ) {
                    Class[] types = new Class [] {
                        java.lang.String.class,java.lang.String.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class,java.lang.Float.class,java.lang.String.class
                    };

                    public Class getColumnClass(int columnIndex) {
                        return types [columnIndex];
                    }
                });
                jTable_bill_items.setRowHeight(30);
                jTable_bill_items.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseReleased(java.awt.event.MouseEvent evt) {
                        jTable_bill_itemsMouseReleased(evt);
                    }
                });
                jScrollPane14.setViewportView(jTable_bill_items);
                jTableCustomization_fayez_vendor(jTable_bill_items);

                jPanel25.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

                jButton_remove_bill_item.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jButton_remove_bill_item.setText("حذف صنف");
                jButton_remove_bill_item.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton_remove_bill_itemActionPerformed(evt);
                    }
                });

                jButton_isert_bill.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jButton_isert_bill.setText("إدخال البيانات");
                jButton_isert_bill.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton_isert_billActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
                jPanel25.setLayout(jPanel25Layout);
                jPanel25Layout.setHorizontalGroup(
                    jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_remove_bill_item)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_isert_bill, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );
                jPanel25Layout.setVerticalGroup(
                    jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton_isert_bill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton_remove_bill_item, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                );

                jPanel26.setBackground(new java.awt.Color(153, 153, 153));
                jPanel26.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

                jTextField_bill_number.setBackground(new java.awt.Color(204, 255, 204));
                jTextField_bill_number.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                alinment_component(jTextField_bill_number);

                jLabel46.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel46.setForeground(new java.awt.Color(204, 0, 0));
                jLabel46.setText("خصم مباشر من الفاتورة :");

                jTextField_bill_discount.setBackground(new java.awt.Color(204, 255, 204));
                jTextField_bill_discount.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField_bill_discount.setText("0");
                jTextField_bill_discount.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyReleased(java.awt.event.KeyEvent evt) {
                        jTextField_bill_discountKeyReleased(evt);
                    }
                });
                jTextField_bill_discount.setHorizontalAlignment(JTextField.RIGHT);

                jLabel28.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel28.setForeground(new java.awt.Color(204, 0, 0));
                jLabel28.setText("رقم الفاتورة:");

                jLabel47.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel47.setForeground(new java.awt.Color(204, 0, 0));
                jLabel47.setText("خصم مئوي من الفاتورة:");

                jTextField_bill_dis_ratio.setBackground(new java.awt.Color(204, 255, 204));
                jTextField_bill_dis_ratio.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField_bill_dis_ratio.setText("0");
                jTextField_bill_dis_ratio.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyReleased(java.awt.event.KeyEvent evt) {
                        jTextField_bill_dis_ratioKeyReleased(evt);
                    }
                });
                alinment_component(jTextField_bill_dis_ratio);

                jLabel48.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                jLabel48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/percent.png"))); // NOI18N

                jLabel49.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel49.setForeground(new java.awt.Color(204, 0, 0));
                jLabel49.setText("صورة الفاتورة:");

                jButton15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
                jButton15.setText("ادخال");
                jButton15.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton15ActionPerformed(evt);
                    }
                });

                jLabel50.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel50.setForeground(new java.awt.Color(204, 0, 0));
                jLabel50.setText("قيمة الفاتورة قبل الخصم:");

                jTextField_bill_before_dis.setEditable(false);
                jTextField_bill_before_dis.setBackground(new java.awt.Color(204, 255, 204));
                jTextField_bill_before_dis.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField_bill_before_dis.setText("0");
                jTextField_bill_before_dis.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField_bill_before_disActionPerformed(evt);
                    }
                });
                alinment_component(jTextField_bill_before_dis);

                javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
                jPanel26.setLayout(jPanel26Layout);
                jPanel26Layout.setHorizontalGroup(
                    jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addComponent(jTextField_bill_before_dis, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel50)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                                .addComponent(jTextField_bill_number)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                                .addComponent(jTextField_bill_discount, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jLabel48)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField_bill_dis_ratio, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                );
                jPanel26Layout.setVerticalGroup(
                    jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField_bill_number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel49)
                            .addComponent(jButton15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_bill_discount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel50)
                                .addComponent(jTextField_bill_before_dis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField_bill_dis_ratio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                jPanel27.setBorder(javax.swing.BorderFactory.createEtchedBorder());

                jLabel52.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel52.setForeground(new java.awt.Color(204, 0, 0));
                jLabel52.setText("قيمة الفاتورة:");

                jCombo_vendor_name.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jCombo_vendor_name.setMaximumRowCount(14);

                jTextField_bill_value.setBackground(new java.awt.Color(204, 255, 204));
                jTextField_bill_value.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
                jTextField_bill_value.setText("0");
                jTextField_bill_value.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
                jTextField_bill_value.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
                jTextField_bill_value.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField_bill_valueActionPerformed(evt);
                    }
                });
                alinment_component(jTextField_bill_value);

                jLabel58.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel58.setForeground(new java.awt.Color(204, 0, 0));
                jLabel58.setText("تاريخ الفاتورة:");

                jLabel61.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel61.setForeground(new java.awt.Color(204, 0, 0));
                jLabel61.setText("المستودع:");

                jLabel62.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel62.setForeground(new java.awt.Color(204, 0, 0));
                jLabel62.setText("إسم المورد :");

                jDateChooser_bill_date.setDate(get_date_jdate());
                jDateChooser_bill_date.setBackground(new java.awt.Color(0, 255, 255));
                jDateChooser_bill_date.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jDateChooser_bill_date.setDateFormatString("yyyy/MM/dd");
                jDateChooser_bill_date.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
                jDateChooser_bill_date.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyPressed(java.awt.event.KeyEvent evt) {
                        jDateChooser_bill_dateKeyPressed(evt);
                    }
                });

                jComboBox_bill_store.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

                jTextArea_bill_note.setBackground(new java.awt.Color(204, 255, 204));
                jTextArea_bill_note.setColumns(20);
                jTextArea_bill_note.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextArea_bill_note.setLineWrap(true);
                jTextArea_bill_note.setRows(5);
                jTextArea_bill_note.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                jTextArea_bill_note.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
                jScrollPane15.setViewportView(jTextArea_bill_note);
                jTextArea_bill_note.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

                jLabel63.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel63.setForeground(new java.awt.Color(204, 0, 0));
                jLabel63.setText("ملاحظات :");

                jButton44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                jButton44.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton44ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
                jPanel27.setLayout(jPanel27Layout);
                jPanel27Layout.setHorizontalGroup(
                    jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addComponent(jComboBox_bill_store, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel61)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel27Layout.createSequentialGroup()
                                        .addGap(118, 118, 118)
                                        .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addComponent(jButton44, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                                        .addComponent(jTextField_bill_value, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel27Layout.createSequentialGroup()
                                        .addComponent(jCombo_vendor_name, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addComponent(jDateChooser_bill_date, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel27Layout.setVerticalGroup(
                    jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton44, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel62)
                                .addComponent(jCombo_vendor_name, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBox_bill_store, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                                    .addComponent(jTextField_bill_value, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel27Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jDateChooser_bill_date, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addComponent(jLabel63)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                AutoCompleteDecorator.decorate(jCombo_vendor_name);

                jPanel28.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

                jButton_search_bill_item.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jButton_search_bill_item.setForeground(new java.awt.Color(204, 0, 0));
                jButton_search_bill_item.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                jButton_search_bill_item.setText("بحث بالاصناف");
                jButton_search_bill_item.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton_search_bill_itemActionPerformed(evt);
                    }
                });

                jLabel76.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel76.setForeground(new java.awt.Color(204, 0, 0));
                jLabel76.setText("باركود:");

                jTextField_search_barcode.setBackground(new java.awt.Color(204, 255, 204));
                jTextField_search_barcode.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                jTextField_search_barcode.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                jTextField_search_barcode.setPreferredSize(new java.awt.Dimension(6, 25));
                jTextField_search_barcode.addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextField_search_barcodeFocusLost(evt);
                    }
                });
                jTextField_search_barcode.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField_search_barcodeActionPerformed(evt);
                    }
                });
                alinment_component(jTextField_bill_number);

                jLabel77.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel77.setForeground(new java.awt.Color(204, 0, 0));
                jLabel77.setText("بحث اصناف الفاتورة");

                jTextField_search_bill_items.setBackground(new java.awt.Color(204, 255, 204));
                jTextField_search_bill_items.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
                jTextField_search_bill_items.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                jTextField_search_bill_items.setPreferredSize(new java.awt.Dimension(6, 25));
                jTextField_search_bill_items.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyReleased(java.awt.event.KeyEvent evt) {
                        jTextField_search_bill_itemsKeyReleased(evt);
                    }
                });
                alinment_component(jTextField_search_bill_items);

                javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
                jPanel28.setLayout(jPanel28Layout);
                jPanel28Layout.setHorizontalGroup(
                    jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addContainerGap(325, Short.MAX_VALUE)
                        .addComponent(jTextField_search_bill_items, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel77)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField_search_barcode, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(137, 137, 137)
                        .addComponent(jButton_search_bill_item, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28))
                );
                jPanel28Layout.setVerticalGroup(
                    jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel77)
                            .addComponent(jTextField_search_barcode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField_search_bill_items, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_search_bill_item, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 2, Short.MAX_VALUE))
                );

                javax.swing.GroupLayout jPanel_add_ven_billLayout = new javax.swing.GroupLayout(jPanel_add_ven_bill);
                jPanel_add_ven_bill.setLayout(jPanel_add_ven_billLayout);
                jPanel_add_ven_billLayout.setHorizontalGroup(
                    jPanel_add_ven_billLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_add_ven_billLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel_add_ven_billLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel_add_ven_billLayout.createSequentialGroup()
                                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(29, 29, 29)))
                        .addGap(18, 18, 18))
                    .addComponent(jScrollPane14)
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );
                jPanel_add_ven_billLayout.setVerticalGroup(
                    jPanel_add_ven_billLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_add_ven_billLayout.createSequentialGroup()
                        .addGroup(jPanel_add_ven_billLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                jTabbedPane_vendor_action.addTab("فاتورة مشتريات", jPanel_add_ven_bill);

                jPanel_ven_pay.addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusGained(java.awt.event.FocusEvent evt) {
                        jPanel_ven_payFocusGained(evt);
                    }
                });

                jButton45.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jButton45.setText("إدخال البيانات");
                jButton45.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton45ActionPerformed(evt);
                    }
                });

                jPanel29.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

                jLabel78.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel78.setForeground(new java.awt.Color(204, 0, 0));
                jLabel78.setText("قيمة السند:");

                jTextField_ven_pay_total.setBackground(new java.awt.Color(204, 255, 204));
                jTextField_ven_pay_total.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField_ven_pay_total.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                jTextField_ven_pay_total.setText("0");
                jTextField_ven_pay_total.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
                jTextField_ven_pay_total.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
                jTextField_ven_pay_total.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField_ven_pay_totalActionPerformed(evt);
                    }
                });
                jTextField_ven_pay_total.setEditable(false);

                jTextField_ven_pay_cash.setBackground(new java.awt.Color(204, 255, 204));
                jTextField_ven_pay_cash.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField_ven_pay_cash.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                jTextField_ven_pay_cash.setText("0");
                jTextField_ven_pay_cash.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
                jTextField_ven_pay_cash.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
                jTextField_ven_pay_cash.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField_ven_pay_cashActionPerformed(evt);
                    }
                });
                jTextField_ven_pay_cash.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyReleased(java.awt.event.KeyEvent evt) {
                        jTextField_ven_pay_cashKeyReleased(evt);
                    }
                });

                jLabel79.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel79.setForeground(new java.awt.Color(204, 0, 0));
                jLabel79.setText("مبلغ نقدي:");

                jTextField_ven_pay_receive.setBackground(new java.awt.Color(204, 255, 204));
                jTextField_ven_pay_receive.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField_ven_pay_receive.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                jTextField_ven_pay_receive.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
                jTextField_ven_pay_receive.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

                jLabel80.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel80.setForeground(new java.awt.Color(204, 0, 0));
                jLabel80.setText("مستلم المبالغ:");

                jTextField_ven_pay_helder.setBackground(new java.awt.Color(204, 255, 204));
                jTextField_ven_pay_helder.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField_ven_pay_helder.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                jTextField_ven_pay_helder.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
                jTextField_ven_pay_helder.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

                jLabel81.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel81.setForeground(new java.awt.Color(204, 0, 0));
                jLabel81.setText("مسلّم المبالغ:");

                jLabel82.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel82.setForeground(new java.awt.Color(204, 0, 0));
                jLabel82.setText("تاريخ الدفعة:");

                jDateChooser_ven_pay_date.setDate(get_date_jdate());
                jDateChooser_ven_pay_date.setBackground(new java.awt.Color(0, 255, 255));
                jDateChooser_ven_pay_date.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jDateChooser_ven_pay_date.setDateFormatString("yyyy/MM/dd");
                jDateChooser_ven_pay_date.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N

                jLabel83.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel83.setForeground(new java.awt.Color(204, 0, 0));
                jLabel83.setText("ملاحظات :");

                jTextArea_ven_pay_note.setBackground(new java.awt.Color(204, 255, 204));
                jTextArea_ven_pay_note.setColumns(20);
                jTextArea_ven_pay_note.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextArea_ven_pay_note.setLineWrap(true);
                jTextArea_ven_pay_note.setRows(5);
                jTextArea_ven_pay_note.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                jTextArea_ven_pay_note.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
                jScrollPane16.setViewportView(jTextArea_ven_pay_note);

                jPanel33.setBackground(new java.awt.Color(204, 204, 255));
                jPanel33.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "شيكات مملوكة", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

                jTable_van_pay_my_checks.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jTable_van_pay_my_checks.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jTable_van_pay_my_checks.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {

                    },
                    new String [] {
                        "#", "رقم الشيك", "القيمة","تاريخ الاستحقاق","البنك"," ملاحظة"
                    }
                ) {
                    Class[] types = new Class [] {
                        java.lang.Integer.class,java.lang.String.class,java.lang.Float.class, java.lang.String.class,java.lang.String.class,java.lang.String.class
                    };

                    public Class getColumnClass(int columnIndex) {
                        return types [columnIndex];
                    }
                });
                jTable_van_pay_my_checks.setRowHeight(30);
                /////add check bank jcombobox
                fayezColumn = jTable_van_pay_my_checks.getColumnModel().getColumn(4);
                fayezColumn.setCellEditor(new DefaultCellEditor(Bank_jcomboBox));
                /////
                jTable_van_pay_my_checks.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mousePressed(java.awt.event.MouseEvent evt) {
                        jTable_van_pay_my_checksMousePressed(evt);
                    }
                });
                jTable_van_pay_my_checks.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                renderer_jTable_obj.Renderer (jTable_van_pay_my_checks);

                jTable_van_pay_my_checks.getModel().addTableModelListener(fayez=new TableModelListener()
                    {
                        @Override
                        public void tableChanged(TableModelEvent evt)
                        {
                            try{
                                float checks_sum=0;
                                for(int i=0;i<jTable_van_pay_my_checks.getRowCount();i++)
                                {
                                    checks_sum=checks_sum+Float.valueOf(jTable_van_pay_my_checks.getValueAt(i, 2).toString());
                                }
                                for(int i=0;i<jTable_ven_edorsed_checks.getRowCount();i++)
                                {
                                    checks_sum=checks_sum+Float.valueOf(jTable_ven_edorsed_checks.getValueAt(i, 3).toString());
                                }
                                float final_sum=checks_sum+Float.valueOf(jTextField_ven_pay_cash.getText());
                                jTextField_ven_pay_total.setText(Float.toString(final_sum));
                            }catch(Exception e)
                            {Joptionpane_message(e.getMessage());
                            }
                        }
                    });

                    jTable_van_pay_my_checks.putClientProperty("terminateEditOnFocusLost", true);
                    jTable_van_pay_my_checks.getColumnModel().getColumn(0).setMaxWidth(50);
                    jScrollPane17.setViewportView(jTable_van_pay_my_checks);

                    javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
                    jPanel33.setLayout(jPanel33Layout);
                    jPanel33Layout.setHorizontalGroup(
                        jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel33Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jScrollPane17)
                            .addContainerGap())
                    );
                    jPanel33Layout.setVerticalGroup(
                        jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    );

                    jButton46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Minus-icon.png"))); // NOI18N
                    jButton46.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            jButton46ActionPerformed(evt);
                        }
                    });

                    jButton47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button-Add-icon.png"))); // NOI18N
                    jButton47.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            jButton47ActionPerformed(evt);
                        }
                    });

                    jPanel40.setBackground(new java.awt.Color(204, 204, 255));
                    jPanel40.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "تجيير شيكات زبائن", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

                    jTable_ven_edorsed_checks.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                    jTable_ven_edorsed_checks.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                    jTable_ven_edorsed_checks.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {

                        },
                        new String [] {
                            "#", "ID" ,"رقم الشيك", "القيمة", "صاحب الشيك", "الاسم المجيّر", "تاريخ الاستحقاق","البنك"," ملاحظة"
                        }
                    ) {
                        Class[] types = new Class [] {
                            java.lang.Integer.class,java.lang.Object.class,java.lang.String.class,java.lang.Float.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class,java.lang.String.class,java.lang.String.class
                        };

                        public Class getColumnClass(int columnIndex) {
                            return types [columnIndex];
                        }
                        public boolean isCellEditable(int row, int column){return false;}
                    }
                );
                jTable_ven_edorsed_checks.setRowHeight(30);
                jTable_ven_edorsed_checks.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mousePressed(java.awt.event.MouseEvent evt) {
                        jTable_ven_edorsed_checksMousePressed(evt);
                    }
                });
                jTable_ven_edorsed_checks.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                renderer_jTable_obj.Renderer (jTable_ven_edorsed_checks);

                jTable_ven_edorsed_checks.getModel().addTableModelListener(fayez=new TableModelListener()
                    {
                        @Override
                        public void tableChanged(TableModelEvent evt)
                        {
                            try{
                                float checks_sum=0;
                                for(int i=0;i<jTable_ven_edorsed_checks.getRowCount();i++)
                                {
                                    checks_sum=checks_sum+Float.valueOf(jTable_ven_edorsed_checks.getValueAt(i, 3).toString());
                                }
                                for(int i=0;i<jTable_van_pay_my_checks.getRowCount();i++)
                                {
                                    checks_sum=checks_sum+Float.valueOf(jTable_van_pay_my_checks.getValueAt(i, 2).toString());
                                }
                                float final_sum=checks_sum+Float.valueOf(jTextField_ven_pay_cash.getText());
                                jTextField_ven_pay_total.setText(Float.toString(final_sum));
                            }catch(Exception e)
                            {Joptionpane_message(e.getMessage());
                            }
                        }
                    });

                    jTable_ven_edorsed_checks.putClientProperty("terminateEditOnFocusLost", true);
                    jTable_ven_edorsed_checks.getColumnModel().getColumn(0).setMaxWidth(50);
                    jScrollPane21.setViewportView(jTable_ven_edorsed_checks);

                    javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
                    jPanel40.setLayout(jPanel40Layout);
                    jPanel40Layout.setHorizontalGroup(
                        jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel40Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jScrollPane21)
                            .addContainerGap())
                    );
                    jPanel40Layout.setVerticalGroup(
                        jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    );

                    jButton48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Minus-icon.png"))); // NOI18N
                    jButton48.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            jButton48ActionPerformed(evt);
                        }
                    });

                    jButton49.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button-Add-icon.png"))); // NOI18N
                    jButton49.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            jButton49ActionPerformed(evt);
                        }
                    });

                    javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
                    jPanel29.setLayout(jPanel29Layout);
                    jPanel29Layout.setHorizontalGroup(
                        jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel29Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel29Layout.createSequentialGroup()
                                    .addGap(10, 10, 10)
                                    .addComponent(jButton49)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton48)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(jPanel29Layout.createSequentialGroup()
                                    .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel29Layout.createSequentialGroup()
                                            .addGap(10, 10, 10)
                                            .addComponent(jButton47)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jButton46)
                                            .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(jPanel29Layout.createSequentialGroup()
                                            .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel83, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                                                    .addComponent(jTextField_ven_pay_receive, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jLabel80, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                                                    .addComponent(jTextField_ven_pay_helder, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jLabel81, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                                            .addComponent(jDateChooser_ven_pay_date, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel82)
                                            .addGap(42, 42, 42)
                                            .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                                                    .addComponent(jTextField_ven_pay_total)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jLabel78, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                                                    .addComponent(jTextField_ven_pay_cash, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jLabel79, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addContainerGap())))
                    );
                    jPanel29Layout.setVerticalGroup(
                        jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel29Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel29Layout.createSequentialGroup()
                                    .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTextField_ven_pay_total, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel78, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextField_ven_pay_receive, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel80, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTextField_ven_pay_cash, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel79, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField_ven_pay_helder, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel81, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel83, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel29Layout.createSequentialGroup()
                                    .addGap(2, 2, 2)
                                    .addComponent(jDateChooser_ven_pay_date, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel82, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                            .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jButton47, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton46, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(2, 2, 2)
                            .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButton48, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton49, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap())
                    );

                    jButton_search_ven_name_pay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                    jButton_search_ven_name_pay.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            jButton_search_ven_name_payActionPerformed(evt);
                        }
                    });

                    jLabel_ven_acc_sum.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                    jLabel_ven_acc_sum.setForeground(new java.awt.Color(255, 0, 0));
                    jLabel_ven_acc_sum.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                    jLabel_ven_acc_sum.setText("الرصيد :");

                    jLabel_in_Tahseel.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                    jLabel_in_Tahseel.setForeground(new java.awt.Color(255, 0, 0));
                    jLabel_in_Tahseel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                    jLabel_in_Tahseel.setText("رصيد في التحصيل:");

                    jLabel84.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                    jLabel84.setForeground(new java.awt.Color(255, 0, 0));
                    jLabel84.setText("إسم المورد:");

                    jComboBox_ven_name_pay.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                    jComboBox_ven_name_pay.setMaximumRowCount(14);
                    jComboBox_ven_name_pay.addItemListener(new java.awt.event.ItemListener() {
                        public void itemStateChanged(java.awt.event.ItemEvent evt) {
                            jComboBox_ven_name_payItemStateChanged(evt);
                        }
                    });

                    jButton_Tahseel_details.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                    jButton_Tahseel_details.setText("تفصيل التحصيل");
                    jButton_Tahseel_details.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            jButton_Tahseel_detailsActionPerformed(evt);
                        }
                    });

                    jButton74.setText("طباعة");
                    jButton74.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            jButton74ActionPerformed(evt);
                        }
                    });

                    javax.swing.GroupLayout jPanel_ven_payLayout = new javax.swing.GroupLayout(jPanel_ven_pay);
                    jPanel_ven_pay.setLayout(jPanel_ven_payLayout);
                    jPanel_ven_payLayout.setHorizontalGroup(
                        jPanel_ven_payLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel_ven_payLayout.createSequentialGroup()
                            .addGap(60, 60, 60)
                            .addComponent(jButton74)
                            .addGap(18, 18, 18)
                            .addComponent(jButton_Tahseel_details)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel_in_Tahseel, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(37, 37, 37)
                            .addComponent(jLabel_ven_acc_sum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jButton_search_ven_name_pay, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jComboBox_ven_name_pay, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel84)
                            .addGap(60, 60, 60))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_ven_payLayout.createSequentialGroup()
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel_ven_payLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_ven_payLayout.createSequentialGroup()
                                    .addComponent(jButton45, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(447, 447, 447))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_ven_payLayout.createSequentialGroup()
                                    .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap())))
                    );
                    jPanel_ven_payLayout.setVerticalGroup(
                        jPanel_ven_payLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel_ven_payLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel_ven_payLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(jLabel_ven_acc_sum)
                                .addComponent(jLabel84)
                                .addComponent(jComboBox_ven_name_pay, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton_search_ven_name_pay, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel_in_Tahseel)
                                .addComponent(jButton_Tahseel_details)
                                .addComponent(jButton74))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton45, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );

                    AutoCompleteDecorator.decorate(jComboBox_ven_name_pay);

                    jTabbedPane_vendor_action.addTab("سند صرف", jPanel_ven_pay);

                    jPanel_ven_acount_details.addComponentListener(new java.awt.event.ComponentAdapter() {
                        public void componentShown(java.awt.event.ComponentEvent evt) {
                            jPanel_ven_acount_detailsComponentShown(evt);
                        }
                    });

                    jTable_show_ven_account_details.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                    jTable_show_ven_account_details.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null}
                        },
                        new String [] {
                            "برقين", "Title 2", "Title 3", "Title 4"
                        }

                    )
                    {public boolean isCellEditable(int row, int column){return false;}}
                );
                jTable_show_ven_account_details.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        jTable_show_ven_account_detailsMouseClicked(evt);
                    }
                });
                jScrollPane25.setViewportView(jTable_show_ven_account_details);
                //fayez modification
                jTable_show_ven_account_details.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
                //jTable3.setBackground(Color.orange);

                jTable_show_ven_account_details.setForeground(Color.blue);
                jTable_show_ven_account_details.setRowHeight(24);

                jTable_show_ven_account_details.setFont(new Font("Arial", Font.BOLD, 20));

                ////// end fayez modification

                jPanel41.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

                jLabel85.setBackground(new java.awt.Color(0, 204, 51));
                jLabel85.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel85.setText("اسم المورد");
                jLabel85.setBorder(javax.swing.BorderFactory.createEtchedBorder());

                jComboBox_ven_name_for_account_details.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jComboBox_ven_name_for_account_details.setMaximumRowCount(14);
                jComboBox_ven_name_for_account_details.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBox_ven_name_for_account_detailsItemStateChanged(evt);
                    }
                });

                jButton50.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
                jButton50.setText("عرض جميع الحركات");
                jButton50.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton50ActionPerformed(evt);
                    }
                });

                jButton51.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
                jButton51.setText("تصفير حساب الزبون");
                jButton51.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton51ActionPerformed(evt);
                    }
                });

                jButton52.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
                jButton52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/printer_icon.png"))); // NOI18N
                jButton52.setText("طباعة");
                jButton52.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton52ActionPerformed(evt);
                    }
                });

                jButton53.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
                jButton53.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/list_icon.png"))); // NOI18N
                jButton53.setText("عرض جميع حسابات الموردين");
                jButton53.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton53ActionPerformed(evt);
                    }
                });

                jButton54.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
                jButton54.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                jButton54.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton54ActionPerformed(evt);
                    }
                });

                jButton55.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
                jButton55.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/printer_icon.png"))); // NOI18N
                jButton55.setText("حفظ في ملف");
                jButton55.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton55ActionPerformed(evt);
                    }
                });

                jButton56.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
                jButton56.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/message_email.png"))); // NOI18N
                jButton56.setText("sending Email");
                jButton56.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton56ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
                jPanel41.setLayout(jPanel41Layout);
                jPanel41Layout.setHorizontalGroup(
                    jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel41Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton53, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton52)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton51, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton50, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton55)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton56)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton54, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox_ven_name_for_account_details, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel85)
                        .addContainerGap())
                );
                jPanel41Layout.setVerticalGroup(
                    jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel41Layout.createSequentialGroup()
                        .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel41Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel85)
                                        .addComponent(jComboBox_ven_name_for_account_details, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton51, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton50, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton55)
                                        .addComponent(jButton56))))
                            .addGroup(jPanel41Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jButton54, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                AutoCompleteDecorator.decorate(jComboBox_ven_name_for_account_details);

                javax.swing.GroupLayout jPanel_ven_acount_detailsLayout = new javax.swing.GroupLayout(jPanel_ven_acount_details);
                jPanel_ven_acount_details.setLayout(jPanel_ven_acount_detailsLayout);
                jPanel_ven_acount_detailsLayout.setHorizontalGroup(
                    jPanel_ven_acount_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_ven_acount_detailsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel_ven_acount_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane25))
                        .addContainerGap())
                );
                jPanel_ven_acount_detailsLayout.setVerticalGroup(
                    jPanel_ven_acount_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_ven_acount_detailsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel_ven_acount_detailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel_ven_acount_detailsLayout.createSequentialGroup()
                                .addComponent(jPanel41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane25, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(56, Short.MAX_VALUE))
                );

                jTabbedPane_vendor_action.addTab("جدول حساب المورد", jPanel_ven_acount_details);

                jPanel_ven_names.addComponentListener(new java.awt.event.ComponentAdapter() {
                    public void componentShown(java.awt.event.ComponentEvent evt) {
                        jPanel_ven_namesComponentShown(evt);
                    }
                });

                jTable_shoe_ven_names.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jTable_shoe_ven_names.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                    },
                    new String [] {
                        "Title 1", "Title 2", "Title 3", "Title 4"
                    }
                ){public boolean isCellEditable(int row, int column){return false;}});
                jTable_shoe_ven_names.setRowHeight(20);
                jTable_shoe_ven_names.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        jTable_shoe_ven_namesMouseClicked(evt);
                    }
                });
                jScrollPane26.setViewportView(jTable_shoe_ven_names);
                try{
                    r = conn_obj.conn_exec("select vendor_id as ID,vendor_name As Name,vendor_tell as Tell,vendor_location as location from vendors order by vendor_name");
                }catch(Exception e){

                }
                jTable_shoe_ven_names.setModel(DbUtils.resultSetToTableModel(r));

                jTabbedPane4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jTabbedPane4.setToolTipText("");
                jTabbedPane4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

                jLabel86.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel86.setForeground(new java.awt.Color(204, 0, 0));
                jLabel86.setText("الاسم الجديد:");

                jLabel87.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel87.setForeground(new java.awt.Color(204, 0, 0));
                jLabel87.setText("العنوان:");

                jLabel88.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel88.setForeground(new java.awt.Color(204, 0, 0));
                jLabel88.setText("رقم الهاتف:");

                jTextField_ven_name_to_add.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jTextField_ven_name_to_add.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

                jTextField_ven_addres_to_add.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

                jTextField_ven_phone_to_add.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

                jButton_add_new_ven.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
                jButton_add_new_ven.setText("تنفيذ");
                jButton_add_new_ven.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton_add_new_venActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel_add_venLayout = new javax.swing.GroupLayout(jPanel_add_ven);
                jPanel_add_ven.setLayout(jPanel_add_venLayout);
                jPanel_add_venLayout.setHorizontalGroup(
                    jPanel_add_venLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_add_venLayout.createSequentialGroup()
                        .addContainerGap(525, Short.MAX_VALUE)
                        .addComponent(jButton_add_new_ven, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addGroup(jPanel_add_venLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField_ven_name_to_add, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                            .addComponent(jTextField_ven_addres_to_add, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField_ven_phone_to_add, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel_add_venLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel87, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel86, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                            .addComponent(jLabel88, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                );
                jPanel_add_venLayout.setVerticalGroup(
                    jPanel_add_venLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_add_venLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel_add_venLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel86)
                            .addComponent(jTextField_ven_name_to_add, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel_add_venLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_add_venLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel_add_venLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel87)
                                    .addComponent(jTextField_ven_addres_to_add, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel_add_venLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel88)
                                    .addComponent(jTextField_ven_phone_to_add, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel_add_venLayout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jButton_add_new_ven, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(87, Short.MAX_VALUE))
                );

                jTabbedPane4.addTab("إضافة اسم ", jPanel_add_ven);

                jButton57.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
                jButton57.setText("حفظ التعديل");
                jButton57.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton57ActionPerformed(evt);
                    }
                });

                jPanel42.setBorder(javax.swing.BorderFactory.createEtchedBorder());

                jTextField21.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jTextField21.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                jTextField21.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField21ActionPerformed(evt);
                    }
                });

                jTextField22.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jTextField22.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                jTextField22.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField22ActionPerformed(evt);
                    }
                });

                jTextField23.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jTextField23.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                jTextField23.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField23ActionPerformed(evt);
                    }
                });

                jLabel89.setText("الاسم");

                jLabel90.setText("الهاتف");

                jLabel91.setText("العنوان");

                jLabel92.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jLabel92.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                jLabel92.setText("حدّد الاسم من الجدول الاعلى");

                javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
                jPanel42.setLayout(jPanel42Layout);
                jPanel42Layout.setHorizontalGroup(
                    jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel42Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel91)
                            .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel90)
                            .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel42Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel89))
                            .addComponent(jTextField23))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel42Layout.createSequentialGroup()
                        .addContainerGap(274, Short.MAX_VALUE)
                        .addComponent(jLabel92)
                        .addGap(246, 246, 246))
                );
                jPanel42Layout.setVerticalGroup(
                    jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel42Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel89)
                            .addComponent(jLabel90)
                            .addComponent(jLabel91))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                );

                javax.swing.GroupLayout jPanel_modify_ven_nameLayout = new javax.swing.GroupLayout(jPanel_modify_ven_name);
                jPanel_modify_ven_name.setLayout(jPanel_modify_ven_nameLayout);
                jPanel_modify_ven_nameLayout.setHorizontalGroup(
                    jPanel_modify_ven_nameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_modify_ven_nameLayout.createSequentialGroup()
                        .addContainerGap(285, Short.MAX_VALUE)
                        .addGroup(jPanel_modify_ven_nameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_modify_ven_nameLayout.createSequentialGroup()
                                .addComponent(jButton57, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(190, 190, 190))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_modify_ven_nameLayout.createSequentialGroup()
                                .addComponent(jPanel42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35))))
                );
                jPanel_modify_ven_nameLayout.setVerticalGroup(
                    jPanel_modify_ven_nameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_modify_ven_nameLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(jButton57, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                jTabbedPane4.addTab("تعديل إسم", jPanel_modify_ven_name);

                jLabel93.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jLabel93.setText("إختر الاسم الذي تريد حذفه من الاعلى:");

                jLabel94.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jLabel94.setForeground(new java.awt.Color(255, 0, 0));

                jButton58.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
                jButton58.setText("حذف");
                jButton58.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton58ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel_del_ven_nameLayout = new javax.swing.GroupLayout(jPanel_del_ven_name);
                jPanel_del_ven_name.setLayout(jPanel_del_ven_nameLayout);
                jPanel_del_ven_nameLayout.setHorizontalGroup(
                    jPanel_del_ven_nameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_del_ven_nameLayout.createSequentialGroup()
                        .addContainerGap(681, Short.MAX_VALUE)
                        .addGroup(jPanel_del_ven_nameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_del_ven_nameLayout.createSequentialGroup()
                                .addComponent(jLabel94)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel93)
                                .addGap(76, 76, 76))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_del_ven_nameLayout.createSequentialGroup()
                                .addComponent(jButton58, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(179, 179, 179))))
                );
                jPanel_del_ven_nameLayout.setVerticalGroup(
                    jPanel_del_ven_nameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_del_ven_nameLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel_del_ven_nameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel94)
                            .addComponent(jLabel93))
                        .addGap(48, 48, 48)
                        .addComponent(jButton58, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(75, Short.MAX_VALUE))
                );

                jTabbedPane4.addTab("حذف اسم", jPanel_del_ven_name);

                jButton59.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jButton59.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                jButton59.setText("بحث");
                jButton59.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton59ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel_ven_namesLayout = new javax.swing.GroupLayout(jPanel_ven_names);
                jPanel_ven_names.setLayout(jPanel_ven_namesLayout);
                jPanel_ven_namesLayout.setHorizontalGroup(
                    jPanel_ven_namesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_ven_namesLayout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addGroup(jPanel_ven_namesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTabbedPane4)
                            .addComponent(jScrollPane26))
                        .addGap(18, 18, 18)
                        .addComponent(jButton59, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(86, 86, 86))
                );
                jPanel_ven_namesLayout.setVerticalGroup(
                    jPanel_ven_namesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_ven_namesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel_ven_namesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane26, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton59, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jTabbedPane4)
                        .addContainerGap())
                );

                jTabbedPane_vendor_action.addTab("اسماء الموردين", jPanel_ven_names);

                jTable_return_ven_bill_items.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jTable_return_ven_bill_items.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jTable_return_ven_bill_items.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {

                    },
                    new String [] {
                        "الصنف", "الوحدة", "الكمية", "سعر الوحدة","المبلغ","ملاحظات"
                    }
                ) {
                    Class[] types = new Class [] {
                        java.lang.String.class,java.lang.String.class, java.lang.Float.class, java.lang.Float.class,java.lang.Object.class,java.lang.String.class
                    };

                    public Class getColumnClass(int columnIndex) {
                        return types [columnIndex];
                    }
                });
                jTable_return_ven_bill_items.setRowHeight(30);
                jTable_return_ven_bill_items.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseReleased(java.awt.event.MouseEvent evt) {
                        jTable_return_ven_bill_itemsMouseReleased(evt);
                    }
                });
                jTableCustomization_fayez_2(jTable_return_ven_bill_items);
                jScrollPane27.setViewportView(jTable_return_ven_bill_items);

                jPanel43.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

                jButton_del_return_ven_bill_item.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
                jButton_del_return_ven_bill_item.setText("حذف صنف");
                jButton_del_return_ven_bill_item.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton_del_return_ven_bill_itemActionPerformed(evt);
                    }
                });

                jButton_return_ven_bill_insert.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jButton_return_ven_bill_insert.setText("إدخال البيانات");
                jButton_return_ven_bill_insert.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton_return_ven_bill_insertActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
                jPanel43.setLayout(jPanel43Layout);
                jPanel43Layout.setHorizontalGroup(
                    jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel43Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_del_return_ven_bill_item)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_return_ven_bill_insert, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );
                jPanel43Layout.setVerticalGroup(
                    jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel43Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton_return_ven_bill_insert, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton_del_return_ven_bill_item, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                );

                jPanel44.setBorder(javax.swing.BorderFactory.createEtchedBorder());

                jLabel95.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel95.setForeground(new java.awt.Color(204, 0, 0));
                jLabel95.setText("قيمة الفاتورة:");

                jComboBox_van_name_return_bill.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jComboBox_van_name_return_bill.setMaximumRowCount(14);
                jComboBox_van_name_return_bill.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));
                jComboBox_van_name_return_bill.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

                jTextField_return_ven_bill_value.setBackground(new java.awt.Color(102, 255, 204));
                jTextField_return_ven_bill_value.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField_return_ven_bill_value.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
                jTextField_return_ven_bill_value.setText("0");
                jTextField_return_ven_bill_value.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
                jTextField_return_ven_bill_value.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
                jTextField_return_ven_bill_value.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField_return_ven_bill_valueActionPerformed(evt);
                    }
                });
                alinment_component(jTextField_bill_value);

                jLabel96.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel96.setForeground(new java.awt.Color(204, 0, 0));
                jLabel96.setText("تاريخ الفاتورة:");

                jLabel97.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel97.setForeground(new java.awt.Color(204, 0, 0));
                jLabel97.setText("المستودع:");

                jLabel98.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel98.setForeground(new java.awt.Color(204, 0, 0));
                jLabel98.setText("إسم المورد :");

                jDateChooser_return_ven_bill_date.setDate(get_date_jdate());
                jDateChooser_return_ven_bill_date.setBackground(new java.awt.Color(0, 255, 255));
                jDateChooser_return_ven_bill_date.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jDateChooser_return_ven_bill_date.setDateFormatString("yyyy/MM/dd");
                jDateChooser_return_ven_bill_date.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N

                jComboBox_return_ven_store.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

                jTextArea_return_ven_bill_note.setBackground(new java.awt.Color(204, 255, 204));
                jTextArea_return_ven_bill_note.setColumns(20);
                jTextArea_return_ven_bill_note.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextArea_return_ven_bill_note.setLineWrap(true);
                jTextArea_return_ven_bill_note.setRows(5);
                jTextArea_return_ven_bill_note.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                jTextArea_return_ven_bill_note.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
                jScrollPane28.setViewportView(jTextArea_return_ven_bill_note);

                jLabel99.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel99.setForeground(new java.awt.Color(204, 0, 0));
                jLabel99.setText("ملاحظات :");

                jButton60.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                jButton60.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton60ActionPerformed(evt);
                    }
                });

                jLabel100.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel100.setForeground(new java.awt.Color(204, 0, 0));
                jLabel100.setText("رقم الفاتورة:");

                jTextField_return_ven_bil_numberl.setBackground(new java.awt.Color(102, 255, 102));
                jTextField_return_ven_bil_numberl.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jTextField_return_ven_bil_numberl.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField_return_ven_bil_numberlActionPerformed(evt);
                    }
                });
                alinment_component(jTextField_bill_number);

                javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
                jPanel44.setLayout(jPanel44Layout);
                jPanel44Layout.setHorizontalGroup(
                    jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel44Layout.createSequentialGroup()
                        .addContainerGap(124, Short.MAX_VALUE)
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel44Layout.createSequentialGroup()
                                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane28, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel44Layout.createSequentialGroup()
                                        .addGap(118, 118, 118)
                                        .addComponent(jLabel99, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel44Layout.createSequentialGroup()
                                        .addComponent(jTextField_return_ven_bill_value, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel95, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel44Layout.createSequentialGroup()
                                        .addComponent(jDateChooser_return_ven_bill_date, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel96, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel44Layout.createSequentialGroup()
                                        .addComponent(jTextField_return_ven_bil_numberl)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel100, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel44Layout.createSequentialGroup()
                                .addComponent(jComboBox_return_ven_store, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel97)
                                .addGap(18, 18, 18)
                                .addComponent(jButton60, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBox_van_name_return_bill, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel98, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                );
                jPanel44Layout.setVerticalGroup(
                    jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel44Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton60, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel98)
                                .addComponent(jComboBox_van_name_return_bill, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel97)
                                .addComponent(jComboBox_return_ven_store, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel44Layout.createSequentialGroup()
                                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel95)
                                    .addComponent(jTextField_return_ven_bill_value, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel44Layout.createSequentialGroup()
                                        .addComponent(jDateChooser_return_ven_bill_date, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(7, 7, 7))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel44Layout.createSequentialGroup()
                                        .addComponent(jLabel96)
                                        .addGap(18, 18, 18)))
                                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel100)
                                    .addComponent(jTextField_return_ven_bil_numberl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel44Layout.createSequentialGroup()
                                .addComponent(jLabel99)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane28, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                AutoCompleteDecorator.decorate(jComboBox_van_name_return_bill);

                jPanel45.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jPanel45.setPreferredSize(new java.awt.Dimension(626, 50));

                jButton61.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jButton61.setForeground(new java.awt.Color(204, 0, 0));
                jButton61.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                jButton61.setText("بحث بالاصناف");
                jButton61.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton61ActionPerformed(evt);
                    }
                });

                jLabel101.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jLabel101.setForeground(new java.awt.Color(204, 0, 0));
                jLabel101.setText("باركود:");

                jTextField_return_ven_barcode.setBackground(new java.awt.Color(102, 255, 102));
                jTextField_return_ven_barcode.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
                jTextField_return_ven_barcode.setPreferredSize(new java.awt.Dimension(6, 25));
                jTextField_return_ven_barcode.addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextField_return_ven_barcodeFocusLost(evt);
                    }
                });
                jTextField_return_ven_barcode.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextField_return_ven_barcodeActionPerformed(evt);
                    }
                });
                alinment_component(jTextField_bill_number);

                jButton_return_ven_bill_last_price.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
                jButton_return_ven_bill_last_price.setForeground(new java.awt.Color(204, 0, 0));
                jButton_return_ven_bill_last_price.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
                jButton_return_ven_bill_last_price.setText("التسعيرة الاخيرة");
                jButton_return_ven_bill_last_price.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton_return_ven_bill_last_priceActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
                jPanel45.setLayout(jPanel45Layout);
                jPanel45Layout.setHorizontalGroup(
                    jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel45Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField_return_ven_barcode, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel101, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_return_ven_bill_last_price, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton61, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );
                jPanel45Layout.setVerticalGroup(
                    jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel45Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton_return_ven_bill_last_price, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton61))
                            .addGroup(jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField_return_ven_barcode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel101, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())
                );

                javax.swing.GroupLayout jPanel_return_ven_billLayout = new javax.swing.GroupLayout(jPanel_return_ven_bill);
                jPanel_return_ven_bill.setLayout(jPanel_return_ven_billLayout);
                jPanel_return_ven_billLayout.setHorizontalGroup(
                    jPanel_return_ven_billLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_return_ven_billLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel_return_ven_billLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel45, javax.swing.GroupLayout.DEFAULT_SIZE, 1266, Short.MAX_VALUE)
                            .addComponent(jPanel43, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel_return_ven_billLayout.createSequentialGroup()
                                .addGap(548, 548, 548)
                                .addComponent(jPanel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(29, 29, 29))
                            .addComponent(jScrollPane27))
                        .addContainerGap())
                );
                jPanel_return_ven_billLayout.setVerticalGroup(
                    jPanel_return_ven_billLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_return_ven_billLayout.createSequentialGroup()
                        .addComponent(jPanel44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane27, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel43, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(58, Short.MAX_VALUE))
                );

                jTabbedPane_vendor_action.addTab("مردود مشتريات", jPanel_return_ven_bill);

                jPanel67.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

                jTable18.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                    },
                    new String [] {
                        "Title 1", "Title 2", "Title 3", "Title 4"
                    }
                )
            );
            renderer_jTable_obj.Renderer (jTable9);
            jScrollPane42.setViewportView(jTable18);

            jPanel68.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

            jDateChooser9.setDate(get_date_jdate());
            jDateChooser9.setBackground(new java.awt.Color(0, 255, 255));
            jDateChooser9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            jDateChooser9.setDateFormatString("yyyy/MM/dd");
            jDateChooser9.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N

            jLabel141.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
            jLabel141.setForeground(new java.awt.Color(204, 0, 0));
            jLabel141.setText("البحث من تاريخ");

            jButton70.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
            jButton70.setText("بحث");
            jButton70.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton70ActionPerformed(evt);
                }
            });

            jPanel69.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jCheckBox9.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            jCheckBox9.setText("سند صرف");
            jCheckBox9.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

            jCheckBox10.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            jCheckBox10.setText("فواتير مشتريات");
            jCheckBox10.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

            jCheckBox11.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            jCheckBox11.setText("خصومات");
            jCheckBox11.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

            jCheckBox12.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            jCheckBox12.setText("فواتير مرتجعات");
            jCheckBox12.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

            javax.swing.GroupLayout jPanel69Layout = new javax.swing.GroupLayout(jPanel69);
            jPanel69.setLayout(jPanel69Layout);
            jPanel69Layout.setHorizontalGroup(
                jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel69Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jCheckBox12)
                    .addGap(18, 18, 18)
                    .addComponent(jCheckBox11)
                    .addGap(18, 18, 18)
                    .addComponent(jCheckBox10)
                    .addGap(18, 18, 18)
                    .addComponent(jCheckBox9)
                    .addContainerGap())
            );
            jPanel69Layout.setVerticalGroup(
                jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel69Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCheckBox9)
                        .addComponent(jCheckBox10)
                        .addComponent(jCheckBox11)
                        .addComponent(jCheckBox12))
                    .addContainerGap())
            );

            jLabel142.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            jLabel142.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

            jDateChooser10.setDate(get_date_jdate());
            jDateChooser10.setBackground(new java.awt.Color(0, 255, 255));
            jDateChooser10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            jDateChooser10.setDateFormatString("yyyy/MM/dd");
            jDateChooser10.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N

            jLabel143.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
            jLabel143.setForeground(new java.awt.Color(204, 0, 0));
            jLabel143.setText("الى تاريخ:");

            javax.swing.GroupLayout jPanel68Layout = new javax.swing.GroupLayout(jPanel68);
            jPanel68.setLayout(jPanel68Layout);
            jPanel68Layout.setHorizontalGroup(
                jPanel68Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel68Layout.createSequentialGroup()
                    .addGroup(jPanel68Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel68Layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(jLabel142, javax.swing.GroupLayout.PREFERRED_SIZE, 988, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel68Layout.createSequentialGroup()
                            .addComponent(jPanel69, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton70)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jDateChooser10, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(2, 2, 2)
                            .addComponent(jLabel143, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jDateChooser9, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel141, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap())
            );
            jPanel68Layout.setVerticalGroup(
                jPanel68Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel68Layout.createSequentialGroup()
                    .addGroup(jPanel68Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel68Layout.createSequentialGroup()
                            .addGap(13, 13, 13)
                            .addGroup(jPanel68Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jDateChooser9, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jPanel69, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel68Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton70, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jDateChooser10, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel143, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel68Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel141, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(18, 18, 18)
                    .addComponent(jLabel142, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );

            jPanel68Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jDateChooser10, jDateChooser9, jLabel143});

            javax.swing.GroupLayout jPanel67Layout = new javax.swing.GroupLayout(jPanel67);
            jPanel67.setLayout(jPanel67Layout);
            jPanel67Layout.setHorizontalGroup(
                jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel67Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel67Layout.createSequentialGroup()
                            .addComponent(jPanel68, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(29, 29, 29))
                        .addGroup(jPanel67Layout.createSequentialGroup()
                            .addComponent(jScrollPane42)
                            .addContainerGap())))
            );
            jPanel67Layout.setVerticalGroup(
                jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel67Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel68, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout jPanel66Layout = new javax.swing.GroupLayout(jPanel66);
            jPanel66.setLayout(jPanel66Layout);
            jPanel66Layout.setHorizontalGroup(
                jPanel66Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel66Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel67, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );
            jPanel66Layout.setVerticalGroup(
                jPanel66Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel66Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel67, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(55, Short.MAX_VALUE))
            );

            jTabbedPane_vendor_action.addTab("إحصائيات", jPanel66);

            jTabbedPane3.addTab("موردين", new javax.swing.ImageIcon(getClass().getResource("/images/vendors.png")), jTabbedPane_vendor_action); // NOI18N

            getContentPane().add(jTabbedPane3, java.awt.BorderLayout.CENTER);

            jMenuBar1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

            jMenu2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Technical-Supporticon.png"))); // NOI18N
            jMenu2.setText("عمليات");
            jMenu2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

            jMenu3.setText("قاعدة البيانات");

            jMenuItem2.setText("نسخة احتياطية");
            jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem2ActionPerformed(evt);
                }
            });
            jMenu3.add(jMenuItem2);

            jMenuItem3.setText("استرجاع");
            jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem3ActionPerformed(evt);
                }
            });
            jMenu3.add(jMenuItem3);

            jMenu2.add(jMenu3);

            jMenu4.setText("بحث عن صنف");

            jMenuItem1.setText("في فواتير المبيعات");
            jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem1ActionPerformed(evt);
                }
            });
            jMenu4.add(jMenuItem1);

            jMenuItem4.setText("في فواتير المشتريات");
            jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem4ActionPerformed(evt);
                }
            });
            jMenu4.add(jMenuItem4);

            jMenuItem26.setText("كرت الصنف");
            jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem26ActionPerformed(evt);
                }
            });
            jMenu4.add(jMenuItem26);

            jMenu2.add(jMenu4);

            jMenuItem5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            jMenuItem5.setText("مسحوبات الزبائن");
            jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem5ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem5);

            jMenuItem6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            jMenuItem6.setText("اعادة الاتصال");
            jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem6ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem6);

            jMenuItem7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            jMenuItem7.setText("كشف شيكات زبون");
            jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem7ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem7);

            jMenuItem8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            jMenuItem8.setText("إعدادات الايميل");
            jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem8ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem8);

            jMenu5.setText("كشف حسابات الزبائن");

            jMenuItem9.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            jMenuItem9.setText("الكل");
            jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem9ActionPerformed(evt);
                }
            });
            jMenu5.add(jMenuItem9);

            jMenuItem10.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            jMenuItem10.setText("حسب الاسم");
            jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem10ActionPerformed(evt);
                }
            });
            jMenu5.add(jMenuItem10);

            jMenu2.add(jMenu5);

            jMenuItem12.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            jMenuItem12.setText("مسحوبات صنف من تاجر");
            jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem12ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem12);

            jMenuItem13.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            jMenuItem13.setText("عرض اصناف مورد");
            jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem13ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem13);

            jMenuItem14.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            jMenuItem14.setText("قيد خصم للزبون");
            jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem14ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem14);

            jMenuItem15.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            jMenuItem15.setText("كشف شيكات الزيائن");
            jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem15ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem15);

            jMenuItem16.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            jMenuItem16.setText("script");
            jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem16ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem16);

            jMenuItem17.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            jMenuItem17.setText("بحث في صنف");
            jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem17ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem17);

            jMenuItem18.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            jMenuItem18.setText("البنوك");
            jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem18ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem18);

            jMenuItem27.setText("تصنيفات الزيائن");
            jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem27ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem27);

            jMenuItem19.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            jMenuItem19.setText("قيد خصم لمورد");
            jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem19ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem19);

            jMenuItem20.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            jMenuItem20.setText("تعديل حالة شيكات بتاريخ");
            jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem20ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem20);

            jMenuItem11.setText("إرجاع شيك زبون");
            jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem11ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem11);

            jMenuItem23.setText("Ranking");
            jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem23ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem23);

            jMenuItem24.setText("مجموع شكات غير مجيرة");
            jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem24ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem24);

            jMenuItem25.setText("تحديث");
            jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem25ActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItem25);

            jMenuBar1.add(jMenu2);

            jMenu6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            jMenu6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Services-icon.png"))); // NOI18N
            jMenu6.setText("خدمات");
            jMenu6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
            jMenu6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

            jMenuItem21.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItem21.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
            jMenuItem21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/currency-dollar-icon.png"))); // NOI18N
            jMenuItem21.setText("أسعار العملات");
            jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem21ActionPerformed(evt);
                }
            });
            jMenu6.add(jMenuItem21);

            jMenuItem22.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItem22.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
            jMenuItem22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/phone-icon.png"))); // NOI18N
            jMenuItem22.setText("دليل الهاتف");
            jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem22ActionPerformed(evt);
                }
            });
            jMenu6.add(jMenuItem22);

            jMenuBar1.add(jMenu6);

            jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Land Sales-32.png"))); // NOI18N
            jMenu1.setText("نقطة بيع");
            jMenu1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
            jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    jMenu1MousePressed(evt);
                }
            });
            jMenuBar1.add(jMenu1);

            setJMenuBar(jMenuBar1);

            setSize(new java.awt.Dimension(1316, 768));
            setLocationRelativeTo(null);
        }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        jButton3.doClick();
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    insert_bill_to_customer_jtable4(false);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        String name = jTextField5.getText().trim();
        String address = jTextField6.getText().trim();
        String phone = jTextField7.getText().trim();
        String catagory_name = jComboBox5.getSelectedItem().toString().trim();
        String stm = "insert into customers (customer_name,customer_tell,customer_location,customer_catagory_id)values('" + name + "','" + phone + "','" + address + "',(select catagory_id from customer_catagory where catagory_name='" + catagory_name + "'))";
        try {
            //////** لمنع ادخال اسم فارغ الى اسماء  الموردين
            //////** ومنع ادخال تصنيف فارغ ------
            if (!name.equals("") && !jComboBox5.getSelectedItem().toString().trim().equals("------")) {
                {
                    
                    conn_obj.exec(stm);
                    Joptionpane_message("تمت إضافة الاسم");

                    jTextField5.setText("");
                    jTextField6.setText("");
                    jTextField7.setText("");
                    jTextField5.requestFocus();
                    if (!phone.equals(""))
                    check_to_add_phone_number(phone,name);
                }
            } else {
                throw new SQLException("الاسم او التصنيف خاطئ...");
            }

            /////////////
            ///** لاعادة تحديث جدول اسماء الموردين بعد اضافة اسم جديد
            r = conn_obj.conn_exec("select customer_id as ID,customer_name As Name,customer_tell as Tell, customer_location as location from customers");
            jTable1.setModel(DbUtils.resultSetToTableModel(r));
            //**end
            r.afterLast();
            r.previous();

            jComboBox1.addItem(name);
            jComboBox4.addItem(name);
            jComboBox3.addItem(name);
            jComboBox8.addItem(name);
            ///////////////////////////////////////////////////to write on file //////////////////////
            write_to_file(get_date() + "     " + "  إدخال إسم  " + name + " " + "بتصنيف " + " "
                    + catagory_name + " ");
            //////////////////////////////////////////////////////////////end write on file////////
        } catch (SQLException ex) {
            Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            Joptionpane_message(ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            Joptionpane_message(ex.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (jPanel6.isShowing())
        try {
            int x = (int) jTable1.getValueAt(jTable1.getSelectedRow(), 0);
            r = conn_obj.conn_exec("select customers.*,customer_catagory.* from customers,customer_catagory where customer_id=" + x + " and customer_catagory_id=catagory_id");
            r.next();
            jLabel13.setText(r.getString(1));
            jTextField8.setText(r.getString(2));
            jTextField9.setText(r.getString(3));
            jTextField10.setText(r.getString(4));
            jComboBox6.setSelectedItem(r.getString("catagory_name"));
        } catch (SQLException ex) {
            Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (jPanel4.isShowing()) {
            jLabel25.setText((String) jTable1.getValueAt(jTable1.getSelectedRow(), 1));//لحذف زبون

        }

    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        update_customer_name_button();
        //update_jcomboBox_1_3_4();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10ActionPerformed

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged

        create_customer_account_table();
        show_last_row_scroll_jtable(jTable3);

    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        if (account_zero==true) {
            try {
                account_zero(jComboBox3.getSelectedItem().toString());
                create_customer_account_table();

            } catch (SQLException ex) {
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {

            delete_name();        // TODO add your handling code here:
        } catch (SQLException ex) {
            Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        show_all_customer_movements();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        
        print_customer_move_table_object=new print_customer_move_table(this);
        print_customer_move_table_object.setVisible(true);
        print_customer_move_table_object.setLocationRelativeTo(this);

    }//GEN-LAST:event_jButton10ActionPerformed

    private void jTextField15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField15ActionPerformed
        jButton3ActionPerformed(evt);//ادخال البيانات عند الضغط على انتر
    }//GEN-LAST:event_jTextField15ActionPerformed

    private void delete_recordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_recordActionPerformed
       
        if(delete_entry==true)
        try {
            int point = jTable3.getSelectedRow();
            delete_bill_or_payment(jTable3.getValueAt(point, 3).toString().trim(), jTable3.getValueAt(point, 6).toString().trim());
        } catch (SQLException ex) {
            Joptionpane_message(ex.getMessage());
        }
    }//GEN-LAST:event_delete_recordActionPerformed

    private void show_itemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_show_itemsActionPerformed
         int point = jTable3.getSelectedRow();
        show_bill_items(jTable3.getValueAt(point, 6).toString().trim(),jTable3.getValueAt(point, 3).toString().trim());

    }//GEN-LAST:event_show_itemsActionPerformed

    private void show_imageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_show_imageActionPerformed
        open_the_bill_picture();
    }//GEN-LAST:event_show_imageActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        show_customers_accounts("");
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jPanel2ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel2ComponentShown

    }//GEN-LAST:event_jPanel2ComponentShown

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed

    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void modify_recordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modify_recordActionPerformed

        int point = jTable3.getSelectedRow();
        if (jComboBox3.getSelectedItem().toString().equals("------"))//لا يمكن التعديل والاسم غير صحيح 
        {
            Joptionpane_message("إختر الاسم من القائمة !!!");
        } else if(modify_entry==true) {
            try {
                if (check_if_accounted(Integer.parseInt(jTable3.getValueAt(point, 6).toString().trim()), jTable3.getValueAt(point, 3).toString().trim())) {
                    Joptionpane_message("لا يمكن تعديل قيد مصفّر !!!");
                } else {

                    if (jTable3.getValueAt(point, 3).toString().trim().equals("فاتورة")) {
                        jframe_to_modify_bill = new modify_customer_bill(this);
             //reset_jpanel_1();//لحتى لما يفرغ البيانات في جدول الفاتورة يفرغها والجدول وكل المربعات فارغة
                        //jTabbedPane1.setSelectedIndex(0);
                        String bill_id = jTable3.getValueAt(point, 6).toString().trim();
                        r = conn_obj.conn_exec("select * from  customer_bills where bill_id=" + bill_id + "");
                        r.next();
                        jframe_to_modify_bill.jComboBox1.setSelectedItem(jComboBox3.getSelectedItem());
                        jframe_to_modify_bill.jTextField1.setText(String.valueOf(r.getFloat("bill_value")));
                        jframe_to_modify_bill.jDateChooser1.setDate(r.getDate("bill_date"));
                        jframe_to_modify_bill.jComboBox2.setSelectedIndex(r.getInt("bill_location_id") - 1);
                        jframe_to_modify_bill.jTextArea1.setText(r.getString("bill_note"));
                        jframe_to_modify_bill.jTextField3.setText(String.valueOf(r.getFloat("discount_ratio")));
                        jframe_to_modify_bill.jTextField16.setText(String.valueOf(r.getFloat("discount_amount")));
                        jframe_to_modify_bill.jTextField15.setText(r.getString("bill_num"));
                        jframe_to_modify_bill.jTextField19.setText(bill_id);

                        String location_id = r.getString("bill_location_id");
                        /////////////////////////////////////////////////// show bill items in jtable////////////
                        r = conn_obj.conn_exec("SELECT \n"
                                + "items.main_items.item_name,\n"
                                + "items.main_items.item_id,\n"
                                + "\n"
                                + "items.item_units.unit_name,\n"
                                + "items.item_units.unit_id,\n"
                                + "\n"
                                + "customer_bills_items.id,customer_bills_items.item_id,customer_bills_items.item_bonus,\n"
                                + "customer_bills_items.item_note,customer_bills_items.item_unit,\n"
                                + "customer_bills_items.item_price,customer_bills_items.item_quantity,\n"
                                + "customer_bills_items.bill_id,customer_bills_items.discount_ratio\n"
                                + "FROM items.main_items,items.item_units,customer_bills_items\n"
                                + "WHERE\n"
                                + "items.main_items.item_id = customer_bills_items.item_id\n"
                                + "AND\n"
                                + "items.item_units.unit_id = customer_bills_items.item_unit\n"
                                + "AND\n"
                                + "customer_bills_items.bill_id=" + bill_id + "  order by customer_bills_items.id");

                        /*
                         عند تعديل القيد يجب معرفة اذا كان عدد سطور
                         الطلبية المغدلة اكبر من عدد سطور الجدول فتضيغ بغض البيانات
                         */
                        DefaultTableModel tm = (DefaultTableModel) jframe_to_modify_bill.jTable4.getModel();
                        r.last();
                        int r_row_count = r.getRow();
                        int jTable_5_row_count = jframe_to_modify_bill.jTable4.getRowCount();
                        r.beforeFirst();
                        while (r_row_count > jTable_5_row_count) {
                            r_row_count--;
                            tm.addRow(new Object[]{null, null, null, 0, null, 0, null, ""});

                        }

                        while (r.next()) {
                            int result_set_row = r.getRow() - 1;
                
                            jframe_to_modify_bill.jTable4.setValueAt(r.getString("item_name"), result_set_row, 0);//item_name
                            jframe_to_modify_bill.jTable4.setValueAt(r.getString("unit_name"), result_set_row, 1);//item_unit
                            jframe_to_modify_bill.jTable4.setValueAt(r.getFloat("item_bonus"), result_set_row, 3);//item_Bonus
                            jframe_to_modify_bill.jTable4.setValueAt(r.getFloat("item_quantity"), result_set_row, 2);//item_quantity
                            //jframe_to_modify_bill.jTable4.setValueAt(String.format("%.2f", r.getFloat("item_price")),result_set_row,4);//item_price
                            jframe_to_modify_bill.jTable4.setValueAt(r.getFloat("item_price"), result_set_row, 4);//item_price
                            jframe_to_modify_bill.jTable4.setValueAt(r.getFloat("discount_ratio"), result_set_row, 5);//item_discountRatio
                            jframe_to_modify_bill.jTable4.setValueAt(r.getString("item_note"), result_set_row, 7);//item_discountRatio
                        }
                        ///////////////////////////////////////end show bill_items in jtable
                        jframe_to_modify_bill.jTable4_listen_to_any_change();
                        jframe_to_modify_bill.prepare_stm_that_return_quantity_to_store();
                        jframe_to_modify_bill.setVisible(true);
            ///////////////////////////////////////end show bill_items in jtable
                        // Joptionpane_message("ملاحظة هامة .. يجب أن تعلم أن الفاتورة السابقة المعدلة تم حذفها");
                        //conn_obj.exec("DELETE FROM customer_bills where bill_id="+bill_id+"");
                    } else if (jTable3.getValueAt(point, 3).toString().trim().equals("دفعة")) {
                        String payment_id = jTable3.getValueAt(point, 6).toString().trim();
                         r = conn_obj.conn_exec("select check_no,check_value,vendor_payment_id from customer_checks where check_payment_id ="+payment_id+" \n"
                    + "and \n"
                    + "vendor_payment_id is not null ;");
                        if (r.next()) {//معناها انو في شكات مجيرة لتاجر من ضمن الشكات المراد تعديلها

                            String message = "";//تحمل اظهار اسم المورد الذي معه الشك او اسماء الموردين اللي معهم الشكات
                            do {
                                r2 = conn_obj2.conn_exec("select vendor_payments.vendor_id_fk,vendor_payments.payment_id,vendor_payments.payment_date,\n"
                                        + "       vendors.vendor_id,vendors.vendor_name\n"
                                        + "\n"
                                        + "       from vendors,vendor_payments\n"
                                        + "\n"
                                        + "where     vendors.vendor_id= vendor_id_fk and\n"
                                        + "           payment_id =" + r.getInt("vendor_payment_id") + "");
                                r2.next();
                                message+="يوجد شك في هذه الدفعة مجير للمورد  "+r2.getString("vendor_name")+" بتاريخ : "+r2.getString("payment_date")+"رقم الشك: "+r.getString("check_no")+"\n";
                                
                            } while (r.next());
                            throw new Exception(message+"قبل تعديل هذه الدفعة يجب الغاء المجير منها من الشيكات");

                        }
                        jframe_to_modify_payment = new modify_customer_payment(this, payment_id);
                        r = conn_obj.conn_exec("select * from  customer_payments where payment_id=" + payment_id + "");
                        r.next();

                        float hole_payment_value = r.getFloat("payment_value");

                        jframe_to_modify_payment.jComboBox4.setSelectedItem(jComboBox3.getSelectedItem());
                        jframe_to_modify_payment.jDateChooser2.setDate(r.getDate("payment_date"));
                        jframe_to_modify_payment.jTextField14.setText(r.getString("payment_rec"));
                        jframe_to_modify_payment.jTextField19.setText(r.getString("payment_maker"));
                        jframe_to_modify_payment.jTextField13.setText(r.getString("voucher_no"));
                        jframe_to_modify_payment.jTextArea2.setText(r.getString("payment_note"));
                        //
                        r = conn_obj.conn_exec("select sum(check_value)as s from  customer_checks where check_payment_id=" + payment_id + "");
                        r.next();
                        float sum_of_checks = r.getFloat("s");
                        float net_of_cash_payments = hole_payment_value - sum_of_checks;
                        jframe_to_modify_payment.jTextField12.setText(Float.toString(net_of_cash_payments));
            //

                        r = conn_obj.conn_exec("select check_owner,check_endorser,check_no,check_due_date,check_note,check_value,check_bank from  customer_checks where check_payment_id=" + payment_id + "");
                        while (r.next()) {
                            DefaultTableModel tm = (DefaultTableModel) jframe_to_modify_payment.jTable13.getModel();
                            tm.addRow(new Object[]{jframe_to_modify_payment.jTable13.getRowCount() + 1, r.getString("check_no"), r.getFloat("check_value"), r.getString("check_owner"), r.getString("check_endorser"), r.getString("check_due_date"), r.getString("check_bank"), r.getString("check_note")});
                        }
                        jframe_to_modify_payment.setVisible(true);
                        jframe_to_modify_payment.prepare_payment_sum();
                         
                    } else if (jTable3.getValueAt(point, 3).toString().trim().equals("م-مبيعات")) {
             //reset_jpanel_1();//لحتى لما يفرغ البيانات في جدول الفاتورة يفرغها والجدول وكل المربعات فارغة
                        //jTabbedPane1.setSelectedIndex(0);
                        String bill_id = jTable3.getValueAt(point, 6).toString().trim();
                        r = conn_obj.conn_exec("select * from  return_customer_bills where return_bill_id=" + bill_id + "");
                        r.next();
                        jComboBox10.setSelectedItem(jComboBox3.getSelectedItem());
                        jTextField36.setText(r.getString("return_bill_value"));
                        jDateChooser7.setDate(r.getDate("return_bill_date"));
                        jComboBox11.setSelectedIndex(r.getInt("return_bill_location_id") - 1);
                        jTextArea6.setText(r.getString("return_bill_note"));
                        jTextField31.setText(r.getString("return_bill_num"));

                        String location_id = r.getString("return_bill_location_id");
                        /////////////////////////////////////////////////// show bill items in jtable////////////
                        r = conn_obj.conn_exec("SELECT \n"
                                + "items.main_items.item_name,\n"
                                + "items.main_items.item_id,\n"
                                + "\n"
                                + "items.item_units.unit_name,\n"
                                + "items.item_units.unit_id,\n"
                                + "\n"
                                + "return_customer_bills_items.id,return_customer_bills_items.return_item_id,\n"
                                + "return_customer_bills_items.return_item_note,return_customer_bills_items.return_item_unit,\n"
                                + "return_customer_bills_items.return_item_price,return_customer_bills_items.return_item_quantity,\n"
                                + "return_customer_bills_items.return_bill_id\n"
                                + "FROM items.main_items,items.item_units,return_customer_bills_items\n"
                                + "WHERE\n"
                                + "items.main_items.item_id = return_customer_bills_items.return_item_id\n"
                                + "AND\n"
                                + "items.item_units.unit_id = return_customer_bills_items.return_item_unit\n"
                                + "AND\n"
                                + "return_customer_bills_items.return_bill_id=" + bill_id + "  order by return_customer_bills_items.id");

                        /*
                         عند تعديل القيد يجب معرفة اذا كان عدد سطور
                         الطلبية المغدلة اكبر من عدد سطور الجدول فتضيغ بغض البيانات
                         */
                        DefaultTableModel tm = (DefaultTableModel) jTable15.getModel();
                        r.last();
                        int r_row_count = r.getRow();
                        tm.setRowCount(0);//لحف سطور التعديل السابق ان وجد
                        int jTable_5_row_count = jTable15.getRowCount();
                        r.beforeFirst();
                        while (r_row_count > jTable_5_row_count) {
                            r_row_count--;
                            tm.addRow(new Object[]{null, null, 1, 1, null, ""});

                        }

                        while (r.next()) {
                            int result_set_row = r.getRow() - 1;
            
                            jTable15.setValueAt(r.getString("item_name"), result_set_row, 0);//item_name
                            jTable15.setValueAt(r.getString("unit_name"), result_set_row, 1);//item_unit
                            //jframe_to_modify_bill.jTable4.setValueAt(r.getString("item_bonus"),result_set_row,3);//item_Bonus
                            jTable15.setValueAt(r.getString("return_item_quantity"), result_set_row, 2);//item_quantity
                            //jframe_to_modify_bill.jTable4.setValueAt(String.format("%.2f", r.getFloat("item_price")),result_set_row,4);//item_price
                            jTable15.setValueAt(r.getFloat("return_item_price"), result_set_row, 3);//item_price
                            //jframe_to_modify_bill.jTable4.setValueAt(r.getString("discount_ratio"),result_set_row,5);//item_discountRatio
                            jTable15.setValueAt(r.getString("return_item_note"), result_set_row, 5);//item_discountRatio
                        }
            ///////////////////////////////////////end show bill_items in jtable
                        //jframe_to_modify_bill.jTable4_listen_to_any_change();
                        jLabel23.setText(bill_id);
                        update_or_show_returned_bill_items.pack();
                        update_or_show_returned_bill_items.setLocationRelativeTo(this);
                        prepare_stm_that_return_quantity_to_store();//تجهيز جملة اعادة المخزونات وتنفيذها فقط عند التعديل لفاتورة الارجاع
                        update_or_show_returned_bill_items.setVisible(true);
                    }
                    else if (jTable3.getValueAt(point, 3).toString().trim().equals("خصم"))
                    {                       
                        String discount_id = jTable3.getValueAt(point, 6).toString().trim();                       
                        customer_discount dis=new customer_discount(this, discount_id, 1);
                        r = conn_obj.conn_exec("select * from  customer_discount where discount_id=" + discount_id + "");
                        r.next();
                        dis.jComboBox4.setSelectedItem(jComboBox3.getSelectedItem());
                        dis.jTextField11.setText(String.valueOf(r.getFloat("discount_value")));
                        dis.jDateChooser2.setDate(r.getDate("discount_date"));
                        dis.jTextArea2.setText(r.getString("discount_note"));
                        dis.setLocationRelativeTo(this);
                        dis.setVisible(true);
                    }
                    else if (jTable3.getValueAt(point, 3).toString().trim().equals("شك-مرتجع"))
                    {                       
                        String check_id = jTable3.getValueAt(point, 6).toString().trim();                       
                        returned_checks re=new returned_checks(check_id,this);
                        r = conn_obj.conn_exec("select * from  returned_checks where id=" + check_id + "");
                        r.next();
                        String customer_check_id=r.getString("check_id_fk");
                        re.jTextField1.setText(r.getString("check_id_fk"));
                        re.jTextField3.setText(r.getString("check_value"));
                        re.jTextField6.setText(Float.toString(r.getFloat("return_commission")));
                        re.jDateChooser1.setDate(r.getDate("return_date"));
                        re.jTextArea1.setText(r.getString("return_note"));
                        re.jTextField2.setText(jComboBox3.getSelectedItem().toString());        
                        r = conn_obj.conn_exec("select check_owner,check_no from  customer_checks where id=" + customer_check_id + "");
                        r.next();
                        re.jTextField4.setText(r.getString("check_owner"));
                        re.jTextField5.setText(r.getString("check_no"));
                        re.setLocationRelativeTo(this);
                        re.setVisible(true);
                    }

                }
            } catch (Exception ex) {
                Joptionpane_message(ex.getMessage());
            }
        }
    }//GEN-LAST:event_modify_recordActionPerformed

    private void show_noteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_show_noteActionPerformed
        int point = jTable3.getSelectedRow();
        if (jTable3.getValueAt(point, 2).toString().equals("")) {
            Joptionpane_message("لا توجد ملاحظة");
        } else {
            Joptionpane_message(jTable3.getValueAt(point, 2).toString());
        }
    }//GEN-LAST:event_show_noteActionPerformed
    String x_getKeyChar = "";
    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed

        jTable4.editingCanceled(null);//هذا اذا نسي المدخل خانات مفتوحة اي مفتوحة للتعديل لا يتعامل معها على انها فارغة فيخرج منها قبل ادخال البيانات
        int row = jTable4.getRowCount();
        int col = jTable4.getColumnCount();
        float account_sum = 0;
        String html = "<body> ";

        html += "<h2 align=\"center\">الشروق</h2>\n"
                + "<h3 align=\"center\">برقين - 042438973</h3>\n"
                + "<h3 align=\"center\">فاتورة</h3>";
        html += "<table align=\"center\" border=\"1\" style=\"width:5px\" cellspacing=\"0\" cellpadding=\"1\" style=\"font-size: 8px\">" + "\n";
        html += "<tr>" + "\n";
        html += "<td >" + "تاريخ الطباعة : " + get_date() + "</td>" + "\n";
        html += "<td >" + "         " + "</td>" + "\n";
        html += "<td align=\"right\">" + "إسم الزبون  : " + jComboBox1.getSelectedItem().toString() + "</td>" + "\n";
        html += "</table>";

        html += "<table align=\"center\" border=\"1\" style=\"width:5px\" cellspacing=\"0\" cellpadding=\"1\" style=\"font-size: 8px\">" + "\n";
        html += "<tr>" + "\n";

        html += "<th bgcolor=" + "#00FF00" + ">" + jTable4.getColumnName(7) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable4.getColumnName(6) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable4.getColumnName(5) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable4.getColumnName(4) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable4.getColumnName(3) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable4.getColumnName(2) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable4.getColumnName(1) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable4.getColumnName(0) + "</th>";

        html += "</tr>" + "\n";
        for (int i = 0; i < row; i++) {
            if (jTable4.getValueAt(i, 0) != null) {
                html += "<tr>" + "\n";
                for (int x = col - 1; x >= 0; x--) {
                    if (jTable4.getValueAt(i, x) == null) {
                        html += "<td align=\"center\">" + "" + "</td>" + "\n";
                    } else {
                        html += "<td align=\"center\">" + jTable4.getValueAt(i, x) + "</td>" + "\n";
                    }
                }
                html += "</tr>" + "\n";
            }

        }
        html += "<tr>" + "\n";
        html += "<td>" + "مجموع الفاتورة =   " + jTextField17.getText() + "</td>" + "\n";
        html += "</tr>" + "\n";
        html += "<tr>" + "\n";
        html += "<td>" + "نسبة خصم على الفاتورة % =  " + jTextField3.getText() + "</td>" + "\n";
        html += "</tr>" + "\n";
        html += "<tr>" + "\n";
        html += "<td>" + "خصم نقدي مباشر   " + jTextField16.getText() + "</td>" + "\n";
        html += "</tr>" + "\n";
        html += "<tr>" + "\n";
        html += "<td>" + "المجموع المستلزم للدفع   " + jTextField1.getText() + "</td>" + "\n";
        html += "</tr>" + "\n";
        /*الحساب الكلي
         html+="<tr>"+"\n";
         html+="<td>"+"الحساب الكلي = "+get_customer_account_sum(jComboBox1.getSelectedItem().toString())+"</td>"+"\n";
         html+="</tr>"+"\n";
         */
        html += "<tr><td></td></tr>" + "\n";
        html += "</table></body>" + "\n";
       //System.out.println(html);
        /////////////////////////////////

        JPanel panel = new JPanel();
        final JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");

        editorPane.setText(html);
        panel.add(editorPane);
        JFrame frame2 = new JFrame();
        frame2.setSize(700, 600);
        frame2.setLocationRelativeTo(this);
        JButton but = new JButton();
        but.setText("طباعة");
        panel.add(but);
        but.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    //Execute when button is pressed
                    editorPane.print();
                } catch (PrinterException ex) {
                    Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        frame2.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Change this to switch between examples
        boolean useScrollPane = true;

        if (useScrollPane) {
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setViewportView(panel);
            frame2.add(scrollPane);
        } else {
            frame2.add(panel);
        }
        frame2.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton14ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        try {
            conn_obj.close();
            conn_obj2.close();
            r.close();
            if (r2 != null) {
                r2.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        DefaultTableModel tm = (DefaultTableModel) jTable4.getModel();
        int[] rows_array;
        rows_array = jTable4.getSelectedRows();
        for (int i = 0; i < rows_array.length; i++) {
            int selectd_row = rows_array[i] - i;
            tm.removeRow(selectd_row);
        }
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jTextField17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField17ActionPerformed

    private void jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyPressed

        int key = evt.getKeyCode();
        if (evt.getSource() == jTextField2) {
            if (key == KeyEvent.VK_ENTER) {
       
            r = conn_obj.conn_exec("select customer_name as اسم_الزبون from ( select customers.customer_name,customers.customer_catagory_id,\n" +
"     user_privileg_on_customer_catag.user_id_fk,user_privileg_on_customer_catag.customer_catagory_fk\n" +
"from customers,user_privileg_on_customer_catag\n" +
"where \n" +
"    user_privileg_on_customer_catag.user_id_fk=(select user_id from users where user_name='"+user_name+"') and\n" +
"    customer_catagory_id=customer_catagory_fk and customer_name LIKE '%" + jTextField2.getText().trim() + "%')as fdvcfx");
            
        
               
                jTable5.setModel(DbUtils.resultSetToTableModel(r));
                renderer_jTable_obj.Renderer(jTable5);
            }
        }


    }//GEN-LAST:event_jTextField2KeyPressed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        which_component_request_searchCustomerName = 3;
        searchCustomerName.pack();
        searchCustomerName.setLocationRelativeTo(this);
        searchCustomerName.setVisible(true);
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jTable5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable5MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable5MouseClicked

    private void jTable5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable5MousePressed
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            evt.consume();
            if (which_component_request_searchCustomerName == 3) {
                jComboBox1.setSelectedItem(jTable5.getValueAt(jTable5.getSelectedRow(), jTable5.getSelectedColumn()));
                jTextField2.requestFocus();
                jTextField2.selectAll();
            } else if (which_component_request_searchCustomerName == 1) {
                jComboBox3.setSelectedItem(jTable5.getValueAt(jTable5.getSelectedRow(), jTable5.getSelectedColumn()));
                jTextField2.requestFocus();
                jTextField2.selectAll();
            } else if (which_component_request_searchCustomerName == 5) {
                jComboBox4.setSelectedItem(jTable5.getValueAt(jTable5.getSelectedRow(), jTable5.getSelectedColumn()));
                jTextField2.requestFocus();
                jTextField2.selectAll();
            } else if (which_component_request_searchCustomerName == 6) {
                jTextField29.setText((String) jTable5.getValueAt(jTable5.getSelectedRow(), jTable5.getSelectedColumn()));
                jTextField2.requestFocus();
                jTextField2.selectAll();
            } else if (which_component_request_searchCustomerName == 2) {
                //jComboBox4.setSelectedItem(jTable5.getValueAt(jTable5.getSelectedRow(), jTable5.getSelectedColumn()));
                jTextField2.requestFocus();
                jTextField2.selectAll();
                try {
                    r = conn_obj.conn_exec("select customer_id as ID,customer_name As Name,customer_tell as Tell,customer_location as location from customers where customer_name like '%" + jTable5.getValueAt(jTable5.getSelectedRow(), jTable5.getSelectedColumn()) + "%' order by customer_name");
                    jTable1.setModel(DbUtils.resultSetToTableModel(r));
                } catch (Exception e) {

                }
            } else if (which_component_request_searchCustomerName == 7) {
                jTextField29.setText((String) jTable5.getValueAt(jTable5.getSelectedRow(), jTable5.getSelectedColumn()));
                jTextField2.requestFocus();
                jTextField2.selectAll();
            } else if (which_component_request_searchCustomerName == 8) {
                jComboBox8.setSelectedItem(jTable5.getValueAt(jTable5.getSelectedRow(), jTable5.getSelectedColumn()));
                jTextField2.requestFocus();
                jTextField2.selectAll();
            }
            else if (which_component_request_searchCustomerName == 9) {
                jComboBox10.setSelectedItem(jTable5.getValueAt(jTable5.getSelectedRow(), jTable5.getSelectedColumn()));
                jTextField2.requestFocus();
                jTextField2.selectAll();
            }
            searchCustomerName.dispose();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_jTable5MousePressed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        which_component_request_searchItemName = 1;
        searchItemName.pack();
        searchItemName.setLocationRelativeTo(this);
        searchItemName.setVisible(true); 
        searchItemName.setState(NORMAL);
        jTextField4.requestFocus();
    }//GEN-LAST:event_jButton21ActionPerformed
ImageIcon[] images;//حطيناها هون لحتى نستخدمها في تكبير الصورة عند المرور عن الصورة في البحث عن الاصناف
    private void jTextField4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyPressed
        int key = evt.getKeyCode();
        if (evt.getSource() == jTextField4) {
            if (key == KeyEvent.VK_ENTER) {
                
if(which_component_request_searchItemName!=8){//زبائن
    String stm="select item_name as الاسم,unit_name as الوحدة,item_price_hole  as السعر,'' as الكمية,'' as ملاحظات , store_id_1/relation as مخزون,itemmm_iddd as image \n"
                        + "from\n"
                        + "( select    items.item_units.unit_name,items.item_units.unit_id,\n"
                        + "            items.main_items.item_name,items.main_items.item_id as itemmm_iddd,\n"
                        + "            items.inventory.store_id_1,items.inventory.item_id,\n"
                        + "            items.items_ranking.rank_item_id,items.items_ranking.rank,\n"
                        + "            items.item_relations.item_id,items.item_relations.item_unit,items.item_relations.item_price_hole,items.item_relations.item_relation as relation\n"
                        + "            from items.item_units,items.main_items,items.item_relations,items.items_ranking,items.inventory\n"
                        + "            where \n"
                        + "            items.main_items.item_name LIKE '%" + jTextField4.getText().trim() + "%'     \n"
                        + "            AND \n"
                        + "            items.item_relations.item_id= items.main_items.item_id \n"
                        + "            AND \n"
                        + "            items.inventory.item_id= items.main_items.item_id \n"
                        + "            AND \n"
                        + "            items.main_items.item_id = items.items_ranking.rank_item_id \n"
                        + "            and \n"
                        + "            items.item_relations.item_unit=items.item_units.unit_id order by rank desc ,main_items.item_name )as one";
               r = conn_obj.conn_exec(stm);

                System.out.println(stm);
                jTable7.setModel(DbUtils.resultSetToTableModel(r));
                jTable7.getColumnModel().getColumn(0).setMinWidth(200);//اسم الصنف
                jTable7.getColumnModel().getColumn(0).setMaxWidth(200);//
                jTable7.getColumnModel().getColumn(1).setMinWidth(90);//الوحدة
                jTable7.getColumnModel().getColumn(1).setMaxWidth(90);//
                jTable7.getColumnModel().getColumn(2).setMinWidth(70);//العدد
                jTable7.getColumnModel().getColumn(2).setMaxWidth(70);//
                jTable7.getColumnModel().getColumn(3).setMinWidth(70);//السعر
                jTable7.getColumnModel().getColumn(3).setMaxWidth(70);//
                jTable7.getColumnModel().getColumn(5).setMinWidth(70);//مخزون
                jTable7.getColumnModel().getColumn(5).setMaxWidth(70);//كوانتتي مخزوون
                jTextField4.selectAll();
    jTable7.changeSelection(0, 3, false, false);
 
    images = new ImageIcon[jTable7.getRowCount()];
    for (int i = 0; i < images.length; i++) {
        images[i] = new ImageIcon( items_images_folder+ jTable7.getValueAt(i, 6).toString() + ".jpg");
        jTable7.setValueAt(images[i], i, 6);
    }
 //renderer_jTable_obj.Renderer(jTable7);   
}
else{//موردين
    
                r = conn_obj.conn_exec("select item_name as الاسم,unit_name as الوحدة,to_char(value, 'FM9999999999.00') as السعر_علينا,relation as علاقتها_بالرئيسية,1 as الكمية,'' as ملاحظات\n" +
"                    from\n" +
"                    ( \n" +
"                    select \n" +
"                      items.item_units.unit_name,items.item_units.unit_id,\n" +
"                      items.main_items.item_name,items.main_items.item_id,items.main_items.item_value*items.item_relations.item_relation as value,\n" +
"                      items.item_relations.item_id,items.item_relations.item_unit,items.item_relations.item_price_hole,items.item_relations.item_relation as relation\n" +
"                               \n" +
"                                from items.item_units,items.main_items,items.item_relations\n" +
"                                                               where \n" +
"                                items.main_items.item_name LIKE '%" + jTextField4.getText().trim() + "%'\n" +
"                                AND \n" +
"                                items.item_relations.item_id= items.main_items.item_id\n" +
"                                and\n" +
"                                items.item_relations.item_unit=items.item_units.unit_id\n" +
"                                \n" +
"                               )  as alias");

            //r=conn_obj.conn_exec("select item_name from items.main_items where item_name LIKE '%"+jTextField4.getText().trim()+"%'");
                jTable7.setModel(DbUtils.resultSetToTableModel(r));
                
                jTable7.getColumnModel().getColumn(0).setMinWidth(250);
                jTable7.getColumnModel().getColumn(1).setMinWidth(70);
                jTable7.getColumnModel().getColumn(2).setMinWidth(70);
                jTable7.getColumnModel().getColumn(3).setMinWidth(70);
                jTable7.getColumnModel().getColumn(4).setMinWidth(70);
                jTable7.getColumnModel().getColumn(5).setMinWidth(200);
                jTextField4.selectAll();
                
renderer_jTable_obj.Renderer(jTable7);
}

            }
        }
    }//GEN-LAST:event_jTextField4KeyPressed

    private void jTable7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable7MousePressed

        if (evt.getClickCount() == 2 && !evt.isConsumed() && jTable7.getSelectedColumn() == 0) {
            evt.consume();
            if (which_component_request_searchItemName == 1) {
                //نريد ان نضيف صنف لفاتورة زبون من خلال 
                //SearchItemName  from
                //وعملنا فنكشن لانو هاي الكتابة مكررة  
                //MouseEvent    and   KeyEvent
                add_item_from_SearchItemName_to_customer_bill();
                } else if (which_component_request_searchItemName == 2) {
                search_in_customer_bills_about_item();
            } else if (which_component_request_searchItemName == 3) {
                try {
                        String item_name, new_item_unit, customer = "";
                        item_name = jTable7.getValueAt(jTable7.getSelectedRow(), 0).toString();
                        new_item_unit = jTable7.getValueAt(jTable7.getSelectedRow(), 1).toString();
                        customer = jComboBox8.getSelectedItem().toString();
                        r = conn_obj.conn_exec("select \n" +
"                               unit_name,unit_id,\n" +
"                               items.main_items.item_name,items.main_items.item_id,\n" +
"                               customer_name,customer_id,\n" +
"                                bill_customer_id,public.customer_bills.bill_id,public.customer_bills.bill_date,\n" +
"                                public.customer_bills_items.bill_id,public.customer_bills_items.item_id,public.customer_bills_items.item_unit,public.customer_bills_items.item_price\n" +
"                                \n" +
"                                from\n" +
"                                items.item_units,\n" +
"                                items.main_items,\n" +
"                                public.customers,\n" +
"                                public.customer_bills,\n" +
"                                public.customer_bills_items\n" +
"                                \n" +
"                                where \n" +
"                                items.main_items.item_name= '"+item_name+"' and\n" +
"                                public.customers.customer_name= '"+customer+"' and\n" +
"                                public.customer_bills.bill_customer_id=public.customers.customer_id and\n" +
"                                public.customer_bills.bill_id=public.customer_bills_items.bill_id and\n" +
"                                public.customer_bills_items.item_id=items.main_items.item_id and\n" +
"                                public.customer_bills_items.item_unit=items.item_units.unit_id \n" +
"                                order by bill_date;");
                        
    
                        if (!r.next()) {
                            DefaultTableModel tm = (DefaultTableModel) jTable12.getModel();
                tm.addRow(new Object[]{
                    jTable7.getValueAt(jTable7.getSelectedRow(), 0),
                    jTable7.getValueAt(jTable7.getSelectedRow(), 1),
                    jTable7.getValueAt(jTable7.getSelectedRow(), 3),
                    jTable7.getValueAt(jTable7.getSelectedRow(), 2),
                    null,
                    jTable7.getValueAt(jTable7.getSelectedRow(), 4)});

                        } else {
                            r.last();

                            int old_unit_id = r.getInt("unit_id");
                            
                            int item_id = r.getInt("item_id");
                            
                            float last_price = r.getFloat("item_price");
                            
                            r2 = conn_obj2.conn_exec("select unit_id from items.item_units where unit_name ='" + new_item_unit + "';");
                            r2.next();
                            int new_unit_id = r2.getInt("unit_id");
                            
                            r2 = conn_obj2.conn_exec("select item_relation from items.item_relations where item_id =" + item_id + "  and item_unit =" + old_unit_id + "  ;");
                            r2.next();
                            float old_unit_relation = r2.getFloat("item_relation");
                            
                            r2 = conn_obj2.conn_exec("select item_relation from items.item_relations where item_id =" + item_id + "  and item_unit =" + new_unit_id + "  ;");
                            r2.next();
                            float new_unit_relation = r2.getFloat("item_relation");
                            
              float new_price=last_price/(old_unit_relation/new_unit_relation);
              
               DefaultTableModel tm = (DefaultTableModel) jTable12.getModel();
                tm.addRow(new Object[]{
                    item_name,new_item_unit,jTable7.getValueAt(jTable7.getSelectedRow(), 3),new_price,null,jTable7.getValueAt(jTable7.getSelectedRow(), 4)
                });
              
                         }
                    } catch (SQLException ex) {
                        Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
                    }
                show_last_row_scroll_jtable(jTable12);
            } else if (which_component_request_searchItemName == 5) {
                DefaultTableModel tm = (DefaultTableModel) jTable15.getModel();
                tm.addRow(new Object[]{
                    jTable7.getValueAt(jTable7.getSelectedRow(), 0),
                    jTable7.getValueAt(jTable7.getSelectedRow(), 1),
                    jTable7.getValueAt(jTable7.getSelectedRow(), 3),
                    jTable7.getValueAt(jTable7.getSelectedRow(), 2),
                    null,
                    jTable7.getValueAt(jTable7.getSelectedRow(), 4)});

                show_last_row_scroll_jtable(jTable15);
            }

            
            
        jTextField4.requestFocus();
        jTextField4.selectAll();
        }
        

    }//GEN-LAST:event_jTable7MousePressed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
//String name = JOptionPane.showInputDialog(this, "ما هو الباسوورد?");
//if(name.equals("12"))
//{
        for (int i = 0; i < jTable4.getRowCount(); i++) {
            if (jTable4.getValueAt(i, 1) != null && jTable4.getValueAt(i, 0) != null) {
                try {
                    //المبدأ هو
                    String item_name, new_item_unit, customer = "";
                    item_name = jTable4.getValueAt(i, 0).toString();
                    new_item_unit = jTable4.getValueAt(i, 1).toString();
                    customer = jComboBox1.getSelectedItem().toString();
                    String stm="select \n" +
"                               unit_name,unit_id,\n" +
"                               items.main_items.item_name,items.main_items.item_id,\n" +
"                               customer_name,customer_id,\n" +
"                                bill_customer_id,public.customer_bills.bill_id,public.customer_bills.bill_date,\n" +
"                                public.customer_bills_items.bill_id,public.customer_bills_items.item_id,public.customer_bills_items.item_unit,public.customer_bills_items.item_price\n" +
"                                \n" +
"                                from\n" +
"                                items.item_units,\n" +
"                                items.main_items,\n" +
"                                public.customers,\n" +
"                                public.customer_bills,\n" +
"                                public.customer_bills_items\n" +
"                                \n" +
"                                where \n" +
"                                items.main_items.item_name= '"+item_name+"' and\n" +
"                                public.customers.customer_name= '"+customer+"' and\n" +
"                                public.customer_bills.bill_customer_id=public.customers.customer_id and\n" +
"                                public.customer_bills.bill_id=public.customer_bills_items.bill_id and\n" +
"                                public.customer_bills_items.item_id=items.main_items.item_id and\n" +
"                                public.customer_bills_items.item_unit=items.item_units.unit_id \n" +
"                                order by bill_date;";
                    r = conn_obj.conn_exec(stm);
                    if (r.next()) {
                        r.last();

                        int old_unit_id = r.getInt("unit_id");

                        int item_id = r.getInt("item_id");

                        float last_price = r.getFloat("item_price");

                        r2 = conn_obj2.conn_exec("select unit_id from items.item_units where unit_name ='" + new_item_unit + "';");
                        r2.next();
                        int new_unit_id = r2.getInt("unit_id");

                        r2 = conn_obj2.conn_exec("select item_relation from items.item_relations where item_id =" + item_id + "  and item_unit =" + old_unit_id + "  ;");
                        r2.next();
                        float old_unit_relation = r2.getFloat("item_relation");

                        r2 = conn_obj2.conn_exec("select item_relation from items.item_relations where item_id =" + item_id + "  and item_unit =" + new_unit_id + "  ;");
                        r2.next();
                        float new_unit_relation = r2.getFloat("item_relation");

                        float new_price = last_price / (old_unit_relation / new_unit_relation);

                        jTable4.setValueAt(new_price, i, 4);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        //} else {
        // Joptionpane_message("الباسوورد خاطئ");
        //}
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
//print jtable fayez
        boolean if_bouns_or_dis=false;
        
        jTable4.editingCanceled(null);//هذا اذا نسي المدخل خانات مفتوحة اي مفتوحة للتعديل لا يتعامل معها على انها فارغة فيخرج منها قبل ادخال البيانات
        int row = jTable6.getRowCount();
        int col = jTable6.getColumnCount();
        
String html = "<body> ";
        html += "<table align=\"center\" border=\"1\" style=\"width:5px\" cellspacing=\"0\" cellpadding=\"1\" style=\"font-size: 8px\">"  + "\n";
        html += "<tr>" + "\n";
        html += "<td >" + jLabel39.getText()+ "</td>" + "\n";
        html += "<td >" + "         " + "</td>" + "\n";
        html += "<td align=\"right\">" + "إسم الزبون  : " + jComboBox3.getSelectedItem().toString() + "</td>" + "\n";
        html += "</table>";

        html += "<table align=\"center\" border=\"1\" style=\"width:5px\" cellspacing=\"0\" cellpadding=\"1\" style=\"font-size: 8px\">"  + "\n";
       
        if(movement_name_to_print=="م-مبيعات")
            html+=build_html_for_return_bill(row,col);
        else//فاتورة عادية
        {    /////
        if_bouns_or_dis=check_if_there_is_a_bouns_in_the_bill();    
        html += "<tr>" + "\n";

        html += "<th>" + jTable6.getColumnName(0) + "</th>";
        html += "<th>" + jTable6.getColumnName(1) + "</th>";
        if(if_bouns_or_dis==true)
        html += "<th>" + jTable6.getColumnName(3) + "</th>";
        html += "<th>" + jTable6.getColumnName(4) + "</th>";
        if(if_bouns_or_dis==true)
        html += "<th>" + jTable6.getColumnName(5) + "</th>";
        html += "<th>" + jTable6.getColumnName(6) + "</th>";
        html += "<th>" + jTable6.getColumnName(7) + "</th>";
        html += "<th>" + jTable6.getColumnName(8) + "</th>";
//ssss
        html += "</tr>" + "\n";
        if(if_bouns_or_dis==true)
        for (int i = 0; i < row; i++) {
            html += "<tr>" + "\n";
            for (int x = 0; x < col ; x++) {
                if (x != 2)//بعد_الخصم غير ضرورية
                {
                    html += "<td align=\"center\">" + jTable6.getValueAt(i, x) + "</td>" + "\n";
                }
            }
            html += "</tr>" + "\n";

        }
        if(if_bouns_or_dis==false)
        for (int i = 0; i < row; i++) {
            html += "<tr>" + "\n";
            for (int x = 0; x < col ; x++) {
                if (x != 2 && x != 3 && x != 5)//بعد_الخصم غير ضرورية
                {
                    html += "<td align=\"center\">" + jTable6.getValueAt(i, x) + "</td>" + "\n";
                }
            }
            html += "</tr>" + "\n";

        }
            
        }/////نهاية الفاتورة العادية
            
            
        
        
        html += "<tr>" + "\n";
        html += "<td>" + jLabel31.getText() + "</td>" + "\n";
        html += "</tr>" + "\n";
        html += "<tr>" + "\n";
        html += "<td>" + jLabel32.getText() + "</td>" + "\n";
        html += "</tr>" + "\n";
        html += "<tr>" + "\n";
        html += "<td>" + jLabel33.getText() + "</td>" + "\n";
        html += "</tr>" + "\n";
        html += "<tr>" + "\n";
        html += "<td>" + " رصيدكم لدينا  =  " + get_customer_account_sum(jComboBox3.getSelectedItem().toString()) + "</td>" + "\n";
        html += "</tr>" + "\n";
        
        html += "</table>";
        
        String note = "";

        
        if (!jTextField20.getText().trim().equals("")) {
            
            note = jTextField20.getText().trim().toString();
            System.out.println(note);
            
            int point = jTable3.getSelectedRow();
            String bill_id = jTable3.getValueAt(point, 6).toString().trim();
            System.out.println(bill_id);
            conn_obj.exec("delete from bill_note_to_print where bill_id_fk ="+bill_id+";");
                            
            conn_obj.exec("insert into bill_note_to_print(note_to_print,bill_id_fk) values ('"+note+"',"+bill_id+");");
                            
                            
            html += "<p align=\"center\"> ملاحظة:" + note + " </p>" + "\n";
           
        }
        html += "<p align=\"right\"> اسم الزبون مستلم البضاعة</p>" + "\n";
        html += "<p align=\"right\"> اسم السائق </p>" + "\n";
        html += "</body>" + "\n";
        /////////////////////////////////

        JPanel panel = new JPanel();
        final JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");

        editorPane.setMargin(new Insets(0,0,0,0));
        editorPane.setText(html);
        panel.add(editorPane);
        JFrame frame2 = new JFrame();
        frame2.setSize(700, 600);
        frame2.setLocationRelativeTo(this);
        JButton but = new JButton();
        but.setText("طباعة");
        panel.add(but);
        but.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                   // editorPane.print(null, null, false, null, null,false);
                    editorPane.print();
                } catch (PrinterException ex) {
                    Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        frame2.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Change this to switch between examples
        boolean useScrollPane = true;

        if (useScrollPane) {
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setViewportView(panel);
            frame2.add(scrollPane);
        } else {
            frame2.add(panel);
        }

        frame2.setVisible(true);
        but.requestFocus();// TODO add your handling code here:

    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed

        DefaultTableModel tm = (DefaultTableModel) jTable4.getModel();
        int[] rows_array;
        rows_array = jTable4.getSelectedRows();
        for (int i = 0; i < rows_array.length; i++) {
            int selectd_row = rows_array[i] - i;

            try {
                if (jTable4.getValueAt(selectd_row, 0) != null) {
                    conn_obj.get_st().executeUpdate("insert into items.non_existant_items(item_id,customer_id,item_note)values"
                            + "( (select item_id from items.main_items where item_name = '" + jTable4.getValueAt(jTable4.getSelectedRow(), 0) + "' ),(select customer_id from customers where customer_name = '" + (String) jComboBox1.getSelectedItem() + "'),'" + jTable4.getValueAt(jTable4.getSelectedRow(), 7).toString() + "')");
                }
                tm.removeRow(selectd_row);

            } catch (SQLException ex) {
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            if (jTable4.getValueAt(jTable4.getSelectedRow(), 0) != null) {
                conn_obj.get_st().executeUpdate("insert into items.non_existant_items(item_id,customer_id,item_note)values"
                        + "( (select item_id from items.main_items where item_name = '" + jTable4.getValueAt(jTable4.getSelectedRow(), 0) + "' ),(select customer_id from customers where customer_name = '" + (String) jComboBox1.getSelectedItem() + "'),'" + jTable4.getValueAt(jTable4.getSelectedRow(), 7) + "')");
            }
        } catch (SQLException ex) {
            Joptionpane_message(ex.getMessage());
            Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        try {
            jTable8.print();
        } catch (PrinterException pe) {
            System.err.println("Error printing: " + pe.getMessage());
        }
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        try {
            DefaultTableModel tm = (DefaultTableModel) jTable8.getModel();
            int[] rows_array;
            rows_array = jTable8.getSelectedRows();
            for (int i = 0; i < rows_array.length; i++) {
                int selectd_row = rows_array[i] - i;
                conn_obj.get_st().execute("delete from items.non_existant_items where id=" + jTable8.getValueAt(selectd_row, 3) + "");
                tm.removeRow(selectd_row);
            }
            Joptionpane_message("تم الحذف");
        } catch (SQLException ex) {
            Joptionpane_message(ex.getMessage());
            Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed

//لفحص على الاثل مربع واحد مختار لكي لا يعمل اكسبشن غير مفهوم  1
        boolean is_there_checked_one_box = false;
        for (int i = 0; i < jPanel24.getComponentCount(); i++) {
            JCheckBox checkBox = (JCheckBox) jPanel24.getComponent(i);
            if (checkBox.isSelected()) {
                is_there_checked_one_box = true;
                i=i+1000;//للخروج من اللوب تحقق الشرط
            }
        }
     //انتهي الفحص 1
        if(is_there_checked_one_box==true)
        try{
            String summary="";
        String date=jDateChooser3.getDate().toString();
        String date2=jDateChooser5.getDate().toString();
        String catagory_name = jComboBox7.getSelectedItem().toString().trim();
        
        String paym_mov_date_or_record="record_time ::timestamp::date ",bill_mov_date_or_record="record_time ::timestamp::date ",return_mov_date_or_record="record_time ::timestamp::date ",discount_mov_date_or_record="record_time ::timestamp::date ";
        
        if(jRadioButton1.isSelected())
        {
            paym_mov_date_or_record="customer_payments.payment_date";
            bill_mov_date_or_record="customer_bills.bill_date";        
            return_mov_date_or_record="return_bill_date";
            discount_mov_date_or_record="discount_date";        
        }
String stm = "select value as القيمة,date as التاريخ,movement as الحركة,id as رقم_الحركة,customer_name as  اسم_الزبون ,row_number() OVER () as num    from(  ";
String payments_movement= "select customer_payments.payment_value as value,customer_payments.payment_date as date,'دفعة' as movement,customer_payments.payment_id as id,customer_payments.customer_id_fk,customer_payments.record_time,customers.customer_id,customers.customer_name\n"
                    + "from customer_payments \n"
                    + "\n"
                    + "join customers\n"
                    + "on customers.customer_id=customer_payments.customer_id_fk\n"
                    + "where "+paym_mov_date_or_record+" >= '" + date + "' and "+paym_mov_date_or_record+" <= '" + date2 + "' ";

String bills_movement="select customer_bills.bill_value as value,customer_bills.bill_date as date,'فاتورة' as movement,customer_bills.bill_id as id,customer_bills.bill_customer_id,customer_bills.record_time,customers.customer_id,customers.customer_name\n"
                    + "from customer_bills \n"
                    + "\n"
                    + "join customers\n"
                    + "on customers.customer_id=customer_bills.bill_customer_id\n"
                    + "where "+bill_mov_date_or_record+" >= '" + date + "' and "+bill_mov_date_or_record+" <= '" + date2 + "'  ";
String return_bill_movement="select return_bill_value as value,return_bill_date as date,'م-مبيعات' as movement,return_bill_id as id,return_bill_customer_id,record_time,customers.customer_id,customers.customer_name\n"
                    + "from return_customer_bills \n"
                    + "\n"
                    + "join customers\n"
                    + "on customers.customer_id=return_bill_customer_id\n"
                    + "where "+return_mov_date_or_record+" >= '" + date + "' and "+return_mov_date_or_record+" <= '" + date2 + "'  ";
String discount_movements="select discount_value as value,discount_date as date,'خصم' as movement,discount_id as id,customer_id_fk,record_time,customers.customer_id,customers.customer_name\n"
                    + "from customer_discount \n"
                    + "\n"
                    + "join customers\n"
                    + "on customers.customer_id=customer_id_fk\n"
                    + "where "+discount_mov_date_or_record+" >= '" + date + "' and "+discount_mov_date_or_record+" <= '" + date2 + "' ";  
if(!catagory_name.equals("------"))
{ 
    payments_movement+=" and customers.customer_catagory_id=(select catagory_id from customer_catagory where catagory_name='"+catagory_name+"') ";
    discount_movements+=" and customers.customer_catagory_id=(select catagory_id from customer_catagory where catagory_name='"+catagory_name+"') ";
    return_bill_movement+=" and customers.customer_catagory_id=(select catagory_id from customer_catagory where catagory_name='"+catagory_name+"') ";
    bills_movement+=" and customers.customer_catagory_id=(select catagory_id from customer_catagory where catagory_name='"+catagory_name+"') ";
    
}
        for (int i = 0; i < jPanel24.getComponentCount(); i++) {
            JCheckBox checkBox = (JCheckBox) jPanel24.getComponent(i);
            if (checkBox.isSelected()) {
                if (checkBox.getText().equals("فواتير مبيعات")) {
                    stm += bills_movement;
                    r = conn_obj.conn_exec("select sum (value) from ( "+bills_movement+" ) as vbg");
                    r.next();
                    summary+="مجموع فواتير المبيعات  =  "+r.getString("sum");          
                }
                else if(checkBox.getText().equals("دفعات قبض")) {
                    stm += payments_movement;
                    r = conn_obj.conn_exec("select sum (value) from ( "+payments_movement+" ) as vbg");
                    r.next();
                    summary+="مجموع سندات القبض  =  "+r.getString("sum"); 
                }
                else if(checkBox.getText().equals("خصومات")) {
                    stm += discount_movements;
                    r = conn_obj.conn_exec("select sum (value) from ( "+discount_movements+" ) as vbg");
                    r.next();
                    summary+="  مجموع قيود الخصومات =  "+r.getString("sum"); 
                }
                else if(checkBox.getText().equals("فواتير مرتجعات")) {
                    stm += return_bill_movement;
                    r = conn_obj.conn_exec("select sum (value) from ( "+return_bill_movement+" ) as vbg");
                    r.next();
                    summary+="مجوع فواتير مردود مبيعات   =   "+r.getString("sum"); 
                }
                //لنتاكد من ان بعد هذد الحركة يوجد حركة مختارة لنضع يونيون والا فالجملة ستكون خطأ
                for (int j = i + 1; j < jPanel24.getComponentCount(); j++) {
                    checkBox = (JCheckBox) jPanel24.getComponent(j);
                    if (checkBox.isSelected()) {
                        stm+= "union ";
                        j=j+100;//للخروج من اللوب ضروري في حال التاكد
                    }
                }
                //انتهينا نمن التاكد من وجود حركة اخرى مختارة ام لا

            }
        }
        stm+=" order by record_time)as ddfv order by num ";
        r = conn_obj.conn_exec(stm);
            System.out.println(stm);
            jTable9.setModel(DbUtils.resultSetToTableModel(r));
            jLabel44.setText(summary);
            renderer_jTable_obj.Renderer(jTable9);
            jTable9.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        }
        catch(Exception e)
        {
            Joptionpane_message(e.getMessage());
        }
        
        else{
            Joptionpane_message("اختر مربعا واحد من الحركات على الاقل!!");
        }    
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jPanel1ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel1ComponentShown
        System.out.println("حسابات الزبون");
        create_customer_account_table();
        show_last_row_scroll_jtable(jTable3);        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1ComponentShown

    private void jTextField18FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField18FocusLost
            

    }//GEN-LAST:event_jTextField18FocusLost

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        which_component_request_searchCustomerName = 1;
        searchCustomerName.pack();
        searchCustomerName.setLocationRelativeTo(this);
        searchCustomerName.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jTable4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseReleased
        if (SwingUtilities.isRightMouseButton(evt) && jTable4.columnAtPoint(evt.getPoint()) == 1) {//if right click and column units
            //////         
            try {
                jPopupMenu2.removeAll();
                int row_selected = jTable4.rowAtPoint(evt.getPoint());
                jTable4.setRowSelectionInterval(row_selected, row_selected);
                String item_name = jTable4.getValueAt(row_selected, 0).toString().trim();
                r = conn_obj.conn_exec("select items.item_relations.item_id,items.item_relations.item_unit,\n"
                        + "items.item_units.unit_name,items.item_units.unit_id,\n"
                        + "items.main_items.item_name,items.main_items.item_id \n"
                        + "from items.item_relations,items.item_units,items.main_items\n"
                        + "where \n"
                        + "items.main_items.item_name='" + item_name + "' and\n"
                        + "items.item_relations.item_id=items.main_items.item_id \n"
                        + "AND\n"
                        + "items.item_relations.item_unit = items.item_units.unit_id ");
                while (r.next()) {
                    JMenuItem item = new JMenuItem(r.getString("unit_name"));
                    item.addActionListener(new java.awt.event.ActionListener() {

                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            select_unit_in_jtable4_right_click(evt);
                        }
                    });
                    jPopupMenu2.add(item);
                }

            } catch (SQLException ex) {
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            }

            jPopupMenu2.show(evt.getComponent(), evt.getX(), evt.getY());
        } //////اذا نريد التسعيرة الاخيرة للصنف1111
        else if (SwingUtilities.isRightMouseButton(evt) && jTable4.columnAtPoint(evt.getPoint()) == 4) {//if right click and column price

            try {
                jPopupMenu2.removeAll();
                int row_selected = jTable4.rowAtPoint(evt.getPoint());
                jTable4.setRowSelectionInterval(row_selected, row_selected);

                String item_unit, customer, item_name = "";
                item_name = jTable4.getValueAt(row_selected, 0).toString().trim();
                item_unit = jTable4.getValueAt(row_selected, 1).toString().trim();
                customer = jComboBox1.getSelectedItem().toString();

                r = conn_obj.conn_exec("select \n"
                        + "unit_name,unit_id,\n"
                        + "items.main_items.item_name,items.main_items.item_id,\n"
                        + "customer_name,customer_id,\n"
                        + "bill_customer_id,public.customer_bills.bill_id,customer_bills.bill_date,\n"
                        + "public.customer_bills_items.bill_id,public.customer_bills_items.item_id,public.customer_bills_items.item_unit,public.customer_bills_items.item_price\n"
                        + "\n"
                        + "from\n"
                        + "items.item_units,\n"
                        + "items.main_items,\n"
                        + "public.customers,\n"
                        + "public.customer_bills,\n"
                        + "public.customer_bills_items\n"
                        + "\n"
                        + "where \n"
                        + "items.item_units.unit_name= '" + item_unit + "'  and\n"
                        + "items.main_items.item_name= '" + item_name + "' and\n"
                        + "public.customers.customer_name= '" + customer + "' and\n"
                        + "public.customer_bills.bill_customer_id=public.customers.customer_id and\n"
                        + "public.customer_bills.bill_id=public.customer_bills_items.bill_id and\n"
                        + "public.customer_bills_items.item_id=items.main_items.item_id and\n"
                        + "public.customer_bills_items.item_unit=items.item_units.unit_id ;\n"
                        + "\n"
                        + "");
                r.last();
                r.previous();
                r.previous();
                r.previous();

                while (r.next()) {
                    JMenuItem item = new JMenuItem(r.getFloat("item_price") + "=" + r.getString("bill_date"));
                    item.addActionListener(new java.awt.event.ActionListener() {

                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt) {

                            String priceAndDate = evt.getActionCommand().trim();
                            String[] parts = priceAndDate.split("=");
                            String price = parts[0];

                            jTable4.setValueAt(price, jTable4.getSelectedRow(), 4);
                        }
                    });
                    jPopupMenu2.add(item);
                }
                /////////////////////////////////////////////////////////222
                //add jMenue with item see_cost 
                JMenu menu_cost = new JMenu("السعر علينا");
                jPopupMenu2.insert(menu_cost, 0);
                r = conn_obj.conn_exec("select item_name as الاسم,unit_name as الوحدة,to_char(value, '9999999999.00') as السعر_علينا,relation as علاقتها_بالرئيسية\n"
                        + "                    from\n"
                        + "                    ( \n"
                        + "                    select \n"
                        + "                      items.item_units.unit_name,items.item_units.unit_id,\n"
                        + "                      items.main_items.item_name,items.main_items.item_id,items.main_items.item_value*items.item_relations.item_relation as value,\n"
                        + "                      items.item_relations.item_id,items.item_relations.item_unit,items.item_relations.item_price_hole,items.item_relations.item_relation as relation\n"
                        + "                               \n"
                        + "                                from items.item_units,items.main_items,items.item_relations\n"
                        + "                                                               where \n"
                        + "                                items.main_items.item_name ='" + item_name + "'\n"
                        + "                                AND \n"
                        + "                                items.item_relations.item_id= items.main_items.item_id\n"
                        + "                                and\n"
                        + "                                items.item_relations.item_unit=items.item_units.unit_id\n"
                        + "                                and\n"
                        + "                                items.item_relations.item_unit=(select unit_id from items.item_units where unit_name='" + item_unit + "')     \n"
                        + "\n"
                        + "                                   )  as alias");
                r.next();
                if(see_cost==true)
                menu_cost.add(r.getString(3));
                ////End add jMenue hold item_Cost
                
                //اضافة منيو متوسط سعر المباع لاخر 30 مرة
                JMenu average_sell_price = new JMenu("متوسط سعر البيع");
                jPopupMenu2.insert(average_sell_price, 1);
                // هذا الامر فقط لمعرفة كم عدد سطور النتيجة لنخرج المعدل
                String stm="select customers.customer_id,customers.customer_catagory_id,\n"
                        + "customer_bills.bill_customer_id,customer_bills.bill_id,\n"
                        + "customer_bills_items.bill_id,customer_bills_items.item_unit,customer_bills_items.item_price,customer_bills_items.item_id,\n"
                        + "items.item_relations.item_id,items.item_relations.item_unit,items.item_relations.item_relation,\n"
                        + "item_price/item_relation as res\n"
                        + "\n"
                        + "from customer_bills_items ,items.item_relations,customers,customer_bills\n"
                        + "\n"
                        + "where \n"
                        + "--لا تساوي متفرقات مثل محل برقيم وجنين\n"
                        + "customers.customer_catagory_id !=10 and\n"
                        + "customer_bills.bill_customer_id=customers.customer_id and\n"
                        + "customer_bills.bill_id=customer_bills_items.bill_id and\n"
                        + "customer_bills_items.item_id=(select item_id from items.main_items where item_name = '"+item_name+"' ) and\n"
                        + "items.item_relations.item_id=customer_bills_items.item_id \n"
                        + "and  items.item_relations.item_unit=customer_bills_items.item_unit \n"
                        + "order by customer_bills_items.bill_id\n" +
                          "desc limit 20";
                r = conn_obj.conn_exec(stm);
                int row_count=getRowCount(r);
                
                //
                //هذا الامر لمعرفة مجموع اخر 20 مبيع او اقل
                stm="select (sum (res)*(select items.item_relations.item_relation from items.item_relations where item_id=(select item_id from items.main_items where item_name = '"+item_name+"' ) and item_unit = (select unit_id from items.item_units where unit_name = '"+item_unit+"' )))  as final_res \n"
                        + "from \n"
                        + "(\n"
                        + "select customers.customer_id,customers.customer_catagory_id,\n"
                        + "customer_bills.bill_customer_id,customer_bills.bill_id,\n"
                        + "customer_bills_items.bill_id,customer_bills_items.item_unit,customer_bills_items.item_price,customer_bills_items.item_id,\n"
                        + "items.item_relations.item_id,items.item_relations.item_unit,items.item_relations.item_relation,\n"
                        + "item_price/item_relation as res\n"
                        + "\n"
                        + "from customer_bills_items ,items.item_relations,customers,customer_bills\n"
                        + "\n"
                        + "where \n"
                        + "--لا تساوي متفرقات مثل محل برقيم وجنين\n"
                        + "customers.customer_catagory_id !=10 and\n"
                        + "customer_bills.bill_customer_id=customers.customer_id and\n"
                        + "customer_bills.bill_id=customer_bills_items.bill_id and\n"
                        + "customer_bills_items.item_id=(select item_id from items.main_items where item_name = '"+item_name+"' ) and\n"
                        + "items.item_relations.item_id=customer_bills_items.item_id \n"
                        + "and  items.item_relations.item_unit=customer_bills_items.item_unit \n"
                        + "order by customer_bills_items.bill_id\n" +
                          "desc limit 20 \n"
                        + ")as dvgh";
                
                r = conn_obj.conn_exec(stm);
                r.next();
                float average=0;
                average = r.getFloat("final_res")/row_count;
                DecimalFormat df = new DecimalFormat("#.#");
                
                average_sell_price.add(df.format(average));
                ////انتهاء اضافة منيو متوسط سعر المباع لاخر 20 مرة
            } catch (SQLException ex) {
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            }

            jPopupMenu2.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTable4MouseReleased

    private void jTextField16KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField16KeyReleased
        try {
            jTextField1.setText(Float.toString(prepare_bill()));
        } catch (Exception ex) {
            Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTextField16KeyReleased

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        try {
            jTextField1.setText(Float.toString(prepare_bill()));
        } catch (Exception ex) {
            Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        try {
            which_component_request_searchCustomerName = 2;
            r = conn_obj.conn_exec("select customer_id as ID,customer_name As الاسم,customer_tell as الهاتف,customer_location as العنوان from customers order by customer_name");
            jTable1.setModel(DbUtils.resultSetToTableModel(r));
            renderer_jTable_obj.Renderer(jTable1);
        } catch (Exception e) {

        }

        searchCustomerName.pack();
        searchCustomerName.setLocationRelativeTo(this);
        searchCustomerName.setVisible(true);
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jTextField27KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField27KeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            String word = jTextField27.getText().trim();
            int table4RowCount = jTable4.getRowCount();

            for (int i = jTable4.getSelectedRow() + 1; i <= table4RowCount; i++) {

                if (findMe(word, jTable4.getValueAt(i, 0).toString())) {
                    jTable4.scrollRectToVisible(jTable4.getCellRect(i, 0, true));
                    jTable4.setRowSelectionInterval(i, i);
                    i = jTable4.getRowCount();

                }else
                {
                    jTable4.setRowSelectionInterval(0, 0);
                }
            }
        }


    }//GEN-LAST:event_jTextField27KeyReleased

    private void jPanel16ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel16ComponentShown
        create_non_existant_table();        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel16ComponentShown

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed

        
        Object[] column = {
            "الصنف", "الوحدة", "الكمية", "بونص", "سعر الوحدة", " %الخصم بالمئة", "المبلغ", "ملاحظات", "ارباح"
        };
        Object[][] data = getTableData(jTable4);
        DefaultTableModel m = new DefaultTableModel(data, column);
        if(see_profit==true)
        for (int i = 0; i < m.getRowCount(); i++) {
            try {
                String stm="select value \n"
                        + "                    from\n"
                        + "                    ( \n"
                        + "                    select \n"
                        + "                      items.item_units.unit_name,items.item_units.unit_id,\n"
                        + "                      items.main_items.item_name,items.main_items.item_id,items.main_items.item_value*items.item_relations.item_relation as value,\n"
                        + "                      items.item_relations.item_id,items.item_relations.item_unit,items.item_relations.item_price_hole,items.item_relations.item_relation as relation\n"
                        + "                               \n"
                        + "                                from items.item_units,items.main_items,items.item_relations\n"
                        + "                                                               where \n"
                        + "                                items.main_items.item_name LIKE '" + m.getValueAt(i, 0) + "'\n"
                        + "                                AND \n"
                        + "                                items.item_relations.item_id= items.main_items.item_id\n"
                        + "                                and\n"
                        + "                                items.item_relations.item_unit=items.item_units.unit_id\n"
                        + "                                AND\n"
                        + "                                items.item_units.unit_name='" + m.getValueAt(i, 1) + "'\n"
                        + "                                \n"
                        + "                               )  as alias";
                System.out.println(stm);
                r = conn_obj.conn_exec(stm);
                r.next();
                double price = Double.parseDouble(m.getValueAt(i, 6).toString());
                double value = r.getDouble("value");
                double quantity = Double.parseDouble(m.getValueAt(i, 2).toString());
                m.setValueAt(price - (value * quantity), i, 8);
            } catch (SQLException ex) {
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        jTable4.setModel(m);
        jTableCustomization_fayez(jTable4);
        double sum = Math.round(get_table_column_sum(jTable4, 8));

        jLabel53.setText(Double.toString(sum));

        double profit_percentage = get_profit_percentage(Double.parseDouble(jLabel53.getText()), Double.parseDouble(jTextField1.getText()));
        double round_percent = Math.round(profit_percentage * 100.0) / 100.0;
        jLabel54.setText(Double.toString(round_percent));
       
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
    
         Thread t = new Thread(backup_and_restore);
        t.start();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
if(check_admin_password())
{
        fileChooser_restore = new JFileChooser(file_loc);
        int returnVal = fileChooser_restore.showOpenDialog((Component) evt.getSource());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser_restore.getSelectedFile();
            try {
                String fileName = file.toString();
                restore_database(fileName);
            } catch (Exception ex) {
                System.out.println("problem accessing file" + file.getAbsolutePath());
            }
        } else {
            System.out.println("File access cancelled by user.");
        }
}
else
    Joptionpane_message("باسوورد خاطئ");
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        which_component_request_searchCustomerName = 6;
        search_items_in_customer_bills.pack();
        search_items_in_customer_bills.setLocationRelativeTo(this);
        search_items_in_customer_bills.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        if (jCheckBox1.isSelected() == true) {
            Joptionpane_message("ازل اشارة البحث العام");
        } else {
            searchCustomerName.pack();
            searchCustomerName.setLocationRelativeTo(this);
            searchCustomerName.setVisible(true);
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        which_component_request_searchItemName = 2;
        searchItemName.pack();
        searchItemName.setLocationRelativeTo(this);
        searchItemName.setVisible(true);
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        customer_withdraw obj = new customer_withdraw();
        obj.setLocationRelativeTo(null);
        obj.setVisible(true);

    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jTable12MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable12MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable12MouseReleased

    private void jTextField30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField30ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField30ActionPerformed

    private void jTextField34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField34ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField34ActionPerformed

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        which_component_request_searchCustomerName = 8;
        searchCustomerName.pack();
        searchCustomerName.setLocationRelativeTo(this);
        searchCustomerName.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton35ActionPerformed

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        which_component_request_searchItemName = 3;
        searchItemName.pack();
        searchItemName.setLocationRelativeTo(this);
        searchItemName.setVisible(true);
        jTextField4.requestFocus();
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jTextField35FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField35FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField35FocusLost

    private void jTextField35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField35ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField35ActionPerformed

    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
         for (int i = 0; i < jTable12.getRowCount(); i++) {
            if (jTable12.getValueAt(i, 1) != null && jTable12.getValueAt(i, 0) != null) {
                try {
                    //المبدأ هو
                    String item_name, new_item_unit, customer = "";
                    item_name = jTable12.getValueAt(i, 0).toString();
                    new_item_unit = jTable12.getValueAt(i, 1).toString();
                    customer = jComboBox8.getSelectedItem().toString();
                    String stm="select \n" +
"                               unit_name,unit_id,\n" +
"                               items.main_items.item_name,items.main_items.item_id,\n" +
"                               customer_name,customer_id,\n" +
"                                bill_customer_id,public.customer_bills.bill_id,public.customer_bills.bill_date,\n" +
"                                public.customer_bills_items.bill_id,public.customer_bills_items.item_id,public.customer_bills_items.item_unit,public.customer_bills_items.item_price\n" +
"                                \n" +
"                                from\n" +
"                                items.item_units,\n" +
"                                items.main_items,\n" +
"                                public.customers,\n" +
"                                public.customer_bills,\n" +
"                                public.customer_bills_items\n" +
"                                \n" +
"                                where \n" +
"                                items.main_items.item_name= '"+item_name+"' and\n" +
"                                public.customers.customer_name= '"+customer+"' and\n" +
"                                public.customer_bills.bill_customer_id=public.customers.customer_id and\n" +
"                                public.customer_bills.bill_id=public.customer_bills_items.bill_id and\n" +
"                                public.customer_bills_items.item_id=items.main_items.item_id and\n" +
"                                public.customer_bills_items.item_unit=items.item_units.unit_id \n" +
"                                order by bill_date;";
                    r = conn_obj.conn_exec(stm);
                    if (r.next()) {
                        r.last();

                        int old_unit_id = r.getInt("unit_id");

                        int item_id = r.getInt("item_id");

                        float last_price = r.getFloat("item_price");

                        r2 = conn_obj2.conn_exec("select unit_id from items.item_units where unit_name ='" + new_item_unit + "';");
                        r2.next();
                        int new_unit_id = r2.getInt("unit_id");

                        r2 = conn_obj2.conn_exec("select item_relation from items.item_relations where item_id =" + item_id + "  and item_unit =" + old_unit_id + "  ;");
                        r2.next();
                        float old_unit_relation = r2.getFloat("item_relation");

                        r2 = conn_obj2.conn_exec("select item_relation from items.item_relations where item_id =" + item_id + "  and item_unit =" + new_unit_id + "  ;");
                        r2.next();
                        float new_unit_relation = r2.getFloat("item_relation");

                        float new_price = last_price / (old_unit_relation / new_unit_relation);

                        jTable12.setValueAt(new_price, i, 4);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_jButton37ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        try {
            System.out.println(conn_obj.get_con().isClosed());
        } catch (SQLException ex) {
            Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            conn_obj.get_con().setAutoCommit(false);
            jTable12.editingCanceled(null);//هذا اذا نسي المدخل خانات مفتوحة اي مفتوحة للتعديل لا يتعامل معها على انها فارغة فيخرج منها قبل ادخال البيانات
            String customer = (String) jComboBox8.getSelectedItem();//اخذ اسم المورد من القائمة
            if (customer.equals("------")) {
                throw new Exception("لم يتم اختيار الاسم الصحيح");
            }
            float value = float_parsing(jTextField34);
            if (Float.valueOf(jTextField34.getText().toString().trim()) <= 0 || jTextField34.getText().matches("")) {
                throw new Exception("لا يمكن ان تساوي الفاتورة القيمة صفر او فارغ");
            }

            if (value != prepare_bill_customer_returns()) {
                if (check_yes_or_no_question("محموع الفاتورة الفعلي لا يساوي المجموع الموجود؟") == false) {
                    throw new Exception("تم الالغاء!!");
                }
            }

            String location = (String) jComboBox9.getSelectedItem();
            String note = jTextArea5.getText().trim();
            //////////////////////////////////////
            java.util.Date d;
            d = jDateChooser6.getDate();
            java.sql.Date sqlDate = new java.sql.Date(d.getTime());
            ///////////////////////////
            String bill_num = jTextField30.getText().trim();

            ///
            r = conn_obj.conn_exec("select customer_id from customers where customer_name='" + customer + "'");
            r.next();
            int customer_id = Integer.parseInt(r.getString(1));

            String insert_table_content = "";
            insert_table_content = "INSERT INTO return_customer_bills (return_bill_value,return_bill_customer_id,return_bill_location_id,return_bill_note,return_bill_date,return_bill_num) VALUES"
                    + "(" + value + "," + customer_id + ",(select location_id from location where location_name='" + location + "'),'" + note + "','" + sqlDate + "','" + bill_num + "');";
            /////////////////إدخال قيم الجدول ////////////////
            for (int i = 0; i < jTable12.getRowCount(); i++) {
                if (jTable12.getValueAt(i, 0) != null) {
                    insert_table_content += "insert into return_customer_bills_items(return_bill_id,return_item_id,return_item_quantity,return_item_unit,return_item_price,return_item_note)values"
                            + "((select last_value from return_customer_bills_return_bill_id_seq),(select item_id from items.main_items where item_name = '" + jTable12.getValueAt(i, 0) + "' ),"
                            + jTable12.getValueAt(i, 2) + ",(select unit_id from items.item_units where unit_name = '" + jTable12.getValueAt(i, 1) + "' )," + jTable12.getValueAt(i, 3) + ",'" + jTable12.getValueAt(i, 5) + "');";
                }
            }
            ////
            conn_obj.get_st().execute(insert_table_content);

            ////إدخال الكميات وانقاصها من المخزون للمخزن المحدد
            String stm_exec = "";
            stm_exec = "select location_id from location where location_name='" + jComboBox9.getSelectedItem().toString() + "'";

            r = conn_obj.conn_exec(stm_exec);
            System.out.println(stm_exec);
            r.next();
            String location_id = r.getString("location_id");

            for (int i = 0; i < jTable12.getRowCount(); i++) {
                stm_exec = ""
                        + "select quantity,main_item_id from  (\n"
                        + "select \n"
                        + "\n"
                        + "items.main_items.item_name,\n"
                        + "items.main_items.item_id as main_item_id,\n"
                        + "\n"
                        + "items.item_units.unit_id,\n"
                        + "items.item_units.unit_name,\n"
                        + "\n"
                        + "items.item_relations.item_id,\n"
                        + "items.item_relations.item_unit,\n"
                        + "items.item_relations.item_relation*" + jTable12.getValueAt(i, 2) + " as quantity\n"
                        + "\n"
                        + "from items.main_items,items.item_units,items.item_relations\n"
                        + "\n"
                        + "where \n"
                        + "items.main_items.item_name='" + jTable12.getValueAt(i, 0) + "'  AND\n"
                        + "items.item_units.unit_name='" + jTable12.getValueAt(i, 1) + "'  AND\n"
                        + "items.main_items.item_id=items.item_relations.it"
                        + "em_id AND\n"
                        + "items.item_units.unit_id=items.item_relations.item_unit )as anyThing";
                r = conn_obj.conn_exec(stm_exec);
                r.next();
                System.out.println(stm_exec);
                double quantity = r.getDouble("quantity");
                int item_id = r.getInt("main_item_id");
                String store_to_update = "store_id_" + location_id;//store_id_1
                stm_exec = "update items.inventory set " + store_to_update + " = " + store_to_update + " + " + quantity + " where item_id=" + item_id + "";
                conn_obj.exec(stm_exec);
            }

            //////////////////////////////////////////////////
            check_two_input_customer(customer_id, sqlDate);

            conn_obj.get_con().commit();
            Joptionpane_message("لقد تم إدخال البيانات");
            float account = get_customer_account_sum(customer_id);
            Joptionpane_message("حساب  " + customer + "  يساوي\n" + account + "");
            move_to_customer_vendor_account();
            ///////////////////////////////////////////////////to write on file //////////////////////
            write_to_file(get_date() + "     " + sqlDate.toString() + " " + "  إدخال فاتورةمرتجعات  لحساب الزبون  " + customer + " " + "بقيمة " + " "
                    + Float.toString(value) + " " + "الرصيد = " + " " + Float.toString(account));

            reset_return_customer_bill();

        } catch (Exception ex) {
            try {
                conn_obj.get_con().rollback();
                Joptionpane_message(ex.getMessage().toString());
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception e) {
                Joptionpane_message(e.getMessage().toString());
            }
            //Logger.getLogger(vendors.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        DefaultTableModel tm = (DefaultTableModel) jTable12.getModel();
        int[] rows_array;
        rows_array = jTable12.getSelectedRows();
        for (int i = 0; i < rows_array.length; i++) {
            int selectd_row = rows_array[i] - i;
            tm.removeRow(selectd_row);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        check_conn_available();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        show_customer_or_vend_checks sh = new show_customer_or_vend_checks(this);
        sh.setVisible(true);
        sh.setLocationRelativeTo(this);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void copy_of_billActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copy_of_billActionPerformed
        copy_bill();        // TODO add your handling code here:
    }//GEN-LAST:event_copy_of_billActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        r = conn_obj.conn_exec("select customer_name as اسم_الزبون,date as تاريخ_النقص,item_note as ملاحظة_الصنف, item_name as اسم_الصنف\n"
                + "from \n"
                + "(\n"
                + "select customers.customer_id,customers.customer_name,\n"
                + "       items.non_existant_items.*,\n"
                + "       items.main_items.item_id,\n"
                + "       items.main_items.item_name\n"
                + "       \n"
                + "       from customers,items.non_existant_items,items.main_items\n"
                + "\n"
                + "       where\n"
                + "       customer_name='" + (String) jComboBox1.getSelectedItem() + "' and\n"
                + "       items.non_existant_items.customer_id=customers.customer_id and\n"
                + "       items.main_items.item_id=items.non_existant_items.item_id\n"
                + "         \n"
                + ") as fd order by تاريخ_النقص");

        jTable6.setModel(DbUtils.resultSetToTableModel(r));
        customer_bill.setLocationRelativeTo(this);
        jPanel14.setVisible(false);
        customer_bill.pack();
        customer_bill.setLocationRelativeTo(this);
        customer_bill.setVisible(true);
        show_last_row_scroll_jtable(jTable6);
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        int number = 0;
        JTextField field1 = new JTextField("");
        JPanel panel = new JPanel(new GridLayout(0, 1));
        Checkbox c = new Checkbox("حفظ كل الحركات؟");
        Checkbox details = new Checkbox("حفظ تفصيلي؟");
        JLabel x = new JLabel("أدخل عدد الحركات للحفظ");
        x.setFont(FayezFont);
        panel.add(x);
        panel.add(field1);
        panel.add(c);
        panel.add(details);
        panel.setFont(FayezFont);
        int result = JOptionPane.showConfirmDialog(null, panel, "?",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            if (c.getState() == true) {
                number = jTable3.getRowCount();
            } else {
                String Num = field1.getText();
                number = Integer.parseInt(Num);
                if (number > jTable3.getRowCount()) {
                    Joptionpane_message("العدد المختار أكبر من عدد صفوف الجدول!");
                }
            }
            try {
                if (c.getState() == true && details.getState() == false) {
                    print_jtable(number,0, "save_to_file");
                } else if (c.getState() == true && details.getState() == true) {
                    print_jtable_with_details(number,0, "save_to_file");
                } else if (c.getState() == false && details.getState() == true) {
                    print_jtable_with_details(number,0, "save_to_file");
                } else if (c.getState() == false && details.getState() == false) {
                    print_jtable(number,0, "save_to_file");
                } else {
                    print_jtable(number,0, "save_to_file");
                }
            } catch (SQLException ex) {
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Joptionpane_message("Canceled");
        }
    }//GEN-LAST:event_jButton34ActionPerformed

    private void jButton38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton38ActionPerformed
        send_email send_email_obj = new send_email();
        send_email_obj.setLocationRelativeTo(this);
        send_email_obj.setVisible(true);
    }//GEN-LAST:event_jButton38ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        email email_obj = new email();
        email_obj.setLocationRelativeTo(this);
        email_obj.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        show_customers_accounts("");   //تشمل جميع الزبائن     // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        mecha=new meachanism_to_search_customers_accounts(this);
        mecha.pack();
    mecha.setLocationRelativeTo(this);
    mecha.setVisible(true);

    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        item_withdrawales sh = new item_withdrawales();
        sh.setVisible(true);
        sh.setLocationRelativeTo(this);
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jTable15MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable15MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable15MouseReleased

    private void jButton39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton39ActionPerformed
DefaultTableModel tm = (DefaultTableModel) jTable15.getModel();
        int[] rows_array;
        rows_array = jTable15.getSelectedRows();
        for (int i = 0; i < rows_array.length; i++) {
            int selectd_row = rows_array[i] - i;
            tm.removeRow(selectd_row);   
        }// TODO add your handling code here:
    }//GEN-LAST:event_jButton39ActionPerformed

    private void jButton40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton40ActionPerformed
      try {
            System.out.println(conn_obj.get_con().isClosed());
        } catch (SQLException ex) {
            Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            conn_obj.get_con().setAutoCommit(false);
            jTable15.editingCanceled(null);//هذا اذا نسي المدخل خانات مفتوحة اي مفتوحة للتعديل لا يتعامل معها على انها فارغة فيخرج منها قبل ادخال البيانات
            String customer = (String) jComboBox10.getSelectedItem();//اخذ اسم المورد من القائمة
            if (customer.equals("------")) {
                throw new Exception("لم يتم اختيار الاسم الصحيح");
            }
            float value = float_parsing(jTextField36);
            if (Float.valueOf(jTextField36.getText().toString().trim()) <= 0 || jTextField36.getText().matches("")) {
                throw new Exception("لا يمكن ان تساوي الفاتورة القيمة صفر او فارغ");
            }

            if (value != prepare_bill_jtable15_returns()) {
                if (check_yes_or_no_question("محموع الفاتورة الفعلي لا يساوي المجموع الموجود؟") == false) {
                    throw new Exception("تم الالغاء!!");
                }
            }

            String location = (String) jComboBox11.getSelectedItem();
            String note = jTextArea6.getText().trim();
            //////////////////////////////////////
            java.util.Date d;
            d = jDateChooser7.getDate();
            java.sql.Date sqlDate = new java.sql.Date(d.getTime());
            ///////////////////////////
            String bill_num = jTextField31.getText().trim();

            ///
            conn_obj.get_st().execute("delete from return_customer_bills where return_bill_id = " + jLabel23.getText() + " and accounted=false");
            
            r = conn_obj.conn_exec("select customer_id from customers where customer_name='" + customer + "'");
            r.next();
            int customer_id = Integer.parseInt(r.getString(1));

            String insert_table_content = "";
            insert_table_content = "INSERT INTO return_customer_bills (return_bill_value,return_bill_customer_id,return_bill_location_id,return_bill_note,return_bill_date,return_bill_num,return_bill_id) VALUES"
                    + "(" + value + "," + customer_id + ",(select location_id from location where location_name='" + location + "'),'" + note + "','" + sqlDate + "','" + bill_num + "'," + jLabel23.getText() + ");";
            /////////////////إدخال قيم الجدول ////////////////
            for (int i = 0; i < jTable15.getRowCount(); i++) {
                if (jTable15.getValueAt(i, 0) != null) {
                    insert_table_content += "insert into return_customer_bills_items(return_item_id,return_item_quantity,return_item_unit,return_item_price,return_item_note,return_bill_id)values"
                            + "((select item_id from items.main_items where item_name = '" + jTable15.getValueAt(i, 0) + "' ),"
                            + jTable15.getValueAt(i, 2) + ",(select unit_id from items.item_units where unit_name = '" + jTable15.getValueAt(i, 1) + "' )," + jTable15.getValueAt(i, 3) + ",'" + jTable15.getValueAt(i, 5) + "'," + jLabel23.getText() + ");";
                }
            }
            ////
            conn_obj.get_st().execute(insert_table_content);

            ////إدخال الكميات وانقاصها من المخزون للمخزن المحدد
            String stm_exec = "";
            stm_exec = "select location_id from location where location_name='" + jComboBox11.getSelectedItem().toString() + "'";

            r = conn_obj.conn_exec(stm_exec);
            
            r.next();
            String location_id = r.getString("location_id");

            for (int i = 0; i < jTable15.getRowCount(); i++) {
                stm_exec = ""
                        + "select quantity,main_item_id from  (\n"
                        + "select \n"
                        + "\n"
                        + "items.main_items.item_name,\n"
                        + "items.main_items.item_id as main_item_id,\n"
                        + "\n"
                        + "items.item_units.unit_id,\n"
                        + "items.item_units.unit_name,\n"
                        + "\n"
                        + "items.item_relations.item_id,\n"
                        + "items.item_relations.item_unit,\n"
                        + "items.item_relations.item_relation*" + jTable15.getValueAt(i, 2) + " as quantity\n"
                        + "\n"
                        + "from items.main_items,items.item_units,items.item_relations\n"
                        + "\n"
                        + "where \n"
                        + "items.main_items.item_name='" + jTable15.getValueAt(i, 0) + "'  AND\n"
                        + "items.item_units.unit_name='" + jTable15.getValueAt(i, 1) + "'  AND\n"
                        + "items.main_items.item_id=items.item_relations.it"
                        + "em_id AND\n"
                        + "items.item_units.unit_id=items.item_relations.item_unit )as anyThing";
                r = conn_obj.conn_exec(stm_exec);
                r.next();
                
                double quantity = r.getDouble("quantity");
                int item_id = r.getInt("main_item_id");
                String store_to_update = "store_id_" + location_id;//store_id_1
                stm_exec = "update items.inventory set " + store_to_update + " = " + store_to_update + " + " + quantity + " where item_id=" + item_id + "";
                conn_obj.exec(stm_exec);
                
            }
             conn_obj.exec(stm_to_return_quantity_to_store);
             stm_to_return_quantity_to_store="";
            //////////////////////////////////////////////////
            check_two_input_customer(customer_id, sqlDate);

            conn_obj.get_con().commit();
            Joptionpane_message("لقد تم تعديل البيانات");
            float account = get_customer_account_sum(customer_id);
            Joptionpane_message("حساب  " + customer + "  يساوي\n" + account + "");
           
            ///////////////////////////////////////////////////to write on file //////////////////////
            write_to_file(get_date() + "     " + sqlDate.toString() + " " + "  إدخال فاتورةمرتجعات  لحساب الزبون  " + customer + " " + "بقيمة " + " "
                    + Float.toString(value) + " " + "الرصيد = " + " " + Float.toString(account));

            update_or_show_returned_bill_items.dispose();
            jTabbedPane1.setSelectedIndex(2);
            create_customer_account_table();
            

            
        } catch (Exception ex) {
            try {
                conn_obj.get_con().rollback();
                Joptionpane_message(ex.getMessage().toString());
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception e) {
                Joptionpane_message(e.getMessage().toString());
            }
            //Logger.getLogger(vendors.class.getName()).log(Level.SEVERE, null, ex);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton40ActionPerformed

    private void jTextField36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField36ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField36ActionPerformed

    private void jButton41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton41ActionPerformed
which_component_request_searchCustomerName = 9;
        searchCustomerName.pack();
        searchCustomerName.setLocationRelativeTo(this);
        searchCustomerName.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton41ActionPerformed

    private void jTextField31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField31ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField31ActionPerformed

    private void jButton42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton42ActionPerformed
which_component_request_searchItemName = 5;
        searchItemName.pack();
        searchItemName.setLocationRelativeTo(this);
        searchItemName.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton42ActionPerformed

    private void jTextField37FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField37FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField37FocusLost

    private void jTextField37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField37ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField37ActionPerformed

    private void jButton43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton43ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton43ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        show_vendor_goods xxxss= new show_vendor_goods();
        xxxss.setLocationRelativeTo(this);
        xxxss.setVisible(true);
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        customer_discount dis;
        if (discount_cus==true) {
            dis = new customer_discount(this);
            dis.setLocationRelativeTo(this);
            dis.setVisible(true);
        }
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
      search_check s=new search_check();
      s.setLocationRelativeTo(this);
      s.setVisible(true);
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
script s=new script();
s.setLocationRelativeTo(this);
s.setVisible(true);// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jDateChooser1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDateChooser1KeyPressed
  
      System.out.println("fffff");
         // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooser1KeyPressed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
search_vendor_items s=new search_vendor_items();
        s.setLocationRelativeTo(this);
        s.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
banks b=new banks();
        b.setLocationRelativeTo(this);
        b.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jTable7KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable7KeyReleased
if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTable7.editingCanceled(null);
           if (which_component_request_searchItemName == 1) {
                //نحن هنا في اضافة صنف لفاتورة في اضافة فاتورة لزبون
               add_item_from_SearchItemName_to_customer_bill();
               
               } 
           else if (which_component_request_searchItemName == 3) {
                try {
                        String item_name, new_item_unit, customer = "";
                        item_name = jTable7.getValueAt(jTable7.getSelectedRow(), 0).toString();
                        new_item_unit = jTable7.getValueAt(jTable7.getSelectedRow(), 1).toString();
                        customer = jComboBox8.getSelectedItem().toString();
                        r = conn_obj.conn_exec("select \n" +
"                               unit_name,unit_id,\n" +
"                               items.main_items.item_name,items.main_items.item_id,\n" +
"                               customer_name,customer_id,\n" +
"                                bill_customer_id,public.customer_bills.bill_id,public.customer_bills.bill_date,\n" +
"                                public.customer_bills_items.bill_id,public.customer_bills_items.item_id,public.customer_bills_items.item_unit,public.customer_bills_items.item_price\n" +
"                                \n" +
"                                from\n" +
"                                items.item_units,\n" +
"                                items.main_items,\n" +
"                                public.customers,\n" +
"                                public.customer_bills,\n" +
"                                public.customer_bills_items\n" +
"                                \n" +
"                                where \n" +
"                                items.main_items.item_name= '"+item_name+"' and\n" +
"                                public.customers.customer_name= '"+customer+"' and\n" +
"                                public.customer_bills.bill_customer_id=public.customers.customer_id and\n" +
"                                public.customer_bills.bill_id=public.customer_bills_items.bill_id and\n" +
"                                public.customer_bills_items.item_id=items.main_items.item_id and\n" +
"                                public.customer_bills_items.item_unit=items.item_units.unit_id \n" +
"                                order by bill_date;");
                        
    

                        // نريد ان نفحص اذا الكمية تساوي فارغ فهي واحد  
                        String quantity="1";
                        if (!jTable7.getValueAt(jTable7.getSelectedRow(), 3).toString().trim().equals(""))
                        {
                            quantity=jTable7.getValueAt(jTable7.getSelectedRow(), 3).toString();   
                        }
                        //انتهي الفحص
                        
                        if (!r.next()) {
                            DefaultTableModel tm = (DefaultTableModel) jTable12.getModel();
                tm.addRow(new Object[]{
                    jTable7.getValueAt(jTable7.getSelectedRow(), 0),
                    jTable7.getValueAt(jTable7.getSelectedRow(), 1),
                    jTable7.getValueAt(jTable7.getSelectedRow(), 3),
                    jTable7.getValueAt(jTable7.getSelectedRow(), 2),
                    null,
                    jTable7.getValueAt(jTable7.getSelectedRow(), 4)});

                        } else {
                            r.last();

                            int old_unit_id = r.getInt("unit_id");
                            
                            int item_id = r.getInt("item_id");
                            
                            float last_price = r.getFloat("item_price");
                            
                            r2 = conn_obj2.conn_exec("select unit_id from items.item_units where unit_name ='" + new_item_unit + "';");
                            r2.next();
                            int new_unit_id = r2.getInt("unit_id");
                            
                            r2 = conn_obj2.conn_exec("select item_relation from items.item_relations where item_id =" + item_id + "  and item_unit =" + old_unit_id + "  ;");
                            r2.next();
                            float old_unit_relation = r2.getFloat("item_relation");
                            
                            r2 = conn_obj2.conn_exec("select item_relation from items.item_relations where item_id =" + item_id + "  and item_unit =" + new_unit_id + "  ;");
                            r2.next();
                            float new_unit_relation = r2.getFloat("item_relation");
                            
              float new_price=last_price/(old_unit_relation/new_unit_relation);
              
               DefaultTableModel tm = (DefaultTableModel) jTable12.getModel();
                tm.addRow(new Object[]{
                    item_name,new_item_unit,jTable7.getValueAt(jTable7.getSelectedRow(), 3),new_price,null,jTable7.getValueAt(jTable7.getSelectedRow(), 4)
                });
              
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
                    }
                show_last_row_scroll_jtable(jTable12);
            }
            show_last_row_scroll_jtable(jTable12);//هنا الذهاب لاخر صف مكتوب بجدول الفاتورة لامكانية رؤيته مباشرة

            jTextField4.requestFocus();
            jTextField4.selectAll();

}    
    }//GEN-LAST:event_jTable7KeyReleased

    private void jTable_bill_itemsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_bill_itemsMouseReleased
        if (SwingUtilities.isRightMouseButton(evt) && jTable_bill_items.columnAtPoint(evt.getPoint()) == 1) {//if right click and column units
            //////
            try {
                jPopupMenu2.removeAll();
                int row_selected = jTable_bill_items.rowAtPoint(evt.getPoint());
                jTable_bill_items.setRowSelectionInterval(row_selected, row_selected);
                String item_name = jTable_bill_items.getValueAt(row_selected, 0).toString().trim();
                r = conn_obj.conn_exec("select items.item_relations.item_id,items.item_relations.item_unit,\n"
                    + "items.item_units.unit_name,items.item_units.unit_id,\n"
                    + "items.main_items.item_name,items.main_items.item_id \n"
                    + "from items.item_relations,items.item_units,items.main_items\n"
                    + "where \n"
                    + "items.main_items.item_name='" + item_name + "' and\n"
                    + "items.item_relations.item_id=items.main_items.item_id \n"
                    + "AND\n"
                    + "items.item_relations.item_unit = items.item_units.unit_id ");
                while (r.next()) {
                    JMenuItem item = new JMenuItem(r.getString("unit_name"));
                    item.addActionListener(new java.awt.event.ActionListener() {

                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            select_unit_in_jTable_bill_items_right_click(evt);
                        }
                    });
                    jPopupMenu2.add(item);
                }

            } catch (SQLException ex) {
               
            }

            jPopupMenu2.show(evt.getComponent(), evt.getX(), evt.getY());
        } //////اذا نريد التسعيرة الاخيرة للصنف
        else if (SwingUtilities.isRightMouseButton(evt) && jTable_bill_items.columnAtPoint(evt.getPoint()) == 4) {//if right click and column price

            try {
                jPopupMenu2.removeAll();
                int row_selected = jTable_bill_items.rowAtPoint(evt.getPoint());
                jTable_bill_items.setRowSelectionInterval(row_selected, row_selected);

                String item_unit, customer, item_name = "";
                item_name = jTable_bill_items.getValueAt(row_selected, 0).toString().trim();
                item_unit = jTable_bill_items.getValueAt(row_selected, 1).toString().trim();
                customer = jCombo_vendor_name.getSelectedItem().toString();

                r = conn_obj.conn_exec("select \n"
                    + "unit_name,unit_id,\n"
                    + "items.main_items.item_name,items.main_items.item_id,\n"
                    + "vendor_name,vendor_id,\n"
                    + "bill_vendor_id,public.vendor_bills.bill_id,vendor_bills.bill_date,\n"
                    + "public.vendor_bills_items.bill_id,public.vendor_bills_items.item_id,public.vendor_bills_items.item_unit,public.vendor_bills_items.item_price\n"
                    + "\n"
                    + "from\n"
                    + "items.item_units,\n"
                    + "items.main_items,\n"
                    + "public.vendors,\n"
                    + "public.vendor_bills,\n"
                    + "public.vendor_bills_items\n"
                    + "\n"
                    + "where \n"
                    + "items.item_units.unit_name= '" + item_unit + "'  and\n"
                    + "items.main_items.item_name= '" + item_name + "' and\n"
                    + "public.vendors.vendor_name= '" + customer + "' and\n"
                    + "public.vendor_bills.bill_vendor_id=public.vendors.vendor_id and\n"
                    + "public.vendor_bills.bill_id=public.vendor_bills_items.bill_id and\n"
                    + "public.vendor_bills_items.item_id=items.main_items.item_id and\n"
                    + "public.vendor_bills_items.item_unit=items.item_units.unit_id ;\n"
                    + "\n"
                    + "");
                r.last();
                r.previous();
                r.previous();
                r.previous();

                while (r.next()) {
                    JMenuItem item = new JMenuItem(r.getString("item_price") + "=" + r.getString("bill_date"));
                    item.addActionListener(new java.awt.event.ActionListener() {

                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt) {

                            String priceAndDate = evt.getActionCommand().trim();
                            String[] parts = priceAndDate.split("=");
                            String price = parts[0];

                            jTable_bill_items.setValueAt(price, jTable_bill_items.getSelectedRow(), 4);
                        }
                    });
                    jPopupMenu2.add(item);
                }
                //add jMenue with item see_cost
                JMenu menu_cost = new JMenu("السعر علينا");
                jPopupMenu2.insert(menu_cost, 0);
                r = conn_obj.conn_exec("select item_name as الاسم,unit_name as الوحدة,to_char(value, '9999999999.00') as السعر_علينا,relation as علاقتها_بالرئيسية\n"
                    + "                    from\n"
                    + "                    ( \n"
                    + "                    select \n"
                    + "                      items.item_units.unit_name,items.item_units.unit_id,\n"
                    + "                      items.main_items.item_name,items.main_items.item_id,items.main_items.item_value*items.item_relations.item_relation as value,\n"
                    + "                      items.item_relations.item_id,items.item_relations.item_unit,items.item_relations.item_price_hole,items.item_relations.item_relation as relation\n"
                    + "                               \n"
                    + "                                from items.item_units,items.main_items,items.item_relations\n"
                    + "                                                               where \n"
                    + "                                items.main_items.item_name ='" + item_name + "'\n"
                    + "                                AND \n"
                    + "                                items.item_relations.item_id= items.main_items.item_id\n"
                    + "                                and\n"
                    + "                                items.item_relations.item_unit=items.item_units.unit_id\n"
                    + "                                and\n"
                    + "                                items.item_relations.item_unit=(select unit_id from items.item_units where unit_name='" + item_unit + "')     \n"
                    + "\n"
                    + "                                   )  as alias");
                r.next();
                menu_cost.add(r.getString(3));
                ////End add jMenue hold item_Cost
            } catch (SQLException ex) {
                
            }

            jPopupMenu2.show(evt.getComponent(), evt.getX(), evt.getY());
        } 
    
    }//GEN-LAST:event_jTable_bill_itemsMouseReleased

    private void jButton_remove_bill_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_remove_bill_itemActionPerformed
        DefaultTableModel tm = (DefaultTableModel) jTable_bill_items.getModel();
        int[] rows_array;
        rows_array = jTable_bill_items.getSelectedRows();
        for (int i = 0; i < rows_array.length; i++) {
            int selectd_row = rows_array[i] - i;
            tm.removeRow(selectd_row);
        }
    }//GEN-LAST:event_jButton_remove_bill_itemActionPerformed

    private void jButton_isert_billActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_isert_billActionPerformed
        try {
            System.out.println(conn_obj.get_con().isClosed());
        } catch (SQLException ex) {
           
        }
        try {
            conn_obj.get_con().setAutoCommit(false);
            jTable_bill_items.editingCanceled(null);//هذا اذا نسي المدخل خانات مفتوحة اي مفتوحة للتعديل لا يتعامل معها على انها فارغة فيخرج منها قبل ادخال البيانات
            String vendor = (String) jCombo_vendor_name.getSelectedItem();//اخذ اسم المورد من القائمة
            if (vendor.equals("------")) {
                throw new Exception("لم يتم اختيار الاسم الصحيح");
            }
            float value = float_parsing(jTextField_bill_value);
            if (Float.valueOf(jTextField_bill_value.getText().toString().trim()) <= 0 || jTextField_bill_value.getText().matches("")) {
                throw new Exception("لا يمكن ان تساوي الفاتورة القيمة صفر او فارغ");
            }
            float bill_value_discount = float_parsing(jTextField_bill_discount);
            float bill_ratio_discount = float_parsing(jTextField_bill_dis_ratio);
            if (bill_ratio_discount > 100 || bill_value_discount > value) {
                throw new Exception("لا يمكن ان يكون الخصم اكبر من قيمة الفاتورة ");
            }
            if (value != prepare_ven_bill()) {
                if (check_yes_or_no_question("محموع الفاتورة الفعلي لا يساوي المجموع الموجود؟") == false) {
                    throw new Exception("تم الالغاء!!");
                }
            }

            String location = (String) jComboBox_bill_store.getSelectedItem();
            r=conn_obj.conn_exec("select location_id from location where location_name='"+location+"'");
            r.next();
            int location_id=Integer.parseInt(r.getString(1));

            String note = jTextArea_bill_note.getText().trim();
            //////////////////////////////////////
            java.util.Date d;
            d = jDateChooser_bill_date.getDate();
            java.sql.Date sqlDate = new java.sql.Date(d.getTime());
            ///////////////////////////
            String bill_num = jTextField_bill_number.getText().trim();

            ///
            r = conn_obj.conn_exec("select vendor_id from vendors where vendor_name='" + vendor + "'");
            r.next();
            int vendor_id=Integer.parseInt(r.getString(1));
            String stm_exec = "";//inventory statement
            String insert_table_content = "";
            insert_table_content += "INSERT INTO vendor_bills (bill_value,bill_vendor_id,bill_location_id,bill_note,bill_date,discount_amount,discount_ratio,bill_num) VALUES"
            + "(" + value + "," + vendor_id + ",(select location_id from location where location_name='" + location + "'),'" + note + "','" + sqlDate + "'," + bill_value_discount + "," + bill_ratio_discount + ",'" + bill_num + "');";
            /////////////////إدخال قيم الجدول ////////////////
            for (int i = 0; i < jTable_bill_items.getRowCount(); i++) {
                if (jTable_bill_items.getValueAt(i, 0) != null) {
                    insert_table_content += "insert into vendor_bills_items(bill_id,item_id,item_quantity,item_unit,item_price,item_bonus,item_note,discount_ratio)values"
                    + "((select last_value from vendor_bills_counter),(select item_id from items.main_items where item_name = '" + jTable_bill_items.getValueAt(i, 0) + "' ),"
                    + jTable_bill_items.getValueAt(i, 2) + ",(select unit_id from items.item_units where unit_name = '" + jTable_bill_items.getValueAt(i, 1) + "' )," + jTable_bill_items.getValueAt(i, 4) + "," + jTable_bill_items.getValueAt(i, 3) + ",'" + jTable_bill_items.getValueAt(i, 7) + "','" + jTable_bill_items.getValueAt(i, 5) + "');";

                    ////تغيير قيم اسعار السلع في جدول العناصر
                    float item_price=Float.parseFloat(jTable_bill_items.getValueAt(i, 4).toString());
                    float item_price_after_item_discount_ratio=item_price-(item_price*Float.parseFloat(jTable_bill_items.getValueAt(i, 5).toString().trim())/100);
                    float item_price_after_bill_discount=item_price_after_item_discount_ratio-(item_price_after_item_discount_ratio*(bill_ratio_discount/100));
                    insert_table_content+="UPDATE items.main_items\n" +
                    "SET item_value="
                    + "("+item_price_after_bill_discount+"/(select item_relation from items.item_relations where item_unit =(select unit_id from items.item_units where unit_name='" + jTable_bill_items.getValueAt(i, 1) + "') and \n" +
                    "item_id = (select item_id from items.main_items where item_name ='"+jTable_bill_items.getValueAt(i, 0)+"')))"
                    + "where items.main_items.item_name='"+jTable_bill_items.getValueAt(i, 0)+"';";
                    //من هنا يبدا تعديل الكميات ي المخازن
                    
                    float quantity=Float.parseFloat(jTable_bill_items.getValueAt(i, 2).toString());
                    float Bounus=Float.parseFloat(jTable_bill_items.getValueAt(i, 3).toString());
                    float net_Q=quantity+Bounus;
                    String stm_exec_ex = ""
                    + "select quantity,main_item_id from  (\n"
                    + "select \n"
                    + "\n"
                    + "items.main_items.item_name,\n"
                    + "items.main_items.item_id as main_item_id,\n"
                    + "\n"
                    + "items.item_units.unit_id,\n"
                    + "items.item_units.unit_name,\n"
                    + "\n"
                    + "items.item_relations.item_id,\n"
                    + "items.item_relations.item_unit,\n"
                    + "items.item_relations.item_relation*" + net_Q + " as quantity\n"
                    + "\n"
                    + "from items.main_items,items.item_units,items.item_relations\n"
                    + "\n"
                    + "where \n"
                    + "items.main_items.item_name='" + jTable_bill_items.getValueAt(i, 0) + "'  AND\n"
                    + "items.item_units.unit_name='" + jTable_bill_items.getValueAt(i, 1) + "'  AND\n"
                    + "items.main_items.item_id=items.item_relations.it"
                    + "em_id AND\n"
                    + "items.item_units.unit_id=items.item_relations.item_unit )as anyThing";
                    r = conn_obj.conn_exec(stm_exec_ex);
                    r.next();
                    double quantity_to_Increase = r.getDouble("quantity") ;
                    int item_id = r.getInt("main_item_id");
                    String store_to_update = "store_id_" + location_id;//store_id_1
                    stm_exec += "update items.inventory set " + store_to_update + " = " + store_to_update + " + " + quantity_to_Increase + " where item_id=" + item_id + ";";
                    //انتهى تعديل الكميات ف المخازن
                    
                    // من هنا نبدأ بحذف الاصناف من النواقص
                    insert_table_content+="delete from items.non_existant_items where item_id = (select item_id from items.main_items where item_name ='"+jTable_bill_items.getValueAt(i, 0)+"');";
                    //انتهينا من حذف الاصناف من النواقص
                }

            }
            conn_obj.exec(stm_exec);
            conn_obj.get_st().execute(insert_table_content);

            //////////////////////////////////////////////////
            check_two_input_vendor(vendor_id, sqlDate);

            conn_obj.get_con().commit();
            Joptionpane_message("لقد تم إدخال البيانات");
            float account = get_vendor_account_sum(vendor_id);
            Joptionpane_message("حساب  " + vendor + "  يساوي\n" + account + "");
            //jTabbedPane_vendor_action.setSelectedIndex(0);
            move_to_vendor_account();
            ///////////////////////////////////////////////////to write on file //////////////////////
            write_to_file(get_date() + "     " + sqlDate.toString() + " " + "  إدخال فاتورة لحساب الزبون  " + vendor + " " + "بقيمة " + " "
                + Float.toString(value) + " " + "الرصيد = " + " " + Float.toString(account));

            reset_vendor_add_bill();

        } catch (Exception ex) {
            try {
                conn_obj.get_con().rollback();
                Joptionpane_message(ex.getMessage().toString());
              
            } catch (Exception e) {
                Joptionpane_message(e.getMessage().toString());
            }
            //Logger.getLogger(vendors.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton_isert_billActionPerformed

    private void jTextField_bill_discountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_bill_discountKeyReleased
        try {
            jTextField_bill_value.setText(Float.toString(prepare_ven_bill()));
        } catch (Exception ex) {
          
        }
    }//GEN-LAST:event_jTextField_bill_discountKeyReleased

    private void jTextField_bill_dis_ratioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_bill_dis_ratioKeyReleased
        try {
            jTextField_bill_value.setText(Float.toString(prepare_ven_bill()));
        } catch (Exception ex) {
           
        }
    }//GEN-LAST:event_jTextField_bill_dis_ratioKeyReleased

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed

        
        try {
            String v_name = (String) jCombo_vendor_name.getSelectedItem();
            if (v_name.equals("------")) {
                throw new IOException("خطأ في اسم المورد");
            }
            r = conn_obj.conn_exec("select last_value from vendor_bills_counter");
            r.next();
            StringSelection stringSelection = new StringSelection(String.valueOf(r.getInt(1) + 1 + "-" + (String) jComboBox1.getSelectedItem()));
            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
            clpbrd.setContents(stringSelection, null);
            Runtime.getRuntime().exec("C:\\Windows\\system32\\wfs.exe");
        } catch (SQLException ex) {
            Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Joptionpane_message(ex.getMessage());
            Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jTextField_bill_before_disActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_bill_before_disActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_bill_before_disActionPerformed

    private void jTextField_bill_valueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_bill_valueActionPerformed
        jButton_isert_bill.doClick();
    }//GEN-LAST:event_jTextField_bill_valueActionPerformed

    private void jDateChooser_bill_dateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDateChooser_bill_dateKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER ) {
            System.out.println("fffff");
            jButton_isert_bill.doClick();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooser_bill_dateKeyPressed

    private void jButton44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton44ActionPerformed
        which_component_request_searchCustomerName_vendor = 3;
        searchvendorName.pack();
        searchvendorName.setLocationRelativeTo(this);
        searchvendorName.setVisible(true);
    }//GEN-LAST:event_jButton44ActionPerformed

    private void jButton_search_bill_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_search_bill_itemActionPerformed
        which_component_request_searchItemName_vendor = 1;
        search_ven_ItemName.pack();
        search_ven_ItemName.setLocationRelativeTo(this);
        search_ven_ItemName.setVisible(true);
        search_ven_ItemName.setState(NORMAL);
        jTextField25.requestFocus();
    }//GEN-LAST:event_jButton_search_bill_itemActionPerformed

    private void jTextField_search_barcodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_search_barcodeFocusLost
        String barcode = jTextField_search_barcode.getText().trim();
        if (!barcode.equals("")) {
            try {

                r = conn_obj.conn_exec("select items.main_items.item_name,items.main_items.item_id,\n"
                    + "\n"
                    + "items.item_units.unit_id,items.item_units.unit_name,\n"
                    + "\n"
                    + "items.item_relations.item_id,items.item_relations.item_unit,items.item_relations.item_barcode,items.item_relations.item_price_retail\n"
                    + "\n"
                    + "from\n"
                    + "items.main_items,items.item_units,items.item_relations\n"
                    + "\n"
                    + "where \n"
                    + "item_barcode='" + barcode + "' and\n"
                    + "items.item_relations.item_id=items.main_items.item_id and\n"
                    + "items.item_relations.item_unit=items.item_units.unit_id");

                if (r.next()) {
                    boolean found = false;
                    //check if the item founded in the Table
                    for (int i = 0; i < jTable_bill_items.getRowCount(); i++) {
                        if (jTable_bill_items.getValueAt(i, 0) != null) {
                            if (jTable_bill_items.getValueAt(i, 0).toString().equals(r.getString("item_name")) && jTable_bill_items.getValueAt(i, 1).toString().equals(r.getString("unit_name"))) {
                                jTable_bill_items.setValueAt(float_parsing_2(jTable_bill_items.getValueAt(i, 2).toString()) + 1, i, 2);
                                found = true;//that mean the item founded increse the item count by one
                            }
                        }
                    }
                    //
                    if (found == false) {
                        add_row_jtable4(jTable_bill_items, (r.getString("item_name")), (r.getString("unit_name")), 1, 0, (r.getString("item_price_retail")), 0, null, null);
                    }

                    jTextField_search_barcode.setText("");
                    jTextField_search_barcode.requestFocus();
                } else {
                    Joptionpane_message(barcode + "هذا الباركود غير موجود");
                    jTextField_search_barcode.setText("");
                    jTextField_search_barcode.requestFocus();
                }
            } catch (SQLException ex) {
                
            } catch (Exception ex) {
                
            }
        }
    }//GEN-LAST:event_jTextField_search_barcodeFocusLost

    private void jTextField_search_barcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_search_barcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_search_barcodeActionPerformed

    private void jTextField_search_bill_itemsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_search_bill_itemsKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            String word = jTextField_search_bill_items.getText().trim();
            int table4RowCount = jTable_bill_items.getRowCount();

            for (int i = jTable_bill_items.getSelectedRow() + 1; i < table4RowCount; i++) {

                if (findMe(word, jTable_bill_items.getValueAt(i, 0).toString())) {
                    jTable_bill_items.scrollRectToVisible(jTable_bill_items.getCellRect(i, 0, true));
                    jTable_bill_items.setRowSelectionInterval(i, i);
                    i = jTable_bill_items.getRowCount();

                }
            }
        }

    }//GEN-LAST:event_jTextField_search_bill_itemsKeyReleased

    private void jButton45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton45ActionPerformed

        try {
            conn_obj.get_con().setAutoCommit(false);
            String vendor = (String) jComboBox_ven_name_pay.getSelectedItem();
            if (vendor.equals("------")) {
                throw new Exception("لم يتم اختيار الاسم الصحيح");
            }
            String reciever = jTextField_ven_pay_receive.getText().trim();
            String sender = jTextField_ven_pay_helder.getText().trim();
            float value = float_parsing(jTextField_ven_pay_total);
            if (value <= 0) {
                throw new Exception("خطأ .. الدفعة أقل او يساوي صفر");
            }
            String note = jTextArea_ven_pay_note.getText().trim();
            //////////////////////////////////////

            java.util.Date d;
            d = jDateChooser_ven_pay_date.getDate();
            java.sql.Date sqlDate = new java.sql.Date(d.getTime());

            ///////////////////////////
            for (int i = 0; i < jTable_van_pay_my_checks.getRowCount(); i++) {
                //فحص سلامة تاريخ استخقاق السيك
                if (!isDateValid(jTable_van_pay_my_checks.getValueAt(i, 3).toString())) {
                    throw new Exception("خطا    " + jTable_van_pay_my_checks.getValueAt(i, 5).toString() + "تاريخ الشيك المدخل رقم   ");
                }
                //فحص سلامة قسمة الشيك ان لا تكون سالبة او صفر
                if (Float.parseFloat(jTable_van_pay_my_checks.getValueAt(i, 2).toString()) <= 0) {
                    throw new Exception("  قيمته خطا" + jTable_van_pay_my_checks.getValueAt(i, 0).toString() + "  الشيك رقم");
                }
            }
            r = conn_obj.conn_exec("select * from vendors where vendor_name='" + vendor + "'");
            r.next();
            int vendor_id = Integer.parseInt(r.getString(1));
            // فحص تجيير لنفس الشك
            String check_no;
            for(int i=0;i<jTable_ven_edorsed_checks.getRowCount();i++)
            {
                check_no=(jTable_ven_edorsed_checks.getValueAt(i, 1).toString().trim());
                System.out.println(check_no);
                for(int j=i+1;j<jTable_ven_edorsed_checks.getRowCount();j++)
                if(jTable_ven_edorsed_checks.getValueAt(j, 1).toString().trim().equals(check_no))
                {
                    throw new Exception("لاحظ ان هناك تجيير لنفس الشك رقم  : "+jTable_ven_edorsed_checks.getValueAt(j, 1).toString().trim());
                }
            }
            //انتهاء الفحص
            String stm = "";

            stm = "INSERT INTO vendor_payments (payment_value,vendor_id_fk,payment_rec,payment_maker,payment_date,payment_note) VALUES(" + value + "," + vendor_id + ",'" + reciever + "','" + sender + "','" + sqlDate + "','" + note + "');";
            for (int i = 0; i < jTable_van_pay_my_checks.getRowCount(); i++) {//ادخال الشيكات المملوكة
                stm += "INSERT INTO checks (check_value,check_due_date,check_status,check_bank,check_number,check_note,check_to_vendor_payment_id)VALUES (" + jTable_van_pay_my_checks.getValueAt(i, 2) + ",'" + jTable_van_pay_my_checks.getValueAt(i, 3) + "',(select status_id from check_status where status='مقطوع غير مسحوب'),(select bank_id from check_bank where bank_name='" + jTable_van_pay_my_checks.getValueAt(i, 4).toString() + "'),'" + jTable_van_pay_my_checks.getValueAt(i, 1) + "','" + jTable_van_pay_my_checks.getValueAt(i, 5) + "',(select last_value from vendor_payments_counter));";
            }
            for (int i = 0; i < jTable_ven_edorsed_checks.getRowCount(); i++) {//ادخال الشيكات المجيرة
                stm += "update customer_checks set vendor_payment_id=(select last_value from vendor_payments_counter) where id="+jTable_ven_edorsed_checks.getValueAt(i, 1).toString().trim()+";";
            }
            conn_obj.exec(stm);
            conn_obj.get_con().commit();
            /*
            if(jLabel52.getIcon()!= null)//صورة الدفعة النقدية
            {
                r = conn_obj.conn_exec("SELECT currval('customer_payments_counter')");
                r.next();
                save_payment_image_to_folder("D:\\Dropbox\\dist\\payments_images\\", r.getString("currval"),jLabel52);
                }*/
                Joptionpane_message("تم إدخال الدفعة الى حساب الزبون");
                float account = get_vendor_account_sum(vendor_id);
                Joptionpane_message("حساب  " + vendor + "  يساوي\n" + account + "");

                move_to_vendor_account();
                reset_panel_vendor_payment();
                write_to_file(get_date() + "     " + sqlDate.toString() + "   " + " تم إدخال دفعة الى حساب الزبون" + "    " + vendor + "    " + "بقيمة " + "    " + value + "  مجموع الحساب " + "    " + account);
            } catch (SQLException ex) {
                try {
                    conn_obj.get_con().rollback();

                    Joptionpane_message("لم يتم إدخال البيانات");
                } catch (SQLException ex1) {
                  
                }
         
            } catch (Exception ex) {
                try {
                    conn_obj.get_con().rollback();
                    Joptionpane_message(ex.getMessage());
                    Joptionpane_message("لم يتم إدخال البيانات");
                } catch (SQLException ex1) {
                    
                }
               
                Joptionpane_message(ex.getMessage());
            }

    }//GEN-LAST:event_jButton45ActionPerformed

    private void jTextField_ven_pay_totalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_ven_pay_totalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_ven_pay_totalActionPerformed

    private void jTextField_ven_pay_cashKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_ven_pay_cashKeyReleased
        ven_prepare_payment_sum();
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_ven_pay_cashKeyReleased

    private void jTable_van_pay_my_checksMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_van_pay_my_checksMousePressed
        if (jTable_van_pay_my_checks.getSelectedColumn() == 3) {

            jDateChooser4.setDateFormatString("yyyy-MM-dd");
            int response = JOptionPane.showConfirmDialog(null, jPanel22,
                "اختر تاريخ الاستحقاق", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (response == JOptionPane.OK_OPTION) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                jTable_van_pay_my_checks.setValueAt(formatter.format(jDateChooser4.getDate()), jTable_van_pay_my_checks.getSelectedRow(), 3);
            }
        }
    }//GEN-LAST:event_jTable_van_pay_my_checksMousePressed

    private void jButton46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton46ActionPerformed
           //DefaultTableModel tm = (DefaultTableModel) jTable_ven_edorsed_checks.getModel();
    DefaultTableModel tm = (DefaultTableModel)  jTable_van_pay_my_checks.getModel();
        int[] rows_array;
        rows_array = jTable_van_pay_my_checks.getSelectedRows();
        for (int i = 0; i < rows_array.length; i++) {
            int selectd_row = rows_array[i] - i;
            tm.removeRow(selectd_row);
        }
    }//GEN-LAST:event_jButton46ActionPerformed

    private void jButton47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton47ActionPerformed
        DefaultTableModel tm = (DefaultTableModel) jTable_van_pay_my_checks.getModel();
        tm.addRow(new Object[]{jTable_van_pay_my_checks.getRowCount() + 1, "", 0, get_date(), "", ""});        // TODO add your handling code here:
    }//GEN-LAST:event_jButton47ActionPerformed

    private void jTable_ven_edorsed_checksMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_ven_edorsed_checksMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable_ven_edorsed_checksMousePressed

    private void jButton48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton48ActionPerformed
       
            //DefaultTableModel tm = (DefaultTableModel) jTable_ven_edorsed_checks.getModel();
    DefaultTableModel tm = (DefaultTableModel)  jTable_ven_edorsed_checks.getModel();
        int[] rows_array;
        rows_array = jTable_ven_edorsed_checks.getSelectedRows();
        for (int i = 0; i < rows_array.length; i++) {
            int selectd_row = rows_array[i] - i;
            tm.removeRow(selectd_row);
        }
    }//GEN-LAST:event_jButton48ActionPerformed

    private void jButton49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton49ActionPerformed
        if (s == null) {
            s = new search_check(this);
            s.setState(NORMAL);
        }
        s.setLocationRelativeTo(this);
        s.setVisible(true);
        s.setState(NORMAL);
    }//GEN-LAST:event_jButton49ActionPerformed

    private void jButton_search_ven_name_payActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_search_ven_name_payActionPerformed
        which_component_request_searchCustomerName_vendor = 5;
        searchvendorName.pack();
        searchvendorName.setLocationRelativeTo(this);
        searchvendorName.setVisible(true);
    }//GEN-LAST:event_jButton_search_ven_name_payActionPerformed

    private void jComboBox_ven_name_payItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_ven_name_payItemStateChanged
        if (jPanel_ven_pay.isVisible()) {

            jLabel_ven_acc_sum.setText("الرصيد = " + String.valueOf(get_vendor_account_sum(jComboBox_ven_name_pay.getSelectedItem().toString())));

            try {
                jLabel_in_Tahseel.setText("في التحصيل = " + Float.toString(get_vendor_checks_sum_under_collection(jComboBox_ven_name_pay.getSelectedItem().toString())));
            } catch (SQLException ex) {
                
            }
        }
    }//GEN-LAST:event_jComboBox_ven_name_payItemStateChanged

    private void jButton_Tahseel_detailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_Tahseel_detailsActionPerformed
 show_customer_or_vend_checks sh = new show_customer_or_vend_checks(this,2);
        sh.jComboBox1.setSelectedItem(jComboBox_ven_name_pay.getSelectedItem());
        sh.setVisible(true);
        sh.setLocationRelativeTo(this); 
    }//GEN-LAST:event_jButton_Tahseel_detailsActionPerformed

    private void jPanel_ven_payFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel_ven_payFocusGained

    }//GEN-LAST:event_jPanel_ven_payFocusGained

    private void jTable_show_ven_account_detailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_show_ven_account_detailsMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1) {
            jPopupMenu_ven.show(evt.getComponent(), evt.getX(), evt.getY());
        } else if (evt.getButton() == MouseEvent.BUTTON3) {
            int r = jTable_show_ven_account_details.rowAtPoint(evt.getPoint());
            try {
                // delete_bill_or_payment(jTable3.getValueAt(r,6).toString().trim(),jTable3.getValueAt(r,9).toString().trim());
            } catch (Exception ex) {

                Joptionpane_message(ex.getMessage());
            }
        }
    }//GEN-LAST:event_jTable_show_ven_account_detailsMouseClicked

    private void jComboBox_ven_name_for_account_detailsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_ven_name_for_account_detailsItemStateChanged
create_ven_account_table();
show_last_row_scroll_jtable(jTable_show_ven_account_details);
    }//GEN-LAST:event_jComboBox_ven_name_for_account_detailsItemStateChanged

    private void jButton50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton50ActionPerformed
        show_all_vendor_movements();
    }//GEN-LAST:event_jButton50ActionPerformed

    private void jButton51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton51ActionPerformed
       try {
            vendor_account_zero(jComboBox_ven_name_for_account_details.getSelectedItem().toString());
            create_ven_account_table();

        } catch (SQLException ex) {
            Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton51ActionPerformed

    private void jButton52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton52ActionPerformed
        /*
        try {
            Print_by_POI_method_to_EXCEL_file(jTable3, jComboBox3.getSelectedItem().toString());
        } catch (IOException ex) {
            Logger.getLogger(vendors.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        int number = 0;
        JTextField field1 = new JTextField("");
        JPanel panel = new JPanel(new GridLayout(0, 1));
        Checkbox c = new Checkbox("طباعة الكل ؟");
        Checkbox details = new Checkbox("طباعة تفصيلية؟");
        JLabel x = new JLabel("أدخل عدد الحركات للطباعة");
        x.setFont(FayezFont);
        panel.add(x);
        panel.add(field1);
        panel.add(c);
        panel.add(details);
        panel.setFont(FayezFont);
        int result = JOptionPane.showConfirmDialog(null, panel, "?",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            if (c.getState() == true) {
                number = jTable_show_ven_account_details.getRowCount();
            } else {
                String Num = field1.getText();
                number = Integer.parseInt(Num);
                if (number > jTable_show_ven_account_details.getRowCount()) {
                    Joptionpane_message("العدد المختار أكبر من عدد صفوف الجدول!");
                }
            }
            try {
                if (c.getState() == true && details.getState() == false) {
                    print_vendor_jtable(number, null);
                } else if (c.getState() == true && details.getState() == true) {
                    print_vendor_jtable_with_details(number, null);
                } else if (c.getState() == false && details.getState() == true) {
                    print_vendor_jtable_with_details(number, null);
                } else if (c.getState() == false && details.getState() == false) {
                    print_vendor_jtable(number, null);
                } else {
                    print_jtable(number,0, null);
                }
            } catch (SQLException ex) {
                
            }
        } else {
            System.out.println("Cancelled");
        }
    }//GEN-LAST:event_jButton52ActionPerformed

    private void jButton53ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton53ActionPerformed
        show_vendors_accounts("");
    }//GEN-LAST:event_jButton53ActionPerformed

    private void jButton54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton54ActionPerformed
        which_component_request_searchCustomerName_vendor = 1;
        searchvendorName.pack();
        searchvendorName.setLocationRelativeTo(this);
        searchvendorName.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton54ActionPerformed

    private void jButton55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton55ActionPerformed
        int number = 0;
        JTextField field1 = new JTextField("");
        JPanel panel = new JPanel(new GridLayout(0, 1));
        Checkbox c = new Checkbox("حفظ كل الحركات؟");
        Checkbox details = new Checkbox("حفظ تفصيلي؟");
        JLabel x = new JLabel("أدخل عدد الحركات للحفظ");
        x.setFont(FayezFont);
        panel.add(x);
        panel.add(field1);
        panel.add(c);
        panel.add(details);
        panel.setFont(FayezFont);
        int result = JOptionPane.showConfirmDialog(null, panel, "?",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            if (c.getState() == true) {
                number = jTable_show_ven_account_details.getRowCount();
            } else {
                String Num = field1.getText();
                number = Integer.parseInt(Num);
                if (number > jTable_show_ven_account_details.getRowCount()) {
                    Joptionpane_message("العدد المختار أكبر من عدد صفوف الجدول!");
                }
            }
            try {
                if (c.getState() == true && details.getState() == false) {
                    print_jtable(number,0, "save_to_file");
                } else if (c.getState() == true && details.getState() == true) {
                    print_jtable_with_details(number,0, "save_to_file");
                } else if (c.getState() == false && details.getState() == true) {
                    print_jtable_with_details(number,0, "save_to_file");
                } else if (c.getState() == false && details.getState() == false) {
                    print_jtable(number,0, "save_to_file");
                } else {
                    print_jtable(number,0, "save_to_file");
                }
            } catch (SQLException ex) {
            }
        } else {
            Joptionpane_message("Canceled");
        }
    }//GEN-LAST:event_jButton55ActionPerformed

    private void jButton56ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton56ActionPerformed
        send_email send_email_obj = new send_email();
        send_email_obj.setLocationRelativeTo(this);
        send_email_obj.setVisible(true);
    }//GEN-LAST:event_jButton56ActionPerformed

    private void jPanel_ven_acount_detailsComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel_ven_acount_detailsComponentShown
       // System.out.println("حسابات الزبون");
        create_customer_account_table();
        show_last_row_scroll_jtable(jTable_show_ven_account_details);        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel_ven_acount_detailsComponentShown

    private void jTable_shoe_ven_namesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_shoe_ven_namesMouseClicked
        int x = (int) jTable_shoe_ven_names.getValueAt(jTable_shoe_ven_names.getSelectedRow(), 0);
        r = conn_obj.conn_exec("select * from vendors where vendor_id=" + x + "");
        try {
            r.next();

            jLabel92.setText(r.getString(1));
            jTextField23.setText(r.getString(2));
            jTextField21.setText(r.getString(3));
            jTextField22.setText(r.getString(4));
        } catch (SQLException ex) {
            
        }
        if (jPanel_del_ven_name.isVisible()) {

            jLabel25.setText((String) jTable_shoe_ven_names.getValueAt(jTable_shoe_ven_names.getSelectedRow(), 1));

        }
    }//GEN-LAST:event_jTable_shoe_ven_namesMouseClicked

    private void jButton_add_new_venActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_add_new_venActionPerformed

        String name = jTextField_ven_name_to_add.getText().trim();
        String address = jTextField_ven_addres_to_add.getText().trim();
        String phone = jTextField_ven_phone_to_add.getText().trim();
        String stm = "insert into vendors (vendor_name,vendor_tell,vendor_location)values('" + name + "','" + phone + "','" + address + "')";
        try {
            //////** لمنع ادخال اسم فارغ الى اسماء  الموردين
            //////** ومنع ادخال تصنيف فارغ ------
            if (!name.equals("")) {
                {
                    conn_obj.exec(stm);
                    Joptionpane_message("تمت إضافة الاسم");

                    jTextField_ven_name_to_add.setText("");
                    jTextField_ven_addres_to_add.setText("");
                    jTextField_ven_phone_to_add.setText("");
                    jTextField_ven_name_to_add.requestFocus();
                    check_to_add_phone_number(phone,name);
                }
            } else {
                throw new SQLException("الاسم او التصنيف خاطئ...");
            }

            /////////////
            ///** لاعادة تحديث جدول اسماء الموردين بعد اضافة اسم جديد
            r = conn_obj.conn_exec("select vendor_id as ID,vendor_name As Name,vendor_tell as Tell, vendor_location as location from vendors");
            jTable_shoe_ven_names.setModel(DbUtils.resultSetToTableModel(r));
            //**end
            r.afterLast();
            r.previous();

            jCombo_vendor_name.addItem(r.getString("Name"));
            jComboBox_ven_name_pay.addItem(r.getString("Name"));
            jComboBox_ven_name_for_account_details.addItem(r.getString("Name"));
            jComboBox_van_name_return_bill.addItem(r.getString("Name"));
            ///////////////////////////////////////////////////to write on file //////////////////////
            write_to_file(get_date() + "     " + "  إدخال إسم  " + name + " ");
            //////////////////////////////////////////////////////////////end write on file////////
        } catch (SQLException ex) {
            
            Joptionpane_message(ex.getMessage());

        } catch (Exception ex) {
            
            Joptionpane_message(ex.getMessage());
        }
    }//GEN-LAST:event_jButton_add_new_venActionPerformed

    private void jButton57ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton57ActionPerformed
        update_ven_name();
        //update_jcomboBox_1_3_4();
    }//GEN-LAST:event_jButton57ActionPerformed

    private void jTextField21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField21ActionPerformed

    private void jTextField22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField22ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField22ActionPerformed

    private void jTextField23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField23ActionPerformed

    private void jButton58ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton58ActionPerformed
        try {

            delete_ven_name();        // TODO add your handling code here:
        } catch (SQLException ex) {
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_jButton58ActionPerformed

    private void jButton59ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton59ActionPerformed
        try {
            which_component_request_searchCustomerName_vendor = 2;
            r = conn_obj.conn_exec("select vendor_id as ID,vendor_name As الاسم,vendor_tell as الهاتف,vendor_location as العنوان from vendors order by vendor_name");
            jTable_shoe_ven_names.setModel(DbUtils.resultSetToTableModel(r));
            System.out.println("تم اضافة الاسماء الى جدول الاسماء");
        } catch (Exception e) {

        }

        searchvendorName.pack();
        searchvendorName.setLocationRelativeTo(this);
        searchvendorName.setVisible(true);
    }//GEN-LAST:event_jButton59ActionPerformed

    private void jPanel_ven_namesComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel_ven_namesComponentShown

    }//GEN-LAST:event_jPanel_ven_namesComponentShown

    private void jTable_return_ven_bill_itemsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_return_ven_bill_itemsMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable_return_ven_bill_itemsMouseReleased

    private void jButton_del_return_ven_bill_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_del_return_ven_bill_itemActionPerformed
        DefaultTableModel tm = (DefaultTableModel) jTable_return_ven_bill_items.getModel();
        int[] rows_array;
        rows_array = jTable_return_ven_bill_items.getSelectedRows();
        for (int i = 0; i < rows_array.length; i++) {
            int selectd_row = rows_array[i] - i;
            tm.removeRow(selectd_row);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton_del_return_ven_bill_itemActionPerformed

    private void jButton_return_ven_bill_insertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_return_ven_bill_insertActionPerformed
        try {
            System.out.println(conn_obj.get_con().isClosed());
        } catch (SQLException ex) {
        }
        try {
            conn_obj.get_con().setAutoCommit(false);
            jTable_return_ven_bill_items.editingCanceled(null);//هذا اذا نسي المدخل خانات مفتوحة اي مفتوحة للتعديل لا يتعامل معها على انها فارغة فيخرج منها قبل ادخال البيانات
            String customer = (String) jComboBox_van_name_return_bill.getSelectedItem();//اخذ اسم المورد من القائمة
            if (customer.equals("------")) {
                throw new Exception("لم يتم اختيار الاسم الصحيح");
            }
            float value = float_parsing(jTextField_return_ven_bill_value);
            if (Float.valueOf(jTextField_return_ven_bill_value.getText().toString().trim()) <= 0 || jTextField_return_ven_bill_value.getText().matches("")) {
                throw new Exception("لا يمكن ان تساوي الفاتورة القيمة صفر او فارغ");
            }

            if (value != prepare_bill_ven_jtable12_returns()) {
                if (check_yes_or_no_question("محموع الفاتورة الفعلي لا يساوي المجموع الموجود؟") == false) {
                    throw new Exception("تم الالغاء!!");
                }
            }

            String location = (String) jComboBox_return_ven_store.getSelectedItem();
            String note = jTextArea_return_ven_bill_note.getText().trim();
            //////////////////////////////////////
            java.util.Date d;
            d = jDateChooser_return_ven_bill_date.getDate();
            java.sql.Date sqlDate = new java.sql.Date(d.getTime());
            ///////////////////////////
            String bill_num = jTextField_return_ven_bil_numberl.getText().trim();

            ///
            r = conn_obj.conn_exec("select vendor_id from vendors where vendor_name='" + customer + "'");
            r.next();
            int customer_id = Integer.parseInt(r.getString(1));

            String insert_table_content = "";
            insert_table_content = "INSERT INTO return_vendor_bills (return_bill_value,return_bill_vendor_id,return_bill_location_id,return_bill_note,return_bill_date,return_bill_num) VALUES"
            + "(" + value + "," + customer_id + ",(select location_id from location where location_name='" + location + "'),'" + note + "','" + sqlDate + "','" + bill_num + "');";
            /////////////////إدخال قيم الجدول ////////////////
            for (int i = 0; i < jTable_return_ven_bill_items.getRowCount(); i++) {
                if (jTable_return_ven_bill_items.getValueAt(i, 0) != null) {
                    insert_table_content += "insert into return_vendor_bills_items(return_bill_id,return_item_id,return_item_quantity,return_item_unit,return_item_price,return_item_note)values"
                    + "((select last_value from return_vendor_bills_return_bill_id_seq),(select item_id from items.main_items where item_name = '" + jTable_return_ven_bill_items.getValueAt(i, 0) + "' ),"
                    + jTable_return_ven_bill_items.getValueAt(i, 2) + ",(select unit_id from items.item_units where unit_name = '" + jTable_return_ven_bill_items.getValueAt(i, 1) + "' )," + jTable_return_ven_bill_items.getValueAt(i, 3) + ",'" + jTable_return_ven_bill_items.getValueAt(i, 5) + "');";
                }
            }
            ////
            conn_obj.get_st().execute(insert_table_content);

            ////إدخال الكميات وانقاصها من المخزون للمخزن المحدد
            String stm_exec = "";
            stm_exec = "select location_id from location where location_name='" + jComboBox_return_ven_store.getSelectedItem().toString() + "'";

            r = conn_obj.conn_exec(stm_exec);
           // System.out.println(stm_exec);
            r.next();
            String location_id = r.getString("location_id");

            for (int i = 0; i < jTable_return_ven_bill_items.getRowCount(); i++) {
                stm_exec = ""
                + "select quantity,main_item_id from  (\n"
                + "select \n"
                + "\n"
                + "items.main_items.item_name,\n"
                + "items.main_items.item_id as main_item_id,\n"
                + "\n"
                + "items.item_units.unit_id,\n"
                + "items.item_units.unit_name,\n"
                + "\n"
                + "items.item_relations.item_id,\n"
                + "items.item_relations.item_unit,\n"
                + "items.item_relations.item_relation*" + jTable_return_ven_bill_items.getValueAt(i, 2) + " as quantity\n"
                + "\n"
                + "from items.main_items,items.item_units,items.item_relations\n"
                + "\n"
                + "where \n"
                + "items.main_items.item_name='" + jTable_return_ven_bill_items.getValueAt(i, 0) + "'  AND\n"
                + "items.item_units.unit_name='" + jTable_return_ven_bill_items.getValueAt(i, 1) + "'  AND\n"
                + "items.main_items.item_id=items.item_relations.it"
                + "em_id AND\n"
                + "items.item_units.unit_id=items.item_relations.item_unit )as anyThing";
                r = conn_obj.conn_exec(stm_exec);
                r.next();
                System.out.println(stm_exec);
                double quantity = r.getDouble("quantity") * -1;
                System.out.println("Quantity=="+quantity);
                int item_id = r.getInt("main_item_id");
                String store_to_update = "store_id_" + location_id;//store_id_1
                stm_exec = "update items.inventory set " + store_to_update + " = " + store_to_update + " + " + quantity + " where item_id=" + item_id + "";
                conn_obj.exec(stm_exec);
            }

            //////////////////////////////////////////////////
            check_two_input_customer(customer_id, sqlDate);

            conn_obj.get_con().commit();
            Joptionpane_message("لقد تم إدخال البيانات");
            float account = get_vendor_account_sum(customer_id);
            Joptionpane_message("حساب  " + customer + "  يساوي\n" + account + "");

            move_to_vendor_account();
            ///////////////////////////////////////////////////to write on file //////////////////////
            write_to_file(get_date() + "     " + sqlDate.toString() + " " + "  إدخال فاتورةمرتجعات  لحساب الزبون  " + customer + " " + "بقيمة " + " "
                + Float.toString(value) + " " + "الرصيد = " + " " + Float.toString(account));

            reset_return_vendor_bill();

        } catch (Exception ex) {
            try {
                conn_obj.get_con().rollback();
                Joptionpane_message(ex.getMessage().toString());
                
            } catch (Exception e) {
                Joptionpane_message(e.getMessage().toString());
            }
            //Logger.getLogger(vendors.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton_return_ven_bill_insertActionPerformed

    private void jTextField_return_ven_bill_valueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_return_ven_bill_valueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_return_ven_bill_valueActionPerformed

    private void jButton60ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton60ActionPerformed
        which_component_request_searchCustomerName_vendor = 8;
        searchvendorName.pack();
        searchvendorName.setLocationRelativeTo(this);
        searchvendorName.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton60ActionPerformed

    private void jTextField_return_ven_bil_numberlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_return_ven_bil_numberlActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_return_ven_bil_numberlActionPerformed

    private void jButton61ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton61ActionPerformed
        which_component_request_searchItemName_vendor = 3;
        search_ven_ItemName.pack();
        search_ven_ItemName.setLocationRelativeTo(this);
        search_ven_ItemName.setVisible(true);
    }//GEN-LAST:event_jButton61ActionPerformed

    private void jTextField_return_ven_barcodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_return_ven_barcodeFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_return_ven_barcodeFocusLost

    private void jTextField_return_ven_barcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_return_ven_barcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_return_ven_barcodeActionPerformed

    private void jButton_return_ven_bill_last_priceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_return_ven_bill_last_priceActionPerformed
        for (int i = 0; i < jTable_return_ven_bill_items.getRowCount(); i++) {
            if (jTable_return_ven_bill_items.getValueAt(i, 1) != null && jTable_return_ven_bill_items.getValueAt(i, 0) != null) {
                try {
                    String item_name, item_unit, customer = "";
                    item_name = jTable_return_ven_bill_items.getValueAt(i, 0).toString();
                    item_unit = jTable_return_ven_bill_items.getValueAt(i, 1).toString();
                    customer = jComboBox_van_name_return_bill.getSelectedItem().toString();
                    r = conn_obj.conn_exec("select \n"
                        + "unit_name,unit_id,\n"
                        + "items.main_items.item_name,items.main_items.item_id,\n"
                        + "vendor_name,vendor_id,\n"
                        + "bill_vendor_id,public.vendor_bills.bill_id,\n"
                        + "public.vendor_bills_items.bill_id,public.vendor_bills_items.item_id,public.vendor_bills_items.item_unit,public.vendor_bills_items.item_price\n"
                        + "\n"
                        + "from\n"
                        + "items.item_units,\n"
                        + "items.main_items,\n"
                        + "public.vendors,\n"
                        + "public.vendor_bills,\n"
                        + "public.vendor_bills_items\n"
                        + "\n"
                        + "where \n"
                        + "items.item_units.unit_name= '" + item_unit + "'  and\n"
                        + "items.main_items.item_name= '" + item_name + "' and\n"
                        + "public.vendors.vendor_name= '" + customer + "' and\n"
                        + "public.vendor_bills.bill_vendor_id=public.vendors.vendor_id and\n"
                        + "public.vendor_bills.bill_id=public.vendor_bills_items.bill_id and\n"
                        + "public.vendor_bills_items.item_id=items.main_items.item_id and\n"
                        + "public.vendor_bills_items.item_unit=items.item_units.unit_id ;\n"
                        + "\n"
                        + "");
                    if (!r.next()) {
                        r = conn_obj.conn_exec("select item_price_hole from items.item_relations where item_id=(select item_id from items.main_items where item_name = '" + item_name + "') and item_unit=(select unit_id from items.item_units where unit_name='" + item_unit + "')");
                        r.next();
                        jTable_return_ven_bill_items.setValueAt(r.getString(1), i, 3);
                    } else {
                        r.last();
                        jTable_return_ven_bill_items.setValueAt(r.getString("item_price"), i, 3);
                    }
                } catch (SQLException ex) {
                    
                }
            }
        }
    }//GEN-LAST:event_jButton_return_ven_bill_last_priceActionPerformed

    private void jTabbedPane_vendor_actionStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane_vendor_actionStateChanged

    }//GEN-LAST:event_jTabbedPane_vendor_actionStateChanged

    private void jTextField24KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField24KeyPressed
        int key = evt.getKeyCode();
        if (evt.getSource() == jTextField24) {
            if (key == KeyEvent.VK_ENTER) {
                r = conn_obj.conn_exec("select vendor_name from vendors where vendor_name LIKE '%" + jTextField24.getText().trim() + "%'");
                jTable_ven_names.setModel(DbUtils.resultSetToTableModel(r));
                renderer_jTable_obj.Renderer(jTable_ven_names);
            }
        }
    }//GEN-LAST:event_jTextField24KeyPressed

    private void jTable_ven_namesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_ven_namesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable_ven_namesMouseClicked

    private void jTable_ven_namesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_ven_namesMousePressed
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            evt.consume();
            if (which_component_request_searchCustomerName_vendor == 3) {
                jCombo_vendor_name.setSelectedItem(jTable_ven_names.getValueAt(jTable_ven_names.getSelectedRow(), jTable_ven_names.getSelectedColumn()));
                jTextField24.requestFocus();
                jTextField24.selectAll();
            } else if (which_component_request_searchCustomerName_vendor == 1) {
                jComboBox_ven_name_for_account_details.setSelectedItem(jTable_ven_names.getValueAt(jTable_ven_names.getSelectedRow(), jTable_ven_names.getSelectedColumn()));
                jTextField24.requestFocus();
                jTextField24.selectAll();
            } else if (which_component_request_searchCustomerName_vendor == 5) {
                jComboBox_ven_name_pay.setSelectedItem(jTable_ven_names.getValueAt(jTable_ven_names.getSelectedRow(), jTable_ven_names.getSelectedColumn()));
                jTextField24.requestFocus();
                jTextField24.selectAll();
            } else if (which_component_request_searchCustomerName_vendor == 6) {
                jTextField33.setText((String) jTable_ven_names.getValueAt(jTable_ven_names.getSelectedRow(), jTable_ven_names.getSelectedColumn()));
                jTextField24.requestFocus();
                jTextField24.selectAll();
            } else if (which_component_request_searchCustomerName_vendor == 2) {
                //jComboBox4.setSelectedItem(jTable5.getValueAt(jTable5.getSelectedRow(), jTable5.getSelectedColumn()));
                jTextField24.requestFocus();
                jTextField24.selectAll();
                try {
                    r = conn_obj.conn_exec("select vendor_id as ID,vendor_name As Name,vendor_tell as Tell,vendor_location as location from vendors where vendor_name like '%" + jTable_ven_names.getValueAt(jTable_ven_names.getSelectedRow(), jTable_ven_names.getSelectedColumn()) + "%' order by vendor_name");
                    jTable_shoe_ven_names.setModel(DbUtils.resultSetToTableModel(r));
                } catch (Exception e) {

                }
            } else if (which_component_request_searchCustomerName_vendor == 7) {
                jTextField29.setText((String) jTable_ven_names.getValueAt(jTable_ven_names.getSelectedRow(), jTable_ven_names.getSelectedColumn()));
                jTextField24.requestFocus();
                jTextField24.selectAll();
            } else if (which_component_request_searchCustomerName_vendor == 8) {
                jComboBox_van_name_return_bill.setSelectedItem(jTable_ven_names.getValueAt(jTable_ven_names.getSelectedRow(), jTable_ven_names.getSelectedColumn()));
                jTextField24.requestFocus();
                jTextField24.selectAll();
            }
            else if (which_component_request_searchCustomerName_vendor == 9) {
                jComboBox10.setSelectedItem(jTable_ven_names.getValueAt(jTable_ven_names.getSelectedRow(), jTable_ven_names.getSelectedColumn()));
                jTextField24.requestFocus();
                jTextField24.selectAll();
            }
            searchvendorName.dispose();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jTable_ven_namesMousePressed

    private void jTextField25KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField25KeyPressed
        int key = evt.getKeyCode();
        if (evt.getSource() == jTextField25) {
            if (key == KeyEvent.VK_ENTER) {

                r = conn_obj.conn_exec("select item_name as الاسم,unit_name as الوحدة,to_char(value, 'FM9999999999.00') as السعر_علينا,relation as علاقتها_بالرئيسية,1 as الكمية,'' as ملاحظات\n" +
                    "                    from\n" +
                    "                    ( \n" +
                    "                    select \n" +
                    "                      items.item_units.unit_name,items.item_units.unit_id,\n" +
                         "            items.items_ranking.rank_item_id,items.items_ranking.rank,\n"+
                    "                      items.main_items.item_name,items.main_items.item_id,items.main_items.item_value*items.item_relations.item_relation as value,\n" +
                    "                      items.item_relations.item_id,items.item_relations.item_unit,items.item_relations.item_price_hole,items.item_relations.item_relation as relation\n" +
                    "                               \n" +
                    "                                from items.item_units,items.main_items,items.item_relations,items.items_ranking\n" +
                    "                                                               where \n" +
                    "                                items.main_items.item_name LIKE '%" + jTextField25.getText().trim() + "%'\n" +
                    "                                AND \n" +
                    "                                items.item_relations.item_id= items.main_items.item_id\n" +
                    "                                and\n" 
                        + "            items.main_items.item_id = items.items_ranking.rank_item_id \n"+
                        "                                AND \n" +
                    "                                items.item_relations.item_unit=items.item_units.unit_id\n" +
                    "                                \n" +
                    "                                order by rank desc ,main_items.item_name )  as alias");

                //r=conn_obj.conn_exec("select item_name from items.main_items where item_name LIKE '%"+jTextField4.getText().trim()+"%'");
                jTable10.setModel(DbUtils.resultSetToTableModel(r));
                 
                jTable10.getColumnModel().getColumn(0).setMinWidth(250);
                jTable10.getColumnModel().getColumn(1).setMinWidth(70);
                jTable10.getColumnModel().getColumn(2).setMinWidth(70);
                jTable10.getColumnModel().getColumn(3).setMinWidth(70);
                jTable10.getColumnModel().getColumn(4).setMinWidth(70);
                jTable10.getColumnModel().getColumn(5).setMinWidth(200);
                renderer_jTable_obj.Renderer(jTable10);
                jTextField25.selectAll();

            }
        }
    }//GEN-LAST:event_jTextField25KeyPressed

    private void jTable10MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable10MousePressed

        if (evt.getClickCount() == 2 && !evt.isConsumed() && jTable10.getSelectedColumn() == 0) {
            evt.consume();
            if (which_component_request_searchItemName_vendor == 1) {

                add_row_jtable4(jTable_bill_items,
                    jTable10.getValueAt(jTable10.getSelectedRow(), 0), jTable10.getValueAt(jTable10.getSelectedRow(), 1), jTable10.getValueAt(jTable10.getSelectedRow(), 4), 0, jTable10.getValueAt(jTable10.getSelectedRow(), 2), 0, null, jTable10.getValueAt(jTable10.getSelectedRow(), 5));

                show_last_row_scroll_jtable(jTable_bill_items);//هنا الذهاب لاخر صف مكتوب بجدول الفاتورة لامكانية رؤيته مباشرة
            } else if (which_component_request_searchItemName_vendor == 2) {
                search_in_vendor_bills_about_item();
            } else if (which_component_request_searchItemName_vendor == 3) {
                DefaultTableModel tm = (DefaultTableModel) jTable_return_ven_bill_items.getModel();
                tm.addRow(new Object[]{
                    jTable10.getValueAt(jTable10.getSelectedRow(), 0),
                    jTable10.getValueAt(jTable10.getSelectedRow(), 1),
                    jTable10.getValueAt(jTable10.getSelectedRow(), 4),
                    jTable10.getValueAt(jTable10.getSelectedRow(), 2),
                    null,
                    jTable10.getValueAt(jTable10.getSelectedRow(), 5)});

            show_last_row_scroll_jtable(jTable_return_ven_bill_items);
        } else if (which_component_request_searchItemName_vendor == 5) {
               // System.out.println("thisss");
            DefaultTableModel tm = (DefaultTableModel) jTable16.getModel();
            tm.addRow(new Object[]{
                jTable10.getValueAt(jTable10.getSelectedRow(), 0),
                jTable10.getValueAt(jTable10.getSelectedRow(), 1),
                jTable10.getValueAt(jTable10.getSelectedRow(), 4),
                jTable10.getValueAt(jTable10.getSelectedRow(), 2),
                null,
                jTable10.getValueAt(jTable10.getSelectedRow(), 5)});

        show_last_row_scroll_jtable(jTable16);
        }

        jTextField25.requestFocus();
        jTextField25.selectAll();
        }
    }//GEN-LAST:event_jTable10MousePressed

    private void jTable10KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable10KeyPressed
 // TODO add your handling code here:
    }//GEN-LAST:event_jTable10KeyPressed

    private void jPanel_add_ven_billComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel_add_ven_billComponentShown
     //   System.out.println(jPanel_add_ven_bill.getComponentCount());        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel_add_ven_billComponentShown

    private void show_items_venActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_show_items_venActionPerformed
        show_bill_items_ven();
    }//GEN-LAST:event_show_items_venActionPerformed

    private void show_image_venActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_show_image_venActionPerformed
        open_the_bill_picture();
    }//GEN-LAST:event_show_image_venActionPerformed

    private void modify_record_venActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modify_record_venActionPerformed
{                                                  

        int point = jTable_show_ven_account_details.getSelectedRow();
        if (jComboBox_ven_name_for_account_details.getSelectedItem().toString().equals("------"))//لا يمكن التعديل والاسم غير صحيح 
        {
            Joptionpane_message("إختر الاسم من القائمة !!!");
        } else {
            try {
                if (check_if_accounted_ven(Integer.parseInt(jTable_show_ven_account_details.getValueAt(point, 6).toString().trim()), jTable_show_ven_account_details.getValueAt(point, 3).toString().trim())) {
                    Joptionpane_message("لا يمكن تعديل قيد مصفّر !!!");
                } else {

                    if (jTable_show_ven_account_details.getValueAt(point, 3).toString().trim().equals("فاتورة")) {
                        jframe_to_modify_bill_vendor = new modify_vendor_bill(this);
             //reset_jpanel_1();//لحتى لما يفرغ البيانات في جدول الفاتورة يفرغها والجدول وكل المربعات فارغة
                        //jTabbedPane1.setSelectedIndex(0);
                        String bill_id = jTable_show_ven_account_details.getValueAt(point, 6).toString().trim();
                        r = conn_obj.conn_exec("select * from  vendor_bills where bill_id=" + bill_id + "");
                        r.next();
                        jframe_to_modify_bill_vendor.jComboBox1.setSelectedItem(jComboBox_ven_name_for_account_details.getSelectedItem());
                        jframe_to_modify_bill_vendor.jTextField1.setText(r.getString("bill_value"));
                        jframe_to_modify_bill_vendor.jDateChooser1.setDate(r.getDate("bill_date"));
                        jframe_to_modify_bill_vendor.jComboBox2.setSelectedIndex(r.getInt("bill_location_id") - 1);
                        jframe_to_modify_bill_vendor.jTextArea1.setText(r.getString("bill_note"));
                        jframe_to_modify_bill_vendor.jTextField3.setText(r.getString("discount_ratio"));
                        jframe_to_modify_bill_vendor.jTextField16.setText(r.getString("discount_amount"));
                        jframe_to_modify_bill_vendor.jTextField15.setText(r.getString("bill_num"));
                        jframe_to_modify_bill_vendor.jTextField19.setText(bill_id);

                        String location_id = r.getString("bill_location_id");
                        /////////////////////////////////////////////////// show bill items in jtable////////////
                        r = conn_obj.conn_exec("SELECT \n"
                                + "items.main_items.item_name,\n"
                                + "items.main_items.item_id,\n"
                                + "\n"
                                + "items.item_units.unit_name,\n"
                                + "items.item_units.unit_id,\n"
                                + "\n"
                                + "vendor_bills_items.id,vendor_bills_items.item_id,vendor_bills_items.item_bonus,\n"
                                + "vendor_bills_items.item_note,vendor_bills_items.item_unit,\n"
                                + "vendor_bills_items.item_price,vendor_bills_items.item_quantity,\n"
                                + "vendor_bills_items.bill_id,vendor_bills_items.discount_ratio\n"
                                + "FROM items.main_items,items.item_units,vendor_bills_items\n"
                                + "WHERE\n"
                                + "items.main_items.item_id = vendor_bills_items.item_id\n"
                                + "AND\n"
                                + "items.item_units.unit_id = vendor_bills_items.item_unit\n"
                                + "AND\n"
                                + "vendor_bills_items.bill_id=" + bill_id + "  order by vendor_bills_items.id");

                        /*
                         عند تعديل القيد يجب معرفة اذا كان عدد سطور
                         الطلبية المغدلة اكبر من عدد سطور الجدول فتضيغ بغض البيانات
                         */
                        DefaultTableModel tm = (DefaultTableModel) jframe_to_modify_bill_vendor.jTable4.getModel();
                        r.last();
                        int r_row_count = r.getRow();
                        int jTable_5_row_count = jframe_to_modify_bill_vendor.jTable4.getRowCount();
                        r.beforeFirst();
                        while (r_row_count > jTable_5_row_count) {
                            r_row_count--;
                            tm.addRow(new Object[]{null, null, null, 0, null, 0, null, ""});

                        }

                        while (r.next()) {
                            int result_set_row = r.getRow() - 1;
                
                            jframe_to_modify_bill_vendor.jTable4.setValueAt(r.getString("item_name"), result_set_row, 0);//item_name
                            jframe_to_modify_bill_vendor.jTable4.setValueAt(r.getString("unit_name"), result_set_row, 1);//item_unit
                            jframe_to_modify_bill_vendor.jTable4.setValueAt(r.getString("item_bonus"), result_set_row, 3);//item_Bonus
                            jframe_to_modify_bill_vendor.jTable4.setValueAt(r.getString("item_quantity"), result_set_row, 2);//item_quantity
                            //jframe_to_modify_bill.jTable4.setValueAt(String.format("%.2f", r.getFloat("item_price")),result_set_row,4);//item_price
                            jframe_to_modify_bill_vendor.jTable4.setValueAt(r.getFloat("item_price"), result_set_row, 4);//item_price
                            jframe_to_modify_bill_vendor.jTable4.setValueAt(r.getString("discount_ratio"), result_set_row, 5);//item_discountRatio
                            jframe_to_modify_bill_vendor.jTable4.setValueAt(r.getString("item_note"), result_set_row, 7);//item_discountRatio
                        }
                        ///////////////////////////////////////end show bill_items in jtable
                        jframe_to_modify_bill_vendor.jTable4_listen_to_any_change();
                        jframe_to_modify_bill_vendor.prepare_stm_that_return_quantity_to_store();//هذا الفنكشن يعيد الكميات وينقصها من المخزون
                        jframe_to_modify_bill_vendor.setVisible(true);
            ///////////////////////////////////////end show bill_items in jtable
                        // Joptionpane_message("ملاحظة هامة .. يجب أن تعلم أن الفاتورة السابقة المعدلة تم حذفها");
                        //conn_obj.exec("DELETE FROM customer_bills where bill_id="+bill_id+"");
                    } else if (jTable_show_ven_account_details.getValueAt(point, 3).toString().trim().equals("دفعة")) {
                        String payment_id = jTable_show_ven_account_details.getValueAt(point, 6).toString().trim();
                        jframe_to_modify_vendor_payment = new modify_vendor_payment(this, payment_id);
                        r = conn_obj.conn_exec("select * from  vendor_payments where payment_id=" + payment_id + "");
                        r.next();

                        float hole_payment_value = r.getFloat("payment_value");

                        jframe_to_modify_vendor_payment.jComboBox4.setSelectedItem(jComboBox_ven_name_for_account_details.getSelectedItem());
                        jframe_to_modify_vendor_payment.jDateChooser2.setDate(r.getDate("payment_date"));
                        jframe_to_modify_vendor_payment.jTextField14.setText(r.getString("payment_rec"));
                        jframe_to_modify_vendor_payment.jTextField19.setText(r.getString("payment_maker"));
                        jframe_to_modify_vendor_payment.jTextArea2.setText(r.getString("payment_note"));
                        //
                        r = conn_obj.conn_exec("select sum(check_value)as s from  customer_checks where vendor_payment_id=" + payment_id + "");
                        r.next();
                        float sum_of_customer_checks = r.getFloat("s");
                        r = conn_obj.conn_exec("select sum(check_value)as v from  checks where check_to_vendor_payment_id=" + payment_id + "");
                        r.next();
                        float sum_of_checks = r.getFloat("v");
                        float net_of_cash_payments = hole_payment_value - sum_of_checks-sum_of_customer_checks;
            //

                        r = conn_obj.conn_exec("select check_owner,check_endorser,check_no,check_due_date,check_note,check_value,check_bank,id from  customer_checks where vendor_payment_id=" + payment_id + "");
                        while (r.next()) {
                            DefaultTableModel tm = (DefaultTableModel) jframe_to_modify_vendor_payment.jTable14.getModel();
                            tm.addRow(new Object[]{jframe_to_modify_vendor_payment.jTable14.getRowCount() + 1,r.getString("id"), r.getString("check_no"), r.getFloat("check_value"), r.getString("check_owner"), r.getString("check_endorser"), r.getString("check_due_date"), r.getString("check_bank"), r.getString("check_note")});
                        }
                        
                        r = conn_obj.conn_exec("select check_number,check_value,check_due_date,(select bank_name from check_bank where bank_id=check_bank),check_note from checks where check_to_vendor_payment_id=" + payment_id + "");
                        while (r.next()) {
                            DefaultTableModel tm = (DefaultTableModel) jframe_to_modify_vendor_payment.jTable13.getModel();
                            tm.addRow(new Object[]{jframe_to_modify_vendor_payment.jTable13.getRowCount() + 1,r.getString("check_number"), r.getFloat("check_value"), r.getString("check_due_date"), r.getString("bank_name"), r.getString("check_note")});
                        }
                        
                        jframe_to_modify_vendor_payment.jTextField12.setText(Float.toString(net_of_cash_payments));
                        
                        jframe_to_modify_vendor_payment.setVisible(true);
                        jframe_to_modify_vendor_payment.prepare_payment_sum();
                         
                    } else if (jTable_show_ven_account_details.getValueAt(point, 3).toString().trim().equals("ف-مرتجع")) {
             //reset_jpanel_1();//لحتى لما يفرغ البيانات في جدول الفاتورة يفرغها والجدول وكل المربعات فارغة
                        //jTabbedPane1.setSelectedIndex(0);
                        String bill_id = jTable_show_ven_account_details.getValueAt(point, 6).toString().trim();
                        r = conn_obj.conn_exec("select * from  return_vendor_bills where return_bill_id=" + bill_id + "");
                        r.next();
                        
                      jComboBox12.setSelectedItem(jComboBox_ven_name_for_account_details.getSelectedItem());
                      jTextField38.setText(r.getString("return_bill_value"));
                        jDateChooser8.setDate(r.getDate("return_bill_date"));
                        jComboBox13.setSelectedIndex(r.getInt("return_bill_location_id") - 1);
                        jTextArea7.setText(r.getString("return_bill_note"));
                        jTextField32.setText(r.getString("return_bill_num"));

                        String location_id = r.getString("return_bill_location_id");
                        /////////////////////////////////////////////////// show bill items in jtable////////////
                        r = conn_obj.conn_exec("SELECT \n"
                                + "items.main_items.item_name,\n"
                                + "items.main_items.item_id,\n"
                                + "\n"
                                + "items.item_units.unit_name,\n"
                                + "items.item_units.unit_id,\n"
                                + "\n"
                                + "return_vendor_bills_items.id,return_vendor_bills_items.return_item_id,\n"
                                + "return_vendor_bills_items.return_item_note,return_vendor_bills_items.return_item_unit,\n"
                                + "return_vendor_bills_items.return_item_price,return_vendor_bills_items.return_item_quantity,\n"
                                + "return_vendor_bills_items.return_bill_id\n"
                                + "FROM items.main_items,items.item_units,return_vendor_bills_items\n"
                                + "WHERE\n"
                                + "items.main_items.item_id = return_vendor_bills_items.return_item_id\n"
                                + "AND\n"
                                + "items.item_units.unit_id = return_vendor_bills_items.return_item_unit\n"
                                + "AND\n"
                                + "return_vendor_bills_items.return_bill_id=" + bill_id + "  order by return_vendor_bills_items.id");

                        /*
                         عند تعديل القيد يجب معرفة اذا كان عدد سطور
                         الطلبية المغدلة اكبر من عدد سطور الجدول فتضيغ بغض البيانات
                         */
                        
                        DefaultTableModel tm = (DefaultTableModel) jTable16.getModel();
                        r.last();
                        int r_row_count = r.getRow();
                        int jTable_5_row_count = jTable16.getRowCount();
                        r.beforeFirst();
                        while (r_row_count > jTable_5_row_count) {
                            r_row_count--;
                            tm.addRow(new Object[]{null, null, 1, 1, null, ""});

                        }

                        while (r.next()) {
                            int result_set_row = r.getRow() - 1;
                
                            jTable16.setValueAt(r.getString("item_name"), result_set_row, 0);//item_name
                            jTable16.setValueAt(r.getString("unit_name"), result_set_row, 1);//item_unit
                            //jframe_to_modify_bill.jTable4.setValueAt(r.getString("item_bonus"),result_set_row,3);//item_Bonus
                            jTable16.setValueAt(r.getString("return_item_quantity"), result_set_row, 2);//item_quantity
                            //jframe_to_modify_bill.jTable4.setValueAt(String.format("%.2f", r.getFloat("item_price")),result_set_row,4);//item_price
                            jTable16.setValueAt(r.getFloat("return_item_price"), result_set_row, 3);//item_price
                            //jframe_to_modify_bill.jTable4.setValueAt(r.getString("discount_ratio"),result_set_row,5);//item_discountRatio
                            jTable16.setValueAt(r.getString("return_item_note"), result_set_row, 5);//item_discountRatio
                        }
                        
            ///////////////////////////////////////end show bill_items in jtable
                        //jframe_to_modify_bill.jTable4_listen_to_any_change();
                        jLabel110.setText(bill_id);
                        update_or_show_returned_bill_items_ven.pack();
                        update_or_show_returned_bill_items_ven.setLocationRelativeTo(this);
                        ven_prepare_stm_that_return_quantity_to_store();
                        update_or_show_returned_bill_items_ven.setVisible(true);
                    }
                    else if (jTable_show_ven_account_details.getValueAt(point, 3).toString().trim().equals("خصم"))
                    {                       
                        String discount_id = jTable_show_ven_account_details.getValueAt(point, 6).toString().trim();                       
                        vendor_discount dis=new vendor_discount(this, discount_id, 1);
                        r = conn_obj.conn_exec("select * from  vendor_discount where discount_id=" + discount_id + "");
                        r.next();
                        dis.jComboBox4.setSelectedItem(jComboBox_ven_name_for_account_details.getSelectedItem());
                        dis.jTextField11.setText(r.getString("discount_value"));
                        dis.jDateChooser2.setDate(r.getDate("discount_date"));
                        dis.jTextArea2.setText(r.getString("discount_note"));
                        dis.setLocationRelativeTo(this);
                        dis.setVisible(true);
                    }

                }
            } catch (Exception ex) {
                Joptionpane_message(ex.getMessage());
            }
        }
    } 
    }//GEN-LAST:event_modify_record_venActionPerformed

    private void show_note_venActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_show_note_venActionPerformed
        int point = jTable_show_ven_account_details.getSelectedRow();
        if (jTable_show_ven_account_details.getValueAt(point, 2).toString().equals("")) {
            Joptionpane_message("لا توجد ملاحظة");
        } else {
            Joptionpane_message(jTable_show_ven_account_details.getValueAt(point, 2).toString());
        }
    }//GEN-LAST:event_show_note_venActionPerformed

    private void delete_record_venActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_record_venActionPerformed

        try {
            int point = jTable_show_ven_account_details.getSelectedRow();
            delete_vendor_bill_or_payment(jTable_show_ven_account_details.getValueAt(point, 3).toString().trim(), jTable_show_ven_account_details.getValueAt(point, 6).toString().trim());
        } catch (SQLException ex) {
            Joptionpane_message(ex.getMessage());
        }
    }//GEN-LAST:event_delete_record_venActionPerformed

    private void copy_of_bill_venActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copy_of_bill_venActionPerformed
       // copy_bill_vendor();        // TODO add your handling code here:
    }//GEN-LAST:event_copy_of_bill_venActionPerformed

    private void jTable16MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable16MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable16MouseReleased

    private void jButton62ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton62ActionPerformed
        DefaultTableModel tm = (DefaultTableModel) jTable16.getModel();
        int[] rows_array;
        rows_array = jTable16.getSelectedRows();
        for (int i = 0; i < rows_array.length; i++) {
            int selectd_row = rows_array[i] - i;
            tm.removeRow(selectd_row);
        }// TODO add your handling code here:
    }//GEN-LAST:event_jButton62ActionPerformed

    private void jButton63ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton63ActionPerformed
        try {
            System.out.println(conn_obj.get_con().isClosed());
        } catch (SQLException ex) {
           // Logger.getLogger(vendors.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            conn_obj.get_con().setAutoCommit(false);
            jTable16.editingCanceled(null);//هذا اذا نسي المدخل خانات مفتوحة اي مفتوحة للتعديل لا يتعامل معها على انها فارغة فيخرج منها قبل ادخال البيانات
            String customer = (String) jComboBox12.getSelectedItem();//اخذ اسم المورد من القائمة
            if (customer.equals("------")) {
                throw new Exception("لم يتم اختيار الاسم الصحيح");
            }
            float value = float_parsing(jTextField38);
            if (Float.valueOf(jTextField38.getText().toString().trim()) <= 0 || jTextField38.getText().matches("")) {
                throw new Exception("لا يمكن ان تساوي الفاتورة القيمة صفر او فارغ");
            }

            if (value !=  prepare_modify_vendor_bill_returns_bill()) {
                if (check_yes_or_no_question("محموع الفاتورة الفعلي لا يساوي المجموع الموجود؟") == false) {
                    throw new Exception("تم الالغاء!!");
                }
            }

            String location = (String) jComboBox13.getSelectedItem();
            String note = jTextArea7.getText().trim();
            //////////////////////////////////////
            java.util.Date d;
            d = jDateChooser8.getDate();
            java.sql.Date sqlDate = new java.sql.Date(d.getTime());
            ///////////////////////////
            String bill_num = jTextField32.getText().trim();

            ///
            conn_obj.get_st().execute("delete from return_vendor_bills where return_bill_id = " + jLabel110.getText() + " and accounted=false");

            r = conn_obj.conn_exec("select vendor_id from vendors where vendor_name='" + customer + "'");
            r.next();
            int customer_id = Integer.parseInt(r.getString(1));

            String insert_table_content = "";
            insert_table_content = "INSERT INTO return_vendor_bills (return_bill_value,return_bill_vendor_id,return_bill_location_id,return_bill_note,return_bill_date,return_bill_num,return_bill_id) VALUES"
            + "(" + value + "," + customer_id + ",(select location_id from location where location_name='" + location + "'),'" + note + "','" + sqlDate + "','" + bill_num + "'," + jLabel110.getText() + ");";
            /////////////////إدخال قيم الجدول ////////////////
            for (int i = 0; i < jTable16.getRowCount(); i++) {
                if (jTable16.getValueAt(i, 0) != null) {
                    insert_table_content += "insert into return_vendor_bills_items(return_item_id,return_item_quantity,return_item_unit,return_item_price,return_item_note,return_bill_id)values"
                    + "((select item_id from items.main_items where item_name = '" + jTable16.getValueAt(i, 0) + "' ),"
                    + jTable16.getValueAt(i, 2) + ",(select unit_id from items.item_units where unit_name = '" + jTable16.getValueAt(i, 1) + "' )," + jTable16.getValueAt(i, 3) + ",'" + jTable16.getValueAt(i, 5) + "'," + jLabel110.getText() + ");";
                }
            }
            ////
            conn_obj.get_st().execute(insert_table_content);

            ////إدخال الكميات وانقاصها من المخزون للمخزن المحدد
            String stm_exec = "";
            stm_exec = "select location_id from location where location_name='" + jComboBox13.getSelectedItem().toString() + "'";

            r = conn_obj.conn_exec(stm_exec);
           // System.out.println(stm_exec);
            r.next();
            String location_id = r.getString("location_id");

            for (int i = 0; i < jTable16.getRowCount(); i++) {
                float quantity = Float.parseFloat(jTable16.getValueAt(i, 2).toString());
                float net_Q=quantity;
                stm_exec = ""
                + "select quantity,main_item_id from  (\n"
                + "select \n"
                + "\n"
                + "items.main_items.item_name,\n"
                + "items.main_items.item_id as main_item_id,\n"
                + "\n"
                + "items.item_units.unit_id,\n"
                + "items.item_units.unit_name,\n"
                + "\n"
                + "items.item_relations.item_id,\n"
                + "items.item_relations.item_unit,\n"
                + "items.item_relations.item_relation*" + net_Q + " as quantity\n"
                + "\n"
                + "from items.main_items,items.item_units,items.item_relations\n"
                + "\n"
                + "where \n"
                + "items.main_items.item_name='" + jTable16.getValueAt(i, 0) + "'  AND\n"
                + "items.item_units.unit_name='" + jTable16.getValueAt(i, 1) + "'  AND\n"
                + "items.main_items.item_id=items.item_relations.it"
                + "em_id AND\n"
                + "items.item_units.unit_id=items.item_relations.item_unit )as anyThing";
                r = conn_obj.conn_exec(stm_exec);
                r.next();
                double quantity_to_decrease = r.getDouble("quantity") * -1;
                System.out.println("Quantity_updated == "+ quantity);
                int item_id = r.getInt("main_item_id");
                String store_to_update = "store_id_" + location_id;//store_id_1
                stm_exec = "update items.inventory set " + store_to_update + " = " + store_to_update + " + " + quantity_to_decrease + " where item_id=" + item_id + "";
                conn_obj.exec(stm_exec);
            }
            conn_obj.exec(ven_stm_to_return_quantity_to_store);
            ven_stm_to_return_quantity_to_store="";
            //////////////////////////////////////////////////
           check_two_input_vendor(customer_id, sqlDate);

            conn_obj.get_con().commit();
            Joptionpane_message("لقد تم تعديل البيانات");
            float account = get_vendor_account_sum(customer_id);
            Joptionpane_message("حساب  " + customer + "  يساوي\n" + account + "");

            ///////////////////////////////////////////////////to write on file //////////////////////
           write_to_file(get_date() + "     " + sqlDate.toString() + " " + "  إدخال فاتورةمرتجعات  لحساب الزبون  " + customer + " " + "بقيمة " + " "
                + Float.toString(value) + " " + "الرصيد = " + " " + Float.toString(account));

            update_or_show_returned_bill_items_ven.dispose();
            jTabbedPane_vendor_action.setSelectedIndex(2);
            create_ven_account_table();

        } catch (Exception ex) {
            try {
                conn_obj.get_con().rollback();
                Joptionpane_message(ex.getMessage().toString());
                //Logger.getLogger(vendors.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception e) {
                Joptionpane_message(e.getMessage().toString());
            }
            //Logger.getLogger(vendors.class.getName()).log(Level.SEVERE, null, ex);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton63ActionPerformed

    private void jTextField38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField38ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField38ActionPerformed

    private void jButton64ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton64ActionPerformed
        which_component_request_searchCustomerName_vendor = 9;
        searchvendorName.pack();
        searchvendorName.setLocationRelativeTo(this);
        searchvendorName.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton64ActionPerformed

    private void jTextField32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField32ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField32ActionPerformed

    private void jButton65ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton65ActionPerformed
       // System.out.println("thisssss1");
        which_component_request_searchItemName_vendor = 5;
        search_ven_ItemName.pack();
        search_ven_ItemName.setLocationRelativeTo(this);
        search_ven_ItemName.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton65ActionPerformed

    private void jTextField39FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField39FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField39FocusLost

    private void jTextField39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField39ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField39ActionPerformed

    private void jButton66ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton66ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton66ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        if (search_in_buy_bills==true) {
            which_component_request_searchCustomerName_vendor = 6;
            search_items_in_vendor_bills.pack();
            search_items_in_vendor_bills.setLocationRelativeTo(this);
            search_items_in_vendor_bills.setVisible(true);
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jButton67ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton67ActionPerformed
        if (jCheckBox8.isSelected() == true) {
            Joptionpane_message("ازل اشارة البحث العام");
        } else {
            searchvendorName.pack();
            searchvendorName.setLocationRelativeTo(this);
            searchvendorName.setVisible(true);
        }
    }//GEN-LAST:event_jButton67ActionPerformed

    private void jButton68ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton68ActionPerformed
        which_component_request_searchItemName_vendor = 2;
        search_ven_ItemName.pack();
        search_ven_ItemName.setLocationRelativeTo(this);
        search_ven_ItemName.setVisible(true);
    }//GEN-LAST:event_jButton68ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        vendor_discount dis;
        if (discount_ven==true) {
            dis = new vendor_discount(this);
            dis.setLocationRelativeTo(this);
            dis.setVisible(true);
        }
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jPanel5FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel5FocusGained

    }//GEN-LAST:event_jPanel5FocusGained

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
              //DefaultTableModel tm = (DefaultTableModel) jTable_ven_edorsed_checks.getModel();
    DefaultTableModel tm = (DefaultTableModel)  jTable13.getModel();
        int[] rows_array;
        rows_array = jTable13.getSelectedRows();
        for (int i = 0; i < rows_array.length; i++) {
            int selectd_row = rows_array[i] - i;
            tm.removeRow(selectd_row);
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        DefaultTableModel tm = (DefaultTableModel) jTable13.getModel();
        tm.addRow(new Object[]{jTable13.getRowCount() + 1, "", "", "", (String) jComboBox4.getSelectedItem(), get_date(), "", ""});        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jTextField12KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField12KeyReleased
        prepare_payment_sum();
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField12KeyReleased

    private void jTextField11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField11ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        show_customer_or_vend_checks sh = new show_customer_or_vend_checks(this);
        sh.jComboBox1.setSelectedItem(jComboBox4.getSelectedItem());
        sh.setVisible(true);
        sh.setLocationRelativeTo(this);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        which_component_request_searchCustomerName = 5;
        searchCustomerName.pack();
        searchCustomerName.setLocationRelativeTo(this);
        searchCustomerName.setVisible(true);
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
        if (jPanel5.isVisible()) {
            jLabel38.setText("الرصيد = " + String.valueOf(get_customer_account_sum(jComboBox4.getSelectedItem().toString())));

            try {
                jLabel37.setText("في التحصيل = " + Float.toString(get_customer_checks_sum_under_collection(jComboBox4.getSelectedItem().toString())));
            } catch (SQLException ex) {
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        try {
            conn_obj.get_con().setAutoCommit(false);
            String vendor = (String) jComboBox4.getSelectedItem();
            if (vendor.equals("------")) {
                throw new Exception("لم يتم اختيار الاسم الصحيح");
            }
            String reciever = jTextField14.getText().trim();
            String sender = jTextField19.getText().trim();
            String voucher_no = jTextField13.getText().trim();
            float value = float_parsing(jTextField11);
            if (value <= 0) {
                throw new Exception("خطأ .. الدفعة أقل او يساوي صفر");
            }
            String note = jTextArea2.getText().trim();
            //////////////////////////////////////

            java.util.Date d;
            d = jDateChooser2.getDate();
            java.sql.Date sqlDate = new java.sql.Date(d.getTime());
            // discount
            float discount_value = float_parsing(jTextField26);
            ///////////////////////////
            for (int i = 0; i < jTable13.getRowCount(); i++) {
                //فحص سلامة تاريخ استخقاق السيك
                if (!isDateValid(jTable13.getValueAt(i, 5).toString())) {
                    throw new Exception("خطا    " + jTable13.getValueAt(i, 5).toString() + "تاريخ الشيك المدخل رقم   ");
                }
                //فحص سلامة قسمة الشيك ان لا تكون سالبة او صفر
                if (Float.parseFloat(jTable13.getValueAt(i, 2).toString()) <= 0) {
                    throw new Exception("  قيمته خطا" + jTable13.getValueAt(i, 5).toString() + "  الشيك رقم");
                }
            }
            r = conn_obj.conn_exec("select * from customers where customer_name='" + vendor + "'");
            r.next();
            int vendor_id = Integer.parseInt(r.getString(1));

            String stm = "";

            stm = "INSERT INTO customer_payments (payment_value,customer_id_fk,payment_rec,payment_maker,payment_date,payment_note,voucher_no) VALUES(" + value + "," + vendor_id + ",'" + reciever + "','" + sender + "','" + sqlDate + "','" + note + "','" + voucher_no + "');";
            for (int i = 0; i < jTable13.getRowCount(); i++) {
                stm += "INSERT INTO customer_checks (check_owner,check_endorser,check_no,check_bank,check_due_date,check_payment_id,check_note,check_value)VALUES ('" + jTable13.getValueAt(i, 3) + "','" + jTable13.getValueAt(i, 4) + "','" + jTable13.getValueAt(i, 1) + "','" + jTable13.getValueAt(i, 6).toString() + "','" + jTable13.getValueAt(i, 5) + "',(select last_value from customer_payments_counter),'" + jTable13.getValueAt(i, 7) + "'," + jTable13.getValueAt(i, 2) + ");";
            }
            //ادخال قيد الخصم بشكل منفصل
            if (discount_value > 0) {
                stm += "INSERT INTO customer_discount (discount_value,customer_id_fk,discount_date) VALUES("+discount_value+","+vendor_id+",'"+sqlDate+"');";
            }
            //الانتهاء من ادخال قيد الخصم
          //  System.out.println(stm);
            conn_obj.exec(stm);
            conn_obj.get_con().commit();
            /*
            if(jLabel52.getIcon()!= null)//صورة الدفعة النقدية
            {
                r = conn_obj.conn_exec("SELECT currval('customer_payments_counter')");
                r.next();
                save_payment_image_to_folder("D:\\Dropbox\\dist\\payments_images\\", r.getString("currval"),jLabel52);
                }*/
                Joptionpane_message("تم إدخال الدفعة الى حساب الزبون");
                float account = get_customer_account_sum(vendor_id);
                Joptionpane_message("حساب  " + vendor + "  يساوي\n" + account + "");

                move_to_customer_vendor_account();
                reset_panel_customer_payment();
                write_to_file(get_date() + "     " + sqlDate.toString() + "   " + " تم إدخال دفعة الى حساب الزبون" + "    " + vendor + "    " + "بقيمة " + "    " + value + "  مجموع الحساب " + "    " + account);
            } catch (Exception ex) {
                try {
                    conn_obj.get_con().rollback();
                    Joptionpane_message(ex.getMessage());
                    Joptionpane_message("لم يتم إدخال البيانات");
                } catch (SQLException ex1) {
                    Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
                Joptionpane_message(ex.getMessage());
            }

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton1_checks_change_orderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1_checks_change_orderActionPerformed
        java.util.Date d;
        d = jDateChooser1_checks_change_1.getDate();
        java.sql.Date sqlDate1 = new java.sql.Date(d.getTime());
        d = jDateChooser2_checks_change2.getDate();
        java.sql.Date sqlDate2 = new java.sql.Date(d.getTime());
        conn_obj.exec("update checks set check_status =(select status_id from check_status where status='"+(String) jComboBox1_checks_status.getSelectedItem()+"') where \n" +
            "                check_due_date>='"+sqlDate1+"' \n" +
            "               and check_due_date<='"+sqlDate2+"'");
        Joptionpane_message("تم");
        jFrame1_modify_checks_to_anothor_stat.dispose();
    }//GEN-LAST:event_jButton1_checks_change_orderActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        if (modify_check_status==true) {
            jFrame1_modify_checks_to_anothor_stat.setLocationRelativeTo(this);
            jFrame1_modify_checks_to_anothor_stat.pack();
            jFrame1_modify_checks_to_anothor_stat.setVisible(true);
        }
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
if(telephone_obj!=null)
{
    if(telephone_obj.isVisible())
        telephone_obj.setState(NORMAL);
    else{
    telephone_obj.dispose();
    telephone_obj=new telephone();
    telephone_obj.setVisible(true);
    }
}
       
else
{
    telephone_obj=new telephone();
    telephone_obj.setVisible(true);
}        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        
        Thread thread1 = new Thread(currency);
        thread1.start();

    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jTextField12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField12ActionPerformed
jButton5.doClick();        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField12ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        if (return_check_to_customer==true) {
            returned_checks ret;
            ret = new returned_checks(this);
            ret.setLocationRelativeTo(this);
            ret.setVisible(true);
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
int safe = JOptionPane.showConfirmDialog(null, "هل تريد بالتأكيد اغلاق البرنامج ؟", "Exit Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);

if(safe == JOptionPane.YES_OPTION){
    do_you_want_to_make_backup();
    setDefaultCloseOperation(EXIT_ON_CLOSE);//yes

} else if (safe == JOptionPane.CANCEL_OPTION) {
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);//cancel
    
} else if (safe == JOptionPane.NO_OPTION) {
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);//no
}
    }//GEN-LAST:event_formWindowClosing

    private void jTextField26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField26ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField26ActionPerformed

    private void jTextField26KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField26KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField26KeyReleased

    private void jButton70ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton70ActionPerformed
//لفحص على الاثل مربع واحد مختار لكي لا يعمل اكسبشن غير مفهوم  1
        boolean is_there_checked_one_box = false;
        for (int i = 0; i < jPanel69.getComponentCount(); i++) {
            JCheckBox checkBox = (JCheckBox) jPanel69.getComponent(i);
            if (checkBox.isSelected()) {
                is_there_checked_one_box = true;
                i=i+10000;//للخروج من اللوب تحقق الشرط
            }
        }
     //انتهي الفحص 1
        if(is_there_checked_one_box==true)
        try{
            String summary="";
            java.util.Date d;
        d = jDateChooser9.getDate();
        java.sql.Date date = new java.sql.Date(d.getTime());
        d = jDateChooser10.getDate();
        java.sql.Date date2 = new java.sql.Date(d.getTime());
             //   وضعنا الرقم 
            //   -0.211524 
            ///   مكان الموقع في قيد خصم للمورد و قيد الدفعة للمورد
            //           وهو رقم تعجيزي لا يمكن ان يحصل ان يكون ال اي دي لموقع هذا الرقم
 
String stm = "select value as القيمة,date as التاريخ,movement as الحركة,vendor_name as اسم_الزبون,(select location_name from public.location where location_id=ddfv.location) from(  ";
String payments_movement= "select vendor_payments.payment_value as value,-0.211524 as location,vendor_payments.payment_date as date,'دفعة' as movement,vendor_payments.vendor_id_fk,vendor_payments.record_time,vendors.vendor_id,vendors.vendor_name\n"
                    + "from vendor_payments \n"
                    + "\n"
                    + "join vendors\n"
                    + "on vendors.vendor_id=vendor_payments.vendor_id_fk\n"
                    + "where vendor_payments.payment_date >= '" + date + "' and vendor_payments.payment_date <= '" + date2 + "' ";

String bills_movement="select vendor_bills.bill_value as value,vendor_bills.bill_location_id as location,vendor_bills.bill_date as date,'فاتورة' as movement,vendor_bills.bill_vendor_id,vendor_bills.record_time,vendors.vendor_id,vendors.vendor_name\n"
                    + "from vendor_bills \n"
                    + "\n"
                    + "join vendors\n"
                    + "on vendors.vendor_id=vendor_bills.bill_vendor_id\n"
                    + "where vendor_bills.bill_date >= '" + date + "' and vendor_bills.bill_date <= '" + date2 + "'  ";
String return_bill_movement="select return_bill_value as value,return_vendor_bills.return_bill_id as location,return_bill_date as date,'ف-مرتجع' as movement,return_bill_vendor_id,record_time,vendors.vendor_id,vendors.vendor_name\n"
                    + "from return_vendor_bills \n"
                    + "\n"
                    + "join vendors\n"
                    + "on vendors.vendor_id=return_bill_vendor_id\n"
                    + "where return_bill_date >= '" + date + "' and return_bill_date <= '" + date2 + "'  ";
String discount_movements="select discount_value as value,-0.211524 as location,discount_date as date,'خصم' as movement,vendor_id_fk,record_time,vendors.vendor_id,vendors.vendor_name\n"
                    + "from vendor_discount \n"
                    + "\n"
                    + "join vendors\n"
                    + "on vendors.vendor_id=vendor_id_fk\n"
                    + "where discount_date >= '" + date + "' and discount_date <= '" + date2 + "' ";        
        for (int i = 0; i < jPanel69.getComponentCount(); i++) {
            JCheckBox checkBox = (JCheckBox) jPanel69.getComponent(i);
            if (checkBox.isSelected()) {
                if (checkBox.getText().equals("فواتير مشتريات")) {
                    stm += bills_movement;
                    r = conn_obj.conn_exec("select sum (value) from ( "+bills_movement+" ) as vbg");
                    r.next();
                    summary+="مجموع فواتير المشتريات  =  "+r.getString("sum");          
                }
                else if(checkBox.getText().equals("سند صرف")) {
                    stm += payments_movement;
                    r = conn_obj.conn_exec("select sum (value) from ( "+payments_movement+" ) as vbg");
                    r.next();
                    summary+="مجموع سندات الصرف  =  "+r.getString("sum"); 
                }
                else if(checkBox.getText().equals("خصومات")) {
                    stm += discount_movements;
                    r = conn_obj.conn_exec("select sum (value) from ( "+discount_movements+" ) as vbg");
                    r.next();
                    summary+="  مجموع قيود الخصومات =  "+r.getString("sum"); 
                }
                else if(checkBox.getText().equals("فواتير مرتجعات")) {
                    stm += return_bill_movement;
                    r = conn_obj.conn_exec("select sum (value) from ( "+return_bill_movement+" ) as vbg");
                    r.next();
                    summary+="مجوع فواتير مردود مبيعات   =   "+r.getString("sum"); 
                }
                //لنتاكد من ان بعد هذد الحركة يوجد حركة مختارة لنضع يونيون والا فالجملة ستكون خطأ
                for (int j = i + 1; j < jPanel69.getComponentCount(); j++) {
                    checkBox = (JCheckBox) jPanel69.getComponent(j);
                    if (checkBox.isSelected()) {
                        stm+= "union ";
                        j=j+100;//للخروج من اللوب ضروري في حال التاكد
                    }
                }
                //انتهينا نمن التاكد من وجود حركة اخرى مختارة ام لا

            }
        }
        stm+=" order by record_time )as ddfv order by التاريخ";
           // System.out.println(stm);
        r = conn_obj.conn_exec(stm);
            jTable18.setModel(DbUtils.resultSetToTableModel(r));
            jLabel142.setText(summary);
            renderer_jTable_obj.Renderer(jTable18);
            jTable18.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        }
        catch(Exception e)
        {
            Joptionpane_message(e.getMessage());
        }
        
        else{
            Joptionpane_message("اختر مربعا واحد من الحركات على الاقل!!");
        }          // TODO add your handling code here:
    }//GEN-LAST:event_jButton70ActionPerformed

    private void jButton71ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton71ActionPerformed

        
        if(jTable4.getColumnCount()==9){
          TableColumn tcol = jTable4.getColumnModel().getColumn(8);
          jTable4.getColumnModel().removeColumn(tcol);
          jLabel53.setText("0");
          jLabel54.setText("0");
}
    }//GEN-LAST:event_jButton71ActionPerformed

    private void jTable9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable9MouseClicked
    javax.swing.JPopupMenu menu=new JPopupMenu();
    JMenuItem item = new JMenuItem("تفصيل الحركة");
    JMenuItem print_item = new JMenuItem("طباعة");
    
        ActionListener menuListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                  int point = jTable9.getSelectedRow();
                  if (jTable9.getValueAt(point, 2).toString().trim().equals("فاتورة"))
        show_bill_items(jTable9.getValueAt(point, 3).toString().trim(),jTable9.getValueAt(point, 2).toString().trim());

            }
        };
        ActionListener printListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    int point = jTable9.getSelectedRow();
                    String bill_id = jTable9.getValueAt(point, 3).toString().trim();
                    if (jTable9.getValueAt(point, 2).toString().trim().equals("فاتورة"))
                    print_customer_bill(bill_id);
                } catch (Exception ex) {
                    Joptionpane_message(ex.getMessage());
                }
            }
        };
    menu.add(item);
    menu.add(print_item);
    item.addActionListener(menuListener);
    print_item.addActionListener(printListener);
    menu.show(evt.getComponent(), evt.getX(), evt.getY());
    
    
    
    
    }//GEN-LAST:event_jTable9MouseClicked

    private void jTextField24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField24ActionPerformed

    private void jTable3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MousePressed
if (evt.getButton() == MouseEvent.BUTTON1) {
            jPopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
        } else if (evt.getButton() == MouseEvent.BUTTON3) {
            int r = jTable3.rowAtPoint(evt.getPoint());
            try {
                // delete_bill_or_payment(jTable3.getValueAt(r,6).toString().trim(),jTable3.getValueAt(r,9).toString().trim());
            } catch (Exception ex) {

                Joptionpane_message(ex.getMessage());
            }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jTable3MousePressed

    private void jMenu1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MousePressed
        if (sale_point==true) {
            sale_point_obj = new sale_point();
            sale_point_obj.setExtendedState(sale_point_obj.MAXIMIZED_BOTH);
            sale_point_obj.pack();
            sale_point_obj.setVisible(true);
        }
    }//GEN-LAST:event_jMenu1MousePressed

    private void jComboBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox6ActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed
        if (ranking_items==true) {
            ranking.start();
        }
    }//GEN-LAST:event_jMenuItem23ActionPerformed

    private void jComboBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox7ActionPerformed

    private void jButton72ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton72ActionPerformed
        try {
            jTable9.print();
        } catch (PrinterException ex) {
            Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton72ActionPerformed

    private void jButton73ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton73ActionPerformed
insert_bill_to_customer_jtable4(true);
    }//GEN-LAST:event_jButton73ActionPerformed

    private void direct_printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_direct_printActionPerformed
        int point = jTable3.getSelectedRow();
        String bill_id = jTable3.getValueAt(point, 6).toString().trim();
        print_customer_bill(bill_id);
    }//GEN-LAST:event_direct_printActionPerformed

    private void jButton74ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton74ActionPerformed
        try {
            jTable_ven_edorsed_checks.print();
        } catch (PrinterException ex) {
            Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton74ActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
       try{
           int count=0;
            r = conn_obj.conn_exec("select check_value from customer_checks where vendor_payment_id is  null ;");
                    while(r.next())
                    {
                        count+=1;
                    }
           r = conn_obj.conn_exec("select sum (check_value)from customer_checks where vendor_payment_id is  null;");
                    r.next();
                    Joptionpane_message("مجموع شكات الزبائن الغير مجيرة هو :"+ r.getFloat("sum")+"\n"+"بعدد شيكات  : "+count);
       }catch(SQLException e)
       {
           
       }
        
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jTextField_ven_pay_cashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_ven_pay_cashActionPerformed
      jButton45.doClick(); 
    }//GEN-LAST:event_jTextField_ven_pay_cashActionPerformed

    private void jTable7MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable7MouseMoved
        int c = jTable7.columnAtPoint(evt.getPoint());
        if (c == 6) {
            int r = jTable7.rowAtPoint(evt.getPoint());
            if (iconnn == null) {
                iconnn = new image_icon(images[r]);
                iconnn.setVisible(true);
            } else {
                iconnn.jLabel1.setIcon(images[r]);
                iconnn.setVisible(true);

            }
        }
        else if (c == 5)
        {
            if(iconnn.isVisible())
           iconnn.setVisible(false);
        }

    }//GEN-LAST:event_jTable7MouseMoved

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
    File xx = new File("//19-11-2016.jar");
    if (xx.exists()) {
        Joptionpane_message("سيتم اغلاق البرنامج");
    }
    if (xx.exists()) {
        xx.delete();
    }
         
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jTable10KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable10KeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER ) {
if (which_component_request_searchItemName_vendor == 1) {

                add_row_jtable4(jTable_bill_items,
                    jTable10.getValueAt(jTable10.getSelectedRow(), 0), jTable10.getValueAt(jTable10.getSelectedRow(), 1), jTable10.getValueAt(jTable10.getSelectedRow(), 4), 0, jTable10.getValueAt(jTable10.getSelectedRow(), 2), 0, null, jTable10.getValueAt(jTable10.getSelectedRow(), 5));

                show_last_row_scroll_jtable(jTable_bill_items);//هنا الذهاب لاخر صف مكتوب بجدول الفاتورة لامكانية رؤيته مباشرة
            } else if (which_component_request_searchItemName_vendor == 3) {//مردود مشتريات
                DefaultTableModel tm = (DefaultTableModel) jTable_return_ven_bill_items.getModel();
                tm.addRow(new Object[]{
                    jTable10.getValueAt(jTable10.getSelectedRow(), 0),
                    jTable10.getValueAt(jTable10.getSelectedRow(), 1),
                    jTable10.getValueAt(jTable10.getSelectedRow(), 4),
                    jTable10.getValueAt(jTable10.getSelectedRow(), 2),
                    null,
                    jTable10.getValueAt(jTable10.getSelectedRow(), 5)});

            show_last_row_scroll_jtable(jTable_return_ven_bill_items);
        }
            jTextField25.requestFocus();
            jTextField25.selectAll();

        }               // TODO add your handling code here:
    }//GEN-LAST:event_jTable10KeyReleased

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        item_card item_card_obj= new item_card();
        item_card_obj.setLocationRelativeTo(this);
        item_card_obj.setVisible(true);
    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void searchItemNameWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_searchItemNameWindowLostFocus
       searchItemName.hide();
    }//GEN-LAST:event_searchItemNameWindowLostFocus

    private void search_ven_ItemNameWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_search_ven_ItemNameWindowLostFocus
        search_ven_ItemName.hide();
    }//GEN-LAST:event_search_ven_ItemNameWindowLostFocus

    private void jTextField14MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField14MousePressed
if (evt.getButton() == MouseEvent.BUTTON1) {
    try{
        popup_menue_payment_reciever.removeAll();
    r=conn_obj.conn_exec("select catagory_name from customer_catagory ;");
               while(r.next())
               {
                   JMenuItem x= new JMenuItem(r.getString("catagory_name"));
                popup_menue_payment_reciever.add(x);
                     x.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        jTextField14.setText(x.getText());
    }
               
          
});
                     }
               popup_menue_payment_reciever.show(evt.getComponent(), evt.getX(), evt.getY());
    }
    catch (SQLException ee){
        Joptionpane_message(ee.getMessage());
    }
            
            
        } 
    }//GEN-LAST:event_jTextField14MousePressed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
        customer_catagories cus_catagories=new customer_catagories();
        cus_catagories.setLocationRelativeTo(this);
        cus_catagories.setVisible(true);
    }//GEN-LAST:event_jMenuItem27ActionPerformed

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(customers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new customers().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JMenuItem copy_of_bill;
    private javax.swing.JMenuItem copy_of_bill_ven;
    private javax.swing.JFrame customer_bill;
    private javax.swing.JMenuItem delete_record;
    private javax.swing.JMenuItem delete_record_ven;
    private javax.swing.JMenuItem direct_print;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton1_checks_change_order;
    private javax.swing.JButton jButton2;
    public javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton44;
    private javax.swing.JButton jButton45;
    private javax.swing.JButton jButton46;
    private javax.swing.JButton jButton47;
    private javax.swing.JButton jButton48;
    private javax.swing.JButton jButton49;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton50;
    private javax.swing.JButton jButton51;
    private javax.swing.JButton jButton52;
    private javax.swing.JButton jButton53;
    private javax.swing.JButton jButton54;
    private javax.swing.JButton jButton55;
    private javax.swing.JButton jButton56;
    private javax.swing.JButton jButton57;
    private javax.swing.JButton jButton58;
    private javax.swing.JButton jButton59;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton60;
    private javax.swing.JButton jButton61;
    private javax.swing.JButton jButton62;
    private javax.swing.JButton jButton63;
    private javax.swing.JButton jButton64;
    private javax.swing.JButton jButton65;
    private javax.swing.JButton jButton66;
    private javax.swing.JButton jButton67;
    private javax.swing.JButton jButton68;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton70;
    private javax.swing.JButton jButton71;
    private javax.swing.JButton jButton72;
    private javax.swing.JButton jButton73;
    private javax.swing.JButton jButton74;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButton_Tahseel_details;
    private javax.swing.JButton jButton_add_new_ven;
    private javax.swing.JButton jButton_del_return_ven_bill_item;
    private javax.swing.JButton jButton_isert_bill;
    private javax.swing.JButton jButton_remove_bill_item;
    private javax.swing.JButton jButton_return_ven_bill_insert;
    private javax.swing.JButton jButton_return_ven_bill_last_price;
    private javax.swing.JButton jButton_search_bill_item;
    private javax.swing.JButton jButton_search_ven_name_pay;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox10;
    private javax.swing.JCheckBox jCheckBox11;
    private javax.swing.JCheckBox jCheckBox12;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    public javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox10;
    private javax.swing.JComboBox jComboBox11;
    public javax.swing.JComboBox jComboBox12;
    private javax.swing.JComboBox jComboBox13;
    private javax.swing.JComboBox jComboBox1_checks_status;
    private javax.swing.JComboBox jComboBox2;
    public javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JComboBox jComboBox8;
    private javax.swing.JComboBox jComboBox9;
    private javax.swing.JComboBox jComboBox_bill_store;
    private javax.swing.JComboBox jComboBox_return_ven_store;
    private javax.swing.JComboBox jComboBox_van_name_return_bill;
    public javax.swing.JComboBox jComboBox_ven_name_for_account_details;
    private javax.swing.JComboBox jComboBox_ven_name_pay;
    public javax.swing.JComboBox jCombo_vendor_name;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser10;
    private com.toedter.calendar.JDateChooser jDateChooser1_checks_change_1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser2_checks_change2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser4;
    private com.toedter.calendar.JDateChooser jDateChooser5;
    private com.toedter.calendar.JDateChooser jDateChooser6;
    private com.toedter.calendar.JDateChooser jDateChooser7;
    private com.toedter.calendar.JDateChooser jDateChooser8;
    private com.toedter.calendar.JDateChooser jDateChooser9;
    private com.toedter.calendar.JDateChooser jDateChooser_bill_date;
    private com.toedter.calendar.JDateChooser jDateChooser_return_ven_bill_date;
    private com.toedter.calendar.JDateChooser jDateChooser_ven_pay_date;
    private javax.swing.JFrame jFrame1_modify_checks_to_anothor_stat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel142;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel144;
    private javax.swing.JLabel jLabel145;
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
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JLabel jLabel_in_Tahseel;
    private javax.swing.JLabel jLabel_ven_acc_sum;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel66;
    private javax.swing.JPanel jPanel67;
    private javax.swing.JPanel jPanel68;
    private javax.swing.JPanel jPanel69;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel72;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanel_add_ven;
    private javax.swing.JPanel jPanel_add_ven_bill;
    private javax.swing.JPanel jPanel_del_ven_name;
    private javax.swing.JPanel jPanel_modify_ven_name;
    private javax.swing.JPanel jPanel_return_ven_bill;
    private javax.swing.JPanel jPanel_ven_acount_details;
    private javax.swing.JPanel jPanel_ven_names;
    private javax.swing.JPanel jPanel_ven_pay;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JPopupMenu jPopupMenu_ven;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane26;
    private javax.swing.JScrollPane jScrollPane27;
    private javax.swing.JScrollPane jScrollPane28;
    private javax.swing.JScrollPane jScrollPane29;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane30;
    private javax.swing.JScrollPane jScrollPane31;
    private javax.swing.JScrollPane jScrollPane32;
    private javax.swing.JScrollPane jScrollPane33;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane42;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    public javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    public javax.swing.JTabbedPane jTabbedPane_vendor_action;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable10;
    private javax.swing.JTable jTable11;
    private javax.swing.JTable jTable12;
    private javax.swing.JTable jTable13;
    private javax.swing.JTable jTable14;
    private javax.swing.JTable jTable15;
    private javax.swing.JTable jTable16;
    private javax.swing.JTable jTable18;
    private javax.swing.JTable jTable2;
    public javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    public javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTable7;
    private javax.swing.JTable jTable8;
    private javax.swing.JTable jTable9;
    private javax.swing.JTable jTable_bill_items;
    private javax.swing.JTable jTable_return_ven_bill_items;
    private javax.swing.JTable jTable_shoe_ven_names;
    public javax.swing.JTable jTable_show_ven_account_details;
    private javax.swing.JTable jTable_van_pay_my_checks;
    public javax.swing.JTable jTable_ven_edorsed_checks;
    private javax.swing.JTable jTable_ven_names;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea5;
    private javax.swing.JTextArea jTextArea6;
    private javax.swing.JTextArea jTextArea7;
    private javax.swing.JTextArea jTextArea_bill_note;
    private javax.swing.JTextArea jTextArea_return_ven_bill_note;
    private javax.swing.JTextArea jTextArea_ven_pay_note;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField27;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField30;
    private javax.swing.JTextField jTextField31;
    private javax.swing.JTextField jTextField32;
    private javax.swing.JTextField jTextField33;
    private javax.swing.JTextField jTextField34;
    private javax.swing.JTextField jTextField35;
    private javax.swing.JTextField jTextField36;
    private javax.swing.JTextField jTextField37;
    private javax.swing.JTextField jTextField38;
    private javax.swing.JTextField jTextField39;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JTextField jTextField_bill_before_dis;
    private javax.swing.JTextField jTextField_bill_dis_ratio;
    private javax.swing.JTextField jTextField_bill_discount;
    private javax.swing.JTextField jTextField_bill_number;
    private javax.swing.JTextField jTextField_bill_value;
    private javax.swing.JTextField jTextField_return_ven_barcode;
    private javax.swing.JTextField jTextField_return_ven_bil_numberl;
    private javax.swing.JTextField jTextField_return_ven_bill_value;
    private javax.swing.JTextField jTextField_search_barcode;
    private javax.swing.JTextField jTextField_search_bill_items;
    private javax.swing.JTextField jTextField_ven_addres_to_add;
    private javax.swing.JTextField jTextField_ven_name_to_add;
    private javax.swing.JTextField jTextField_ven_pay_cash;
    private javax.swing.JTextField jTextField_ven_pay_helder;
    private javax.swing.JTextField jTextField_ven_pay_receive;
    private javax.swing.JTextField jTextField_ven_pay_total;
    private javax.swing.JTextField jTextField_ven_phone_to_add;
    private javax.swing.JMenuItem modify_record;
    private javax.swing.JMenuItem modify_record_ven;
    private javax.swing.JPopupMenu popup_menue_payment_reciever;
    public javax.swing.JFrame searchCustomerName;
    private javax.swing.JFrame searchItemName;
    private javax.swing.JFrame search_items_in_customer_bills;
    private javax.swing.JFrame search_items_in_vendor_bills;
    private javax.swing.JFrame search_ven_ItemName;
    public javax.swing.JFrame searchvendorName;
    private javax.swing.JMenuItem show_image;
    private javax.swing.JMenuItem show_image_ven;
    private javax.swing.JMenuItem show_items;
    private javax.swing.JMenuItem show_items_ven;
    private javax.swing.JMenuItem show_note;
    private javax.swing.JMenuItem show_note_ven;
    private javax.swing.JFrame update_or_show_returned_bill_items;
    private javax.swing.JFrame update_or_show_returned_bill_items_ven;
    // End of variables declaration//GEN-END:variables
//هذا الفنكشن ما نقوم بعمله عند العديل على الاسم في جدول المزودين ... ونقوم بتعديل الاسم وتعديله على الجدول

    private void update_customer_name_button() {

        try {
            String name=jTextField8.getText().trim();
            String tel=jTextField9.getText().trim();
            String address=jTextField10.getText().trim();
            String id=jLabel13.getText();
            String catagory_name = jComboBox6.getSelectedItem().toString().trim();
            String origin_name_to_modify=""; 
            if(!name.equals("") && !catagory_name.equals("------"))
            {
                r=conn_obj.conn_exec("select customer_name from customers where customer_id="+id+";");
                r.next();
                origin_name_to_modify=r.getString("customer_name");
                conn_obj.exec("UPDATE customers SET customer_name='" + name + "' , customer_tell='" + tel + "' , customer_location='" + address + "' ,customer_catagory_id= (select catagory_id from customer_catagory where catagory_name= '"+catagory_name+"') where customer_id=" + id + ";");
                r = conn_obj.conn_exec("select customer_id as ID,customer_name As Name,customer_tell as Tell,customer_location as location from customers");
            jTable1.setModel(DbUtils.resultSetToTableModel(r));
            /////
            jComboBox1.removeItem(origin_name_to_modify);
            jComboBox3.removeItem(origin_name_to_modify);
            jComboBox4.removeItem(origin_name_to_modify);
            jComboBox8.removeItem(origin_name_to_modify);
                jComboBox1.addItem(name);
                jComboBox3.addItem(name);
                jComboBox4.addItem(name);
                jComboBox8.addItem(name);
            ///
            jTextField10.setText("");
            jTextField8.setText("");
            jTextField9.setText("");
                Joptionpane_message("تم تعديل الاسم في الجدول الاعلى");
            }
            else
            {
                JOptionPane.showMessageDialog(null, "اسم الزبون فارغ او التصنيف خاطئ");
            }
            
        } catch (SQLException ex) {
            Joptionpane_message(ex.getMessage());
        }
    }

    public void update_jcomboBox_1_3_4() {
        //لتحديث قائمة اسماء الموردين عند اضافة اسم جديد
        // To refresh jcombobox1 after addind a new vendor name
        try {
            jComboBox1.removeAllItems();
            jComboBox3.removeAllItems();
            jComboBox4.removeAllItems();
            jComboBox8.removeAllItems();
            r = conn_obj.conn_exec("select customers.customer_name,customers.customer_catagory_id,\n" +
"     user_privileg_on_customer_catag.user_id_fk,user_privileg_on_customer_catag.customer_catagory_fk\n" +
"from customers,user_privileg_on_customer_catag\n" +
"where \n" +
"    user_privileg_on_customer_catag.user_id_fk=(select user_id from users where user_name='"+user_name+"') and\n" +
"    customer_catagory_id=customer_catagory_fk order by customer_name");
                jComboBox1.addItem("------");
                jComboBox3.addItem("------");
                jComboBox4.addItem("------");
                jComboBox8.addItem("------");
            while (r.next()) {
                jComboBox1.addItem(r.getString(1));
                jComboBox3.addItem(r.getString(1));
                jComboBox4.addItem(r.getString(1));
                jComboBox8.addItem(r.getString(1));
            }
            
            jComboBox_van_name_return_bill.removeAllItems();
            jComboBox_ven_name_for_account_details.removeAllItems();
            jComboBox_ven_name_pay.removeAllItems();
            jCombo_vendor_name.removeAllItems();
            r = conn_obj.conn_exec("select vendor_name from vendors order by vendor_name ");
            while (r.next()) {
                jComboBox_van_name_return_bill.addItem(r.getString(1));
                jComboBox_ven_name_for_account_details.addItem(r.getString(1));
                jComboBox_ven_name_pay.addItem(r.getString(1));
                jCombo_vendor_name.addItem(r.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
        }catch (Exception ex) {
            Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void render_table3() {
        TableColumn tcol;
        tcol = jTable3.getColumnModel().getColumn(0);
        tcol.setCellRenderer(new CustomTableCellRenderer());
        tcol = jTable3.getColumnModel().getColumn(1);
        tcol.setCellRenderer(new CustomTableCellRenderer());
        
        tcol = jTable_show_ven_account_details.getColumnModel().getColumn(0);
        tcol.setCellRenderer(new CustomTableCellRenderer());
        tcol = jTable_show_ven_account_details.getColumnModel().getColumn(1);
        tcol.setCellRenderer(new CustomTableCellRenderer());

    }
    /*
     public int search(int x)
     {
     //File folder = new File("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\Scanned Documents");
     File folder = new File(scanner_path);
     File[] listOfFiles = folder.listFiles();
     // loop through each of the files looking for filenames that match
     for(int i = 0; i < listOfFiles.length; i++){
     String filename = listOfFiles[i].getName();
     if(filename.startsWith(Integer.toString(x)) && listOfFiles[i].getName().endsWith("jpg")){
     {System.out.println("tfffd   true");return  1;}
     }
     }
     return 0;
     }
     */

    public void Joptionpane_message(String message) {
        JOptionPane.showMessageDialog(this, message);

    }

    public void Joptionpane_confirm(String message, String res) throws Exception {

        int response = JOptionPane.showConfirmDialog(null, message,
                "", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (response == JOptionPane.CLOSED_OPTION || response == JOptionPane.NO_OPTION) {
            throw new SQLException(res);
        }

    }

    public void check_two_input_customer(int customer_id, java.sql.Date date) throws Exception {
        r = conn_obj.conn_exec("select bill_id,bill_date,bill_value from customer_bills where bill_customer_id=" + customer_id + " and bill_date='" + date + "'");
        r.last();
        if (r.getRow() > 1) {
            int response = JOptionPane.showConfirmDialog(null, "ملاحظة:...لقد أدخلت اكثر من فاتورة لنفس المورد بنفس اليوم" + "\n" + date + "\nللتأكيد اضغط موافق                         ",
                    "", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (response == JOptionPane.CLOSED_OPTION || response == JOptionPane.NO_OPTION) {
                throw new Exception("تم الغاء الادخال");
            }

        }
    }
public void check_two_input_vendor(int vendor_id, java.sql.Date date) throws Exception {
        r = conn_obj.conn_exec("select bill_id,bill_date,bill_value from vendor_bills where bill_vendor_id=" + vendor_id + " and bill_date='" + date + "'");
        r.last();
        if (r.getRow() > 1) {
            int response = JOptionPane.showConfirmDialog(null, "ملاحظة:...لقد أدخلت اكثر من فاتورة لنفس المورد بنفس اليوم" + "\n" + date + "\nللتأكيد اضغط موافق                         ",
                    "", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (response == JOptionPane.CLOSED_OPTION || response == JOptionPane.NO_OPTION) {
                throw new Exception("تم الغاء الادخال");
            }

        }
    }
    public void reset_jpanel_1() {
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jTextField1.setText("0");
        jTextField15.setText("");
        jTextField17.setText("0");
        jTextField16.setText("0");
        jTextField3.setText("0");
        jTextArea1.setText("");
        jDateChooser1.setDate(get_date_jdate());
        DefaultTableModel model = (DefaultTableModel) jTable4.getModel();
        model.setRowCount(0);
        /////////////////////////////////////////reset jtable4///////////////

////////////////////////////////////////////////////////////////////
    }
    public void reset_panel_customer_payment() {
        jComboBox4.setSelectedIndex(0);
        jTextField11.setText("0");
        jTextField12.setText("0");
        jTextField13.setText("");
        jTextField14.setText("");
        jTextField19.setText("");
        jTextField26.setText("0");//الخصم
        jTextArea2.setText("");
        jDateChooser2.setDate(get_date_jdate());
        DefaultTableModel model = (DefaultTableModel) jTable13.getModel();
        model.setRowCount(0);
    }

    public float float_parsing(JTextField field) throws Exception {
        if (field.getText().indexOf('-') != -1 || field.getText().indexOf('f') != -1 || +field.getText().indexOf('d') != -1) {
            throw new Exception("خطأ في قيمة المدخلات");
        }
        try {
            float value = Float.parseFloat(field.getText());
            return value;
        } catch (Exception e) {
            throw new Exception("خطأ في قيمة المدخلات");
        }

    }

    public float float_parsing_2(String field) throws Exception {
        if (field.indexOf('-') != -1 || field.indexOf('f') != -1 || +field.indexOf('d') != -1) {
            throw new Exception("خطأ في قيمة المدخلات");
        }
        try {
            float value = Float.parseFloat(field);
            return value;
        } catch (Exception e) {
            throw new Exception("خطأ في قيمة المدخلات");
        }

    }

    public void account_zero(String name) throws SQLException, Exception {
        try {
            conn_obj.get_con().setAutoCommit(false);
            r = conn_obj.conn_exec("select customer_id from customers where customer_name='" + name + "'");
            r.next();
            int vendor_id = r.getInt(1);
            r = conn_obj.conn_exec("select location_id from location");//get any location ID to insert in bill to make account zero
            r.next();
            int location_id = r.getInt(1);
            float vendor_account = get_customer_account_sum(vendor_id);
            if (vendor_account < 0) {
                conn_obj.get_st().execute("insert into customer_bills(bill_value,bill_note,bill_customer_id,bill_date,accounted,bill_location_id)"
                        + "values(" + vendor_account * -1 + ",'" + "فاتورة لتصفير الحساب" + "'," + vendor_id + ",'" + get_date() + "',true," + location_id + ")");
            }
            if (vendor_account > 0) {
                conn_obj.get_st().executeUpdate("insert into customer_payments(payment_value,payment_note,customer_id_fk,payment_date,accounted)values(" + vendor_account + ",'" + "دفعة لتصفير الحساب" + "'," + vendor_id + ",'" + get_date() + "',true)");
            }
            conn_obj.get_st().executeUpdate("UPDATE customer_bills SET accounted=true where bill_customer_id=" + vendor_id + " and accounted=false");
            conn_obj.get_st().executeUpdate("UPDATE customer_payments SET accounted=true where customer_id_fk=" + vendor_id + " and accounted=false");
            conn_obj.get_st().executeUpdate("UPDATE return_customer_bills SET accounted=true where return_bill_customer_id=" + vendor_id + " and accounted=false");
            conn_obj.get_st().executeUpdate("UPDATE customer_discount SET accounted=true where customer_id_fk=" + vendor_id + " and accounted=false");
            conn_obj.get_st().executeUpdate("UPDATE returned_checks SET accounted=true where customer_id_fk=" + vendor_id + " and accounted=false");
            Joptionpane_confirm("هل انت متأكد من تصفير الحساب", "لم يتم تصفير الحساب");
            conn_obj.get_con().commit();
            Joptionpane_message("تم تصفير الحساب");

        } catch (Exception e) {
            conn_obj.get_con().rollback();
            Joptionpane_message("لم يتم تصفير الحساب...");
            Joptionpane_message(e.getMessage());
        }
    }

    public void delete_name() throws SQLException, Exception {

        try {
            if (jTable1.getValueAt(jTable1.getSelectedRow(), 1).equals("------")) {
                throw new Exception("خطأ في اختيار الاسم !!!!");
            }
            conn_obj.get_con().setAutoCommit(false);
            conn_obj.get_st().execute("delete from customers where customer_id =" + jTable1.getValueAt(jTable1.getSelectedRow(), 0) + "");
            Joptionpane_confirm("هل انت متأكد من حذف الاسم", "لم يتم حذف الاسم");
            conn_obj.get_con().commit();
            Joptionpane_message("تم الحذف");
            ///** لاعادة تحديث جدول اسماء الزبائب بعد حذف الاسم
            r = conn_obj.conn_exec("select customer_id as ID,customer_name As Name,customer_tell as Tell,customer_location as location from customers");
            jTable1.setModel(DbUtils.resultSetToTableModel(r));
       //**end
            //**To refresh comboBox 3 and 1
            // update_jcomboBox_1_3_4();
            //end
        } catch (Exception e) {
            Joptionpane_message(e.getMessage());
            conn_obj.get_con().rollback();
        }
    }

    public void create_customer_account_table() {
        if (jPanel1.isVisible()) {
            try {
                String vendor = jComboBox3.getSelectedItem().toString();
                String stm="select  round (sum (القيمة) over (order by التاريخ, record_time)::numeric,2)::float8 as المجموع,القيمة,ملاحظة,الحركة ,المكان, التاريخ ,رقم_القيد   ,رقم_الفاتورة  from(\n"
                        + "SELECT round((bill_value::numeric),2)::float8 as القيمة,record_time,bill_location_id,location.location_id,bill_note as ملاحظة,'فاتورة' as الحركة,location.location_name as المكان,bill_date as التاريخ,bill_id as رقم_القيد,bill_num as رقم_الفاتورة\n"
                        + "FROM customer_bills,location\n"
                        + "           where bill_customer_id=(select customer_id as cus_name from customers where customer_name='" + vendor + "') and accounted=false and bill_location_id=location_id\n"
                        + "                        UNION\n"
                        + "SELECT round((return_bill_value::numeric)*-1,2)::float8 as القيمة,record_time,return_bill_location_id,location.location_id,return_bill_note as ملاحظة,'م-مبيعات' as الحركة,location.location_name as المكان,return_bill_date as التاريخ,return_bill_id as رقم_القيد,return_bill_num as رقم_الفاتورة\n"
                        + "FROM return_customer_bills,location\n"
                        + "           where return_bill_customer_id=(select customer_id as cus_name from customers where customer_name='" + vendor + "') and accounted=false and return_bill_location_id=location_id\n"
                        + "                        UNION\n"
                        + "                      \n"
                        + "SELECT round((payment_value::numeric)*-1,2)::float8,record_time,0 ,null,payment_note,'دفعة','---',payment_date,payment_id,'---'\n"
                        + "                        FROM customer_payments,location \n"
                        + "                        where customer_id_fk=(select customer_id as cus_name from customers where customer_name='" + vendor + "') and accounted=false \n"
                        + "                        UNION\n"
                        + "                      \n"
                        + "SELECT round((discount_value::numeric)*-1,2)::float8,record_time,0 ,null,discount_note,'خصم','---',discount_date,discount_id,'---'\n"
                        + "                        FROM customer_discount \n"
                        + "                        where customer_id_fk=(select customer_id as cus_name from customers where customer_name='" + vendor + "') and accounted=false "
                        + ""
                        + "                        UNION\n"
                        + "                      \n"
                        + "SELECT round(((check_value+return_commission)::numeric),2)::float8,record_time,0 ,null,return_note,'شك-مرتجع','---',return_date,id,'---'\n"
                        + "                        FROM returned_checks \n"
                        + "                        where customer_id_fk=(select customer_id as cus_name from customers where customer_name='" + vendor + "') and accounted=false "
                        + ""
                        + "order by التاريخ, record_time\n"
                        + ") as ffff";
                r = conn_obj.conn_exec(stm);
                System.out.println(stm);
                jTable3.setModel(DbUtils.resultSetToTableModel(r));
                render_table3();
                

                

            } catch (Exception ex) {
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    float get_customer_account_sum(int customer_id) {
        {
            try {
                r = conn_obj.conn_exec("select \n" +
"\n" +
"coalesce((select sum (bill_value) from customer_bills where bill_customer_id=" + customer_id + " and accounted=false),0)-  \n" +
"\n" +
"coalesce((select sum (payment_value) from customer_payments where customer_id_fk=" + customer_id + " and accounted=false),0)-  \n" +
"\n" +
"coalesce((select sum (discount_value) from customer_discount where customer_id_fk=" + customer_id + " and accounted=false),0)-  \n" +
"\n" +
"coalesce((select sum (return_bill_value) from return_customer_bills where return_bill_customer_id=" + customer_id + " and accounted=false),0)+  \n" +
"\n" +
"coalesce((select sum (check_value+return_commission) from returned_checks where customer_id_fk=" + customer_id + " and accounted=false),0)\n" +
"\n" +
"as result\n" +
"");
                r.next();
                return (Math.round(r.getFloat(1)*100)/100.0f);
            } catch (SQLException ex) {
                Joptionpane_message(ex.getMessage() + ex.getSQLState());
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Joptionpane_message(ex.getMessage());
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);

            }
            return 0;
        }
    }

    float get_customer_account_sum(String customer_name) {
        {
            try {
                r = conn_obj.conn_exec("select customer_id from customers where customer_name='" + customer_name + "'");
                r.next();
                int customer_id = r.getInt(1);
                
                String stm="select \n" +
"\n" +
"coalesce((select sum (bill_value) from customer_bills where bill_customer_id=" + customer_id + " and accounted=false),0)-  \n" +
"\n" +
"coalesce((select sum (payment_value) from customer_payments where customer_id_fk=" + customer_id + " and accounted=false),0)-  \n" +
"\n" +
"coalesce((select sum (discount_value) from customer_discount where customer_id_fk=" + customer_id + " and accounted=false),0)-  \n" +
"\n" +
"coalesce((select sum (return_bill_value) from return_customer_bills where return_bill_customer_id=" + customer_id + " and accounted=false),0)+  \n" +
"\n" +
"coalesce((select sum (check_value+return_commission) from returned_checks where customer_id_fk=" + customer_id + " and accounted=false),0)\n" +
"\n" +
"as result\n" +
"";
                System.out.println(stm);
                r = conn_obj.conn_exec(stm);
                r.next();
                return (Math.round(r.getFloat(1)*100)/100.0f);
            } catch (SQLException ex) {
                Joptionpane_message(ex.getMessage() + ex.getSQLState());
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Joptionpane_message(ex.getMessage());
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);

            }
            return 0;
        }
    }

    public void show_all_customer_movements() {
        if (jPanel1.isVisible()) {
            try {
                String vendor = jComboBox3.getSelectedItem().toString();
                r = conn_obj.conn_exec("select  0.0 as المجموع,القيمة,ملاحظة,الحركة ,المكان, التاريخ ,رقم_القيد   ,رقم_الفاتورة  from(\n"
                        + "SELECT round((bill_value::numeric),2)::float8 as القيمة,record_time,bill_location_id,location.location_id,bill_note as ملاحظة,'فاتورة' as الحركة,location.location_name as المكان,bill_date as التاريخ,bill_id as رقم_القيد,bill_num as رقم_الفاتورة\n"
                        + "FROM customer_bills,location\n"
                        + "           where bill_customer_id=(select customer_id as cus_name from customers where customer_name='" + vendor + "')  and bill_location_id=location_id\n"
                        + "                        UNION\n"
                        + "SELECT round((return_bill_value::numeric),2)::float8 as القيمة,record_time,return_bill_location_id,location.location_id,return_bill_note as ملاحظة,'م-مبيعات' as الحركة,location.location_name as المكان,return_bill_date as التاريخ,return_bill_id as رقم_القيد,return_bill_num as رقم_الفاتورة\n"
                        + "FROM return_customer_bills,location\n"
                        + "           where return_bill_customer_id=(select customer_id as cus_name from customers where customer_name='" + vendor + "')  and return_bill_location_id=location_id\n"
                        + "                        UNION\n"
                        + "                      \n"
                        + "SELECT round((payment_value::numeric),2)::float8,record_time,0 ,null,payment_note,'دفعة','---',payment_date,payment_id,'---'\n"
                        + "                        FROM customer_payments,location \n"
                        + "                        where customer_id_fk=(select customer_id as cus_name from customers where customer_name='" + vendor + "')  \n"
                        + "                        UNION\n"
                        + "                      \n"
                        + "SELECT round((discount_value::numeric),2)::float8,record_time,0 ,null,discount_note,'خصم','---',discount_date,discount_id,'---'\n"
                        + "                        FROM customer_discount \n"
                        + "                        where customer_id_fk=(select customer_id as cus_name from customers where customer_name='" + vendor + "')  "
                        + ""
                        + "                        UNION\n"
                        + "                      \n"
                        + "SELECT round(((check_value+return_commission)::numeric),2)::float8,record_time,0 ,null,return_note,'شك-مرتجع','---',return_date,id,'---'\n"
                        + "                        FROM returned_checks \n"
                        + "                        where customer_id_fk=(select customer_id as cus_name from customers where customer_name='" + vendor + "') "
                        + ""
                        + "order by التاريخ, record_time\n"
                        + ") as ffff");
                jTable3.setModel(DbUtils.resultSetToTableModel(r));
                render_table3();
                if (jTable3.getRowCount() != 0) {
                    jTable3.setValueAt(0.0, 0, 0);
                }
                for (int i = 0; i < jTable3.getRowCount(); i++) {
                    if (i == 0) {
                        if (jTable3.getValueAt(i, 3).equals("فاتورة")) {
                            float value = Float.parseFloat(jTable3.getModel().getValueAt(i, 1).toString());
                            jTable3.setValueAt(String.format("%.2f", value), i, 0);
                        } else if (jTable3.getValueAt(i, 3).equals("دفعة")) {
                            float value = -1 * Float.parseFloat(jTable3.getModel().getValueAt(i, 1).toString());
                            jTable3.setValueAt(String.format("%.2f", value), i, 0);
                        } else if (jTable3.getValueAt(i, 3).equals("خصم")) {
                            float value = -1 * Float.parseFloat(jTable3.getModel().getValueAt(i, 1).toString());
                            jTable3.setValueAt(String.format("%.2f", value), i, 0);
                        }else if (jTable3.getValueAt(i, 3).equals("م-مبيعات")) {
                            float value = -1 * Float.parseFloat(jTable3.getModel().getValueAt(i, 1).toString());
                            jTable3.setValueAt(String.format("%.2f", value), i, 0);
                        }
                        else if (jTable3.getValueAt(i, 3).equals("شك-مرتجع")) {
                            float value = Float.parseFloat(jTable3.getModel().getValueAt(i, 1).toString());
                            jTable3.setValueAt(String.format("%.2f", value), i, 0);
                        }
                    } else {
                        if (jTable3.getValueAt(i, 3).equals("فاتورة")) {
                            float pre_value = Float.parseFloat(jTable3.getModel().getValueAt(i - 1, 0).toString());
                            float value2 = Float.parseFloat(jTable3.getModel().getValueAt(i, 1).toString());
                            jTable3.setValueAt(String.format("%.2f", pre_value + value2), i, 0);
                        } else if (jTable3.getValueAt(i, 3).equals("دفعة")) {
                            float pre_value = Float.parseFloat(jTable3.getModel().getValueAt(i - 1, 0).toString());
                            float value2 = Float.parseFloat(jTable3.getModel().getValueAt(i, 1).toString());
                            jTable3.setValueAt(String.format("%.2f", pre_value - value2), i, 0);
                        }else if (jTable3.getValueAt(i, 3).equals("خصم")) {
                            float pre_value = Float.parseFloat(jTable3.getModel().getValueAt(i - 1, 0).toString());
                            float value2 = Float.parseFloat(jTable3.getModel().getValueAt(i, 1).toString());
                            jTable3.setValueAt(String.format("%.2f", pre_value - value2), i, 0);
                        } else if (jTable3.getValueAt(i, 3).equals("م-مبيعات")) {
                            float pre_value = Float.parseFloat(jTable3.getModel().getValueAt(i - 1, 0).toString());
                            float value2 = Float.parseFloat(jTable3.getModel().getValueAt(i, 1).toString());
                            jTable3.setValueAt(String.format("%.2f", pre_value - value2), i, 0);
                        }
                        else if (jTable3.getValueAt(i, 3).equals("شك-مرتجع")) {
                            float pre_value = Float.parseFloat(jTable3.getModel().getValueAt(i - 1, 0).toString());
                            float value2 = Float.parseFloat(jTable3.getModel().getValueAt(i, 1).toString());
                            jTable3.setValueAt(String.format("%.2f", pre_value + value2), i, 0);
                        }
                    }

                }
            } catch (Exception ex) {
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    String get_date() {
        return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }

    public void reset_jbanel_5() {
        /*
         jComboBox4.setSelectedIndex(0);
         jTextField11.setText("0");
         jTextField12.setText("");
         jTextField13.setText("");
         jTextField14.setText("");
         //jComboBox7.setSelectedIndex(0);
         jLabel38.setText("الرصيد = ");
         jTextArea2.setText("");
         jDateChooser2.setDate(get_date_jdate());
         jLabel52.setIcon(null);
        
        
         if(jTabbedPane3.getSelectedIndex()==1){
         jTextField19.setText("");
         jTextField20.setText("");
         jTextField21.setText("");
         jTextField22.setText("");
         jTextField23.setText("");
         jTextField24.setText("");
         jTextField25.setText("");
         jTextField26.setText("");
         jTextArea3.setText("");
         jDateChooser4.setDate(get_date_jdate());
         jDateChooser5.setDate(get_date_jdate());
         jLabel44.setIcon(null);
         }
         */
    }

    public void delete_bill_or_payment(String bill_or_payment, String id) throws SQLException {
        int id_int = Integer.parseInt(id.trim());
        /////////////////
        if (bill_or_payment.equals("فاتورة")) {
            try {
                
                /////////////////////////////للتأكد من ان القيد المحذوف ليس مصفر//////////////////////////////////////////////
                r = conn_obj.conn_exec("select accounted from customer_bills where bill_id=" + id_int + "");
                r.next();
                boolean x = r.getBoolean(1);
                if (x == true) {
                    throw new Exception("لا يمكن حذف قيد من حساب مصفّر");
                }
                ///////////////////////////////////////////////////////////////////////////////////////////////////////
                
                conn_obj.get_con().setAutoCommit(false);
                /////
                //ارجاع الكميات الى المخازن
                        r = conn_obj.conn_exec("select * from  customer_bills where bill_id=" + id_int + "");
                        r.next();
int bill_loc_id=r.getInt("bill_location_id");
            String update_stm_inventory="";
            String stm_exec="";
            r = conn_obj.conn_exec("select * from  customer_bills_items where bill_id=" + id_int + "");
                        while(r.next())
                        {
                stm_exec = "select items.item_relations.item_id,\n"
                        + "items.item_relations.item_unit,\n"
                        + "items.item_relations.item_relation*" + r.getInt("item_quantity") + " as quantity\n"
                        + "\n"
                        + "from items.item_relations\n"
                        + "\n"
                        + "where \n"
                        + "items.item_relations.item_id=" +r.getInt("item_id") + "  AND\n"
                        + "items.item_relations.item_unit=" + r.getInt("item_unit") + ";";
                
                           
            //    System.out.println(stm_exec);
                r2 = conn_obj2.conn_exec(stm_exec);
                r2.next();
                double quantity = r2.getDouble("quantity");
                int item_id =r2.getInt("item_id");
                String store_to_update = "store_id_" + bill_loc_id;//store_id_1
                update_stm_inventory+= "update items.inventory set " + store_to_update + " = " + store_to_update + " + " + quantity + " where item_id=" + item_id + ";";
                
                        }
           
            conn_obj.exec(update_stm_inventory);
                //انتهي ارالرجاع للمخازن الكميات
                
                conn_obj.get_st().execute("delete from customer_bills where bill_id = " + id_int + " and accounted=false");
                Joptionpane_confirm("هل انت متأكد من حذف الفاتورة", "لم يتم حذف الفاتورة");
                conn_obj.get_con().commit();
                Joptionpane_message("تم الحذف");
                create_customer_account_table();
                write_to_file(get_date() + "  تم حذف فاتورة");
            } catch (Exception e) {
                Joptionpane_message(e.getMessage());
                conn_obj.get_con().rollback();
            }
        } else if (bill_or_payment.equals("دفعة")) {
            try {
                /////////////////////////////للتأكد من ان القيد المحذوف ليس مصفر//////////////////////////////////////////////
                r = conn_obj.conn_exec("select accounted from customer_payments where payment_id=" + id_int + "");
                r.next();
                boolean x = r.getBoolean(1);
                if (x == true) {
                    throw new Exception("لا يمكن حذف قيد من حساب مصفّر");
                }
                ////////////////////////////////////////////////////////////////////////////////////
                conn_obj.get_con().setAutoCommit(false);
                conn_obj.get_st().execute("delete from customer_payments where payment_id = " + id_int + " and accounted=false");
                Joptionpane_confirm("هل انت متأكد من حذف الدفعة", "لم يتم حذف الدفعة");
                conn_obj.get_con().commit();
                Joptionpane_message("تم الحذف");
                create_customer_account_table();
                write_to_file(get_date() + "  تم حذف دفعة");
            } catch (Exception e) {
                Joptionpane_message(e.getMessage());
                conn_obj.get_con().rollback();
            }
            
        }
        else if (bill_or_payment.equals("م-مبيعات")) {
            try {
                /////////////////////////////للتأكد من ان القيد المحذوف ليس مصفر//////////////////////////////////////////////
                r = conn_obj.conn_exec("select accounted from return_customer_bills where return_bill_id=" + id_int + "");
                r.next();
                boolean x = r.getBoolean(1);
                if (x == true) {
                    throw new Exception("لا يمكن حذف قيد من حساب مصفّر");
                }
                ////////////////////////////////////////////////////////////////////////////////////
                conn_obj.get_con().setAutoCommit(false);
                //ارجاع الكميات الى المخازن
                        r = conn_obj.conn_exec("select * from  return_customer_bills where return_bill_id=" + id_int + "");
                        r.next();
int bill_loc_id=r.getInt("return_bill_location_id");
            String update_stm_inventory="";
            String stm_exec="";
            r = conn_obj.conn_exec("select * from  return_customer_bills_items where return_bill_id=" + id_int + "");
                        while(r.next())
                        {
                stm_exec = "select items.item_relations.item_id,\n"
                        + "items.item_relations.item_unit,\n"
                        + "items.item_relations.item_relation*" + r.getInt("return_item_quantity") + " as quantity\n"
                        + "\n"
                        + "from items.item_relations\n"
                        + "\n"
                        + "where \n"
                        + "items.item_relations.item_id=" +r.getInt("return_item_id") + "  AND\n"
                        + "items.item_relations.item_unit=" + r.getInt("return_item_unit") + ";";
                
                           
                r2 = conn_obj2.conn_exec(stm_exec);
                r2.next();
                double quantity = r2.getDouble("quantity")*-1;
                int item_id =r2.getInt("item_id");
                String store_to_update = "store_id_" + bill_loc_id;//store_id_1
                update_stm_inventory+= "update items.inventory set " + store_to_update + " = " + store_to_update + " + " + quantity + " where item_id=" + item_id + ";";
                
                        }
           
            conn_obj.exec(update_stm_inventory);
                //انتهي ارالرجاع للمخازن الكميات
                
                conn_obj.get_st().execute("delete from return_customer_bills where return_bill_id = " + id_int + " and accounted=false");
                Joptionpane_confirm("هل انت متأكد من حذف الدفعة", "لم يتم حذف الدفعة");
                conn_obj.get_con().commit();
                Joptionpane_message("تم الحذف");
                create_customer_account_table();
                write_to_file(get_date() + "  تم حذف دفعة");
            } catch (Exception e) {
                Joptionpane_message(e.getMessage());
                conn_obj.get_con().rollback();
            }
        }
        else if (bill_or_payment.equals("خصم")) {
            try {
                /////////////////////////////للتأكد من ان القيد المحذوف ليس مصفر//////////////////////////////////////////////
                r = conn_obj.conn_exec("select accounted from customer_discount where discount_id=" + id_int + "");
                r.next();
                boolean x = r.getBoolean(1);
                if (x == true) {
                    throw new Exception("لا يمكن حذف قيد من حساب مصفّر");
                }
                ////////////////////////////////////////////////////////////////////////////////////
                conn_obj.get_con().setAutoCommit(false);
                conn_obj.get_st().execute("delete from customer_discount where discount_id = " + id_int + " and accounted=false");
                Joptionpane_confirm("هل انت متأكد من حذف الدفعة", "لم يتم حذف الدفعة");
                conn_obj.get_con().commit();
                Joptionpane_message("تم الحذف");
                create_customer_account_table();
                write_to_file(get_date() + "  تم حذف خصم");
            } catch (Exception e) {
                Joptionpane_message(e.getMessage());
                conn_obj.get_con().rollback();
            }
        }
        else if (bill_or_payment.equals("شك-مرتجع")) {
            try {
                /////////////////////////////للتأكد من ان القيد المحذوف ليس مصفر//////////////////////////////////////////////
                r = conn_obj.conn_exec("select accounted from returned_checks where id=" + id_int + "");
                r.next();
                boolean x = r.getBoolean(1);
                if (x == true) {
                    throw new Exception("لا يمكن حذف قيد من حساب مصفّر");
                }
                ////////////////////////////////////////////////////////////////////////////////////
                conn_obj.get_con().setAutoCommit(false);
                conn_obj.get_st().execute("delete from returned_checks where id = " + id_int + " and accounted=false");
                Joptionpane_confirm("هل انت متأكد من حذف الشك", "لم يتم حذف الشك");
                conn_obj.get_con().commit();
                Joptionpane_message("تم الحذف");
                create_customer_account_table();
                write_to_file(get_date() + "  تم حذف الشك");
            } catch (Exception e) {
                Joptionpane_message(e.getMessage());
                conn_obj.get_con().rollback();
            }
        }
    }
public void delete_vendor_bill_or_payment(String bill_or_payment, String id) throws SQLException {
        int id_int = Integer.parseInt(id.trim());
        /////////////////
        if (bill_or_payment.equals("فاتورة")) {
            try {
                /////////////////////////////للتأكد من ان القيد المحذوف ليس مصفر//////////////////////////////////////////////
                r = conn_obj.conn_exec("select accounted from vendor_bills where bill_id=" + id_int + "");
                r.next();
                boolean x = r.getBoolean(1);
                if (x == true) {
                    throw new Exception("لا يمكن حذف قيد من حساب مصفّر");
                }
                ///////////////////////////////////////////////////////////////////////////////////////////////////////
                conn_obj.get_con().setAutoCommit(false);
                /////
                //ارجاع الكميات الى المخازن
                        r = conn_obj.conn_exec("select * from  vendor_bills where bill_id=" + id_int + "");
                        r.next();
int bill_loc_id=r.getInt("bill_location_id");
            String update_stm_inventory="";
            String stm_exec="";
            r = conn_obj.conn_exec("select * from  vendor_bills_items where bill_id=" + id_int + "");
                        while(r.next())
                        {
                stm_exec = "select items.item_relations.item_id,\n"
                        + "items.item_relations.item_unit,\n"
                        + "items.item_relations.item_relation*" + r.getFloat("item_quantity")+ r.getFloat("item_bonus")+ " as quantity\n"
                        + "\n"
                        + "from items.item_relations\n"
                        + "\n"
                        + "where \n"
                        + "items.item_relations.item_id=" +r.getInt("item_id") + "  AND\n"
                        + "items.item_relations.item_unit=" + r.getInt("item_unit") + ";";
                
                           
              //  System.out.println(stm_exec);
                r2 = conn_obj2.conn_exec(stm_exec);
                r2.next();
                double quantity = r2.getDouble("quantity")*-1;
                int item_id =r2.getInt("item_id");
                String store_to_update = "store_id_" + bill_loc_id;//store_id_1
                update_stm_inventory+= "update items.inventory set " + store_to_update + " = " + store_to_update + " + " + quantity + " where item_id=" + item_id + ";";
                
                        }
           
            conn_obj.exec(update_stm_inventory);
                //انتهي ارالرجاع للمخازن الكميات
                
                conn_obj.get_st().execute("delete from vendor_bills where bill_id = " + id_int + " and accounted=false");
                Joptionpane_confirm("هل انت متأكد من حذف الفاتورة", "لم يتم حذف الفاتورة");
                conn_obj.get_con().commit();
                Joptionpane_message("تم الحذف");
                create_ven_account_table();
                write_to_file(get_date() + "  تم حذف فاتورة");
            } catch (Exception e) {
                Joptionpane_message(e.getMessage());
                conn_obj.get_con().rollback();
            }
        } else if (bill_or_payment.equals("دفعة")) {
            try {
                /////////////////////////////للتأكد من ان القيد المحذوف ليس مصفر//////////////////////////////////////////////
                r = conn_obj.conn_exec("select accounted from vendor_payments where payment_id=" + id_int + "");
                r.next();
                boolean x = r.getBoolean(1);
                if (x == true) {
                    throw new Exception("لا يمكن حذف قيد من حساب مصفّر");
                }
                ////////////////////////////////////////////////////////////////////////////////////
                conn_obj.get_con().setAutoCommit(false);
                conn_obj.get_st().execute("delete from vendor_payments where payment_id = " + id_int + " and accounted=false");
                Joptionpane_confirm("هل انت متأكد من حذف الدفعة", "لم يتم حذف الدفعة");
                conn_obj.get_con().commit();
                Joptionpane_message("تم الحذف");
                create_ven_account_table();
                write_to_file(get_date() + "  تم حذف دفعة");
            } catch (Exception e) {
                Joptionpane_message(e.getMessage());
                conn_obj.get_con().rollback();
            }
            
        }
        else if (bill_or_payment.equals("ف-مرتجع")) {
            try {
                /////////////////////////////للتأكد من ان القيد المحذوف ليس مصفر//////////////////////////////////////////////
                r = conn_obj.conn_exec("select accounted from return_vendor_bills where return_bill_id=" + id_int + "");
                r.next();
                boolean x = r.getBoolean(1);
                if (x == true) {
                    throw new Exception("لا يمكن حذف قيد من حساب مصفّر");
                }
                ////////////////////////////////////////////////////////////////////////////////////
                conn_obj.get_con().setAutoCommit(false);
                 //ارجاع الكميات الى المخازن
                        r = conn_obj.conn_exec("select * from  return_vendor_bills where return_bill_id=" + id_int + "");
                        r.next();
int bill_loc_id=r.getInt("return_bill_location_id");
            String update_stm_inventory="";
            String stm_exec="";
            r = conn_obj.conn_exec("select * from  return_vendor_bills_items where return_bill_id=" + id_int + "");
                        while(r.next())
                        {
                stm_exec = "select items.item_relations.item_id,\n"
                        + "items.item_relations.item_unit,\n"
                        + "items.item_relations.item_relation*" + r.getInt("return_item_quantity") + " as quantity\n"
                        + "\n"
                        + "from items.item_relations\n"
                        + "\n"
                        + "where \n"
                        + "items.item_relations.item_id=" +r.getInt("return_item_id") + "  AND\n"
                        + "items.item_relations.item_unit=" + r.getInt("return_item_unit") + ";";
                
                r2 = conn_obj2.conn_exec(stm_exec);
                r2.next();
                double quantity = r2.getDouble("quantity");
                int item_id =r2.getInt("item_id");
                String store_to_update = "store_id_" + bill_loc_id;//store_id_1
                update_stm_inventory+= "update items.inventory set " + store_to_update + " = " + store_to_update + " + " + quantity + " where item_id=" + item_id + ";";
                
                        }
           
            conn_obj.exec(update_stm_inventory);
                //انتهي ارالرجاع للمخازن الكميات
                
                conn_obj.get_st().execute("delete from return_vendor_bills where return_bill_id = " + id_int + " and accounted=false");
                Joptionpane_confirm("هل انت متأكد من حذف الدفعة", "لم يتم حذف الدفعة");
                conn_obj.get_con().commit();
                Joptionpane_message("تم الحذف");
                create_ven_account_table();
                write_to_file(get_date() + "  تم حذف دفعة");
            } catch (Exception e) {
                Joptionpane_message(e.getMessage());
                conn_obj.get_con().rollback();
            }
        }
        else if (bill_or_payment.equals("خصم")) {
            try {
                /////////////////////////////للتأكد من ان القيد المحذوف ليس مصفر//////////////////////////////////////////////
                r = conn_obj.conn_exec("select accounted from vendor_discount where discount_id=" + id_int + "");
                r.next();
                boolean x = r.getBoolean(1);
                if (x == true) {
                    throw new Exception("لا يمكن حذف قيد من حساب مصفّر");
                }
                ////////////////////////////////////////////////////////////////////////////////////
                conn_obj.get_con().setAutoCommit(false);
                conn_obj.get_st().execute("delete from vendor_discount where discount_id = " + id_int + " and accounted=false");
                Joptionpane_confirm("هل انت متأكد من حذف الدفعة", "لم يتم حذف الدفعة");
                conn_obj.get_con().commit();
                Joptionpane_message("تم الحذف");
                create_ven_account_table();
                write_to_file(get_date() + "  تم حذف خصم");
            } catch (Exception e) {
                Joptionpane_message(e.getMessage());
                conn_obj.get_con().rollback();
            }
        }
    }


    public void open_the_bill_picture() {
        if (jComboBox3.getSelectedIndex() != 0)//لحتى ما تعمل اكسبشن لما يختار اشي على الجدول بدون ما يكون فيندور محدد
        {
            try {
                String id = jTable3.getValueAt(jTable3.getSelectedRow(), 6).toString();
                String path = "D:\\Dropbox\\dist\\payments_images\\";
                String fileName = path + id + ".jpg";

                String[] commands = {
                    "cmd.exe", "/c", "start", "\"DummyTitle\"", "\"" + fileName + "\""
                };

                Process p = Runtime.getRuntime().exec(commands);
                p.waitFor();

            } catch (IOException ex) {
                Joptionpane_message(ex.getMessage());
            } catch (InterruptedException ex) {
                Joptionpane_message(ex.getMessage());
            }
        }
    }
String movement_name_to_print="";
    public void show_bill_items(String move_id,String move_name) {
    
//اذا الحركة فاتورة
        if (move_name.equals("فاتورة"))//لحتى ما تعمل اكسبشن لما يختار اشي على الجدول بدون ما يكون فيندور محدد
        {
            
            try {
                movement_name_to_print="any_thing";
                String bill_id = move_id;
                r = conn_obj.conn_exec("select discount_ratio,discount_amount,round(CAST(bill_value as numeric),1)as bill,bill_note,bill_date  from  customer_bills where bill_id=" + bill_id + "");
                r.next();
                String bill_date = "تاريخ الفاتورة :  " + r.getString("bill_date");
                String dis_ratio = "نسبة خصم الفاتورة بالمئة = " + r.getString("discount_ratio") + " %";
                String dis_value = "قيمة الخصم المباشر على الفاتورة = " + r.getString("discount_amount");
                String bill_value = "قيمة الفاتورة النهائي يساوي =  " + r.getString("bill");
                String bill_note = "ملاحظات =  " + r.getString("bill_note");
                /////////////////////////////////////////////////// show bill items in jtable////////////
                r = conn_obj.conn_exec("select item_note as ملاحظة ,(SELECT round(CAST(price*item_quantity as numeric),1) as المبلغ),price as السعر_بعد,discount_ratio as نسبة_الخصم,item_price as السعر,item_bonus as بونص,item_quantity as الكمية,unit_name as الوحدة,item_name as اسم_الصنف from ("
                        + "SELECT \n"
                        + "items.main_items.item_name,\n"
                        + "items.main_items.item_id,\n"
                        + "\n"
                        + "items.item_units.unit_name,\n"
                        + "items.item_units.unit_id,\n"
                        + "\n"
                        + "customer_bills_items.id,customer_bills_items.item_id,customer_bills_items.item_bonus,\n"
                        + "customer_bills_items.item_note,customer_bills_items.item_unit,\n"
                        + "customer_bills_items.item_price,customer_bills_items.item_quantity,\n"
                        + "customer_bills_items.bill_id,customer_bills_items.discount_ratio,\n"
                        + "(1-customer_bills_items.discount_ratio/100)*customer_bills_items.item_price as price\n"
                        + "FROM items.main_items,items.item_units,customer_bills_items\n"
                        + "WHERE\n"
                        + "items.main_items.item_id = customer_bills_items.item_id\n"
                        + "AND\n"
                        + "items.item_units.unit_id = customer_bills_items.item_unit\n"
                        + "AND\n"
                        + "customer_bills_items.bill_id=" + bill_id + " order by customer_bills_items.id)as dvfc");

                jPanel14.setVisible(true);
                
                customer_bill.pack();
                jTable6.getColumnModel().getColumn(2).setCellRenderer(new CustomTableCellRenderer());
                jTable6.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                jTable6.getColumnModel().getColumn(1).setPreferredWidth(150);
                jLabel31.setText(dis_ratio);
                jLabel32.setText(dis_value);
                jLabel33.setText(bill_value);
                jLabel39.setText(bill_date);
                jLabel57.setText(bill_note);
                //
                jTextField20.setText("");
                r2 = conn_obj2.conn_exec("select * from bill_note_to_print where bill_id_fk= " + bill_id + "");
                if (r2.next()) {
                   jTextField20.setText(r2.getString(3));
                } 
                //
                jTable6.getTableHeader().setFont(new Font("arial", Font.BOLD, 20));

                jTable6.setModel(DbUtils.resultSetToTableModel(r));

                jTable6.getColumnModel().getColumn(8).setMinWidth(200);
                int width = (int) get_screen_dimension().getWidth() - 80;
                int height = (int) get_screen_dimension().getHeight() - 100;
                customer_bill.setSize(width, height);
                customer_bill.setLocationRelativeTo(this);
                customer_bill.setVisible(true);
                jButton20.requestFocus();

            } catch (Exception ex) {
                Joptionpane_message(ex.getMessage());
            }
        } //اذا الحركة دفعة يتغير 
        else if (move_name.equals("دفعة")) {

            try {
                String payment_id = move_id;
                jframe_to_modify_payment = new modify_customer_payment(this, payment_id);
                r = conn_obj.conn_exec("select * from  customer_payments where payment_id=" + payment_id + "");
                r.next();

                float hole_payment_value = r.getFloat("payment_value");

                jframe_to_modify_payment.jComboBox4.setSelectedItem(jComboBox3.getSelectedItem());
                jframe_to_modify_payment.jDateChooser2.setDate(r.getDate("payment_date"));
                jframe_to_modify_payment.jTextField14.setText(r.getString("payment_rec"));
                jframe_to_modify_payment.jTextField19.setText(r.getString("payment_maker"));
                jframe_to_modify_payment.jTextField13.setText(r.getString("voucher_no"));
                jframe_to_modify_payment.jTextArea2.setText(r.getString("payment_note"));
                //
                r = conn_obj.conn_exec("select sum(check_value) from  customer_checks where check_payment_id=" + payment_id + "");
                r.next();
                float sum_of_checks = r.getFloat("sum");
                float net_of_cash_payments = hole_payment_value - sum_of_checks;
                jframe_to_modify_payment.jTextField12.setText(Float.toString(net_of_cash_payments));
                //

                r = conn_obj.conn_exec("select check_owner,check_endorser,check_no,check_due_date,check_note,check_value,check_bank from  customer_checks where check_payment_id=" + payment_id + "");
                while (r.next()) {
                    DefaultTableModel tm = (DefaultTableModel) jframe_to_modify_payment.jTable13.getModel();
                    tm.addRow(new Object[]{jframe_to_modify_payment.jTable13.getRowCount() + 1, r.getString("check_no"), r.getFloat("check_value"), r.getString("check_owner"), r.getString("check_endorser"), r.getString("check_due_date"), r.getString("check_bank"), r.getString("check_note")});
                }
                jframe_to_modify_payment.jTextField13.setEditable(false);
                jframe_to_modify_payment.jTextField12.setEditable(false);
                jframe_to_modify_payment.jTextField11.setEditable(false);
                jframe_to_modify_payment.jTextField14.setEditable(false);
                jframe_to_modify_payment.jTextField19.setEditable(false);
                jframe_to_modify_payment.jDateChooser2.setEnabled(false);
                jframe_to_modify_payment.jTextArea2.setEditable(false);
                jframe_to_modify_payment.jTable13.setEnabled(false);
                jframe_to_modify_payment.jComboBox4.setEnabled(false);
                jframe_to_modify_payment.jButton5.setVisible(false);
                jframe_to_modify_payment.jButton28.setVisible(false);
                jframe_to_modify_payment.jButton8.setVisible(false);
                jframe_to_modify_payment.jButton12.setVisible(false);
                jframe_to_modify_payment.setLocationRelativeTo(this);
                jframe_to_modify_payment.setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        //اذا الحركة فاتورة ارجاع
        else if ( move_name.equals("م-مبيعات"))//لحتى ما تعمل اكسبشن لما يختار اشي على الجدول بدون ما يكون فيندور محدد
        {
            try {
                //movement_name_to_print="ف-مرتجع";
                String bill_id = move_id;
                r = conn_obj.conn_exec("select round(CAST(return_bill_value as numeric),1)as bill,return_bill_note,return_bill_date  from  return_customer_bills where return_bill_id=" + bill_id + "");
                r.next();
                String bill_date = "تاريخ الفاتورة :  " + r.getString("return_bill_date");
                String bill_value = "قيمة الفاتورة النهائي يساوي =  " + r.getString("bill");
                String bill_note = "ملاحظات =  " + r.getString("return_bill_note");
                /////////////////////////////////////////////////// show bill items in jtable////////////
                r = conn_obj.conn_exec("select return_item_note as ملاحظة ,(SELECT round(CAST(price*return_item_quantity as numeric),1) as المبلغ),return_item_price as السعر,return_item_quantity as الكمية,unit_name as الوحدة,item_name as اسم_الصنف from ("
                        + "SELECT \n"
                        + "items.main_items.item_name,\n"
                        + "items.main_items.item_id,\n"
                        + "\n"
                        + "items.item_units.unit_name,\n"
                        + "items.item_units.unit_id,\n"
                        + "\n"
                        + "return_customer_bills_items.id,return_customer_bills_items.return_item_id,\n"
                        + "return_customer_bills_items.return_item_note,return_customer_bills_items.return_item_unit,\n"
                        + "return_customer_bills_items.return_item_price,return_customer_bills_items.return_item_quantity,\n"
                        + "return_customer_bills_items.return_bill_id,\n"
                        + "return_customer_bills_items.return_item_price as price\n"
                        + "FROM items.main_items,items.item_units,return_customer_bills_items\n"
                        + "WHERE\n"
                        + "items.main_items.item_id = return_customer_bills_items.return_item_id\n"
                        + "AND\n"
                        + "items.item_units.unit_id = return_customer_bills_items.return_item_unit\n"
                        + "AND\n"
                        + "return_customer_bills_items.return_bill_id=" + bill_id + " order by return_customer_bills_items.id)as dvfc");

                jPanel14.setVisible(true);

                customer_bill.pack();
                jTable6.getColumnModel().getColumn(2).setCellRenderer(new CustomTableCellRenderer());
                jTable6.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                jTable6.getColumnModel().getColumn(1).setPreferredWidth(150);
                jLabel31.setText("");
                jLabel32.setText("");
                jLabel33.setText(bill_value);
                jLabel39.setText(bill_date);
                jLabel57.setText(bill_note);

                jTable6.getTableHeader().setFont(new Font("arial", Font.BOLD, 20));

                jTable6.setModel(DbUtils.resultSetToTableModel(r));

                jTable6.getColumnModel().getColumn(5).setMinWidth(200);
                int width = (int) get_screen_dimension().getWidth() - 80;
                int height = (int) get_screen_dimension().getHeight() - 100;
                customer_bill.setSize(width, height);
                customer_bill.setLocationRelativeTo(this);
                customer_bill.setVisible(true);
                jButton20.requestFocus();

            } catch (Exception ex) {
                Joptionpane_message(ex.getMessage());
            }
        }
        //اذا الحركة شك مرتجع
        else if (move_name.equals("شك-مرتجع"))//لحتى ما تعمل اكسبشن لما يختار اشي على الجدول بدون ما يكون فيندور محدد
        {
          try {
            String check_id = move_id;
            returned_checks re = new returned_checks(check_id,this);
            r = conn_obj.conn_exec("select * from  returned_checks where id=" + check_id + "");
            r.next();
            String customer_check_id = r.getString("check_id_fk");
            re.jTextField1.setText(r.getString("check_id_fk"));
            re.jTextField3.setText(r.getString("check_value"));
            re.jDateChooser1.setDate(r.getDate("return_date"));
            re.jTextArea1.setText(r.getString("return_note"));
            re.jTextField6.setText(Float.toString(r.getFloat("return_commission")));
            re.jTextField2.setText(jComboBox3.getSelectedItem().toString());
            r = conn_obj.conn_exec("select check_owner,check_no from  customer_checks where id=" + customer_check_id + "");
            r.next();
            re.jTextField4.setText(r.getString("check_owner"));
            re.jTextField5.setText(r.getString("check_no"));
            re.jButton1.hide();
            re.jButton2.hide();
            re.setLocationRelativeTo(this);
            re.setVisible(true);
          }catch(Exception e)
          {
              
          }
        }
        
    }

    public void right_alighment() {

        jTabbedPane1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
//        jTabbedPane3.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

    }

    float prepare_bill() throws Exception {
        float bill_amount = 0;
        try {
            jTable4.editingCanceled(null);//هذا اذا نسي المدخل خانات مفتوحة اي مفتوحة للتعديل لا يتعامل معها على انها فارغة فيخرج منها قبل ادخال البيانات
            int row = jTable4.getRowCount();
            float sum = 0;
            float price;//price
            float quantity;//quantity
            float bonus;//bonus
            float item_discount;
            float bill_discount_ratio = 0;
            float bill_discount_value = 0;

            for (int i = 0; i < row; i++) {

                //here to make sure the item is not null... if item null the function doesnt work on that row and skip on
                if (jTable4.getModel().getValueAt(i, 0) != null && !jTable4.getModel().getValueAt(i, 0).toString().trim().equals("")) {
                    price = Float.parseFloat(jTable4.getModel().getValueAt(i, 4).toString());
                    quantity = Float.parseFloat(jTable4.getModel().getValueAt(i, 2).toString());
                    bonus = Float.parseFloat(jTable4.getModel().getValueAt(i, 3).toString());
                    item_discount = Float.parseFloat(jTable4.getModel().getValueAt(i, 5).toString());
                    System.out.println(price+quantity);
                    if (price <= 0) {
                        throw new Exception("خطأ في مدخلات الجدول السعر اقل او يساوي صفر ");
                    }
                    if (item_discount < 0 || item_discount > 100) {
                        throw new Exception("خطأ في مدخلات الجدول الخصم اقل من  صفر ");
                    }
                    if (quantity < 0 || bonus < 0)//price&quantity not equal - & <0
                    {
                        throw new Exception("خطأ في مدخلات الجدول قيمة صفر او سالب ");
                    }
                    if (quantity == 0 && bonus == 0)//الصنف لا هو كمية ولا هو بونص
                    {
                        throw new Exception("الصنف لا هو كمية ولا هو بونص");
                    }
                    sum = sum + (price * quantity * (1 - item_discount / 100));
                    float item_sum = (price * quantity* (1 - item_discount / 100));
                    //////////لتنجب عمل تغييرات في الجدول غير مفيدة وبالتالي اي تغيير يعيد عملة المستمع وتصبح العملية غير منتهية  
                    if (jTable4.getModel().getValueAt(i, 6) == null) {
                        jTable4.getModel().setValueAt(item_sum, i, 6);
                    } else if (item_sum != Float.parseFloat(jTable4.getModel().getValueAt(i, 6).toString())) {
                        jTable4.getModel().setValueAt(item_sum, i, 6);
                    }
                    //////////
                }
            }
            bill_discount_ratio = Float.parseFloat(jTextField3.getText().trim());
            if (bill_discount_ratio < 0 || bill_discount_ratio >= 100) {
                throw new Exception("خطأ بنسبة الخصم");
            }
            bill_discount_value = Float.parseFloat(jTextField16.getText().trim());
            if (bill_discount_value < 0) {
                throw new Exception("خطأ بقيمة خصم الفاتورة");
            }

            bill_amount = sum * (1 - bill_discount_ratio / 100) - bill_discount_value;
            //jTextField1.setText(Float.toString(bill_amount));
            jTextField17.setText(Float.toString(sum));

            if (bill_discount_value > bill_amount) {
                throw new Exception("خطأ بقيمة خصم الفاتورة");
            }

        } catch (Exception e) {
            Joptionpane_message(e.toString() + "\n" + "خطأأأأأأأأ");
            jTextField1.setText("0.0");

        }
        
        return bill_amount;
    }
float prepare_ven_bill() throws Exception {
        float bill_amount = 0;
        try {
            jTable_bill_items.editingCanceled(null);//هذا اذا نسي المدخل خانات مفتوحة اي مفتوحة للتعديل لا يتعامل معها على انها فارغة فيخرج منها قبل ادخال البيانات
            int row = jTable_bill_items.getRowCount();
            float sum = 0;
            float price;//price
            float quantity;//quantity
            float bonus;//bonus
            float item_discount;
            float bill_discount_ratio = 0;
            float bill_discount_value = 0;

            for (int i = 0; i < row; i++) {

                //here to make sure the item is not null... if item null the function doesnt work on that row and skip on
                if (jTable_bill_items.getModel().getValueAt(i, 0) != null && !jTable_bill_items.getModel().getValueAt(i, 0).toString().trim().equals("")) {
                    price = Float.parseFloat(jTable_bill_items.getModel().getValueAt(i, 4).toString());
                    quantity = Float.parseFloat(jTable_bill_items.getModel().getValueAt(i, 2).toString());
                    bonus = Float.parseFloat(jTable_bill_items.getModel().getValueAt(i, 3).toString());
                    item_discount = Float.parseFloat(jTable_bill_items.getModel().getValueAt(i, 5).toString());
                    System.out.println(price+quantity);
                    if (price <= 0) {
                        throw new Exception("خطأ في مدخلات الجدول السعر اقل او يساوي صفر ");
                    }
                    if (item_discount < 0 || item_discount > 100) {
                        throw new Exception("خطأ في مدخلات الجدول الخصم اقل من  صفر ");
                    }
                    if (quantity < 0 || bonus < 0)//price&quantity not equal - & <0
                    {
                        throw new Exception("خطأ في مدخلات الجدول قيمة صفر او سالب ");
                    }
                    if (quantity == 0 && bonus == 0)//الصنف لا هو كمية ولا هو بونص
                    {
                        throw new Exception("الصنف لا هو كمية ولا هو بونص");
                    }
                    sum = sum + (price * quantity * (1 - item_discount / 100));
                    float item_sum = (price * quantity* (1 - item_discount / 100));
                    //////////لتنجب عمل تغييرات في الجدول غير مفيدة وبالتالي اي تغيير يعيد عملة المستمع وتصبح العملية غير منتهية  
                    if (jTable_bill_items.getModel().getValueAt(i, 6) == null) {
                        jTable_bill_items.getModel().setValueAt(item_sum, i, 6);
                    } else if (item_sum != Float.parseFloat(jTable_bill_items.getModel().getValueAt(i, 6).toString())) {
                        jTable_bill_items.getModel().setValueAt(item_sum, i, 6);
                    }
                    //////////
                }
            }
            bill_discount_ratio = Float.parseFloat(jTextField_bill_dis_ratio.getText().trim());
            if (bill_discount_ratio < 0 || bill_discount_ratio >= 100) {
                throw new Exception("خطأ بنسبة الخصم");
            }
            bill_discount_value = Float.parseFloat(jTextField_bill_discount.getText().trim());
            if (bill_discount_value < 0) {
                throw new Exception("خطأ بقيمة خصم الفاتورة");
            }

            bill_amount = sum * (1 - bill_discount_ratio / 100) - bill_discount_value;
            //jTextField1.setText(Float.toString(bill_amount));
            jTextField_bill_before_dis.setText(Float.toString(sum));

            if (bill_discount_value > bill_amount) {
                throw new Exception("خطأ بقيمة خصم الفاتورة");
            }

        } catch (Exception e) {
            Joptionpane_message(e.toString() + "\n" + "خطأأأأأأأأ");
            jTextField_bill_value.setText("0.0");

        }
        return bill_amount;
    }

    float prepare_bill_ven_jtable12_returns() throws Exception {
        float bill_amount = 0;
        try {
            jTable_return_ven_bill_items.editingCanceled(null);//هذا اذا نسي المدخل خانات مفتوحة اي مفتوحة للتعديل لا يتعامل معها على انها فارغة فيخرج منها قبل ادخال البيانات
            int row = jTable_return_ven_bill_items.getRowCount();
            float sum = 0;
            float price;//price
            float quantity;//quantity

            for (int i = 0; i < row; i++) {

                //here to make sure the item is not null... if item null the function doesnt work on that row and skip on
                if (jTable_return_ven_bill_items.getModel().getValueAt(i, 0) != null && !jTable_return_ven_bill_items.getModel().getValueAt(i, 0).toString().trim().equals("")) {
                    price = Float.parseFloat(jTable_return_ven_bill_items.getModel().getValueAt(i, 3).toString());
                    quantity = Float.parseFloat(jTable_return_ven_bill_items.getModel().getValueAt(i, 2).toString());

                    if (price <= 0) {
                        throw new Exception("خطأ في مدخلات الجدول السعر اقل او يساوي صفر ");
                    }

                    if (quantity == 0)//الصنف لا هو كمية ولا هو بونص
                    {
                        throw new Exception("خطا كمية االصنف تساوي صفر");
                    }
                    sum = sum + (price * quantity);
                    float item_sum = (price * quantity);
                    //////////لتنجب عمل تغييرات في الجدول غير مفيدة وبالتالي اي تغيير يعيد عملة المستمع وتصبح العملية غير منتهية  
                    if (jTable_return_ven_bill_items.getModel().getValueAt(i, 4) == null) {
                        jTable_return_ven_bill_items.getModel().setValueAt(item_sum, i, 4);
                    } else if (item_sum != Float.parseFloat(jTable_return_ven_bill_items.getModel().getValueAt(i, 4).toString())) {
                        jTable_return_ven_bill_items.getModel().setValueAt(item_sum, i, 4);
                    }

                    //////////
                }
            }

            bill_amount = sum;

        } catch (Exception e) {
            Joptionpane_message(e.toString() + "\n" + "خطأأأأأأأأ");
            jTextField_bill_value.setText("0.0");

        }
        return bill_amount;
    }
float prepare_bill_customer_returns() throws Exception {
        float bill_amount = 0;
        try {
            jTable12.editingCanceled(null);//هذا اذا نسي المدخل خانات مفتوحة اي مفتوحة للتعديل لا يتعامل معها على انها فارغة فيخرج منها قبل ادخال البيانات
            int row = jTable12.getRowCount();
            float sum = 0;
            float price;//price
            float quantity;//quantity

            for (int i = 0; i < row; i++) {

                //here to make sure the item is not null... if item null the function doesnt work on that row and skip on
                if (jTable12.getModel().getValueAt(i, 0) != null && !jTable12.getModel().getValueAt(i, 0).toString().trim().equals("")) {
                    price = Float.parseFloat(jTable12.getModel().getValueAt(i, 3).toString());
                    quantity = Float.parseFloat(jTable12.getModel().getValueAt(i, 2).toString());

                    if (price <= 0) {
                        throw new Exception("خطأ في مدخلات الجدول السعر اقل او يساوي صفر ");
                    }

                    if (quantity == 0)//الصنف لا هو كمية ولا هو بونص
                    {
                        throw new Exception("خطا كمية االصنف تساوي صفر");
                    }
                    sum = sum + (price * quantity);
                    float item_sum = (price * quantity);
                    //////////لتنجب عمل تغييرات في الجدول غير مفيدة وبالتالي اي تغيير يعيد عملة المستمع وتصبح العملية غير منتهية  
                    if (jTable12.getModel().getValueAt(i, 4) == null) {
                        jTable12.getModel().setValueAt(item_sum, i, 4);
                    } else if (item_sum != Float.parseFloat(jTable12.getModel().getValueAt(i, 4).toString())) {
                        jTable12.getModel().setValueAt(item_sum, i, 4);
                    }

                    //////////
                }
            }

            bill_amount = sum;

        } catch (Exception e) {
            Joptionpane_message(e.toString() + "\n" + "خطأأأأأأأأ");
            jTextField34.setText("0.0");

        }
        return bill_amount;
    }

    float prepare_bill_jtable15_returns() throws Exception {
        float bill_amount = 0;
        try {
            jTable15.editingCanceled(null);//هذا اذا نسي المدخل خانات مفتوحة اي مفتوحة للتعديل لا يتعامل معها على انها فارغة فيخرج منها قبل ادخال البيانات
            int row = jTable15.getRowCount();
            float sum = 0;
            float price;//price
            float quantity;//quantity

            for (int i = 0; i < row; i++) {

                //here to make sure the item is not null... if item null the function doesnt work on that row and skip on
                if (jTable15.getModel().getValueAt(i, 0) != null && !jTable15.getModel().getValueAt(i, 0).toString().trim().equals("")) {
                    price = Float.parseFloat(jTable15.getModel().getValueAt(i, 3).toString());
                    quantity = Float.parseFloat(jTable15.getModel().getValueAt(i, 2).toString());

                    if (price <= 0) {
                        throw new Exception("خطأ في مدخلات الجدول السعر اقل او يساوي صفر ");
                    }

                    if (quantity == 0)//الصنف لا هو كمية ولا هو بونص
                    {
                        throw new Exception("خطا كمية االصنف تساوي صفر");
                    }
                    sum = sum + (price * quantity);
                    float item_sum = (price * quantity);
                    //////////لتنجب عمل تغييرات في الجدول غير مفيدة وبالتالي اي تغيير يعيد عملة المستمع وتصبح العملية غير منتهية  
                    if (jTable15.getModel().getValueAt(i, 4) == null) {
                        jTable15.getModel().setValueAt(item_sum, i, 4);
                    } else if (item_sum != Float.parseFloat(jTable15.getModel().getValueAt(i, 4).toString())) {
                        jTable15.getModel().setValueAt(item_sum, i, 4);
                    }

                    //////////
                }
            }

            bill_amount = sum;

        } catch (Exception e) {
            Joptionpane_message(e.toString() + "\n" + "خطأأأأأأأأ");
            jTextField1.setText("0.0");

        }
        return bill_amount;
    }
    float prepare_modify_vendor_bill_returns_bill() throws Exception {
        float bill_amount = 0;
        try {
            jTable16.editingCanceled(null);//هذا اذا نسي المدخل خانات مفتوحة اي مفتوحة للتعديل لا يتعامل معها على انها فارغة فيخرج منها قبل ادخال البيانات
            int row = jTable16.getRowCount();
            float sum = 0;
            float price;//price
            float quantity;//quantity

            for (int i = 0; i < row; i++) {

                //here to make sure the item is not null... if item null the function doesnt work on that row and skip on
                if (jTable16.getModel().getValueAt(i, 0) != null && !jTable16.getModel().getValueAt(i, 0).toString().trim().equals("")) {
                    price = Float.parseFloat(jTable16.getModel().getValueAt(i, 3).toString());
                    quantity = Float.parseFloat(jTable16.getModel().getValueAt(i, 2).toString());

                    if (price <= 0) {
                        throw new Exception("خطأ في مدخلات الجدول السعر اقل او يساوي صفر ");
                    }

                    if (quantity == 0)//الصنف لا هو كمية ولا هو بونص
                    {
                        throw new Exception("خطا كمية االصنف تساوي صفر");
                    }
                    sum = sum + (price * quantity);
                    float item_sum = (price * quantity);
                    //////////لتنجب عمل تغييرات في الجدول غير مفيدة وبالتالي اي تغيير يعيد عملة المستمع وتصبح العملية غير منتهية  
                    if (jTable16.getModel().getValueAt(i, 4) == null) {
                        jTable16.getModel().setValueAt(item_sum, i, 4);
                    } else if (item_sum != Float.parseFloat(jTable16.getModel().getValueAt(i, 4).toString())) {
                        jTable16.getModel().setValueAt(item_sum, i, 4);
                    }

                    //////////
                }
            }

            bill_amount = sum;

        } catch (Exception e) {
            Joptionpane_message(e.toString() + "\n" + "خطأأأأأأأأ");
            jTextField38.setText("0.0");

        }
        return bill_amount;
    }

    public Date get_date_jdate() {
        //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date d = new Date();
        return d;

    }

    public void write_to_file(String text) {
        BufferedWriter bw = null;
        try {
            /////////////////get date to naming file
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String file_name = dateFormat.format(date);
            /////////////////////////
            File file = new File(write_to_file_loc + file_name + ".txt");
            FileWriter fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);
            bw.write(text);
            bw.newLine();
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                if (bw != null) {
                    bw.flush();
                    bw.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public void move_to_customer_vendor_account() {
        int response = JOptionPane.showConfirmDialog(null, "هل تريد الانتقال الى جدول الحسابات؟؟",
                "", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);
        System.out.println(jTabbedPane1.getSelectedIndex());
        if (response == JOptionPane.OK_OPTION) {
            if (jTabbedPane1.getSelectedIndex() == 0) {
                jTabbedPane1.setSelectedIndex(2);
                jComboBox3.setSelectedItem(jComboBox1.getSelectedItem());
            } else if (jTabbedPane1.getSelectedIndex() == 1) {
                jTabbedPane1.setSelectedIndex(2);
                jComboBox3.setSelectedItem(jComboBox4.getSelectedItem());
            }
            else if (jTabbedPane1.getSelectedIndex() == 7) {
                jTabbedPane1.setSelectedIndex(2);
                jComboBox3.setSelectedItem(jComboBox8.getSelectedItem());
            }
            else if (jTabbedPane1.getSelectedIndex() == 6) {
                jTabbedPane1.setSelectedIndex(2);
                jComboBox3.setSelectedItem(jComboBox8.getSelectedItem());
            }
        }
    }
public void move_to_vendor_account() {
        int response = JOptionPane.showConfirmDialog(null, "هل تريد الانتقال الى جدول الحسابات؟؟",
                "", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);
        System.out.println(jTabbedPane_vendor_action.getSelectedIndex());
        if (response == JOptionPane.OK_OPTION) {
            if (jTabbedPane_vendor_action.getSelectedIndex() == 0) {
                jTabbedPane_vendor_action.setSelectedIndex(2);
                jComboBox_ven_name_for_account_details.setSelectedItem(jCombo_vendor_name.getSelectedItem());
            } else if (jTabbedPane_vendor_action.getSelectedIndex() == 1) {
                jTabbedPane_vendor_action.setSelectedIndex(2);
                jComboBox_ven_name_for_account_details.setSelectedItem(jComboBox_ven_name_pay.getSelectedItem());
            }
            else if (jTabbedPane_vendor_action.getSelectedIndex() == 4) {
                jTabbedPane_vendor_action.setSelectedIndex(2);
                jComboBox_ven_name_for_account_details.setSelectedItem(jComboBox_van_name_return_bill.getSelectedItem());
            }
            
        }
        create_ven_account_table();
    }
    public boolean check_yes_or_no_question(String question) {

        int response = JOptionPane.showConfirmDialog(null, question,
                "", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);

        return response == JOptionPane.OK_OPTION;
    }

    public void backup_database() {
        try {
            conn_obj.close();
            //We initialize some variables that weвЂ™ll be using
            Runtime r3 = Runtime.getRuntime();
    //Path to the place we store our backups

            //PostgreSQL variables
            String IP = conn_obj.get_connection_type();
            String user = "postgres";
            String port = "8081";
            String dbase = "shayeb_2015";
            String password = "25121986";
            Process p;
            ProcessBuilder pb;

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh-mm");
            Date date = new Date();
            String file_name = dateFormat.format(date);

            pb = new ProcessBuilder("C:\\Program Files\\PostgreSQL\\9.2\\bin\\pg_dump.exe", "-f", file_loc + file_name + ".backup", "-F", "c", "-Z", "9", "-v", "-o", "-h", IP, "-p", port, "-U", user, dbase);
            pb.environment().put("PGPASSWORD", password);
            pb.redirectErrorStream(true);
            p = pb.start();
            //p.destroy();
            try {
                InputStream is = p.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String ll;
                String text = null;
                int va=0;
                while ((ll = br.readLine()) != null) {
                    x.jProgressBar1.setValue(va++);
                    x.jLabel1.setText(ll);
                    text = text + ll + "\n";
                }
                x.dispose();
                joptionpane_jtextarea(text);
                conn_obj.create_conn();
            } catch (IOException e) {
                System.out.println(e);
                System.out.println(e.getMessage());
            }

        } catch (IOException x) {
            System.err.println("Could not invoke browser, command=");
            System.err.println("Caught: " + x.getMessage());
        }
    }

    public void restore_database(String database_file) throws IOException {
        conn_obj.close();
        Runtime r2 = Runtime.getRuntime();
        Process p;
        ProcessBuilder pb;
        r2 = Runtime.getRuntime();
        pb = new ProcessBuilder(
                "c:\\Program Files\\PostgreSQL\\9.2\\bin\\pg_restore.exe",
                "--host", "localhost",
                "--port", "8081",
                "--username", "postgres",
                "--dbname", "shayeb_2015",
                "--role", "postgres",
                "--no-password",
                "--verbose",
                "--clean",
                database_file);
        pb.redirectErrorStream(true);
        p = pb.start();
        InputStream is = p.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String ll;
        String text = null;
        while ((ll = br.readLine()) != null) {
            text = text + ll + "\n";
        }
        joptionpane_jtextarea(text);
        conn_obj.create_conn();

    }

    public void jframe_dimention_location() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(screenSize.width, screenSize.height -30);//dimention
        //Location in full screen
        this.setLocation((screenSize.width - screenSize.width) / 2,
                (screenSize.height - screenSize.height) / 2);
    }

    public void jframe_dimention_location_center(Component component) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        component.setLocation((screenSize.width - component.getWidth()) / 2,
                (screenSize.height - component.getHeight()) / 2);
    }

    public void joptionpane_jtextarea(String text) {
        JTextArea fatextArea = new JTextArea(text);
        fatextArea.setFont(FayezFont);
        JScrollPane scrollPane = new JScrollPane(fatextArea);
        fatextArea.setLineWrap(true);
        fatextArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showMessageDialog(null, scrollPane, "الاصناف", INFORMATION_MESSAGE);
    }

    boolean check_if_accounted(int id, String type) throws SQLException {
        if (type.equals("دفعة")) {
            r = conn_obj.conn_exec("select accounted from customer_payments where payment_id=" + id + "");
            r.next();
            return r.getBoolean(1);
        }
        if (type.equals("فاتورة")) {
            r = conn_obj.conn_exec("select accounted from customer_bills where bill_id=" + id + "");
            r.next();
            return r.getBoolean(1);
        }
        return false;

    }
boolean check_if_accounted_ven(int id, String type) throws SQLException {
        if (type.equals("دفعة")) {
            r = conn_obj.conn_exec("select accounted from vendor_payments where payment_id=" + id + "");
            r.next();
            return r.getBoolean(1);
        }
        if (type.equals("فاتورة")) {
            r = conn_obj.conn_exec("select accounted from vendor_bills where bill_id=" + id + "");
            r.next();
            return r.getBoolean(1);
        }
        return false;

    }

    public void show_last_row_scroll_jtable(JTable table) {
        table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, 0, true));
    }

     public void print_jtable(int start_row,int end_row, String status) throws SQLException {
        ///////////////////
        int row = jTable3.getRowCount();
        int col = jTable3.getColumnCount();
        float account_sum = 0;
        String html = "<body>" + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>"
               // + "<h2 align=\"center\">محلات الشايب التجارية</h2>\n"
               // + "<h3 align=\"center\">برقين - 042438973</h3>\n"
                + "<h3 align=\"center\">كشف حساب الذمم</h3>";
        html += "<table align=\"center\" border=\"1\" style=\"width:5px\" cellspacing=\"0\" cellpadding=\"1\" style=\"font-size: 8px\">" + "\n";
        html += "<tr>" + "\n";
        html += "<td >" + "تاريخ الطباعة : " + get_date() + "</td>" + "\n";
        html += "<td >" + "         " + "</td>" + "\n";
        html += "<td align=\"right\">" + "إسم الزبون  : " + jComboBox3.getSelectedItem().toString() + "</td>" + "\n";
        html += "</table>";
        html += "<table align=\"center\" border=\"1\" style=\"width:5px\" cellspacing=\"0\" cellpadding=\"1\" style=\"font-size: 8px\">" + "\n";
        html += "<tr>" + "\n";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable3.getColumnName(0) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable3.getColumnName(1) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable3.getColumnName(2) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable3.getColumnName(3) + "</th>";
        //html += "<th bgcolor=" + "#00FF00" + ">" + jTable3.getColumnName(4) + "</th>"; المكان
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable3.getColumnName(5) + "</th>";
        //html += "<th bgcolor=" + "#00FF00" + ">" + jTable3.getColumnName(7) + "</th>";رقم الفاتورة
        html += "</tr>" + "\n";
         for (int i = row - start_row; i < row-end_row; i++) {
            html += "<tr>" + "\n";
            for (int x = 0; x < col-2; x++) {// انتبه -2 لحذف رقم القيد ورقم الفاتورة
                if (x != 6 && x != 4) {//لا تساوي 6 لحذف عامود رقم القيد 
                    // لا تساوي 4 لحذف المكان
                    if (x == 2)//اذا ملاحظة اعمل العرض ثابت
                    {
                        html += "<td width=\"60\" align=\"center\">" + jTable3.getValueAt(i, x) + "</td>" + "\n";
                    } else {
                        html += "<td align=\"center\">" + jTable3.getValueAt(i, x) + "</td>" + "\n";
                    }
                }
            }
            html += "</tr>" + "\n";
           
            
            account_sum = Float.parseFloat(jTable3.getModel().getValueAt(i, 0).toString());
        }
        html += "<table align=\"center\" border=\"1\" style=\"width:5px\" cellspacing=\"0\" cellpadding=\"1\" style=\"font-size: 8px\">" + "\n";

        html += "<tr>" + "\n";
        html += "<td>" + "مجموع الحساب = " + account_sum + "</td>" + "\n";
        html += "</tr>" + "\n";
        html += "<tr><td></td></tr>" + "\n";
        html += "</table>";
        html += "</table></body>" + "\n";

        /////////////////////////////////
        if (status == ("save_to_file")) {
            generateHtmlPage(html);
        } else {
            JPanel panel = new JPanel();
            final JEditorPane editorPane = new JEditorPane();
            editorPane.setEditable(false);
            editorPane.setContentType("text/html");

            editorPane.setText(html);
            panel.add(editorPane);
            JFrame frame2 = new JFrame();
            frame2.setSize(700, 600);
            frame2.setLocationRelativeTo(this);
            JButton but = new JButton();
            but.setText("طباعة");
            but.requestFocus(true);
            panel.add(but);
            but.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        //Execute when button is pressed
                        editorPane.print();
                    } catch (PrinterException ex) {
                        Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            frame2.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            // Change this to switch between examples
            boolean useScrollPane = true;

            if (useScrollPane) {
                JScrollPane scrollPane = new JScrollPane();
                scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setViewportView(panel);
                frame2.add(scrollPane);
            } else {
                frame2.add(panel);
            }
            frame2.setVisible(true);

        }
    }

    public void print_jtable_with_details(int start_row,int end_row, String status) throws SQLException {
        ///////////////////
        int row = jTable3.getRowCount();
        int col = jTable3.getColumnCount();
        float account_sum = 0;
        
        

        String html = "<body>"
                + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>"
                //+ "<h2 align=\"center\">محلات الشايب التجارية</h2>\n"
                //+ "<h3 align=\"center\">برقين - 042438973</h3>\n"
                + "<h3 align=\"center\">كشف حساب الذمم</h3>";
        html += "<table align=\"center\" border=\"1\" style=\"width:5px\" cellspacing=\"0\" cellpadding=\"1\" style=\"font-size: 8px\">" + "\n";
        html += "<tr>" + "\n";
        html += "<td >" + "تاريخ الطباعة : " + get_date() + "</td>" + "\n";
        html += "<td >" + "         " + "</td>" + "\n";
        html += "<td align=\"right\">" + "إسم الزبون  : " + jComboBox3.getSelectedItem().toString() + "</td>" + "\n";
        html += "</table>";
        html += "<table align=\"center\" border=\"1\" style=\"width:5px\" cellspacing=\"0\" cellpadding=\"1\" style=\"font-size: 8px\">" + "\n";
        html += "<tr>" + "\n";

        html += "<th bgcolor=" + "#00FF00" + ">" + jTable3.getColumnName(0) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable3.getColumnName(1) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable3.getColumnName(2) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable3.getColumnName(3) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable3.getColumnName(4) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable3.getColumnName(5) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable3.getColumnName(7) + "</th>";

        html += "</tr>" + "\n";
        for (int i = row - start_row; i < row-end_row; i++) {
            html += "<tr>" + "\n";
            for (int x = 0; x < col; x++) {
                if (x != 6) {
                    if (x == 2)//اذا ملاحظة اعمل العرض ثابت
                    {
                        html += "<td width=\"60\" align=\"center\">" + jTable3.getValueAt(i, x) + "</td>" + "\n";
                    } else {
                        html += "<td align=\"center\">" + jTable3.getValueAt(i, x) + "</td>" + "\n";
                    }
                }
            }
            html += "</tr>" + "\n";
            ////// من هنا اضافة التفاصيل
            String movement_type = (jTable3.getValueAt(i, 3)).toString();
            String movement_id = (jTable3.getValueAt(i, 6)).toString();

            if (movement_type.equalsIgnoreCase("فاتورة")) {
                r = conn_obj.conn_exec("select item_note as ملاحظة ,(SELECT round(CAST(price*item_quantity as numeric),1) as المبلغ),price as السعر_بعد,discount_ratio as نسبة_الخصم,round(cast(item_price as numeric),2)as السعر,item_bonus as بونص,item_quantity as الكمية,unit_name as الوحدة,item_name as اسم_الصنف from ("
                        + "SELECT \n"
                        + "items.main_items.item_name,\n"
                        + "items.main_items.item_id,\n"
                        + "\n"
                        + "items.item_units.unit_name,\n"
                        + "items.item_units.unit_id,\n"
                        + "\n"
                        + "customer_bills_items.id,customer_bills_items.item_id,customer_bills_items.item_bonus,\n"
                        + "customer_bills_items.item_note,customer_bills_items.item_unit,\n"
                        + "customer_bills_items.item_price,customer_bills_items.item_quantity,\n"
                        + "customer_bills_items.bill_id,customer_bills_items.discount_ratio,\n"
                        + "(1-customer_bills_items.discount_ratio/100)*customer_bills_items.item_price as price\n"
                        + "FROM items.main_items,items.item_units,customer_bills_items\n"
                        + "WHERE\n"
                        + "items.main_items.item_id = customer_bills_items.item_id\n"
                        + "AND\n"
                        + "items.item_units.unit_id = customer_bills_items.item_unit\n"
                        + "AND\n"
                        + "customer_bills_items.bill_id=" + movement_id + " order by customer_bills_items.id)as dvfc");

                while (r.next()) {
                    html += "<tr>" + "\n";

                    html += "<td align=\"center\">" + r.getString(1) + "</td>" + "\n";
                    html += "<td align=\"center\">" + r.getString(2) + "</td>" + "\n";
                    html += "<td align=\"center\">" + r.getString(4) + "</td>" + "\n";
                    html += "<td align=\"center\">" + r.getString(5) + "</td>" + "\n";
                    html += "<td align=\"center\">" + r.getString(6) + "</td>" + "\n";
                    html += "<td align=\"center\">" + r.getString(7) + "</td>" + "\n";
                    html += "<td align=\"center\">" + r.getString(8) + "</td>" + "\n";
                    html += "<td align=\"center\">" + r.getString(9) + "</td>" + "\n";

                    html += "</tr>" + "\n";
                }
            }

            ///////  
            account_sum = Float.parseFloat(jTable3.getModel().getValueAt(i, 0).toString());
        }
        html += "<table align=\"center\" border=\"1\" style=\"width:5px\" cellspacing=\"0\" cellpadding=\"1\" style=\"font-size: 8px\">" + "\n";

        html += "<tr>" + "\n";
        html += "<td>" + "مجموع الحساب = " + account_sum + "</td>" + "\n";
        html += "</tr>" + "\n";
        html += "<tr><td></td></tr>" + "\n";
        html += "</table>";
        html += "</table></body>" + "\n";
        /////////////////////////////////
        if (status == ("save_to_file")) {
            generateHtmlPage(html);
        } else {
            JPanel panel = new JPanel();
            final JEditorPane editorPane = new JEditorPane();
            editorPane.setEditable(false);
            editorPane.setContentType("text/html");

            editorPane.setText(html);
            panel.add(editorPane);
            JFrame frame2 = new JFrame();
            frame2.setSize(700, 600);
            frame2.setLocationRelativeTo(this);
            JButton but = new JButton();
            but.setText("طباعة");
            but.requestFocus(true);
            panel.add(but);
            but.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        //Execute when button is pressed
                        editorPane.print();
                    } catch (PrinterException ex) {
                        Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            frame2.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            // Change this to switch between examples
            boolean useScrollPane = true;

            if (useScrollPane) {
                JScrollPane scrollPane = new JScrollPane();
                scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setViewportView(panel);
                frame2.add(scrollPane);
            } else {
                frame2.add(panel);
            }
            frame2.setVisible(true);
        }

    }

    public void php_insert_bill(String parameters) {
        try {
            String inLine;
            URL u1 = new URL("http://shayeb.bugs3.com/insert_bill.php");
            URLConnection uc1 = u1.openConnection();
            uc1.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(uc1.getOutputStream());
            out.write(parameters);
            out.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(uc1.getInputStream(), "UTF-8"));
          // هذا الكود لقراءة كل السطور لا نريده الان لاننا نريد ان نقرأ السطر الاول فق

            while ((inLine = in.readLine()) != null) {
                System.out.println(inLine);
            }

            in.close();
        } catch (MalformedURLException ex) {
            Joptionpane_message(ex.getMessage());
        } catch (IOException ex) {
            Joptionpane_message(ex.getMessage());
        }
    }

    public boolean check_admin_password() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter Admin password:");
        JPasswordField pass = new JPasswordField(30);
        panel.add(label);
        panel.add(pass);
        String[] options = new String[]{"OK", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, panel, "Admin Password?",
                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
        pass.requestFocus();
        if (option == 0) // pressing OK button
        {
            try {
                char[] password = pass.getPassword();
                r = conn_obj.conn_exec("select * from variables where var_name ='admin_password'");
                r.next();
                if(r.getString("var_value").equals(new String(password)))
                    return true;
            } catch (SQLException ex) {
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return  false;
    }

    public class MyTableCellEditor_items_names extends AbstractCellEditor implements TableCellEditor {

        public JComboBox editor;
        private String[] values = {};

        public MyTableCellEditor_items_names() {
            // Create a new Combobox with the array of values.
            editor = new JComboBox(values);

            r = conn_obj.conn_exec("select item_name from items.main_items");
            try {
                while (r.next()) {
                    editor.addItem(r.getString(1));
                }
            } catch (SQLException ex) {
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            }
            //editor = jComboBox1;
            AutoCompleteDecorator.decorate(this.editor);
            editor.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent arg0) {

                    // jTable_bill_items.setCellEditor(jTable_bill_items.getCellEditor(5, 0));
                    int selected_row_table4 = jTable_bill_items.getSelectedRow();
                    /*
                     if(jTable_bill_items.getCellEditor().getCellEditorValue()!=null)
                     {
                     r = conn_obj.conn_exec("select items.item_units.unit_name,items.item_units.unit_id,\n" +
                     "items.main_items.item_name,items.main_items.unit_default_sell \n" +
                     "from items.item_units,items.main_items \n" +
                     "where \n" +
                     "items.main_items.item_name='"+jTable_bill_items.getCellEditor().getCellEditorValue().toString().trim()+"'\n" +           
                     "AND\n" +
                     "items.item_units.unit_id= items.main_items.unit_default_sell ");
   
                     try {
                     r.next();
                     jTable_bill_items.setValueAt(r.getString(1), selected_row_table4, 1);
                    
                     } catch (SQLException ex) {
                     Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
                     }
    
                     }
                     */
                }
            }
            );

        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int colIndex) {

            // Set the model data of the table
            if (isSelected) {
                editor.setSelectedItem(value);
                TableModel model = table.getModel();
                model.setValueAt(value, rowIndex, colIndex);
                System.out.println("selected");
            }

            return editor;
        }

        @Override
        public Object getCellEditorValue() {
            return editor.getSelectedItem();
        }

    }

    public Dimension get_screen_dimension() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return screenSize.getSize();
    }

    public void alinment_component(JTextField x) {
        x.setHorizontalAlignment(x.RIGHT);
    }

    public void add_row_jtable4(JTable table, Object a, Object b, Object c, Object d, Object e, Object f, Object g, Object h) {
        DefaultTableModel tm = (DefaultTableModel) table.getModel();
        tm.addRow(new Object[]{a, b, c, d, e, f, g, h});
    }

    public void create_non_existant_table() {
        try {
            r = conn_obj.conn_exec("select customer_name as اسم_الزبون,item_name as الصنف,date (date) as تاريخ_الادخال ,id , item_note from(\n"
                    + "\n"
                    + "select\n"
                    + "customers.customer_id,customers.customer_name,\n"
                    + "items.main_items.item_id,items.main_items.item_name,\n"
                    + "items.non_existant_items.item_id,items.non_existant_items.customer_id,items.non_existant_items.date,items.non_existant_items.id,items.non_existant_items.item_note\n"
                    + "from\n"
                    + "customers,items.main_items,items.non_existant_items\n"
                    + "where\n"
                    + "customers.customer_id = items.non_existant_items.customer_id and\n"
                    + "items.main_items.item_id = items.non_existant_items.item_id\n"
                    + "\n"
                    + "order by items.non_existant_items.date)as ddd");
            jTable8.setModel(DbUtils.resultSetToTableModel(r));
            renderer_jTable_obj.Renderer(jTable8);
        } catch (Exception e) {
            Joptionpane_message(e.getMessage());
        }
    }

    public void create_movement_date_table(String date) {
        try {
            String payments_movement= "select customer_payments.payment_value,customer_payments.payment_date,'دفعة' as movement,customer_payments.customer_id_fk,customer_payments.record_time,customers.customer_id,customers.customer_name\n"
                    + "from customer_payments \n"
                    + "\n"
                    + "join customers\n"
                    + "on customers.customer_id=customer_payments.customer_id_fk\n"
                    + "where customer_payments.payment_date >= '" + date + "'\n"
                    + "union\n";
            
            String bills_movement="select customer_bills.bill_value,customer_bills.bill_date,'فاتورة' as movement,customer_bills.bill_customer_id,customer_bills.record_time,customers.customer_id,customers.customer_name\n"
                    + "from customer_bills \n"
                    + "\n"
                    + "join customers\n"
                    + "on customers.customer_id=customer_bills.bill_customer_id\n"
                    + "where customer_bills.bill_date >= '" + date + "' \n"
                    + "union\n";
            String return_bill_movement="select return_bill_value,return_bill_date,'م-مبيعات' as movement,return_bill_customer_id,record_time,customers.customer_id,customers.customer_name\n"
                    + "from return_customer_bills \n"
                    + "\n"
                    + "join customers\n"
                    + "on customers.customer_id=return_bill_customer_id\n"
                    + "where return_bill_date >= '" + date + "' \n"
                    + "union\n";
            //String discount_movement=
            String stm = "select payment_value as القيمة,payment_date as التاريخ,movement as الحركة,customer_name as اسم_الزبون from(\n"
                    +payments_movement
                    +bills_movement
                    +return_bill_movement
                    + "select discount_value,discount_date,'خصم' as movement,customer_id_fk,record_time,customers.customer_id,customers.customer_name\n"
                    + "from customer_discount \n"
                    + "\n"
                    + "join customers\n"
                    + "on customers.customer_id=customer_id_fk\n"
                    + "where discount_date >= '" + date + "'"
                    + " order by record_time\n"
                    + ")as ddfv order by التاريخ";
            System.out.println(stm);
            r = conn_obj.conn_exec(stm);
            jTable9.setModel(DbUtils.resultSetToTableModel(r));
            renderer_jTable_obj.Renderer(jTable9);
            jTable9.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        } catch (Exception e) {
            Joptionpane_message(e.getMessage());
        }
    }

    private void select_unit_in_jtable4_right_click(java.awt.event.ActionEvent evt) {

        String item_name = jTable4.getValueAt(jTable4.getSelectedRow(), 0).toString().trim();
        String old_unit_name = jTable4.getValueAt(jTable4.getSelectedRow(), 1).toString().trim();
        String new_unit_name = evt.getActionCommand().trim();
        
        int old_unit_name_id,new_unit_name_id,item_name_id;
        float old_unit_relation,new_unit_relation;
        //float last_price=
        try {
            r = conn_obj.conn_exec("select unit_id from items.item_units where unit_name ='"+old_unit_name+"';");
            r.next();
            old_unit_name_id=r.getInt("unit_id");
            
            r = conn_obj.conn_exec("select unit_id from items.item_units where unit_name ='"+new_unit_name+"';");
            r.next();
            new_unit_name_id=r.getInt("unit_id");
            
            r = conn_obj.conn_exec("select item_id from items.main_items where item_name ='"+item_name+"';");
            r.next();
            item_name_id=r.getInt("item_id");
            
            r = conn_obj.conn_exec("select item_relation from items.item_relations where item_id ="+item_name_id+"  and item_unit ="+old_unit_name_id+"  ;");
            r.next();
            old_unit_relation=r.getFloat("item_relation");
            
            r = conn_obj.conn_exec("select item_relation from items.item_relations where item_id ="+item_name_id+"  and item_unit ="+new_unit_name_id+"  ;");
            r.next();
            new_unit_relation=r.getFloat("item_relation");
            float existance_price=Float.parseFloat(jTable4.getValueAt(jTable4.getSelectedRow(), 4).toString());
            float new_price=existance_price/(old_unit_relation/new_unit_relation);
            System.out.println(new_price);
            jTable4.setValueAt(new_price, jTable4.getSelectedRow(), 4);
            jTable4.setValueAt(new_unit_name, jTable4.getSelectedRow(), 1);
        } catch (Exception e) {
            Joptionpane_message(e.getMessage());
        }
    }
private void select_unit_in_jTable_bill_items_right_click(java.awt.event.ActionEvent evt) {

        String unit_name = evt.getActionCommand().trim();
        String item_name = jTable_bill_items.getValueAt(jTable_bill_items.getSelectedRow(), 0).toString().trim();
        jTable_bill_items.setValueAt(unit_name, jTable_bill_items.getSelectedRow(), 1);

        try {
            r = conn_obj.conn_exec("select item_name as الاسم,unit_name as الوحدة,to_char(value, '9999999999.00') as السعر_علينا,relation as علاقتها_بالرئيسية\n"
                    + "                    from\n"
                    + "                    ( \n"
                    + "                    select \n"
                    + "                      items.item_units.unit_name,items.item_units.unit_id,\n"
                    + "                      items.main_items.item_name,items.main_items.item_id,items.main_items.item_value*items.item_relations.item_relation as value,\n"
                    + "                      items.item_relations.item_id,items.item_relations.item_unit,items.item_relations.item_price_hole,items.item_relations.item_relation as relation\n"
                    + "                               \n"
                    + "                                from items.item_units,items.main_items,items.item_relations\n"
                    + "                                                               where \n"
                    + "                                items.main_items.item_name ='" + item_name + "'\n"
                    + "                                AND \n"
                    + "                                items.item_relations.item_id= items.main_items.item_id\n"
                    + "                                and\n"
                    + "                                items.item_relations.item_unit=items.item_units.unit_id\n"
                    + "                                and\n"
                    + "                                items.item_relations.item_unit=(select unit_id from items.item_units where unit_name='" + unit_name + "')     \n"
                    + "\n"
                    + "                                   )  as alias");
            r.next();
            jTable_bill_items.setValueAt(r.getFloat(3), jTable_bill_items.getSelectedRow(), 4);
        } catch (Exception e) {
            Joptionpane_message(e.getMessage());
        }
    }
    public JFrame getjframe_searchItemName() {
        return searchItemName;
    }

    public float get_customer_checks_sum(String customer_name) throws SQLException {
        r = conn_obj.conn_exec("select sum (check_value) from (\n"
                + "select \n"
                + "customer_checks.check_value,\n"
                + "\n"
                + "customer_payments.payment_id,\n"
                + "customer_payments.customer_id_fk,customers.customer_name,\n"
                + "customers.customer_id\n"
                + "\n"
                + "from customers,customer_payments,customer_checks\n"
                + "where customers.customer_name='" + customer_name + "' and\n"
                + "customer_payments.customer_id_fk=customers.customer_id and\n"
                + "customer_payments.payment_id=customer_checks.check_payment_id \n"
                + ")as ddd");
        r.next();
        return r.getFloat("sum");

    }
    public float get_customer_checks_sum_under_collection(String customer_name) throws SQLException {
        String stm="select sum (check_value) from (\n"
                + "select \n"
                + "customer_checks.check_value,\n"
                + "customer_checks.check_due_date,\n"
                + "customer_payments.payment_id,\n"
                + "customer_payments.customer_id_fk,customers.customer_name,\n"
                + "customers.customer_id\n"
                + "\n"
                + "from customers,customer_payments,customer_checks\n"
                + "where customers.customer_name='" + customer_name + "' and\n"
                + "customer_payments.customer_id_fk=customers.customer_id and\n"
                + "customer_payments.payment_id=customer_checks.check_payment_id and\n"
                + "check_due_date>='" + get_date() + "')as ddd";
        r = conn_obj.conn_exec(stm);
         System.out.println(stm);
        r.next();
        return r.getFloat("sum");

    }
    public float get_vendor_checks_sum_under_collection(String vendor_name) throws SQLException {
        r = conn_obj.conn_exec("select sum(check_value) from\n" +
"(\n" +
"select vendors.vendor_id,vendors.vendor_name,\n" +
"       public.vendor_payments.vendor_id_fk,public.vendor_payments.payment_id,\n" +
"       public.checks.check_value,public.checks.check_to_vendor_payment_id,public.checks.check_due_date\n" +
"       from vendors,vendor_payments,checks\n" +
"       \n" +
"       where vendor_name='"+vendor_name+"' and\n" +
"             vendor_id=vendor_id_fk and\n" +
"             checks.check_due_date > '"+get_date()+"' and\n" +
"             check_to_vendor_payment_id=payment_id\n" +
"             ) d");
        r.next();
        return r.getFloat("sum");

    }
    public float get_vendor_checks_sum(String vendor_name) throws SQLException {
        r = conn_obj.conn_exec("select sum(check_value) from\n" +
"(\n" +
"select vendors.vendor_id,vendors.vendor_name,\n" +
"       public.vendor_payments.vendor_id_fk,public.vendor_payments.payment_id,\n" +
"       public.checks.check_value,public.checks.check_to_vendor_payment_id \n" +
"       from vendors,vendor_payments,checks\n" +
"       \n" +
"       where vendor_name='"+vendor_name+"' and \n" +
"             vendor_id=vendor_id_fk and \n" +
"             check_to_vendor_payment_id=payment_id\n" +
"             ) d");
        r.next();
        return r.getFloat("sum");

    }

    public void save_payment_image_to_folder(String path, String image_name, JLabel label) throws IOException {
        ImageIcon icon = (ImageIcon) label.getIcon();

        Image img = icon.getImage();
        BufferedImage bi = null;
        if (img.getWidth(this) > 0) {
            bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);

            Graphics2D g2 = bi.createGraphics();
            g2.drawImage(img, 0, 0, null);
            g2.dispose();
            ImageIO.write(bi, "jpg", new File(path + image_name + ".jpg"));
        }         //

    }

    public static boolean findMe(String subString, String mainString) {
        boolean foundme = false;
        int max = mainString.length() - subString.length();

        // Implement your own Contains Method with Recursion
        checkrecusion:
        for (int i = 0; i <= max; i++) {
            int n = subString.length();

            int j = i;
            int k = 0;

            while (n-- != 0) {
                if (mainString.charAt(j++) != subString.charAt(k++)) {
                    continue checkrecusion;
                }
            }
            foundme = true;
            break checkrecusion;
        }

        return foundme;
    }

    public Object[][] getTableData(JTable table) {
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        int nRow = dtm.getRowCount(), nCol = 8;
        Object[][] tableData = new Object[nRow][nCol];
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                tableData[i][j] = dtm.getValueAt(i, j);
            }
        }
        return tableData;
    }

    public void jTableCustomization_fayez(JTable table) {
        table.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        renderer_jTable_obj.Renderer(table);

        table.getModel().addTableModelListener(fayez = new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent evt) {
                try {
                    jTextField1.setText(Float.toString(prepare_bill()));
                } catch (Exception e) {
                    Joptionpane_message(e.getMessage());
                }
            }
        });

        table.putClientProperty("terminateEditOnFocusLost", true);

        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setMaxWidth(100);
        table.getColumnModel().getColumn(3).setMaxWidth(100);
        table.getColumnModel().getColumn(5).setMaxWidth(130);

    }
   public void jTableCustomization_fayez_vendor(JTable table) {
        table.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        renderer_jTable_obj.Renderer(table);

        table.getModel().addTableModelListener(fayez = new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent evt) {
                try {
                    jTextField_bill_value.setText(Float.toString(prepare_ven_bill()));
                } catch (Exception e) {
                    Joptionpane_message(e.getMessage());
                }
            }
        });

        table.putClientProperty("terminateEditOnFocusLost", true);

        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setMaxWidth(100);
        table.getColumnModel().getColumn(3).setMaxWidth(100);
        table.getColumnModel().getColumn(5).setMaxWidth(130);

    }
    public void jTableCustomization_fayez_2(JTable table) {
        table.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        renderer_jTable_obj.Renderer(table);
        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent evt) {
                try {
                    jTextField_return_ven_bill_value.setText(Float.toString(prepare_bill_ven_jtable12_returns()));
                } catch (Exception e) {
                    Joptionpane_message(e.getMessage());
                }
            }
        });

    }
      public void jTableCustomization_modify_vendor_return_bill(JTable table) {
        table.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        renderer_jTable_obj.Renderer(table);
        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent evt) {
                try {
                    jTextField38.setText(Float.toString(prepare_modify_vendor_bill_returns_bill()));
                } catch (Exception e) {
                    Joptionpane_message(e.getMessage());
                }
            }
        });

    }
public void return_customer_jTable(JTable table) {
        table.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        renderer_jTable_obj.Renderer(table);
        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent evt) {
                try {
                    jTextField34.setText(Float.toString(prepare_bill_customer_returns()));
                } catch (Exception e) {
                    Joptionpane_message(e.getMessage());
                }
            }
        });

    }
    public double get_table_column_sum(JTable table, int column) {
        double total = 0;

        for (int i = 0; i < table.getRowCount(); i++) {
            total += Double.parseDouble((table.getValueAt(i, column)).toString());
        }
        return total;
    }

    public double get_profit_percentage(double profit, double bill_value) {
        return ((profit / bill_value) * 100);
    }

    void search_in_customer_bills_about_item() {
        if (jCheckBox1.isSelected()) {
            jTextField29.setText("");
        }
        String word = (String) jTable7.getValueAt(jTable7.getSelectedRow(), 0);
        r = conn_obj.conn_exec("select id as رقم_الفاتورة ,discount_ratio as خصم_على_الصنف ,dis as خصم_نسبة_على_الفاتورة ,customer_name as المورد,bill_date التاريخ ,item_price السعر,item_quantity الكمية,unit_name الوحدة,item_name الصنف from (\n"
                + "\n"
                + "select customers.customer_id,\n"
                + "customers.customer_name,\n"
                + "customer_bills.bill_id as id,\n"
                + "customer_bills.discount_ratio as dis,\n"
                + "customer_bills.bill_date,\n"
                + "customer_bills.bill_customer_id,\n"
                + "customer_bills_items.bill_id,\n"
                + "customer_bills_items.item_id,\n"
                + "customer_bills_items.item_price,\n"
                + "customer_bills_items.item_quantity,\n"
                + "customer_bills_items.item_bonus,\n"
                + "customer_bills_items.discount_ratio,\n"
                + "customer_bills_items.item_unit,\n"
                + "items.main_items.item_name,\n"
                + "items.main_items.item_id,\n"
                + "items.item_units.unit_id,\n"
                + "items.item_units.unit_name \n"
                + "\n"
                + "from customer_bills_items,customers,customer_bills,items.main_items,items.item_units \n"
                + "where item_name LIKE '%" + word + "%'\n"
                + "and customer_bills.bill_id=customer_bills_items.bill_id\n"
                + "and bill_customer_id=customer_id\n"
                + "and customers.customer_name like '%" + jTextField29.getText().trim() + "%'"
                + "and customer_bills_items.item_unit=items.item_units.unit_id\n"
                + "and customer_bills_items.item_id=items.main_items.item_id\n"
                + "\n"
                + ") as foo order by التاريخ ;");

        jTable11.setModel(DbUtils.resultSetToTableModel(r));
        jTable11.getColumnModel().getColumn(1).setPreferredWidth(150);

        show_last_row_scroll_jtable(jTable11);
    }

    public void check_conn_available() {
        conn_obj = new db_Connection();
        Joptionpane_message("الاتصال بالبيانات صالح");
    }

    public void copy_bill() {
        int point = jTable3.getSelectedRow();

        if (jTable3.getValueAt(point, 3).toString().trim().equals("فاتورة")) {
            
            try {
                if (jTable4.getRowCount() > 0) {
                    Joptionpane_message("يوجد اسطر بالجدول المراد نسخ البانات عليه");
                    int response = JOptionPane.showConfirmDialog(null, "هل تريد حذف هذه السطور ؟",
                            "", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (response == JOptionPane.OK_OPTION) {
                        DefaultTableModel dtm = (DefaultTableModel) jTable4.getModel();
                        dtm.setNumRows(0);
                    }
                }
                jTable4.getModel().removeTableModelListener(fayez);//كي لا يعمل اكسيبشن عند كل صنف نضيفه وبعد ما ننهي النسخنعيد اضافة الليسنر
                //reset_jpanel_1();//لحتى لما يفرغ البيانات في جدول الفاتورة يفرغها والجدول وكل المربعات فارغة
                //jTabbedPane1.setSelectedIndex(0);
                String bill_id = jTable3.getValueAt(point, 6).toString().trim();
                r = conn_obj.conn_exec("select * from  customer_bills where bill_id=" + bill_id + "");
                r.next();
                jComboBox1.setSelectedItem(jComboBox3.getSelectedItem());
                jTextField1.setText(r.getString("bill_value"));
                jDateChooser1.setDate(r.getDate("bill_date"));
                jComboBox2.setSelectedIndex(r.getInt("bill_location_id") - 1);
                jTextArea1.setText(r.getString("bill_note"));
                jTextField3.setText(r.getString("discount_ratio"));
                jTextField16.setText(r.getString("discount_amount"));
                jTextField15.setText(r.getString("bill_num"));
                //      jTextField19.setText(bill_id); new fayez

                String location_id = r.getString("bill_location_id");
                /////////////////////////////////////////////////// show bill items in jtable////////////
                r = conn_obj.conn_exec("SELECT \n"
                        + "items.main_items.item_name,\n"
                        + "items.main_items.item_id,\n"
                        + "\n"
                        + "items.item_units.unit_name,\n"
                        + "items.item_units.unit_id,\n"
                        + "\n"
                        + "customer_bills_items.id,customer_bills_items.item_id,customer_bills_items.item_bonus,\n"
                        + "customer_bills_items.item_note,customer_bills_items.item_unit,\n"
                        + "customer_bills_items.item_price,customer_bills_items.item_quantity,\n"
                        + "customer_bills_items.bill_id,customer_bills_items.discount_ratio\n"
                        + "FROM items.main_items,items.item_units,customer_bills_items\n"
                        + "WHERE\n"
                        + "items.main_items.item_id = customer_bills_items.item_id\n"
                        + "AND\n"
                        + "items.item_units.unit_id = customer_bills_items.item_unit\n"
                        + "AND\n"
                        + "customer_bills_items.bill_id=" + bill_id + "  order by customer_bills_items.id");

                /*
                 عند تعديل القيد يجب معرفة اذا كان عدد سطور
                 الطلبية المغدلة اكبر من عدد سطور الجدول فتضيغ بغض البيانات
                 */
                DefaultTableModel tm = (DefaultTableModel) jTable4.getModel();
                r.last();
                int r_row_count = r.getRow();
                int jTable_5_row_count = jTable4.getRowCount();
                r.beforeFirst();

                while (r_row_count > jTable_5_row_count) {
                    r_row_count--;
                    tm.addRow(new Object[]{null, null, 1, 1, 1, 1, null, ""});
                   // tm.addRow(new Object[]{null, null, null, 0, null, 0, null, ""});
                    
                }

                while (r.next()) {
                    int result_set_row = r.getRow() - 1;
                    jTable4.setValueAt(r.getString("item_name"), result_set_row, 0);//item_name
                    jTable4.setValueAt(r.getString("unit_name"), result_set_row, 1);//item_unit
                    jTable4.setValueAt(r.getString("item_bonus"), result_set_row, 3);//item_Bonus
                    jTable4.setValueAt(r.getString("item_quantity"), result_set_row, 2);//item_quantity
                    jTable4.setValueAt(String.format("%.2f", r.getFloat("item_price")), result_set_row, 4);//item_price
                    jTable4.setValueAt(r.getString("discount_ratio"), result_set_row, 5);//item_discountRatio
                    jTable4.setValueAt(r.getString("item_note"), result_set_row, 7);//item_discountRatio
                }
                ///////////////////////////////////////end show bill_items in jtable
                jTable4.getModel().addTableModelListener(fayez);//بهذه الخطوة نعيد ما كنا قد حذفناه لكي لا يعمل اكسبشن عند اضافة اي عنصر عند نسخ الاصناف
                jTabbedPane1.setSelectedIndex(0);
                ///////////////////////////////////////end show bill_items in jtable
                // Joptionpane_message("ملاحظة هامة .. يجب أن تعلم أن الفاتورة السابقة المعدلة تم حذفها");
                //conn_obj.exec("DELETE FROM customer_bills where bill_id="+bill_id+"");
            } catch (SQLException ex) {
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void generateHtmlPage(String html) {
        JFrame parentFrame = new JFrame();
        parentFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Write your file name");
        int userSelection = fileChooser.showSaveDialog(parentFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            //System.out.println("Save as file: " + fileToSave.getAbsolutePath());

            File f = new File(fileToSave.getAbsolutePath() + "_account.htm");
            BufferedWriter bw;
            try {
                bw = new BufferedWriter(new FileWriter(f));
                bw.write(html);

                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            }
            parentFrame.dispose();
        }

        //Desktop.getDesktop().browse(f.toURI());
    }

    public void show_customers_accounts(String customer_like_name_or_location) {
       // r = conn_obj.conn_exec("SELECT customer_name as الاسم,1 as الرصيد ,1 as  تاريخ_اخر_فاتورة   from customers where customer_name like '%" + customer_like_name_or_location + "%' OR customer_location like '%" + customer_like_name_or_location + "%' order by customer_name");

        try {
             
        String[] locations_or_names = customer_like_name_or_location.split("--");
        //
        String catagory=mecha.jComboBox14.getSelectedItem().toString().trim();
        String stm="";
        
        ///////
        if(!catagory.equals("------"))
        for (int i=0; i < locations_or_names.length; i++) {
            if(i!=0)
                stm+="union all ";//نتجنب اول مرة
            
            stm+="(select DISTINCT customer_name as  الاسم  ,1 as الرصيد ,1 as  تاريخ_اخر_فاتورة     ,customer_tell "
                    + "from "
                    + "(select customers.customer_name,customers.customer_tell,customers.customer_location,customers.customer_catagory_id,user_privileg_on_customer_catag.user_id_fk,user_privileg_on_customer_catag.customer_catagory_fk ,"
                    + "customer_catagory.catagory_name , customer_catagory.catagory_id   \n"
                    + "from customers,user_privileg_on_customer_catag,customer_catagory \n"
                    + "where \n"
                    + " user_privileg_on_customer_catag.user_id_fk=(select user_id from users where user_name like '" + user_name + "') and\n"
                    + " customer_catagory_id=customer_catagory_fk and "
                    + "customer_catagory.catagory_name='" + catagory + "' and\n" 
                    +" customer_catagory.catagory_id=customers.customer_catagory_id and\n"
                    + " (customer_name like '%" + locations_or_names[i] + "%' OR customer_location like '%" + locations_or_names[i] + "%' ) order by customer_name)as dd order by customer_name)";
        }
        ///////
        ///////
        else
        for (int i=0; i < locations_or_names.length; i++) {
            if(i!=0)
                stm+="union all ";//نتجنب اول مرة
            
            stm+="(select DISTINCT customer_name as  الاسم  ,1 as الرصيد ,1 as  تاريخ_اخر_فاتورة     ,customer_tell "
                    + "from "
                    + "(select customers.customer_name,customers.customer_tell,customers.customer_location,customers.customer_catagory_id,user_privileg_on_customer_catag.user_id_fk,user_privileg_on_customer_catag.customer_catagory_fk "
                    + "  \n"
                    + "from customers,user_privileg_on_customer_catag,customer_catagory \n"
                    + "where \n"
                    + " user_privileg_on_customer_catag.user_id_fk=(select user_id from users where user_name like '" + user_name + "') and\n"
                    + "  "
                    + "\n" 
                    +" customer_catagory.catagory_id=customers.customer_catagory_id and\n"
                    + " (customer_name like '%" + locations_or_names[i] + "%' OR customer_location like '%" + locations_or_names[i] + "%' ) order by customer_name)as dd order by customer_name)";
        }
        ///////
        

            System.out.println(stm);
            r = conn_obj.conn_exec(stm);

            show_obj = new show_bill_items();
            show_obj.show_ven_cus_accounts(r);
            String y;
            for (int i = 0; i < show_obj.jTable1.getModel().getRowCount(); i++) {
                y = String.valueOf((String) show_obj.jTable1.getModel().getValueAt(i, 0));
                show_obj.jTable1.getModel().setValueAt(get_customer_account_sum(y), i, 1);
                show_obj.jTable1.getModel().setValueAt(show_date_of_last_customer_bill(y), i, 2);
            }
            show_obj.table_accpunt_sum();

        } catch (Exception ex) {
            Joptionpane_message(ex.getMessage());
            Joptionpane_message(ex.getMessage());
        }

    }
    
    public void show_vendors_accounts(String vendor) {
        r = conn_obj.conn_exec("SELECT vendor_name as الاسم,1 as الرصيد ,1 as  تاريخ_اخر_فاتورة   from vendors where vendor_name like '%" + vendor + "%'  order by vendor_name");

        try {
            
            show_obj = new show_bill_items();
            show_obj.show_ven_cus_accounts(r);
            String y;
            for (int i = 0; i < show_obj.jTable1.getModel().getRowCount(); i++) {
                y = String.valueOf((String) show_obj.jTable1.getModel().getValueAt(i, 0));
                show_obj.jTable1.getModel().setValueAt(get_vendor_account_sum(y), i, 1);
            }
            //show_obj.table_accpunt_sum();

        } catch (Exception ex) {
            Joptionpane_message(ex.getMessage());
            Joptionpane_message(ex.getMessage());
        }

    }
  
    //validate date
    final static String DATE_FORMAT = "yyyy-MM-dd";

    public static boolean isDateValid(String date) {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public void reset_return_customer_bill() {
        jComboBox8.setSelectedIndex(0);
        jComboBox9.setSelectedIndex(0);
        jTextField34.setText("0");
        jTextField30.setText("");
        jTextArea5.setText("");
        jDateChooser6.setDate(get_date_jdate());
        DefaultTableModel model = (DefaultTableModel) jTable12.getModel();
        model.setRowCount(0);
        /////////////////////////////////////////reset jtable4///////////////

    }
     public void reset_return_vendor_bill() {
        jComboBox_return_ven_store.setSelectedIndex(0);
        jTextField_return_ven_bill_value.setText("0");
        jTextField_return_ven_bil_numberl.setText("");
        jTextArea_return_ven_bill_note.setText("");
        jDateChooser_return_ven_bill_date.setDate(get_date_jdate());
        DefaultTableModel model = (DefaultTableModel) jTable_return_ven_bill_items.getModel();
        model.setRowCount(0);

    }
    public void prepare_payment_sum()
    {
         try {
            float checks_sum = 0;
            float cash=0;
            
            for (int i = 0; i < jTable13.getRowCount(); i++) {
                if(!jTable13.getValueAt(i, 2).toString().trim().equals(""))//اذا مربع قيمة الشك لا يساوي صفر يقوم بجمعه
                checks_sum = checks_sum + Float.valueOf(jTable13.getValueAt(i, 2).toString());
            }
            if(!jTextField12.getText().trim().equals(""))
            {
               cash=Float.valueOf(jTextField12.getText());
            }
            
            float final_sum = checks_sum + cash;
            jTextField11.setText(Float.toString(final_sum));
        } catch (Exception e) {
            Joptionpane_message(e.getMessage());
        }
    }
    public String show_date_of_last_customer_bill(String customer_name) {
        try {
            r = conn_obj.conn_exec(" select bill_date from customer_bills \n"
                    + "where bill_customer_id =(select customer_id from customers where customer_name='" + customer_name + "')\n"
                    + "order by bill_date desc limit 1; ");
            if (!r.next()) {
                return ("Zero");
            } else {
                r.beforeFirst();
                r.next();
                return r.getString(1);
            }

        } catch (SQLException e) {
            Joptionpane_message(e.getMessage());
        }
        return ("");
    }
        public void jcombo_table_bank_names()
{
    try {
        r=conn_obj.conn_exec("select bank_name from check_bank");
        while(r.next())
            Bank_jcomboBox.addItem(r.getString(1));       
        
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this,ex.getMessage());
    }
    Bank_jcomboBox.setFont(new java.awt.Font("Arial", 1, 18));
    // AutoCompleteDecorator.decorate(Bank_jcomboBox);

    

}
public void check_there_is_no_lose() throws Exception
{
    boolean is=false;
    String msg="";
for (int i = 0; i < jTable4.getRowCount(); i++) {
            try {
                r = conn_obj.conn_exec("select value \n"
                        + "                    from\n"
                        + "                    ( \n"
                        + "                    select \n"
                        + "                      items.item_units.unit_name,items.item_units.unit_id,\n"
                        + "                      items.main_items.item_name,items.main_items.item_id,items.main_items.item_value*items.item_relations.item_relation as value,\n"
                        + "                      items.item_relations.item_id,items.item_relations.item_unit,items.item_relations.item_price_hole,items.item_relations.item_relation as relation\n"
                        + "                               \n"
                        + "                                from items.item_units,items.main_items,items.item_relations\n"
                        + "                                                               where \n"
                        + "                                items.main_items.item_name LIKE '" + jTable4.getValueAt(i, 0) + "'\n"
                        + "                                AND \n"
                        + "                                items.item_relations.item_id= items.main_items.item_id\n"
                        + "                                and\n"
                        + "                                items.item_relations.item_unit=items.item_units.unit_id\n"
                        + "                                AND\n"
                        + "                                items.item_units.unit_name='" + jTable4.getValueAt(i, 1) + "'\n"
                        + "                                \n"
                        + "                               )  as alias");
                r.next();
                double price = Double.parseDouble(jTable4.getValueAt(i, 4).toString());
                double value = r.getDouble("value");
                
                if(price<=value)
                {
                    is=true;
                    msg+="يوجد خسارة في الصنف رقم: "+(i+1)+"\n"+"اسم الصنف :"+jTable4.getValueAt(i, 0).toString()+"\n"+"السعر علينا:"+value+"\n"+"سعر المباع :"+price+"\n";
                }
            } catch (SQLException ex) {
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
                     if(is==true)
                     {
                        Joptionpane_message(msg);
                      if (check_yes_or_no_question("هل تريد انهاء الادخال؟") == true) {
                    throw new Exception("تم الالغاء!!");
                }
                    
                     }

            
}
public void prepare_stm_that_return_quantity_to_store()//عند تعديل قيد فاتورة الارجاع ننقص كميات الفاتورة من المحزون
{
    try {
        r=conn_obj.conn_exec("select location_id from location where location_name='"+jComboBox11.getSelectedItem().toString()+"'");
        r.next();
        String location_id=r.getString("location_id");
        
        for (int i = 0; i < jTable15.getRowCount(); i++) {
            try {
                float quantity = Float.parseFloat(jTable15.getValueAt(i, 2).toString());
                float bounus = Float.parseFloat(jTable15.getValueAt(i, 3).toString());
                float net_Q=quantity+bounus;
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
                        "items.item_relations.item_relation*" +net_Q + " as quantity\n" +
                        "\n" +
                        "from items.main_items,items.item_units,items.item_relations\n" +
                        "\n" +
                        "where \n" +
                        "items.main_items.item_name='" + jTable15.getValueAt(i, 0) + "'  AND\n" +
                        "items.item_units.unit_name='" + jTable15.getValueAt(i, 1) + "'  AND\n" +
                        "items.main_items.item_id=items.item_relations.item_id AND\n" +
                        "items.item_units.unit_id=items.item_relations.item_unit )as anyThing");
                r.next();
                
                double quantity_to_increse=r.getDouble("quantity")*-1;
                int item_id=r.getInt("main_item_id");
                String store_to_update="store_id_"+location_id;//store_id_1
                stm_to_return_quantity_to_store+="update items.inventory set "+store_to_update+" = "+store_to_update+" + "+quantity_to_increse+" where item_id="+item_id+";";
                
            } catch (SQLException ex) {
                Logger.getLogger(modify_customer_bill.class.getName()).log(Level.SEVERE, null, ex);
            }    
        }
    } catch (SQLException ex) {
        Logger.getLogger(modify_customer_bill.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    
}
public void ven_prepare_payment_sum()
    {
         try {
            float checks_sum = 0;
            for (int i = 0; i < jTable_van_pay_my_checks.getRowCount(); i++) {
                checks_sum = checks_sum + Float.valueOf(jTable_van_pay_my_checks.getValueAt(i, 2).toString());
            }
            for (int i = 0; i < jTable_ven_edorsed_checks.getRowCount(); i++) {
                checks_sum = checks_sum + Float.valueOf(jTable_ven_edorsed_checks.getValueAt(i, 3).toString());
            }
            float final_sum = checks_sum + Float.valueOf(jTextField_ven_pay_cash.getText());
            jTextField_ven_pay_total.setText(Float.toString(final_sum));
        } catch (Exception e) {
            Joptionpane_message(e.getMessage());
        }
    }

void search_in_vendor_bills_about_item() {
        if (jCheckBox8.isSelected()) {
            jTextField33.setText("");
        }
        String word = (String) jTable10.getValueAt(jTable10.getSelectedRow(), 0);
        r = conn_obj.conn_exec("select id as رقم_الفاتورة ,discount_ratio as خصم_على_الصنف ,dis as خصم_نسبة_على_الفاتورة ,vendor_name as المورد,bill_date التاريخ ,item_price السعر,item_quantity العدد,unit_name الوحدة,item_name الصنف from (\n"
                + "\n"
                + "select vendors.vendor_id,\n"
                + "vendors.vendor_name,\n"
                + "vendor_bills.bill_id as id,\n"
                + "vendor_bills.discount_ratio as dis,\n"
                + "vendor_bills.bill_date,\n"
                + "vendor_bills.bill_vendor_id,\n"
                + "vendor_bills_items.bill_id,\n"
                + "vendor_bills_items.item_id,\n"
                + "vendor_bills_items.item_price,\n"
                + "vendor_bills_items.item_quantity,\n"
                + "vendor_bills_items.item_bonus,\n"
                + "vendor_bills_items.discount_ratio,\n"
                + "vendor_bills_items.item_unit,\n"
                + "items.main_items.item_name,\n"
                + "items.main_items.item_id,\n"
                + "items.item_units.unit_id,\n"
                + "items.item_units.unit_name \n"
                + "\n"
                + "from vendor_bills_items,vendors,vendor_bills,items.main_items,items.item_units \n"
                + "where item_name LIKE '%" + word + "%'\n"
                + "and vendor_bills.bill_id=vendor_bills_items.bill_id\n"
                + "and bill_vendor_id=vendor_id\n"
                + "and vendors.vendor_name like '%" + jTextField33.getText().trim() + "%'"
                + "and vendor_bills_items.item_unit=items.item_units.unit_id\n"
                + "and vendor_bills_items.item_id=items.main_items.item_id\n"
                + "\n"
                + ") as foo order by التاريخ ;");

        jTable14.setModel(DbUtils.resultSetToTableModel(r));
        jTable14.getColumnModel().getColumn(1).setPreferredWidth(150);

        show_last_row_scroll_jtable(jTable14);
    }

private void update_ven_name() {

        try {
            String name=jTextField23.getText().trim();
            String tel=jTextField21.getText().trim();
            String address=jTextField22.getText().trim();
            String id=jLabel92.getText();
            if(!name.equals(""))
            {
                conn_obj.get_st().executeUpdate("UPDATE vendors SET vendor_name='" + name + "' where vendor_id=" + id + ";");
                conn_obj.get_st().executeUpdate("UPDATE vendors SET vendor_tell='" + tel + "' where vendor_id=" + id + ";");
                conn_obj.get_st().executeUpdate("UPDATE vendors SET vendor_location='" + address + "' where vendor_id=" + id + ";");
                r = conn_obj.conn_exec("select vendor_id as ID,vendor_name As Name,vendor_tell as Tell,vendor_location as location from vendors");
            jTable_shoe_ven_names.setModel(DbUtils.resultSetToTableModel(r));
            /////
            jTextField23.setText("");
            jTextField22.setText("");
            jTextField21.setText("");
            jLabel92.setText("");

            JOptionPane.showMessageDialog(null, "تم تعديل الاسم في الجدول الاعلى");
            }
            else
            {
                JOptionPane.showMessageDialog(null, "الاسم المعدّل فارغ   !!");
            }
            
        } catch (SQLException ex) {
        }
    }
public void delete_ven_name() throws SQLException, Exception {

        try {
            if (jTable_shoe_ven_names.getValueAt(jTable_shoe_ven_names.getSelectedRow(), 1).equals("------")) {
                throw new Exception("خطأ في اختيار الاسم !!!!");
            }
            conn_obj.get_con().setAutoCommit(false);
            conn_obj.get_st().execute("delete from vendors where vendor_id =" + jTable_shoe_ven_names.getValueAt(jTable_shoe_ven_names.getSelectedRow(), 0) + "");
            Joptionpane_confirm("هل انت متأكد من حذف الاسم", "لم يتم حذف الاسم");
            conn_obj.get_con().commit();
            Joptionpane_message("تم الحذف");
            ///** لاعادة تحديث جدول اسماء الزبائب بعد حذف الاسم
            r = conn_obj.conn_exec("select vendor_id as ID,vendor_name As Name,vendor_tell as Tell,vendor_location as location from vendors");
            jTable_shoe_ven_names.setModel(DbUtils.resultSetToTableModel(r));
       //**end
            //**To refresh comboBox 3 and 1
            // update_jcomboBox_1_3_4();
            //end
        } catch (Exception e) {
            Joptionpane_message(e.getMessage());
            conn_obj.get_con().rollback();
        }
    }

 public String ven_show_date_of_last_customer_bill(String customer_name)
    {
        try{
        r = conn_obj.conn_exec(" select bill_date from vendor_bills \n" +
"where bill_vendor_id =(select vendor_id from vendors where vendor_name='"+customer_name+"')\n" +
"order by bill_date desc limit 1; ");
        if (!r.next()) {
                return ("No Date");
            } else {
                r.beforeFirst();
                r.next();
                return r.getString(1);
            }

        } catch (SQLException e) {
            Joptionpane_message(e.getMessage());
        }
        return ("");
    }
 
 
 public void create_ven_account_table() {
        if (jPanel_ven_acount_details.isVisible()) {
            try {
                String vendor = jComboBox_ven_name_for_account_details.getSelectedItem().toString();
                r = conn_obj.conn_exec("select  0.0 as المجموع,القيمة,ملاحظة,الحركة ,المكان, التاريخ ,رقم_القيد   ,رقم_الفاتورة  from(\n"
                        + "SELECT round((bill_value::numeric),2)::float8 as القيمة,record_time,bill_location_id,location.location_id,bill_note as ملاحظة,'فاتورة' as الحركة,location.location_name as المكان,bill_date as التاريخ,bill_id as رقم_القيد,bill_num as رقم_الفاتورة\n"
                        + "FROM vendor_bills,location\n"
                        + "           where bill_vendor_id=(select vendor_id as cus_name from vendors where vendor_name='" + vendor + "') and accounted=false and bill_location_id=location_id\n"
                        + "                        UNION\n"
                        + "SELECT round((return_bill_value::numeric),2)::float8 as القيمة,record_time,return_bill_location_id,location.location_id,return_bill_note as ملاحظة,'ف-مرتجع' as الحركة,location.location_name as المكان,return_bill_date as التاريخ,return_bill_id as رقم_القيد,return_bill_num as رقم_الفاتورة\n"
                        + "FROM return_vendor_bills,location\n"
                        + "           where return_bill_vendor_id=(select vendor_id as cus_name from vendors where vendor_name='" + vendor + "') and accounted=false and return_bill_location_id=location_id\n"
                        + "                        UNION\n"
                        + "                      \n"
                        + "SELECT round((payment_value::numeric),2)::float8,record_time,0 ,null,payment_note,'دفعة','---',payment_date,payment_id,'---'\n"
                        + "                        FROM vendor_payments,location \n"
                        + "                        where vendor_id_fk=(select vendor_id as cus_name from vendors where vendor_name='" + vendor + "') and accounted=false \n"
                        + "                        UNION\n"
                        + "                      \n"
                        + "SELECT round((discount_value::numeric),2)::float8,record_time,0 ,null,discount_note,'خصم','---',discount_date,discount_id,'---'\n"
                        + "                        FROM vendor_discount \n"
                        + "                        where vendor_id_fk=(select vendor_id as cus_name from vendors where vendor_name='" + vendor + "') and accounted=false order by التاريخ, record_time\n"
                        + ") as ffff");
                jTable_show_ven_account_details.setModel(DbUtils.resultSetToTableModel(r));
                render_table3();
                if (jTable_show_ven_account_details.getRowCount() != 0) {
                    jTable_show_ven_account_details.setValueAt(0.0, 0, 0);
                }
                for (int i = 0; i < jTable_show_ven_account_details.getRowCount(); i++) {
                    if (i == 0) {
                        if (jTable_show_ven_account_details.getValueAt(i, 3).equals("فاتورة")) {
                            float value = Float.parseFloat(jTable_show_ven_account_details.getModel().getValueAt(i, 1).toString());
                            jTable_show_ven_account_details.setValueAt(String.format("%.2f", value), i, 0);
                        } else if (jTable_show_ven_account_details.getValueAt(i, 3).equals("دفعة")) {
                            float value = -1 * Float.parseFloat(jTable_show_ven_account_details.getModel().getValueAt(i, 1).toString());
                            jTable_show_ven_account_details.setValueAt(String.format("%.2f", value), i, 0);
                        } else if (jTable_show_ven_account_details.getValueAt(i, 3).equals("خصم")) {
                            float value = -1 * Float.parseFloat(jTable_show_ven_account_details.getModel().getValueAt(i, 1).toString());
                            jTable_show_ven_account_details.setValueAt(String.format("%.2f", value), i, 0);
                        }else if (jTable_show_ven_account_details.getValueAt(i, 3).equals("ف-مرتجع")) {
                            float value = -1 * Float.parseFloat(jTable_show_ven_account_details.getModel().getValueAt(i, 1).toString());
                            jTable_show_ven_account_details.setValueAt(String.format("%.2f", value), i, 0);
                        }
                    } else {
                        if (jTable_show_ven_account_details.getValueAt(i, 3).equals("فاتورة")) {
                            float pre_value = Float.parseFloat(jTable_show_ven_account_details.getModel().getValueAt(i - 1, 0).toString());
                            float value2 = Float.parseFloat(jTable_show_ven_account_details.getModel().getValueAt(i, 1).toString());
                            jTable_show_ven_account_details.setValueAt(String.format("%.2f", pre_value + value2), i, 0);
                        } else if (jTable_show_ven_account_details.getValueAt(i, 3).equals("دفعة")) {
                            float pre_value = Float.parseFloat(jTable_show_ven_account_details.getModel().getValueAt(i - 1, 0).toString());
                            float value2 = Float.parseFloat(jTable_show_ven_account_details.getModel().getValueAt(i, 1).toString());
                            jTable_show_ven_account_details.setValueAt(String.format("%.2f", pre_value - value2), i, 0);
                        }else if (jTable_show_ven_account_details.getValueAt(i, 3).equals("خصم")) {
                            float pre_value = Float.parseFloat(jTable_show_ven_account_details.getModel().getValueAt(i - 1, 0).toString());
                            float value2 = Float.parseFloat(jTable_show_ven_account_details.getModel().getValueAt(i, 1).toString());
                            jTable_show_ven_account_details.setValueAt(String.format("%.2f", pre_value - value2), i, 0);
                        } else if (jTable_show_ven_account_details.getValueAt(i, 3).equals("ف-مرتجع")) {
                            float pre_value = Float.parseFloat(jTable_show_ven_account_details.getModel().getValueAt(i - 1, 0).toString());
                            float value2 = Float.parseFloat(jTable_show_ven_account_details.getModel().getValueAt(i, 1).toString());
                            jTable_show_ven_account_details.setValueAt(String.format("%.2f", pre_value - value2), i, 0);
                        }
                    }

                }

            } catch (Exception ex) {
               
            }

        }
 }
    float get_vendor_account_sum(int customer_id) {
        {
            try {
                r = conn_obj.conn_exec("select sum (bill_value) from vendor_bills where bill_vendor_id=" + customer_id + " and accounted=false");
                r.next();
                float sum_bills_values = r.getFloat(1);
                r = conn_obj.conn_exec("select sum (payment_value) from vendor_payments where vendor_id_fk=" + customer_id + " and accounted=false");
                r.next();
                float sum_payments = r.getFloat(1);
                r = conn_obj.conn_exec("select sum (discount_value) from vendor_discount where vendor_id_fk=" + customer_id + " and accounted=false");
                r.next();
                float sum_discount = r.getFloat(1);
                r = conn_obj.conn_exec("select sum (return_bill_value) from return_vendor_bills where return_bill_vendor_id=" + customer_id + " and accounted=false");
                r.next();
                float sum_returns = r.getFloat(1);

                float result=sum_bills_values - sum_payments - sum_discount-sum_returns;
                return (Math.round(result*100)/100.0f);
            } catch (SQLException ex) {
                Joptionpane_message(ex.getMessage() + ex.getSQLState());
            } catch (Exception ex) {
                Joptionpane_message(ex.getMessage());

            }
            return 0;
        }
    }

    float get_vendor_account_sum(String vendor_name) {
        {
            try {
                r = conn_obj.conn_exec("select vendor_id from vendors where vendor_name='" + vendor_name + "'");
                r.next();
                int customer_id = r.getInt(1);
                r = conn_obj.conn_exec("select sum (bill_value) from vendor_bills where bill_vendor_id=" + customer_id + " and accounted=false");
                r.next();
                float sum_bills_values = r.getFloat(1);
                r = conn_obj.conn_exec("select sum (payment_value) from vendor_payments where vendor_id_fk=" + customer_id + " and accounted=false");
                r.next();
                float sum_payments = r.getFloat(1);
                r = conn_obj.conn_exec("select sum (discount_value) from vendor_discount where vendor_id_fk=" + customer_id + " and accounted=false");
                r.next();
                float sum_discount = r.getFloat(1);
                r = conn_obj.conn_exec("select sum (return_bill_value) from return_vendor_bills where return_bill_vendor_id=" + customer_id + " and accounted=false ");
                r.next();
                float sum_returns = r.getFloat(1);

                float result=sum_bills_values - sum_payments -sum_discount- sum_returns;
                return (Math.round(result*100)/100.0f);
            } catch (SQLException ex) {
                Joptionpane_message(ex.getMessage() + ex.getSQLState());
            } catch (Exception ex) {
                Joptionpane_message(ex.getMessage());
            }
            return 0;
        }
    }
 public void reset_vendor_add_bill() {
        jCombo_vendor_name.setSelectedIndex(0);
        jComboBox_bill_store.setSelectedIndex(0);
        jTextField_bill_value.setText("0");
        jTextField_bill_number.setText("");
        jTextField_bill_before_dis.setText("0");
        jTextField_bill_discount.setText("0");
        jTextField_bill_dis_ratio.setText("0");
        jTextArea_bill_note.setText("");
        jDateChooser_bill_date.setDate(get_date_jdate());
        DefaultTableModel model = (DefaultTableModel) jTable_bill_items.getModel();
        model.setRowCount(0);
        /////////////////////////////////////////reset jtable4///////////////

////////////////////////////////////////////////////////////////////
    }
  public void reset_panel_vendor_payment() {
        jComboBox_ven_name_pay.setSelectedIndex(0);
        jTextField_ven_pay_total.setText("0");
        jTextField_ven_pay_cash.setText("0");
        jTextField_ven_pay_receive.setText("");
        jTextField_ven_pay_helder.setText("");
        jTextArea_ven_pay_note.setText("");
        jDateChooser_ven_pay_date.setDate(get_date_jdate());
        DefaultTableModel model = (DefaultTableModel) jTable_van_pay_my_checks.getModel();
        model.setRowCount(0);
        model = (DefaultTableModel) jTable_ven_edorsed_checks.getModel();
        model.setRowCount(0);
    }
  
  public void show_bill_items_ven() {
        int point = jTable_show_ven_account_details.getSelectedRow();
//اذا الحركة فاتورة
        if (jComboBox_ven_name_for_account_details.getSelectedIndex() != 0 && jTable_show_ven_account_details.getValueAt(point, 3).toString().trim().equals("فاتورة"))//لحتى ما تعمل اكسبشن لما يختار اشي على الجدول بدون ما يكون فيندور محدد
        {
            jPanel14.setVisible(true);
            try {
                String bill_id = jTable_show_ven_account_details.getValueAt(point, 6).toString().trim();
                r = conn_obj.conn_exec("select discount_ratio,discount_amount,round(CAST(bill_value as numeric),1)as bill,bill_note,bill_date  from  vendor_bills where bill_id=" + bill_id + "");
                r.next();
                String bill_date = "تاريخ الفاتورة :  " + r.getString("bill_date");
                String dis_ratio = "نسبة خصم الفاتورة بالمئة = " + r.getString("discount_ratio") + " %";
                String dis_value = "قيمة الخصم المباشر على الفاتورة = " + r.getString("discount_amount");
                String bill_value = "قيمة الفاتورة النهائي يساوي =  " + r.getString("bill");
                String bill_note = "ملاحظات =  " + r.getString("bill_note");
                /////////////////////////////////////////////////// show bill items in jtable////////////
                r = conn_obj.conn_exec("select item_note as ملاحظة ,(SELECT round(CAST(price*item_quantity as numeric),1) as المبلغ),price as السعر_بعد,discount_ratio as نسبة_الخصم,item_price as السعر,item_bonus as بونص,item_quantity as الكمية,unit_name as الوحدة,item_name as اسم_الصنف from ("
                        + "SELECT \n"
                        + "items.main_items.item_name,\n"
                        + "items.main_items.item_id,\n"
                        + "\n"
                        + "items.item_units.unit_name,\n"
                        + "items.item_units.unit_id,\n"
                        + "\n"
                        + "vendor_bills_items.id,vendor_bills_items.item_id,vendor_bills_items.item_bonus,\n"
                        + "vendor_bills_items.item_note,vendor_bills_items.item_unit,\n"
                        + "vendor_bills_items.item_price,vendor_bills_items.item_quantity,\n"
                        + "vendor_bills_items.bill_id,vendor_bills_items.discount_ratio,\n"
                        + "(1-vendor_bills_items.discount_ratio/100)*vendor_bills_items.item_price as price\n"
                        + "FROM items.main_items,items.item_units,vendor_bills_items\n"
                        + "WHERE\n"
                        + "items.main_items.item_id = vendor_bills_items.item_id\n"
                        + "AND\n"
                        + "items.item_units.unit_id = vendor_bills_items.item_unit\n"
                        + "AND\n"
                        + "vendor_bills_items.bill_id=" + bill_id + " order by vendor_bills_items.id)as dvfc");

                jPanel14.setVisible(true);

                customer_bill.pack();
                jTable6.getColumnModel().getColumn(2).setCellRenderer(new CustomTableCellRenderer());
                jTable6.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                jTable6.getColumnModel().getColumn(1).setPreferredWidth(150);
                jLabel31.setText(dis_ratio);
                jLabel32.setText(dis_value);
                jLabel33.setText(bill_value);
                jLabel39.setText(bill_date);
                jLabel57.setText(bill_note);

                jTable6.getTableHeader().setFont(new Font("arial", Font.BOLD, 20));

                jTable6.setModel(DbUtils.resultSetToTableModel(r));

                jTable6.getColumnModel().getColumn(8).setMinWidth(200);
                int width = (int) get_screen_dimension().getWidth() - 80;
                int height = (int) get_screen_dimension().getHeight() - 100;
                customer_bill.setSize(width, height);
                customer_bill.setLocationRelativeTo(this);
                customer_bill.setVisible(true);
                jButton20.requestFocus();

            } catch (Exception ex) {
                Joptionpane_message(ex.getMessage());
            }
        } //اذا الحركة دفعة يتغير 
        else if (jComboBox_ven_name_for_account_details.getSelectedIndex() != 0 && jTable_show_ven_account_details.getValueAt(point, 3).toString().trim().equals("دفعة")) {

            try {
                String payment_id = jTable_show_ven_account_details.getValueAt(point, 6).toString().trim();
                jframe_to_modify_vendor_payment = new modify_vendor_payment(this, payment_id);
                r = conn_obj.conn_exec("select * from  vendor_payments where payment_id=" + payment_id + "");
                r.next();
                
                float hole_payment_value = r.getFloat("payment_value");
                
                jframe_to_modify_vendor_payment.jComboBox4.setSelectedItem(jComboBox_ven_name_for_account_details.getSelectedItem());
                jframe_to_modify_vendor_payment.jDateChooser2.setDate(r.getDate("payment_date"));
                jframe_to_modify_vendor_payment.jTextField14.setText(r.getString("payment_rec"));
                jframe_to_modify_vendor_payment.jTextField19.setText(r.getString("payment_maker"));
                jframe_to_modify_vendor_payment.jTextArea2.setText(r.getString("payment_note"));
                //
                r = conn_obj.conn_exec("select sum(check_value)as s from  customer_checks where vendor_payment_id=" + payment_id + "");
                r.next();
                float sum_of_customer_checks = r.getFloat("s");
                r = conn_obj.conn_exec("select sum(check_value)as v from  checks where check_to_vendor_payment_id=" + payment_id + "");
                r.next();
                float sum_of_checks = r.getFloat("v");
                float net_of_cash_payments = hole_payment_value - sum_of_checks-sum_of_customer_checks;
                //
                
                r = conn_obj.conn_exec("select check_owner,check_endorser,check_no,check_due_date,check_note,check_value,check_bank,id from  customer_checks where vendor_payment_id=" + payment_id + "");
                while (r.next()) {
                    DefaultTableModel tm = (DefaultTableModel) jframe_to_modify_vendor_payment.jTable14.getModel();
                    tm.addRow(new Object[]{jframe_to_modify_vendor_payment.jTable14.getRowCount() + 1,r.getString("id"), r.getString("check_no"), r.getFloat("check_value"), r.getString("check_owner"), r.getString("check_endorser"), r.getString("check_due_date"), r.getString("check_bank"), r.getString("check_note")});
                }
                
                r = conn_obj.conn_exec("select check_number,check_value,check_due_date,(select bank_name from check_bank where bank_id=check_bank),check_note from checks where check_to_vendor_payment_id=" + payment_id + "");
                while (r.next()) {
                    DefaultTableModel tm = (DefaultTableModel) jframe_to_modify_vendor_payment.jTable13.getModel();
                    tm.addRow(new Object[]{jframe_to_modify_vendor_payment.jTable13.getRowCount() + 1,r.getString("check_number"), r.getFloat("check_value"), r.getString("check_due_date"), r.getString("bank_name"), r.getString("check_note")});
                }
                
                jframe_to_modify_vendor_payment.jTextField12.setText(Float.toString(net_of_cash_payments));
                jframe_to_modify_vendor_payment.jButton5.setVisible(false);
                jframe_to_modify_vendor_payment.jButton8.setVisible(false);
                jframe_to_modify_vendor_payment.jButton12.setVisible(false);
                jframe_to_modify_vendor_payment.jButton13.setVisible(false);
                jframe_to_modify_vendor_payment.jButton14.setVisible(false);
                jframe_to_modify_vendor_payment.jButton30.setVisible(false);
                jframe_to_modify_vendor_payment.jButton28.setVisible(false);
                jframe_to_modify_vendor_payment.jComboBox4.setVisible(false);
                jframe_to_modify_vendor_payment.jLabel37.setVisible(false);
                jframe_to_modify_vendor_payment.jLabel38.setVisible(false);
                jframe_to_modify_vendor_payment.jLabel22.setVisible(false);
                ///
                jframe_to_modify_vendor_payment.jComboBox4.setEditable(false);
                jframe_to_modify_vendor_payment.jDateChooser2.setEnabled(false);
                jframe_to_modify_vendor_payment.jTextField14.setEditable(false);
                jframe_to_modify_vendor_payment.jTextField19.setEditable(false);
                jframe_to_modify_vendor_payment.jTextArea2.setEditable(false);
                jframe_to_modify_vendor_payment.jTable13.setEnabled(false);
                //
                jframe_to_modify_vendor_payment.setVisible(true);
                jframe_to_modify_vendor_payment.prepare_payment_sum();
            } catch (SQLException ex) {
                
            }
                         
                    
        }
        //اذا الحركة فاتورة ارجاع
        else if (jComboBox_ven_name_for_account_details.getSelectedIndex() != 0 && jTable_show_ven_account_details.getValueAt(point, 3).toString().trim().equals("ف-مرتجع"))//لحتى ما تعمل اكسبشن لما يختار اشي على الجدول بدون ما يكون فيندور محدد
        {
            jPanel14.setVisible(true);
            try {
                String bill_id = jTable_show_ven_account_details.getValueAt(point, 6).toString().trim();
                r = conn_obj.conn_exec("select round(CAST(return_bill_value as numeric),1)as bill,return_bill_note,return_bill_date  from  return_vendor_bills where return_bill_id=" + bill_id + "");
                r.next();
                String bill_date = "تاريخ الفاتورة :  " + r.getString("return_bill_date");
                String bill_value = "قيمة الفاتورة النهائي يساوي =  " + r.getString("bill");
                String bill_note = "ملاحظات =  " + r.getString("return_bill_note");
                /////////////////////////////////////////////////// show bill items in jtable////////////
                r = conn_obj.conn_exec("select return_item_note as ملاحظة ,(SELECT round(CAST(price*return_item_quantity as numeric),1) as المبلغ),price as السعر_بعد,return_item_price as السعر,return_item_quantity as الكمية,unit_name as الوحدة,item_name as اسم_الصنف from ("
                        + "SELECT \n"
                        + "items.main_items.item_name,\n"
                        + "items.main_items.item_id,\n"
                        + "\n"
                        + "items.item_units.unit_name,\n"
                        + "items.item_units.unit_id,\n"
                        + "\n"
                        + "return_vendor_bills_items.id,return_vendor_bills_items.return_item_id,\n"
                        + "return_vendor_bills_items.return_item_note,return_vendor_bills_items.return_item_unit,\n"
                        + "return_vendor_bills_items.return_item_price,return_vendor_bills_items.return_item_quantity,\n"
                        + "return_vendor_bills_items.return_bill_id,\n"
                        + "return_vendor_bills_items.return_item_price as price\n"
                        + "FROM items.main_items,items.item_units,return_vendor_bills_items\n"
                        + "WHERE\n"
                        + "items.main_items.item_id = return_vendor_bills_items.return_item_id\n"
                        + "AND\n"
                        + "items.item_units.unit_id = return_vendor_bills_items.return_item_unit\n"
                        + "AND\n"
                        + "return_vendor_bills_items.return_bill_id=" + bill_id + " order by return_vendor_bills_items.id)as dvfc");

                jPanel14.setVisible(true);

                customer_bill.pack();
                jTable6.getColumnModel().getColumn(2).setCellRenderer(new CustomTableCellRenderer());
                jTable6.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                jTable6.getColumnModel().getColumn(1).setPreferredWidth(150);
                jLabel31.setText("");
                jLabel32.setText("");
                jLabel33.setText(bill_value);
                jLabel39.setText(bill_date);
                jLabel57.setText(bill_note);

                jTable6.getTableHeader().setFont(new Font("arial", Font.BOLD, 20));

                jTable6.setModel(DbUtils.resultSetToTableModel(r));

                jTable6.getColumnModel().getColumn(6).setMinWidth(200);
                int width = (int) get_screen_dimension().getWidth() - 80;
                int height = (int) get_screen_dimension().getHeight() - 100;
                customer_bill.setSize(width, height);
                customer_bill.setLocationRelativeTo(this);
                customer_bill.setVisible(true);
                jButton20.requestFocus();

            } catch (Exception ex) {
                Joptionpane_message(ex.getMessage());
            }
        }
    }

  
          
        
        
        public void ven_prepare_stm_that_return_quantity_to_store()//عند تعديل قيد فاتورة الارجاع ننقص كميات الفاتورة من المحزون
{
    ven_stm_to_return_quantity_to_store="";
    try {
        r=conn_obj.conn_exec("select location_id from location where location_name='"+jComboBox13.getSelectedItem().toString()+"'");
        r.next();
        String location_id=r.getString("location_id");
        
        for (int i = 0; i < jTable16.getRowCount(); i++) {
            try {
                float quantity = Float.parseFloat(jTable16.getValueAt(i, 2).toString());
                float net_Q=quantity;
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
                        "items.item_relations.item_relation*" +net_Q + " as quantity\n" +
                        "\n" +
                        "from items.main_items,items.item_units,items.item_relations\n" +
                        "\n" +
                        "where \n" +
                        "items.main_items.item_name='" + jTable16.getValueAt(i, 0) + "'  AND\n" +
                        "items.item_units.unit_name='" + jTable16.getValueAt(i, 1) + "'  AND\n" +
                        "items.main_items.item_id=items.item_relations.item_id AND\n" +
                        "items.item_units.unit_id=items.item_relations.item_unit )as anyThing");
                r.next();
                
                double quantity_to_Increse=r.getDouble("quantity");
                int item_id=r.getInt("main_item_id");
                String store_to_update="store_id_"+location_id;//store_id_1
                ven_stm_to_return_quantity_to_store+="update items.inventory set "+store_to_update+" = "+store_to_update+" + "+quantity_to_Increse+" where item_id="+item_id+";";
                
            } catch (SQLException ex) {
                Joptionpane_message(ex.getMessage());
            }    
        }
    } catch (SQLException ex) {
        Joptionpane_message(ex.getMessage());
    }
    
    
}
public void show_all_vendor_movements() {
        if (jPanel_ven_acount_details.isVisible()) {
            try {
                String vendor = jComboBox_ven_name_for_account_details.getSelectedItem().toString();
                r = conn_obj.conn_exec("select  0.0 as المجموع,القيمة,ملاحظة,الحركة ,المكان, التاريخ ,رقم_القيد   ,رقم_الفاتورة  from(\n"
                        + "SELECT bill_value as القيمة,record_time,bill_location_id,location.location_id,bill_note as ملاحظة,'فاتورة' as الحركة,location.location_name as المكان,bill_date as التاريخ,bill_id as رقم_القيد,bill_num as رقم_الفاتورة\n"
                        + "FROM vendor_bills,location\n"
                        + "           where bill_vendor_id=(select vendor_id as cus_name from vendors where vendor_name='" + vendor + "')  and bill_location_id=location_id\n"
                        + "                        UNION\n"
                        + "SELECT return_bill_value as القيمة,record_time,return_bill_location_id,location.location_id,return_bill_note as ملاحظة,'ف-مرتجع' as الحركة,location.location_name as المكان,return_bill_date as التاريخ,return_bill_id as رقم_القيد,return_bill_num as رقم_الفاتورة\n"
                        + "FROM return_vendor_bills,location\n"
                        + "           where return_bill_vendor_id=(select vendor_id as cus_name from vendors where vendor_name='" + vendor + "')  and return_bill_location_id=location_id\n"
                        + "                        UNION\n"
                        + "                      \n"
                        + "SELECT payment_value,record_time,0 ,null,payment_note,'دفعة','---',payment_date,payment_id,'---'\n"
                        + "                        FROM vendor_payments,location \n"
                        + "                        where vendor_id_fk=(select vendor_id as cus_name from vendors where vendor_name='" + vendor + "')  order by التاريخ, record_time\n"
                        + ") as ffff");
                jTable_show_ven_account_details.setModel(DbUtils.resultSetToTableModel(r));
                render_table3();
                if (jTable_show_ven_account_details.getRowCount() != 0) {
                    jTable_show_ven_account_details.setValueAt(0.0, 0, 0);
                }
                for (int i = 0; i < jTable_show_ven_account_details.getRowCount(); i++) {
                    if (i == 0) {
                        if (jTable_show_ven_account_details.getValueAt(i, 3).equals("فاتورة")) {
                            float value = Float.parseFloat(jTable_show_ven_account_details.getModel().getValueAt(i, 1).toString());
                            jTable_show_ven_account_details.setValueAt(value, i, 0);
                        } else if (jTable_show_ven_account_details.getValueAt(i, 3).equals("دفعة")) {
                            float value = -1 * Float.parseFloat(jTable_show_ven_account_details.getModel().getValueAt(i, 1).toString());
                            jTable_show_ven_account_details.setValueAt(value, i, 0);
                        } else if (jTable_show_ven_account_details.getValueAt(i, 3).equals("ف-مرتجع")) {
                            float value = -1 * Float.parseFloat(jTable_show_ven_account_details.getModel().getValueAt(i, 1).toString());
                            jTable_show_ven_account_details.setValueAt(value, i, 0);
                        }
                    } else {
                        if (jTable_show_ven_account_details.getValueAt(i, 3).equals("فاتورة")) {
                            float pre_value = Float.parseFloat(jTable_show_ven_account_details.getModel().getValueAt(i - 1, 0).toString());
                            float value2 = Float.parseFloat(jTable_show_ven_account_details.getModel().getValueAt(i, 1).toString());
                            jTable_show_ven_account_details.setValueAt(pre_value + value2, i, 0);
                        } else if (jTable_show_ven_account_details.getValueAt(i, 3).equals("دفعة")) {
                            float pre_value = Float.parseFloat(jTable_show_ven_account_details.getModel().getValueAt(i - 1, 0).toString());
                            float value2 = Float.parseFloat(jTable_show_ven_account_details.getModel().getValueAt(i, 1).toString());
                            jTable_show_ven_account_details.setValueAt(pre_value - value2, i, 0);
                        } else if (jTable_show_ven_account_details.getValueAt(i, 3).equals("ف-مرتجع")) {
                            float pre_value = Float.parseFloat(jTable_show_ven_account_details.getModel().getValueAt(i - 1, 0).toString());
                            float value2 = Float.parseFloat(jTable_show_ven_account_details.getModel().getValueAt(i, 1).toString());
                            jTable_show_ven_account_details.setValueAt(pre_value - value2, i, 0);
                        }
                    }

                }

            } catch (Exception ex) {
                Joptionpane_message(ex.getMessage());
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
public void vendor_account_zero(String name) throws SQLException, Exception {
        try {
            conn_obj.get_con().setAutoCommit(false);
            r = conn_obj.conn_exec("select vendor_id from vendors where vendor_name='" + name + "'");
            r.next();
            int vendor_id = r.getInt(1);
            r = conn_obj.conn_exec("select location_id from location");//get any location ID to insert in bill to make account zero
            r.next();
            int location_id = r.getInt(1);
            float vendor_account = get_vendor_account_sum(vendor_id);
            if (vendor_account < 0) {
                conn_obj.get_st().execute("insert into vendor_bills(bill_value,bill_note,bill_vendor_id,bill_date,accounted,bill_location_id)"
                        + "values(" + vendor_account * -1 + ",'" + "فاتورة لتصفير الحساب" + "'," + vendor_id + ",'" + get_date() + "',true," + location_id + ")");
            }
            if (vendor_account > 0) {
                conn_obj.get_st().executeUpdate("insert into vendor_payments(payment_value,payment_note,vendor_id_fk,payment_date,accounted)values(" + vendor_account + ",'" + "دفعة لتصفير الحساب" + "'," + vendor_id + ",'" + get_date() + "',true)");
            }
            conn_obj.get_st().executeUpdate("UPDATE vendor_bills SET accounted=true where bill_vendor_id=" + vendor_id + " and accounted=false");
            conn_obj.get_st().executeUpdate("UPDATE vendor_payments SET accounted=true where vendor_id_fk=" + vendor_id + " and accounted=false");
            conn_obj.get_st().executeUpdate("UPDATE return_vendor_bills SET accounted=true where return_bill_vendor_id=" + vendor_id + " and accounted=false");
            conn_obj.get_st().executeUpdate("UPDATE vendor_discount SET accounted=true where vendor_id_fk=" + vendor_id + " and accounted=false");
            Joptionpane_confirm("هل انت متأكد من تصفير الحساب", "لم يتم تصفير الحساب");
            conn_obj.get_con().commit();
            Joptionpane_message("تم تصفير الحساب");

        } catch (Exception e) {
            conn_obj.get_con().rollback();
            Joptionpane_message("لم يتم تصفير الحساب...");
            Joptionpane_message(e.getMessage());
        }
    }
public void print_vendor_jtable(int num_of_move, String status) throws SQLException {
        ///////////////////
        int row = jTable_show_ven_account_details.getRowCount();
        int col = jTable_show_ven_account_details.getColumnCount();
        float account_sum = 0;
        String html = "<body>" + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>"
                //+ "<h2 align=\"center\">محلات الشايب التجارية</h2>\n"
                //+ "<h3 align=\"center\">برقين - 042438973</h3>\n"
                + "<h3 align=\"center\">كشف حساب الذمم</h3>";
        html += "<table align=\"center\" border=\"1\" style=\"width:5px\" cellspacing=\"0\" cellpadding=\"1\" style=\"font-size: 8px\">" + "\n";
        html += "<tr>" + "\n";
        html += "<td >" + "تاريخ الطباعة : " + get_date() + "</td>" + "\n";
        html += "<td >" + "         " + "</td>" + "\n";
        html += "<td align=\"right\">" + "إسم الزبون  : " + jComboBox_ven_name_for_account_details.getSelectedItem().toString() + "</td>" + "\n";
        html += "</table>";
        html += "<table align=\"center\" border=\"1\" style=\"width:5px\" cellspacing=\"0\" cellpadding=\"1\" style=\"font-size: 8px\">" + "\n";
        html += "<tr>" + "\n";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable_show_ven_account_details.getColumnName(0) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable_show_ven_account_details.getColumnName(1) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable_show_ven_account_details.getColumnName(2) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable_show_ven_account_details.getColumnName(3) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable_show_ven_account_details.getColumnName(4) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable_show_ven_account_details.getColumnName(5) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable_show_ven_account_details.getColumnName(7) + "</th>";

        html += "</tr>" + "\n";
        for (int i = row - num_of_move; i < row; i++) {
            html += "<tr>" + "\n";
            for (int x = 0; x < col; x++) {
                if (x != 6) {
                    if (x == 2)//اذا ملاحظة اعمل العرض ثابت
                    {
                        html += "<td width=\"60\" align=\"center\">" + jTable_show_ven_account_details.getValueAt(i, x) + "</td>" + "\n";
                    } else {
                        html += "<td align=\"center\">" + jTable_show_ven_account_details.getValueAt(i, x) + "</td>" + "\n";
                    }
                }
            }
            html += "</tr>" + "\n";
            
            account_sum=Float.parseFloat(jTable_show_ven_account_details.getValueAt(i, 0).toString());
        }
        html += "<table align=\"center\" border=\"1\" style=\"width:5px\" cellspacing=\"0\" cellpadding=\"1\" style=\"font-size: 8px\">" + "\n";

        html += "<tr>" + "\n";
        html += "<td>" + "مجموع الحساب = " + account_sum + "</td>" + "\n";
        html += "</tr>" + "\n";
        html += "<tr><td></td></tr>" + "\n";
        html += "</table>";
        html += "</table></body>" + "\n";

        /////////////////////////////////
        if (status == ("save_to_file")) {
            generateHtmlPage(html);
        } else {
            JPanel panel = new JPanel();
            final JEditorPane editorPane = new JEditorPane();
            editorPane.setEditable(false);
            editorPane.setContentType("text/html");

            editorPane.setText(html);
            panel.add(editorPane);
            JFrame frame2 = new JFrame();
            frame2.setSize(700, 600);
            frame2.setLocationRelativeTo(this);
            JButton but = new JButton();
            but.setText("طباعة");
            but.requestFocus(true);
            panel.add(but);
            but.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        //Execute when button is pressed
                        editorPane.print();
                    } catch (PrinterException ex) {
                        Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            frame2.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            // Change this to switch between examples
            boolean useScrollPane = true;

            if (useScrollPane) {
                JScrollPane scrollPane = new JScrollPane();
                scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setViewportView(panel);
                frame2.add(scrollPane);
            } else {
                frame2.add(panel);
            }
            frame2.setVisible(true);

        }
    }
public void print_vendor_jtable_with_details(int num_of_move, String status) throws SQLException {
        ///////////////////
        int row = jTable_show_ven_account_details.getRowCount();
        int col = jTable_show_ven_account_details.getColumnCount();
        float account_sum = 0;

        String html = "<body>"
                + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>"
                //+ "<h2 align=\"center\">محلات الشايب التجارية</h2>\n"
                //+ "<h3 align=\"center\">برقين - 042438973</h3>\n"
                + "<h3 align=\"center\">كشف حساب الذمم</h3>";
        html += "<table align=\"center\" border=\"1\" style=\"width:5px\" cellspacing=\"0\" cellpadding=\"1\" style=\"font-size: 8px\">" + "\n";
        html += "<tr>" + "\n";
        html += "<td >" + "تاريخ الطباعة : " + get_date() + "</td>" + "\n";
        html += "<td >" + "         " + "</td>" + "\n";
        html += "<td align=\"right\">" + "إسم الزبون  : " + jComboBox_ven_name_for_account_details.getSelectedItem().toString() + "</td>" + "\n";
        html += "</table>";
        html += "<table align=\"center\" border=\"1\" style=\"width:5px\" cellspacing=\"0\" cellpadding=\"1\" style=\"font-size: 8px\">" + "\n";
        html += "<tr>" + "\n";

        html += "<th bgcolor=" + "#00FF00" + ">" + jTable_show_ven_account_details.getColumnName(0) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable_show_ven_account_details.getColumnName(1) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable_show_ven_account_details.getColumnName(2) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable_show_ven_account_details.getColumnName(3) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable_show_ven_account_details.getColumnName(4) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable_show_ven_account_details.getColumnName(5) + "</th>";
        html += "<th bgcolor=" + "#00FF00" + ">" + jTable_show_ven_account_details.getColumnName(7) + "</th>";

        html += "</tr>" + "\n";
        for (int i = row - num_of_move; i < row; i++) {
            html += "<tr>" + "\n";
            for (int x = 0; x < col; x++) {
                if (x != 6) {
                    if (x == 2)//اذا ملاحظة اعمل العرض ثابت
                    {
                        html += "<td width=\"60\" align=\"center\">" + jTable_show_ven_account_details.getValueAt(i, x) + "</td>" + "\n";
                    } else {
                        html += "<td align=\"center\">" + jTable_show_ven_account_details.getValueAt(i, x) + "</td>" + "\n";
                    }
                }
            }
            html += "</tr>" + "\n";
            ////// من هنا اضافة التفاصيل
            String movement_type = (jTable_show_ven_account_details.getValueAt(i, 3)).toString();
            String movement_id = (jTable_show_ven_account_details.getValueAt(i, 6)).toString();

            if (movement_type.equalsIgnoreCase("فاتورة")) {
                r = conn_obj.conn_exec("select item_note as ملاحظة ,(SELECT round(CAST(price*item_quantity as numeric),1) as المبلغ),price as السعر_بعد,discount_ratio as نسبة_الخصم,round(cast(item_price as numeric),2)as السعر,item_bonus as بونص,item_quantity as الكمية,unit_name as الوحدة,item_name as اسم_الصنف from ("
                        + "SELECT \n"
                        + "items.main_items.item_name,\n"
                        + "items.main_items.item_id,\n"
                        + "\n"
                        + "items.item_units.unit_name,\n"
                        + "items.item_units.unit_id,\n"
                        + "\n"
                        + "vendor_bills_items.id,vendor_bills_items.item_id,vendor_bills_items.item_bonus,\n"
                        + "vendor_bills_items.item_note,vendor_bills_items.item_unit,\n"
                        + "vendor_bills_items.item_price,vendor_bills_items.item_quantity,\n"
                        + "vendor_bills_items.bill_id,vendor_bills_items.discount_ratio,\n"
                        + "(1-vendor_bills_items.discount_ratio/100)*vendor_bills_items.item_price as price\n"
                        + "FROM items.main_items,items.item_units,vendor_bills_items\n"
                        + "WHERE\n"
                        + "items.main_items.item_id = vendor_bills_items.item_id\n"
                        + "AND\n"
                        + "items.item_units.unit_id = vendor_bills_items.item_unit\n"
                        + "AND\n"
                        + "vendor_bills_items.bill_id=" + movement_id + " order by vendor_bills_items.id)as dvfc");

                while (r.next()) {
                    html += "<tr>" + "\n";

                    html += "<td align=\"center\">" + r.getString(1) + "</td>" + "\n";
                    html += "<td align=\"center\">" + r.getString(2) + "</td>" + "\n";
                    html += "<td align=\"center\">" + r.getString(4) + "</td>" + "\n";
                    html += "<td align=\"center\">" + r.getString(5) + "</td>" + "\n";
                    html += "<td align=\"center\">" + r.getString(6) + "</td>" + "\n";
                    html += "<td align=\"center\">" + r.getString(7) + "</td>" + "\n";
                    html += "<td align=\"center\">" + r.getString(8) + "</td>" + "\n";
                    html += "<td align=\"center\">" + r.getString(9) + "</td>" + "\n";

                    html += "</tr>" + "\n";
                }
            }

            ///////  
            account_sum=Float.parseFloat(jTable_show_ven_account_details.getValueAt(i, 0).toString());
        }
        html += "<table align=\"center\" border=\"1\" style=\"width:5px\" cellspacing=\"0\" cellpadding=\"1\" style=\"font-size: 8px\">" + "\n";

        html += "<tr>" + "\n";
        html += "<td>" + "مجموع الحساب = " + account_sum + "</td>" + "\n";
        html += "</tr>" + "\n";
        html += "<tr><td></td></tr>" + "\n";
        html += "</table>";
        html += "</table></body>" + "\n";
        /////////////////////////////////
        if (status == ("save_to_file")) {
            generateHtmlPage(html);
        } else {
            JPanel panel = new JPanel();
            final JEditorPane editorPane = new JEditorPane();
            editorPane.setEditable(false);
            editorPane.setContentType("text/html");

            editorPane.setText(html);
            panel.add(editorPane);
            JFrame frame2 = new JFrame();
            frame2.setSize(700, 600);
            frame2.setLocationRelativeTo(this);
            JButton but = new JButton();
            but.setText("طباعة");
            but.requestFocus(true);
            panel.add(but);
            but.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        //Execute when button is pressed
                        editorPane.print();
                    } catch (PrinterException ex) {
                        Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            frame2.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            // Change this to switch between examples
            boolean useScrollPane = true;

            if (useScrollPane) {
                JScrollPane scrollPane = new JScrollPane();
                scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setViewportView(panel);
                frame2.add(scrollPane);
            } else {
                frame2.add(panel);
            }
            frame2.setVisible(true);
        }

    }


 

public void check_to_add_phone_number(String phone,String name)
{
    int response = JOptionPane.showConfirmDialog(null, "هل تريد ادخال الاسم ورقم الهاتف الى دليل الهاتف؟" +"\nللتأكيد اضغط موافق                         ",
                    "", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (response == JOptionPane.CLOSED_OPTION || response == JOptionPane.NO_OPTION) {
                
            }
            else{
                
                conn_obj.exec("insert into telephone(tel_name,tel_number)values('"+name+"','"+phone+"')");
            }
}
public boolean check_if_there_is_a_bouns_in_the_bill()
{
    jTable4.editingCanceled(null);
        int row = jTable6.getRowCount();
for (int i = 0; i < row; i++) {
            
                if (Float.parseFloat(jTable6.getValueAt(i, 5).toString())!=0)
                {
                    System.out.println("its true");
                   return true;
                }
                if (Float.parseFloat(jTable6.getValueAt(i, 3).toString())!=0)
                {
                    System.out.println("its true");
                   return true;
                }
            
        }
return false;
}
public String build_html_for_return_bill(int row,int col)
{
    String html="";
    
    html += "<tr>" + "\n";

        
        html += "<th>" + jTable6.getColumnName(0) + "</th>";
        html += "<th>" + jTable6.getColumnName(1) + "</th>";
        html += "<th>" + jTable6.getColumnName(2) + "</th>";
        html += "<th>" + jTable6.getColumnName(3) + "</th>";
        html += "<th>" + jTable6.getColumnName(4) + "</th>";
        html += "<th>" + jTable6.getColumnName(5) + "</th>";

    html += "</tr>" + "\n";
        
            for (int i = 0; i < row; i++) {
            html += "<tr>" + "\n";
            for (int x = 0; x < col ; x++) {
                
                    html += "<td align=\"center\">" + jTable6.getValueAt(i, x) + "</td>" + "\n";
                
            }
            html += "</tr>" + "\n";
        }
 
    
    return html;
}

    Thread backup_and_restore = new Thread() {
        public void run() {
            /*
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    
                    //here the code 
                }
            }, 0, 60 * (1000 * 1));
          */
          x=new progressbar_jframe();
          x.setLocationRelativeTo(customers.this);
          x.setVisible(true);
          
            backup_database();
            x.dispose();
        }
    };
 
    Thread currency = new Thread() {
        public void run() {
            currency_obj=new currency();
            currency_obj.setLocationRelativeTo(customers.this);
            currency_obj.setVisible(true);
        }
        
    };    
    public class Currency_thread implements Runnable {

        @Override
        public void run() {
            System.out.println("in run() method, method completed.");
            currency_obj = new currency();
            currency_obj.setLocationRelativeTo(customers.this);
            currency_obj.setVisible(true);
        }
    }
     
    Thread ranking = new Thread() {
        public void run() {

            try {
                String start_date="";
            jDateChooser4.setDateFormatString("yyyy-MM-dd");
            int response = JOptionPane.showConfirmDialog(null, jPanel22,
                "اختر تاريخ بداية تصنيف اصناف الفواتير", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (response == JOptionPane.OK_OPTION) {
                start_date=jDateChooser4.getDate().toString();
            }
        
                conn_obj.exec("UPDATE items.items_ranking SET rank=0;");
                r2 = conn_obj2.conn_exec("select customer_bills.bill_id,customer_bills.bill_date,customer_bills_items.*"
                                        +" from customer_bills,customer_bills_items where customer_bills.bill_id =customer_bills_items.bill_id "
                                        +"and bill_date>='"+start_date+"'");
                while (r2.next()) {
                    conn_obj.exec("UPDATE items.items_ranking SET rank=rank+1 where rank_item_id= " + r2.getInt("item_id") + ";");
                }
                Joptionpane_message("تم تصنيف الاصناف من جديد لتسهيل وترتيب البحث عنها");
            } catch (SQLException ex) {
                Joptionpane_message(ex.getMessage());
            }

        }

    };
 public void print_customer_bill(String bill_id)
 {
     try{
         r = conn_obj.conn_exec("select discount_ratio,discount_amount,round(CAST(bill_value as numeric),1)as bill,bill_note,bill_date,bill_customer_id,customers.customer_id,customers.customer_name  from  customer_bills,customers where bill_id=" + bill_id + " and customers.customer_id=bill_customer_id");
                r.next();
                String bill_date = "تاريخ الفاتورة :  " + r.getString("bill_date");
                String dis_ratio = "نسبة خصم الفاتورة بالمئة = " + r.getString("discount_ratio") + " %";
                String dis_value = "قيمة الخصم المباشر على الفاتورة = " + r.getString("discount_amount");
                String bill_value="قيمة الفاتورة النهائي يساوي =  "+ r.getString("bill");
                String customer_name=r.getString("customer_name");
                System.out.println(customer_name);
                String html = "<body> ";
        html += "<table align=\"center\" border=\"1\" style=\"width:5px\" cellspacing=\"0\" cellpadding=\"1\" style=\"font-size: 8px\">" + "\n";
        html += "<tr>" + "\n";
        html += "<td >" + bill_date + "</td>" + "\n";
        html += "<td >" + "         " + "</td>" + "\n";
        html += "<td align=\"right\">" + "إسم الزبون  : " + customer_name+ "</td>" + "\n";
        html += "</table>";

        html += "<table align=\"center\" border=\"1\" style=\"width:5px\" cellspacing=\"0\" cellpadding=\"1\" style=\"font-size: 8px\">"  + "\n";
 
        //
        String stm="select item_bonus,discount_ratio from customer_bills_items where bill_id=" + bill_id + " and (item_bonus>0 or discount_ratio>0)";
         System.out.println(stm);
        r = conn_obj.conn_exec(stm);
        boolean if_bouns_or_dis=false;
         if (r.next()) {
             if_bouns_or_dis = true;//معناتو يوجد صنف بونص او عليه خصم
         }
         System.out.println(if_bouns_or_dis);
       html += "<tr>" + "\n";

        html += "<th>" + "ملاحظات" + "</th>";
        html += "<th>" + "المبلغ" + "</th>";
        if(if_bouns_or_dis==true)
        html += "<th>" + "نسبة الخصم" + "</th>";
        html += "<th>" + "السعر"+ "</th>";
        if(if_bouns_or_dis==true)
        html += "<th>" + "بونص" + "</th>";
        html += "<th>" + "الكمية" + "</th>";
        html += "<th>" + "الوحدة" + "</th>";
        html += "<th>" + "اسم الصنف" + "</th>";
        html += "</tr>" + "\n";
        
        
                    r = conn_obj.conn_exec("select "
                            + "customer_bills_items.id,customer_bills_items.item_quantity,customer_bills_items.item_unit,customer_bills_items.item_price,customer_bills_items.item_id,customer_bills_items.item_note,customer_bills_items.item_bonus,customer_bills_items.discount_ratio,"
                            + "items.item_units.unit_id,items.item_units.unit_name,"
                            + "main_items.item_id,main_items.item_name , "
                            + "(1-customer_bills_items.discount_ratio/100)*customer_bills_items.item_price*customer_bills_items.item_quantity as tatal "
                            + "from customer_bills_items,items.item_units,items.main_items "
                            + "where bill_id=" + bill_id + " and "
                            + "customer_bills_items.item_unit=items.item_units.unit_id and "
                            + "customer_bills_items.item_id=main_items.item_id order by customer_bills_items.id ");

     while (r.next()) {
            html += "<tr>" + "\n";
           
                    html += "<td align=\"center\">" + r.getString("item_note") + "</td>" + "\n";
                    html += "<td align=\"center\">" + r.getFloat("tatal") + "</td>" + "\n";
                    if(if_bouns_or_dis==true)
                    html += "<td align=\"center\">" + r.getFloat("discount_ratio") + "</td>" + "\n";
                    html += "<td align=\"center\">" + r.getFloat("item_price") + "</td>" + "\n";
                    if(if_bouns_or_dis==true)
                    html += "<td align=\"center\">" + r.getFloat("item_bonus") + "</td>" + "\n";
                    html += "<td align=\"center\">" + r.getFloat("item_quantity") + "</td>" + "\n";
                    html += "<td align=\"center\">" + r.getString("unit_name") + "</td>" + "\n";
                    html += "<td align=\"center\">" + r.getString("item_name") + "</td>" + "\n";
             
            html += "</tr>" + "\n";
        }
        html += "<tr>" + "\n";
        html += "<td>" + dis_ratio + "</td>" + "\n";
        html += "</tr>" + "\n";
        html += "<tr>" + "\n";
        html += "<td>" + dis_value+ "</td>" + "\n";
        html += "</tr>" + "\n";
        html += "<tr>" + "\n";
        html += "<td>" + bill_value + "</td>" + "\n";
        html += "</tr>" + "\n";
        html += "<tr>" + "\n";
        html += "<td>" + " رصيدكم لدينا  =  " + get_customer_account_sum(customer_name) + "</td>" + "\n";
        html += "</tr>" + "\n";
        
        html += "</table></body>\n" +
"</html>";
  final JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");

        editorPane.setMargin(new Insets(0,0,0,0));
        editorPane.setText(html);
        editorPane.print(null, null, false, PrintServiceLookup.lookupDefaultPrintService(), null, false);//لمنع ظهور شاشة الطباعة
         System.out.println(html);
     }catch(Exception e)
     {
         Joptionpane_message(e.getMessage());
     }
 }
 public void insert_bill_to_customer_jtable4(boolean with_print_or_not)
 {
             try {
            conn_obj.get_con().isClosed();
        } catch (SQLException ex) {
            Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            conn_obj.get_con().setAutoCommit(false);
            jTable4.editingCanceled(null);//هذا اذا نسي المدخل خانات مفتوحة اي مفتوحة للتعديل لا يتعامل معها على انها فارغة فيخرج منها قبل ادخال البيانات
            String customer = (String) jComboBox1.getSelectedItem();//اخذ اسم المورد من القائمة
            if (customer.equals("------")) {
                throw new Exception("لم يتم اختيار الاسم الصحيح");
            }
            float value = float_parsing(jTextField1);
            if (Float.valueOf(jTextField1.getText().toString().trim()) <= 0 || jTextField1.getText().matches("")) {
                throw new Exception("لا يمكن ان تساوي الفاتورة القيمة صفر او فارغ");
            }
            float bill_value_discount = float_parsing(jTextField16);
            float bill_ratio_discount = float_parsing(jTextField3);
            if (bill_ratio_discount > 100 || bill_value_discount > value) {
                throw new Exception("لا يمكن ان يكون الخصم اكبر من قيمة الفاتورة ");
            }
            if (value != prepare_bill()) {
                if (check_yes_or_no_question("محموع الفاتورة الفعلي لا يساوي المجموع الموجود؟") == false) {
                    throw new Exception("تم الالغاء!!");
                }
            }

            String location = (String) jComboBox2.getSelectedItem();
            String note = jTextArea1.getText().trim();
            //////////////////////////////////////
            java.util.Date d;
            d = jDateChooser1.getDate();
            java.sql.Date sqlDate = new java.sql.Date(d.getTime());
            ///////////////////////////
            String bill_num = jTextField15.getText().trim();

            ///
            r = conn_obj.conn_exec("select customer_id from customers where customer_name='" + customer + "'");
            r.next();
            int customer_id = Integer.parseInt(r.getString(1));
            if(jCheckBox3.isSelected()&&check_lose==true)
            check_there_is_no_lose();
            String insert_table_content = "";
            insert_table_content = "INSERT INTO customer_bills (bill_value,bill_customer_id,bill_location_id,bill_note,bill_date,discount_amount,discount_ratio,bill_num) VALUES"
                    + "(" + value + "," + customer_id + ",(select location_id from location where location_name='" + location + "'),'" + note + "','" + sqlDate + "'," + bill_value_discount + "," + bill_ratio_discount + ",'" + bill_num + "');";
            /////////////////إدخال قيم الجدول ////////////////
            for (int i = 0; i < jTable4.getRowCount(); i++) {
                if (jTable4.getValueAt(i, 0) != null) {
                    insert_table_content += "insert into customer_bills_items(bill_id,item_id,item_quantity,item_unit,item_price,item_bonus,item_note,discount_ratio)values"
                            + "((select last_value from customer_bills_counter),(select item_id from items.main_items where item_name = '" + jTable4.getValueAt(i, 0) + "' ),"
                            + jTable4.getValueAt(i, 2) + ",(select unit_id from items.item_units where unit_name = '" + jTable4.getValueAt(i, 1) + "' )," + jTable4.getValueAt(i, 4) + "," + jTable4.getValueAt(i, 3) + ",'" + jTable4.getValueAt(i, 7) + "','" + jTable4.getValueAt(i, 5) + "');";
                }
            }
            
            ////
            System.out.println(insert_table_content+"\n");
            conn_obj.get_st().execute(insert_table_content);
            
            //لمعرفة رقم اخر فاتورة داخلة ومن ثم طباعتها او لا
            String stm_exec = "select last_value from customer_bills_counter";
            r = conn_obj.conn_exec(stm_exec);
            r.next();
            String last_bill_id = r.getString("last_value");
            //انتهي هنا
            
            ////إدخال الكميات وانقاصها من المخزون للمخزن المحدد
             stm_exec = "";
            stm_exec = "select location_id from location where location_name='" + jComboBox2.getSelectedItem().toString() + "'";

            r = conn_obj.conn_exec(stm_exec);
            r.next();
            String location_id = r.getString("location_id");
            String update_stm_inventory="";
            float quantity=0;
            float Bounus=0;
            for (int i = 0; i < jTable4.getRowCount(); i++) {
                quantity=Float.parseFloat(jTable4.getValueAt(i, 2).toString());
                Bounus=Float.parseFloat(jTable4.getValueAt(i, 3).toString());
                float net_Q=quantity+Bounus;
                stm_exec = ""
                        + "select quantity,main_item_id from  (\n"
                        + "select \n"
                        + "\n"
                        + "items.main_items.item_name,\n"
                        + "items.main_items.item_id as main_item_id,\n"
                        + "\n"
                        + "items.item_units.unit_id,\n"
                        + "items.item_units.unit_name,\n"
                        + "\n"
                        + "items.item_relations.item_id,\n"
                        + "items.item_relations.item_unit,\n"
                        + "items.item_relations.item_relation*" + net_Q + " as quantity\n"
                        + "\n"
                        + "from items.main_items,items.item_units,items.item_relations\n"
                        + "\n"
                        + "where \n"
                        + "items.main_items.item_name='" + jTable4.getValueAt(i, 0) + "'  AND\n"
                        + "items.item_units.unit_name='" + jTable4.getValueAt(i, 1) + "'  AND\n"
                        + "items.main_items.item_id=items.item_relations.it"
                        + "em_id AND\n"
                        + "items.item_units.unit_id=items.item_relations.item_unit )as anyThing";
                r = conn_obj.conn_exec(stm_exec);
                r.next();
                double quantity_to_Decrease = r.getDouble("quantity") * -1;
                int item_id = r.getInt("main_item_id");
                String store_to_update = "store_id_" + location_id;//store_id_1
                update_stm_inventory+= "update items.inventory set " + store_to_update + " = " + store_to_update + " + " + quantity_to_Decrease + " where item_id=" + item_id + ";";
                
            }
            conn_obj.exec(update_stm_inventory);
            System.out.println(update_stm_inventory);
            //////////////////////////////////////////////////
            check_two_input_customer(customer_id, sqlDate);

            conn_obj.get_con().commit();
            Joptionpane_message("لقد تم إدخال البيانات");
            float account = get_customer_account_sum(customer_id);
            Joptionpane_message("حساب  " + customer + "  يساوي\n" + account + "");
            move_to_customer_vendor_account();
            ///////////////////////////////////////////////////to write on file //////////////////////
            write_to_file(get_date() + "     " + sqlDate.toString() + " " + "  إدخال فاتورة لحساب الزبون  " + customer + " " + "بقيمة " + " "
                    + Float.toString(value) + " " + "الرصيد = " + " " + Float.toString(account));

            reset_jpanel_1();
            if(with_print_or_not==true)
            {
                print_customer_bill(last_bill_id);
            }
        } catch (Exception ex) {
            try {
                conn_obj.get_con().rollback();
                Joptionpane_message(ex.getMessage());
                Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception e) {
                Joptionpane_message(e.getMessage());
            }
            //Logger.getLogger(vendors.class.getName()).log(Level.SEVERE, null, ex);
        }

 }
 
 public void privileg_panels(String privileg_text)
 {
     
            if(privileg_text.contains("insert_customer_bill_panel"))//add or remove panel
            {
                jTabbedPane1.remove(jPanel3);
            }
            if(privileg_text.contains("add_payment_panel"))//add or remove panel
            {
                jTabbedPane1.remove(jPanel5);
            }
            if(privileg_text.contains("customer_names_panel"))//add or remove panel
            {
                jTabbedPane1.remove(jPanel2);
            }
            if(privileg_text.contains("varieties_shortage_panel"))//add or remove panel
            {
                jTabbedPane1.remove(jPanel16);
            }
            if(privileg_text.contains("statistics_panel"))//add or remove panel
            {
                jTabbedPane1.remove(jPanel17);
            }
            if(privileg_text.contains("return_bill_customer_panel"))//add or remove panel
            {
                jTabbedPane1.remove(jPanel31);
            }
            if(privileg_text.contains("vendors_panel"))//add or remove panel
            {
                jTabbedPane3.remove(jTabbedPane_vendor_action);
            }
            if(privileg_text.contains("items_panel"))//add or remove panel
            {
                jTabbedPane3.remove(items_obj.jTabbedPane3_Items);
            }
            if(privileg_text.contains("checks_panel"))//add or remove panel
            {
                jTabbedPane3.remove(checks_obj.jTabbedPane_checks);
            }
 }
 
  public void privileg_actions(String privileg_text)
 {
     //see_profit,see_cost,delete_entry,modify_entry,check_lose,search_in_buy_bills,discount_cus,discount_ven,
     //       modify_check_status,return_check_to_customer,ranking_items,sale_point,account_zero=false
            if(privileg_text.contains("see_profit"))//allows or prevents actions
                see_profit=true;
            if(privileg_text.contains("see_cost_price"))//allows or prevents actions
                see_cost=true;
            if(privileg_text.contains("delete_entry"))//allows or prevents actions
                delete_entry=true;
            if(privileg_text.contains("modify_entry"))//allows or prevents actions
                modify_entry=true;
            if(privileg_text.contains("check_lose_in_item"))//allows or prevents actions
             check_lose=true; 
            if(privileg_text.contains("search_in_buy_bills"))//allows or prevents actions
                search_in_buy_bills=true;
            if(privileg_text.contains("discount_customer"))//allows or prevents actions
                discount_cus=true;
            if(privileg_text.contains("discount_vendor"))//allows or prevents actions
                discount_ven=true;
            if(privileg_text.contains("modify_checks_status"))//allows or prevents actions
                modify_check_status=true;
            if(privileg_text.contains("return_check_to_customer"))//allows or prevents actions
                return_check_to_customer=true;
            if(privileg_text.contains("ranking_items"))//allows or prevents actions
                ranking_items=true;
            if(privileg_text.contains("sale_point"))//allows or prevents actions
                sale_point=true;
            if(privileg_text.contains("account_zero"))//allows or prevents actions
                account_zero=true;
            
 }
    public void do_you_want_to_make_backup() {
        int safe = JOptionPane.showConfirmDialog(null, "هل تريد عمل نسخة احتياطية قبل الاغلاق؟", "Exit Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);

        if (safe == JOptionPane.YES_OPTION) {
            Thread t = new Thread(backup_and_restore);
            t.start();
        }
    }
    
    public int getRowCount(ResultSet resultSet) {
    if (resultSet == null) {
        return 0;
    }
    try {
        resultSet.last();
        return resultSet.getRow();
    } catch (SQLException exp) {
        exp.printStackTrace();
    } finally {
        try {
            resultSet.beforeFirst();
        } catch (SQLException exp) {
            exp.printStackTrace();
        }
    }
    return 0;
}
    public void add_item_from_SearchItemName_to_customer_bill()
    {
        if (jCheckBox2.isSelected()) {//الدخول للتسعيرة الاخيرة للزبون ماشرة
                    try {
                        String item_name, new_item_unit, customer = "";
                        item_name = jTable7.getValueAt(jTable7.getSelectedRow(), 0).toString();
                        new_item_unit = jTable7.getValueAt(jTable7.getSelectedRow(), 1).toString();
                        customer = jComboBox1.getSelectedItem().toString();
                        r = conn_obj.conn_exec("select \n" +
"                               unit_name,unit_id,\n" +
"                               items.main_items.item_name,items.main_items.item_id,\n" +
"                               customer_name,customer_id,\n" +
"                                bill_customer_id,public.customer_bills.bill_id,public.customer_bills.bill_date,\n" +
"                                public.customer_bills_items.bill_id,public.customer_bills_items.item_id,public.customer_bills_items.item_unit,public.customer_bills_items.item_price\n" +
"                                \n" +
"                                from\n" +
"                                items.item_units,\n" +
"                                items.main_items,\n" +
"                                public.customers,\n" +
"                                public.customer_bills,\n" +
"                                public.customer_bills_items\n" +
"                                \n" +
"                                where \n" +
"                                items.main_items.item_name= '"+item_name+"' and\n" +
"                                public.customers.customer_name= '"+customer+"' and\n" +
"                                public.customer_bills.bill_customer_id=public.customers.customer_id and\n" +
"                                public.customer_bills.bill_id=public.customer_bills_items.bill_id and\n" +
"                                public.customer_bills_items.item_id=items.main_items.item_id and\n" +
"                                public.customer_bills_items.item_unit=items.item_units.unit_id \n" +
"                                order by bill_date;");
                        
                        // نريد ان نفحص اذا الكمية تساوي فارغ فهي واحد  
                        String quantity="1";
                        if (!jTable7.getValueAt(jTable7.getSelectedRow(), 3).toString().trim().equals(""))
                        {
                            quantity=jTable7.getValueAt(jTable7.getSelectedRow(), 3).toString();   
                        }
                        //انتهي الفحص
                        if (!r.next()) {
                          
                            add_row_jtable4(jTable4,
                                    jTable7.getValueAt(jTable7.getSelectedRow(), 0), jTable7.getValueAt(jTable7.getSelectedRow(), 1),quantity , 0, jTable7.getValueAt(jTable7.getSelectedRow(), 2), 0, null, jTable7.getValueAt(jTable7.getSelectedRow(), 4));
                        } else {
                            r.last();

                            int old_unit_id = r.getInt("unit_id");
                            
                            int item_id = r.getInt("item_id");
                            
                            float last_price = r.getFloat("item_price");
                            
                            r2 = conn_obj2.conn_exec("select unit_id from items.item_units where unit_name ='" + new_item_unit + "';");
                            r2.next();
                            int new_unit_id = r2.getInt("unit_id");
                            
                            r2 = conn_obj2.conn_exec("select item_relation from items.item_relations where item_id =" + item_id + "  and item_unit =" + old_unit_id + "  ;");
                            r2.next();
                            float old_unit_relation = r2.getFloat("item_relation");
                            
                            r2 = conn_obj2.conn_exec("select item_relation from items.item_relations where item_id =" + item_id + "  and item_unit =" + new_unit_id + "  ;");
                            r2.next();
                            float new_unit_relation = r2.getFloat("item_relation");
                            
              float new_price=last_price/(old_unit_relation/new_unit_relation);
              
                            add_row_jtable4(jTable4,
                                    jTable7.getValueAt(jTable7.getSelectedRow(), 0), jTable7.getValueAt(jTable7.getSelectedRow(), 1),quantity, 0,new_price , 0, null, jTable7.getValueAt(jTable7.getSelectedRow(), 4));
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(customers.class.getName()).log(Level.SEVERE, null, ex);
                    }//الانتهاء من تنفيذ التسعيرة الاخيرة
                } else {//هنا اذا مش مختار التسعيرة الاخيرة للزبون يسعر على حساب الجدول الموجود
                    add_row_jtable4(jTable4,
                            jTable7.getValueAt(jTable7.getSelectedRow(), 0), jTable7.getValueAt(jTable7.getSelectedRow(), 1), jTable7.getValueAt(jTable7.getSelectedRow(), 3), 0, jTable7.getValueAt(jTable7.getSelectedRow(), 2), 0, null, jTable7.getValueAt(jTable7.getSelectedRow(), 4));
                }
                show_last_row_scroll_jtable(jTable4);//هنا الذهاب لاخر صف مكتوب بجدول الفاتورة لامكانية رؤيته مباشرة
            
    }
}
