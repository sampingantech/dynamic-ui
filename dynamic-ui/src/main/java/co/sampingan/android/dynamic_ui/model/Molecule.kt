package co.sampingan.android.dynamic_ui.model

/**
 * Created by ilgaputra15
 * on Wednesday, 13/05/2020 23.39
 * Mobile Engineer - https://github.com/ilgaputra15
 **/

data class DropDownValue(
    val label: String,
    val value: String? = null,
    var isSelected: Boolean = false,
    var tempValue: String? = null,
    val sectionTargetRef: String? = null, // null means default to use `nextSectionRef`, otherwise: section_<idx|eof>
)

data class RadioValue(
    val label: String,
    val value: String? = null,
    var tempValue: String? = null,
    val sectionTargetRef: String? = null, // null means default to use `nextSectionRef`, otherwise: section_<idx|eof>
)