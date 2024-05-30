import SwiftUI

@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            ZStack {
                Color.white.ignoresSafeArea(.all) // status bar color
                 ContentView().onOpenURL { url in
                     handleDeepLink(url)
                 }
            }.preferredColorScheme(.light)
        }
    }

     func handleDeepLink(_ url: URL) {
         print("handleDeepLink: \(url)")
         // Handle the deep link URL
         if url.scheme == "okcredit", url.host == "web" {
             if let urlString = url.queryParameters?["url"],
                 let dynamicURL = URL(string: urlString) {
                 // Instantiate the WebViewController and set the dynamic URL
                 let webViewController = WebViewController()
                 webViewController.url = dynamicURL

                 // Present the webViewController
                 if let rootViewController = UIApplication.shared.windows.first?.rootViewController {
                     rootViewController.present(webViewController, animated: true, completion: nil)
                 }
             }
         }
      }
 }

extension URL {
 var queryParameters: [String: String]? {
     guard let components = URLComponents(url: self, resolvingAgainstBaseURL: true),
           let queryItems = components.queryItems else {
         return nil
     }
     var parameters = [String: String]()
     for item in queryItems {
         parameters[item.name] = item.value
     }
     return parameters
 }
}
