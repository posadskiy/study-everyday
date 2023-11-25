package com.posadskiy.kata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TheLift {

    public static int[] theLift(int[][] queues, final int capacity) {
        final Lift lift = new Lift(capacity);
        List<Integer> history = new ArrayList<>();

        history.add(0);
        Integer floor = lift.takeFirstPassengers(queues);
        if (floor != null && floor != 0) {
            history.add(floor);
        }
        if (lift.passengers.size() != 0) {
            floor = lift.work(queues);
            if (floor != null) {
                history.add(floor);
            }
        }

        while (isQueueNotEmpty(queues) || lift.passengers.size() != 0) {
            if (lift.floor == 0 && lift.passengers.size() == 0) {
                final int e = lift.takeFirstPassengers(queues);
                if (!history.get(history.size() - 1).equals(e)) {
                    history.add(e);
                }
            }
            floor = lift.work(queues);
            if (floor == null || history.get(history.size() - 1).equals(floor)) {
                continue;
            }

            history.add(floor);
        }

        if (history.size() != 0 && history.get(history.size() - 1) != 0) {
            history.add(0);
        }

        return history.stream().mapToInt(Integer::intValue).toArray();
    }

    private static boolean isQueueNotEmpty(int[][] queue) {
        for (int[] floor : queue) {
            if (floor.length != 0) {
                return true;
            }
        }

        return false;
    }

}

class Lift {

    public int capacity;
    public int floor;
    public boolean direction;

    public List<Integer> passengers = new ArrayList<>();

    public Lift(int capacity) {
        this.capacity = capacity;
    }

    public int takeFirstPassengers(int[][] queues) {
        final int[] elementsHere = Arrays.stream(queues).flatMapToInt(IntStream::of).toArray();
        final long count = Arrays.stream(elementsHere).count();
        if (count <= capacity && isSortedDesc(queues)) {
            for (int i = queues.length - 1; i >= 0; i--) {
                if (queues[i].length != 0) {
                    this.passengers.addAll(Arrays.stream(queues[i]).boxed().collect(Collectors.toList()));
                    queues[i] = new int[]{};
                    this.floor = i;
                    this.direction = true;

                    return this.floor;
                }
            }
        }

        boolean startFromEnd = true;
        for (int i = 0; i < queues.length; i++) {
            if (!startFromEnd) {
                break;
            }
            for (int i1 = 0; i1 < queues[i].length; i1++) {
                if (queues[i][i1] > i) {
                    startFromEnd = false;
                    break;
                }
            }
        }

        if (startFromEnd) {
            for (int i = queues.length - 1; i >= 0; --i) {
                if (queues[i].length != 0) {
                    List<Integer> els = new ArrayList<>();
                    List<Integer> floor = new ArrayList<>();
                    for (int i1 = 0; i1 < queues[i].length; ++i1) {
                        if ((capacity > els.size()) && (queues[i][i1] < i)) {
                            els.add(queues[i][i1]);
                        } else {
                            floor.add(queues[i][i1]);
                        }
                    }
                    queues[i] = floor.stream().mapToInt(Integer::intValue).toArray();
                    this.passengers = els;
                    this.floor = i;

                    return this.floor;
                }
            }
        }

        for (int i = 0; i < queues.length; i++) {
            if (queues[i].length != 0) {
                List<Integer> els = new ArrayList<>();
                List<Integer> floor = new ArrayList<>();
                for (int i1 = 0; i1 < queues[i].length; ++i1) {
                    if ((capacity > els.size()) && (queues[i][i1] > i)) {
                        els.add(queues[i][i1]);
                    } else {
                        floor.add(queues[i][i1]);
                    }
                }
                queues[i] = floor.stream().mapToInt(Integer::intValue).toArray();
                this.passengers = els;
                this.floor = i;

                return this.floor;
            }
        }

        return floor;
    }

    boolean isSortedDesc(int[][] queue) {
        for (int i = queue.length - 1; i >= 0; i--) {
            for (int i1 : queue[i]) {
                if (i1 > i) {
                    return false;
                }
            }
        }
        return true;
    }

    public Integer work(int[][] queues) {
        if (passengers.size() == capacity) {
            if (passengers.stream().noneMatch(value -> direction ? value < floor : value > floor)) {
                direction = !direction;
            }
        }
        if (this.direction) {
            if (floor == 0) {
                this.floor++;
                this.direction = false;
            } else {
                this.floor--;
            }
        } else {
            if (floor == queues.length - 1) {
                this.floor--;
                this.direction = true;
            } else {
                this.floor++;
            }
        }

        boolean shouldStop = false;
        final boolean disembark = passengers.contains(floor);
        final boolean buttonPressed = Arrays.stream(queues[floor]).anyMatch(value -> direction ? value < floor : value > floor);
        int lastRow = -1;
        if (passengers.size() == 0) {
            if (direction) {
                for (int i = 0; i < queues.length; i++) {
                    int finalI = i;
                    if (Arrays.stream(queues[i]).anyMatch(value -> value > finalI)) {
                        lastRow = i;
                        break;
                    }
                }
            } else {
                for (int i = queues.length - 1; i >= 0; --i) {
                    int finalI = i;
                    if (Arrays.stream(queues[i]).anyMatch(value -> value < finalI)) {
                        lastRow = i;
                        break;
                    }
                }
            }
        }
        final boolean returnWay =
            passengers.size() == 0 && floor == lastRow;
        final boolean nonEmptyFloor = queues[floor].length != 0;
        final boolean embark = nonEmptyFloor && (buttonPressed || returnWay);
        if (disembark || embark) {
            if (passengers.size() == 0 && !buttonPressed) {
                direction = !direction;
            }
            shouldStop = true;
            this.passengers = passengers.stream().filter(passenger -> passenger != this.floor).collect(Collectors.toList());

            List<Integer> els = new ArrayList<>();
            List<Integer> floorElements = new ArrayList<>();
            for (int i1 = 0; i1 < queues[floor].length; ++i1) {
                if ((capacity > (passengers.size() + els.size())) && (floor == 0 || floor == queues.length - 1 || (
                    !direction && (queues[floor][i1] > floor)
                        || direction && (queues[floor][i1] < floor)))) {
                    els.add(queues[floor][i1]);
                } else {
                    floorElements.add(queues[floor][i1]);
                }
            }
            queues[floor] = floorElements.stream().mapToInt(Integer::intValue).toArray();
            passengers.addAll(els);
        }

        return shouldStop ? this.floor : null;
    }
}
