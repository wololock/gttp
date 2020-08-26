import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer

def args = getProperty("args") as String[]

def port = args.length > 0 ? args[0].toInteger() : 8080
def baseDir = new File(args.length > 1 ? args[1] : ".").canonicalFile

def server = HttpServer.create(new InetSocketAddress(port), 0)
server.createContext("/") {exchange ->
    def uri = exchange.requestURI
    def file = new File(baseDir, uri.path).canonicalFile

    if (!file.path.startsWith(baseDir.path)) {
        sendResponse(exchange, 403, "403 (Forbidden)\n")

    } else if (file.directory) {
        String base = file == baseDir ? '': uri.path
        String listing = file.list()
            .collect { "<li><a href=\"${base}/${it}\">${it}</a></li>" }
            .join("\n")
        sendResponse(exchange, 200, "<html><body><h1>${uri.path}</h1><hr><ul>${listing}</ul></body></html>")

    } else if (!file.file) {
        sendResponse(exchange, 404, "404 (Not Found)\n")

    } else {
        sendResponse(exchange, 200, new FileInputStream(file))
    }
}

println "Listening at http://localhost:${port}/"
server.start()

static void sendResponse(HttpExchange ex, int code, InputStream ins) {
    ex.sendResponseHeaders(code, 0)
    ex.responseBody << ins
    ex.responseBody.flush()
    ex.responseBody.close()
}

static void sendResponse(HttpExchange ex, int code, byte[] answer) {
    ex.sendResponseHeaders(code, answer.length)
    ex.responseBody.write(answer)
    ex.responseBody.close()
}

static void sendResponse(HttpExchange ex, int code, String answer) {
    sendResponse(ex, code, answer.bytes)
}
