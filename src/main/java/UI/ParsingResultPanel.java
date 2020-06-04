package UI;

import UI.DemoScrollBarUI;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class ParsingResultPanel extends JPanel {
    public ParsingResultPanel(int width, int height, DefaultTableModel DTM) {
        setBackground(new Color(60, 63, 65));
        this.width = width;
        this.height = height;
        this.parsingResultDTM = DTM;
        initComponents();
        initLayout();
    }

    public ParsingResultPanel() {
        setBackground(new Color(60, 63, 65));
        this.width = 580;
        this.height = 580;
        parsingResultDTM = new DefaultTableModel(null, new String[]{"栈", "符号", "输入", "动作"}) {
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        initComponents();
        initLayout();
    }

    void initComponents() {
        parsingResultTable = new JTable(parsingResultDTM);
        //parsingResultTable.setEnabled(false);
        parsingResultScrollPane = new JScrollPane(parsingResultTable);
        parsingResultDTCR = new DefaultTableCellRenderer();
        //parsingResultDTCR.setHorizontalAlignment(JLabel.CENTER);
        parsingResultTable.setDefaultRenderer(Object.class, parsingResultDTCR);
        parsingResultTable.getTableHeader().setResizingAllowed(false);
        parsingResultTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 1) {
                    int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint()); //获得行位置
                    int col = ((JTable) e.getSource()).columnAtPoint(e.getPoint()); //获得列位置
                    String cellVal = (String)(parsingResultDTM.getValueAt(row,col)); //获得点击单元格数据
                    UIManager UI = new UIManager();
                    UI.put("OptionPane.background", new Color(60, 63, 65));
                    UI.put("Panel.background", new Color(60, 63, 65));
                    UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("微软雅黑", Font.PLAIN, 15)));
                    UIManager.put("OptionPane.messageForeground", Color.WHITE);
                    JOptionPane.showMessageDialog(parsingResultScrollPane, cellVal, "表格信息", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    void initLayout() {
        setLayout(null);
        add(parsingResultScrollPane);

        parsingResultScrollPane.setBounds(0, 0, width, height);
        parsingResultScrollPane.getVerticalScrollBar().setUI(new DemoScrollBarUI());
        parsingResultTable.setRowHeight(25);
        parsingResultTable.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        parsingResultTable.getTableHeader().setBackground(new Color(60, 63, 65));
        parsingResultTable.getTableHeader().setForeground(Color.WHITE);
        parsingResultTable.getTableHeader().setPreferredSize(new Dimension(0, 35));
        parsingResultTable.getTableHeader().setFont(new Font("微软雅黑", Font.PLAIN, 15));
        parsingResultTable.getTableHeader().setReorderingAllowed(false);
        parsingResultScrollPane.getViewport().setBackground(new Color(43, 43, 43));
        parsingResultDTCR.setBackground(new Color(43, 43, 43));
        parsingResultDTCR.setForeground(Color.WHITE);
        parsingResultScrollPane.setBackground(new Color(60, 63, 65));
    }

    public void clear() {
        //将表格内容清空，注意要先取出行数，否则无法删除干净
        int rows = parsingResultDTM.getRowCount();
        for (int i = 0; i < rows; i++) {
            parsingResultDTM.removeRow(0);
            parsingResultTable.updateUI();
        }
        parsingResultDTCR.setBackground(new Color(43, 43, 43));
        parsingResultDTCR.setForeground(Color.WHITE);
    }

    private JTable parsingResultTable;
    public DefaultTableModel parsingResultDTM;
    private DefaultTableCellRenderer parsingResultDTCR;
    private JScrollPane parsingResultScrollPane;
    private int width;
    private int height;
}
