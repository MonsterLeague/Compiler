package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ResultTabbedPane extends JTabbedPane {
    public ResultTabbedPane() {
        super(JTabbedPane.TOP);
        setUI(new DemoTabbedPaneUI(75, 15));
        setForeground(Color.WHITE);
        setFont(new Font("微软雅黑", Font.PLAIN, 15));
        lexingResultPanel = new LexingResultPanel();
        parsingResultPanel = new ParsingResultPanel();
        addTab("词法分析结果", lexingResultPanel);
        addTab("语法分析结果", parsingResultPanel);
        addTab("语义分析结果", new JPanel());
    }

    public void clear() {
        lexingResultPanel.clear();
        parsingResultPanel.clear();
    }

    public DefaultTableModel getLexingResultDTM() {
        return lexingResultPanel.lexingResultDTM;
    }

    public DefaultTableModel getSymbolDTM() {
        return lexingResultPanel.symbolDTM;
    }

    public DefaultTableModel getParsingDTM() {
        return parsingResultPanel.parsingResultDTM;
    }

    private LexingResultPanel lexingResultPanel;
    private ParsingResultPanel parsingResultPanel;
}
