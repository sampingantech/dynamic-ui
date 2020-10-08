/*
 * Copyright (c) 2020.
 * PT. Sampingan Mitra Indonesia
 */

@file:JvmMultifileClass
@file:JvmName("ViewUtils")

package co.sampingan.android.dynamic_ui.rule

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

/**
 * Created by mochadwi on 3/13/20.
 * Copyright (c) 2020 sampingan.co.id. All rights reserved.
 */
fun provideJsonSchema(keyName: String, jsonRule: JsonObject, requiredField: String) =
    JsonSchemaRule(
        title = jsonRule["title"]?.asString!! + requiredField,
        type = jsonRule["type"]?.asString!!,
        default = jsonRule["default"]?.asString,
        minimum = jsonRule["minimum"]?.asInt,
        maximum = jsonRule["maximum"]?.asInt,
        items = jsonRule["items"]?.asJsonObject,
        // TODO(mochadwi): 3/13/20 remove `enumDropdown`, json schema mozilla doesn't have that
        //  backend still using different perspective
        oneOf = jsonRule["oneOf"]?.asJsonArray,
        description = jsonRule["description"]?.asString
    )

fun provideUiSchema(uiRule: JsonObject) =
    UiSchemaRule(
        order = uiRule["ui:order"]?.asInt,
        uiTitle = uiRule["ui:title"]?.asString,
        uiWidget = uiRule["ui:widget"]?.asString,
        uiHelp = uiRule["ui:help"]?.asString,
        uiPlaceholder = uiRule["ui:placeholder"]?.asString,
        uiDescription = uiRule["ui:description"]?.asString,
        uiOptions = uiRule["ui:options"]?.asJsonObject
    )

fun provideAnswerSchema(
    assessmentStatus: String? = null,
    answerRule: JsonElement?,
    answerStatusRule: JsonObject?
): AnswerSchemaRule =
    AnswerSchemaRule(
        assessmentStatus = assessmentStatus,
        answer = answerRule?.handled,
        status = answerStatusRule?.get("status")?.asString,
        rejectionReason = answerStatusRule?.get("rejectionReason")?.asString
    )

fun provideJsonSchema(jsonRule: JsonObject) =
    JsonSchemaRule(
        title = jsonRule["title"]?.asString.orEmpty(),
        default = jsonRule["default"]?.asString.orEmpty(),
        type = jsonRule["type"]?.asString.orEmpty(),
        minimum = jsonRule["minimum"]?.asInt,
        maximum = jsonRule["maximum"]?.asInt,
        items = jsonRule["items"]?.asJsonObject,
        enum = jsonRule["enum"]?.asJsonArray.transform(),
        enumNames = jsonRule["enumNames"]?.asJsonArray.transformNonNull(),
        enumSectionTargetRef = jsonRule["enumSectionTargetRef"]?.asJsonArray.transform(),
        description = jsonRule["description"]?.asString
    )

private fun JsonArray?.transform() =
    this?.map { if (it.isJsonNull) null else it.asString }?.toList()

private fun JsonArray?.transformNonNull() =
    this?.map { it.asString }?.toList()

fun provideQuestionSchema(jsonRule: JsonObject) =
    QuestionSchema(
        keyName = "keyName",
        title = jsonRule["title"]?.asString.orEmpty(),
        type = jsonRule["type"]?.asString.orEmpty(),
        properties = jsonRule["properties"]?.asJsonObject!!
    )

fun provideAnswerSchema(answerRule: JsonElement?, answerStatusRule: JsonObject?): AnswerSchemaRule =
    AnswerSchemaRule(
        answer = answerRule?.handled,
        status = answerStatusRule?.get("status")?.asString,
        rejectionReason = answerStatusRule?.get("rejectionReason")?.asString
    )

// default value in this class means, optional, otherwise required
data class QuestionSchema(
    val keyName: String,
    val type: String,
    val title: String,
    val properties: JsonObject
)

data class JsonSchemaRule(
    val title: String, // as question/label text
    val type: String, // as data `type` holder, not ui:widget type
    val default: String? = "", // as `default` value, overridden by `ui:placeholder` if exist
    val description: String? = "", // as `description` above field
    val minimum: Int? = 1, // min counts
    val maximum: Int? = 1, // max counts
    val minLength: Int? = 1, // min char length
    val maxLength: Int? = 1, // max char length
    val items: JsonObject? = JsonObject(), // as data holder for: checkboxes or multiple files
    val enum: List<String?>? = null, // as answer enums/list: select (dropdown), radio
    /**
     * If the [JsonArray] exist for [enumNames]
     * The label wouldn't never has `null` String, so List<String>? used instead of List<String?>?
     * as label for enums/list: select (dropdown), radio
     */
    val enumNames: List<String>? = null,
    val enumSectionTargetRef: List<String?>? = null, // as logic jump section target, e.g: section_<idx|eof>
    @Deprecated("Will be replaced by `enumNames` & `enum`")
    val oneOf: JsonArray? = JsonArray() // for multiple radio button
)

data class UiSchemaRule(
    val order: Int? = 0,
    val uiTitle: String? = null, // `ui:title` replace `title` json schema
    val uiWidget: String? = null, // `ui:widget` to define an android view/widget type
    val uiHelp: String? = null, // `ui:help` as caption/error text
    val uiPlaceholder: String? = null, // replace `default` json schema, exist only in: select
    val uiDescription: String? = null, // replace `description` json schema
    val uiHelpImage: String? = null,
    val uiOptions: JsonObject? = JsonObject(), // custom option/rule
)

fun UiSchemaRule.toTitle(default: JsonSchemaRule) =
    this.uiTitle ?: default.title

fun UiSchemaRule.toDescription(default: JsonSchemaRule) =
    this.uiDescription ?: default.description

data class AnswerSchemaRule(
    val assessmentStatus: String? = null,
    val answer: JsonElement? = null, // answer data holder for each field
    val status: String? = null, // to check each answer field status
    val rejectionReason: String? = null, // replacing field hint as error message
)

data class ReapplyDate(val untilDate: String, val remainingDays: String)

val JsonElement?.handled: JsonElement?
    get() = when {
        this?.isJsonNull == true || this?.isJsonObject == true -> JsonObject()
        this?.isJsonArray == true -> this.asJsonArray
        else -> this
    }