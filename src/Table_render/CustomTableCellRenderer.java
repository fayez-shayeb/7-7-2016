package Table_render;


import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Fayez
 */
public class CustomTableCellRenderer extends DefaultTableCellRenderer{
    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
  @Override
  public Component getTableCellRendererComponent (JTable table, 
Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
  Component cell = super.getTableCellRendererComponent(
   table, obj, isSelected, hasFocus, row, column);
  if (isSelected) {
  cell.setBackground(Color.green);
  } 
  else {
  if (row % 2 == 0) {
  cell.setBackground(Color.cyan);
  }
  else {
  cell.setBackground(Color.lightGray);
  }
  if(column==0)
      cell.setBackground(Color.PINK);
  }
 
  return cell;
  }
  
  private void alignRight(JTable table, int column) {
    
    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
    table.getColumnModel().getColumn(column).setCellRenderer(rightRenderer);
}
  }
