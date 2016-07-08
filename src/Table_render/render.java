/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Table_render;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Fayez_shayeb
 */
public class render extends DefaultTableCellRenderer{
    
  @Override
public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
        String actualValue = (String) value;
        // Set the colors as per the value in the cell...
        if(actualValue.equals("دفعة") ){
            setBackground(Color.YELLOW);
        }
        return this;
    }
  
  }

