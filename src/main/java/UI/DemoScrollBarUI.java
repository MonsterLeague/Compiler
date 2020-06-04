package UI;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

class DemoScrollBarUI extends BasicScrollBarUI {

    // 手柄宽度
    private static final int thumbWidth = 10;

    //手柄透明度
    private static final float opaque = 0.6f;

    // 手柄边框颜色
    private static final Color thumbColor = new Color(43, 43, 43);

    // 手柄颜色
    private static final Color thumbColorFrom = new Color(103, 100, 93);
    private static final Color thumbColorTo = new Color(103, 100, 93);

    // 滑道颜色
    private static final Color backColorFrom = new Color(43, 43, 43);
    private static final Color backColorTo = new Color(43, 43, 43);

    @Override
    protected void configureScrollBarColors() {
        setThumbBounds(0, 0, 3, 10);
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        c.setPreferredSize(new Dimension(thumbWidth, thumbWidth));
        return super.getPreferredSize(c);
    }

    // 重绘滑块的滑动区域背景
    public void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2 = (Graphics2D) g;
        GradientPaint gp = null;

        if (this.scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            gp = new GradientPaint(0, 0, backColorFrom, 0, trackBounds.height, backColorTo);
        }

        if (this.scrollbar.getOrientation() == JScrollBar.HORIZONTAL) {
            gp = new GradientPaint(0, 0, backColorFrom, trackBounds.width, 0, backColorTo);
        }

        g2.setPaint(gp);
        g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);

        if (trackHighlight == BasicScrollBarUI.DECREASE_HIGHLIGHT)
            this.paintDecreaseHighlight(g);

        if (trackHighlight == BasicScrollBarUI.INCREASE_HIGHLIGHT)
            this.paintIncreaseHighlight(g);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        g.translate(thumbBounds.x, thumbBounds.y);
        g.setColor(thumbColor);

        g.drawRoundRect(0, 0, thumbBounds.width - 1, thumbBounds.height - 1, 5, 5);

        Graphics2D g2 = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.addRenderingHints(rh);

        // 半透明
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opaque));

        // 设置填充颜色，这里设置了渐变，由下往上
        g2.setPaint(new GradientPaint(c.getWidth() / 2, 1, thumbColorFrom, c.getWidth() / 2, c.getHeight(), thumbColorTo));

        // 填充圆角矩形
        g2.fillRoundRect(0, 0, thumbBounds.width - 1, thumbBounds.height - 1, 5, 5);

    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        JButton button = new JButton();

        button.setBorderPainted(true);

        button.setContentAreaFilled(true);

        // 设置为null, 禁止上方按钮
        button.setBorder(null);

        return button;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        JButton button = new JButton();

        button.setBorderPainted(true);

        button.setContentAreaFilled(true);

        button.setFocusable(false);

        // 设置为null, 禁止上方按钮
        button.setBorder(null);

        return button;
    }
}