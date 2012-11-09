package com.jaysan1292.groupproject.data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;

/** @author Jason Recillo */
public class PathTest {
    private Path path;

    @Before
    public void setUp() {
        path = new Path();
        ArrayList<Checkpoint> points = new ArrayList<Checkpoint>() {{
            add(new Checkpoint(0L, 43.08343f, -75.13454f));
            add(new Checkpoint(1L, 43.15846f, -75.45685f));
            add(new Checkpoint(2L, 43.18965f, -75.12435f));
            add(new Checkpoint(3L, 43.45796f, -75.21651f));
            add(new Checkpoint(4L, 43.78952f, -75.18532f));
            add(new Checkpoint(5L, 43.05496f, -75.78965f));
            add(new Checkpoint(6L, 43.48655f, -75.18565f));
        }};
        path.setCheckpoints(points);
    }

    @After
    public void tearDown() {
        path = null;
    }

    @Test
    public void testAddCheckpoint() {
        path.addCheckpoint(new Checkpoint(7L, 43.48865f, -75.89517f));

        ArrayList<Checkpoint> expected = new ArrayList<Checkpoint>() {{
            add(new Checkpoint(0L, 43.08343f, -75.13454f));
            add(new Checkpoint(1L, 43.15846f, -75.45685f));
            add(new Checkpoint(2L, 43.18965f, -75.12435f));
            add(new Checkpoint(3L, 43.45796f, -75.21651f));
            add(new Checkpoint(4L, 43.78952f, -75.18532f));
            add(new Checkpoint(5L, 43.05496f, -75.78965f));
            add(new Checkpoint(6L, 43.48655f, -75.18565f));
            add(new Checkpoint(7L, 43.48865f, -75.89517f));
        }};

        assertArrayEquals(expected.toArray(), path.getCheckpoints().toArray());
    }

    @Test
    public void testRemoveCheckpoint() {
        // Regular test
        path.removeCheckpoint(0);

        ArrayList<Checkpoint> expected = new ArrayList<Checkpoint>() {{
            add(new Checkpoint(1L, 43.15846f, -75.45685f));
            add(new Checkpoint(2L, 43.18965f, -75.12435f));
            add(new Checkpoint(3L, 43.45796f, -75.21651f));
            add(new Checkpoint(4L, 43.78952f, -75.18532f));
            add(new Checkpoint(5L, 43.05496f, -75.78965f));
            add(new Checkpoint(6L, 43.48655f, -75.18565f));
        }};
        assertArrayEquals(expected.toArray(), path.getCheckpoints().toArray());
    }

    @Test
    public void testMoveCheckpoint() {
        path.moveCheckpoint(path.getCheckpoint(5), Path.DIRECTION_UP);

        ArrayList<Checkpoint> expected = new ArrayList<Checkpoint>() {{
            add(new Checkpoint(0L, 43.08343f, -75.13454f));
            add(new Checkpoint(1L, 43.15846f, -75.45685f));
            add(new Checkpoint(2L, 43.18965f, -75.12435f));
            add(new Checkpoint(3L, 43.45796f, -75.21651f));
            add(new Checkpoint(5L, 43.05496f, -75.78965f));
            add(new Checkpoint(4L, 43.78952f, -75.18532f));
            add(new Checkpoint(6L, 43.48655f, -75.18565f));
        }};

        assertArrayEquals(expected.toArray(), path.getCheckpoints().toArray());
    }
}
