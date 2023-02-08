//
//  NoteListScreen.swift
//  iosApp
//
//  Created by Shubham Tomar on 08/02/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct NoteListScreen: View {
    
    private var noteRepository :NoteRepository.Companion
    @StateObject var viewModel = NoteListViewModel(noteRepository: nil)
    
    @State private  var isNoteSelected = false
    @State private var selectedNotedId:Int64? = nil
    
    init(noteRepository: NoteRepository.Companion) {
        self.noteRepository = noteRepository
    }
    
    var body: some View {
        VStack{
            ZStack{
                NavigationLink( destination: NoteDetailScreen(noteRepository: self.noteRepository, noteId: self.selectedNotedId),isActive: $isNoteSelected){
                    EmptyView()
                }.hidden()
                
                HideableSearchTextField<NoteDetailScreen>(onSearchToggle: {
                    viewModel.toggleIsSearchActive()
                }, destinationProvider: {
                    NoteDetailScreen(
                        noteRepository: self.noteRepository,
                        noteId: nil
                    )
                }, isSearchActive: viewModel.isSearchActive,
                   searchText: $viewModel.searchText)
                .frame(maxWidth : .infinity,minHeight: 40)
                .padding()
                
                if !viewModel.isSearchActive{
                    Text("All Notes")
                        .font(.title2)
                }
            }
            
            List{
                ForEach(viewModel.filteredNotes,id: \.self.id){ note in
                    Button(action: {
                        isNoteSelected = true
                        selectedNotedId = note.id
                    }){
                        NoteItem(note: note, onDeleteClick: {
                            viewModel.deleteNoteById(id: note.id)
                        })
                    }
                }
            }.listStyle(.plain)
                .listRowSeparator(.hidden)
            
        }.onAppear{
            viewModel.setNoteRepository(noteRepository: noteRepository)
        }.onDisappear{
            viewModel.stopObserving()
        }
    }
}


