package com.github.goldy1992.mp3player.commons;

public interface DependencyInitialiser {

    /** Utility method used to initialise the dependencies set up by Dagger2. DOES NOT need to be
     * called by sub class  */
    void initialiseDependencies();
}
