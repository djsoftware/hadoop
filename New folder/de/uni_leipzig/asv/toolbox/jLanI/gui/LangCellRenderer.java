package de.uni_leipzig.asv.toolbox.jLanI.gui;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
/**
 * @author Alexandr Uciteli
 * @version 1.0
 */
class LangCellRenderer extends JCheckBox implements TableCellRenderer
{
  public Component getTableCellRendererComponent( JTable table,
    Object value, boolean isSelected, boolean hasFocus,
    int row, int column )
  {
		if(value instanceof String) {
			setSelected((new Boolean((String)value)).booleanValue());
		} else {
			setSelected(((Boolean)value).booleanValue());
		}
		
		return this;
  }
}
