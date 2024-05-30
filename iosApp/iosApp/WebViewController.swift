import Foundation
import UIKit
import WebKit

class WebViewController: UIViewController, WKUIDelegate, WKScriptMessageHandler {
    
    var webView: WKWebView!
    var url: URL?
        
    override func loadView() {
        let webConfiguration = WKWebViewConfiguration()
        webConfiguration.userContentController.add(self, name: "Android")
        webConfiguration.defaultWebpagePreferences.allowsContentJavaScript = true
        webView = WKWebView(frame: .zero, configuration: webConfiguration)
        webView.uiDelegate = self
        view = webView
    }
    
    
    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        print(message)
        if let body = message.body as? [String: Any] {
            print(body)
        }
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        
        if let url = url {
            print(url)
            let request = URLRequest(url: url)
            webView.load(request)
        }
    }
}
