/*
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.watabou.noosa.audio;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;

import com.nyrds.android.util.ModdingMode;
import com.nyrds.pixeldungeon.ml.EventCollector;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.utils.Utils;

import java.io.File;

public enum Music implements MediaPlayer.OnPreparedListener,
		MediaPlayer.OnErrorListener {

	INSTANCE;

	private MediaPlayer player;

	private String lastPlayed;
	private boolean lastLooping;

	private boolean enabled = true;

	public void play(@NonNull String assetName, boolean looping) {

		if (isPlaying() && assetName.equals(lastPlayed)) {
			return;
		}

		stop();

		lastPlayed = assetName;
		lastLooping = looping;

		if (!enabled) {
			return;
		}

		assetName = "sound/"+assetName;
		
		try {
			player = new MediaPlayer();
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			
			File file = ModdingMode.getFile(assetName);
			if (file!=null && file.exists()) {
				player.setDataSource(file.getAbsolutePath());
			} else {
				AssetFileDescriptor afd = Game.instance().getAssets().openFd(assetName);
				player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
						afd.getLength());
				afd.close();
			}
			
			player.setOnPreparedListener(this);
			player.setOnErrorListener(this);
			player.setLooping(looping);
			player.prepareAsync();

		} catch (Exception e) {
			player.release();
			player = null;
			EventCollector.logException(e);
		}
	}

	public void mute() {
		lastPlayed = null;
		stop();
	}

	@Override
	public void onPrepared(MediaPlayer player) {
		player.start();
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		EventCollector.logEvent("Music", Utils.format("%d %d",what, extra));
		if (player != null) {
			player.release();
			player = null;
		}
		return true;
	}

	public void pause() {
		if (player != null && player.isPlaying()) {
			player.pause();
		}
	}

	public void resume() {
		if (player != null && !player.isPlaying()) {
			player.start();
		}
	}

	public void stop() {
		if (player != null) {
			player.stop();
			player.release();
			player = null;
		}
	}

	public void volume(float value) {
		if (player != null) {
			player.setVolume(value, value);
		}
	}

	public boolean isPlaying() {
		return player != null && player.isPlaying();
	}

	public void enable(boolean value) {
		enabled = value;
		if (isPlaying() && !value) {
			stop();
		} else if (!isPlaying() && value) {
			play(lastPlayed, lastLooping);
		}
	}
}
