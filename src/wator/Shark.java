package wator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matthew Pfeiffer
 */
public class Shark extends SimObject {
    int starve = 0;
    int lifetime = 0;
    
    public Shark(Position pos, Grid gr) {
        this.pos = pos;
        grid = gr;
    }
    
    public Fish findAdjacentFish() {
        List<Fish> fish = new ArrayList<Fish>();
        for (Position p : getAdjacentPositions()) {
            SimObject o = grid.get(p);
            if (o instanceof Fish) {
                fish.add((Fish) o);
            }
        }
        
        if (fish.isEmpty()) {
            return null;
        } else {
            int index = (int)(Math.random() * fish.size());
            return fish.get(index);
        }
    }
    
    public void die() {
        grid.remove(pos);
    }
    
    @Override
    public void act() {
        lifetime++;
        Position oldPos = pos;
//        Position newPos = getRandomAdjacentPosition();
//        if (grid.get(newPos) instanceof Fish) {
//            starve = 0;
//        } else {
//            starve += 1;
//            if (starve > starvelimit) {
//                die();
//                return;
//            }
//        }
        Fish adjacentFish = findAdjacentFish();
        if (adjacentFish == null) {
            moveToRandomEmptySquare();
            starve += 1;
            if (starve > grid.starveLimit) {
                die();
                return;
            }
        } else {
            moveTo(adjacentFish.pos);
            starve = 0;
        }
        if (lifetime % grid.sbreed == 0) {
            // spawn a new shark
            grid.set(new Shark(oldPos, grid), oldPos);
        }
    }
}
