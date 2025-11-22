package com.example.mimuseo.components

import com.example.mimuseo.R
import com.meta.spatial.core.AttributePrimitive
import com.meta.spatial.core.BooleanAttribute
import com.meta.spatial.core.BooleanAttributeData
import com.meta.spatial.core.ComponentBase
import com.meta.spatial.core.ComponentCompanion
import com.meta.spatial.core.EnumAttribute
import com.meta.spatial.core.EnumAttributeData
import com.meta.spatial.core.RegisteredAttributeType
import com.meta.spatial.core.Vector3
import com.meta.spatial.core.Vector3Attribute
import com.meta.spatial.core.Vector3AttributeData
import com.meta.spatial.core.CB_EnableCaching

/**
 * A component which positions and orients the entity on the user's wrist.
 */
class WristAttached(
    position: Vector3 = Vector3(0.0f, 0.0f, 0.0f),
    rotation: Vector3 = Vector3(0.0f, 0.0f, 0.0f),
    side: HandSide = HandSide.LEFT,
    faceUser: Boolean = false,
): ComponentBase() {

    override var cachable = CB_EnableCaching
    /** The position offset to apply to the object, relative to the hand. */
    var position by Vector3Attribute("position", WristAttached.positionId, this, position)
    /** The rotation offset in euler angles to apply to the object, relative to the hand. */
    var rotation by Vector3Attribute("rotation", WristAttached.rotationId, this, rotation)
    var side by EnumAttribute("side", WristAttached.sideId, this, HandSide::class.java, side)
    /** Whether or not to orient the entity such that it faces the user (ignores the rotation offset). */
    var faceUser by BooleanAttribute("faceUser", WristAttached.faceUserId, this, faceUser)

    override fun typeID(): Int {
        return WristAttached.id
    }

    override fun companion(): ComponentCompanion {
        return WristAttached.Companion
    }

    override fun companionUnsafe(): ComponentCompanion? {
        return WristAttached.Companion
    }

    companion object : ComponentCompanion {
        override val id = R.id.wrist_attached_class
        val attributeTypeCounts_ = intArrayOf(2, 0, 6, 0)
        val attributeKeys_ = intArrayOf(R.id.wrist_attached_position, R.id.wrist_attached_rotation, R.id.wrist_attached_side, R.id.wrist_attached_face_user)
        val attributeTypes_ = intArrayOf(AttributePrimitive.VECTOR3.ordinal, AttributePrimitive.VECTOR3.ordinal, AttributePrimitive.INT.ordinal, AttributePrimitive.INT.ordinal)
        val enumClassesMap_ : Map<Int, Class<out Enum<*>>> = mapOf(
            R.id.wrist_attached_side to HandSide::class.java,
        )
        val keyStringToKeyIntMap_: Map<String, Int> = mapOf(
            "position" to R.id.wrist_attached_position,
            "rotation" to R.id.wrist_attached_rotation,
            "side" to R.id.wrist_attached_side,
            "faceUser" to R.id.wrist_attached_face_user,
        )
        val attrMetaData_: Map<Int, Pair<RegisteredAttributeType, String>> = mapOf(
            R.id.wrist_attached_position to Pair(RegisteredAttributeType.VECTOR3, "position"),
            R.id.wrist_attached_rotation to Pair(RegisteredAttributeType.VECTOR3, "rotation"),
            R.id.wrist_attached_side to Pair(RegisteredAttributeType.INT, "side"),
            R.id.wrist_attached_face_user to Pair(RegisteredAttributeType.INT, "faceUser"),
        )
        override fun attributeTypeCountAvailable(): Boolean {
            return true
        }
        override fun attributeTypeCounts(): IntArray {
            return attributeTypeCounts_
        }
        override fun attributeKeys(): IntArray {
            return attributeKeys_
        }
        override fun attributeTypes(): IntArray {
            return attributeTypes_
        }
        override fun attributeMetaData(): Map<Int, Pair<RegisteredAttributeType, String>> {
            return attrMetaData_
        }
        override fun enumClassesMap(): Map<Int, Class<out Enum<*>>> {
            return enumClassesMap_
        }
        override fun keyStringToKeyIntMap(keyString: String): Int? {
            return keyStringToKeyIntMap_[keyString]
        }
        override val createDefaultInstance = { WristAttached() }

        val positionId = R.id.wrist_attached_position
        val rotationId = R.id.wrist_attached_rotation
        val sideId = R.id.wrist_attached_side
        val faceUserId = R.id.wrist_attached_face_user
        val positionData = Vector3AttributeData(positionId)
        val rotationData = Vector3AttributeData(rotationId)
        val sideData = EnumAttributeData(sideId)
        val faceUserData = BooleanAttributeData(faceUserId)
    }
}
