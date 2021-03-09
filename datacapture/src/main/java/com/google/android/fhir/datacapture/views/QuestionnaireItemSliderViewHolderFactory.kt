/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.fhir.datacapture.views

import android.view.View
import android.widget.TextView
import com.google.android.fhir.datacapture.R
import com.google.android.material.slider.Slider
import com.google.fhir.r4.core.Integer
import com.google.fhir.r4.core.QuestionnaireResponse

internal object QuestionnaireItemSliderViewHolderFactory : QuestionnaireItemViewHolderFactory(
    R.layout.questionnaire_item_slider
) {
    override fun getQuestionnaireItemViewHolderDelegate(): QuestionnaireItemViewHolderDelegate =
        object : QuestionnaireItemViewHolderDelegate {
            private lateinit var sliderHeader: TextView
            private lateinit var slider: Slider
            private lateinit var questionnaireItemViewItem: QuestionnaireItemViewItem

            override fun init(itemView: View) {
                sliderHeader = itemView.findViewById(R.id.slider_header)
                slider = itemView.findViewById(R.id.slider)
            }

            override fun bind(questionnaireItemViewItem: QuestionnaireItemViewItem) {
                this.questionnaireItemViewItem = questionnaireItemViewItem
                val questionnaireItem = questionnaireItemViewItem.questionnaireItem
                val answer = questionnaireItemViewItem.singleAnswerOrNull
                sliderHeader.text = questionnaireItem.text.value
                slider.valueFrom = 0.0F
                slider.valueTo = 100.0F
                slider.stepSize = 10.0F
                val sliderValue = answer?.value?.integer?.value?.toString() ?: "0.0"
                slider.value = sliderValue.toFloat()

                slider.addOnChangeListener { _, newValue, _ ->
                    // Responds to when slider's value is changed
                    QuestionnaireResponse.Item.Answer.newBuilder()
                        .apply {
                            value = QuestionnaireResponse.Item.Answer.ValueX.newBuilder()
                                .setInteger(Integer.newBuilder().setValue(newValue.toInt())
                                    .build()).build()

                            questionnaireItemViewItem.singleAnswerOrNull = this
                        }
                    questionnaireItemViewItem.questionnaireResponseItemChangedCallback()
                }
            }
        }
}
