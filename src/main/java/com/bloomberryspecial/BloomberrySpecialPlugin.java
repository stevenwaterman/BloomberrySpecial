package com.bloomberryspecial;

import com.bloomberryspecial.graphs.BloomberrySpecialChartOverlay;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ItemComposition;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetClosed;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.http.api.RuneLiteAPI;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.runelite.api.VarPlayer.CURRENT_GE_ITEM;

@Singleton
@Slf4j
@PluginDescriptor(name = "Bloomberry Special")
public class BloomberrySpecialPlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
    @Getter
	private BloomberrySpecialConfig config;

	private Map<String, BloomberrySpecialChartOverlay> overlays = new HashMap<>();

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private ItemManager itemManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ConfigManager configManager;

	private final OkHttpClient httpClient = new OkHttpClient.Builder()
			.addNetworkInterceptor(chain ->
				chain.proceed(chain.request()
						.newBuilder()
						.header("User-Agent", "https://github.com/stevenwaterman/BloomberrySpecial")
						.build()))
			.build();

	@Getter
	private ItemModel itemModel = null;

	@Override
	protected void startUp() throws Exception {
		if (config.priceChart()) addOverlay("priceChart");
		if (config.volumeChart()) addOverlay("volumeChart");
		if (config.dailyChart()) addOverlay("dailyChart");
		if (config.weeklyChart()) addOverlay("weeklyChart");
		if (config.marginChart()) addOverlay("marginChart");
		if (config.abnormalityChart()) addOverlay("abnormalityChart");
	}

	public void setItemModel(ItemModel itemModel) {
		this.itemModel = itemModel;
		overlays.values().forEach(overlay -> overlay.itemChanged(itemModel));
	}

	private void addOverlay(String name) {
		BloomberrySpecialChartOverlay overlay = BloomberrySpecialChartOverlay.create(name, config);
		overlay.itemChanged(itemModel);
		overlays.put(name, overlay);
		overlayManager.add(overlay);
	}

	private void removeOverlay(String name) {
		BloomberrySpecialChartOverlay overlay = overlays.remove(name);
		overlays.remove(name);
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged configChanged) {
		if (configChanged.getGroup().equals("BloomberrySpecial") && configChanged.getKey().endsWith("Chart")) {
			if(configChanged.getNewValue().equals("true")) {
				addOverlay(configChanged.getKey());
			} else {
				removeOverlay(configChanged.getKey());
			}
		}
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event) throws IOException {
		if(event.getIndex() == CURRENT_GE_ITEM.getId()) {
			int currentItem = client.getVar(CURRENT_GE_ITEM);
			if (itemManager.canonicalize(currentItem) != currentItem || currentItem <= 0) {
				setItemModel(null);
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

			ItemComposition item = client.getItemDefinition(currentItem);
			setItemModel(new ItemModel(item, data));
		}
	}

	@Subscribe
	public void onWidgetClosed(WidgetClosed widgetClosed) {
		if (widgetClosed.getGroupId() == 465) {
		    setItemModel(null);
		}
	}

	@Override
	public void shutDown() {
		overlays.values().forEach(overlay -> overlayManager.remove(overlay));
	}

	@Provides
	BloomberrySpecialConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(BloomberrySpecialConfig.class);
	}
}
