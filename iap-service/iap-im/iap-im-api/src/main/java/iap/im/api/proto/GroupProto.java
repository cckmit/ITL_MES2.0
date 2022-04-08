// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Group.proto

package iap.im.api.proto;

public final class GroupProto {
  private GroupProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface ModelOrBuilder extends
      // @@protoc_insertion_point(interface_extends:com.farsunset.lvxin.model.proto.Model)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>int64 id = 1;</code>
     */
    long getId();

    /**
     * <code>string name = 2;</code>
     */
    String getName();
    /**
     * <code>string name = 2;</code>
     */
    com.google.protobuf.ByteString
        getNameBytes();

    /**
     * <code>string founder = 3;</code>
     */
    String getFounder();
    /**
     * <code>string founder = 3;</code>
     */
    com.google.protobuf.ByteString
        getFounderBytes();

    /**
     * <code>string category = 4;</code>
     */
    String getCategory();
    /**
     * <code>string category = 4;</code>
     */
    com.google.protobuf.ByteString
        getCategoryBytes();

    /**
     * <code>string summary = 5;</code>
     */
    String getSummary();
    /**
     * <code>string summary = 5;</code>
     */
    com.google.protobuf.ByteString
        getSummaryBytes();
  }
  /**
   * Protobuf type {@code com.farsunset.lvxin.model.proto.Model}
   */
  public  static final class Model extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:com.farsunset.lvxin.model.proto.Model)
      ModelOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use Model.newBuilder() to construct.
    private Model(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private Model() {
      id_ = 0L;
      name_ = "";
      founder_ = "";
      category_ = "";
      summary_ = "";
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private Model(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new NullPointerException();
      }
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownFieldProto3(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {

              id_ = input.readInt64();
              break;
            }
            case 18: {
              String s = input.readStringRequireUtf8();

              name_ = s;
              break;
            }
            case 26: {
              String s = input.readStringRequireUtf8();

              founder_ = s;
              break;
            }
            case 34: {
              String s = input.readStringRequireUtf8();

              category_ = s;
              break;
            }
            case 42: {
              String s = input.readStringRequireUtf8();

              summary_ = s;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return GroupProto.INTERNAL_STATIC_COM_FARSUNSET_LVXIN_MODEL_PROTO_MODEL_DESCRIPTOR;
    }

    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return GroupProto.INTERNAL_STATIC_COM_FARSUNSET_LVXIN_MODEL_PROTO_MODEL_FIELDACCESSORTABLE
          .ensureFieldAccessorsInitialized(
              Model.class, Builder.class);
    }

    public static final int ID_FIELD_NUMBER = 1;
    private long id_;
    /**
     * <code>int64 id = 1;</code>
     */
    public long getId() {
      return id_;
    }

    public static final int NAME_FIELD_NUMBER = 2;
    private volatile Object name_;
    /**
     * <code>string name = 2;</code>
     */
    public String getName() {
      Object ref = name_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        name_ = s;
        return s;
      }
    }
    /**
     * <code>string name = 2;</code>
     */
    public com.google.protobuf.ByteString
        getNameBytes() {
      Object ref = name_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int FOUNDER_FIELD_NUMBER = 3;
    private volatile Object founder_;
    /**
     * <code>string founder = 3;</code>
     */
    public String getFounder() {
      Object ref = founder_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        founder_ = s;
        return s;
      }
    }
    /**
     * <code>string founder = 3;</code>
     */
    public com.google.protobuf.ByteString
        getFounderBytes() {
      Object ref = founder_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        founder_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int CATEGORY_FIELD_NUMBER = 4;
    private volatile Object category_;
    /**
     * <code>string category = 4;</code>
     */
    public String getCategory() {
      Object ref = category_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        category_ = s;
        return s;
      }
    }
    /**
     * <code>string category = 4;</code>
     */
    public com.google.protobuf.ByteString
        getCategoryBytes() {
      Object ref = category_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        category_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int SUMMARY_FIELD_NUMBER = 5;
    private volatile Object summary_;
    /**
     * <code>string summary = 5;</code>
     */
    public String getSummary() {
      Object ref = summary_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        summary_ = s;
        return s;
      }
    }
    /**
     * <code>string summary = 5;</code>
     */
    public com.google.protobuf.ByteString
        getSummaryBytes() {
      Object ref = summary_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        summary_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) {
        return true;
      }
      if (isInitialized == 0) {
        return false;
      }

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (id_ != 0L) {
        output.writeInt64(1, id_);
      }
      if (!getNameBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 2, name_);
      }
      if (!getFounderBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 3, founder_);
      }
      if (!getCategoryBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 4, category_);
      }
      if (!getSummaryBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 5, summary_);
      }
      unknownFields.writeTo(output);
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) {
        return size;
      }

      size = 0;
      if (id_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(1, id_);
      }
      if (!getNameBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, name_);
      }
      if (!getFounderBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, founder_);
      }
      if (!getCategoryBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, category_);
      }
      if (!getSummaryBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(5, summary_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof Model)) {
        return super.equals(obj);
      }
      Model other = (Model) obj;

      boolean result = true;
      result = result && (getId()
          == other.getId());
      result = result && getName()
          .equals(other.getName());
      result = result && getFounder()
          .equals(other.getFounder());
      result = result && getCategory()
          .equals(other.getCategory());
      result = result && getSummary()
          .equals(other.getSummary());
      result = result && unknownFields.equals(other.unknownFields);
      return result;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + ID_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getId());
      hash = (37 * hash) + NAME_FIELD_NUMBER;
      hash = (53 * hash) + getName().hashCode();
      hash = (37 * hash) + FOUNDER_FIELD_NUMBER;
      hash = (53 * hash) + getFounder().hashCode();
      hash = (37 * hash) + CATEGORY_FIELD_NUMBER;
      hash = (53 * hash) + getCategory().hashCode();
      hash = (37 * hash) + SUMMARY_FIELD_NUMBER;
      hash = (53 * hash) + getSummary().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      return hash;
    }

    public static Model parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static Model parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static Model parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static Model parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static Model parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static Model parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static Model parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static Model parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static Model parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static Model parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static Model parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static Model parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(Model prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code com.farsunset.lvxin.model.proto.Model}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:com.farsunset.lvxin.model.proto.Model)
        ModelOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return GroupProto.INTERNAL_STATIC_COM_FARSUNSET_LVXIN_MODEL_PROTO_MODEL_DESCRIPTOR;
      }

      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return GroupProto.INTERNAL_STATIC_COM_FARSUNSET_LVXIN_MODEL_PROTO_MODEL_FIELDACCESSORTABLE
            .ensureFieldAccessorsInitialized(
                Model.class, Builder.class);
      }

      // Construct using com.farsunset.lvxin.model.proto.GroupProto.Model.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        id_ = 0L;

        name_ = "";

        founder_ = "";

        category_ = "";

        summary_ = "";

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return GroupProto.INTERNAL_STATIC_COM_FARSUNSET_LVXIN_MODEL_PROTO_MODEL_DESCRIPTOR;
      }

      public Model getDefaultInstanceForType() {
        return Model.getDefaultInstance();
      }

      public Model build() {
        Model result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public Model buildPartial() {
        Model result = new Model(this);
        result.id_ = id_;
        result.name_ = name_;
        result.founder_ = founder_;
        result.category_ = category_;
        result.summary_ = summary_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof Model) {
          return mergeFrom((Model)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(Model other) {
        if (other == Model.getDefaultInstance()) {
          return this;
        }
        if (other.getId() != 0L) {
          setId(other.getId());
        }
        if (!other.getName().isEmpty()) {
          name_ = other.name_;
          onChanged();
        }
        if (!other.getFounder().isEmpty()) {
          founder_ = other.founder_;
          onChanged();
        }
        if (!other.getCategory().isEmpty()) {
          category_ = other.category_;
          onChanged();
        }
        if (!other.getSummary().isEmpty()) {
          summary_ = other.summary_;
          onChanged();
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        Model parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (Model) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private long id_ ;
      /**
       * <code>int64 id = 1;</code>
       */
      public long getId() {
        return id_;
      }
      /**
       * <code>int64 id = 1;</code>
       */
      public Builder setId(long value) {
        
        id_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int64 id = 1;</code>
       */
      public Builder clearId() {
        
        id_ = 0L;
        onChanged();
        return this;
      }

      private Object name_ = "";
      /**
       * <code>string name = 2;</code>
       */
      public String getName() {
        Object ref = name_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          name_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>string name = 2;</code>
       */
      public com.google.protobuf.ByteString
          getNameBytes() {
        Object ref = name_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          name_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string name = 2;</code>
       */
      public Builder setName(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        name_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>string name = 2;</code>
       */
      public Builder clearName() {
        
        name_ = getDefaultInstance().getName();
        onChanged();
        return this;
      }
      /**
       * <code>string name = 2;</code>
       */
      public Builder setNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        name_ = value;
        onChanged();
        return this;
      }

      private Object founder_ = "";
      /**
       * <code>string founder = 3;</code>
       */
      public String getFounder() {
        Object ref = founder_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          founder_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>string founder = 3;</code>
       */
      public com.google.protobuf.ByteString
          getFounderBytes() {
        Object ref = founder_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          founder_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string founder = 3;</code>
       */
      public Builder setFounder(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        founder_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>string founder = 3;</code>
       */
      public Builder clearFounder() {
        
        founder_ = getDefaultInstance().getFounder();
        onChanged();
        return this;
      }
      /**
       * <code>string founder = 3;</code>
       */
      public Builder setFounderBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        founder_ = value;
        onChanged();
        return this;
      }

      private Object category_ = "";
      /**
       * <code>string category = 4;</code>
       */
      public String getCategory() {
        Object ref = category_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          category_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>string category = 4;</code>
       */
      public com.google.protobuf.ByteString
          getCategoryBytes() {
        Object ref = category_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          category_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string category = 4;</code>
       */
      public Builder setCategory(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        category_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>string category = 4;</code>
       */
      public Builder clearCategory() {
        
        category_ = getDefaultInstance().getCategory();
        onChanged();
        return this;
      }
      /**
       * <code>string category = 4;</code>
       */
      public Builder setCategoryBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        category_ = value;
        onChanged();
        return this;
      }

      private Object summary_ = "";
      /**
       * <code>string summary = 5;</code>
       */
      public String getSummary() {
        Object ref = summary_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          summary_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>string summary = 5;</code>
       */
      public com.google.protobuf.ByteString
          getSummaryBytes() {
        Object ref = summary_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          summary_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string summary = 5;</code>
       */
      public Builder setSummary(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        summary_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>string summary = 5;</code>
       */
      public Builder clearSummary() {
        
        summary_ = getDefaultInstance().getSummary();
        onChanged();
        return this;
      }
      /**
       * <code>string summary = 5;</code>
       */
      public Builder setSummaryBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        summary_ = value;
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFieldsProto3(unknownFields);
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:com.farsunset.lvxin.model.proto.Model)
    }

    // @@protoc_insertion_point(class_scope:com.farsunset.lvxin.model.proto.Model)
    private static final Model DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new Model();
    }

    public static Model getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<Model>
        PARSER = new com.google.protobuf.AbstractParser<Model>() {
      public Model parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Model(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<Model> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<Model> getParserForType() {
      return PARSER;
    }

    public Model getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
          INTERNAL_STATIC_COM_FARSUNSET_LVXIN_MODEL_PROTO_MODEL_DESCRIPTOR;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          INTERNAL_STATIC_COM_FARSUNSET_LVXIN_MODEL_PROTO_MODEL_FIELDACCESSORTABLE;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\013Group.proto\022\037com.farsunset.lvxin.model" +
      ".proto\"U\n\005Model\022\n\n\002id\030\001 \001(\003\022\014\n\004name\030\002 \001(" +
      "\t\022\017\n\007founder\030\003 \001(\t\022\020\n\010category\030\004 \001(\t\022\017\n\007" +
      "summary\030\005 \001(\tB\014B\nGroupProtob\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    INTERNAL_STATIC_COM_FARSUNSET_LVXIN_MODEL_PROTO_MODEL_DESCRIPTOR =
      getDescriptor().getMessageTypes().get(0);
    INTERNAL_STATIC_COM_FARSUNSET_LVXIN_MODEL_PROTO_MODEL_FIELDACCESSORTABLE = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            INTERNAL_STATIC_COM_FARSUNSET_LVXIN_MODEL_PROTO_MODEL_DESCRIPTOR,
        new String[] { "Id", "Name", "Founder", "Category", "Summary", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}