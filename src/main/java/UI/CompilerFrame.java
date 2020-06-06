package UI;

import Core.AnalyseList;
import Core.TextLex;
import Core.TextParse;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class CompilerFrame extends JFrame implements ActionListener {

    public CompilerFrame() {
        // 设置标题
        setTitle("Compiler");
        // 设置窗口大小
        setSize(1200,700);
        //设置窗口图标
        setIconImage(Toolkit.getDefaultToolkit().getImage(CompilerFrame.class.getResource("/icon.jpg")));
        //初始化组件
        initComponents();
        //初始化布局
        initLayout();
    }

    public void initComponents() {

        //初始化窗口菜单栏
        menubar = new JMenuBar();

        menubarFile = new JMenu("文件");
        menubarFileOpen = new JMenuItem("打开");
        menubarFileClose = new JMenuItem("关闭");
        menubarFileSave = new JMenuItem("保存");
        menubarFileSaveAs = new JMenuItem("另存为");
        menubarFileExit = new JMenuItem("退出");
        menubarFileOpen.addActionListener(this);
        menubarFileClose.addActionListener(this);
        menubarFileSave.addActionListener(this);
        menubarFileSaveAs.addActionListener(this);
        menubarFileExit.addActionListener(this);

        menubarExecute = new JMenu("运行");
        menubarExecuteLexing = new JMenuItem("词法分析");
        menubarExecuteParsing = new JMenuItem("语法分析");
        menubarExecuteSemantic = new JMenuItem("语义分析");
        menubarExecuteClear = new JMenuItem("清空结果");
        menubarExecuteLexing.addActionListener(this);
        menubarExecuteParsing.addActionListener(this);
        menubarExecuteSemantic.addActionListener(this);
        menubarExecuteClear.addActionListener(this);

        menubarView = new JMenu("视图");
        menubarViewParsingResult = new JMenuItem("语法分析结果");
        menubarViewParsingResult.addActionListener(this);
        menubarViewExpandedStack = new JMenuItem("语义分析拓展栈");
        menubarViewExpandedStack.addActionListener(this);
        menubarViewTotalStack = new JMenuItem("总分析栈");
        menubarViewTotalStack.addActionListener(this);

        menubarHelp = new JMenu("帮助");
        menubarHelpAbout = new JMenuItem("关于我们");
        menubarHelpAbout.addActionListener(this);

        mainPanel = new JPanel();

        editCodeArea = new JTextArea();
        editCodeScrollPane = new JScrollPane(editCodeArea);

        errorLabel = new JLabel("错误日志");
        errorIcon = new JLabel(new ImageIcon(CompilerFrame.class.getResource("/warning.png")));

        resultTabbedPane = new ResultTabbedPane();
        errorTabbedPane = new ErrorTabbedPane();

        analyseList = new AnalyseList();

        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        expandedStackPanel = new ExpandedStackPanel(width, height - 90);
    }

    public void initLayout() {
        menubar.add(menubarFile);
        menubar.add(menubarExecute);
        menubar.add(menubarView);
        menubar.add(menubarHelp);
        menubarFile.add(menubarFileOpen);
        menubarFile.add(menubarFileClose);
        menubarFile.add(menubarFileSave);
        menubarFile.add(menubarFileSaveAs);
        menubarFile.add(menubarFileExit);
        menubarExecute.add(menubarExecuteLexing);
        menubarExecute.add(menubarExecuteParsing);
        menubarExecute.add(menubarExecuteSemantic);
        menubarExecute.add(menubarExecuteClear);
        menubarView.add(menubarViewParsingResult);
        menubarView.add(menubarViewExpandedStack);
        menubarView.add(menubarViewTotalStack);
        menubarHelp.add(menubarHelpAbout);
        setJMenuBar(menubar);
        mainPanel.add(editCodeScrollPane);
        mainPanel.add(resultTabbedPane);
        mainPanel.add(errorLabel);
        mainPanel.add(errorIcon);
        mainPanel.add(errorTabbedPane);
        add(mainPanel);

        mainPanel.setLayout(null);
        editCodeScrollPane.setBounds(5, 5, 580, 360);
        resultTabbedPane.setBounds(605, 0, 585, 626);
        errorTabbedPane.setBounds(0, 390, 585, 235);

        errorLabel.setBounds(38, 368, 100, 20);
        errorIcon.setBounds(10, 363, 30, 30);

        editCodeScrollPane.setRowHeaderView(new LineNumberHeaderView());

        editCodeScrollPane.getVerticalScrollBar().setUI(new DemoScrollBarUI());
        editCodeScrollPane.getHorizontalScrollBar().setUI(new DemoScrollBarUI());

        menubar.setBackground(new Color(60, 63, 65));
        menubar.setPreferredSize(new Dimension(0, 35));

        menubarFile.setForeground(Color.WHITE);
        menubarExecute.setForeground(Color.WHITE);
        menubarView.setForeground(Color.WHITE);
        menubarHelp.setForeground(Color.WHITE);

        menubarFileOpen.setForeground(Color.WHITE);
        menubarFileClose.setForeground(Color.WHITE);
        menubarFileSave.setForeground(Color.WHITE);
        menubarFileSaveAs.setForeground(Color.WHITE);
        menubarFileExit.setForeground(Color.WHITE);
        menubarExecuteLexing.setForeground(Color.WHITE);
        menubarExecuteParsing.setForeground(Color.WHITE);
        menubarExecuteSemantic.setForeground(Color.WHITE);
        menubarExecuteClear.setForeground(Color.WHITE);
        menubarViewParsingResult.setForeground(Color.WHITE);
        menubarViewExpandedStack.setForeground(Color.WHITE);
        menubarViewTotalStack.setForeground(Color.WHITE);
        menubarHelpAbout.setForeground(Color.WHITE);

        menubarFileOpen.setBackground(new Color(60, 63, 65));
        menubarFileClose.setBackground(new Color(60, 63, 65));
        menubarFileSave.setBackground(new Color(60, 63, 65));
        menubarFileSaveAs.setBackground(new Color(60, 63, 65));
        menubarFileExit.setBackground(new Color(60, 63, 65));
        menubarExecuteLexing.setBackground(new Color(60, 63, 65));
        menubarExecuteParsing.setBackground(new Color(60, 63, 65));
        menubarExecuteSemantic.setBackground(new Color(60, 63, 65));
        menubarExecuteClear.setBackground(new Color(60, 63, 65));
        menubarViewParsingResult.setBackground(new Color(60, 63, 65));
        menubarViewExpandedStack.setBackground(new Color(60, 63, 65));
        menubarViewTotalStack.setBackground(new Color(60, 63, 65));
        menubarHelpAbout.setBackground(new Color(60, 63, 65));

        menubarFile.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        menubarExecute.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        menubarView.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        menubarHelp.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        menubarFileOpen.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        menubarFileClose.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        menubarFileSave.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        menubarFileSaveAs.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        menubarFileExit.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        menubarExecuteLexing.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        menubarExecuteParsing.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        menubarExecuteSemantic.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        menubarExecuteClear.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        menubarViewParsingResult.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        menubarViewExpandedStack.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        menubarViewTotalStack.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        menubarHelpAbout.setFont(new Font("微软雅黑", Font.PLAIN, 15));

        mainPanel.setBackground(new Color(60, 63, 65));

        editCodeArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
        editCodeArea.setForeground(Color.WHITE);
        editCodeArea.setBackground(new Color(43, 43, 43));
        editCodeArea.setCaretColor(Color.WHITE);
        editCodeScrollPane.setBackground(new Color(60, 63, 65));

        errorLabel.setFont(new Font("微软雅黑", Font.BOLD, 15));
        errorLabel.setForeground(Color.WHITE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //当运行词法分析
        if (e.getSource() == menubarExecuteLexing) {
            clearTableData();
            if(editCodeArea.getText().equals("")){
                JOptionPane.showMessageDialog(mainPanel, "未输入代码！", "提示", JOptionPane.WARNING_MESSAGE);
            }
            else {
                // 词法分析
                TextLex lex = new TextLex(editCodeArea.getText(), resultTabbedPane.getLexingResultDTM(),
                        errorTabbedPane.getLexingErrorDTM(), resultTabbedPane.getSymbolDTM());
                lex.scannerAll();
            }
        }
        //当运行语法分析
        else if (e.getSource() == menubarExecuteParsing) {
            clearTableData();
            if(editCodeArea.getText().equals("")){
                JOptionPane.showMessageDialog(mainPanel, "未输入代码！", "提示", JOptionPane.WARNING_MESSAGE);
            }
            else {
                // 词法分析
                TextLex lex = new TextLex(editCodeArea.getText(), resultTabbedPane.getLexingResultDTM(),
                        errorTabbedPane.getLexingErrorDTM(), resultTabbedPane.getSymbolDTM());
                lex.scannerAll();
                // 语法分析
                if (lex.get_Lex_Error().size() != 0) {
                    JOptionPane.showMessageDialog(mainPanel, "词法分析阶段出现错误！", "提示", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    TextParse textParse = new TextParse(analyseList, lex.get_Lex_Result(), lex.get_Lex_Table(),
                            errorTabbedPane.getParsingErrorDTM(), resultTabbedPane.getParsingDTM(),
                            new DefaultTableModel(), new DefaultTableModel());
                    textParse.Parsing();
                }
            }
        }
        //当运行语义分析
        else if (e.getSource() == menubarExecuteSemantic) {
            clearTableData();
            if(editCodeArea.getText().equals("")){
                JOptionPane.showMessageDialog(mainPanel, "未输入代码！", "提示", JOptionPane.WARNING_MESSAGE);
            }
            else {
                // 词法分析
                TextLex lex = new TextLex(editCodeArea.getText(), resultTabbedPane.getLexingResultDTM(),
                        errorTabbedPane.getLexingErrorDTM(), resultTabbedPane.getSymbolDTM());
                lex.scannerAll();
                // 语法分析
                if (lex.get_Lex_Error().size() != 0) {
                    JOptionPane.showMessageDialog(mainPanel, "词法分析阶段出现错误！", "提示", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    TextParse parse = new TextParse(analyseList, lex.get_Lex_Result(), lex.get_Lex_Table(),
                            errorTabbedPane.getParsingErrorDTM(), resultTabbedPane.getParsingDTM(),
                            expandedStackPanel.expandedStackDTM, resultTabbedPane.getSemanticDTM());
                    parse.Parsing();
                }
            }
        }
        //当点击清空结果
        else if (e.getSource() == menubarExecuteClear) {
            clearTableData();
        }
        //当点击打开文件
        else if (e.getSource() == menubarFileOpen) {
            String fileName;
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (fileChooser.showOpenDialog(mainPanel) == JFileChooser.APPROVE_OPTION) {
                fileName = fileChooser.getSelectedFile().getPath();
                File file = new File(fileName);
                try{
                    InputStream in = new FileInputStream(file);
                    int tempByte = -1;
                    editCodeArea.setText("");
                    while ((tempByte = in.read()) != -1) {
                        editCodeArea.append("" + (char)tempByte);
                    }
                    in.close();
                }
                catch(Exception event){
                    event.printStackTrace();
                }
            }
        }
        //当点击关闭文件
        else if (e.getSource() == menubarFileClose) {
            editCodeArea.setText("");
            clearTableData();
        }
        //当点击退出
        else if (e.getSource() == menubarFileExit) {
            System.exit(0);
        }
        //当点击语法分析结果视图
        else if (e.getSource() == menubarViewParsingResult) {
            JFrame jf = new JFrame();
            // 设置标题
            jf.setTitle("语法分析结果视图");
            // 设置窗口大小
            int width = Toolkit.getDefaultToolkit().getScreenSize().width;
            int height = Toolkit.getDefaultToolkit().getScreenSize().height;
            jf.setSize(width, height);
            //设置窗口图标
            jf.setIconImage(Toolkit.getDefaultToolkit().getImage(CompilerFrame.class.getResource("/enlarge.png")));
            jf.setContentPane(new ParsingResultPanel(width, height - 90, resultTabbedPane.getParsingDTM()));
            jf.setLocation(0, 0);
            jf.setVisible(true);
        }
        //当点击语义分析拓展栈视图
        else if (e.getSource() == menubarViewExpandedStack) {
            JFrame jf = new JFrame();
            // 设置标题
            jf.setTitle("语义分析拓展栈视图");
            // 设置窗口大小
            int width = Toolkit.getDefaultToolkit().getScreenSize().width;
            int height = Toolkit.getDefaultToolkit().getScreenSize().height;
            jf.setSize(width, height);
            //设置窗口图标
            jf.setIconImage(Toolkit.getDefaultToolkit().getImage(CompilerFrame.class.getResource("/enlarge.png")));
            jf.setContentPane(expandedStackPanel);
            jf.setLocation(0, 0);
            jf.setVisible(true);
        }
        //当点击总分析栈
        else if (e.getSource() == menubarViewTotalStack) {
            JFrame jf = new JFrame();
            // 设置标题
            jf.setTitle("总分析栈视图");
            // 设置窗口大小
            int width = Toolkit.getDefaultToolkit().getScreenSize().width;
            int height = Toolkit.getDefaultToolkit().getScreenSize().height;
            jf.setSize(width, height);
            //设置窗口图标
            jf.setIconImage(Toolkit.getDefaultToolkit().getImage(CompilerFrame.class.getResource("/enlarge.png")));
            jf.setContentPane(new TotalStackPanel(width, height - 90, expandedStackPanel.expandedStackDTM,
                                resultTabbedPane.getParsingDTM()));
            jf.setLocation(0, 0);
            jf.setVisible(true);
        }
        //当点击关于我们
        else if (e.getSource() == menubarHelpAbout) {
            JOptionPane.showMessageDialog(mainPanel, "编译原理课程设计\n031702645吴宜航\n031702646鲍子涵", "关于我们", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void clearTableData() {
        resultTabbedPane.clear();
        errorTabbedPane.clear();
        expandedStackPanel.clear();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //Graphics2D g2 = (Graphics2D) g;
        //g2.setColor(new Color(122, 138, 153));
        //g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
        //g2.draw(new Line2D.Float(578, 73, 603, 73));
        //g2.draw(new Line2D.Float(578, 691, 603, 691));

        //g2.setColor(Color.WHITE);
        //g2.draw(new Line2D.Float(578, 431, 603, 431));
        //g2.draw(new Line2D.Float(578, 458, 603, 458));
        //g2.draw(new Line2D.Float(577, 431, 577, 458));
        //g2.draw(new Line2D.Float(604, 431, 604, 458));

        //g2.draw(new Line2D.Float(9, 431, 9, 458));
        //g2.draw(new Line2D.Float(790, 431, 790, 458));

        //g2.setColor(new Color(103, 100, 93));
        //g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
        //g2.draw(new Line2D.Float(11, 460, 574, 460));
        //g2.draw(new Line2D.Float(606, 460, 787, 460));

        //g2.setStroke(new BasicStroke(4f));
        //g2.draw(new Line2D.Float(100, 433, 115, 456));
        //g2.draw(new Line2D.Float(683, 433, 698, 456));

        //repaint();
        //try {
        //    Thread.sleep(20);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
    }

    private JPanel mainPanel;
    private JMenuBar menubar;
    private JMenu menubarFile;
    private JMenu menubarExecute;
    private JMenu menubarView;
    private JMenu menubarHelp;
    private JMenuItem menubarFileOpen;
    private JMenuItem menubarFileClose;
    private JMenuItem menubarFileSave;
    private JMenuItem menubarFileSaveAs;
    private JMenuItem menubarFileExit;
    private JMenuItem menubarExecuteLexing;
    private JMenuItem menubarExecuteParsing;
    private JMenuItem menubarExecuteSemantic;
    private JMenuItem menubarExecuteClear;
    private JMenuItem menubarViewParsingResult;
    private JMenuItem menubarViewExpandedStack;
    private JMenuItem menubarViewTotalStack;
    private JMenuItem menubarHelpAbout;
    private JTextArea editCodeArea;
    private JScrollPane editCodeScrollPane;
    private JLabel errorLabel;
    private JLabel errorIcon;
    private ResultTabbedPane resultTabbedPane;
    private ErrorTabbedPane errorTabbedPane;
    private AnalyseList analyseList;
    private ExpandedStackPanel expandedStackPanel;
}