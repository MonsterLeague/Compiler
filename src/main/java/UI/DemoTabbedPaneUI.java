package UI;

import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

class DemoTabbedPaneUI extends BasicTabbedPaneUI {
    private int widthOffset = 50;
    private int heightOffset = 30;
    protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
        //自定义选项卡的高：比默认的高度 高30
        return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight) + heightOffset;
    }

    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        //自定义选项卡的宽：比默认的宽度 宽50
        return super.calculateTabWidth(tabPlacement, tabIndex, metrics) + widthOffset;
    }

    public DemoTabbedPaneUI(int w, int h) {
        widthOffset = w;
        heightOffset = h;
    }

    /**
     * 自定义选项卡的背景色
     *
     * @param g 图形设置
     * @param tabPlacement 标签位置
     * @param tabIndex 标签下标
     * @param x x轴
     * @param y y轴
     * @param w 宽
     * @param h 高
     * @param isSelected 是否被选中
     */
    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex,
                                      int x, int y, int w, int h, boolean isSelected) {
        Color defaultColor = new Color(60, 63, 65);
        Color selectedColor = new Color(43, 43, 43);
        //设置选中时和未被选中时的颜色
        g.setColor(!isSelected ? defaultColor : selectedColor);
        //填充图形，即选项卡为矩形
        g.fillRect(x + 5, y + 5, w, h);
    }

    /**
     * 绘制标签的边框
     * @param g 图形
     * @param tabPlacement 标签位置
     * @param tabIndex 标签下标
     * @param x x轴
     * @param y y轴
     * @param w 宽
     * @param h 高
     * @param isSelected 标签是否被选中
     */
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
                                  int x, int y, int w, int h, boolean isSelected) {//自定义选项卡的边框色
        //注意：这个方法中的具体实现内容，可以写，也可以不写，根据自己实际情况决定。但这个方法定义必须有。否则会影响最终的显示效果
        Color defaultColor = Color.WHITE;
        Color selectedColor = Color.WHITE;
        //设置选中时和未被选中时的颜色
        g.setColor(!isSelected ? defaultColor : selectedColor);
        //绘制边框，即选项卡边框为矩形
        g.drawRect(x + 5, y + 5, w, h);
    }

    /*protected void paintText(Graphics g,
                             int tabPlacement, Font font, FontMetrics metrics,
                             int tabIndex, String title, Rectangle textRect,
                             boolean isSelected) {//定义选项卡上的文本（颜色，字体，大小）

        g.setColor(isSelected ? Color.white : new Color(189, 189, 189));
        g.setFont(new Font("微软雅黑", Font.PLAIN, 18));

        int h = calculateTabHeight(tabPlacement, tabIndex, 18);
        int w = calculateTabWidth(tabPlacement, tabIndex, metrics);
        //分别在选项卡上画上文字。(文字的具体位置计算是根据定义的字体大小算出的)
        if (tabIndex == 0) {
            g.drawString(title, 45, h / 2 + 11);
        } else if (tabIndex == 1) {
            g.drawString(title, 236, h / 2 + 11);
        } else if (tabIndex == 2) {
            g.drawString(title, 427, h / 2 + 11);
        }
    }*/

    protected void paintFocusIndicator(Graphics g,
                                       int tabPlacement, Rectangle[] rects,
                                       int tabIndex, Rectangle iconRect, Rectangle textRect,
                                       boolean isSelected) {//这个方法定义如果没有的话，选项卡在选中时，内测会有虚线。
        //Do nothing
    }

    protected LayoutManager createLayoutManager() {// 设置Layout
        return new TabbedPaneLayout();
    }

    public class TabbedPaneLayout extends BasicTabbedPaneUI.TabbedPaneLayout {
        // 要想实现：1.选中选项卡时，选项卡突出显示 2.选项卡之间有间距。那么必须重写以下方法！！
        protected void calculateTabRects(int tabPlacement, int tabCount) {
            super.calculateTabRects(tabPlacement, tabCount);
            // 设置间距
            setRec(5);
        }

        public void setRec(int rec) {
            for (int i = 0; i < rects.length; i++) {
                rects[i].x = rects[i].x + rec * i;
            }
        }
    }

}