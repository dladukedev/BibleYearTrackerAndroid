package com.dladukedev.core.statistics

import com.dladukedev.common.models.BibleBook
import com.dladukedev.common.models.Reading
import com.dladukedev.common.models.ReadingPlan
import com.dladukedev.common.models.ReadingPlanKey
import com.dladukedev.common.util.extensions.splitAt
import com.dladukedev.data.plan.PlanRepository
import com.dladukedev.data.progress.ProgressOffsetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

interface SubscribeBibleBooksReadState {
    val values: Flow<List<BibleBookReadState>>
}

@Singleton
internal class SubscribeBibleBooksReadStateUseCase @Inject constructor(
    progressOffsetRepository: ProgressOffsetRepository,
    planRepository: PlanRepository,
) : SubscribeBibleBooksReadState {
    override val values = combine(
        planRepository.selectedPlan,
        progressOffsetRepository.lastReadOffset,
    ) { plan, offset ->
        getBookState(plan, offset)
    }

    private fun getBookState(plan: ReadingPlan, lastReadOffset: Int?): List<BibleBookReadState> {
        val splitIndex = lastReadOffset ?: -1

        val (read, unread) = plan.readingSets.splitAt(splitIndex)

        val unreadBooks = unread.flatten().map { it.book }.filter { removeBooksReadTwiceInPlan(it, plan, splitIndex) }.distinct()
        val readBooks = read.flatten().map { it.book }.distinct()

        return BibleBook.entries.map { book ->
            val status = getReadStatus(book, readBooks, unreadBooks)
            BibleBookReadState(book, status)
        }
    }

    private fun getReadStatus(
        book: BibleBook,
        readBooks: List<BibleBook>,
        unreadBooks: List<BibleBook>
    ): ReadStatus {
        val isUnread = unreadBooks.contains(book)
        val isRead = readBooks.contains(book)

        return when {
            isUnread && isRead -> ReadStatus.IN_PROGRESS
            isRead -> ReadStatus.READ
            else -> ReadStatus.UNREAD
        }

    }

    private fun removeBooksReadTwiceInPlan(book: BibleBook, plan: ReadingPlan, lastReadOffset: Int): Boolean {
        return when(plan.key) {
            ReadingPlanKey.BibleInAYear -> {
                when(book) {
                    BibleBook.MATTHEW -> lastReadOffset < 58
                    BibleBook.MARK -> lastReadOffset < 91
                    BibleBook.LUKE -> lastReadOffset < 139
                    BibleBook.JOHN -> lastReadOffset < 181
                    else -> true
                }
            }
        }
    }
}

data class BibleBookReadState(
    val book: BibleBook,
    val status: ReadStatus,
)

enum class ReadStatus {
    UNREAD, READ, IN_PROGRESS,
}