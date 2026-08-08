package com.example.lxquiz.data.repository

import com.example.lxquiz.data.models.Country
import com.example.lxquiz.data.models.EducationLevel
import com.example.lxquiz.data.models.EducationSection
import com.example.lxquiz.data.models.SubLevel
import com.example.lxquiz.data.models.Subject
import java.util.Locale

class CurriculumRepository {

    private val countryOverrides = mapOf(
        "CM" to "🇨🇲", "US" to "🇺🇸", "NG" to "🇳🇬", "GH" to "🇬🇭", 
        "ZA" to "🇿🇦", "GB" to "🇬🇧", "CA" to "🇨🇦", "FR" to "🇫🇷",
        "DE" to "🇩🇪", "IN" to "🇮🇳", "BR" to "🇧🇷", "JP" to "🇯🇵",
        "CN" to "🇨🇳", "RU" to "🇷🇺", "AU" to "🇦🇺", "KE" to "🇰🇪"
    )

    fun getCountries(): List<Country> {
        val countries = mutableListOf<Country>()
        val locales = Locale.getAvailableLocales()
        val seenIsoCodes = mutableSetOf<String>()

        for (locale in locales) {
            val countryCode = locale.country
            if (countryCode.length == 2 && seenIsoCodes.add(countryCode)) {
                val name = Locale.Builder().setRegion(countryCode).build().displayCountry
                if (name.isNotEmpty()) {
                    val emoji = countryOverrides[countryCode] ?: getFlagEmoji(countryCode)
                    countries.add(Country(countryCode.lowercase(), name, emoji))
                }
            }
        }
        return countries.sortedBy { it.name }
    }

    private fun getFlagEmoji(countryCode: String): String {
        if (countryCode.length != 2) return "🏳️"
        val firstChar = Character.codePointAt(countryCode.uppercase(), 0) - 0x41 + 0x1F1E6
        val secondChar = Character.codePointAt(countryCode.uppercase(), 1) - 0x41 + 0x1F1E6
        return String(Character.toChars(firstChar)) + String(Character.toChars(secondChar))
    }

    fun getSubLevels(countryId: String, level: EducationLevel): List<SubLevel> {
        val cid = countryId.uppercase()
        return when (level) {
            EducationLevel.PRIMARY -> getPrimaryLevels(cid)
            EducationLevel.SECONDARY -> getSecondaryLevels(cid)
            EducationLevel.UNIVERSITY -> (1..5).map { SubLevel("y$it", "Year $it") }
        }
    }

    private fun getPrimaryLevels(cid: String): List<SubLevel> {
        return when (cid) {
            "FR", "CM" -> listOf(
                SubLevel("sil", "SIL / CP"), SubLevel("cp", "CP / CE1"),
                SubLevel("ce1", "CE1 / CE2"), SubLevel("ce2", "CE2 / CM1"),
                SubLevel("cm1", "CM1 / CM2"), SubLevel("cm2", "CM2")
            )
            else -> (1..6).map { SubLevel("p$it", "Grade $it") }
        }
    }

    private fun getSecondaryLevels(cid: String): List<SubLevel> {
        val scienceArtsComm = listOf(
            EducationSection("sci", "Sciences"),
            EducationSection("art", "Arts & Humanities"),
            EducationSection("com", "Commercial / Business"),
            EducationSection("tech", "Technical / Vocational")
        )

        return when (cid) {
            "CM" -> listOf(
                SubLevel("f1", "Form 1"), SubLevel("f2", "Form 2"),
                SubLevel("f3", "Form 3"), SubLevel("f4", "Form 4", scienceArtsComm),
                SubLevel("f5", "Form 5", scienceArtsComm), SubLevel("l6", "Lower Sixth", scienceArtsComm),
                SubLevel("u6", "Upper Sixth", scienceArtsComm)
            )
            "NG" -> listOf(
                SubLevel("ss1", "Senior Secondary 1", scienceArtsComm),
                SubLevel("ss2", "Senior Secondary 2", scienceArtsComm),
                SubLevel("ss3", "Senior Secondary 3", scienceArtsComm)
            )
            "IN" -> listOf(
                SubLevel("c9", "Class 9"), SubLevel("c10", "Class 10"),
                SubLevel("c11", "Class 11", listOf(
                    EducationSection("sci_m", "Science (Medical)"),
                    EducationSection("sci_nm", "Science (Non-Medical)"),
                    EducationSection("comm", "Commerce"),
                    EducationSection("hum", "Humanities")
                )),
                SubLevel("c12", "Class 12", listOf(
                    EducationSection("sci_m", "Science (Medical)"),
                    EducationSection("sci_nm", "Science (Non-Medical)"),
                    EducationSection("comm", "Commerce"),
                    EducationSection("hum", "Humanities")
                ))
            )
            "KE" -> listOf(
                SubLevel("g7", "Grade 7 (JSS)"), SubLevel("g8", "Grade 8 (JSS)"),
                SubLevel("g9", "Grade 9 (JSS)"),
                SubLevel("g10", "Grade 10 (Senior)", listOf(
                    EducationSection("stem", "STEM"),
                    EducationSection("soc", "Social Sciences"),
                    EducationSection("arts", "Arts & Sports")
                )),
                SubLevel("g11", "Grade 11 (Senior)", listOf(
                    EducationSection("stem", "STEM"),
                    EducationSection("soc", "Social Sciences"),
                    EducationSection("arts", "Arts & Sports")
                )),
                SubLevel("g12", "Grade 12 (Senior)", listOf(
                    EducationSection("stem", "STEM"),
                    EducationSection("soc", "Social Sciences"),
                    EducationSection("arts", "Arts & Sports")
                ))
            )
            "GB" -> (7..13).map { 
                SubLevel("y$it", "Year $it", if (it >= 12) scienceArtsComm else emptyList()) 
            }
            "US", "CA" -> (9..12).map { SubLevel("g$it", "Grade $it") }
            else -> (1..6).map { SubLevel("s$it", "Senior $it") }
        }
    }

    fun getSections(countryId: String, level: EducationLevel, subLevelId: String): List<EducationSection> {
        val cid = countryId.uppercase()
        val scienceArtsComm = listOf(
            EducationSection("sci", "Sciences"),
            EducationSection("art", "Arts & Humanities"),
            EducationSection("com", "Commercial / Business"),
            EducationSection("tech", "Technical / Vocational")
        )

        return when (level) {
            EducationLevel.SECONDARY -> {
                when (cid) {
                    "CM" -> scienceArtsComm
                    "NG" -> scienceArtsComm
                    "IN" -> listOf(
                        EducationSection("sci_m", "Science (Medical)"),
                        EducationSection("sci_nm", "Science (Non-Medical)"),
                        EducationSection("comm", "Commerce"),
                        EducationSection("hum", "Humanities")
                    )
                    "KE" -> listOf(
                        EducationSection("stem", "STEM"),
                        EducationSection("soc", "Social Sciences"),
                        EducationSection("arts", "Arts & Sports")
                    )
                    else -> scienceArtsComm
                }
            }
            else -> emptyList()
        }
    }

    fun getSubjects(countryId: String, level: EducationLevel, subLevelId: String, sectionId: String?): List<Subject> {
        val cid = countryId.uppercase()
        
        // Base subjects for all
        val coreSubjects = mutableListOf(
            Subject("math", "Mathematics", "calculate", "Core Mathematics", "Sciences"),
            Subject("eng", "English Language", "book", "Grammar and Literature", "Arts & Humanities")
        )

        return when (level) {
            EducationLevel.PRIMARY -> listOf(
                Subject("math_p", "Mathematics", "numbers", "Basic arithmetic"),
                Subject("eng_p", "English", "book", "Reading and writing"),
                Subject("sci_p", "Basic Science", "science", "Nature and environment"),
                Subject("soc_p", "Social Studies", "public", "Community and culture")
            )
            EducationLevel.SECONDARY -> {
                val subjects = coreSubjects.toMutableList()
                
                // Add country specific subjects
                when (cid) {
            "NG", "GH" -> {
                subjects.add(Subject("civ", "Civic Education", "gavel", "Citizenship", "Arts & Humanities"))
                when (sectionId) {
                    "sci" -> subjects.addAll(listOf(
                        Subject("phy", "Physics", "bolt", "Mechanics and Energy", "Sciences"),
                        Subject("che", "Chemistry", "biotech", "Matter and reactions", "Sciences"),
                        Subject("bio", "Biology", "eco", "Living organisms", "Sciences")
                    ))
                    "art" -> subjects.addAll(listOf(
                        Subject("lit", "Literature-in-English", "menu_book", "Prose and Drama", "Arts & Humanities"),
                        Subject("gov", "Government", "account_balance", "Political systems", "Arts & Humanities"),
                        Subject("crs", "CRS / IRS", "church", "Religious studies", "Arts & Humanities")
                    ))
                    "com" -> subjects.addAll(listOf(
                        Subject("acc", "Financial Accounting", "payments", "Ledgers and accounts", "Commercial"),
                        Subject("com_s", "Commerce", "store", "Trade and business", "Commercial"),
                        Subject("eco", "Economics", "trending_up", "Market principles", "Commercial")
                    ))
                    "tech" -> subjects.addAll(listOf(
                        Subject("draw", "Technical Drawing", "architecture", "", "Technical"),
                        Subject("elec", "Basic Electronics", "electrical_services", "", "Technical"),
                        Subject("cons", "Building Construction", "construction", "", "Technical")
                    ))
                }
            }
                    "CM" -> {
                        when (sectionId) {
                            "sci" -> subjects.addAll(listOf(
                                Subject("phy_cm", "Physics", "bolt", "", "Sciences"),
                                Subject("che_cm", "Chemistry", "biotech", "", "Sciences"),
                                Subject("bio_cm", "Biology", "eco", "", "Sciences"),
                                Subject("fmath", "Further Math", "functions", "", "Sciences")
                            ))
                            "art" -> subjects.addAll(listOf(
                                Subject("his", "History", "history", "", "Arts & Humanities"),
                                Subject("geo", "Geography", "map", "", "Arts & Humanities"),
                                Subject("phi", "Philosophy", "psychology", "", "Arts & Humanities"),
                                Subject("lit_cm", "Literature", "menu_book", "", "Arts & Humanities")
                            ))
                            "com" -> subjects.addAll(listOf(
                                Subject("acc_cm", "Accounting", "payments", "", "Commercial"),
                                Subject("eco_cm", "Economics", "trending_up", "", "Commercial")
                            ))
                            "tech" -> subjects.addAll(listOf(
                                Subject("eng_drawing", "Engineering Drawing", "architecture", "", "Technical"),
                                Subject("workshop", "Workshop Technology", "build", "", "Technical")
                            ))
                        }
                    }
                    "IN" -> {
                        if (subLevelId in listOf("c9", "c10")) {
                            subjects.addAll(listOf(
                                Subject("sci_in", "Science", "science", "Physics, Chem, Bio", "Sciences"),
                                Subject("sst", "Social Science", "public", "Hist, Geo, Civics", "Arts & Humanities")
                            ))
                        } else {
                            when (sectionId) {
                                "sci_m" -> subjects.addAll(listOf(Subject("phy", "Physics", "bolt", field = "Sciences"), Subject("che", "Chemistry", "biotech", field = "Sciences"), Subject("bio", "Biology", "eco", field = "Sciences")))
                                "sci_nm" -> subjects.addAll(listOf(Subject("phy", "Physics", "bolt", field = "Sciences"), Subject("che", "Chemistry", "biotech", field = "Sciences"), Subject("math_a", "Advanced Math", "functions", field = "Sciences")))
                                "comm" -> subjects.addAll(listOf(Subject("acc", "Accountancy", "payments", field = "Commercial"), Subject("bst", "Business Studies", "business", field = "Commercial"), Subject("eco", "Economics", "trending_up", field = "Commercial")))
                                "hum" -> subjects.addAll(listOf(Subject("his", "History", "history", field = "Arts & Humanities"), Subject("pol", "Pol. Science", "account_balance", field = "Arts & Humanities"), Subject("psy", "Psychology", "psychology", field = "Arts & Humanities")))
                            }
                        }
                    }
                }
                
                // Generic fallback if list is still just core
                if (subjects.size <= 2) {
                    subjects.addAll(listOf(
                        Subject("sci_g", "General Science", "science", "", "Sciences"),
                        Subject("soc_g", "Social Studies", "public", "", "Arts & Humanities"),
                        Subject("ict", "Information Tech", "computer", "", "Technical")
                    ))
                }
                
                subjects.distinctBy { it.id }
            }
            EducationLevel.UNIVERSITY -> listOf(
                Subject("cs_101", "Computer Science", "computer", "Programming basics"),
                Subject("law_101", "Law", "gavel", "Introduction to legal systems"),
                Subject("med_101", "Medicine", "medical_services", "Anatomy and Physiology"),
                Subject("eng_101", "Engineering", "engineering", "Calculus and Design")
            )
        }
    }
}
