package com.posadskiy.kata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Write a method that takes a field for well-known board game "Battleship" as an argument and returns true if it has a valid disposition of ships, false otherwise. Argument is guaranteed to be 10*10 two-dimension array. Elements in the array are numbers, 0 if the cell is free and 1 if occupied by ship.
 * <p>
 * Battleship (also Battleships or Sea Battle) is a guessing game for two players. Each player has a 10x10 grid containing several "ships" and objective is to destroy enemy's forces by targetting individual cells on his field. The ship occupies one or more cells in the grid. Size and number of ships may differ from version to version. In this kata we will use Soviet/Russian version of the game.
 * <p>
 * <p>
 * Before the game begins, players set up the board and place the ships accordingly to the following rules:
 * There must be single battleship (size of 4 cells), 2 cruisers (size 3), 3 destroyers (size 2) and 4 submarines (size 1). Any additional ships are not allowed, as well as missing ships.
 * Each ship must be a straight line, except for submarines, which are just single cell.
 * <p>
 * The ship cannot overlap or be in contact with any other ship, neither by edge nor by corner.
 * <p>
 * This is all you need to solve this kata.
 */
public class BattleField {
    public static boolean fieldValidator(int[][] field) {
        List<Ship> ships = findAllShips(field);

        return validateShipsNumber(ships) && validateShipPositions(ships);
    }

    private static List<Ship> findAllShips(int[][] field) {
        final int n = field.length;
        int[][] usedShips = new int[n][n];
        List<Ship> ships = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (field[i][j] == 1 && usedShips[i][j] != 1) {
                    final Ship ship = new Ship();
                    if (i - 1 > 0 && field[i - 1][j] == 1 || i + 1 < n && field[i + 1][j] == 1) {
                        ship.points.add(new Point(i, j));
                        for (int i1 = i - 1; i1 >= 0; --i1) {
                            if (field[i1][j] == 1) {
                                ship.points.add(new Point(i1, j));
                                usedShips[i1][j] = 1;
                            } else {
                                break;
                            }
                        }
                        for (int i1 = i + 1; i1 < n; ++i1) {
                            if (field[i1][j] == 1) {
                                ship.points.add(new Point(i1, j));
                                usedShips[i1][j] = 1;
                            } else {
                                break;
                            }
                        }
                    } else if (j - 1 > 0 && field[i][j - 1] == 1 || j + 1 < n && field[i][j + 1] == 1) {
                        ship.points.add(new Point(i, j));
                        for (int j1 = j - 1; j1 >= 0; --j1) {
                            if (field[i][j1] == 1) {
                                ship.points.add(new Point(i, j1));
                                usedShips[i][j1] = 1;
                            } else {
                                break;
                            }
                        }
                        for (int j1 = j + 1; j1 < n; ++j1) {
                            if (field[i][j1] == 1) {
                                ship.points.add(new Point(i, j1));
                                usedShips[i][j1] = 1;
                            } else {
                                break;
                            }
                        }
                    } else {
                        usedShips[i][j] = 1;
                        ship.points.add(new Point(i, j));
                    }
                    ships.add(ship);
                }
            }
        }

        return ships;
    }

    private static boolean validateShipsNumber(List<Ship> ships) {
        final int[] shipsLength = ships.stream().mapToInt(ship -> ship.points.size()).sorted().toArray();

        if (shipsLength.length != 10) return false;

        final int[] oneUnits = Arrays.copyOfRange(shipsLength, 0, 4);
        if (IntStream.of(oneUnits).anyMatch(value -> value != 1)) return false;

        final int[] twoUnits = Arrays.copyOfRange(shipsLength, 4, 7);
        if (IntStream.of(twoUnits).anyMatch(value -> value != 2)) return false;

        final int[] threeUnits = Arrays.copyOfRange(shipsLength, 7, 9);
        if (IntStream.of(threeUnits).anyMatch(value -> value != 3)) return false;

        final int[] fourUnits = Arrays.copyOfRange(shipsLength, 9, 10);
        if (IntStream.of(fourUnits).anyMatch(value -> value != 4)) return false;

        return true;
    }

    private static boolean validateShipPositions(List<Ship> ships) {
        for (Ship ship : ships) {
            final List<Point> points = ships.stream()
                .filter(ship1 -> !ship.equals(ship1))
                .map(ship1 -> ship1.points)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

            for (Point point : ship.points) {
                if (points.stream()
                    .filter(point::notEqual)
                    .anyMatch(point1 -> Math.abs(point.x - point1.x) < 2 && Math.abs(point.y - point1.y) < 2)) {
                    return false;
                }
            }
        }

        return true;
    }
}

class Ship {
    List<Point> points = new ArrayList<>();
}

class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean notEqual(Object point) {
        return !this.equals(point);
    }
}
