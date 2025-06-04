package frontend;

import java.awt.*;

public class WrappingLayout implements LayoutManager {
    private int hgap;
    private int vgap;
    private int alignment;

    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;

    public WrappingLayout(int hgap, int vgap) {
        this(hgap, vgap, LEFT); // Default alignment
    }

    public WrappingLayout(int hgap, int vgap, int alignment) {
        this.hgap = hgap;
        this.vgap = vgap;
        this.alignment = alignment;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return layoutSize(parent, true);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return layoutSize(parent, false);
    }

    private Dimension layoutSize(Container parent, boolean preferred) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int width = parent.getParent() != null ? parent.getParent().getWidth() : parent.getWidth();
            if (width <= 0)
                width = Integer.MAX_VALUE;

            int x = insets.left;
            int y = insets.top;
            int rowHeight = 0;
            int maxWidth = 0;

            for (Component comp : parent.getComponents()) {
                if (!comp.isVisible())
                    continue;
                Dimension d = preferred ? comp.getPreferredSize() : comp.getMinimumSize();

                if (x + d.width > width - insets.right) {
                    x = insets.left;
                    y += rowHeight + vgap;
                    rowHeight = 0;
                }

                x += d.width + hgap;
                rowHeight = Math.max(rowHeight, d.height);
                maxWidth = Math.max(maxWidth, x);
            }

            y += rowHeight + insets.bottom;
            return new Dimension(maxWidth, y);
        }
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int maxWidth = parent.getWidth() - insets.left - insets.right;

            int x = insets.left;
            int y = insets.top;
            int rowHeight = 0;

            java.util.List<Component> row = new java.util.ArrayList<>();
            int rowWidth = 0;

            for (Component comp : parent.getComponents()) {
                if (!comp.isVisible())
                    continue;
                Dimension d = comp.getPreferredSize();

                if (x + d.width > maxWidth + insets.left) {
                    // Align and place current row
                    placeRow(row, rowWidth, y, rowHeight, insets, maxWidth);
                    row.clear();
                    y += rowHeight + vgap;
                    x = insets.left;
                    rowHeight = 0;
                    rowWidth = 0;
                }

                row.add(comp);
                rowWidth += d.width + hgap;
                rowHeight = Math.max(rowHeight, d.height);
                x += d.width + hgap;
            }

            // Final row
            if (!row.isEmpty()) {
                placeRow(row, rowWidth, y, rowHeight, insets, maxWidth);
            }
        }
    }

    private void placeRow(java.util.List<Component> row, int rowWidth, int y, int rowHeight, Insets insets,
            int maxWidth) {
        int startX;
        rowWidth -= hgap; // remove last gap

        switch (alignment) {
            case CENTER:
                startX = insets.left + (maxWidth - rowWidth) / 2;
                break;
            case RIGHT:
                startX = insets.left + (maxWidth - rowWidth);
                break;
            case LEFT:
            default:
                startX = insets.left;
                break;
        }

        for (Component comp : row) {
            Dimension d = comp.getPreferredSize();
            comp.setBounds(startX, y, d.width, d.height);
            startX += d.width + hgap;
        }
    }
}
