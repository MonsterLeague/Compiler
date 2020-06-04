package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LexingErrorPanel extends JPanel {
    public LexingErrorPanel() {
        setBackground(new Color(60, 63, 65));
        initComponents();
        initLayout();
    }

    void initComponents() {
        lexingErrorDTM = new DefaultTableModel(null, new String[]{"行号", "错误单词", "错误类型"}) {
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        lexingErrorTable = new JTable(lexingErrorDTM);
        //lexingErrorTable.setEnabled(false);
        lexingErrorScrollPane = new JScrollPane(lexingErrorTable);
        lexingErrorDTCR = new DefaultTableCellRenderer();
        lexingErrorDTCR.setHorizontalAlignment(JLabel.CENTER);
        lexingErrorTable.setDefaultRenderer(Object.class, lexingErrorDTCR);
        lexingErrorTable.getTableHeader().setResizingAllowed(false);
        lexingErrorTable.getColumnModel().getColumn(0).setMaxWidth(50);
        lexingErrorTable.getColumnModel().getColumn(1).setMaxWidth(100);

    }

    void initLayout() {
        setLayout(null);
        add(lexingErrorScrollPane);
        lexingErrorScrollPane.setBounds(0, 0, 497, 235);
        lexingErrorScrollPane.getVerticalScrollBar().setUI(new DemoScrollBarUI());

        lexingErrorTable.setRowHeight(25);
        lexingErrorTable.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        lexingErrorTable.getTableHeader().setBackground(new Color(60, 63, 65));
        lexingErrorTable.getTableHeader().setForeground(Color.WHITE);
        lexingErrorTable.getTableHeader().setPreferredSize(new Dimension(0, 35));
        lexingErrorTable.getTableHeader().setFont(new Font("微软雅黑", Font.PLAIN, 15));
        lexingErrorTable.getTableHeader().setReorderingAllowed(false);
        lexingErrorScrollPane.getViewport().setBackground(new Color(43, 43, 43));
        lexingErrorDTCR.setBackground(new Color(43, 43, 43));
        lexingErrorDTCR.setForeground(new Color(222, 106, 92));
        lexingErrorScrollPane.setBackground(new Color(60, 63, 65));
    }

    void clear() {
        //将表格内容清空，注意要先取出行数，否则无法删除干净
        int errorRows = lexingErrorDTM.getRowCount();
        for(int i = 0; i < errorRows; i++) {
            lexingErrorDTM.removeRow(0);
            lexingErrorTable.updateUI();
        }
        lexingErrorDTCR.setBackground(new Color(43, 43, 43));
        lexingErrorDTCR.setForeground(new Color(222, 106, 92));
    }

    private JTable lexingErrorTable;
    public DefaultTableModel lexingErrorDTM;
    private DefaultTableCellRenderer lexingErrorDTCR;
    private JScrollPane lexingErrorScrollPane;
}
