package wator;

/**
 *
 * @author Matthew Pfeiffer
 */
public class Grid {
    SimObject[][] grid;
    private int width, height;
    int[][] algae;
    int turncount;
    int maxAlgae;
    int algaeRespawnTime;
    int sbreed, fbreed;
    int starveLimit;
    
    
    public Grid(int width, int height, int maxAlgae, int algaeRespawnTime,
            int fbreed, int sbreed, int starveLimit) {
        this.width = width;
        this.height = height;
        this.maxAlgae = maxAlgae;
        this.algaeRespawnTime = algaeRespawnTime;
        this.sbreed = sbreed;
        this.fbreed = fbreed;
        this.starveLimit = starveLimit;
        grid = new SimObject[width][height];
        algae = new int[width][height];
        turncount = 0;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    
    public SimObject get(Position pos) {
        return grid[pos.x % width][pos.y % height];
    }
    
    public int getAlgae(Position pos) {
        return algae[pos.x % width][pos.y % height];
    }
    
    public void eatAlgae(Position pos) {
        algae[pos.x % width][pos.y % height] -= 1;
    }
    
    public void remove(Position pos) {
        set(null, pos);
    }
    
    public void set(SimObject x, Position pos) {
        grid[pos.x % width][pos.y % height] = x;
    }
   
    public void updateAll() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++){ 
                SimObject obj = grid[x][y];
                if (obj instanceof Shark) obj.act();
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++){ 
                SimObject obj = grid[x][y];
                if (obj instanceof Fish) obj.act();
            }
        }
        if (turncount % algaeRespawnTime == 0) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++){ 
                    algae[x][y] = Math.min(algae[x][y] + 1, maxAlgae);
                }
            }
        }
        turncount++;
    }
    
}
