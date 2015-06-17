package wator;

/**
 *
 * @author Matthew Pfeiffer
 */
public class Simulation {
    
    private Grid grid;
    public int width;
    public int height;
    public int startingAlgae;
    public int startingFish;
    public int startingSharks;
    public int maxAlgae;
    public int algaeRespawnTime;
    public int numFish, numAlgae, numSharks;
    
    public Grid getGrid() {
        return grid;
    }
    
    public Simulation(int width, int height, int startingAlgae, int startingFish,
            int startingSharks, int maxAlgae, int algaeRespawnTime,
            int fishBreed, int sharkBreed, int starveLimit) {
        grid = new Grid(width, height, maxAlgae, algaeRespawnTime,
                fishBreed, sharkBreed, starveLimit);
        numFish = numAlgae = numSharks = 0;
        this.width = width;
        this.height = height;
        
        // populate fish
        for (int i = 0; i < startingFish; i++) {
            Position pos = new Position(
                (int) (Math.random() * grid.getWidth()),
                (int) (Math.random() * grid.getHeight()));
            grid.set(new Fish(pos, grid), pos);
            numFish++;
        }
        // populate sharks
        for (int i = 0; i < startingSharks; i++) {
            Position pos = new Position(
                (int) (Math.random() * grid.getWidth()),
                (int) (Math.random() * grid.getHeight()));
            grid.set(new Shark(pos, grid), pos);
            numSharks++;
        }
        // populate algae
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid.algae[x][y] = startingAlgae;
                numAlgae += startingAlgae;
            }
        }
    }
    
    public void update() {
        grid.updateAll();
        numAlgae = numSharks = numFish = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                numAlgae += grid.algae[x][y];
                if (grid.grid[x][y] instanceof Shark) {
                    numSharks++;
                } else if (grid.grid[x][y] instanceof Fish) {
                    numFish++;
                }
            }
        }
    }
    
    
}
