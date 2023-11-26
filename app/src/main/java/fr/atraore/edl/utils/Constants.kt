package fr.atraore.edl.utils

val POSITION_FRAGMENT_BIENS = 0
val POSITION_FRAGMENT_PROPRIETAIRE = 1
val POSITION_FRAGMENT_LOCATAIRE = 2
val POSITION_FRAGMENT_MANDATAIRE = 3
val POSITION_FRAGMENT_AGENCES = 4
val POSITION_FRAGMENT_USER = 5

val ARGS_TAB_POSITION = "tabPosition"
val ARGS_CONSTAT_ID = "constatId"
val ARGS_TENANT_ID = "tenantItemId"
val ARGS_OWNER_ID = "ownerItemId"
val ARGS_PROPERTY_ID = "propertyItemId"
val ARGS_CONTRACTOR_ID = "contractorItemId"
val ARGS_USER_ID = "userItemId"
val ARGS_AGENCY_ID = "agencyItemId"
val ARGS_CONSTAT = "constat"
val ARGS_ALREADY_ADDED = "alreadyAdded"

val PROPERTY_LABEL = "Biens"
val OWNER_LABEL = "Propriétaires"
val CONTRACTOR_LABEL = "Mandataires"
val TENANT_LABEL = "Locataires"
val AGENCY_LABEL = "Agences"
val USER_LABEL = "Utilisateurs"
val SUITE_CONSTAT_LABEL = "Suite du constat"
val ELEMENT_CONFIG_LABEL = "Ecran de configuration des elements"
val GRID_CONFIG_LABEL = "Ecran de configuration des elements"
val PDF_GENERATOR = "Ecran de génération du PDF"

val EN_SERVICE_LABEL = "En Service"
val COUPE_LABEL = "Coupé"
val EQUIPE_LABEL = "Equipé"
val NON_EQUIPE_LABEL = "Non équipé"

val IMAGES_FOLDER_NAME = "Constat-images"

val ROOMS_LABELS = arrayOf(
    "ACCES / ENTREE",
    "Pièce(s) humide(s)",
    "CUISINE",
    "SEJOUR",
    "CHAMBRE",
    "SALLE DE BAINS",
    "WC",
    "GARAGE",
    "Salle d'eau",
    "Jardin",
    "Salle de douche",
    "Extérieurs",
    "Escalier",
    "BUANDERIE",
    "GRENIER",
    "CAVE",
    "POOL HOUSE",
    "DRESSING",
    "MEZZANINE",
    "CABANON",
    "LOCAL COMMERCIAL - FACADES ET EXTERIEURS",
    "LOCAL COMMERCIAL - ACCUEIL",
    "LOCAL COMMERCIAL - PIECE",
    "LOCAL COMMERCIAL - CUISINE",
    "LOCAL COMMERCIAL - SANITAIRES"
)

val ELEMENTS_REVETEMENTS_LABELS = arrayOf(
    "Murs",
    "Plafond",
    "Plinthes",
    "Sol"
)

val ELEMENTS_OUVRANTS_LABELS = arrayOf(
    listOf("Porte paliere", "Plaque poignée", "Judas", "Autre équipements", "Serrure", "Verrou", "Sonnette", "Portier audio vidéo"),
    listOf("Fenêtre", "Fenêtre", "Porte fenêtre", "Vitrage", "Garde corps", "Equipements", "Barraudage anti effraction"),
    listOf("Volet", "Accordeon", "Battant", "Coulissant", "Jalousie", "Roulant Manuel", "Roulant Electrique", "Telecommande", "Bois", "Metal", "PVC"),
    listOf("Velux", "Vitrage", "Equipement"),
    listOf("Balcon Loggia", "Garde Corps", "Robinet de puisage", "Store banne", "Equipement", "Interrupteur", "Prise electrique", "Eclairage murale", "Eclairage plafond"),
    listOf("Terrage RDJ", "Garde Corps", "Robinet de puisage", "Store banne", "Equipement", "Interrupteur", "Prise electrique", "Eclairage murale", "Eclairage plafond" ),
    "Pelouse",
    "Haies"
)

val ELEMENTS_ELEC_LABELS = arrayOf(
    "Interrupteur",
    "Prise électrisue NC",
    "Prise four 32 A NC",
    "Prise téléphonique NC",
    "Prise TV NC",
    "Cable TV Volant NC",
    "Prise RJ45",
    "Prise numérique",
    "Prise fibre",
    "Eclairage murale",
    "Eclairage plafond",
    "Tableau éléctrique",
    "DAAF Détecteur de fumée"
)

val ELEMENTS_PLOMBERIE_LABELS = arrayOf(
    listOf("Evier/Lavabo", "Robinneterie", "Bonde", "Siphon", "Joint"),
    listOf("Douche/Baignoire", "Robinneterie", "Flexible/Douchette", "Bonde", "Siphon", "Pare douche/Baignoire"),
    listOf("Bidet", "Robinneterie", "Bonde"),
    "Robinet machine à laver",
    listOf("Robinet de puisage", "Cumulus", "Chaudière")
)

val ELEMENTS_CHAUFFAGE_LABELS = arrayOf(
    "Electrique",
    "Radiateur NC",
    "Clim NC",
    "Thermostat Ambiance NC",
    "Aerotherme",
    "Ventilation"
)

val ELEMENTS_ELECTRONIQUE_LABELS = arrayOf(
    "Plaque de cuisson",
    "Four",
    "Four micro onde",
    "Cusinière gazinière",
    "Hotte aspirante",
    "Réfrigérateur",
    "Lave vaisselle",
    "Lave linge",
    "Sèche linge"
)

val ELEMENTS_RANGEMENT_LABELS = arrayOf(
    "Placard",
    "Equipements",
    "Cheminée",
    "Accessoires/Autres"
)

val LOTS_LABELS = arrayOf(
    "Revetement",
    "Ouvrants",
    "Electricite",
    "Plomberie",
    "Chauffage/Climatisation",
    "Electromenager",
    "Rangements/Mobilier",
    "Meublé"
)

val COMPTEUR_LABELS = listOf(
    "Compteur d'eau froide",
    "Compteur d'électricité",
    "Détecteur de fumée",
    "Compteur d'eau chaude",
    "Compteur Gaz",
    "Cuve à fuel / gaz"
)

val COMPTEUR_LABELS_LIGHT = listOf(
    "Compteur d'eau chaude",
    "Compteur Gaz",
    "Cuve à fuel / gaz"
)

enum class IdDetailStatesEnum(val value: Int) {
    ETAT(1),
    PROPRETE(2),
    DESCRIPTIF(3),
    ALTERATION(4),
}

val KEYS_LABELS = listOf(
    "Carte magnétique",
    "Badge magnétique",
    "Clé multifonction",
    "Clé non identifiée",
    "Clé porte palière",
    "Clé verrou palière haut",
    "Clé verrou palière bas",
    "Clé immeuble",
    "Clé boite aux lettres",
    "Clé accès aux communs",
    "Clé cave",
    "Clé garage",
    "Clé local poubelle",
    "Clé local vélo",
    "Clé arceau",
    "Clé ascenseur",
    "Clé portillon",
    "Clé portail",
    "Clé piscine",
    "Clé volet",
    "Clé rideau(x) métallique(s)",
    "Clé alarme",
    "Clé coffre-fort",
    "Clé abri jardin",
    "Carte de reproduction",
    "Bracelet"
)

val OUTDOORS_EQUIPMNT_LABELS = listOf(
    "Boite aux lettres",
    "Serrure boite aux lettres",
    "Porte garage / box",
    "Serrure garage / box"
)

val PROPRETE_LABELS = listOf(
    "Propre",
    "Poussière",
    "Gras",
    "Non nettoyé"
)