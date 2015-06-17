package wator;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JPanel;

/**
 *
 * @author Matthew Pfeiffer
 */
public class ChartPanel extends JPanel {
    private Canvas chartCanvas;
    private int[] values;
    
    public ChartPanel(int[] values) {
        this.values = values;
        chartCanvas = new Canvas();
    }
    
    public static int getMaximum(int[] data) {
        int max = data[0];
        for (int i : data) {
            if (i > max) max = i;
        }
        return max;
    }
    
    public static int getMinimum(int[] data) {
        int min = data[0];
        for (int i : data) {
            if (i < min) min = i;
        }
        return min;
    }
    
    @Override
    public void paint(Graphics g) {
        int min = getMinimum(values);
        int max = getMaximum(values);
        for (int i = 0; i < values.length; i++) {
            Rectangle s = g.getClipBounds();
            int x = s.width / values.length * i;
            int y = s.height / max * values[i];
            g.drawLine(i, i, i, i);
        }
    }
    
    
}
