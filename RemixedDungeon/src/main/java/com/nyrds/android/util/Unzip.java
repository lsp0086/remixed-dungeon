package com.nyrds.android.util;

import com.nyrds.pixeldungeon.ml.EventCollector;
import com.watabou.pixeldungeon.RemixedDungeon;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import lombok.var;

public class Unzip {

	private static final int    BUFFER_SIZE = 16384;

	private static void ensureDir(String dir) {
		File f = new File(dir);

		if (!f.exists() || !f.isDirectory()) {
			f.mkdirs();
		}
	}

	static public Mods.ModDesc inspectMod(InputStream fin, String tgtDir) {
		Mods.ModDesc ret = new Mods.ModDesc();
		ret.name = Utils.EMPTY_STRING;

		String primaryStrings = Utils.format("strings_%s.json", RemixedDungeon.uiLanguage());
		String fallbackStrings = Utils.format("strings_en.json");

		try {
			ZipInputStream zin = new ZipInputStream(fin);
			ZipEntry ze;

			while ((ze = zin.getNextEntry()) != null) {
				GLog.debug( "Unzipping " + ze.getName());

				if (ze.isDirectory() && ret.name.isEmpty()) {
					ret.name = ze.getName().replace("/","");
				} else {
					if(ze.getName().contains("version.json")) {
						var modVersion = JsonHelper.readJsonFromStream(zin);
						ret.version = modVersion.getInt("version");
					}
					zin.closeEntry();
				}

			}
			zin.close();
		} catch (Exception e) {
			EventCollector.logException(e);
			return ret;
		}

		return ret;
	}

	static public boolean unzipStream(InputStream fin, String tgtDir, @Nullable UnzipProgress listener) {
		ensureDir(tgtDir);
		try {
			ZipInputStream zin = new ZipInputStream(fin);
			ZipEntry ze;

			byte[] data = new byte[BUFFER_SIZE];

			int entriesProcessed = 0;

			while ((ze = zin.getNextEntry()) != null) {
				entriesProcessed = entriesProcessed + 1;
				if(listener!=null) {
					listener.progress(entriesProcessed);
				}

				GLog.debug( "Unzipping " + ze.getName());

				if (ze.isDirectory()) {
					ensureDir(tgtDir + "/" + ze.getName());
				} else {

					FileOutputStream fout = new FileOutputStream(tgtDir + "/" + ze.getName());

					int bytesRead;
					while ((bytesRead = zin.read(data)) != -1) {
						fout.write(data, 0, bytesRead);
					}

					zin.closeEntry();
					fout.close();
				}
			}
			zin.close();
		} catch (Exception e) {
			EventCollector.logException(e);
			return false;
		}

		return true;
	}

	static public boolean unzip(String zipFile, String tgtDir, @Nullable UnzipProgress listener) {
		try {
			return unzipStream(new FileInputStream(zipFile), tgtDir, listener);
		} catch (FileNotFoundException e) {
			return false;
		}
	}

	interface UnzipProgress {
		void progress(int unpacked);
	}
}