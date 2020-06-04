package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ErrorTabbedPane extends JTabbedPane {
    public ErrorTabbedPane() {
        super(JTabbedPane.LEFT);
        setUI(new DemoTabbedPaneUI(0, 50));
        setForeground(Color.WHITE);
        setFont(new Font("微软雅黑", Font.PLAIN, 15));
        lexingErrorPanel = new LexingErrorPanel();
        parsingErrorPanel = new ParsingErrorPanel();
        addTab("词法错误", lexingErrorPanel);
        addTab("语法错误", parsingErrorPanel);
        addTab("语义错误", new JPanel());
    }
    public void clear() {
        lexingErrorPanel.clear();
        parsingErrorPanel.clear();
    }
    public DefaultTableModel getLexingErrorDTM() {
        return lexingErrorPanel.lexingErrorDTM;
    }
    public DefaultTableModel getParsingErrorDTM() {
        return parsingErrorPanel.parsingErrorDTM;
    }
    private LexingErrorPanel lexingErrorPanel;
    private ParsingErrorPanel parsingErrorPanel;
}
