/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Fayez
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class TableIcon extends JFrame
{
    public TableIcon()
    {
        ImageIcon aboutIcon1 = new ImageIcon("C:\\Users\\Fayez\\Documents\\NetBeansProjects\\8-10-2016\\src\\111.jpg");
        ImageIcon aboutIcon2 = new ImageIcon("C:\\Users\\Fayez\\Documents\\NetBeansProjects\\8-10-2016\\src\\222.png");
        ImageIcon aboutIcon3 = new ImageIcon("C:\\Users\\Fayez\\Documents\\NetBeansProjects\\8-10-2016\\src\\333.png");
        ImageIcon aboutIcon4 = new ImageIcon("C:\\Users\\Fayez\\Documents\\NetBeansProjects\\8-10-2016\\src\\444.png");
        String[] columnNames = {"Picture", "Description"};
        Object[][] data =
        {
            {aboutIcon1, "About1"},
            {aboutIcon2, "About2"},
            {aboutIcon3, "About3"},
            {aboutIcon4, "About4"},
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable( model )
        {
            //  Returning the Class of each column will allow different
            //  renderers to be used based on Class
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }
        };
        table.setPreferredScrollableViewportSize(table.getPreferredSize());

        JScrollPane scrollPane = new JScrollPane( table );
        getContentPane().add( scrollPane );
    }

    public static void main(String[] args)
    {
        TableIcon frame = new TableIcon();
        frame.setDefaultCloseOperation( EXIT_ON_CLOSE );
        frame.pack();
        frame.setVisible(true);
    }

}
