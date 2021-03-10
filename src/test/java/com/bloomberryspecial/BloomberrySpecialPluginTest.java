package com.bloomberryspecial;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class BloomberrySpecialPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(BloomberrySpecialPlugin.class);
		RuneLite.main(args);
	}
}