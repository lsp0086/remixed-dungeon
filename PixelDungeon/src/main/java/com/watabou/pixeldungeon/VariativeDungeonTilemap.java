package com.watabou.pixeldungeon;

import com.watabou.pixeldungeon.levels.Level;

/**
 * Created by mike on 15.02.2018.
 * This file is part of Remixed Pixel Dungeon.
 */

public class VariativeDungeonTilemap extends DungeonTilemap {
    public VariativeDungeonTilemap(Level level, String tiles, int[] baseMap, int[] decoMap) {
        super(level, tiles, baseMap, decoMap);
    }
}
