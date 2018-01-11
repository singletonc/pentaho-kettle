package org.pentaho.di.ui.spoon;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * This class is used to share the staticSpoon instance with SWTBot.
 * SWTBot needs access to Spoon's shell in its constructor.
 *
 * @author csingleton
 */

public class SafeSpoonSharer {
    private static volatile Spoon spoon;
    private static final CyclicBarrier spoonSetBarrier = new CyclicBarrier(2);

    public static void setSpoon(Spoon ispoon) throws BrokenBarrierException, InterruptedException {
        spoon = ispoon;
        spoonSetBarrier.await();
    }

    public static Spoon getSpoon() throws BrokenBarrierException, InterruptedException {
        spoonSetBarrier.await();
        return spoon;
    }
}
