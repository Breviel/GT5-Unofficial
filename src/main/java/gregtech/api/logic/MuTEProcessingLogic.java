package gregtech.api.logic;

import static net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.gtnewhorizons.mutecore.MuTECore;
import com.gtnewhorizons.mutecore.api.data.ItemInputInventory;
import com.gtnewhorizons.mutecore.api.registry.MultiTileContainer.FakeEntity;
import com.gtnewhorizons.mutecore.shadow.dev.dominion.ecs.api.Entity;
import com.gtnewhorizons.mutecore.shadow.dev.dominion.ecs.api.Results;
import com.gtnewhorizons.mutecore.shadow.dev.dominion.ecs.api.Results.With1;

/**
 * Processing logic class, dedicated for MultiTileEntities.
 */
public abstract class MuTEProcessingLogic<P extends MuTEProcessingLogic<P>> implements Runnable {

    public final void run() {
        Results<? extends With1<?>> results = MuTECore.ENGINE.findEntitiesWith(getProcessingDataClass());
        for (With1<?> result : results) {
            Entity entity = result.entity();
            if (entity.has(FakeEntity.class)) continue;
            if (!validateEntityComponents(entity)) continue;
            process(entity);
        }
    }

    protected abstract Class<?> getProcessingDataClass();

    protected boolean validateEntityComponents(@Nonnull Entity entity) {
        return true;
    }

    // #region Logic

    @Nonnull
    public void process(@Nonnull Entity entity) {
    }

    // #endregion
}
