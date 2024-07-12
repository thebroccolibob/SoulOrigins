@file:Suppress("unused")

package io.github.thebroccolibob.soulorigins.datagen.lib

import net.minecraft.block.Block
import net.minecraft.data.client.*
import net.minecraft.data.client.VariantSettings.Rotation
import net.minecraft.item.Item
import net.minecraft.state.property.Property
import net.minecraft.util.Identifier

val Block.modelId: Identifier
    get() = ModelIds.getBlockModelId(this)

fun Block.modelId(suffix: String): Identifier = ModelIds.getBlockSubModelId(this, suffix)

val Item.modelId: Identifier
    get() = ModelIds.getItemModelId(this)

fun Item.modelId(suffix: String): Identifier = ModelIds.getItemSubModelId(this, suffix)

val Block.textureId: Identifier
    get() = TextureMap.getId(this)

fun Block.textureId(suffix: String): Identifier = TextureMap.getSubId(this, suffix)

val Item.textureId: Identifier
    get() = TextureMap.getId(this)

fun Item.textureId(suffix: String): Identifier = TextureMap.getSubId(this, suffix)

fun BlockStateModelGenerator.coordinateVariants(block: Block, map: BlockStateVariantMap) {
    this.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(map))
}

// Variant Coordinations
// This should probably go in some sort of library

fun <T1: Comparable<T1>> BlockStateModelGenerator.registerStates(block: Block, property1: Property<T1>, variantFactory: (T1) -> BlockStateVariant) {
    coordinateVariants(block, BlockStateVariantMap.create(property1).register(variantFactory))
}

fun <T1: Comparable<T1>> BlockStateModelGenerator.registerVariantStates(block: Block, property1: Property<T1>, variantFactory: (T1) -> List<BlockStateVariant>) {
    coordinateVariants(block, BlockStateVariantMap.create(property1).registerVariants(variantFactory))
}

fun <T1: Comparable<T1>, T2: Comparable<T2>> BlockStateModelGenerator.registerStates(block: Block, property1: Property<T1>, property2: Property<T2>, variantFactory: (T1, T2) -> BlockStateVariant) {
    coordinateVariants(block, BlockStateVariantMap.create(property1, property2).register(variantFactory))
}

fun <T1: Comparable<T1>, T2: Comparable<T2>> BlockStateModelGenerator.registerVariantStates(block: Block, property1: Property<T1>, property2: Property<T2>, variantFactory: (T1, T2) -> List<BlockStateVariant>) {
    coordinateVariants(block, BlockStateVariantMap.create(property1, property2).registerVariants(variantFactory))
}

fun <T1: Comparable<T1>, T2: Comparable<T2>, T3: Comparable<T3>> BlockStateModelGenerator.registerStates(block: Block, property1: Property<T1>, property2: Property<T2>, property3: Property<T3>, variantFactory: (T1, T2, T3) -> BlockStateVariant) {
    coordinateVariants(block, BlockStateVariantMap.create(property1, property2, property3).register(variantFactory))
}

fun <T1: Comparable<T1>, T2: Comparable<T2>, T3: Comparable<T3>> BlockStateModelGenerator.registerVariantStates(block: Block, property1: Property<T1>, property2: Property<T2>, property3: Property<T3>, variantFactory: (T1, T2, T3) -> List<BlockStateVariant>) {
    coordinateVariants(block, BlockStateVariantMap.create(property1, property2, property3).registerVariants(variantFactory))
}

fun <T1: Comparable<T1>, T2: Comparable<T2>, T3: Comparable<T3>, T4: Comparable<T4>> BlockStateModelGenerator.registerStates(block: Block, property1: Property<T1>, property2: Property<T2>, property3: Property<T3>, property4: Property<T4>, variantFactory: (T1, T2, T3, T4) -> BlockStateVariant) {
    coordinateVariants(block, BlockStateVariantMap.create(property1, property2, property3, property4).register(variantFactory))
}

fun <T1: Comparable<T1>, T2: Comparable<T2>, T3: Comparable<T3>, T4: Comparable<T4>> BlockStateModelGenerator.registerVariantStates(block: Block, property1: Property<T1>, property2: Property<T2>, property3: Property<T3>, property4: Property<T4>, variantFactory: (T1, T2, T3, T4) -> List<BlockStateVariant>) {
    coordinateVariants(block, BlockStateVariantMap.create(property1, property2, property3, property4).registerVariants(variantFactory))
}

fun <T1: Comparable<T1>, T2: Comparable<T2>, T3: Comparable<T3>, T4: Comparable<T4>, T5: Comparable<T5>> BlockStateModelGenerator.registerStates(block: Block, property1: Property<T1>, property2: Property<T2>, property3: Property<T3>, property4: Property<T4>, property5: Property<T5>, variantFactory: (T1, T2, T3, T4, T5) -> BlockStateVariant) {
    coordinateVariants(block, BlockStateVariantMap.create(property1, property2, property3, property4, property5).register(variantFactory))
}

fun <T1: Comparable<T1>, T2: Comparable<T2>, T3: Comparable<T3>, T4: Comparable<T4>, T5: Comparable<T5>> BlockStateModelGenerator.registerVariantStates(block: Block, property1: Property<T1>, property2: Property<T2>, property3: Property<T3>, property4: Property<T4>, property5: Property<T5>, variantFactory: (T1, T2, T3, T4, T5) -> List<BlockStateVariant>) {
    coordinateVariants(block, BlockStateVariantMap.create(property1, property2, property3, property4, property5).registerVariants(variantFactory))
}

fun BlockStateModelGenerator.upload(model: Model, id: Identifier, textureMap: TextureMap): Identifier =
    model.upload(id, textureMap, this.modelCollector)

fun BlockStateModelGenerator.upload(model: Model, block: Block, textureMap: TextureMap): Identifier =
    upload(model, block.modelId, textureMap)

inline fun BlockStateModelGenerator.upload(model: Model, id: Identifier, textureMapBuilder: TextureMapBuilder.() -> Unit): Identifier =
    upload(model, id, TextureMapBuilder().apply(textureMapBuilder).result)

inline fun BlockStateModelGenerator.upload(model: Model, block: Block, textureMapBuilder: TextureMapBuilder.() -> Unit): Identifier =
    upload(model, block.modelId, textureMapBuilder)

fun BlockStateModelGenerator.upload(texturedModel: TexturedModel.Factory, block: Block): Identifier =
    texturedModel.upload(block, this.modelCollector)

fun BlockStateModelGenerator.upload(texturedModel: TexturedModel.Factory, block: Block, suffix: String): Identifier =
    texturedModel.upload(block, suffix, this.modelCollector)

@JvmInline
value class TextureMapBuilder(val result: TextureMap = TextureMap()) {

    infix fun TextureKey.to(id: Identifier) {
        result.put(this, id)
    }
}

fun BlockStateVariant(
    model: Identifier,
    x: Rotation? = null,
    y: Rotation? = null,
    uvLock: Boolean? = null,
    weight: Int? = null,
): BlockStateVariant = BlockStateVariant.create().apply {
    put(VariantSettings.MODEL, model)
    x?.let { put(VariantSettings.X, it) }
    y?.let { put(VariantSettings.Y, it) }
    uvLock?.let { put(VariantSettings.UVLOCK, it) }
    weight?.let { put(VariantSettings.WEIGHT, it) }
}
