package UI;

import UI.DemoScrollBarUI;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ParsingErrorPanel extends JPanel {
    public ParsingErrorPanel() {
        setBackground(new Color(60, 63, 65));
        initComponents();
        initLayout();
    }
    void initComponents() {
        parsingErrorDTM = new DefaultTableModel(null, new String[]{"行号", "错误信息"}) {
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        parsingErrorTable = new JTable(parsingErrorDTM);
        //parsingErrorTable.setEnabled(false);
        parsingErrorScrollPane = new JScrollPane(parsingErrorTable);
        parsingErrorDTCR = new DefaultTableCellRenderer();
        parsingErrorDTCR.setHorizontalAlignment(JLabel.CENTER);
        parsingErrorTable.setDefaultRenderer(Object.class, parsingErrorDTCR);
        parsingErrorTable.getTableHeader().setResizingAllowed(false);
        parsingErrorTable.getColumnModel().getColumn(0).setMaxWidth(50);
        parsingErrorTable.getColumnModel().getColumn(1).setMaxWidth(444);
        parsingErrorTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 1) {
                    int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint()); //获得行位置
                    int col = ((JTable) e.getSource()).columnAtPoint(e.getPoint()); //获得列位置
                    String cellVal = (String)(parsingErrorDTM.getValueAt(row,col)); //获得点击单元格数据
                    UIManager UI = new UIManager();
                    UI.put("OptionPane.background", new Color(60, 63, 65));
                    UI.put("Panel.background", new Color(60, 63, 65));
                    UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("微软雅黑", Font.PLAIN, 15)));
                    UIManager.put("OptionPane.messageForeground", Color.WHITE);
                    JOptionPane.showMessageDialog(parsingErrorScrollPane, cellVal, "表格信息", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    void initLayout() {
        setLayout(null);
        add(parsingErrorScrollPane);
        parsingErrorScrollPane.setBounds(0, 0, 497, 235);
        parsingErrorScrollPane.getVerticalScrollBar().setUI(new DemoScrollBarUI());

        parsingErrorTable.setRowHeight(25);
        parsingErrorTable.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        parsingErrorTable.getTableHeader().setBackground(new Color(60, 63, 65));
        parsingErrorTable.getTableHeader().setForeground(Color.WHITE);
        parsingErrorTable.getTableHeader().setPreferredSize(new Dimension(0, 35));
        parsingErrorTable.getTableHeader().setFont(new Font("微软雅黑", Font.PLAIN, 15));
        parsingErrorTable.getTableHeader().setReorderingAllowed(false);
        parsingErrorScrollPane.getViewport().setBackground(new Color(43, 43, 43));
        parsingErrorDTCR.setBackground(new Color(43, 43, 43));
        parsingErrorDTCR.setForeground(new Color(222, 106, 92));
        parsingErrorScrollPane.setBackground(new Color(60, 63, 65));
    }

    void clear() {
        //将表格内容清空，注意要先取出行数，否则无法删除干净
        int errorRows = parsingErrorDTM.getRowCount();
        for(int i = 0; i < errorRows; i++) {
            parsingErrorDTM.removeRow(0);
            parsingErrorTable.updateUI();
        }
        parsingErrorDTCR.setBackground(new Color(43, 43, 43));
        parsingErrorDTCR.setForeground(new Color(222, 106, 92));
    }

    private JTable parsingErrorTable;
    public DefaultTableModel parsingErrorDTM;
    private DefaultTableCellRenderer parsingErrorDTCR;
    private JScrollPane parsingErrorScrollPane;
}
