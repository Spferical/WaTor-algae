package wator;

/**
 *
 * @author Matthew Pfeiffer
 */
public class Position {
    int x, y;
    
    public Position(int xPos, int yPos) {
        x = xPos;
        y = yPos;
    }
    
    @Override
    public String toString() {
        return "Position(" + x + ", " + y + ")";
    }
}
