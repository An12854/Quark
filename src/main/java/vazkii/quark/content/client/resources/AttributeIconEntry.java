package vazkii.quark.content.client.resources;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public record AttributeIconEntry(
	 Map<AttributeSlot, AttributeDisplayType> displayTypes,
	 ResourceLocation texture) {
	public static class Serializer implements JsonDeserializer<AttributeIconEntry>, JsonSerializer<AttributeIconEntry> {
		public static Serializer INSTANCE = new Serializer();

		@Override
		public AttributeIconEntry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject obj = GsonHelper.convertToJsonObject(json, "attribute icon");
			JsonObject displayObj = GsonHelper.getAsJsonObject(obj, "display");
			Map<AttributeSlot, AttributeDisplayType> display = new HashMap<>();
			for (AttributeSlot slot : AttributeSlot.values()) {
				String key = slot.name().toLowerCase(Locale.ROOT);
				String displayType = GsonHelper.getAsString(displayObj, key).toUpperCase(Locale.ROOT);
				AttributeDisplayType trueType = null;
				for (AttributeDisplayType type : AttributeDisplayType.values()) {
					if (type.name().equals(displayType)) {
						trueType = type;
						break;
					}
				}
				if (trueType == null)
					throw new JsonSyntaxException("Display type " + displayType + " is not valid");

				display.put(slot, trueType);
			}

			String texturePath = GsonHelper.getAsString(obj, "texture");
			ResourceLocation truncatedPath = new ResourceLocation(texturePath);
			ResourceLocation texture = new ResourceLocation(truncatedPath.getNamespace(), "textures/" + truncatedPath.getPath() + ".png");

			return new AttributeIconEntry(display, texture);
		}

		@Override
		public JsonElement serialize(AttributeIconEntry src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject obj = new JsonObject();
			JsonObject display = new JsonObject();
			for (AttributeSlot slot : AttributeSlot.values()) {
				display.addProperty(slot.name().toLowerCase(Locale.ROOT), src.displayTypes.get(slot).name().toLowerCase(Locale.ROOT));
			}
			obj.add("display", display);
			obj.addProperty("texture", src.texture.getNamespace() + ":" + snipTexturePath(src.texture.getNamespace()));
			return null;
		}

		private static String snipTexturePath(String texture) {
			if (texture.startsWith("textures/"))
				texture = texture.substring(9);
			if (texture.endsWith(".png"))
				texture = texture.substring(0, texture.length() - 4);
			return texture;
		}
	}
}