package com.example.rapicarmen.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*


abstract class FirestoreAdapter<VH : ViewHolder?> : RecyclerView.Adapter<VH>(), EventListener<QuerySnapshot?> {

    private val TAG = "Firestore Adapter"

    private var mQuery: Query? = null
    private var mRegistration: ListenerRegistration? = null

    private val mSnapshots = ArrayList<DocumentSnapshot>()

    fun FirestoreAdapter(query: Query?) { mQuery = query }

    override fun onEvent(documentSnapshots: QuerySnapshot?, e: FirebaseFirestoreException?) {

        // Handle errors
        if (e != null) {
            //Log.w(TAG, "onEvent:error", e)
            return
        }

        // Dispatch the event
        for (change in documentSnapshots!!.documentChanges) {
            // Snapshot of the changed document
            //val snapshot: DocumentSnapshot = change.document
            when (change.type) {
                DocumentChange.Type.ADDED-> {
                    onDocumentAdded(change)
                }
                DocumentChange.Type.MODIFIED -> {
                    onDocumentModified(change)
                }
                DocumentChange.Type.REMOVED -> {
                    onDocumentRemoved(change)
                }
            }
        }
    }

    open fun startListening() {
        if (mQuery != null /* && mRegistration == null*/) {
            mRegistration = mQuery!!.addSnapshotListener(this)
        }
    }


    open fun stopListening() {
        if (mRegistration != null) {
            mRegistration!!.remove()
            mRegistration = null
        }
        mSnapshots.clear()
        notifyDataSetChanged()
    }

    open fun setQuery(query: Query?) {
        // Stop listening
        stopListening()

        // Clear existing data
        mSnapshots.clear()
        notifyDataSetChanged()

        // Listen to new query
        mQuery = query
        startListening()
    }


    override fun getItemCount(): Int {
        return (mSnapshots.size)
    }

    protected open fun getSnapshot(index: Int): DocumentSnapshot? { return mSnapshots[index] }

    protected open fun onError(e: FirebaseFirestoreException?) {}

    protected open fun onDataChanged() {}


    protected open fun onDocumentAdded(change: DocumentChange) {
        mSnapshots.add(change.newIndex, change.document)
        notifyItemInserted(change.newIndex)
    }

    protected open fun onDocumentModified(change: DocumentChange) {
        if (change.oldIndex == change.newIndex) {
            // Item changed but remained in same position
            mSnapshots[change.oldIndex] = change.document
            notifyItemChanged(change.oldIndex)
        } else {
            // Item changed and changed position
            mSnapshots.removeAt(change.oldIndex)
            mSnapshots.add(change.newIndex, change.document)
            notifyItemMoved(change.oldIndex, change.newIndex)
        }
    }

    protected open fun onDocumentRemoved(change: DocumentChange) {
        mSnapshots.removeAt(change.oldIndex)
        notifyItemRemoved(change.oldIndex)
    }

}