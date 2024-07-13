package io.github.thebroccolibob.soulorigins;

import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.NotNull;

public interface IngredientSlotProvider {
    Slot replaceIngredientSlot(@NotNull Slot original);
}
