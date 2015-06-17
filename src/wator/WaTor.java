package wator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 *
 * @author Matthew Pfeiffer
 */
public class WaTor extends JPanel implements ActionListener {

    private Image src = null;
    private SimWorker simWorker;
    private JSlider width, height, startingAlgae, startingFish, startingSharks,
            maxAlgae, algaeRespawnTime, framesPerSecond, fishBreed, sharkBreed,
            sharkStarveLimit;
    private JButton startButton, stopButton, resumeButton;
    private JPanel controlPanel;
    private JFrame jf;

    public WaTor() {
        width = new JSlider(JSlider.HORIZONTAL, 100, 640, 200);
        height = new JSlider(JSlider.HORIZONTAL, 100, 480, 200);
        startingAlgae = new JSlider(JSlider.HORIZONTAL, 0, 10, 5);
        startingFish = new JSlider(JSlider.HORIZONTAL, 0, 2000, 1000);
        startingSharks = new JSlider(JSlider.HORIZONTAL, 0, 100, 25);
        maxAlgae = new JSlider(JSlider.HORIZONTAL, 0, 10, 5);
        algaeRespawnTime = new JSlider(JSlider.HORIZONTAL, 1, 10, 6);
        framesPerSecond = new JSlider(JSlider.HORIZONTAL, 0, 60, 10);
        fishBreed = new JSlider(JSlider.HORIZONTAL, 1, 20, 3);
        sharkBreed = new JSlider(JSlider.HORIZONTAL, 1, 40, 6);
        sharkStarveLimit = new JSlider(JSlider.HORIZONTAL, 0, 20, 4);
        JSlider[] sliders = {
            width, height, startingAlgae, startingFish, startingSharks,
            maxAlgae, algaeRespawnTime, framesPerSecond, fishBreed, sharkBreed,
            sharkStarveLimit
        };
        String[] sliderNames = {
            "Width", "Height", "Starting Algae", "Starting Fish",
            "Starting Sharks", "Max Algae", "Algae Respawn Time",
            "Frames per Second", "Fish Breed Time", "Shark Breed Time",
            "Shark Starve Limit"
        };
        JLabel[] sliderLabels = new JLabel[sliders.length];
        for (int i = 0; i < sliders.length; i++) {
            sliders[i].setPaintTicks(true);
            sliders[i].setPaintLabels(true);
            sliders[i].setMajorTickSpacing(sliders[i].getMaximum() / 7);
            //sliders[i].setExtent(300);
            sliders[i].setAlignmentX(LEFT_ALIGNMENT);
            sliderLabels[i] = new JLabel(sliderNames[i]);
            sliderLabels[i].setAlignmentX(RIGHT_ALIGNMENT);
        }
        Box buttonBox = Box.createHorizontalBox();
        startButton = new JButton("Start simulation");
        startButton.setActionCommand("start");
        startButton.addActionListener(this);
        stopButton = new JButton("Stop simulation");
        stopButton.setActionCommand("stop");
        stopButton.addActionListener(this);
        stopButton.setEnabled(false);
        resumeButton = new JButton("Resume Simulation");
        resumeButton.setActionCommand("resume");
        resumeButton.addActionListener(this);
        resumeButton.setEnabled(false);
        buttonBox.add(startButton);
        buttonBox.add(stopButton);
        buttonBox.add(resumeButton);
        controlPanel = new JPanel();
        BoxLayout cpLayout = new BoxLayout(controlPanel, BoxLayout.Y_AXIS);
        controlPanel.setLayout(cpLayout);
        controlPanel.add(buttonBox);
        for (int i = 0; i < sliders.length; i++) {
            Box b = Box.createHorizontalBox();
            b.add(sliderLabels[i]);
            b.add(sliders[i]);
            controlPanel.add(b);
        }
        controlPanel.setVisible(true);
        simWorker = new SimWorker(new Simulation(width.getValue(),
                height.getValue(), startingAlgae.getValue(),
                startingFish.getValue(), startingSharks.getValue(),
                maxAlgae.getValue(), algaeRespawnTime.getValue(),
                fishBreed.getValue(), sharkBreed.getValue(),
                sharkStarveLimit.getValue()));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("start".equals(e.getActionCommand())) {
            startSimulation();
        } else if ("stop".equals(e.getActionCommand())) {
            stopSimulation();
        } else if ("resume".equals(e.getActionCommand())) {
            resumeSimulation();
        }
    }
    
    public void startSimulation() {
        if(simWorker != null) {
            simWorker.cancel(true);
        }
        jf.setSize(width.getValue(), height.getValue());
        simWorker = new SimWorker(new Simulation(width.getValue(),
                height.getValue(), startingAlgae.getValue(),
                startingFish.getValue(), startingSharks.getValue(),
                maxAlgae.getValue(), algaeRespawnTime.getValue(),
                fishBreed.getValue(), sharkBreed.getValue(),
                sharkStarveLimit.getValue()));
        simWorker.fps = framesPerSecond.getValue();
        simWorker.execute();
        stopButton.setEnabled(true);
        startButton.setEnabled(false);
        resumeButton.setEnabled(false);
    }
    
    public void stopSimulation() {
        if (simWorker != null && !(simWorker.isCancelled())) {
            simWorker.cancel(true);
        }
        assert(simWorker.isCancelled());
        stopButton.setEnabled(false);
        startButton.setEnabled(true);
        resumeButton.setEnabled(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Rectangle rect = new Rectangle();
        rect = g.getClipBounds(rect);
        if (src != null) {
            g.drawImage(src, 0, 0, rect.width, rect.height, this);
        }
    }

    private void resumeSimulation() {
        if (simWorker != null && simWorker.isCancelled()) {
            simWorker = new SimWorker(simWorker.sim);
            simWorker.fps = framesPerSecond.getValue();
            simWorker.execute();
        }
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        resumeButton.setEnabled(false);
    }

    private class SimWorker extends SwingWorker<Void, Image> {

        public Simulation sim;
        public int fps;
        
        public SimWorker(Simulation sim) {
            this.sim = sim;
        }

        @Override
        protected void process(List<Image> chunks) {
            for (Image bufferedImage : chunks) {
                src = bufferedImage;
                repaint();
            }
        }

        @Override
        protected Void doInBackground() throws Exception {
            int frames = 0;
            int[] mem = new int[sim.width * sim.height];
            long start = System.currentTimeMillis();
            long last = start;
            SimObject obj;
            Color col = null;
            int refreshIntervalMS;
            if (fps == 0) {
                refreshIntervalMS = 0;
            } else {
                refreshIntervalMS = 1000 / fps;
            }
            long time;


            int numColors = sim.getGrid().maxAlgae + 1;
            Color[] algaeColors = new Color[numColors];
            for (int i = 0; i < numColors; i++) {
                algaeColors[i] = new Color(0, 255/ numColors * i, 0);
            }

            while (!isCancelled()) {//last < end){
                time = System.currentTimeMillis();
                sim.update();
                for (int y = 0; y < sim.height; y++) {
                    for (int x = 0; x < sim.width; x++) {
                        Position pos = new Position(x, y);
                        obj = sim.getGrid().get(new Position(x, y));
                        if (obj == null) {
                            col = algaeColors[sim.getGrid().getAlgae(pos)];
                        } else if (obj instanceof Fish) {
                            col = Color.blue;
                        } else if (obj instanceof Shark) {
                            col = Color.red;
                        }
                        mem[x + y * sim.width] = col.getRGB();
                    }
                }
                Image img = createImage(new MemoryImageSource(sim.width,
                        sim.height, mem, 0, sim.width));
                BufferedImage bi = new BufferedImage(
                        sim.width, sim.height,
                        BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = bi.createGraphics();
                g2.drawImage(img, 0, 0, null);
                g2.dispose();
                publish(bi);
                last = System.currentTimeMillis();
                if (fps != 0) {
                    try {
                        Thread.sleep(
                                Math.max(0, refreshIntervalMS - (last - time)));
                     } catch (InterruptedException e) {
                     }
                }

                frames++;
                System.out.println(sim.numAlgae + "," + sim.numFish + ","
                        + sim.numSharks);
            }
            System.err.println("Frames = " + frames
                    + ", fps = " + ((double) frames / (last - start) * 1000));
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame simFrame = new JFrame("WaTor Simulation");
                JFrame controlFrame = new JFrame("WaTor Control Panel");
                final WaTor w = new WaTor();
                w.jf = simFrame;
                simFrame.getContentPane().add(w, BorderLayout.CENTER);
                controlFrame.getContentPane().add(w.controlPanel);
                simFrame.setSize(200, 200);
                controlFrame.setSize(500, 550);
                simFrame.setVisible(true);
                controlFrame.setVisible(true);
                simFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                simFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        w.simWorker.cancel(true);
                    }
                });
            }
        });
    }
}