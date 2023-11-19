package fr.atraore.edl.repository

import fr.atraore.edl.data.dao.DetailDao
import fr.atraore.edl.data.models.entity.Detail
import fr.atraore.edl.data.models.entity.ElementReference
import fr.atraore.edl.data.models.entity.RoomReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class DetailRepository @Inject constructor(
    private val detailDao: DetailDao
) : BaseRepository<Detail>(detailDao) {
    fun getDetailById(idEqp: String, idConstat: String, idLot: Int): Flow<Detail> = detailDao.getDetailById(idEqp, idConstat, idLot)

    suspend fun updateEtat(etat: String, idDetail: String) = detailDao.updateEtat(etat, idDetail)
    suspend fun updateProprete(propre: String, idDetail: String) = detailDao.updateProprete(propre, idDetail)
    suspend fun updateFonctionnement(fonctionnement: String, idDetail: String) = detailDao.updateFonctionnement(fonctionnement, idDetail)
    suspend fun updateImagesPaths(imagesPaths: String, idDetail: String) = detailDao.updateImagesPaths(imagesPaths, idDetail)

    fun getRoomDetails(idConstat: String) : Flow<Map<RoomReference, List<Detail>>> = detailDao.getRoomDetails(idConstat)
    fun getDetailByIdKeyAndConstat(idKey: Int, constatId: String): Flow<Detail> = detailDao.getDetailByIdKeyAndConstat(idKey, constatId)
    fun getDetailByIdOutdoorAndConstat(idOutdoorEquipement: Int, constatId: String): Flow<Detail> = detailDao.getDetailByIdOutdoorAndConstat(idOutdoorEquipement, constatId)
    fun getOrCreateDetails(elementsRefs: List<ElementReference>, clickedLot: Int, constatId: String, roomId: String): Flow<List<Detail>> {
        return detailDao.getDetailsByElementRefIdAndIdLotIdAndConstatId(elementsRefs.map { elementReference -> elementReference.elementReferenceId }, clickedLot, constatId)
            .map { details ->
                val existingElementIds = details.map { it.idElement }
                val newDetails = mutableListOf<Detail>()

                // Vérifier si un détail existe pour chaque élément
                for (elementRef in elementsRefs) {
                    if (elementRef.elementReferenceId !in existingElementIds) {
                        // Le détail n'existe pas, créer un nouveau détail
                        val newDetail = Detail(UUID.randomUUID().toString(), elementRef.elementReferenceId, roomId, constatId, clickedLot, elementRef.name)
                        newDetails.add(newDetail)
                    }
                }

                // Insérer les nouveaux détails dans la base de données
                if (newDetails.isNotEmpty()) {
                    detailDao.saveList(newDetails)
                }

                details + newDetails
            }
    }
}