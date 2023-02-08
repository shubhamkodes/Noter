//
//  NoteListViewModel.swift
//  iosApp
//
//  Created by Shubham Tomar on 08/02/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

extension NoteListScreen{
    
    @MainActor class NoteListViewModel:ObservableObject {
        private var noteRepository : NoteRepository.Companion?  = nil
        
        
        private let searchNotes = SearchNotes()
        private var job : Closeable?
        
        private var notes = [Note]()
        @Published private(set) var filteredNotes = [Note]()
        @Published var searchText = "" {
            didSet{
                self.filteredNotes = searchNotes.execute(notes: self.notes, query: searchText)
            }
        }
        
        @Published private(set) var isSearchActive = false
        
        init(noteRepository: NoteRepository.Companion? = nil) {
            self.noteRepository = noteRepository
        }
        
        func setNoteRepository(noteRepository:NoteRepository.Companion){
            self.noteRepository = noteRepository
            
            job = listen()
        }
        
         
        func listen() -> Closeable? {
            return noteRepository?.getAllNotes().watch { list in
                self.notes = list as? [Note] ?? [Note]()
                self.filteredNotes = list as? [Note] ?? [Note]()
                NSLog("Items is \(list ?? NSArray())")
            }
        }
        
        func deleteNoteById(id:Int64?){
            if id != nil {
                noteRepository?.deleteNoteById(id: id!, completionHandler: { error in
                    
                })
            }
        }
        
        func stopObserving(){
            job?.close()
        }
        
        func toggleIsSearchActive(){
            isSearchActive = !isSearchActive
            if !isSearchActive {
                searchText = ""
            }
        }
        
        
    }
    
  
    
}
