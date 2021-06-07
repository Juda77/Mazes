package mazes.logic.carvers;

import graphs.EdgeWithData;
import graphs.minspantrees.MinimumSpanningTreeFinder;
import mazes.entities.LineSegment;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeGraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver extends MazeCarver {
    MinimumSpanningTreeFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder;
    private final Random rand;

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random();
    }

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder,
                             long seed) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(seed);
    }

    @Override
    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) {

        // Hint: you'll probably need to include something like the following:
        // this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(edges));

        List<EdgeWithData<Room, Wall>> randomEdges = new ArrayList<>();
        Iterator wallsIterator = walls.iterator();
        // create edges with random weights
        for (int i = 0; i < walls.size(); i++) {

            Wall currWall = (Wall) wallsIterator.next();
            EdgeWithData randomEdge = new EdgeWithData<>(currWall.getRoom1(), currWall.getRoom2(),
                rand.nextDouble(), currWall);
            randomEdges.add(randomEdge);
        }

        Collection<EdgeWithData<Room, Wall>> mst =
            this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(randomEdges)).edges();

        Set<Wall> randomWalls = new HashSet<>();
        Iterator mstIterator = mst.iterator();
        while (mstIterator.hasNext()) {
            EdgeWithData<Room, Wall> randomMSTEdge = (EdgeWithData) mstIterator.next();
            Room start = randomMSTEdge.from();
            Room end = randomMSTEdge.to();
            LineSegment line = new LineSegment(start.getCenter(), end.getCenter());
            Wall randomWall = new Wall(start, end, line);
            randomWalls.add(randomWall);
        }

        return randomWalls;

    }
}
