//
//  NoteDetailViewModel.swift
//  iosApp
//
//  Created by Shubham Tomar on 08/02/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

extension NoteDetailScreen{
    class NoteDetailViewModel : ObservableObject{
        private var noteRepository : NoteRepository.Companion?
        private var noteId:Int64? = nil
        

        
        @Published var noteTitle = ""
        @Published var noteContent = ""
        @Published private(set) var noteColor = Note.Companion().generateRandomColor()
        
        init(noteRepository: NoteRepository.Companion? = nil) {
            self.noteRepository = noteRepository
        }
        
        func loadNoteIfExists(id:Int64?){
            if id != nil {
                self.noteId = id
                noteRepository?.getNoteById(id: id!, completionHandler: { note, error in
                    
                    self.noteTitle = note?.title ?? ""
                    self.noteContent = note?.content ?? ""
                    self.noteColor = note?.colorHex ?? Note.Companion().generateRandomColor()
                })
            }
        }
        
        func saveNote(onSaved:@escaping () -> Void){
            noteRepository?.insertNote(
                note: Note(
                    id: self.noteId == nil ? DateTimeUtil().toEpochMillis(dateTime: DateTimeUtil().now()) : self.noteId!,
                    title: self.noteTitle,
                    content: self.noteContent,
                    colorHex: self.noteColor,
                    created:DateTimeUtil().now()),
                completionHandler: {error in
                    onSaved()
                })
        }
        
        
        func setparamsAndLoadNote(noteRepository:NoteRepository.Companion, noteId:Int64?){
            self.noteRepository = noteRepository
            loadNoteIfExists(id: noteId)
        }
    }
}
