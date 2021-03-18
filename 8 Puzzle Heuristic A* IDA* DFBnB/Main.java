import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main{
    public static void main(String[] args) throws IOException{

        try(BufferedWriter output = new BufferedWriter(new FileWriter(args[0]));){
                // output.write(Integer.toString(arr[i][j]) + " "); 
            int dim = 3; // dimension
            // use 0 to represent the white tile. use 1D array

            int[] goal = new int[]{1,2,3,8,0,4,7,6,5};
            int[] easy = new int[]{1,3,4,8,6,2,7,0,5};
            int[] medium = new int[]{2,8,1,0,4,3,7,6,5};
            int[] hard = new int[]{2,8,1,4,6,3,0,7,5};
            int[] worst = new int[]{5,6,7,4,0,8,3,2,1};
        
            ArrayList<int[]> a = new ArrayList<>();
            String[] name = new String[]{"Easy", "Medium", "Hard", "Worst"};
            a.add(easy);
            a.add(medium);
            a.add(hard);
            // a.add(worst);

            for (int i=0; i<a.size(); i++){ 
                output.write(name[i]+" : \n");
                Puzzle p = new Puzzle(dim, goal, a.get(i));
                
                // 1. Manhattan heuristic A* -> f(n) = g(n) current steps + h(n) heuristic value
                output.write("\n1. A* Manhattan ----------------------------------------------------------------------\n");
                p.solve_manhattan(output);
                
                // 2. misplaced heuristic A* 
                output.write("\n2. A* Misplaced ----------------------------------------------------------------------\n");
                p.solve_misplaced(output);
  
                // 3. Iterative deepening A* with Manhattan heuristic.
                output.write("\n3. IDA* Manhattan ----------------------------------------------------------------------\n");
                p.solve_IDA_manhattan(output);

                // 4. Depth-first Branch and Bound with Manhattan heuristic. 
                // pass the limit of L as parameter.
                output.write("\n4. Depth-first Branch and Bound --------------------------------------------------------\n");
                p.solve_DFBB_manhattan(output, 35);
            }
        }
        
        
    }
}
