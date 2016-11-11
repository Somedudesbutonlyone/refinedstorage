package com.raoulvdberge.refinedstorage.apiimpl.network.readerwriter;

import com.raoulvdberge.refinedstorage.api.network.INetworkMaster;
import com.raoulvdberge.refinedstorage.api.network.readerwriter.*;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import com.raoulvdberge.refinedstorage.tile.IReaderWriter;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReaderWriterChannel implements IReaderWriterChannel {
    private static final String NBT_HANDLER = "Handler_%s";

    private String name;
    private INetworkMaster network;

    private List<IReaderWriterHandler> handlers = new ArrayList<>();

    public ReaderWriterChannel(String name, INetworkMaster network) {
        this.name = name;
        this.network = network;
        this.handlers.addAll(API.instance().getReaderWriterHandlerRegistry().getFactories().stream().map(f -> f.create(null)).collect(Collectors.toList()));
    }

    @Override
    public List<IReaderWriterHandler> getHandlers() {
        return handlers;
    }

    @Override
    public List<IReader> getReaders() {
        return network.getNodeGraph().all().stream()
            .filter(n -> n instanceof IReader && n instanceof IReaderWriter && name.equals(((IReaderWriter) n).getChannel()))
            .map(n -> (IReader) n)
            .collect(Collectors.toList());
    }

    @Override
    public List<IWriter> getWriters() {
        return network.getNodeGraph().all().stream()
            .filter(n -> n instanceof IWriter && n instanceof IReaderWriter && name.equals(((IReaderWriter) n).getChannel()))
            .map(n -> (IWriter) n)
            .collect(Collectors.toList());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        for (IReaderWriterHandler handler : handlers) {
            tag.setTag(String.format(NBT_HANDLER, handler.getId()), handler.writeToNBT(new NBTTagCompound()));
        }

        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        for (IReaderWriterHandler handler : handlers) {
            String id = String.format(NBT_HANDLER, handler.getId());

            if (tag.hasKey(id)) {
                IReaderWriterHandlerFactory factory = API.instance().getReaderWriterHandlerRegistry().getFactory(id);

                if (factory != null) {
                    handlers.add(factory.create(tag.getCompoundTag(id)));
                }
            }
        }
    }
}