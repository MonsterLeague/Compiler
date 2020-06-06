package UI;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TotalStackPanel extends JPanel {
    public TotalStackPanel(int width, int height, DefaultTableModel expandedStackDTM, DefaultTableModel parsingResultDTM) {
        setBackground(new Color(60, 63, 65));
        this.width = width;
        this.height = height;
        this.expandedStackDTM = expandedStackDTM;
        this.parsingResultDTM = parsingResultDTM;
        initComponents();
        initLayout();
    }

    void initComponents() {
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
        add(expandedStackScrollPane);

        expandedStackScrollPane.setBounds(width * 2 / 3 + 1, 0, width / 3 - 20, height);
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

        add(parsingResultScrollPane);

        parsingResultScrollPane.setBounds(0, 0, width * 2 / 3, height);
        parsingResultScrollPane.getVerticalScrollBar().setUI(new DemoScrollBarUI());
        parsingResultTable.setRowHeight(25);
        //parsingResultTable.getColumnModel().getColumn(0).setMaxWidth();
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

    private JTable expandedStackTable;
    private DefaultTableModel expandedStackDTM;
    private DefaultTableCellRenderer expandedStackDTCR;
    private JScrollPane expandedStackScrollPane;
    private JTable parsingResultTable;
    private DefaultTableModel parsingResultDTM;
    private DefaultTableCellRenderer parsingResultDTCR;
    private JScrollPane parsingResultScrollPane;
    private int width;
    private int height;
}
