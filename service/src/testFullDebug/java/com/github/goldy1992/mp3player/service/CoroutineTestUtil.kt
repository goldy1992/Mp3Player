package com.github.goldy1992.mp3player.service

import org.mockito.internal.invocation.InterceptedInvocation
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.OngoingStubbing
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.startCoroutineUninterceptedOrReturn

object CoroutineTestUtil {

    infix fun <T> OngoingStubbing<T>.doAnswer(answer: (InvocationOnMock) -> T?): OngoingStubbing<T> {
        return thenAnswer(answer)
    }

    infix fun <T> OngoingStubbing<T>.doAnswerForCoroutine(answer: suspend (InvocationOnMock) -> T?): OngoingStubbing<T> {
        return thenAnswer {
            //all suspend functions/lambdas has Continuation as the last argument.
            //InvocationOnMock does not see last argument
            val rawInvocation = it as InterceptedInvocation
            val continuation = rawInvocation.rawArguments.last() as Continuation<T?>

            // https://youtrack.jetbrains.com/issue/KT-33766#focus=Comments-27-3707299.0-0
            answer.startCoroutineUninterceptedOrReturn(it, continuation)
        }
    }
}