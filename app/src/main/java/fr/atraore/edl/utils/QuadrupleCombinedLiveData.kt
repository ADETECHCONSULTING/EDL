package fr.atraore.edl.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class QuadrupleCombinedLiveData<F, S, T, U>(
    firstLiveData: LiveData<F>,
    secondLiveData: LiveData<S>,
    thirdLiveData: LiveData<T>,
    quadLiveData: LiveData<U>
) : MediatorLiveData<Pair<Pair<F?, S?>, Pair<T?, U?>>>() {
    init {
        addSource(firstLiveData) { firstLiveDataValue: F -> value = Pair(Pair(firstLiveDataValue, secondLiveData.value), Pair(thirdLiveData.value, quadLiveData.value)) }
        addSource(secondLiveData) { secondLiveDataValue: S -> value = Pair(Pair(firstLiveData.value, secondLiveDataValue), Pair(thirdLiveData.value, quadLiveData.value)) }
        addSource(thirdLiveData) { thirdLiveDataValue: T -> value = Pair(Pair(firstLiveData.value, secondLiveData.value), Pair(thirdLiveDataValue, quadLiveData.value)) }
        addSource(quadLiveData) { quadLiveDataValue: U -> value = Pair(Pair(firstLiveData.value, secondLiveData.value), Pair(thirdLiveData.value, quadLiveDataValue)) }
    }
}