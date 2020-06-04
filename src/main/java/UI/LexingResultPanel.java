package UI;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LexingResultPanel extends JPanel {
    public LexingResultPanel() {
        setBackground(new Color(60, 63, 65));
        initComponents();
        initLayout();
    }

    void initComponents() {
        //设置表格各种格式
        lexingResultDTM = new DefaultTableModel(null, new String[]{"词法单元"}) {
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        lexingResultTable = new JTable(lexingResultDTM);
        //lexingResultTable.setEnabled(false);
        lexingResultScrollPane = new JScrollPane(lexingResultTable);
        lexingResultDTCR = new DefaultTableCellRenderer();
        lexingResultDTCR.setHorizontalAlignment(JLabel.CENTER);
        lexingResultTable.setDefaultRenderer(Object.class, lexingResultDTCR);
        lexingResultTable.getTableHeader().setResizingAllowed(false);

        symbolDTM = new DefaultTableModel(null, new String[]{"类型", "符号"}) {
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        symbolTable = new JTable(symbolDTM);
        //symbolTable.setEnabled(false);
        symbolScrollPane = new JScrollPane(symbolTable);
        symbolDTCR = new DefaultTableCellRenderer();
        symbolDTCR.setHorizontalAlignment(JLabel.CENTER);
        symbolTable.setDefaultRenderer(Object.class, symbolDTCR);
        symbolTable.getTableHeader().setResizingAllowed(false);

        //symbolLabel = new JLabel("符号表");
        //symbolcon = new JLabel(new ImageIcon(ParserFrame.class.getResource("table.png")));

        lexingResultTabbedPane = new JTabbedPane();
        symbolTabbedPane = new JTabbedPane();

        lexingResultTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 1) {
                    int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint()); //获得行位置
                    int col = ((JTable) e.getSource()).columnAtPoint(e.getPoint()); //获得列位置
                    String cellVal = (String)(lexingResultDTM.getValueAt(row,col)); //获得点击单元格数据
                    UIManager UI = new UIManager();
                    UI.put("OptionPane.background", new Color(60, 63, 65));
                    UI.put("Panel.background", new Color(60, 63, 65));
                    UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("微软雅黑", Font.PLAIN, 15)));
                    UIManager.put("OptionPane.messageForeground", Color.WHITE);
                    JOptionPane.showMessageDialog(lexingResultScrollPane, cellVal, "表格信息", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        symbolTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 1) {
                    int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint()); //获得行位置
                    int col = ((JTable) e.getSource()).columnAtPoint(e.getPoint()); //获得列位置
                    String cellVal = (String)(symbolDTM.getValueAt(row,col)); //获得点击单元格数据
                    UIManager UI = new UIManager();
                    UI.put("OptionPane.background", new Color(60, 63, 65));
                    UI.put("Panel.background", new Color(60, 63, 65));
                    UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("微软雅黑", Font.PLAIN, 15)));
                    UIManager.put("OptionPane.messageForeground", Color.WHITE);
                    JOptionPane.showMessageDialog(symbolScrollPane, cellVal, "表格信息", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    void initLayout() {
        setLayout(null);
        add(lexingResultTabbedPane);
        add(symbolTabbedPane);
        lexingResultTabbedPane.setUI(new DemoTabbedPaneUI(215, 10));
        symbolTabbedPane.setUI(new DemoTabbedPaneUI(198, 10));
        lexingResultTabbedPane.addTab("记号", lexingResultScrollPane);
        symbolTabbedPane.addTab("符号表", symbolScrollPane);
        lexingResultTabbedPane.setBounds(0, 0, 282, 580);
        symbolTabbedPane.setBounds(302, 0, 282, 580);
        lexingResultTabbedPane.setForeground(Color.WHITE);
        symbolTabbedPane.setForeground(Color.WHITE);
        lexingResultTabbedPane.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        symbolTabbedPane.setFont(new Font("微软雅黑", Font.PLAIN, 15));

        lexingResultScrollPane.getVerticalScrollBar().setUI(new DemoScrollBarUI());
        symbolScrollPane.getVerticalScrollBar().setUI(new DemoScrollBarUI());

        //symbolLabel.setBounds(332, 15, 100, 20);
        //symbolcon.setBounds(302, 10, 30, 30);
        //symbolLabel.setFont(new Font("微软雅黑", Font.BOLD, 15));
        //symbolLabel.setForeground(Color.WHITE);

        lexingResultTable.setRowHeight(25);
        lexingResultTable.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        lexingResultTable.getTableHeader().setBackground(new Color(60, 63, 65));
        lexingResultTable.getTableHeader().setForeground(Color.WHITE);
        lexingResultTable.getTableHeader().setPreferredSize(new Dimension(0, 35));
        lexingResultTable.getTableHeader().setFont(new Font("微软雅黑", Font.PLAIN, 15));
        lexingResultTable.getTableHeader().setReorderingAllowed(false);
        lexingResultScrollPane.getViewport().setBackground(new Color(43, 43, 43));
        lexingResultDTCR.setBackground(new Color(43, 43, 43));
        lexingResultDTCR.setForeground(Color.WHITE);
        lexingResultScrollPane.setBackground(new Color(60, 63, 65));

        symbolTable.setRowHeight(25);
        symbolTable.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        symbolTable.getTableHeader().setBackground(new Color(60, 63, 65));
        symbolTable.getTableHeader().setForeground(Color.WHITE);
        symbolTable.getTableHeader().setPreferredSize(new Dimension(0, 35));
        symbolTable.getTableHeader().setFont(new Font("微软雅黑", Font.PLAIN, 15));
        symbolTable.getTableHeader().setReorderingAllowed(false);
        symbolScrollPane.getViewport().setBackground(new Color(43, 43, 43));
        symbolDTCR.setBackground(new Color(43, 43, 43));
        symbolDTCR.setForeground(Color.WHITE);
        symbolScrollPane.setBackground(new Color(60, 63, 65));
    }

    public void clear() {
        //将表格内容清空，注意要先取出行数，否则无法删除干净
        int resultRows = lexingResultDTM.getRowCount();
        int symbolRows = symbolDTM.getRowCount();
        for (int i = 0; i < resultRows; i++) {
            lexingResultDTM.removeRow(0);
            lexingResultTable.updateUI();
        }
        for (int i = 0; i < symbolRows; i++) {
            symbolDTM.removeRow(0);
            symbolTable.updateUI();
        }
        lexingResultDTCR.setBackground(new Color(43, 43, 43));
        lexingResultDTCR.setForeground(Color.WHITE);
        symbolDTCR.setBackground(new Color(43, 43, 43));
        symbolDTCR.setForeground(Color.WHITE);
    }

    private JTable lexingResultTable;
    public DefaultTableModel lexingResultDTM;
    private DefaultTableCellRenderer lexingResultDTCR;
    private JScrollPane lexingResultScrollPane;
    private JTable symbolTable;
    public DefaultTableModel symbolDTM;
    private DefaultTableCellRenderer symbolDTCR;
    private JScrollPane symbolScrollPane;
    private JLabel symbolLabel;
    private JLabel symbolcon;
    private JTabbedPane lexingResultTabbedPane;
    private JTabbedPane symbolTabbedPane;
}
