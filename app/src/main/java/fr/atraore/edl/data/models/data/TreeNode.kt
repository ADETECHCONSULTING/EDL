package fr.atraore.edl.data.models.data

import fr.atraore.edl.data.models.entity.EquipmentReference
import java.util.UUID

data class TreeNode(val name: String, val id: String, val children: MutableList<TreeNode> = mutableListOf())

object TreeParser {

    fun buildHierarchy(results: List<EquipmentReference>): TreeNode {
        val root = TreeNode("Root", UUID.randomUUID().toString())

        // Create a map to keep track of created nodes at level one and two to avoid duplication
        val levelOneMap = mutableMapOf<String, TreeNode>()
        val levelTwoMap = mutableMapOf<String, TreeNode>()

        results.forEach { eqpRef ->
            val firstLevelNode = levelOneMap.getOrPut(eqpRef.level1) {
                TreeNode(eqpRef.level1, eqpRef.id).also { root.children.add(it) }
            }

            val secondLevelKey = "${eqpRef.level1}->${eqpRef.level2}"
            val secondLevelNode = levelTwoMap.getOrPut(secondLevelKey) {
                TreeNode(eqpRef.level2, eqpRef.id).also { firstLevelNode.children.add(it) }
            }

            if (eqpRef.level3 != null) {
                if (secondLevelNode.children.find { node -> node.name == eqpRef.level3 } == null) {
                    secondLevelNode.children.add(TreeNode(eqpRef.level3, eqpRef.id))
                }
            }
        }

        return root
    }

}