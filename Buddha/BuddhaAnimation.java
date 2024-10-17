import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class BuddhaAnimation extends JPanel implements ActionListener {

    private Image buddhaImage;
    private Timer timer;
    private int glowAlpha = 0;
    private boolean glowIncreasing = true;
    private Random rand = new Random();
    private int particleCount = 50;
    private Particle[] particles;
    private Font iskoolaPotaFont;  // Font variable to store Iskoola Pota

    public BuddhaAnimation() {
        // Load the Buddha image from file
        ImageIcon icon = new ImageIcon("D:\\\\java practical\\\\buddha\\11.jpg");  // Change to the correct file path
        buddhaImage = icon.getImage();

        // Load the Iskoola Pota font
        try {
            iskoolaPotaFont = Font.createFont(Font.TRUETYPE_FONT, new File("D:\\java practical\\buddha\\Iskoola Pota Regular.ttf")).deriveFont(Font.BOLD, 36);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            // Fallback to default font if Iskoola Pota cannot be loaded
            iskoolaPotaFont = new Font("SansSerif", Font.BOLD, 36);
        }

        // Initialize particles for glowing effect
        particles = new Particle[particleCount];
        for (int i = 0; i < particleCount; i++) {
            particles[i] = new Particle();
        }

        // Timer to control the animation
        timer = new Timer(30, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the glowing particle effect
        drawParticleEffect(g);

        // Draw the Buddha image cropped as a round shape, centered
        drawRoundCroppedBuddhaImage(g);

        // Draw the text below the image
        drawTextBelowImage(g);
    }

    private void drawParticleEffect(Graphics g) {
        // Create glowing particles radiating from the Buddha image
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(new Color(0, 255, 0, glowAlpha));

        // Update particle positions and draw them
        for (Particle particle : particles) {
            g2d.fillOval(particle.x, particle.y, particle.size, particle.size);
            particle.update(getWidth(), getHeight());
        }

        // Adjust glow alpha for a pulsating effect
        if (glowIncreasing) {
            glowAlpha += 5;
            if (glowAlpha >= 200) {
                glowIncreasing = false;
            }
        } else {
            glowAlpha -= 5;
            if (glowAlpha <= 50) {
                glowIncreasing = true;
            }
        }

        g2d.dispose();
    }

    private void drawRoundCroppedBuddhaImage(Graphics g) {
        // Set new image size (smaller)
        int scaledSize = 200;  // New size for the image (200x200 pixels)

        // Find the center of the panel and place the image in the middle
        int x = (getWidth() - scaledSize) / 2;
        int y = (getHeight() - scaledSize) / 2 - 50;  // Adjust to leave space for text below

        // Create a circular clipping area
        Graphics2D g2d = (Graphics2D) g.create();
        Shape circle = new Ellipse2D.Double(x, y, scaledSize, scaledSize);
        g2d.setClip(circle);  // Apply the circular clip

        // Draw the scaled-down image
        g2d.drawImage(buddhaImage, x, y, scaledSize, scaledSize, this);

        g2d.dispose();
    }

    private void drawTextBelowImage(Graphics g) {
        // Set the Iskoola Pota font for Sinhala text
        g.setFont(iskoolaPotaFont);
        g.setColor(new Color(0, 255, 0));  // Glowing green text

        // Text to be displayed
        String text = "නමෝ බුද්ධාය";

        // Get the width of the text to center it below the image
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);

        // Position the text below the image
        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() + 200) / 2 + 60;  // Position the text 60px below the scaled-down image

        // Draw the text
        g.drawString(text, x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Repaint the panel to create the animation
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Buddha Animation with Text");
        BuddhaAnimation panel = new BuddhaAnimation();
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Class representing a single particle for the animation effect
    class Particle {
        int x, y, size;
        int dx, dy;

        public Particle() {
            // Random initial position
            x = rand.nextInt(800);
            y = rand.nextInt(600);
            size = rand.nextInt(10) + 5;  // Random size between 5 and 15

            // Random direction and speed
            dx = rand.nextInt(3) - 1;  // Speed in x (-1, 0, or 1)
            dy = rand.nextInt(3) - 1;  // Speed in y (-1, 0, or 1)
        }

        public void update(int width, int height) {
            // Move particle
            x += dx;
            y += dy;

            // Bounce off walls
            if (x < 0 || x > width - size) dx = -dx;
            if (y < 0 || y > height - size) dy = -dy;
        }
    }
}
