package Table_render;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.awt.ComponentOrientation;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Fayez
 */
public class Jtable_render {

    DefaultTableCellRenderer renderer;

    public void Renderer(JTable table) {
        renderer = (DefaultTableCellRenderer)
            table.getTableHeader().getDefaultRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        /////////////////////////
        table.getTableHeader().setFont(new Font("Arial",Font.BOLD,16));
        table.setRowHeight(30);
        table.setFont(new java.awt.Font("Arial", 0, 18));
        ////////////////////////
        table.setGridColor(new java.awt.Color(255, 0, 0));
        table.setShowGrid(true);
        //////////////////////////
        table.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        /////////////////////////
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table.getColumnModel().getColumnCount();
        for(int x=0;x<table.getColumnModel().getColumnCount();x++)
        {
            table.getColumnModel().getColumn(x).setCellRenderer( centerRenderer );
        }
       
        
    }

}