package com.orukunnn.shapesnapapp.data.repository.preset

import com.google.firebase.firestore.DocumentSnapshot
import com.orukunnn.shapesnapapp.data.model.preset.Preset

interface PresetsRepository {

    companion object {
        /** [loadPresetsPage] の 1 ページあたり件数（Firestore の limit と一致） */
        const val PAGE_SIZE: Long = 4L
    }

    /**
     * プリセット一覧の 1 ページを取得。[lastDocument] に前ページ末尾のスナップショットを渡すと次ページ。
     */
    suspend fun loadPresetsPage(lastDocument: DocumentSnapshot? = null): Pair<List<Preset>, DocumentSnapshot?>

    suspend fun getPostedPresetsOf(userId: String): List<Preset>
}
