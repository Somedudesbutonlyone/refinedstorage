package com.raoulvdberge.refinedstorage.tile.data;

import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class TileDataParameter<T, E extends TileEntity> {
    private int id;
    private DataSerializer<T> serializer;
    private Function<E, T> valueProducer;
    @Nullable
    private BiConsumer<E, T> valueConsumer;
    @Nullable
    private Consumer<T> listener;
    private T value;

    public TileDataParameter(DataSerializer<T> serializer, T defaultValue, Function<E, T> producer) {
        this(serializer, defaultValue, producer, null);
    }

    public TileDataParameter(DataSerializer<T> serializer, T defaultValue, Function<E, T> producer, @Nullable BiConsumer<E, T> consumer) {
        this(serializer, defaultValue, producer, consumer, null);
    }

    public TileDataParameter(DataSerializer<T> serializer, T defaultValue, Function<E, T> producer, @Nullable BiConsumer<E, T> consumer, @Nullable Consumer<T> listener) {
        this.value = defaultValue;
        this.serializer = serializer;
        this.valueProducer = producer;
        this.valueConsumer = consumer;
        this.listener = listener;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public DataSerializer<T> getSerializer() {
        return serializer;
    }

    public Function<E, T> getValueProducer() {
        return valueProducer;
    }

    @Nullable
    public BiConsumer<E, T> getValueConsumer() {
        return valueConsumer;
    }

    public void setValue(T value) {
        this.value = value;

        if (listener != null) {
            listener.accept(value);
        }
    }

    public T getValue() {
        return value;
    }
}