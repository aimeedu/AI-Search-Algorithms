import java.util.*;

public class Puzzle{
    // calculate Misplaced heuristics
    // h_misplaced(goal, hard);
    // calculate Manhattan heuristics
    // h_manhattan(dim, goal, hard);
    int dim, h_misplaced, h_manhattan;
    int[] goal; // {board1, board2, ....} the board is represent by a list of 9 integers.
    int[] start;
    Node goal_node;
    Node start_node;
    // Map<List<Integer>, Node> open_map, close_map; // <board, f_value>
    // PriorityQueue<Node> open, close;
    // TreeSet<Node> open, close;
    int n_id = 2; // start to assign from 2nd node when add to open list.

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
        
        // System.out.println("white tile index: " + white_tile_index);
        // System.out.println("board size: " + board.size());
        for (int i = 0; i<board.length; i++){
            // right
            if (white_tile_index % 3 != 2){
                if(i == white_tile_index){
                    right[i] = board[i+1];
                }else if(i == white_tile_index+1){
                    right[i] = 0;
                    // System.out.println("right: " + i);
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
                    // System.out.println("done: "+ i);
                }else{
                    done[i] = board[i];
                }
            }
            // left
            if (white_tile_index % 3 != 0){
                
                if(i == (white_tile_index-1)){
                    left[i] = 0;
                    // System.out.println("left: "+ i);
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
                    // System.out.println("up: "+ i);
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

    public int build_sequence(Node cur){
        List<Node> res = new ArrayList<>();
        while(cur != null){
            res.add(0,cur);
            cur = cur.parent;
        }
        int i = -1;
        for (Node n : res){
            i++;
            // System.out.print("Step : "+ i + n+"\n");
            
        }
        // System.out.println("The shortest path is " + i + " steps.");
        return i;
    }

    public void solve_manhattan(){ 
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
            // System.out.println("\n"+g+"------------------------");
            // first() Returns the first (lowest) element currently in this set. not remove yet.
            Node cur = open.first(); // get the best node in the open list. least f(n). 
            // Node cur = open.poll();
            open.remove(cur);
            close.add(cur); // expand
            total_expand_nodes++;

            // System.out.println("\nTop Board: " + cur.toString());
            // System.out.println("\n open list: "+ open);
            // System.out.println("close list: "+ close+"\n");

            if (cur.equals(goal_node)){
                end = true;
                long endTime = System.currentTimeMillis();
                // System.out.println("Found path with " + g + " Steps!");
                System.out.printf("Total time to solve A * with Manhattan heuristic : " + (endTime-startTime) + " ms\n");
                System.out.println("Number of Nodes expanded : " + total_expand_nodes+"\n");
                build_sequence(cur);
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
                        // System.out.println("\nNeighbor: "+node.toString());                       
                        if (close.contains(node)){
                            Node temp = close.floor(node);
                            if (g<temp.g){
                                System.out.println("close contains node! current g is lower.");
                                close.remove(temp);
                                node.setId(n_id);
                                n_id++;
                                open.add(node);
                            }
                        }else if(open.contains(node)){
                            Node temp = open.floor(node);
                            if(g<temp.g){
                                System.out.println("open contains node! current g is lower.");
                                temp.g = g;
                                temp.parent = cur;
                            }

                        }else{
                            node.setId(n_id);
                            n_id++;
                            open.add(node);  
                        }
                        // System.out.println("\nNeighbor: "+node.toString());
                    }
                }
                // System.out.println("open list: "+ open);
                // System.out.println("close list: "+ close);
            }
            // g++;
        }
        return;
    }

    public void solve_misplaced(){
        TreeSet<Node> open, close;
        long startTime = System.currentTimeMillis();
        boolean end = false;
        open = new TreeSet<>();
        close = new TreeSet<>();
        open.add(start_node);
        int g = 0;
        int total_expand_nodes = 0;
        while(!end){//!end
            // System.out.println("\n"+g+"------------------------");
            // first() Returns the first (lowest) element currently in this set. not remove yet.
            Node cur = open.first(); // get the best node in the open list. least f(n). 
            open.remove(cur);
            close.add(cur); // expand
            total_expand_nodes++;

            // System.out.println("\nTop Board: " + cur.toString());
            // System.out.println("\n open list: "+ open);
            // System.out.println("close list: "+ close+"\n");

            if (cur.equals(goal_node)){
                end = true;
                long endTime = System.currentTimeMillis();
                // System.out.println("Found path with " + g + " Steps!");
                System.out.printf("Total time to solve A * with Misplaced heuristic : " + (endTime-startTime) + " ms\n");
                System.out.println("Number of Nodes expanded : " + total_expand_nodes + "\n");
                build_sequence(cur);
                return; // we want to return sequence of Node.
            
            }else{
                g++; // take one more step to check neighbors.
                // build all neighbor's board after move the white tile, and check one by one.
                List<int[]> neighbors = getNeighbors(cur.board);
                for (int[] n : neighbors){
                    // System.out.println("--------------");
                    Node node = new Node(n);
                    node.set_g(g);
                    node.h_misplaced(goal, n);
                    node.setParent(cur);
                    if (cur.parent == null || !node.equals(cur.parent) ){
                        // print current neighbor.
                        // System.out.println("\nNeighbor: "+node.toString());
                        
                        if (close.contains(node)){
                            Node temp = close.floor(node);
                            // System.out.println("close temp.g = "+ temp.g + " do nothing!");
                            if (g<temp.g){
                                System.out.println("close contains node! current g is lower.");
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
                            // System.out.println("open temp.g = "+ temp.g + " do nothing!");
                            if(g<temp.g){
                                System.out.println("open contains node! current g is lower.");
                                temp.g = g;
                                temp.parent = cur;
                            }

                        }else{
                            // System.out.println("3. Not in both set!");
                            // node.g = g;
                            // node.parent = cur;
                            // node.h_manhattan(goal, n,g); // calculate h and f.
                            // System.out.print("h="+temp+ ", g="+node.g+", f=" +node.f +"\n");
                            node.setId(n_id);
                            n_id++;
                            open.add(node);
                            
                        }
                        // System.out.println("\nNeighbor: "+node.toString());
                    }

                }
                // System.out.println("open list: "+ open);
                // System.out.println("close list: "+ close);
            }
            // g++;
        }
        return;
    }

    boolean stop = false;
    public PriorityQueue<Integer> solve_DFBB_manhattan(int limit){
        long startTime = System.currentTimeMillis();
        Stack<Node> open = new Stack<>();
        open.push(start_node);
        int L = limit;
        int g = 0; 
        int total_expand_nodes = 0;
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        while(!open.isEmpty()){//!open.isEmpty()
            // System.out.println(open);
            Node cur = open.pop();
            // System.out.println(cur);
            total_expand_nodes++;
            if (cur.equals(goal_node)){ // reach the goal state.
                int cost = build_sequence(cur);
                L = Math.min(L, cost);  
                stop = true;
                long endTime = System.currentTimeMillis();
                System.out.printf("Time to solve Depth-first Branch and Bound with Manhattan heuristic : " + (endTime-startTime) + " ms\n");
                System.out.println("Number of Nodes expanded : " + total_expand_nodes+" nodes.");
                System.out.print("End L (length of optimal path) : " + L + "\n");
                return pq;
            }else{
                g++;
                List<int[]> neighbors = getNeighbors(cur.board);
                PriorityQueue<Node> temp = new PriorityQueue<>((a,b)->b.h-a.h);
                for (int[] n : neighbors){
                    Node node = new Node(n);
                    // !open.contains(node) ||
                    if (cur.parent == null || !node.equals(cur.parent)){  
                        node.g = g;
                        // node.set_g(cur.g + 1);
                        node.h_manhattan(goal, n);
                        node.setParent(cur);
                        // if (cur.parent == null || !node.equals(cur.parent) ){
                            int cost = build_sequence(node);
                            if (cost < L){
                                temp.offer(node);
                            }
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
        System.out.printf("Time to solve Depth-first Branch and Bound with Manhattan heuristic : " + (endTime-startTime) + " ms\n");
        System.out.println("Number of Nodes expanded : " + total_expand_nodes+" nodes.\n");
        System.out.print("End L (length of optimal path) : " + L + "\n");
        return pq;
    }

    public void solve_IDA_manhattan(){
        long startTime = System.currentTimeMillis();
        int L = start_node.h_manhattan(goal, start);
        int iter = 0;
        while(!stop){
            iter++;
            System.out.print("\nN_iter = " +iter +", L = " + L +"-------------------\n");
            PriorityQueue<Integer> pq  = solve_DFBB_manhattan(L);

            while(!pq.isEmpty()){
                int temp = pq.poll();
                if (temp > L){
                    L = temp;
                    break;
                }     
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.printf("Time to solve IDA* with Manhattan heuristic : " + (endTime-startTime) + " ms\n");
    }
}