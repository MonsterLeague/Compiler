package UI;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SemanticResultPanel extends JPanel {
    public SemanticResultPanel() {
        setBackground(new Color(60, 63, 65));
        initComponents();
        initLayout();
    }

    void initComponents() {
        //设置表格各种格式
        addrCodeDTM = new DefaultTableModel(null, new String[]{"位置号", "指令"}) {
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        addrCodeTable = new JTable(addrCodeDTM);
        //addrCodeTable.setEnabled(false);
        addrCodeScrollPane = new JScrollPane(addrCodeTable);
        addrCodeDTCR = new DefaultTableCellRenderer();
        addrCodeTable.setDefaultRenderer(Object.class, addrCodeDTCR);
        addrCodeTable.getTableHeader().setResizingAllowed(false);

        addrCodeTabbedPane = new JTabbedPane();

        addrCodeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 1) {
                    int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint()); //获得行位置
                    int col = ((JTable) e.getSource()).columnAtPoint(e.getPoint()); //获得列位置
                    String cellVal = (String)(addrCodeDTM.getValueAt(row,col)); //获得点击单元格数据
                    UIManager UI = new UIManager();
                    UI.put("OptionPane.background", new Color(60, 63, 65));
                    UI.put("Panel.background", new Color(60, 63, 65));
                    UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("微软雅黑", Font.PLAIN, 15)));
                    UIManager.put("OptionPane.messageForeground", Color.WHITE);
                    JOptionPane.showMessageDialog(addrCodeScrollPane, cellVal, "表格信息", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        TableColumn col = addrCodeTable.getColumnModel().getColumn(0);
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(SwingConstants.CENTER);
        render.setBackground(new Color(43, 43, 43));
        render.setForeground(Color.WHITE);
        col.setCellRenderer(render);
    }

    void initLayout() {
        setLayout(null);
        add(addrCodeTabbedPane);
        addrCodeTabbedPane.setUI(new DemoTabbedPaneUI(253, 10));
        addrCodeTabbedPane.addTab("三地址码", addrCodeScrollPane);
        addrCodeTabbedPane.setBounds(117, 0, 351, 580);
        addrCodeTabbedPane.setForeground(Color.WHITE);
        addrCodeTabbedPane.setFont(new Font("微软雅黑", Font.PLAIN, 15));

        addrCodeScrollPane.getVerticalScrollBar().setUI(new DemoScrollBarUI());

        addrCodeTable.setRowHeight(25);
        addrCodeTable.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        addrCodeTable.getTableHeader().setBackground(new Color(60, 63, 65));
        addrCodeTable.getTableHeader().setForeground(Color.WHITE);
        addrCodeTable.getTableHeader().setPreferredSize(new Dimension(0, 35));
        addrCodeTable.getTableHeader().setFont(new Font("微软雅黑", Font.PLAIN, 15));
        addrCodeTable.getTableHeader().setReorderingAllowed(false);
        addrCodeScrollPane.getViewport().setBackground(new Color(43, 43, 43));
        addrCodeDTCR.setBackground(new Color(43, 43, 43));
        addrCodeDTCR.setForeground(Color.WHITE);
        addrCodeScrollPane.setBackground(new Color(60, 63, 65));
    }

    public void clear() {
        //将表格内容清空，注意要先取出行数，否则无法删除干净
        int resultRows = addrCodeDTM.getRowCount();
        for (int i = 0; i < resultRows; i++) {
            addrCodeDTM.removeRow(0);
            addrCodeTable.updateUI();
        }
        addrCodeDTCR.setBackground(new Color(43, 43, 43));
        addrCodeDTCR.setForeground(Color.WHITE);
        TableColumn col = addrCodeTable.getColumnModel().getColumn(0);
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(SwingConstants.CENTER);
        render.setBackground(new Color(43, 43, 43));
        render.setForeground(Color.WHITE);
        col.setCellRenderer(render);
    }

    private JTable addrCodeTable;
    public DefaultTableModel addrCodeDTM;
    private DefaultTableCellRenderer addrCodeDTCR;
    private JScrollPane addrCodeScrollPane;
    private JTabbedPane addrCodeTabbedPane;
}
