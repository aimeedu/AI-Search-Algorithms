import java.util.*;

public class Node implements Comparable<Node>{
    int id;
    int g, h;
    int f;
    int[] goal;
    int[] board;
    Node parent;

    // constructors
    public Node(int id, int[] board,int g, int h, int f){ // for goal board.
        this.id = id;
        this.board = board; 
        this.g = g;
        this.h = h;
        this.f = f;
    }

    public Node(int id, int[] board, int g){ // start board
        this.id = id;
        this.g = g;
        this.board = board; 
        this.parent = null;
    }

    public Node(int[] board){
        this.board = board; 
    }

    // methods

    public void setId(int id){
        this.id = id;
    } 
    public void setParent(Node p){
        this.parent = p;
    } 

    public void set_g(int g){
        this.g = g;
    }

    // calculate Misplaced heuristics
    public void h_misplaced(int[] goal, int[] board){
        int h = 0;
        for(int i=0; i<board.length; i++){
            int tile = board[i];
            if(tile != 0 && tile != goal[i]){
                h++;
            }
        }
        // this.g = g;
        this.h = h;
        this.f = h + g;
        return;
    } 
    
    public int findIndex(int[] arr, int tile){
        int pos = 0;
        for (int i = 0; i<arr.length; i++){
            if (arr[i] == tile) pos = i;
        }
        return pos;
    }

    // calculate Manhattan heuristics
    public int h_manhattan(int[] goal, int[] board) {
        int dim =3;
        int h = 0;
        for(int i = 0; i<goal.length; i++){
            int tile = board[i];
            if(tile != 0){    
                int goal_index = findIndex(goal, tile);
                int step = Math.abs(i%dim - goal_index%dim) +  Math.abs(i/dim - goal_index/dim);
                // System.out.println("tile is: " + tile + ", Step is: " + step);
                h += (step); 
            }
        }
        // this.g = g;
        this.h = h;
        this.f = h + g;
        // System.out.println("g = " + this.g +", h="+this.h+ ", f = " + this.f);
        return this.h;
        // return this.h;
    }

    @Override 
    public String toString(){
        String temp = "\n";
        // String temp = "\nBoard id: "+ this.id  + ", g = "+this.g+ ", h = "+this.h+ ", f = "+this.f+"\n";
        for (int i=0; i<this.board.length; i++){
            temp += this.board[i];
            if(i%3==2){
                temp += "\n";
            }else{
                temp += " ";
            }
        }
        return temp;
    }

    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (!(obj instanceof Node)) return false;
        Node node = (Node) obj;
        if(Arrays.equals(this.board, node.board)){
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Node n) {
        // return this.f-n.f;
        if (this.equals(n)) {
            return 0;
        }
        else{
            if (this.f != n.f) {
                return this.f-n.f;
            }else if (this.h != n.h){
                return this.h-n.h;
            }else if (this.g != n.g){
                return this.g-n.g;
            }
            else return n.id-this.id;
        }

    }
}
