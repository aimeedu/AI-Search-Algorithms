import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class Puzzle{
    int dim, h_misplaced, h_manhattan;
    int[] goal; // {board1, board2, ....} the board is represent by a list of 9 integers.
    int[] start;
    Node goal_node;
    Node start_node;
    // PriorityQueue<Node> open, close;

    // constructor
    public Puzzle(int dim, int[] goal_arr, int[] start_arr){
        this.dim = dim;
        this.goal = goal_arr;
        this.start = start_arr;
        this.goal_node = new Node(0, goal_arr, 0, 0, 0); // goal node has id = 0
        this.start_node = new Node(1, start_arr, 0); // start id = 1
        this.start_node.h_manhattan(goal_arr, start_arr); // calculate heuristic for starting node.
        this.start_node.h_misplaced(goal_arr, start_arr); // calculate heuristic for starting node.
    }

    // in class methods
    public List<int[]> getNeighbors(int[] board){

        int white_tile_index = 0;
        for (int j = 0; j<board.length; j++){
            if (board[j] == 0) white_tile_index = j;
        }

        List<int[]> neighbors = new ArrayList<>();

        int[] right = new int[9];
        int[] done = new int[9];
        int[] left = new int[9];
        int[] up = new int[9];
        
        // output.write("white tile index: " + white_tile_index);
        // output.write("board size: " + board.size());
        for (int i = 0; i<board.length; i++){
            // right
            if (white_tile_index % 3 != 2){
                if(i == white_tile_index){
                    right[i] = board[i+1];
                }else if(i == white_tile_index+1){
                    right[i] = 0;
                    // output.write("right: " + i);
                }else{
                    right[i] = board[i];
                }  
            }
            // done
            if ((white_tile_index+3) < board.length){
                if(i == white_tile_index){
                    done[i] = board[i+3];
                }else if(i == white_tile_index+3){
                    done[i] = 0;
                    // output.write("done: "+ i);
                }else{
                    done[i] = board[i];
                }
            }
            // left
            if (white_tile_index % 3 != 0){
                
                if(i == (white_tile_index-1)){
                    left[i] = 0;
                    // output.write("left: "+ i);
                }else if(i == white_tile_index){
                    left[i] = board[white_tile_index-1];
                }else{
                    left[i] = board[i];
                }
            }
            // up
            if ((white_tile_index-3) >= 0){
              
                if(i == (white_tile_index-3)){
                    up[i] = 0;
                    // output.write("up: "+ i);
                }else if(i == white_tile_index){
                    up[i] = board[i-3];
                }else{
                    up[i] = board[i];
                }
            }
        }
        if(!allZero(right)) neighbors.add(right);
        if(!allZero(done)) neighbors.add(done);
        if(!allZero(left)) neighbors.add(left);
        if(!allZero(up)) neighbors.add(up);
        return neighbors;
    }

    public boolean allZero(int[] a){
        int count = 0;
        for (int i = 0; i<2; i++){
            if (a[i] == 0) count ++;
        }
        return count==2;
    }

    public int build_sequence(BufferedWriter output, Node cur, boolean w) throws IOException{
        if(w) output.write("\nOptimal Sequence : \n");
        List<Node> res = new ArrayList<>();
        while(cur != null){
            res.add(0,cur);
            cur = cur.parent;
        }
        int i = -1;
       
        for (Node n : res){
            i++;
            if(w) output.write("Step : "+ i + n+"\n");
        }
        if(w) output.write("The shortest path is " + i + " steps.\n");  
    

        return i;
    }

    public void solve_manhattan(BufferedWriter output) throws IOException{ 
        int n_id = 2;
        long startTime = System.currentTimeMillis();
        boolean end = false;
        TreeSet<Node> open, close;
        open = new TreeSet<>();
        close = new TreeSet<>();
        // PriorityQueue<Node> open = new PriorityQueue<>();
        // PriorityQueue<Node> close = new PriorityQueue<>();
        open.add(start_node);
        int g = 0;
        int total_expand_nodes = 0;
        while(!end){//!end
            // output.write("\n"+g+"------------------------");
            // first() Returns the first (lowest) element currently in this set. not remove yet.
            Node cur = open.first(); // get the best node in the open list. least f(n). 
            // Node cur = open.poll();
            open.remove(cur);
            close.add(cur); // expand
            total_expand_nodes++;

            // output.write("\nTop Board: " + cur.toString());
            // output.write("\n open list: "+ open);
            // output.write("close list: "+ close+"\n");

            if (cur.equals(goal_node)){
                end = true;
                long endTime = System.currentTimeMillis();
                // output.write("Found path with " + g + " Steps!");
                output.write("Total time to solve A * with Manhattan heuristic : " + (endTime-startTime) + " ms\n");
                output.write("Number of Nodes expanded : " + total_expand_nodes+"\n");
                build_sequence(output, cur, true);
                return; // we want to return sequence of Node.
            
            }else{
                g++; // take one more step to check neighbors.
                // build all neighbor's board after move the white tile, and check one by one.
                List<int[]> neighbors = getNeighbors(cur.board);
                for (int[] n : neighbors){
                    Node node = new Node(n);
                    node.set_g(g);
                    node.h_manhattan(goal, n);
                    node.setParent(cur);
                    if (cur.parent == null || !node.equals(cur.parent) ){
                        // print current neighbor.
                        // output.write("\nNeighbor: "+node.toString());                       
                        if (close.contains(node)){
                            Node temp = close.floor(node);
                            if (g<temp.g){
                                output.write("close contains node! current g is lower.");
                                close.remove(temp);
                                node.setId(n_id);
                                n_id++;
                                open.add(node);
                            }
                        }else if(open.contains(node)){
                            Node temp = open.floor(node);
                            if(g<temp.g){
                                output.write("open contains node! current g is lower.");
                                temp.g = g;
                                temp.parent = cur;
                            }

                        }else{
                            node.setId(n_id);
                            n_id++;
                            open.add(node);  
                        }
                        // output.write("\nNeighbor: "+node.toString());
                    }
                }
                // output.write("open list: "+ open);
                // output.write("close list: "+ close);
            }
            // g++;
        }
        return;
    }

    public void solve_misplaced(BufferedWriter output) throws IOException{
        int n_id = 2;
        TreeSet<Node> open, close;
        long startTime = System.currentTimeMillis();
        boolean end = false;
        open = new TreeSet<>();
        close = new TreeSet<>();
        open.add(start_node);
        int g = 0;
        int total_expand_nodes = 0;
        while(!end){//!end
            // output.write("\n"+g+"------------------------");
            // first() Returns the first (lowest) element currently in this set. not remove yet.
            Node cur = open.first(); // get the best node in the open list. least f(n). 
            open.remove(cur);
            close.add(cur); // expand
            total_expand_nodes++;

            // output.write("\nTop Board: " + cur.toString());
            // output.write("\n open list: "+ open);
            // output.write("close list: "+ close+"\n");

            if (cur.equals(goal_node)){
                end = true;
                long endTime = System.currentTimeMillis();
                // output.write("Found path with " + g + " Steps!");
                output.write("Total time to solve A * with Misplaced heuristic : " + (endTime-startTime) + " ms\n");
                output.write("Number of Nodes expanded : " + total_expand_nodes + "\n");
                build_sequence(output, cur, false);
                return; // we want to return sequence of Node.
            
            }else{
                g++; // take one more step to check neighbors.
                // build all neighbor's board after move the white tile, and check one by one.
                List<int[]> neighbors = getNeighbors(cur.board);
                for (int[] n : neighbors){
                    // output.write("--------------");
                    Node node = new Node(n);
                    node.set_g(g);
                    node.h_misplaced(goal, n);
                    node.setParent(cur);
                    if (cur.parent == null || !node.equals(cur.parent) ){
                        // print current neighbor.
                        // output.write("\nNeighbor: "+node.toString());
                        
                        if (close.contains(node)){
                            Node temp = close.floor(node);
                            // output.write("close temp.g = "+ temp.g + " do nothing!");
                            if (g<temp.g){
                                output.write("close contains node! current g is lower.");
                                close.remove(temp);
                                // node.g = g;
                                // node.parent = cur;
                                // node.h_manhattan(goal, n, g);
                                node.setId(n_id);
                                n_id++;
                                open.add(node);
                            }

                        }else if(open.contains(node)){
                            Node temp = open.floor(node);
                            // output.write("open temp.g = "+ temp.g + " do nothing!");
                            if(g<temp.g){
                                output.write("open contains node! current g is lower.");
                                temp.g = g;
                                temp.parent = cur;
                            }

                        }else{
                            // output.write("3. Not in both set!");
                            // node.g = g;
                            // node.parent = cur;
                            // node.h_manhattan(goal, n,g); // calculate h and f.
                            // output.write("h="+temp+ ", g="+node.g+", f=" +node.f +"\n");
                            node.setId(n_id);
                            n_id++;
                            open.add(node);     
                        }
                        // output.write("\nNeighbor: "+node.toString());
                    }
                }
                // output.write("open list: "+ open);
                // output.write("close list: "+ close);
            }
            // g++;
        }
        return;
    }

    boolean stop = false;
    public PriorityQueue<Integer> solve_DFBB_manhattan(BufferedWriter output, int limit) throws IOException{
        long startTime = System.currentTimeMillis();
        Stack<Node> open = new Stack<>();
        open.push(start_node);
        int L = limit;
        int id = 2; 
        int total_expand_nodes = 0;
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        while(!open.isEmpty()){//!open.isEmpty()
            // output.write(open);
            Node cur = open.pop();
            // output.write(cur);
            total_expand_nodes++;
            if (cur.equals(goal_node)){ // reach the goal state.
                int cost = build_sequence(output, cur, true);
                L = Math.min(L, cost);  
                stop = true;
                long endTime = System.currentTimeMillis();
                output.write("Time to find an optimal solution DFBnB : " + (endTime-startTime) + " ms\n");
                output.write("Nodes expanded : " + total_expand_nodes+" nodes.\n");
                output.write("End L (length of optimal path) : " + L + "\n");
                if(open.isEmpty()) return pq;
                else continue;
            }else{
                // g++;
                List<int[]> neighbors = getNeighbors(cur.board);
                PriorityQueue<Node> temp = new PriorityQueue<>((a,b)->b.h-a.h);
                for (int[] n : neighbors){
                    Node node = new Node(n);
                    // !open.contains(node) ||
                    if (cur.parent == null || !node.equals(cur.parent)){  
                        // node.g = g;
                        node.set_g(cur.g + 1);
                        node.h_manhattan(goal, n);
                        node.setParent(cur);
                        node.setId(id);
                        id++;
                        // if (cur.parent == null || !node.equals(cur.parent) ){
                            int cost = build_sequence(output, node, false);
                            if (cost < L){
                                temp.offer(node);
                            }
                            // L = Math.min(L, cost);
                        // }
                    }
                }
                while(!temp.isEmpty()){
                    Node t = temp.poll();
                    open.push(t);
                    pq.add(t.f);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        output.write("\nTime to finish DFBnB : " + (endTime-startTime) + " ms\n");
        output.write("Nodes expanded when finished : " + total_expand_nodes+" nodes.\n");
        // output.write("End L (length of optimal path) : " + L + "\n");
        ida_node += total_expand_nodes;
        return pq;
    }

    int ida_node = 0;
    public void solve_IDA_manhattan(BufferedWriter output) throws IOException{     
        long startTime = System.currentTimeMillis();
        int L = start_node.h_manhattan(goal, start);
        int iter = 0;
        while(!stop){
            iter++;
            output.write("\nN_iter = " +iter +", L = " + L +" ------------------------\n");
            PriorityQueue<Integer> pq  = solve_DFBB_manhattan(output, L);
            while(!pq.isEmpty()){
                int temp = pq.poll();
                if (temp > L){
                    L = temp;
                    break;
                }     
            }
        }
        long endTime = System.currentTimeMillis();
        output.write("\nNumber of Nodes expanded IDA* : " + ida_node +" nodes.\n");
        output.write("Time to solve IDA* with Manhattan heuristic : " + (endTime-startTime) + " ms\n");
    }
}