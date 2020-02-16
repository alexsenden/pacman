package pacman;

/*
 * Pathfinding algorithm used by the ghosts
 * all created by me, influenced by Dijkstra and A* pathfinding algorithms
 */

public class Node {
	
	//2d array of locations a node has been created
    private static boolean[][] created;
    
    //max number of times a path to the goal can be looked for,
    //theoretically impossible to hit max due to board size
    private static int num = 1000;
    
    //target destination
    private static int goaly, goalx;
    
    //number of nodes created
    private static int numNodes;
    
    //collection of all created nodes
    private static Node[] nodes;
    
    //type of entity using this pathfinding
    //used to see what tiles can be walked on/used for pathfinding
    private static EntityType type = EntityType.GHOST;
    
    //index of parent node in Node[] nodes
    private int parentIndex;
    
    //location of node
    private int y, x;
    
    //heuristic of distance from node to goal
    //computer science principles topic!
    private int heurdist;
    
    //true if node is located at goal destination
    private static boolean found;
    
    //true if the node to this direction of the current node has been tested
    private boolean l, r, u, d;
    
    //true if there is still possible child nodes from this node
    private boolean space;
    
    //constructor
    public Node(int y, int x, int parentIndex) {
        this.y = y;
        this.x = x;
        this.parentIndex = parentIndex;
        
        space = true;
        
        l = r = u = d = false;
        
        heurdist = Math.abs(y - goaly) + Math.abs(x - goalx);
    }
    
    //method run by outside class to start the pathfinding
    public static String newPath(int starty, int startx, int targety, int targetx) {
        goaly = targety;
        goalx = targetx;
        
        created = new boolean[31][28];
        
        found = false;
        
        nodes = new Node[num];
        
        //i = y;
        /*
        for(int i = 0; i < 31; i++){
            for(int j = 0; j < 28; j++){
                if(type.walkablesContains(GameClass.board.getBoard()[i][j].getType())){
                    created[i][j] = true;
                }
            }
        }
        */
        nodes[0] = new Node(starty, startx, -1);
        
        numNodes = 1;
        
        int curIndex = 0;
        
        while(numNodes < num && !found) {
            /*
            if(!(curIndex == numNodes - 1)) {
                curIndex = getBestPotential();
            }
            */
            //System.out.println(curIndex);
            if(nodes[curIndex].x == goalx && nodes[curIndex].y == goaly) {
                found = true;
                break;
            }
            if(nodes[curIndex].x < 27 && type.walkablesContains(GameClass.board.getBoard()[nodes[curIndex].y][nodes[curIndex].x + 1].getType()) && !nodes[curIndex].r && !created[nodes[curIndex].y][nodes[curIndex].x + 1]) {
                nodes[numNodes] = new Node(nodes[curIndex].y, nodes[curIndex].x + 1, curIndex);
                nodes[curIndex].r = true;
                created[nodes[curIndex].y][nodes[curIndex].x + 1] = true;
                if(nodes[numNodes].heurdist < nodes[curIndex].heurdist){
                    curIndex = numNodes;
                }
                numNodes++;
            }
            else if(nodes[curIndex].x > 0 && type.walkablesContains(GameClass.board.getBoard()[nodes[curIndex].y][nodes[curIndex].x - 1].getType()) && !nodes[curIndex].l && !created[nodes[curIndex].y][nodes[curIndex].x - 1]) {
                nodes[numNodes] = new Node(nodes[curIndex].y, nodes[curIndex].x - 1, curIndex);
                nodes[curIndex].l = true;
                created[nodes[curIndex].y][nodes[curIndex].x - 1] = true;
                if(nodes[numNodes].heurdist < nodes[curIndex].heurdist){
                    curIndex = numNodes;
                }
                numNodes++;
            }
            else if(nodes[curIndex].y > 0 && type.walkablesContains(GameClass.board.getBoard()[nodes[curIndex].y - 1][nodes[curIndex].x].getType()) && !nodes[curIndex].u && !created[nodes[curIndex].y - 1][nodes[curIndex].x]) {
                nodes[numNodes] = new Node(nodes[curIndex].y - 1, nodes[curIndex].x, curIndex);
                nodes[curIndex].u = true;
                created[nodes[curIndex].y - 1][nodes[curIndex].x] = true;
                if(nodes[numNodes].heurdist < nodes[curIndex].heurdist){
                    curIndex = numNodes;
                }
                numNodes++;
            }
            else if(nodes[curIndex].y < 30 && type.walkablesContains(GameClass.board.getBoard()[nodes[curIndex].y + 1][nodes[curIndex].x].getType()) && !nodes[curIndex].d  && !created[nodes[curIndex].y + 1][nodes[curIndex].x]) {
                nodes[numNodes] = new Node(nodes[curIndex].y + 1, nodes[curIndex].x, curIndex);
                nodes[curIndex].d = true;
                created[nodes[curIndex].y + 1][nodes[curIndex].x] = true;
                if(nodes[numNodes].heurdist < nodes[curIndex].heurdist){
                    curIndex = numNodes;
                }
                numNodes++;
            }
            else {
                nodes[curIndex].space = false;
                curIndex = getBestPotential();
            }
            //System.out.println(curIndex + " " + numNodes + " " + nodes[curIndex].y + " " + nodes[curIndex].x);
        }
        
        if(!found) {
            curIndex = getBestPotential();
        }

        int[] coords = findFirstMove(nodes[curIndex]);
        //System.out.println("YE");
        if(coords[0] == -1) {
            return "last";
        }
        if(coords[0] > starty) {
            return "d";
        }
        else if(coords[0] < starty) {
            return "u";
        }
        else if(coords[1] > startx) {
            return "r";
        }
        else if(coords[1] < startx) {
            return "l";
        }
        else {
            return "r";
        }
    }
    
    //finds index of node in Node[] nodes with highest potential to be
    //a part of the most efficient path
    public static int getBestPotential() {
        int smallest = 9999999;
        int index = -1;
        for(int i = 0; i < nodes.length; i++) {
            if(nodes[i] == null) {
                break;
            }
            if(nodes[i].space && smallest > nodes[i].heurdist) {
                //System.out.println(nodes[i].space);
                smallest = nodes[i].heurdist;
                index = i;
            }
        }
        if(index != -1) {
            return index;
        }
        else {
            for(int i = 0; i < numNodes; i++) {
                //System.out.println("Node: " + i + ", X: " + nodes[i].x + ", Y: " + nodes[i].y + " " + nodes[i].l + " " + nodes[i].r + " " + nodes[i].u + " " + nodes[i].d + ", " + nodes[i].space);
            }
            return index;
        }
    }
    
    //once the path has been completed, recursively traces back through
    //the list of moves to find the direction of the first move
    public static int[] findFirstMove(Node node) {
        for(int i = 0; i < numNodes; i++) {
            //System.out.println("Node: " + i + ", X: " + nodes[i].x + ", Y: " + nodes[i].y + " " + nodes[i].l + " " + nodes[i].r + " " + nodes[i].u + " " + nodes[i].d + ", " + nodes[i].space);
        }
        //System.out.println(goalx + " " + goaly);
        if(isStarter(node)) {
            return new int[] {-1,-1};
        }
        if(isStarter(nodes[node.parentIndex])) {
            return new int[] {node.y, node.x};
        }
        return findFirstMove(nodes[node.parentIndex]);
    }
    
    //checks if node is the node at the starting point
    public static boolean isStarter(Node node) {
        if(node == nodes[0])
            return true;
        return false;
    }
}
