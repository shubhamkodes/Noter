//
//  NoteDetailScreen.swift
//  iosApp
//
//  Created by Shubham Tomar on 08/02/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct NoteDetailScreen: View {
    
    private var noteRepository : NoteRepository.Companion
    private var noteId : Int64? = nil
    
    @StateObject var viewModel = NoteDetailViewModel(noteRepository: nil)
    @Environment(\.presentationMode) var presentation
    
    init(noteRepository: NoteRepository.Companion, noteId: Int64? = nil) {
        self.noteRepository = noteRepository
        self.noteId = noteId
    }
    
    
    var body: some View {
        VStack{
            TextField("Enter a title...", text: $viewModel.noteTitle)
                .font(.title)
            
            TextField("Enter some content...", text: $viewModel.noteContent)
      
                
            
            Spacer()
        }.toolbar(content: {
            Button(action: {
                viewModel.saveNote {
                    self.presentation.wrappedValue.dismiss()
                }
            }){
                Image(systemName: "checkmark")
            }
        }).padding()
            .background(Color(hex: viewModel.noteColor))
            .onAppear{
                viewModel.setparamsAndLoadNote(noteRepository: noteRepository, noteId: noteId)
            }
    }
}

 
