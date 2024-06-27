package io.github.thebroccolibob.soulorigins.datagen

import io.github.thebroccolibob.soulorigins.SoulOriginsTags
import io.github.thebroccolibob.soulorigins.datagen.lib.inputs
import io.github.thebroccolibob.soulorigins.datagen.lib.itemCriterion
import io.github.thebroccolibob.soulorigins.datagen.lib.patterns
import io.github.thebroccolibob.soulorigins.datagen.lib.shaped
import io.github.thebroccolibob.soulorigins.item.SoulOriginsItems
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.recipe.book.RecipeCategory
import java.util.function.Consumer

fun Consumer<RecipeJsonProvider>.generateSoulOriginsRecipes() {
    shaped(RecipeCategory.TRANSPORTATION, SoulOriginsItems.ARTIFICER_COLUMN_BUILDER) {
        patterns("""
            #
            %
            #
        """)
        inputs {
            '#' to SoulOriginsTags.ARTIFICER_SECONDARY_MATERIAL
            '%' to SoulOriginsItems.ARTIFICER_CORE
        }
        itemCriterion(SoulOriginsItems.ARTIFICER_CORE)
    }

    shaped(RecipeCategory.TRANSPORTATION, SoulOriginsItems.ARTIFICER_PLATFORM_BUILDER) {
        patterns("""
             # 
            #%#
             # 
        """)
        inputs {
            '#' to SoulOriginsTags.ARTIFICER_SECONDARY_MATERIAL
            '%' to SoulOriginsItems.ARTIFICER_CORE
        }
        itemCriterion(SoulOriginsItems.ARTIFICER_CORE)
    }

    shaped(RecipeCategory.TRANSPORTATION, SoulOriginsItems.ARTIFICER_WALL_BUILDER) {
        patterns("""
            # #
             % 
            # #
        """)
        inputs {
            '#' to SoulOriginsTags.ARTIFICER_SECONDARY_MATERIAL
            '%' to SoulOriginsItems.ARTIFICER_COLUMN_BUILDER
        }
        itemCriterion(SoulOriginsItems.ARTIFICER_CORE)
    }
}

