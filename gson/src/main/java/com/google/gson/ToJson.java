package com.google.gson;

import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Type;

public class ToJson {

  private final Gson gson;

  public ToJson(Gson gson) {
    this.gson = gson;
  }

  public JsonElement toJsonTree(Object src) {
    if (src == null) {
      return JsonNull.INSTANCE;
    }
    return toJsonTree(src, src.getClass());
  }

  public JsonElement toJsonTree(Object src, Type typeOfSrc) {
    JsonTreeWriter writer = new JsonTreeWriter();
    toJson(src, typeOfSrc, writer);
    return writer.get();
  }

  public String toJson(Object src) {
    if (src == null) {
      return toJson(JsonNull.INSTANCE);
    }
    return toJson(src, src.getClass());
  }

  public String toJson(Object src, Type typeOfSrc) {
    StringWriter writer = new StringWriter();
    toJson(src, typeOfSrc, writer);
    return writer.toString();
  }

  public void toJson(Object src, Appendable writer) throws JsonIOException {
    if (src != null) {
      toJson(src, src.getClass(), writer);
    } else {
      toJson(JsonNull.INSTANCE, writer);
    }
  }

  public void toJson(Object src, Type typeOfSrc, Appendable writer) throws JsonIOException {
    try {
      JsonWriter jsonWriter = gson.newJsonWriter(Streams.writerForAppendable(writer));
      toJson(src, typeOfSrc, jsonWriter);
    } catch (IOException e) {
      throw new JsonIOException(e);
    }
  }

  public void toJson(Object src, Type typeOfSrc, JsonWriter writer) throws JsonIOException {
    @SuppressWarnings("unchecked")
    TypeAdapter<Object> adapter = (TypeAdapter<Object>) gson.getAdapter(TypeToken.get(typeOfSrc));

    Strictness oldStrictness = writer.getStrictness();
    if (this.gson.strictness != null) {
      writer.setStrictness(this.gson.strictness);
    } else if (writer.getStrictness() == Strictness.LEGACY_STRICT) {
      // For backward compatibility change to LENIENT if writer has default strictness LEGACY_STRICT
      writer.setStrictness(Strictness.LENIENT);
    }

    boolean oldHtmlSafe = writer.isHtmlSafe();
    boolean oldSerializeNulls = writer.getSerializeNulls();

    writer.setHtmlSafe(gson.htmlSafe);
    writer.setSerializeNulls(gson.serializeNulls);
    try {
      adapter.write(writer, src);
    } catch (IOException e) {
      throw new JsonIOException(e);
    } catch (AssertionError e) {
      throw new AssertionError(
          "AssertionError (GSON " + GsonBuildConfig.VERSION + "): " + e.getMessage(), e);
    } finally {
      writer.setStrictness(oldStrictness);
      writer.setHtmlSafe(oldHtmlSafe);
      writer.setSerializeNulls(oldSerializeNulls);
    }
  }

  public String toJson(JsonElement jsonElement) {
    StringWriter writer = new StringWriter();
    toJson(jsonElement, writer);
    return writer.toString();
  }

  public void toJson(JsonElement jsonElement, Appendable writer) throws JsonIOException {
    try {
      JsonWriter jsonWriter = gson.newJsonWriter(Streams.writerForAppendable(writer));
      toJson(jsonElement, jsonWriter);
    } catch (IOException e) {
      throw new JsonIOException(e);
    }
  }

  public void toJson(JsonElement jsonElement, JsonWriter writer) throws JsonIOException {
    Strictness oldStrictness = writer.getStrictness();
    boolean oldHtmlSafe = writer.isHtmlSafe();
    boolean oldSerializeNulls = writer.getSerializeNulls();

    writer.setHtmlSafe(gson.htmlSafe);
    writer.setSerializeNulls(gson.serializeNulls);

    if (this.gson.strictness != null) {
      writer.setStrictness(this.gson.strictness);
    } else if (writer.getStrictness() == Strictness.LEGACY_STRICT) {
      // For backward compatibility change to LENIENT if writer has default strictness LEGACY_STRICT
      writer.setStrictness(Strictness.LENIENT);
    }

    try {
      Streams.write(jsonElement, writer);
    } catch (IOException e) {
      throw new JsonIOException(e);
    } catch (AssertionError e) {
      throw new AssertionError(
          "AssertionError (GSON " + GsonBuildConfig.VERSION + "): " + e.getMessage(), e);
    } finally {
      writer.setStrictness(oldStrictness);
      writer.setHtmlSafe(oldHtmlSafe);
      writer.setSerializeNulls(oldSerializeNulls);
    }
  }
}
