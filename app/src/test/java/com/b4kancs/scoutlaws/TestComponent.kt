package com.b4kancs.scoutlaws

import com.b4kancs.scoutlaws.data.RepositoryTest
import com.b4kancs.scoutlaws.services.NotificationSchedulerTest
import com.b4kancs.scoutlaws.views.details.DetailsActivityViewModelTest
import com.b4kancs.scoutlaws.views.quiz.multiplechoice.MultipleChoiceSharedViewModelTest
import com.b4kancs.scoutlaws.views.quiz.picker.PickerSharedViewModelTest
import com.b4kancs.scoutlaws.views.quiz.sorter.SorterSharedViewModelTest
import com.b4kancs.scoutlaws.views.quiz.sorter.SorterViewModelTest
import com.b4kancs.scoutlaws.views.start.StartActivityViewModelTest
import dagger.Component
import javax.inject.Singleton

/**
 * Created by hszilard on 08-May-18.
 */
@Singleton
@Component(modules = [TestModule::class])
interface TestComponent : AppComponent {
    fun inject(test: StartActivityViewModelTest)
    fun inject(test: RepositoryTest)
    fun inject(test: PickerSharedViewModelTest)
    fun inject(test: MultipleChoiceSharedViewModelTest)
    fun inject(test: DetailsActivityViewModelTest)
    fun inject(test: NotificationSchedulerTest)
    fun inject(test: SorterViewModelTest)
    fun inject(test: SorterSharedViewModelTest)

}
