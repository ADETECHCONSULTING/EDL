package fr.atraore.edl.data.models.data

import fr.atraore.edl.data.models.entity.EquipmentReference
import fr.atraore.edl.utils.ROOMS_LABELS
import java.util.UUID

data class TreeNode(val name: String, val id: String, val children: MutableList<TreeNode> = mutableListOf(), val idRoomRef: Int? = null)

object TreeParser {

    fun buildHierarchy(results: List<EquipmentReference>): TreeNode {
        val root = TreeNode("Root", UUID.randomUUID().toString())

        val levelZeroMap = mutableMapOf<Int?, TreeNode>() // Modification pour accepter des clés null
        val levelOneMap = mutableMapOf<String, TreeNode>()
        val levelTwoMap = mutableMapOf<String, TreeNode>()

        results.forEach { eqpRef ->
            // Gérer les cas où idRoomRef est null
            val parentId = (eqpRef.idRoomRef ?: 0) -1 // Utiliser -1 ou tout autre valeur par défaut
            val parentLabel = if (eqpRef.idRoomRef != null) {
                ROOMS_LABELS.getOrElse(eqpRef.idRoomRef - 1) { "Inconnu" }
            } else {
                "Parent Inconnu" // Label par défaut pour les parents sans idRoomRef
            }

            val zeroLevelNode = levelZeroMap.getOrPut(parentId) {
                TreeNode(parentLabel, UUID.randomUUID().toString()).also { root.children.add(it) }
            }

            val firstLevelNode = levelOneMap.getOrPut(eqpRef.level1) {
                TreeNode(eqpRef.level1, eqpRef.id, idRoomRef = eqpRef.idRoomRef).also { zeroLevelNode.children.add(it) }
            }

            val secondLevelKey = "${eqpRef.level1}->${eqpRef.level2}"
            val secondLevelNode = levelTwoMap.getOrPut(secondLevelKey) {
                TreeNode(eqpRef.level2, eqpRef.id, idRoomRef = eqpRef.idRoomRef).also { firstLevelNode.children.add(it) }
            }

            if (eqpRef.level3 != null) {
                if (secondLevelNode.children.none { node -> node.name == eqpRef.level3 }) {
                    secondLevelNode.children.add(TreeNode(eqpRef.level3, eqpRef.id, idRoomRef = eqpRef.idRoomRef))
                }
            }
        }

        return root
    }



}