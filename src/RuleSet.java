public class RuleSet {
    /*
     * 1. Any live cell with fewer than two live neighbours dies, as if by underpopulation.
     * 2. Any live cell with two or three live neighbours lives on to the next generation.
     * 3. Any live cell with more than three live neighbours dies, as if by overpopulation.
     * 4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
     */
    public int ruleSet(int numNeighbours, int state){
         if (state == 0) {
             // Birth
             if (numNeighbours == 3) {
                 return 1;
             }
         } else {
             // Death due to underpopulation.
             if (numNeighbours < 2) {
                 return 0;
             }
             // Survival due to steady numbers.
             else if (numNeighbours == 3 || numNeighbours == 2) {
                 return 1;
             }
         }
         // Overpopulation
         return 0;
    }
}
