package com.google.gson.stream;

import com.google.gson.FormattingStyle;
import com.google.gson.Strictness;
import java.io.Writer;

public class JsonWriterBuilder {

  private Writer out;
  private FormattingStyle formattingStyle;
  private boolean htmlSafe;
  private Strictness strictness;
  private boolean serializeNulls;

  public JsonWriterBuilder setFormattingStyle(FormattingStyle f) {
    this.formattingStyle = f;
    return this;
  }

  public JsonWriterBuilder setHtmlSafe(boolean b) {
    this.htmlSafe = b;
    return this;
  }

  public JsonWriterBuilder setStrictness(Strictness s) {
    this.strictness = s;
    return this;
  }

  public JsonWriterBuilder setSerializeNulls(boolean b) {
    this.serializeNulls = b;
    return this;
  }

  public JsonWriterBuilder setOut(Writer o) {
    this.out = o;
    return this;
  }

  public JsonWriter build() {
    return new JsonWriter(this.out, formattingStyle, htmlSafe, strictness, serializeNulls);
  }
}
