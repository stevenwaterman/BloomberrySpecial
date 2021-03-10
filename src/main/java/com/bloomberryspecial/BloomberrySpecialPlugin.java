package com.bloomberryspecial;

import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Provides;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ItemComposition;
import net.runelite.api.events.ItemDespawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.AsyncBufferedImage;
import net.runelite.client.util.ImageUtil;
import net.runelite.http.api.RuneLiteAPI;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.runelite.api.VarPlayer.CURRENT_GE_ITEM;

@Slf4j
@PluginDescriptor(name = "Bloomberry Special")
public class BloomberrySpecialPlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private BloomberrySpecialConfig config;

	@Inject
	private BloomberrySpecialOverlay overlay;

	@Inject
	private BloomberrySpecialPanel panel;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private ItemManager itemManager;

	@Inject
	private OverlayManager overlayManager;

	private final OkHttpClient httpClient = new OkHttpClient.Builder()
			.addNetworkInterceptor(chain ->
				chain.proceed(chain.request()
						.newBuilder()
						.header("User-Agent", "https://github.com/stevenwaterman/BloomberrySpecial")
						.build()))
			.build();

	@Override
	protected void startUp() {
		final BufferedImage icon = ImageUtil.getResourceStreamFromClass(getClass(), "blurberry_special.png");
		final NavigationButton button = NavigationButton.builder()
				.tooltip("Bloomberry Special")
				.priority(3)
				.icon(icon)
				.panel(panel)
				.build();

		clientToolbar.addNavigation(button);
		overlayManager.add(overlay);
	}

	@Getter
	private ItemModel itemModel = null;

	@Subscribe
	public void onVarbitChanged(net.runelite.api.events.VarbitChanged event) throws Exception {
		if(event.getIndex() == CURRENT_GE_ITEM.getId()) {
			int currentItem = client.getVar(CURRENT_GE_ITEM);
			if (itemManager.canonicalize(currentItem) != currentItem || currentItem == -1) {
			    itemModel = null;
			    panel.updated();
			    overlay.updated();
			    return;
			}

			final HttpUrl url = HttpUrl.parse("https://prices.runescape.wiki/osrs")
					.newBuilder()
					.addPathSegment("5m")
					.addQueryParameter("id", Integer.toString(currentItem))
					.build();

			log.info("item id: {}", currentItem);
			log.info("url: {}", url);
			final Request request = new Request.Builder().url(url).build();
			final Response response = httpClient.newCall(request).execute();
			final InputStream in = response.body().byteStream();
			final InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
			Type type = new TypeToken<ArrayList<RLHistoricalDatapoint>>() {}.getType();
			final List<RLHistoricalDatapoint> data = RuneLiteAPI.GSON.fromJson(reader, type);
//			log.info("data: {}", data);

			ItemComposition item = client.getItemDefinition(currentItem);
			AsyncBufferedImage image = itemManager.getImage(currentItem);
			itemModel = new ItemModel(item, data, image);
			panel.updated();
			overlay.updated();
		}
	}

	@Provides
	BloomberrySpecialConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(BloomberrySpecialConfig.class);
	}
}
