package com.example.data.model

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val nativeGreeting: String,
    val flag: String,
    val region: String
) {
    FRENCH("fr", "Français", "Français", "Bienvenue sur KOMDÉ", "🇧🇫", "Langue officielle"),
    MOORE("mos", "Mòoré", "Mòoré", "Ne y beeoogo / Y paama", "🇧🇫", "Burkina Faso"),
    BAMBARA("bm", "Bamanankan", "Bamanankan", "I ni ce / Sambe sambe", "🇲🇱", "Mali • Afrique de l'Ouest"),
    HAUSA("ha", "Hausa", "Harshen Hausa", "Sannu da zuwa", "🇳🇪", "Niger • Nigeria"),
    WOLOF("wo", "Wolof", "Wolof", "Dalal ak jamm", "🇸🇳", "Sénégal"),
    ENGLISH("en", "English", "English", "Welcome to KOMDÉ", "🌍", "International")
}

data class LocalizedStrings(
    val slogan: String,
    val chats: String,
    val communities: String,
    val status: String,
    val contacts: String,
    val profile: String,
    val dataSaver: String,
    val dataSaverSub: String,
    val searchPlaceholder: String,
    val writeMessage: String,
    val online: String,
    val offline: String,
    val recordingVoice: String,
    val voiceSpeed: String,
    val smartReplyPrompt: String,
    val aiAssistantName: String,
    val creatorTitle: String
) {
    val appSlogan: String get() = slogan
}

object LanguageDictionary {
    fun getStrings(lang: AppLanguage): LocalizedStrings {
        return when (lang) {
            AppLanguage.MOORE -> LocalizedStrings(
                slogan = "Gom-bãngre • Puusgo • Tãngre",
                chats = "Gom-biis (Discussions)",
                communities = "Tigs-n-taase (Groupes)",
                status = "Kiba-paala (Statuts)",
                contacts = "Tond ramba (Contacts)",
                profile = "Yɩlem-sebre (Profil)",
                dataSaver = "Meg-f data (Économie Data)",
                dataSaverSub = "Kʋɩlem mega la sɩd-bãngre",
                searchPlaceholder = "Bãng n bao gomde, ned...",
                writeMessage = "Gom n togs ka...",
                online = "Bɩla (En ligne)",
                offline = "Ka bɩla (Hors ligne)",
                recordingVoice = "N wẽeda koɛɛga...",
                voiceSpeed = "Soodo",
                smartReplyPrompt = "Mòoré leokre",
                aiAssistantName = "KOMDÉ Burkindi IA",
                creatorTitle = "Tʋʋmd tʋmtda & seb-gʋlsda"
            )
            AppLanguage.BAMBARA -> LocalizedStrings(
                slogan = "Kuma • Tla • Jɛgɛɲɔgɔnya",
                chats = "Barow (Discussions)",
                communities = "Kuluw (Groupes)",
                status = "Kibaruw (Statuts)",
                contacts = "Mɔgɔw (Contacts)",
                profile = "I yɛrɛ (Profil)",
                dataSaver = "Donan mara (Économie Data)",
                dataSaverSub = "Donan kɛlɛli kɛlɛkɛlɛ",
                searchPlaceholder = "Fɛn bɛɛ ɲini...",
                writeMessage = "Kuma dɔ sɛbɛn yan...",
                online = "A bɛ yen (En ligne)",
                offline = "A tɛ yen (Hors ligne)",
                recordingVoice = "Kan taabolo...",
                voiceSpeed = "Teliya",
                smartReplyPrompt = "Bamanankan jaabi",
                aiAssistantName = "KOMDÉ Burkindi IA",
                creatorTitle = "Baarakɛla & Dɛsɛnyɔnna"
            )
            AppLanguage.HAUSA -> LocalizedStrings(
                slogan = "Sadarwa • Raba • Haɗawa",
                chats = "Hirarraki (Discussions)",
                communities = "Ƙungiyoyi (Groupes)",
                status = "Labarai (Statuts)",
                contacts = "Abokan hulɗa (Contacts)",
                profile = "Bayanin martaba (Profil)",
                dataSaver = "Tattalin Bayanai (Data Saver)",
                dataSaverSub = "Rage yawan amfani da intanet",
                searchPlaceholder = "Bincika sakonni, mutane...",
                writeMessage = "Rubuta sako anan...",
                online = "Yana nan (En ligne)",
                offline = "Baya nan (Hors ligne)",
                recordingVoice = "Yin rikodin sauti...",
                voiceSpeed = "Sauri",
                smartReplyPrompt = "Amsa cikin Hausa",
                aiAssistantName = "KOMDÉ Burkindi IA",
                creatorTitle = "Mai kirkira & Injiniya"
            )
            AppLanguage.WOLOF -> LocalizedStrings(
                slogan = "Waxtaan • Séddale • Jokkoo",
                chats = "Waxtaan yi (Discussions)",
                communities = "Mbootaay yi (Groupes)",
                status = "Xibaar yi (Statuts)",
                contacts = "Xarit yi (Contacts)",
                profile = "Sa bopp (Profil)",
                dataSaver = "Denc ay data (Économie)",
                dataSaverSub = "Waññi ko am solo ci kow réseau",
                searchPlaceholder = "Seet mbir mi...",
                writeMessage = "Bindal sa bataaxal...",
                online = "Mu ngi fi (En ligne)",
                offline = "Nekkul fi (Hors ligne)",
                recordingVoice = "Mu ngi jël baat...",
                voiceSpeed = "Gaawaay",
                smartReplyPrompt = "Tontu ci Wolof",
                aiAssistantName = "KOMDÉ Burkindi IA",
                creatorTitle = "Kiy sàkk té xalaat"
            )
            AppLanguage.ENGLISH -> LocalizedStrings(
                slogan = "Communicate • Share • Connect",
                chats = "Chats",
                communities = "Communities",
                status = "Status",
                contacts = "Contacts",
                profile = "Profile",
                dataSaver = "Data Saver",
                dataSaverSub = "Adaptive media compression & low bandwidth",
                searchPlaceholder = "Search messages, people, groups...",
                writeMessage = "Write a message...",
                online = "Online",
                offline = "Offline",
                recordingVoice = "Recording voice...",
                voiceSpeed = "Speed",
                smartReplyPrompt = "Smart suggestions",
                aiAssistantName = "KOMDÉ Burkindi AI",
                creatorTitle = "Creator & Digital Visionary"
            )
            AppLanguage.FRENCH -> LocalizedStrings(
                slogan = "Communiquer • Partager • Connecter",
                chats = "Discussions",
                communities = "Communautés",
                status = "Statuts",
                contacts = "Contacts",
                profile = "Profil",
                dataSaver = "Économie de données",
                dataSaverSub = "Compression adaptative et faible consommation",
                searchPlaceholder = "Rechercher messages, contacts, groupes...",
                writeMessage = "Écrire un message...",
                online = "En ligne",
                offline = "Hors ligne",
                recordingVoice = "Enregistrement vocal...",
                voiceSpeed = "Vitesse",
                smartReplyPrompt = "Suggestions rapides",
                aiAssistantName = "KOMDÉ Burkindi IA",
                creatorTitle = "Concepteur du projet KOMDÉ"
            )
        }
    }
}
