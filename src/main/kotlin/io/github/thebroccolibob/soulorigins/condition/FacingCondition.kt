package io.github.thebroccolibob.soulorigins.condition

import io.github.apace100.apoli.data.ApoliDataTypes
import io.github.apace100.apoli.power.factory.condition.ConditionFactory
import io.github.apace100.apoli.power.factory.condition.bientity.RelativeRotationCondition.RotationType
import io.github.apace100.apoli.util.Comparison
import io.github.apace100.calio.data.SerializableData
import io.github.apace100.calio.data.SerializableDataType
import io.github.apace100.calio.data.SerializableDataTypes
import io.github.thebroccolibob.soulorigins.*
import net.minecraft.entity.Entity
import net.minecraft.util.Pair
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import java.util.*

object FacingCondition {
    fun condition(data: SerializableData.Instance, pair: Pair<Entity, Entity>): Boolean {
        val (actor, target) = pair
        val actorRotation: RotationType = data["actor_rotation"]
        val axes = data.get<EnumSet<Direction.Axis>>("axes")
        val vec0 = reduceAxes(actorRotation.getRotation(pair.left), axes)
        val vec1 = reduceAxes(target.eyePos - actor.eyePos, axes)
        val angle = getAngleBetween(vec0, vec1)
        val comparison = data.get<Comparison>("comparison")
        val compareTo = data.getDouble("compare_to")
        return comparison.compare(angle, compareTo)
    }

    private fun getAngleBetween(a: Vec3d, b: Vec3d): Double {
        val dot = a.dotProduct(b)
        return dot / (a.length() * b.length())
    }

    private fun reduceAxes(vector: Vec3d, axesToKeep: EnumSet<Direction.Axis>): Vec3d {
        return Vec3d(
            if (axesToKeep.contains(Direction.Axis.X)) vector.x else 0.0,
            if (axesToKeep.contains(Direction.Axis.Y)) vector.y else 0.0,
            if (axesToKeep.contains(Direction.Axis.Z)) vector.z else 0.0
        )
    }

    val factory = ConditionFactory(
        SoulOrigins.id("facing"),
        SerializableData {
            add("axes", SerializableDataTypes.AXIS_SET, EnumSet.allOf(Direction.Axis::class.java))
            add("actor_rotation", SerializableDataType.enumValue(RotationType::class.java), RotationType.HEAD)
            add("comparison", ApoliDataTypes.COMPARISON)
            add("compare_to", SerializableDataTypes.DOUBLE)
        }, ::condition
    )
}
