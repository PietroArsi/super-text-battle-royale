package org.supertextbattleroyale.maps;

import org.javatuples.Pair;
import org.supertextbattleroyale.maps.tiles.base.Tile;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class MapUtils {

    static int NO_RAY = 0;
    static int RAY = 1;

    /**
     * @param type the class of a Tile child class
     * @return a list of all tiles that are of the selected type
     */
    public static List<Pair<Integer, Integer>> getAllTilesFromType(GameMap map, Class<? extends Tile> type) {
        List<Pair<Integer, Integer>> list = new ArrayList<>();
        for (int i = 0; i < map.getMatrixWidth(); i++) {
            for (int j = 0; j < map.getMatrixHeight(); j++) {
                if (map.getMatrixMap()[i][j].getClass().equals(type)) list.add(new Pair<>(i, j));
            }
        }
        return list;
    }

    /**
     * A BFS alghorithm on a matrix
     *
     * @param zeroTiles a list of "source nodes"
     * @return a int matrix with the distance from the nearest source node
     */
    public static int[][] calculateDistances(GameMap map, List<Pair<Integer, Integer>> zeroTiles, boolean allowDiagonalMovement) {
        //Initialize all tile with MAX_INT
        int[][] distances = new int[map.getMatrixWidth()][map.getMatrixHeight()];
        for (int i = 0; i < map.getMatrixWidth(); i++)
            for (int j = 0; j < map.getMatrixHeight(); j++)
                distances[i][j] = Integer.MAX_VALUE;

        ArrayDeque<Pair<Integer, Integer>> visit_queue = new ArrayDeque<>();
        //Add all source nodes to the visit queue and set their distance to the source nodes to 0 (gac)
        for (Pair<Integer, Integer> p : zeroTiles) {
            distances[p.getValue0()][p.getValue1()] = 0;
            visit_queue.offer(p);
        }
        Pair<Integer, Integer> u;
        //Do a BFS search
        while (!visit_queue.isEmpty()) {
            u = visit_queue.poll();
            assert (u != null);
            int i = u.getValue0();
            int j = u.getValue1();
            //Visit all neighbours (also diagonal neighbours)
            for (int y = Math.max(0, j - 1); y <= Math.min(j + 1, map.getMatrixHeight() - 1); y++) {
                for (int x = Math.max(0, i - 1); x <= Math.min(i + 1, map.getMatrixWidth() - 1); x++) {
                    if (x != i || j != y) {
                        if(allowDiagonalMovement || x == i || y == j)
                            if (distances[x][y] == Integer.MAX_VALUE && map.getMatrixMap()[x][y].isTileWalkable()) {
                                distances[x][y] = distances[i][j] + 1;
                                visit_queue.offer(new Pair<>(x, y));
                        }
                    }
                }
            }
        }
        return distances;
    }

    public static Pair<Integer,Integer> pairFloatToInt(Pair<Float,Float> p) {
        return new Pair<>(Math.round(p.getValue0()), Math.round(p.getValue1()));
    }


    public static ArrayList<Pair<Integer,Integer>> discretizeRay(GameMap map, Pair<Integer,Integer> p1, Pair<Integer,Integer> p2) {
        ArrayList<Pair<Integer,Integer>> rayList = new ArrayList<>();
        int x1 = p1.getValue0();
        int y1 = p1.getValue1();
        int x2 = p2.getValue0();
        int y2 = p2.getValue1();
        int mx = x2 - x1;
        int my = y2 - y1;
        int dirX = mx > 0 ? 1 : -1;
        int dirY = my > 0 ? 1 : -1;
        int currentX = x1;
        int currentY = y1;
        float nextX = x1 + 0.5f*dirX;
        float nextY = y1 + 0.5f*dirY;
        //Initialize the Matrix
        rayList.add(new Pair<>(currentX,currentY));
        while(currentX != x2 && currentY != y2) {
            float tx = (nextX - x1)/mx;
            float ty = (nextY - y1)/my;
            rayList.add(new Pair<>(currentX,currentY));
            if(tx < ty) {
                currentX += dirX;
                nextX = currentX + 0.5f*dirX;
            } else {
                currentY += dirY;
                nextY = currentY +0.5f*dirY;
            }
        }
        if(currentX == x2) for(int i = currentY; i < y2; i++) rayList.add(new Pair<>(currentX,i));
        else for(int i = currentX; i < x2; i++) rayList.add(new Pair<>(i,currentY));
        return rayList;
    }

    public static void printRayMatrix(GameMap map, Pair<Integer,Integer> x1, Pair<Integer, Integer> x2) {
        System.out.println();
        System.out.printf("MAP: W %d x H %d\n", map.getMatrixWidth(), map.getMatrixHeight());
        ArrayList<Pair<Integer,Integer>> l = discretizeRay(map,x1,x2);
        for (int h = 0; h < map.getMatrixHeight(); h++) {
            System.out.println();
            for (int w = 0; w < map.getMatrixWidth(); w++) {
                System.out.print(l.contains(new Pair<Integer,Integer>(w,h)) ? 'O' : 'X');
            }
        }
    }

    public static void printRoomMatrix(GameMap map) {
        System.out.println();
        System.out.printf("MAP: W %d x H %d\n", map.getMatrixWidth(), map.getMatrixHeight());
        for (int h = 0; h < map.getMatrixHeight(); h++) {
            System.out.println();
            for (int w = 0; w < map.getMatrixWidth(); w++) {
                System.out.print(map.getMatrixMap()[w][h].getSymbol());
            }
        }
    }

    public static void printDistancesMatrix(GameMap map, List<Pair<Integer, Integer>> zeroTiles) {
        int[][] distances = calculateDistances(map, zeroTiles, false);
        System.out.println();
        for (int j = 0; j < map.getMatrixHeight(); j++) {
            System.out.println();
            for (int i = 0; i < map.getMatrixWidth(); i++) {
                if (distances[i][j] == Integer.MAX_VALUE)
                    System.out.print("X\t");
                else System.out.printf("%d\t", distances[i][j]);
            }
        }
        System.out.println();
    }
}
