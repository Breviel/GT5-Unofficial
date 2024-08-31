package gregtech.common.gui.modularui2.value;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.value.sync.ValueSyncHandler;
import com.google.common.io.ByteStreams;

import gregtech.api.gui.modularui2.CoverGuiData;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.ISerializableObject;

public class CoverDataSyncHandler extends ValueSyncHandler<ISerializableObject> {

    private final GT_CoverBehaviorBase<?> coverBehavior;
    private final Supplier<ISerializableObject> getter;
    private final Consumer<ISerializableObject> setter;
    private ISerializableObject cache;

    public CoverDataSyncHandler(GT_CoverBehaviorBase<?> coverBehavior, CoverGuiData guiData) {
        this.coverBehavior = coverBehavior;
        this.getter = guiData::getCoverData;
        this.setter = guiData::setCoverData;
        this.cache = this.getter.get();
    }

    @Override
    public void setValue(ISerializableObject value, boolean setSource, boolean sync) {
        this.cache = value;
        if (setSource) {
            this.setter.accept(value);
        }
        if (sync) {
            sync(0, this::write);
        }
    }

    @Override
    public boolean updateCacheFromSource(boolean isFirstSync) {
        if (isFirstSync || !this.getter.get()
            .equals(this.cache)) {
            setValue(this.getter.get(), false, false);
            return true;
        }
        return false;
    }

    @Override
    public void write(PacketBuffer buffer) throws IOException {
        getValue().writeToByteBuf(buffer);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void read(PacketBuffer buffer) throws IOException {
        ISerializableObject newObject = this.coverBehavior.createDataObject();
        newObject.readFromPacket(ByteStreams.newDataInput(buffer.array()), null);
        setValue(newObject, true, false);
    }

    @Override
    public ISerializableObject getValue() {
        return cache;
    }
}
