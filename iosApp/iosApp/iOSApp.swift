import SwiftUI
import shared

@main
struct iOSApp: App {
	var body: some Scene {
		WindowGroup {
            NavigationView{
                NoteListScreen(noteRepository: NoteRepository.Companion())
            }
            .accentColor(.black)
		}
	}
}
