package wator;

import java.util.ArrayList;

/**
 *
 * @author Matthew Pfeiffer
 */
public class Fish extends SimObject {
    int lifetime = 0;
    
    public Fish(Position pos, Grid gr) {
        this.pos = pos;
        grid = gr;
    }
    
    @Override
    public void act() {
        lifetime++;
        Position oldPos = pos;
        Position algae = getRandomNearbyAlgae();
        if (algae != null) {
            moveTo(algae);
        } else {
            moveToRandomEmptySquare();
        }
        if (grid.getAlgae(pos) == 0) {
            grid.set(null, pos);
        } else {
            grid.eatAlgae(pos);
        }
        if (lifetime % grid.fbreed == 0) {
            // spawn a new fish
            grid.set(new Fish(oldPos, grid), oldPos);
        }
    }
    
    public ArrayList<Position>getNearbyAlgae() {
        ArrayList<Position> positions = new ArrayList<Position>();
        for (Position position : getAdjacentPositions()) {
            if (grid.getAlgae(position) != 0 && grid.get(position) == null) {
                positions.add(position);
            }
        }
        
        return positions;
    }
    
    public Position getRandomNearbyAlgae() {
        ArrayList<Position> positions = getNearbyAlgae();
        if (positions.isEmpty()) return null;
        int index = (int) (Math.random() * positions.size());
        return positions.get(index);
    }
    
    @Override
    public String toString() {
        return "Fish(" + pos + ", " + lifetime + ")";
    }
    
}
