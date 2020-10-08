/*
 * Copyright (c) 2020.
 * PT. Sampingan Mitra Indonesia
 */

package com.sampingan.agentapp.dynamic_ui.model

import com.sampingan.agentapp.dynamic_ui.rule.AnswerSchemaRule
import com.sampingan.agentapp.dynamic_ui.rule.JsonSchemaRule
import com.sampingan.agentapp.dynamic_ui.rule.UiSchemaRule

/**
 * Created by ilgaputra15
 * on Sunday, 22/03/2020 20.39
 * Mobile Engineer - https://github.com/ilgaputra15
 **/
data class SectionView(
    val name: String,
    val dynamicViews: ArrayList<DynamicView>,
    val nextSectionRef: String,
    var enumSectionTargetRef: String? = null,
)

data class DynamicView(
    val componentName: String,
    val jsonSchema: JsonSchemaRule,
    val uiSchemaRule: UiSchemaRule,
    val answerSchemaRule: AnswerSchemaRule?,
    val isRequired: Boolean,
    var isValidated: Boolean = true,
    var value: Any? = null,
    var fileName: String? = null,
    var preview: Any? = null,
    var isError: Boolean = false,
    var errorValue: String? = null,
    var isEnable: Boolean = true,
    var enumSectionTargetRef: String? = null,
)