package com.example.presentation

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import com.example.domain.dashboard.DashboardItem
import com.example.domain.dashboard.DashboardRepository
import com.example.domain.dashboard.DashboardResult
import com.example.presentation.dashboard.DownloadWorker
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DownloadWorkerTest {

    private lateinit var worker: DownloadWorker
    private lateinit var repository: FakeDashboardRepository
    private lateinit var mapper: FakeMapper

    @Before
    fun setUp() {
        repository = FakeDashboardRepository()
        mapper = FakeMapper()
        worker = TestListenableWorkerBuilder<DownloadWorker>(
            context = ApplicationProvider.getApplicationContext(),
        ).setWorkerFactory(TestWorkerFactory()).build()
    }

    inner class TestWorkerFactory : WorkerFactory() {

        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return DownloadWorker(
                context = appContext,
                parameters = workerParameters,
                repository = repository,
                mapper = mapper
            )
        }
    }

    @Test
    fun testDownloadWorkerSuccess() = runBlocking {
        repository.returnSuccess()

        val result = worker.doWork()
        mapper.checkMappedSuccess(
            listOf(
                DashboardItem.Base("A", "B", 1.457),
                DashboardItem.Base("C", "D", 2.1132),
            )
        )
        assertThat(result, `is`(ListenableWorker.Result.success()))
    }

    @Test
    fun testDownloadWorkerEmpty() = runBlocking {
        repository.returnEmpty()

        val result = worker.doWork()
        mapper.checkMappedEmptyCalledCount(1)
        assertThat(result, `is`(ListenableWorker.Result.success()))
    }

    @Test
    fun testDownloadWorkerError() = runBlocking {
        repository.returnError()

        val result = worker.doWork()
        mapper.checkMappedError("No internet connection")
        assertThat(result, `is`(ListenableWorker.Result.success()))
    }
}

private class FakeDashboardRepository : DashboardRepository {

    private var dashboardResult: DashboardResult = DashboardResult.Empty

    override suspend fun dashboards(): DashboardResult {
        return dashboardResult
    }

    fun returnSuccess() {
        dashboardResult = DashboardResult.Success(
            listOfItems = listOf(
                DashboardItem.Base("A", "B", 1.457),
                DashboardItem.Base("C", "D", 2.1132),
            )
        )
    }

    fun returnEmpty() {
        dashboardResult = DashboardResult.Empty
    }

    fun returnError() {
        dashboardResult = DashboardResult.Error(message = "No internet connection")
    }

    override suspend fun removePair(from: String, to: String): DashboardResult =
        throw IllegalStateException("No need to use in this test")
}

private class FakeMapper : DashboardResult.Mapper {

    private var actualList: List<DashboardItem> = listOf()

    override fun mapSuccess(listOfItems: List<DashboardItem>) {
        actualList = listOfItems
    }

    fun checkMappedSuccess(expectedList: List<DashboardItem>) {
        assertEquals(expectedList, actualList)
    }

    private var actualMessage = ""

    override fun mapError(message: String) {
        actualMessage = message
    }

    fun checkMappedError(expectedMessage: String) {
        assertEquals(expectedMessage, actualMessage)
    }

    private var mapEmptyCalledCount = 0

    override fun mapEmpty() {
        mapEmptyCalledCount++
    }

    fun checkMappedEmptyCalledCount(expectedCalledCount: Int) {
        assertEquals(expectedCalledCount, mapEmptyCalledCount)
    }
}