package com.watabou.pixeldungeon.actors.mobs;

import com.nyrds.pixeldungeon.mobs.common.IDepthAdjustable;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.blobs.ToxicGas;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.actors.buffs.Stun;
import com.watabou.pixeldungeon.items.food.RottenPasty;

public class MimicPie extends Mob implements IDepthAdjustable {
	
	private int level;
	
	public MimicPie() {
		baseSpeed = 1.25f;
		
		flying = true;

		level = Dungeon.depth;
		carcassChance = 0;

		collect(new RottenPasty());

		addImmunity( ToxicGas.class );
		addImmunity( Paralysis.class );
		addImmunity( Stun.class );
		adjustStats(level);
	}

	public void adjustStats( int level ) {
		this.level = level;

		hp(ht((3 + level) * 5));
		expForKill = 2 + 2 * (level - 1) / 5;
		baseAttackSkill = 9 + level;
		baseDefenseSkill = 2 * baseAttackSkill / 3;

		dmgMin = ht()/10;
		dmgMax = ht()/4;

		enemySeen = true;
	}

	@Override
	public boolean canBePet() {
		return false;
	}
}
