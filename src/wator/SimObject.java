package wator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matthew Pfeiffer
 */
public abstract class SimObject {
    Position pos;
    Grid grid;
    
    public abstract void act();
    
    public void moveTo(Position pos) {
        grid.remove(this.pos);
        this.pos = pos;
        grid.set(this, pos);
    }
    
    public List<Position> getAdjacentPositions() {
        List<Position> positions = new ArrayList<Position>();
        int[] displacements = {-1, 0, 1};
        for (int dx : displacements) {
            for (int dy : displacements) {
                if (!(dx == 0 && dy == 0)) {
                    int px = (pos.x + dx) % grid.getWidth();
                    int py = (pos.y + dy) % grid.getHeight();
                    if (px < 0) px += grid.getWidth();
                    if (py < 0) py += grid.getWidth();
                    Position p = new Position(px, py);
                    positions.add(p);
                }
            }
        }
        return positions;
    }
    
    public boolean moveToRandomEmptySquare() {
        List<Position> possibleMovements = new ArrayList<Position>();
        for (Position possPos : getAdjacentPositions()) {
            if (grid.get(possPos) == null) {
                possibleMovements.add(possPos);
            }
        }
        if (possibleMovements.isEmpty()) {
            return false;
        } else {
            Position newPos = possibleMovements.get((int) (Math.random() * possibleMovements.size()));
            moveTo(newPos);
            return true;
        }
    }
    
    public void moveRandomly() {
        moveTo(getRandomAdjacentPosition());
    }
    
    public Position getRandomAdjacentPosition() {
        List<Position> possibleMovements = getAdjacentPositions();
        Position newPos = possibleMovements.get((int) (Math.random() * possibleMovements.size()));
        return newPos;
    }
}
