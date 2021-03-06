package samplemvvm.presentation.house

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import samplemvvm.data.entities.response.HouseResponse
import samplemvvm.usecase.GetHouseUseCase

interface HouseViewModel {

    val output: Output
    val input: Input

    interface Output {
        val houseList: Observable<List<HouseResponse.House>>
    }

    interface Input {
        val loadData: Observer<Unit>
    }
}

class HouseViewModelImpl(
        private val getHouseUseCase: GetHouseUseCase
) : HouseViewModel.Input, HouseViewModel.Output, HouseViewModel {

    override val output: HouseViewModel.Output = this
    override val input: HouseViewModel.Input = this

    // output
    override val houseList: Observable<List<HouseResponse.House>>

    // input
    override val loadData: PublishSubject<Unit> = PublishSubject.create()

    init {

        houseList = loadData
                .observeOn(Schedulers.io())
                .flatMap { getHouseUseCase.getListHouse() }
                .map { it.data!! }
    }
}