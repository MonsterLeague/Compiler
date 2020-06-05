package UI;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ExpandedStackPanel extends JPanel {
    public ExpandedStackPanel(int width, int height) {
        setBackground(new Color(60, 63, 65));
        this.width = width;
        this.height = height;
        initComponents();
        initLayout();
    }

    void initComponents() {
        expandedStackDTM = new DefaultTableModel(null, new String[]{"记号", "符号", "addr",
                "truelist", "falselist", "nextlist"}) {
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        expandedStackTable = new JTable(expandedStackDTM);
        //expandedStackTable.setEnabled(false);
        expandedStackScrollPane = new JScrollPane(expandedStackTable);
        expandedStackDTCR = new DefaultTableCellRenderer();
        expandedStackDTCR.setHorizontalAlignment(JLabel.CENTER);
        expandedStackTable.setDefaultRenderer(Object.class, expandedStackDTCR);
        expandedStackTable.getTableHeader().setResizingAllowed(false);
        expandedStackTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 1) {
                    int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint()); //获得行位置
                    int col = ((JTable) e.getSource()).columnAtPoint(e.getPoint()); //获得列位置
                    String cellVal = (String)(expandedStackDTM.getValueAt(row,col)); //获得点击单元格数据
                    UIManager UI = new UIManager();
                    UI.put("OptionPane.background", new Color(60, 63, 65));
                    UI.put("Panel.background", new Color(60, 63, 65));
                    UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("微软雅黑", Font.PLAIN, 15)));
                    UIManager.put("OptionPane.messageForeground", Color.WHITE);
                    JOptionPane.showMessageDialog(expandedStackScrollPane, cellVal, "表格信息", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    void initLayout() {
        setLayout(null);
        add(expandedStackScrollPane);

        expandedStackScrollPane.setBounds(0, 0, width, height);
        expandedStackScrollPane.getVerticalScrollBar().setUI(new DemoScrollBarUI());
        expandedStackTable.setRowHeight(25);
        expandedStackTable.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        expandedStackTable.getTableHeader().setBackground(new Color(60, 63, 65));
        expandedStackTable.getTableHeader().setForeground(Color.WHITE);
        expandedStackTable.getTableHeader().setPreferredSize(new Dimension(0, 35));
        expandedStackTable.getTableHeader().setFont(new Font("微软雅黑", Font.PLAIN, 15));
        expandedStackTable.getTableHeader().setReorderingAllowed(false);
        expandedStackScrollPane.getViewport().setBackground(new Color(43, 43, 43));
        expandedStackDTCR.setBackground(new Color(43, 43, 43));
        expandedStackDTCR.setForeground(Color.WHITE);
        expandedStackScrollPane.setBackground(new Color(60, 63, 65));
    }

    public void clear() {
        //将表格内容清空，注意要先取出行数，否则无法删除干净
        int rows = expandedStackDTM.getRowCount();
        for (int i = 0; i < rows; i++) {
            expandedStackDTM.removeRow(0);
            expandedStackTable.updateUI();
        }
        expandedStackDTCR.setBackground(new Color(43, 43, 43));
        expandedStackDTCR.setForeground(Color.WHITE);
    }

    private JTable expandedStackTable;
    public DefaultTableModel expandedStackDTM;
    private DefaultTableCellRenderer expandedStackDTCR;
    private JScrollPane expandedStackScrollPane;
    private int width;
    private int height;
}
