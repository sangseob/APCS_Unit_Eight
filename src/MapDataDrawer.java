import java.util.*;
import java.io.*;
import java.awt.*;

public class MapDataDrawer
{

    private int[][] grid;

    public MapDataDrawer(String filename, int rows, int cols) throws FileNotFoundException{
        // initialize grid
        //read the data from the file into the grid
        Scanner scan = new Scanner(new File(filename));
        grid = new int[rows][cols];

        for (int r = 0; r < rows; r++){
            for (int c = 0; c < cols; c++){
                grid[r][c] = scan.nextInt();
            }

        }


    }

    /**
     * @return the min value in the entire grid
     */
    public int findMinValue(){
        int min = grid[0][0];
        for(int n = 0; n < grid.length; n++){
            for(int k = 0; k < grid[n].length; k++){
                if(grid[n][k] < min){
                    min = grid[n][k];
                }
            }
        }
        return min;
    }
    /**
     * @return the max value in the entire grid
     */
    public int findMaxValue(){
        int max = grid[0][0];
        for(int i = 0; i < grid.length; i++){
            for(int k = 0; k < grid[i].length; k++){
                if(grid[i][k] > max){
                    max = grid[i][k];
                }
            }
        }
        return max;
    }

    /**
     * @param col the column of the grid to check
     * @return the index of the row with the lowest value in the given col for the grid
     */
    public  int indexOfMinInCol(int col){
        //min and minIndex
        int min = grid[0][col];
        int minIndex = 0;

        for(int i = 0; i < grid.length; i++){
            if(grid[i][col] < min){
                min = grid[i][col];
                minIndex = i;
            }
        }
        return minIndex;
    }

    /**
     * Draws the grid using the given Graphics object.
     * Colors should be grayscale values 0-255, scaled based on min/max values in grid
     */
    public void drawMap(Graphics g){

        for(int r = 0; r < grid.length; r++){

            for(int c = 0; c < grid[r].length; c++){

                //calc
                int x = grid[r][c];
                double y = x - 1099;
                int max = 4334-1099;
                int a = (int) ((y/max) * 255);

                //color
                g.setColor(new Color(c, c, c));
                g.fillRect(c, r, 1, 1);

            }
        }

    }

    /**
     * Find a path from West-to-East starting at given row.
     * Choose a foward step out of 3 possible forward locations, using greedy method described in assignment.
     * @return the total change in elevation traveled from West-to-East
     */
    public int drawLowestElevPath(Graphics g, int row){
        int a = row;
        int elevation = 0;
        g.fillRect(0, row, 1, 1);
        for (int c = 1; c < grid[0].length; c++) {
            int current = grid[a][c - 1];

            if (a == 0) {
                int forward = grid[a][c];
                int down = grid[a + 1][c];
                if (Math.abs(current - forward) < Math.abs(current - down)) {
                    g.fillRect(c, a, 1, 1);
                    elevation+= Math.abs(current - forward);
                } else if (Math.abs(current - down) < Math.abs(current - forward)) {
                    a+=1;
                    g.fillRect(c, a, 1, 1);
                    elevation+= Math.abs(current - down);
                } else {
                    g.fillRect(c, a, 1, 1);
                    elevation+= Math.abs(current - forward);
                }
            } else if (a < grid.length-1) {
                int forward = grid[a][c];
                int up = grid[a - 1][c];
                int down = grid[a + 1][c];
                if (Math.abs(current - forward) < Math.abs(current - up) && Math.abs(current - forward) < Math.abs(current - down)) {
                    g.fillRect(c, a, 1, 1);
                    elevation+= Math.abs(current - forward);
                } else if (Math.abs(current - up) < Math.abs(current - forward) && Math.abs(current - up) < Math.abs(current - down)) {
                    a-=1;
                    g.fillRect(c, a, 1, 1);
                    elevation+= Math.abs(current - up);
                } else if (Math.abs(current - down) < Math.abs(current - up) && Math.abs(current - down) < Math.abs(current - forward)) {
                    a+=1;
                    g.fillRect(c, a, 1, 1);
                    elevation+= Math.abs(current - down);
                } else {
                    if (Math.abs(current - forward) == Math.abs(current - up) || Math.abs(current - forward) == Math.abs(current - down)) {
                        g.fillRect(c, a, 1, 1);
                        elevation+= Math.abs(current - forward);
                    }
                    else {
                        int flip = (int)(Math.random() * 2) + 1;
                        if (flip == 1) {
                            a+=1;
                            g.fillRect(c, a, 1, 1);
                            elevation+= Math.abs(current - down);
                        }
                        else {
                            a-=1;
                            g.fillRect(c, a, 1, 1);
                            elevation+= Math.abs(current - up);
                        }
                    }
                }
            } else {
                int forward = grid[a][c];
                int up = grid[a - 1][c];
                if (Math.abs(current - forward) < Math.abs(current - up)) {
                    g.fillRect(c, a, 1, 1);
                    elevation+= Math.abs(current - forward);
                } else if (Math.abs(current - up) < Math.abs(current - forward)) {
                    a-=1;
                    g.fillRect(c, a, 1, 1);
                    elevation+= Math.abs(current - up);
                } else {
                    g.fillRect(c, a, 1, 1);
                    elevation+= Math.abs(current - forward);
                }
            }
        }
        return elevation;
    }

    /**
     * @return the index of the starting row for the lowest-elevation-change path in the entire grid.
     */
    public int indexOfLowestElevPath(Graphics g){
        int x = drawLowestElevPath(g, 0);
        int index = 0;
        int temp = 0;
        for (int r = 1; r < grid.length-1; r++) {
            if (drawLowestElevPath(g, r) < drawLowestElevPath(g, (r+1))) {
                temp = drawLowestElevPath(g, r);
                if(temp < x) {
                    x = temp;
                    index = r;
                }
            } else if (drawLowestElevPath(g, r) > drawLowestElevPath(g, (r+1))) {
                temp = drawLowestElevPath(g, (r + 1));
                if(temp < x) {
                    x = temp;
                    index = r;
                }
            }
        }
        return index;
    }

}