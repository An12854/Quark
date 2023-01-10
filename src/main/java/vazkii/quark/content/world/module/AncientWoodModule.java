package vazkii.quark.content.world.module;

import net.minecraft.world.level.material.MaterialColor;
import vazkii.quark.base.handler.WoodSetHandler;
import vazkii.quark.base.handler.WoodSetHandler.WoodSet;
import vazkii.quark.base.module.QuarkModule;

public class AncientWoodModule extends QuarkModule {

	public static WoodSet woodSet;

	@Override
	public void register() {
		woodSet = WoodSetHandler.addWoodSet(this, "ancient", MaterialColor.TERRACOTTA_WHITE, MaterialColor.TERRACOTTA_WHITE);
	}
	
}