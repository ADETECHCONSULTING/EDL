package fr.atraore.edl.data.models.data

import fr.atraore.edl.data.models.entity.EquipmentReference

data class TreeNode(val name: String, val children: MutableList<TreeNode> = mutableListOf())

object TreeParser {

    fun buildHierarchy(results: List<EquipmentReference>): TreeNode {
        val root = TreeNode("Root")

        // Create a map to keep track of created nodes at level one and two to avoid duplication
        val levelOneMap = mutableMapOf<String, TreeNode>()
        val levelTwoMap = mutableMapOf<String, TreeNode>()

        results.forEach { eqpRef ->
            val firstLevelNode = levelOneMap.getOrPut(eqpRef.level1) {
                TreeNode(eqpRef.level1).also { root.children.add(it) }
            }

            val secondLevelKey = "${eqpRef.level1}->${eqpRef.level2}"
            val secondLevelNode = levelTwoMap.getOrPut(secondLevelKey) {
                TreeNode(eqpRef.level2).also { firstLevelNode.children.add(it) }
            }

            eqpRef.level3?.let {
                secondLevelNode.children.add(TreeNode(it))
            }
        }

        return root
    }

}