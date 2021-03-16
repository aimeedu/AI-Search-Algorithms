import java.util.*;

public class Main{

    
    public static void main(String[] args){
        
        int dim = 3; // dimension
        // use 0 to represent the white tile. use 1D array

        int[] goal = new int[]{1,2,3,8,0,4,7,6,5};
        int[] easy = new int[]{1,3,4,8,6,2,7,0,5};
        int[] medium = new int[]{2,8,1,0,4,3,7,6,5};
        int[] hard = new int[]{2,8,1,4,6,3,0,7,5};
        int[] worst = new int[]{5,6,7,4,0,8,3,2,1};
    
        ArrayList<int[]> a = new ArrayList<>();
        String[] name = new String[]{"Easy", "Medium", "Hard"};
        // a.add(easy);
        a.add(medium);
        // a.add(hard);

        for (int i=0; i<a.size(); i++){ 
            System.out.print(name[i]+" : \n");
            Puzzle p = new Puzzle(dim, goal, a.get(i));

            // // 1. misplaced heuristic A*
            // p.solve_misplaced();
            // System.out.print("----------------------------------------------------------------------\n");
            
            // // 2. Manhattan heuristic A* -> f(n) = g(n) current steps + h(n) heuristic value
            // p.solve_manhattan();
            // System.out.print("----------------------------------------------------------------------\n");

            // // 3. Iterative deepening A* with Manhattan heuristic.
            // p.solve_IDA_manhattan();
            // System.out.print("----------------------------------------------------------------------\n");

            // 4. Depth-first Branch and Bound with Manhattan heuristic. 
            p.solve_DFBB_manhattan();

            // Stack<Integer> s = new Stack<>();
            // s.push(1);
            // s.push(2);
            // s.push(3);
            // System.out.print(s.peek());
        }
    }
}
