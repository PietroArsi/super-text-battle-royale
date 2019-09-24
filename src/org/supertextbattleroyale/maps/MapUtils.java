package org.supertextbattleroyale.maps;

import org.javatuples.Pair;
import org.supertextbattleroyale.interfaces.TileFilter;
import org.supertextbattleroyale.maps.tiles.base.Tile;
import org.supertextbattleroyale.players.Player;
import org.supertextbattleroyale.utils.RandomUtils;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MapUtils {

    private static int NO_RAY = 0;
    private static int RAY = 1;

    /**
     * @param type the class of a Tile child class
     * @return a list of all tiles that are of the selected type
     */
    public static List<Point> getAllTilesFromType(GameMap map, Class<? extends Tile> type) {
        List<Point> list = new ArrayList<>();
        for (int i = 0; i < map.getMatrixWidth(); i++) {
            for (int j = 0; j < map.getMatrixHeight(); j++) {
                if (map.getMatrixMap()[i][j].getClass().equals(type)) list.add(new Point(i, j));
            }
        }
        return list;
    }

    /**
     * A BFS alghorithm on a matrix
     *
     * @param filter    a function of the map and the coordinates that is true when the player can cross the tile in that coordinate
     * @param zeroTiles a list of "source nodes"
     * @return a int matrix with the distance from the nearest source node
     */
    public static int[][] calculateDistances(GameMap map, TileFilter filter, List<Point> zeroTiles, boolean allowDiagonalMovement) {
        //Initialize all tile with MAX_INT
        int[][] distances = new int[map.getMatrixWidth()][map.getMatrixHeight()];
        for (int i = 0; i < map.getMatrixWidth(); i++)
            for (int j = 0; j < map.getMatrixHeight(); j++)
                distances[i][j] = Integer.MAX_VALUE;

        ArrayDeque<Point> visit_queue = new ArrayDeque<>();
        //Add all source nodes to the visit queue and set their distance to the source nodes to 0 (gac)
        for (Point p : zeroTiles) {
            distances[p.x][p.y] = 0;
            visit_queue.offer(p);
        }
        Point u;
        //Do a BFS search
        while (!visit_queue.isEmpty()) {
            u = visit_queue.poll();
            assert (u != null);
            int i = u.x;
            int j = u.y;
            //Visit all neighbours (also diagonal neighbours)
            for (int y = Math.max(0, j - 1); y <= Math.min(j + 1, map.getMatrixHeight() - 1); y++) {
                for (int x = Math.max(0, i - 1); x <= Math.min(i + 1, map.getMatrixWidth() - 1); x++) {
                    if (x != i || j != y) {
                        if (allowDiagonalMovement || x == i || y == j) {
                            if (distances[x][y] == Integer.MAX_VALUE && filter.canCross(map, new Point(x, y))) {
                                distances[x][y] = distances[i][j] + 1;
                                visit_queue.offer(new Point(x, y));
                            }
                        }
                    }
                }
            }
        }
        return distances;
    }


    public static Optional<Point> getNextPathStep(GameMap map, int[][] distances, Point starting, boolean allowDiagonalMovement) {
        int i = starting.x;
        int j = starting.y;
        ArrayList<Point> bestNeighbours = new ArrayList<>();
        int targetDistance = Math.max(distances[i][j] - 1, 0);
        for (int y = Math.max(0, j - 1); y <= Math.min(j + 1, map.getMatrixHeight() - 1); y++) {
            for (int x = Math.max(0, i - 1); x <= Math.min(i + 1, map.getMatrixWidth() - 1); x++) {
                if (allowDiagonalMovement || x == i || y == j) {
                    if (distances[x][y] == targetDistance) bestNeighbours.add(new Point(x, y));
                }
            }
        }
        if (bestNeighbours.isEmpty()) return Optional.empty();
        else
            return Optional.of(bestNeighbours.get(RandomUtils.randomIntRange(0, bestNeighbours.size() - 1))); //Scelgo a caso
    }

    public static Point pairFloatToInt(Pair<Float, Float> p) {
        return new Point(Math.round(p.getValue0()), Math.round(p.getValue1()));
    }

    /**
     * A function that traces continue rays in a tile map
     *
     * @param map The actual gamemap
     * @param p1  the starting point of the line
     * @param p2  the ending point of the line
     * @return A list of coordinates of the tiled crossed by the line between p1 and p2
     */
    public static ArrayList<Point> discretizeRay(GameMap map, Point p1, Point p2) {
        ArrayList<Point> rayList = new ArrayList<>();
        int x1 = p1.x;
        int y1 = p1.y;
        int x2 = p2.x;
        int y2 = p2.y;
        int mx = x2 - x1;
        int my = y2 - y1;
        int dirX = mx > 0 ? 1 : -1;
        int dirY = my > 0 ? 1 : -1;
        int currentX = x1;
        int currentY = y1;
        float nextX = x1 + 0.5f * dirX;
        float nextY = y1 + 0.5f * dirY;
        //Initialize the Matrix
        rayList.add(new Point(currentX, currentY));
        while (currentX != x2 && currentY != y2) {
            float tx = (nextX - x1) / mx;
            float ty = (nextY - y1) / my;
            rayList.add(new Point(currentX, currentY));
            if (tx < ty) {
                currentX += dirX;
                nextX = currentX + 0.5f * dirX;
            } else if (tx > ty) {
                currentY += dirY;
                nextY = currentY + 0.5f * dirY;
            } else {
                currentX += dirX;
                currentY += dirY;
                nextX = currentX + 0.5f * dirX;
                nextY = currentY + 0.5f * dirY;
            }
        }
        rayList.add(new Point(currentX, currentY));
        if (currentX == x2)
            for (int i = currentY; (i <= y2 && dirY > 0) || (i >= y2 && dirY < 0); i += dirY)
                rayList.add(new Point(currentX, i));
        else
            for (int i = currentX; (i <= x2 && dirX > 0) || (i >= x2 && dirX < 0); i += dirX)
                rayList.add(new Point(i, currentY));

        return rayList;
    }

    /**
     * @param map  The current game map
     * @param func A function that returns true if the tile could be crossed by the ray (e.g. is not solid)
     * @param p1   The starting point of the ray
     * @param p2   The ending point of the ray
     * @return true if there aren't tiles that couldn't be crossed by the ray (based on func), false otherwise
     */
    public static boolean canRayReachTile(GameMap map, TileFilter func, Point p1, Point p2) {
        return !discretizeRay(map, p1, p2).stream().anyMatch(i -> !func.canCross(map, i));
    }

    public static Point getBestObjective(Point from, GameMap map, TileFilter filter, List<Point> zeroTiles, boolean allowDiagonalMovement) {
        int[][] m = calculateDistances(map, filter, zeroTiles, allowDiagonalMovement);

        do {
            from = getNextPathStep(map, m, from, allowDiagonalMovement).get();
        } while (m[from.x][from.y] > 0);

        return from;
    }


    public static void printRayMatrix(GameMap map, TileFilter func, Point x1, Point x2) {
        System.out.println();
        System.out.printf("MAP: W %d x H %d\n", map.getMatrixWidth(), map.getMatrixHeight());
        ArrayList<Point> l = discretizeRay(map, x1, x2);
        for (int h = 0; h < map.getMatrixHeight(); h++) {
            System.out.println();
            for (int w = 0; w < map.getMatrixWidth(); w++) {
                System.out.print(l.contains(new Point(w, h)) ? func.canCross(map, new Point(w, h)) ? 'O' : 'X' : '-');
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

    public static void printDistancesMatrix(GameMap gameMap, List<Point> zeroTiles) {
        int[][] distances = calculateDistances(gameMap, Filters.filterNonWalkable(), zeroTiles, false);
        System.out.println();
        for (int j = 0; j < gameMap.getMatrixHeight(); j++) {
            System.out.println();
            for (int i = 0; i < gameMap.getMatrixWidth(); i++) {
                if (distances[i][j] == Integer.MAX_VALUE)
                    System.out.print("X\t");
                else System.out.printf("%d\t", distances[i][j]);
            }
        }
        System.out.println();
    }
}
