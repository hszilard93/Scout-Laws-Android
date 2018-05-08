package com.b4kancs.scoutlaws

import com.b4kancs.scoutlaws.data.RepositoryTest
import com.b4kancs.scoutlaws.views.quiz.AbstractSharedViewModelTest
import com.b4kancs.scoutlaws.views.start.StartActivityViewModelTest
import dagger.Component
import javax.inject.Singleton

/**
 * Created by hszilard on 08-May-18.
 */
@Singleton
@Component(modules = [(TestModule::class)])
interface TestComponent : ApplicationComponent {
    fun inject(test: StartActivityViewModelTest)
    fun inject(test: RepositoryTest)
    fun inject(test: AbstractSharedViewModelTest)
}