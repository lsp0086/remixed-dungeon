
package com.watabou.pixeldungeon.effects;

import com.nyrds.pixeldungeon.game.GameLoop;
import com.nyrds.platform.EventCollector;
import com.watabou.noosa.Visual;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.utils.PointF;

import org.jetbrains.annotations.NotNull;

public class Pushing extends Actor {

	private Char ch;
	private int from;
	private int to;
	
	private Effect effect;
	
	public Pushing(@NotNull Char ch, int from, int to ) {
		this.ch = ch;
		this.from = from;
		this.to = to;
	}
	
	@Override
	protected boolean act() {
		if (effect == null) {
			effect = new Effect();
		}
		deactivateActor();
		return true;
	}

	private class Effect extends Visual {

		private static final float DELAY = 0.15f;
		
		private PointF end;
		
		private float delay;
		
		Effect() {
			super( 0, 0, 0, 0 );

			if(ch==null) {
				EventCollector.logException("pushing null char");
				Actor.remove( Pushing.this );
				return;
			}

			if(!ch.valid()) {
				EventCollector.logException("pushing dummy char");
				Actor.remove( Pushing.this );
				return;
			}

			if(ch.getSprite().getParent()==null) {
				EventCollector.logException("pushing orphaned char");
				Actor.remove( Pushing.this );
				return;
			}


			point( ch.getSprite().worldToCamera( from ) );
			end = ch.getSprite().worldToCamera( to );
			
			speed.set( 2 * (end.x - getX()) / DELAY, 2 * (end.y - getY()) / DELAY );
			acc.set( -speed.x / DELAY, -speed.y / DELAY );
			
			delay = 0;

			GameScene.addToMobLayer( this );
		}
		
		@Override
		public void update() {
			super.update();
			
			if ((delay += GameLoop.elapsed) < DELAY) {

				ch.getSprite().setX(getX());
				ch.getSprite().setY(getY());
				
			} else {

				ch.getSprite().point( end );
				
				killAndErase();
				Actor.remove( Pushing.this );
				
				next();
			}
		}
	}

}
